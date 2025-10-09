package com.wo.module.regulationSocialization.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupTrc;

@Repository("socializationPICFollowupTrcDao")
public class SocializationPICFollowupTrcDaoImpl extends GenericDAOHibernate<SocializationPICFollowupTrc, Long>
		implements SocializationPICFollowupTrcDao {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SocializationPICFollowupTrc> getPICFollowupTrcBySocializationId(Long socializationId) throws Exception{
		 String hql = "FROM SocializationPICFollowupTrc where socializationPICFollowupTrc.socializationTrc.socializationId = :socializationId";
		 Query result = getSession().createQuery(hql);
		 result.setParameter("socializationId", socializationId);
		 return result.getResultList();
	 }

}
