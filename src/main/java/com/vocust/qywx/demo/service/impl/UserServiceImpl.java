package com.vocust.qywx.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vocust.qywx.demo.dao.entity.User;
import com.vocust.qywx.demo.dao.mapper.UserMapper;
import com.vocust.qywx.demo.service.MsgContentService;
import com.vocust.qywx.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private MsgContentService msgContentService;

	private static final Gson gson = new Gson();;

	@Override
	public List<User> queryAllUsers() {
		log.info("/queryAllUsers start...");
		return userMapper.queryAllUsers();
	}

	/**
	 * 机器人与外部联系人的账号都是external_userid，其中机器人的external_userid是以”wb”开头，例如：”wbjc7bDwAAJVylUKpSA3Z5U11tDO4AAA”，外部联系人的external_userid以”wo”或”wm”开头。
	 * 
	 * @param userId
	 * @return
	 * @return
	 */
	@Override
	public String getUsernameByUserid(String userId) {
		if(StringUtils.isEmpty(userId))
			return null;
		String userName = null;
		if (userId.startsWith("wb") || userId.startsWith("wo") || userId.startsWith("wm")) { // 外部联系人信息获取
			String data = msgContentService.getOuterCustomerDetails(userId);
			userName = analysisOuterCustomerData(data);
		} else { // 内部部联系人信息获取
			String data = msgContentService.getInnerCustomerDetails(userId);
			userName = analysisOuterCustomerData2(data);
		}
		return userName;

	}

	private String analysisOuterCustomerData2(String data) {
		String userName = null;
		JsonObject result = gson.fromJson(data, JsonObject.class);
		if (result.get("errcode").getAsInt() != 0) {
			log.info("解析异常 errcode" + result.get("errcode").getAsInt());
		} else {
			userName = result.get("name").getAsString();
		}
		return userName;
	}

	private String analysisOuterCustomerData(String data) {
		String userName = null;
		JsonObject result = gson.fromJson(data, JsonObject.class);
		if (result.get("errcode").getAsInt() != 0) {
			log.info("解析异常 errcode" + result.get("errcode").getAsInt());
		} else {
			userName = result.get("external_contact").getAsJsonObject().get("name").getAsString();
		}
		return userName;

	}
}