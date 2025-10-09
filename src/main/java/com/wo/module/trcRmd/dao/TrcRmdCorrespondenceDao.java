package com.wo.module.trcRmd.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.trcRmd.model.TrcRmdCorrespondence;

public interface TrcRmdCorrespondenceDao extends GenericDAO<TrcRmdCorrespondence, Long>{
	
	List<TrcRmdCorrespondence> getTrcRmdCorrespondenceByRmdId(Long rmdId) throws Exception;
}