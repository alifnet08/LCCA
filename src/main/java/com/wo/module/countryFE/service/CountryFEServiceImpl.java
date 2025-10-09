package com.wo.module.countryFE.service;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.countryFE.dao.CountryFEDao;
import com.wo.module.countryFE.vo.CountryFEVO;

@Transactional
@Service("countryFEService")
public class CountryFEServiceImpl implements CountryFEService, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5477703234702869755L;

	@Autowired
	@Qualifier("countryFEDao")
	private CountryFEDao countryFEDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<CountryFEVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return countryFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return countryFEDao.searchCountData(searchCriteria);
	}

	public CountryFEDao getCountryFEDao() {
		return countryFEDao;
	}

	public void setCountryFEDao(CountryFEDao countryFEDao) {
		this.countryFEDao = countryFEDao;
	}
}
