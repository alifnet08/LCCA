package com.wo.module.tmpAudit.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.tmpAudit.model.TmpAudit;
import com.wo.module.tmpAudit.vo.AuditConfirmationVO;
import com.wo.module.tmpAudit.vo.TmpAuditVO;
import com.wo.module.tmpAuditApproval.vo.TmpAuditApprovalVO;

public interface TmpAuditDao extends GenericDAO<TmpAudit, Long>, RetrieverDataPage<TmpAuditVO> {

	Boolean hasReachedMaximumReschedule(Long socializationPicFollowupId) throws Exception;

	@SuppressWarnings("rawtypes")
	List<TmpAuditVO> searchDataXLS(List<? extends SearchObject> searchCriteria);

	List<TmpAuditApprovalVO> getDataApprovalByAuditId(Long auditId);

	List<AuditConfirmationVO> getDataConfirmStatusByAuditId(Long auditId);
	
	Integer getTmpAuditByIdAndNameIn(Long id, String findingNameIn, Long mstAuditId) throws Exception;

}
