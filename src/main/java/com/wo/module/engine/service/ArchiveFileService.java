package com.wo.module.engine.service;

import java.util.List;

public interface ArchiveFileService {

	String getSystemProperty(String propertyCode) throws Exception;
	
	List<String> getFileExpiredFromDate(Integer expiredDays) throws Exception;
}
