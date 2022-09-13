package com.myblog.blog.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.myblog.blog.model.User;

import lombok.Data;
import lombok.Getter;

// 스프링 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 완료가 되면
// UserDetails의 오브젝트를 스프링 시큐리티의 고유한 세션저장소에 저당한다.
// 그때 저장되는 것이 UserDetails타입의 PrincipalDetail이다.
@Getter
public class PrincipalDetail implements UserDetails{
	
	private User user; // 콤포지션
	
	public PrincipalDetail(User user) {
		this.user = user;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}
	
	// 계정이 만료되지 않았는지 리턴한다. (true:만료안됨)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	// 계정이 잠겨있지 않았는지 리턴한다. (true:잠기지 않음)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	// 비밀번호가 만료되지 않았는지 리턴한다. (true:만료안됨)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	// 계정 활성화(사용가능)인지 리턴한다. (true:활성화)
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	// 계정의 갖고있는 권한 목록을 리턴한다.
	// 권한이 여러개 있을 경우 루프를 돌아야 하는데 이 소스는 한개만 한다.
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> collectors = new ArrayList<>();
//		collectors.add(new GrantedAuthority() {
//			
//			@Override
//			public String getAuthority() {
//				return "ROLE_"+user.getRole(); // ROLE_ ~ 는 규칙이다.
//			}
//		});
		
		// 위 메소드를 람다식으로 표현할 경우.. (내부 함수가 하나밖에 없는 경우)
		collectors.add(()->{return "ROLE_"+user.getRole();});
		return collectors;
	}
}
