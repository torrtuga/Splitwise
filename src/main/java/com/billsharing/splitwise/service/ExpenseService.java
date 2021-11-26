package com.billsharing.splitwise.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.billsharing.splitwise.exceptions.ExpenseDoesNotExistsException;
import com.billsharing.splitwise.model.Contribution;
import com.billsharing.splitwise.model.Expense;
import com.billsharing.splitwise.model.ExpenseGroup;
import com.billsharing.splitwise.model.ExpenseStatus;
import com.billsharing.splitwise.model.UserShare;
import com.billsharing.splitwise.repository.ExpenseRepository;
import com.billsharing.splitwise.repository.UserRepository;

public class ExpenseService {
	
	private NotificationService notificationService = new NotificationServiceImpl();

	public Expense createExpense(String title, String description, LocalDateTime expenseDate, double expenseAmount,
			String userId) {

		Expense expense = Expense.builder().id(UUID.randomUUID().toString()).title(title).description(description)
				.expenseDate(expenseDate).expenseAmount(expenseAmount).userId(userId)
				.expenseStatus(ExpenseStatus.CREATED).expenseGroup(new ExpenseGroup()).build();

		ExpenseRepository.expenseMap.putIfAbsent(expense.getId(), expense);
		return expense;
	}

	public void addUsersToExpense(String expenseId, String emailId) throws ExpenseDoesNotExistsException {
		if (!ExpenseRepository.expenseMap.containsKey(expenseId)) {
			throw new ExpenseDoesNotExistsException("Exception not present");
		}
		ExpenseRepository.expenseMap.get(expenseId).getExpenseGroup().getGroupMembers()
				.add(UserRepository.userHashMap.get(emailId));
	}
	
	public void assignExpenseShare(String expenseId, String emailId, double share) throws ExpenseDoesNotExistsException  {
		if (!ExpenseRepository.expenseMap.containsKey(expenseId)) {
			throw new ExpenseDoesNotExistsException("Exception not present");
		}
		Expense expense = ExpenseRepository.expenseMap.get(expenseId);
		expense.getExpenseGroup().getUserContributions().putIfAbsent(emailId, new UserShare(emailId, share));
		if (notificationService != null) {
			notificationService.notifyUser(UserRepository.userHashMap.get(emailId), ExpenseRepository.expenseMap.get(expenseId));
		}
	}
	
	public void setExpenseStatus(String expenseId, ExpenseStatus expenseStatus) {
		Expense expense = ExpenseRepository.expenseMap.get(expenseId);
		expense.setExpenseStatus(expenseStatus);
	}
	
	public boolean isExpenseSettled(String expenseId) {
		Expense expense = ExpenseRepository.expenseMap.get(expenseId);
		ExpenseGroup expenseGroup = expense.getExpenseGroup();
		Map<String, UserShare> userContributions = expenseGroup.getUserContributions();
		
		double total = expense.getExpenseAmount();
		System.out.println("total " + total);
		
		for (Map.Entry<String, UserShare> entry : userContributions.entrySet()) {
			UserShare userShare = entry.getValue();
			for (Contribution contribution : userShare.getContributions()) {
				total -= contribution.getContributionVal();
			}
		}
		System.out.println("total now : " + total);
		if (total <= 1) {
			return true;
		}
		return false;
	}
	

}
