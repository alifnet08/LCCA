/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.logAccess.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.litigation.model.Litigation;
import com.wo.module.logAccess.dao.LogAccessDao;
import com.wo.module.logAccess.model.LogAccess;

@Transactional
@Service("logAccessService")
public class LogAccessServiceImpl implements LogAccessService {
    @Autowired
    @Qualifier("logAccessDao")
    private LogAccessDao logAccessDao;
    
	public void save(LogAccess entity) {
		logAccessDao.save(entity);
	}
	
	public void update(LogAccess entity) {
		logAccessDao.update(entity);
	}
	
	public void delete(LogAccess entity) {
		logAccessDao.delete(entity);
	}
  
    public LogAccess findById(Long id) {
    	return logAccessDao.getById(id);
    }

    @SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<LogAccess> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws Exception {
		return logAccessDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return logAccessDao.searchCountData(searchCriteria);
	}
	
    
    
   
        
}
