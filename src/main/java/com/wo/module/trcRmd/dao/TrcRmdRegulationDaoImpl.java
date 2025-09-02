package com.wo.module.trcRmd.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.trcRmd.model.TrcRmdRegulation;

@Repository("trcRmdRegulationDao")
public class TrcRmdRegulationDaoImpl extends GenericDAOHibernate<TrcRmdRegulation, Long>
		implements TrcRmdRegulationDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<TrcRmdRegulation> getTrcRmdRegulationByRmdId(Long rmdId) throws Exception {
		 String hql = "FROM TrcRmdRegulation where trcRmd.rmdId = :rmdId";
		 Query result = getSession().createQuery(hql);
		 result.setParameter("rmdId", rmdId);
		 return result.getResultList();
	 }
}
