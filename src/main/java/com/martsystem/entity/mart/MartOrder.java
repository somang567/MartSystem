package com.martsystem.entity.mart;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.martsystem.entity.producer.Product;
import com.martsystem.constant.OrderStatus;

@Entity
public class MartOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@Column(nullable = false)
	private Long userId;  // 마트 유저의 ID

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId", nullable = false)
	private Product product;

	@Column(nullable = false)
	private int orderQuantity;

	@Column(nullable = false)
	private LocalDateTime orderDate;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus orderStatus;  // 주문 상태
}
