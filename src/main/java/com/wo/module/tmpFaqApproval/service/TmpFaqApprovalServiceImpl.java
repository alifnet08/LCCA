package com.wo.module.tmpFaqApproval.service;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.faq.model.TmpFaq;
import com.wo.module.tmpFaqApproval.dao.TmpFaqApprovalDao;

@Transactional
@Service("tmpFaqApprovalService")
public class TmpFaqApprovalServiceImpl implements TmpFaqApprovalService, Serializable{

	private static final long serialVersionUID = -1629030002611734883L;

	@Autowired
	@Qualifier("tmpFaqApprovalDao")
	private TmpFaqApprovalDao tmpFaqApprovalDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<TmpFaq> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return tmpFaqApprovalDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return tmpFaqApprovalDao.searchCountData(searchCriteria);
	}

	@Override
	public void update(TmpFaq tmpFaq) {
		tmpFaqApprovalDao.update(tmpFaq);
	}

	@Override
	public TmpFaq findById(Long id) {
		return tmpFaqApprovalDao.findById(id);
	}

	public TmpFaqApprovalDao getTmpFaqApprovalDao() {
		return tmpFaqApprovalDao;
	}

	public void setTmpFaqApprovalDao(TmpFaqApprovalDao tmpFaqApprovalDao) {
		this.tmpFaqApprovalDao = tmpFaqApprovalDao;
	}

}
