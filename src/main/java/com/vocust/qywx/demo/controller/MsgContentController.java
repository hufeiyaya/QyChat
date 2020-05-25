package com.vocust.qywx.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vocust.qywx.demo.service.MsgContentService;
import com.vocust.qywx.demo.utils.EnumMsgType;
import com.vocust.qywx.demo.utils.page.PageBean;
import com.vocust.qywx.demo.utils.page.PageParam;
import com.vocust.qywx.demo.utils.page.PageResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/msg")
public class MsgContentController {
	@Autowired
	private MsgContentService msgContentService;

	@RequestMapping(value = "/findAll", method = RequestMethod.POST)
	public PageResponse queryAllInfos(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
		PageResponse pageResponse = new PageResponse();
		PageParam page = new PageParam();
		page.setPageNum(pageNum);
		page.setNumPerPage(pageSize);
		PageBean pageBean = msgContentService.findAll(page);
		pageResponse.setTotal(pageBean.getTotalCount());
		pageResponse.setRes(pageBean.getRecordList());
		return pageResponse;
	}

	@RequestMapping(value = "/getMsgById", method = RequestMethod.POST)
	public Map getMsgById(@RequestParam("id") int id, @RequestParam("msgtype") String msgtype) {
		Map map = msgContentService.getMsgByIdAndType(id, EnumMsgType.getIndex(msgtype));
		return map;
	}

}