package com.vocust.qywx.demo.service;

import java.util.Map;

import com.vocust.qywx.demo.utils.page.PageBean;
import com.vocust.qywx.demo.utils.page.PageParam;

/**
*@author  hf
*@version 1.0
*@date  2020年5月15日 下午7:22:12
*@desc 
*/

public interface MsgContentService {

	PageBean findAll(PageParam page);

	Map getMsgByIdAndType(int id, String msgtype);
	
	/**
	 * 获取外部成员详细信息
	 * @param extendsUserid
	 * @throws Exception 
	 */
	String getOuterCustomerDetails(String extendsUserid);
	
	/**
	 * 获取内部成员详细信息
	 * @param extendsUserid
	 * @throws Exception 
	 */
 	String getInnerCustomerDetails(String extendsUserid);

	String getGroupchatInfoByRoomid(String roomid);

}
