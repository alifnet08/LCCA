package com.wo.module.internalRegulationView.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.internalRegulationView.dao.InternalRegulationViewDao;
import com.wo.module.internalRegulationView.model.InternalRegulationView;

@Transactional
@Service("internalRegulationViewService")
public class InternalRegulationViewServiceImpl implements InternalRegulationViewService {
	
    @Autowired
    @Qualifier("internalRegulationViewDao")
    private InternalRegulationViewDao internalRegulationViewDao;


	public InternalRegulationViewDao getInternalRegulationViewDao() {
		return internalRegulationViewDao;
	}

	public void setInternalRegulationViewDao(InternalRegulationViewDao internalRegulationViewDao) {
		this.internalRegulationViewDao = internalRegulationViewDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<InternalRegulationView> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return internalRegulationViewDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return internalRegulationViewDao.searchCountData(searchCriteria);
	}
	
}
