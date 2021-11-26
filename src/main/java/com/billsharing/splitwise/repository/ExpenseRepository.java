package com.billsharing.splitwise.repository;

import java.util.HashMap;
import java.util.Map;

import com.billsharing.splitwise.model.Expense;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseRepository {
	public static Map<String, Expense> expenseMap = new HashMap<>();
}
