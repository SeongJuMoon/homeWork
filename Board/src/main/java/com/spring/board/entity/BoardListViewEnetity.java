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

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name="BOARD")
@Getter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
public class BoardListViewEnetity {

	@Id
	@Column(name="board_uuid")
	private String boardUuid;
	@CreatedDate
	@Convert(converter=LocalTimeToPersistanceCovertor.class)
	@Column(name="reg_dttm")
	private LocalDateTime regTime;
	@Column(name="user_name")
	private String userName;
	@Column(name="title")
	private String title;
	
	@Builder
	public BoardListViewEnetity(String boardUuid, LocalDateTime regTime, String userName, String title) {
		super();
		this.boardUuid = boardUuid;
		this.regTime = regTime;
		this.userName = userName;
		this.title = title;
	}
	
	
	
}
