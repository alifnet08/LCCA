package com.wo.module.externalRegulation.dao;


import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.externalRegulation.model.RegulationProposerUnit;
import com.wo.module.externalRegulation.model.RegulationTrackRecord;

@Repository("regulationProposerUnitDao")
public class RegulationProposerUnitDaoImpl extends GenericDAOHibernate<RegulationProposerUnit, Long> 
    implements RegulationProposerUnitDao {
	
	
	@SuppressWarnings("unchecked")
	public List<RegulationProposerUnit> getRegulationProposerUnitByRegulationId(Long regulationId) throws Exception {

		String hql = " FROM RegulationProposerUnit where regulation.regulationId = :regulationId and enabledFlag = 'Y'";
		Query result = getSession().createQuery(hql);
		result.setParameter("regulationId", regulationId);
		

		return (List<RegulationProposerUnit>)result.getResultList();
	}
	
    
}
