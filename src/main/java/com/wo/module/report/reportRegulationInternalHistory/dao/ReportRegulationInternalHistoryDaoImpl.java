package com.wo.module.report.reportRegulationInternalHistory.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportRegulationInternalHistory.constant.ReportRegulationInternalHistoryConstant;
import com.wo.module.report.reportRegulationInternalHistory.vo.ReportRegulationInternalHistoryVo;

@Repository("reportRegulationInternalHistoryDao")
public class ReportRegulationInternalHistoryDaoImpl extends GenericDAOHibernate<ReportGen, Long>
	implements ReportRegulationInternalHistoryDao, Serializable{

	private static final long serialVersionUID = -9013636357539586228L;

	@SuppressWarnings("rawtypes")
	private void setQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ReportRegulationInternalHistoryConstant.WHERE_CREATE_DATE_FROM, col)) {
						sb.append(" AND TRUNC(at.CREATION_DATE) >= TO_DATE(:createDateFrom, 'yyyy-MM-dd' ) ");
					}
					if (StringUtils.equals(ReportRegulationInternalHistoryConstant.WHERE_CREATE_DATE_TO, col)) {
						sb.append(" AND TRUNC(at.CREATION_DATE) <= TO_DATE(:createDateTo, 'yyyy-MM-dd' ) ");
					}
				}
				
			}
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	private void setQuerySetString(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ReportRegulationInternalHistoryConstant.WHERE_CREATE_DATE_FROM, col)) {
						query.setParameter("createDateFrom", val);
					}
					if (StringUtils.equals(ReportRegulationInternalHistoryConstant.WHERE_CREATE_DATE_TO, col)) {
						query.setParameter("createDateTo", val);
					}
				}
				
			}
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportRegulationInternalHistoryVo> getReportRegulationIntHistoryDetailAsVo(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT pd.NAME_IN AS ACTIVITY_TYPE ");
		sb.append("       ,at.ACTIVITY_DATE ");
		sb.append("       ,at.ACTIVITY_NOTE ");
		sb.append("       ,at.CREATED_BY ");
		sb.append("       ,at.CREATION_DATE ");
		sb.append("       ,TO_CHAR(at.CREATION_DATE, 'DD-MON-YYYY') CREATION_DATE_STR ");
		sb.append("       ,TO_CHAR(at.LAST_UPDATE_DATE, 'DD-MON-YYYY') LAST_UPDATE_DATE_STR ");
		sb.append(" FROM WO_LOG_ACTIVITY at ");
		sb.append(" INNER JOIN WO_MST_PARAMETER_DTL pd ON pd.PARAMETER_DTL_CODE = at.ACTIVITY_TYPE ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" 	And at.ACTIVITY_TYPE = 'ACTIVITY_TYPE_PERATURAN_INTERNAL' ");
		
		this.setQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY at.ACTIVITY_DATE desc ");
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQuerySetString(query, searchCriteria);
		
		List result = query.getResultList();
		List<ReportRegulationInternalHistoryVo> vo = new ArrayList<ReportRegulationInternalHistoryVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				ReportRegulationInternalHistoryVo data = new ReportRegulationInternalHistoryVo();
				
				data.setActivityType(obj[0] != null ? (String) obj[0] : null);
				data.setActivityDate(obj[1] != null ? (Date) obj[1] : null);
				data.setActivityNote(obj[2] != null ? (String) obj[2] : null);
				data.setCreatedBy(obj[3] != null ? (String) obj[3] : null);
				data.setCreatedDate(obj[4] != null ? (Date) obj[4] : null);
				data.setCreatedDateStr(obj[5] != null ? (String) obj[5] : null);
				data.setLastUpdateDateStr(obj[6] != null ? (String) obj[6] : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}


}
