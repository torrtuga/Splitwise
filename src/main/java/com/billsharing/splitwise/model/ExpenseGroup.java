package com.billsharing.splitwise.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ExpenseGroup {
	
	private String expenseGroupId;
	private Set<User> groupMembers = new HashSet<>();
	@Setter
	private Map<String, UserShare> userContributions;
	
	public ExpenseGroup() {
		this.expenseGroupId = UUID.randomUUID().toString();
		this.groupMembers = new HashSet<>();
		this.userContributions = new HashMap<String, UserShare>();
	}
	
	public void ExpenseGroup() {
		
	}

}
