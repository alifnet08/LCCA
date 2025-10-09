package com.wo.module.report.reportSocializationDetail.dao;

import java.math.BigInteger;
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
import com.wo.module.report.reportSocializationDetail.constant.ReportSocializationDetailConstant;
import com.wo.module.report.reportSocializationDetail.vo.ReportSocializationDetailVo;

@Repository("reportSocializationDetailDao")
public class ReportSocializationDetailDaoImpl extends GenericDAOHibernate<ReportGen, Long> 
    implements ReportSocializationDetailDao, ReportSocializationDetailConstant {
	
	@SuppressWarnings("rawtypes")
    private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(WHERE_CREATION_DATE_FROM, col)) {
						sb.append(" and TRUNC(vsd.tanggal_filter) >= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_CREATION_DATE_TO, col)) {
						sb.append(" and TRUNC(vsd.tanggal_filter) <= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_PROV_TYPE, col)) {
						sb.append(" and vsd.jenis_ketentuan = '" +  val + "' ");
					} else if (StringUtils.equals(WHERE_DOC_TYPE, col)) {
						sb.append(" and vsd.document_type_id = " + val + " ");
					} else if (StringUtils.equals(WHERE_TARGET_DATE_FROM, col)) {
						sb.append(" and TRUNC(COALESCE(vsd.reschedule_target_3, vsd.reschedule_target_2, vsd.reschedule_target_1, vsd.target_date)) >= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_TARGET_DATE_TO, col)) {
						sb.append(" and TRUNC(COALESCE(vsd.reschedule_target_3, vsd.reschedule_target_2, vsd.reschedule_target_1, vsd.target_date)) <= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");
					}
				}
			}
		}
		
		return sb;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
	public List<ReportSocializationDetailVo> getReportSocializationDetailAsVo(List<? extends SearchObject> searchCriteria) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" SELECT vsd.peraturan_in, " + 
    			"	vsd.peraturan_en, " + 
    			"	vsd.jenis_peraturan_in, " + 
    			"	vsd.jenis_peraturan_en, " + 
    			"	vsd.no_peraturan, " + 
    			"	vsd.judul_peraturan_in, " + 
    			"	vsd.judul_peraturan_en, " + 
    			"	vsd.kategori_dokumen_in, " + 
    			"	vsd.kategori_dokumen_en, " + 
    			"	vsd.tindak_lanjut, " + 
    			"	vsd.tindak_lanjut_note, " + 
    			"	vsd.target_date, " + 
    			"	vsd.reschedule_target_1, " + 
    			"	vsd.reschedule_target_2, " + 
    			"	vsd.reschedule_target_3, " + 
    			"	vsd.status_in, " + 
    			"	vsd.status_en, " + 
    			"	vsd.pic_1, " + 
    			"	vsd.pic_2, " + 
    			"	vsd.pic_3, " + 
    			"	vsd.divisi, " + 
    			"	vsd.pic_compliance, " + 
    			"	vsd.bukti_konfirmasi, " + 
    			"	vsd.tanggal_konfirmasi, " + 
    			"	vsd.tanggal_tindak_lanjut, " + 
    			"	vsd.keterangan, " + 
    			"	vsd.sla, " + 
    			"	vsd.cnt, " + 
    			"	vsd.tanggal_filter, " + 
    			"	vsd.jenis_ketentuan, " + 
    			"	vsd.document_type_id " + 
    			"FROM wo_v_socilization_detail vsd  where 1=1 ");

    	sb = getQueryWhereString(sb, searchCriteria);
    	
		Query query = getSession().createSQLQuery(sb.toString());

		List resultList = query.getResultList();

		List<ReportSocializationDetailVo> vo = new ArrayList<ReportSocializationDetailVo>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ReportSocializationDetailVo data = new ReportSocializationDetailVo();
                
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
                data.setRescheduleTargetDate1(obj[12] != null ? (Date) obj[12] : null);
                data.setRescheduleTargetDate2(obj[13] != null ? (Date) obj[13] : null);
                data.setRescheduleTargetDate3(obj[14] != null ? (Date) obj[14] : null);
                data.setStatusIn(obj[15] != null ? (String) obj[15] : null);
                data.setStatusEn(obj[16] != null ? (String) obj[16] : null);
                data.setPic1(obj[17] != null ? (String) obj[17] : null);
                data.setPic2(obj[18] != null ? (String) obj[18] : null);
                data.setPic3(obj[19] != null ? (String) obj[19] : null);
                data.setDivisi(obj[20] != null ? (String) obj[20] : null);
                data.setPicCompliance(obj[21] != null ? (String) obj[21] : null);
                data.setBuktiKonfirmasi(obj[22] != null ? (String) obj[22] : null);
                data.setTanggalKonfirmasi(obj[23] != null ? (Date) obj[23] : null);
                data.setTanggalTindakLanjut(obj[24] != null ? (Date) obj[24] : null);
                data.setKeterangan(obj[25] != null ? (String) obj[25] : null);
                data.setSla(obj[26] != null ? (String) obj[26] : null);
                data.setCnt(obj[27] != null ? (BigInteger) obj[27] : null);
                data.setTanggalFilter(obj[28] != null ? (Date) obj[28] : null);
                data.setJenisKetentuan(obj[29] != null ? (String) obj[29] : null);
                data.setDocumentTypeId(obj[30] != null ? MathUtil.returnIdObjectToLong(obj[30]) : null);
				vo.add(data);
			}
		}

		return vo;
    }

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportSocializationDetailAsObj(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
    	sb.append(" SELECT vsd.peraturan_in, " + 
    			"	vsd.peraturan_en, " + 
    			"	vsd.jenis_peraturan_in, " + 
    			"	vsd.jenis_peraturan_en, " + 
    			"	vsd.no_peraturan, " + 
    			"	vsd.judul_peraturan_in, " + 
    			"	vsd.judul_peraturan_en, " + 
    			"	vsd.kategori_dokumen_in, " + 
    			"	vsd.kategori_dokumen_en, " + 
    			"	vsd.tindak_lanjut, " + 
    			"	vsd.tindak_lanjut_note, " + 
    			"	vsd.target_date, " + 
    			"	vsd.reschedule_target_1, " + 
    			"	vsd.reschedule_target_2, " + 
    			"	vsd.reschedule_target_3, " + 
    			"	vsd.status_in, " + 
    			"	vsd.status_en, " + 
    			"	vsd.pic_1, " + 
    			"	vsd.pic_2, " + 
    			"	vsd.pic_3, " + 
    			"	vsd.divisi, " + 
    			"	vsd.pic_compliance, " + 
    			"	vsd.bukti_konfirmasi, " + 
    			"	vsd.tanggal_konfirmasi, " + 
    			"	vsd.tanggal_tindak_lanjut, " + 
    			"	vsd.keterangan, " + 
    			"	vsd.sla, " + 
    			"	vsd.cnt, " + 
    			"	vsd.tanggal_filter, " + 
    			"	vsd.jenis_ketentuan, " + 
    			"	vsd.document_type_id " + 
    			"FROM wo_v_socilization_detail vsd where 1=1 ");

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
