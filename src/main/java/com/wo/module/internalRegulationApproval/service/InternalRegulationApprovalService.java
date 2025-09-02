package com.wo.module.internalRegulationApproval.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.externalRegulation.model.Regulation;
import com.wo.module.internalRegulationApproval.model.InternalRegulationApproval;

public interface InternalRegulationApprovalService extends RetrieverDataPage<InternalRegulationApproval>  {
    
	public void processApprove(Regulation regulation,String note, String nik,String statusReg,String statusApp) throws Exception;
	
	public String procedureUpdateTmpTrackRecord(Long internalId) throws Exception;
	
}
