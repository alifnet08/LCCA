package com.wo.module.regulationSocialization.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupAttachmentTrc;

@Repository("socializationPICFollowupAttachmentTrcDao")
public class SocializationPICFollowupAttachmentTrcDaoImpl extends GenericDAOHibernate<SocializationPICFollowupAttachmentTrc, Long>
		implements SocializationPICFollowupAttachmentTrcDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<SocializationPICFollowupAttachmentTrc> getPICFollowupAttachmentTrcBySocializationId(Long socializationPicFollowupId) throws Exception{
		 String hql = "FROM SocializationPICFollowupAttachmentTrc where socializationPICFollowupTrc.socializationPicFollowupId = :socializationPicFollowupId";
		 Query result = getSession().createQuery(hql);
		 result.setParameter("socializationPicFollowupId", socializationPicFollowupId);
		 return result.getResultList();
	 }
}
