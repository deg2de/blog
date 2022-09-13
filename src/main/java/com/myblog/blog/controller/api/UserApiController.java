package com.myblog.blog.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.myblog.blog.config.auth.PrincipalDetail;
import com.myblog.blog.dto.ResponseDto;
import com.myblog.blog.model.RoleType;
import com.myblog.blog.model.User;
import com.myblog.blog.service.UserService;

@RestController
public class UserApiController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/auth/joinProc")
	public ResponseDto<Integer> save(@RequestBody User user) {
		System.out.println("UserApiController : save 호출됨");
		
		userService.userJoin(user);

		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/user")
	public ResponseDto<Integer> update(@RequestBody User user) { // json 데이터를 받으려면 @RequestBody를 적어야된다. (일반 객체는 안적어도 된다.)		
		userService.userUpdate(user);

		// 세션 강제 등록
		// username과 password로 토큰을 만들고 매니저에서 인증작업 처리 후에 Authentication을 만들어준다.
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		// 세션 등록처리
		SecurityContextHolder.getContext().setAuthentication(authentication);
				
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
//	@PostMapping("/api/user/login")
//	public ResponseDto<Integer> login(@RequestBody User user, HttpSession session) {
//		System.out.println("UserApiController : login 호출됨");
//		
//		User principal = userService.userLogin(user); // princpal : 접근주체
//		
//		if(principal != null) {
//			session.setAttribute("principal", principal);
//		}
//
//		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
//	}
}
