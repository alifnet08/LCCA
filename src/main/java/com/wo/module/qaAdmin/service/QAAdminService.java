/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.qaAdmin.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.qa.model.QA;
import com.wo.module.qa.model.QAKeyword;
import com.wo.module.qaAdmin.vo.QAAdminVo;

public interface QAAdminService extends RetrieverDataPage<QAAdminVo> {
    
	public void save(QA qa); 
	
	public void update(QA qa);
	
	public void delete(QA qa);
  
    public QA findById(Long id) ;
    
    @SuppressWarnings("rawtypes")
	public List<QA> searchDataXls(List<? extends SearchObject> searchCriteria);
    
    public List<QAKeyword> getKeyword(Long qnaId, Long qnaKeywordId);
    
}
