/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcComplianceReview.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.EntityUtil;
import com.wo.module.complianceTestingMockup.vo.ComplianceTestingVO;
import com.wo.module.parameter.dao.ParameterDetailDao;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.trcComplianceReview.dao.TrcComplianceReviewDao;
import com.wo.module.trcComplianceReview.dao.TrcComplianceReviewPicFollowupDao;
import com.wo.module.trcComplianceReview.model.TrcComplianceReview;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowup;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupPoints;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupPointsAttachment;
import com.wo.module.trcComplianceReview.vo.TrcComplianceReviewVO;
import com.wo.module.user.model.User;

@Transactional
@Service("trcComplianceReviewService")
public class TrcComplianceReviewServiceImpl implements TrcComplianceReviewService {
	@Autowired
	@Qualifier("trcComplianceReviewDao")
	private TrcComplianceReviewDao trcComplianceReviewDao;
	
	@Autowired
	@Qualifier("trcComplianceReviewPicFollowupDao")
	private TrcComplianceReviewPicFollowupDao trcComplianceReviewPicFollowupDao;

	public TrcComplianceReviewDao getTrcComplianceReviewDao() {
		return trcComplianceReviewDao;
	}

	@Autowired
	@Qualifier("parameterDetailDao")
	private ParameterDetailDao parameterDetailDao;

	public ParameterDetailDao getParameterDetailDao() {
		return parameterDetailDao;
	}

	public void setParameterDetailDao(ParameterDetailDao parameterDetailDao) {
		this.parameterDetailDao = parameterDetailDao;
	}

	public void setTrcComplianceReviewDao(TrcComplianceReviewDao trcComplianceReviewDao) {
		this.trcComplianceReviewDao = trcComplianceReviewDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<TrcComplianceReviewVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return trcComplianceReviewDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return trcComplianceReviewDao.searchCountData(searchCriteria);
	}

	@Override
	public TrcComplianceReview findById(Long id) {
		return trcComplianceReviewDao.findById(id);
	}

