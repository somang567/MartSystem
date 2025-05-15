package com.martsystem.entity.user;

import com.martsystem.constant.UserRole;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Users {


	// 유저번호
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(nullable = false , unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false , unique = true)
	private String nickname;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;

	private LocalDateTime createdAt;

}
