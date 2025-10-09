package com.wo.module.cpsa.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessment;
import com.wo.module.cpsa.vo.CompliancePlanSelfAssessmentVo;

public interface CompliancePlanSelfAssesmentDao extends GenericDAO<CompliancePlanSelfAssessment, Long>, RetrieverDataPage<CompliancePlanSelfAssessmentVo>{

}
