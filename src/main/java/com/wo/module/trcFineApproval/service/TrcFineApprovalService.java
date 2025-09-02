/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcFineApproval.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcFineApproval.model.TrcFine;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowup;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowupHistory;
import com.wo.module.trcFineApproval.vo.TrcFineSearchVo;

public interface TrcFineApprovalService extends RetrieverDataPage<TrcFineSearchVo> {
	public void save(TrcFine entity); 
	
	public void update(TrcFine entity);
	
	public void delete(TrcFine entity);
  
    public TrcFine findById(Long id) ;
    
    public void saveDtl(TrcFinePicFollowup entity);
    
    public void updateDtl(TrcFinePicFollowup entity);
    
    public TrcFinePicFollowup findDtlById(Long id) ;
    
    public void saveHis(TrcFinePicFollowupHistory entity);
    
    public void updateHis(TrcFinePicFollowupHistory entity);
    
    public TrcFinePicFollowupHistory findHisById(Long id) ;
}
