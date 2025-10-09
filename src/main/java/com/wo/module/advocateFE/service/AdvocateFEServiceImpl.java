/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.advocateFE.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.advocate.dao.AdvocateDao;
import com.wo.module.advocate.model.Advocate;
import com.wo.module.advocateFE.dao.AdvocateFEDao;
import com.wo.module.advocateFE.vo.AdvocateFEVO;
import com.wo.module.common.paging.SearchObject;

@Transactional
@Service("advocateFEService")
public class AdvocateFEServiceImpl implements AdvocateFEService {
    @Autowired
    @Qualifier("advocateFEDao")
    private AdvocateFEDao advocateFEDao;

	public AdvocateFEDao getAdvocateFEDao() {
		return advocateFEDao;
	}

	public void setAdvocateFEDao(AdvocateFEDao advocateFEDao) {
		this.advocateFEDao = advocateFEDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<AdvocateFEVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return advocateFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return advocateFEDao.searchCountData(searchCriteria);
	}

	
}
