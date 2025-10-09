/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpCorrespondenceAml.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondence;
import com.wo.module.tmpCorrespondence.vo.TmpCorrespondenceSearchVo;
import com.wo.module.tmpCorrespondenceAml.dao.TmpCorrespondenceAmlDao;

@Transactional
@Service("tmpCorrespondenceAmlService")
public class TmpCorrespondenceAmlServiceImpl implements TmpCorrespondenceAmlService {
	@Autowired
	@Qualifier("tmpCorrespondenceAmlDao")
	private TmpCorrespondenceAmlDao tmpCorrespondenceAmlDao;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<TmpCorrespondenceSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return tmpCorrespondenceAmlDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return tmpCorrespondenceAmlDao.searchCountData(searchCriteria);
	}

	public void save(TmpCorrespondence entity) {
		tmpCorrespondenceAmlDao.save(entity);
	}

	public void update(TmpCorrespondence entity) {
		tmpCorrespondenceAmlDao.update(entity);
	}

	public void delete(TmpCorrespondence entity) {
		tmpCorrespondenceAmlDao.delete(entity);
	}

	public TmpCorrespondence findById(Long id) {
		return tmpCorrespondenceAmlDao.getById(id);
	}
	
	public Boolean hasReachedMaximumReschedule(Long correspondenceId) {
		return tmpCorrespondenceAmlDao.hasReachedMaximumReschedule(correspondenceId);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<TmpCorrespondenceSearchVo> searchDataXLS(List<? extends SearchObject> searchCriteria) {
		return tmpCorrespondenceAmlDao.searchDataXLS(searchCriteria);
	}
}
