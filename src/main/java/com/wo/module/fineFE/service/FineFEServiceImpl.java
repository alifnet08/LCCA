package com.wo.module.fineFE.service;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.fineFE.dao.FineFEDao;
import com.wo.module.fineFE.vo.FineFEVo;
import com.wo.module.trcFineApproval.model.TrcFine;

@Transactional
@Service("fineFEService")
public class FineFEServiceImpl implements FineFEService, Serializable{

	private static final long serialVersionUID = -9127227041583031489L;

	@Autowired
	@Qualifier("fineFEDao")
	private FineFEDao fineFEDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<FineFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return fineFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return fineFEDao.searchCountData(searchCriteria);
	}

	@Override
	public void save(TrcFine entity) {
		fineFEDao.save(entity);
	}

	@Override
	public void update(TrcFine entity) {
		fineFEDao.update(entity);
	}

	@Override
	public void delete(TrcFine entity) {
		fineFEDao.delete(entity);
	}

	@Override
	public TrcFine findById(Long id) {
		return fineFEDao.getById(id);
	}
	
	public FineFEDao getFineFEDao() {
		return fineFEDao;
	}

	public void setFineFEDao(FineFEDao fineFEDao) {
		this.fineFEDao = fineFEDao;
	}

}
