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
// 중요: 어떤 자식 타입의 엔티티가 저장되는지 구분하는 컬럼을 지정합니다.
// 이 컬럼의 값(예: "PRODUCER", "MART_OWNER")에 따라 실제 로드될 자식 엔티티 타입이 결정됩니다.
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor // Lombok 어노테이션
public abstract class User { // 'abstract' 키워드로 추상 클래스임을 명시

	// 유저 고유 번호 (Primary Key)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id") // DB 컬럼명 명시
	private Long id;

	// 회원가입 시 아이디로 사용될 이메일
	@Column(name = "email", nullable = false , unique = true, length = 100)
	private String email;

	// 로그인 비밀번호 (암호화된 비밀번호 저장)
	@Column(name = "password", nullable = false, length = 255)
	private String password;

	@Column(name = "password_question", length = 255)
	private String passwordQuestion; // 비밀번호 찾기 질문

	@Column(name = "password_answer", length = 255)
	private String passwordAnswer; // 비밀번호 찾기 답변


	// 사용자 닉네임
	@Column(name = "nickname", nullable = false , unique = true, length = 50)
	private String nickname;

	@Column(name = "phone_number", nullable = false, length = 20)
	private String phoneNumber; // 전화번호

	@Column(name = "contact_name", nullable = false, length = 100)
	private String name; // 담당자 이름

	// 사용자 권한 (예: USER, ADMIN, PRODUCER, MART_OWNER, DISTRIBUTOR)
	@Enumerated(EnumType.STRING) // Enum 값을 문자열로 DB에 저장
	@Column(name = "role", nullable = false)
	private UserRole role;

	// 서비스 이용 약관 동의 여부
	@Column(name = "agreed_to_terms", nullable = false)
	private Boolean agreedToTerms;

	// 개인정보 처리방침 동의 여부
	@Column(name = "agree_to_privacy", nullable = false)
	private Boolean agreeToPrivacy;

	// 마케팅 정보 수신 동의 여부 (선택 사항)
	@Column(name = "agree_to_marketing") // null 허용
	private Boolean agreeToMarketing;

	// 약관 동의 시점 (법적 증빙에 유용)
	@Column(name = "terms_agreed_at")
	private LocalDateTime termsAgreedAt;

	// 개인정보 처리방침 동의 시점 (법적 증빙에 유용)
	@Column(name = "privacy_agreed_at")
	private LocalDateTime privacyAgreedAt;

	// 유저 생성 날짜 (계정 생성 시점)
	@Column(name = "created_at", nullable = false, updatable = false) // 생성 시간, 필수, 수정 불가
	private LocalDateTime createdAt;

	// 유저 정보 마지막 수정 날짜 (정보 업데이트 시점)
	@Column(name = "updated_at") // 수정 시간
	private LocalDateTime updatedAt;

	// --- 엔티티 라이프사이클 콜백 (생성/수정 시간 자동화 및 약관 동의 시점 기록) ---
	@PrePersist // 엔티티가 데이터베이스에 저장되기 전에 호출됩니다.
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		// 약관이 동의되었고, 동의 시점이 아직 설정되지 않았을 때만 기록
		if (Boolean.TRUE.equals(this.agreedToTerms) && this.termsAgreedAt == null) {
			this.termsAgreedAt = LocalDateTime.now();
		}
		if (Boolean.TRUE.equals(this.agreeToPrivacy) && this.privacyAgreedAt == null) {
			this.privacyAgreedAt = LocalDateTime.now();
		}
	}

	@PreUpdate // 엔티티가 데이터베이스에서 업데이트되기 전에 호출됩니다.
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
		// 약관 동의 여부가 나중에 true로 변경되었고, 동의 시점이 아직 설정되지 않았을 때 기록
		if (Boolean.TRUE.equals(this.agreedToTerms) && this.termsAgreedAt == null) {
			this.termsAgreedAt = LocalDateTime.now();
		}
		if (Boolean.TRUE.equals(this.agreeToPrivacy) && this.privacyAgreedAt == null) {
			this.privacyAgreedAt = LocalDateTime.now();
		}
	}
}