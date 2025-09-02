package com.wo.module.engine.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.engine.vo.AutoAbsoluteInternalRegulationVO;
import com.wo.module.log.model.LogHeader;

public interface AutoAbsoluteInternalRegulationDao extends GenericDAO<LogHeader, Long> {

	public List<AutoAbsoluteInternalRegulationVO> getDataAutoAbsoluteInternalRegulation();
	
}
