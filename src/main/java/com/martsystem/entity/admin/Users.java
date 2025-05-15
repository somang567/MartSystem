package com.martsystem.entity.admin;

import com.martsystem.constant.UserRole;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Users {


	// 유저번호
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	// 회원가입 시 아이디로 사용될 이메일
	@Column(nullable = false , unique = true)
	private String email;

	// 로그인 비밀번호
	@Column(nullable = false)
	private String password;

	// 사용자 닉네임
	@Column(nullable = false , unique = true)
	private String nickname;

	// 권한설정을 위한 권한
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;

	// 유저 생성날짜
	@Column(nullable = false)
	private LocalDateTime createdAt;

}
