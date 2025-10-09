/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.responsibility.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.responsibility.dao.ResponsibilityDao;
import com.wo.module.responsibility.model.Responsibility;
import com.wo.module.responsibility.vo.ResponsibilityDtlVO;

@Transactional
@Service("responsibilityService")
public class ResponsibilityServiceImpl implements ResponsibilityService {
    @Autowired
    @Qualifier("responsibilityDao")
    private ResponsibilityDao responsibilityDao;
    

	public ResponsibilityDao getResponsibilityDao() {
		return responsibilityDao;
	}

	public void setResponsibilityDao(ResponsibilityDao responsibilityDao) {
		this.responsibilityDao = responsibilityDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<Responsibility> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws Exception {
		return responsibilityDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return responsibilityDao.searchCountData(searchCriteria);
	}
	
	public List<Responsibility> getAllResponsibility() {
		return responsibilityDao.getAllResponsibility();
	}
	
	public void save(Responsibility entity) {
		responsibilityDao.save(entity);
	}
	
	public void update(Responsibility entity) {
		responsibilityDao.update(entity);
	}
	
	public void delete(Responsibility entity) {
		responsibilityDao.delete(entity);
	}
  
    public Responsibility findById(Long id) {
    	return responsibilityDao.getById(id);
    }
    
    public List<ResponsibilityDtlVO> searchResponsibilityMenuAllMenu(ResponsibilityDtlVO responsibilityMenuVO) {
    	return responsibilityDao.searchResponsibilityMenuAllMenu(responsibilityMenuVO);
    }
    
    public void deleteInsertResponsibilityMenu(
			ResponsibilityDtlVO responsibilityId, String user) {
    	responsibilityDao.deleteInsertResponsibilityMenu(
    			responsibilityId, user);
    }
    
        
}
