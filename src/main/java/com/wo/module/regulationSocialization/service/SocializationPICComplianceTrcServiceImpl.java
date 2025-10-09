package com.wo.module.regulationSocialization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.regulationSocialization.dao.SocializationPICComplianceTrcDao;

@Transactional
@Service("socializationPICComplianceTrcService")
public class SocializationPICComplianceTrcServiceImpl implements SocializationPICComplianceTrcService {
   
	@Autowired
    @Qualifier("socializationPICComplianceTrcDao")
    private SocializationPICComplianceTrcDao socializationPICComplianceTrcDao;

        
}
