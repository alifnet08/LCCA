/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.dao;


import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.externalRegulation.model.RegulationTrackRecordMst;

/**
 *
 * @author hendra
 */

@Repository("regulationTrackRecordMstDao")
public class RegulationTrackRecordMstDaoImpl extends GenericDAOHibernate<RegulationTrackRecordMst, Long> 
    implements RegulationTrackRecordMstDao {
	
	@SuppressWarnings("unchecked")
	public List<RegulationTrackRecordMst> getRegulationTrackRecordByRegulationId(Long regulationId) throws Exception {

		String hql = " FROM RegulationTrackRecordMst where regulationMst.regulationId = :regulationId and enabledFlag = 'Y'";
		Query result = getSession().createQuery(hql);
		result.setParameter("regulationId", regulationId);
		

		return (List<RegulationTrackRecordMst>)result.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<RegulationTrackRecordMst> getRegulationTrackRecordByRegulationLinkId(Long regulationId) throws Exception {

		String hql = " FROM RegulationTrackRecordMst where regulationLinkId = :regulationId and enabledFlag = 'Y'";
		Query result = getSession().createQuery(hql);
		result.setParameter("regulationId", regulationId);
		

		return (List<RegulationTrackRecordMst>)result.getResultList();
	}
}
