package com.vocust.qywx.demo.utils;


/**
*@author  hf
*@version 1.0
*@date  2020年5月18日 下午4:27:09
*@desc 
*/

public enum EnumMsgType {
    TEXT("文本", "text"), IMAGE("图片", "image"), VOICE("语音", "voice"), VIDEO("视频", "video"), WEAPP("小程序", "weapp"), FILE("文件", "file"),CHATRECORD("会话记录消息","chatrecord");
    private String name;
    private String index;

    // 构造方法
    private EnumMsgType(String name, String index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(String index) {
        for (EnumMsgType c : EnumMsgType.values()) {
            if (c.getIndex().equals(index)) {
                return c.name;
            }
        }
        return null;
    }
    
    // 普通方法
    public static String getIndex(String name) {
        for (EnumMsgType c : EnumMsgType.values()) {
            if (c.getName().equals(name)) {
                return c.index;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

}

