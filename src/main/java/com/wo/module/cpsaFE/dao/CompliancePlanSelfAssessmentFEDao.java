package com.wo.module.cpsaFE.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessment;
import com.wo.module.cpsaFE.vo.CompliancePlanSelfAssessmentFEVo;
import com.wo.module.cpsaFE.vo.CompliancePlanSelfAssessmentQuestionFEVo;

public interface CompliancePlanSelfAssessmentFEDao extends GenericDAO<CompliancePlanSelfAssessment, Long>, RetrieverDataPage<CompliancePlanSelfAssessmentFEVo>{
	
	public CompliancePlanSelfAssessmentFEVo getDataCpsaPic(Long cpsaId, Long userAnswerId) throws Exception;
	
	public List<CompliancePlanSelfAssessmentQuestionFEVo> getQuestionHeaderByCpsaId(Long cpsaId) throws Exception;
	
	public List<CompliancePlanSelfAssessmentQuestionFEVo> getDataQuestionDetail(Long cpsaId, Long questId, Long userAnswerId) throws Exception;
	
	public Long totalDataNotAnswer(Long cpsaId, Long userAnswerId) throws Exception;
	
}
