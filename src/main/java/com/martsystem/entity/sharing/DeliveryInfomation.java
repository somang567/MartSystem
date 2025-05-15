package com.martsystem.entity.sharing;

import jakarta.persistence.*;

@Entity
public class DeliveryInfomation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long deliveryId;

	// 수령장소 ( 주소 )
	@Column(nullable = false)
	private String address;

	// 수령인 전화번호
	@Column(nullable = false)
	private String contactNumber;

	// 수령인 이름
	@Column(nullable = false)
	private String receiverName;
}
