package com.wo.module.regulationSocialization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.regulationSocialization.dao.SocializationRegulationTrcDao;

@Transactional
@Service("socializationRegulationTrcService")
public class SocializationRegulationTrcServiceImpl implements SocializationRegulationTrcService {
    @Autowired
    @Qualifier("socializationRegulationTrcDao")
    private SocializationRegulationTrcDao socializationRegulationTrcDao;

	public SocializationRegulationTrcDao getSocializationRegulationTrcDao() {
		return socializationRegulationTrcDao;
	}

	public void setSocializationRegulationTrcDao(SocializationRegulationTrcDao socializationRegulationTrcDao) {
		this.socializationRegulationTrcDao = socializationRegulationTrcDao;
	}

}
