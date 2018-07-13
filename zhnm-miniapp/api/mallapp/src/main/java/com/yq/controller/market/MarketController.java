package com.yq.controller.market;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wqwy.zhnm.base.component.response.IndexMarketResponse;
import com.wqwy.zhnm.base.service.MarketService;

@RestController
public class MarketController {
	
	private static final Logger logger = LoggerFactory.getLogger(MarketController.class);
	
	@Autowired
	private MarketService marketService;

	@RequestMapping(value = "/markets")
	public IndexMarketResponse findMarketList(String location, IndexMarketResponse imr, HttpServletRequest request, Model model) {
		imr.setMarketList(marketService.findListMarketByLocation(location));
		return imr;
	}
	
}
