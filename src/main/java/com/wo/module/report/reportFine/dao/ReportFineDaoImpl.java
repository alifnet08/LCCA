package com.wo.module.report.reportFine.dao;

import java.io.Serializable;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.report.reportFine.constant.ReportFineConstant;
import com.wo.module.report.reportFine.vo.ReportFineDetailVo;
import com.wo.module.report.reportFine.vo.ReportFineRekapVo;
import com.wo.module.report.reportGen.model.ReportGen;

@Repository("reportFineDao")
public class ReportFineDaoImpl extends GenericDAOHibernate<ReportGen, Long>
	implements ReportFineDao, Serializable{

	private static final long serialVersionUID = -9045006301314070589L;

	@SuppressWarnings("rawtypes")
	private void getQueryWhereStringDetail(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ReportFineConstant.WHERE_CREATION_DATE_FROM, col)) {
						sb.append(" AND TRUNC(fd.tanggal_filter) >= TO_DATE(:creationDateFrom, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportFineConstant.WHERE_CREATION_DATE_TO, col)) {
						sb.append(" AND TRUNC(fd.tanggal_filter) <= TO_DATE(:creationDateTo, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportFineConstant.WHERE_SENDER_CODE, col)) {
						sb.append(" AND fd.sender_code = :senderCode ");
					}
					if (StringUtils.equals(ReportFineConstant.WHERE_TARGET_DATE_FROM, col)) {
						sb.append(" and TRUNC(fd.target_date) >= TO_DATE(:targetDateFrom, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportFineConstant.WHERE_TARGET_DATE_TO, col)) {
						sb.append(" and TRUNC(fd.target_date) <= TO_DATE(:targetDateTo, 'yyyy-MM-dd') ");
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
					if (StringUtils.equals(ReportFineConstant.WHERE_CREATION_DATE_FROM, col)) {
						query.setParameter("creationDateFrom", val);
					}
					if (StringUtils.equals(ReportFineConstant.WHERE_CREATION_DATE_TO, col)) {
						query.setParameter("creationDateTo", val);
					}
					if (StringUtils.equals(ReportFineConstant.WHERE_SENDER_CODE, col)) {
						query.setParameter("senderCode", val);
					}
					if (StringUtils.equals(ReportFineConstant.WHERE_TARGET_DATE_FROM, col)) {
						query.setParameter("targetDateFrom", val);
					}
					if (StringUtils.equals(ReportFineConstant.WHERE_TARGET_DATE_TO, col)) {
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
					if (StringUtils.equals(ReportFineConstant.WHERE_CREATION_DATE_FROM, col)) {
						sb.append(" AND TRUNC(f.creation_date) >= TO_DATE(:creationDateFrom, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportFineConstant.WHERE_CREATION_DATE_TO, col)) {
						sb.append(" AND TRUNC(f.creation_date) <= TO_DATE(:creationDateTo, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportFineConstant.WHERE_SENDER_CODE, col)) {
						sb.append(" AND f.sender_code = :senderCode ");
					}
					if (StringUtils.equals(ReportFineConstant.WHERE_TARGET_DATE_FROM, col)) {
						sb.append(" and TRUNC(fpf.target_date) >= TO_DATE(:targetDateFrom, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportFineConstant.WHERE_TARGET_DATE_TO, col)) {
						sb.append(" and TRUNC(fpf.target_date) <= TO_DATE(:targetDateTo, 'yyyy-MM-dd') ");
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
					if (StringUtils.equals(ReportFineConstant.WHERE_CREATION_DATE_FROM, col)) {
						query.setParameter("creationDateFrom", val);
					}
					if (StringUtils.equals(ReportFineConstant.WHERE_CREATION_DATE_TO, col)) {
						query.setParameter("creationDateTo", val);
					}
					if (StringUtils.equals(ReportFineConstant.WHERE_SENDER_CODE, col)) {
						query.setParameter("senderCode", val);
					}
					if (StringUtils.equals(ReportFineConstant.WHERE_TARGET_DATE_FROM, col)) {
						query.setParameter("targetDateFrom", val);
					}
					if (StringUtils.equals(ReportFineConstant.WHERE_TARGET_DATE_TO, col)) {
						query.setParameter("targetDateTo", val);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportFineDetailVo> getReportFinaDetailAsVo(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT fd.pengirim_in ");
		sb.append("       ,fd.pengirim_en ");
		sb.append("       ,fd.tanggal_terima_surat ");
		sb.append("       ,TO_CHAR(fd.tanggal_terima_surat, 'DD-MON-YYYY') tanggal_terima_surat_str ");
		sb.append("       ,fd.no_surat ");
		sb.append("       ,fd.tanggal_surat ");
		sb.append("       ,TO_CHAR(fd.tanggal_surat, 'DD-MON-YYYY') tanggal_surat_str ");
		sb.append("       ,fd.perihal_in ");
		sb.append("       ,fd.perihal_en ");
		sb.append("       ,fd.ringkasan_surat ");
		sb.append("       ,fd.nama_laporan_in ");
		sb.append("       ,fd.nama_laporan_en ");
		sb.append("       ,fd.tindak_lanjut ");
		sb.append("       ,fd.RC ");
		sb.append("       ,fd.working_unit ");
		sb.append("       ,fd.region ");
		sb.append("       ,fd.debited_by_ojk ");
		sb.append("       ,TO_CHAR(fd.debited_by_ojk, 'DD-MON-YYYY') debited_by_ojk_str ");
		sb.append("       ,fd.fine_amount ");
		sb.append("       ,fd.breaches ");
		sb.append("       ,fd.root_cause ");
		sb.append("       ,fd.kategori_in ");
		sb.append("       ,fd.kategori_en ");
		sb.append("       ,fd.remedial_action ");
		sb.append("       ,fd.timeline ");
		sb.append("       ,TO_CHAR(fd.timeline, 'DD-MON-YYYY') timeline_str ");
		sb.append("       ,fd.user_maker ");
		sb.append("       ,fd.user_spv ");
		sb.append("       ,fd.no_err ");
		sb.append("       ,fd.status_in ");
		sb.append("       ,fd.status_en ");
		sb.append("       ,fd.pic_1 ");
		sb.append("       ,fd.pic_2 ");
		sb.append("       ,fd.pic_3 ");
		sb.append("       ,fd.divisi ");
		sb.append("       ,fd.pic_compliance ");
		sb.append("       ,fd.tanggal_konfirmasi ");
		sb.append("       ,TO_CHAR(fd.tanggal_konfirmasi, 'DD-MON-YYYY') tanggal_konfirmasi_str ");
		sb.append("       ,fd.tanggal_tindak_lanjut ");
		sb.append("       ,TO_CHAR(fd.tanggal_tindak_lanjut, 'DD-MON-YYYY') tanggal_tindak_lanjut_str ");
		sb.append("       ,fd.keterangan ");
		sb.append("       ,fd.sla ");
		sb.append("       ,fd.tanggal_filter ");
		sb.append("       ,TO_CHAR(fd.tanggal_filter, 'DD-MON-YYYY') tanggal_filter_str ");
		sb.append("       ,fd.sender_code ");
		sb.append(" FROM WO_V_FINE_DETAIL fd ");
		sb.append(" WHERE 1 = 1 ");
		
		this.getQueryWhereStringDetail(sb, searchCriteria);
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQuerySetStringDetail(query, searchCriteria);
		
		List result = query.getResultList();
		List<ReportFineDetailVo> vo = new ArrayList<ReportFineDetailVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				ReportFineDetailVo data = new ReportFineDetailVo();
				
				data.setPengirimIn(obj[0] != null ? (String) obj[0] : null);
				data.setPengirimEn(obj[1] != null ? (String) obj[1] : null);
				data.setTanggalTerimaSurat(obj[2] != null ? (Date) obj[2] : null);
				data.setTanggalTerimaSuratStr(obj[3] != null ? (String) obj[3] : null);
				data.setNoSurat(obj[4] != null ? (String) obj[4] : null);
				data.setTanggalSurat(obj[5] != null ? (Date) obj[5] : null);
				data.setTanggalSuratStr(obj[6] != null ? (String) obj[6] : null);
				data.setPerihalIn(obj[7] != null ? clobToString((Clob) obj[7]) : null);
				data.setPerihalEn(obj[8] != null ? clobToString((Clob) obj[8]) : null);
				data.setRingkasanSurat(obj[9] != null ? clobToString((Clob) obj[9]) : null);
				data.setNamaLaporanIn(obj[10] != null ? (String) obj[10] : null);
				data.setNamaLaporanEn(obj[11] != null ? (String) obj[11] : null);
				data.setTindakLanjut(obj[12] != null ? (String) obj[12] : null);
				data.setRC(obj[13] != null ? (String) obj[13] : null);
				data.setWorkingUnit(obj[14] != null ? (String) obj[14] : null);
				data.setRegion(obj[15] != null ? (String) obj[15] : null);
				data.setDebitedByOjk(obj[16] != null ? (Date) obj[16] : null);
				data.setDebitedByOjkStr(obj[17] != null ? (String) obj[17] : null);
				data.setFineAmount(obj[18] != null ? MathUtil.returnIdObjectToLong(obj[18]) : null);
				data.setBreaches(obj[19] != null ? clobToString((Clob) obj[19]) : null);
				data.setRootCause(obj[20] != null ? clobToString((Clob) obj[20]) : null);
				data.setKategoriIn(obj[21] != null ? (String) obj[21] : null);
				data.setKategoriEn(obj[22] != null ? (String) obj[22] : null);
				data.setRemedialAction(obj[23] != null ? (String) obj[23] : null);
				data.setTimeline(obj[24] != null ? (Date) obj[24] : null);
				data.setTimelineStr(obj[25] != null ? (String) obj[25] : null);
				data.setUserMaker(obj[26] != null ? (String) obj[26] : null);
				data.setUserSpv(obj[27] != null ? (String) obj[27] : null);
				data.setNoErr(obj[28] != null ? (String) obj[28] : null);
				data.setStatusIn(obj[29] != null ? (String) obj[29] : null);
				data.setStatusEn(obj[30] != null ? (String) obj[30] : null);
				data.setPic1(obj[31] != null ? (String) obj[31] : null);
				data.setPic2(obj[32] != null ? (String) obj[32] : null);
				data.setPic3(obj[33] != null ? (String) obj[33] : null);
				data.setDivisi(obj[34] != null ? (String) obj[34] : null);
				data.setPicCompliance(obj[35] != null ? (String) obj[35] : null);
				data.setTanggalKonfirmasi(obj[36] != null ? (Date) obj[36] : null);
				data.setTanggalKonfirmasiStr(obj[37] != null ? (String) obj[37] : null);
				data.setTanggalTindakLanjut(obj[38] != null ? (Date) obj[38] : null);
				data.setTanggalTindakLanjutStr(obj[39] != null ? (String) obj[39] : null);
				data.setKeterangan(obj[40] != null ? (String) obj[40] : null);
				data.setSla(obj[41] != null ? (String) obj[41] : null);
				data.setTanggalFilter(obj[42] != null ? (Date) obj[42] : null);
				data.setTanggalFilterStr(obj[43] != null ? (String) obj[43] : null);
				data.setSenderCode(obj[44] != null ? (String) obj[44] : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}	

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportFineRekapVo> getReportFinaRekapAsVo(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT pdSender.NAME_IN AS jenis_peraturan_in ");
		sb.append("       ,pdSender.NAME_EN AS jenis_peraturan_en ");
		sb.append("       ,COUNT(1) total_fine ");
		sb.append("       ,SUM(CASE WHEN f.FOLLOW_UP = 'Y' THEN 1 ELSE 0 END) tindak_lanjut_y ");
		sb.append("       ,SUM(CASE WHEN f.FOLLOW_UP IS NULL OR f.FOLLOW_UP = 'N' THEN 1 ELSE 0 END) tindak_lanjut_n ");
		sb.append("       ,SUM(CASE WHEN f.FOLLOW_UP = 'Y' AND (fpf.COMPLIANCE_STATUS IS NULL OR fpf.COMPLIANCE_STATUS != 'COMPLIANCE_CLOSE') THEN 1 ELSE 0 END) in_progress ");
		sb.append("       ,SUM(CASE WHEN f.FOLLOW_UP = 'Y' AND fpf.COMPLIANCE_STATUS = 'COMPLIANCE_CLOSE' THEN 1 ELSE 0 END) closed ");
		sb.append("       ,SUM(CASE WHEN f.FOLLOW_UP = 'Y' AND fpf.COMPLIANCE_STATUS = 'COMPLIANCE_CLOSE' AND COALESCE(fpf.FOLLOWUP_DATE,fpf.COMPLIANCE_DATE) = fpf.TARGET_DATE THEN 1 ELSE 0 END) meet_sla ");
		sb.append("       ,SUM(CASE WHEN f.FOLLOW_UP = 'Y' AND fpf.COMPLIANCE_STATUS = 'COMPLIANCE_CLOSE' AND COALESCE(fpf.FOLLOWUP_DATE,fpf.COMPLIANCE_DATE) < fpf.TARGET_DATE THEN 1 ELSE 0 END) before_sla ");
		sb.append("       ,SUM(CASE WHEN f.FOLLOW_UP = 'Y' AND fpf.COMPLIANCE_STATUS = 'COMPLIANCE_CLOSE' AND COALESCE(fpf.FOLLOWUP_DATE,fpf.COMPLIANCE_DATE) > fpf.TARGET_DATE THEN 1 ELSE 0 END) over_sla ");
		sb.append("       ,SUM(CASE WHEN f.CORRESPONDENCE_TYPE = 'FINE' THEN 1 ELSE 0 END) tipe_undangan ");
		sb.append(" FROM WO_TRC_FINE f ");
		sb.append("     INNER JOIN WO_MST_PARAMETER_DTL pdSender ");
		sb.append("         ON pdSender.PARAMETER_DTL_CODE = f.SENDER_CODE ");
		sb.append("     INNER JOIN WO_TRC_FINE_PIC_FOLLOWUP fpf ");
		sb.append("         ON fpf.FINE_ID = f.FINE_ID ");
		sb.append(" WHERE 1 = 1 ");
		
		this.getQueryWhereStringRekap(sb, searchCriteria);
		sb.append(" GROUP BY pdSender.NAME_IN ");
		sb.append("         ,pdSender.NAME_EN ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQuerySetStringRekap(query, searchCriteria);
		
		List result = query.getResultList();
		List<ReportFineRekapVo> vo = new ArrayList<ReportFineRekapVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				ReportFineRekapVo data = new ReportFineRekapVo();
				
				data.setPengirimSuratIn(obj[0] != null ? (String) obj[0] : null);
				data.setPengirimSuratEn(obj[1] != null ? (String) obj[1] : null);
				data.setTotalDenda(obj[2] != null ? MathUtil.returnIdObjectToInteger(obj[2]) : null);
				data.setTindakLanjutYes(obj[3] != null ? MathUtil.returnIdObjectToInteger(obj[3]) : null);
				data.setTindakLanjutNo(obj[4] != null ? MathUtil.returnIdObjectToInteger(obj[4]) : null);
				data.setInProgress(obj[5] != null ? MathUtil.returnIdObjectToInteger(obj[5]) : null);
				data.setClosed(obj[6] != null ? MathUtil.returnIdObjectToInteger(obj[6]) : null);
				data.setMeetSla(obj[7] != null ? MathUtil.returnIdObjectToInteger(obj[7]) : null);
				data.setBeforeSla(obj[8] != null ? MathUtil.returnIdObjectToInteger(obj[8]) : null);
				data.setOverSla(obj[9] != null ? MathUtil.returnIdObjectToInteger(obj[9]) : null);
				data.setTipeUndangan(obj[10] != null ? MathUtil.returnIdObjectToInteger(obj[10]) : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	public static String clobToString(Clob clob) {
		String result = "";
		int MAX_TEXTITEM_LENGTH = 8000;
		
		if (clob != null) {
			try {
				result = clob.getSubString(1, MAX_TEXTITEM_LENGTH);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
}
