package com.wo.module.engine.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.engine.dao.ArchiveFileDao;
import com.wo.module.engine.dao.OscarJobDao;
import com.wo.module.user.dao.UserDao;

@Transactional
@Service("archiveFileService")
public class ArchiveFileServiceImpl implements ArchiveFileService {

	@Autowired
	@Qualifier("oscarJobDao")
	private OscarJobDao oscarJobDao;

	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;
	
	@Autowired
	@Qualifier("archiveFileDao")
	private ArchiveFileDao archiveFileDao;

	@Override
	public String getSystemProperty(String propertyCode) throws Exception {
		return archiveFileDao.getSystemProperty(propertyCode);
	}

	@Override
	public List<String> getFileExpiredFromDate(Integer expiredDays) throws Exception {
		return archiveFileDao.getFileExpiredFromDate(expiredDays);
		
	}
	
	
	
}
