package com.wo.module.cpsaVerification.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessment;
import com.wo.module.cpsaVerification.vo.CpsaVerificationPicVo;
import com.wo.module.cpsaVerification.vo.CpsaVerificationQuestionVo;
import com.wo.module.cpsaVerification.vo.CpsaVerificationVo;

public interface CpsaVerificationDao extends GenericDAO<CompliancePlanSelfAssessment, Long>, RetrieverDataPage<CpsaVerificationVo>{

	public CpsaVerificationPicVo getDataCpsaPic(Long cpsaId, Long userId1, Long userApproval) throws Exception;
	
	public List<CpsaVerificationQuestionVo> getQuestionHeaderByCpsaId(Long cpsaId) throws Exception;
	
	public List<CpsaVerificationQuestionVo> getDataQuestionDetail(Long cpsaId, Long questId, Long userId1) throws Exception;
}
