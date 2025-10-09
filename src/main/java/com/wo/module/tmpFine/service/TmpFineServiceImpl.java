/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpFine.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.tmpFine.dao.TmpFineDao;
import com.wo.module.tmpFine.dao.TmpFinePicFollowupDao;
import com.wo.module.tmpFine.model.TmpFine;
import com.wo.module.tmpFine.model.TmpFineApproval;
import com.wo.module.tmpFine.model.TmpFinePicFollowup;
import com.wo.module.tmpFine.vo.TmpFineSearchVo;

@Transactional
@Service("tmpFineService")
public class TmpFineServiceImpl implements TmpFineService {
	@Autowired
	@Qualifier("tmpFineDao")
	private TmpFineDao tmpFineDao;
	
	
	@Autowired
	@Qualifier("tmpFinePicFollowupDao")
	private TmpFinePicFollowupDao tmpFinePicFollowupDao;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<TmpFineSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return tmpFineDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return tmpFineDao.searchCountData(searchCriteria);
	}

	public void save(TmpFine entity) {
		tmpFineDao.save(entity);
	}

	public void update(TmpFine entity) {
		tmpFineDao.update(entity);
	}

	public void delete(TmpFine entity) {
		tmpFineDao.delete(entity);
	}

	public TmpFine findById(Long id) {
		return tmpFineDao.getById(id);
	}
	
	public Boolean hasReachedMaximumReschedule(Long fineId) {
		return tmpFineDao.hasReachedMaximumReschedule(fineId);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<TmpFineSearchVo> searchDataXLS(List<? extends SearchObject> searchCriteria) {
		return tmpFineDao.searchDataXLS(searchCriteria);
	}

	@Override
	public List<TmpFineApproval> getDataApprovalByFineId(Long fineId) {
		return tmpFineDao.getDataApprovalByFineId(fineId);
	}

	@Override
	public void saveFolup(TmpFinePicFollowup entity) {
		tmpFinePicFollowupDao.save(entity);
	}

	@Override
	public void updateFolup(TmpFinePicFollowup entity) {
		tmpFinePicFollowupDao.update(entity);
	}

	@Override
	public TmpFinePicFollowup findFolupById(Long id) {
		return tmpFinePicFollowupDao.findById(id);
		
	}
	
	
}
