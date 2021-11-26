package com.billsharing.splitwise.service;

import com.billsharing.splitwise.model.Expense;
import com.billsharing.splitwise.model.User;

public interface NotificationService {
	void notifyUser(User user, Expense expense);
}
