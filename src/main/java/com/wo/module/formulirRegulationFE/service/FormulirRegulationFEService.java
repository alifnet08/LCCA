package com.wo.module.formulirRegulationFE.service;

import java.util.List;

import javax.faces.model.SelectItem;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.formulirRegulationFE.vo.FormulirRegulationFEVO;

public interface FormulirRegulationFEService extends RetrieverDataPage<FormulirRegulationFEVO>  {
    
	public List<SelectItem> getDataDirectorateList();
	
}
