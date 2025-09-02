package com.wo.module.cpsaPicLockHistory.service;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.cpsaPicLockHistory.dao.CpsaPicLockHistoryDao;
import com.wo.module.cpsaPicLockHistory.model.CpsaPicLockHistory;

@Transactional
@Service("cpsaPicLockHistoryService")
public class CpsaPicLockHistoryServiceImpl implements CpsaPicLockHistoryService, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 151784508376246159L;
	
	@Autowired
	@Qualifier("cpsaPicLockHistoryDao")
	private CpsaPicLockHistoryDao cpsaPicLockHistoryDao;

	@Override
	public List<CpsaPicLockHistory> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Long cpsaId, String nik) {
		// TODO Auto-generated method stub
		
	}
}
