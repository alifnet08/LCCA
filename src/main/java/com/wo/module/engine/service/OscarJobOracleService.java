package com.wo.module.engine.service;

import java.util.List;

import com.wo.module.user.model.User;

public interface OscarJobOracleService {
	List<User> getUserFromOracle(String tableQuery) throws Exception;
}