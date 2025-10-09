/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpComplianceReviewApproval.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReview;
import com.wo.module.tmpComplianceReviewApproval.vo.ComplianceReviewApprovalVO;
public interface ComplianceReviewApprovalService extends RetrieverDataPage<ComplianceReviewApprovalVO>  {
    
	public void processApprove(TmpComplianceReview tmpComplianceReview,String note, String nik,ParameterDetail statusReg,String statusApp) throws Exception;
}
