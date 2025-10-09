package com.wo.module.regulationSocialization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.regulationSocialization.dao.SocializationPICFollowupTrcDao;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupTrc;

@Transactional
@Service("socializationPICFollowupTrcService")
public class SocializationPICFollowupTrcServiceImpl implements SocializationPICFollowupTrcService {
    @Autowired
    @Qualifier("socializationPICFollowupTrcDao")
    private SocializationPICFollowupTrcDao socializationPICFollowupTrcDao;

	public SocializationPICFollowupTrcDao getSocializationPICFollowupTrcDao() {
		return socializationPICFollowupTrcDao;
	}

	public void setSocializationPICFollowupTrcDao(SocializationPICFollowupTrcDao socializationPICFollowupTrcDao) {
		this.socializationPICFollowupTrcDao = socializationPICFollowupTrcDao;
	}
	
	
	public SocializationPICFollowupTrc findById(Long id) {
		return socializationPICFollowupTrcDao.getById(id);
	}
	    
}
