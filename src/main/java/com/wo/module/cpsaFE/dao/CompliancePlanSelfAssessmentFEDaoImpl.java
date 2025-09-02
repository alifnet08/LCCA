package com.wo.module.cpsaFE.dao;

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
import com.wo.module.cpsa.model.CompliancePlanSelfAssessment;
import com.wo.module.cpsaFE.constant.CompliancePlanSelfAssessmentFEConstant;
import com.wo.module.cpsaFE.vo.CompliancePlanSelfAssessmentFEVo;
import com.wo.module.cpsaFE.vo.CompliancePlanSelfAssessmentQuestionFEVo;

@Repository("compliancePlanSelfAssessmentFEDao")
public class CompliancePlanSelfAssessmentFEDaoImpl extends GenericDAOHibernate<CompliancePlanSelfAssessment, Long>
	implements CompliancePlanSelfAssessmentFEDao, Serializable{

	private static final long serialVersionUID = 2713661276517743749L;

	@SuppressWarnings("rawtypes")
	private void setQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
						sb.append(" AND c.CPSA_TYPE = :comboBox ");
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						sb.append(" AND (UPPER(c.CPSA_NAME) LIKE UPPER(:textBox) " +
								  "   OR UPPER(c.LETTER_ABOUT) LIKE UPPER(:textBox)) ");
					}		
					if(StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						sb.append(" AND (q.USER_ID_1 = :userLogin "
								+ "OR q.USER_ID_2 = :userLogin) ");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void setQuerySetString(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
						query.setParameter("comboBox", val);
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						String textBox = "%"+val+"%";
						query.setParameter("textBox", textBox);
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						query.setParameter("userLogin", val);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<CompliancePlanSelfAssessmentFEVo> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		List<CompliancePlanSelfAssessmentFEVo> vo = searchDataCriteria(searchCriteria, first, pageSize);
		return vo;
	}

	@SuppressWarnings("rawtypes")
	public List<CompliancePlanSelfAssessmentFEVo> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first,int pageSize) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT c.CPSA_ID ");
		sb.append("       ,c.CPSA_TYPE ");
		sb.append("       ,pdCpsa.NAME_IN TYPE_IN ");
		sb.append("       ,pdCpsa.NAME_EN TYPE_EN ");
		sb.append("       ,c.CPSA_NAME ");
		sb.append("       ,c.UPLOAD_DATE ");
		sb.append("       ,TO_CHAR(c.UPLOAD_DATE, 'dd FMMonth yyyy', 'nls_date_language=indonesian') UPLOAD_DATE_STR ");
		sb.append("       ,c.LETTER_ABOUT");
		sb.append("       ,NVL2(pdsts2.PARAMETER_DTL_CODE, pdsts.PARAMETER_DTL_CODE, pdsts.PARAMETER_DTL_CODE) STATUS");
		sb.append("       ,NVL2(pdsts2.NAME_IN, pdsts.NAME_IN, pdsts.NAME_IN) STATUS_NAME");
		sb.append("       ,q.TOTAL_QUESTION ");
		sb.append("       ,q.TOTAL_ANSWER ");
		sb.append("       ,q.USER_ID_1 ");
		sb.append("       ,q.USER_ID_2 ");
		sb.append("       ,q.NOTE ");
		sb.append("       ,q.CPSA_NOTE ");
		sb.append("       ,CASE"
				+ "				WHEN USER_ID_1 = :userLogin THEN 'PIC' "
				+ "       		WHEN USER_ID_2 = :userLogin THEN 'PIC SECONDARY' "
				+ "       		ELSE 'PIC' "
				+ "       	END AS PIC_TYPE ");
		sb.append("       ,q.LOCK_FLAG ");
		sb.append("		  ,q.USER_NIK_LOCK ");
		sb.append("  FROM WO_MST_CPSA c ");
		sb.append("       INNER JOIN WO_MST_CPSA_PIC q ON c.CPSA_ID = q.CPSA_ID ");
		sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL pdCpsa ON pdCpsa.PARAMETER_DTL_CODE = c.CPSA_TYPE ");
		sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL pdsts ON pdsts.PARAMETER_DTL_ID = q.STATUS_PIC_ID");
		sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL pdsts2 ON pdsts2.PARAMETER_DTL_ID = q.CPSA_STATUS_ID ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("       AND c.ENABLED_FLAG = 'Y' ");
		
		this.setQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY c.CPSA_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQuerySetString(query, searchCriteria);
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		List result = query.getResultList();
		List<CompliancePlanSelfAssessmentFEVo> vo = new ArrayList<CompliancePlanSelfAssessmentFEVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				CompliancePlanSelfAssessmentFEVo data = new CompliancePlanSelfAssessmentFEVo();
				
				data.setCpsaId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setCpsaTypeCode(obj[1] != null ? (String) obj[1] : null);
				data.setCpsaTypeIn(obj[2] != null ? (String) obj[2] : null);
				data.setCpsaTypeEn(obj[3] != null ? (String) obj[3] : null);
				data.setCpsaName(obj[4] != null ? (String) obj[4] : null);
				data.setUploadDate(obj[5] != null ? (Date) obj[5] : null);
				data.setUploadDateStr(obj[6] != null ? (String) obj[6] : null);
				data.setLetterAbout(obj[7] != null ? (String) obj[7] : null);		
				data.setStatusPic(obj[8] != null ? (String) obj[8] : null);
				data.setStatusPicName(obj[9] != null ? (String) obj[9] : null);
				data.setTotalQuestion(obj[10] != null ? MathUtil.returnIdObjectToInteger(obj[10]) : 0);
				data.setTotalAnswer(obj[11] != null ? MathUtil.returnIdObjectToInteger(obj[11]) : 0);
				data.setUserId1(obj[12] != null ? MathUtil.returnIdObjectToLong(obj[12]) : null);
				data.setUserId2(obj[13] != null ? MathUtil.returnIdObjectToLong(obj[13]) : null);
				data.setNote(obj[14] != null ? (String) obj[14] : null);
				data.setCpsaNote(obj[15] != null ? (String) obj[15] : null);
				data.setPicType(obj[16].toString());
				data.setLockFlag(obj[17] != null ? obj[17].toString() : null);
				data.setUserNikLock(obj[18] != null ? obj[18].toString() : null);
				
				if(data.getLockFlag() != null) {
					data.setBooleanLockFlag(data.getLockFlag().equals(CommonConstants.RECORD_FLAG_YES) ? true : false);
				}else {
					data.setBooleanLockFlag(true);
				}
				
				
				if(data.getStatusPic() == null || data.getStatusPic().equals(CompliancePlanSelfAssessmentFEConstant.STRING_EMPTY) ||
						data.getStatusPic().equals(CompliancePlanSelfAssessmentFEConstant.STATUS_CPSA_INPROGRESS) ||
						data.getStatusPic().equals(CompliancePlanSelfAssessmentFEConstant.STATUS_CPSA_REJECTED) ||
						data.getStatusPic().equals(CompliancePlanSelfAssessmentFEConstant.STATUS_COMPLIANCE_OPEN)) {
					data.setFlagStatus(true);
				}else {
					data.setFlagStatus(false);
				}
				
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
	public Number searchCountDataCriteria(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT COUNT(1) ");
		sb.append("   FROM WO_MST_CPSA c ");
		sb.append("        INNER JOIN WO_MST_CPSA_PIC q ON c.CPSA_ID = q.CPSA_ID ");
		sb.append("        LEFT JOIN WO_MST_PARAMETER_DTL pdCpsa ON pdCpsa.PARAMETER_DTL_CODE = c.CPSA_TYPE ");
		sb.append("        LEFT JOIN WO_MST_PARAMETER_DTL pdsts ON pdsts.PARAMETER_DTL_ID = q.STATUS_PIC_ID");
		sb.append("        LEFT JOIN WO_MST_PARAMETER_DTL pdsts2 ON pdsts2.PARAMETER_DTL_ID = q.CPSA_STATUS_ID ");
		sb.append("  WHERE 1 = 1 ");
		sb.append("        AND c.ENABLED_FLAG = 'Y' ");
		
		this.setQueryWhereString(sb, searchCriteria);
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQuerySetString(query, searchCriteria);
		
		Number result = (Number) query.getSingleResult();
		
		return result.longValue();
	}
	
	@SuppressWarnings("rawtypes")
	public CompliancePlanSelfAssessmentFEVo getDataCpsaPic(Long cpsaId, Long userAnswerId) throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT DISTINCT cpsa.CPSA_ID, pic.CPSA_PIC_ID, pic.DIVISION_ID, div.DIVISION_NAME, ");
		sb.append("       cpsa.PERIOD_FROM, TO_CHAR(cpsa.PERIOD_FROM, 'DD-MON') PERIOD_FROM_STR, ");
		sb.append("       cpsa.PERIOD_TO, TO_CHAR(cpsa.PERIOD_TO, 'DD-MON') PERIOD_TO_STR, ");
		sb.append("       TO_CHAR(cpsa.PERIOD_FROM, 'YYYY') YEAR_STR, pic.USER_ID_1, pic.USER_ID_2, ");
		sb.append("       pic.USER_ID_3, pdtl.PARAMETER_DTL_CODE CPSA_STATUS, pdtl.NAME_IN CPSA_STATUS_NAME, ");
		sb.append("       pic.TOTAL_QUESTION, pic.TOTAL_ANSWER, ");
		sb.append("       pdtl2.PARAMETER_DTL_CODE STATUS_PIC, pdtl2.NAME_IN STATUS_PIC_NAME, ");
		sb.append("       pic.LOCK_FLAG, pic.USER_NIK_LOCK, cpsa.CPSA_TYPE, pic.BRANCH_CODE, usr.BRANCH_NAME ");
		sb.append("  FROM WO_MST_CPSA_PIC pic ");
		sb.append("       INNER JOIN WO_MST_CPSA cpsa ON pic.CPSA_ID = cpsa.CPSA_ID ");
		sb.append("       INNER JOIN WO_MST_DIVISION div ON pic.DIVISION_ID = div.DIVISION_ID ");
		sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL pdtl ON pic.CPSA_STATUS_ID = pdtl.PARAMETER_DTL_ID ");
		sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL pdtl2 ON pic.STATUS_PIC_ID = pdtl2.PARAMETER_DTL_ID ");
		sb.append(" 	  LEFT JOIN WO_MST_USER usr ON pic.BRANCH_CODE = usr.BRANCH_CODE AND usr.ENABLED_FLAG = 'Y' ");
		sb.append(" WHERE 1=1 ");
		sb.append("       AND cpsa.ENABLED_FLAG = 'Y' ");
		
		if(cpsaId !=null) {
			sb.append("       AND cpsa.CPSA_ID = " + cpsaId);
		}
		
		//comment by Jovan 10-10-2023
