package com.vocust.qywx.demo.dao.mapper;

import java.util.List;

import com.vocust.qywx.demo.dao.entity.Qychat;

/**
 * @author hf
 * @version 1.0
 * @date 2020年5月15日 下午7:20:17
 * @desc
 */

public interface QychatMapper {

	 List<Qychat> queryAllInfos();
	 
	 void insertQychat(Qychat quchat);
	 
	 void clearDatas();

	 Integer getSeq();

}
