package com.spring.board.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.spring.board.entity.BoardEntity;
import com.spring.board.entity.BoardListViewEnetity;
import com.spring.board.generator.BoardEntityGenerator;
import com.spring.board.repository.BoardPagingRepository;
import com.spring.board.repository.BoardRepository;

@Service
public class BoardService {

	@Autowired
	private BoardRepository boardRepository;
	@Autowired
	private BoardPagingRepository boardPageRepository;

	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public boolean insertEntity(final BoardEntity entity) {
		if (!boardRepository.existsById(entity.getBoardUuid())) {
			logger.info("INSERT ENTITY ");
			boardRepository.save(entity);
			return true;
		} else {
			return false;
		}
	}
	
	public Page<BoardListViewEnetity> getPageView(PageRequest request) {
		return boardPageRepository.findAll(request);
	}
	
	public boolean updateEntity(final BoardEntity paramEntity) {
		logger.info("UUID : " + paramEntity.getBoardUuid() + " PASSWORD : " + paramEntity.getPassword());
		
		Optional<BoardEntity> entityWrapper = boardRepository.findByboardUuidAndPassword(paramEntity.getBoardUuid(), paramEntity.getPassword());
		
		if (entityWrapper.isPresent()) {
			boardRepository.save(paramEntity);
			return true;
		} else {
			return false;
		}
	}
	
	public Optional<BoardEntity> findEntityByUuid (final String boardUuid) {
		return boardRepository.findById(boardUuid);
	}
	
	public boolean deleteEntityByUuidAndPassword(final BoardEntityGenerator generator) {
		
		logger.info("UUID : " + generator.getBoardUuid() + " PASSWORD : " + generator.getPassword());
		
		Optional<BoardEntity> entity = boardRepository.findByboardUuidAndPassword(generator.getBoardUuid(), generator.getPassword());
		
		if(entity.isPresent()) {
			boardRepository.deleteById(entity.get().getBoardUuid());
			return true;
		} else {
			return false;
		}
		
	}
	
	public List<BoardEntity> getAllList() {
		return (List<BoardEntity>) boardRepository.getAllView();
	}
	
	public Optional<BoardEntity> BoardEntity(String boardId) {
		return boardRepository.findById(boardId);
	}
	
}
