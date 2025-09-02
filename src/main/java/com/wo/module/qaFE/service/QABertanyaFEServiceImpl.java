package com.wo.module.qaFE.service;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.qaFE.dao.QABertanyaFEDao;
import com.wo.module.qaFE.vo.QAFEVo;

@Transactional
@Service("qaBertanyaFEService")
public class QABertanyaFEServiceImpl implements QABertanyaFEService, Serializable{

	private static final long serialVersionUID = -886046711818298321L;
	
	@Autowired
	@Qualifier("qaBertanyaFEDao")
	private QABertanyaFEDao qaBertanyaFEDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<QAFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return qaBertanyaFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return qaBertanyaFEDao.searchCountData(searchCriteria);
	}

	public QABertanyaFEDao getQaBertanyaFEDao() {
		return qaBertanyaFEDao;
	}

	public void setQaBertanyaFEDao(QABertanyaFEDao qaBertanyaFEDao) {
		this.qaBertanyaFEDao = qaBertanyaFEDao;
	}

	

	
	

	

}
