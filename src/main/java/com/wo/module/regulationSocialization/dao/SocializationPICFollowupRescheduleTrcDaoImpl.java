package com.wo.module.regulationSocialization.dao;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupRescheduleTrc;

@Repository("socializationPICFollowupRescheduleTrcDao")
public class SocializationPICFollowupRescheduleTrcDaoImpl
		extends GenericDAOHibernate<SocializationPICFollowupRescheduleTrc, Long>
		implements SocializationPICFollowupRescheduleTrcDao {

}
