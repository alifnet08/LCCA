package com.wo.module.outgoingLetter.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.outgoingLetter.model.OutgoingLetterAttachment;

@Repository("outgoingLetterAttachmentDao")
public class OutgoingLetterAttachmentDaoImpl extends GenericDAOHibernate<OutgoingLetterAttachment, Long> implements OutgoingLetterAttachmentDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<OutgoingLetterAttachment> getOutgoingLetterAttachmentByOutgoingLetterId(Long outgoingLetterId)
			throws Exception {
		String hql = " from OutgoingLetterAttachment where outgoingLetter.outgoingLetterId =: outgoingLetterId and enabledFlag = 'Y' ";
		Query result = getSession().createQuery(hql);
		result.setParameter("outgoingLetterId", outgoingLetterId);
		
		return (List<OutgoingLetterAttachment>) result.getResultList();
	}
	
}