package com.martsystem.service;

import com.martsystem.dto.NtsApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class NtsApiService {

	@Value("${nts.api.service-key}")
	private String serviceKey;

	@Value("${nts.api.status-url}")
	private String apiUrl;

	private final WebClient webClient;

	public NtsApiService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.build();
	}

	public NtsApiResponse checkBusinessStatus(List<String> businessNumbers) {
		// 요청 바디 구성
		Map<String, List<String>> requestBody = Map.of("b_no", businessNumbers);

		// URL 인코딩 없이 그대로 사용 (이미 인코딩된 형태로 제공됨)
		String finalRequestUrl = apiUrl + "?serviceKey=" + serviceKey.trim();

		Mono<NtsApiResponse> responseMono = webClient.post()
				.uri(finalRequestUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(requestBody)
				.retrieve()
				.bodyToMono(NtsApiResponse.class);

		try {
			return responseMono.block(); // 동기 방식으로 결과 받기
		} catch (Exception e) {
			if (e instanceof WebClientResponseException wcException) {
				System.err.println("국세청 API 호출 중 HTTP 오류 발생:");
				System.err.println("  Status Code: " + wcException.getStatusCode());
				System.err.println("  Status Text: " + wcException.getStatusText());
				System.err.println("  Request URL: " + wcException.getRequest().getURI());
				System.err.println("  Response Body: " + wcException.getResponseBodyAsString());
			} else {
				System.err.println("국세청 API 호출 중 일반 오류 발생: " + e.getMessage());
			}
			return null;
		}
	}
}
