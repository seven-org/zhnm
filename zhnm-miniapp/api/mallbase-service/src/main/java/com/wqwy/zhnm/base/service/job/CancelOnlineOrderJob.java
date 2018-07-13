package com.wqwy.zhnm.base.service.job;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.wqwy.zhnm.base.component.constant.DefaultConstants;
import com.wqwy.zhnm.base.component.exception.BusinessException;
import com.wqwy.zhnm.base.component.utils.ConvertUtils;
import com.wqwy.zhnm.base.entity.SellerGoods;
import com.wqwy.zhnm.base.entity.ShopOrder;
import com.wqwy.zhnm.base.entity.ShopOrderDetail;
import com.wqwy.zhnm.base.service.SellerGoodsService;
import com.wqwy.zhnm.base.service.ShopOrderDetailService;
import com.wqwy.zhnm.base.service.ShopOrderService;
import com.wqwy.zhnm.base.service.base.WechatPayPerformance;

public class CancelOnlineOrderJob implements Job {
	
	private static final Logger logger = LoggerFactory.getLogger(CancelOnlineOrderJob.class);

	@Autowired
	private ShopOrderService shopOrderService;
	
	@Autowired
	private ShopOrderDetailService shopOrderDetailService;
	
	@Autowired
	private SellerGoodsService sellerGoodsService;
	
	@Autowired
	private WechatPayPerformance wechatPayPerformance;
	
	/**
	 * TODO 事务
	 * 1,2可归于一个业务的事务
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataLoadMap = context.getJobDetail().getJobDataMap();
		String shopOrderId = dataLoadMap.getString(DefaultConstants.JOB_DETAIL_ONLINE_SHOPORDER_ID);
		logger.info("execute online order: " + shopOrderId + " cancel operation. ");
		/*
		 * 1.将订单改为超时取消状态
		 * 2.将商户订单商品改为超时取消状态(已有商户抢的订单商品)
		 * 3.用户退款
		 */
		/*
		 * 1
		 */
		ShopOrder so = new ShopOrder();
		so.setOrderId(shopOrderId);
		so = shopOrderService.get(shopOrderId);
		
		/*
		 * 仅有待接单的订单可以被取消
		 * (可能存在订单在改变状态后没来得及删除定时任务,任务被触发)
		 * TODO 考虑锁表
		 */
		if (DefaultConstants.ShopOrderEnum.SHOPORDER_PAYED_NEED_ACCEPT_ORDER.getShopOrderEnum().equals(so.getStatus()))
			return;
		
		so.setStatus(DefaultConstants.ShopOrderEnum.SHOPORDER_AUTOMATIC_CANCEL.getShopOrderEnum());
		shopOrderService.updateForJob(so);
		
		/*
		 * 2.
		 * 	2.1将订单商品
		 * 	2.2将所有订单商品中有商户抢的商品的库存退回商户
		 */
		ShopOrderDetail sod = new ShopOrderDetail();
		sod.setOrderId(shopOrderId);
		List<ShopOrderDetail> sodList = shopOrderDetailService.findList(sod);
		
		sod.setStatus(DefaultConstants.ShopOrderEnum.SHOPORDER_AUTOMATIC_CANCEL.getShopOrderEnum());
		if (sodList != null && !sodList.isEmpty())
			shopOrderDetailService.update(sod);
		
		sodList = sodList.stream().filter(s -> s.getSellerId()!=null).collect(Collectors.toList());//TODO s.getSellerId()!=null 可能需要修改
		sodList.forEach(s -> {
			SellerGoods sg = new SellerGoods();
			sg.setSellerId(s.getSellerId());
			sg.setGoodsId(s.getGoodsId());
			sg = sellerGoodsService.findList(sg).get(0);//一个商户只能添加一个同种商品
			sg.setCurrentStock(sg.getCurrentStock().add(s.getGoodsCount()));
			sellerGoodsService.update(sg);
		});
		
		/*
		 * 3
		 * 	用户微信退款
		 * note: out_trade_no为线上订单的订单编号(ShopOrder.orderId) {@link com.weixin.util.Package#getPackage}
		 */
		logger.info("do refund in job");
		BigDecimal orderTotal = so.getOrderTotal();
		orderTotal = new BigDecimal(0.01); // TODO 暂时使用1分钱用来测试
		try {
			wechatPayPerformance.doRefund(shopOrderId, ConvertUtils.DoubleMoneyUnitYuanToLongMoneyUnitFen(orderTotal).toString());
		} catch (BusinessException e) {
			/*
			 * TODO
			 * 微信退款失败,记录需要被记录
			 */
		}
	}

}
