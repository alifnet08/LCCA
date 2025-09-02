/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.qaPIC.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.qa.model.QA;
import com.wo.module.qaPIC.dao.QAPICDao;

@Transactional
@Service("qaPICService")
public class QAPICServiceImpl implements QAPICService {
    @Autowired
    @Qualifier("qaPICDao")
    private QAPICDao qaPICDao;

	
	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public List<QA> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return qaPICDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return qaPICDao.searchCountData(searchCriteria);
    }

	public void save(QA qa) {
		qaPICDao.save(qa);
	}
	
	public void update(QA qa) {
		qaPICDao.update(qa);
	}
	
	public void delete(QA qa) {
		qaPICDao.delete(qa);
	}
  
    public QA findById(Long id) {
    	return qaPICDao.getById(id);
    }

	
}
