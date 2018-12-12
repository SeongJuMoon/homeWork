package com.spring.board.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
	
	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}
	
	@RequestMapping(value="/board/allView.vw/{page}")
	public Page<BoardListViewEnetity> getAllList(@PathVariable(name="page") Integer page) {
		int rowPerPage = 10;
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
			responseMap.put("message", "��û�Ͻ� �Խù��� ã�� �� �����ϴ�.");
			return new ResponseEntity<HashMap>(responseMap,HttpStatus.OK);
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
			responseMap.put("message","��й�ȣ�� �����Ǿ����ϴ�.");
			logger.info("Crypto value is null");
			return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
		}
		
		BoardEntity entity = requestData.toEntity();
		
		try {
			boolean isExecute = boardService.insertEntity(entity);
			
			if(isExecute) {
				responseMap.put("message", "���������� ��ϵǾ����ϴ�.");
				return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
			} else {
				responseMap.put("message", "���� �Խù� �ѹ��� ��ϵ� ���� �ֽ��ϴ�.\n �ٽ��ѹ� �õ����ּ���");
				return new ResponseEntity<HashMap>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			responseMap.put("message", "�Ͻ����� ���� �����Դϴ�. \n �ٽ� �ѹ� �õ����ֽðų� ��ڿ��� ���� ��Ź�帮�ڽ��ϴ�. �� arcuer.dev@gmail.com");
			return new ResponseEntity<HashMap>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, value = "/board/boardDataManage.do", method = RequestMethod.PUT )
	public ResponseEntity updateData(@RequestBody BoardEntityGenerator requestData) {
		HashMap<String, String> responseMap = new HashMap<>();	
		String encPassword = CryptitUtil.encryptSha(requestData.getPassword());
		
		LocalDateTime clientRequestTime = LocalDateTime.now();
		
		if(encPassword != null) {
			requestData.setPassword(encPassword);
		} else {
			responseMap.put("message","��й�ȣ�� �����Ǿ����ϴ�.");
			return new ResponseEntity<HashMap>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// update time �߰�
		requestData.setUptTime(clientRequestTime);
		logger.info("Request Entity : "  + requestData.toString());
		
		BoardEntity entity = requestData.toEntity();
		
		try {
			boolean isExecute = boardService.updateEntity(entity);
			
			if(isExecute) {
				responseMap.put("message", "���������� �����Ǿ����ϴ�.");
				return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
			} else {
				logger.info("FAIL  :  NOT FOUND AT TABLE UUID : " + entity.getBoardUuid());
				responseMap.put("message", "�Խ��� ��ȣ�� ã�� �� �����ϴ�. \n �̹� �����Ǿ����� Ȯ�����ּ���.");
				return new ResponseEntity<HashMap>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.info(e.getMessage() + e.getCause());
			responseMap.put("message", "�Ͻ����� ���� ���� �Դϴ�.");
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
				responseMap.put("message","��й�ȣ�� �����Ǿ����ϴ�.");
				return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
			}
			
			boolean isExecute = boardService.deleteEntityByUuidAndPassword(requestData);
			
			if(isExecute) {
				responseMap.put("message", "���������� ���� ó���Ǿ����ϴ�.");
				return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
			} else {
				responseMap.put("message", "������ �����Ͽ����ϴ�.\n��й�ȣ�� �ٽ��ѹ� Ȯ�����ּ���");
				return new ResponseEntity<HashMap>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.info(e.getMessage() + e.getCause());
			responseMap.put("message", "�Ͻ����� ���� ���� �Դϴ�.");
			return new ResponseEntity<HashMap>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
