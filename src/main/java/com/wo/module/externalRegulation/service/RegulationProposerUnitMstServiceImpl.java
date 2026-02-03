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

import com.wo.module.externalRegulation.dao.RegulationProposerUnitMstDao;
import com.wo.module.externalRegulation.model.RegulationProposerUnitMst;

@Transactional
@Service("regulationProposerUnitMstService")
public class RegulationProposerUnitMstServiceImpl implements RegulationProposerUnitMstService {
	
    @Autowired
    @Qualifier("regulationProposerUnitMstDao")
    private RegulationProposerUnitMstDao regulationProposerUnitMstDao;

	
    public RegulationProposerUnitMstDao getRegulationProposerUnitMstDao() {
		return regulationProposerUnitMstDao;
	}

	public void setRegulationProposerUnitMstDao(RegulationProposerUnitMstDao regulationProposerUnitMstDao) {
		this.regulationProposerUnitMstDao = regulationProposerUnitMstDao;
	}

	public List<RegulationProposerUnitMst> getRegulationProposerUnitByRegulationId(Long regulationId) throws Exception{
    	return regulationProposerUnitMstDao.getRegulationProposerUnitByRegulationId(regulationId);
    }
        
}
