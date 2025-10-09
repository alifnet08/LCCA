/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.picFollowupConfirmation.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.parameter.dao.ParameterDetailDao;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.picFollowupConfirmation.dao.PICFollowupConfirmationDao;
import com.wo.module.picFollowupConfirmation.vo.PICFollowupConfirmationVO;
import com.wo.module.regulationSocialization.dao.SocializationTrcDao;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupAttachmentTrc;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupTrc;
import com.wo.module.regulationSocialization.model.SocializationTrc;
import com.wo.module.regulationSocializationApproval.dao.SocializationApprovalTmpDao;
import com.wo.module.user.dao.UserDao;
import com.wo.module.user.model.User;

@Transactional
@Service("picFollowupConfirmationService")
public class PICFollowupConfirmationServiceImpl implements PICFollowupConfirmationService {
	@Autowired
	@Qualifier("picFollowupConfirmationDao")
	private PICFollowupConfirmationDao picFollowupConfirmationDao;

	@Autowired
	@Qualifier("socializationTrcDao")
	private SocializationTrcDao socializationTrcDao;

	@Autowired
	@Qualifier("parameterDetailDao")
	private ParameterDetailDao parameterDetailDao;

	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;

	@Autowired
	@Qualifier("socializationApprovalTmpDao")
	private SocializationApprovalTmpDao socializationApprovalTmpDao;

	@SuppressWarnings("rawtypes")
	@Override

	public List<PICFollowupConfirmationVO> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return picFollowupConfirmationDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
    
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return picFollowupConfirmationDao.searchCountData(searchCriteria);
    }
	
    @SuppressWarnings("unused")
	@Transactional(rollbackOn = { Exception.class})
	public void processConfirm(SocializationTrc socializationTrc, 
			Long socializationPICFollowupTrcId, 
			String keterangan,Date followupDate,List<UploadedFileWO> uploadFiles,  User user) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

			SocializationTrc socializationNew = socializationTrcDao.getById(socializationTrc.getSocializationId());

			socializationNew.setLastUpdateBy(user.getNik());
			socializationNew.setLastUpdateDate(new Timestamp(new Date().getTime()));

			if (socializationNew.getSocializationPICFollowupTrcs() != null) {
				for (int i = 0; i < socializationNew.getSocializationPICFollowupTrcs().size(); i++) {
					SocializationPICFollowupTrc pft = (SocializationPICFollowupTrc) socializationNew
							.getSocializationPICFollowupTrcs().get(i);

					if(pft.getSocializationPicFollowupId() == socializationPICFollowupTrcId.longValue()) {
						pft.setFollowupDate(followupDate);
						pft.setFollowupNote(keterangan);
						pft.setConfirmationDate(new Date());
						ParameterDetail followupStatus = parameterDetailDao.getParameterDetailByParamDtlCode(
								ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE);
						pft.setFollowupStatus(followupStatus);
						pft.setFollowupBy(user);

						List<SocializationPICFollowupAttachmentTrc> listDtl = new ArrayList<SocializationPICFollowupAttachmentTrc>();

						for (int x = 0; x < uploadFiles.size(); x++) {
							UploadedFileWO u = uploadFiles.get(x);
							
							SocializationPICFollowupAttachmentTrc dtl = new SocializationPICFollowupAttachmentTrc();
							dtl.setSocializationPICFollowupTrc(pft);
							dtl.setAttachmentFile(u.getFileName());
							dtl.setCreatedBy(user.getNik());
							dtl.setCreationDate(new Timestamp(new Date().getTime()));
							dtl.setDelId(new Long(0));
							dtl.setFileId(u.getFileId());
							dtl.setFileSize(u.getFileSize());
							dtl.setEnabledFlag(Constants.CONSTANT_YES);
							
							listDtl.add(dtl);
						}

						if (pft.getSocializationPICFollowupAttachmentTrcs() == null) {
							pft.setSocializationPICFollowupAttachmentTrcs(listDtl);
						} else {
							pft.getSocializationPICFollowupAttachmentTrcs().clear();
							pft.getSocializationPICFollowupAttachmentTrcs().addAll(listDtl);
						}
					}

				}
			}

			socializationTrcDao.update(socializationNew);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public PICFollowupConfirmationDao getPicFollowupConfirmationDao() {
		return picFollowupConfirmationDao;
	}

	public void setPicFollowupConfirmationDao(PICFollowupConfirmationDao picFollowupConfirmationDao) {
		this.picFollowupConfirmationDao = picFollowupConfirmationDao;
	}

	public SocializationTrcDao getSocializationTrcDao() {
		return socializationTrcDao;
	}

	public void setSocializationTrcDao(SocializationTrcDao socializationTrcDao) {
		this.socializationTrcDao = socializationTrcDao;
	}

	public SocializationApprovalTmpDao getSocializationApprovalTmpDao() {
		return socializationApprovalTmpDao;
	}

	public void setSocializationApprovalTmpDao(SocializationApprovalTmpDao socializationApprovalTmpDao) {
		this.socializationApprovalTmpDao = socializationApprovalTmpDao;
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

}
