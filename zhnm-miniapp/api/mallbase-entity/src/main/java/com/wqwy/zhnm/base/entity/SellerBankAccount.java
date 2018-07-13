/**
 * Copyright (c) 2015-2018 <a href="">wqwy</a> All rights reserved.
 */package com.wqwy.zhnm.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * createTime: 2018-05-08 18:51:07
 * @author seven
 * @version
 */
public class SellerBankAccount{

    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer sellerId;

    /**
     * 开户行
     */
    private String accountBank;

    /**
     * 银行账号
     */
    private String account;

    /**
     * 户名
     */
    private String accountName;
    
    /**
     * 银行卡账户开户行
     */
    private String accountPhone;


    public void setId(Integer value){
        this.id = value;
    }

    public Integer getId(){
        return this.id;
    }

    public void setSellerId(Integer value){
        this.sellerId = value;
    }

    public Integer getSellerId(){
        return this.sellerId;
    }

    public void setAccountBank(String value){
        this.accountBank = value;
    }

    public String getAccountBank(){
        return this.accountBank;
    }

    public void setAccount(String value){
        this.account = value;
    }

    public String getAccount(){
        return this.account;
    }

    public void setAccountName(String value){
        this.accountName = value;
    }

    public String getAccountName(){
        return this.accountName;
    }

	public String getAccountPhone() {
		return accountPhone;
	}

	public void setAccountPhone(String accountPhone) {
		this.accountPhone = accountPhone;
	}

}
