/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulationApproval.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.externalRegulation.model.Regulation;
import com.wo.module.externalRegulationApproval.model.ExternalRegulationApproval;

public interface ExternalRegulationApprovalService extends RetrieverDataPage<ExternalRegulationApproval>  {
    
	public void processApprove(Regulation regulation,String note, String nik,String statusReg,String statusApp) throws Exception;
	
	//public void processRevise(Regulation regulation,String note, String nik) throws Exception;
	
	 public String procedureUpdateTmpTrackRecord(Long internalId) throws Exception;
}
