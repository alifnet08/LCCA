/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.externalRegulation.model.RegulationTrackRecordMst;
/**
 *
 * @author hendra
 */
public interface RegulationTrackRecordMstDao extends  GenericDAO<RegulationTrackRecordMst, Long>{

	public List<RegulationTrackRecordMst> getRegulationTrackRecordByRegulationId(Long regulationId) throws Exception;
	
	public List<RegulationTrackRecordMst> getRegulationTrackRecordByRegulationLinkId(Long regulationId) throws Exception;
	
}
