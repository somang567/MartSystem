package com.martsystem.entity.Distributor;

import com.martsystem.constant.OrderStatus;
import com.martsystem.entity.producer.Product;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PurchaseOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long purchaseOrderId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(nullable = false)
	private int orderQuantity;

	@Column(nullable = false)
	private LocalDateTime orderDate;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus orderStatus;
}
