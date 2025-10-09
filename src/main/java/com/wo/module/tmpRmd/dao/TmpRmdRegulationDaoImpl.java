package com.wo.module.tmpRmd.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.tmpRmd.model.TmpRmdRegulation;

@Repository("tmpRmdRegulationDao")
public class TmpRmdRegulationDaoImpl extends GenericDAOHibernate<TmpRmdRegulation, Long>
		implements TmpRmdRegulationDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<TmpRmdRegulation> getTmpRmdRegulationByRmdId(Long rmdId) throws Exception {
		 String hql = "FROM TmpRmdRegulation where tmpRmd.rmdId = :rmdId";
		 Query result = getSession().createQuery(hql);
		 result.setParameter("rmdId", rmdId);
		 return result.getResultList();
	 }
}
