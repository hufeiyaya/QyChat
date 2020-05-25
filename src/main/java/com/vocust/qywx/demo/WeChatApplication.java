package com.vocust.qywx.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync  //开启异步调用
@MapperScan({"com.vocust.qywx.demo.dao.mapper"})
@SpringBootApplication
public class WeChatApplication
{
  private static final Logger log = LoggerFactory.getLogger(WeChatApplication.class);

  public static void main(String[] args)
  {
    SpringApplication.run(WeChatApplication.class, args);
    log.info("server start success!");
  }
}