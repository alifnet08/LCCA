package com.wo.module.cpsa.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.cpsa.constant.CompliancePlanSelfAssessmentConstant;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessment;
import com.wo.module.cpsa.vo.CompliancePlanSelfAssessmentVo;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;

@Repository("compliancePlanSelfAssesmentDao")
public class CompliancePlanSelfAssesmentDaoImpl extends GenericDAOHibernate<CompliancePlanSelfAssessment, Long>
	implements CompliancePlanSelfAssesmentDao, Serializable{

	private static final long serialVersionUID = -8086443049563137234L;

	@SuppressWarnings("rawtypes")
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_CPSA_TYPE, col)) {
						sb.append(" AND c.CPSA_TYPE = :cpsaType ");
					}
					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_CPSA_NAME, col)) {
						sb.append(" AND c.CPSA_NAME = :cpsaName ");
					}
					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_UPLOAD_DATE, col)) {
						sb.append(" AND c.UPLOAD_DATE = :uploadDate ");
					}
					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_LETTER_NO, col)) {
						sb.append(" AND upper(c.LETTER_NO) LIKE upper(:letterNo) ");
					}
					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_LETTER_DATE, col)) {
						sb.append(" AND TRUNC(c.LETTER_DATE) = TO_DATE(:letterDate,'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PERIOD_START_FROM, col)) {
						sb.append(" and TRUNC(c.period_from) >= TO_DATE(:periodStartFrom,'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PERIOD_START_TO, col)) {
						sb.append(" and TRUNC(c.period_from) <= TO_DATE(:periodStartTo,'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PERIOD_END_FROM, col)) {
						sb.append(" and TRUNC(c.period_to) >= TO_DATE(:periodEndFrom,'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PERIOD_END_TO, col)) {
						sb.append(" and TRUNC(c.period_to) <= TO_DATE(:periodEndTo,'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_BRANCH_SUB_BRANCH, col)) {
						sb.append(" and UPPER(c.CPSA_BRANCH_SUB_BRANCH) like UPPER(:branchSubBranch) ");
					}
					
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_CPSA_STATUS, col)) {
						sb.append(" and UPPER(c.CPSA_STATUS) = UPPER(:cpsaStatus) ");
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
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_BRANCH_SUB_BRANCH, col)) {
						query.setParameter("branchSubBranch", "%"+val+"%");
					}
					if (StringUtils.equals(CompliancePlanSelfAssessmentConstant.SEARCH_BY_CPSA_STATUS, col)) {
						query.setParameter("cpsaStatus", val);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<CompliancePlanSelfAssessmentVo> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		List<CompliancePlanSelfAssessmentVo> vo = searchDataCriteria(searchCriteria, first, pageSize);
		
		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	private List<CompliancePlanSelfAssessmentVo> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize){
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
		sb.append("  FROM WO_MST_CPSA c ");
		sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL pdCpsaType ");
		sb.append("              ON pdCpsaType.PARAMETER_DTL_CODE = c.CPSA_TYPE ");
		sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL pdFilePath ON pdFilePath.PARAMETER_DTL_CODE = 'ATTACHMENT_FILE_PATH' ");
		sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL pdCpsaStatus ON pdCpsaStatus.PARAMETER_DTL_CODE = c.CPSA_STATUS ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("       AND c.ENABLED_FLAG <> 'N' ");
		
		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY c.CPSA_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQuerySetString(query, searchCriteria);
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		List result = query.getResultList();
		List<CompliancePlanSelfAssessmentVo> vo = new ArrayList<CompliancePlanSelfAssessmentVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				CompliancePlanSelfAssessmentVo data = new CompliancePlanSelfAssessmentVo();
				
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
				
//				ParameterDetail paramStatus = getDataCpsaStatus(data.getCompliancePlanSelfAssessmentId());
//				data.setCpsaStatus(paramStatus.getParameterDtlCode());
//				data.setCpsaStatusName(paramStatus.getNameIn());
				
				vo.add(data);
			}
		}
		
		return vo;
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
	private Number searchCountDataCritreia(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT COUNT(1) ");
		sb.append(" FROM WO_MST_CPSA c ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdCpsaType ");
		sb.append("         ON pdCpsaType.PARAMETER_DTL_CODE = c.CPSA_TYPE ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND c.ENABLED_FLAG <> 'N' ");
		this.getQueryWhereString(sb, searchCriteria);
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQuerySetString(query, searchCriteria);
		
		return (Number) query.getSingleResult();
	}
	
	@SuppressWarnings("rawtypes")
	private ParameterDetail getDataCpsaStatus(Long cpsaId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT gdtl.PARAMETER_DTL_CODE, gdtl.NAME_IN ");
		sb.append("   FROM WO_MST_CPSA_PIC gpic ");
		sb.append("        INNER JOIN WO_MST_PARAMETER_DTL gdtl ON gpic.CPSA_STATUS_ID = gdtl.PARAMETER_DTL_ID ");
		sb.append("  WHERE gpic.CPSA_ID = "+cpsaId+" ");
		sb.append("  GROUP BY gdtl.PARAMETER_DTL_CODE, gdtl.NAME_IN ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List result = query.getResultList();
		ParameterDetail paramDtl = new ParameterDetail();
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				String paramDtlCode = (obj[0] != null ? (String) obj[0] : CompliancePlanSelfAssessmentConstant.STRING_EMPTY);
				if(paramDtlCode !=null) {
					if(paramDtlCode.equals(CompliancePlanSelfAssessmentConstant.STATUS_CPSA_CODE_INPROGRESS)) {
						paramDtl.setParameterDtlCode(paramDtlCode);
						paramDtl.setNameIn(obj[1] != null ? (String) obj[1] : null);
						break;
					} else if(paramDtlCode.equals(CompliancePlanSelfAssessmentConstant.STATUS_CPSA_CODE_COMPLETED)) {
						paramDtl.setParameterDtlCode(paramDtlCode);
						paramDtl.setNameIn(obj[1] != null ? (String) obj[1] : null);
					}
				}else {
					paramDtl.setParameterDtlCode(CompliancePlanSelfAssessmentConstant.STATUS_CPSA_CODE_INPROGRESS);
					paramDtl.setNameIn(CompliancePlanSelfAssessmentConstant.STATUS_CPSA_NAME_INPROGRESS);
					break;
				}
			}
		}
		
		return paramDtl;
	}
}
