package com.billsharing.splitwise.model;

import java.util.UUID;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class User {
	
	private String name;
	private String userId;
	private String emailId;
	private String phoneNumber;
	
	public User(@NonNull String emailId, String name, String phoneNumber) {
		userId = UUID.randomUUID().toString();
		this.emailId = emailId;
		this.name = name;
		this.phoneNumber = phoneNumber;
	}

}
