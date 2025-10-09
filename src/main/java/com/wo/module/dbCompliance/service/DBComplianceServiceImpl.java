/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.dbCompliance.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.dbCompliance.dao.DBComplianceDao;
import com.wo.module.dbCompliance.model.DBCompliance;
import com.wo.module.common.paging.SearchObject;

@Transactional
@Service("dbComplianceService")
public class DBComplianceServiceImpl implements DBComplianceService {
    @Autowired
    @Qualifier("dbComplianceDao")
    private DBComplianceDao dbComplianceDao;
    
	public DBComplianceDao getDBComplianceDao() {
		return dbComplianceDao;
	}

	public void setDBComplianceDao(DBComplianceDao dbComplianceDao) {
		this.dbComplianceDao = dbComplianceDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<DBCompliance> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws Exception {
		return dbComplianceDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return dbComplianceDao.searchCountData(searchCriteria);
	}
	
	public void save(DBCompliance entity) {
		dbComplianceDao.save(entity);
	}
	
	public void update(DBCompliance entity) {
		dbComplianceDao.update(entity);
	}
	
	public void delete(DBCompliance entity) {
		dbComplianceDao.delete(entity);
	}
  
    public DBCompliance findById(Long id) {
    	return dbComplianceDao.getById(id);
    }
    
    public DBCompliance getCheckDataDBCompliance(Long dbComplianceId, String reportName, String reportType){
    	return dbComplianceDao.getCheckDataDBCompliance(dbComplianceId, reportName, reportType);
    }
    
   
        
}
