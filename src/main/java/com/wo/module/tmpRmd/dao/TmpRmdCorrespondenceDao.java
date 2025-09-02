package com.wo.module.tmpRmd.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.tmpRmd.model.TmpRmdCorrespondence;

public interface TmpRmdCorrespondenceDao extends  GenericDAO<TmpRmdCorrespondence, Long>{

	public List<TmpRmdCorrespondence> getTmpRmdCorrespondenceByRmdId(Long rmdId) throws Exception;
	
}
