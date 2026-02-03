package com.wo.module.externalRegulation.service;

import java.util.List;

import com.wo.module.externalRegulation.model.RegulationProposerUnitMst;

public interface RegulationProposerUnitMstService  {
    
    public List<RegulationProposerUnitMst> getRegulationProposerUnitByRegulationId(Long regulationId) throws Exception;
}
