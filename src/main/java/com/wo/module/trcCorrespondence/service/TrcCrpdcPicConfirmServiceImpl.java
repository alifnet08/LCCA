/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcCorrespondence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.trcCorrespondence.dao.TrcCrpdcPicConfirmDao;
import com.wo.module.trcCorrespondence.model.TrcCrpdcPicConfirm;

@Transactional
@Service("trcCrpdcPicConfirmService")
public class TrcCrpdcPicConfirmServiceImpl implements TrcCrpdcPicConfirmService {


	@Autowired
	@Qualifier("trcCrpdcPicConfirmDao")
	private TrcCrpdcPicConfirmDao trcCrpdcPicConfirmDao;
	

	@Override
	public TrcCrpdcPicConfirm findById(Long id) {
		return trcCrpdcPicConfirmDao.findById(id);
	}


	@Override
	public void update(TrcCrpdcPicConfirm entity) {
		trcCrpdcPicConfirmDao.update(entity);		
	}
	
	
	
}