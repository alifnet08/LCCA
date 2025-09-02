package com.wo.module.report.reportComplianceDocumentDetail.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportComplianceDocumentDetail.constant.ReportComplianceDocumentDetailConstants;
import com.wo.module.report.reportComplianceDocumentDetail.model.ReportComplianceDocumentDetail;
import com.wo.module.report.reportGen.model.ReportGen;

@Repository("reportComplianceDocumentDetailDao")
public class ReportComplianceDocumentDetailDaoImpl extends GenericDAOHibernate<ReportGen, Long> 
	implements ReportComplianceDocumentDetailConstants, ReportComplianceDocumentDetailDao{

	@SuppressWarnings("rawtypes")
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString() != null ? searchVal.getSearchValueAsString() : "";
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(WHERE_CREATION_DATE_FROM, col)) {
						sb.append(" and TRUNC(cdd.creation_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_CREATION_DATE_TO, col)) {
						sb.append(" and TRUNC(cdd.creation_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_DOCUMENT_DATE_FROM, col)) {
						sb.append(" and TRUNC(cdd.tanggal_dokumen) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_DOCUMENT_DATE_TO, col)) {
						sb.append(" and TRUNC(cdd.tanggal_dokumen) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_DOCUMENT_TYPE, col)) {
						sb.append(" and cdd.document_type = '" + val + "' ");
					} else if (StringUtils.equals(WHERE_RECEIVED_DOCUMENT_DATE_FROM, col)) {
						sb.append(" and TRUNC(cdd.tanggal_diterima) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_RECEIVED_DOCUMENT_DATE_TO, col)) {
						sb.append(" and TRUNC(cdd.tanggal_diterima) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					}
				}
			}
		}
		
		return sb;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportComplianceDocumentDetail> getReportComplianceDocumentDetailByData(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select tipe_dokumen_in "
				+ "		,cdd.tipe_dokumen_en "
				+ "		,cdd.no_dokumen "
				+ "		,cdd.tanggal_dokumen "
				+ "		,cdd.tanggal_diterima "
				+ "		,cdd.materi "
				+ "		,cdd.kelompok_huk_in "
				+ "		,cdd.kelompok_huk_en "
				+ "		,cdd.unit_kerja_pengusul "
				+ "		,cdd.pic_compliance "
				+ "		,cdd.keterangan "
				+ "		,cdd.attachment_dokumen "
				+ "		,cdd.creation_date "
				+ "		,cdd.document_type "
				+ "		,TO_CHAR(cdd.tanggal_dokumen, 'dd-Mon-yyyy') tanggal_dokumen_str "
				+ "		,TO_CHAR(cdd.tanggal_diterima, 'dd-Mon-yyyy') tanggal_diterima_str "
				+ "		,TO_CHAR(cdd.creation_date, 'dd-Mon-yyyy') creation_date_str "
				+ "	from wo_v_cmplc_document_detail cdd "
				+ "	where 1=1 ");
		
		sb = getQueryWhereString(sb, searchCriteria);
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();

		List<ReportComplianceDocumentDetail> vo = new ArrayList<ReportComplianceDocumentDetail>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ReportComplianceDocumentDetail data = new ReportComplianceDocumentDetail();
				
				data.setTipeDokumenIn(obj[0] != null ? (String) obj[0] : null);
				data.setTipeDokumenEn(obj[1] != null ? (String) obj[1] : null);
				data.setNoDokumen(obj[2] != null ? (String) obj[2] : null);
				data.setTanggalDokumen(obj[3] != null ? (Date) obj[3] : null);
				data.setTanggalDiterima(obj[4] != null ? (Date) obj[4] : null);
				data.setMateri(obj[5] != null ? (String) obj[5] : null);
				data.setKelompokHukIn(obj[6] != null ? (String) obj[6] : null);
				data.setKelompokHukEn(obj[7] != null ? (String) obj[7] : null);
				data.setUnitKerjaPengusul(obj[8] != null ? (String) obj[8] : null);
				data.setPicCompliance(obj[9] != null ? (String) obj[9] : null);
				data.setKeterangan(obj[10] != null ? (String) obj[10] : null);
				data.setAttachmentDokumen(obj[11] != null ? (String) obj[11] : null);
				data.setCreationDate(obj[12] != null ? (Date) obj[12] : null);			
				data.setDocumentType(obj[13] != null ? (String) obj[13] : null);
				
				//helper
				data.setTanggalDokumenStr(obj[14] != null ? (String) obj[14] : null);
				data.setTanggalDiterimaStr(obj[15] != null ? (String) obj[15] : null);
				data.setCreationDateStr(obj[16] != null ? (String) obj[16] : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportComplianceDocumentDetailByObj(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select tipe_dokumen_in "
				+ "		,cdd.tipe_dokumen_en "
				+ "		,cdd.no_dokumen "
				+ "		,cdd.tanggal_dokumen "
				+ "		,cdd.tanggal_diterima "
				+ "		,cdd.materi "
				+ "		,cdd.kelompok_huk_in "
				+ "		,cdd.kelompok_huk_en "
				+ "		,cdd.unit_kerja_pengusul "
				+ "		,cdd.pic_compliance "
				+ "		,cdd.keterangan "
				+ "		,cdd.attachment_dokumen "
				+ "		,cdd.creation_date "
				+ "		,cdd.document_type "
				+ "		,TO_CHAR(cdd.tanggal_dokumen, 'dd-Mon-yyyy') tanggal_dokumen_str "
				+ "		,TO_CHAR(cdd.tanggal_diterima, 'dd-Mon-yyyy') tanggal_diterima_str "
				+ "		,TO_CHAR(cdd.creation_date, 'dd-Mon-yyyy') creation_date_str "
				+ "	from wo_v_compliance_document_detail cdd "
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
