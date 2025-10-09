/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.rc.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.rc.dao.RCDao;
import com.wo.module.rc.model.RC;

@Transactional
@Service("rcService")
public class RCServiceImpl implements RCService {
    @Autowired
    @Qualifier("rcDao")
    private RCDao rcDao;
    
	public RCDao getRcDao() {
		return rcDao;
	}

	public void setRcDao(RCDao rcDao) {
		this.rcDao = rcDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<RC> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws Exception {
		return rcDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return rcDao.searchCountData(searchCriteria);
	}
	
	public void save(RC entity) {
		rcDao.save(entity);
	}
	
	public void update(RC entity) {
		rcDao.update(entity);
	}
	
	public void delete(RC entity) {
		rcDao.delete(entity);
	}
  
    public RC findById(Long id) {
    	return rcDao.getById(id);
    }

	@Override
	public List<RC> getRCList() {
		return rcDao.getRCList();
	}
    
   
        
}
