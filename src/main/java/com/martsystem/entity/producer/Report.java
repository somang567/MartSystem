package com.martsystem.entity.producer;

import com.martsystem.constant.ReportType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Report {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reportId;

	@Enumerated(EnumType.STRING)
	private ReportType reportType;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "total_sales", nullable = false)
	private BigDecimal totalSales;

	@Column(name = "total_revenue" , nullable = false)
	private BigDecimal totalRevenue;

}
