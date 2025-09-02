/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.complianceTestingMockup.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.complianceTestingMockup.dao.ComplianceTestingDao;
import com.wo.module.complianceTestingMockup.model.ComplianceTesting;
import com.wo.module.complianceTestingMockup.vo.ComplianceTestingVO;


@Transactional
@Service("complianceTestingService")
public class ComplianceTestingServiceImpl implements ComplianceTestingService {
    @Autowired
    @Qualifier("complianceTestingDao")
    private ComplianceTestingDao complianceTestingDao;
    

	public ComplianceTestingDao getComplianceTestingDao() {
		return complianceTestingDao;
	}

	public void setComplianceTestingDao(ComplianceTestingDao complianceTestingDao) {
		this.complianceTestingDao = complianceTestingDao;
	}

	
	public void save(ComplianceTesting entity) {
		complianceTestingDao.save(entity);
	}
	
	public void update(ComplianceTesting entity) {
		complianceTestingDao.update(entity);
	}
	
	public void delete(ComplianceTesting entity) {
		complianceTestingDao.delete(entity);
	}
  
    public ComplianceTesting findById(Long id) {
    	return complianceTestingDao.getById(id);
    }

	@Override
	public List<ComplianceTestingVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return complianceTestingDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return complianceTestingDao.searchCountData(searchCriteria);
	}
    
	@Override
	public List<ComplianceTestingVO> searchDataXls(List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return complianceTestingDao.searchDataXls(searchCriteria);
	}
        
}
