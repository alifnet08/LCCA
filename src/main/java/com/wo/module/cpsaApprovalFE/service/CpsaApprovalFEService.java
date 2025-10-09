package com.wo.module.cpsaApprovalFE.service;

import java.util.List;

import org.primefaces.model.StreamedContent;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessment;
import com.wo.module.cpsaApprovalFE.vo.CpsaApprovalFEVo;
import com.wo.module.cpsaApprovalFE.vo.CpsaApprovalQuestionFEVo;

public interface CpsaApprovalFEService extends RetrieverDataPage<CpsaApprovalFEVo>{

	public CompliancePlanSelfAssessment findById(long cpsaId);
	
	public CpsaApprovalFEVo getDataCpsaPic(Long cpsaId, Long userId1, Long userApprovalId) throws Exception;
	
	public List<CpsaApprovalQuestionFEVo> getDataQuestion(Long cpsaId, Long userId1) throws Exception;	
		
	public void approval(CpsaApprovalFEVo cpsaVo, String userLogin) throws Exception;
	
	public void reject(CpsaApprovalFEVo cpsaVo, String userLogin) throws Exception;
	
	public StreamedContent generateDataExcel(Long cpsaId, Long userId1, Long userApprovalId) throws Exception;
	
}
