package com.martsystem.dto.api;

import lombok.Data;

import java.util.List;

@Data
public class NpsApiResponse {
	private int totalCount;
	private List<NpsBusinessData> items;
}