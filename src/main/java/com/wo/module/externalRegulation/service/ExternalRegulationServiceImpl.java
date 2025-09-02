/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.externalRegulation.dao.ExternalRegulationDao;
import com.wo.module.externalRegulation.model.ExternalRegulation;

@Transactional
@Service("externalRegulationService")
public class ExternalRegulationServiceImpl implements ExternalRegulationService {
    @Autowired
    @Qualifier("externalRegulationDao")
    private ExternalRegulationDao externalRegulationDao;

	public ExternalRegulationDao getExternalRegulationDao() {
		return externalRegulationDao;
	}

	public void setExternalRegulationDao(ExternalRegulationDao externalRegulationDao) {
		this.externalRegulationDao = externalRegulationDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public List<ExternalRegulation> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return externalRegulationDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return externalRegulationDao.searchCountData(searchCriteria);
    }
	
    
    public String getQueryTrackRecord(Long regulationId,String language){
    	return externalRegulationDao.getQueryTrackRecord(regulationId, language);
    }
        
}
