package com.wo.module.cpsaApprovalFE.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessment;
import com.wo.module.cpsaApprovalFE.vo.CpsaApprovalFEVo;
import com.wo.module.cpsaApprovalFE.vo.CpsaApprovalQuestionFEVo;

public interface CpsaApprovalFEDao extends GenericDAO<CompliancePlanSelfAssessment, Long>, RetrieverDataPage<CpsaApprovalFEVo>{
	
	public CpsaApprovalFEVo getDataCpsaPic(Long cpsaId, Long userId1, Long userApproval) throws Exception;
	
	public List<CpsaApprovalQuestionFEVo> getQuestionHeaderByCpsaId(Long cpsaId) throws Exception;
	
	public List<CpsaApprovalQuestionFEVo> getDataQuestionDetail(Long cpsaId, Long questId, Long userId1) throws Exception;
		
}
