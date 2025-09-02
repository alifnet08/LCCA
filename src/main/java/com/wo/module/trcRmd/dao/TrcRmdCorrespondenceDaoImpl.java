package com.wo.module.trcRmd.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.trcRmd.model.TrcRmdCorrespondence;

@Repository("trcRmdCorrespondenceDao")
public class TrcRmdCorrespondenceDaoImpl extends  GenericDAOHibernate<TrcRmdCorrespondence, Long>
	implements TrcRmdCorrespondenceDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<TrcRmdCorrespondence> getTrcRmdCorrespondenceByRmdId(Long rmdId) throws Exception {
		 String hql = "FROM TrcRmdCorrespondence where trcRmd.rmdId = :rmdId";
		 Query result = getSession().createQuery(hql);
		 result.setParameter("rmdId", rmdId);
		 return result.getResultList();
	}

}
