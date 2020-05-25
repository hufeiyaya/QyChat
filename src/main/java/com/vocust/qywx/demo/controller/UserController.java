package com.vocust.qywx.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.vocust.qywx.demo.dao.entity.User;
import com.vocust.qywx.demo.service.UserService;
import com.vocust.qywx.demo.utils.EnterperiseUtils;
import com.vocust.qywx.demo.utils.EnterpriseParame;
import com.vocust.qywx.demo.utils.HttpUtils;
import com.vocust.qywx.demo.utils.page.PageResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/login")
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/queryAllUsers")
	public List<User> queryAllUsers() {
		return userService.queryAllUsers();
	}

	@RequestMapping(value = "/userLogin", method = RequestMethod.POST)
	public Map<String, Object> userLogin(@RequestParam("account") String account,@RequestParam("password") String password) {
		Map<String, Object> map = new HashMap<>();
		if("admin".equals(account)&&"admin".equals(password))
		{
			
			User user = new User();
			user.setUsername("admin");
			user.setPassword("admin");
			user.setAge(18);
			map.put("loginUser", user);
			map.put("result", "yes");
		}else
		{
			map.put("result", "no");
		}
		
		return map;
	}

	/**
	 * 获取会话内容存档开启成员列表 企业可通过此接口，获取企业开启会话内容存档的成员列表
	 * 
	 * 请求方式：GET（HTTPS）
	 * 请求地址：https://qyapi.weixin.qq.com/cgi-bin/msgaudit/get_permit_user_list?access_token=ACCESS_TOKEN
	 * 
	 * @param account
	 * @param password
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/getAllowUsers", method = RequestMethod.POST)
	public PageResponse getAllowUsers() throws Exception {
		PageResponse pageResponse = new PageResponse();
		List<?> idLst =getIdList();
		List<User> result = new  ArrayList<User>();
		int i=0;
		for (Object obj : idLst) {
			User user =new User();
			user.setUsername(userService.getUsernameByUserid(obj.toString()));
			user.setId(i);
			result.add(user);
			i++;
		}
		pageResponse.setTotal(result.size());
		pageResponse.setRes(result);
		return pageResponse;
	}

	private List<Object> getIdList() throws Exception {
		String accessToken = EnterperiseUtils.getAccessToken(EnterpriseParame.CORPID, EnterpriseParame.SECRET);
		Map<String, String> param = new HashMap<String, String>();
		param.put("access_token", accessToken);
		String datas = HttpUtils.get("https://qyapi.weixin.qq.com/cgi-bin/msgaudit/get_permit_user_list", param);
		JSONObject result = JSONObject.parseObject(datas);
		List<Object> list = result.getJSONArray("ids");
		return list;
	}

}