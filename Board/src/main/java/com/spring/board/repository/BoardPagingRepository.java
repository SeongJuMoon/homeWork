package com.spring.board.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.spring.board.entity.BoardListViewEnetity;

public interface BoardPagingRepository extends PagingAndSortingRepository<BoardListViewEnetity, String>{

}
