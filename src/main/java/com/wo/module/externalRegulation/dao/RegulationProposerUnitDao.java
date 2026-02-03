package com.wo.module.externalRegulation.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.externalRegulation.model.RegulationProposerUnit;

public interface RegulationProposerUnitDao extends  GenericDAO<RegulationProposerUnit, Long>{

	public List<RegulationProposerUnit> getRegulationProposerUnitByRegulationId(Long regulationId) throws Exception;
	
}
