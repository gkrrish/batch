package com.batch.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponse {
	private String newsPaperName;
	private String language;
	private String state;
	private String district;
	private String mandal;
	private String batchTime;
	private BigDecimal subscriptionCharges;
	private BigDecimal subscriptionChargesPerMonth;

}
