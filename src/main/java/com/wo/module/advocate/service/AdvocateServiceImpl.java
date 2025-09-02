/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.advocate.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.advocate.dao.AdvocateDao;
import com.wo.module.advocate.model.Advocate;
import com.wo.module.common.paging.SearchObject;

@Transactional
@Service("advocateService")
public class AdvocateServiceImpl implements AdvocateService {
    @Autowired
    @Qualifier("advocateDao")
    private AdvocateDao advocateDao;
    
	public AdvocateDao getAdvocateDao() {
		return advocateDao;
	}

	public void setAdvocateDao(AdvocateDao advocateDao) {
		this.advocateDao = advocateDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<Advocate> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws Exception {
		return advocateDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return advocateDao.searchCountData(searchCriteria);
	}
	
	public void save(Advocate entity) {
		advocateDao.save(entity);
	}
	
	public void update(Advocate entity) {
		advocateDao.update(entity);
	}
	
	public void delete(Advocate entity) {
		advocateDao.delete(entity);
	}
  
    public Advocate findById(Long id) {
    	return advocateDao.getById(id);
    }
    
   
        
}
