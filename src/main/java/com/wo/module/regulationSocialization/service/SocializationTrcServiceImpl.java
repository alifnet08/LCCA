package com.wo.module.regulationSocialization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.regulationSocialization.dao.SocializationTrcDao;
import com.wo.module.regulationSocialization.model.SocializationTrc;

@Transactional
@Service("socializationTrcService")
public class SocializationTrcServiceImpl implements SocializationTrcService {
  
	@Autowired
    @Qualifier("socializationTrcDao")
    private SocializationTrcDao socializationTrcDao;

	public SocializationTrcDao getSocializationTrcDao() {
		return socializationTrcDao;
	}

	public void setSocializationTrcDao(SocializationTrcDao socializationTrcDao) {
		this.socializationTrcDao = socializationTrcDao;
	}

	@Override
	public SocializationTrc findById(Long id) {
		return socializationTrcDao.getById(id);
	}
        
}
