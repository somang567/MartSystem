package com.martsystem.entity.producer;

import com.martsystem.constant.OrderStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ProductOrderList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId" , nullable = false)
	private Product product;

	@Column(nullable = false)
	private int orderQuantity;

	@Column(nullable = false)
	private LocalDateTime orderDate;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus orderStatus;

}
