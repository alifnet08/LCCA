package com.wo.module.LitigationViewFE.service;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.LitigationViewFE.dao.LitigationViewFEDao;
import com.wo.module.LitigationViewFE.vo.LitigationAttachmentViewFEVo;
import com.wo.module.LitigationViewFE.vo.LitigationDetailViewFEVo;
import com.wo.module.LitigationViewFE.vo.LitigationViewFEVo;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.litigation.model.Litigation;

@Transactional
@Service("litigationViewFEService")
public class LitigationViewFEServiceImpl implements LitigationViewFEService, Serializable{

	private static final long serialVersionUID = 4988824911637236902L;

	@Autowired
	@Qualifier("litigationViewFEDao")
	private LitigationViewFEDao litigationViewFEDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<LitigationViewFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return litigationViewFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return litigationViewFEDao.searchCountData(searchCriteria);
	}

	@Override
	public Litigation findById(Long id) {
		return litigationViewFEDao.findById(id);
	}
	
	@Override
	public List<LitigationDetailViewFEVo> getLitigationDtlListsByLitigationId(Long litigationId) {
		return litigationViewFEDao.getLitigationDtlListsByLitigationId(litigationId);
	}

	@Override
	public List<LitigationAttachmentViewFEVo> getLitigationAttachListsByLitigationIdAndCourtType(Long litigationId,
			String courtType) {
		return litigationViewFEDao.getLitigationAttachListsByLitigationIdAndCourtType(litigationId, courtType);
	}
	
	public LitigationViewFEDao getLitigationViewFEDao() {
		return litigationViewFEDao;
	}

	public void setLitigationViewFEDao(LitigationViewFEDao litigationViewFEDao) {
		this.litigationViewFEDao = litigationViewFEDao;
	}

}
