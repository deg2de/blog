package com.myblog.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity // User 클래스가 MySQL에 테이블이 생성이 된다.
// @DynamicInsert // insert시 값이 null인 필드는 제외한다.
public class User {
	
	@Id // 주키라는것을 인식 primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에 연결된 DB 넘버링 전략을 따라간다.
	private int id; // auto_increment
	
	@Column(nullable = false, length = 100, unique = true) // null금지, 길이 100, 중복데이터x
	private String username; // id
	
	@Column(nullable = false, length = 100) // null금지, 길이 100 (비밀번호 해쉬로 암호화를 하기 위해 길이 넉넉히)
	private String password;
	
	@Column(nullable = false, length = 50) // null금지, 길이 50
	private String email;
	
	// @ColumnDefault("'user'") // db default시 기본값
	@Enumerated(EnumType.STRING) // 해당 Enum은 String타입이다.
	private RoleType role; // 정확히는 Enum을 쓰는게 좋다. (데이터 오타 입력 방지, Enum으로 들어갈 수 있는 데이터 지정) (USER, ADMIN)
	
	private String oauth; // kakao, google... 로그인한 방식 (일반 로그인은 null)
	
	@CreationTimestamp // 시간이 자동 입력
	private Timestamp createDate;
}
