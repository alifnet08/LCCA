package com.wo.module.engine.service;

import java.util.List;
import java.util.Map;

import com.wo.module.log.model.LogHeader;
import com.wo.module.user.model.User;

public interface OscarJobService {
	void connectSftp(String hostname, String username, String password, String outboundPath,
			List<String> encryptedFiles);
	void retrieveSftp(String hostname, String username, String password, String outboundPath,
			Map<String, String> generatedFiles);
	String getSystemProperty(String propertyCode) throws Exception;
//	List<User> getUserFromOracle() throws Exception;
	void updateUser(List<User> users,LogHeader logH, com.wo.module.engine.ReadXlsController.Wrapper seq, String location) throws Exception;
	void updateUser(List<User> users) throws Exception;
	String execInboundCommon(String procedureName, String jsonData) throws Exception;
	void updateDivisionId();
	void updateEnableFlagToN();
	void updateEnableFlagToY();
}