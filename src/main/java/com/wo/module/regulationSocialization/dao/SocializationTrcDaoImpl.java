package com.wo.module.regulationSocialization.dao;


import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.regulationSocialization.model.SocializationTrc;


@Repository("socializationTrcDao")
public class SocializationTrcDaoImpl extends GenericDAOHibernate<SocializationTrc, Long> 
    implements SocializationTrcDao {
	   
    
}
