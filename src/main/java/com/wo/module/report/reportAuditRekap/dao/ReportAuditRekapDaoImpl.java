package com.wo.module.report.reportAuditRekap.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportAuditDetail.constant.ReportAuditDetailConstants;
import com.wo.module.report.reportAuditRekap.model.ReportAuditRekap;
import com.wo.module.report.reportGen.model.ReportGen;

@Repository("reportAuditRekapDao")
public class ReportAuditRekapDaoImpl extends GenericDAOHibernate<ReportGen, Long> implements ReportAuditDetailConstants, ReportAuditRekapDao{

	@SuppressWarnings("rawtypes")
	private StringBuilder getQueryWhereString (StringBuilder sb, List<? extends SearchObject> searchCriteria) {
//		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString() != null ? searchVal.getSearchValueAsString() : "";
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(WHERE_AUDITOR, col)) {
						sb.append(" and a.auditor ='" + val + "' ");
					} else if (StringUtils.equals(WHERE_TARGET_DATE_FROM, col)) {
						sb.append(" and TRUNC(apf.target_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if(StringUtils.equals(WHERE_TARGET_DATE_TO, col)) {
						sb.append(" and TRUNC(apf.target_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_AUDIT_DATE_FROM, col)) {
						sb.append(" and TRUNC(a.audit_date_from) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_AUDIT_DATE_TO, col)) {
						sb.append(" and TRUNC(a.audit_date_to) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_DIVISION_ID, col)) {
						sb.append(" and apf.division_id = '" + Long.parseLong(val) + "' ");
					} else if (StringUtils.equals(WHERE_STATUS_FOLLOWUP, col)) {
						sb.append(" and apf.followup_status = '" + val + "' ");
					} else if (StringUtils.equals(WHERE_STATUS_VERIFICATION, col)) {
						sb.append(" and apf.compliance_status = '" + val + "' ");
					} else if (StringUtils.equals(WHERE_FINDING_NAME, col)) {
//						if (locale != null && locale.equals(locale.ENGLISH)) {
//						sb.append(" and a.finding_name_en like '" + val + "' ");
//					} else {
//						sb.append(" and a.finding_name_in like '" + val + "' ");
//					}
				} else if (StringUtils.equals(WHERE_MST_AUDIT_ID, col)) {
					sb.append(" and (a.mst_audit_id = " + Long.parseLong(val) + ") ");
				} 
				}
			}
		}
		
		return sb;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportAuditRekap> getReportAuditRekapByData(List<? extends SearchObject> searchCriteria, String findingNameEn, String findingNameIn) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select  atyp.name_in type_audit_in "
				+ "			,atyp.name_en type_audit_en "
				+ "			,COUNT(1) total_audit "
				+ "			,SUM(CASE WHEN a.follow_up = 'Y' THEN 1 ELSE 0 END) tindak_lanjut_y "
				+ "			,SUM(CASE WHEN a.follow_up IS NULL OR a.follow_up = 'N' THEN 1 ELSE 0 END) tindak_lanjut_n "
				+ "			,SUM(CASE WHEN a.follow_up = 'Y' AND (apf.compliance_status IS NULL OR apf.compliance_status != 'COMPLIANCE_CLOSE') THEN 1 ELSE 0 END) in_progress "
				+ "			,SUM(CASE WHEN a.follow_up = 'Y' AND apf.compliance_status = 'COMPLIANCE_CLOSE' THEN 1 ELSE 0 END) closed "
				+ "			,SUM(CASE WHEN a.follow_up = 'Y' AND apf.compliance_status = 'COMPLIANCE_CLOSE' AND apf.followup_date = apf.target_date THEN 1 ELSE 0 END) meet_sla "
				+ "			,SUM(CASE WHEN a.follow_up = 'Y' AND apf.compliance_status = 'COMPLIANCE_CLOSE' AND apf.followup_date < apf.target_date THEN 1 ELSE 0 END) before_sla "
				+ "			,SUM(CASE WHEN a.follow_up = 'Y' AND apf.compliance_status = 'COMPLIANCE_CLOSE' AND apf.followup_date > apf.target_date THEN 1 ELSE 0 END) over_sla "
				+ "	from wo_trc_audit a "
				+ "		LEFT JOIN wo_mst_audit ma ON ma.mst_audit_id = a.mst_audit_id "
				+ "		LEFT JOIN wo_mst_parameter_dtl atyp ON atyp.parameter_code = 'AUDITOR' AND atyp.parameter_dtl_code = a.auditor "
				+ "		LEFT JOIN wo_trc_audit_pic_followup apf ON apf.audit_id = a.audit_id "
				+ "	where 1=1 " );
		
