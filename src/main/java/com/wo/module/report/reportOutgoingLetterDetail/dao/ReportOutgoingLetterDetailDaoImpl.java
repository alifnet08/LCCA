package com.wo.module.report.reportOutgoingLetterDetail.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportOutgoingLetterDetail.constant.ReportOutgoingLetterDetailConstants;
import com.wo.module.report.reportOutgoingLetterDetail.model.ReportOutgoingLetterDetail;

@Repository("reportOutgoingLetterDetailDao")
public class ReportOutgoingLetterDetailDaoImpl extends GenericDAOHibernate<ReportGen, Long>
	implements ReportOutgoingLetterDetailDao, ReportOutgoingLetterDetailConstants{

	@SuppressWarnings("rawtypes")
	private StringBuilder getWhereQueryString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString() != null ? searchVal.getSearchValueAsString() : "";
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(WHERE_CREATION_DATE_FROM, col)) {
						sb.append(" and TRUNC(old.creation_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_CREATION_DATE_TO, col)) {
						sb.append(" and TRUNC(old.creation_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_LETTER_DATE_FROM, col)) {
						sb.append(" and TRUNC(old.tanggal_surat) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_LETTER_DATE_TO, col)) {
						sb.append(" and TRUNC(old.tanggal_surat) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					}
				}
			}
		}
		
		return sb;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportOutgoingLetterDetail> getReportOutgoingLetterDetailByAllData(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select  old.tujuan_surat_in "
				+ "		,old.tujuan_surat_en "
				+ "		,old.no_surat "
				+ "		,old.tanggal_surat "
				+ "		,old.perihal_in "
				+ "		,old.perihal_en "
				+ "		,old.disampaikan_kepada "
				+ "		,old.tembusan_surat "
				+ "		,old.attachment_dokumen "
				+ "		,old.creation_date "
				+ "		,TO_CHAR(old.tanggal_surat, 'dd-Mon-yyyy') tanggal_surat_str "
				+ "		,TO_CHAR(old.creation_date, 'dd-Mon-yyyy') creation_date_str "
				+ "		,old.dibuat_oleh "
				+ "	from wo_v_outgoing_letter_detail old "
				+ "	where 1 = 1 ");
		
		sb = getWhereQueryString(sb, searchCriteria);
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<ReportOutgoingLetterDetail> vo = new ArrayList<ReportOutgoingLetterDetail>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ReportOutgoingLetterDetail data = new ReportOutgoingLetterDetail();
				
				data.setTujuanSuratIn(obj[0] != null ? (String) obj[0] : null);
				data.setTujuanSuratEn(obj[1] != null ? (String) obj[1] : null);
				data.setNoSurat(obj[2] != null ? (String) obj[2] : null);
				data.setTanggalSurat(obj[3] != null ? (Date) obj[3] : null);
				data.setPerihalIn(obj[4] != null ? (String) obj[4] : null);
				data.setPerihalEn(obj[5] != null ? (String) obj[5] : null);
				data.setDisampaikanKepada(obj[6] != null ? (String) obj[6] : null);
				data.setTembusanSurat(obj[7] != null ? (String) obj[7] : null);
				data.setAttachmentDokumen(obj[8] != null ? (String) obj[8] : null);
				data.setCreationDate(obj[9] != null ? (Date) obj[9] : null);
				data.setTanggalSuratStr(obj[10] != null ? (String) obj[10] : null);
				data.setCreationDateStr(obj[11] != null ? (String) obj[11] : null);
				data.setNamaPembuatSurat(obj[12] != null ? (String) obj[12] : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportOutgoingLetterDetailByAllObj(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select  old.tujuan_surat_in "
				+ "		,old.tujuan_surat_en "
				+ "		,old.no_surat "
				+ "		,old.tanggal_surat "
				+ "		,old.perihal_in "
				+ "		,old.perihal_en "
				+ "		,old.disampaikan_kepada "
				+ "		,old.tembusan_surat "
				+ "		,old.attachment_dokumen "
				+ "		,old.creation_date "
				+ "		,TO_CHAR(old.tanggal_surat, 'dd-Mon-yyyy') tanggal_surat_str "
				+ "		,TO_CHAR(old.creation_date, 'dd-Mon-yyyy') creation_date_str "
				+ "		,old.dibuat_oleh "
				+ "	from wo_v_outgoing_letter_detail old "
				+ "	where 1 = 1 ");
		
		sb = getWhereQueryString(sb, searchCriteria);
		
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
