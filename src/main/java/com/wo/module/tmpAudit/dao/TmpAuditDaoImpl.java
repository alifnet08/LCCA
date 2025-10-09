package com.wo.module.tmpAudit.dao;

import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.tmpAudit.constant.AuditConstant;
import com.wo.module.tmpAudit.model.TmpAudit;
import com.wo.module.tmpAudit.vo.AuditConfirmationVO;
import com.wo.module.tmpAudit.vo.TmpAuditVO;
import com.wo.module.tmpAuditApproval.vo.TmpAuditApprovalVO;
import com.wo.module.trcAudit.dao.TrcAuditPICFollowupDao;

@Repository("tmpAuditDao")
public class TmpAuditDaoImpl extends GenericDAOHibernate<TmpAudit, Long> implements TmpAuditDao, AuditConstant {

	@Autowired
	@Qualifier("trcAuditPICFollowupDao")
	private TrcAuditPICFollowupDao trcAuditPICFollowupDao;
	
	@Override
	public Boolean hasReachedMaximumReschedule(Long auditPicFollowupId) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT COUNT(1) ");
		sb.append(" FROM wo_tmp_audit_pc_fp_reschedule  ");
		sb.append(" where 1=1 ");
		sb.append(" and audit_pic_followup_id = :auditPicFollowupId ");
		Query query = getSession().createSQLQuery(sb.toString());

		query.setParameter("auditPicFollowupId", auditPicFollowupId);
		
		Number count = (Number) query.getSingleResult();
		if(count == null) {
			count = 0;
		}
		
		StringBuilder sb2 = new StringBuilder();
		sb2.append(" SELECT name_in FROM wo_mst_parameter_dtl where parameter_dtl_code = 'MAX_RESCHEDULE_DATE' ");
	
		Query query2 = getSession().createSQLQuery(sb2.toString());

		Long maxRescheduleDate = new Long(((String) query2.getSingleResult()) );
		
		if( count.longValue() >= maxRescheduleDate.longValue() ) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<TmpAuditVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		List<TmpAuditVO> voList = searchDataCriteria(searchCriteria, first, pageSize);

