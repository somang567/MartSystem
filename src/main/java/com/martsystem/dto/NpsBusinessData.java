package com.martsystem.dto;

import lombok.Data;

@Data
public class NpsBusinessData {
	private String b_no;        // 사업자번호
	private String bplc_man_no; // 사업장관리번호
	private String bplc_nm;     // 사업장명
	private String bplc_addr;   // 사업장 주소
}