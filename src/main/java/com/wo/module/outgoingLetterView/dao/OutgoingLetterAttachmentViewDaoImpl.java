package com.wo.module.outgoingLetterView.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.outgoingLetterView.model.OutgoingLetterAttachmentView;

@Repository("outgoingLetterAttachmentViewDao")
public class OutgoingLetterAttachmentViewDaoImpl extends GenericDAOHibernate<OutgoingLetterAttachmentView, Long>
	implements OutgoingLetterAttachmentViewDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<OutgoingLetterAttachmentView> getOutgoingLetterAttachmentViewByOutgoingLetterId(Long outgoingLetterViewId)
			throws Exception {
		String hql = " from OutgoingLetterAttachmentView where outgoingLetterView.outgoingLetterId =: outgoingLetterId and enabledFlag = 'Y' ";
		Query result = getSession().createQuery(hql);
		result.setParameter("outgoingLetterId", outgoingLetterViewId);
		
		return (List<OutgoingLetterAttachmentView>) result.getResultList();
	}

}
