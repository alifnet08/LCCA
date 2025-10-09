/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationSocialization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.regulationSocialization.dao.SocializationPICFollowupTmpDao;

@Transactional
@Service("socializationPICFollowupTmpService")
public class SocializationPICFollowupTmpServiceImpl implements SocializationPICFollowupTmpService {
    @Autowired
    @Qualifier("socializationPICFollowupTmpDao")
    private SocializationPICFollowupTmpDao socializationPICFollowupTmpDao;

	public SocializationPICFollowupTmpDao getSocializationPICFollowupTmpDao() {
		return socializationPICFollowupTmpDao;
	}

	public void setSocializationPICFollowupTmpDao(SocializationPICFollowupTmpDao socializationPICFollowupTmpDao) {
		this.socializationPICFollowupTmpDao = socializationPICFollowupTmpDao;
	}

	
	
        
}
