/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcComplianceReviewApproval.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.complianceTestingMockup.vo.ComplianceTestingVO;
import com.wo.module.trcComplianceReview.model.TrcComplianceReview;
import com.wo.module.trcComplianceReviewApproval.vo.TrcComplianceReviewApprovalVO;

public interface TrcComplianceReviewApprovalDao extends  GenericDAO<TrcComplianceReview, Long>, RetrieverDataPage<ComplianceTestingVO>{

	
	
}
