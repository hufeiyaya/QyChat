package com.vocust.qywx.demo.dao.entity;

import lombok.Data;

/**
*@author  hf
*@version 1.0
*@date  2020年5月15日 下午7:20:49
*@desc 
*/

@Data
public class User {
    private long id;
	private String username;
	private String password;
    private String sex;
    private Integer age;
    private Integer classNo;
    private String loginTime;
}