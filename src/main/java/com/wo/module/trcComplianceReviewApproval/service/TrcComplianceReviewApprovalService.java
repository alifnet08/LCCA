/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcComplianceReviewApproval.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.complianceTestingMockup.vo.ComplianceTestingVO;
import com.wo.module.trcComplianceReview.model.TrcComplianceReview;
import com.wo.module.user.model.User;


public interface TrcComplianceReviewApprovalService extends RetrieverDataPage<ComplianceTestingVO>  {
    
	public void update(TrcComplianceReview trcComplianceReview, User user)throws Exception;
}
