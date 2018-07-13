/**
 * Copyright (c) 2015-2018 <a href="">wqwy</a> All rights reserved.
 */package com.wqwy.zhnm.base.service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wqwy.zhnm.base.component.component.BalanceOfflineOrderGoodsComponent;
import com.wqwy.zhnm.base.component.component.Pagenation;
import com.wqwy.zhnm.base.component.constant.DefaultConstants;
import com.wqwy.zhnm.base.component.constant.DefaultConstants.BalanceOfflineOrderEnum;
import com.wqwy.zhnm.base.component.constant.DefaultConstants.ThirdPartPayNotifyEnum;
import com.wqwy.zhnm.base.component.dto.BalanceOfflineOrderDTO;
import com.wqwy.zhnm.base.component.dto.BalanceOfflineOrderGoodsDTO;
import com.wqwy.zhnm.base.component.exception.BusinessException;
import com.wqwy.zhnm.base.component.request.BalanceOfflineOrderWithGoodsRequest;
import com.wqwy.zhnm.base.component.response.QRCodeGenerateResponseToClient;
import com.wqwy.zhnm.base.component.response.WechatQRCodeGenerateResponse;
import com.wqwy.zhnm.base.component.response.WechatQRCodeGenerateResponseToClient;
import com.wqwy.zhnm.base.component.utils.ConvertUtils;
import com.wqwy.zhnm.base.component.utils.OrderUtils;
import com.wqwy.zhnm.base.component.utils.ResultUtils;
import com.wqwy.zhnm.base.dao.BalanceOfflineOrderGoodsMapper;
import com.wqwy.zhnm.base.dao.BalanceOfflineOrderMapper;
import com.wqwy.zhnm.base.dao.SellerDynamicMapper;
import com.wqwy.zhnm.base.dao.SellerGoodsMapper;
import com.wqwy.zhnm.base.dao.ShopGoodsMapper;
import com.wqwy.zhnm.base.dao.ThirdPartNotifyMapper;
import com.wqwy.zhnm.base.entity.BalanceOfflineOrder;
import com.wqwy.zhnm.base.entity.SellerDynamic;
import com.wqwy.zhnm.base.entity.SellerGoods;
import com.wqwy.zhnm.base.entity.ShopGoods;
import com.wqwy.zhnm.base.entity.ThirdPartNotify;
import com.wqwy.zhnm.base.service.base.WechatPayPerformance;

/**
 * createTime: 2018-05-08 18:51:13
 * @author seven
 * @version
 */
@Service
public class BalanceOfflineOrderServiceImpl implements BalanceOfflineOrderService {

	private static final Logger logger = LoggerFactory.getLogger(BalanceOfflineOrderServiceImpl.class);
	
    @Autowired
    private BalanceOfflineOrderMapper balanceOfflineOrderMapper;
    
    @Autowired
    private SellerDynamicMapper sellerDynamicMapper;
    
    @Autowired
    private ShopGoodsMapper shopGoodsMapper;
    
    @Autowired
    private BalanceOfflineOrderGoodsMapper balanceOfflineOrderGoodsMapper;
    
    @Autowired
    private SellerGoodsMapper sellerGoodsMapper;
    
    @Autowired(required = false)
    private WechatPayPerformance wechatPayPerformance;
    
    @Autowired
    private ThirdPartNotifyMapper thirdPartNotifyMapper;

    @Override
    public BalanceOfflineOrder get(String id) {
        return balanceOfflineOrderMapper.get(id);
    }

    @Override
    public List<BalanceOfflineOrder> findList(BalanceOfflineOrder balanceOfflineOrder) {
        return balanceOfflineOrderMapper.findList(balanceOfflineOrder);
    }

    @Override
    public Page<BalanceOfflineOrderDTO> findListByPage(BalanceOfflineOrderDTO balanceOfflineOrder, Pagenation pagenation) {
        PageHelper.startPage(pagenation.getPageNum(), pagenation.getPageSize());
        return balanceOfflineOrderMapper.findListByPage(balanceOfflineOrder);
    }

