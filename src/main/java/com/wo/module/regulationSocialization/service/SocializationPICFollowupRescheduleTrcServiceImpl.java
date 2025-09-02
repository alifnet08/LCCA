package com.wo.module.regulationSocialization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.regulationSocialization.dao.SocializationPICFollowupRescheduleTrcDao;

@Transactional
@Service("socializationPICFollowupRescheduleTrcService")
public class SocializationPICFollowupRescheduleTrcServiceImpl implements SocializationPICFollowupRescheduleTrcService {
   
	@Autowired
    @Qualifier("socializationPICFollowupRescheduleTrcDao")
    private SocializationPICFollowupRescheduleTrcDao socializationPICFollowupRescheduleTrcDao;

	public SocializationPICFollowupRescheduleTrcDao getSocializationPICFollowupRescheduleTrcDao() {
		return socializationPICFollowupRescheduleTrcDao;
	}

	public void setSocializationPICFollowupRescheduleTrcDao(
			SocializationPICFollowupRescheduleTrcDao socializationPICFollowupRescheduleTrcDao) {
		this.socializationPICFollowupRescheduleTrcDao = socializationPICFollowupRescheduleTrcDao;
	}
	
}
