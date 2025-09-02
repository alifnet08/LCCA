package com.wo.module.fineFE.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.fineFE.vo.FineFEVo;
import com.wo.module.trcFineApproval.model.TrcFine;

public interface FineFEService extends RetrieverDataPage<FineFEVo>{

	public void save(TrcFine entity);
	
	public void update(TrcFine entity);
	
	public void delete(TrcFine entity);
	
	public TrcFine findById(Long id);
	
}
