package com.vocust.qywx.demo.service;

import java.util.List;

import com.vocust.qywx.demo.dao.entity.User;

/**
*@author  hf
*@version 1.0
*@date  2020年5月15日 下午7:22:12
*@desc 
*/

public interface UserService {

	List<User> queryAllUsers();
	
	String getUsernameByUserid(String userId);

}
