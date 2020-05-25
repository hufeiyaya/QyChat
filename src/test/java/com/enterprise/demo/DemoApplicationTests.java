package com.enterprise.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.junit.jupiter.api.Test;
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
	void contextLoads() {
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
			String encrypt_random_key ="Hk2cKKtnshe2aFhDwE0UAikSXXnUWC23MQZQa4UVDkGzUrdKAasSPTMi3jI/whhhiEWyFBkAPH064K1r+4MNuGp2W5MU52N9p3pSgByoDBG1V+vP7iTgC+6hQHCXApUfBB5/8UOUu/HKbzTZ+DoH/v02BUlISY3hzWZ9pA1dC+I67DF3q6mnJBkMmNDqAVpfCGdiFKS4i5g9lIRgf3fdEEycnLi+wPlYYgZnNuH5wRgJKqnqfvmPya6tFiBoBW5pfIWaBBVp1Af0Uqu0QNtbQtaVm9fyhXHX8dslcPYtv6fc4ndKDK1pJx+kMAWRP7niWEG9pI0mmv35OrC765zMwA==";
			String encrypt_key = null;
			String encrypt_msg="eAnewCo3NjGyVKXW9GSsbHr5WCpJOpleTUPGOUA42XwUD2ZTybAwaoo75H3U4a0dOPuOcGa5IocuBbb5jRbcQQCc+VetBMfL9cZNGzKEHMTciRjmyNJpDo46f/eHN3IfSg7VF/gJsYgVqyobSecaaYDJLstkYJFcHXymlYFtWlhedN4u1cdsw0s2qkj9qrLl5DVl9b+6i5Z3ug2zRMpwqvNRGzSDv7zA7Zy6pWGdUUclaLpGc11obBMywZQLpxfc7SW3WN2TQiZ7tUyCbLp3ob7s74/MU5vxC1aCqLRzHRupjgGYYzwGiIHXQAmytF8AWePuMPa4nIJ9An2zJ9OXAsp7UjkXTzMX8INYmATY8KgZAuAkvnW/FNQOVO8Wa0YgLinTCutKmoSYMaoD4XsMeDjnh94RS78PhBtbCVSztMc9wZlmA76um1MlYiuSRE6Tx3/EjPs2QnTHrm4mwu5U6k3ONf4PxFnRNRHJ19De5XJYnpPHo+ea+hwjs3kvl/EU5TR6TeN9ZKqsKO8IGAnZQnK9rcsTdo9o/4PDwMlk21KB/dtdHAOfWOQjdNRxlgIHa2JEPhw9kkifUpIZ2NIXcpn6yQ/RxZlpqtY6KnxIU/S3aDDJ6weGs6QDtrUghQi5pTf/Ii8r0QyrM6lO5km2IBwMxuMOdcaJU304WPDRmoFnfmAXihME41EFBlEPXDKtMbGXLHPSHo9hg/3kWUft/9RmvuBQexWsajdD6Qvh1M/cPWi4rpl9+0irsTFUG7tlXDX/5A5+gV1wbi8/366uHA3WEmmXz6bl19bMEnjfMWz0t48AKgAxosgqsN26Xw0/lx8xyAccjsbZqTiMFwJ4hGgjZ6g5l8I+cJ8WndHvqwbmdfAt+pmJc4YbA1RwO40r+nbym0qYTXQ6y90aEaCzXouGhDN/lJqhQWk0nsGU6jMHJOyDva4/XUdU8T3v1Wtl49PLlctqu";
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
			System.out.println("decrypt ret:" + ret + " msg:" + Finance.GetContentFromSlice(msg));
			Finance.FreeSlice(msg);
		} else {
			System.out.println("wrong args " + args[0]);
		}
		Finance.DestroySdk(sdk);
	}

}
