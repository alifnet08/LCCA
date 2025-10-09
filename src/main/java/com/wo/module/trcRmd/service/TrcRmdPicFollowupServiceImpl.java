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
import com.wo.module.trcRmd.dao.TrcRmdPicFollowupDao;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;
import com.wo.module.trcRmd.vo.TrcRmdSearchVo;

@Transactional
@Service("trcRmdPicFollowupService")
public class TrcRmdPicFollowupServiceImpl implements TrcRmdPicFollowupService {
	@Autowired
	@Qualifier("trcRmdPicFollowupDao")
	private TrcRmdPicFollowupDao trcRmdPicFollowupDao;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<TrcRmdSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return trcRmdPicFollowupDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return trcRmdPicFollowupDao.searchCountData(searchCriteria);
	}

	public void save(TrcRmdPicFollowup entity) {
		trcRmdPicFollowupDao.save(entity);
	}

	public void update(TrcRmdPicFollowup entity) {
		trcRmdPicFollowupDao.update(entity);
	}

	public void delete(TrcRmdPicFollowup entity) {
		trcRmdPicFollowupDao.delete(entity);
	}

	public TrcRmdPicFollowup findById(Long id) {
		return trcRmdPicFollowupDao.getById(id);
	}

	public TrcRmdPicFollowupDao getTrcRmdPicFollowupDao() {
		return trcRmdPicFollowupDao;
	}

	public void setTrcRmdPicFollowupDao(TrcRmdPicFollowupDao trcRmdPicFollowupDao) {
		this.trcRmdPicFollowupDao = trcRmdPicFollowupDao;
	}
	
	public List<TrcRmdPicFollowup> getTrcRmdPicFollowupByRmdId(Long rmdId) throws Exception {
		return trcRmdPicFollowupDao.getTrcRmdPicFollowupByRmdId(rmdId);
	}

}