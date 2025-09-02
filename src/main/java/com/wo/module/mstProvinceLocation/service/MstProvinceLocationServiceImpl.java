package com.wo.module.mstProvinceLocation.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.mstProvince.model.MstProvinceLocation;
import com.wo.module.mstProvinceLocation.dao.MstProvinceLocationDAO;

@Transactional
@Service("mstProvinceLocationService")
public class MstProvinceLocationServiceImpl implements MstProvinceLocationService {

	private Logger logger = Logger.getLogger(MstProvinceLocationServiceImpl.class);
	
	@Autowired
	@Qualifier("mstProvinceLocationDAO")
	private MstProvinceLocationDAO mstProvinceLocationDAO;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<MstProvinceLocation> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return mstProvinceLocationDAO.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return mstProvinceLocationDAO.searchCountData(searchCriteria);
	}

	@Override
	public void save(MstProvinceLocation entity) {
		mstProvinceLocationDAO.save(entity);
	}

	@Override
	public void update(MstProvinceLocation entity) {
		mstProvinceLocationDAO.update(entity);
	}

	@Override
	public void delete(MstProvinceLocation entity) {
		mstProvinceLocationDAO.delete(entity);
	}

	@Override
	public MstProvinceLocation findById(Long id) {
		return mstProvinceLocationDAO.getById(id);
	}
	
	public MstProvinceLocation getProvinceLocationByProvince(String province){
		return mstProvinceLocationDAO.getProvinceLocationByProvince(province);
	}

	public MstProvinceLocationDAO getMstProvinceLocationDAO() {
		return mstProvinceLocationDAO;
	}

	public void setMstProvinceLocationDAO(MstProvinceLocationDAO mstProvinceLocationDAO) {
		this.mstProvinceLocationDAO = mstProvinceLocationDAO;
	}

	

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
}