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

	@SuppressWarnings("unchecked")
	@Override
	public List<NotaryHistory> searchHistory(String notaryName) throws Exception {
		StringBuilder hql = new StringBuilder();
		hql.append(" from NotaryHistory h join fetch h.notary n left join fetch n.notaryCategory ");
		hql.append(" where h.enabledFlag = 'Y' ");
		if (notaryName != null && notaryName.trim().length() > 0) {
			hql.append(" and lower(n.notaryName) like :notaryName ");
		}
		hql.append(" order by n.notaryId asc, h.notaryHistoryId asc ");
		Query result = getSession().createQuery(hql.toString());
		if (notaryName != null && notaryName.trim().length() > 0) {
			result.setParameter("notaryName", "%" + notaryName.trim().toLowerCase() + "%");
		}
		return (List<NotaryHistory>) result.getResultList();
	}

}
