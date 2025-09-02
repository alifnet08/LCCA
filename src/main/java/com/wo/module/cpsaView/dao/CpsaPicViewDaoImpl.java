package com.wo.module.cpsaView.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.cpsa.constant.CompliancePlanSelfAssessmentConstant;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessmentPic;
import com.wo.module.cpsaView.vo.CpsaPicViewVo;
import com.wo.module.cpsaView.vo.CpsaViewVO;
@Repository("cpsaPicViewDao")
public class CpsaPicViewDaoImpl extends GenericDAOHibernate<CompliancePlanSelfAssessmentPic, Long> implements CpsaPicViewDao{
	
	@SuppressWarnings("rawtypes")
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_CPSA_TYPE, col)) {
						sb.append(" AND b.CPSA_TYPE = :cpsaType ");
					}
					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_CPSA_NAME, col)) {
						sb.append(" AND b.CPSA_NAME = :cpsaName ");
					}
					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_UPLOAD_DATE, col)) {
						sb.append(" AND b.UPLOAD_DATE = :uploadDate ");
					}
					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_LETTER_NO, col)) {
						sb.append(" AND upper(b.LETTER_NO) LIKE upper(:letterNo) ");
					}
					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_LETTER_DATE, col)) {
						sb.append(" AND TRUNC(b.LETTER_DATE) = TO_DATE(:letterDate,'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PERIOD_START_FROM, col)) {
						sb.append(" and TRUNC(b.period_from) >= TO_DATE(:periodStartFrom,'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PERIOD_START_TO, col)) {
						sb.append(" and TRUNC(b.period_from) <= TO_DATE(:periodStartTo,'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PERIOD_END_FROM, col)) {
						sb.append(" and TRUNC(b.period_to) >= TO_DATE(:periodEndFrom,'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PERIOD_END_TO, col)) {
						sb.append(" and TRUNC(b.period_to) <= TO_DATE(:periodEndTo,'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_WORKING_UNIT, col)) {
						sb.append(" 	and upper(a.DIVISION_ID) LIKE upper(:workingUnit) ");
					}
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_BRANCH, col)) {
						sb.append(" 	and upper(a.BRANCH_CODE) LIKE upper(:branch) ");
					}
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PIC_1, col)) {
						sb.append(" 	and UPPER(user1.name) LIKE UPPER(:pic1) ");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void getQuerySetString(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_CPSA_TYPE, col)) {
						query.setParameter("cpsaType", val);
					}
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_CPSA_NAME, col)) {
						query.setParameter("cpsaName", val);
					}
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_UPLOAD_DATE, col)) {
						query.setParameter("uploadDate", val);
					}
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_LETTER_NO, col)) {
						query.setParameter("letterNo", "%"+val+"%");
					}
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_LETTER_DATE, col)) {
						query.setParameter("letterDate", val);
					}					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PERIOD_START_FROM, col)) {
						query.setParameter("periodStartFrom", val);
					}					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PERIOD_START_TO, col)) {
						query.setParameter("periodStartTo", val);
					}					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PERIOD_END_FROM, col)) {
						query.setParameter("periodEndFrom", val);
					}					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PERIOD_END_TO, col)) {
						query.setParameter("periodEndTo", val);
					}
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_WORKING_UNIT, col)) {
						query.setParameter("workingUnit", val);
					}
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_BRANCH, col)) {
						query.setParameter("branch", val);
					}
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PIC_1, col)) {
						query.setParameter("pic1", "%"+val+"%");
					}
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<CpsaPicViewVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return searchDataCriteria(searchCriteria, first, pageSize);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		Number result = searchCountDataCritreia(searchCriteria);
		
		if (result == null) {
			result = 0;
		}
		
		return result.longValue();
	}
	
	@SuppressWarnings("rawtypes")
	private List<CpsaPicViewVo> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize){
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT a.CPSA_PIC_ID, a.CPSA_ID, div.DIVISION_NAME, br.BRANCH_NAME, user1.NAME pic1, "
				+ "user2.NAME pic2, user3.NAME pic3, status.NAME_IN "
				+ "	,coalesce(user4.NAME, user5.NAME) "
				+ " FROM WO_MST_CPSA_PIC a"
				+ " LEFT JOIN WO_MST_CPSA b ON b.CPSA_ID = a.CPSA_ID "
				+ " LEFT JOIN ( "
				+ "		SELECT DISTINCT wmu.DIVISION_NAME , wmu.DIVISION_ID"
				+ "		FROM wo_mst_user wmu "
				+ "		WHERE 1 = 1 AND wmu.ENABLED_FLAG = 'Y' "
				+ "		) div ON div.DIVISION_ID = a.DIVISION_ID "
				+ "	LEFT JOIN ( "
				+ "		SELECT DISTINCT wmu.BRANCH_CODE  , wmu.BRANCH_NAME "
				+ "		FROM wo_mst_user wmu "
				+ "		WHERE 1 = 1 AND wmu.ENABLED_FLAG = 'Y' "
				+ "		) br ON br.BRANCH_CODE = a.BRANCH_CODE "
				+ "	LEFT JOIN WO_MST_USER user1 ON a.USER_ID_1 = user1.USER_ID  "
				+ "	LEFT JOIN WO_MST_USER user2 ON a.USER_ID_2 = user2.USER_ID "
				+ "	LEFT JOIN WO_MST_USER user3 ON a.USER_ID_3 = user3.USER_ID "
				+ " LEFT JOIN WO_MST_PARAMETER_DTL status ON status.PARAMETER_DTL_ID = a.STATUS_PIC_ID"
				+ "	LEFT JOIN WO_MST_USER user4 ON b.CREATED_BY = user4.NIK "
				+ "	LEFT JOIN WO_MST_USER user5 ON b.LAST_UPDATE_BY = user5.NIK "
				+ " WHERE 1 = 1"
				+ " AND a.ENABLED_FLAG <> 'N'");
		
		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY a.CPSA_PIC_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQuerySetString(query, searchCriteria);
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		List result = query.getResultList();
		List<CpsaPicViewVo> vo = new ArrayList<>();
		if(!result.isEmpty()) {
			for(int i=0; i<result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				CpsaPicViewVo data = new CpsaPicViewVo();
				data.setCpsaPicId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setCpsaId(obj[1] != null ? MathUtil.returnIdObjectToLong(obj[1]) : null);
				data.setDivisionName(obj[2] != null ? (String) obj[2] : null);
				data.setBranchName(obj[3] != null ? (String) obj[3] : null);
				data.setUserName1(obj[4] != null ? (String) obj[4] : null);
				data.setUserName2(obj[5] != null ? (String) obj[5] : null);
				data.setUserName3(obj[6] != null ? (String) obj[6] : null);
				data.setCpsaStatusName(obj[7] != null ? (String) obj[7] : null);
				data.setPicAdmin(obj[8] != null ? (String) obj[8] : null);
				data.setCpsa(getCpsaViewByCpsaId(data.getCpsaId()));
				
				vo.add(data);
			}
		}
		
		
		return vo;
	}
	
	private CpsaViewVO getCpsaViewByCpsaId(Long cpsaId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT c.CPSA_ID CPSA_ID ");
		sb.append("       ,c.CPSA_TYPE CPSA_TYPE_CODE ");
		sb.append("       ,pdCpsaType.NAME_IN CPSA_TYPE_IN ");
		sb.append("       ,pdCpsaType.NAME_EN CPSA_TYPE_EN ");
		sb.append("       ,c.CPSA_NAME CPSA_NAME ");
		sb.append("       ,c.UPLOAD_DATE UPLOAD_DATE ");
		sb.append("       ,TO_CHAR(c.UPLOAD_DATE, 'DD MON YYYY') UPLOAD_DATE_STR ");
		sb.append("       ,c.PERIOD_FROM PERIOD_FROM ");
		sb.append("       ,TO_CHAR(c.PERIOD_FROM, 'DD MON YYYY' ) PERIOD_FROM_STR ");
		sb.append("       ,c.PERIOD_TO PERIOD_TO ");
		sb.append("       ,TO_CHAR(c.PERIOD_TO, 'DD MON YYYY') PERIOD_TO_STR ");
		sb.append("       ,c.ATTACHMENT_FILE ATTACHMENT_FILE ");
		sb.append("       ,c.FILE_ID FILE_ID ");
		sb.append("       ,c.FILE_SIZE FILE_SIZE ");
		sb.append("       ,c.NOTE NOTE ");
		sb.append("       ,c.LETTER_NO ");
		sb.append("       ,c.LETTER_DATE ");
		sb.append("       ,TO_CHAR(c.LETTER_DATE, 'DD MON YYYY' ) LETTER_DATE_STR ");
		sb.append("       ,c.LETTER_ABOUT ");
		sb.append("       ,c.STATUS_DOWNLOAD ");
		sb.append("       ,CASE WHEN c.FILE_PATH_DOWNLOAD IS NOT NULL THEN pdFilePath.NAME_IN || c.FILE_PATH_DOWNLOAD ");
		sb.append("       	ELSE NULL ");
		sb.append("        END FILE_PATH_DOWNLOAD ");
		sb.append("       ,c.CPSA_STATUS ");
		sb.append("       ,pdCpsaStatus.NAME_IN CPSA_STATUS_NAME_IN ");
		sb.append("       ,pdCpsaStatus.NAME_EN CPSA_STATUS_NAME_EN ");
		sb.append("       ,c.CPSA_BRANCH_SUB_BRANCH ");
		sb.append("       ,coalesce(user4.NAME, user5.NAME) ");
		sb.append("  FROM WO_MST_CPSA c ");
		sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL pdCpsaType ");
		sb.append("              ON pdCpsaType.PARAMETER_DTL_CODE = c.CPSA_TYPE ");
		sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL pdFilePath ON pdFilePath.PARAMETER_DTL_CODE = 'ATTACHMENT_FILE_PATH' ");
		sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL pdCpsaStatus ON pdCpsaStatus.PARAMETER_DTL_CODE = c.CPSA_STATUS ");
		sb.append("       LEFT JOIN WO_MST_USER user4 ON c.CREATED_BY = user4.NIK ");
		sb.append("       LEFT JOIN WO_MST_USER user5 ON c.LAST_UPDATE_BY = user5.NIK ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("       AND c.ENABLED_FLAG <> 'N' ");
		sb.append(" 	  AND c.CPSA_ID = :cpsaId");
		sb.append(" ORDER BY c.CPSA_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("cpsaId", cpsaId);
		
		Object result = query.getSingleResult();
		CpsaViewVO data = new CpsaViewVO();
		
		if (result != null) {
			Object[] obj = (Object[]) result;

			data.setCompliancePlanSelfAssessmentId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
			data.setCpsaTypeCode(obj[1] != null ? (String) obj[1] : null);
			data.setCpsaTypeIn(obj[2] != null ? (String) obj[2] : null);
			data.setCpsaTypeEn(obj[3] != null ? (String) obj[3] : null);
			data.setCpsaName(obj[4] != null ? (String) obj[4] : null);
			data.setUploadDate(obj[5] != null ? (Date) obj[5] : null);
			data.setUploadDateStr(obj[6] != null ? (String) obj[6] : null);
			data.setPeriodeFrom(obj[7] != null ? (Date) obj[7] : null);
			data.setPeriodeFromStr(obj[8] != null ? (String) obj[8] : null);
			data.setPeriodeTo(obj[9] != null ? (Date) obj[9] : null);
			data.setPeriodeToStr(obj[10] != null ? (String) obj[10] : null);
			data.setUploadFile(obj[11] != null ? (String) obj[11] : null);
			data.setFileId(obj[12] != null ? (String) obj[12] : null);
			data.setFileSize(obj[13] != null ? MathUtil.returnIdObjectToLong(obj[13]) : null);
			data.setNote(obj[14] != null ? (String) obj[14] : null);
			data.setLetterNo(obj[15] != null ? (String) obj[15] : null);
			data.setLetterDate(obj[16] != null ? (Date) obj[16] : null);
			data.setLetterDateStr(obj[17] != null ? (String) obj[17] : null);
			data.setLetterAbout(obj[18] != null ? (String) obj[18] : null);
			data.setStatusDownload(obj[19] != null ? (String) obj[19] : null);
			data.setFilePathDownload(obj[20] != null ? (String) obj[20] : null);
			data.setCpsaStatusCode(obj[21] != null ? (String) obj[21] : null);
			data.setCpsaStatusNameIn(obj[22] != null ? (String) obj[22] : null);
			data.setCpsaStatusNameEn(obj[23] != null ? (String) obj[23] : null);
			data.setCpsaBranchSubBranch(obj[24] != null ? (String) obj[24] : null);
			data.setPicAdmin(obj[25] != null ? (String) obj[25] : null);
//			data.setCpsaPic(getAllCpsaPicByCpsaId(data.getCompliancePlanSelfAssessmentId()));
//				ParameterDetail paramStatus = getDataCpsaStatus(data.getCompliancePlanSelfAssessmentId());
//				data.setCpsaStatus(paramStatus.getParameterDtlCode());
//				data.setCpsaStatusName(paramStatus.getNameIn());
		}

		return data;
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private Number searchCountDataCritreia(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT COUNT(1) "
				+ "	FROM WO_MST_CPSA_PIC a"
				+ "	LEFT JOIN WO_MST_CPSA b ON b.CPSA_ID = a.CPSA_ID "
				+ " LEFT JOIN WO_MST_USER user1 ON user1.USER_ID  = a.USER_ID_1 "
				+ "	WHERE 1 = 1"
				+ "	AND a.ENABLED_FLAG <> 'N'");
		this.getQueryWhereString(sb, searchCriteria);
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQuerySetString(query, searchCriteria);
		
		return (Number) query.getSingleResult();
	}
	
	

}
