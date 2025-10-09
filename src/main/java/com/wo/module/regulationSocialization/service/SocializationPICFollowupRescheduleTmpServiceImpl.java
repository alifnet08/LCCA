/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationSocialization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.regulationSocialization.dao.SocializationPICFollowupRescheduleTmpDao;

@Transactional
@Service("socializationPICFollowupRescheduleTmpService")
public class SocializationPICFollowupRescheduleTmpServiceImpl implements SocializationPICFollowupRescheduleTmpService {
    @Autowired
    @Qualifier("socializationPICFollowupRescheduleTmpDao")
    private SocializationPICFollowupRescheduleTmpDao socializationPICFollowupRescheduleTmpDao;

	public SocializationPICFollowupRescheduleTmpDao getSocializationPICFollowupRescheduleTmpDao() {
		return socializationPICFollowupRescheduleTmpDao;
	}

	public void setSocializationPICFollowupRescheduleTmpDao(
			SocializationPICFollowupRescheduleTmpDao socializationPICFollowupRescheduleTmpDao) {
		this.socializationPICFollowupRescheduleTmpDao = socializationPICFollowupRescheduleTmpDao;
	}

	
	
        
}
