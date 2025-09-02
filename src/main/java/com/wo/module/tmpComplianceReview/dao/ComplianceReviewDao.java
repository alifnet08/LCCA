/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpComplianceReview.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReview;
import com.wo.module.tmpComplianceReview.vo.ComplianceReviewApprovalVO;
import com.wo.module.tmpComplianceReview.vo.ComplianceReviewVO;
import com.wo.module.tmpComplianceReview.vo.StatusConfirmationVO;

public interface ComplianceReviewDao extends  GenericDAO<TmpComplianceReview, Long>, RetrieverDataPage<ComplianceReviewVO>{
	
	public List<ComplianceReviewApprovalVO> getDataApprovalByComplianceReviewId(Long complianceReviewId);
	
	public List<StatusConfirmationVO> getDataConfirmStatusByComplianceReviewId(Long complianceReviewId);
	
	public Boolean hasReachedMaximumReschedule(Long complianceReviewPicFollowupId);
	
	public Number countDocNo(String complianceReviewDocNo);
	
	@SuppressWarnings("rawtypes")
	public List<ComplianceReviewVO> searchDataXLS(List<? extends SearchObject> searchCriteria);

	List<StatusConfirmationVO> getDataPicFollowupComplianceReviewId(Long complianceReviewId);
	
}
