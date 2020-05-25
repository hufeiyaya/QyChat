package com.vocust.qywx.demo.utils;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

/**
*@author  hf
*@version 1.0
*@date  2020年5月21日 下午8:31:25
*@desc 
*/

@Slf4j
public class EnterperiseUtils {
	
	/**
	 * 获取accessToken
	 * @param corpid
	 * @param corpsecret
	 * @return
	 * @throws Exception
	 */
	public static String getAccessToken(String corpid, String corpsecret) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("corpid", corpid);
		param.put("corpsecret", corpsecret);
		String accessToken = null;
		try {
			accessToken = HttpUtils.get("https://qyapi.weixin.qq.com/cgi-bin/gettoken", param);
		} catch (Exception e) {
		log.info("获取accessToken出错："+e);
		}
		JSONObject content = JSONObject.parseObject(accessToken);
		return  content.getString("access_token");
	}
}
