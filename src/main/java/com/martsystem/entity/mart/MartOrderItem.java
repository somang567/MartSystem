package com.martsystem.entity.mart;

import jakarta.persistence.*;
import com.martsystem.entity.producer.Product;

@Entity
public class MartOrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderItemId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private MartOrder martOrder;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false)
	private int price;  // 상품 가격
}
