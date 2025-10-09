package com.wo.module.regulationSocializationView.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.regulationSocializationView.dao.RegulationSocializationViewDao;
import com.wo.module.regulationSocializationView.vo.RegulationSocializationViewVO;


@Transactional
@Service("regulationSocializationViewService")
public class RegulationSocializationViewServiceImpl implements RegulationSocializationViewService {
	
    @Autowired
    @Qualifier("regulationSocializationViewDao")
    private RegulationSocializationViewDao regulationSocializationViewDao;
	

	public RegulationSocializationViewDao getRegulationSocializationViewDao() {
		return regulationSocializationViewDao;
	}

	public void setRegulationSocializationViewDao(RegulationSocializationViewDao regulationSocializationViewDao) {
		this.regulationSocializationViewDao = regulationSocializationViewDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public List<RegulationSocializationViewVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return regulationSocializationViewDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return regulationSocializationViewDao.searchCountData(searchCriteria);
    }
	
        
}
