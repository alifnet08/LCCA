/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpAuditApproval.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.tmpAudit.model.TmpAudit;
import com.wo.module.tmpAuditApproval.vo.TmpAuditApprovalVO;


public interface TmpAuditApprovalService extends RetrieverDataPage<TmpAuditApprovalVO>  {
    
	public void processApprove(TmpAudit tmpAudit,String note, String nik,String statusReg,String statusApp) throws Exception;
	
	//public void processRevise(Regulation regulation,String note, String nik) throws Exception;
}
