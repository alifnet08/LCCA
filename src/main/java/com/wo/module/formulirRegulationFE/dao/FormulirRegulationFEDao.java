package com.wo.module.formulirRegulationFE.dao;

import java.util.List;

import javax.faces.model.SelectItem;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.formulirRegulationFE.vo.FormulirRegulationFEVO;
import com.wo.module.internalRegulation.model.InternalRegulation;

public interface FormulirRegulationFEDao extends  GenericDAO<InternalRegulation, Long>, 
		RetrieverDataPage<FormulirRegulationFEVO>{
	
	 public List<SelectItem> getDataDirectorateList();
	
}
