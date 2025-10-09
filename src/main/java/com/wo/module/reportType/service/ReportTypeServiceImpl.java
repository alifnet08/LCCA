/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.reportType.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.reportType.dao.ReportTypeDao;
import com.wo.module.reportType.model.ReportType;

@Transactional
@Service("reportTypeService")
public class ReportTypeServiceImpl implements ReportTypeService {
    @Autowired
    @Qualifier("reportTypeDao")
    private ReportTypeDao reportTypeDao;

	public ReportTypeDao getReportTypeDao() {
		return reportTypeDao;
	}

	public void setReportTypeDao(ReportTypeDao reportTypeDao) {
		this.reportTypeDao = reportTypeDao;
	}
    
	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public List<ReportType> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return reportTypeDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return reportTypeDao.searchCountData(searchCriteria);
    }

	public void save(ReportType reportType) {
		reportTypeDao.save(reportType);
	}
	
	public void update(ReportType reportType) {
		reportTypeDao.update(reportType);
	}
	
	public void delete(ReportType reportType) {
		reportTypeDao.delete(reportType);
	}
  
    public ReportType findById(Long id) {
    	return reportTypeDao.getById(id);
    }
    
    public List<ReportType> getAllReportType() {
		return reportTypeDao.getAllReportType();
	}
    
    public Boolean isReportTypeDuplicate(ReportType reportType) {
    	return reportTypeDao.isReportTypeDuplicate(reportType);
    }
    
    public Boolean isUsedInTransaction(Long id) {
    	return reportTypeDao.isUsedInTransaction(id);
    }
        
}
