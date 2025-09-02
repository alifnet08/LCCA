/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationSocialization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.regulationSocialization.dao.SocializationPICFollowupEmailTmpDao;

@Transactional
@Service("socializationPICFollowupEmailTmpService")
public class SocializationPICFollowupEmailTmpServiceImpl implements SocializationPICFollowupEmailTmpService {
    @Autowired
    @Qualifier("socializationPICFollowupEmailTmpDao")
    private SocializationPICFollowupEmailTmpDao socializationPICFollowupEmailTmpDao;

	public SocializationPICFollowupEmailTmpDao getSocializationPICFollowupEmailTmpDao() {
		return socializationPICFollowupEmailTmpDao;
	}

	public void setSocializationPICFollowupEmailTmpDao(
			SocializationPICFollowupEmailTmpDao socializationPICFollowupEmailTmpDao) {
		this.socializationPICFollowupEmailTmpDao = socializationPICFollowupEmailTmpDao;
	}

	
        
}
