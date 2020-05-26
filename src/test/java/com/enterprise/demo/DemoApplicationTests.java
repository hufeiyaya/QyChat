package com.enterprise.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.wework.Finance;
import com.vocust.qywx.demo.dao.entity.ChatDatas;
import com.vocust.qywx.demo.dao.entity.Qychat;
import com.vocust.qywx.demo.utils.EnterpriseParame;
import com.vocust.qywx.demo.utils.RSAUtils;

@SpringBootTest
public class DemoApplicationTests {

	// 从官网上拉下来的代码  自己测了一下 有很多需要改的地方  实际应用看代码里边具体实现。。。
	@Test
	public void contextLoads() {
		String[] args = new String[6];
		args[0] = "3";// 查询类型
		args[1] = "0";// seq
		args[2] = "999";// limit
		args[3] = "";
		args[4] = "";
		args[5] = "3";// timeout
		long sdk = Finance.NewSdk();

		System.out.println(Finance.Init(sdk, EnterpriseParame.CORPID, EnterpriseParame.SECRET));
		long ret = 0;
		if (args[0].equals("1")) {
			int seq = Integer.parseInt(args[1]);
			int limit = Integer.parseInt(args[2]);
			long slice = Finance.NewSlice();
			ret = Finance.GetChatData(sdk, seq, limit, args[3], args[4], Integer.parseInt(args[5]), slice);
			if (ret != 0) {
				System.out.println("getchatdata ret " + ret);
				return;
			}
			String data = Finance.GetContentFromSlice(slice);

			JSONObject jsonObject = JSONObject.parseObject(data);
			ChatDatas cdata = JSON.toJavaObject(jsonObject, ChatDatas.class);
			List<Qychat> list = cdata.getChatdata();

			for (Qychat ChatData : list) {
				System.out.println("msgid             :" + ChatData.getMsgid());
				System.out.println("Encrypt_random_key:" + ChatData.getEncrypt_random_key());
				String msgs = ChatData.getEncrypt_chat_msg();
				System.out.println("Encrypt_chat_msg  :" + msgs);
				String encrypt_key = null;
				try {
					encrypt_key = RSAUtils.getPrivateKey(ChatData.getEncrypt_random_key());
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("encrypt_key       :" + encrypt_key);
				long msg = Finance.NewSlice();
				System.out.println("result " + Finance.DecryptData(sdk, encrypt_key, msgs, msg));
				String datas = Finance.GetContentFromSlice(msg);

				System.out.println("--------------" + datas);
			}

			Finance.FreeSlice(slice);
		} else if (args[0].equals("2")) {
			String indexbuf = "";
			//sdkFileid 是我们从第一步拉取下来的解密消息 然后通过第三步解密后 的消息内容中 获取到的值（text消息没有   只有文件  图片 语音 视频等消息才有此字段）
			String sdkFileid ="CtYBMzA2OTAyMDEwMjA0NjIzMDYwMDIwMTAwMDIwNGVjN2ZhODFlMDIwMzBmNDI0MTAyMDQ0N2FkOTczZDAyMDQ1ZWMzYTQ2YTA0MjQzMzMwMzEzNTY1MzYzODM0MmQzMzM0MzIzNzJkMzQzODM3MzMyZDYyNjY2NTMyMmQzMzM4NjY2MzY2NjIzODM1MzQzMjMzNjQwMjAxMDAwMjAzMDE2MDEwMDQxMDIzNDZlOWUzYzAzMzZkNjNmYzFmNWU3MTg0Njg4MzBmMDIwMTAxMDIwMTAwMDQwMBI4TkRkZk1UWTRPRGcxTkRFME5EQTNOVE0zTVY4eE16UTFOalE1TXpNM1h6RTFPRGs0TnprNU1UUT0aIDY1MzkzMDM1MzUzNTMyMzQzMTYzMzc2MTM2Mzk2NTM0";
			while (true) {
				long media_data = Finance.NewMediaData();
				System.out.println(media_data + "--" + sdk);
				ret = Finance.GetMediaData(sdk, indexbuf, sdkFileid, null, null, 3, media_data);
				System.out.println("getmediadata ret:" + ret);
				if (ret != 0) {
					return;
				}
				System.out.printf("getmediadata outindex len:%d, data_len:%d, is_finis:%d\n",
						Finance.GetIndexLen(media_data), Finance.GetDataLen(media_data),
						Finance.IsMediaDataFinish(media_data));
				try {
					String fileName ="a.jpg";//文件名根据自己需要下载的文件类型自定义
					FileOutputStream outputStream = new FileOutputStream(new File("C:\\Users\\lenovo\\Desktop\\java\\"+fileName), true);
					outputStream.write(Finance.GetData(media_data));
					outputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (Finance.IsMediaDataFinish(media_data) == 1) {
					// need free media_data
					Finance.FreeMediaData(media_data);
					break;
				} else {
					indexbuf = Finance.GetOutIndexBuf(media_data);
					// need free media_data
					Finance.FreeMediaData(media_data);
				}
			}
		} else if (args[0].equals("3")) {
			// 获取到的消息进行解密
			String encrypt_random_key ="dTJhxJVjHuOHyl8f1Co3TsrJMGa7Y9bHKGaSFjSolHXC0FvybDP1v3oCRo0dYk1UmWRWILgpU02VNpag5cHOtOxKleWyozT4ml/ndFPu+t8aK7M+rv8Ojtsj8awSS2t1P3Y1cE4Xgq1+RPZkrCRKq3xc2UJdutML/SUv3tCzJSP8LpHHxdbJqqA0AwHKb5E3g7DHUlJ6OFM0NQkQKyjh8v7efNXnhJY0w0q01wT8QhC9Si6m6TT+/B7eSTf82YJ/ER8rCwBruLNXN+Z+zGTOaklJsBwaETve6KU3OwSTy1lvLl/VD+Hc5J+Ka+9XEQ/tMg342T/+ascWHVtuq/duUw==";
			String encrypt_key = null;
			String encrypt_msg="Hw0zlZSHkNcMW5cFILoupw42mTyDnxsfc5ls3cJKwePHtzNGCTksRjDttB0pSCUiAyhJl+TF09dzXGD0bngOwHGUcf1zIYjyf8PX9jG9Q3WTjvr4FbzCnxGQKBkr72bZxu9p/KvhqOC+ZDSd2ILJshXbqx5rs5NVreY94sp8kv4u8Y/czdF2Xi46dGvaPX1G4WAz3UMPWMHVr8n+nNaqW9PGOY7kNanATk=Xj";
			try {
				encrypt_key = RSAUtils.getPrivateKey(encrypt_random_key);
			} catch (Exception e) {
				e.printStackTrace();
			}
			long msg = Finance.NewSlice();
			ret =Finance.DecryptData(sdk, encrypt_key, encrypt_msg, msg);
			String decrypt_msg = Finance.GetContentFromSlice(msg);//解密后的消息
			System.out.println(decrypt_msg);
			if (ret != 0) {
				System.out.println("getchatdata ret " + ret);
				return;
			}
			Finance.FreeSlice(msg);
		} else {
			System.out.println("wrong args " + args[0]);
		}
		Finance.DestroySdk(sdk);
	}

}
