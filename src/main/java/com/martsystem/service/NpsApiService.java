package com.martsystem.service;

import com.martsystem.dto.NpsApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class NpsApiService {

	@Value("${nps.api.service-key}")
	private String serviceKey;

	@Value("${nps.api.status-url}")
	private String apiUrl;

	private final WebClient webClient;

	public NpsApiService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.build();
	}

	public NpsApiResponse getBusinessInfo(String businessNumber, String managementNumber) {
		// 국민연금 API는 GET 방식인 경우가 많으므로 쿼리 파라미터 예시
		String url = apiUrl + "?serviceKey=" + serviceKey.trim()
				+ "&b_no=" + businessNumber
				+ "&bplc_man_no=" + managementNumber
				+ "&_type=json"; // 필요 시 타입 지정

		Mono<NpsApiResponse> responseMono = webClient.get()
				.uri(url)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(NpsApiResponse.class);

		try {
			return responseMono.block();
		} catch (Exception e) {
			if (e instanceof WebClientResponseException wcException) {
				System.err.println("국민연금 API 호출 중 HTTP 오류 발생:");
				System.err.println("  Status Code: " + wcException.getStatusCode());
				System.err.println("  Status Text: " + wcException.getStatusText());
				System.err.println("  Request URL: " + wcException.getRequest().getURI());
				System.err.println("  Response Body: " + wcException.getResponseBodyAsString());
			} else {
				System.err.println("국민연금 API 호출 중 일반 오류 발생: " + e.getMessage());
			}
			return null;
		}
	}
}
