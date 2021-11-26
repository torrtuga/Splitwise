package com.billsharing.splitwise.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class UserShare {
	
	private String userId;
	private double share;
	List<Contribution> contributions;
	
	public UserShare(String userId, double share) {
		this.userId = userId;
		this.share = share;
		contributions = new ArrayList<>();
	}

}
