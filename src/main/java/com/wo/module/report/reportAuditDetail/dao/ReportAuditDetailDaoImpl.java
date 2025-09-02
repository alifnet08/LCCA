package com.wo.module.report.reportAuditDetail.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.report.reportAuditDetail.constant.ReportAuditDetailConstants;
import com.wo.module.report.reportAuditDetail.model.ReportAuditDetail;
import com.wo.module.report.reportGen.model.ReportGen;

@Repository("reportAuditDetailDao")
public class ReportAuditDetailDaoImpl extends GenericDAOHibernate<ReportGen, Long>
		implements ReportAuditDetailDao, ReportAuditDetailConstants {

	@SuppressWarnings("rawtypes")
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
//		locale = facesUtil.retrieveDefaultLocale();
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString() != null ? searchVal.getSearchValueAsString() : "";
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(WHERE_AUDITOR, col)) {
						sb.append(" and ad.auditor = '" + val + "' ");
					} else if (StringUtils.equals(WHERE_TARGET_DATE_FROM, col)) {
						sb.append(
								" and TRUNC(COALESCE(ad.reschedule_target_3, ad.reschedule_target_2, ad.reschedule_target_1, ad.target_date)) >= TO_DATE('"
										+ val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_TARGET_DATE_TO, col)) {
						sb.append(
								" and TRUNC(COALESCE(ad.reschedule_target_3, ad.reschedule_target_2, ad.reschedule_target_1, ad.target_date)) <= TO_DATE('"
										+ val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_AUDIT_DATE_FROM, col)) {
						sb.append(" and TRUNC(ad.audit_date_from) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_AUDIT_DATE_TO, col)) {
						sb.append(" and TRUNC(ad.audit_date_to) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_DIVISION_ID, col)) {
						sb.append(" and (ad.division_id = " + Long.parseLong(val) + " or ad.division_id is null) ");
					} else if (StringUtils.equals(WHERE_STATUS_FOLLOWUP, col)) {
						sb.append(" and ad.followup_status = '" + val + "' ");
					} else if (StringUtils.equals(WHERE_STATUS_VERIFICATION, col)) {
						sb.append(" and ad.compliance_status = '" + val + "' ");
//					} else if (StringUtils.equals(WHERE_FINDING_NAME, col)) {
//						if (locale != null && locale.equals(locale.ENGLISH)) {
//							sb.append(" and ad.finding_name_en like '" + val + "' ");
//						} else {
//							sb.append(" and ad.finding_name_in like '" + val + "' ");
//						}
					} else if (StringUtils.equals(WHERE_MST_AUDIT_ID, col)) {
						sb.append(" and ad.mst_audit_id = " + Long.parseLong(val));
					}
				}
			}
		}

		return sb;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportAuditDetail> getReportAuditDetailByData(List<? extends SearchObject> searchCriteria,
			String findingNameEn, String findingNameIn) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ad.type_audit_in " + "		,ad.type_audit_en " + "		,ad.tanggapan_bank "
				+ "		,ad.komitmen_bank " + "		,ad.unit_kerja " + "		,ad.pic_1 " + "		,ad.pic_2 "
				+ "		,ad.pic_3 " + "		,ad.target_date " + "		,ad.reschedule_target_1 "
				+ "		,ad.reschedule_target_2 " + "		,ad.reschedule_target_3 "
