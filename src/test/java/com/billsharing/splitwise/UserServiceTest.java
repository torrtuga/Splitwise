package com.billsharing.splitwise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.billsharing.splitwise.exceptions.ContributionExceededException;
import com.billsharing.splitwise.exceptions.ExpenseDoesNotExistsException;
import com.billsharing.splitwise.exceptions.ExpenseSettledException;
import com.billsharing.splitwise.exceptions.InvalidExpenseState;
import com.billsharing.splitwise.model.Contribution;
import com.billsharing.splitwise.model.Expense;
import com.billsharing.splitwise.model.ExpenseGroup;
import com.billsharing.splitwise.model.ExpenseStatus;
import com.billsharing.splitwise.model.User;
import com.billsharing.splitwise.model.UserShare;
import com.billsharing.splitwise.repository.ExpenseRepository;
import com.billsharing.splitwise.service.ExpenseService;
import com.billsharing.splitwise.service.UserService;

public class UserServiceTest {
	
    static UserService userService;
    static ExpenseService expenseService;
    
    @BeforeAll
    public static void initClass() {
        userService = new UserService();
        expenseService = new ExpenseService();
    }
    
    @Test
    @DisplayName("Create user test")
    public void createUserTest() {
        User user = userService.createUser("bagesh@gmail.com", "bagesh", "3486199635");
        assertNotNull(user);
        assertEquals("bagesh@gmail.com", user.getEmailId());
    }
    
    @Test
    @DisplayName("Create user null email id test")
    public void createUserNullEmailTest() {
        assertThrows(NullPointerException.class, () -> {
            User user = userService.createUser(null, "bagesh", "3486199635");
        });
    }
    
    private static void bifurcateExpense(String expenseId) throws ExpenseDoesNotExistsException {
        expenseService.addUsersToExpense(expenseId, "bagesh@gmail.com");
        expenseService.assignExpenseShare(expenseId, ExpenseRepository.expenseMap.get(expenseId).getUserId(), 2000);
    }
    
    
    @Test
    @DisplayName("Contribute share to expense")
    public void contributeToExpense() throws ContributionExceededException, InvalidExpenseState, ExpenseSettledException {
        User user = userService.createUser("bagesh@gmail.com", "bagesh", "3486199635");
        Expense expense = expenseService.createExpense("Team Lunch", "Friday 19Th June Lunch in Briyani zone"
                , LocalDateTime.of(2020, Month.JUNE, 19, 12, 0), 2000.00, user.getEmailId());
        try {
            bifurcateExpense(expense.getId());
        } catch (ExpenseDoesNotExistsException expenseDoesNotExistsException) {
            System.out.println(expenseDoesNotExistsException.getMessage());
        }
        expense.setExpenseStatus(ExpenseStatus.PENDING);
        Set<User> users = expense.getExpenseGroup().getGroupMembers();
        for (User usr : users) {
            Contribution contribution = new Contribution();
            ExpenseGroup expenseGroup = expense.getExpenseGroup();
            UserShare userShare = expenseGroup.getUserContributions().get(usr.getEmailId());
            contribution.setContributionVal(600);
            contribution.setContributionDate(LocalDateTime.now());
            contribution.setTransactionId("T" + Instant.EPOCH);
            contribution.setTransactionDescription("Transferred from UPI");
            userService.contributeToExpense(expense.getId(), usr.getEmailId(), contribution);
        }

        assertEquals(600,
                expense.getExpenseGroup().getUserContributions()
                        .get(user.getEmailId()).getContributions().get(0).getContributionVal());
    }

}
