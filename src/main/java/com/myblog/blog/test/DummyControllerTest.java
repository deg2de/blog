package com.myblog.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myblog.blog.model.RoleType;
import com.myblog.blog.model.User;
import com.myblog.blog.repository.UserRepository;

@RestController
public class DummyControllerTest {
	
	@Autowired
	private UserRepository userRepository;
	
	// http://localhost:8000/blog/dummy/join
	// http의 body에 username, passoword, email 데이터를 가지고 요청
	// @RequestParam사용시 파라미터 명을 컬럼명과 같이 맞추면 @RequestParam으로 컬럼명을 지정하지 않아도 된다.
	// ex) @RequestParam("username") String username, String password, String email
	// 더 편리하게 객체로 받아올 수 있다. (User 객체)
	@PostMapping("/dummy/join")
	public String join(User user) {
		
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		System.out.println(user.getEmail());
		System.out.println(user);
		
		user.setRole(RoleType.USER);
		userRepository.save(user);
		
		return "회원가입이 완료되었습니다.";
	}
	
	// {id}주소로 파라미터를 전달 받을 수 있다.
	// http://localhost:8000/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		// 데이터 습득 실패시 null이 된다.
		// 그러므로 Optional으로 객체를 감싸서 가져오니 null값은 알아서 판단한다.
		// Supplier는 값이 없을 경우 IllegalArgumentException를 리턴하도록 한다.
		
		// 일반식
//		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
//
//			@Override
//			public IllegalArgumentException get() {
//				return new IllegalArgumentException("해당 유저는 없습니다. id : " +id);
//			}
//			
//		});
		
		// 람다식
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("해당 유저는 없습니다. id : " +id);
		});
		
		// 응답시 Json변환 필요, spring에서는 Gson라이브러리로 변환했으나
		// 스프링부트는 MessageConverter가 응답시 자동으로 작동해서
		// 자바 오브젝트를 리턴하게 되면 MessageConverter의 Jackson라이브러리를 사용해서
		// 자동으로 json타입으로 변환해준다.
		return user;
	}
	
	@GetMapping("/dummy/users")
	public List<User> list() {
		return userRepository.findAll();
	}
	
	// 한 페이지당 2건의 데이터를 리턴 (페이징)
	// 2건씩, 정렬은 id로, 최신순으로
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<User> pagingUser = userRepository.findAll(pageable);
		
		List<User> users = pagingUser.getContent();
		return users;
	}
	
	// email, password 수정
	// JSON요청은 @RequestBody로 받는다.
	@Transactional // 트랜젝션을 걸면 sava를 하지 않아도 업데이트가 된다. (함수 시작시 시작, 함수 종료시 자동 commit이 된다.)
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) {
		System.out.println("id : "+id);
		System.out.println("password : "+requestUser.getPassword());
		System.out.println("email : "+requestUser.getEmail());
		
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정에 실패하였습니다.");
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
		// userRepository.save(user);
		return user;
	}
	
	
	@DeleteMapping("/dummy/user/{id}")
	public String deleteUser(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			return "삭제에 실패하였습니다. 해당 id는 존재하지 않습니다.";
		}

		return "삭제되었습니다." +id;
	}
}
