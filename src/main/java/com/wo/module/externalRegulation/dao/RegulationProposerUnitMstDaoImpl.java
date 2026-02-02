package com.wo.module.externalRegulation.dao;


import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.externalRegulation.model.RegulationProposerUnitMst;

@Repository("regulationProposerUnitMstDao")
public class RegulationProposerUnitMstDaoImpl extends GenericDAOHibernate<RegulationProposerUnitMst, Long> 
    implements RegulationProposerUnitMstDao {
	
	
	@SuppressWarnings("unchecked")
	public List<RegulationProposerUnitMst> getRegulationProposerUnitByRegulationId(Long regulationId) throws Exception {

		String hql = " FROM RegulationProposerUnitMst where regulation.regulationId = :regulationId and enabledFlag = 'Y'";
		Query result = getSession().createQuery(hql);
		result.setParameter("regulationId", regulationId);
		

		return (List<RegulationProposerUnitMst>)result.getResultList();
	}
	
    
}
