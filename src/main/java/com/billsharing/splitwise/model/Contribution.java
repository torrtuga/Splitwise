package com.billsharing.splitwise.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contribution {
	
	private String contributionId;
	private double contributionVal;
	private String transactionId;
	private LocalDateTime contributionDate;
	private String transactionDescription;

}
