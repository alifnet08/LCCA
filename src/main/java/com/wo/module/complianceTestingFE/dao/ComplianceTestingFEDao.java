package com.wo.module.complianceTestingFE.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.complianceTestingFE.vo.ComplianceTestingFEDtlVO;
import com.wo.module.complianceTestingFE.vo.ComplianceTestingFEVO;
import com.wo.module.trcComplianceReview.model.TrcComplianceReview;

public interface ComplianceTestingFEDao extends  GenericDAO<TrcComplianceReview, Long>, 
		RetrieverDataPage<ComplianceTestingFEDtlVO>{
	
}
