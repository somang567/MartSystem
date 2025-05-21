package com.martsystem.entity.Distributor;

import com.martsystem.entity.producer.Product;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Entity
public class Label {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long labelId;

	// 상품 정보
	@ManyToOne(fetch =  FetchType.LAZY)
	@JoinColumn(name = "productId" , nullable = false)
	private Product product;

	// 라벨지 정보 코드
	@Column(nullable = false)
	private String labelCode;

	// 라벨에 달린 내용을 추가할 것.
	@Column(name = "label_content", nullable = false)
	private String labelContent;

	@Enumerated(EnumType.STRING)
	@Column(name = "alertStatus" , nullable = false)
	private Boolean alertStatus;

	//
	@Column(name = "total_revenue")
	private BigDecimal total_revenue;


	@Column(nullable = false)
	private LocalDateTime createdAt;


}
