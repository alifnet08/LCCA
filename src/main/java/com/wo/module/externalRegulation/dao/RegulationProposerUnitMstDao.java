package com.wo.module.externalRegulation.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.externalRegulation.model.RegulationProposerUnitMst;

public interface RegulationProposerUnitMstDao extends GenericDAO<RegulationProposerUnitMst, Long> {

	public List<RegulationProposerUnitMst> getRegulationProposerUnitByRegulationId(Long regulationId) throws Exception;
	
}
