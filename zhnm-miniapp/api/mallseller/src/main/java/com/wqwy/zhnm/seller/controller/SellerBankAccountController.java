/**
 * Copyright (c) 2015-2018 <a href="">wqwy</a> All rights reserved.
 */
package com.wqwy.zhnm.seller.controller;

import com.github.pagehelper.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.wqwy.zhnm.base.entity.SellerBankAccount;
import com.wqwy.zhnm.base.service.SellerBankAccountService;
import com.wqwy.zhnm.seller.base.BaseController;
import com.wqwy.zhnm.base.component.component.JsonResponse;
import com.wqwy.zhnm.base.component.component.PageJsonResponse;
import com.wqwy.zhnm.base.component.component.Pagenation;
import com.wqwy.zhnm.base.component.utils.ResultUtils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * createTime: 2018-05-08 18:51:07
 * @author seven
 * @version
 */
@RestController
@RequestMapping("/v1/")
public class SellerBankAccountController extends BaseController {

	@Autowired
	private SellerBankAccountService  sellerBankAccountService;

	/**
	 * 
	 * @date 2018-05-08 18:51:07
	 * @Title: findByPage 
	 * @Description: TODO
	 * @param @param sellerBankAccount
	 * @param @param pagenation
	 * @param @param request
	 * @param @return
	 * @return PageJsonResponse<List<SellerBankAccount>>
	 * @throws
	 */
	@RequestMapping(value="sellerBankAccounts", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE+";charset=utf-8"})
	public PageJsonResponse<List<SellerBankAccount>> findByPage(SellerBankAccount sellerBankAccount, Pagenation pagenation, HttpServletRequest request, Model model) {
		Page<SellerBankAccount> queryResultList = sellerBankAccountService.findListByPage(sellerBankAccount, pagenation);
		pagenation.setTotal(queryResultList.getTotal());
		return new PageJsonResponse<List<SellerBankAccount>>(ResultUtils.SUCCESS, ResultUtils.SUCCESS_MSG, queryResultList, pagenation);
	}


	/**
	 * 
	 * @date 2018-05-08 18:51:07
	 * @Title: findSellerBankAccountDetail 
	 * @Description: TODO
	 * @param @param sellerBankAccount
	 * @param @param pagenation
	 * @param @param request
	 * @param @return
	 * @return JsonResponse<SellerBankAccount>
	 * @throws
	 */
	@RequestMapping(value="sellerBankAccounts/{id}", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE+";charset=utf-8"})
	public JsonResponse<SellerBankAccount> findSellerBankAccountDetail(@PathVariable("id")Integer id, SellerBankAccount sellerBankAccount, HttpServletRequest request){
		sellerBankAccount = sellerBankAccountService.get(id.toString());
		return new JsonResponse<SellerBankAccount>(ResultUtils.SUCCESS, ResultUtils.SUCCESS_MSG, sellerBankAccount);
	}

	
	/**
	 * 
	 * @date 2018-05-08 18:51:07
	 * @Title: modifySellerBankAccountDetail 
	 * @Description: TODO
	 * @param @param id
	 * @param @param sellerBankAccount
	 * @param @param request
	 * @param @return
	 * @return JsonResponse<SellerBankAccount>
	 * @throws
	 */
	@RequestMapping(value="sellerBankAccounts/{id}", method=RequestMethod.PATCH, produces={MediaType.APPLICATION_JSON_VALUE+";charset=utf-8"})
	public JsonResponse<SellerBankAccount> modifySellerBankAccountDetail(@PathVariable("id")Integer id, @RequestBody SellerBankAccount sellerBankAccount, HttpServletRequest request){
		sellerBankAccount.setId(id);
		sellerBankAccountService.update(sellerBankAccount);
		return new JsonResponse<SellerBankAccount>(ResultUtils.SUCCESS, ResultUtils.SUCCESS_MSG, sellerBankAccountService.get(id.toString()));
	}
	
	
	/**
	 * 
	 * @date 2018-05-08 18:51:07
	 * @Title: addSellerBankAccount 
	 * @Description: TODO
	 * @param @param sellerBankAccount
	 * @param @param request
	 * @param @return
	 * @return JsonResponse<SellerBankAccount>
	 * @throws
	 */
	@RequestMapping(value="sellerBankAccounts", method=RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE+";charset=utf-8"})
	public JsonResponse<SellerBankAccount> addSellerBankAccount(@RequestBody SellerBankAccount sellerBankAccount, HttpServletRequest request){
		sellerBankAccountService.insert(sellerBankAccount);
		return new JsonResponse<SellerBankAccount>(ResultUtils.SUCCESS, ResultUtils.SUCCESS_MSG, sellerBankAccountService.findList(sellerBankAccount).get(0));
	}
	
	/**
	 * 
	 * @date 2018-05-08 18:51:07
	 * @Title: removeSellerBankAccount 
	 * @Description: TODO
	 * @param @param id
	 * @param @param request
	 * @param @return
	 * @return JsonResponse<>
	 * @throws
	 */
	@RequestMapping(value="sellerBankAccounts/{id}", method=RequestMethod.DELETE, produces={MediaType.APPLICATION_JSON_VALUE+";charset=utf-8"})
	public JsonResponse<?> removeSellerBankAccount(@PathVariable("id")Integer id, HttpServletRequest request){
		sellerBankAccountService.delete(id.toString());
		return new JsonResponse<>(ResultUtils.SUCCESS, ResultUtils.SUCCESS_MSG);
	}
	
}
