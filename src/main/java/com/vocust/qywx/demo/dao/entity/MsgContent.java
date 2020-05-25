package com.vocust.qywx.demo.dao.entity;

import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author hf
 * @version 1.0
 * @date 2020年5月16日 上午11:24:31
 * @desc
 */

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "tbl_msgcontent")
public class MsgContent {
	private long id;
	private String msgid;// 消息id，消息的唯一标识，企业可以使用此字段进行消息去重。String类型
	private String action;// 消息动作，目前有send(发送消息)/recall(撤回消息)/switch(切换企业日志)三种类型。String类型
	private String from;// 消息发送方id。同一企业内容为userid，非相同企业为external_userid。消息如果是机器人发出，也为external_userid。String类型
	private String fromView;
	private String tolist;// 消息接收方列表，可能是多个，同一个企业内容为userid，非相同企业为external_userid。数组，内容为string类型
	private String tolistView;
	private String roomid;// 群聊消息的群id。如果是单聊则为空。String类型
	private String roomidView;
	private String msgtime;// 消息发送时间戳，utc时间，ms单位。
	private String msgtype;// 文本消息为：text/image/file...
	private String text;// 消息内容。String类型
	private String image;// 消息内容。String类型
	private String weapp;// 消息内容。String类型
	private String redpacket;// 消息内容。String类型
	private String file;// 消息内容。String类型
	private String video;// 消息内容。String类型
	private String voice;// 消息内容。String类型
	private String chatrecord;// 消息内容。String类型
	private String filename;//文件名称

}
