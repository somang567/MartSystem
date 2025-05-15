package com.martsystem.entity.sharing;

import com.martsystem.constant.NotificationType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long notificationId;

	@Column(nullable = false)
	private String message;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NotificationType notification;

	@Column(nullable = false)
	private boolean alertStatus;

	private LocalDateTime createAt;

}
