/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.documentTopic.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.documentTopic.dao.DocumentTopicDao;
import com.wo.module.documentTopic.model.DocumentTopic;

@Transactional
@Service("documentTopicService")
public class DocumentTopicServiceImpl implements DocumentTopicService {
    @Autowired
    @Qualifier("documentTopicDao")
    private DocumentTopicDao documentTopicDao;

	public DocumentTopicDao getDocumentTopicDao() {
		return documentTopicDao;
	}

	public void setDocumentTopicDao(DocumentTopicDao documentTopicDao) {
		this.documentTopicDao = documentTopicDao;
	}
    
	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public List<DocumentTopic> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return documentTopicDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return documentTopicDao.searchCountData(searchCriteria);
    }

	public void save(DocumentTopic documentTopic) {
		documentTopicDao.save(documentTopic);
	}
	
	public void update(DocumentTopic documentTopic) {
		documentTopicDao.update(documentTopic);
	}
	
	public void delete(DocumentTopic documentTopic) {
		documentTopicDao.delete(documentTopic);
	}
  
    public DocumentTopic findById(Long id) {
    	return documentTopicDao.getById(id);
    }

	@Override
	@Transactional(readOnly=true)
	public Integer getDocumentTopicByProvCategoryAndTopik(String provision, Long category, String topic)
			throws Exception {
		return documentTopicDao.getDocumentTopicByProvCategoryInAndTopic(provision, category, topic);
	}

	@Override
	public Integer getEditDocumentTopicByIdProvCategoryAndTopic(Long id, String provision, Long category, String topic)
			throws Exception {
		return documentTopicDao.getEditDocumentTopicByIdProvCategoryAndTopic(id, provision, category, topic);
	}
	
	@Override
    public Boolean isUsedInTransaction(Long id) {
    	return documentTopicDao.isUsedInTransaction(id);
    }
	
	public Integer getDocumentTopicByDocumentTopicIn(String topicIn) throws Exception {
		return documentTopicDao.getDocumentTopicByDocumentTopicIn(topicIn);
	}
        
}
