package com.wo.module.mstAudit.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.mstAudit.model.MstAudit;
import com.wo.module.mstAudit.vo.MstAuditVO;

/**
 *
 * @author Neir Kate
 * 
 */
public interface MstAuditDao extends GenericDAO<MstAudit, Long>, RetrieverDataPage<MstAuditVO> {

	public Integer countSameData(String tempalteNameIn);
	
	public Integer countSameDataById(Long id,String tempalteNameIn);
	
	public List<MstAudit> getAllMstAuditData();

	public MstAuditVO getSingleData(Long mstAuditId);
	
	public Boolean isUsedInTransaction(Long id);
}	