//		if(userAnswerId !=null) {
//			sb.append("       AND pic.USER_ID_1 = " + userAnswerId);
//		}
		
		//new if statement for 2 PIC system
		if(userAnswerId !=null) {
			sb.append("       AND (pic.USER_ID_1 = " + userAnswerId + " OR pic.USER_ID_2 = " + userAnswerId + ")");
		}
		
		sb.append(" ORDER BY cpsa.CPSA_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());	
		
		List result = query.getResultList();
		CompliancePlanSelfAssessmentFEVo data = new CompliancePlanSelfAssessmentFEVo();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				data.setCpsaId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setCpsaQuestId(obj[1] != null ? MathUtil.returnIdObjectToLong(obj[1]) : null);
				data.setDivisionId(obj[2] != null ? MathUtil.returnIdObjectToLong(obj[2]) : null);
				data.setDivisionName(obj[3] != null ? (String) obj[3] : null);				
				data.setPeriodStartDate(obj[4] != null ? (Date) obj[4] : null);
				data.setPeriodStartDateStr(obj[5] != null ? (String) obj[5] : null);
				data.setPeriodEndDate(obj[6] != null ? (Date) obj[6] : null);
				data.setPeriodEndDateStr(obj[7] != null ? (String) obj[7] : null);
				data.setYearStr(obj[8] != null ? (String) obj[8] : null);
				data.setUserId1(obj[9] != null ? MathUtil.returnIdObjectToLong(obj[9]) : null);
				data.setUserId2(obj[10] != null ? MathUtil.returnIdObjectToLong(obj[10]) : null);
				data.setUserId3(obj[11] != null ? MathUtil.returnIdObjectToLong(obj[11]) : null);
				data.setCpsaStatus(obj[12] != null ? (String) obj[12] : null);
				data.setCpsaStatusName(obj[13] != null ? (String) obj[13] : null);
				data.setTotalQuestion(obj[14] != null ? MathUtil.returnIdObjectToInteger(obj[14]) : null);
				data.setTotalAnswer(obj[15] != null ? MathUtil.returnIdObjectToInteger(obj[15]) : null);
				data.setStatusPic(obj[16] != null ? (String) obj[16] : null);
				data.setStatusPicName(obj[17] != null ? (String) obj[17] : null);
				data.setLockFlag(obj[18] != null ? (String) obj[18] : null);
				data.setUserNikLock(obj[19] != null ? (String) obj[19] : null);
				data.setCpsaType(obj[20] != null ? (String)obj[20] : null);
				data.setBranchCode(obj[21] != null ? (String)obj[21] : null);
				data.setBranchName(obj[22] != null ? (String)obj[22] : null);
			}
		}
		
		return data;
	}
	
	@SuppressWarnings("rawtypes")
	public List<CompliancePlanSelfAssessmentQuestionFEVo> getQuestionHeaderByCpsaId(Long cpsaId) throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT q.CPSA_ID, q.CPSA_QUESTION_ID, q.CPSA_QUESTION_NO, ");
		sb.append("       q.CPSA_QUESTION, q.CPSA_PARENT_QUESTION ");
		sb.append("  FROM WO_MST_CPSA_QUESTION q ");
		sb.append("       INNER JOIN WO_MST_CPSA c ON q.CPSA_ID = c.CPSA_ID ");
		sb.append(" WHERE 1=1 ");
		sb.append("       AND c.ENABLED_FLAG = 'Y' ");
		sb.append("       AND (q.CPSA_PARENT_QUESTION is null or q.CPSA_QUESTION_NO = '') ");
		
		if(cpsaId !=null) {
			sb.append("   AND q.CPSA_ID = " + cpsaId);
		}
		
		sb.append(" ORDER BY q.CPSA_QUESTION_ID ASC ");
		
		Query query = getSession().createSQLQuery(sb.toString());	
		
		List result = query.getResultList();
		List<CompliancePlanSelfAssessmentQuestionFEVo> questDataList = new ArrayList<CompliancePlanSelfAssessmentQuestionFEVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				CompliancePlanSelfAssessmentQuestionFEVo data = new CompliancePlanSelfAssessmentQuestionFEVo();
				data.setCpsaId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setCpsaQuestId(obj[1] != null ? MathUtil.returnIdObjectToLong(obj[1]) : null);
				data.setCpsaQuestNo(obj[2] != null ? (String) obj[2] : null);				
				data.setCpsaQuestion(obj[3] != null ? (String) obj[3] : null);
				data.setCpsaParentQuestId(obj[4] != null ? MathUtil.returnIdObjectToLong(obj[4]) : null);
				data.setFlagHeader(CommonConstants.ENABLED_FLAG_TRUE);
				questDataList.add(data);
			}
		}
		
		return questDataList;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CompliancePlanSelfAssessmentQuestionFEVo> getDataQuestionDetail(Long cpsaId, Long questId, Long userAnswerId) throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT q.CPSA_ID, q.CPSA_QUESTION_ID, q.CPSA_QUESTION_NO, q.CPSA_QUESTION, ");
		sb.append("       q.CPSA_PARENT_QUESTION, an.CPSA_PIC_ID, an.CPSA_ANSWER, an.CPSA_ANSWER_NAME, an.CPSA_NOTE ");		
		sb.append("  FROM WO_MST_CPSA_QUESTION q ");
		sb.append("       INNER JOIN WO_MST_CPSA c ON q.CPSA_ID = c.CPSA_ID ");
		sb.append("       LEFT JOIN (SELECT an2.CPSA_PIC_ID, an2.CPSA_ANSWER, pram.NAME_IN CPSA_ANSWER_NAME, ");
		sb.append("                         an2.CPSA_NOTE, an2.CPSA_QUESTION_ID ");
		sb.append("                    FROM WO_MST_CPSA_ANSWER an2 ");
		sb.append("                         INNER JOIN WO_MST_CPSA_PIC pic ON pic.CPSA_PIC_ID = an2.CPSA_PIC_ID ");
		sb.append("                             AND (pic.USER_ID_1 = "+userAnswerId+" OR pic.USER_ID_2 = "+userAnswerId+") ");
		sb.append("                         LEFT JOIN WO_MST_PARAMETER_DTL pram ON pram.PARAMETER_DTL_CODE = an2.CPSA_ANSWER ");
		sb.append("                  )an ON an.CPSA_QUESTION_ID = q.CPSA_QUESTION_ID ");
		sb.append(" WHERE 1=1 ");
		sb.append("       AND c.ENABLED_FLAG = 'Y' ");
		
		if(cpsaId !=null) {
			sb.append("   AND q.CPSA_ID = " + cpsaId);
		}
		if(questId !=null) {
			sb.append("   AND q.CPSA_PARENT_QUESTION = " + questId);
		}
		
		sb.append(" ORDER BY q.CPSA_QUESTION_ID ASC ");
		
		Query query = getSession().createSQLQuery(sb.toString());	
		
		List result = query.getResultList();
		List<CompliancePlanSelfAssessmentQuestionFEVo> questDataList = new ArrayList();
		Integer rowIndex = 1;
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				CompliancePlanSelfAssessmentQuestionFEVo data = new CompliancePlanSelfAssessmentQuestionFEVo();
				data.setCpsaId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setCpsaQuestId(obj[1] != null ? MathUtil.returnIdObjectToLong(obj[1]) : null);
				data.setCpsaQuestNo(obj[2] != null ? (String) obj[2] : null);				
				data.setCpsaQuestion(obj[3] != null ? (String) obj[3] : null);
				data.setCpsaParentQuestId(obj[4] != null ? MathUtil.returnIdObjectToLong(obj[4]) : null);
				data.setCpsaPicId(obj[5] != null ? MathUtil.returnIdObjectToLong(obj[5]) : null);
				data.setCpsaAnswer(obj[6] != null ? (String) obj[6] : null);
				data.setCpsaAnswerName(obj[7] != null ? (String) obj[7] : null);
				data.setCpsaNote(obj[8] != null ? (String) obj[8] : null);
				data.setRowIndex(rowIndex);
				if(data.getCpsaParentQuestId() !=null && data.getCpsaParentQuestId() > 0) {
					data.setFlagHeader(CompliancePlanSelfAssessmentFEConstant.FLAG_HEADER_N);
				}else{
					data.setFlagHeader(CompliancePlanSelfAssessmentFEConstant.FLAG_HEADER_Y);
				}
				questDataList.add(data);
				rowIndex++;
			}
		}
		
		return questDataList;
	}
	
	
	public Long totalDataNotAnswer(Long cpsaId, Long userAnswerId) throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT COUNT(*) ");
		sb.append("  FROM WO_MST_CPSA_QUESTION q ");
		sb.append("       INNER JOIN WO_MST_CPSA_ANSWER an ON q.CPSA_QUESTION_ID = an.CPSA_QUESTION_ID ");
		sb.append("       LEFT JOIN WO_MST_CPSA_PIC pic ON pic.CPSA_PIC_ID = an.CPSA_PIC_ID ");
		sb.append("                                    AND PIC.USER_ID_1  = "+userAnswerId+" ");
		sb.append(" WHERE 1=1 ");
		sb.append("       AND q.CPSA_PARENT_QUESTION IS NOT NULL ");
		sb.append("       AND an.CPSA_ANSWER IS NULL ");
	       
		if(cpsaId !=null) {
			sb.append("   AND q.CPSA_ID = " + cpsaId);
		}
		
		Query query = getSession().createSQLQuery(sb.toString());			
		Number result = (Number) query.getSingleResult();
		
		return result.longValue();
	}
	
	

}
