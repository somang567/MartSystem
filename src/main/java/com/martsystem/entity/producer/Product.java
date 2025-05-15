package com.martsystem.entity.producer;

import com.martsystem.constant.ProductStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;

	@Column(nullable = false , name = "productName")
	private String productName;

	// 상품 설명
	@Column(name = "description")
	private String productDescription;

	// 상품 가격
	@Column(nullable = false , name = "price")
	private BigDecimal productPrice;

	// 상품 상태
	@Enumerated(EnumType.STRING)
	@Column(nullable = false , name = "status")
	private ProductStatus status;

	// 상품 원산지
	@Column(name = "Product_origin")
	private String Origin;

	// 유통기한
	@Column(name = "expirationDate")
	private LocalDate expirationDate;

	// 제조일자 ( or 소비기한 )
	@Column(name = "manufactureDate")
	private LocalDate manufactureDate;

	// 상품 수량
	@Column(nullable = false , name = "Quantity")
	private Integer productQuantity;

}
