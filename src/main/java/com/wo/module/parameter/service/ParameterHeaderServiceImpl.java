package com.wo.module.parameter.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.parameter.dao.ParameterHeaderDao;
import com.wo.module.parameter.model.ParameterHeader;

@Transactional
@Service("parameterHeaderService")
public class ParameterHeaderServiceImpl implements ParameterHeaderService{
	
	@Autowired
    @Qualifier("parameterHeaderDao")
	private ParameterHeaderDao parameterHeaderDao;

	@SuppressWarnings("rawtypes")
	@Override
	public List<ParameterHeader> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return parameterHeaderDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return parameterHeaderDao.searchCountData(searchCriteria);
	}
	
	@Override
	public void save(ParameterHeader parameterHeader) {
		parameterHeaderDao.save(parameterHeader);
	}

	@Override
	public void update(ParameterHeader parameterHeader) {
		parameterHeaderDao.update(parameterHeader);
	}

	@Override
	public void delete(ParameterHeader parameterHeader) {
		parameterHeaderDao.delete(parameterHeader);
	}

	@Override
	public ParameterHeader findById(Long id) {
		return parameterHeaderDao.getById(id);
	}

	public ParameterHeaderDao getParameterHeaderDao() {
		return parameterHeaderDao;
	}

	public void setParameterHeaderDao(ParameterHeaderDao parameterHeaderDao) {
		this.parameterHeaderDao = parameterHeaderDao;
	}

	@Override
	@Transactional(readOnly=true)
	public List<ParameterHeader> getListParameterAll() throws Exception {
		return parameterHeaderDao.getListParameterAll();
	}

	@Override
	public ParameterHeader getParameterHeaderByParamCode(String paramCode) throws Exception {
		return parameterHeaderDao.getParameterHeaderByParamCode(paramCode);
	}

	@Override
	public List<ParameterHeader> getListParameterAllOrderByName() throws Exception {
		return parameterHeaderDao.getListParameterAllOrderByName();
	}
}