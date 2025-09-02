/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcFineApproval.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.trcFineApproval.dao.TrcFineApprovalDao;
import com.wo.module.trcFineApproval.dao.TrcFinePicFollowupDao;
import com.wo.module.trcFineApproval.dao.TrcFinePicFpHistoryDao;
import com.wo.module.trcFineApproval.model.TrcFine;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowup;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowupHistory;
import com.wo.module.trcFineApproval.vo.TrcFineSearchVo;

@Transactional
@Service("trcFineApprovalService")
public class TrcFineApprovalServiceImpl implements TrcFineApprovalService {
	@Autowired
	@Qualifier("trcFineApprovalDao")
	private TrcFineApprovalDao trcFineApprovalDao;
	
	@Autowired
	@Qualifier("trcFinePicFollowupDao")
	private TrcFinePicFollowupDao trcFinePicFollowupDao;
	
	@Autowired
	@Qualifier("trcFinePicFpHistoryDao")
	private TrcFinePicFpHistoryDao trcFinePicFpHistoryDao;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<TrcFineSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return trcFineApprovalDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return trcFineApprovalDao.searchCountData(searchCriteria);
	}

	public void save(TrcFine entity) {
		trcFineApprovalDao.save(entity);
	}

	public void update(TrcFine entity) {
		trcFineApprovalDao.update(entity);
	}

	public void delete(TrcFine entity) {
		trcFineApprovalDao.delete(entity);
	}

	public TrcFine findById(Long id) {
		return trcFineApprovalDao.getById(id);
	}

	@Override
	public void saveDtl(TrcFinePicFollowup entity) {
		trcFinePicFollowupDao.save(entity);
		
	}

	@Override
	public void updateDtl(TrcFinePicFollowup entity) {
		trcFinePicFollowupDao.update(entity);
		
	}

	@Override
	public TrcFinePicFollowup findDtlById(Long id) {
		return trcFinePicFollowupDao.findById(id);
		
	}

	@Override
	public void saveHis(TrcFinePicFollowupHistory entity) {
		trcFinePicFpHistoryDao.save(entity);
		
	}

	@Override
	public void updateHis(TrcFinePicFollowupHistory entity) {
		trcFinePicFpHistoryDao.save(entity);
	}

	@Override
	public TrcFinePicFollowupHistory findHisById(Long id) {
		return trcFinePicFpHistoryDao.findById(id);
		
	}
	
	

}
