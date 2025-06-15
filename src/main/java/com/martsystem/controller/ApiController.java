package com.martsystem.controller;

import com.martsystem.dto.NpsApiResponse;
import com.martsystem.dto.NtsApiResponse;
import com.martsystem.service.NpsApiService;
import com.martsystem.service.NtsApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

	private final NtsApiService ntsApiService;
	private final NpsApiService npsApiService;

	public ApiController(NtsApiService ntsApiService, NpsApiService npsApiService) {
		this.ntsApiService = ntsApiService;
		this.npsApiService = npsApiService;
	}

	// 국세청 사업자등록번호 요청 DTO
	static class BusinessNumberRequest {
		public List<String> businessNumbers;
	}

	// 국민연금 API 요청 DTO
	static class NpsRequest {
		public String businessNumber;       // 사업자번호
		public String managementNumber;     // 사업장관리번호
	}

	@PostMapping("/checkBusinessStatus")
	public ResponseEntity<?> checkBusinessStatus(@RequestBody BusinessNumberRequest request) {
		if (request.businessNumbers == null || request.businessNumbers.isEmpty()) {
			return new ResponseEntity<>("사업자등록번호가 필요합니다.", HttpStatus.BAD_REQUEST);
		}

		NtsApiResponse response = ntsApiService.checkBusinessStatus(request.businessNumbers);

		if (response != null && response.getData() != null && !response.getData().isEmpty()) {
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else if (response != null && response.getMatch_cnt() == 0) {
			return new ResponseEntity<>("조회된 사업자 정보가 없습니다.", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>("사업자 정보 조회 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/getNpsBusinessInfo")
	public ResponseEntity<?> getNpsBusinessInfo(@RequestBody NpsRequest request) {
		if (request.businessNumber == null || request.businessNumber.isEmpty() ||
				request.managementNumber == null || request.managementNumber.isEmpty()) {
			return new ResponseEntity<>("사업자번호와 사업장관리번호가 필요합니다.", HttpStatus.BAD_REQUEST);
		}

		NpsApiResponse response = npsApiService.getBusinessInfo(request.businessNumber, request.managementNumber);

		if (response != null && response.getItems() != null && !response.getItems().isEmpty()) {
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("국민연금 사업장 정보가 없습니다.", HttpStatus.NOT_FOUND);
		}
	}
}
