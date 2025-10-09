package com.wo.module.trcRmd.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.trcRmd.model.TrcRmdRegulation;

public interface TrcRmdRegulationDao extends  GenericDAO<TrcRmdRegulation, Long>{

	public List<TrcRmdRegulation> getTrcRmdRegulationByRmdId(Long rmdId) throws Exception;
	
}
