package com.wo.module.trcCorrespondenceAml.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;
import com.wo.module.trcCorrespondence.vo.TrcCorrespondenceSearchVo;

public interface TrcCorrespondenceAmlService extends RetrieverDataPage<TrcCorrespondenceSearchVo>{
	
	public void save(TrcCorrespondence entity); 
	
	public void update(TrcCorrespondence entity);
	
	public void delete(TrcCorrespondence entity);
  
    public TrcCorrespondence findById(Long id) ;
}
