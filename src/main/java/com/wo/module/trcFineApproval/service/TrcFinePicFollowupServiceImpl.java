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
import com.wo.module.trcFineApproval.model.TrcFine;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowup;
import com.wo.module.trcFineApproval.vo.TrcFineSearchVo;

@Transactional
@Service("trcFinePicFollowupService")
public class TrcFinePicFollowupServiceImpl implements TrcFinePicFollowupService {
	@Autowired
	@Qualifier("trcFinePicFollowupDao")
	private TrcFinePicFollowupDao trcFinePicFollowupDao;
	
	

	
	public TrcFinePicFollowupDao getTrcFinePicFollowupDao() {
		return trcFinePicFollowupDao;
	}

	public void setTrcFinePicFollowupDao(TrcFinePicFollowupDao trcFinePicFollowupDao) {
		this.trcFinePicFollowupDao = trcFinePicFollowupDao;
	}

	public void save(TrcFinePicFollowup entity) {
		trcFinePicFollowupDao.save(entity);
	}

	public void update(TrcFinePicFollowup entity) {
		trcFinePicFollowupDao.update(entity);
	}

	public void delete(TrcFinePicFollowup entity) {
		trcFinePicFollowupDao.delete(entity);
	}

	public TrcFinePicFollowup findById(Long id) {
		return trcFinePicFollowupDao.getById(id);
	}

}
