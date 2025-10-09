package com.wo.module.report.reportComplianceOnSiteReviewDetail.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportComplianceOnSiteReviewDetail.constant.ReportComplianceOnSiteReviewDetailConstants;
import com.wo.module.report.reportComplianceOnSiteReviewDetail.model.ReportComplianceOnSiteReviewDetail;
import com.wo.module.report.reportGen.model.ReportGen;

@Repository("reportComplianceOnSiteReviewDetailDao")
public class ReportComplianceOnSiteReviewDetailDaoImpl extends GenericDAOHibernate<ReportGen, Long> 
	implements ReportComplianceOnSiteReviewDetailDao, ReportComplianceOnSiteReviewDetailConstants{

	@SuppressWarnings("rawtypes")
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString() != null ? searchVal.getSearchValueAsString() : "";
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(WHERE_CREATION_DATE_FROM, col)) {
						sb.append(" and TRUNC(crd.creation_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_CREATION_DATE_TO, col)) {
						sb.append(" and TRUNC(crd.creation_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_CATEGORY_REVIEW, col)) {
						sb.append(" and crd.review_category = '" + val + "' ");
					} else if (StringUtils.equals(WHERE_TARGET_DATE_FROM, col)) {
						sb.append(" and TRUNC(crd.target_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_TARGET_DATE_TO, col)) {
						sb.append(" and TRUNC(crd.target_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					}
				}
			}
		}
		
		return sb;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportComplianceOnSiteReviewDetail> getReportComplianceOnSiterReviewByData(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append("	select crd.kategori_review_in "
				+ "		,crd.kategori_review_en "
				+ "		,crd.document_no "
				+ "		,crd.perihal_in "
				+ "		,crd.perihal_en "
				+ "		,crd.area_review "
				+ "		,crd.temuan "
				+ "		,crd.poin_tindak_lanjut "
				+ "		,crd.target_date "
				+ "		,crd.pic_compliance "
				+ "		,crd.pic_1 "
				+ "		,crd.pic_2 "
				+ "		,crd.pic_3 "
				+ "		,crd.status_in "
				+ "		,crd.status_en "
				+ "		,crd.bukti_konfirmasi "
				+ "		,crd.tanggal_pemenuhan "
				+ "		,crd.tanggal_konfirmasi "
				+ "		,crd.creation_date "
				+ "		,crd.review_category "
				+ "		,TO_CHAR(crd.target_date, 'dd-Mon-yyyy') target_date_str "
				+ "		,TO_CHAR(crd.tanggal_pemenuhan, 'dd-Mon-yyyy') tanggal_pemenuhan_str "
				+ "		,TO_CHAR(crd.tanggal_konfirmasi, 'dd-Mon-yyyy') tanggal_konfirmasi_str "
				+ "		,TO_CHAR(crd.creation_date, 'dd-Mon-yyyy') creation_date_str "
				+ "		,crd.sla "
				+ "	from wo_v_compliance_review_detail crd "
				+ "	where 1=1 ");
		
		sb = getQueryWhereString(sb, searchCriteria);
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<ReportComplianceOnSiteReviewDetail> vo = new ArrayList<ReportComplianceOnSiteReviewDetail>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ReportComplianceOnSiteReviewDetail data = new ReportComplianceOnSiteReviewDetail();
				
				data.setKategoriReviewIn(obj[0] != null ? (String) obj[0] : null);
				data.setKategoriReviewEn(obj[1] != null ? (String) obj[1] : null);
				data.setDocumentNo(obj[2] != null ? (String) obj[2] : null);
				data.setPerihalIn(obj[3] != null ? (String) obj[3] : null);
				data.setPerihalEn(obj[4] != null ? (String) obj[4] : null);
				data.setAreaReview(obj[5] != null ? (String) obj[5] : null);
				data.setTemuan(obj[6] != null ? (String) obj[6] : null);
				data.setPoinTindakLanjut(obj[7] != null ? (String) obj[7] : null);
				data.setTargetDate(obj[8] != null ? (Date) obj[8] : null);
				data.setPicCompliance(obj[9] != null ? (String) obj[9] : null);
				data.setPic1(obj[10] != null ? (String) obj[10] : null);
				data.setPic2(obj[11] != null ? (String) obj[11] : null);
				data.setPic3(obj[12] != null ? (String) obj[12] : null);
				data.setStatusIn(obj[13] != null ? (String) obj[13] : null);
				data.setStatusEn(obj[14] != null ? (String) obj[14] : null);
				data.setBuktiKonfirmasi(obj[15] != null ? (String) obj[15] : null);
				data.setTanggalPemenuhan(obj[16] != null ? (Date) obj[16] : null);
				data.setTanggalKonfirmasi(obj[17] != null ? (Date) obj[17] : null);
				data.setCreationDate(obj[18] != null ? (Date) obj[18] : null);
				data.setReviewCategory(obj[19] != null ? (String) obj[19] : null);
				
				//helper
				data.setTargetDateStr(obj[20] != null ? (String) obj[20] : null);
				data.setTanggalPemenuhanStr(obj[21] != null ? (String) obj[21] : null);
				data.setTanggalKonfirmasiStr(obj[22] != null ? (String) obj[22] : null);
				data.setCreationDateStr(obj[23] != null ? (String) obj[23] : null);
				data.setSla(obj[24] != null ? (String) obj[24] : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportComplianceOnSiterReviewByObj(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append("	select crd.kategori_review_in "
				+ "		,crd.kategori_review_en "
				+ "		,crd.document_no "
				+ "		,crd.perihal_in "
				+ "		,crd.perihal_en "
				+ "		,crd.area_review "
				+ "		,crd.temuan "
				+ "		,crd.poin_tindak_lanjut "
				+ "		,crd.target_date "
				+ "		,crd.pic_compliance "
				+ "		,crd.pic_1 "
				+ "		,crd.pic_2 "
				+ "		,crd.pic_3 "
				+ "		,crd.status_in "
				+ "		,crd.status_en "
				+ "		,crd.bukti_konfirmasi "
				+ "		,crd.tanggal_pemenuhan "
				+ "		,crd.tanggal_konfirmasi "
				+ "		,crd.creation_date "
				+ "		,crd.review_category "
				+ "		,TO_CHAR(crd.target_date, 'dd-Mon-yyyy') target_date_str "
				+ "		,TO_CHAR(crd.tanggal_pemenuhan, 'dd-Mon-yyyy') tanggal_pemenuhan_str "
				+ "		,TO_CHAR(crd.tanggal_konfirmasi, 'dd-Mon-yyyy') tanggal_konfirmasi_str "
				+ "		,TO_CHAR(crd.creation_date, 'dd-Mon-yyyy') creation_date_str "
				+ "	from wo_v_compliance_review_detail crd "
				+ "	where 1=1 ");
		
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
