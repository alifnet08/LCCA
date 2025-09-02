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

import com.wo.module.externalRegulation.dao.RegulationAttachmentMstDao;
import com.wo.module.externalRegulation.model.RegulationAttachmentMst;

@Transactional
@Service("regulationAttachmentMstService")
public class RegulationAttachmentMstServiceImpl implements RegulationAttachmentMstService {
    @Autowired
    @Qualifier("regulationAttachmentMstDao")
    private RegulationAttachmentMstDao regulationAttachmentMstDao;

	
	public RegulationAttachmentMstDao getRegulationAttachmentMstDao() {
		return regulationAttachmentMstDao;
	}

	public void setRegulationAttachmentMstDao(RegulationAttachmentMstDao regulationAttachmentMstDao) {
		this.regulationAttachmentMstDao = regulationAttachmentMstDao;
	}

	public void save(RegulationAttachmentMst regulation) {
		regulationAttachmentMstDao.save(regulation);
	}
	
	public void update(RegulationAttachmentMst regulation) {
		regulationAttachmentMstDao.update(regulation);
	}
	
	public void delete(RegulationAttachmentMst regulation) {
		regulationAttachmentMstDao.delete(regulation);
	}
  
    public RegulationAttachmentMst findById(Long id) {
    	return regulationAttachmentMstDao.getById(id);
    }

	@Override
	public List<RegulationAttachmentMst> getRegulationAttachmentByRegulationId(Long regulationId) throws Exception {
		return regulationAttachmentMstDao.getRegulationAttachmentByRegulationId(regulationId);
	}
    
   
        
}
