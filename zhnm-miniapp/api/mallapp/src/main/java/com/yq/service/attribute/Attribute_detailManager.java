package com.yq.service.attribute;

import java.util.List;

import org.change.entity.Page;
import org.change.util.PageData;

/** 
 * 说明： 商品属性详情接口
 * 创建人：千派网络 www.qanpai.com
 * 创建时间：2017-06-22
 * @version
 */
public interface Attribute_detailManager{

	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	
}

