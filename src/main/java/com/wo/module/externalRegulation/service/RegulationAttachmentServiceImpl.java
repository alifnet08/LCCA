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

import com.wo.module.externalRegulation.dao.RegulationAttachmentDao;
import com.wo.module.externalRegulation.model.RegulationAttachment;

@Transactional
@Service("regulationAttachmentService")
public class RegulationAttachmentServiceImpl implements RegulationAttachmentService {
    @Autowired
    @Qualifier("regulationAttachmentDao")
    private RegulationAttachmentDao regulationAttachmentDao;

	
	public RegulationAttachmentDao getRegulationAttachmentDao() {
		return regulationAttachmentDao;
	}

	public void setRegulationAttachmentDao(RegulationAttachmentDao regulationAttachmentDao) {
		this.regulationAttachmentDao = regulationAttachmentDao;
	}

	public void save(RegulationAttachment regulation) {
		regulationAttachmentDao.save(regulation);
	}
	
	public void update(RegulationAttachment regulation) {
		regulationAttachmentDao.update(regulation);
	}
	
	public void delete(RegulationAttachment regulation) {
		regulationAttachmentDao.delete(regulation);
	}
  
    public RegulationAttachment findById(Long id) {
    	return regulationAttachmentDao.getById(id);
    }
    
    public List<RegulationAttachment> getRegulationAttachmentByRegulationId(Long regulationId) throws Exception{
    	return regulationAttachmentDao.getRegulationAttachmentByRegulationId(regulationId);
    }
        
}
