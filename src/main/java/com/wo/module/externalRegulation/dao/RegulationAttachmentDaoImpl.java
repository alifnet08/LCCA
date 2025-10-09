/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.dao;


import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.externalRegulation.model.RegulationAttachment;

/**
 *
 * @author hendra
 */

@Repository("regulationAttachmentDao")
public class RegulationAttachmentDaoImpl extends GenericDAOHibernate<RegulationAttachment, Long> 
    implements RegulationAttachmentDao {
	
	
	@SuppressWarnings("unchecked")
	public List<RegulationAttachment> getRegulationAttachmentByRegulationId(Long regulationId) throws Exception {

		String hql = " FROM RegulationAttachment where regulation.regulationId = :regulationId and enabledFlag = 'Y'";
		Query result = getSession().createQuery(hql);
		result.setParameter("regulationId", regulationId);
		

		return (List<RegulationAttachment>)result.getResultList();
	}
	
    
}
