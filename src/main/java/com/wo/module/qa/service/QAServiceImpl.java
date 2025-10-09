/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.qa.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.qa.dao.QADao;
import com.wo.module.qa.model.QA;

@Transactional
@Service("qaService")
public class QAServiceImpl implements QAService {
    @Autowired
    @Qualifier("qaDao")
    private QADao qaDao;

	
	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public List<QA> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return qaDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return qaDao.searchCountData(searchCriteria);
    }

	public void save(QA qa) {
		qaDao.save(qa);
	}
	
	public void update(QA qa) {
		qaDao.update(qa);
	}
	
	public void delete(QA qa) {
		qaDao.delete(qa);
	}
  
    public QA findById(Long id) {
    	return qaDao.getById(id);
    }
    
    public Number getTicketNo(){
    	return qaDao.getTicketNo();
    }
    
    public Number getCountQuestionNotAnswered(){
    	return qaDao.getCountQuestionNotAnswered();
    }
    
    public QA getQAQuestionNotAnswered()
    {
    	return qaDao.getQAQuestionNotAnswered();
    }
    
    
}
