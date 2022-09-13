package com.myblog.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TempControllerTest {
	
	// application.yml에 mvc 미설정시
	// localhost:8000/blog/temp/home
	@GetMapping("/temp/home")
	public String tempHome() {
		System.out.println("tempHome()");
		// application.yml에 mvc 미설정시 기본값
		// 파일리턴 기본경로 : src/main/resources/static
		// 리턴명 : /home.html
		// 풀경로 : src/main/resources/static/home.html
		return "/home.html";
	}
	
	// application.yml에 mvc 경로 설정시
	@GetMapping("/temp/jsp")
	public String tempJsp() {
		// prefix : /WEB-INF/views/
	    // suffix : .jsp
		// 풀네임 : /WEB-INF/views/test.jsp
		return "test";
	}
}
