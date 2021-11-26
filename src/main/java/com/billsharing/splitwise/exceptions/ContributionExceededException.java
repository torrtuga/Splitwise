package com.billsharing.splitwise.exceptions;

public class ContributionExceededException extends Exception {

	public ContributionExceededException(String message) {
		super(message);
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return super.getMessage();
	}
}
