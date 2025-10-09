/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.dbCompliance.dao;

//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.dbCompliance.constant.DBComplianceConstants;
import com.wo.module.dbCompliance.model.DBCompliance;
import com.wo.module.externalRegulation.model.Regulation;
import com.wo.module.parameter.model.ParameterDetail;

/**
 *
 * @author hendra
 */
@Repository("dbComplianceDao")
public class DBComplianceDaoImpl extends GenericDAOHibernate<DBCompliance, Long> implements DBComplianceDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(DBComplianceDaoImpl.class);
	

	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(DBComplianceConstants.SEARCH_BY_REPORT_TYPE, col)) {
							sb.append(" and report_type_id = :reportTypeId ");
					}
					if (StringUtils.equals(DBComplianceConstants.SEARCH_BY_REPORT_NAME, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and report_name_en = :reportName ");
						} else {
							sb.append(" and report_name_in = :reportName ");
						}
						
					}
					if (StringUtils.equals(DBComplianceConstants.SEARCH_BY_DATE, col)) {
						sb.append(" and TRUNC(creation_date) <= TO_DATE(:date,'yyyy-MM-dd') ");
					
				}
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void getQuerySetValue(Query query, List<? extends SearchObject> searchCriteria) {
		
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();

				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(DBComplianceConstants.SEARCH_BY_REPORT_TYPE, col)) {
						query.setParameter("reportTypeId", val);
					}
					if (StringUtils.equals(DBComplianceConstants.SEARCH_BY_REPORT_NAME, col)) {
						query.setParameter("reportTypeName", "%" + val + "%");
					}
					if (StringUtils.equals(DBComplianceConstants.SEARCH_BY_DATE, col)) {
						query.setParameter("date", val);
					}
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {

		Number results = searchCountDataCriteria(searchCriteria);
		if (results == null) {
			results = 0;
		}

		return results.longValue();
	}

	@SuppressWarnings("rawtypes")
	private Number searchCountDataCriteria(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_mst_database_compliance ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<DBCompliance> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<DBCompliance> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<DBCompliance> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ct.DATABASE_COMPLIANCE_ID, REPORT_NAME_EN ");
		sb.append(" from wo_mst_database_compliance ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY DATABASE_COMPLIANCE_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<DBCompliance> vo = new ArrayList<DBCompliance>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				DBCompliance data = new DBCompliance();

				Long dbComplianceId = MathUtil.returnIdObjectToLong(obj[0]);
				data = findById(dbComplianceId);
				
				vo.add(data);
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
	}
	
	public DBCompliance getCheckDataDBCompliance(Long dbComplianceId, String reportName, String reportType) {
    	StringBuilder sb = new StringBuilder();
 		sb.append(" SELECT DATABASE_COMPLIANCE_ID, REPORT_NAME_IN,rt.report_type_in ");
 		sb.append("   FROM WO_MST_DATABASE_COMPLIANCE dc ");
 		sb.append("        INNER JOIN wo_mst_report_type rt ON dc.report_type_id = rt.report_type_id ");
 		sb.append("  WHERE dc.enabled_flag = 'Y' and UPPER(rt.report_type_in) = UPPER(:reportType) ");
 		sb.append("        AND UPPER(dc.REPORT_NAME_IN) = UPPER(:reportName) ");
 		
 		
 		if(dbComplianceId !=null && dbComplianceId > 0) {
 			sb.append("    AND dc.DATABASE_COMPLIANCE_ID <> " + dbComplianceId);
 		}
 		 		
 		Query result = getSession().createSQLQuery(sb.toString());
 		result.setParameter("reportType", reportType);
 		result.setParameter("reportName", reportName);
 		
 		
 		result.setMaxResults(1);
 		
 		List resultList = result.getResultList();
 		
 		DBCompliance dbCompliance = new DBCompliance();
 		if(resultList !=null && resultList.size() > 0) {
	 		for(int i=0; i<resultList.size(); i++) {
	 			Object[] obj = (Object[])resultList.get(i);
	 			
	 			if(obj[0] !=null) {
	 				dbCompliance.setDatabaseComplianceId(Long.parseLong(obj[0]+""));
	 			}
	 			
	 			
	 			dbCompliance.setReportNameIn((String)obj[1]);
	 			dbCompliance.setReportTypeStr((String)obj[2]);
	 			
	 		}
 		}
	 		
 		return dbCompliance;
    }

	

}
