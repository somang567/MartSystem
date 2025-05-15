package com.martsystem.entity.sharing;

import com.martsystem.constant.DeliveryStatusType;
import jakarta.persistence.*;

@Entity
public class DeliveryStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long statusId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DeliveryStatusType deliveryStatusType;



}
