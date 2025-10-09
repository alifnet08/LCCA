package com.wo.module.cpsaView.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.cpsaView.dao.CpsaPicViewDao;
import com.wo.module.cpsaView.vo.CpsaPicViewVo;

@Service("cpsaPicViewService")
@Transactional
public class CpsaPicViewServiceImpl implements CpsaPicViewService {

	@Qualifier("cpsaPicViewDao")
	private CpsaPicViewDao cpsaPicViewDao;
	
	
	public CpsaPicViewServiceImpl(CpsaPicViewDao cpsaPicViewDao) {
		super();
		this.cpsaPicViewDao = cpsaPicViewDao;
	}

	public CpsaPicViewDao getCpsaPicViewDao() {
		return cpsaPicViewDao;
	}

	public void setCpsaPicViewDao(CpsaPicViewDao cpsaPicViewDao) {
		this.cpsaPicViewDao = cpsaPicViewDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<CpsaPicViewVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return cpsaPicViewDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return cpsaPicViewDao.searchCountData(searchCriteria);
	}

	
}
