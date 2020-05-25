package com.vocust.qywx.demo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vocust.qywx.demo.dao.entity.MsgContent;
import com.vocust.qywx.demo.dao.mapper.MsgContentMapper;
import com.vocust.qywx.demo.service.MsgContentService;
import com.vocust.qywx.demo.utils.EnterperiseUtils;
import com.vocust.qywx.demo.utils.EnterpriseParame;
import com.vocust.qywx.demo.utils.EnumMsgType;
import com.vocust.qywx.demo.utils.HttpUtils;
import com.vocust.qywx.demo.utils.page.PageBean;
import com.vocust.qywx.demo.utils.page.PageParam;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MsgContentServiceImpl implements MsgContentService {

	@Autowired
	private MsgContentMapper msgContentMapper;

	@Value("${filepath}")
	private String filepath;
	
	private static final Gson gson =new Gson();

	@Override
	public PageBean findAll(PageParam page) {

		PageBean pageBean = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("page", page);
			List<MsgContent> msgList = msgContentMapper.findAll(page);
			for (MsgContent msgContent : msgList) {
				msgContent.setMsgtype(EnumMsgType.getName(msgContent.getMsgtype()));

			}
			pageBean = new PageBean(page.getPageNum(), page.getNumPerPage(), page.getCount(), msgList);
			pageBean.setTotalCount(msgContentMapper.counts());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageBean;

	}

	@Override
	public Map getMsgByIdAndType(int id, String msgtype) {
		Map<String, Comparable> map = new HashMap<String, Comparable>();
		map.put("id", id);
		map.put("msgtype", msgtype);
		String fileName = msgContentMapper.getFileById(id);
		String content = msgContentMapper.getMsgByIdAndType(map);
		log.info("get msg------> " + content);
		JsonObject jsonObject = gson.fromJson(content, JsonObject.class);
		Map<String, String> data = new HashMap<String, String>();
		if ("text".equals(msgtype)) {
			data.put("result", jsonObject.get("content").getAsString());
		} else if ("image".equals(msgtype) || "voice".equals(msgtype) || "video".equals(msgtype)) {
			data.put("result", jsonObject.toString());
			data.put("filename", fileName);
		} else {
			data.put("result", jsonObject.toString());
		}

		return data;
	}

	@Override
	public String getOuterCustomerDetails(String externalUserid) {
		String accessToken = EnterperiseUtils.getAccessToken(EnterpriseParame.CORPID, EnterpriseParame.CUSTOMER_SECRET);
		Map<String, String> param = new HashMap<String, String>();
		param.put("access_token", accessToken);
		param.put("external_userid", externalUserid);
		String data = null;
		try {
			data = HttpUtils.get("https://qyapi.weixin.qq.com/cgi-bin/externalcontact/get", param);
		} catch (Exception e) {
			log.error("获取外部客户信息失败："+e);
		}
		
		return data;

	}

	@Override
	public String getInnerCustomerDetails(String userid) {
		String data = null;
		String accessToken = EnterperiseUtils.getAccessToken(EnterpriseParame.CORPID, EnterpriseParame.CUSTOMER_SECRET);
		Map<String, String> param = new HashMap<String, String>();
		param.put("access_token", accessToken);
		param.put("userid", userid);
		try {
			data = HttpUtils.get("https://qyapi.weixin.qq.com/cgi-bin/user/get", param);
		} catch (Exception e) {
			log.error("获取内部用户信息失败："+e);
		}
		return data;
	}

	@Override
	public String getGroupchatInfoByRoomid(String roomid) {
		String accessToken = EnterperiseUtils.getAccessToken(EnterpriseParame.CORPID, EnterpriseParame.CUSTOMER_SECRET);
		Map<String, String> param = new HashMap<String, String>();
		param.put("chat_id", roomid);
		System.out.println(new Gson().toJson(param));
		String result = null;
		try {
			result = HttpUtils.post("https://qyapi.weixin.qq.com/cgi-bin/externalcontact/groupchat/get?access_token="+accessToken,new Gson().toJson(param));
		} catch (Exception e) {
			log.error("获取客户群信息失败："+e);
		}
		log.info(result);
		return result;
	}

}