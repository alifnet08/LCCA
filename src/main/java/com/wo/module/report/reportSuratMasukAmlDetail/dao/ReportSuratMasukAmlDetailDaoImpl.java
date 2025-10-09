package com.wo.module.report.reportSuratMasukAmlDetail.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportSuratMasukAmlDetail.model.ReportSuratMasukAmlDetail;
import com.wo.module.report.reportSuratMasukDetail.constant.ReportSuratMasukDetailConstants;

@Repository("reportSuratMasukAmlDetailDao")
public class ReportSuratMasukAmlDetailDaoImpl extends GenericDAOHibernate<ReportGen, Long> 
	implements ReportSuratMasukAmlDetailDao, ReportSuratMasukDetailConstants{

	@SuppressWarnings("rawtypes")
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString() != null ? searchVal.getSearchValueAsString() : "";
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(WHERE_CREATION_DATE_FROM, col)) {
						sb.append(" and TRUNC(vcad.tanggal_filter) >= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_CREATION_DATE_TO, col)) {
						sb.append(" and TRUNC(vcad.tanggal_filter) <= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_SENDER_CODE, col)) {
						sb.append(" and vcad.sender_code = '" +  val + "' ");
					} else if (StringUtils.equals(WHERE_TARGET_DATE_FROM, col)) {
						sb.append(" and TRUNC(vcad.target_date) >= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_TARGET_DATE_TO, col)) {
						sb.append(" and TRUNC(vcad.target_date) <= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");
					} 
				}
			}
		}
		return sb;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportSuratMasukAmlDetail> getReportSuratMasukAmlDetailByData(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select vcad.jenis_peraturan_in "
				+ "		,vcad.jenis_peraturan_en "
				+ "		,vcad.tipe_surat_in "
				+ "		,vcad.tipe_surat_en "
				+ "		,vcad.tanggal_terima_surat "
				+ "		,vcad.no_surat "
				+ "		,vcad.tanggal_surat "
				+ "		,vcad.perihal_in "
				+ "		,vcad.perihal_en "
				+ "		,vcad.ringkasan_surat "
				+ "		,vcad.tindak_lanjut "
				+ "		,vcad.target_date "
				+ "		,vcad.status_in "
				+ "		,vcad.status_en "
				+ "		,vcad.pic_1 "
				+ "		,vcad.pic_2 "
				+ "		,vcad.pic_3 "
				+ "		,vcad.divisi "
				+ "		,vcad.pic_compliance "
				+ "		,vcad.bukti_konfirmasi "
				+ "		,vcad.tanggal_konfirmasi "
				+ "		,vcad.tanggal_tindak_lanjut "
				+ "		,vcad.keterangan "
				+ "		,vcad.sla "
				+ "		,vcad.tanggal_filter "
				+ "		,vcad.sender_code "
				+ "		,TO_CHAR(vcad.tanggal_terima_surat, 'dd-Mon-yyyy' ) tanggal_terima_surat_str "
				+ "		,TO_CHAR(vcad.tanggal_surat, 'dd-Mon-yyyy' ) tanggal_surat_str "
				+ "		,TO_CHAR(vcad.target_date, 'dd-Mon-yyyy' ) target_date_str"
				+ "		,TO_CHAR(vcad.tanggal_konfirmasi, 'dd-Mon-yyyy' ) tanggal_konfirmasi_str "
				+ "		,TO_CHAR(vcad.tanggal_tindak_lanjut, 'dd-Mon-yyyy' ) tanggal_tindak_lanjut_str "
				+ "		,TO_CHAR(vcad.tanggal_filter, 'dd-Mon-yyyy' ) tanggal_filtert_str "
				+ "	from wo_v_correspondence_aml_detail vcad "
				+ "	where 1=1 ");
		
		sb = getQueryWhereString(sb, searchCriteria);
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<ReportSuratMasukAmlDetail> vo = new ArrayList<ReportSuratMasukAmlDetail>();
		
		if(resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj  = (Object[]) resultList.get(i);
				ReportSuratMasukAmlDetail data = new ReportSuratMasukAmlDetail();
				
				data.setJenisPeraturanIn(obj[0] != null ? (String) obj[0] : null);
				data.setJenisPeraturanEn(obj[1] != null ? (String) obj[1] : null);
				data.setTipeSuratIn(obj[2] != null ? (String) obj[2] : null);
				data.setTipeSuratEn(obj[3] != null ? (String) obj[3] : null);
				data.setTanggalTerimaSurat(obj[4] != null ? (Date) obj[4] : null);
				data.setNoSurat(obj[5] != null ? (String) obj[5] : null);
				data.setTanggalSurat(obj[6] != null ? (Date) obj[6] : null);
				data.setPerihalIn(obj[7] != null ? (String) obj[7] : null);
				data.setPerihalEn(obj[8] != null ? (String) obj[8] : null);
				data.setRingkasanSurat(obj[9] != null ? (String) obj[9] : null);
				data.setTindakLanjut(obj[10] != null ? (String) obj[10] : null);
				data.setTargetDate(obj[11] != null ? (Date) obj[11] : null);
				data.setStatusIn(obj[12] != null ? (String) obj[12] : null);
				data.setStatusEn(obj[13] != null ? (String) obj[13] : null);
				data.setPic1(obj[14] != null ? (String) obj[14] : null);
				data.setPic2(obj[15] != null ? (String) obj[15] : null);
				data.setPic3(obj[16] != null ? (String) obj[16] : null);
				data.setDivisi(obj[17] != null ? (String) obj[17] : null);
				data.setPicCompliance(obj[18] != null ? (String) obj[18] : null);
				data.setBuktiKonfirmasi(obj[19] != null ? (String) obj[19] : null);
				data.setTanggalKonfirmasi(obj[20] !=  null ? (Date) obj[20] : null);
				data.setTanggalTindakLanjut(obj[21] != null ? (Date) obj[21] : null);
				data.setKeterangan(obj[22] != null ? (String) obj[22] : null);
				data.setSla(obj[23] != null ? (String) obj[23] : null);
				data.setTanggalFilter(obj[24] != null ? (Date) obj[24] : null);
				data.setSenderCode(obj[25] != null ? (String) obj[25] : null);
				
				// helper
				data.setTanggalTerimaSuratStr(obj[26] != null ? (String) obj[26] : null);
				data.setTanggalSuratStr(obj[27] != null ? (String) obj[27] : null);
				data.setTargetDateStr(obj[28] != null ? (String) obj[28] : null);
				data.setTanggalKonfirmasiStr(obj[29] != null ? (String) obj[29] : null);
				data.setTanggalTindakLanjutStr(obj[30] != null ? (String) obj[30] : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportSuratMasukAmlDetailByObj(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select vcad.jenis_peraturan_in "
				+ "		,vcad.jenis_peraturan_en "
				+ "		,vcad.tipe_surat_in "
				+ "		,vcad.tipe_surat_en "
				+ "		,vcad.tanggal_terima_surat "
				+ "		,vcad.no_surat "
				+ "		,vcad.tanggal_surat "
				+ "		,vcad.perihal_in "
				+ "		,vcad.perihal_en "
				+ "		,vcad.ringkasan_surat "
				+ "		,vcad.tindak_lanjut "
				+ "		,vcad.target_date "
				+ "		,vcad.status_in "
				+ "		,vcad.status_en "
				+ "		,vcad.pic_1 "
				+ "		,vcad.pic_2 "
				+ "		,vcad.pic_3 "
				+ "		,vcad.divisi "
				+ "		,vcad.pic_compliance "
				+ "		,vcad.bukti_konfirmasi "
				+ "		,vcad.tanggal_konfirmasi "
				+ "		,vcad.tanggal_tindak_lanjut "
				+ "		,vcad.keterangan "
				+ "		,vcad.sla "
				+ "		,vcad.tanggal_filter "
				+ "		,vcad.sender_code "
				+ "		,TO_CHAR(vcad.tanggal_terima_surat, 'dd-Mon-yyyy' ) tanggal_terima_surat_str "
				+ "		,TO_CHAR(vcad.tanggal_surat, 'dd-Mon-yyyy' ) tanggal_surat_str "
				+ "		,TO_CHAR(vcad.target_date, 'dd-Mon-yyyy' ) target_date_str"
				+ "		,TO_CHAR(vcad.tanggal_konfirmasi, 'dd-Mon-yyyy' ) tanggal_konfirmasi_str "
				+ "		,TO_CHAR(vcad.tanggal_tindak_lanjut, 'dd-Mon-yyyy' ) tanggal_tindak_lanjut_str "
				+ "		,TO_CHAR(vcad.tanggal_filter, 'dd-Mon-yyyy' ) tanggal_filtert_str "
				+ "	from wo_v_correspondence_aml_detail vcad "
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
