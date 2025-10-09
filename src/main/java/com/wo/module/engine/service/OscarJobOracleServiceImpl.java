package com.wo.module.engine.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.engine.dao.OscarJobDao;
import com.wo.module.user.model.User;

@Transactional("transactionOrcl")
@Service("oscarJobOracleService")
public class OscarJobOracleServiceImpl implements OscarJobOracleService {

	
//	private OscarJobDao oscarJobOracleDao = OscarJobOracleDaoImpl.getInstance();
	
	//@Autowired
	//@Qualifier("oscarJobOracleDao")
	private OscarJobDao oscarJobOracleDao;

	@Override
	public List<User> getUserFromOracle(String tableQuery) throws Exception {
		return oscarJobOracleDao.getListUserFromOracle(tableQuery);
	}
	
	
	public OscarJobDao getOscarJobOracleDao() {
		return oscarJobOracleDao;
	}

	public void setOscarJobOracleDao(OscarJobDao oscarJobOracleDao) {
		this.oscarJobOracleDao = oscarJobOracleDao;
	}

//	public UserOtherDao getUserOtherDao() {
//		return userOtherDao;
//	}
//
//	public void setUserOtherDao(UserOtherDao userOtherDao) {
//		this.userOtherDao = userOtherDao;
//	}

}
