package com.martsystem.entity.user;

import com.martsystem.constant.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users") // 상속 전략을 JOINED로 설정합니다.
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor // Lombok 어노테이션
public abstract class User { // 'abstract' 키워드로 추상 클래스임을 명시

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "email", nullable = false , unique = true, length = 100)
	private String email;

	@Column(name = "password", nullable = false, length = 255)
	private String password;

	@Column(name = "password_question", length = 255)
	private String passwordQuestion; // 비밀번호 찾기 질문

	@Column(name = "password_answer", length = 255)
	private String passwordAnswer; // 비밀번호 찾기 답변


	@Column(name = "nickname", nullable = false , unique = true, length = 50)
	private String nickname;

	@Column(name = "phone_number", nullable = false, length = 20)
	private String phoneNumber;

	@Column(name = "contact_name", nullable = false, length = 100)
	private String contactPersonName; // 담당자 이름. DTO의 contactPersonName과 매핑될 수 있도록 ModelMapper 설정 필요 또는 필드명 통일

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private UserRole role;

	@Column(name = "agreed_to_terms", nullable = false)
	private Boolean agreedToTerms;

	// 필드명 변경: agree_to_privacy -> agreed_to_privacy
	@Column(name = "agreed_to_privacy", nullable = false) // DTO와 일치하도록 필드명 변경
	private Boolean agreedToPrivacy;

	@Column(name = "agree_to_marketing")
	private Boolean agreedToMarketing;

	@Column(name = "terms_agreed_at")
	private LocalDateTime termsAgreedAt;

	@Column(name = "privacy_agreed_at")
	private LocalDateTime privacyAgreedAt;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		if (Boolean.TRUE.equals(this.agreedToTerms) && this.termsAgreedAt == null) {
			this.termsAgreedAt = LocalDateTime.now();
		}
		// 필드명 변경 반영
		if (Boolean.TRUE.equals(this.agreedToPrivacy) && this.privacyAgreedAt == null) {
			this.privacyAgreedAt = LocalDateTime.now();
		}
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
		if (Boolean.TRUE.equals(this.agreedToTerms) && this.termsAgreedAt == null) {
			this.termsAgreedAt = LocalDateTime.now();
		}
		// 필드명 변경 반영
		if (Boolean.TRUE.equals(this.agreedToPrivacy) && this.privacyAgreedAt == null) {
			this.privacyAgreedAt = LocalDateTime.now();
		}
	}
}