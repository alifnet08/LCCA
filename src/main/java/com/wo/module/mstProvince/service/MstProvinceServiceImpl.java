package com.wo.module.mstProvince.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.mstProvince.dao.MstProvinceDAO;
import com.wo.module.mstProvince.model.MstProvince;

@Transactional
@Service("mstProvinceService")
public class MstProvinceServiceImpl implements MstProvinceService {

	private Logger logger = Logger.getLogger(MstProvinceServiceImpl.class);
	
	@Autowired
	@Qualifier("mstProvinceDAO")
	private MstProvinceDAO mstProvinceDAO;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<MstProvince> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return mstProvinceDAO.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return mstProvinceDAO.searchCountData(searchCriteria);
	}

	@Override
	public void save(MstProvince entity) {
		mstProvinceDAO.save(entity);
	}

	@Override
	public void update(MstProvince entity) {
		mstProvinceDAO.update(entity);
	}

	@Override
	public void delete(MstProvince entity) {
		mstProvinceDAO.delete(entity);
	}

	@Override
	public MstProvince findById(Long id) {
		return mstProvinceDAO.getById(id);
	}

	public MstProvinceDAO getMstProvinceDAO() {
		return mstProvinceDAO;
	}

	public void setMstProvinceDAO(MstProvinceDAO mstProvinceDAO) {
		this.mstProvinceDAO = mstProvinceDAO;
	}

	@Override
	public Integer countBranchCodeDuplicate(String branchCode) {
		return mstProvinceDAO.countBranchCodeDuplicate(branchCode);
	}
	
	 public MstProvince getProvinceByProvinceName(String name){
		 return mstProvinceDAO.getProvinceByProvinceName(name);
	 }

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
}