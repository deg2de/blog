package com.myblog.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myblog.blog.model.KakaoProfile;
import com.myblog.blog.model.OAuthToken;
import com.myblog.blog.model.User;
import com.myblog.blog.service.UserService;

// /auth경로 : 인증이 안된 사용자들이 출입할 수 있는 경로
// 그냥 주소가 /이면 index.jsp 허용
// static 이하에 있는 리소스 js, css, image 허용

@Controller
public class UserController {
	
	@Value("${blog.key}")
	private String blogKey;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@GetMapping("/auth/joinForm")
	public String joinForm() {
		return "user/joinForm";
	}
	
	@GetMapping("/auth/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}
	
	@GetMapping("/auth/kakao/callback")
	public String kakaoCallback(String code) { // 반환값에 @ResponseBody 설정시 : 데이터를 리턴해주는 함수로 설정
		
		// 인증 토큰 받기
		// post방식으로 key=value 타입의 데이터를 요청(카카오쪽으로)
		// 다른 방식으로는 Retrofit2(주로 안드로이드에서 사용), OkHttp등이 있다.
		RestTemplate rt = new RestTemplate();
		// HttpHeader 객체 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		// HttpBody (body데이터 담을 객체)
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "a2ee59d6e922aa5fe553bb4bacacadff");
		params.add("redirect_uri", "http://localhost:8000/auth/kakao/callback");
		params.add("code", code);
		
		// body와 header값을 가지고 있는 entity (HttpHeader와 HttpBody를 하나의 객체에 담기)
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
		
		// Http 요청하기
		ResponseEntity<String> response = rt.exchange(
				"https://kauth.kakao.com/oauth/token", // 전송 주소
				HttpMethod.POST, // 전송 방식
				kakaoTokenRequest, // 헤더, 바디 데이터
				String.class // 응답 받을 데이터 변수
		);
		
		// Gson, Json Simple, ObjectMapper
		// 자바에서 처리하기 위해 json데이터를 자바 오브젝트로 저장
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;
		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) { // 데이터의 이름이 틀린 경우
			e.printStackTrace();
		} catch (JsonProcessingException e) { // get, set 오류시
			e.printStackTrace();
		}
		
		System.out.println("카카오 엑세스 토큰 : " + oauthToken.getAccess_token());
		
		// 토큰을 통해 사용자 정보 가져오기
		// 인증 토큰 받기
		// post방식으로 key=value 타입의 데이터를 요청(카카오쪽으로)
		// 다른 방식으로는 Retrofit2(주로 안드로이드에서 사용), OkHttp등이 있다.
		RestTemplate rt2 = new RestTemplate();
		// HttpHeader 객체 생성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// body와 header값을 가지고 있는 entity (HttpHeader와 HttpBody를 하나의 객체에 담기)
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>(headers2);

		// Http 요청하기
		ResponseEntity<String> response2 = rt2.exchange(
				"https://kapi.kakao.com/v2/user/me", // 전송 주소
				HttpMethod.POST, // 전송 방식
				kakaoProfileRequest2, // 헤더, 바디 데이터
				String.class // 응답 받을 데이터 변수
		);
		
		System.out.println("카카오 엑세스 토큰으로 사용자 정보 요청 : " + response2);
		
		// Gson, Json Simple, ObjectMapper
		// 자바에서 처리하기 위해 json데이터를 자바 오브젝트로 저장
		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakaoProfile = null;
		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) { // 데이터의 이름이 틀린 경우
			e.printStackTrace();
		} catch (JsonProcessingException e) { // get, set 오류시
			e.printStackTrace();
		}
		
		System.out.println("카카오 사용자 정보 : " + kakaoProfile);
		
		// User 오브젝트 : username, password, email
		System.out.println("카카오 아이디(번호) : " + kakaoProfile.getId());
		System.out.println("카카오 이메일 : " + kakaoProfile.getKakao_account().getEmail());
		
		System.out.println("블로그서버 유저네임 : " + kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId());
		System.out.println("블로그서버 이메일 : " + kakaoProfile.getKakao_account().getEmail());
		// UUID는 중복되지 않는 어떤 특정 값을 만들어내느 알고리즘으로 비밀번호를 찾을수가 없다.
		// 그러므로 특정 비밀번호로 통일해서 설정하도록 한다.
		System.out.println("블로그서버 패스워드 : " + blogKey);
		
		// 강제 회원가입 처리 (카카오로그인 정보를 토대로 사이트 회원 가입 처리)
		User kakaoUser = User.builder()
				.username(kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId())
				.password(blogKey)
				.email(kakaoProfile.getKakao_account().getEmail())
				.oauth("kakao")
				.build();
		// 가입자 혹은 비가입자 체크 처리
		User originUser = userService.findByUser(kakaoUser.getUsername());
		// 비가입중인 경우 회원가입
		if(originUser.getUsername() == null) {
			System.out.println("기존 회원이 아닙니다. 새로운 회원가입 처리");
			userService.userJoin(kakaoUser);
		}
		
		// 로그인 처리
		// 세션 강제 등록
		// username과 password로 토큰을 만들고 매니저에서 인증작업 처리 후에 Authentication을 만들어준다.
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), blogKey));
		// 세션 등록처리
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "redirect:/";
	}
	
	@GetMapping("/user/updateForm")
	public String updateForm() {
		return "user/updateForm";
	}
}
