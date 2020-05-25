package com.vocust.qywx.demo.strategy;

import com.alibaba.fastjson.JSONObject;

/**
*@author  hf
*@version 1.0
*@date  2020年5月21日 下午3:08:20
*@desc 
*/

public interface MsgTypeStrategy {

		public void downLoadFile(String msgtype, JSONObject jsonObject);
}