//				+ "		,ad.status_in "
//				+ "		,ad.status_en "
				+ "		,ad.attach_dokumen_tindak_lanjut " + "		,ad.keterangan "
				+ "		,ad.attachment_surat_ojk_bi " + "		,ad.sla " + "		,ad.auditor "
				+ "		,ad.audit_pic_followup_id " + "		,TO_CHAR(ad.target_date, 'dd-Mon-yyyy') target_date_str "
				+ "		,TO_CHAR(ad.reschedule_target_1, 'dd-Mon-yyyy') reschedule_target_1_str "
				+ "		,TO_CHAR(ad.reschedule_target_2, 'dd-Mon-yyyy') reschedule_target_2_str "
				+ "		,TO_CHAR(ad.reschedule_target_3, 'dd-Mon-yyyy') reschedule_target_3_str "
				+ "		,ad.total_done " + "		,ad.total_not_done " + "		,ad.max_audit_findings_column "
				+ "		,ad.audit_date_from " + "		,ad.audit_date_to "
				+ "		,TO_CHAR(ad.audit_date_from, 'dd-Mon-yyyy') audit_date_from_str "
				+ "		,TO_CHAR(ad.audit_date_to, 'dd-Mon-yyyy') audit_date_to_str " + "		,ad.status_followup_in "
				+ "		,ad.status_followup_en " + "		,ad.status_verifikasi_in "
				+ "		,ad.status_verifikasi_en " + "		,ad.finding_name_in " + "		,ad.finding_name_en "
				+ "		,ad.audit_template_name_in " + "		,ad.audit_template_name_en "
				+ "		,ad.mst_audit_id " + "	from wo_v_audit_detail ad " + "	where 1=1 ");

		sb = getQueryWhereString(sb, searchCriteria);

		if (findingNameEn != null && !findingNameEn.isEmpty() && !findingNameEn.equals("")) {
			sb.append(" and ad.finding_name_en like '%" + findingNameEn + "%' ");
		}

		if (findingNameIn != null && !findingNameIn.isEmpty() && !findingNameIn.equals("")) {
			sb.append(" and ad.finding_name_in like '%" + findingNameIn + "%' ");
		}

		Query query = getSession().createSQLQuery(sb.toString());

		List resultList = query.getResultList();

		List<ReportAuditDetail> vo = new ArrayList<ReportAuditDetail>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ReportAuditDetail data = new ReportAuditDetail();

				data.setTypeAuditIn(obj[0] != null ? (String) obj[0] : null);
				data.setTypeAuditEn(obj[1] != null ? (String) obj[1] : null);
				data.setTanggapanBank(obj[2] != null ? (String) obj[2] : null);
				data.setKomitmenBank(obj[3] != null ? (String) obj[3] : null);
				data.setUnitKerja(obj[4] != null ? (String) obj[4] : null);
				data.setPic1(obj[5] != null ? (String) obj[5] : null);
				data.setPic2(obj[6] != null ? (String) obj[6] : null);
				data.setPic3(obj[7] != null ? (String) obj[7] : null);
				data.setTargetDate(obj[8] != null ? (Date) obj[8] : null);
				data.setRescheduleTarget1(obj[9] != null ? (Date) obj[9] : null);
				data.setRescheduleTarget2(obj[10] != null ? (Date) obj[10] : null);
				data.setRescheduleTarget3(obj[11] != null ? (Date) obj[11] : null);
