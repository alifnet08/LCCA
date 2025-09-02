package com.wo.module.searchAllFE.service;

import java.util.List;

import javax.transaction.Transactional;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.searchAllFE.dao.SearchAllFEDao;
import com.wo.module.searchAllFE.vo.SearchAllFEVO;

@Transactional
@Service("searchAllFEService")
public class SearchAllFEServiceImpl implements SearchAllFEService {
	
    @Autowired
    @Qualifier("searchAllFEDao")
    private SearchAllFEDao searchAllFEDao;
    
	public SearchAllFEDao getSearchAllFEDao() {
		return searchAllFEDao;
	}

	public void setSearchAllFEDao(SearchAllFEDao searchAllFEDao) {
		this.searchAllFEDao = searchAllFEDao;
	}

	@SuppressWarnings("rawtypes")
	@Override    
    public List<SearchAllFEVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return searchAllFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
	@Override    
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return searchAllFEDao.searchCountData(searchCriteria);
    }
	
    
	
	
}
