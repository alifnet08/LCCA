package com.wo.module.economySectorFE.service;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.economySectorFE.dao.EconomySectorFEDao;
import com.wo.module.economySectorFE.vo.EconomySectorFEVO;

@Transactional
@Service("economySectorFEService")
public class EconomySectorFEServiceImpl implements EconomySectorFEService, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5477703234702869755L;

	@Autowired
	@Qualifier("economySectorFEDao")
	private EconomySectorFEDao economySectorFEDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<EconomySectorFEVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return economySectorFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return economySectorFEDao.searchCountData(searchCriteria);
	}

	public EconomySectorFEDao getEconomySectorFEDao() {
		return economySectorFEDao;
	}

	public void setEconomySectorFEDao(EconomySectorFEDao economySectorFEDao) {
		this.economySectorFEDao = economySectorFEDao;
	}
}
