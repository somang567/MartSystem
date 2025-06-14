package com.martsystem.service;

import com.martsystem.dto.users.UserRegisterDTO;
import com.martsystem.entity.user.User;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface UserService {
	// 회원가입 메서드: 공통 DTO를 받아 User 엔티티 반환
	User registerUser(UserRegisterDTO dto);

	// 사용자 인증(로그인) 메서드
	Optional<User> authenticate(String email, String rawPassword);

	// 이메일로 사용자 조회
	Optional<User> findUserByEmail(String email);

	// ID로 사용자 조회
	Optional<User> findUserById(Long id);

	// 이메일 중복 확인 (회원가입 시 사용)
	boolean checkEmailDuplication(String email);

	// 닉네임 중복 확인 (회원가입 시 사용)
	boolean checkNicknameDuplication(String nickname);

	// 사업자등록번호 중복 확인 (사업자 회원 가입 시 사용)
	boolean checkBusinessRegistrationNumberDuplication(String businessRegistrationNumber);

}
