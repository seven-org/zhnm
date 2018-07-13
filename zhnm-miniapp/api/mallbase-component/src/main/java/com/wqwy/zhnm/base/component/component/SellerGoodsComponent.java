package com.wqwy.zhnm.base.component.component;

import com.wqwy.zhnm.base.entity.SellerGoods;
import com.wqwy.zhnm.base.entity.ShopGoods;

public class SellerGoodsComponent extends SellerGoods {

	private ShopGoods shopGoods;

	public ShopGoods getShopGoods() {
		return shopGoods;
	}

	public void setShopGoods(ShopGoods shopGoods) {
		this.shopGoods = shopGoods;
	}
	
}
