/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationSocializationApproval.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.regulationSocialization.model.SocializationTmp;
import com.wo.module.regulationSocializationApproval.vo.RegulationSocializationApprovalVO;


public interface RegulationSocializationApprovalService extends RetrieverDataPage<RegulationSocializationApprovalVO>  {
    
	public void processApprove(SocializationTmp socializationTmp,String note, String nik,String statusReg,String statusApp) throws Exception;
	
	//public void processRevise(Regulation regulation,String note, String nik) throws Exception;
}
