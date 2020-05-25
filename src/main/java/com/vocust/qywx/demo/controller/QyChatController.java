package com.vocust.qywx.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vocust.qywx.demo.dao.entity.Qychat;
import com.vocust.qywx.demo.service.QychatService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/web")
public class QyChatController {
    @Autowired
    private QychatService qychatService;

    /**
     * @see 获取所有会话信息列表
     * @return
     */
    @GetMapping("/queryAllInfos")
    public List<Qychat> queryAllInfos(){
        return qychatService.queryAllInfos();
    }
    
}