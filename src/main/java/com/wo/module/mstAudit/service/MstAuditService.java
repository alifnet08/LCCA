package com.wo.module.mstAudit.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.mstAudit.model.MstAudit;
import com.wo.module.mstAudit.vo.MstAuditVO;

public interface MstAuditService extends RetrieverDataPage<MstAuditVO>{
	
	public void save(MstAudit entity);
	
	public void update(MstAudit entity);
	
	public void delete (MstAudit entity);
	
	public MstAudit findById(Long id);
	
	public Integer countSameData(String templateNameIn);
	
	public Integer countSameDataById(Long id, String templateNameIn);
	
	public List<MstAudit> getAllMstAuditData();
	
	public MstAuditVO getSingleDataMstAudit(Long mstAuditId);
	
	public Boolean isUsedInTransaction(Long id);
}
