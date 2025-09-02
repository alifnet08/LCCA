/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.litigation.dao;

//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.litigation.constant.LitigationConstants;
import com.wo.module.litigation.model.LitigationNew;

/**
 *
 * @author hendra
 */
@Repository("litigationNewDao")
public class LitigationNewDaoImpl extends GenericDAOHibernate<LitigationNew, Long> implements LitigationNewDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(LitigationNewDaoImpl.class);
	
	@SuppressWarnings({ "rawtypes" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(LitigationConstants.SEARCH_BY_JENIS_PERKARA, col)) {
						sb.append(" and UPPER(ln.case_type) = UPPER(:caseType) ");
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_DIV_NAME_OR_BRANCH_OFFICE, col)) {
						sb.append(" and UPPER(ln.DIV_NAME_OR_BRANCH_OFFICE) LIKE UPPER(:divNameBranchOff) ");
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_SEGMENT, col)) {
						sb.append(" and UPPER(ln.SEGMENT) LIKE UPPER(:segment) ");
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_DEBTOR, col)) {
						sb.append(" AND UPPER(lpt.DEBITUR) LIKE UPPER(:debtor) ");
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_CASE_NUMBER, col)) {
						sb.append(" AND (UPPER(lpp.CASE_NUMBER) LIKE UPPER(:caseNumber) ");
						sb.append(" 	OR UPPER(lpt.CASE_NUMBER) LIKE UPPER(:caseNumber) ");
						sb.append(" 	OR UPPER(lpapk.CASE_NUMBER) LIKE UPPER(:caseNumber)) ");
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_REPORT_NUMBER, col)) {
						sb.append(" AND (UPPER(lppr.CASE_NUMBER) LIKE UPPER(:reportNumber) ");
						sb.append(" 	OR UPPER(lptr.CASE_NUMBER) LIKE UPPER(:reportNumber)) ");
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
					if (StringUtils.equals(LitigationConstants.SEARCH_BY_JENIS_PERKARA, col)) {
						query.setParameter("caseType", val);
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_DIV_NAME_OR_BRANCH_OFFICE, col)) {
						query.setParameter("divNameBranchOff", "%"+val+"%");
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_SEGMENT, col)) {
						query.setParameter("segment", "%"+val+"%");
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_DEBTOR, col)) {
						query.setParameter("debtor", "%"+val+"%");
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_CASE_NUMBER, col)) {
						query.setParameter("caseNumber", "%"+val+"%");
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_REPORT_NUMBER, col)) {
						query.setParameter("reportNumber", "%"+val+"%");
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
		sb.append(" SELECT COUNT(1) ");
		sb.append(" FROM WO_MST_LITIGATION ln ");
		sb.append(" LEFT JOIN WO_MST_PARAMETER_DTL pd1 ON pd1.PARAMETER_DTL_CODE = ln.CASE_TYPE ");
		sb.append(" LEFT JOIN WO_MST_PARAMETER_DTL pd2 ON pd2.PARAMETER_DTL_CODE = ln.CASE_TYPE_DTL ");
		sb.append(" INNER JOIN WO_MST_LITI_PERDATA_TERGUGAT lpt ON lpt.LITIGATION_ID = ln.LITIGATION_ID  ");
		sb.append(" INNER JOIN WO_MST_LITI_PERDATA_PENGGUGAT lpp ON lpp.LITIGATION_ID = ln.LITIGATION_ID ");
		sb.append(" INNER JOIN WO_MST_LITI_PAILIT_PKPU lpapk ON lpapk.LITIGATION_ID = ln.LITIGATION_ID ");
		sb.append(" INNER JOIN WO_MST_LITI_PIDANA_PELAPOR lppr ON lppr.LITIGATION_ID = ln.LITIGATION_ID ");
		sb.append(" INNER JOIN WO_MST_LITI_PIDANA_TERLAPOR lptr ON lptr.LITIGATION_ID = ln.LITIGATION_ID ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND ln.ENABLED_FLAG <> 'N' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<LitigationNew> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<LitigationNew> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<LitigationNew> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT ln.LITIGATION_ID, ");
		sb.append("	pd1.NAME_IN AS CASE_TYPE_NAME, ");
		sb.append("	pd2.NAME_IN AS CASE_TYPE_DTL_NAME, ");
		sb.append("	ln.DIV_NAME_OR_BRANCH_OFFICE, ");
		sb.append("	ln.SEGMENT, ");
		sb.append(" lpt.DEBITUR, ");
		sb.append(" CASE "
				+ "		WHEN lpp.CASE_NUMBER IS NOT NULL "
				+ "		THEN lpp.CASE_NUMBER "
				+ "		WHEN lpt.CASE_NUMBER IS NOT NULL "
				+ "		THEN lpt.CASE_NUMBER "
				+ "		WHEN lpapk.CASE_NUMBER IS NOT NULL "
				+ "		THEN lpapk.CASE_NUMBER "
				+ " END AS NOMOR_PERKARA, ");
		sb.append(" CASE "
				+ "		WHEN lppr.CASE_NUMBER IS NOT NULL "
				+ "		THEN lppr.CASE_NUMBER "
				+ "		WHEN lptr.CASE_NUMBER IS NOT NULL "
				+ "		THEN lptr.CASE_NUMBER "
				+ " END AS NOMOR_LAPORAN ");
		sb.append(" FROM WO_MST_LITIGATION ln ");
		sb.append(" LEFT JOIN WO_MST_PARAMETER_DTL pd1 ON pd1.PARAMETER_DTL_CODE = ln.CASE_TYPE ");
		sb.append(" LEFT JOIN WO_MST_PARAMETER_DTL pd2 ON pd2.PARAMETER_DTL_CODE = ln.CASE_TYPE_DTL ");
		sb.append(" INNER JOIN WO_MST_LITI_PERDATA_TERGUGAT lpt ON lpt.LITIGATION_ID = ln.LITIGATION_ID  ");
		sb.append(" INNER JOIN WO_MST_LITI_PERDATA_PENGGUGAT lpp ON lpp.LITIGATION_ID = ln.LITIGATION_ID ");
		sb.append(" INNER JOIN WO_MST_LITI_PAILIT_PKPU lpapk ON lpapk.LITIGATION_ID = ln.LITIGATION_ID ");
		sb.append(" INNER JOIN WO_MST_LITI_PIDANA_PELAPOR lppr ON lppr.LITIGATION_ID = ln.LITIGATION_ID ");
		sb.append(" INNER JOIN WO_MST_LITI_PIDANA_TERLAPOR lptr ON lptr.LITIGATION_ID = ln.LITIGATION_ID ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND ln.ENABLED_FLAG <> 'N' ");

		this.getQueryWhereString(sb, searchCriteria);
		
		sb.append(" ORDER BY ln.LITIGATION_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<LitigationNew> vo = new ArrayList<LitigationNew>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				LitigationNew data = new LitigationNew();
				
				data.setLitigationId(MathUtil.returnIdObjectToLong(obj[0]));
				data.setCaseTypeName(obj[1].toString());
				if(obj[2] != null) {
					data.setCaseTypeDtlName(obj[2].toString());
				}else {
					data.setCaseTypeDtlName("");
				}
			//	data.setLitigationNo(obj[3].toString());
				data.setDivisionOrBranchOffice(obj[3].toString());
				data.setSegment(obj[4].toString());		
				data.setDebtorName(obj[5] != null ? obj[5].toString() : "");
				data.setCaseNumberName(obj[6] != null ? obj[6].toString() : "");
				data.setReportNumberName(obj[7] != null ? obj[7].toString() : "");
				
				vo.add(data);
			}
		}
		return vo;
	}
	

}
