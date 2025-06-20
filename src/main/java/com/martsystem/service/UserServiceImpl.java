package com.martsystem.service;

import com.martsystem.constant.UserRole; // UserRole 임포트
import com.martsystem.dto.users.UserRegisterDTO;
import com.martsystem.entity.user.Distributor;
import com.martsystem.entity.user.Mart;
import com.martsystem.entity.user.Producer;
import com.martsystem.entity.user.User;
import com.martsystem.repository.DistributorRepository;
import com.martsystem.repository.MartRepository;
import com.martsystem.repository.ProducerRepository;
import com.martsystem.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserRepository userRepository;
	private final ProducerRepository producerRepository;
	private final MartRepository martRepository;
	private final DistributorRepository distributorRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;

	public UserServiceImpl(UserRepository userRepository,
	                       ProducerRepository producerRepository,
	                       MartRepository martRepository,
	                       DistributorRepository distributorRepository,
	                       PasswordEncoder passwordEncoder,
	                       ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.producerRepository = producerRepository;
		this.martRepository = martRepository;
		this.distributorRepository = distributorRepository;
		this.passwordEncoder = passwordEncoder;
		this.modelMapper = modelMapper;
	}

	@Override
	public User registerUser(UserRegisterDTO dto) {
		log.info("Attempting to register user with DTO: {}", dto);
		log.info("User role: {}, email: {}, nickname: {}, agreedToTerms: {}, agreedToPrivacy: {}, agreedToMarketing: {}",
				dto.getRole(), dto.getEmail(), dto.getNickname(), dto.getAgreedToTerms(), dto.getAgreedToPrivacy(), dto.getAgreedToMarketing());

		// --- 여기 아래에 contactPersonName 값 로그 추가 ---
		log.info("Debugging: DTO contactPersonName received: '{}'", dto.getContactPersonName());

		// 공통 유효성 검사
		if (checkEmailDuplication(dto.getEmail())) {
			log.warn("Email duplication detected: {}", dto.getEmail());
			throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
		}
		if (checkNicknameDuplication(dto.getNickname())) {
			log.warn("Nickname duplication detected: {}", dto.getNickname());
			throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
		}
		if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
			log.warn("Password and password confirm do not match for email: {}", dto.getEmail());
			throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
		}
		if (Boolean.FALSE.equals(dto.getAgreedToTerms())) {
			log.warn("Terms not agreed for email: {}", dto.getEmail());
			throw new IllegalArgumentException("서비스 이용 약관에 동의해야 합니다.");
		}
		if (Boolean.FALSE.equals(dto.getAgreedToPrivacy())) {
			log.warn("Privacy policy not agreed for email: {}", dto.getEmail());
			throw new IllegalArgumentException("개인정보 처리 방침에 동의해야 합니다.");
		}

		String encodedPassword = passwordEncoder.encode(dto.getPassword());
		log.debug("Password encoded for email: {}", dto.getEmail());

		User newUser;

		switch (dto.getRole()) {
			case PRODUCER:
			case MART:
			case DISTRIBUTOR:
				// 사업자 유형 공통 필수 필드 검증 (이메일 중복확인 후에만 필요)
				// 만약 비사업자 회원도 사업자등록번호 필드를 받을 수 있다면, 이 검사는 해당 Role에서만 이루어져야 함.
				// 현재 HTML 구조상 businessRegistrationNumber는 userRoleType != null 일 때만 보임.
				// 즉, PRODUCER, MART, DISTRIBUTOR만 해당 필드를 가짐.
				if (dto.getBusinessRegistrationNumber() == null || dto.getBusinessRegistrationNumber().isBlank()) {
					throw new IllegalArgumentException("사업자등록번호는 필수 입력 항목입니다.");
				}
				if (checkBusinessRegistrationNumberDuplication(dto.getBusinessRegistrationNumber())) {
					log.warn("Business registration number duplication detected: {}", dto.getBusinessRegistrationNumber());
					throw new IllegalArgumentException("이미 등록된 사업자등록번호입니다.");
				}
				if (dto.getBusinessZipCode() == null || dto.getBusinessZipCode().isBlank()) {
					throw new IllegalArgumentException("사업장 우편번호는 필수입니다.");
				}
				if (dto.getBusinessRoadAddress() == null || dto.getBusinessRoadAddress().isBlank()) {
					throw new IllegalArgumentException("사업장 도로명 주소는 필수입니다.");
				}
				if (dto.getBusinessDetailAddress() == null || dto.getBusinessDetailAddress().isBlank()) {
					throw new IllegalArgumentException("사업장 상세 주소는 필수입니다.");
				}
				if (dto.getCompanyName() == null || dto.getCompanyName().isBlank()) {
					throw new IllegalArgumentException("업체명은 필수 입력 항목입니다.");
				}
				if (dto.getCeoName() == null || dto.getCeoName().isBlank()) {
					throw new IllegalArgumentException("대표자명은 필수 입력 항목입니다.");
				}
				if (dto.getContactPersonName() == null || dto.getContactPersonName().isBlank()) {
					throw new IllegalArgumentException("업무담당자명은 필수 입력 항목입니다.");
				}

				if (dto.getRole() == UserRole.PRODUCER) {
					Producer producer = modelMapper.map(dto, Producer.class);
					producer.setPassword(encodedPassword);
					producer.setContactPersonName(dto.getContactPersonName()); // 추가
					newUser = producer;
					log.info("Mapping DTO to Producer for email: {}", dto.getEmail());
				} else if (dto.getRole() == UserRole.MART) {
					Mart mart = modelMapper.map(dto, Mart.class);
					mart.setPassword(encodedPassword);
					mart.setContactPersonName(dto.getContactPersonName()); // 추가
					newUser = mart;
					log.info("Mapping DTO to Mart for email: {}", dto.getEmail());
				} else { // DISTRIBUTOR
					Distributor distributor = modelMapper.map(dto, Distributor.class);
					distributor.setPassword(encodedPassword);
					distributor.setContactPersonName(dto.getContactPersonName()); // 추가
					newUser = distributor;
					log.info("Mapping DTO to Distributor for email: {}", dto.getEmail());
				}
				break;

			default:
				log.error("Unsupported user role: {}", dto.getRole());
				throw new IllegalArgumentException("지원하지 않는 회원 유형입니다.");
		}

		log.info("Saving new user of type {}: {}", newUser.getClass().getSimpleName(), newUser.getEmail());
		return userRepository.save(newUser);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> authenticate(String email, String rawPassword) {
		log.debug("Authenticating user with email: {}", email);
		Optional<User> userOptional = userRepository.findByEmail(email);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			if (passwordEncoder.matches(rawPassword, user.getPassword())) {
				log.info("User authenticated successfully: {}", email);
				return Optional.of(user);
			} else {
				log.warn("Authentication failed: Incorrect password for email {}", email);
			}
		} else {
			log.warn("Authentication failed: User not found for email {}", email);
		}
		return Optional.empty();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> findUserByEmail(String email) {
		log.debug("Finding user by email: {}", email);
		return userRepository.findByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> findUserById(Long id) {
		log.debug("Finding user by ID: {}", id);
		return userRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean checkEmailDuplication(String email) {
		log.debug("Checking email duplication for: {}", email);
		boolean exists = userRepository.existsByEmail(email);
		log.debug("Email {} exists: {}", email, exists);
		return exists;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean checkNicknameDuplication(String nickname) {
		log.debug("Checking nickname duplication for: {}", nickname);
		boolean exists = userRepository.existsByNickname(nickname);
		log.debug("Nickname {} exists: {}", nickname, exists);
		return exists;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean checkBusinessRegistrationNumberDuplication(String businessRegistrationNumber) {
		log.debug("Checking business registration number duplication for: {}", businessRegistrationNumber);
		boolean exists = producerRepository.existsByBusinessRegistrationNumber(businessRegistrationNumber) ||
				martRepository.existsByBusinessRegistrationNumber(businessRegistrationNumber) ||
				distributorRepository.existsByBusinessRegistrationNumber(businessRegistrationNumber);
		log.debug("Business registration number {} exists: {}", businessRegistrationNumber, exists);
		return exists;
	}
}