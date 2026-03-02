package com.wo.module.internalRegulationFE.dao;

import java.util.List;

import javax.faces.model.SelectItem;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.internalRegulation.model.InternalRegulation;
import com.wo.module.internalRegulationFE.vo.InternalRegulationFEVO;

public interface InternalRegulationFEDao extends  GenericDAO<InternalRegulation, Long>, 
		RetrieverDataPage<InternalRegulationFEVO>{
	
	public List<SelectItem> getDataDirectorateList();
	
}
