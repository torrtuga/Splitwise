package com.billsharing.splitwise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.billsharing.splitwise.exceptions.ExpenseDoesNotExistsException;
import com.billsharing.splitwise.model.Expense;
import com.billsharing.splitwise.model.ExpenseStatus;
import com.billsharing.splitwise.model.User;
import com.billsharing.splitwise.repository.ExpenseRepository;
import com.billsharing.splitwise.service.ExpenseService;
import com.billsharing.splitwise.service.UserService;

public class ExpenseServiveTest {
	
	static UserService userService;
    static ExpenseService expenseService;
    
    @BeforeAll
    public static void initClass() {
        userService = new UserService();
        expenseService = new ExpenseService();
    }
    
    @Test
    @DisplayName("Create expense test")
    public void createUserTest() {
        User user = userService.createUser("bagesh@gmail.com", "bagesh", "3486199635");
        assertNotNull(user);
        assertEquals("bagesh@gmail.com", user.getEmailId());
        Expense expense = expenseService.createExpense("Team Lunch", "Friday 19Th June Lunch in Briyani zone"
                , LocalDateTime.of(2020, Month.JUNE, 19, 12, 0), 2000.00, user.getEmailId());

        assertNotNull(expense);
        assertEquals(2000, expense.getExpenseAmount());
    }
    
    
    @Test
    @DisplayName("Add user to test")
    public void addUserToExpenseTest() throws ExpenseDoesNotExistsException{
        User user = userService.createUser("bagesh@gmail.com", "bagesh", "3486199635");
        Expense expense = expenseService.createExpense("Team Lunch", "Friday 19Th June Lunch in Briyani zone"
                , LocalDateTime.of(2020, Month.JUNE, 19, 12, 0), 2000.00, user.getEmailId());
        expenseService.addUsersToExpense(expense.getId(),  "bagesh@gmail.com");


        assertNotNull(expense.getExpenseGroup().getGroupMembers());
        assertEquals(1, expense.getExpenseGroup().getGroupMembers().size());
    }
    
    
    @Test
    @DisplayName("Add user to test")
    public void assignExpenseShareTest() {
        User user = userService.createUser("bagesh@gmail.com", "bagesh", "3486199635");
        Expense expense = expenseService.createExpense("Team Lunch", "Friday 19Th June Lunch in Briyani zone"
                , LocalDateTime.of(2020, Month.JUNE, 19, 12, 0), 2000.00, user.getEmailId());
        try {
            expenseService.addUsersToExpense(expense.getId(), "bagesh@gmail.com");
            expenseService.assignExpenseShare(expense.getId(),
                    ExpenseRepository.expenseMap.get(expense.getId()).getUserId(), 2000);
        } catch (ExpenseDoesNotExistsException expenseDoesNotExistsException) {
            System.out.println(expenseDoesNotExistsException.getMessage());
        }
        assertNotNull(expense.getExpenseGroup().getGroupMembers());
        assertEquals(1, expense.getExpenseGroup().getGroupMembers().size());
        assertNotNull(expense.getExpenseGroup().getExpenseGroupId());
    }
    
    
    @Test
    @DisplayName("Add user to test")
    public void setExpenseStatusTest() {
        User user = userService.createUser("bagesh@gmail.com", "bagesh", "3486199635");
        Expense expense = expenseService.createExpense("Team Lunch", "Friday 19Th June Lunch in Briyani zone"
                , LocalDateTime.of(2020, Month.JUNE, 19, 12, 0), 2000.00, user.getEmailId());
        expense.setExpenseStatus(ExpenseStatus.SETTLED);
        assertEquals(ExpenseStatus.SETTLED, expense.getExpenseStatus());
    }


}
