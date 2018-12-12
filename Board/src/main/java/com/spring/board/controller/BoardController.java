package com.spring.board.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.board.entity.BoardEntity;
import com.spring.board.entity.BoardListViewEnetity;
import com.spring.board.generator.BoardEntityGenerator;
import com.spring.board.service.BoardService;
import com.spring.board.util.CryptitUtil;
import com.spring.board.util.UUIDUtil;

@RestController
public class BoardController {

	private BoardService boardService;
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final int rowPerPage = 10;
	
	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}
	
	@RequestMapping(value="/board/allView.vw/{page}")
	public Page<BoardListViewEnetity> getAllList(@PathVariable(name="page") Integer page) {
		PageRequest request = PageRequest.of(page -1, rowPerPage, Sort.Direction.DESC, "regTime");
		return boardService.getPageView(request);
	}
	
	@GetMapping("/board/detailView.vw")
	public ResponseEntity<?> getList(@RequestParam("boardUuid") String boardUuid) {
		
		logger.info("SEARCH KETWORD " + boardUuid);
		Optional<BoardEntity> entityWrapper = boardService.findEntityByUuid(boardUuid);
		
		HashMap<String, String> responseMap = new HashMap<>();
		
		if(entityWrapper.isPresent()) {
			return new ResponseEntity<BoardEntity>(entityWrapper.get(),HttpStatus.OK);
		}else {
			responseMap.put("message", "요청하신 게시물을 찾을 수 없습니다.");
			return new ResponseEntity<HashMap>(responseMap,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, value = "/board/boardDataManage.do", method = RequestMethod.POST )
	public ResponseEntity<?> insertData(@RequestBody BoardEntityGenerator requestData) {
		LocalDateTime clientRequestTime = LocalDateTime.now();
		String UUIDString = UUIDUtil.getUUID();
		
		HashMap<String, String> responseMap = new HashMap<>();	
		
		
		logger.info("Request Entity : "  + requestData);
		
		requestData.setBoardUuid(UUIDString);
		requestData.setRegTime(clientRequestTime);
		requestData.setUptTime(clientRequestTime);
		
		String encPassword = CryptitUtil.encryptSha(requestData.getPassword());
		if(encPassword != null) {
			requestData.setPassword(encPassword);
		} else {
			responseMap.put("message","비밀번호가 누락되었습니다.");
			logger.info("Crypto value is null");
			return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
		}
		
		BoardEntity entity = requestData.toEntity();
		
		try {
			boolean isExecute = boardService.insertEntity(entity);
			
			if(isExecute) {
				responseMap.put("message", "정상적으로 등록되었습니다.");
				return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
			} else {
				responseMap.put("message", "같은 게시물 번호로 등록된 게시물이 있습니다. \n 다시 한번 시도해 주세요");
				return new ResponseEntity<HashMap>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			responseMap.put("message", "일시적인 서버 오류입니다. \n 다시 한번 시도해주시거나 운영자에게 연락 부탁드리겠습니다.\n※ arcuer.dev@gmail.com");
			return new ResponseEntity<HashMap>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, value = "/board/boardDataManage.do", method = RequestMethod.PUT )
	public ResponseEntity<?> updateData(@RequestBody BoardEntityGenerator requestData) {
		HashMap<String, String> responseMap = new HashMap<>();	
		String encPassword = CryptitUtil.encryptSha(requestData.getPassword());
		
		LocalDateTime clientRequestTime = LocalDateTime.now();
		
		if(encPassword != null) {
			requestData.setPassword(encPassword);
		} else {
			responseMap.put("message","비밀번호가 누락되었습니다.");
			logger.info("Crypto value is null");
			return new ResponseEntity<HashMap>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// update time set
		requestData.setUptTime(clientRequestTime);
		logger.info("Request Entity : "  + requestData.toString());
		
		BoardEntity entity = requestData.toEntity();
		
		try {
			boolean isExecute = boardService.updateEntity(entity);
			
			if(isExecute) {
				responseMap.put("message", "정상적으로 수정되었습니다.");
				return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
			} else {
				logger.info("FAIL  :  NOT FOUND AT TABLE UUID : " + entity.getBoardUuid());
				responseMap.put("message", "게시판을 수정할 수 없습니다.\n 이미 삭제된 게시물이거나 비밀번호가 틀립니다.");
				return new ResponseEntity<HashMap>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.info(e.getMessage() + e.getCause());
			responseMap.put("message", "일시적인 서버 오류입니다. \n 다시 한번 시도해주시거나 운영자에게 연락 부탁드리겠습니다.\n※ arcuer.dev@gmail.com");
			return new ResponseEntity<HashMap>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, value = "/board/boardDataManage.do" , method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteData(@RequestBody BoardEntityGenerator requestData) {
		HashMap<String, String> responseMap = new HashMap<>();	
		try {
			String encPassword = CryptitUtil.encryptSha(requestData.getPassword());
			
			if(encPassword != null) {
				requestData.setPassword(encPassword);
			} else {
				responseMap.put("message","비밀번호가 누락되었습니다.");
				logger.info("Crypto value is null");
				return new ResponseEntity<HashMap>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			boolean isExecute = boardService.deleteEntityByUuidAndPassword(requestData);
			
			if(isExecute) {
				responseMap.put("message", "정상적으로 삭제되었습니다.");
				return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
			} else {
				responseMap.put("message", "게시판을 삭제할 수 없습니다.\n 이미 삭제된 게시물이거나 비밀번호가 틀립니다.");
				return new ResponseEntity<HashMap>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.info(e.getMessage() + e.getCause());
			responseMap.put("message", "일시적인 서버 오류입니다. \n 다시 한번 시도해주시거나 운영자에게 연락 부탁드리겠습니다.\n※ arcuer.dev@gmail.com");
			return new ResponseEntity<HashMap>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