//				data.setStatusIn(obj[12] != null ? (String) obj[12] : null);
//				data.setStatusEn(obj[13] != null ? (String) obj[13] : null);
				data.setAttachmentDokumenTindakLanjut(obj[12] != null ? (String) obj[12] : null);
				data.setKeterangan(obj[13] != null ? (String) obj[13] : null);
				data.setAttachmentSuratOjkBi(obj[14] != null ? (String) obj[14] : null);
				data.setSla(obj[15] != null ? (String) obj[15] : null);
				data.setAuditor(obj[16] != null ? (String) obj[16] : null);
				data.setAuditPicFollowupId(MathUtil.returnIdObjectToLong(obj[17]));
				data.setTargetDateStr(obj[18] != null ? (String) obj[18] : null);
				data.setRescheduleTarget1Str(obj[19] != null ? (String) obj[19] : null);
				data.setRescheduleTarget2Str(obj[20] != null ? (String) obj[20] : null);
				data.setRescheduleTarget3Str(obj[21] != null ? (String) obj[21] : null);
				data.setTotalDone(obj[22] != null ? (String) (obj[22]) : null);
				data.setTotalNotDone(obj[23] != null ? (String) (obj[23]) : null);
				data.setMaxAuditFindingsColumn(obj[24] != null ? MathUtil.returnIdObjectToInteger(obj[24]) : 0);
				data.setAuditDateFrom(obj[25] != null ? (Date) obj[25] : null);
				data.setAuditDateTo(obj[26] != null ? (Date) obj[26] : null);
				data.setAuditDateFromStr(obj[27] != null ? (String) obj[27] : null);
				data.setAuditDateToStr(obj[28] != null ? (String) obj[28] : null);
				data.setStatusFollowUpIn(obj[29] != null ? (String) obj[29] : null);
				data.setStatusFollowUpEn(obj[30] != null ? (String) obj[30] : null);
				data.setStatusVerifikasiIn(obj[31] != null ? (String) obj[31] : null);
				data.setStatusVerifikasiEn(obj[32] != null ? (String) obj[32] : null);
				data.setFindingNameIn(obj[33] != null ? (String) obj[33] : null);
				data.setFindingNameEn(obj[34] != null ? (String) obj[34] : null);
				data.setAuditTemplateNameIn(obj[35] != null ? (String) obj[35] : null);
				data.setAuditTemplateNameEn(obj[36] != null ? (String) obj[36] : null);
				data.setMstAuditId(obj[37] != null ? MathUtil.returnIdObjectToLong(obj[37]) : 0);

				vo.add(data);
			}
		}

		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportAuditDetailByObj(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ad.type_audit_in " + "		,ad.type_audit_en " + "		,ad.tanggapan_bank "
				+ "		,ad.komitmen_bank " + "		,ad.unit_kerja " + "		,ad.pic_1 " + "		,ad.pic_2 "
				+ "		,ad.pic_3 " + "		,ad.target_date " + "		,ad.reschedule_target_1 "
				+ "		,ad.reschedule_target_2 " + "		,ad.reschedule_target_3 "
//				+ "		,ad.status_in "
//				+ "		,ad.status_en "
				+ "		,ad.attachment_dokumen_tindak_lanjut " + "		,ad.keterangan "
				+ "		,ad.attachment_surat_ojk_bi " + "		,ad.sla " + "		,ad.auditor "
				+ "		,ad.audit_pic_followup_id " + "		,TO_CHAR(ad.target_date, 'dd-Mon-yyyy') target_date_str "
				+ "		,TO_CHAR(ad.reschedule_target_1, 'dd-Mon-yyyy') reschedule_target_1_str "
				+ "		,TO_CHAR(ad.reschedule_target_2, 'dd-Mon-yyyy') reschedule_target_2_str "
				+ "		,TO_CHAR(ad.reschedule_target_3, 'dd-Mon-yyyy') reschedule_target_3_str "
				+ "		,ad.total_done " + "		,ad.total_not_done " + "		,ad.max_audit_findings_column "
				+ "		,ad.audit_date_from " + "		,ad.audit_date_to " + "		,ad.status_followup_in "
				+ "		,ad.status_followup_en " + "		,ad.status_verifikasi_in "
				+ "		,ad.status_verifikasi_en "
				+ "		,TO_CHAR(ad.audit_date_from, 'dd-Mon-yyyy') audit_date_from_str "
				+ "		,TO_CHAR(ad.audit_date_to, 'dd-Mon-yyyy') audit_date_to_str " + "		,ad.status_followup_in "
				+ "		,ad.status_followup_en " + "		,ad.status_verifikasi_in "
				+ "		,ad.status_verifikasi_en " + "		,ad.finding_name_in " + "		,ad.finding_name_en "
				+ "		,ad.audit_template_name_in " + "		,ad.audit_template_name_en "
				+ "		,ad.mst_audit_id " + "	from wo_v_audit_detail ad " + "	where 1=1 ");

		sb = getQueryWhereString(sb, searchCriteria);

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
}
