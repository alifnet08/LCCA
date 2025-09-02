package com.wo.module.internalRegulation.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.internalRegulation.dao.InternalRegulationDao;
import com.wo.module.internalRegulation.model.InternalRegulation;

@Transactional
@Service("internalRegulationService")
public class InternalRegulationServiceImpl implements InternalRegulationService {
	
    @Autowired
    @Qualifier("internalRegulationDao")
    private InternalRegulationDao internalRegulationDao;

	public InternalRegulationDao getInternalRegulationDao() {
		return internalRegulationDao;
	}

	public void setInternalRegulationDao(InternalRegulationDao internalRegulationDao) {
		this.internalRegulationDao = internalRegulationDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<InternalRegulation> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return internalRegulationDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return internalRegulationDao.searchCountData(searchCriteria);
	}

	@Override
	public Integer getCheckDataRegulation(Long regulationId) {
		return internalRegulationDao.getCheckDataRegulation(regulationId);
	}
        
}
