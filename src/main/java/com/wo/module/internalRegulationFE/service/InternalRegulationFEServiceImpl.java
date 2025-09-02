package com.wo.module.internalRegulationFE.service;

import java.util.List;

import javax.transaction.Transactional;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.internalRegulationFE.dao.InternalRegulationFEDao;
import com.wo.module.internalRegulationFE.vo.InternalRegulationFEVO;

@Transactional
@Service("internalRegulationFEService")
public class InternalRegulationFEServiceImpl implements InternalRegulationFEService {
	
    @Autowired
    @Qualifier("internalRegulationFEDao")
    private InternalRegulationFEDao internalRegulationFEDao;
    
   
    
	public InternalRegulationFEDao getInternalRegulationFEDao() {
		return internalRegulationFEDao;
	}

	public void setInternalRegulationFEDao(InternalRegulationFEDao internalRegulationFEDao) {
		this.internalRegulationFEDao = internalRegulationFEDao;
	}

	@SuppressWarnings("rawtypes")
	@Override    
    public List<InternalRegulationFEVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return internalRegulationFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
	@Override    
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return internalRegulationFEDao.searchCountData(searchCriteria);
    }
	
    
	
	
}
