package com.billsharing.splitwise.service;

import com.billsharing.splitwise.model.Expense;
import com.billsharing.splitwise.model.User;

public class NotificationServiceImpl implements NotificationService {

	@Override
	public void notifyUser(User user, Expense expense) {
		System.out.println("User notified");
	}

}
