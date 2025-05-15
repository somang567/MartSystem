package com.martsystem.entity.mart;

import com.martsystem.entity.producer.Product;
import jakarta.persistence.*;

@Entity
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cartId;

	@Column(nullable = false)
	private Long userId;  // 마트 유저의 ID

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId", nullable = false)
	private Product product;

	@Column(nullable = false)
	private int quantity;  // 장바구니에 담은 상품 수량
}