    @Override
    public QRCodeGenerateResponseToClient insert(BalanceOfflineOrderWithGoodsRequest balanceOfflineOrderWithGoodsRequest) {
    	logger.info("balanceOfflineOrderWithGoodsRequest: " + balanceOfflineOrderWithGoodsRequest);
    	/*
    	 * 1.新增线下订单商品数据
    	 * 2.新增线下订单
    	 * 3.生成qrCode返回
    	 */
    	String orderNumber = OrderUtils.createOrderNum();
    	BalanceOfflineOrderDTO balanceOfflineOrder = new BalanceOfflineOrderDTO();
    	balanceOfflineOrder.setSellerId(Integer.valueOf(balanceOfflineOrderWithGoodsRequest.getSellerId()));
    	balanceOfflineOrder.setBalanceId(Integer.valueOf(balanceOfflineOrderWithGoodsRequest.getBalanceId()));
    	balanceOfflineOrder.setPayWay(Integer.valueOf(balanceOfflineOrderWithGoodsRequest.getPayWay()));
    	
    	balanceOfflineOrder.setOrderNumber(orderNumber);
    	List<BalanceOfflineOrderGoodsComponent> boogcList = balanceOfflineOrderWithGoodsRequest.getBoogcList();
    	BigDecimal orderPayTotal = BigDecimal.ZERO;
    	List<BalanceOfflineOrderGoodsDTO> boogList = new ArrayList<BalanceOfflineOrderGoodsDTO>();
//    	boogcList.forEach(boogc -> {
    	for (BalanceOfflineOrderGoodsComponent boogc : boogcList) {
    		logger.info("BalanceOfflineOrderGoodsComponent: " + boogc);
    		BigDecimal goodsCount = new BigDecimal(boogc.getGoodsCount());
    		BalanceOfflineOrderGoodsDTO boog = new BalanceOfflineOrderGoodsDTO();
    		
    		BigDecimal goodsPayTotal = null;
    		//排除称上自定义的其他商品
    		if (boogc.getGoodsId()!=null) {
	    		ShopGoods sg = shopGoodsMapper.get(boogc.getGoodsId());
	    		BeanUtils.copyProperties(sg, boog);
	    		goodsPayTotal = boog.getGoodsPrice().multiply(goodsCount);
	    		//常规商品操作库存
	    		SellerGoods sGoods = new SellerGoods();
	    		sGoods.setSellerId(balanceOfflineOrder.getSellerId());
	    		sGoods.setGoodsId(Integer.valueOf(boogc.getGoodsId()));
	    		sGoods = sellerGoodsMapper.findList(sGoods).get(0);
	    		sGoods.setCurrentStock(sGoods.getCurrentStock().subtract(goodsCount));
	    		sellerGoodsMapper.update(sGoods);
    		} else {
    			boog.setGoodsPrice(new BigDecimal(boogc.getGoodsPrice()));
    			goodsPayTotal = new BigDecimal(boogc.getGoodsPrice()).multiply(goodsCount);
    		}
    		logger.info(goodsPayTotal.toString());
    		boog.setId(null);
    		boog.setOrderNumber(orderNumber);
    		boog.setSort(Integer.valueOf(boogc.getGoodsSort()));
    		boog.setGoodsCount(goodsCount);
//    		boog.getGoodsPrice().multiply(goodsCount);
    		boog.setPayTotal(goodsPayTotal);
    		balanceOfflineOrderGoodsMapper.insert(boog);
    		boogList.add(boog);
    		orderPayTotal = orderPayTotal.add(goodsPayTotal);
    	}
//    	});
//        balanceOfflineOrder.setCreateTime(new Date());
    	balanceOfflineOrder.setAddtime(new Date());
    	balanceOfflineOrder.setTotalPrice(orderPayTotal);
    	balanceOfflineOrder.setBoogList(boogList);//for QRCode
    	
    	//现金支付直接完成订单
//    	UnionPayQRCodeGenerateResponseToClient upqrcgrtc = new UnionPayQRCodeGenerateResponseToClient();
    	QRCodeGenerateResponseToClient qrcgrtc = new WechatQRCodeGenerateResponseToClient();
    	if (DefaultConstants.BalanceOfflineOrderEnum.PAY_WAY_CASH.getBalanceOfflineOrderEnum().equals(balanceOfflineOrder.getPayWay()))
    		balanceOfflineOrder.setStatus(DefaultConstants.BalanceOfflineOrderEnum.BALANCEOFFLINEORDER_PAYED_WAIT_EVALUATE.getBalanceOfflineOrderEnum());
    	else {
    		//其他支付方式生成二维码并返回client
    		balanceOfflineOrder.setStatus(DefaultConstants.BalanceOfflineOrderEnum.BALANCEOFFLINEORDER_NOT_PAY.getBalanceOfflineOrderEnum());
    		
    		if (DefaultConstants.BalanceOfflineOrderEnum.PAY_WAY_WECHATPAY.getBalanceOfflineOrderEnum().equals(balanceOfflineOrder.getPayWay())) {
    			try {
    				WechatQRCodeGenerateResponse wqrcgr = wechatPayPerformance.doUnifiedOrder(balanceOfflineOrder.getOrderNumber(), ConvertUtils.DoubleMoneyUnitYuanToLongMoneyUnitFen(balanceOfflineOrder.getTotalPrice()).toString());
    				qrcgrtc = ConvertUtils.OfflineOrderToWechatQRCodeGRToClient(wqrcgr);
    			} catch (BusinessException e) {
    				logger.error(e.getMessage());
    				throw new BusinessException(ResultUtils.WECHAT_PAY_GENERATE_WQ_CODE_FAIL, ResultUtils.WECHAT_PAY_GENERATE_WQ_CODE_FAIL_MSG);
    			}
    		} else // 暂时第三方支付仅支持wechat TODO
    			throw new BusinessException(ResultUtils.PAY_METHOD_NOT_SUPPORT, ResultUtils.PAY_METHOD_NOT_SUPPORT_MSG);//不支持的支付类型
    		
    		/*
    		 * 第三方QRCode支付需要放入notify中数据
    		 * 后期可考虑将wechatresponse放入nosql	TODO
    		 */
    		ThirdPartNotify tpn = new ThirdPartNotify();
    		tpn.setCreateTime(new Date());
    		tpn.setTradeNo(balanceOfflineOrder.getOrderNumber());
    		tpn.setNotifyType(DefaultConstants.BalanceOfflinePayMap.get(BalanceOfflineOrderEnum.PAY_WAY_WECHATPAY));
    		tpn.setOperationType(ThirdPartNotify.PayEnum.QR_PAY);
    		tpn.setNotifyStatus(ThirdPartPayNotifyEnum.STATUS_CREATED.getThirdPartPayNotifyEnum());
    		thirdPartNotifyMapper.insert(tpn);
    	}
    	balanceOfflineOrderMapper.insert(balanceOfflineOrder);
    	
    	//将新增的balanceOfflineOrder的id返回
    	BalanceOfflineOrder boo = new BalanceOfflineOrder();
    	boo.setOrderNumber(orderNumber);
    	qrcgrtc.setOrderId(balanceOfflineOrderMapper.findList(boo).get(0).getId());
        return qrcgrtc;
    }

