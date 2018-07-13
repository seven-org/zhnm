package com.wqwy.zhnm.base.service.base;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wqwy.zhnm.base.component.constant.DefaultConstants;
import com.wqwy.zhnm.base.component.dto.ShopOrderDTO;
import com.wqwy.zhnm.base.component.dto.ShopOrderDetailDTO;
import com.wqwy.zhnm.base.component.exception.BusinessException;
import com.wqwy.zhnm.base.component.request.GeoGetLocationByAddressStringRequest;
import com.wqwy.zhnm.base.component.utils.DateUtils;
import com.wqwy.zhnm.base.component.utils.GeoDeUtils;
import com.wqwy.zhnm.base.component.utils.ResultUtils;
import com.wqwy.zhnm.base.dao.ShopOrderMapper;
import com.wqwy.zhnm.base.entity.ShopOrderDetail;
import com.wqwy.zhnm.base.service.ShopOrderDetailService;
import com.wqwy.zhnm.base.service.ShopOrderService;
import com.wqwy.zhnm.base.service.async.RabbitMQAsyncJobService;
import com.wqwy.zhnm.base.service.job.CancelOnlineOrderJob;

@Component
public class AboutOrderService {
	
	private static final Logger logger = LoggerFactory.getLogger(AboutOrderService.class);

	@Autowired
	private QuartzManager quartzManager;
	
	@Autowired
	private ShopOrderService shopOrderService;
	
	@Autowired
	private ShopOrderDetailService shopOrderDetailService;
	
	@Autowired
	private RabbitMQAsyncJobService rabbitMQAsyncJobService;
	
	@Autowired
	private ShopOrderMapper shopOrderMapper;
	
	public static final String JOB_NAME = "job_orderCancel_";
	public static final String TRIGGER_NAME = "trigger_orderCancel_";
	public static final String JOB_GROUP_NAME = "job_order_orderCancel";
	public static final String TRIGGER_GROUP_NAME = "trigger_order_orderCancel";
	
	public static final long OrderTimeLimit = 60000;// 3*60*1000
	
	public static final BigDecimal DefaultDeliveryCost = new BigDecimal(5.0);// 配送费用默认值
	
