package com.wo.module.trcAuditVerification.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.EntityUtil;
import com.wo.module.parameter.dao.ParameterDetailDao;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.trcAudit.dao.TrcAuditDao;
import com.wo.module.trcAudit.dao.TrcAuditPICFollowupDao;
import com.wo.module.trcAudit.model.TrcAudit;
import com.wo.module.trcAudit.model.TrcAuditPicFollowup;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupAttachment;
import com.wo.module.trcAuditVerification.dao.TrcAuditVerificationDao;
import com.wo.module.trcAuditVerification.vo.TrcAuditVerificationSearchVO;
import com.wo.module.user.dao.UserDao;
import com.wo.module.user.model.User;

@Transactional
@Service("trcAuditVerificationService")
public class TrcAuditVerificationServiceImpl implements TrcAuditVerificationService {
	@Autowired
	@Qualifier("trcAuditVerificationDao")
	private TrcAuditVerificationDao trcAuditVerificationDao;

	@Autowired
	@Qualifier("trcAuditDao")
	private TrcAuditDao trcAuditDao;
	
	@Autowired
	@Qualifier("trcAuditPICFollowupDao")
	private TrcAuditPICFollowupDao trcAuditPICFollowupDao;

	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;

	@Autowired
	@Qualifier("parameterDetailDao")
	private ParameterDetailDao parameterDetailDao;

	@SuppressWarnings("rawtypes")
	@Override
	public List<TrcAuditVerificationSearchVO> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return trcAuditVerificationDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return trcAuditVerificationDao.searchCountData(searchCriteria);
	}

	@Transactional(rollbackOn = { Exception.class })
	public void processConfirm(TrcAuditPicFollowup trcAuditPicFollowup, User user) throws Exception {
		try {

			if (trcAuditPicFollowup.getComplianceStatus() != null && trcAuditPicFollowup.getComplianceStatus().getParameterDtlCode() != null
					&& !trcAuditPicFollowup.getComplianceStatus().getParameterDtlCode().equals("")) {
				ParameterDetail complianceStatus = parameterDetailDao
						.getParameterDetailByParamDtlCode(trcAuditPicFollowup.getComplianceStatus().getParameterDtlCode());
				trcAuditPicFollowup.setComplianceStatus(complianceStatus);
				// trcAuditPicFollowup.setComplianceNote(trcAuditPicFollowup.getComplianceNote());
				trcAuditPicFollowup.setComplianceBy(user);
				trcAuditPicFollowup.setComplianceDate(new Date());

				if (ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_OPEN
						.equals(complianceStatus.getParameterDtlCode())) {
					ParameterDetail followupStatus = parameterDetailDao.getParameterDetailByParamDtlCode(
							ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_INPROGRESS);
					trcAuditPicFollowup.setFollowupStatus(followupStatus);
				}

				EntityUtil.setUpdateInfo(trcAuditPicFollowup, user.getNik());
			}
			
			trcAuditPICFollowupDao.update(trcAuditPicFollowup);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
	
	@Transactional(rollbackOn = { Exception.class })
	public void processConfirm(TrcAudit trcAudit, User user) throws Exception {
		try {
			ParameterDetail followupLetterAttachDoc = parameterDetailDao.getParameterDetailByParamDtlCode(
					ParameterDetail.PARAM_DET_CODE_LETTER_ATTACH_DOC);
			/*trcAudit.getTrcAuditPicFollowups().forEach(picFollowup -> {
				if (picFollowup.getComplianceStatus() != null && picFollowup.getComplianceStatus().getParameterDtlCode() != null
						&& !picFollowup.getComplianceStatus().getParameterDtlCode().equals("")) {
					ParameterDetail complianceStatus = null;
					try {
						complianceStatus = parameterDetailDao
								.getParameterDetailByParamDtlCode(picFollowup.getComplianceStatus().getParameterDtlCode());
					} catch (Exception e) {
						e.printStackTrace();
					}
					picFollowup.setComplianceStatus(complianceStatus);
					// trcAuditPicFollowup.setComplianceNote(trcAuditPicFollowup.getComplianceNote());
					picFollowup.setComplianceBy(user);
					picFollowup.setComplianceDate(new Date());
	
					if (ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_OPEN
							.equals(complianceStatus.getParameterDtlCode())) {
						ParameterDetail followupStatus = null;
						try {
							followupStatus = parameterDetailDao.getParameterDetailByParamDtlCode(
									ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_INPROGRESS);
						} catch (Exception e) {
							e.printStackTrace();
						}
						picFollowup.setFollowupStatus(followupStatus);
					} else if (ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_CLOSE
							.equals(complianceStatus.getParameterDtlCode())) {
						ParameterDetail followupStatus = null;
						try {
							followupStatus = parameterDetailDao.getParameterDetailByParamDtlCode(
									ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE);
						} catch (Exception e) {
							e.printStackTrace();
						}
						picFollowup.setFollowupStatus(followupStatus);
					}
					
					List<TrcAuditPicFollowupAttachment> list = picFollowup.getTrcAuditPicFollowupAttachment().stream()
							.filter(att -> {
								if (att.getAttachmentType().getParameterDtlCode()
										.equals(ParameterDetail.PARAM_DET_CODE_LETTER_ATTACH_DOC)) {
									return true;
								} else {
									return false;
								}
							}).collect(Collectors.toList());
					
					picFollowup.getTrcAuditPicFollowupAttachment().removeAll(list);
					
					
					for (int x = 0; x < picFollowup.getUploadedFilesAttachmentLetter().size(); x++) {
						UploadedFileWO u = picFollowup.getUploadedFilesAttachmentLetter().get(x);

						TrcAuditPicFollowupAttachment dtl = new TrcAuditPicFollowupAttachment();
						dtl.setTrcAuditPicFollowup(picFollowup);
						dtl.setAttachmentType(followupLetterAttachDoc);
						dtl.setAttachmentFile(u.getFileName());
						dtl.setFileId(u.getFileId());
						dtl.setFileSize(u.getFileSize());
						
						EntityUtil.setCreationInfo(dtl, user.getNik());

						picFollowup.getTrcAuditPicFollowupAttachment().add(dtl);
					}
					
					EntityUtil.setUpdateInfo(picFollowup, user.getNik());
				}
			});*/
			
			trcAuditDao.update(trcAudit);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public ParameterDetailDao getParameterDetailDao() {
		return parameterDetailDao;
	}

	public void setParameterDetailDao(ParameterDetailDao parameterDetailDao) {
		this.parameterDetailDao = parameterDetailDao;
	}

	public TrcAuditVerificationDao getTrcAuditVerificationDao() {
		return trcAuditVerificationDao;
	}

	public void setTrcAuditVerificationDao(TrcAuditVerificationDao trcAuditVerificationDao) {
		this.trcAuditVerificationDao = trcAuditVerificationDao;
	}

	public TrcAuditPICFollowupDao getTrcAuditPICFollowupDao() {
		return trcAuditPICFollowupDao;
	}

	public void setTrcAuditPICFollowupDao(TrcAuditPICFollowupDao trcAuditPICFollowupDao) {
		this.trcAuditPICFollowupDao = trcAuditPICFollowupDao;
	}

}
