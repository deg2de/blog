package com.myblog.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.myblog.blog.model.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
	
	@Modifying
	@Query(value="INSERT INTO reply(userId, boardId, content, createDate) VALUES(?1, ?2, ?3, now())", nativeQuery = true)
	// 기본은 int로 업데이트 된 행 갯수 리턴
	public int mSave(int userId, int boardId, String content);
}
