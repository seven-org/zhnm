package com.wqwy.zhnm.base.component.utils;

public class MQResultUtils {
	
	public static final Integer SELLER_BALANCE_LOGIN = 30200;
	public static final String SELLER_BALANCE_LOGIN_MSG = "商户扫码登录称";
	
	public static final Integer MQ_PREEMPT_SHOP_ORDER_GOODS = 30300;
	public static final String MQ_PREEMPT_SHOP_ORDER_GOODS_MSG = "可抢商品列表";
	
	public static final Integer MQ_PREPARE_SHOP_ORDER_GOODS = 30301;
	public static final String MQ_PREPARE_SHOP_ORDER_GOODS_MSG = "备货商品列表";

	public static final Integer MQ_DELIVERY_SHOP_ORDER = 30305;
	public static final String MQ_DELIVERY_SHOP_ORDER_MSG = "待配送订单";
	
	public static final Integer MQ_OFFLINE_ORDER_NOTIFY_RESULT_TO_BALANCE = 30309;
	public static final String MQ_OFFLINE_ORDER_NOTIFY_RESULT_TO_BALANCE_MSG = "支付回调消息";
	
}
