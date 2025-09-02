package com.wo.module.notaryFE.service;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.notaryFE.dao.NotaryFEDao;
import com.wo.module.notaryFE.vo.NotaryFEVo;

@Transactional
@Service("notaryFEService")
public class NotaryFEServiceImpl implements NotaryFEService, Serializable{

	private static final long serialVersionUID = 679220478332296083L;

	@Autowired
	@Qualifier("notaryFEDao")
	private NotaryFEDao notaryFEDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<NotaryFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return notaryFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return notaryFEDao.searchCountData(searchCriteria);
	}

	public NotaryFEDao getNotaryFEDao() {
		return notaryFEDao;
	}

	public void setNotaryFEDao(NotaryFEDao notaryFEDao) {
		this.notaryFEDao = notaryFEDao;
	}

}
