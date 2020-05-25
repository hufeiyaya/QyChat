package com.vocust.qywx.demo.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tencent.wework.Finance;
import com.vocust.qywx.demo.dao.entity.ChatDatas;
import com.vocust.qywx.demo.dao.entity.MsgContent;
import com.vocust.qywx.demo.dao.entity.QueryParam;
import com.vocust.qywx.demo.dao.entity.Qychat;
import com.vocust.qywx.demo.dao.mapper.MsgContentMapper;
import com.vocust.qywx.demo.dao.mapper.QychatMapper;
import com.vocust.qywx.demo.service.MsgContentService;
import com.vocust.qywx.demo.service.QychatService;
import com.vocust.qywx.demo.service.UserService;
import com.vocust.qywx.demo.utils.EnterpriseParame;
import com.vocust.qywx.demo.utils.RSAUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Service
@EnableScheduling
public class QychatServiceImpl implements QychatService {
	@Autowired
	private QychatMapper qychatMapper;

	@Autowired
	private MsgContentMapper msgContentMapper;
	
	@Autowired
	private MsgContentService msgContentService;
	
	@Autowired
	private UserService userService;
	
	@Value("${filepath}")
	private String filepath;
	
	private static final Gson gson =new Gson();;

	@Override
	public List<Qychat> queryAllInfos() {
		log.info("/queryAllUsers start...");
		return qychatMapper.queryAllInfos();
	}

