/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpComplianceReview.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.tmpComplianceReview.dao.ComplianceReviewDao;
import com.wo.module.tmpComplianceReview.vo.ComplianceReviewApprovalVO;
import com.wo.module.tmpComplianceReview.vo.ComplianceReviewVO;
import com.wo.module.tmpComplianceReview.vo.StatusConfirmationVO;

@Transactional
@Service("complianceReviewService")
public class ComplianceReviewServiceImpl implements ComplianceReviewService {
	@Autowired
    @Qualifier("complianceReviewDao")
    private ComplianceReviewDao complianceReviewDao;
	 
	@SuppressWarnings("rawtypes")
	@Override
	public List<ComplianceReviewVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return complianceReviewDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return complianceReviewDao.searchCountData(searchCriteria);
	}

	@Override
	public List<ComplianceReviewApprovalVO> getDataApprovalByComplianceReviewId(Long complianceReviewId) {
		return complianceReviewDao.getDataApprovalByComplianceReviewId(complianceReviewId);
	}

	@Override
	public List<StatusConfirmationVO> getDataConfirmStatusByComplianceReviewId(Long complianceReviewId) {
		return complianceReviewDao.getDataConfirmStatusByComplianceReviewId(complianceReviewId);
	}

	@Override
	public Boolean hasReachedMaximumReschedule(Long complianceReviewPicFollowupId) {
		return complianceReviewDao.hasReachedMaximumReschedule(complianceReviewPicFollowupId);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ComplianceReviewVO> searchDataXLS(List<? extends SearchObject> searchCriteria) {
		return complianceReviewDao.searchDataXLS(searchCriteria);
	}

	@Override
	public Number countDocNo(String complianceReviewDocNo) {
		return complianceReviewDao.countDocNo(complianceReviewDocNo);
	}
    
	
}
