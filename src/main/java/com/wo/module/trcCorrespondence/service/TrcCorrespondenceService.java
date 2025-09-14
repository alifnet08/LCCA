/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcCorrespondence.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupAttachment;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupAttendance;
import com.wo.module.trcCorrespondence.vo.TrcCorrespondenceSearchVo;

public interface TrcCorrespondenceService extends RetrieverDataPage<TrcCorrespondenceSearchVo> {
	public void save(TrcCorrespondence entity);

	public void update(TrcCorrespondence entity);

	public void delete(TrcCorrespondence entity);

	public TrcCorrespondence findById(Long id);

	public List<TrcCorrespondencePicFollowupAttendance> getDataPicFollowupAttendenceList(Long correspondenceId,
			Long crpdcPicConfirmId);

	public List<TrcCorrespondencePicFollowupAttachment> getDataPicFollowupAttachmentList(Long correspondenceId,
			Long crpdcPicConfirmId);
}
