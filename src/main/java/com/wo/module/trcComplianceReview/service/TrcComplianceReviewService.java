/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcComplianceReview.service;

import java.util.Date;
import java.util.List;

import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.complianceTestingMockup.vo.ComplianceTestingVO;
import com.wo.module.trcComplianceReview.model.TrcComplianceReview;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowup;
import com.wo.module.trcComplianceReview.vo.TrcComplianceReviewVO;
import com.wo.module.user.model.User;

public interface TrcComplianceReviewService extends RetrieverDataPage<TrcComplianceReviewVO> {
  
    public TrcComplianceReview findById(Long id) ;
    
    public void update(TrcComplianceReview trcComplianceReview, 
			Long complianceReviewPICFollowupTrcId, 
			String keterangan,Date followupDate,List<UploadedFileWO> uploadedFilesEvidence,  User user)throws Exception;

//	void update(TrcComplianceReview trcComplianceReview, TrcComplianceReviewPicFollowup trcComplianceReviewPicFollowup,
//			User user) throws Exception;

	void update(TrcComplianceReview trcComplianceReview, TrcComplianceReviewPicFollowup trcComplianceReviewPicFollowup, User user) throws Exception;
}
