/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.template.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.template.dao.TemplateDao;
import com.wo.module.template.model.Template;
import com.wo.module.common.paging.SearchObject;

@Transactional
@Service("templateService")
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    @Qualifier("templateDao")
    private TemplateDao templateDao;
    
	public TemplateDao getTemplateDao() {
		return templateDao;
	}

	public void setTemplateDao(TemplateDao templateDao) {
		this.templateDao = templateDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<Template> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws Exception {
		return templateDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return templateDao.searchCountData(searchCriteria);
	}
	
	public void save(Template entity) {
		templateDao.save(entity);
	}
	
	public void update(Template entity) {
		templateDao.update(entity);
	}
	
	public void delete(Template entity) {
		templateDao.delete(entity);
	}
  
    public Template findById(Long id) {
    	return templateDao.getById(id);
    }
    
   
        
}
