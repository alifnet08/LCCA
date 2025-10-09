package com.wo.module.trcComplianceReview.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.trcComplianceReview.dao.TrcComplianceReviewPicFollowupDao;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowup;

@Transactional
@Service("trcComplianceReviewPicFollowupService")
public class TrcComplianceReviewPicFollowupServiceImpl implements TrcComplianceReviewPicFollowupService {
	@Autowired
	@Qualifier("trcComplianceReviewPicFollowupDao")
	private TrcComplianceReviewPicFollowupDao trcComplianceReviewPicFollowupDao;

	public TrcComplianceReviewPicFollowupDao getTrcComplianceReviewPicFollowupDao() {
		return trcComplianceReviewPicFollowupDao;
	}

	public void setTrcComplianceReviewPicFollowupDao(
			TrcComplianceReviewPicFollowupDao trcComplianceReviewPicFollowupDao) {
		this.trcComplianceReviewPicFollowupDao = trcComplianceReviewPicFollowupDao;
	}

	public TrcComplianceReviewPicFollowup findById(Long id) {
		return trcComplianceReviewPicFollowupDao.getById(id);
	}

}
