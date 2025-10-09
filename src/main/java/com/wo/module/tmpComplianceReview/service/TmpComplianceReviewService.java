/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpComplianceReview.service;

import com.wo.module.tmpComplianceReview.model.TmpComplianceReview;

public interface TmpComplianceReviewService  {
    
	public void save(TmpComplianceReview entity); 
	
	public void update(TmpComplianceReview entity);
	
	public void delete(TmpComplianceReview entity);
  
    public TmpComplianceReview findById(Long id) ;

    public void updateDataAlreadyExist(TmpComplianceReview tmpComplianceReviewNew, TmpComplianceReview tmpComplianceReviewDb, String userLogin) throws Exception;
}
