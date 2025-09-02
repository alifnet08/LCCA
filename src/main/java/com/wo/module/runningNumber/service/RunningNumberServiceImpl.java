package com.wo.module.runningNumber.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.runningNumber.dao.RunningNumberDao;
import com.wo.module.runningNumber.model.RunningNumber;

@Transactional
@Service("runningNumberService")
public class RunningNumberServiceImpl implements RunningNumberService {
    @Autowired
    @Qualifier("runningNumberDao")
    private RunningNumberDao runningNumberDao;
    
	public RunningNumberDao getRunningNumberDao() {
		return runningNumberDao;
	}

	public void setRunningNumberDao(RunningNumberDao runningNumberDao) {
		this.runningNumberDao = runningNumberDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<RunningNumber> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public RunningNumber getRunningNumber(String runningNumberNo, String runningNumberReset, String runningNumberType)
			throws Exception {
		return runningNumberDao.getRunningNumber(runningNumberNo, runningNumberReset, runningNumberType);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public Integer getRunningNumberSeq(String runningNumberNo, String runningNumberReset, String runningNumberType, String userLogin)
			throws Exception {
		Integer runSeq = runningNumberDao.getRunningNumberSeq(runningNumberNo, runningNumberReset);
		if(runSeq !=null && runSeq > 0) {
			runSeq = runSeq + 1;
			RunningNumber runEdit = runningNumberDao.getRunningNumber(runningNumberNo, runningNumberReset, runningNumberType);
			runEdit.setLastUpdateBy(userLogin);
			runEdit.setLastUpdateDate(new Timestamp(new Date().getTime()));		
			runEdit.setRunningNumberSeq((runSeq));
			runEdit.setDelId(new Long(0));
			runEdit.setEnabledFlag(Constants.CONSTANT_YES);
			runningNumberDao.update(runEdit);
		}else {
			RunningNumber runAdd = new RunningNumber();
			runSeq = 1;
			runAdd.setRunningNumberNo(runningNumberNo);
			runAdd.setRunningNumberReset(runningNumberReset);
			runAdd.setRunningNumberType(runningNumberType);
			runAdd.setRunningNumberSeq(runSeq);
			runAdd.setDelId(new Long(0));
			runAdd.setEnabledFlag(Constants.CONSTANT_YES);
			runAdd.setCreatedBy(userLogin);
			runAdd.setCreationDate(new Timestamp(new Date().getTime()));
			runningNumberDao.save(runAdd);
		}
		
		return runSeq;
	}
		
    
    
}