	@SuppressWarnings("unused")
	@Override
	public void update(TrcComplianceReview trcComplianceReview, Long complianceReviewPICFollowupTrcId,
			String keterangan, Date followupDate, List<UploadedFileWO> uploadedFilesEvidence, User user) throws Exception {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

			TrcComplianceReview trcComplianceReviewNew = trcComplianceReviewDao
					.getById(trcComplianceReview.getComplianceReviewId());

			trcComplianceReviewNew.setLastUpdateBy(user.getNik());
			trcComplianceReviewNew.setLastUpdateDate(new Timestamp(new Date().getTime()));

//			if (trcComplianceReviewNew.getTrcComplianceReviewPicFollowups() != null) {
//				for (int i = 0; i < trcComplianceReviewNew.getTrcComplianceReviewPicFollowups().size(); i++) {
//					TrcComplianceReviewPicFollowup pft = (TrcComplianceReviewPicFollowup) trcComplianceReviewNew
//							.getTrcComplianceReviewPicFollowups().get(i);
//
//					if (pft.getComplianceReviewPicFollowupId() == complianceReviewPICFollowupTrcId.longValue()) {
////						pft.setFollowupDate(followupDate);
////						pft.setFollowupNote(keterangan);
////						pft.setConfirmationDate(new Date());
//						ParameterDetail followupStatus = parameterDetailDao.getParameterDetailByParamDtlCode(
//						ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE);
//						pft.setFollowupStatus(followupStatus);
////						pft.setFollowupBy(user);
//
//						List<TrcComplianceReviewPicFollowupPointsAttachment> listDtl = new ArrayList<TrcComplianceReviewPicFollowupPointsAttachment>();
//
//						for (int x = 0; x < uploadedFilesEvidence.size(); x++) {
//							UploadedFileWO u = uploadedFilesEvidence.get(x);
//
//							TrcComplianceReviewPicFollowupPointsAttachment dtl = new TrcComplianceReviewPicFollowupPointsAttachment();
////							dtl.setTrcComplianceReviewPicFollowup(pft);
//							dtl.setAttachmentFile(u.getFileName());
//							dtl.setCreatedBy(user.getNik());
//							dtl.setCreationDate(new Timestamp(new Date().getTime()));
//							dtl.setDelId(new Long(0));
//							dtl.setFileId(u.getFileId());
//							dtl.setFileSize(u.getFileSize());
//							dtl.setEnabledFlag(Constants.CONSTANT_YES);
//
//							listDtl.add(dtl);
//						}
//
////						if (pft.getTrcComplianceReviewPicFollowupAttachments() == null) {
////							pft.setTrcComplianceReviewPicFollowupAttachments(listDtl);
////						} else {
////							pft.getTrcComplianceReviewPicFollowupAttachments().clear();
////							pft.getTrcComplianceReviewPicFollowupAttachments().addAll(listDtl);
////						}
//					}
//
//				}
//			}

			trcComplianceReviewDao.update(trcComplianceReviewNew);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void update(TrcComplianceReview trcComplianceReview, TrcComplianceReviewPicFollowup trcComplianceReviewPicFollowup, User user) throws Exception {
		trcComplianceReviewPicFollowup.setComplianceStatus(null);
		trcComplianceReviewPicFollowup.getTrcComplianceReviewPicFollowupPoints().forEach(fol -> {
			if(fol.getTrcComplianceReviewPicFollowupPointsAttachments() == null)
				fol.setTrcComplianceReviewPicFollowupPointsAttachments(new ArrayList<TrcComplianceReviewPicFollowupPointsAttachment>());
			
			fol.getUploadedFilesEvidence().forEach(file -> {
				boolean exist = false;
				for(TrcComplianceReviewPicFollowupPointsAttachment att : fol.getTrcComplianceReviewPicFollowupPointsAttachments()) {
					if(att.getFileId().equals(file.getFileId())) {
						exist = true;
						break;
					}
				}
				
				if(!exist) { //jika tidak ada
					TrcComplianceReviewPicFollowupPointsAttachment dtl = new TrcComplianceReviewPicFollowupPointsAttachment();
					dtl.setTrcComplianceReviewPicFollowupPoints(fol);
					dtl.setAttachmentFile(file.getFileName());
					dtl.setFileId(file.getFileId());
					dtl.setFileSize(file.getFileSize());
					fol.getTrcComplianceReviewPicFollowupPointsAttachments().add(dtl);
					EntityUtil.setCreationInfo(dtl, user.getNik());
				}
				
			});
			
			// jika attachment tidak ada di databse, delete di database
			boolean exist = false;
			List<TrcComplianceReviewPicFollowupPointsAttachment> toBeDel = new ArrayList<TrcComplianceReviewPicFollowupPointsAttachment>();
			for(TrcComplianceReviewPicFollowupPointsAttachment att : fol.getTrcComplianceReviewPicFollowupPointsAttachments()) {
				for(UploadedFileWO wo : fol.getUploadedFilesEvidence()) {
					if(att.getFileId().equals(wo.getFileId())) {
						exist = true;
						break;
					}else {
						exist = false;
					}
				}
				
				if(!exist) {
					toBeDel.add(att);
				}
			}
			fol.getTrcComplianceReviewPicFollowupPointsAttachments().removeAll(toBeDel);
			
			if (fol.getFollowupDate() != null) {
				fol.setFollowupBy(user);
				fol.setConfirmationDate(new Date());
			}
			
//			try {
//				if(fol.getUploadedFilesEvidence() != null && fol.getUploadedFilesEvidence().size() > 0 && fol.getFollowupDate() != null) {
//					ParameterDetail followupStatus = parameterDetailDao.getParameterDetailByParamDtlCode(
//							ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE);
//							trcComplianceReviewPicFollowup.setFollowupStatus(followupStatus);
//				}else {
//					ParameterDetail followupStatus = parameterDetailDao.getParameterDetailByParamDtlCode(
//							ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_INPROGRESS);
//							trcComplianceReviewPicFollowup.setFollowupStatus(followupStatus);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
			if(fol.getComplianceReviewPicFollowupPointsId() != null) {
				EntityUtil.setUpdateInfo(fol, user.getNik());
			}else {
				EntityUtil.setCreationInfo(fol, user.getNik());
			}
		});
		
		//cek jika semua udah diisi evidence nya, berarti pic done
		for(TrcComplianceReviewPicFollowupPoints p : trcComplianceReviewPicFollowup.getTrcComplianceReviewPicFollowupPoints())
		{
			try {
				if(p.getUploadedFilesEvidence() == null || p.getUploadedFilesEvidence().isEmpty()) {
					ParameterDetail followupStatus = parameterDetailDao.getParameterDetailByParamDtlCode(
							ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_INPROGRESS);
							trcComplianceReviewPicFollowup.setFollowupStatus(followupStatus);
					break;
				}else {
					ParameterDetail followupStatus = parameterDetailDao.getParameterDetailByParamDtlCode(
					ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE);
					trcComplianceReviewPicFollowup.setFollowupStatus(followupStatus);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
				
		EntityUtil.setUpdateInfo(trcComplianceReviewPicFollowup, user.getNik());
		trcComplianceReviewPicFollowupDao.update(trcComplianceReviewPicFollowup);
		
		EntityUtil.setUpdateInfo(trcComplianceReview, user.getNik());
		trcComplianceReviewDao.update(trcComplianceReview);
	}

	@SuppressWarnings("unused")
	private List<TrcComplianceReviewPicFollowupPointsAttachment> setFollowupAttachment(User user,
			TrcComplianceReviewPicFollowupPoints fol, UploadedFileWO file) {
		List<TrcComplianceReviewPicFollowupPointsAttachment> listDtl = new ArrayList<TrcComplianceReviewPicFollowupPointsAttachment>();
		TrcComplianceReviewPicFollowupPointsAttachment dtl = new TrcComplianceReviewPicFollowupPointsAttachment();
		dtl.setTrcComplianceReviewPicFollowupPoints(fol);
		dtl.setAttachmentFile(file.getFileName());
		dtl.setFileId(file.getFileId());
		dtl.setFileSize(file.getFileSize());
		EntityUtil.setCreationInfo(dtl, user.getNik());
		
		listDtl.add(dtl);
		return listDtl;
	}

	public TrcComplianceReviewPicFollowupDao getTrcComplianceReviewPicFollowupDao() {
		return trcComplianceReviewPicFollowupDao;
	}

	public void setTrcComplianceReviewPicFollowupDao(TrcComplianceReviewPicFollowupDao trcComplianceReviewPicFollowupDao) {
		this.trcComplianceReviewPicFollowupDao = trcComplianceReviewPicFollowupDao;
	}
}