		return voList;
	}
	
	@SuppressWarnings("rawtypes")
	private List<TmpAuditVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first,
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
					"	ta.audit_topic_en," +
					"	CASE WHEN ( " + 
					"		SELECT count(1) " + 
					"       FROM wo_tmp_audit_pic_followup apf  " +
					"       INNER JOIN wo_tmp_adt_pc_fp_bank_commit apb ON apb.AUDIT_PIC_FP_BANK_COMMIT_ID = apf.AUDIT_PIC_FP_BANK_COMMIT_ID " + 
					"       INNER JOIN WO_TMP_AUDIT_CHECK_POINT acp ON acp.AUDIT_CHECK_POINT_ID  = apb.AUDIT_CHECK_POINT_ID  " + 
					"		WHERE acp.audit_id = ta.audit_id " + 
					"			AND apf.followup_date is not null " + 
					"	) = 0 THEN null else 'ada isi' " + 
					"	END as followup_status, " +
					"	ta.finding_name_in, " +
					"	ta.finding_name_en, " +
					"	ma.audit_template_name_in, " +
					"	ma.audit_template_name_en, " +
					"   u1.NIK || '-' ||u1.name pic1, u2.name pic2,u3.name pic3, TO_CHAR(f2.confirmation_date, 'dd-Mon-yyyy') confirmation_date, TO_CHAR(f2.followup_date, 'dd-Mon-yyyy')followup_date, " + 
					"				f2.followup_note, TO_CHAR(f2.compliance_date, 'dd-Mon-yyyy') compliance_date, " + 
					"				d.name_in compliance_statusIn,d.name_en compliance_statusEn, " + 
					"				f2.compliance_note, " + 
					"				u4.name followupBy,d2.name_in,d2.name_en, f2.audit_pic_followup_id, TO_CHAR(f2.target_date, 'dd-Mon-yyyy')target_date," + 
					"  f3.audit_pic_followup_id audit_pic_followup_id_rec, TO_CHAR(f3.confirmation_date, 'dd-Mon-yyyy') confirmation_date_rec, TO_CHAR(f3.followup_date, 'dd-Mon-yyyy')followup_date_rec, " + 
					"				f3.followup_note followup_note_rec, TO_CHAR(f3.compliance_date, 'dd-Mon-yyyy') compliance_date_rec, " + 
					"				d3.name_in compliance_statusInRec,d3.name_en compliance_statusEnRec, " + 
					"				f3.compliance_note compliance_note_rec, " + 
					"				u5.name followupByRec,d4.name_in name_in_rec,d4.name_en name_en_rec, TO_CHAR(f3.target_date, 'dd-Mon-yyyy')target_date_rec" + 
					" FROM wo_tmp_audit ta" +
					" LEFT JOIN wo_mst_audit ma ON ma.mst_audit_id = ta.mst_audit_id " +
					" LEFT JOIN wo_mst_parameter_dtl pd1 ON pd1.parameter_dtl_code = ta.auditor" + 
					" LEFT JOIN wo_mst_parameter_dtl pd2 ON pd2.parameter_dtl_code = ta.audit_object" + 
					" LEFT JOIN wo_mst_parameter_dtl pd3 ON pd3.parameter_dtl_code = ta.audit_category" + 
					" LEFT JOIN wo_mst_parameter_dtl pd4 ON pd4.parameter_dtl_code = ta.STATUS" + 
					" INNER JOIN WO_TMP_AUDIT_CHECK_POINT acp ON acp.AUDIT_ID  = ta.AUDIT_ID  " +
					" INNER JOIN wo_tmp_adt_pc_fp_bank_commit apb ON apb.AUDIT_CHECK_POINT_ID = acp.AUDIT_CHECK_POINT_ID "  +
					" LEFT JOIN wo_tmp_audit_pic_followup f ON f.AUDIT_PIC_FP_BANK_COMMIT_ID = apb.AUDIT_PIC_FP_BANK_COMMIT_ID" + 
					" LEFT JOIN wo_trc_audit_pic_followup f2 ON f2.audit_pic_followup_id = f.audit_pic_followup_id" + 
					" LEFT JOIN wo_trc_audit_pic_followup_rec f3 ON f3.audit_pic_followup_id = f2.audit_pic_followup_id" + 
					" LEFT JOIN wo_mst_parameter_dtl d ON f2.compliance_status = d.parameter_dtl_code" + 
					" LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1" + 
					" LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2" + 
					" LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3" + 
					" LEFT JOIN wo_mst_user u4 ON u4.user_id = f2.followup_by_id" + 
					" LEFT JOIN wo_mst_user u5 ON u5.user_id = f3.followup_by_id" + 
					" LEFT JOIN wo_mst_parameter_dtl d2 ON f2.followup_status = d2.parameter_dtl_code" + 
					" LEFT JOIN wo_mst_parameter_dtl d3 ON f3.compliance_status = d3.parameter_dtl_code" + 
					" LEFT JOIN wo_mst_parameter_dtl d4 ON f3.followup_status = d4.parameter_dtl_code" + 
					" WHERE 1=1 and ta.enabled_flag = 'Y' ");

		sb = getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY ta.audit_id DESC, f3.audit_pic_followup_rec_id asc ");
		
		
		Query result = getSession().createSQLQuery(sb.toString());
		result.setFirstResult(first);
		result.setMaxResults(pageSize);
		List resultList = result.getResultList();

		List<TmpAuditVO> vo = new ArrayList<TmpAuditVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				TmpAuditVO data = new TmpAuditVO();
				data.setAuditId(obj[0] != null ? ((Number) obj[0]).longValue() : null);
				data.setAuditorIn(obj[1] != null ? (String) obj[1] : null);
				data.setAuditorEn(obj[2] != null ? (String) obj[2] : null);
				data.setAuditObjectIn(obj[3] != null ? (String) obj[3] : null);
				data.setAuditObjectEn(obj[4] != null ? (String) obj[4] : null);
				data.setAuditCategoryIn(obj[5] != null ? (String) obj[5] : null);
				data.setAuditCategoryEn(obj[6] != null ? (String) obj[6] : null);
				data.setAuditDateFrom(obj[7] != null ? (String) obj[7] : null);
				data.setAuditDateTo(obj[8] != null ? (String) obj[8] : null);
				data.setScope(obj[9] != null ?  FacesUtil.convertClobToString((Clob)obj[9]) : null);
				data.setStatusCd(obj[10] != null ? (String) obj[10] : null);
				data.setStatusIn(obj[11] != null ? (String) obj[11] : null);
				data.setStatusEn(obj[12] != null ? (String) obj[12] : null);
				data.setAuditTopicIn(obj[13] != null ? (String) obj[13] : null);
				data.setAuditTopicEn(obj[14] != null ? (String) obj[14] : null);
				data.setFollowupStatusCd(obj[15] != null ? (String) obj[15] : null);
				data.setFindingNameIn(obj[16] != null ? (String) obj[16] : null);
				data.setFindingNameEn(obj[17] != null ? (String) obj[17] : null);
				data.setAuditTemplateNameIn(obj[18] != null ? (String) obj[18] : null);
				data.setAuditTemplateNameEn(obj[19] != null ? (String) obj[19] : null);
				//data.setStatusList(getDataConfirmStatusByAuditId(data.getAuditId()));
				data.setPic1(obj[20] != null ? (String) obj[20] : null);
				data.setPic2(obj[21] != null ? (String) obj[21] : null);
				data.setPic3(obj[22] != null ? (String) obj[22] : null);
				if(obj[35]!=null) {
					data.setConfirmationDate(obj[36] != null ? (String) obj[36] : null);
					data.setFollowupDate(obj[37] != null ? (String) obj[37] : null);
					data.setFollowupNote(obj[38] != null ? (String) obj[38] : null);
					data.setComplianceDate(obj[39] != null ? (String) obj[39] : null);
					data.setComplianceStatusIn(obj[40] != null ? (String) obj[40] : null);
					data.setComplianceStatusEn(obj[41] != null ? (String) obj[41] : null);
					data.setComplianceNote(obj[42] != null ? (String) obj[42] : null);
					data.setFollowupBy(obj[43] != null ? (String) obj[43] : null);
					data.setFollowupStatusIn(obj[44] != null ? (String) obj[44] : null);
					data.setFollowupStatusEn(obj[45] != null ? (String) obj[45] : null);
					data.setPicFollowupId(obj[35] != null ? MathUtil.returnIdObjectToLong(obj[35]) : null);
					data.setTargetDate(obj[46] != null ? (String)obj[46] : null);
				}else {
					data.setConfirmationDate(obj[23] != null ? (String) obj[23] : null);
					data.setFollowupDate(obj[24] != null ? (String) obj[24] : null);
					data.setFollowupNote(obj[25] != null ? (String) obj[25] : null);
					data.setComplianceDate(obj[26] != null ? (String) obj[26] : null);
					data.setComplianceStatusIn(obj[27] != null ? (String) obj[27] : null);
					data.setComplianceStatusEn(obj[28] != null ? (String) obj[28] : null);
					data.setComplianceNote(obj[29] != null ? (String) obj[29] : null);
					data.setFollowupBy(obj[30] != null ? (String) obj[30] : null);
					data.setFollowupStatusIn(obj[31] != null ? (String) obj[31] : null);
					data.setFollowupStatusEn(obj[32] != null ? (String) obj[32] : null);
					data.setPicFollowupId(obj[33] != null ? MathUtil.returnIdObjectToLong(obj[33]) : null);
					data.setTargetDate(obj[34] != null ? (String)obj[34] : null);
				}
				
				
				
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
				" FROM wo_tmp_audit ta" +
				" LEFT JOIN wo_mst_audit ma ON ma.mst_audit_id = ta.mst_audit_id " +
				" LEFT JOIN wo_mst_parameter_dtl pd1 ON pd1.parameter_dtl_code = ta.auditor" + 
				" LEFT JOIN wo_mst_parameter_dtl pd2 ON pd2.parameter_dtl_code = ta.audit_object" + 
				" LEFT JOIN wo_mst_parameter_dtl pd3 ON pd3.parameter_dtl_code = ta.audit_category" + 
				" LEFT JOIN wo_mst_parameter_dtl pd4 ON pd4.parameter_dtl_code = ta.STATUS" + 
				" INNER JOIN WO_TMP_AUDIT_CHECK_POINT acp ON acp.AUDIT_ID  = ta.AUDIT_ID  " +
				" INNER JOIN wo_tmp_adt_pc_fp_bank_commit apb ON apb.AUDIT_CHECK_POINT_ID = acp.AUDIT_CHECK_POINT_ID "  +
				" LEFT JOIN wo_tmp_audit_pic_followup f ON f.AUDIT_PIC_FP_BANK_COMMIT_ID = apb.AUDIT_PIC_FP_BANK_COMMIT_ID" + 
				" LEFT JOIN wo_trc_audit_pic_followup f2 ON f2.audit_pic_followup_id = f.audit_pic_followup_id" + 
				" LEFT JOIN wo_trc_audit_pic_followup_rec f3 ON f3.audit_pic_followup_id = f2.audit_pic_followup_id" + 
				" LEFT JOIN wo_mst_parameter_dtl d ON f2.compliance_status = d.parameter_dtl_code" + 
				" LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1" + 
				" LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2" + 
				" LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3" + 
				" LEFT JOIN wo_mst_user u4 ON u4.user_id = f2.followup_by_id" + 
				" LEFT JOIN wo_mst_user u5 ON u5.user_id = f3.followup_by_id" + 
				" LEFT JOIN wo_mst_parameter_dtl d2 ON f2.followup_status = d2.parameter_dtl_code" + 
				" LEFT JOIN wo_mst_parameter_dtl d3 ON f3.compliance_status = d3.parameter_dtl_code" + 
				" LEFT JOIN wo_mst_parameter_dtl d4 ON f3.followup_status = d4.parameter_dtl_code" + 
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
								+ "       FROM wo_tmp_audit_pic_followup apf  " 
								+ "       INNER JOIN wo_tmp_adt_pc_fp_bank_commit apb ON apb.AUDIT_PIC_FP_BANK_COMMIT_ID = apf.AUD_PC_FP_BANK_CMITMT_ID "  
								+ "       INNER JOIN WO_TMP_AUDIT_CHECK_POINT acp ON acp.AUDIT_CHECK_POINT_ID  = apb.AUDIT_CHECK_POINT_ID  " 
								+ "       WHERE    acp.audit_id = ta.audit_id "
								+ "                AND TRUNC(apf.target_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ) ");
						
					} else if (StringUtils.equals(WHERE_AUDIT_TARGET_TO, col)) {						
						sb.append(" and EXISTS( SELECT 1 FROM"
								+ "       FROM wo_tmp_audit_pic_followup apf  " 
								+ "       INNER JOIN wo_tmp_adt_pc_fp_bank_commit apb ON apb.AUDIT_PIC_FP_BANK_COMMIT_ID = apf.AUD_PC_FP_BANK_CMITMT_ID "  
								+ "       INNER JOIN WO_TMP_AUDIT_CHECK_POINT acp ON acp.AUDIT_CHECK_POINT_ID  = apb.AUDIT_CHECK_POINT_ID  " 
								+ "       WHERE    acp.audit_id = ta.audit_id"
								+ "                AND TRUNC(apf.target_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ) ");					
					} else if (StringUtils.equals(WHERE_AUDIT_DIVISION_ID, col)) {						
						sb.append(" and (EXISTS( SELECT 1 FROM"
								+ "            wo_tmp_audit_pic_followup apf "
								+ "            INNER JOIN wo_tmp_adt_pc_fp_bank_commit apb ON apb.AUDIT_PIC_FP_BANK_COMMIT_ID = apf.AUD_PC_FP_BANK_CMITMT_ID "  
								+ "            INNER JOIN WO_TMP_AUDIT_CHECK_POINT acp ON acp.AUDIT_CHECK_POINT_ID  = apb.AUDIT_CHECK_POINT_ID  " 
								+ "            WHERE acp.audit_id = ta.audit_id"
								+ "                AND apf.division_id = " + val + ") OR EXISTS( SELECT 1 FROM wo_tmp_audit_pic_followup apf  INNER JOIN wo_tmp_adt_pc_fp_bank_commit apb ON apb.AUDIT_PIC_FP_BANK_COMMIT_ID = apf.AUDIT_PIC_FP_BANK_COMMIT_ID INNER JOIN WO_TMP_AUDIT_CHECK_POINT acp ON acp.AUDIT_CHECK_POINT_ID  = apb.AUDIT_CHECK_POINT_ID WHERE acp.audit_id = ta.audit_id AND apf.division_id IS NULL )) ");
						
					} else if (StringUtils.equals(WHERE_AUDIT_FOLLOWUP_STATUS, col)) {
						if (val.equals("x")) {
							sb.append(" and (EXISTS( SELECT 1 FROM"
									+ "            wo_trc_audit_pic_followup apf "
									+ "       LEFT JOIN wo_trc_audit_pic_followup_rec apfr ON apfr.AUDIT_PIC_FOLLOWUP_ID = apf.AUDIT_PIC_FOLLOWUP_ID "  
									+ "       INNER JOIN wo_tmp_adt_pc_fp_bank_commit apb ON apb.AUDIT_PIC_FP_BANK_COMMIT_ID = apf.AUD_PC_FP_BANK_CMITMT_ID "  
									+ "       INNER JOIN WO_TMP_AUDIT_CHECK_POINT acp ON acp.AUDIT_CHECK_POINT_ID  = apb.AUDIT_CHECK_POINT_ID  " 
									+ "       WHERE  acp.audit_id = ta.audit_id"
									+ "                AND apf.followup_status is null)) ");
						} else {
							sb.append(" and EXISTS( SELECT 1 FROM"
									+ "            wo_trc_audit_pic_followup apf "
									+ "       LEFT JOIN wo_trc_audit_pic_followup_rec apfr ON apfr.AUDIT_PIC_FOLLOWUP_ID = apf.AUDIT_PIC_FOLLOWUP_ID "  
									+ "       INNER JOIN wo_tmp_adt_pc_fp_bank_commit apb ON apb.AUDIT_PIC_FP_BANK_COMMIT_ID = apf.AUD_PC_FP_BANK_CMITMT_ID "  
									+ "       INNER JOIN WO_TMP_AUDIT_CHECK_POINT acp ON acp.AUDIT_CHECK_POINT_ID  = apb.AUDIT_CHECK_POINT_ID  " 
									+ "           WHERE acp.audit_id = ta.audit_id"
									+ "                AND (apfr.followup_status = '" + val + "' OR apf.followup_status = '" + val + "')) ");
						}
						
					} else if (StringUtils.equals(WHERE_AUDIT_COMPLIANCE_STATUS, col)) {
						if (val.equals("x")) {
							sb.append(" and (EXISTS( SELECT 1 FROM"
									+ "            wo_trc_audit_pic_followup apf  "
									+ "       LEFT JOIN wo_trc_audit_pic_followup_rec apfr ON apfr.AUDIT_PIC_FOLLOWUP_ID = apf.AUDIT_PIC_FOLLOWUP_ID "  
									+ "       INNER JOIN wo_tmp_adt_pc_fp_bank_commit apb ON apb.AUDIT_PIC_FP_BANK_COMMIT_ID = apf.AUD_PC_FP_BANK_CMITMT_ID "  
									+ "       INNER JOIN WO_TMP_AUDIT_CHECK_POINT acp ON acp.AUDIT_CHECK_POINT_ID  = apb.AUDIT_CHECK_POINT_ID  " 
									+ "       WHERE acp.audit_id = ta.audit_id"
									+ "                AND apf.compliance_status is null)) ");
						} else {
							sb.append(" and EXISTS( SELECT 1 FROM"
									+ "            wo_trc_audit_pic_followup apf "
									+ "       LEFT JOIN wo_trc_audit_pic_followup_rec apfr ON apfr.AUDIT_PIC_FOLLOWUP_ID = apf.AUDIT_PIC_FOLLOWUP_ID "  
									+ "       INNER JOIN wo_tmp_adt_pc_fp_bank_commit apb ON apb.AUDIT_PIC_FP_BANK_COMMIT_ID = apf.AUD_PC_FP_BANK_CMITMT_ID "  
									+ "       INNER JOIN WO_TMP_AUDIT_CHECK_POINT acp ON acp.AUDIT_CHECK_POINT_ID  = apb.AUDIT_CHECK_POINT_ID  " 
									+ "       WHERE     acp.audit_id = ta.audit_id"
									+ "                AND (apfr.compliance_status = '" + val + "' OR apf.compliance_status = '" + val + "')) ");
						}
					} else if (StringUtils.equals(WHERE_AUDIT_FINDINGS_NAME, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and UPPER(ta.finding_name_en) like UPPER('%" + val + "%') ");
						} else {
							sb.append(" and UPPER(ta.finding_name_in) like UPPER('%" + val + "%') ");
						}
					} else if (StringUtils.equals(WHERE_AUDIT_TEMPLATE_NAME, col)) {
						sb.append(" and ta.mst_audit_id  = '" + val + "' ");
					}
				}
			}
		}

		return sb;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<TmpAuditVO> searchDataXLS(List<? extends SearchObject> searchCriteria) {
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
				"	ta.audit_topic_en," +
				"	CASE WHEN ( " + 
				"		SELECT count(1) " + 
				"       FROM wo_tmp_audit_pic_followup apf  " +
				"       INNER JOIN wo_tmp_adt_pc_fp_bank_commit apb ON apb.AUDIT_PIC_FP_BANK_COMMIT_ID = apf.AUDIT_PIC_FP_BANK_COMMIT_ID " + 
				"       INNER JOIN WO_TMP_AUDIT_CHECK_POINT acp ON acp.AUDIT_CHECK_POINT_ID  = apb.AUDIT_CHECK_POINT_ID  " + 
				"		WHERE acp.audit_id = ta.audit_id " + 
				"			AND apf.followup_date is not null " + 
				"	) = 0 THEN null else 'ada isi' " + 
				"	END as followup_status, " +
				"	ta.finding_name_in, " +
				"	ta.finding_name_en, " +
				"	ma.audit_template_name_in, " +
				"	ma.audit_template_name_en, " +
				"   u1.NIK || '-' ||u1.name pic1, u2.name pic2,u3.name pic3, TO_CHAR(f2.confirmation_date, 'dd-Mon-yyyy') confirmation_date, TO_CHAR(f2.followup_date, 'dd-Mon-yyyy')followup_date, " + 
				"				f2.followup_note, TO_CHAR(f2.compliance_date, 'dd-Mon-yyyy') compliance_date, " + 
				"				d.name_in compliance_statusIn,d.name_en compliance_statusEn, " + 
				"				f2.compliance_note, " + 
				"				u4.name followupBy,d2.name_in,d2.name_en, f2.audit_pic_followup_id, TO_CHAR(f2.target_date, 'dd-Mon-yyyy')target_date," + 
				"  f3.audit_pic_followup_id audit_pic_followup_id_rec, TO_CHAR(f3.confirmation_date, 'dd-Mon-yyyy') confirmation_date_rec, TO_CHAR(f3.followup_date, 'dd-Mon-yyyy')followup_date_rec, " + 
				"				f3.followup_note followup_note_rec, TO_CHAR(f3.compliance_date, 'dd-Mon-yyyy') compliance_date_rec, " + 
				"				d3.name_in compliance_statusInRec,d3.name_en compliance_statusEnRec, " + 
				"				f3.compliance_note compliance_note_rec, " + 
				"				u5.name followupByRec,d4.name_in name_in_rec,d4.name_en name_en_rec, TO_CHAR(f3.target_date, 'dd-Mon-yyyy')target_date_rec,audit_findings,bank_response,bank_commitment" + 
				" FROM wo_tmp_audit ta" +
				" LEFT JOIN wo_mst_audit ma ON ma.mst_audit_id = ta.mst_audit_id " +
				" LEFT JOIN wo_mst_parameter_dtl pd1 ON pd1.parameter_dtl_code = ta.auditor" + 
				" LEFT JOIN wo_mst_parameter_dtl pd2 ON pd2.parameter_dtl_code = ta.audit_object" + 
				" LEFT JOIN wo_mst_parameter_dtl pd3 ON pd3.parameter_dtl_code = ta.audit_category" + 
				" LEFT JOIN wo_mst_parameter_dtl pd4 ON pd4.parameter_dtl_code = ta.STATUS" + 
				" INNER JOIN WO_TMP_AUDIT_CHECK_POINT acp ON acp.AUDIT_ID  = ta.AUDIT_ID  " +
				" INNER JOIN wo_tmp_adt_pc_fp_bank_commit apb ON apb.AUDIT_CHECK_POINT_ID = acp.AUDIT_CHECK_POINT_ID "  +
				" INNER JOIN wo_tmp_audit_pic_followup f ON f.AUDIT_PIC_FP_BANK_COMMIT_ID = apb.AUDIT_PIC_FP_BANK_COMMIT_ID" + 
				" LEFT JOIN wo_trc_audit_pic_followup f2 ON f2.audit_pic_followup_id = f.audit_pic_followup_id" + 
				" LEFT JOIN wo_trc_audit_pic_followup_rec f3 ON f3.audit_pic_followup_id = f2.audit_pic_followup_id" + 
				" LEFT JOIN wo_mst_parameter_dtl d ON f2.compliance_status = d.parameter_dtl_code" + 
				" LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1" + 
				" LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2" + 
				" LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3" + 
				" LEFT JOIN wo_mst_user u4 ON u4.user_id = f2.followup_by_id" + 
				" LEFT JOIN wo_mst_user u5 ON u5.user_id = f3.followup_by_id" + 
				" LEFT JOIN wo_mst_parameter_dtl d2 ON f2.followup_status = d2.parameter_dtl_code" + 
				" LEFT JOIN wo_mst_parameter_dtl d3 ON f3.compliance_status = d3.parameter_dtl_code" + 
				" LEFT JOIN wo_mst_parameter_dtl d4 ON f3.followup_status = d4.parameter_dtl_code" + 
				" WHERE 1=1 and ta.enabled_flag = 'Y' ");

		sb = getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY ta.audit_id DESC, f3.audit_pic_followup_rec_id asc ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		List resultList = result.getResultList();

		List<TmpAuditVO> vo = new ArrayList<TmpAuditVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				TmpAuditVO data = new TmpAuditVO();
				data.setAuditId(obj[0] != null ? ((java.math.BigDecimal) obj[0]).longValue() : null);
				data.setAuditorIn(obj[1] != null ? (String) obj[1] : null);
				data.setAuditorEn(obj[2] != null ? (String) obj[2] : null);
				data.setAuditObjectIn(obj[3] != null ? (String) obj[3] : null);
				data.setAuditObjectEn(obj[4] != null ? (String) obj[4] : null);
				data.setAuditCategoryIn(obj[5] != null ? (String) obj[5] : null);
				data.setAuditCategoryEn(obj[6] != null ? (String) obj[6] : null);
				data.setAuditDateFrom(obj[7] != null ? (String) obj[7] : null);
				data.setAuditDateTo(obj[8] != null ? (String) obj[8] : null);
				data.setScope(obj[9] != null ?  FacesUtil.convertClobToString((Clob)obj[9]) : null);
				data.setStatusCd(obj[10] != null ? (String) obj[10] : null);
				data.setStatusIn(obj[11] != null ? (String) obj[11] : null);
				data.setStatusEn(obj[12] != null ? (String) obj[12] : null);
				data.setAuditTopicIn(obj[13] != null ? (String) obj[13] : null);
				data.setAuditTopicEn(obj[14] != null ? (String) obj[14] : null);
				data.setFollowupStatusCd(obj[15] != null ? (String) obj[15] : null);
				data.setFindingNameIn(obj[16] != null ? (String) obj[16] : null);
				data.setFindingNameEn(obj[17] != null ? (String) obj[17] : null);
				data.setAuditTemplateNameIn(obj[18] != null ? (String) obj[18] : null);
				data.setAuditTemplateNameEn(obj[19] != null ? (String) obj[19] : null);
				//data.setStatusList(getDataConfirmStatusByAuditId(data.getAuditId()));
				data.setPic1(obj[20] != null ? (String) obj[20] : null);
				data.setPic2(obj[21] != null ? (String) obj[21] : null);
				data.setPic3(obj[22] != null ? (String) obj[22] : null);
				if(obj[35]!=null) {
					data.setConfirmationDate(obj[36] != null ? (String) obj[36] : null);
					data.setFollowupDate(obj[37] != null ? (String) obj[37] : null);
					data.setFollowupNote(obj[38] != null ? (String) obj[38] : null);
					data.setComplianceDate(obj[39] != null ? (String) obj[39] : null);
					data.setComplianceStatusIn(obj[40] != null ? (String) obj[40] : null);
					data.setComplianceStatusEn(obj[41] != null ? (String) obj[41] : null);
					data.setComplianceNote(obj[42] != null ? (String) obj[42] : null);
					data.setFollowupBy(obj[43] != null ? (String) obj[43] : null);
					data.setFollowupStatusIn(obj[44] != null ? (String) obj[44] : null);
					data.setFollowupStatusEn(obj[45] != null ? (String) obj[45] : null);
					data.setPicFollowupId(obj[35] != null ? MathUtil.returnIdObjectToLong(obj[35]) : null);
					data.setTargetDate(obj[46] != null ? (String)obj[46] : null);
				}else {
					data.setConfirmationDate(obj[23] != null ? (String) obj[23] : null);
					data.setFollowupDate(obj[24] != null ? (String) obj[24] : null);
					data.setFollowupNote(obj[25] != null ? (String) obj[25] : null);
					data.setComplianceDate(obj[26] != null ? (String) obj[26] : null);
					data.setComplianceStatusIn(obj[27] != null ? (String) obj[27] : null);
					data.setComplianceStatusEn(obj[28] != null ? (String) obj[28] : null);
					data.setComplianceNote(obj[29] != null ? (String) obj[29] : null);
					data.setFollowupBy(obj[30] != null ? (String) obj[30] : null);
					data.setFollowupStatusIn(obj[31] != null ? (String) obj[31] : null);
					data.setFollowupStatusEn(obj[32] != null ? (String) obj[32] : null);
					data.setPicFollowupId(obj[33] != null ? MathUtil.returnIdObjectToLong(obj[33]) : null);
					data.setTargetDate(obj[34] != null ? (String)obj[34] : null);
				}
				
				data.setAuditFindings(obj[47] != null ? FacesUtil.convertClobToString((Clob)obj[47]): null);
				data.setBankResponse(obj[48] != null ? FacesUtil.convertClobToString((Clob)obj[48]) : null);
				data.setBankCommitment(obj[49] != null ? FacesUtil.convertClobToString((Clob)obj[49]) : null);
				vo.add(data);
			}
		}

		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<TmpAuditApprovalVO> getDataApprovalByAuditId(Long auditId) {
		StringBuilder sb = new StringBuilder();
		sb.append(
				" select u.name,d.name_in,d.name_en,TO_CHAR(approval_date, 'dd-Mon-yyyy')approval_date,approval_note  from wo_tmp_audit_approval a "
						+ "inner join wo_mst_user u on a.user_id =u.user_id "
						+ "inner join wo_mst_parameter_dtl d on a.approval_status = d.parameter_dtl_code "
						+ "where a.audit_id = :auditId ");

		sb.append(" ORDER BY audit_approval_id ASC ");

		Query result = getSession().createSQLQuery(sb.toString());
		result.setParameter("auditId", auditId);
		List resultList = result.getResultList();

		List<TmpAuditApprovalVO> vo = new ArrayList<TmpAuditApprovalVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				TmpAuditApprovalVO data = new TmpAuditApprovalVO();
				data.setApprovalBy(obj[0] != null ? (String) obj[0] : null);
				data.setApprovalStatusIn(obj[1] != null ? (String) obj[1] : null);
				data.setApprovalStatusEn(obj[2] != null ? (String) obj[2] : null);
				data.setApprovalDate(obj[3] != null ? (String) obj[3] : null);
				data.setApprovalNote(obj[4] != null ? (String) obj[4] : null);
				vo.add(data);
			}
		}

		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<AuditConfirmationVO> getDataConfirmStatusByAuditId(Long auditId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT u1.name pic1, u2.name pic2,u3.name pic3, TO_CHAR(f2.confirmation_date, 'dd-Mon-yyyy')confirmation_date, TO_CHAR(f2.followup_date, 'dd-Mon-yyyy')followup_date, " + 
				"				f2.followup_note, TO_CHAR(f2.compliance_date, 'dd-Mon-yyyy') compliance_date, " + 
				"				d.name_in compliance_statusIn,d.name_en compliance_statusEn, " + 
				"				f2.compliance_note, " + 
				"				u4.name followupBy,d2.name_in,d2.name_en, f2.audit_pic_followup_id, TO_CHAR(f.target_date, 'dd-Mon-yyyy')target_date" + 
				" FROM wo_tmp_audit_pic_followup f" + 
				" LEFT JOIN wo_tmp_adt_pc_fp_bank_commit apb ON apb.AUDIT_PIC_FP_BANK_COMMIT_ID = f.AUDIT_PIC_FP_BANK_COMMIT_ID "  +
				" LEFT JOIN WO_TMP_AUDIT_CHECK_POINT acp ON acp.AUDIT_CHECK_POINT_ID  = apb.AUDIT_CHECK_POINT_ID  " +
				" LEFT JOIN wo_trc_audit_pic_followup f2 ON f2.audit_pic_followup_id = f.audit_pic_followup_id" + 
				" LEFT JOIN wo_mst_parameter_dtl d ON f2.compliance_status = d.parameter_dtl_code" + 
				" LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1" + 
				" LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2" + 
				" LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3" + 
				" LEFT JOIN wo_mst_user u4 ON u4.user_id = f2.followup_by_id" + 
				" LEFT JOIN wo_mst_parameter_dtl d2 ON f2.followup_status = d2.parameter_dtl_code" + 
				" WHERE acp.audit_id = :auditId");

		sb.append(" ORDER BY f.audit_pic_followup_id ASC ");

		Query result = getSession().createSQLQuery(sb.toString());
		result.setParameter("auditId", auditId);
		List resultList = result.getResultList();

		List<AuditConfirmationVO> vo = new ArrayList<AuditConfirmationVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				AuditConfirmationVO data = new AuditConfirmationVO();
				data.setPic1(obj[0] != null ? (String) obj[0] : null);
				data.setPic2(obj[1] != null ? (String) obj[1] : null);
				data.setPic3(obj[2] != null ? (String) obj[2] : null);
				data.setConfirmationDate(obj[3] != null ? (String) obj[3] : null);
				data.setFollowupDate(obj[4] != null ? (String) obj[4] : null);
				data.setFollowupNote(obj[5] != null ? (String) obj[5] : null);
				data.setComplianceDate(obj[6] != null ? (String) obj[6] : null);
				data.setComplianceStatusIn(obj[7] != null ? (String) obj[7] : null);
				data.setComplianceStatusEn(obj[8] != null ? (String) obj[8] : null);
				data.setComplianceNote(obj[9] != null ? (String) obj[9] : null);
				data.setFollowupBy(obj[10] != null ? (String) obj[10] : null);
				data.setFollowupStatusIn(obj[11] != null ? (String) obj[11] : null);
				data.setFollowupStatusEn(obj[12] != null ? (String) obj[12] : null);
				data.setPicFollowupId(obj[13] != null ? MathUtil.returnIdObjectToLong(obj[13]) : null);
				data.setTargetDate(obj[14] != null ? (String)obj[14] : null);
				try {
					data.setTrcAuditPicFollowupAttachments(trcAuditPICFollowupDao.getTrcAuditPICFollowupAttachmentsByAuditPicFollowupId(data.getPicFollowupId()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					data.setTrcAuditPicFollowupLetterAttachments(trcAuditPICFollowupDao.getTrcAuditPICFollowupLetterAttachmentsByAuditPicFollowupId(data.getPicFollowupId()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				vo.add(data);
			}
		}

		return vo;
	}

	public TrcAuditPICFollowupDao getTrcAuditPICFollowupDao() {
		return trcAuditPICFollowupDao;
	}

	public void setTrcAuditPICFollowupDao(TrcAuditPICFollowupDao trcAuditPICFollowupDao) {
		this.trcAuditPICFollowupDao = trcAuditPICFollowupDao;
	}
	
	@Override
	public Integer getTmpAuditByIdAndNameIn(Long id, String findingNameIn, Long mstAuditId) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT COUNT(1)"
				+ "		FROM wo_tmp_audit ");
		sb.append("		WHERE audit_id <> " + ((id == null)? 0l : id));
		sb.append("			AND finding_name_in = '" + findingNameIn + "' ");
		sb.append("			AND mst_audit_id = " + mstAuditId);
		sb.append("			AND enabled_flag = 'Y' ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		
		Number count = (Number) result.getSingleResult();
		if(count == null) {
			count = 0;
		}
		
		return (Integer) count.intValue();
	}
}
