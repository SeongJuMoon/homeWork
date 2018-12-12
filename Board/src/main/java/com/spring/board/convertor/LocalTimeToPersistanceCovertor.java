package com.spring.board.convertor;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;

@Converter
public class LocalTimeToPersistanceCovertor implements AttributeConverter<LocalDateTime, Date>{

	@Override
	public Date convertToDatabaseColumn(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(systemDefault()).toInstant());
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Date dbData) {
		return ofInstant(ofEpochMilli(dbData.getTime()), systemDefault());
	}
}
