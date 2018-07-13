package com.wqwy.zhnm;



import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.wqwy.zhnm.base.component.constant.DefaultConstants;
import com.wqwy.zhnm.base.component.utils.wxpay.WXPayUtil;
import com.wqwy.zhnm.mallapp.constant.TestConsts;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DefaultControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象
    }

    @Test
    public void getIndex() throws Exception {
//    	https://www.wqwy2014.com/mallapp/goods/info?goods_id=38
        mockMvc.perform(MockMvcRequestBuilders.get("/")
        		.header(DefaultConstants.TOKEN, TestConsts.TokenValue)
                .accept(MediaType.APPLICATION_JSON_UTF8)).andDo(print());
    }
    
    @Test
    public void getGoods() throws Exception {
//    	https://www.wqwy2014.com/mallapp/goods/info?goods_id=38
        mockMvc.perform(MockMvcRequestBuilders.get("/goods/info?goods_id=38")
        		.header(DefaultConstants.TOKEN, TestConsts.TokenValue)
                .accept(MediaType.APPLICATION_JSON_UTF8)).andDo(print());
    }
    
}