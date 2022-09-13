package com.myblog.blog.controller.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.myblog.blog.config.auth.PrincipalDetail;
import com.myblog.blog.dto.ReplySaveRequestDto;
import com.myblog.blog.dto.ResponseDto;
import com.myblog.blog.model.Board;
import com.myblog.blog.model.Reply;
import com.myblog.blog.service.BoardService;

@RestController
public class BoardApiController {
	
	@Autowired
	private BoardService boardService;
	
	@PostMapping("/api/board")
	public ResponseDto<Integer> save(@RequestBody Board board, @AuthenticationPrincipal PrincipalDetail principal) {
		
		boardService.boardSave(board, principal.getUser());

		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/api/board/{id}")
	public ResponseDto<Integer> update(@PathVariable int id, @RequestBody Board board) {
		
		boardService.boardUpdate(id, board);

		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@DeleteMapping("/api/board/{id}")
	public ResponseDto<Integer> deleteById(@PathVariable int id) {

		boardService.boardDelete(id);

		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	// 데이터 받을 때 컨트롤러에서 dto를 만들어서 받는게 좋다.
	// 여기서 dto 사용하지 않은 이유는 지금 프로젝트가 조그마한 프로젝트이기 때문이다.
	// 큰 프로젝트에서는 dto를 무조건 써야 한다.
	// DTO 예시는 ReplySaveRequestDto 참조
	// @RequestBody ReplySaveRequestDto replySaveRequestDto
//	@PostMapping("/api/board/{boardId}/reply")
//	public ResponseDto<Integer> replySave(@PathVariable int boardId, @RequestBody Reply reply, @AuthenticationPrincipal PrincipalDetail principal) {
//		
//		
//		boardService.replySave(reply, principal.getUser(), boardId);
//
//		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
//	}
	
	@PostMapping("/api/board/{boardId}/reply")
	public ResponseDto<Integer> replySave(@RequestBody ReplySaveRequestDto replySaveRequestDto, @AuthenticationPrincipal PrincipalDetail principal) {
		
		
		boardService.replySave(replySaveRequestDto);

		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@DeleteMapping("/api/board/{boardId}/reply/{replyId}")
	public ResponseDto<Integer> replyDelete(@PathVariable int replyId) {
		
		boardService.replyDelete(replyId);

		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
}