	/**
	 * 
	 * @Title: doAfterUserCreateOnlineOrder  
	 * @Description: 用户创建线上订单操作完成后  
	 * @date 15 May 2018 4:57:47 PM  
	 * @param @param onlineOrderId  
	 * @return void  
	 * @throws
	 */
	/*
	 * 1.同步创建定时任务，在规定时间到达后取消线上订单
	 * 2.异步发送消息到商户MQ
	 */
	public void doAfterUserCreateOnlineOrder(String onlineOrderId) {
		logger.info("create onlineOrderId: " + onlineOrderId);
		
//		ShopOrder shopOrder = shopOrderService.get(onlineOrderId);
		ShopOrderDTO shopOrder = new ShopOrderDTO();
		shopOrder.setOrderId(onlineOrderId);
		List<ShopOrderDTO> shopOrderDTOList = shopOrderService.findShopOrders(shopOrder);
		if (shopOrderDTOList==null || shopOrderDTOList.isEmpty())
			throw new BusinessException();
		shopOrder = shopOrderDTOList.get(0);
		
		/*
		 * -2
		 * 
		 * 用户下单后直接添加 1.配送费用 2.配送到达时间段
		 */
		shopOrder.setDeliveryCost(DefaultDeliveryCost);
		shopOrder.setDeliveryTimeSlice(DateUtils.GetDeliveryTimeSlice());
//		shopOrder.setDeliveryFinishTime(deliveryFinishTime);
		
		/*
		 * -1
		 * 
		 * 用户 地理位置信息(location) 需要放入订单
		 */
		GeoGetLocationByAddressStringRequest gglbasRequest = new GeoGetLocationByAddressStringRequest();
//		gglbasRequest.setCity(shopOrder.getAddrCity().trim()); // Geode地图不能获取包含省的city信息
		gglbasRequest.setAddress(shopOrder.getAddrCity().trim() + shopOrder.getAddress().trim());
		String location = null;
		try {
			location = GeoDeUtils.getLocationByAddressByGeoMap(gglbasRequest).getLocation();
			logger.info("提交地理位置坐标: ");
			logger.info("city: " + gglbasRequest.getCity());
			logger.info("address: " + gglbasRequest.getAddress());
			logger.info("location: " + location);
		} catch (Exception e1) {
			throw new BusinessException();
		}
		shopOrder.setLocation(location);
		
		/*
		 * 0
		 * 
		 * 判断订单能否有可能完成
		 * (订单中有存在最近的一家市场的所有商户都没有的(包括没有库存的)商品)
		 */
		
		ShopOrderDetail shopOrderDetail = new ShopOrderDetail();
		shopOrderDetail.setOrderId(onlineOrderId);
		List<ShopOrderDetail> shopOrderDetailList = shopOrderDetailService.findList(shopOrderDetail);
		List<Integer> shopOrderDetailGoodsIdList = shopOrderDetailList.stream().map(ShopOrderDetail::getGoodsId).collect(Collectors.toList());
		
		Integer marketId = shopOrderService.getOneNearMarketByShopOrder(shopOrder);
		
		shopOrder.setMarketId(marketId);
		shopOrderMapper.update(shopOrder);
		
		/*
		 * 通过marketId 和shopOrderDetail 搜索出符合条件的商户
		 * 1.商户属于marketId对应的market
		 * 2.商户有shopOrderDetail.goodsId的商品 且 商品库存满足用户条件(sellerGoods.currentStock > shopOrderDetail.goodsCount)
		 */
		ShopOrderDetailDTO shopOrderDetailDTO = new ShopOrderDetailDTO();
		shopOrderDetailDTO.setMarketId(marketId);
		shopOrderDetailDTO.setOrderId(onlineOrderId);
		shopOrderDetailDTO.setPreSellerGoodsStatus(DefaultConstants.SellerGoodsEnum.ON_SALE.getSellerGoodsEnum());
		List<ShopOrderDetailDTO> shopOrderDetailDTOList = shopOrderDetailService.findListByCondition(shopOrderDetailDTO);
		List<Integer> shopOrderDetailDTOGoodsIdList = shopOrderDetailDTOList.stream().map(ShopOrderDetailDTO::getGoodsId).collect(Collectors.toList());
		if (!shopOrderDetailDTOGoodsIdList.containsAll(shopOrderDetailGoodsIdList))
			throw new BusinessException(ResultUtils.SHOP_ORDER_GOODS_STOCK_OVER, ResultUtils.SHOP_ORDER_GOODS_STOCK_OVER_MSG);
	}
	
	
	/**
	 * 
	 * @Title: doAfterUserPayedOnlineOrder  
	 * @Description: 用户支付线上订单操作完成后  
	 * @date 9 Jun 2018 5:03:26 PM  
	 * @param @param onlineOrderId  
	 * @return void  
	 * @throws
	 */
	public void doAfterUserPayedOnlineOrder(String onlineOrderId) {
		logger.info("payed onlineOrderId: " + onlineOrderId);
		
		/*
		 * 1
		 * 
		 * note:
		 * 1.定时任务创建失败，用户下单&生成订单 操作需要终止(订单自动取消依赖定时任务)
		 */
		try {
			logger.info("do create task for online order: " + onlineOrderId);
			//TODO
			Map<String, String> dataLoadMap = new HashMap<String, String>();
			dataLoadMap.put(DefaultConstants.JOB_DETAIL_ONLINE_SHOPORDER_ID, onlineOrderId);
			Date triggerTime = new Date(new Date().getTime() + OrderTimeLimit);
			String triggerTimeString = DateUtils.GetCron(triggerTime);
			quartzManager.addJob(JOB_NAME + onlineOrderId, JOB_GROUP_NAME, TRIGGER_NAME + onlineOrderId,
					TRIGGER_GROUP_NAME, CancelOnlineOrderJob.class, triggerTimeString, dataLoadMap);
		} catch (Exception e) {
			logger.error("do create task for online order " + onlineOrderId + " failed");
			logger.error(e.getMessage());
			throw new BusinessException();
		}
		
		ShopOrderDTO shopOrder = new ShopOrderDTO();
		shopOrder.setOrderId(onlineOrderId);
		List<ShopOrderDTO> shopOrderDTOList = shopOrderService.findShopOrders(shopOrder);
		if (shopOrderDTOList==null || shopOrderDTOList.isEmpty())
			throw new BusinessException();
		shopOrder = shopOrderDTOList.get(0);
		
		Integer marketId = shopOrderService.getOneNearMarketByShopOrder(shopOrder);
		
		ShopOrderDetailDTO shopOrderDetailDTO = new ShopOrderDetailDTO();
		shopOrderDetailDTO.setMarketId(marketId);
		shopOrderDetailDTO.setOrderId(onlineOrderId);
		shopOrderDetailDTO.setPreSellerGoodsStatus(DefaultConstants.SellerGoodsEnum.ON_SALE.getSellerGoodsEnum());
		List<ShopOrderDetailDTO> shopOrderDetailDTOList = shopOrderDetailService.findListByCondition(shopOrderDetailDTO);
		
		/*
		 * 2
		 * 
		 * note:
		 * 1.消息发送失败或者部分失败后，操作继续(即使订单没有通知任何商户，可以依赖定时任务取消订单)
		 */
		try {
			logger.info("do send message to MQ for online order: " + onlineOrderId);
			rabbitMQAsyncJobService.doSendOrderGoodsMessageToMQBalanceQueue(shopOrder, shopOrderDetailDTOList);
			rabbitMQAsyncJobService.doSendOrderGoodsMessageToMQSellerQueue(shopOrder, shopOrderDetailDTOList);
		} catch (Exception e) {
			logger.warn("do send message to MQ for online order " + onlineOrderId + " failed");
			logger.warn(e.getMessage());
		}
	}
	
}
