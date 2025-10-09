package com.wo.module.regulationSocialization.dao;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.regulationSocialization.model.SocializationPICComplianceTrc;

@Repository("socializationPICComplianceTrcDao")
public class SocializationPICComplianceTrcDaoImpl extends GenericDAOHibernate<SocializationPICComplianceTrc, Long>
		implements SocializationPICComplianceTrcDao {

}
