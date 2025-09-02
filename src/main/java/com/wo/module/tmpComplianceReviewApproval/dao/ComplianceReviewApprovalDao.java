/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpComplianceReviewApproval.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReview;
import com.wo.module.tmpComplianceReviewApproval.vo.ComplianceReviewApprovalVO;

public interface ComplianceReviewApprovalDao extends  GenericDAO<TmpComplianceReview, Long>, RetrieverDataPage<ComplianceReviewApprovalVO>{

	
	
}
