/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationSocialization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.regulationSocialization.dao.SocializationPICComplianceTmpDao;

@Transactional
@Service("socializationPICComplianceTmpService")
public class SocializationPICComplianceTmpServiceImpl implements SocializationPICComplianceTmpService {
    @Autowired
    @Qualifier("socializationPICComplianceTmpDao")
    private SocializationPICComplianceTmpDao socializationPICComplianceTmpDao;

	public SocializationPICComplianceTmpDao getSocializationPICComplianceTmpDao() {
		return socializationPICComplianceTmpDao;
	}

	public void setSocializationPICComplianceTmpDao(SocializationPICComplianceTmpDao socializationPICComplianceTmpDao) {
		this.socializationPICComplianceTmpDao = socializationPICComplianceTmpDao;
	}

	
	
	
        
}
