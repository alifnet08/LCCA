/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.staticPage.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.faq.dao.FaqDao;
import com.wo.module.faq.model.Faq;
import com.wo.module.staticPage.dao.StaticPageDao;
import com.wo.module.staticPage.model.StaticPage;
import com.wo.module.common.paging.SearchObject;

@Transactional
@Service("staticPageService")
public class StaticPageServiceImpl implements StaticPageService {
    @Autowired
    @Qualifier("staticPageDao")
    private StaticPageDao staticPageDao;
    
	

	public StaticPageDao getStaticPageDao() {
		return staticPageDao;
	}

	public void setStaticPageDao(StaticPageDao staticPageDao) {
		this.staticPageDao = staticPageDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<StaticPage> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws Exception {
		return staticPageDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return staticPageDao.searchCountData(searchCriteria);
	}
	
	public void save(StaticPage entity) {
		staticPageDao.save(entity);
	}
	
	public void update(StaticPage entity) {
		staticPageDao.update(entity);
	}
	
	public void delete(StaticPage entity) {
		staticPageDao.delete(entity);
	}
  
    public StaticPage findById(Long id) {
    	return staticPageDao.getById(id);
    }
    
    public StaticPage getStaticPageByCategory(String category){
    	return staticPageDao.getStaticPageByCategory(category);
    }
        
}
