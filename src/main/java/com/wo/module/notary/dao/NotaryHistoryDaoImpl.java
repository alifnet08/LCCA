package com.wo.module.notary.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.notary.model.NotaryHistory;

@Repository("notaryHistoryDao")
public class NotaryHistoryDaoImpl extends GenericDAOHibernate<NotaryHistory, Long>
		implements NotaryHistoryDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<NotaryHistory> getNotaryHistoryByNotaryId(Long notaryId) throws Exception {
		String hql = " from NotaryHistory where notary.notaryId = :notaryId and enabledFlag = 'Y' order by notaryHistoryId desc ";
		Query result = getSession().createQuery(hql);
		result.setParameter("notaryId", notaryId);
		return (List<NotaryHistory>) result.getResultList();
	}

}
