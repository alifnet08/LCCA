package com.wo.module.report.reportRegulationMonitoring.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportRegulationMonitoring.constant.ReportRegulationMonitoringConstant;
import com.wo.module.report.reportRegulationMonitoring.vo.ReportRegulationMonitoringDetailVo;
import com.wo.module.report.reportRegulationMonitoring.vo.ReportRegulationMonitoringRekapVo;

@Repository("reportRegulationMonitoringDao")
public class ReportRegulationMonitoringDaoImpl extends GenericDAOHibernate<ReportGen, Long>
	implements ReportRegulationMonitoringDao, Serializable{

	private static final long serialVersionUID = 8418172889257215357L;

	@SuppressWarnings("rawtypes")
	private void getQueryWhereStringDetail(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_CREATION_DATE_FROM, col)) {
						sb.append(" AND TRUNC (vrmd.tanggal_filter) >= TO_DATE(:creationDateFrom, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_CREATION_DATE_FROM, col)) {
						sb.append(" AND TRUNC (vrmd.tanggal_filter) <= TO_DATE(:creationDateTo, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_PROV_TYPE, col)) {
						sb.append(" AND vrmd.jenis_ketentuan = :jenisKentuan ");
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_DOC_TYPE, col)) {
						sb.append(" AND vrmd.document_type_id = :documentTypeId ");
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_TARGET_DATE_FROM, col)) {
						sb.append(" AND TRUNC (vrmd.target_date) >= TO_DATE(:targetDateFrom, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_TARGET_DATE_TO, col)) {
						sb.append(" AND TRUNC (vrmd.target_date) <= TO_DATE(:targetDateTo, 'yyyy-MM-dd') ");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void getQueryWhereStringDetail2(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_CREATION_DATE_FROM, col)) {
						sb.append(" AND TRUNC (s.creation_date) >= TO_DATE(:creationDateFrom, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_CREATION_DATE_TO, col)) {
						sb.append(" AND TRUNC (s.creation_date) <= TO_DATE(:creationDateTo, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_PROV_TYPE, col)) {
						sb.append(" AND s.jenis_ketentuan = :jenisKentuan ");
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_DOC_TYPE, col)) {
						sb.append(" AND r.document_type_id = :documentTypeId ");
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_TARGET_DATE_FROM, col)) {
						sb.append(" AND TRUNC (spf.target_date) >= TO_DATE(:targetDateFrom, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_TARGET_DATE_TO, col)) {
						sb.append(" AND TRUNC (spf.target_date) <= TO_DATE(:targetDateTo, 'yyyy-MM-dd') ");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void getQuerySetStringDetail(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_CREATION_DATE_FROM, col)) {
						query.setParameter("creationDateFrom", val);
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_CREATION_DATE_TO, col)) {
						query.setParameter("creationDateTo", val);
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_PROV_TYPE, col)) {
						query.setParameter("jenisKentuan", val);
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_DOC_TYPE, col)) {
						query.setParameter("documentTypeId", val);
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_TARGET_DATE_FROM, col)) {
						query.setParameter("targetDateFrom", val);
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_TARGET_DATE_TO, col)) {
						query.setParameter("targetDateTo", val);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void getQueryWhereStringRekap(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_CREATION_DATE_FROM, col)) {
						sb.append(" 	AND trunc(rc.creation_date) >= TO_DATE(:creationDateFrom, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_CREATION_DATE_TO, col)) {
						sb.append(" 	AND trunc(rc.creation_date) <= TO_DATE(:creationDateTo, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_PROV_TYPE, col)) {
						sb.append(" 	AND rc.jenis_ketentuan = :jenisKentuan ");
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_DOC_TYPE, col)) {
						sb.append(" 	AND dt.document_type_id = :documentTypeId ");
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_TARGET_DATE_FROM, col)) {
						sb.append(" 	AND TRUNC (rmpf.target_date) >= TO_DATE(:targetDateFrom, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_TARGET_DATE_TO, col)) {
						sb.append(" 	AND TRUNC (rmpf.target_date) <= TO_DATE(:targetDateTo, 'yyyy-MM-dd') ");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void getQuerySetStringRekap(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_CREATION_DATE_FROM, col)) {
						query.setParameter("creationDateFrom", val);
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_CREATION_DATE_TO, col)) {
						query.setParameter("creationDateTo", val);
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_PROV_TYPE, col)) {
						query.setParameter("jenisKentuan", val);
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_DOC_TYPE, col)) {
						query.setParameter("documentTypeId", val);
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_TARGET_DATE_FROM, col)) {
						query.setParameter("targetDateFrom", val);
					}
					if (StringUtils.equals(ReportRegulationMonitoringConstant.WHERE_TARGET_DATE_TO, col)) {
						query.setParameter("targetDateTo", val);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportRegulationMonitoringDetailVo> getReportRegulationMonitoringDetailAsVo(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT reg.NAME_IN AS peraturan_in ");
		sb.append(" 	,reg.name_en AS peraturan_en ");
		sb.append(" 	,dt.document_type_in AS jenis_peraturan_in ");
		sb.append(" 	,dt.document_type_en AS jenis_peraturan_en ");
		sb.append(" 	,r.document_no AS no_peraturan ");
		sb.append(" 	,r.NAME_IN AS judul_peraturan_in ");
		sb.append(" 	,r.name_en AS judul_peraturan_en ");
		sb.append(" 	,dc.document_category_in AS kategori_dokumen_in ");
		sb.append(" 	,dc.document_category_en AS kategori_dokumen_en ");
		sb.append(" 	,s.follow_up AS tindak_lanjut ");
		sb.append(" 	,spf.notes AS tindak_lanjut_note ");
		sb.append(" 	,spf.target_date AS target_date ");
		sb.append(" 	,TO_CHAR(spf.target_date, 'DD-MON-YYYY') AS target_date_str ");
		sb.append(" 	,CASE WHEN s.follow_up = 'Y' THEN ");
		sb.append(" 			CASE WHEN (fus.parameter_dtl_code = 'PIC_DONE' AND (spf.compliance_status IS NULL OR spf.compliance_status <> 'COMPLIANCE_CLOSE')) ");
		sb.append("         		THEN (SELECT wo_mst_parameter_dtl.NAME_IN ");
		sb.append("         				FROM wo_mst_parameter_dtl ");
		sb.append("             		WHERE wo_mst_parameter_dtl.parameter_code = 'PIC_FOLLOWUP_STATUS' ");
		sb.append("             			AND wo_mst_parameter_dtl.parameter_dtl_code ='PIC_INPROGRESS') ");
		sb.append(" 	    	ELSE fus.NAME_IN ");
		sb.append(" 	    	END ");
		sb.append(" 		ELSE NULL ");
		sb.append(" 	 END AS status_in ");
		sb.append(" 	,CASE WHEN s.follow_up = 'Y' THEN ");
		sb.append(" 			CASE WHEN (fus.parameter_dtl_code = 'PIC_DONE' AND (spf.compliance_status IS NULL OR spf.compliance_status <> 'COMPLIANCE_CLOSE')) ");
		sb.append("         		THEN (SELECT wo_mst_parameter_dtl.name_en ");
		sb.append("         				FROM wo_mst_parameter_dtl ");
		sb.append("             		WHERE wo_mst_parameter_dtl.parameter_code = 'PIC_FOLLOWUP_STATUS' ");
		sb.append("             			AND wo_mst_parameter_dtl.parameter_dtl_code = 'PIC_INPROGRESS') ");
		sb.append("         		ELSE fus.name_en ");
		sb.append("         		END ");
		sb.append("     	ELSE NULL ");
		sb.append(" 	 END AS status_en ");
		sb.append(" 	,u1.name AS pic_1 ");
		sb.append(" 	,u2.name AS pic_2 ");
		sb.append(" 	,u3.name AS pic_3 ");
		sb.append(" 	,CASE WHEN u1.division_id = spf.division_id THEN u1.division_name ");
		sb.append(" 		ELSE NULL ");
		sb.append("     END AS divisi ");
		sb.append(" 	,(	SELECT rtrim (xmlagg (xmlelement(e,uc.NAME||', ')).extract ('//text()'), ', ') AS STR ");
		sb.append(" 		FROM (wo_trc_reg_monitor_pic_cmplc spc ");
		sb.append("     		LEFT JOIN wo_mst_user uc ON (uc.user_id = spc.user_id)) ");
		sb.append("     	WHERE spc.reg_monitoring_id = s.reg_monitoring_id ");
		sb.append("     ) AS pic_compliance ");		
		sb.append("     ,CAST ((SELECT RTRIM (XMLAGG (XMLELEMENT (e, spfa.attachment_file || ', ')).EXTRACT ('//text()'),', ') AS STR ");
		sb.append("               FROM wo_trc_reg_monitor_pic_fp_atch spfa ");
		sb.append("              WHERE spfa.reg_monitoring_pic_followup_id = spf.reg_monitoring_pic_followup_id) AS VARCHAR2 (2000)) ");
		sb.append("	         AS bukti_konfirmasi ");		
		sb.append(" 	,spf.confirmation_date AS tanggal_konfirmasi ");
		sb.append(" 	,TO_CHAR(spf.confirmation_date, 'DD-MON-YYYY') tanggal_konfirmasi_str ");
		sb.append(" 	,spf.followup_date AS tanggal_tindak_lanjut ");
		sb.append(" 	,TO_CHAR(spf.followup_date, 'DD-MON-YYYY') tanggal_tindak_lanjut_str ");
		sb.append(" 	,spf.followup_note AS keterangan ");
		sb.append(" 	,CASE WHEN spf.compliance_status = 'COMPLIANCE_CLOSE' THEN ");
		sb.append(" 			CASE WHEN spf.followup_date = spf.target_date THEN 'Meet SLA' ");
		sb.append("     			WHEN spf.followup_date > spf.target_date THEN 'Over SLA' ");
		sb.append("     			WHEN spf.followup_date < spf.target_date THEN 'Before SLA' ");
		sb.append("     		END ");
		sb.append("     	ELSE NULL ");
		sb.append(" 	 END AS sla ");
		sb.append("    ,s.reg_monitoring_id AS cnt ");
		sb.append("    ,s.creation_date AS tanggal_filter ");
		sb.append("    ,TO_CHAR(s.creation_date, 'DD-MON-YYYY') tanggal_filter_str ");
		sb.append("    ,s.jenis_ketentuan AS jenis_ketentuan ");
		sb.append(" 	,r.document_type_id AS document_type_id ");
		sb.append(" FROM wo_trc_reg_monitoring s ");
		sb.append(" 	JOIN wo_trc_reg_monitor_regulation sr ON (sr.reg_monitoring_id = s.reg_monitoring_id ");
		sb.append(" 		AND sr.primary_flag = 'Y') ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL reg ON (reg.parameter_code = 'JENIS_KETENTUAN' ");
		sb.append(" 		AND reg.parameter_dtl_code = s.jenis_ketentuan) ");
		sb.append(" 	LEFT JOIN wo_mst_regulation r ON (r.regulation_id = sr.regulation_id) ");
		sb.append(" 	LEFT JOIN wo_mst_document_type dt ON (dt.document_type_id = r.document_type_id) ");
		sb.append(" 	LEFT JOIN wo_mst_document_category dc ON (dc.document_category_id = r.document_category_id) ");
		sb.append(" 	LEFT JOIN wo_trc_reg_monitoring_pic_fp spf ON (spf.reg_monitoring_id = s.reg_monitoring_id) ");
		sb.append(" 	LEFT JOIN wo_mst_parameter_dtl fus ON (fus.parameter_code = 'PIC_FOLLOWUP_STATUS' ");
		sb.append(" 		AND fus.parameter_dtl_code = COALESCE(spf.followup_status, 'PIC_INPROGRESS')) ");
		sb.append(" 	LEFT JOIN wo_mst_user u1 ON (u1.user_id = spf.user_id_1) ");
		sb.append(" 	LEFT JOIN wo_mst_user u2 ON (u2.user_id = spf.user_id_2) ");
		sb.append(" 	LEFT JOIN wo_mst_user u3 ON (u3.user_id = spf.user_id_3) ");
		sb.append(" WHERE 1 = 1 ");
		
		this.getQueryWhereStringDetail2(sb, searchCriteria);
		sb.append(" ORDER BY s.reg_monitoring_id ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQuerySetStringDetail(query, searchCriteria);
		
		List result = query.getResultList();
		List<ReportRegulationMonitoringDetailVo> vo = new ArrayList<ReportRegulationMonitoringDetailVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				ReportRegulationMonitoringDetailVo data = new ReportRegulationMonitoringDetailVo();
				
				data.setPeraturanIn(obj[0] != null ? (String) obj[0] : null);
				data.setPeraturanEn(obj[1] != null ? (String) obj[1] : null);
				data.setJenisPeraturanIn(obj[2] != null ? (String) obj[2] : null);
				data.setJenisPeraturanEn(obj[3] != null ? (String) obj[3] : null);
				data.setNoPeraturan(obj[4] != null ? (String) obj[4] : null);
				data.setJudulPeraturanIn(obj[5] != null ? (String) obj[5] : null);
				data.setJudulPeraturanEn(obj[6] != null ? (String) obj[6] : null);
				data.setKategoriDokumenIn(obj[7] != null ? (String) obj[7] : null);
				data.setKategoriDokumenEn(obj[8] != null ? (String) obj[8] : null);
				data.setTindakLanjut(obj[9] != null ? (String) obj[9] : null);
				data.setTindakLanjutNote(obj[10] != null ? (String) obj[10] : null);
				data.setTargetDate(obj[11] != null ? (Date) obj[11] : null);
				data.setTargetDateStr(obj[12] != null ? (String) obj[12] : null);
				data.setStatusIn(obj[13] != null ? (String) obj[13] : null);
				data.setStatusEn(obj[14] != null ? (String) obj[14] : null);
				data.setPic1(obj[15] != null ? (String) obj[15] : null);
				data.setPic2(obj[16] != null ? (String) obj[16] : null);
				data.setPic3(obj[17] != null ? (String) obj[17] : null);
				data.setDivisi(obj[18] != null ? (String) obj[18] : null);
				data.setPicCompliance(obj[19] != null ? (String) obj[19] : null);
				data.setBuktiKonfirmasi(obj[20] != null ? (String) obj[20] : null);
				data.setTanggalKonfirmasi(obj[21] != null ? (Date) obj[21] : null);
				data.setTanggalKonfirmasiStr(obj[22] != null ? (String) obj[22] : null);
				data.setTanggalTindakLanjut(obj[23] != null ? (Date) obj[23] : null);
				data.setTanggalTindakLanjutStr(obj[24] != null ? (String) obj[24] : null);
				data.setKeterangan(obj[25] != null ? (String) obj[25] : null);
				data.setSLA(obj[26] != null ? (String) obj[26] : null);
				data.setCNT(obj[27] != null ? MathUtil.returnIdObjectToLong(obj[27]) : null);
				data.setTanggalFilter(obj[28] != null ? (Date) obj[28] : null);
				data.setTanggalFilterStr(obj[29] != null ? (String) obj[29] : null);
				data.setJenisKetentuan(obj[30] != null ? (String) obj[30] : null);
				data.setDocumenTypeId(obj[31] != null ? MathUtil.returnIdObjectToLong(obj[31]) : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportRegulationMonitoringDetailAsObj(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT count(1) ");
		sb.append(" FROM wo_trc_reg_monitoring s ");
		sb.append(" 	JOIN wo_trc_reg_monitor_regulation sr ON (sr.reg_monitoring_id = s.reg_monitoring_id ");
		sb.append(" 		AND sr.primary_flag = 'Y') ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL reg ON (reg.parameter_code = 'JENIS_KETENTUAN' ");
		sb.append(" 		AND reg.parameter_dtl_code = s.jenis_ketentuan) ");
		sb.append(" 	LEFT JOIN wo_mst_regulation r ON (r.regulation_id = sr.regulation_id) ");
		sb.append(" 	LEFT JOIN wo_mst_document_type dt ON (dt.document_type_id = r.document_type_id) ");
		sb.append(" 	LEFT JOIN wo_mst_document_category dc ON (dc.document_category_id = r.document_category_id) ");
		sb.append(" 	LEFT JOIN wo_trc_reg_monitoring_pic_fp spf ON (spf.reg_monitoring_id = s.reg_monitoring_id) ");
		sb.append(" 	LEFT JOIN wo_mst_parameter_dtl fus ON (fus.parameter_code = 'PIC_FOLLOWUP_STATUS' ");
		sb.append(" 		AND fus.parameter_dtl_code = COALESCE(spf.followup_status, 'PIC_INPROGRESS')) ");
		sb.append(" 	LEFT JOIN wo_mst_user u1 ON (u1.user_id = spf.user_id_1) ");
		sb.append(" 	LEFT JOIN wo_mst_user u2 ON (u2.user_id = spf.user_id_2) ");
		sb.append(" 	LEFT JOIN wo_mst_user u3 ON (u3.user_id = spf.user_id_3) ");
		sb.append(" WHERE 1 = 1 ");
		
		this.getQueryWhereStringDetail(sb, searchCriteria);
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQuerySetStringDetail(query, searchCriteria);
		
		List result = query.getResultList();
		
		List<Object[]> vo = new ArrayList<Object[]>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				
				vo.add(obj);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportRegulationMonitoringRekapVo> getReportRegulationMonitoringRekapByCatRegAsVo(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT dt.DOCUMENT_TYPE_IN AS JENIS_PERATURAN_IN ");
		sb.append("       ,dt.DOCUMENT_TYPE_EN AS JENIS_PERATURAN_EN ");
		sb.append("       ,dc.DOCUMENT_CATEGORY_IN AS KATEGORI_DOCUMENT_IN ");
		sb.append("       ,dc.DOCUMENT_CATEGORY_EN AS KATEGORI_DOCUMENT_EN ");
		sb.append("       ,COUNT(1) TOTAL_REG_MONITORING ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' THEN 1 ELSE 0 END) TINDAK_LANJUT_Y ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP IS NULL OR rc.FOLLOW_UP = 'N' THEN 1 ELSE 0 END) TINDAK_LANJUT_N ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' AND (rmpf.COMPLIANCE_STATUS IS NULL OR rmpf.COMPLIANCE_STATUS != 'COMPLIANCE_STATUS') THEN 1 ELSE 0 END) IN_PROGRESS ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' AND rmpf.COMPLIANCE_STATUS = 'COMPLIANCE_STATUS' THEN 1 ELSE 0 END) CLOSED ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' AND rmpf.COMPLIANCE_STATUS = 'COMPLIANCE_STATUS' AND rmpf.FOLLOWUP_DATE = rmpf.TARGET_DATE THEN 1 ELSE 0 END) MEET_SLA ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' AND rmpf.COMPLIANCE_STATUS = 'COMPLIANCE_STATUS' AND rmpf.FOLLOWUP_DATE < rmpf.TARGET_DATE THEN 1 ELSE 0 END) BEFORE_SLA ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' AND rmpf.COMPLIANCE_STATUS = 'COMPLIANCE_STATUS' AND rmpf.FOLLOWUP_DATE > rmpf.TARGET_DATE THEN 1 ELSE 0 END) AFTER_SLA ");
		sb.append(" FROM WO_TRC_REG_MONITORING rc ");
		sb.append("     JOIN WO_TRC_REG_MONITOR_REGULATION rmr ");
		sb.append("         ON (rmr.REG_MONITORING_ID = rc.REG_MONITORING_ID) ");
		sb.append("     LEFT JOIN WO_MST_REGULATION r ");
		sb.append("         ON r.REGULATION_ID = rmr.REGULATION_ID ");
		sb.append("     LEFT JOIN WO_MST_DOCUMENT_TYPE dt ");
		sb.append("         ON dt.DOCUMENT_TYPE_ID = r.DOCUMENT_TYPE_ID ");
		sb.append("     LEFT JOIN WO_MST_DOCUMENT_CATEGORY dc ");
		sb.append("         ON dc.DOCUMENT_CATEGORY_ID = r.DOCUMENT_CATEGORY_ID ");
		sb.append("     LEFT JOIN WO_TRC_REG_MONITORING_PIC_FP rmpf ");
		sb.append("         ON rmpf.REG_MONITORING_ID = rc.REG_MONITORING_ID ");
		sb.append(" WHERE 1 = 1 ");
		
		this.getQueryWhereStringRekap(sb, searchCriteria);
		
		sb.append(" GROUP BY  dt.DOCUMENT_TYPE_IN ");
		sb.append("          ,dt.DOCUMENT_TYPE_EN ");
		sb.append("          ,dc.DOCUMENT_CATEGORY_IN ");
		sb.append("          ,dc.DOCUMENT_CATEGORY_EN ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQuerySetStringRekap(query, searchCriteria);
		
		List result = query.getResultList();
		List<ReportRegulationMonitoringRekapVo> vo = new ArrayList<ReportRegulationMonitoringRekapVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				ReportRegulationMonitoringRekapVo data = new ReportRegulationMonitoringRekapVo();
				
				data.setJenisPeraturanIn(obj[0] != null ? (String) obj[0] : null);
				data.setJenisPeraturanEn(obj[1] != null ? (String) obj[1] : null);
				data.setKategoriDokumenIn(obj[2] != null ? (String) obj[2] : null);
				data.setKategoriDokumenEn(obj[3] != null ? (String) obj[3] : null);
				data.setTotalRegulationMonitoring(obj[4] != null ? ((BigDecimal) obj[4]).intValue() : null);
				data.setTindakLanjutYes(obj[5] != null ? ((BigDecimal) obj[5]).intValue() : null);
				data.setTindakLanjutNo(obj[6] != null ? ((BigDecimal) obj[6]).intValue() : null);
				data.setStatusTindakLanjutInProgress(obj[7] != null ? ((BigDecimal) obj[7]).intValue() : null);
				data.setStatusTindakLanjutClosed(obj[8] != null ? ((BigDecimal) obj[8]).intValue() : null);
				data.setMeetSla(obj[9] != null ? ((BigDecimal) obj[9]).intValue() : null);
				data.setBeforeSla(obj[10] != null ? ((BigDecimal) obj[10]).intValue() : null);
				data.setOverSla(obj[11] != null ? ((BigDecimal) obj[11]).intValue() : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportRegulationMonitoringRekapByCatRegAsObj(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT dt.DOCUMENT_TYPE_IN AS JENIS_PERATURAN_IN ");
		sb.append("       ,dt.DOCUMENT_TYPE_EN AS JENIS_PERATURAN_EN ");
		sb.append("       ,dc.DOCUMENT_CATEGORY_IN AS KATEGORI_DOCUMENT_IN ");
		sb.append("       ,dc.DOCUMENT_CATEGORY_EN AS KATEGORI_DOCUMENT_EN ");
		sb.append("       ,COUNT(1) TOTAL_REG_MONITORING ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' THEN 1 ELSE 0 END) TINDAK_LANJUT_Y ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP IS NULL OR rc.FOLLOW_UP = 'N' THEN 1 ELSE 0 END) TINDAK_LANJUT_N ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' AND (rmpf.COMPLIANCE_STATUS IS NULL OR rmpf.COMPLIANCE_STATUS != 'COMPLIANCE_STATUS') THEN 1 ELSE 0 END) IN_PROGRESS ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' AND rmpf.COMPLIANCE_STATUS = 'COMPLIANCE_STATUS' THEN 1 ELSE 0 END) CLOSED ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' AND rmpf.COMPLIANCE_STATUS = 'COMPLIANCE_STATUS' AND rmpf.FOLLOWUP_DATE = rmpf.TARGET_DATE THEN 1 ELSE 0 END) MEET_SLA ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' AND rmpf.COMPLIANCE_STATUS = 'COMPLIANCE_STATUS' AND rmpf.FOLLOWUP_DATE < rmpf.TARGET_DATE THEN 1 ELSE 0 END) BEFORE_SLA ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' AND rmpf.COMPLIANCE_STATUS = 'COMPLIANCE_STATUS' AND rmpf.FOLLOWUP_DATE > rmpf.TARGET_DATE THEN 1 ELSE 0 END) AFTER_SLA ");
		sb.append(" FROM WO_TRC_REG_MONITORING rc ");
		sb.append("     JOIN WO_TRC_REG_MONITOR_REGULATION rmr ");
		sb.append("         ON (rmr.REG_MONITORING_ID = rc.REG_MONITORING_ID) ");
		sb.append("     LEFT JOIN WO_MST_REGULATION r ");
		sb.append("         ON r.REGULATION_ID = rmr.REGULATION_ID ");
		sb.append("     LEFT JOIN WO_MST_DOCUMENT_TYPE dt ");
		sb.append("         ON dt.DOCUMENT_TYPE_ID = r.DOCUMENT_TYPE_ID ");
		sb.append("     LEFT JOIN WO_MST_DOCUMENT_CATEGORY dc ");
		sb.append("         ON dc.DOCUMENT_CATEGORY_ID = r.DOCUMENT_CATEGORY_ID ");
		sb.append("     LEFT JOIN WO_TRC_REG_MONITORING_PIC_FP rmpf ");
		sb.append("         ON rmpf.REG_MONITORING_ID = rc.REG_MONITORING_ID ");
		sb.append(" WHERE 1 = 1 ");
		
		this.getQueryWhereStringRekap(sb, searchCriteria);
		
		sb.append(" GROUP BY  dt.DOCUMENT_TYPE_IN ");
		sb.append("          ,dt.DOCUMENT_TYPE_EN ");
		sb.append("          ,dc.DOCUMENT_CATEGORY_IN ");
		sb.append("          ,dc.DOCUMENT_CATEGORY_EN ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQuerySetStringRekap(query, searchCriteria);
		
		List result = query.getResultList();
		List<Object[]> vo = new ArrayList<Object[]>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				
				vo.add(obj);
			}
		}
		
		return vo;
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportRegulationMonitoringRekapVo> getReportRegulationMonitoringRekapByTotRegMonAsVo(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT dt.DOCUMENT_TYPE_IN AS JENIS_PERATURAN_IN ");
		sb.append("       ,dt.DOCUMENT_TYPE_EN AS JENIS_PERATURAN_EN ");
		sb.append("       ,COUNT(1) TOTAL_REG_MONITORING ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' THEN 1 ELSE 0 END) TINDAK_LANJUT_Y ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP IS NULL OR rc.FOLLOW_UP = 'N' THEN 1 ELSE 0 END) TINDAK_LANJUT_N ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' AND (rmpf.COMPLIANCE_STATUS IS NULL OR rmpf.COMPLIANCE_STATUS != 'COMPLIANCE_STATUS') THEN 1 ELSE 0 END) IN_PROGRESS ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' AND rmpf.COMPLIANCE_STATUS = 'COMPLIANCE_STATUS' THEN 1 ELSE 0 END) CLOSED ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' AND rmpf.COMPLIANCE_STATUS = 'COMPLIANCE_STATUS' AND rmpf.FOLLOWUP_DATE = rmpf.TARGET_DATE THEN 1 ELSE 0 END) MEET_SLA ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' AND rmpf.COMPLIANCE_STATUS = 'COMPLIANCE_STATUS' AND rmpf.FOLLOWUP_DATE < rmpf.TARGET_DATE THEN 1 ELSE 0 END) BEFORE_SLA ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' AND rmpf.COMPLIANCE_STATUS = 'COMPLIANCE_STATUS' AND rmpf.FOLLOWUP_DATE > rmpf.TARGET_DATE THEN 1 ELSE 0 END) AFTER_SLA ");
		sb.append(" FROM WO_TRC_REG_MONITORING rc ");
		sb.append("     JOIN WO_TRC_REG_MONITOR_REGULATION rmr ");
		sb.append("         ON (rmr.REG_MONITORING_ID = rc.REG_MONITORING_ID) ");
		sb.append("     LEFT JOIN WO_MST_REGULATION r ");
		sb.append("         ON r.REGULATION_ID = rmr.REGULATION_ID ");
		sb.append("     LEFT JOIN WO_MST_DOCUMENT_TYPE dt ");
		sb.append("         ON dt.DOCUMENT_TYPE_ID = r.DOCUMENT_TYPE_ID ");
		sb.append("     LEFT JOIN WO_MST_DOCUMENT_CATEGORY dc ");
		sb.append("         ON dc.DOCUMENT_CATEGORY_ID = r.DOCUMENT_CATEGORY_ID ");
		sb.append("     LEFT JOIN WO_TRC_REG_MONITORING_PIC_FP rmpf ");
		sb.append("         ON rmpf.REG_MONITORING_ID = rc.REG_MONITORING_ID ");
		sb.append(" WHERE 1 = 1 ");
		
		this.getQueryWhereStringRekap(sb, searchCriteria);
		
		sb.append(" GROUP BY  dt.DOCUMENT_TYPE_IN ");
		sb.append("          ,dt.DOCUMENT_TYPE_EN ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQuerySetStringRekap(query, searchCriteria);
		
		List result = query.getResultList();
		List<ReportRegulationMonitoringRekapVo> vo = new ArrayList<ReportRegulationMonitoringRekapVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				ReportRegulationMonitoringRekapVo data = new ReportRegulationMonitoringRekapVo();
				
				data.setJenisPeraturanIn(obj[0] != null ? (String) obj[0] : null);
				data.setJenisPeraturanEn(obj[1] != null ? (String) obj[1] : null);
				data.setTotalRegulationMonitoring(obj[2] != null ? ((BigDecimal) obj[2]).intValue() : null);
				data.setTindakLanjutYes(obj[3] != null ? ((BigDecimal) obj[3]).intValue() : null);
				data.setTindakLanjutNo(obj[4] != null ? ((BigDecimal) obj[4]).intValue() : null);
				data.setStatusTindakLanjutInProgress(obj[5] != null ? ((BigDecimal) obj[5]).intValue() : null);
				data.setStatusTindakLanjutClosed(obj[6] != null ? ((BigDecimal) obj[6]).intValue() : null);
				data.setMeetSla(obj[7] != null ? ((BigDecimal) obj[7]).intValue() : null);
				data.setBeforeSla(obj[8] != null ? ((BigDecimal) obj[8]).intValue() : null);
				data.setOverSla(obj[9] != null ? ((BigDecimal) obj[9]).intValue() : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportRegulationMonitoringRekapTotRegMonAsObj(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT dt.DOCUMENT_TYPE_IN AS JENIS_PERATURAN_IN ");
		sb.append("       ,dt.DOCUMENT_TYPE_EN AS JENIS_PERATURAN_EN ");
		sb.append("       ,COUNT(1) TOTAL_REG_MONITORING ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' THEN 1 ELSE 0 END) TINDAK_LANJUT_Y ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP IS NULL OR rc.FOLLOW_UP = 'N' THEN 1 ELSE 0 END) TINDAK_LANJUT_N ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' AND (rmpf.COMPLIANCE_STATUS IS NULL OR rmpf.COMPLIANCE_STATUS != 'COMPLIANCE_STATUS') THEN 1 ELSE 0 END) IN_PROGRESS ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' AND rmpf.COMPLIANCE_STATUS = 'COMPLIANCE_STATUS' THEN 1 ELSE 0 END) CLOSED ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' AND rmpf.COMPLIANCE_STATUS = 'COMPLIANCE_STATUS' AND rmpf.FOLLOWUP_DATE = rmpf.TARGET_DATE THEN 1 ELSE 0 END) MEET_SLA ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' AND rmpf.COMPLIANCE_STATUS = 'COMPLIANCE_STATUS' AND rmpf.FOLLOWUP_DATE < rmpf.TARGET_DATE THEN 1 ELSE 0 END) BEFORE_SLA ");
		sb.append("       ,SUM (CASE WHEN rc.FOLLOW_UP = 'Y' AND rmpf.COMPLIANCE_STATUS = 'COMPLIANCE_STATUS' AND rmpf.FOLLOWUP_DATE > rmpf.TARGET_DATE THEN 1 ELSE 0 END) AFTER_SLA ");
		sb.append(" FROM WO_TRC_REG_MONITORING rc ");
		sb.append("     JOIN WO_TRC_REG_MONITOR_REGULATION rmr ");
		sb.append("         ON (rmr.REG_MONITORING_ID = rc.REG_MONITORING_ID) ");
		sb.append("     LEFT JOIN WO_MST_REGULATION r ");
		sb.append("         ON r.REGULATION_ID = rmr.REGULATION_ID ");
		sb.append("     LEFT JOIN WO_MST_DOCUMENT_TYPE dt ");
		sb.append("         ON dt.DOCUMENT_TYPE_ID = r.DOCUMENT_TYPE_ID ");
		sb.append("     LEFT JOIN WO_MST_DOCUMENT_CATEGORY dc ");
		sb.append("         ON dc.DOCUMENT_CATEGORY_ID = r.DOCUMENT_CATEGORY_ID ");
		sb.append("     LEFT JOIN WO_TRC_REG_MONITORING_PIC_FP rmpf ");
		sb.append("         ON rmpf.REG_MONITORING_ID = rc.REG_MONITORING_ID ");
		sb.append(" WHERE 1 = 1 ");
		
		this.getQueryWhereStringRekap(sb, searchCriteria);
		
		sb.append(" GROUP BY  dt.DOCUMENT_TYPE_IN ");
		sb.append("          ,dt.DOCUMENT_TYPE_EN ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQuerySetStringRekap(query, searchCriteria);
		
		List result = query.getResultList();
		List<Object[]> vo = new ArrayList<Object[]>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				
				vo.add(obj);
			}
		}
		
		return vo;
	}

}
