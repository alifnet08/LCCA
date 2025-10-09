package com.wo.module.report.reportSuratMasukAmlRekap.dao;

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
import com.wo.module.report.reportSuratMasukAmlDetail.constant.ReportSuratMasukAmlDetailConstants;
import com.wo.module.report.reportSuratMasukAmlRekap.model.ReportSuratMasukAmlRekap;

@Repository("reportSuratMasukAmlRekapDao")
public class ReportSuratMasukAmlRekapDaoImpl extends GenericDAOHibernate<ReportGen, Long>
	implements ReportSuratMasukAmlRekapDao, ReportSuratMasukAmlDetailConstants{

	@SuppressWarnings("rawtypes")
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString() != null ? searchVal.getSearchValueAsString() : "";
				
				if (!(StringUtils.isBlank(val))) {
					if (StringUtils.equals(WHERE_CREATION_DATE_FROM, col)) {
						sb.append("	and TRUNC(c.creation_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_CREATION_DATE_TO, col)) {
						sb.append("	and TRUNC(c.creation_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_SENDER_CODE, col)) {
						sb.append("	and c.sender_code = '" + val + "' ");
					} else if (StringUtils.equals(WHERE_TARGET_DATE_FROM, col)) {
						sb.append("	and TRUNC(c.target_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_TARGET_DATE_TO, col)) {
						sb.append("	and TRUNC(c.target_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					}
				}
			}
		}
		
		return sb;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportSuratMasukAmlRekap> getReportSuratMasukAmlRekapByData(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select sndr.name_in AS jenis_peraturan_in "
				+ "		,sndr.name_en AS jenis_peraturan_en "
				+ "		,count(1) total_surat "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' THEN 1 ELSE 0 END) tindak_lanjut_y "
				+ "		,SUM(CASE WHEN c.follow_up IS NULL OR c.follow_up = 'N' THEN 1 ELSE 0 END) tindak_lanjut_n "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' AND (c.compliance_status IS NULL OR c.compliance_status != 'COMPLIANCE_CLOSE') THEN 1 ELSE 0 END) in_progress "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' AND c.compliance_status = 'COMPLIANCE_CLOSE' THEN 1 ELSE 0 END) closed "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' AND c.compliance_status = 'COMPLIANCE_CLOSE' AND COALESCE(c.followup_date, c.confirmation_date) = c.target_date THEN 1 ELSE 0 END) meet_sla "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' AND c.compliance_status = 'COMPLIANCE_CLOSE' AND COALESCE(c.followup_date, c.confirmation_date) < c.target_date THEN 1 ELSE 0 END) before_sla "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' AND c.compliance_status = 'COMPLIANCE_CLOSE' AND COALESCE(c.followup_date, c.confirmation_date) > c.target_date THEN 1 ELSE 0 END) over_sla "
				+ "	from wo_trc_correspondence c "
				+ "		INNER JOIN wo_mst_parameter_dtl sndr ON sndr.parameter_code = 'SENDER_AML' "
				+ "			AND sndr.parameter_dtl_code = c.sender_code "
				+ "	where 1=1 ");
		
		sb = getQueryWhereString(sb, searchCriteria);
		
		sb.append("GROUP BY sndr.name_in, sndr.name_en ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<ReportSuratMasukAmlRekap> vo = new ArrayList<ReportSuratMasukAmlRekap>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ReportSuratMasukAmlRekap data = new ReportSuratMasukAmlRekap();
				
				data.setPengirimSuratIn(obj[0] != null ? (String) obj[0] : null);
				data.setPengirimSuratEn(obj[1] != null ? (String) obj[1] : null);
				data.setTotalSuratMasuk(obj[2] != null ? ((BigInteger) obj[2]).intValue() : null);
				data.setTindakLanjutYes(obj[3] != null ? (((BigDecimal) obj[3]).toBigInteger()).intValue() : null);
				data.setTindakLanjutNo(obj[4] != null ? (((BigDecimal) obj[4]).toBigInteger()).intValue() : null);
				data.setInProgress(obj[5] != null ? (((BigDecimal) obj[5]).toBigInteger()).intValue() : null);
				data.setClosed(obj[6] != null ? (((BigDecimal) obj[6]).toBigInteger()).intValue() : null);
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
	public List<Object[]> getReportSuratMasukAmlRekapByObj(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select sndr.name_in AS jenis_peraturan_in "
				+ "		,sndr.name_en AS jenis_peraturan_en "
				+ "		,count(1) total_surat "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' THEN 1 ELSE 0 END) tindak_lanjut_y "
				+ "		,SUM(CASE WHEN c.follow_up IS NULL OR c.follow_up = 'N' THEN 1 ELSE 0 END) tindak_lanjut_n "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' AND (c.compliance_status IS NULL OR c.compliance_status != 'COMPLIANCE_CLOSE') THEN 1 ELSE 0 END) in_progress "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' AND c.compliance_status = 'COMPLIANCE_CLOSE' THEN 1 ELSE 0 END) closed "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' AND c.compliance_status = 'COMPLIANCE_CLOSE' AND COALESCE(c.followup_date, c.confirmation_date) = c.target_date THEN 1 ELSE 0 END) meet_sla "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' AND c.compliance_status = 'COMPLIANCE_CLOSE' AND COALESCE(c.followup_date, c.confirmation_date) < c.target_date THEN 1 ELSE 0 END) before_sla "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' AND c.compliance_status = 'COMPLIANCE_CLOSE' AND COALESCE(c.followup_date, c.confirmation_date) > c.target_date THEN 1 ELSE 0 END) over_sla "
				+ "	from wo_trc_correspondence c "
				+ "		INNER JOIN wo_mst_parameter_dtl sndr ON sndr.parameter_code = 'SENDER_AML' "
				+ "			AND sndr.parameter_dtl_code = c.sender_code "
				+ "	where 1=1 ");
		
		sb = getQueryWhereString(sb, searchCriteria);
		
		sb.append("GROUP BY sndr.name_in, sndr.name_en ");
		
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
	public List<ReportSuratMasukAmlRekap> getReportSuratMasukAmlRekapByTipeSurat(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select sndr.name_in AS jenis_peraturan_in "
				+ "		,sndr.name_en AS jenis_peraturan_en "
				+ "		,count(1) total_surat "
				+ "		,SUM(CASE WHEN c.correspondence_type = 'INVITATION' THEN 1 ELSE 0 END) undangan "
				+ "		,SUM(CASE WHEN c.correspondence_type = 'NONINVITATION' THEN 1 ELSE 0 END) non_undangan "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' THEN 1 ELSE 0 END) tindak_lanjut_y "
				+ "		,SUM(CASE WHEN c.follow_up IS NULL OR c.follow_up = 'N' THEN 1 ELSE 0 END) tindak_lanjut_n "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' AND (c.compliance_status IS NULL OR c.compliance_status != 'COMPLIANCE_CLOSE') THEN 1 ELSE 0 END) in_progress "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' AND c.compliance_status = 'COMPLIANCE_CLOSE' THEN 1 ELSE 0 END) closed "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' AND c.compliance_status = 'COMPLIANCE_CLOSE' AND COALESCE(c.followup_date, c.confirmation_date) = c.target_date THEN 1 ELSE 0 END) meet_sla "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' AND c.compliance_status = 'COMPLIANCE_CLOSE' AND COALESCE(c.followup_date, c.confirmation_date) < c.target_date THEN 1 ELSE 0 END) before_sla "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' AND c.compliance_status = 'COMPLIANCE_CLOSE' AND COALESCE(c.followup_date, c.confirmation_date) > c.target_date THEN 1 ELSE 0 END) over_sla "
				+ "	from wo_trc_correspondence c "
				+ "		INNER JOIN wo_mst_parameter_dtl sndr ON sndr.parameter_code = 'SENDER_AML' "
				+ "			AND sndr.parameter_dtl_code = c.sender_code "
				+ "	where 1=1 ");
		
		sb = getQueryWhereString(sb, searchCriteria);	
		
		sb.append("	GROUP BY sndr.name_in, sndr.name_en ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<ReportSuratMasukAmlRekap> vo = new ArrayList<ReportSuratMasukAmlRekap>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ReportSuratMasukAmlRekap data = new ReportSuratMasukAmlRekap();
				
				data.setPengirimSuratIn(obj[0] != null ? (String) obj[0] : null);
				data.setPengirimSuratEn(obj[1] != null ? (String) obj[1] : null);
				data.setTotalSuratMasuk(obj[2] != null ? ((BigInteger) obj[2]).intValue() : null);
				data.setUndangan(obj[3] != null ? (((BigDecimal) obj[3]).toBigInteger()).intValue() : null);
				data.setNonUndangan(obj[4] != null ? (((BigDecimal) obj[4]).toBigInteger()).intValue() : null);
				data.setTindakLanjutYes(obj[5] != null ? (((BigDecimal) obj[5]).toBigInteger()).intValue() : null);
				data.setTindakLanjutNo(obj[6] != null ? (((BigDecimal) obj[6]).toBigInteger()).intValue() : null);
				data.setInProgress(obj[7] != null ? (((BigDecimal) obj[7]).toBigInteger()).intValue() : null);
				data.setClosed(obj[8] != null ? (((BigDecimal) obj[8]).toBigInteger()).intValue() : null);
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
	public List<Object[]> getReportSuratMasukAmlRekapByTipeSuratObj(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select sndr.name_in AS jenis_peraturan_in "
				+ "		,sndr.name_en AS jenis_peraturan_en "
				+ "		,count(1) total_surat "
				+ "		,SUM(CASE WHEN c.correspondence_type = 'INVITATION' THEN 1 ELSE 0 END) undangan "
				+ "		,SUM(CASE WHEN c.correspondence_type = 'NONINVITATION' THEN 1 ELSE 0 END) non_undangan "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' THEN 1 ELSE 0 END) tindak_lanjut_y "
				+ "		,SUM(CASE WHEN c.follow_up IS NULL OR c.follow_up = 'N' THEN 1 ELSE 0 END) tindak_lanjut_n "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' AND (c.compliance_status IS NULL OR c.compliance_status != 'COMPLIANCE_CLOSE') THEN 1 ELSE 0 END) in_progress "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' AND c.compliance_status = 'COMPLIANCE_CLOSE' THEN 1 ELSE 0 END) closed "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' AND c.compliance_status = 'COMPLIANCE_CLOSE' AND COALESCE(c.followup_date, c.confirmation_date) = c.target_date THEN 1 ELSE 0 END) meet_sla "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' AND c.compliance_status = 'COMPLIANCE_CLOSE' AND COALESCE(c.followup_date, c.confirmation_date) < c.target_date THEN 1 ELSE 0 END) before_sla "
				+ "		,SUM(CASE WHEN c.follow_up = 'Y' AND c.compliance_status = 'COMPLIANCE_CLOSE' AND COALESCE(c.followup_date, c.confirmation_date) > c.target_date THEN 1 ELSE 0 END) over_sla "
				+ "	from wo_trc_correspondence c "
				+ "		INNER JOIN wo_mst_parameter_dtl sndr ON sndr.parameter_code = 'SENDER_AML' "
				+ "			AND sndr.parameter_dtl_code = c.sender_code "
				+ "	where 1=1 ");
		
		sb = getQueryWhereString(sb, searchCriteria);	
		
		sb.append("	GROUP BY sndr.name_in, sndr.name_en ");
		
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
