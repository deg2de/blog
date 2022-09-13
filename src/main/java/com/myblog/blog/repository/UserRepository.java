package com.myblog.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.myblog.blog.model.User;

// User테이블을 관리하는 Repository, 프라이머리키는 Integer
// CRUD 기본은 작성하지 않아도 된다.
// DAO기능, 자동으로 bean등록이 된다.
@Repository // 생략 가능
public interface UserRepository extends JpaRepository<User, Integer>{
	
	// SELECT * FORM user WHERE username = ?1;
	Optional<User> findByUsername(String username);
	
	// SELECT * FORM user WHERE username = ?1 AND password = ?2
//	User findByUsernameAndPassword(String username, String password);
	
	// findByUsernameAndPassword를 @Query를 사용해서 나타낼 수 있다.
//	@Query(value="SELECT * FORM user WHERE username = ?1 AND password = ?2", nativeQuery = true)
//	User login(String username, String password);
}
