/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcRmd.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.trcRmd.dao.TrcRmdDao;
import com.wo.module.trcRmd.model.TrcRmd;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;
import com.wo.module.trcRmd.vo.TrcRmdSearchVo;

@Transactional
@Service("trcRmdService")
public class TrcRmdServiceImpl implements TrcRmdService {
	@Autowired
	@Qualifier("trcRmdDao")
	private TrcRmdDao trcRmdDao;

	public TrcRmdDao getTrcRmdDao() {
		return trcRmdDao;
	}

	public void setTrcRmdDao(TrcRmdDao trcRmdDao) {
		this.trcRmdDao = trcRmdDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<TrcRmdSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return trcRmdDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return trcRmdDao.searchCountData(searchCriteria);
	}

	public void save(TrcRmd entity) {
		trcRmdDao.save(entity);
	}

	public void update(TrcRmd entity) {
		trcRmdDao.update(entity);
	}

	public void delete(TrcRmd entity) {
		trcRmdDao.delete(entity);
	}

	public TrcRmd findById(Long id) {
		return trcRmdDao.getById(id);
	}
	
	public TrcRmdPicFollowup getFirstPicFollowupId(Long rmdId) throws Exception {
		return trcRmdDao.getFirstPicFollowupId(rmdId);
	}

}