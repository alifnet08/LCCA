/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.opinionFE.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.opinionFE.dao.OpinionFEDao;
import com.wo.module.opinionFE.vo.OpinionFEVO;
import com.wo.module.common.paging.SearchObject;

@Transactional
@Service("opinionFEService")
public class OpinionFEServiceImpl implements OpinionFEService {
    @Autowired
    @Qualifier("opinionFEDao")
    private OpinionFEDao opinionFEDao;
    
	
	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<OpinionFEVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws Exception {
		return opinionFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return opinionFEDao.searchCountData(searchCriteria);
	}

	public OpinionFEDao getOpinionFEDao() {
		return opinionFEDao;
	}

	public void setOpinionFEDao(OpinionFEDao opinionFEDao) {
		this.opinionFEDao = opinionFEDao;
	}
	    
}
