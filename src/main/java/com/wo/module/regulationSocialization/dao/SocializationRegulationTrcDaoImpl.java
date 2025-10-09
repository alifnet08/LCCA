package com.wo.module.regulationSocialization.dao;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.regulationSocialization.model.SocializationRegulationTrc;

@Repository("socializationRegulationTrcDao")
public class SocializationRegulationTrcDaoImpl extends GenericDAOHibernate<SocializationRegulationTrc, Long>
		implements SocializationRegulationTrcDao {

}
