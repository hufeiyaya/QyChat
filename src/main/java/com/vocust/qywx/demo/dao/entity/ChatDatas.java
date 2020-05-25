package com.vocust.qywx.demo.dao.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @author hf
 * @version 1.0
 * @date 2020年5月15日 下午4:27:43
 * @desc
 */
@Data
public class ChatDatas implements Serializable {

	private static final long serialVersionUID = 1700586620843625231L;
	private String errcode;
	private String errmsg;
	private List<Qychat> chatdata;
}
