package com.wo.module.trcAuditView.dao;

import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.trcAudit.model.TrcAudit;
import com.wo.module.trcAuditView.constant.AuditViewConstant;
import com.wo.module.trcAuditView.vo.TrcAuditViewVO;

@Repository("trcAuditViewDao")
public class TrcAuditViewDaoImpl extends GenericDAOHibernate<TrcAudit, Long> implements TrcAuditViewDao, AuditViewConstant {

	@SuppressWarnings("rawtypes")
	@Override
	public List<TrcAuditViewVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		List<TrcAuditViewVO> voList = searchDataCriteria(searchCriteria, first, pageSize);

		return voList;
	}
	
	@SuppressWarnings("rawtypes")
	private List<TrcAuditViewVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first,
			int pageSize) {

		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT " + 
					"	ta.audit_id auditId, " + 
					"	pd1.name_in auditorIn, " + 
					"	pd1.name_en auditorEn," + 
					"	pd2.name_in auditObjectIn," + 
					"	pd2.name_en auditObjectEn," + 
					"	pd3.name_in auditCategoryIn," + 
					"	pd3.name_en auditCategoryEn," + 
					"	TO_CHAR(ta.audit_date_from, 'dd-Mon-yyyy') auditDateFrom," + 
					"	TO_CHAR(ta.audit_date_to, 'dd-Mon-yyyy') auditDateTo," +
					"	ta.scope," + 
					" 	ta.STATUS, " +
					"	pd4.name_in statusIn," + 
					"	pd4.name_en statusEn," + 
					"	ta.audit_topic_in," + 
					"	ta.audit_topic_en" +
					"	,ta.finding_name_in " +
					"	,ta.finding_name_en " +
					"	,ma.audit_template_name_in " +
					"	,ma.audit_template_name_en " +
					" FROM wo_trc_audit ta" + 
					" LEFT JOIN wo_mst_audit ma ON ma.mst_audit_id = ta.mst_audit_id " +
					" LEFT JOIN wo_mst_parameter_dtl pd1 ON pd1.parameter_dtl_code = ta.auditor" + 
					" LEFT JOIN wo_mst_parameter_dtl pd2 ON pd2.parameter_dtl_code = ta.audit_object" + 
					" LEFT JOIN wo_mst_parameter_dtl pd3 ON pd3.parameter_dtl_code = ta.audit_category" + 
					" LEFT JOIN wo_mst_parameter_dtl pd4 ON pd4.parameter_dtl_code = ta.STATUS" + 
					" WHERE 1=1 and ta.enabled_flag = 'Y' ");

		sb = getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY ta.audit_id DESC ");
		
		
		Query result = getSession().createSQLQuery(sb.toString());
		result.setFirstResult(first);
		result.setMaxResults(pageSize);
		
		List resultList = result.getResultList();

		List<TrcAuditViewVO> vo = new ArrayList<TrcAuditViewVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				TrcAuditViewVO data = new TrcAuditViewVO();
				data.setAuditId(obj[0] != null ? ((java.math.BigDecimal) obj[0]).longValue() : null);
				data.setAuditorIn(obj[1] != null ? (String) obj[1] : null);
				data.setAuditorEn(obj[2] != null ? (String) obj[2] : null);
				data.setAuditObjectIn(obj[3] != null ? (String) obj[3] : null);
				data.setAuditObjectEn(obj[4] != null ? (String) obj[4] : null);
				data.setAuditCategoryIn(obj[5] != null ? (String) obj[5] : null);
				data.setAuditCategoryEn(obj[6] != null ? (String) obj[6] : null);
				data.setAuditDateFrom(obj[7] != null ? (String) obj[7] : null);
				data.setAuditDateTo(obj[8] != null ? (String) obj[8] : null);
				data.setScope(obj[9] != null ? FacesUtil.convertClobToString((Clob)obj[9]) : null);
				data.setStatusCd(obj[10] != null ? (String) obj[10] : null);
				data.setStatusIn(obj[11] != null ? (String) obj[11] : null);
				data.setStatusEn(obj[12] != null ? (String) obj[12] : null);
				data.setAuditTopicIn(obj[13] != null ? (String) obj[13] : null);
				data.setAuditTopicEn(obj[14] != null ? (String) obj[14] : null);
				data.setFindingNameIn(obj[15] != null ? (String) obj[15] : null);
				data.setFindingNameEn(obj[16] != null ? (String) obj[16] : null);
				data.setAuditTemplateNameIn(obj[17] != null ? (String) obj[17] : null);
				data.setAuditTemplateNameEn(obj[18] != null ? (String) obj[18] : null);
				vo.add(data);
			}
		}

		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
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
		sb.append(" SELECT count(1) " + 
				" FROM wo_trc_audit ta" + 
				" LEFT JOIN wo_mst_audit ma ON ma.mst_audit_id = ta.mst_audit_id " +
				" LEFT JOIN wo_mst_parameter_dtl pd1 ON pd1.parameter_dtl_code = ta.auditor" + 
				" LEFT JOIN wo_mst_parameter_dtl pd2 ON pd2.parameter_dtl_code = ta.audit_object" + 
				" LEFT JOIN wo_mst_parameter_dtl pd3 ON pd3.parameter_dtl_code = ta.audit_category" + 
				" LEFT JOIN wo_mst_parameter_dtl pd4 ON pd4.parameter_dtl_code = ta.STATUS" + 
				" WHERE 1=1 and ta.enabled_flag = 'Y' ");

		sb = getQueryWhereString(sb, searchCriteria);

		Query result = getSession().createSQLQuery(sb.toString());

		return (Number) result.getSingleResult();
	}

	@SuppressWarnings({ "rawtypes", "unused", "static-access" })
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue() != null ? searchVal.getSearchValueAsString() : "";
				Object valReal = searchVal.getSearchValue();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(WHERE_AUDIT_FOLLOWUP, col)) {
						sb.append(" and ta.auditor = '" + val + "' ");
					} else if (StringUtils.equals(WHERE_AUDIT_OBJECT, col)) {
						sb.append(" and ta.audit_object = '" + val + "' ");
					} else if (StringUtils.equals(WHERE_AUDIT_STATUS, col)) {
						sb.append(" and ta.status = '" + val + "' ");
					} else if (StringUtils.equals(WHERE_AUDIT_PERIOD_FROM, col)) {
						
						sb.append(" and TRUNC(ta.audit_date_from) >= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");
						
					} else if (StringUtils.equals(WHERE_AUDIT_PERIOD_TO, col)) {
						
						sb.append(" and TRUNC(ta.audit_date_to) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
						
					} else if (StringUtils.equals(WHERE_AUDIT_TARGET_FROM, col)) {						
						sb.append(" and EXISTS( SELECT 1 FROM"
								+ "            wo_tmp_audit_pic_followup apf WHERE"
								+ "            apf.socialization_id = ta.audit_id"
								+ "                AND TRUNC(apf.target_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ) ");
						
					} else if (StringUtils.equals(WHERE_AUDIT_TARGET_TO, col)) {						
						sb.append(" and EXISTS( SELECT 1 FROM"
								+ "            wo_tmp_audit_pic_followup apf WHERE"
								+ "            apf.socialization_id = ta.audit_id"
								+ "                AND TRUNC(apf.target_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ) ");					
					} else if (StringUtils.equals(WHERE_AUDIT_DIVISION_ID, col)) {						
						sb.append(" and (EXISTS( SELECT 1 FROM"
								+ "            wo_tmp_audit_pic_followup apf WHERE"
								+ "            apf.audit_id = ta.audit_id"
								+ "                AND apf.division_id = " + val + ") OR EXISTS( SELECT 1 FROM wo_tmp_audit_pic_followup apf WHERE apf.audit_id = ta.audit_id AND apf.division_id IS NULL )) ");
						
					} else if (StringUtils.equals(WHERE_AUDIT_FOLLOWUP_STATUS, col)) {
						if (val.equals("x")) {
							sb.append(" and (EXISTS( SELECT 1 FROM"
									+ "            wo_trc_audit_pic_followup apf WHERE"
									+ "            apf.audit_id = ta.audit_id"
									+ "                AND apf.followup_status is null) OR (SELECT COUNT(1) FROM wo_trc_audit_pic_followup apf WHERE apf.audit_id = ta.audit_id) <= 0) ");
						} else {
							sb.append(" and EXISTS( SELECT 1 FROM"
									+ "            wo_trc_audit_pic_followup apf WHERE"
									+ "            apf.audit_id = ta.audit_id"
									+ "                AND apf.followup_status = '" + val + "') ");
						}
						
					} else if (StringUtils.equals(WHERE_AUDIT_COMPLIANCE_STATUS, col)) {
						if (val.equals("x")) {
							sb.append(" and (EXISTS( SELECT 1 FROM"
									+ "            wo_trc_audit_pic_followup apf WHERE"
									+ "            apf.audit_id = ta.audit_id"
									+ "                AND apf.compliance_status is null) OR (SELECT COUNT(1) FROM wo_trc_audit_pic_followup apf WHERE apf.audit_id = ta.audit_id) <= 0) ");
						} else {
							sb.append(" and EXISTS( SELECT 1 FROM"
									+ "            wo_trc_audit_pic_followup apf WHERE"
									+ "            apf.audit_id = ta.audit_id"
									+ "                AND apf.compliance_status = '" + val + "') ");
						}
					} else if (StringUtils.equals(WHERE_AUDIT_FINDING_NAME, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and ta.finding_name_en like '%" + val + "%' ");
						} else {
							sb.append(" and ta.finding_name_in like '%" + val + "%' ");
						}
					} else if (StringUtils.equals(WHERE_AUDIT_TEMPLATE_NAME, col)) {
						sb.append(" and ta.mst_audit_id  = '" + val + "' ");
					}
				}
			}
		}

		return sb;
	}
}
