package com.wo.module.cpsaView.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessmentPic;
import com.wo.module.cpsaView.vo.CpsaPicViewVo;

public interface CpsaPicViewDao extends GenericDAO<CompliancePlanSelfAssessmentPic, Long>, RetrieverDataPage<CpsaPicViewVo>{

}
