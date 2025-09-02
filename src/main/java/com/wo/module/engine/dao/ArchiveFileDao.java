package com.wo.module.engine.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.log.model.LogHeader;

public interface ArchiveFileDao extends GenericDAO<LogHeader, Long>{

	public String getSystemProperty(String propertyCode);
	
	List<String> getFileExpiredFromDate(Integer expiredDays) throws Exception;
}
