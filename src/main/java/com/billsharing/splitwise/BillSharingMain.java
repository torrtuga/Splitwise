package com.billsharing.splitwise;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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
import com.billsharing.splitwise.utils.Utils;

public class BillSharingMain {

	static ExpenseService expenseService;
	static UserService userService;
	private static final Logger logger = LogManager.getLogger(BillSharingMain.class);

	public static void main(String[] args) throws ContributionExceededException, InvalidExpenseState,
			ExpenseDoesNotExistsException, ExpenseSettledException {
		


//		Logger.logInfo(BillSharingMain.class, "log check");
//		Logger.logInfo(this.getClass(), "inside sampleClass");
		logger.info("Started program ");
		expenseService = new ExpenseService();
		userService = new UserService();
		createTestUsers();

		Expense expense = createLunchExpense();
		try {
			bifurcateExpense(expense.getId());
		} catch (ExpenseDoesNotExistsException expenseDoesNotExistsException) {
		}
		expense.setExpenseStatus(ExpenseStatus.PENDING);
 
		Set<User> users = expense.getExpenseGroup().getGroupMembers();
		for (User user : users) {
			System.out.println(user.getEmailId());
			contributeToExpense(expense.getId(), user.getEmailId());
		}
		if (expenseService.isExpenseSettled(expense.getId())) {
			System.out.println("Expense Settled....");
			expenseService.setExpenseStatus(expense.getId(), ExpenseStatus.SETTLED);
		}
		System.out.println("Bye......");
		
//		final ClassName objectTemp = object;
//		Utils.DEFAULT_THREAD_POOL.execute(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
		
	}

	public static Expense createLunchExpense() {
		Expense expense = expenseService.createExpense("Team Lunch", "Friday 19Th June Lunch in Briyani zone",
				LocalDateTime.of(2020, Month.JUNE, 19, 12, 0), 1600.00, "vishnu@gmail.com");
		return expense;
	}

	private static void contributeToExpense(String expenseId, String userId)
			throws ContributionExceededException, InvalidExpenseState, ExpenseSettledException {
		Contribution contribution = new Contribution();
		Expense expense = ExpenseRepository.expenseMap.get(expenseId);
		ExpenseGroup expenseGroup = expense.getExpenseGroup();
		UserShare userShare = expenseGroup.getUserContributions().get(userId);
		contribution.setContributionVal(userShare.getShare());
		contribution.setContributionDate(LocalDateTime.now());
		contribution.setTransactionId("T" + Instant.EPOCH);
		contribution.setTransactionDescription("Transferred from UPI");
		userService.contributeToExpense(expenseId, userId, contribution);

	}

	private static void bifurcateExpense(String expenseId) throws ExpenseDoesNotExistsException {
		expenseService.addUsersToExpense(expenseId, "bagesh@gmail.com");
		expenseService.addUsersToExpense(expenseId, "divya@gmail.com");
		expenseService.addUsersToExpense(expenseId, "palani@gmail.com");
		expenseService.addUsersToExpense(expenseId, "neha@gmail.com");

		expenseService.assignExpenseShare(expenseId, "bagesh@gmail.com", 400);
		expenseService.assignExpenseShare(expenseId, "divya@gmail.com", 400);
		expenseService.assignExpenseShare(expenseId, "palani@gmail.com", 400);
		expenseService.assignExpenseShare(expenseId, "neha@gmail.com", 400);
	}

	private static void createTestUsers() {
		User user1 = userService.createUser("bagesh@gmail.com", "bagesh", "3486199635");
		User user2 = userService.createUser("ajay@gmail.com", "ajay", "6112482630");
		User user3 = userService.createUser("amit@gmail.com", "amit", "2509699232");
		User user4 = userService.createUser("kamal@gmail.com", "kamal", "5816355154");
		User user5 = userService.createUser("neha@gmail.com", "neha", "7737316054");
		User user6 = userService.createUser("kajal@gmail.com", "kajal", "4813053349");
		User user7 = userService.createUser("jyothi@gmail.com", "jyothi", "3974178644");
		User user8 = userService.createUser("subin@gmail.com", "subin", "4768463294");
		User user9 = userService.createUser("deepak@gmail.com", "deepak", "4829338803");
		User user10 = userService.createUser("vishnu@gmail.com", "vishnu", "3384071602");
		User user11 = userService.createUser("mayank@gmail.com", "mayank", "2376951206");
		User user12 = userService.createUser("anu@gmail.com", "anu", "8478577491");
		User user13 = userService.createUser("kavya@gmail.com", "kavya", "7505888698");
		User user14 = userService.createUser("divya@gmail.com", "divya", "9587030077");
		User user15 = userService.createUser("prabhu@gmail.com", "prabhu", "3303167757");
		User user16 = userService.createUser("sangeeth@gmail.com", "sangeeth", "7409081739");
		User user17 = userService.createUser("rajesh@gmail.com", "rajesh", "2367659285");
		User user18 = userService.createUser("alamelu@gmail.com", "alamelu", "8938025834");
		User user19 = userService.createUser("aruna@gmail.com", "aruna", "8189506064");
		User user20 = userService.createUser("palani@gmail.com", "palani", "2973733105");
	}

}
