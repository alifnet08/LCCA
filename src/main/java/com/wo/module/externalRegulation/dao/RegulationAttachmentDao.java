/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.externalRegulation.model.RegulationAttachment;
/**
 *
 * @author hendra
 */
public interface RegulationAttachmentDao extends  GenericDAO<RegulationAttachment, Long>{

	public List<RegulationAttachment> getRegulationAttachmentByRegulationId(Long regulationId) throws Exception;
}
