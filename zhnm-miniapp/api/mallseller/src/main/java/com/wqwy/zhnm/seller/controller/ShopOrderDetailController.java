/**
 * Copyright (c) 2015-2018 <a href="">wqwy</a> All rights reserved.
 */
package com.wqwy.zhnm.seller.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.wqwy.zhnm.base.component.component.JsonResponse;
import com.wqwy.zhnm.base.component.component.PageJsonResponse;
import com.wqwy.zhnm.base.component.component.Pagenation;
import com.wqwy.zhnm.base.component.dto.ShopOrderDTO;
import com.wqwy.zhnm.base.component.exception.BusinessException;
import com.wqwy.zhnm.base.component.request.SellerPreemptRequest;
import com.wqwy.zhnm.base.component.utils.ResultUtils;
import com.wqwy.zhnm.base.entity.ShopOrderDetail;
import com.wqwy.zhnm.base.service.ShopOrderDetailService;

/**
 * createTime: 2018-05-15 11:51:43
 * @author seven
 * @version
 */
@RestController
@RequestMapping("/v1/")
public class ShopOrderDetailController {

	@Autowired
	private ShopOrderDetailService  shopOrderDetailService;

	/**
	 * 
	 * @date 2018-05-15 11:51:43
	 * @Title: findByPage 
	 * @Description: TODO
	 * @param @param shopOrderDetail
	 * @param @param pagenation
	 * @param @param request
	 * @param @return
	 * @return PageJsonResponse<List<ShopOrderDetail>>
	 * @throws
	 */
	@RequestMapping(value="shopOrderDetails", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE+";charset=utf-8"})
	public PageJsonResponse<List<ShopOrderDetail>> findByPage(ShopOrderDetail shopOrderDetail, Pagenation pagenation, HttpServletRequest request, Model model) {
		Page<ShopOrderDetail> queryResultList = shopOrderDetailService.findListByPage(shopOrderDetail, pagenation);
		pagenation.setTotal(queryResultList.getTotal());
		return new PageJsonResponse<List<ShopOrderDetail>>(ResultUtils.SUCCESS, ResultUtils.SUCCESS_MSG, queryResultList, pagenation);
	}


	/**
	 * 
	 * @date 2018-05-15 11:51:43
	 * @Title: findShopOrderDetailDetail 
	 * @Description: TODO
	 * @param @param shopOrderDetail
	 * @param @param pagenation
	 * @param @param request
	 * @param @return
	 * @return JsonResponse<ShopOrderDetail>
	 * @throws
	 */
	@RequestMapping(value="shopOrderDetails/{id}", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE+";charset=utf-8"})
	public JsonResponse<ShopOrderDetail> findShopOrderDetailDetail(@PathVariable("id")Integer id, ShopOrderDetail shopOrderDetail, HttpServletRequest request){
		shopOrderDetail = shopOrderDetailService.get(id.toString());
		return new JsonResponse<ShopOrderDetail>(ResultUtils.SUCCESS, ResultUtils.SUCCESS_MSG, shopOrderDetail);
	}

	
	/**
	 * 
	 * @date 2018-05-15 11:51:43
	 * @Title: modifyShopOrderDetailDetail 
	 * @Description: TODO
	 * @param @param id
	 * @param @param shopOrderDetail
	 * @param @param request
	 * @param @return
	 * @return JsonResponse<ShopOrderDetail>
	 * @throws
	 */
	@RequestMapping(value="shopOrderDetails/{id}", method=RequestMethod.PATCH, produces={MediaType.APPLICATION_JSON_VALUE+";charset=utf-8"})
	public JsonResponse<ShopOrderDetail> modifyShopOrderDetailDetail(@PathVariable("id")String id, @RequestBody ShopOrderDetail shopOrderDetail, HttpServletRequest request){
		shopOrderDetail.setOrderDetailId(id);
		shopOrderDetailService.update(shopOrderDetail);
		return new JsonResponse<ShopOrderDetail>(ResultUtils.SUCCESS, ResultUtils.SUCCESS_MSG, shopOrderDetailService.get(id.toString()));
	}
	
	/**
	 * 
	 * @Title: modifyMultipleShopOrderDetailDetail  
	 * @Description: 商户抢单  
	 * @date 23 May 2018 2:20:20 PM  
	 * @param @param sellerPreemptRequest
	 * @param @param request
	 * @param @return  
	 * @return JsonResponse<ShopOrderDetail>  
	 * @throws
	 */
	@RequestMapping(value="shopOrderDetails", method=RequestMethod.PATCH, produces={MediaType.APPLICATION_JSON_VALUE+";charset=utf-8"})
	public JsonResponse<ShopOrderDTO> modifyMultipleShopOrderDetailForPreempt(@RequestBody SellerPreemptRequest sellerPreemptRequest, HttpServletRequest request){
		/**
		 * 前端要求code必须200?
		 * message成功
		 * 报错message放入data? :(
		 */
		ShopOrderDTO shopOrderDTO = new ShopOrderDTO();
		try {
			shopOrderDTO = shopOrderDetailService.updateMultipleForPreempt(sellerPreemptRequest);
		} catch (BusinessException e) {
			shopOrderDTO = (ShopOrderDTO) e.getData();
			shopOrderDTO.setErrorMessage(e.getMessage());
			return new JsonResponse<ShopOrderDTO>(ResultUtils.SUCCESS, ResultUtils.SUCCESS_MSG, shopOrderDTO);
		}
		return new JsonResponse<ShopOrderDTO>(ResultUtils.SUCCESS, ResultUtils.SUCCESS_MSG, shopOrderDTO);
	}
	
	/**
	 * 
	 * @Title: modifyMultipleShopOrderDetailForPrepared  
	 * @Description: 商户在某个订单中备货完成  
	 * @date 23 May 2018 6:04:04 PM  
	 * @param @param sellerId
	 * @param @param shopOrderId
	 * @param @param shopOrderDetail
	 * @param @param request
	 * @param @return  
	 * @return JsonResponse<Integer>  
	 * @throws
	 */
	@RequestMapping(value="shopOrderDetails/seller/{sellerId}/shopOrder/{shopOrderId}", method=RequestMethod.PATCH, produces={MediaType.APPLICATION_JSON_VALUE+";charset=utf-8"})
	public JsonResponse<Integer> modifyMultipleShopOrderDetailForPrepared(@PathVariable("sellerId")Integer sellerId, @PathVariable("shopOrderId")String shopOrderId, ShopOrderDetail shopOrderDetail, HttpServletRequest request){
		shopOrderDetail.setSellerId(sellerId);
		shopOrderDetail.setOrderId(shopOrderId);
		return new JsonResponse<Integer>(ResultUtils.SUCCESS, ResultUtils.SUCCESS_MSG, shopOrderDetailService.updateMultipleForPrepared(shopOrderDetail));
	}
	
	/**
	 * 
	 * @date 2018-05-15 11:51:43
	 * @Title: addShopOrderDetail 
	 * @Description: TODO
	 * @param @param shopOrderDetail
	 * @param @param request
	 * @param @return
	 * @return JsonResponse<ShopOrderDetail>
	 * @throws
	 */
	@RequestMapping(value="shopOrderDetails", method=RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE+";charset=utf-8"})
	public JsonResponse<ShopOrderDetail> addShopOrderDetail(@RequestBody ShopOrderDetail shopOrderDetail, HttpServletRequest request){
		shopOrderDetailService.insert(shopOrderDetail);
		return new JsonResponse<ShopOrderDetail>(ResultUtils.SUCCESS, ResultUtils.SUCCESS_MSG, shopOrderDetailService.findList(shopOrderDetail).get(0));
	}
	
	/**
	 * 
	 * @date 2018-05-15 11:51:43
	 * @Title: removeShopOrderDetail 
	 * @Description: TODO
	 * @param @param id
	 * @param @param request
	 * @param @return
	 * @return JsonResponse<>
	 * @throws
	 */
	@RequestMapping(value="shopOrderDetails/{id}", method=RequestMethod.DELETE, produces={MediaType.APPLICATION_JSON_VALUE+";charset=utf-8"})
	public JsonResponse<?> removeShopOrderDetail(@PathVariable("id")Integer id, HttpServletRequest request){
		shopOrderDetailService.delete(id.toString());
		return new JsonResponse<>(ResultUtils.SUCCESS, ResultUtils.SUCCESS_MSG);
	}
	
}
