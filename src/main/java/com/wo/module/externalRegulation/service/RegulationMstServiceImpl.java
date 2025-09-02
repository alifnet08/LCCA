/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.externalRegulation.dao.RegulationMstDao;
import com.wo.module.externalRegulation.model.RegulationMst;

@Transactional
@Service("regulationMstService")
public class RegulationMstServiceImpl implements RegulationMstService {
    @Autowired
    @Qualifier("regulationMstDao")
    private RegulationMstDao regulationMstDao;

	
	public RegulationMstDao getRegulationMstDao() {
		return regulationMstDao;
	}

	public void setRegulationMstDao(RegulationMstDao regulationMstDao) {
		this.regulationMstDao = regulationMstDao;
	}

	public void save(RegulationMst regulation) {
		regulationMstDao.save(regulation);
	}
	
	public void update(RegulationMst regulation) {
		regulationMstDao.update(regulation);
	}
	
	public void delete(RegulationMst regulation) {
		regulationMstDao.delete(regulation);
	}
  
    public RegulationMst findById(Long id) {
    	return regulationMstDao.getById(id);
    }

    public Integer getCountHitRegulation(Long regulationId, String accessAction) throws Exception {
		return regulationMstDao.getCountHitRegulation(regulationId, accessAction);
	}     
}