package com.spring.board.generator;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.spring.board.entity.BoardEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardEntityGenerator {
	
	private String boardUuid;
	private String userName;
	private String title;
	private String content;
	private String password;
	private LocalDateTime regTime;
	private LocalDateTime uptTime;
	
	@Override
	public String toString() {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd hh:mm:ss.SSS");
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("DAO ");
		stringBuilder.append("[ boardUuid : ");
		stringBuilder.append(this.boardUuid);
		stringBuilder.append(" userName : ");
		stringBuilder.append(this.userName);
		stringBuilder.append(" title : ");
		stringBuilder.append(this.title);
		stringBuilder.append(" content : ");
		stringBuilder.append(this.content);
		stringBuilder.append(" password : ");
		stringBuilder.append(this.password);
		
		if(regTime == null) {
			stringBuilder.append(" regTime not mappered this Generator !!!");
		} else {
			stringBuilder.append(" regTime : ");
			stringBuilder.append(this.regTime.format(formatter));
		}
		
		if(uptTime == null) {
			stringBuilder.append(" uptTime not mappered this Generator !!!");
		} else {
			stringBuilder.append(" uptTime : ");
			stringBuilder.append(this.uptTime.format(formatter));
		}

		return stringBuilder.toString();
	}
	
	public BoardEntity toEntity() {
		return BoardEntity.builder()
				.boardUuid(boardUuid)
				.userName(userName)
				.title(title)
				.password(password)
				.content(content)
				.regTime(regTime)
				.uptTime(uptTime)
				.build();
	}
}
