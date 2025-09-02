package com.wo.module.engine.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.log.model.LogHeader;
import com.wo.module.user.model.User;

public interface OscarJobDao extends GenericDAO<LogHeader, Long> {

	public String execInboundCommon(String procedureName, String jsonData) throws Exception;

	public String getSystemProperty(String propertyCode);
	
	public List<User> getListUserFromOracle(String tableQuery) throws Exception;
}
