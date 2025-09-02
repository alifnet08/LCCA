/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.discussion.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.discussion.dao.DiscussionDao;
import com.wo.module.discussion.model.Discussion;
import com.wo.module.common.paging.SearchObject;

@Transactional
@Service("discussionService")
public class DiscussionServiceImpl implements DiscussionService {
    @Autowired
    @Qualifier("discussionDao")
    private DiscussionDao discussionDao;
    
	public DiscussionDao getDiscussionDao() {
		return discussionDao;
	}

	public void setDiscussionDao(DiscussionDao discussionDao) {
		this.discussionDao = discussionDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<Discussion> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws Exception {
		return discussionDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return discussionDao.searchCountData(searchCriteria);
	}
	
	public void save(Discussion entity) {
		discussionDao.save(entity);
	}
	
	public void update(Discussion entity) {
		discussionDao.update(entity);
	}
	
	public void delete(Discussion entity) {
		discussionDao.delete(entity);
	}
  
    public Discussion findById(Long id) {
    	return discussionDao.getById(id);
    }
    
   
        
}
