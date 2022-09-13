package com.myblog.blog.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

//@Getter
//@Setter
@Data // Getter, Setter
//@AllArgsConstructor // 모든 생성자
@NoArgsConstructor // 빈 생성자
//@RequiredArgsConstructor // final 붙은 변수들에 대해 생성자 생성
public class Member {
	// 데이터가 변하지 않도록 final 불변성 유지
	// 변경할 경우가 생길 경우는 final을 붙이지 않는다. (ex 비밀번호 변경)
	private int id;
	private String username;
	private String password;
	private String email;
	
	@Builder
	public Member(int id, String username, String password, String email) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	
}
