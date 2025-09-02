/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcFine.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.trcFine.dao.TrcFineDao;
import com.wo.module.trcFine.vo.TrcFineSearchVo;
import com.wo.module.trcFineApproval.model.TrcFine;

@Transactional
@Service("trcFineService")
public class TrcFineServiceImpl implements TrcFineService {
	@Autowired
	@Qualifier("trcFineDao")
	private TrcFineDao trcFineDao;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<TrcFineSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return trcFineDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return trcFineDao.searchCountData(searchCriteria);
	}

	public void save(TrcFine entity) {
		trcFineDao.save(entity);
	}

	public void update(TrcFine entity) {
		trcFineDao.update(entity);
	}

	public void delete(TrcFine entity) {
		trcFineDao.delete(entity);
	}

	public TrcFine findById(Long id) {
		return trcFineDao.getById(id);
	}

}
