package com.wo.module.trcCorrespondenceAml.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;
import com.wo.module.trcCorrespondence.vo.TrcCorrespondenceSearchVo;
import com.wo.module.trcCorrespondenceAml.dao.TrcCorrespondenceAmlDao;

@Transactional
@Repository("trcCorrespondenceAmlService")
public class TrcCorrespondenceAmlServiceImpl implements TrcCorrespondenceAmlService{

	@Autowired
	@Qualifier("trcCorrespondenceAmlDao")
	private TrcCorrespondenceAmlDao trcCorrespondenceAmlDao;
	
	public TrcCorrespondenceAmlDao getTrcCorrespondenceAmlDao() {
		return trcCorrespondenceAmlDao;
	}

	public void setTrcCorrespondenceAmlDao(TrcCorrespondenceAmlDao trcCorrespondenceAmlDao) {
		this.trcCorrespondenceAmlDao = trcCorrespondenceAmlDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<TrcCorrespondenceSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return trcCorrespondenceAmlDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return trcCorrespondenceAmlDao.searchCountData(searchCriteria);
	}

	@Override
	public void save(TrcCorrespondence entity) {
		trcCorrespondenceAmlDao.save(entity);
	}

	@Override
	public void update(TrcCorrespondence entity) {
		trcCorrespondenceAmlDao.update(entity);
	}

	@Override
	public void delete(TrcCorrespondence entity) {
		trcCorrespondenceAmlDao.delete(entity);
	}

	@Override
	public TrcCorrespondence findById(Long id) {
		return trcCorrespondenceAmlDao.findById(id);
	}

}
