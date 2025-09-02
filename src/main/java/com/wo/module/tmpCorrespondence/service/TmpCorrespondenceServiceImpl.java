/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpCorrespondence.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.tmpCorrespondence.dao.TmpCorrespondenceDao;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondence;
import com.wo.module.tmpCorrespondence.vo.TmpCorrespondenceSearchVo;

@Transactional
@Service("tmpCorrespondenceService")
public class TmpCorrespondenceServiceImpl implements TmpCorrespondenceService {
	@Autowired
	@Qualifier("tmpCorrespondenceDao")
	private TmpCorrespondenceDao tmpCorrespondenceDao;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<TmpCorrespondenceSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return tmpCorrespondenceDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return tmpCorrespondenceDao.searchCountData(searchCriteria);
	}

	public void save(TmpCorrespondence entity) {
		tmpCorrespondenceDao.save(entity);
	}

	public void update(TmpCorrespondence entity) {
		tmpCorrespondenceDao.update(entity);
	}

	public void delete(TmpCorrespondence entity) {
		tmpCorrespondenceDao.delete(entity);
	}

	public TmpCorrespondence findById(Long id) {
		return tmpCorrespondenceDao.getById(id);
	}
	
	public Boolean hasReachedMaximumReschedule(Long correspondenceId) {
		return tmpCorrespondenceDao.hasReachedMaximumReschedule(correspondenceId);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<TmpCorrespondenceSearchVo> searchDataXLS(List<? extends SearchObject> searchCriteria) {
		return tmpCorrespondenceDao.searchDataXLS(searchCriteria);
	}
}
