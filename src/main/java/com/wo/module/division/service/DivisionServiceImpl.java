/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.division.service;

import java.util.List;
import java.util.Map;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.division.dao.DivisionDao;
import com.wo.module.division.model.Division;
import com.wo.module.common.paging.SearchObject;

@Transactional
@Service("divisionService")
public class DivisionServiceImpl implements DivisionService {
    @Autowired
    @Qualifier("divisionDao")
    private DivisionDao divisionDao;
    
	public DivisionDao getDivisionDao() {
		return divisionDao;
	}

	public void setDivisionDao(DivisionDao divisionDao) {
		this.divisionDao = divisionDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<Division> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws Exception {
		return divisionDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return divisionDao.searchCountData(searchCriteria);
	}
	
	public void save(Division entity) {
		divisionDao.save(entity);
	}
	
	public void update(Division entity) {
		divisionDao.update(entity);
	}
	
	public void delete(Division entity) {
		divisionDao.delete(entity);
	}
  
    public Division findById(Long id) {
    	return divisionDao.getById(id);
    }
    
    public Number getCountDivisionByName(String divName){
    	return divisionDao.getCountDivisionByName(divName);
    }
    
    public Division getDivisionByName(String divName){
    	return divisionDao.getDivisionByName(divName);
    }
}
