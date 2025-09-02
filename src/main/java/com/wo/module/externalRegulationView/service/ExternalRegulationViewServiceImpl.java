package com.wo.module.externalRegulationView.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.externalRegulationView.dao.ExternalRegulationViewDao;
import com.wo.module.externalRegulationView.model.ExternalRegulationView;

@Transactional
@Service("externalRegulationViewService")
public class ExternalRegulationViewServiceImpl implements ExternalRegulationViewService {
	
    @Autowired
    @Qualifier("externalRegulationViewDao")
    private ExternalRegulationViewDao externalRegulationViewDao;

	public ExternalRegulationViewDao getExternalRegulationViewDao() {
		return externalRegulationViewDao;
	}

	public void setExternalRegulationViewDao(ExternalRegulationViewDao externalRegulationViewDao) {
		this.externalRegulationViewDao = externalRegulationViewDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public List<ExternalRegulationView> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return externalRegulationViewDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return externalRegulationViewDao.searchCountData(searchCriteria);
    }
	
        
}
