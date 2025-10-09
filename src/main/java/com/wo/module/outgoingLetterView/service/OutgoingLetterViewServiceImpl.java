package com.wo.module.outgoingLetterView.service;

import java.util.List;

import javax.transaction.Transactional;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.outgoingLetterView.dao.OutgoingLetterViewDao;
import com.wo.module.outgoingLetterView.model.OutgoingLetterView;

@Transactional
@Repository("outgoingLetterViewService")
public class OutgoingLetterViewServiceImpl implements OutgoingLetterViewService{

	@Autowired
    @Qualifier("outgoingLetterViewDao")
	private OutgoingLetterViewDao outgoingLetterViewDao;

	public OutgoingLetterViewDao getOutgoingLetterViewDao() {
		return outgoingLetterViewDao;
	}

	public void setOutgoingLetterViewDao(OutgoingLetterViewDao outgoingLetterViewDao) {
		this.outgoingLetterViewDao = outgoingLetterViewDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<OutgoingLetterView> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return outgoingLetterViewDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return outgoingLetterViewDao.searchCountData(searchCriteria);
	}

	@Override
	public OutgoingLetterView findById(Long idLong) {
		return outgoingLetterViewDao.findById(idLong);
	}
	
	
}
