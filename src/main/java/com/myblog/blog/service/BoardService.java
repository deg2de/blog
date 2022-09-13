package com.myblog.blog.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myblog.blog.dto.ReplySaveRequestDto;
import com.myblog.blog.model.Board;
import com.myblog.blog.model.Reply;
import com.myblog.blog.model.RoleType;
import com.myblog.blog.model.User;
import com.myblog.blog.repository.BoardRepository;
import com.myblog.blog.repository.ReplyRepository;
import com.myblog.blog.repository.UserRepository;

// 스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해준다. (IoC를 해준다.)
@Service
public class BoardService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	@Transactional // DB 연동 작업 전체가 성공해야 커밋이 된다.
	public void boardSave(Board board, User user) { // title. content
		
		board.setCount(0);
		board.setUser(user);
		boardRepository.save(board);
	}
	
	@Transactional(readOnly = true) // DB 연동 작업 전체가 성공해야 커밋이 된다.
	public Page<Board> readBoardList(Pageable pageable) {
		return boardRepository.findAll(pageable);
	}
	
	@Transactional(readOnly = true) // DB 연동 작업 전체가 성공해야 커밋이 된다.
	public Board viewBoard(int id) {
		return boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 상세보기 실패 : id를 찾을 수 없습니다.");
				});
	}
	
	@Transactional // DB 연동 작업 전체가 성공해야 커밋이 된다.
	public void boardUpdate(int id, Board requestBoard) {
		
		Board board = boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 찾기 실패 : id를 찾을 수 없습니다.");
				}); // 영속화 완료
		
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
		// 해당 함수 종료시(service가 종료될 때) 트랜잭션이 종료된다. 이때 더티체킹 - 자동 업데이트 flush(commit이 된다는것)
	}
	
	@Transactional // DB 연동 작업 전체가 성공해야 커밋이 된다.
	public void boardDelete(int id) {
		boardRepository.deleteById(id);
	}
	
//	@Transactional // DB 연동 작업 전체가 성공해야 커밋이 된다.
//	public void replySave(Reply requestReply, User user, int boardId) { // title. content
//		
//		Board board = boardRepository.findById(boardId)
//				.orElseThrow(()->{
//					return new IllegalArgumentException("글 상세보기 실패 : id를 찾을 수 없습니다.");
//				});
//		requestReply.setUser(user);
//		requestReply.setBoard(board);
//		replyRepository.save(requestReply);
//	}
	@Transactional // DB 연동 작업 전체가 성공해야 커밋이 된다.
	public void replySave(ReplySaveRequestDto replySaveRequestDto) { // title. content
		
//		User user = userRepository.findById(replySaveRequestDto.getBoardId())
//				.orElseThrow(()->{
//					return new IllegalArgumentException("글 상세보기 실패 : id를 찾을 수 없습니다.");
//				});
//		
//		Board board = boardRepository.findById(replySaveRequestDto.getBoardId())
//				.orElseThrow(()->{
//					return new IllegalArgumentException("글 상세보기 실패 : id를 찾을 수 없습니다.");
//				});
//		
//		Reply reply = Reply.builder()
//				.user(user)
//				.board(board)
//				.content(replySaveRequestDto.getContent())
//				.build();
//		
//		replyRepository.save(reply);
		
		replyRepository.mSave(replySaveRequestDto.getUserId(), replySaveRequestDto.getBoardId(), replySaveRequestDto.getContent());
	}
	
	@Transactional // DB 연동 작업 전체가 성공해야 커밋이 된다.
	public void replyDelete(int replyId) {
		
		replyRepository.deleteById(replyId);
	}
}
