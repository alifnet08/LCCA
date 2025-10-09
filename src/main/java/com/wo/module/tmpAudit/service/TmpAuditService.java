package com.wo.module.tmpAudit.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.tmpAudit.model.TmpAudit;
import com.wo.module.tmpAudit.vo.AuditConfirmationVO;
import com.wo.module.tmpAudit.vo.TmpAuditVO;
import com.wo.module.tmpAuditApproval.vo.TmpAuditApprovalVO;

public interface TmpAuditService extends RetrieverDataPage<TmpAuditVO> {

	public void save(TmpAudit entity);

	public void update(TmpAudit entity);

	public void delete(TmpAudit entity);

	public TmpAudit findById(Long id);

	Boolean hasReachedMaximumReschedule(Long socializationPicFollowupId) throws Exception;

	public void updateDataAlreadyExist(TmpAudit tmpAudit, TmpAudit tmpAuditDb, String retrieveUserLogin);

	@SuppressWarnings("rawtypes")
	List<TmpAuditVO> searchDataXLS(List<? extends SearchObject> searchCriteria);
	
	List<TmpAuditApprovalVO> getDataApprovalByAuditId(Long auditId);

	List<AuditConfirmationVO> getDataConfirmStatusByAuditId(Long auditId);

	void merge(TmpAudit entity);
	
	public Integer getTmpAuditByIdAndNameIn(Long id, String findingNameIn, Long mstAuditId) throws Exception;
	
	public Boolean hasDuplicateBankCommitment(Long auditId,String auditFinding,String bankResponse,String bankCommitment);

}
