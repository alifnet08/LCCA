/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.externalRegulation.dao.RegulationTrackRecordMstDao;
import com.wo.module.externalRegulation.model.RegulationTrackRecordMst;

@Transactional
@Service("regulationTrackRecordMstService")
public class RegulationTrackRecordMstServiceImpl implements RegulationTrackRecordMstService {
    @Autowired
    @Qualifier("regulationTrackRecordMstDao")
    private RegulationTrackRecordMstDao regulationTrackRecordMstDao;

	
	public RegulationTrackRecordMstDao getRegulationTrackRecordMstDao() {
		return regulationTrackRecordMstDao;
	}

	public void setRegulationTrackRecordMstDao(RegulationTrackRecordMstDao regulationTrackRecordMstDao) {
		this.regulationTrackRecordMstDao = regulationTrackRecordMstDao;
	}

	public void save(RegulationTrackRecordMst regulation) {
		regulationTrackRecordMstDao.save(regulation);
	}
	
	public void update(RegulationTrackRecordMst regulation) {
		regulationTrackRecordMstDao.update(regulation);
	}
	
	public void delete(RegulationTrackRecordMst regulation) {
		regulationTrackRecordMstDao.delete(regulation);
	}
  
    public RegulationTrackRecordMst findById(Long id) {
    	return regulationTrackRecordMstDao.getById(id);
    }
    
	public List<RegulationTrackRecordMst> getRegulationTrackRecordByRegulationId(Long regulationId) throws Exception {
		return regulationTrackRecordMstDao.getRegulationTrackRecordByRegulationId(regulationId);
	}
	
	public List<RegulationTrackRecordMst> getRegulationTrackRecordByRegulationLinkId(Long regulationId) throws Exception {
		return regulationTrackRecordMstDao.getRegulationTrackRecordByRegulationLinkId(regulationId);
	}
    
    
        
}
