package com.wo.module.qa.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.qa.dao.QABackEndDao;
import com.wo.module.qa.model.QA;

@Transactional
@Service("qaBackEndService")
public class QABackEndServiceImpl implements QABackEndService{

	@Autowired
	@Qualifier("qaBackEndDao")
	private QABackEndDao qaBackEndDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<QA> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return qaBackEndDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return qaBackEndDao.searchCountData(searchCriteria);
	}

	@Override
	public void update(QA qa) {
		qaBackEndDao.update(qa);
	}

	@Override
	public QA findById(Long id) {
		return qaBackEndDao.findById(id);
	}

	public QABackEndDao getQaBackEndDao() {
		return qaBackEndDao;
	}

	public void setQaBackEndDao(QABackEndDao qaBackEndDao) {
		this.qaBackEndDao = qaBackEndDao;
	}

}
