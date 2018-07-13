package com.wqwy.zhnm.base.component.component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceOfflineOrderGoodsComponent {

	private String goodsId;
	
	private String goodsCount;
	
	private String goodsSort;
	
	private String goodsPrice;

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}

	public String getGoodsSort() {
		return goodsSort;
	}

	public void setGoodsSort(String goodsSort) {
		this.goodsSort = goodsSort;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	@Override
	public String toString() {
		return "BalanceOfflineOrderGoodsComponent [goodsId=" + goodsId + ", goodsCount=" + goodsCount + ", goodsSort="
				+ goodsSort + ", goodsPrice=" + goodsPrice + "]";
	}

}
