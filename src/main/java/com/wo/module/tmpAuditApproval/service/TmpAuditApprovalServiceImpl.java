/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpAuditApproval.service;

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
import com.wo.module.parameter.dao.ParameterDetailDao;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.tmpAudit.dao.TmpAuditDao;
import com.wo.module.tmpAudit.model.TmpAudit;
import com.wo.module.tmpAudit.model.TmpAuditApproval;
import com.wo.module.tmpAuditApproval.dao.TmpAuditApprovalDao;
import com.wo.module.tmpAuditApproval.vo.TmpAuditApprovalVO;
import com.wo.module.user.dao.UserDao;
import com.wo.module.user.model.User;

@Transactional
@Service("tmpAuditApprovalService")
public class TmpAuditApprovalServiceImpl implements TmpAuditApprovalService {
	@Autowired
	@Qualifier("tmpAuditApprovalDao")
	private TmpAuditApprovalDao tmpAuditApprovalDao;
	
	@Autowired
	@Qualifier("tmpAuditDao")
	private TmpAuditDao tmpAuditDao;
	
	@Autowired
	@Qualifier("parameterDetailDao")
	private ParameterDetailDao parameterDetailDao;
	
	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;

	@SuppressWarnings("rawtypes")
	@Override
	public List<TmpAuditApprovalVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return tmpAuditApprovalDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return tmpAuditApprovalDao.searchCountData(searchCriteria);
	}

	@Transactional(rollbackOn = { Exception.class })
	public void processApprove(TmpAudit tmpAudit, String note, String nik, String statusReg, String statusApp) {
		try {
			TmpAudit tmpAuditDb = tmpAuditDao.getById(tmpAudit.getAuditId());
			ParameterDetail pdStatus = parameterDetailDao.getParameterDetailByParamDtlCode(statusReg);
			tmpAuditDb.setStatus(pdStatus);
			tmpAuditDb.setLastUpdateBy(nik);
			tmpAuditDb.setLastUpdateDate(new Timestamp(new Date().getTime()));

			List<TmpAuditApproval> approvalList = new ArrayList<TmpAuditApproval>();

			TmpAuditApproval ra = new TmpAuditApproval();
			ra.setTmpAudit(tmpAuditDb);
			User user = userDao.getUserByNik(nik);
			ra.setUser(user);
			ra.setApprovalDate(new Date());
			ra.setApprovalNote(note);
			
			ParameterDetail pdApproval = parameterDetailDao.getParameterDetailByParamDtlCode(statusApp);
			ra.setApprovalStatus(pdApproval);
			
			ra.setLastUpdateBy(nik);
			ra.setLastUpdateDate(new Timestamp(new Date().getTime()));
			ra.setCreatedBy(nik);
			ra.setCreationDate(new Timestamp(new Date().getTime()));
			ra.setDelId(new Long(0));
			ra.setEnabledFlag(Constants.CONSTANT_YES);
			approvalList.add(ra);

			if (tmpAuditDb.getTmpAuditApprovals() == null) {
				tmpAuditDb.setTmpAuditApprovals(approvalList);
			} else {
				tmpAuditDb.getTmpAuditApprovals().add(ra);
			}

			tmpAuditDao.update(tmpAuditDb);
			// regulationDao.procedureUpdateTmpTrackRecord(regulation.getRegulationId());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public TmpAuditApprovalDao getTmpAuditApprovalDao() {
		return tmpAuditApprovalDao;
	}

	public void setTmpAuditApprovalDao(TmpAuditApprovalDao tmpAuditApprovalDao) {
		this.tmpAuditApprovalDao = tmpAuditApprovalDao;
	}

}
