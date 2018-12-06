package com.spring.board.entity;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Builder;

@Entity
public class BoardEntity {
	@Column(name="USER_NAME")
	private  String userName;
	@Column(name="TITLE")
	private  String title;
	@Column(name="CONTENT")
	private  String content;
	@Column(name="REG_DTTM")
	private  LocalTime time;
	
	protected BoardEntity() {
		// JPA USING
	}

	@Builder
	public BoardEntity(String userName, String title, String content, LocalTime time) {
		super();
		this.userName = userName;
		this.title = title;
		this.content = content;
		this.time = time;
	}

	public String getUserName() {
		return userName;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public LocalTime getTime() {
		return time;
	}

}
