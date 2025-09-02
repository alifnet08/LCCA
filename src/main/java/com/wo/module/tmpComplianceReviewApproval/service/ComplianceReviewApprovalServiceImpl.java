/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpComplianceReviewApproval.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.tmpComplianceReview.dao.TmpComplianceReviewDao;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReview;
import com.wo.module.tmpComplianceReviewApproval.dao.ComplianceReviewApprovalDao;
import com.wo.module.tmpComplianceReviewApproval.model.TmpComplianceReviewApproval;
import com.wo.module.tmpComplianceReviewApproval.vo.ComplianceReviewApprovalVO;
import com.wo.module.user.dao.UserDao;
import com.wo.module.user.model.User;

@Transactional
@Service("complianceReviewApprovalService")
public class ComplianceReviewApprovalServiceImpl implements ComplianceReviewApprovalService {
	@Autowired
	@Qualifier("complianceReviewApprovalDao")
	private ComplianceReviewApprovalDao complianceReviewApprovalDao;

	@Autowired
	@Qualifier("tmpComplianceReviewDao")
	private TmpComplianceReviewDao tmpComplianceReviewDao;

	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;

	public ComplianceReviewApprovalDao getComplianceReviewApprovalDao() {
		return complianceReviewApprovalDao;
	}

	public void setComplianceReviewApprovalDao(ComplianceReviewApprovalDao complianceReviewApprovalDao) {
		this.complianceReviewApprovalDao = complianceReviewApprovalDao;
	}

	public TmpComplianceReviewDao getTmpComplianceReviewDao() {
		return tmpComplianceReviewDao;
	}

	public void setTmpComplianceReviewDao(TmpComplianceReviewDao tmpComplianceReviewDao) {
		this.tmpComplianceReviewDao = tmpComplianceReviewDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ComplianceReviewApprovalVO> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return complianceReviewApprovalDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return complianceReviewApprovalDao.searchCountData(searchCriteria);
	}

	 @Transactional(rollbackOn = { Exception.class})
	public void processApprove(TmpComplianceReview tmpComplianceReview, String note, String nik, ParameterDetail status,
			String statusApp) throws Exception {
		try {
			TmpComplianceReview tmpComplianceReviewNew = tmpComplianceReviewDao
					.getById(tmpComplianceReview.getComplianceReviewId());
			tmpComplianceReviewNew.setStatus(status);

			tmpComplianceReviewNew.setLastUpdateBy(nik);
			tmpComplianceReviewNew.setLastUpdateDate(new Timestamp(new Date().getTime()));

			List<TmpComplianceReviewApproval> approvalList = new ArrayList<TmpComplianceReviewApproval>();

			TmpComplianceReviewApproval ra = new TmpComplianceReviewApproval();
			ra.setTmpComplianceReview(tmpComplianceReviewNew);
			User user = userDao.getUserByNik(nik);
			ra.setUser(user);
			ra.setApprovalDate(new Date());
			ra.setApprovalNote(note);
			ra.setApprovalStatus(statusApp);
			ra.setLastUpdateBy(nik);
			ra.setLastUpdateDate(new Timestamp(new Date().getTime()));
			ra.setCreatedBy(nik);
			ra.setCreationDate(new Timestamp(new Date().getTime()));
			ra.setDelId(new Long(0));
			ra.setEnabledFlag(Constants.CONSTANT_YES);
			approvalList.add(ra);

			if (tmpComplianceReviewNew.getTmpComplianceReviewApprovals() == null) {
				tmpComplianceReviewNew.setTmpComplianceReviewApprovals(approvalList);
			} else {
				tmpComplianceReviewNew.getTmpComplianceReviewApprovals().add(ra);
			}

			tmpComplianceReviewDao.update(tmpComplianceReviewNew);
			// regulationDao.procedureUpdateTmpTrackRecord(regulation.getRegulationId());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
