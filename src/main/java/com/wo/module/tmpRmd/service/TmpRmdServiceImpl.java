/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpRmd.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.tmpRmd.dao.TmpRmdDao;
import com.wo.module.tmpRmd.model.TmpRmd;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;

@Transactional
@Service("tmpRmdService")
public class TmpRmdServiceImpl implements TmpRmdService {
	@Autowired
	@Qualifier("tmpRmdDao")
	private TmpRmdDao tmpRmdDao;

	public TmpRmdDao getTmpRmdDao() {
		return tmpRmdDao;
	}

	public void setTmpRmdDao(TmpRmdDao tmpRmdDao) {
		this.tmpRmdDao = tmpRmdDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<TmpRmd> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return tmpRmdDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return tmpRmdDao.searchCountData(searchCriteria);
	}

	public void save(TmpRmd entity) {
		tmpRmdDao.save(entity);
	}

	public void update(TmpRmd entity) {
		tmpRmdDao.update(entity);
	}

	public void delete(TmpRmd entity) {
		tmpRmdDao.delete(entity);
	}

	public TmpRmd findById(Long id) {
		return tmpRmdDao.getById(id);
	}

	public Boolean isDataDuplicate(TmpRmd entity) {
		return tmpRmdDao.isDataDuplicate(entity);
	}
	
	@SuppressWarnings("rawtypes")
	public List<TmpRmd> searchDataXls(List<? extends SearchObject> searchCriteria) throws Exception  {
		return tmpRmdDao.searchDataXls(searchCriteria);
	}

	@Override
	public Integer getTmpRmdByNameIn(String reportNameIn, Long reportTypeId) throws Exception {
		return tmpRmdDao.getTmpRmdByNameIn(reportNameIn, reportTypeId);
	}

	@Override
	public Integer getTmpRmdByIdAndNameIn(Long id, String reportNameIn, Long reportTypeId) throws Exception {
		return tmpRmdDao.getTmpRmdByIdAndNameIn(id, reportNameIn, reportTypeId);
	}
	
	public List<TrcRmdPicFollowup> getRmdPicFollowupByRmdId(Long rmdId){
		return tmpRmdDao.getRmdPicFollowupByRmdId(rmdId);
	}
}
