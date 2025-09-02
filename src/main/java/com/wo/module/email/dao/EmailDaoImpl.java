package com.wo.module.email.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.DateUtil;
import com.wo.module.common.util.MathUtil;
import com.wo.module.email.constant.EmailConstant;
import com.wo.module.email.vo.EmailVO;
import com.wo.module.trcAudit.model.TrcAudit;

@Repository("emailDao")
public class EmailDaoImpl extends GenericDAOHibernate<TrcAudit, Long> implements EmailDao, EmailConstant {

	@SuppressWarnings("rawtypes")
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				Object valObject = searchVal.getSearchValue();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(SEARCH_BY_EMAIL_TYPE, col)) {

						sb.append(" and email_type = '" + val + "' ");

					} else if (StringUtils.equals(SEARCH_BY_EMAIL_DATE_FROM, col)) {
						
						sb.append(" and TRUNC(email_date) >= TO_DATE('" +  DateUtil.dateToStringYYYYMMDD((Date)valObject, false) + "', 'yyyy-MM-dd') ");
						
					} else if (StringUtils.equals(SEARCH_BY_EMAIL_DATE_TO, col)) {
						
						sb.append(" and TRUNC(email_date) <= TO_DATE('" +  DateUtil.dateToStringYYYYMMDD((Date)valObject, false) + "', 'yyyy-MM-dd') ");
						
					} else if (StringUtils.equals(SEARCH_BY_EMAIL_LAST_SENT_DATE_FROM, col)) {
						
						sb.append(" and TRUNC(last_sent_date) >= TO_DATE('" +  DateUtil.dateToStringYYYYMMDD((Date)valObject, false) + "', 'yyyy-MM-dd') ");
						
					} else if (StringUtils.equals(SEARCH_BY_EMAIL_LAST_SENT_DATE_TO, col)) {
						
						sb.append(" and TRUNC(last_sent_date) <= TO_DATE('" +  DateUtil.dateToStringYYYYMMDD((Date)valObject, false) + "', 'yyyy-MM-dd') ");
						
					} else if (StringUtils.equals(SEARCH_BY_EMAIL_STATUS, col)) {

						sb.append(" and email_status = '" + val + "' ");

					}
				}
			}
		}
		return sb;
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
		sb.append(" select COUNT(1) FROM " + "(" + "SELECT 'AUDIT' AS email_type, apfe.email_date, apfe.email_status,  "
				+ "	apfe.email_subject, SUBSTRING(apfe.email_content, 0, 100) email_content,  " + "	apfe.resend_count, "
				+ "	COALESCE(apfe.last_update_date, apfe.creation_date) last_sent_date "
				+ "FROM wo_trc_audit_pic_followup_email apfe WHERE apfe.email_status IS NOT NULL " + "UNION ALL  "
				+ "SELECT 'COMPLIANCE' AS emailType, cpfe.email_date, cpfe.email_status,   "
				+ "	SUBSTRING(cpfe.email_subject, 0, 100) email_subject, " + "	cpfe.resend_count, "
				+ "	cpfe.email_content, COALESCE(cpfe.last_update_date, cpfe.creation_date) lastSentDate "
				+ "FROM wo_trc_cmplc_review_pic_fp_email cpfe WHERE cpfe.email_status IS NOT NULL "
				+ "UNION ALL  " + "SELECT 'CORRESPONDENCE' AS emailType, copfe.email_date, copfe.email_status,  "
				+ "	SUBSTRING(copfe.email_subject, 0, 100) email_subject, " + "	copfe.resend_count, "
				+ "	copfe.email_content, COALESCE(copfe.last_update_date, copfe.creation_date) lastSentDate "
				+ "FROM wo_trc_crpdc_pic_fp_email copfe WHERE copfe.email_status IS NOT NULL "
				+ "UNION ALL  " + "SELECT 'RMD' AS emailType, rfe.email_date, rfe.email_status, "
				+ "	SUBSTRING(rfe.email_subject, 0, 100) email_subject, " + "	rfe.resend_count, "
				+ "	rfe.email_content, COALESCE(rfe.last_update_date, rfe.creation_date) lastSentDate "
				+ "FROM wo_trc_rmd_pic_followup_email rfe WHERE rfe.email_status IS NOT NULL " + "UNION ALL  "
				+ "SELECT 'SOCIALIZATION' AS emailType, spfe.email_date, spfe.email_status,  "
				+ "	SUBSTRING(spfe.email_subject, 0, 100) email_subject, " + "	spfe.resend_count, "
				+ "	spfe.email_content, COALESCE(spfe.last_update_date, spfe.creation_date) lastSentDate "
				+ "FROM wo_trc_socialization_pic_fp_email spfe WHERE spfe.email_status IS NOT NULL " + ") cnt where 1=1 ");

		sb = getQueryWhereString(sb, searchCriteria);

		Query result = getSession().createSQLQuery(sb.toString());

		return (Number) result.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<EmailVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<EmailVO> voList = searchDataCriteria(searchCriteria, first, pageSize);

		return voList;
	}

	@SuppressWarnings({ "rawtypes" })
	private List<EmailVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {

		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT email_id, email_type, TO_CHAR(email_date, 'dd-Mon-yyyy') email_date, email_status, email_subject, email_content, resend_count " + 
				"	, TO_CHAR(last_sent_date, 'dd-Mon-yyyy %H:%i') last_sent_date" + 
				"	FROM " + 
				"	( " +
				"SELECT apfe.adt_pic_fp_email_id as email_id, " + 
				"		'AUDIT' AS email_type,  " + 
				"		apfe.email_date,  " + 
				"		apfe.email_status,  " + 
				"		apfe.email_subject,  " + 
				"		SUBSTRING(apfe.email_content, 1, 100) email_content, 	 " + 
				"		apfe.resend_count,  " + 
				"		COALESCE(apfe.last_update_date, apfe.creation_date) last_sent_date " + 
				"FROM wo_trc_audit_pic_followup_email apfe " + 
				"WHERE apfe.email_status IS NOT NULL  " + 
				"UNION ALL " + 
				"SELECT cpfe.compliance_review_pic_followup_email_id, " + 
				"		'COMPLIANCE' AS emailType,  " + 
				"		cpfe.email_date,  " + 
				"		cpfe.email_status,  " + 
				"		cpfe.email_subject, 	 " + 
				"		SUBSTRING(cpfe.email_content, 1, 100) email_content,  " + 
				"		cpfe.resend_count,  " + 
				"		COALESCE(cpfe.last_update_date, cpfe.creation_date) " + 
				"FROM wo_trc_cmplc_review_pic_fp_email cpfe " + 
				"WHERE cpfe.email_status IS NOT NULL UNION ALL " + 
				"SELECT copfe.crpdc_pic_fp_email_id, " + 
				"		'CORRESPONDENCE' AS emailType,  " + 
				"		copfe.email_date,  " + 
				"		copfe.email_status,  " + 
				"		copfe.email_subject, " + 
				"		SUBSTRING(copfe.email_content, 1, 100) email_content,  " + 
				"		copfe.resend_count,  " + 
				"		COALESCE(copfe.last_update_date, copfe.creation_date) " + 
				"FROM wo_trc_crpdc_pic_fp_email copfe " + 
				"WHERE copfe.email_status IS NOT NULL UNION ALL " + 
				"SELECT rfe.rmd_pic_followup_email_id,  " + 
				"		'RMD' AS emailType,  " + 
				"		rfe.email_date,  " + 
				"		rfe.email_status,  " + 
				"		rfe.email_subject, " + 
				"		SUBSTRING(rfe.email_content, 1, 100) email_content, 	 " + 
				"		rfe.resend_count,  " + 
				"		COALESCE(rfe.last_update_date, rfe.creation_date) " + 
				"FROM wo_trc_rmd_pic_followup_email rfe " + 
				"WHERE rfe.email_status IS NOT NULL UNION ALL " + 
				"SELECT spfe.socialization_pic_followup_email_id,  " + 
				"		'SOCIALIZATION' AS emailType,  " + 
				"		spfe.email_date,  " + 
				"		spfe.email_status,  " + 
				"		spfe.email_subject, " + 
				"		SUBSTRING(spfe.email_content, 1, 100) email_content, 	 " + 
				"		spfe.resend_count,  " + 
				"		COALESCE(spfe.last_update_date, spfe.creation_date) " + 
				"FROM wo_trc_socialization_pic_fp_email spfe " + 
				"WHERE spfe.email_status IS NOT NULL " +
				" ) frm where 1=1 ");

		sb = getQueryWhereString(sb, searchCriteria);
		

		Query result = getSession().createSQLQuery(sb.toString());
		result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
		List resultList = result.getResultList();

		List<EmailVO> vo = new ArrayList<EmailVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				EmailVO data = new EmailVO();
				data.setEmailId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setEmailType(obj[1] != null ? (String) obj[1] : null);
				data.setEmailDate(obj[2] != null ? (String) obj[2] : null);
				data.setEmailStatus(obj[3] != null ? (String) obj[3] : null);
				data.setEmailSubject(obj[4] != null ? (String) obj[4] : null);
				data.setEmailContent(obj[5] != null ? (String) obj[5] : null);
				data.setResendCount(obj[6] != null ? (int) obj[6] : 0);
				data.setLastSentDate(obj[7] != null ? (String) obj[7] : null);
 
				vo.add(data);
			}
		}

		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		return vo;
	}

}
