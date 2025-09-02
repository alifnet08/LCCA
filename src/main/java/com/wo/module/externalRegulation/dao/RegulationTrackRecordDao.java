/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.externalRegulation.model.RegulationTrackRecord;
/**
 *
 * @author hendra
 */
public interface RegulationTrackRecordDao extends  GenericDAO<RegulationTrackRecord, Long>{

	public List<RegulationTrackRecord> getRegulationTrackRecordByRegulationId(Long regulationId) throws Exception;
	
}
