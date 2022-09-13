package com.myblog.blog.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HttpControllerTest {
	
	private static final String TAG = "HttpControllerTest : ";
	
	// lombok test
	@GetMapping("/http/lombok")
	public String lombokTest() {
//		Member m = new Member(1, "ssar", "1234", "email");
		// lombok의 @builder 사용시 아래와 같이 설정한다.
		Member m = Member.builder().username("ssar").password("234").email("email").build();
		
		System.out.println(TAG + "getter : " + m.getId());
		m.setId(5000);
		System.out.println(TAG + "getter : " + m.getId());
		
		return "lombok test 완료";
	}
	
	// http://localhost:8080/http/get
	// select
//	@GetMapping("/http/get")
//	public String getTest(@RequestParam int id, @RequestParam String username) {
//		return "get 요청 : " + id + ", " + username;
//	}
	// http://localhost:8080/http/get?id=1&username=ssal&password=1234&email=ssar@nate.com
	@GetMapping("/http/get")
	public String getTest(Member m) {
		return "get 요청 : " + m.getId() + ", " + m.getUsername() + ", " + m.getPassword() + ", " + m.getEmail();
	}
	
	// http://localhost:8080/http/post
	// insert
	// MessageConverter가 메세지 요청 방식을 구분한다.
	// x-www-form-unlencoded 방식
//	@PostMapping("/http/post")
//	public String postTest(Member m) {
//		return "post 요청 : " + m.getId() + ", " + m.getUsername() + ", " + m.getPassword() + ", " + m.getEmail();
//	}
	// raw 방식 (text/plain : 요청 -> 안녕)
//	@PostMapping("/http/post")
//	public String postTest(@RequestBody String text) {
//		return "post 요청 : " + text;
//	}
	// raw 방식 (application/json : 요청 -> json데이터 형식)
	// ex {"id":1, "username":"ssar", "passowrd":123456, "email":"ssar@nate.com"}
	@PostMapping("/http/post")
	public String postTest(@RequestBody Member m) {
		return "post 요청 : " + m.getId() + ", " + m.getUsername() + ", " + m.getPassword() + ", " + m.getEmail();
	}
	
	// http://localhost:8080/http/put
	// update
	@PutMapping("/http/put")
	public String putTest(@RequestBody Member m) {
		return "put 요청 : " + m.getId() + ", " + m.getUsername() + ", " + m.getPassword() + ", " + m.getEmail();
	}
	
	// http://localhost:8080/http/delete
	// delete
	@DeleteMapping("/http/delete")
	public String deleteTest() {
		return "delete 요청";
	}
}
