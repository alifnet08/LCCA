package com.wo.module.report.reportSocializationRekap.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportSocializationDetail.constant.ReportSocializationDetailConstant;
import com.wo.module.report.reportSocializationRekap.model.ReportSocializationRekap;

@Repository("reportSocializationRekapDao")
public class ReportSocializationRekapDaoImpl extends GenericDAOHibernate<ReportGen, Long>
	implements ReportSocializationRekapDao, ReportSocializationDetailConstant{

	@SuppressWarnings("rawtypes")
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		
		//Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if(searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString() != null ? searchVal.getSearchValueAsString() : "";
				
				if(!StringUtils.isBlank(val)) {
					if(StringUtils.equals(WHERE_CREATION_DATE_FROM, col)) {
						sb.append(" and TRUNC(s.creation_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if(StringUtils.equals(WHERE_CREATION_DATE_TO, col)) {
						sb.append(" and TRUNC(s.creation_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if(StringUtils.equals(WHERE_DOC_TYPE, col)) {
						sb.append(" and r.document_type_id = '" + val +"' ");
					} else if(StringUtils.equals(WHERE_PROV_TYPE, col)) {
						sb.append("	and s.jenis_ketentuan = '" + val + "' ");
					} else if(StringUtils.equals(WHERE_TARGET_DATE_FROM, col)) {
						sb.append(" and TRUNC(spf.target_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if(StringUtils.equals(WHERE_TARGET_DATE_TO, col)) {
						sb.append(" and TRUNC(spf.target_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					}
				}
			}
		}
		return sb;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportSocializationRekap> getReportSocializationRekapDataByKategoriDokumen(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT dt.document_type_in AS jenis_peraturan_in "
				+ "		,dt.document_type_en AS jenis_peraturan_en "
				+ "		,dc.document_category_in AS kategori_dokumen_in "
				+ "		,dc.document_category_en AS kategori_dokumen_en "
				+ "		,COUNT(1) total_sosialisasi "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' THEN 1 ELSE 0 END) tindak_lanjut_y "
				+ "		,SUM(CASE WHEN s.follow_up IS NULL OR s.follow_up = 'N' THEN 1 ELSE 0 END) tindak_lanjut_n "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' AND (spf.compliance_status IS NULL OR spf.compliance_status != 'COMPLIANCE_CLOSE') THEN 1 ELSE 0 END) in_progress "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' AND spf.compliance_status = 'COMPLIANCE_CLOSE' THEN 1 ELSE 0 END) closed "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' AND spf.compliance_status = 'COMPLIANCE_CLOSE' AND spf.followup_date = spf.target_date THEN 1 ELSE 0 END) meet_sla "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' AND spf.compliance_status = 'COMPLIANCE_CLOSE' AND spf.followup_date < spf.target_date THEN 1 ELSE 0 END) before_sla "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' AND spf.compliance_status = 'COMPLIANCE_CLOSE' AND spf.followup_date > spf.target_date THEN 1 ELSE 0 END) over_sla " 
				+ "	FROM wo_trc_socialization s " 
				+"		JOIN wo_trc_socialization_rgltn sr ON (sr.socialization_id = s.socialization_id) " 
				+"		LEFT JOIN wo_mst_regulation r ON (r.regulation_id = sr.regulation_id) " 
				+"		LEFT JOIN wo_mst_document_type dt ON (dt.document_type_id = r.document_type_id) " 
				+"		LEFT JOIN wo_mst_document_category dc ON (dc.document_category_id = r.document_category_id) " 
				+"		LEFT JOIN wo_trc_socialization_pic_fp spf ON (spf.socialization_id = s.socialization_id) "
				+ "	WHERE 1=1 " );
		
		sb = getQueryWhereString(sb, searchCriteria);
		
		sb.append(" GROUP BY dt.document_type_in "
				+"		,dt.document_type_en "
				+"		,dc.document_category_in "
				+"		,dc.document_category_en ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<ReportSocializationRekap> vo = new ArrayList<ReportSocializationRekap>();
		
		if(resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ReportSocializationRekap data = new ReportSocializationRekap();
				
				data.setJenisPeraturanIn(obj[0] != null ? (String) obj[0] : null);
				data.setJenisPeraturanEn(obj[1] != null ? (String) obj[1] : null);
				data.setKategoriDokumenIn(obj[2] != null ? (String) obj[2] : null);
				data.setKategoriDokumenEn(obj[3] != null ? (String) obj[3] : null);
				data.setTotalSosialisasi(obj[4] != null ? (((BigInteger) obj[4]).intValue()) : null);
				data.setTindakLanjutYes(obj[5] != null ? (((BigDecimal) obj[5]).toBigInteger()).intValue() : null);
				data.setTindakLanjutNo(obj[6] != null ? (((BigDecimal) obj[6]).toBigInteger()).intValue() : null);
				data.setStatusTindakLanjutInProgress(obj[7] != null ? (((BigDecimal) obj[7]).toBigInteger()).intValue() : null);
				data.setStatusTindakLanjutClosed(obj[8] != null ? (((BigDecimal) obj[8]).toBigInteger()).intValue() : null);
				data.setMeetSla(obj[9] != null ? (((BigDecimal) obj[9]).toBigInteger()).intValue() : null);
				data.setBeforeSla(obj[10] != null ? (((BigDecimal) obj[10]).toBigInteger()).intValue() : null);
				data.setOverSla(obj[11] != null ? (((BigDecimal) obj[11]).toBigInteger()).intValue() : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportSocializationRekapDataObjByKategoriDokumen(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT dt.document_type_in AS jenis_peraturan_in "
				+ "		,dt.document_type_en AS jenis_peraturan_en "
				+ "		,dc.document_category_in AS kategori_dokumen_in "
				+ "		,dc.document_category_en AS kategori_dokumen_en "
				+ "		,COUNT(1) total_sosialisasi "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' THEN 1 ELSE 0 END) tindak_lanjut_y "
				+ "		,SUM(CASE WHEN s.follow_up IS NULL OR s.follow_up = 'N' THEN 1 ELSE 0 END) tindak_lanjut_n "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' AND (spf.compliance_status IS NULL OR spf.compliance_status != 'COMPLIANCE_CLOSE') THEN 1 ELSE 0 END) in_progress "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' AND spf.compliance_status = 'COMPLIANCE_CLOSE' THEN 1 ELSE 0 END) closed "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' AND spf.compliance_status = 'COMPLIANCE_CLOSE' AND spf.followup_date = spf.target_date THEN 1 ELSE 0 END) meet_sla "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' AND spf.compliance_status = 'COMPLIANCE_CLOSE' AND spf.followup_date < spf.target_date THEN 1 ELSE 0 END) before_sla "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' AND spf.compliance_status = 'COMPLIANCE_CLOSE' AND spf.followup_date > spf.target_date THEN 1 ELSE 0 END) over_sla " 
				+ "	FROM wo_trc_socialization s " 
				+"		JOIN wo_trc_socialization_rgltn sr ON (sr.socialization_id = s.socialization_id) " 
				+"		LEFT JOIN wo_mst_regulation r ON (r.regulation_id = sr.regulation_id) " 
				+"		LEFT JOIN wo_mst_document_type dt ON (dt.document_type_id = r.document_type_id) " 
				+"		LEFT JOIN wo_mst_document_category dc ON (dc.document_category_id = r.document_category_id) " 
				+"		LEFT JOIN wo_trc_socialization_pic_fp spf ON (spf.socialization_id = s.socialization_id) " 
				+ "	WHERE 1=1 " );
		
		sb = getQueryWhereString(sb, searchCriteria);
		
		sb.append(" GROUP BY dt.document_type_in "
				+"		,dt.document_type_en "
				+"		,dc.document_category_in "
				+"		,dc.document_category_en ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<Object[]> vo = new ArrayList<Object[]>();
		if(resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				
				vo.add(obj);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportSocializationRekap> getReportSocializationRekapDataByTotalSosialisasi(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT dt.document_type_in AS jenis_peraturan_in "
				+ "		,dt.document_type_en AS jenis_peraturan_en "
				+ "		,COUNT(1) total_sosialisasi "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' THEN 1 ELSE 0 END) tindak_lanjut_y "
				+ "		,SUM(CASE WHEN s.follow_up IS NULL OR s.follow_up = 'N' THEN 1 ELSE 0 END) tindak_lanjut_n "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' AND (spf.compliance_status IS NULL OR spf.compliance_status != 'COMPLIANCE_CLOSE') THEN 1 ELSE 0 END) in_progress "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' AND spf.compliance_status = 'COMPLIANCE_CLOSE' THEN 1 ELSE 0 END) closed "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' AND spf.compliance_status = 'COMPLIANCE_CLOSE' AND spf.followup_date = spf.target_date THEN 1 ELSE 0 END) meet_sla "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' AND spf.compliance_status = 'COMPLIANCE_CLOSE' AND spf.followup_date < spf.target_date THEN 1 ELSE 0 END) before_sla "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' AND spf.compliance_status = 'COMPLIANCE_CLOSE' AND spf.followup_date > spf.target_date THEN 1 ELSE 0 END) over_sla " 
				+ "	FROM wo_trc_socialization s "
				+ "		JOIN wo_trc_socialization_rgltn sr ON (sr.socialization_id = s.socialization_id) " 
				+ "		LEFT JOIN wo_mst_regulation r ON (r.regulation_id = sr.regulation_id) " 
				+ "		LEFT JOIN wo_mst_document_type dt ON (dt.document_type_id = r.document_type_id) " 
				+ "		LEFT JOIN wo_mst_document_category dc ON (dc.document_category_id = r.document_category_id) " 
				+ "		LEFT JOIN wo_trc_socialization_pic_fp spf ON (spf.socialization_id = s.socialization_id) " 
				+ "	WHERE 1=1 " );
		
		sb = getQueryWhereString(sb, searchCriteria);
		
		sb.append(" GROUP BY dt.document_type_in "
				+"		,dt.document_type_en ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<ReportSocializationRekap> vo = new ArrayList<ReportSocializationRekap>();
		
		if(resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ReportSocializationRekap data = new ReportSocializationRekap();
				
				data.setJenisPeraturanIn(obj[0] != null ? (String) obj[0] : null);
				data.setJenisPeraturanEn(obj[1] != null ? (String) obj[1] : null);
				data.setTotalSosialisasi(obj[2] != null ? ((BigInteger) obj[2]).intValue() : null);
				data.setTindakLanjutYes(obj[3] != null ? (((BigDecimal) obj[3]).toBigInteger()).intValue() : null);
				data.setTindakLanjutNo(obj[4] != null ? (((BigDecimal) obj[4]).toBigInteger()).intValue(): null);
				data.setStatusTindakLanjutInProgress(obj[5] != null ? (((BigDecimal) obj[5]).toBigInteger()).intValue() : null);
				data.setStatusTindakLanjutClosed(obj[6] != null ? (((BigDecimal) obj[6]).toBigInteger()).intValue() : null);
				data.setMeetSla(obj[7] != null ? (((BigDecimal) obj[7]).toBigInteger()).intValue() : null);
				data.setBeforeSla(obj[8] != null ? (((BigDecimal) obj[8]).toBigInteger()).intValue() : null);
				data.setOverSla(obj[9] != null ? (((BigDecimal) obj[9]).toBigInteger()).intValue() : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportSocializationRekapDataObjByTotalSosialisasi(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT dt.document_type_in AS jenis_peraturan_in "
				+ "		,dt.document_type_en AS jenis_peraturan_en "
				+ "		,COUNT(1) total_sosialisasi "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' THEN 1 ELSE 0 END) tindak_lanjut_y "
				+ "		,SUM(CASE WHEN s.follow_up IS NULL OR s.follow_up = 'N' THEN 1 ELSE 0 END) tindak_lanjut_n "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' AND (spf.compliance_status IS NULL OR spf.compliance_status != 'COMPLIANCE_CLOSE') THEN 1 ELSE 0 END) in_progress "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' AND spf.compliance_status = 'COMPLIANCE_CLOSE' THEN 1 ELSE 0 END) closed "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' AND spf.compliance_status = 'COMPLIANCE_CLOSE' AND spf.followup_date = spf.target_date THEN 1 ELSE 0 END) meet_sla "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' AND spf.compliance_status = 'COMPLIANCE_CLOSE' AND spf.followup_date < spf.target_date THEN 1 ELSE 0 END) before_sla "
				+ "		,SUM(CASE WHEN s.follow_up = 'Y' AND spf.compliance_status = 'COMPLIANCE_CLOSE' AND spf.followup_date > spf.target_date THEN 1 ELSE 0 END) over_sla " 
				+ "	FROM wo_trc_socialization s "
				+ "		JOIN wo_trc_socialization_rgltn sr ON (sr.socialization_id = s.socialization_id) " 
				+ "		LEFT JOIN wo_mst_regulation r ON (r.regulation_id = sr.regulation_id) " 
				+ "		LEFT JOIN wo_mst_document_type dt ON (dt.document_type_id = r.document_type_id) " 
				+ "		LEFT JOIN wo_mst_document_category dc ON (dc.document_category_id = r.document_category_id) " 
				+ "		LEFT JOIN wo_trc_socialization_pic_fp spf ON (spf.socialization_id = s.socialization_id) "
				+ "	WHERE 1=1 " );
		
		sb = getQueryWhereString(sb, searchCriteria);
		
		sb.append(" GROUP BY dt.document_type_in "
				+"		,dt.document_type_en "
				+"		,dc.document_category_in "
				+"		,dc.document_category_en ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<Object[]> vo = new ArrayList<Object[]>();
		
		if(resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				
				vo.add(obj);
			}
		}
		
		return vo;
	}
}
