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

import com.wo.module.externalRegulation.dao.RegulationTrackRecordDao;
import com.wo.module.externalRegulation.model.RegulationTrackRecord;

@Transactional
@Service("regulationTrackRecordService")
public class RegulationTrackRecordServiceImpl implements RegulationTrackRecordService {
    @Autowired
    @Qualifier("regulationTrackRecordDao")
    private RegulationTrackRecordDao regulationTrackRecordDao;

	
	public RegulationTrackRecordDao getRegulationTrackRecordDao() {
		return regulationTrackRecordDao;
	}

	public void setRegulationTrackRecordDao(RegulationTrackRecordDao regulationTrackRecordDao) {
		this.regulationTrackRecordDao = regulationTrackRecordDao;
	}

	public void save(RegulationTrackRecord regulation) {
		regulationTrackRecordDao.save(regulation);
	}
	
	public void update(RegulationTrackRecord regulation) {
		regulationTrackRecordDao.update(regulation);
	}
	
	public void delete(RegulationTrackRecord regulation) {
		regulationTrackRecordDao.delete(regulation);
	}
  
    public RegulationTrackRecord findById(Long id) {
    	return regulationTrackRecordDao.getById(id);
    }
    
    public List<RegulationTrackRecord> getRegulationTrackRecordByRegulationId(Long regulationId) throws Exception{
    	return regulationTrackRecordDao.getRegulationTrackRecordByRegulationId(regulationId);
    }
        
}
