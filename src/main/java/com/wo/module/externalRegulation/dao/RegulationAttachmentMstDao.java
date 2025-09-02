/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.externalRegulation.model.RegulationAttachmentMst;
/**
 *
 * @author hendra
 */
public interface RegulationAttachmentMstDao extends  GenericDAO<RegulationAttachmentMst, Long>{
	
	public List<RegulationAttachmentMst> getRegulationAttachmentByRegulationId(Long regulationId) throws Exception;
	
}
