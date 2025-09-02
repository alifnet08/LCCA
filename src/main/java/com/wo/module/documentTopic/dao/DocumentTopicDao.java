/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.documentTopic.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.documentTopic.model.DocumentTopic;

/**
 *
 * @author hendra
 * 
 *         Modification Alex
 */
public interface DocumentTopicDao extends GenericDAO<DocumentTopic, Long>, RetrieverDataPage<DocumentTopic> {

	public Integer getDocumentTopicByProvCategoryInAndTopic(String provision, Long categoryIn, String topic)
			throws Exception;

	public Integer getEditDocumentTopicByIdProvCategoryAndTopic(Long id, String provision, Long categoryIn,
			String topic) throws Exception;
	
    public Boolean isUsedInTransaction(Long id);
    
    public Integer getDocumentTopicByDocumentTopicIn(String topicIn) throws Exception ;
}
