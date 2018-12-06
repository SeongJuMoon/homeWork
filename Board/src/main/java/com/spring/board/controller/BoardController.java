package com.spring.board.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardController {
	
	private List<?> runtileSavedList;
	
	public BoardController(List<?> runtileSavedList) {
		this.runtileSavedList = runtileSavedList;
	}
	
	@RequestMapping("/")
	public String setMainvw() {
		return "index";
	}
	
	@RequestMapping("/board/allView.vw")
	public List<String> getAllList() {
		return (List<String>) runtileSavedList;
	}
	
	@RequestMapping("/board/detailView.vw")
	public String getList(@PathVariable(name="id") String id) {
		return id;
	}
	
	@RequestMapping(value = "/board/dataManage.do", method = RequestMethod.PUT )
	public void insertData() {
		
	}
	
	@RequestMapping(value = "/board/dataManage.do", method = RequestMethod.POST )
	public void updateData() {
		
	}
	
	@RequestMapping(value = "/board/dataManage.do" , method = RequestMethod.DELETE)
	public void deleteData() {
		
	}
	
}
