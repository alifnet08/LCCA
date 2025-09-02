/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.dao;


import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.externalRegulation.model.RegulationTrackRecord;

/**
 *
 * @author hendra
 */

@Repository("regulationTrackRecordDao")
public class RegulationTrackRecordDaoImpl extends GenericDAOHibernate<RegulationTrackRecord, Long> 
    implements RegulationTrackRecordDao {
	
	
	@SuppressWarnings("unchecked")
	public List<RegulationTrackRecord> getRegulationTrackRecordByRegulationId(Long regulationId) throws Exception {

		String hql = " FROM RegulationTrackRecord where regulation.regulationId = :regulationId and enabledFlag = 'Y'";
		Query result = getSession().createQuery(hql);
		result.setParameter("regulationId", regulationId);
		

		return (List<RegulationTrackRecord>)result.getResultList();
	}
	
    
}
