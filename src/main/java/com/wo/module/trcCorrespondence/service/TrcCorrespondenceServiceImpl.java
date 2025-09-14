/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcCorrespondence.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.trcCorrespondence.dao.TrcCorrespondenceDao;
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupAttachment;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupAttendance;
import com.wo.module.trcCorrespondence.vo.TrcCorrespondenceSearchVo;

@Transactional
@Service("trcCorrespondenceService")
public class TrcCorrespondenceServiceImpl implements TrcCorrespondenceService {
	@Autowired
	@Qualifier("trcCorrespondenceDao")
	private TrcCorrespondenceDao trcCorrespondenceDao;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<TrcCorrespondenceSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return trcCorrespondenceDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return trcCorrespondenceDao.searchCountData(searchCriteria);
	}

	public void save(TrcCorrespondence entity) {
		trcCorrespondenceDao.save(entity);
	}

	public void update(TrcCorrespondence entity) {
		trcCorrespondenceDao.update(entity);
	}

	public void delete(TrcCorrespondence entity) {
		trcCorrespondenceDao.delete(entity);
	}

	public TrcCorrespondence findById(Long id) {
		return trcCorrespondenceDao.getById(id);
	}

	@Override
	public List<TrcCorrespondencePicFollowupAttendance> getDataPicFollowupAttendenceList(Long correspondenceId,
			Long crpdcPicConfirmId) {
		return trcCorrespondenceDao.getDataPicFollowupAttendenceList(correspondenceId, crpdcPicConfirmId);
	}

	@Override
	public List<TrcCorrespondencePicFollowupAttachment> getDataPicFollowupAttachmentList(Long correspondenceId,
			Long crpdcPicConfirmId) {
		return trcCorrespondenceDao.getDataPicFollowupAttachmentList(correspondenceId, crpdcPicConfirmId);
	}

}
