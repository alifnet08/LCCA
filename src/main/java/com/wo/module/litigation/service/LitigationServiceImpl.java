/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.litigation.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.litigation.dao.LitigationDao;
import com.wo.module.litigation.model.Litigation;
import com.wo.module.common.paging.SearchObject;

@Transactional
@Service("litigationService")
public class LitigationServiceImpl implements LitigationService {
    @Autowired
    @Qualifier("litigationDao")
    private LitigationDao litigationDao;
    
	public LitigationDao getLitigationDao() {
		return litigationDao;
	}

	public void setLitigationDao(LitigationDao litigationDao) {
		this.litigationDao = litigationDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<Litigation> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws Exception {
		return litigationDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return litigationDao.searchCountData(searchCriteria);
	}
	
	public void save(Litigation entity) {
		litigationDao.save(entity);
	}
	
	public void update(Litigation entity) {
		litigationDao.update(entity);
	}
	
	public void delete(Litigation entity) {
		litigationDao.delete(entity);
	}
  
    public Litigation findById(Long id) {
    	return litigationDao.getById(id);
    }
    
   
        
}