	/**
	 * 每五分钟执行一次定时任务
	 */
	@Override
	@Scheduled(cron = "0 */5 * * * ?")
	public void initQychatData() {
		QueryParam param = new QueryParam();
		Integer seq = qychatMapper.getSeq() == null ? 0 : qychatMapper.getSeq();// 从第几条开始拉取
		param.setLimit(100);// 一次拉取多少条消息 最大值为1000
		param.setTimeout(5);
		int ret = 0;
		long sdk = Finance.NewSdk();

		// 初始化
		Finance.Init(sdk, EnterpriseParame.CORPID, EnterpriseParame.SECRET);
		int limit = param.getLimit();
		long slice = Finance.NewSlice();
		ret = Finance.GetChatData(sdk, seq, limit, param.getProxy(), param.getPassword(), param.getTimeout(), slice);
		if (ret != 0) {
			log.error("getchatdata ret " + ret);
			return;
		}
		// 获取消息
		String data = Finance.GetContentFromSlice(slice);

		JSONObject jsonObject = JSONObject.parseObject(data);
		ChatDatas cdata = JSON.toJavaObject(jsonObject, ChatDatas.class);
		List<Qychat> list = cdata.getChatdata();
		for (Qychat qychat : list) {
			String msgs = qychat.getEncrypt_chat_msg();
			String encrypt_key = null;
			try {
				encrypt_key = RSAUtils.getPrivateKey(qychat.getEncrypt_random_key());
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 将获取到的数据进行解密操作
			long msg = Finance.NewSlice();
			Finance.DecryptData(sdk, encrypt_key, msgs, msg);
			String decrypt_msg = Finance.GetContentFromSlice(msg);// 解密后的消息
			qychatMapper.insertQychat(qychat);
			JSONObject content = JSONObject.parseObject(decrypt_msg);
			MsgContent msgcontent = new MsgContent();
			if(content.getString("action").equals("send"))
			{
				msgcontent.setMsgid(content.getString("msgid"));
				msgcontent.setAction(content.getString("action"));
				msgcontent.setFrom(content.getString("from"));
				msgcontent.setFromView(userService.getUsernameByUserid(content.getString("from")));
				msgcontent.setTolist(content.getString("tolist"));
				msgcontent.setTolistView(getTolistByUserId(content.getString("tolist")));
				msgcontent.setRoomid(content.getString("roomid"));
				msgcontent.setRoomidView(getGroupchatName(content.getString("roomid")));
				msgcontent.setMsgtime(content.getString("msgtime"));
				msgcontent.setMsgtype(content.getString("msgtype"));
				msgcontent.setText(isEmpty(content.getString("text")));
				msgcontent.setImage(isEmpty(content.getString("image")));
				msgcontent.setWeapp(isEmpty(content.getString("weapp")));
				msgcontent.setRedpacket(isEmpty(content.getString("redpacket")));
				msgcontent.setFile(isEmpty(content.getString("file")));
				msgcontent.setVideo(isEmpty(content.getString("video")));
				msgcontent.setVoice(isEmpty(content.getString("voice")));
				msgcontent.setChatrecord(isEmpty(content.getString("chatrecord")));
				msgcontent.setFilename(getFileNameAndDownloadData(msgcontent));	
			}else if(content.getString("action").equals("switch"))
			{
				msgcontent.setMsgid(content.getString("msgid"));
				msgcontent.setAction(content.getString("action"));
				msgcontent.setFrom(content.getString("user"));
				msgcontent.setFromView(userService.getUsernameByUserid(content.getString("user")));
			}else 
			{
				msgcontent.setMsgid(content.getString("msgid"));
				msgcontent.setAction(content.getString("action"));
			}
			// 解析消息 并插入到数据库
			msgContentMapper.insertMsgContent(msgcontent);
		}
		Finance.FreeSlice(slice);
		log.info("----------------------scheduled tasks qywx data success-----------------------");

	}

	
	private String getGroupchatName(String roomid) {
		if(StringUtils.isEmpty(roomid))
			return null;
		String data  = msgContentService.getGroupchatInfoByRoomid(roomid);
		JsonObject result = gson.fromJson(data, JsonObject.class);
		String name=null;
		if(result.get("errcode").getAsInt()!=0)
		{
			name = "该群不是客户群";
		}
		else
		{
			name  = result.get("group_chat").getAsJsonObject().get("name").getAsString();
		}
	
		return name;
	}

	private String getTolistByUserId(String tolist) {
		if(StringUtils.isEmpty(tolist))
			return null;
		List<String> list =new ArrayList<String>();
		JsonArray result = gson.fromJson(tolist, JsonArray.class);
		for (JsonElement jsonElement : result) {
			list.add(userService.getUsernameByUserid(jsonElement.getAsString()));
		}
		return list.toString();
	}



	private String content = null;

	private String isEmpty(String data) {
		if (StringUtils.isEmpty(data)) {
			return null;
		} else {
			content = data;
			return data;

		}
	}

	private String getFileNameAndDownloadData(MsgContent msgcontent) {
		String fileName = null;
		try {
			String fileType = msgcontent.getMsgtype();
			JSONObject jsonObject = JSONObject.parseObject(content);
			String sdkfileid = jsonObject.getString("sdkfileid");
			fileName =getFileName(jsonObject,fileType);
			if (!StringUtils.isEmpty(sdkfileid) && null != fileName) {
				downLodaFile(fileName, sdkfileid);
			}
		} catch (Exception e) {
			log.info("下载文件出错" + e);
		}
		return fileName;

	}

	private String getFileName(JSONObject jsonObject, String fileType) {
		String fileName = null;
		String md5sum =jsonObject.getString("md5sum");
		switch (fileType) {
		case "image":
			fileName = md5sum+ ".jpg";
			break;
		case "voice":
			fileName = md5sum+ ".mp3";
			break;
		case "video":
			fileName = md5sum+ ".mp4";
			break;
		case "file":
			fileName = jsonObject.getString("filename");
			break;
		default:
			fileName = "default.jpg";
			break;
		}
		return fileName;
	}

	@Async
	public void downLodaFile(String fileName, String sdkFileid) {
		int ret = 0;
		long sdk = Finance.NewSdk();
		// 初始化
		Finance.Init(sdk, EnterpriseParame.CORPID, EnterpriseParame.SECRET);
		String indexbuf = "";
		while (true) {
			long media_data = Finance.NewMediaData();
			ret = Finance.GetMediaData(sdk, indexbuf, sdkFileid, null, null, 3, media_data);
			if (ret != 0) {
				return;
			}
			System.out.printf("getmediadata outindex len:%d, data_len:%d, is_finis:%d\n",
					Finance.GetIndexLen(media_data), Finance.GetDataLen(media_data),
					Finance.IsMediaDataFinish(media_data));
			try {
				FileOutputStream outputStream = new FileOutputStream(new File(filepath + fileName), true);
				outputStream.write(Finance.GetData(media_data));
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (Finance.IsMediaDataFinish(media_data) == 1) {
				Finance.FreeMediaData(media_data);
				break;
			} else {
				indexbuf = Finance.GetOutIndexBuf(media_data);
				Finance.FreeMediaData(media_data);
			}
		}
		log.info("下载完毕");
		Finance.DestroySdk(sdk);
	}

}