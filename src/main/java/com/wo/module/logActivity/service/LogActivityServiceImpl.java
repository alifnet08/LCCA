package com.wo.module.logActivity.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.logActivity.dao.LogActivityDao;
import com.wo.module.logActivity.model.LogActivity;

@Transactional
@Service("logActivityService")
public class LogActivityServiceImpl implements LogActivityService{
	@Autowired
    @Qualifier("logActivityDao")
	private LogActivityDao logActivityDao;
	
	@Override
	public List<LogActivity> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void save(LogActivity entity) {
		logActivityDao.save(entity);
		
	}

	public void update(LogActivity entity) {
		logActivityDao.update(entity);
		
	}

	public void delete(LogActivity entity) {
		logActivityDao.delete(entity);
		
	}

	public LogActivity findById(Long id) {
		return logActivityDao.getById(id);
	}
	

}
