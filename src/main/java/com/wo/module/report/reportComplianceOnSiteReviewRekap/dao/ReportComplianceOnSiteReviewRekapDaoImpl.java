package com.wo.module.report.reportComplianceOnSiteReviewRekap.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportComplianceOnSiteReviewDetail.constant.ReportComplianceOnSiteReviewDetailConstants;
import com.wo.module.report.reportComplianceOnSiteReviewRekap.model.ReportComplianceOnSiteReviewRekap;
import com.wo.module.report.reportGen.model.ReportGen;

@Repository("reportComplianceOnSiteReviewRekapDao")
public class ReportComplianceOnSiteReviewRekapDaoImpl extends GenericDAOHibernate<ReportGen, Long> 
	implements ReportComplianceOnSiteReviewDetailConstants, ReportComplianceOnSiteReviewRekapDao{

	@SuppressWarnings("rawtypes")
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString() != null ? searchVal.getSearchValueAsString() : "";
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(WHERE_CREATION_DATE_FROM, col)) {
						sb.append(" and TRUNC(cr.creation_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_CREATION_DATE_TO, col)) {
						sb.append(" and TRUNC(cr.creation_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_CATEGORY_REVIEW, col)) {
						sb.append(" and cr.review_category = '" + val + "' ");
					} else if (StringUtils.equals(WHERE_TARGET_DATE_FROM, col)) {
						sb.append(" and TRUNC(crpf.target_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_TARGET_DATE_TO, col)) {
						sb.append(" and TRUNC(crpf.target_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					}
				}
			}
		}
		
		return sb;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportComplianceOnSiteReviewRekap> getReportComplianceOnSiteReviewRekapByData(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select cat.name_in AS kategori_review_in "
				+ "		,cat.name_en AS kategori_review_en "
				+ "		,COUNT(1) total_review "
				+ "		,SUM(CASE WHEN cr.follow_up = 'Y' THEN 1 ELSE 0 END) tindak_lanjut_y "
				+ "		,SUM(CASE WHEN cr.follow_up IS NULL OR cr.follow_up = 'N' THEN 1 ELSE 0 END) tindak_lanjut_n "
				+ "		,SUM(CASE WHEN cr.follow_up = 'Y' AND (crpf.compliance_status IS NULL OR crpf.compliance_status != 'COMPLIANCE_CLOSE') THEN 1 ELSE 0 END) in_progress "
				+ "		,SUM(CASE WHEN cr.follow_up = 'Y' AND crpf.compliance_status = 'COMPLIANCE_CLOSE' THEN 1 ELSE 0 END) closed "
				+ "		,SUM(CASE WHEN cr.follow_up = 'Y' AND crpf.compliance_status = 'COMPLIANCE_CLOSE' AND "
				+ "			( SELECT MAX(followup_date) FROM wo_trc_cmplc_rvw_pc_fp_points crpfp "
				+ "				WHERE crpfp.CMPLC_REVIEW_PIC_FOLLOWUP_ID = crpf.CMPLC_REVIEW_PIC_FOLLOWUP_ID) = crpf.target_date THEN 1 ELSE 0 END) meet_sla "
				+ "		,SUM(CASE WHEN cr.follow_up = 'Y' AND crpf.compliance_status = 'COMPLIANCE_CLOSE' AND "
				+ "			( SELECT MAX(followup_date) FROM wo_trc_cmplc_rvw_pc_fp_points crpfp "
				+ "				WHERE crpfp.CMPLC_REVIEW_PIC_FOLLOWUP_ID = crpf.CMPLC_REVIEW_PIC_FOLLOWUP_ID) < crpf.target_date THEN 1 ELSE 0 END) before_sla "
				+ "		,SUM(CASE WHEN cr.follow_up = 'Y' AND crpf.compliance_status = 'COMPLIANCE_CLOSE' AND "
				+ "			( SELECT MAX(followup_date) FROM wo_trc_cmplc_rvw_pc_fp_points crpfp "
				+ "				WHERE crpfp.CMPLC_REVIEW_PIC_FOLLOWUP_ID = crpf.CMPLC_REVIEW_PIC_FOLLOWUP_ID) > crpf.target_date THEN 1 ELSE 0 END) over_sla "
				+ "	from wo_trc_compliance_review cr "
				+ "		LEFT JOIN wo_mst_parameter_dtl cat ON cat.parameter_code = 'REVIEW_CATEGORY' AND cat.parameter_dtl_code = cr.review_category "
				+ "		LEFT JOIN wo_trc_cmplc_review_pic_fp crpf ON crpf.compliance_review_id = cr.compliance_review_id "
				+ "	where 1=1 ");
		
		sb = getQueryWhereString(sb, searchCriteria);
		
		sb.append("GROUP BY cat.name_in, cat.name_en");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<ReportComplianceOnSiteReviewRekap> vo = new ArrayList<ReportComplianceOnSiteReviewRekap>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ReportComplianceOnSiteReviewRekap data = new ReportComplianceOnSiteReviewRekap();
				
				data.setKategoriNameIn(obj[0] != null ? (String) obj[0] : null);
				data.setKategoriNameEn(obj[1] != null ? (String) obj[1]  : null);
				data.setTotalReview(obj[2] != null ? (((BigDecimal) obj[2]).toBigInteger()).intValue() : null);
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
	public List<Object[]> getReportComplianceOnSiteReviewRekapByObj(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select cat.name_in AS kategori_review_in "
				+ "		,cat.name_en AS kategori_review_en "
				+ "		,COUNT(1) total_review "
				+ "		,SUM(CASE WHEN cr.follow_up = 'Y' THEN 1 ELSE 0 END) tindak_lanjut_y "
				+ "		,SUM(CASE WHEN cr.follow_up IS NULL OR cr.follow_up = 'N' THEN 1 ELSE 0 END) tindak_lanjut_n "
				+ "		,SUM(CASE WHEN cr.follow_up = 'Y' AND (crpf.compliance_status IS NULL OR crpf.compliance_status != 'COMPLIANCE_CLOSE') THEN 1 ELSE 0 END) in_progress "
				+ "		,SUM(CASE WHEN cr.follow_up = 'Y' AND crpf.compliance_status = 'COMPLIANCE_CLOSE' THEN 1 ELSE 0 END) closed "
				+ "		,SUM(CASE WHEN cr.follow_up = 'Y' AND crpf.compliance_status = 'COMPLIANCE_CLOSE' AND "
				+ "			( SELECT MAX(followup_date) FROM wo_trc_cmplc_rvw_pc_fp_points crpfp "
				+ "				WHERE crpfp.CMPLC_REVIEW_PIC_FOLLOWUP_ID = crpf.CMPLC_REVIEW_PIC_FOLLOWUP_ID) = crpf.target_date THEN 1 ELSE 0 END) meet_sla "
				+ "		,SUM(CASE WHEN cr.follow_up = 'Y' AND crpf.compliance_status = 'COMPLIANCE_CLOSE' AND "
				+ "			( SELECT MAX(followup_date) FROM wo_trc_cmplc_rvw_pc_fp_points crpfp "
				+ "				WHERE crpfp.CMPLC_REVIEW_PIC_FOLLOWUP_ID = crpf.CMPLC_REVIEW_PIC_FOLLOWUP_ID) < crpf.target_date THEN 1 ELSE 0 END) before_sla "
				+ "		,SUM(CASE WHEN cr.follow_up = 'Y' AND crpf.compliance_status = 'COMPLIANCE_CLOSE' AND "
				+ "			( SELECT MAX(followup_date) FROM wo_trc_cmplc_rvw_pc_fp_points crpfp "
				+ "				WHERE crpfp.CMPLC_REVIEW_PIC_FOLLOWUP_ID = crpf.CMPLC_REVIEW_PIC_FOLLOWUP_ID) > crpf.target_date THEN 1 ELSE 0 END) over_sla "
				+ "	from wo_trc_compliance_review cr "
				+ "		LEFT JOIN wo_mst_parameter_dtl cat ON cat.parameter_code = 'REVIEW_CATEGORY' AND cat.parameter_dtl_code = cr.review_category "
				+ "		LEFT JOIN wo_trc_cmplc_review_pic_fp crpf ON crpf.compliance_review_id = cr.compliance_review_id "
				+ "	where 1=1 ");
		
		sb = getQueryWhereString(sb, searchCriteria);
		
		sb.append("GROUP BY cat.name_in, cat.name_en");
		
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
