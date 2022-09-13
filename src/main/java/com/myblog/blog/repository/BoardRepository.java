package com.myblog.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.myblog.blog.model.Board;
import com.myblog.blog.model.User;

@Repository // 생략 가능
public interface BoardRepository extends JpaRepository<Board, Integer>{
	
}
