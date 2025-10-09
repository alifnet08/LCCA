/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcComplianceReviewApproval.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.EntityUtil;
import com.wo.module.complianceTestingMockup.vo.ComplianceTestingVO;
import com.wo.module.parameter.dao.ParameterDetailDao;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.trcComplianceReview.dao.TrcComplianceReviewDao;
import com.wo.module.trcComplianceReview.model.TrcComplianceReview;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowup;
import com.wo.module.trcComplianceReviewApproval.dao.TrcComplianceReviewApprovalDao;
import com.wo.module.user.dao.UserDao;
import com.wo.module.user.model.User;

@Transactional
@Service("trcComplianceReviewApprovalService")
public class TrcComplianceReviewApprovalServiceImpl implements TrcComplianceReviewApprovalService {
    @Autowired
    @Qualifier("trcComplianceReviewApprovalDao")
    private TrcComplianceReviewApprovalDao trcComplianceReviewApprovalDao;
    
    @Autowired
    @Qualifier("trcComplianceReviewDao")
    private TrcComplianceReviewDao trcComplianceReviewDao;
    
    @Autowired
    @Qualifier("userDao")
    private UserDao userDao;

    @Autowired
    @Qualifier("parameterDetailDao")
    private ParameterDetailDao parameterDetailDao;

	@Override
	public List<ComplianceTestingVO> searchData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return trcComplianceReviewApprovalDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return trcComplianceReviewApprovalDao.searchCountData(searchCriteria);
	}

	@SuppressWarnings("unused")
	@Transactional(rollbackOn = { Exception.class})
	public void update(TrcComplianceReview trcComplianceReview, User user) throws Exception {
		try {
			SimpleDateFormat sdf= new SimpleDateFormat("dd MMM yyyy");
			
			EntityUtil.setUpdateInfo(trcComplianceReview, user.getNik());
			
			if (trcComplianceReview.getTrcComplianceReviewPicFollowupPoints() != null) {
				for (int i = 0; i < trcComplianceReview.getTrcComplianceReviewPicFollowupPoints().size(); i++) {
					TrcComplianceReviewPicFollowup dtl = (TrcComplianceReviewPicFollowup) trcComplianceReview
							.getTrcComplianceReviewPicFollowupPoints().get(i);
					
					if (dtl.getComplianceStatus() != null && dtl.getComplianceStatus().getParameterDtlCode() != null
						&& !dtl.getComplianceStatus().getParameterDtlCode().equals("")) {
//						ParameterDetail complianceStatus = parameterDetailDao
//								.getParameterDetailByParamDtlCode(dtl.getComplianceStatus().getParameterDtlCode());					
//						dtl.setComplianceStatus(complianceStatus);
						//dtl.setComplianceNote(dtl.getComplianceNote());
						dtl.setComplianceBy(user);
						dtl.setComplianceDate(new Date());		
						
						if (ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_OPEN
								.equals(dtl.getComplianceStatus().getParameterDtlCode())) {
							ParameterDetail followupStatus = parameterDetailDao
									.getParameterDetailByParamDtlCode(
											ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_INPROGRESS);
							dtl.setFollowupStatus(followupStatus);
//							dtl.setComplianceStatus(null); // request untuk hilang dari layar ketika beres verifikasi
						} else if (ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_CLOSE
								.equals(dtl.getComplianceStatus().getParameterDtlCode())) {
							ParameterDetail followupStatus = parameterDetailDao
									.getParameterDetailByParamDtlCode(
											ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE);
							dtl.setFollowupStatus(followupStatus);
//							dtl.setComplianceStatus(null); // request untuk hilang dari layar ketika beres verifikasi
						}
						
						EntityUtil.setUpdateInfo(dtl, user.getNik());
						
					}
				}
			}

			trcComplianceReviewDao.update(trcComplianceReview);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
	
	
	
}
