package com.martsystem.entity.producer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class ProductInventory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productInventoryId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId" , nullable = false)
	private Product product;

	@Column(nullable = false , name = "Quantity")
	private Integer savedProductQuantity;



}
