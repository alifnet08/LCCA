package com.wo.module.litigationView.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.litigationView.dao.LitigationViewAttachmentDetailDao;
import com.wo.module.litigationView.dao.LitigationViewDao;
import com.wo.module.litigationView.vo.LitigationViewAttachmentVo;
import com.wo.module.litigationView.vo.LitigationViewVo;

@Transactional
@Service("litigationViewService")
public class LitigationViewServiceImpl implements LitigationViewService{

	@Autowired
	@Qualifier("litigationViewDao")
	private LitigationViewDao litigationViewDao;
	
	@Autowired
	@Qualifier("litigationViewAttachmentDetailDao")	
	private LitigationViewAttachmentDetailDao litigationViewAttachmentDetailDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<LitigationViewVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return litigationViewDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return litigationViewDao.searchCountData(searchCriteria);
	}

	@Override
	public LitigationViewVo getData(Long litigationId) {
		return litigationViewDao.getData(litigationId);
	}
	
	@Override
	public List<LitigationViewAttachmentVo> getLitigationAttachmentList(Long litigationId, Long attachId,
			String attachCode) {
		return litigationViewAttachmentDetailDao.getLitigationAttachmentList(litigationId, attachId, attachCode);
	}
	
	public LitigationViewDao getLitigationViewDao() {
		return litigationViewDao;
	}

	public void setLitigationViewDao(LitigationViewDao litigationViewDao) {
		this.litigationViewDao = litigationViewDao;
	}

	public LitigationViewAttachmentDetailDao getLitigationViewAttachmentDetailDao() {
		return litigationViewAttachmentDetailDao;
	}

	public void setLitigationViewAttachmentDetailDao(LitigationViewAttachmentDetailDao litigationViewAttachmentDetailDao) {
		this.litigationViewAttachmentDetailDao = litigationViewAttachmentDetailDao;
	}

}
