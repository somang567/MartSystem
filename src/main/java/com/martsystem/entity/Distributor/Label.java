package com.martsystem.entity.Distributor;

import com.martsystem.entity.producer.Product;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Label {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long labelId;

	@ManyToOne(fetch =  FetchType.LAZY)
	@JoinColumn(name = "productId" , nullable = false)
	private Product product;

	@Column(nullable = false)
	private String labelCode;

	@Column(nullable = false)
	private LocalDateTime createdAt;


}
