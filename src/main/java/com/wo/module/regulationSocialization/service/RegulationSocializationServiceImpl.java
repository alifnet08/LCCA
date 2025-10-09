/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationSocialization.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.regulationSocialization.dao.RegulationSocializationDao;
import com.wo.module.regulationSocialization.vo.RegulationSocializationVO;
import com.wo.module.regulationSocialization.vo.SocializationApprovalVO;
import com.wo.module.regulationSocialization.vo.StatusConfirmationVO;


@Transactional
@Service("regulationSocializationService")
public class RegulationSocializationServiceImpl implements RegulationSocializationService {
    @Autowired
    @Qualifier("regulationSocializationDao")
    private RegulationSocializationDao regulationSocializationDao;

	public RegulationSocializationDao getRegulationSocializationDao() {
		return regulationSocializationDao;
	}

	public void setRegulationSocializationDao(RegulationSocializationDao regulationSocializationDao) {
		this.regulationSocializationDao = regulationSocializationDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public List<RegulationSocializationVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return regulationSocializationDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return regulationSocializationDao.searchCountData(searchCriteria);
    }
	
    public List<SocializationApprovalVO> getDataApprovalBySocializationId(Long socializationId){
    	return regulationSocializationDao.getDataApprovalBySocializationId(socializationId);
    }
    
    public List<StatusConfirmationVO> getDataConfirmStatusBySocializationId(Long socializationId){
    	return regulationSocializationDao.getDataConfirmStatusBySocializationId(socializationId);
    }
    
    
    public Boolean hasReachedMaximumReschedule(Long socializationPicFollowupId) {
    	return regulationSocializationDao.hasReachedMaximumReschedule(socializationPicFollowupId);
    }

	@SuppressWarnings("rawtypes")
	@Override
	public List<RegulationSocializationVO> searchDataXLS(List<? extends SearchObject> searchCriteria) {
		return regulationSocializationDao.searchDataXLS(searchCriteria);
	}
        
}
