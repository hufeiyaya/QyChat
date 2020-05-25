package com.vocust.qywx.demo.dao.mapper;

import java.util.List;
import java.util.Map;

import com.vocust.qywx.demo.dao.entity.MsgContent;
import com.vocust.qywx.demo.utils.page.PageParam;

/**
 * @author hf
 * @version 1.0
 * @date 2020年5月15日 下午7:20:17
 * @desc
 */

public interface MsgContentMapper {

	List<MsgContent> findAll(PageParam page);
	
	int counts();
	
	void insertMsgContent(MsgContent msgContent);

	void clearDatas();

	String getMsgByIdAndType(Map map);

	void updateFileName(Map map);

	String getFileById(int id);

}
