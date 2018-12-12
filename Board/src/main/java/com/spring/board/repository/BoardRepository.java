package com.spring.board.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.spring.board.entity.BoardEntity;

public interface BoardRepository extends CrudRepository<BoardEntity, String>{
	
	static final String allViewQueryString = "SELECT BOARD_UUID,REG_DTTM,TITLE,USER_NAME FROM BOARD;";
	
	@Query(value=allViewQueryString, nativeQuery=true)
	List<BoardEntity> getAllView();
	
	Optional<BoardEntity> findByboardUuidAndPassword(String boardUuid, String password);

}
