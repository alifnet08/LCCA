/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.qaPIC.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.qa.model.QA;

public interface QAPICService extends RetrieverDataPage<QA> {
    
	public void save(QA qa); 
	
	public void update(QA qa);
	
	public void delete(QA qa);
  
    public QA findById(Long id) ;
    
    
}
