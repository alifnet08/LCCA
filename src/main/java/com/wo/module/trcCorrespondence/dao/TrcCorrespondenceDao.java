package com.wo.module.trcCorrespondence.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupAttachment;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupAttendance;
import com.wo.module.trcCorrespondence.vo.TrcCorrespondenceSearchVo;

public interface TrcCorrespondenceDao extends  GenericDAO<TrcCorrespondence, Long>, RetrieverDataPage<TrcCorrespondenceSearchVo> {
	
	public List<TrcCorrespondencePicFollowupAttendance> getDataPicFollowupAttendenceList (Long correspondenceId, Long crpdcPicConfirmId);
	public List<TrcCorrespondencePicFollowupAttachment> getDataPicFollowupAttachmentList (Long correspondenceId, Long crpdcPicConfirmId);
	
	public Integer checkComplianceCloseById (Long correspondenceId);
	public Integer checkPicFollowupInvitationById (Long correspondenceId);

}
