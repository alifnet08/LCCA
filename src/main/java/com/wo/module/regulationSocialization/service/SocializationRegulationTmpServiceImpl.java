/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationSocialization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.regulationSocialization.dao.SocializationRegulationTmpDao;

@Transactional
@Service("socializationRegulationTmpService")
public class SocializationRegulationTmpServiceImpl implements SocializationRegulationTmpService {
    @Autowired
    @Qualifier("socializationRegulationTmpDao")
    private SocializationRegulationTmpDao socializationRegulationTmpDao;

	public SocializationRegulationTmpDao getSocializationRegulationTmpDao() {
		return socializationRegulationTmpDao;
	}

	public void setSocializationRegulationTmpDao(SocializationRegulationTmpDao socializationRegulationTmpDao) {
		this.socializationRegulationTmpDao = socializationRegulationTmpDao;
	}

	
	
	
        
}
