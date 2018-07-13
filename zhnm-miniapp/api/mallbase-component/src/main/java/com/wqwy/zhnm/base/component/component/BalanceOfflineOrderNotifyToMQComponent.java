package com.wqwy.zhnm.base.component.component;

/**
 * 
 * @ClassName: BalanceOfflineOrderNotifyToMQComponent  
 * @author seven  
 * @date 9 Jun 2018 10:15:57 AM  
 *
 */
public class BalanceOfflineOrderNotifyToMQComponent {
	
	private Integer balanceId;
	
	private Integer orderId;
	
	private String url;

	public Integer getBalanceId() {
		return balanceId;
	}

	public void setBalanceId(Integer balanceId) {
		this.balanceId = balanceId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
