package com.wo.module.internalRegulationFE.service;

import java.util.List;

import javax.faces.model.SelectItem;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.internalRegulationFE.vo.InternalRegulationFEVO;

public interface InternalRegulationFEService extends RetrieverDataPage<InternalRegulationFEVO>  {
    
	public List<SelectItem> getDataDirectorateList();
	
}
