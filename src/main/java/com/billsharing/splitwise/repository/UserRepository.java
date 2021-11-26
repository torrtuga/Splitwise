package com.billsharing.splitwise.repository;

import java.util.HashMap;
import java.util.Map;

import com.billsharing.splitwise.model.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRepository {
	public static Map<String, User> userHashMap = new HashMap<>();

}
