package com.billsharing.splitwise.service;

import com.billsharing.splitwise.exceptions.ContributionExceededException;
import com.billsharing.splitwise.exceptions.ExpenseSettledException;
import com.billsharing.splitwise.exceptions.InvalidExpenseState;
import com.billsharing.splitwise.model.Contribution;
import com.billsharing.splitwise.model.Expense;
import com.billsharing.splitwise.model.ExpenseGroup;
import com.billsharing.splitwise.model.ExpenseStatus;
import com.billsharing.splitwise.model.User;
import com.billsharing.splitwise.model.UserShare;
import com.billsharing.splitwise.repository.ExpenseRepository;
import com.billsharing.splitwise.repository.UserRepository;

public class UserService {

	public User createUser(String emailId, String name, String phoneNumber) {
		User user = new User(emailId, name, phoneNumber);
		UserRepository.userHashMap.putIfAbsent(emailId, user);
		return user;
	}

	public void contributeToExpense(String expenseId, String emailId, Contribution contribution)
			throws InvalidExpenseState, ExpenseSettledException, ContributionExceededException {
		Expense expense = ExpenseRepository.expenseMap.get(expenseId);
		ExpenseGroup expenseGroup = expense.getExpenseGroup();

		if (expense.getExpenseStatus() == ExpenseStatus.CREATED) {
			throw new InvalidExpenseState("Invalid expense state");
		}
		if (expense.getExpenseStatus() == ExpenseStatus.SETTLED) {
			throw new ExpenseSettledException("Expense already settled");
		}

		UserShare userShare = expenseGroup.getUserContributions().get(emailId);
		if (contribution.getContributionVal() > userShare.getShare()) {
			throw new ContributionExceededException(String.format("User %s contribtion %d exceeded the share %d",
					emailId, contribution.getContributionVal(), userShare.getShare()));
		}
		userShare.getContributions().add(contribution);
	}

}
