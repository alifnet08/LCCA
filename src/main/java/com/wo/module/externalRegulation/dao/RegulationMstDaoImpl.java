/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.dao;


import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.externalRegulation.model.RegulationMst;

/**
 *
 * @author hendra
 */

@Repository("regulationMstDao")
public class RegulationMstDaoImpl extends GenericDAOHibernate<RegulationMst, Long> 
    implements RegulationMstDao {
	public Integer getCountHitRegulation(Long regulationId, String accessAction) throws Exception {    	
    	StringBuilder sb = new StringBuilder();
 		sb.append(" select count(1) from wo_log_access a,  ");
 		sb.append("   wo_mst_user u where u.user_id = a.user_id  ");
 		sb.append("           and a.access_id = :access_id  and a.access_action LIKE :access_action");
 		
 		    
 		Query result = getSession().createSQLQuery(sb.toString());
 		result.setParameter("access_id", regulationId);
 		result.setParameter("access_action", accessAction);
 		
 		Number number = (Number) result.getSingleResult();
 		Integer count = 0;
 		if(number !=null) {
 			count = number.intValue();
 		}
 		 		
 		return count;
    
    }
}