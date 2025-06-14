package com.martsystem.repository;

import com.martsystem.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Long> {
	Optional<User> findByEmail(String email);
	// 회원가입 중복체크
	boolean existsByEmail(String email);
	// 닉네임 중복체크
	boolean existsByNickname(String nickname);

}