		sb = getQueryWhereString(sb, searchCriteria);
		
		if (findingNameEn != null 
				&& !findingNameEn.isEmpty()
				&& !findingNameEn.equals("")) {
			sb.append(" and a.finding_name_en like '%" + findingNameEn + "%' ");
		}
		
		if (findingNameIn != null
				&& !findingNameIn.isEmpty()
				&& !findingNameIn.equals("")) {
			sb.append(" and a.finding_name_in like '%" + findingNameIn + "%' ");
		}
		
		sb.append("GROUP BY atyp.name_in, atyp.name_en ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<ReportAuditRekap> vo = new ArrayList<ReportAuditRekap>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ReportAuditRekap data = new ReportAuditRekap();
				
				data.setTypeAuditIn(obj[0] != null ? (String) obj[0] : null);
				data.setTypeAuditEn(obj[1] != null ? (String) obj[1] : null);
				data.setTotalAudit(obj[2] != null ? (((BigDecimal) obj[2]).toBigInteger()).intValue() : null);
				data.setTindakLanjutYes(obj[3] != null ?  (((BigDecimal) obj[3]).toBigInteger()).intValue() : null);
				data.setTindakLanjutNo(obj[4] != null ?  (((BigDecimal) obj[4]).toBigInteger()).intValue() : null);
				data.setInProgress(obj[5] != null ?  (((BigDecimal) obj[5]).toBigInteger()).intValue() : null);
				data.setClosed(obj[6] != null ?  (((BigDecimal) obj[6]).toBigInteger()).intValue() : null);
				data.setMeetSla(obj[7] != null ?  (((BigDecimal) obj[7]).toBigInteger()).intValue() : null);
				data.setBeforeSla(obj[8] != null ?  (((BigDecimal) obj[8]).toBigInteger()).intValue() : null);
				data.setOverSla(obj[9] != null ?  (((BigDecimal) obj[9]).toBigInteger()).intValue() : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportAuditRekapByObj(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select  atyp.name_in type_audit_in "
				+ "			,atyp.name_en type_audit_en "
				+ "			,COUNT(1) total_audit "
				+ "			,SUM(CASE WHEN a.follow_up = 'Y' THEN 1 ELSE 0 END) tindak_lanjut_y "
				+ "			,SUM(CASE WHEN a.follow_up IS NULL OR a.follow_up = 'N' THEN 1 ELSE 0 END) tindak_lanjut_n "
				+ "			,SUM(CASE WHEN a.follow_up = 'Y' AND (apf.compliance_status IS NULL OR apf.compliance_status != 'COMPLIANCE_CLOSE') THEN 1 ELSE 0 END) in_progress "
				+ "			,SUM(CASE WHEN a.follow_up = 'Y' AND apf.compliance_status = 'COMPLIANCE_CLOSE' THEN 1 ELSE 0 END) closed "
				+ "			,SUM(CASE WHEN a.follow_up = 'Y' AND apf.compliance_status = 'COMPLIANCE_CLOSE' AND apf.followup_date = apf.target_date THEN 1 ELSE 0 END) meet_sla "
				+ "			,SUM(CASE WHEN a.follow_up = 'Y' AND apf.compliance_status = 'COMPLIANCE_CLOSE' AND apf.followup_date < apf.target_date THEN 1 ELSE 0 END) before_sla "
				+ "			,SUM(CASE WHEN a.follow_up = 'Y' AND apf.compliance_status = 'COMPLIANCE_CLOSE' AND apf.followup_date > apf.target_date THEN 1 ELSE 0 END) over_sla "
				+ "	from wo_trc_audit a "
				+ "		LEFT JOIN wo_mst_audit ma ON ma.mst_audit_id = a.mst_audit_id "
				+ "		LEFT JOIN wo_mst_parameter_dtl atyp ON atyp.parameter_code = 'AUDITOR' AND atyp.parameter_dtl_code = a.auditor "
				+ "		LEFT JOIN wo_trc_audit_pic_followup apf ON apf.audit_id = a.audit_id " );
		
		sb = getQueryWhereString(sb, searchCriteria);
		
		sb.append("GROUP BY atyp.name_in, atyp.name_en ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<Object[]> vo = new ArrayList<Object[]>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				
				vo.add(obj);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportAuditRekap> getReportAuditRekapByTemplateName(List<? extends SearchObject> searchCriteria,
			String findingNameEn, String findingNameIn) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select  ma.audit_template_name_in "
				+ "			,ma.audit_template_name_en "
				+ "			,COUNT(1) total_audit "
				+ "			,SUM(CASE WHEN a.follow_up = 'Y' THEN 1 ELSE 0 END) tindak_lanjut_y "
				+ "			,SUM(CASE WHEN a.follow_up IS NULL OR a.follow_up = 'N' THEN 1 ELSE 0 END) tindak_lanjut_n "
				+ "			,SUM(CASE WHEN a.follow_up = 'Y' AND (apf.compliance_status IS NULL OR apf.compliance_status != 'COMPLIANCE_CLOSE') THEN 1 ELSE 0 END) in_progress "
				+ "			,SUM(CASE WHEN a.follow_up = 'Y' AND apf.compliance_status = 'COMPLIANCE_CLOSE' THEN 1 ELSE 0 END) closed "
				+ "			,SUM(CASE WHEN a.follow_up = 'Y' AND apf.compliance_status = 'COMPLIANCE_CLOSE' AND apf.followup_date = apf.target_date THEN 1 ELSE 0 END) meet_sla "
				+ "			,SUM(CASE WHEN a.follow_up = 'Y' AND apf.compliance_status = 'COMPLIANCE_CLOSE' AND apf.followup_date < apf.target_date THEN 1 ELSE 0 END) before_sla "
				+ "			,SUM(CASE WHEN a.follow_up = 'Y' AND apf.compliance_status = 'COMPLIANCE_CLOSE' AND apf.followup_date > apf.target_date THEN 1 ELSE 0 END) over_sla "
				+ "	from wo_trc_audit a "
				+ "		LEFT JOIN wo_mst_audit ma ON ma.mst_audit_id = a.mst_audit_id "
				+ "		LEFT JOIN wo_mst_parameter_dtl atyp ON atyp.parameter_code = 'AUDITOR' AND atyp.parameter_dtl_code = a.auditor "
				+ "		LEFT JOIN wo_trc_audit_pic_followup apf ON apf.audit_id = a.audit_id "
				+ "	where 1=1 " );
		
		sb = getQueryWhereString(sb, searchCriteria);
		
		if (findingNameEn != null 
				&& !findingNameEn.isEmpty()
				&& !findingNameEn.equals("")) {
			sb.append(" and a.finding_name_en like '%" + findingNameEn + "%' ");
		}
		
		if (findingNameIn != null
				&& !findingNameIn.isEmpty()
				&& !findingNameIn.equals("")) {
			sb.append(" and a.finding_name_in like '%" + findingNameIn + "%' ");
		}
		
		sb.append("GROUP BY ma.audit_template_name_in, ma.audit_template_name_en ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<ReportAuditRekap> vo = new ArrayList<ReportAuditRekap>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ReportAuditRekap data = new ReportAuditRekap();
				
				data.setAuditTemplateNameIn(obj[0] != null ? (String) obj[0] : null);
				data.setAuditTemplateNameEn(obj[1] != null ? (String) obj[1] : null);
				data.setTotalAudit(obj[2] != null ? (((BigDecimal) obj[2]).toBigInteger()).intValue() : null);
				data.setTindakLanjutYes(obj[3] != null ?  (((BigDecimal) obj[3]).toBigInteger()).intValue() : null);
				data.setTindakLanjutNo(obj[4] != null ?  (((BigDecimal) obj[4]).toBigInteger()).intValue() : null);
				data.setInProgress(obj[5] != null ?  (((BigDecimal) obj[5]).toBigInteger()).intValue() : null);
				data.setClosed(obj[6] != null ?  (((BigDecimal) obj[6]).toBigInteger()).intValue() : null);
				data.setMeetSla(obj[7] != null ?  (((BigDecimal) obj[7]).toBigInteger()).intValue() : null);
				data.setBeforeSla(obj[8] != null ?  (((BigDecimal) obj[8]).toBigInteger()).intValue() : null);
				data.setOverSla(obj[9] != null ?  (((BigDecimal) obj[9]).toBigInteger()).intValue() : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

}
