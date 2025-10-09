package com.wo.module.socializationFE.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.regulationSocialization.model.SocializationTrc;
import com.wo.module.socializationFE.vo.SocializationFEVo;

public interface SocializationFEService extends RetrieverDataPage<SocializationFEVo>{

	public void save(SocializationTrc entity);
	
	public void update(SocializationTrc entity);
	
	public void delete(SocializationTrc entity);
	
	public SocializationTrc findById(Long id);
	
}
