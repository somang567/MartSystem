package com.martsystem.entity.sharing;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class DeliveryStatusTracking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long trackingId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deliveryId" , nullable = false)
	private DeliveryInfomation delivery;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "statusId" , nullable = false)
	private DeliveryStatus status;

	@Column(nullable = false)
	private LocalDateTime timestamp;

}
