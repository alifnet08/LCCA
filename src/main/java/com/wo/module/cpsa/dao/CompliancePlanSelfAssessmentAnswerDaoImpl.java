package com.wo.module.cpsa.dao;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessmentAnswer;

@Repository("compliancePlanSelfAssessmentAnswerDao")
public class CompliancePlanSelfAssessmentAnswerDaoImpl extends GenericDAOHibernate<CompliancePlanSelfAssessmentAnswer, Long>
	implements CompliancePlanSelfAssessmentAnswerDao, Serializable{

	private static final long serialVersionUID = 8643924209629650513L;

	@SuppressWarnings("rawtypes")
	@Override
	public List<CompliancePlanSelfAssessmentAnswer> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}