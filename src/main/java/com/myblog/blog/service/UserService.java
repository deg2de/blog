package com.myblog.blog.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myblog.blog.model.RoleType;
import com.myblog.blog.model.User;
import com.myblog.blog.repository.UserRepository;

// 스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해준다. (IoC를 해준다.)
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Transactional // DB 연동 작업 전체가 성공해야 커밋이 된다.
	public void userJoin(User user) {
		
		String rawPassword = user.getPassword();
		String encPassword = encoder.encode(rawPassword);
		user.setPassword(encPassword);
		user.setRole(RoleType.USER);
		userRepository.save(user);
	}
	
	@Transactional // DB 연동 작업 전체가 성공해야 커밋이 된다.
	public void userUpdate(User user) {
		
		User persistance = userRepository.findById(user.getId()) //persistance(영속성) 유저 정보
				.orElseThrow(()->{
					return new IllegalArgumentException("회원 찾기 실패 : 회원 찾을 수 없습니다.");
				}); 
		
		// 카카오 회원은 정보를 수정할 수 없도록 검사 (Validate 체크)
		if(persistance.getOauth() == null || persistance.getOauth().equals("")) {
			String rawPassword = user.getPassword();
			String encPassword = encoder.encode(rawPassword);
			persistance.setPassword(encPassword);
			persistance.setEmail(user.getEmail());
		}
		// 회원수정 함수 종료시 = 서비스 종료 = 트랜잭션 종료 = commit
	}
	
	@Transactional // DB 연동 작업 전체가 성공해야 커밋이 된다.
	public User findByUser(String username) {
		
		User user = userRepository.findByUsername(username).orElseGet(() -> {
			return new User();
		});
		return user; // 가입 정보가 없는 경우 null return
	}
	
//	@Transactional(readOnly = true) // select할 때 트랜젝션 시작, 서비스 종료시에 트랜잭션 종료(정합성 유지)
//	public User userLogin(User user) {
//		return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
//	}
}
