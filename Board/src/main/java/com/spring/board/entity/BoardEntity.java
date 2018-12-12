package com.spring.board.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.spring.board.convertor.LocalTimeToPersistanceCovertor;

import lombok.Builder;
import lombok.Getter;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Table(name = "BOARD")
public class BoardEntity {

	@Id
	@Column(name="BOARD_UUID", length=40, nullable=false)
	private String boardUuid;
	@Column(name="USER_NAME", length=100, nullable=false)
	private  String userName;
	@Column(name="TITLE", length=2000, nullable=false)
	private  String title;
	@Column(name="CONTENT", length=4000, nullable=false)
	private  String content;
	@CreatedDate
	@Convert(converter = LocalTimeToPersistanceCovertor.class)
	@Column(name="REG_DTTM", updatable = false)
	private  LocalDateTime regTime;
	@CreatedDate
	@Convert(converter = LocalTimeToPersistanceCovertor.class)
	@Column(name="UPT_DTTM")
	private  LocalDateTime uptTime;
	@Column(name="PASSWORD", length=200, nullable = true)
	private String password;
	
	
	protected BoardEntity() {
		// JPA USING
	}


	@Builder
	public BoardEntity(String boardUuid, String userName, String title, String content, LocalDateTime regTime,
			LocalDateTime uptTime, String password) {
		super();
		this.boardUuid = boardUuid;
		this.userName = userName;
		this.title = title;
		this.content = content;
		this.regTime = regTime;
		this.uptTime = uptTime;
		this.password = password;
	}

	

	
}
