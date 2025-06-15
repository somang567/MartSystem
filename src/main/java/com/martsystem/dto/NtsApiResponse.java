package com.martsystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class NtsApiResponse {
	private int request_cnt;
	private int match_cnt;
	private List<BusinessStatusData> data;
}