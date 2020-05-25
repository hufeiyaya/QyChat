package com.vocust.qywx.demo.dao.entity;

import lombok.Data;

/**
 * @author hf
 * @version 1.0
 * @date 2020年5月15日 下午8:23:10
 * @desc
 */

@Data
public class QueryParam {
	private String searchType;
	
	private int limit;

	private String proxy;

	private String password;

	private long timeout;
}
