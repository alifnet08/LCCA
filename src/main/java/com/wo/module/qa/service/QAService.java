/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.qa.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.qa.model.QA;

public interface QAService extends RetrieverDataPage<QA> {
    
	public void save(QA qa); 
	
	public void update(QA qa);
	
	public void delete(QA qa);
  
    public QA findById(Long id) ;
    
    public Number getTicketNo();
    
    public Number getCountQuestionNotAnswered();
    
    public QA getQAQuestionNotAnswered();
    
    
 
}
