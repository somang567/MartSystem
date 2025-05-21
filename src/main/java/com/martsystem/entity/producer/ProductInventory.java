package com.martsystem.entity.producer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
// 생산자 마이페이지용 엔티티
public class ProductInventory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productInventoryId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId" , nullable = false)
	private Product product;

	@Column(nullable = false , name = "savedProductQuantity")
	private Integer savedProductQuantity;

	@Column(nullable = false)
	private String Location;




}
