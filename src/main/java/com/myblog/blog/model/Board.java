package com.myblog.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false, length = 100)
	private String title; // 제목
	
	@Lob // 대용량 데이터
	private String content; // 섬머노트 라이브러리 : <html>태그가 섞여서 디자인이 됨. 용량이 커짐
	
	// @ColumnDefault("0")
	private int count; // 조회수
	
	// EAGER : 기본전략으로 항상 조인을 해서 가져온다. (ManyToOne fetch 미작성시 기본 EAGER)
	@ManyToOne(fetch = FetchType.EAGER) // N:1 (User 1명이 여러 Board 작성이 가능하다. / Many = Board, one = User)
	@JoinColumn(name="userid") // DB에서는 userid라는 필드로 만들어진다.
	private User user; // 작성 아이디 (DB는 오브젝트를 저장할 수 없다. 자바는 오브젝트를 저장할 수 있다.)
	
	// LAZY : 기본전략으로 필요에 의할 때, 셀렉트를 해서 가져온다. (OneToMany fetch 미작성시 기본 LAZY)
	// 여기서는 UI상 게시글 내용과 댓글이 동시에 보여야 하므로 EAGER전략을 쓴다.
	// mappedBy : 연관관계의 주인이 아니다. (FK가 아님. DB에 컬럼을 만들지 않는다, join문으로 값만 얻어올때 쓴다.)
	// cascade = CascadeType.REMOVE : board 게시글을 지울때 연관 댓글들을 다 지운다.
	@OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"board"})
	@OrderBy("id desc")
	private List<Reply> replys;
	
	@CreationTimestamp
	private Timestamp createDate;
}
