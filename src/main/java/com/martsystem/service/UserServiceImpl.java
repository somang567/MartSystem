package com.martsystem.service;

// DTO
import com.martsystem.dto.users.DistributorRegisterDTO;
import com.martsystem.dto.users.MartRegisterDTO;
import com.martsystem.dto.users.ProducerRegisterDTO;
import com.martsystem.dto.users.UserRegisterDTO;

// 엔티티
import com.martsystem.entity.user.Distributor;
import com.martsystem.entity.user.Mart;
import com.martsystem.entity.user.Producer;
import com.martsystem.entity.user.User;


// 레포지토리
import com.martsystem.repository.DistributorRepository;
import com.martsystem.repository.MartRepository;
import com.martsystem.repository.ProducerRepository;
import com.martsystem.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final ProducerRepository producerRepository;
	private final MartRepository martRepository;
	private final DistributorRepository distributorRepository; // Typo 수정: DistributorRepositoy -> DistributorRepository
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;

	public UserServiceImpl(UserRepository userRepository,
	                       ProducerRepository producerRepository,
	                       MartRepository martRepository,
	                       DistributorRepository distributorRepository, // Typo 수정
	                       PasswordEncoder passwordEncoder,
	                       ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.producerRepository = producerRepository;
		this.martRepository = martRepository;
		this.distributorRepository = distributorRepository;
		this.passwordEncoder = passwordEncoder;
		this.modelMapper = modelMapper;
	}

	// 회원가입 개인정보 페이지 중복확인 throws 예외처리.
	@Override
	public User registerUser(UserRegisterDTO dto) {
		if (checkEmailDuplication(dto.getEmail())) {
			throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
		}
		if (checkNicknameDuplication(dto.getNickname())) {
			throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
		}
		if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
			throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
		}
		if (Boolean.FALSE.equals(dto.getAgreedToTerms())) {
			throw new IllegalArgumentException("서비스 이용 약관에 동의해야 합니다.");
		}
		if (Boolean.FALSE.equals(dto.getAgreedToPrivacy())) {
			throw new IllegalArgumentException("개인정보 처리 방침에 동의해야 합니다.");
		}

		// 비밀번호 암호화
		String encodedPassword = passwordEncoder.encode(dto.getPassword());

		User newUser;
		switch (dto.getRole()) {
			case PRODUCER:
				if (!(dto instanceof ProducerRegisterDTO producerDto)) {
					throw new IllegalArgumentException("생산자 회원가입에 필요한 정보가 부족합니다.");
				}
				Producer producer = modelMapper.map(producerDto, Producer.class);
				producer.setPassword(encodedPassword);
				if (checkBusinessRegistrationNumberDuplication(producerDto.getBusinessRegistrationNumber())) {
					throw new IllegalArgumentException("이미 등록된 사업자등록번호입니다.");
				}
				newUser = producer;
				break;

			case MART:
				if (!(dto instanceof MartRegisterDTO martDto)) {
					throw new IllegalArgumentException("마트 소유주 회원가입에 필요한 정보가 부족합니다.");
				}
				Mart martOwner = modelMapper.map(martDto, Mart.class);
				martOwner.setPassword(encodedPassword);
				if (checkBusinessRegistrationNumberDuplication(martDto.getBusinessRegistrationNumber())) {
					throw new IllegalArgumentException("이미 등록된 사업자등록번호입니다.");
				}
				newUser = martOwner;
				break;

			case DISTRIBUTOR:
				if (!(dto instanceof DistributorRegisterDTO distributorDto)) {
					throw new IllegalArgumentException("유통업체 회원가입에 필요한 정보가 부족합니다.");
				}
				Distributor distributor = modelMapper.map(distributorDto, Distributor.class);
				distributor.setPassword(encodedPassword);
				if (checkBusinessRegistrationNumberDuplication(distributorDto.getBusinessRegistrationNumber())) {
					throw new IllegalArgumentException("이미 등록된 사업자등록번호입니다.");
				}
				newUser = distributor;
				break;

			default:
				throw new IllegalArgumentException("지원하지 않는 회원 유형입니다.");
		}

		return userRepository.save(newUser);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> authenticate(String email, String rawPassword) {
		Optional<User> userOptional = userRepository.findByEmail(email);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			if (passwordEncoder.matches(rawPassword, user.getPassword())) {
				return Optional.of(user);
			}
		}
		return Optional.empty();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> findUserById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean checkEmailDuplication(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean checkNicknameDuplication(String nickname) {
		return userRepository.existsByNickname(nickname);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean checkBusinessRegistrationNumberDuplication(String businessRegistrationNumber) {
		return producerRepository.existsByBusinessRegistrationNumber(businessRegistrationNumber) ||
				martRepository.existsByBusinessRegistrationNumber(businessRegistrationNumber) ||
				distributorRepository.existsByBusinessRegistrationNumber(businessRegistrationNumber);
	}
}
