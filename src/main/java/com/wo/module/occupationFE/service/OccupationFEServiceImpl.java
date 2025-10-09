package com.wo.module.occupationFE.service;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.occupationFE.dao.OccupationFEDao;
import com.wo.module.occupationFE.vo.OccupationFEVO;

@Transactional
@Service("occupationFEService")
public class OccupationFEServiceImpl implements OccupationFEService, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5477703234702869755L;

	@Autowired
	@Qualifier("occupationFEDao")
	private OccupationFEDao occupationFEDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<OccupationFEVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return occupationFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return occupationFEDao.searchCountData(searchCriteria);
	}

	public OccupationFEDao getOccupationFEDao() {
		return occupationFEDao;
	}

	public void setOccupationFEDao(OccupationFEDao occupationFEDao) {
		this.occupationFEDao = occupationFEDao;
	}
}
