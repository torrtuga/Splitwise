package com.billsharing.splitwise.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
	
	private String id;
	private String userId;
	private String title;
	private String description;
	private LocalDateTime expenseDate;
	private ExpenseStatus expenseStatus;
	private double expenseAmount;
	private ExpenseGroup expenseGroup;

}
