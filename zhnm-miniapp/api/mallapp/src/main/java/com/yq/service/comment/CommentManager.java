package com.yq.service.comment;

import java.util.List;

import org.change.entity.Page;
import org.change.util.PageData;

/** 
 * 说明： 评价接口
 * 创建人：壹仟科技 qq 357788906
 * 创建时间：2017-01-05
 * @version
 */
public interface CommentManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public int save(List<PageData> list,PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
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
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}

