package com.wo.module.litigationView.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.litigation.constant.LitigationConstants;
import com.wo.module.litigationView.vo.LitigationViewDetailVo;
import com.wo.module.litigationView.vo.LitigationViewViewerVo;
import com.wo.module.litigationView.vo.LitigationViewVo;

@Repository("litigationViewDao")
public class LitigationViewDaoImpl extends GenericDAOHibernate<Object, Long> implements LitigationViewDao, Serializable{

	private static final long serialVersionUID = -5575366283384845105L;

	@SuppressWarnings("rawtypes")
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(LitigationConstants.SEARCH_BY_JENIS_PERKARA, col)) {
						sb.append(" and UPPER(ln.case_type) = UPPER(:caseType) ");
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_DIV_NAME_OR_BRANCH_OFFICE, col)) {
						sb.append(" and UPPER(ln.DIV_NAME_OR_BRANCH_OFFICE) LIKE UPPER(:divNameBranchOff) ");
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_SEGMENT, col)) {
						sb.append(" and UPPER(ln.SEGMENT) LIKE UPPER(:segment) ");
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_ACTIVE_STATUS, col)) {
						sb.append(" and ln.ENABLED_FLAG = :enabledFlag ");
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_DEBTOR, col)) {
						sb.append(" AND UPPER(lpt.DEBITUR) LIKE UPPER(:debtor) ");
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_CASE_NUMBER, col)) {
						sb.append(" AND (UPPER(lpp.CASE_NUMBER) LIKE UPPER(:caseNumber) ");
						sb.append(" 	OR UPPER(lpt.CASE_NUMBER) LIKE UPPER(:caseNumber) ");
						sb.append(" 	OR UPPER(lpapk.CASE_NUMBER) LIKE UPPER(:caseNumber)) ");
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_REPORT_NUMBER, col)) {
						sb.append(" AND (UPPER(lppr.CASE_NUMBER) LIKE UPPER(:reportNumber) ");
						sb.append(" 	OR UPPER(lptr.CASE_NUMBER) LIKE UPPER(:reportNumber)) ");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void getQuerySetValue(Query query, List<? extends SearchObject> searchCriteria) {
		
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();

				if (!StringUtils.isBlank(val)) {					
					if (StringUtils.equals(LitigationConstants.SEARCH_BY_JENIS_PERKARA, col)) {
						query.setParameter("caseType", val);
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_DIV_NAME_OR_BRANCH_OFFICE, col)) {
						query.setParameter("divNameBranchOff", "%"+val+"%");
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_SEGMENT, col)) {
						query.setParameter("segment", "%"+val+"%");
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_ACTIVE_STATUS, col)) {
						query.setParameter("enabledFlag", val);
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_DEBTOR, col)) {
						query.setParameter("debtor", "%"+val+"%");
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_CASE_NUMBER, col)) {
						query.setParameter("caseNumber", "%"+val+"%");
					}
					
					if(StringUtils.equals(LitigationConstants.SEARCH_BY_REPORT_NUMBER, col)) {
						query.setParameter("reportNumber", "%"+val+"%");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<LitigationViewVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		List<LitigationViewVo> vo = searchDataCriteria(searchCriteria,first,pageSize);
		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	public List<LitigationViewVo> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize){
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT ln.LITIGATION_ID, "
				+ "	pd1.NAME_IN AS CASE_TYPE_NAME, "
				+ "	pd2.NAME_IN AS CASE_TYPE_DTL_NAME, "
				+ "	ln.DIV_NAME_OR_BRANCH_OFFICE, "
				+ "	ln.SEGMENT ");
		sb.append(" 	,COALESCE(wmu.name, wmu2.name) pic_created_updated ");
		sb.append(" 	,ln.DESCRIPTION ");
		sb.append(" ,ln.ENABLED_FLAG, ");
		sb.append(" lpt.DEBITUR, ");
		sb.append(" CASE "
				+ "		WHEN lpp.CASE_NUMBER IS NOT NULL "
				+ "		THEN lpp.CASE_NUMBER "
				+ "		WHEN lpt.CASE_NUMBER IS NOT NULL "
				+ "		THEN lpt.CASE_NUMBER "
				+ "		WHEN lpapk.CASE_NUMBER IS NOT NULL "
				+ "		THEN lpapk.CASE_NUMBER "
				+ " END AS NOMOR_PERKARA, ");
		sb.append(" CASE "
				+ "		WHEN lppr.CASE_NUMBER IS NOT NULL "
				+ "		THEN lppr.CASE_NUMBER "
				+ "		WHEN lptr.CASE_NUMBER IS NOT NULL "
				+ "		THEN lptr.CASE_NUMBER "
				+ " END AS NOMOR_LAPORAN ");
		sb.append(" FROM WO_MST_LITIGATION ln ");
		sb.append(" INNER JOIN WO_MST_PARAMETER_DTL pd1 ON pd1.PARAMETER_DTL_CODE = ln.CASE_TYPE ");
		sb.append(" LEFT JOIN WO_MST_PARAMETER_DTL pd2 ON pd2.PARAMETER_DTL_CODE = ln.CASE_TYPE_DTL ");
		sb.append(" left join wo_mst_user wmu on wmu.nik = ln.last_update_by ");
		sb.append(" left join wo_mst_user wmu2 on wmu2.nik = ln.created_by ");
		sb.append(" LEFT JOIN WO_MST_LITI_PERDATA_TERGUGAT lpt ON lpt.LITIGATION_ID = ln.LITIGATION_ID  ");
		sb.append(" LEFT JOIN WO_MST_LITI_PERDATA_PENGGUGAT lpp ON lpp.LITIGATION_ID = ln.LITIGATION_ID ");
		sb.append(" LEFT JOIN WO_MST_LITI_PAILIT_PKPU lpapk ON lpapk.LITIGATION_ID = ln.LITIGATION_ID ");
		sb.append(" LEFT JOIN WO_MST_LITI_PIDANA_PELAPOR lppr ON lppr.LITIGATION_ID = ln.LITIGATION_ID ");
		sb.append(" LEFT JOIN WO_MST_LITI_PIDANA_TERLAPOR lptr ON lptr.LITIGATION_ID = ln.LITIGATION_ID ");
		sb.append(" WHERE 1 = 1 ");
		
		this.getQueryWhereString(sb, searchCriteria);
		
		sb.append(" ORDER BY ln.LITIGATION_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQuerySetValue(query, searchCriteria);
		
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		List result = query.getResultList();
		List<LitigationViewVo> vo = new ArrayList<LitigationViewVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				LitigationViewVo data = new LitigationViewVo();
				
				data.setLitigationId(MathUtil.returnIdObjectToLong(obj[0]));
				data.setCaseTypeName(obj[1].toString());
				if(obj[2] != null) {
					data.setCaseTypeDtlName(obj[2].toString());
				}else {
					data.setCaseTypeDtlName("");
				}
				data.setDivisionOrBranchOffice(obj[3].toString());
				data.setSegment(obj[4].toString());	
				data.setCreatedUpdatePic(obj[5] != null ? (String) obj[5] : null);
				data.setNote(obj[6] != null ? (String) obj[6] : null);
				
				String activeStatusStr = "Tidak Aktif";
				if(obj[7] != null) {
					if(obj[7].toString().equals(CommonConstants.RECORD_FLAG_YES)){
						activeStatusStr = "Aktif";
					}
				}
				
				data.setDebtor(obj[8] != null ? (String) obj[8] : null);
				data.setCaseNumber(obj[9] != null ? (String) obj[9] : null);
				data.setReportNumber(obj[10] != null ? (String) obj[10] : null);
				
				data.setActiveStatus(activeStatusStr);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		
		Number result = searchCountDataCriteria(searchCriteria);
		if (result == null) {
			result = 0;
		}
		
		return result.longValue();
	}
	
	@SuppressWarnings("rawtypes")
	private Number searchCountDataCriteria(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT COUNT(1) ");
		sb.append(" FROM WO_MST_LITIGATION ln ");
		sb.append(" INNER JOIN WO_MST_PARAMETER_DTL pd1 ON pd1.PARAMETER_DTL_CODE = ln.CASE_TYPE ");
		sb.append(" LEFT JOIN WO_MST_PARAMETER_DTL pd2 ON pd2.PARAMETER_DTL_CODE = ln.CASE_TYPE_DTL ");
		sb.append(" LEFT JOIN WO_MST_LITI_PERDATA_TERGUGAT lpt ON lpt.LITIGATION_ID = ln.LITIGATION_ID  ");
		sb.append(" LEFT JOIN WO_MST_LITI_PERDATA_PENGGUGAT lpp ON lpp.LITIGATION_ID = ln.LITIGATION_ID ");
		sb.append(" LEFT JOIN WO_MST_LITI_PAILIT_PKPU lpapk ON lpapk.LITIGATION_ID = ln.LITIGATION_ID ");
		sb.append(" LEFT JOIN WO_MST_LITI_PIDANA_PELAPOR lppr ON lppr.LITIGATION_ID = ln.LITIGATION_ID ");
		sb.append(" LEFT JOIN WO_MST_LITI_PIDANA_TERLAPOR lptr ON lptr.LITIGATION_ID = ln.LITIGATION_ID ");
		sb.append(" WHERE 1 = 1 ");
		
		this.getQueryWhereString(sb, searchCriteria);
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQuerySetValue(query, searchCriteria);
		
		Number result = (Number) query.getSingleResult();
		
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public LitigationViewVo getData(Long litigationId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT l.LITIGATION_ID LITIGATION_ID ");
		sb.append("       ,l.CASE_TYPE CASE_TYPE_CODE ");
		sb.append("       ,pdCaseType.NAME_IN CASE_TYPE_IN ");
		sb.append("       ,pdCaseType.NAME_EN CASE_TYPE_EN ");
		sb.append("       ,l.BRANCH_OFFICE BRANCH_OFFICE_CODE ");
		sb.append("       ,pdBranchOffice.NAME_IN BRANCH_OFFICE_IN ");
		sb.append("       ,pdBranchOffice.NAME_EN BRANCH_OFFICE_EN ");
		sb.append("       ,l.LITIGATION_NO LITIGATION_NO ");
		sb.append("       ,l.LAWSUIT LAWSUIT ");
		sb.append("       ,l.LITIGATION_PLACE LITIGATION_PLACE_CODE ");
		sb.append("       ,pdLitigationPlace.NAME_IN LITIGATION_PLACE_IN ");
		sb.append("       ,pdLitigationPlace.NAME_EN LITIGATION_PLACE_EN ");
		sb.append("       ,l.CHARGES ");
		sb.append("       ,l.MATERIAL_CHARGES ");
		sb.append("       ,l.IMMATERIAL_CHARGES ");
		sb.append("       ,l.DEBITUR ");
		sb.append(" FROM WO_MST_LITIGATION l ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdCaseType ");
		sb.append("         ON pdCaseType.PARAMETER_DTL_CODE = l.CASE_TYPE ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdBranchOffice ");
		sb.append("         ON pdBranchOffice.PARAMETER_DTL_CODE = l.BRANCH_OFFICE ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdLitigationPlace ");
		sb.append("         ON pdLitigationPlace.PARAMETER_DTL_CODE = l.LITIGATION_PLACE ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND l.LITIGATION_ID = :litigationId ");
		sb.append("     AND l.ENABLED_FLAG <> 'N' ");
		
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("litigationId", litigationId); 
		
		List result = query.getResultList();
		LitigationViewVo vo = new LitigationViewVo();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				
				vo.setLitigationId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
//				vo.setJenisPerkaraCode(obj[1] != null ? (String) obj[1] : null);
//				vo.setJenisPerkaraIn(obj[2] != null ? (String) obj[2] : null);
//				vo.setJenisPerkaraEn(obj[3] != null ? (String) obj[3] : null);
//				vo.setKantorCabangCode(obj[4] != null ? (String) obj[4] : null);
//				vo.setKantorCabangIn(obj[5] != null ? (String) obj[5] : null);
//				vo.setKantorCabangEn(obj[6] != null ? (String) obj[6] : null);
//				vo.setNoPerkara(obj[7] != null ? (String) obj[7] : null);
//				vo.setJenisGugatan(obj[8] != null ? (String) obj[8] : null);
//				vo.setDaerahPerkaraCode(obj[9] != null ? (String) obj[9] : null);
//				vo.setDaerahPerkaraIn(obj[10] != null ? (String) obj[10] : null);
//				vo.setDaerahPerkaraEn(obj[11] != null ? (String) obj[11] : null);
////				vo.setJenisPerkaraCode(obj[12] != null ? (String) obj[12] : null);
//				vo.setMaterial(obj[13] != null ? MathUtil.returnIdObjectToLong(obj[13]) : null);
//				vo.setImmaterial(obj[14] != null ? MathUtil.returnIdObjectToLong(obj[14]) : null);
//				vo.setDebitur(obj[15] != null ? (String) obj[15] : null);
//				
//				if (obj[0] != null) {
//					vo.setLitigationViewerVoList(getLitigationViewerList(MathUtil.returnIdObjectToLong(obj[0])));
//					vo.setLitigationDetailVoList(getLitigationDetailList(MathUtil.returnIdObjectToLong(obj[0])));
//				} else {
//					vo.setLitigationViewerVoList(new ArrayList<LitigationViewViewerVo>());
//					vo.setLitigationDetailVoList(new ArrayList<LitigationViewDetailVo>());
//				}
				
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<LitigationViewViewerVo> getLitigationViewerList(Long litigationId){
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT lv.LITIGATION_VIEWER_ID VIEWER_ID ");
		sb.append("       ,u.NIK EMPLOYEE_NUMBER ");
		sb.append("       ,u.NAME EMPLOYEE_NAME ");
		sb.append("       ,lv.STATUS STATUS_CODE ");
		sb.append("       ,pdStatus.NAME_IN STATUS_CODE_IN ");
		sb.append("       ,pdStatus.NAME_EN STATUS_CODE_EN ");
		sb.append(" FROM WO_MST_LITIGATION_VIEWER lv ");
		sb.append("     INNER JOIN WO_MST_USER u ");
		sb.append("         ON u.USER_ID = lv.USER_ID ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdStatus ");
		sb.append("         ON pdStatus.PARAMETER_DTL_CODE = lv.STATUS ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND lv.LITIGATION_ID = :litigationId ");
		sb.append("     AND lv.ENABLED_FLAG <> 'N' ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("litigationId", litigationId);
		
		List result = query.getResultList();
		List<LitigationViewViewerVo> vo = new ArrayList<LitigationViewViewerVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				LitigationViewViewerVo data = new LitigationViewViewerVo();
				
				data.setLitigationViewId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setNik(obj[1] != null ? (String) obj[1] : null);
				data.setNama(obj[2] != null ? (String) obj[2] : null);
				data.setStatusCode(obj[3] != null ? (String) obj[3] : null);
				data.setStatusIn(obj[4] != null ? (String) obj[4] : null);
				data.setStatusEn(obj[5] != null ? (String) obj[5] : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	private List<LitigationViewDetailVo> getLitigationDetailList(Long litigationId){
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT ld.LITIGATION_DTL_ID DETAIL_ID ");
		sb.append("       ,ld.COURT_TYPE COURT_TYPE_CODE ");
		sb.append("       ,pdCourtType.NAME_IN COURT_TYPE_IN ");
		sb.append("       ,pdCourtType.NAME_EN COURT_TYPE_EN ");
		sb.append("       ,ld.COURT_NAME COURT_NAME_CODE ");
		sb.append("       ,pdCourtName.NAME_IN COURT_NAME_IN ");
		sb.append("       ,pdCourtName.NAME_EN COURT_NAME_EN ");
		sb.append("       ,ld.COURT_DATE COURT_DATE ");
		sb.append("       ,TO_CHAR(ld.COURT_DATE, 'DD-MON-YYYY') COURT_DATE_STR ");
		sb.append("       ,ld.PROGRESS PROGRESS ");
		sb.append("       ,ld.SENTENCE SENTENCE_CODE ");
		sb.append("       ,pdSentence.NAME_IN SENTENCE_IN ");
		sb.append("       ,pdSentence.NAME_EN SENTENCE_EN ");
		sb.append("       ,ld.NOTE NOTE ");
		sb.append("       ,ld.LEGAL_EFFORT LEGAL_EFFORT_CODE ");
		sb.append("       ,pdLegalEffort.NAME_IN LEGAL_EFFORT_IN ");
		sb.append("       ,pdLegalEffort.NAME_EN LEGAL_EFFORT_EN ");
		sb.append(" FROM WO_MST_LITIGATION_DTL ld ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdCourtType ");
		sb.append("         ON pdCourtType.PARAMETER_DTL_CODE = ld.COURT_TYPE ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdCourtName ");
		sb.append("         ON pdCourtName.PARAMETER_DTL_CODE = ld.COURT_NAME ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdSentence ");
		sb.append("         ON pdSentence.PARAMETER_DTL_CODE = ld.SENTENCE ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdLegalEffort ");
		sb.append("         ON pdLegalEffort.PARAMETER_DTL_CODE = ld.LEGAL_EFFORT ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND ld.LITIGATION_ID = :litigationId ");
		sb.append("     AND ld.ENABLED_FLAG <> 'D' ");
		
		sb.append(" ORDER BY ld.LITIGATION_DTL_ID ASC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("litigationId", litigationId);
		
		List result = query.getResultList();
		List<LitigationViewDetailVo> vo = new ArrayList<LitigationViewDetailVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				LitigationViewDetailVo data = new LitigationViewDetailVo();
				
				data.setLitigationDetailId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setCourtTypeCode(obj[1] != null ? (String) obj[1] : null);
				data.setCourtTypeIn(obj[2] != null ? (String) obj[2] : null);
				data.setCourtTypeEn(obj[3] != null ? (String) obj[3] : null);
				data.setCourtNameCode(obj[4] != null ? (String) obj[4] : null);
				data.setCourtNameIn(obj[5] != null ? (String) obj[5] : null);
				data.setCourtNameEn(obj[6] != null ? (String) obj[6] : null);
				data.setCourtDate(obj[7] != null ? (Date) obj[7] : null);
				data.setCourtDateStr(obj[8] != null ? (String) obj[8] : null);
				data.setProgress(obj[9] != null ? (String) obj[9] : null);
				data.setPutusanCode(obj[10] != null ? (String) obj[10] : null);
				data.setPutusanIn(obj[11] != null ? (String) obj[11] : null);
				data.setPutusanEn(obj[12] != null ? (String) obj[12] : null);
				data.setNote(obj[13] != null ? (String) obj[13] : null);
				data.setUpayaHukumCode(obj[14] != null ? (String) obj[14] : null);
				data.setUpayaHukumIn(obj[15] != null ? (String) obj[15] : null);
				data.setUpayaHukumEn(obj[16] != null ? (String) obj[16] : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}
	
}
