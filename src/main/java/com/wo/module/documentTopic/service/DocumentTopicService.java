/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.documentTopic.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.documentTopic.model.DocumentTopic;

public interface DocumentTopicService extends RetrieverDataPage<DocumentTopic> {
    
	public void save(DocumentTopic documentTopic); 
	
	public void update(DocumentTopic documentTopic);
	
	public void delete(DocumentTopic documentTopic);
  
    public DocumentTopic findById(Long id) ;
    
    public Integer getDocumentTopicByProvCategoryAndTopik(String provision, Long category, String topic) throws Exception;
    
    public Integer getEditDocumentTopicByIdProvCategoryAndTopic(Long id, String provision, Long category, String topic) throws Exception;
    
    public Boolean isUsedInTransaction(Long id);
    
    public Integer getDocumentTopicByDocumentTopicIn(String topicIn) throws Exception ;
}
