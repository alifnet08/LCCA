/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.aboutUs.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.aboutUs.dao.AboutUsDao;
import com.wo.module.aboutUs.model.AboutUs;
import com.wo.module.common.paging.SearchObject;

@Transactional
@Service("aboutUsService")
public class AboutUsServiceImpl implements AboutUsService {
    @Autowired
    @Qualifier("aboutUsDao")
    private AboutUsDao aboutUsDao;
    
	public AboutUsDao getAboutUsDao() {
		return aboutUsDao;
	}

	public void setAboutUsDao(AboutUsDao aboutUsDao) {
		this.aboutUsDao = aboutUsDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<AboutUs> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws Exception {
		return aboutUsDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return aboutUsDao.searchCountData(searchCriteria);
	}
	
	public void save(AboutUs entity) {
		aboutUsDao.save(entity);
	}
	
	public void update(AboutUs entity) {
		aboutUsDao.update(entity);
	}
	
	public void delete(AboutUs entity) {
		aboutUsDao.delete(entity);
	}
  
    public AboutUs findById(Long id) {
    	return aboutUsDao.getById(id);
    }
    
   
        
}
