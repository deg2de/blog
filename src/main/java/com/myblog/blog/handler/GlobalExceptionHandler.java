package com.myblog.blog.handler;

import java.nio.channels.IllegalChannelGroupException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.myblog.blog.dto.ResponseDto;

@ControllerAdvice // 모든 클래스에서 익셉션이 발생하면 이 클래스로 온다.
@RestController
public class GlobalExceptionHandler {
	
//	@ExceptionHandler(value = IllegalArgumentException.class) // 해당 익셉션에 대해 호출된다.
//	public String handleArgumentException(IllegalArgumentException e) {
//		return "<h1>" + e.getMessage() + "</h1>";
//	}
	
	@ExceptionHandler(value = Exception.class) // 모든 익셉션에 대해 호출된다. [500번 서버 오류]
	public ResponseDto<String> handleArgumentException(Exception e) {
		return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
	}
}
