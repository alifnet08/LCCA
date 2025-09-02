/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcFineApproval.service;

import com.wo.module.trcFineApproval.model.TrcFinePicFollowup;

public interface TrcFinePicFollowupService {
	public void save(TrcFinePicFollowup entity); 
	
	public void update(TrcFinePicFollowup entity);
	
	public void delete(TrcFinePicFollowup entity);
  
    public TrcFinePicFollowup findById(Long id) ;
}
