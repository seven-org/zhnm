package com.wqwy.zhnm;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.gson.Gson;
import com.wqwy.zhnm.base.component.constant.DefaultConstants;

public class DefaultTest {
	
	@Test
	public void t1() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", 1005);
		map.put("message", "尚未登录，跳转中...");
		System.out.println(new Gson().toJson(map));
		System.out.println(DefaultConstants.MiniAppLoginRedirectStr);
	}

}
