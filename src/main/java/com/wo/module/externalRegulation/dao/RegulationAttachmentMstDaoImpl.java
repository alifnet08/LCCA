/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.dao;


import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.externalRegulation.model.RegulationAttachmentMst;

/**
 *
 * @author hendra
 */

@Repository("regulationAttachmentMstDao")
public class RegulationAttachmentMstDaoImpl extends GenericDAOHibernate<RegulationAttachmentMst, Long> 
    implements RegulationAttachmentMstDao {
	
	@SuppressWarnings("unchecked")
	public List<RegulationAttachmentMst> getRegulationAttachmentByRegulationId(Long regulationId) throws Exception {

		String hql = " FROM RegulationAttachmentMst where regulationMst.regulationId = :regulationId and enabledFlag = 'Y'";
		Query result = getSession().createQuery(hql);
		result.setParameter("regulationId", regulationId);
		

		return (List<RegulationAttachmentMst>)result.getResultList();
	}	
    
}
