package com.wo.module.regulationSocialization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.regulationSocialization.dao.SocializationPICFollowupEmailTrcDao;

@Transactional
@Service("socializationPICFollowupEmailTrcService")
public class SocializationPICFollowupEmailTrcServiceImpl implements SocializationPICFollowupEmailTrcService {
  
	@Autowired
    @Qualifier("socializationPICFollowupEmailTrcDao")
    private SocializationPICFollowupEmailTrcDao socializationPICFollowupEmailTrcDao;

	public SocializationPICFollowupEmailTrcDao getSocializationPICFollowupEmailTrcDao() {
		return socializationPICFollowupEmailTrcDao;
	}

	public void setSocializationPICFollowupEmailTrcDao(
			SocializationPICFollowupEmailTrcDao socializationPICFollowupEmailTrcDao) {
		this.socializationPICFollowupEmailTrcDao = socializationPICFollowupEmailTrcDao;
	}
        
}
