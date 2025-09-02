package com.wo.module.correspondenceFE.service;

import java.util.List;

import javax.transaction.Transactional;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.correspondenceFE.dao.CorrespondenceFEDao;
import com.wo.module.correspondenceFE.vo.CorrespondenceFEVO;

@Transactional
@Service("correspondenceFEService")
public class CorrespondenceFEServiceImpl implements CorrespondenceFEService {
	
    @Autowired
    @Qualifier("correspondenceFEDao")
    private CorrespondenceFEDao correspondenceFEDao;
    

	public CorrespondenceFEDao getCorrespondenceFEDao() {
		return correspondenceFEDao;
	}

	public void setCorrespondenceFEDao(CorrespondenceFEDao correspondenceFEDao) {
		this.correspondenceFEDao = correspondenceFEDao;
	}

	@SuppressWarnings("rawtypes")
	@Override    
    public List<CorrespondenceFEVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return correspondenceFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
	@Override    
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return correspondenceFEDao.searchCountData(searchCriteria);
    }
	
    
	
	
}
