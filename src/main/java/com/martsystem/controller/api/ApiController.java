package com.martsystem.controller.api;

import com.martsystem.dto.api.NtsApiResponse;
// NpsApiResponse와 NpsApiService는 이전 요청에 따라 삭제된 것으로 가정하고 제외합니다.
// 만약 NPS 관련 기능이 필요하다면 다시 포함해야 합니다.
// import com.martsystem.dto.api.NpsApiResponse;
// import com.martsystem.service.api.NpsApiService;

import com.martsystem.service.UserService; // UserService 임포트 추가

import com.martsystem.service.api.NtsApiService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Collections; // Collections.singletonMap 사용을 위해 임포트

@RestController
@RequestMapping("/api")
public class ApiController {

	private static final Logger log = LoggerFactory.getLogger(ApiController.class);

	private final NtsApiService ntsApiService;
	// private final NpsApiService npsApiService; // NPS 기능이 삭제되었다면 이 줄은 없음
	private final UserService userService; // UserService 필드 추가

	// 생성자 수정 (NPS 기능 유무에 따라):
	// NPS 기능이 있다면: public ApiController(NtsApiService ntsApiService, NpsApiService npsApiService, UserService userService)
	// NPS 기능이 없다면:
	public ApiController(NtsApiService ntsApiService, UserService userService) {
		this.ntsApiService = ntsApiService;
		this.userService = userService;
	}

	// 국세청 사업자등록번호 요청 DTO
	@Getter
	@Setter
	@NoArgsConstructor
	public static class BusinessNumberRequest {
		public List<String> businessNumbers;
	}

	// NpsRequest DTO는 NPS 기능 삭제에 따라 제거됨 (이전에 제거 요청 있었음)


	// 이메일 중복 확인 요청 DTO
	@Getter
	@Setter
	@NoArgsConstructor
	public static class EmailDuplicationRequest {
		private String email;
	}


	@PostMapping("/checkBusinessStatus")
	public ResponseEntity<?> checkBusinessStatus(@RequestBody BusinessNumberRequest request) {
		log.info("Received /api/checkBusinessStatus request. businessNumbers: {}", request.businessNumbers);

		if (request.businessNumbers == null || request.businessNumbers.isEmpty()) {
			// BAD_REQUEST 응답을 JSON 형식으로 통일
			return new ResponseEntity<>(Collections.singletonMap("message", "사업자등록번호가 필요합니다."), HttpStatus.BAD_REQUEST);
		}

		NtsApiResponse response = ntsApiService.checkBusinessStatus(request.businessNumbers);
		log.info("NtsApiService response: {}", response);

		if (response != null && response.getData() != null && !response.getData().isEmpty()) {
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else if (response != null && response.getMatch_cnt() == 0) {
			log.warn("No business info found for numbers: {}", request.businessNumbers);
			// NOT_FOUND 응답을 JSON 형식으로 통일
			return new ResponseEntity<>(Collections.singletonMap("message", "조회된 사업자 정보가 없습니다."), HttpStatus.NOT_FOUND);
		} else {
			log.error("Error during NtsApiService call for numbers: {}", request.businessNumbers);
			// INTERNAL_SERVER_ERROR 응답을 JSON 형식으로 통일
			return new ResponseEntity<>(Collections.singletonMap("message", "사업자 정보 조회 중 오류가 발생했습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @PostMapping("/getNpsBusinessInfo") 엔드포인트는 NPS 기능 삭제에 따라 제거됨으로 간주


	// 이메일 중복 확인 엔드포인트 (기존 코드와 동일)
	@PostMapping("/checkEmailDuplication")
	public ResponseEntity<?> checkEmailDuplication(@RequestBody EmailDuplicationRequest request) {
		log.info("Received /api/checkEmailDuplication request for email: {}", request.getEmail());

		if (request.getEmail() == null || request.getEmail().isEmpty()) {
			log.warn("Email parameter is missing or empty in duplication check request.");
			// BAD_REQUEST 응답을 JSON 형식으로 통일
			return new ResponseEntity<>(Collections.singletonMap("message", "이메일 주소가 필요합니다."), HttpStatus.BAD_REQUEST);
		}

		boolean isDuplicated = userService.checkEmailDuplication(request.getEmail());

		return new ResponseEntity<>(Collections.singletonMap("duplicated", isDuplicated), HttpStatus.OK);
	}
}