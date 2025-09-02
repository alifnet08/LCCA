package com.wo.module.tmpRmd.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.tmpRmd.model.TmpRmdCorrespondence;

@Repository("tmpRmdCorrespondencenDao")
public class TmpRmdCorrespondenceDaoImpl extends GenericDAOHibernate<TmpRmdCorrespondence, Long>
		implements TmpRmdCorrespondenceDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<TmpRmdCorrespondence> getTmpRmdCorrespondenceByRmdId(Long rmdId) throws Exception {
		String hql = "FROM TmpRmdRegulation where tmpRmd.rmdId = :rmdId";
		Query result = getSession().createQuery(hql);
		result.setParameter("rmdId", rmdId);
		return result.getResultList();
	}
}
