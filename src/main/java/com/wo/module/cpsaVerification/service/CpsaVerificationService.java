package com.wo.module.cpsaVerification.service;

import java.util.List;

import org.primefaces.model.StreamedContent;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessment;
import com.wo.module.cpsaVerification.vo.CpsaVerificationPicVo;
import com.wo.module.cpsaVerification.vo.CpsaVerificationQuestionVo;
import com.wo.module.cpsaVerification.vo.CpsaVerificationVo;
import com.wo.module.user.model.User;

public interface CpsaVerificationService extends RetrieverDataPage<CpsaVerificationVo>{
	
	public CpsaVerificationPicVo getDataCpsaPic(Long cpsaId, Long userId1, Long userApprovalId) throws Exception;

	public List<CpsaVerificationQuestionVo> getDataQuestion(Long cpsaId, Long userId1) throws Exception;	
	
	public void save(CompliancePlanSelfAssessment entity, User user) throws Exception;
	
	public void submit(CompliancePlanSelfAssessment entity, User user) throws Exception;

	public StreamedContent generateDataExcel(Long cpsaId, Long userId1) throws Exception;
	
}