    @Override
    public Integer update(BalanceOfflineOrder balanceOfflineOrder) {
    	/*
    	 * 用户评价订单
    	 * 1.记录线下订单的评分
    	 * 2.修改商户的评分
    	 * 	2.1修改商户的总评分 	-> evaluation_total += remark
    	 * 	2.2修改商户的评分次数 	-> evaluation_count += 1
    	 */
    	if (balanceOfflineOrder.getRemark() != null) {
    		SellerDynamic sd = new SellerDynamic();
    		sd.setSellerId(balanceOfflineOrder.getSellerId());
    		sd = sellerDynamicMapper.findList(sd).get(0);
    		sd.setEvaluationTotal(sd.getEvaluationTotal() + balanceOfflineOrder.getRemark());
    		sd.setEvaluationCount(sd.getEvaluationCount() + 1);
    		sellerDynamicMapper.update(sd);
    		balanceOfflineOrder.setStatus(DefaultConstants.BalanceOfflineOrderEnum.BALANCEOFFLINEORDER_EVALUATED.getBalanceOfflineOrderEnum());
    	}
//        balanceOfflineOrder.setUpdateTime(new Date());
        return balanceOfflineOrderMapper.update(balanceOfflineOrder);
    }

    @Override
    public Integer delete(String id) {
        return balanceOfflineOrderMapper.delete(id);
    }
}
