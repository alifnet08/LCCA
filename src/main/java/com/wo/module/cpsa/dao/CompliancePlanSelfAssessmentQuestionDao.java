package com.wo.module.cpsa.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessmentQuestion;
import com.wo.module.cpsa.vo.CompliancePlanSelfAssessmentPicVo;
import com.wo.module.cpsa.vo.CompliancePlanSelfAssessmentQuestionVo;

public interface CompliancePlanSelfAssessmentQuestionDao extends GenericDAO<CompliancePlanSelfAssessmentQuestion, Long>, RetrieverDataPage<CompliancePlanSelfAssessmentQuestionVo>{

	  public List<CompliancePlanSelfAssessmentPicVo> getDataCpsaPic(Long cpsaId, Long userId, String cpsaStatus) throws Exception;
	  
	  public List<CompliancePlanSelfAssessmentQuestionVo> getDataPicAnswer(Long cpsaId, Long picId, Long questId, Long parentQuestId, boolean flagHeader) throws Exception;
	  
	  public Long totalDataNotAnswer(Long cpsaId, Long userAnswerId) throws Exception;
	  
	  public Integer totalDataQuestion(Long cpsaId) throws Exception;
	  
}
