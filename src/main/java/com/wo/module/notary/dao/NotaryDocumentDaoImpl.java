package com.wo.module.notary.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.notary.model.NotaryDocument;

@Repository("notaryDocumentDao")
public class NotaryDocumentDaoImpl extends GenericDAOHibernate<NotaryDocument, Long>
		implements NotaryDocumentDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<NotaryDocument> getNotaryDocumentByNotaryId(Long notaryId) throws Exception {
		String hql = " from NotaryDocument where notary.notaryId = :notaryId and enabledFlag = 'Y' ";
		Query result = getSession().createQuery(hql);
		result.setParameter("notaryId", notaryId);
		return (List<NotaryDocument>) result.getResultList();
	}

}
