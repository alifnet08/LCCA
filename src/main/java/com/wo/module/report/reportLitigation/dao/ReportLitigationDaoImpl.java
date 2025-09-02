package com.wo.module.report.reportLitigation.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wo.module.common.constant.Constants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportLitigation.vo.ReportLitiPerdataTergugatVo;
import com.wo.module.report.reportLitigation.vo.ReportLitiPerdataPenggugatVo;
import com.wo.module.report.reportLitigation.constant.ReportLitigationConstant;
import com.wo.module.report.reportLitigation.vo.ReportLitiPailitPKPUVo;
import com.wo.module.report.reportLitigation.vo.ReportLitiPidanaPelaporVo;
import com.wo.module.report.reportLitigation.vo.ReportLitiPidanaTerlaporVo;

@Repository("reportLitigationDao")
public class ReportLitigationDaoImpl extends GenericDAOHibernate<ReportGen, Long> implements ReportLitigationDao, Serializable{

	private static final long serialVersionUID = 6384968192079850808L;

	@SuppressWarnings("rawtypes")
	private void setQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ReportLitigationConstant.SEARCH_TANGGAL_PEMBUATAN_FROM, col)) {
						sb.append(" 	AND TRUNC(l.CREATION_DATE) >= TO_DATE(:searchCreatedDateFrom, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportLitigationConstant.SEARCH_TANGGAL_PEMBUATAN_TO, col)) {
						sb.append(" 	AND TRUNC(l.CREATION_DATE) <= TO_DATE(:searchCreatedDateTo, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportLitigationConstant.SEARCH_STATUS, col)) {
						sb.append(" 	AND l.ENABLED_FLAG = :searchEnabledFlag ");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void getQueryWhereString(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ReportLitigationConstant.SEARCH_TANGGAL_PEMBUATAN_FROM, col)) {
						query.setParameter("searchCreatedDateFrom", val);
					}
					if (StringUtils.equals(ReportLitigationConstant.SEARCH_TANGGAL_PEMBUATAN_TO, col)) {
						query.setParameter("searchCreatedDateTo", val);
					}
					if (StringUtils.equals(ReportLitigationConstant.SEARCH_STATUS, col)) {
						query.setParameter("searchEnabledFlag", val);
					}
				}
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<ReportLitiPerdataTergugatVo> getReportLitigationPerDataTergugatAsVo(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT l.LITIGATION_ID ");
		sb.append(" 	,l.DIV_NAME_OR_BRANCH_OFFICE AS UNIT_KERJA ");
		sb.append(" 	,l.SEGMENT ");
		sb.append(" 	,lpt.DEBITUR ");
		sb.append(" 	,lpt.CASE_YEAR AS TAHUN_PERKARA ");
		sb.append(" 	,lpt.CASE_NUMBER AS NOMOR_PERKARA ");
		sb.append(" 	,pd1.NAME_IN AS TIPE_PENGADILAN ");
		sb.append(" 	,lpt.COURT_DOMICILE AS DOMISILI_PENGADILAN ");
		sb.append(" 	,lpt.KUASA_HUKUM_PLAINTIFF AS KUASA_HUKUM_PENGGUGAT ");
		sb.append(" 	,pd5.NAME_IN AS KUASA_HUKUM_TERGUGAT ");
		sb.append(" 	,lpt.ATTORNEY_OFFICE_NAME AS NAMA_KANTOR_HUKUM ");
		sb.append(" 	,l.CASE_TEAM_HANDLER AS PENANGANAN_TIM_DTL_CODE ");
		sb.append(" 	,l.PIC_NAME ");
		sb.append(" 	,lpt.CASE_HANDLER ");
		sb.append(" 	,TO_CHAR(lpt.HEARING_DATE,'DD Month YYYY') AS TANGGAL_PANGGILAN_SIDANG ");
		sb.append(" 	,pd3.NAME_IN AS POKOK_PERKARA ");
		sb.append(" 	,pd4.NAME_IN AS SUB_POKOK_PERKARA ");
		sb.append(" 	,lpt.CASE_CLAIMS_POSITION AS KASUS_POSISI_GUGATAN ");
		sb.append(" 	,lpt.MAYBANK_LEGAL_POSITION AS KEDUDUKAN_HUKUM_MAYBANK ");
		sb.append(" 	,lpt.PLAINTIFF_DEMAND AS TUNTUTAN_PENGGUGAT ");
		sb.append(" 	,lpt.CLAIMS_VALUE_MATERIAL_IDR AS NILAI_TUNTUTAN_MATERIAL_IDR ");
		sb.append(" 	,lpt.CLAIMS_VALUE_IMMATERIAL_IDR AS NILAI_TUNTUTAN_IMMATERIAL_IDR ");
		sb.append(" 	,lpt.TOTAL_CLAIMS_VALUE_IDR AS TOTAL_TUNTUTAN ");
		sb.append(" 	,pd6.NAME_IN AS MATA_UANG_VALAS ");
		sb.append(" 	,lpt.CLAIMS_VALUE_MATERIAL_VALAS AS NILAI_TUNTUTAN_MATERIAL_VALAS ");
		sb.append(" 	,lpt.POTENTIAL_LOSS_CESSIE_VALUE AS POTENSI_KERUGIAN_CESSIE ");
		sb.append(" 	,lpt.POTENTIAL_LOSS_OTHERS AS POTENSI_KERUGIAN_LAIN ");
		sb.append(" 	,lpt.COURT_DECISION AS PUTUSAN_PENGADILAN ");
		sb.append(" 	,pd7.PARAMETER_DTL_CODE AS STATUS_PUTUSAN ");
		sb.append(" 	,pil.CASE_ONGOING_PN ");
		sb.append(" 	,pil.CASE_ONGOING_BANI ");
		sb.append(" 	,pil.CASE_ONGOING_PT ");
		sb.append(" 	,pil.CASE_ONGOING_MA_KASASI ");
		sb.append(" 	,pil.CASE_ONGOING_MA_PK ");
		sb.append(" 	,pil.CASE_ONGOING_PTUN ");
		sb.append(" 	,pil.CASE_ONGOING_PA ");
		sb.append(" 	,pil.CASE_ONGOING_PT_AGAMA ");
		sb.append(" 	,pil.TOTAL_ONGOING_CASE ");
		sb.append(" 	,pil.TOTAL_FINISHED_CASE ");
		sb.append(" 	,pil.CASE_FINISHED_PN ");
		sb.append(" 	,pil.CASE_FINISHED_BANI ");
		sb.append(" 	,pil.CASE_FINISHED_PT ");
		sb.append(" 	,pil.CASE_FINISHED_MA_KASASI ");
		sb.append(" 	,pil.CASE_FINISHED_MA_PK ");
		sb.append(" 	,pil.CASE_FINISHED_PTUN ");
		sb.append(" 	,pil.CASE_FINISHED_PA ");
		sb.append(" 	,TO_CHAR(lpt.FINISH_DATE,'DD Month YYYY') AS TANGGAL_SELESAI ");
		sb.append("		,l.PIC_DESCRIPTION ");
		sb.append(" FROM WO_MST_LITIGATION l ");
		sb.append(" 	LEFT JOIN WO_MST_LITI_PERDATA_TERGUGAT lpt ON lpt.LITIGATION_ID = l.LITIGATION_ID ");
		sb.append(" 	LEFT JOIN WO_MST_LITI_PRDT_INSPCT_LVL pil ON pil.LITIGATION_PERDATA_ID = l.LITIGATION_ID ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pd1 ON lpt.COURT_TYPE = pd1.PARAMETER_DTL_CODE ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pd2 ON l.CASE_TEAM_HANDLER = pd2.PARAMETER_DTL_CODE ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pd3 ON lpt.CASE_MAIN_TOPIC = pd3.PARAMETER_DTL_CODE ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pd4 ON lpt.CASE_SUB_TOPIC = pd4.PARAMETER_DTL_CODE ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pd5 ON lpt.CASE_HANDLER = pd5.PARAMETER_DTL_CODE ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pd6 ON lpt.FOREIGN_CURRENCY_TYPE = pd6.PARAMETER_DTL_CODE ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pd7 ON lpt.DECISION_STATUS = pd7.PARAMETER_DTL_CODE ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" 	AND l.CASE_TYPE = 'CASE_TYPE_PERDATA' ");
		sb.append(" 	AND l.CASE_TYPE_DTL = 'CASE_TYPE_DTL_PERDATA_TERGUGAT' ");
		
		this.setQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY l.LITIGATION_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQueryWhereString(query, searchCriteria);
		
		List<Object[]> result = query.getResultList();
		List<ReportLitiPerdataTergugatVo> vo = new ArrayList<>();
		
		if (!result.isEmpty()) {
			for (Object[] obj : result) {
				ReportLitiPerdataTergugatVo data = new ReportLitiPerdataTergugatVo();
				
				data.setTim1("-");
				data.setTim2("-");
				data.setInternal("-");
				data.setLawyer("-");
				data.setStatusPutusanMenang("-");
				data.setStatusPutusanKalah("-");
				data.setStatusPutusanSidang("-");
				
				data.setLitigationId(obj[0] != null ? ((Number) obj[0]).longValue() : null);
				data.setUnitKerja(obj[1] != null ? (String) obj[1] : null);
				data.setSegmen(obj[2] != null ? (String) obj[2] : null);
				data.setDebitur(obj[3] != null ? (String) obj[3] : null);
				data.setTahunPerkara(obj[4] != null ? ((Number) obj[4]).longValue() : null);
				data.setNomorPerkara(obj[5] != null ? (String) obj[5] : null);
				data.setTipePengadilan(obj[6] != null ? (String) obj[6] : null);
				data.setDomisiliPengadilan(obj[7] != null ? (String) obj[7] : null);
				data.setPenggugatKuasaHukum(obj[8] != null ? (String) obj[8] : null);
				data.setTergugatKuasaHukum(obj[9] != null ? (String) obj[9] : null);
				data.setNamaKantorHukum(obj[10] != null ? (String) obj[10] : null);
				if (obj[11] != null) {
					String penangananTimCode = (String) obj[11];
					if (penangananTimCode.equals("CASE_TEAM_HANDLER_1")) data.setTim1(String.valueOf(1));
					if (penangananTimCode.equals("CASE_TEAM_HANDLER_2")) data.setTim2(String.valueOf(1));
				}
				data.setPic(obj[12] != null ? (String) obj[12] : null);
				if (obj[13] != null) {
					String penangananPerkaraCode = (String) obj[13];
					if (penangananPerkaraCode.equals("CASE_HANDLER_INTERNAL")) data.setInternal(String.valueOf(1));;
					if (penangananPerkaraCode.equals("CASE_HANDLER_EXTERNAL")) data.setLawyer(String.valueOf(1));
				}
				data.setTanggalPanggilanSidang(obj[14] != null ? (String) obj[14] : null);
				data.setPokokPerkara(obj[15] != null ? (String) obj[15] : null);
				data.setSubPokokPerkara(obj[16] != null ? (String) obj[16] : null);
				data.setKasusPosisiGugatan(obj[17] != null ? (String) obj[17] : null);
				data.setKedudukanHukumMaybank(obj[18] != null ? (String) obj[18] : null);
				data.setTuntutanPenggugat(obj[19] != null ? (String) obj[19] : null);
				data.setMaterialIDR(obj[20] != null ? ((Number) obj[20]).doubleValue() : null);
				data.setImmaterial(obj[21] != null ? ((Number) obj[21]).doubleValue() : null);
				data.setTotalTuntutan(obj[22] != null ? ((Number) obj[22]).doubleValue() : null);
				data.setMaterialValasTipe(obj[23] != null ? (String) obj[23] : null);
				data.setMaterialValasValue(obj[24] != null ? (String) obj[24] : null);
				data.setTotalNilaiHakTanggungan(obj[25] != null ? ((Number) obj[25]).doubleValue() : null);
				data.setPotensiKerugianLain(obj[26] != null ? ((Number) obj[26]).doubleValue() : null);
				data.setPutusanPengadilan(obj[27] != null ? (String) obj[27] : null);
				if (obj[28] != null) {
					String statusPutusanCode = (String) obj[28];
					if (statusPutusanCode.equals("DECISION_STATUS_WIN")) data.setStatusPutusanMenang(String.valueOf(1));;
					if (statusPutusanCode.equals("DECISION_STATUS_LOSE")) data.setStatusPutusanKalah(String.valueOf(1));
					if (statusPutusanCode.equals("DECISION_STATUS_COURT")) data.setStatusPutusanSidang(String.valueOf(1));
				}
				data.setPerkaraBerjalanPN(obj[29] != null && ((String) obj[29]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraBerjalanBani(obj[30] != null && ((String) obj[30]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraBerjalanPT(obj[31] != null && ((String) obj[31]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraBerjalanMAKasasi(obj[32] != null && ((String) obj[32]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraBerjalanMAPK(obj[33] != null && ((String) obj[33]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraBerjalanPTUN(obj[34] != null && ((String) obj[34]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraBerjalanPA(obj[35] != null && ((String) obj[35]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraBerjalanPTAgama(obj[36] != null && ((String) obj[36]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setTotalPerkaraBerjalan(obj[37] != null ? ((Number) obj[37]).longValue() : null);
				data.setTotalPerkaraSelesai(obj[38] != null ? ((Number) obj[38]).longValue() : null);
				data.setPerkaraSelesaiPN(obj[39] != null && ((String) obj[39]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraSelesaiBani(obj[40] != null && ((String) obj[40]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraSelesaiPT(obj[41] != null && ((String) obj[41]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraSelesaiMAKasasi(obj[42] != null && ((String) obj[42]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraSelesaiMAPK(obj[43] != null && ((String) obj[43]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraSelesaiPTUN(obj[44] != null && ((String) obj[44]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraSelesaiPA(obj[45] != null && ((String) obj[45]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraSelesaiTanggalSelesai(obj[46] != null ? (String) obj[46] : null);
				data.setKeteranganPic(obj[47] != null ? (String) obj[47] : null);
				
				data.setParaPihakPenggugat(getParaPihakPenggugat(data.getLitigationId()));
				data.setParaPihakTergugat(getParaPihakTergugat(data.getLitigationId()));
				data.setParaPihakTurutTergugat(getParaPihakTurutTergugat(data.getLitigationId()));
				data.setProgressTerakhir(getProgressPerkara(data.getLitigationId()));
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<ReportLitiPerdataPenggugatVo> getReportLitigationPerDataPenggugatAsVo(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT l.LITIGATION_ID "); //0
		sb.append(" 	,l.DIV_NAME_OR_BRANCH_OFFICE AS UNIT_KERJA "); //1
		sb.append(" 	,lpp.CASE_YEAR AS TAHUN_PERKARA "); //2
		sb.append(" 	,lpp.CASE_NUMBER AS NOMOR_PERKARA "); //3
		sb.append(" 	,pd1.NAME_IN AS TIPE_PENGADILAN "); //4
		sb.append(" 	,lpp.COURT_DOMICILE AS DOMISILI_PENGADILAN "); //5
		sb.append(" 	,lpp.KUASA_HUKUM_PLAINTIFF AS KUASA_HUKUM_PENGGUGAT "); //6
		sb.append(" 	,pd2.NAME_IN AS KUASA_HUKUM_TERGUGAT "); //7
		sb.append(" 	,lpp.CASE_HANDLER AS KUASA_HUKUM_DTL_CODE "); //8
		sb.append(" 	,l.CASE_TEAM_HANDLER PENANGANAN_PERKARA_DTL_CODE "); //9
		sb.append(" 	,l.PIC_NAME "); //10
		sb.append(" 	,lpp.PLAINTIFF_DEMAND AS TUNTUTAN_PENGGUGAT "); //11
		sb.append(" 	,lpp.CLAIMS_VALUE_MATERIAL_IDR AS NILAI_TUNTUTAN_MATERIAL_IDR "); //12
		sb.append(" 	,lpp.CLAIMS_VALUE_MATERIAL_VALAS AS NILAI_TUNTUTAN_MATERIAL_VALAS "); //13
		sb.append(" 	,lpp.CLAIMS_VALUE_IMMATERIAL_IDR AS NILAI_TUNTUTAN_IMMATERIAL_IDR "); //14
		sb.append(" 	,lpp.TOTAL_CLAIMS_VALUE_IDR AS TOTAL_NILAI_TUNTUTAN "); //15
		sb.append(" 	,lpp.COURT_DECISION AS PUTUSAN_PENGADILAN "); //16
		sb.append(" 	,lpp.INSPCT_LVL_ONGOING_PN "); //17
		sb.append(" 	,lpp.INSPCT_LVL_ONGOING_BANI "); //18
		sb.append(" 	,lpp.INSPCT_LVL_ONGOING_PT "); //19
		sb.append(" 	,lpp.INSPCT_LVL_ONGOING_MA_KASASI "); //20
		sb.append(" 	,lpp.INSPCT_LVL_ONGOING_MA_PK "); //21
		sb.append(" 	,lpp.TOTAL_ONGOING_CASE ");//22
		sb.append(" 	,lpp.TOTAL_FINISHED_CASE ");//23
		sb.append(" 	,lpp.INSPCT_LVL_FINISHED_PN ");//24
		sb.append(" 	,lpp.INSPCT_LVL_FINISHED_BANI ");//25
		sb.append(" 	,lpp.INSPCT_LVL_FINISHED_PT ");//26
		sb.append(" 	,lpp.INSPCT_LVL_FINISHED_MA_KASASI ");//27
		sb.append(" 	,lpp.INSPCT_LVL_FINISHED_MA_PK ");//28
		sb.append(" 	,TO_CHAR(lpp.FINISH_DATE,'DD Month YYYY') AS TANGGAL_SELESAI ");//29
		sb.append("		,pd3.NAME_IN AS MATA_UANG_VALAS ");//30
		sb.append("		,lpp.ATTORNEY_OFFICE_NAME AS NAMA_KANTOR_HUKUM ");//31
		sb.append("		,l.PIC_DESCRIPTION ");
		sb.append(" FROM WO_MST_LITIGATION l ");
		sb.append(" 	LEFT JOIN WO_MST_LITI_PERDATA_PENGGUGAT lpp ON lpp.LITIGATION_ID = l.LITIGATION_ID ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pd1 ON lpp.COURT_TYPE = pd1.PARAMETER_DTL_CODE ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pd2 ON lpp.CASE_HANDLER = pd2.PARAMETER_DTL_CODE ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pd3 ON lpp.FOREIGN_CURRENCY_TYPE = pd3.PARAMETER_DTL_CODE ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pd4 ON lpp.CASE_MAIN_TOPIC = pd4.PARAMETER_DTL_CODE ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pd5 ON lpp.CASE_SUB_TOPIC = pd5.PARAMETER_DTL_CODE ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" 	AND l.CASE_TYPE = 'CASE_TYPE_PERDATA' ");
		sb.append(" 	AND l.CASE_TYPE_DTL = 'CASE_TYPE_DTL_PERDATA_PENGGUGAT' ");
		
		this.setQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY l.LITIGATION_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQueryWhereString(query, searchCriteria);
		
		List<Object[]> result = query.getResultList();
		List<ReportLitiPerdataPenggugatVo> vo = new ArrayList<>();
		
		if (!result.isEmpty()) {
			for (Object[] obj : result) {
				ReportLitiPerdataPenggugatVo data = new ReportLitiPerdataPenggugatVo();
				
				data.setKuasaHukumInternal("-");
				data.setKuasaHukumLawyer("-");
				data.setTim1("-");
				data.setTim2("-");
				
				data.setLitigationId(obj[0] != null ? ((Number) obj[0]).longValue() : null);
				data.setUnitKerja(obj[1] != null ? (String) obj[1] : null);
				data.setTahunPerkara(obj[2] != null ? ((Number) obj[2]).longValue() : null);
				data.setNomorPerkara(obj[3] != null ? (String) obj[3] : null);
				data.setTipePengadilan(obj[4] != null ? (String) obj[4] : null);
				data.setDomisiliPengadilan(obj[5] != null ? (String) obj[5] : null);
				data.setKuasaHukumPenggugat(obj[6] != null ? (String) obj[6] : null);
				data.setKuasaHukumTergugat(obj[7] != null ? (String) obj[7] : null);
				if (obj[8] != null) {
					String penangananPerkaraCode = (String) obj[8];
					if (penangananPerkaraCode.equals("CASE_HANDLER_INTERNAL")) data.setKuasaHukumInternal(String.valueOf(1));;
					if (penangananPerkaraCode.equals("CASE_HANDLER_EXTERNAL")) data.setKuasaHukumLawyer(String.valueOf(1));
				}
				if (obj[9] != null) {
					String penangananTimCode = (String) obj[9];
					if (penangananTimCode.equals("CASE_TEAM_HANDLER_1")) data.setTim1(String.valueOf(1));
					if (penangananTimCode.equals("CASE_TEAM_HANDLER_2")) data.setTim2(String.valueOf(1));
				}
				data.setPic(obj[10] != null ? (String) obj[10] : null);
				data.setTuntutanPenggugat(obj[11] != null ? (String) obj[11] : null);
				data.setMaterialIDR(obj[12] != null ? ((Number) obj[12]).doubleValue() : null);
				data.setMaterialValas(obj[13] != null ? (String) obj[13] : null);
				data.setImmaterial(obj[14] != null ? ((Number) obj[14]).doubleValue() : null);
				data.setTotalTuntutan(obj[15] != null ? ((Number) obj[15]).doubleValue() : null);
				data.setPutusanPengadilan(obj[16] != null ? (String) obj[16] : null);
				data.setPerkaraBerjalanPN(obj[17] != null && ((String) obj[17]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraBerjalanBani(obj[18] != null && ((String) obj[18]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraBerjalanPT(obj[19] != null && ((String) obj[19]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraBerjalanMAKasasi(obj[20] != null && ((String) obj[20]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraBerjalanMAPK(obj[21] != null && ((String) obj[21]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setTotalPerkaraBerjalan(obj[22] != null ? ((Number) obj[22]).longValue() : null);
				data.setTotalPerkaraSelesai(obj[23] != null ? ((Number) obj[23]).longValue() : null);
				data.setPerkaraSelesaiPN(obj[24] != null && ((String) obj[24]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraSelesaiBani(obj[25] != null && ((String) obj[25]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraSelesaiPT(obj[26] != null && ((String) obj[26]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraSelesaiMAKasasi(obj[27] != null && ((String) obj[27]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraSelesaiMAPK(obj[28] != null && ((String) obj[28]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setPerkaraSelesaiTanggalSelesai(obj[29] != null ? (String) obj[29] : null);
				data.setMataUangValas(obj[30] != null ? (String) obj[30] : null);
				data.setNamaKantorHukum(obj[31] != null ? (String) obj[31] : null );
				data.setKeteranganPic(obj[32] != null ? (String) obj[32] : null);
				
				data.setParaPihakPenggugat(getParaPihakPenggugat(data.getLitigationId()));
				data.setParaPihakTergugat(getParaPihakTergugat(data.getLitigationId()));
				data.setParaPihakTurutTergugat(getParaPihakTurutTergugat(data.getLitigationId()));
				data.setProgressTerakhir(getProgressPerkara(data.getLitigationId()));
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<ReportLitiPailitPKPUVo> getReportLitigationKepailitanDanPKPUAsVo(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT lpapk.LITIGATION_ID ");
		sb.append(" 	,l.DIV_NAME_OR_BRANCH_OFFICE AS UNIT_KERJA ");
		sb.append(" 	,l.SEGMENT ");
		sb.append(" 	,lpapk.CASE_YEAR ");
		sb.append(" 	,lpapk.CASE_NUMBER ");
		sb.append(" 	,pd1.NAME_IN AS PENGADILAN_NIAGA ");
		sb.append(" 	,lpapk.INVOICE_VALUE_PKPU AS NILAI_TAGIHAN_PKPU ");
		sb.append(" 	,lpapk.INVOICE_VALUE_PAILIT AS NILAI_TAGIHAN_PAILIT ");
		sb.append(" 	,lpapk.KETERANGAN ");
		sb.append(" 	,lpapk.ATTORNEY_OFFICE_NAME AS KEDUDUKAN_HUKUM_MAYBANK ");
		sb.append(" 	,l.CASE_TEAM_HANDLER AS PENANGANAN_TIM_DTL_CODE ");
		sb.append(" 	,lpapk.CASE_HANDLER AS PENANGANAN_INTERNAL_CODE ");
		sb.append(" 	,l.PIC_NAME ");
		sb.append(" 	,l.PIC_DESCRIPTION ");
		sb.append(" FROM WO_MST_LITIGATION l ");
		sb.append(" 	INNER JOIN WO_MST_LITI_PAILIT_PKPU lpapk ON lpapk.LITIGATION_ID = l.LITIGATION_ID ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pd1 ON lpapk.COMMERCIAL_COURT = pd1.PARAMETER_DTL_CODE ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pd2 ON lpapk.CASE_HANDLER = pd2.PARAMETER_DTL_CODE ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pd3 ON l.CASE_TEAM_HANDLER = pd3.PARAMETER_DTL_CODE ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" 	AND l.CASE_TYPE = 'CASE_TYPE_PAILIT_PKPU' ");
		
		this.setQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY lpapk.LITIGATION_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQueryWhereString(query, searchCriteria);
		
		List<Object[]> result = query.getResultList();
		List<ReportLitiPailitPKPUVo> vo = new ArrayList<>();
		
		if (!result.isEmpty()) {
			for (Object[] obj : result) {
				ReportLitiPailitPKPUVo data = new ReportLitiPailitPKPUVo();
				
				data.setTim1("-");
				data.setTim2("-");
				data.setInternal("-");
				data.setLawyer("-");
				
				data.setLitigationId(obj[0] != null ? ((Number) obj[0]).longValue() : null);
				data.setCabang(obj[1] != null ? (String) obj[1] : null);
				data.setSegmen(obj[2] != null ? (String) obj[2] : null);
				data.setTahunPerkara(obj[3] != null ? ((Number) obj[3]).longValue() : null);
				data.setNomorPerkara(obj[4] != null ? (String) obj[4] : null);
				data.setPengadilanNiaga(obj[5] != null ? (String) obj[5] : null);
				data.setNilaiTagihanMaybankPKPU(obj[6] != null ? ((Number) obj[6]).doubleValue() : null);
				data.setNilaiTagihanMaybankKepailitan(obj[7] != null ? ((Number) obj[7]).doubleValue() : null);
				data.setKeterangan(obj[8] != null ? (String) obj[8] : null);
				data.setKuasaHukumMaybank(obj[9] != null ? (String) obj[9] : null);
				if (obj[10] != null) {
					String penangananTimCode = (String) obj[10];
					if (penangananTimCode.equals("CASE_TEAM_HANDLER_1")) data.setTim1(String.valueOf(1));
					if (penangananTimCode.equals("CASE_TEAM_HANDLER_2")) data.setTim2(String.valueOf(1));
				}
				if (obj[11] != null) {
					String penangananPerkaraCode = (String) obj[11];
					if (penangananPerkaraCode.equals("CASE_HANDLER_INTERNAL")) data.setInternal(String.valueOf(1));;
					if (penangananPerkaraCode.equals("CASE_HANDLER_EXTERNAL")) data.setLawyer(String.valueOf(1));
				}
				data.setPic(obj[12] != null ? (String) obj[12] : null);
				data.setKeteranganPic(obj[13] != null ? (String) obj[13] : null);
				
				data.setParaPihakPenggugat(getParaPihakPenggugat(data.getLitigationId()));
				data.setParaPihakTergugat(getParaPihakTergugat(data.getLitigationId()));
				data.setAmarPutusanPengadilan(getAmarPutusanPengadilan(data.getLitigationId()));
				data.setTanggalPutusanPengadilan(getTanggalPutusanPengadilan(data.getLitigationId()));
				data.setKurator(getKurator(data.getLitigationId()));
				data.setPerkembanganTerakhir(getProgressPerkara(data.getLitigationId()));
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<ReportLitiPidanaPelaporVo> getReportLitigationPidanaPelaporAsVo(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT l.LITIGATION_ID "); //0
		sb.append(" 	,l.DIV_NAME_OR_BRANCH_OFFICE AS CABANG "); //1
		sb.append(" 	,l.SEGMENT "); //2
		sb.append(" 	,lpip.CASE_YEAR AS TAHUN_PERKARA "); //3
		sb.append(" 	,TO_CHAR(lpip.REPORT_DATE,'DD Month YYYY') AS TANGGAL_LAPORAN "); //4
		sb.append(" 	,lpip.CASE_NUMBER AS NOMOR_LAPORAN "); //5
		sb.append(" 	,lpip.EXAMINING_AGENCY AS INSTANSI_PEMERIKSA "); //6
		sb.append(" 	,l.CASE_TEAM_HANDLER AS PENANGANAN_TIM_DTL_CODE "); //7
		sb.append(" 	,lpip.CASE_POSITION AS KASUS_POSISI "); //8
		sb.append(" 	,lpip.CASE_VALUE_IDR AS NILAI_PERKARA_IDR "); //9
		sb.append(" 	,lpip.CASE_VALUE_VALAS AS NILAI_PERKARA_VALAS "); //10
		sb.append(" 	,lpip.INSPECT_LEVEL_POL ");
		sb.append(" 	,lpip.INSPECT_LEVEL_JAKSA ");
		sb.append(" 	,lpip.INSPECT_LEVEL_PN AS INSPECT_LEVEL_PENGADILAN ");
		sb.append(" 	,lpip.INSPECT_LEVEL_FINISHED ");
		sb.append(" 	,lpip.ATTORNEY_OFFICE_NAME AS KUASA_HUKUM_MAYBANK ");
		sb.append(" 	,lpip.CASE_HANDLER AS HANDLER_DTL_CODE ");
		sb.append("		,l.PIC_NAME ");
		sb.append("		,pd3.NAME_IN AS MATA_UANG_VALAS ");
		sb.append("		,lpip.TINDAK_PIDANA ");
		sb.append(" 	,l.PIC_DESCRIPTION ");
		sb.append(" FROM WO_MST_LITIGATION l ");
		sb.append(" 	LEFT JOIN WO_MST_LITI_PIDANA_PELAPOR lpip ON lpip.LITIGATION_ID = l.LITIGATION_ID ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pd1 ON l.CASE_TEAM_HANDLER = pd1.PARAMETER_DTL_CODE ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pd2 ON lpip.CASE_HANDLER = pd2.PARAMETER_DTL_CODE ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pd3 ON lpip.FOREIGN_CURRENCY = pd3.PARAMETER_DTL_CODE ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" 	AND l.CASE_TYPE = 'CASE_TYPE_PIDANA' ");
		sb.append(" 	AND l.CASE_TYPE_DTL = 'CASE_TYPE_DTL_PIDANA_PELAPOR' ");
		
		this.setQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY l.LITIGATION_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQueryWhereString(query, searchCriteria);
		
		List<Object[]> result = query.getResultList();
		List<ReportLitiPidanaPelaporVo> vo = new ArrayList<>();
		
		if (!result.isEmpty()) {
			for (Object[] obj : result) {
				ReportLitiPidanaPelaporVo data = new ReportLitiPidanaPelaporVo();
				
				data.setTim1("-");
				data.setTim2("-");
				data.setInternal("-");
				data.setLawyer("-");
				
				data.setLitigationId(obj[0] != null ? ((Number) obj[0]).longValue() : null);
				data.setCabang(obj[1] != null ? (String) obj[1] : null);
				data.setSegmen(obj[2] != null ? (String) obj[2] : null);
				data.setTahunPerkara(obj[3] != null ? ((Number) obj[3]).longValue() : null);
				data.setTanggalLaporan(obj[4] != null ? (String) obj[4] : null);
				data.setNomorLaporan(obj[5] != null ? (String) obj[5] : null);
				data.setInstansiPemeriksa(obj[6] != null ? (String) obj[6] : null);
				if (obj[7] != null) {
					String penangananTimCode = (String) obj[7];
					if (penangananTimCode.equals("CASE_TEAM_HANDLER_1")) data.setTim1(String.valueOf(1));
					if (penangananTimCode.equals("CASE_TEAM_HANDLER_2")) data.setTim2(String.valueOf(1));
				}
				data.setKasusPosisi(obj[8] != null ? (String) obj[8] : null);
				data.setNilaiPerkaraIDR(obj[9] != null ? ((Number) obj[9]).doubleValue() : null);
				data.setNilaiPerkaraValas(obj[10] != null ? (String) obj[10] : null);
				data.setTingkatPemeriksaanPol(obj[11] != null && ((String) obj[11]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setTingkatPemeriksaanJaksa(obj[12] != null && ((String) obj[12]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setTingkatPemeriksaanPN(obj[13] != null && ((String) obj[13]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setTingkatPemeriksaanSelesai(obj[14] != null && ((String) obj[14]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setKuasaHukumMaybank(obj[15] != null ? (String) obj[15] : null);
				if (obj[16] != null) {
					String penangananPerkaraCode = (String) obj[16];
					if (penangananPerkaraCode.equals("CASE_HANDLER_INTERNAL")) data.setInternal(String.valueOf(1));;
					if (penangananPerkaraCode.equals("CASE_HANDLER_EXTERNAL")) data.setLawyer(String.valueOf(1));
				}
				data.setNamaPic(obj[17] != null ? (String) obj[17] : null);
				data.setMataUangValas(obj[18] != null ? (String) obj[18] : null);
				data.setTindakPidana(obj[19] != null ? (String) obj[19] : null);
				data.setKeteranganPic(obj[20] != null ? (String) obj[20] : null);
				
				data.setPelapor(getParaPihakPenggugat(data.getLitigationId()));
				data.setTerlapor(getParaPihakTergugat(data.getLitigationId()));
				data.setPerkembanganPerkara(getProgressPerkara(data.getLitigationId()));
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<ReportLitiPidanaTerlaporVo> getReportLitigationPidanaTerlaporAsVo(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();

		sb.append(" SELECT l.LITIGATION_ID ");
		sb.append(" 	,l.DIV_NAME_OR_BRANCH_OFFICE AS UNIT_KERJA ");
		sb.append(" 	,lpit.CASE_YEAR AS TAHUN_PERKARA ");
		sb.append(" 	,lpit.CASE_NUMBER AS NOMOR_LAPORAN ");
		sb.append(" 	,lpit.EXAMINING_AGENCY AS INSTANSI_PEMERIKSA ");
		sb.append(" 	,l.CASE_TEAM_HANDLER AS TEAM_DTL_CODE ");
		sb.append(" 	,lpit.CASE_POSITION AS KASUS_POSISI ");
		sb.append(" 	,lpit.CASE_VALUE_IDR AS NILAI_PERKARA_IDR ");
		sb.append(" 	,pd3.NAME_IN AS MATA_UANG_VALAS ");
		sb.append(" 	,lpit.INSPECT_LEVEL_POL ");
		sb.append(" 	,lpit.INSPECT_LEVEL_PN ");
		sb.append(" 	,lpit.INSPECT_LEVEL_FINISHED ");
		sb.append(" 	,lpit.ATTORNEY_OFFICE_NAME AS KUASA_HUKUM_MAYBANK ");
		sb.append(" 	,lpit.CASE_HANDLER HANDLER_DTL_CODE ");
		sb.append("		,l.PIC_NAME ");
		sb.append("		,lpit.CASE_VALUE_VALAS AS NILAI_PERKARA_VALAS ");
		sb.append("		,lpit.TINDAK_PIDANA ");
		sb.append(" 	,lpit.INSPECT_LEVEL_JAKSA ");
		sb.append("		,l.PIC_DESCRIPTION ");
		sb.append(" FROM WO_MST_LITIGATION l ");
		sb.append(" 	LEFT JOIN WO_MST_LITI_PIDANA_TERLAPOR lpit ON lpit.LITIGATION_ID = l.LITIGATION_ID ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pd1 ON l.CASE_TEAM_HANDLER = pd1.PARAMETER_DTL_CODE ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pd2 ON lpit.CASE_HANDLER = pd2.PARAMETER_DTL_CODE ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pd3 ON lpit.FOREIGN_CURRENCY = pd3.PARAMETER_DTL_CODE ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" 	AND l.CASE_TYPE = 'CASE_TYPE_PIDANA' ");
		sb.append(" 	AND l.CASE_TYPE_DTL = 'CASE_TYPE_DTL_PIDANA_TERLAPOR' ");
		
		this.setQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY l.LITIGATION_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQueryWhereString(query, searchCriteria);
		
		List<Object[]> result = query.getResultList();
		List<ReportLitiPidanaTerlaporVo> vo = new ArrayList<>();
		
		if (!result.isEmpty()) {
			for (Object[] obj : result) {
				ReportLitiPidanaTerlaporVo data = new ReportLitiPidanaTerlaporVo();
				
				data.setTim1("-");
				data.setTim2("-");
				data.setInternal("-");
				data.setLawyer("-");
				
				data.setLitigationId(obj[0] != null ? ((Number) obj[0]).longValue() : null);
				data.setUnitKerja(obj[1] != null ? (String ) obj[1] : null);
				data.setTahunPerkara(obj[2] != null ? ((Number) obj[2]).longValue() : null);
				data.setNomorPerkara(obj[3] != null ? (String) obj[3] : null);
				data.setInstansiPemeriksa(obj[4] != null ? (String) obj[4] : null);
				if (obj[5] != null) {
					String penangananTimCode = (String) obj[5];
					if (penangananTimCode.equals("CASE_TEAM_HANDLER_1")) data.setTim1(String.valueOf(1));
					if (penangananTimCode.equals("CASE_TEAM_HANDLER_2")) data.setTim2(String.valueOf(1));
				}
				data.setKasusPosisi(obj[6] != null ? (String) obj[6] : null);
				data.setNilaiPerkaraIDR(obj[7] != null ? ((Number) obj[7]).doubleValue() : null);
				data.setMataUangValas(obj[8] != null ? (String) obj[8] : null);
				data.setTingkatPemeriksaanPol(obj[9] != null && ((String) obj[9]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setTingkatPemeriksaanPN(obj[10] != null && ((String) obj[10]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setTingkatPemeriksaanSelesai(obj[11] != null && ((String) obj[11]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setKuasaHukumMaybank(obj[12] != null ? (String) obj[12] : null);
				if (obj[13] != null) {
					String penangananPerkaraCode = (String) obj[13];
					if (penangananPerkaraCode.equals("CASE_HANDLER_INTERNAL")) data.setInternal(String.valueOf(1));;
					if (penangananPerkaraCode.equals("CASE_HANDLER_EXTERNAL")) data.setLawyer(String.valueOf(1));
				}
				data.setNamaPic(obj[14] != null ? (String) obj[14] : null);
				data.setNilaiPerkaraValas(obj[15] != null ? (String) obj[15] : null);
				data.setTindakPidana(obj[16] != null ? (String) obj[16] : null);
				data.setTingkatPemeriksaanJaksa(obj[17] != null && ((String) obj[17]).equals(Constants.CONSTANT_YES) ? String.valueOf(1) : "-");
				data.setKeteranganPic(obj[18] != null ? (String) obj[18] : null);
				
				data.setPelapor(getParaPihakPenggugat(data.getLitigationId()));
				data.setTerlapor(getParaPihakTergugat(data.getLitigationId()));
				data.setPerkembanganPerkara(getProgressPerkara(data.getLitigationId()));
				
				vo.add(data);
			}
		}
		
		return vo;
	}
	
	@SuppressWarnings("unchecked")
	private StringBuilder getParaPihakPenggugat(Long litigationId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT WMLPP.LITI_PIHAK_P_ID ");
		sb.append(" 	,WMLPP.PENGGUGAT_PEMOHON ");
		sb.append(" FROM WO_MST_LITI_PIHAK_P wmlpp ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" 	AND WMLPP.ENABLED_FLAG = 'Y' ");
		sb.append(" 	AND WMLPP.LITIGATION_ID = :litigationId ");
		sb.append(" ORDER BY WMLPP.LITI_PIHAK_P_ID asc ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("litigationId", litigationId);
		
		List<Object[]> result = query.getResultList();
		StringBuilder vo = new StringBuilder();
		
		if (!result.isEmpty()) {
			int idx = 1;
			for (Object[] obj : result) {
				String str = obj[1] != null ? (String) obj[1] : null;
				if (idx == 1) {
					vo.append(str);
				} else {
					vo.append("\n");
					vo.append(str);
				}
				idx++;
			}
		}
		return vo;
	}
	
	@SuppressWarnings("unchecked")
	private StringBuilder getParaPihakTergugat(Long litigationId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT WMLPT.LITI_PIHAK_T_ID ");
		sb.append(" 	,WMLPT.TERGUGAT_TERMOHON ");
		sb.append(" FROM WO_MST_LITI_PIHAK_T wmlpt ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" 	AND WMLPT.ENABLED_FLAG = 'Y' ");
		sb.append(" 	AND WMLPT.LITIGATION_ID = :litigationId ");
		sb.append(" ORDER BY WMLPT.LITI_PIHAK_T_ID asc ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("litigationId", litigationId);
		
		List<Object[]> result = query.getResultList();
		StringBuilder vo = new StringBuilder();
		
		if (!result.isEmpty()) {
			int idx = 1;
			for (Object[] obj : result) {
				String str = obj[1] != null ? (String) obj[1] : null;
				if (idx == 1) {
					vo.append(str);
				} else {
					vo.append("\n");
					vo.append(str);
				}
				idx++;
			}
		}
		return vo;
	}
	
	@SuppressWarnings("unchecked")
	private StringBuilder getParaPihakTurutTergugat(Long litigationId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT wmlpk.LITI_PIHAK_KTT_ID ");
		sb.append(" 	,wmlpk.KURATOR_TURUT_TERGUGAT ");
		sb.append(" FROM WO_MST_LITI_PIHAK_KTT wmlpk ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" 	AND wmlpk.ENABLED_FLAG = 'Y' ");
		sb.append(" 	AND wmlpk.LITIGATION_ID = :litigationId ");
		sb.append(" ORDER BY wmlpk.LITI_PIHAK_KTT_ID ASC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("litigationId", litigationId);
		
		List<Object[]> result = query.getResultList();
		StringBuilder vo = new StringBuilder();
		
		if (!result.isEmpty()) {
			int idx = 1;
			for (Object[] obj : result) {
				String str = obj[1] != null ? (String) obj[1] : null;
				if (idx == 1) {
					vo.append(str);
				} else {
					vo.append("\n");
					vo.append(str);
				}
				idx++;
			}
		}
		return vo;
	}
	
	@SuppressWarnings("unchecked")
	private StringBuilder getAmarPutusanPengadilan(Long litigationId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT wmlpp.LITI_PUTUSAN_PNGADILAN_ID ");
		sb.append(" 	,wmpd.NAME_IN AS AMAR_PENGADILAN ");
		sb.append(" FROM WO_MST_LITI_PTUSAN_PNGADILAN wmlpp ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL wmpd ON wmpd.PARAMETER_DTL_CODE = wmlpp.JUDGEMENT_WARNING ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" 	AND wmlpp.ENABLED_FLAG = 'Y' ");
		sb.append(" 	AND WMLPP.LITIGATION_ID = :litigationId ");
		sb.append(" ORDER BY wmlpp.LITI_PUTUSAN_PNGADILAN_ID asc ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("litigationId", litigationId);
		
		List<Object[]> result = query.getResultList();
		StringBuilder vo = new StringBuilder();
		
		if (!result.isEmpty()) {
			int idx = 1;
			for (Object[] obj : result) {
				String str = obj[1] != null ? (String) obj[1] : null;
				if (idx == 1) {
					vo.append(str);
				} else {
					vo.append("\n");
					vo.append(str);
				}
				idx++;
			}
		}
		return vo;
	}
	
	@SuppressWarnings("unchecked")
	private StringBuilder getTanggalPutusanPengadilan(Long litigationId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT wmlpp.LITI_PUTUSAN_PNGADILAN_ID ");
		sb.append(" 	,TO_CHAR(wmlpp.HEARING_DATE,'DD Month YYYY') AS TANGGAL_LAPORAN ");
		sb.append(" FROM WO_MST_LITI_PTUSAN_PNGADILAN wmlpp ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL wmpd ON wmpd.PARAMETER_DTL_CODE = wmlpp.JUDGEMENT_WARNING ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" 	AND wmlpp.ENABLED_FLAG = 'Y' ");
		sb.append(" 	AND WMLPP.LITIGATION_ID = :litigationId ");
		sb.append(" ORDER BY wmlpp.LITI_PUTUSAN_PNGADILAN_ID asc ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("litigationId", litigationId);
		
		List<Object[]> result = query.getResultList();
		StringBuilder vo = new StringBuilder();
		
		if (!result.isEmpty()) {
			int idx = 1;
			for (Object[] obj : result) {
				String str = obj[1] != null ? (String) obj[1] : null;
				if (idx == 1) {
					vo.append(str);
				} else {
					vo.append("\n");
					vo.append(str);
				}
				idx++;
			}
		}
		return vo;
	}
	
	@SuppressWarnings("unchecked")
	private StringBuilder getKurator(Long litigationId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT wmlpk.LITI_PIHAK_KTT_ID ");
		sb.append(" 	,wmlpk.KURATOR_TURUT_TERGUGAT ");
		sb.append(" FROM WO_MST_LITI_PIHAK_KTT wmlpk ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" 	AND wmlpk.ENABLED_FLAG = 'Y' ");
		sb.append(" 	AND wmlpk.LITIGATION_ID = :litigationId ");
		sb.append(" ORDER BY wmlpk.LITI_PIHAK_KTT_ID asc ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("litigationId", litigationId);
		
		List<Object[]> result = query.getResultList();
		StringBuilder vo = new StringBuilder();
		
		if (!result.isEmpty()) {
			int idx = 1;
			for (Object[] obj : result) {
				String str = obj[1] != null ? (String) obj[1] : null;
				if (idx == 1) {
					vo.append(str);
				} else {
					vo.append("\n");
					vo.append(str);
				}
				idx++;
			}
		}
		return vo;
	}
	
	@SuppressWarnings("unchecked")
	private StringBuilder getProgressPerkara(Long litigationId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT wmlpp.LITI_PROGRESS_PERKARA_ID ");
		sb.append(" 	,wmlpp.PROGRESS_DESC ");
		sb.append(" 	,TO_CHAR(wmlpp.PROGRESS_DATE,'DD Month YYYY') AS PROGRESS_DATE ");
		sb.append(" FROM WO_MST_LITI_PROGRESS_PERKARA wmlpp ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" 	AND wmlpp.ENABLED_FLAG = 'Y' ");
		sb.append(" 	AND wmlpp.LITIGATION_ID = :litigationId ");
		sb.append(" ORDER BY wmlpp.LITI_PROGRESS_PERKARA_ID asc ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("litigationId", litigationId);
		
		List<Object[]> result = query.getResultList();
		StringBuilder vo = new StringBuilder();
		
		if (!result.isEmpty()) {
			int idx = 1;
			for (Object[] obj : result) {
				String str = obj[1] != null ? (String) obj[1] : null;
				String strDate = obj[2] != null ? (String) obj[2] : null;
				
				if (StringUtils.isNotBlank(strDate)) strDate = strDate + " : ";
				
				if (idx == 1) {
					vo.append(strDate);
					vo.append(str);
				} else {
					vo.append("\n");
					vo.append(strDate);
					vo.append(str);
				}
				idx++;
			}
		}
		return vo;
	}
	
}