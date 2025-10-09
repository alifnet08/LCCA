package com.wo.module.tmpRmd.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.tmpRmd.model.TmpRmdRegulation;

public interface TmpRmdRegulationDao extends  GenericDAO<TmpRmdRegulation, Long>{

	public List<TmpRmdRegulation> getTmpRmdRegulationByRmdId(Long rmdId) throws Exception;
	
}
