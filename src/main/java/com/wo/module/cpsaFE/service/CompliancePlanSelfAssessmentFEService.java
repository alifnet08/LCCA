package com.wo.module.cpsaFE.service;

import java.util.List;

import org.primefaces.model.StreamedContent;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessment;
import com.wo.module.cpsaFE.vo.CompliancePlanSelfAssessmentFEVo;
import com.wo.module.cpsaFE.vo.CompliancePlanSelfAssessmentQuestionFEVo;
import com.wo.module.user.model.User;

public interface CompliancePlanSelfAssessmentFEService extends RetrieverDataPage<CompliancePlanSelfAssessmentFEVo>{

	public CompliancePlanSelfAssessment findById(long cpsaId);
	
	public CompliancePlanSelfAssessmentFEVo getDataCpsaPic(Long cpsaId, Long userAnswerId) throws Exception;
	
	public List<CompliancePlanSelfAssessmentQuestionFEVo> getDataQuestion(Long cpsaId, Long userAnswerId) throws Exception;
	
	public Long totalDataNotAnswer(Long cpsaId, Long userAnswerId) throws Exception;
	
	public void saveAnswer(CompliancePlanSelfAssessmentFEVo cpsaVo, List<CompliancePlanSelfAssessmentQuestionFEVo> questVoList, String userLogin) throws Exception;
	
	public StreamedContent generateDataExcel(Long cpsaId, Long userId1) throws Exception;

	public void submitAnswer(CompliancePlanSelfAssessmentFEVo cpsaVo, List<CompliancePlanSelfAssessmentQuestionFEVo> questVoList, String userLogin) throws Exception;

	public void lockUnlockCpsa(CompliancePlanSelfAssessmentFEVo cpsaVo, User userLogin, boolean lockFlag);
	
}
