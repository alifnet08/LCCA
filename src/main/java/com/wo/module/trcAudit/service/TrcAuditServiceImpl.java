/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcAudit.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.wo.module.trcAudit.vo.TrcAuditVO;
import com.wo.module.user.dao.UserDao;
import com.wo.module.user.model.User;

@Transactional
@Service("trcAuditService")
public class TrcAuditServiceImpl implements TrcAuditService {
	@Autowired
	@Qualifier("trcAuditDao")
	private TrcAuditDao trcAuditDao;
	
	@Autowired
	@Qualifier("trcAuditPICFollowupDao")
	private TrcAuditPICFollowupDao trcAuditPICFollowupDao;

	@Autowired
	@Qualifier("parameterDetailDao")
	private ParameterDetailDao parameterDetailDao;

	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;

	@SuppressWarnings("rawtypes")
	@Override

	public List<TrcAuditVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return trcAuditDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override

	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return trcAuditDao.searchCountData(searchCriteria);
	}

	@SuppressWarnings("unused")
	@Transactional(rollbackOn = { Exception.class })
	public void processConfirm(TrcAuditPicFollowup picFollowup, List<UploadedFileWO> uploadFiles, List<UploadedFileWO> uploadAttachmentLetterFiles
			, User user) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
			ParameterDetail followupStatus = parameterDetailDao.getParameterDetailByParamDtlCode(
					ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE);
			
			ParameterDetail followupFollowupAttachDoc = parameterDetailDao.getParameterDetailByParamDtlCode(
					ParameterDetail.PARAM_DET_CODE_FOLLOWUP_ATTACH_DOC);
			
			ParameterDetail followupLetterAttachDoc = parameterDetailDao.getParameterDetailByParamDtlCode(
					ParameterDetail.PARAM_DET_CODE_LETTER_ATTACH_DOC);
			
			picFollowup.setConfirmationDate(new Date());
			picFollowup.setFollowupStatus(followupStatus);
			picFollowup.setFollowupBy(user);
			
			if(picFollowup.getAuditPicFollowupId() != null) {
				EntityUtil.setUpdateInfo(picFollowup, user.getNik());
			}else {
				EntityUtil.setCreationInfo(picFollowup, user.getNik());
			}
			
			
			picFollowup.getTrcAuditPicFollowupAttachment().clear();
			
			picFollowup.getTrcAuditPicFollowupAttachment()
					.addAll(addAttachment(picFollowup, uploadFiles, user, followupFollowupAttachDoc));
			picFollowup.getTrcAuditPicFollowupAttachment()
					.addAll(addAttachment(picFollowup, uploadAttachmentLetterFiles, user, followupLetterAttachDoc));

			EntityUtil.setUpdateInfo(picFollowup, user.getNik());
			System.out.println("masuk " + picFollowup.getFollowupStatus());
			trcAuditPICFollowupDao.update(picFollowup);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private List<TrcAuditPicFollowupAttachment> addAttachment(TrcAuditPicFollowup picFollowup, List<UploadedFileWO> uploadFiles, User user,
			ParameterDetail attachmentType) {
		List<TrcAuditPicFollowupAttachment> listDtl = new ArrayList<TrcAuditPicFollowupAttachment>();
		for (int x = 0; x < uploadFiles.size(); x++) {
			UploadedFileWO u = uploadFiles.get(x);

			TrcAuditPicFollowupAttachment dtl = new TrcAuditPicFollowupAttachment();
			dtl.setTrcAuditPicFollowup(picFollowup);
			dtl.setAttachmentType(attachmentType);
			dtl.setAttachmentFile(u.getFileName());
			dtl.setFileId(u.getFileId());
			dtl.setFileSize(u.getFileSize());
			
			EntityUtil.setCreationInfo(dtl, user.getNik());

			listDtl.add(dtl);
		}
		
		return listDtl;
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

	public TrcAuditDao getTrcAuditDao() {
		return trcAuditDao;
	}

	public void setTrcAuditDao(TrcAuditDao trcAuditDao) {
		this.trcAuditDao = trcAuditDao;
	}

	@Override
	public TrcAudit findById(Long id) {
		return trcAuditDao.findById(id);
	}

	public TrcAuditPICFollowupDao getTrcAuditPICFollowupDao() {
		return trcAuditPICFollowupDao;
	}

	public void setTrcAuditPICFollowupDao(TrcAuditPICFollowupDao trcAuditPICFollowupDao) {
		this.trcAuditPICFollowupDao = trcAuditPICFollowupDao;
	}

}
