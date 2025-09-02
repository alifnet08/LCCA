package com.wo.module.socializationFE.service;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.regulationSocialization.model.SocializationTrc;
import com.wo.module.socializationFE.dao.SocializationFEDao;
import com.wo.module.socializationFE.vo.SocializationFEVo;

@Transactional
@Service("socializationFEService")
public class SocializationFEServiceImpl implements SocializationFEService, Serializable{

	private static final long serialVersionUID = 7698791765841271927L;

	@Autowired
	@Qualifier("socializationFEDao")
	private SocializationFEDao socializationFEDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<SocializationFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return socializationFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return socializationFEDao.searchCountData(searchCriteria);
	}

	@Override
	public void save(SocializationTrc entity) {
		socializationFEDao.save(entity);
	}

	@Override
	public void update(SocializationTrc entity) {
		socializationFEDao.update(entity);
	}

	@Override
	public void delete(SocializationTrc entity) {
		socializationFEDao.delete(entity);
	}

	@Override
	public SocializationTrc findById(Long id) {
		return socializationFEDao.getById(id);
	}
	
	public SocializationFEDao getSocializationFEDao() {
		return socializationFEDao;
	}

	public void setSocializationFEDao(SocializationFEDao socializationFEDao) {
		this.socializationFEDao = socializationFEDao;
	}
	
}
