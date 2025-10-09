package com.wo.module.report.reportRmdDetail.dao;

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
import com.wo.module.report.reportRmdDetail.constant.ReportRmdDetailConstant;
import com.wo.module.report.reportRmdDetail.vo.ReportRmdDetailVo;

@Repository("reportRmdDetailDao")
public class ReportRmdDetailDaoImpl extends GenericDAOHibernate<ReportGen, Long> 
    implements ReportRmdDetailDao, ReportRmdDetailConstant {
	
	@SuppressWarnings({ "rawtypes" })
    private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString() != null ? searchVal.getSearchValueAsString() : "";
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(WHERE_CREATION_DATE_FROM, col)) {
						sb.append(" and TRUNC(rd.tanggal_filter) >= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_CREATION_DATE_TO, col)) {
						sb.append(" and TRUNC(rd.tanggal_filter) <= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_PROV_TYPE, col)) {
						sb.append(" and rd.jenis_ketentuan = '" +  val + "' ");
					} else if (StringUtils.equals(WHERE_DOC_TYPE, col)) {
						sb.append(" and rd.document_type_id = '" + val + "' ");
					} else if (StringUtils.equals(WHERE_SENDER_CODE, col)) {
						sb.append(" and rd.sender_code = '" + val + "' ");
					} else if (StringUtils.equals(WHERE_TARGET_DATE_FROM, col)) {
						sb.append(" and TRUNC(rd.target_date) >= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_TARGET_DATE_TO, col)) {
						sb.append(" and TRUNC(rd.target_date) <= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");
					}
				}
			}
		}
		
		return sb;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
	public List<ReportRmdDetailVo> getReportRmdDetailAsVo(List<? extends SearchObject> searchCriteria) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" SELECT " + 
    			"	rd.jenis_peraturan_in, " + 
    			"	rd.jenis_peraturan_en, " + 
    			"	rd.no_surat, " + 
    			"	rd.no_peraturan, " + 
    			"	rd.tipe_laporan_in, " + 
    			"	rd.tipe_laporan_en, " + 
    			"	rd.nama_laporan_in, " + 
    			"	rd.nama_laporan_en, " + 
    			"	rd.deskripsi, " + 
    			"	rd.target_date, " + 
    			"	rd.pic_1, " + 
    			"	rd.pic_2, " + 
    			"	rd.pic_3, " + 
    			"	rd.divisi, " + 
    			"	rd.status_in, " + 
    			"	rd.status_en, " + 
    			"	rd.bukti_konfirmasi, " + 
    			"	rd.tanggal_konfirmasi, " + 
    			"	rd.tanggal_tindak_lanjut, " + 
    			"	rd.keterangan, " + 
    			"	rd.sla, " + 
    			"	rd.tanggal_filter, " + 
    			"	rd.sender_code, " + 
    			"	rd.jenis_ketentuan, " + 
    			"	rd.document_type_id, " + 
    			"	rd.rmd_id " + 
    			"FROM  " + 
    			"wo_v_rmd_detail rd  where 1=1 ");

    	sb = getQueryWhereString(sb, searchCriteria);
    	
		Query query = getSession().createSQLQuery(sb.toString());

		List resultList = query.getResultList();

		List<ReportRmdDetailVo> vo = new ArrayList<ReportRmdDetailVo>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ReportRmdDetailVo data = new ReportRmdDetailVo();
                
                data.setJenisPeraturanIn(obj[0] != null ? (String) obj[0] : null);
                data.setJenisPeraturanEn(obj[1] != null ? (String) obj[1] : null);
                data.setNoSurat(obj[2] != null ? (String) obj[2] : null);
                data.setNoPeraturan(obj[3] != null ? (String) obj[3] : null);
                data.setTipeLaporanIn(obj[4] != null ? (String) obj[4] : null);
                data.setTipeLaporanEn(obj[5] != null ? (String) obj[5] : null);
                data.setNamaLaporanIn(obj[6] != null ? (String) obj[6] : null);
                data.setNamaLaporanEn(obj[7] != null ? (String) obj[7] : null);
                data.setDeskripsi(obj[8] != null ? (String) obj[8] : null);
                data.setTargetDate(obj[9] != null ? (Date) obj[9] : null);
                data.setPic1(obj[10] != null ? (String) obj[10] : null);
                data.setPic2(obj[11] != null ? (String) obj[11] : null);
                data.setPic3(obj[12] != null ? (String) obj[12] : null);
                data.setDivisi(obj[13] != null ? (String) obj[13] : null);
                data.setStatusIn(obj[14] != null ? (String) obj[14] : null);
                data.setStatusEn(obj[15] != null ? (String) obj[15] : null);
                data.setBuktiKonfirmasi(obj[16] != null ? (String) obj[16] : null);
                data.setTanggalKonfirmasi(obj[17] != null ? (Date) obj[17] : null);
                data.setTanggalTindakLanjut(obj[18] != null ? (Date) obj[18] : null);
                data.setKeterangan(obj[19] != null ? (String) obj[19] : null);
                data.setSla(obj[20] != null ? (String) obj[20] : null);
                data.setTanggalFilter(obj[21] != null ? (Date) obj[21] : null);
                data.setSenderCode(obj[22] != null ? (String) obj[22] : null);
                data.setJenisKetentuan(obj[23] != null ? (String) obj[23] : null);
                data.setDocumentTypeId(obj[24] != null ? MathUtil.returnIdObjectToLong(obj[24]) : null);
                data.setRmdId(obj[25] != null ? MathUtil.returnIdObjectToLong(obj[25]) : null);
                
				vo.add(data);
			}
		}

		return vo;
    }

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportRmdDetailAsObj(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT " + 
    			"	rd.jenis_peraturan_in, " + 
    			"	rd.jenis_peraturan_en, " + 
    			"	rd.no_surat, " + 
    			"	rd.no_peraturan, " + 
    			"	rd.tipe_laporan_in, " + 
    			"	rd.tipe_laporan_en, " + 
    			"	rd.nama_laporan_in, " + 
    			"	rd.nama_laporan_en, " + 
    			"	rd.deskripsi, " + 
    			"	rd.target_date, " + 
    			"	rd.pic_1, " + 
    			"	rd.pic_2, " + 
    			"	rd.pic_3, " + 
    			"	rd.divisi, " + 
    			"	rd.status_in, " + 
    			"	rd.status_en, " + 
    			"	rd.bukti_konfirmasi, " + 
    			"	rd.tanggal_konfirmasi, " + 
    			"	rd.tanggal_tindak_lanjut, " + 
    			"	rd.keterangan, " + 
    			"	rd.sla, " + 
    			"	rd.tanggal_filter, " + 
    			"	rd.sender_code, " + 
    			"	rd.jenis_ketentuan, " + 
    			"	rd.document_type_id, " + 
    			"	rd.rmd_id " + 
    			"FROM  " + 
    			"wo_v_rmd_detail rd  where 1=1 ");

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
