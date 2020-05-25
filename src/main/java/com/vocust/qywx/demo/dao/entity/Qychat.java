package com.vocust.qywx.demo.dao.entity;

import java.io.Serializable;

import lombok.Data;

/**
*@author  hf
*@version 1.0
*@date  2020年5月15日 下午4:27:43
*@desc 
*/

@Data
public class Qychat implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String seq;

	private String msgid;

	private String publickey_ver;

	/**加密RSA秘钥*/
	private String encrypt_random_key;

	/**加密消息*/
	private String encrypt_chat_msg;
}
