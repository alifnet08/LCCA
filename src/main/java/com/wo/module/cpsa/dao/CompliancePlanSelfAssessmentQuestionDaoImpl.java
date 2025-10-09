package com.wo.module.cpsa.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.cpsa.constant.CompliancePlanSelfAssessmentConstant;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessmentQuestion;
import com.wo.module.cpsa.vo.CompliancePlanSelfAssessmentPicVo;
import com.wo.module.cpsa.vo.CompliancePlanSelfAssessmentQuestionVo;

@Repository("compliancePlanSelfAssessmentQuestionDao")
public class CompliancePlanSelfAssessmentQuestionDaoImpl extends GenericDAOHibernate<CompliancePlanSelfAssessmentQuestion, Long>
	implements CompliancePlanSelfAssessmentQuestionDao, Serializable{

	private static final long serialVersionUID = 8326816285504023990L;

	@SuppressWarnings("rawtypes")
	@Override
	public List<CompliancePlanSelfAssessmentQuestionVo> searchData(List<? extends SearchObject> searchCriteria,
			int first, int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public List<CompliancePlanSelfAssessmentPicVo> getDataCpsaPic(Long cpsaId, Long userId, String cpsaStatus) throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT DISTINCT cpsa.CPSA_ID, pic.CPSA_PIC_ID, pic.DIVISION_ID, div.DIVISION_NAME, ");
		sb.append("       cpsa.PERIOD_FROM, TO_CHAR(cpsa.PERIOD_FROM, 'DD-MON') PERIOD_FROM_STR, ");
		sb.append("       cpsa.PERIOD_TO, TO_CHAR(cpsa.PERIOD_TO, 'DD-MON') PERIOD_TO_STR, ");
		sb.append("       TO_CHAR(cpsa.PERIOD_FROM, 'YYYY') YEAR_STR, pic.USER_ID_1, pic.USER_ID_2, ");
		sb.append("       pic.USER_ID_3, usr.NIK USER_NIK_1, usr.NAME USER_NAME_1, ");
		sb.append("       usr.DIRECTORATE_NAME DIRECTORATE_NAME_1, pdtl.PARAMETER_DTL_CODE CPSA_STATUS, ");
		sb.append("       pdtl.NAME_IN CPSA_STATUS_NAME, pic.BRANCH_CODE, usr2.BRANCH_NAME ");
		sb.append("  FROM WO_MST_CPSA_PIC pic ");
		sb.append("       INNER JOIN WO_MST_CPSA cpsa ON pic.CPSA_ID = cpsa.CPSA_ID ");
		sb.append("       INNER JOIN WO_MST_DIVISION div ON pic.DIVISION_ID = div.DIVISION_ID ");
		sb.append("       INNER JOIN WO_MST_USER usr ON pic.USER_ID_1 = usr.USER_ID ");
		sb.append("       INNER JOIN WO_MST_USER usr2 ON pic.BRANCH_CODE = usr2.BRANCH_CODE ");
		sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL pdtl ON pic.CPSA_STATUS_ID = pdtl.PARAMETER_DTL_ID ");
		sb.append(" WHERE 1=1 ");
		sb.append("       AND cpsa.ENABLED_FLAG = 'Y' ");
		sb.append("		  AND usr2.ENABLED_FLAG = 'Y' ");
		
		if(cpsaId !=null) {
			sb.append("       AND cpsa.CPSA_ID = " + cpsaId);
		}		
		if(userId !=null) {
			sb.append("       AND pic.USER_ID_1 = " + userId);
		}		
		if (cpsaStatus != null && !cpsaStatus.equals(CompliancePlanSelfAssessmentConstant.STRING_EMPTY)) {
			sb.append("       AND pdtl.PARAMETER_DTL_CODE = '" + cpsaStatus + "' ");
		}
		
		sb.append(" ORDER BY cpsa.CPSA_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());	
		
		List result = query.getResultList();
		List<CompliancePlanSelfAssessmentPicVo> dataList = new ArrayList<CompliancePlanSelfAssessmentPicVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				
				CompliancePlanSelfAssessmentPicVo data = new CompliancePlanSelfAssessmentPicVo();
				data.setCpsaId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setCpsaPicId(obj[1] != null ? MathUtil.returnIdObjectToLong(obj[1]) : null);
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
				data.setUserNik1(obj[12] != null ? (String) obj[12] : null);
				data.setUserName1(obj[13] != null ? (String) obj[13] : null);
				data.setDirectorateName(obj[14] != null ? (String) obj[14] : null);
				data.setCpsaStatus(obj[15] != null ? (String) obj[15] : null);
				data.setCpsaStatusName(obj[16] != null ? (String) obj[16] : null);
				data.setBranchCode(obj[17] != null ? (String) obj[17] : null);
				data.setBranchName(obj[18] != null ? (String) obj[18] : null);
				
				dataList.add(data);
			}
		}
		
		return dataList;
	}
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CompliancePlanSelfAssessmentQuestionVo> getDataPicAnswer(Long cpsaId, Long picId, Long questId, Long parentQuestId, boolean flagHeader) throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT q.CPSA_ID, q.CPSA_QUESTION_ID, q.CPSA_QUESTION_NO, q.CPSA_QUESTION, ");
		sb.append("       q.CPSA_PARENT_QUESTION, an.CPSA_PIC_ID, an.CPSA_ANSWER, ");
		sb.append("       an.NAME_IN CPSA_ANSWER_NAME, an.CPSA_NOTE ");
		sb.append("  FROM WO_MST_CPSA_QUESTION q ");
		sb.append("       INNER JOIN WO_MST_CPSA c ON q.CPSA_ID = c.CPSA_ID ");
		sb.append("  	  LEFT JOIN (SELECT an2.CPSA_PIC_ID, an2.CPSA_ANSWER, an2.CPSA_NOTE, ");
		sb.append("                         pdtl.NAME_IN, an2.CPSA_QUESTION_ID ");
		sb.append("                    FROM WO_MST_CPSA_ANSWER an2 ");
		sb.append("                         INNER JOIN WO_MST_CPSA_PIC pic ON pic.CPSA_PIC_ID = an2.CPSA_PIC_ID ");
		sb.append("                         LEFT JOIN WO_MST_PARAMETER_DTL pdtl ON an2.CPSA_ANSWER = pdtl.PARAMETER_DTL_CODE ");
		sb.append("                   WHERE 1=1 ");
		sb.append("                         AND pic.USER_ID_1 = "+picId+") an ON an.CPSA_QUESTION_ID = q.CPSA_QUESTION_ID "); 
		//sb.append("       INNER JOIN WO_MST_CPSA_ANSWER an ON q.CPSA_QUESTION_ID = an.CPSA_QUESTION_ID ");
		//sb.append("       INNER JOIN WO_MST_CPSA_PIC pic ON pic.CPSA_PIC_ID = an.CPSA_PIC_ID ");
		//sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL pdtl ON an.CPSA_ANSWER = pdtl.PARAMETER_DTL_CODE ");
		sb.append(" WHERE 1=1 ");
		sb.append("       AND c.ENABLED_FLAG = 'Y' ");
		
		if(cpsaId !=null) {
			sb.append("   AND q.CPSA_ID = " + cpsaId);
		}		
		if(questId !=null) {
			sb.append("   AND q.CPSA_QUESTION_ID = " + questId);
		}
		if(parentQuestId !=null) {
			sb.append("   AND q.CPSA_PARENT_QUESTION = " + parentQuestId);
		}
		/*
		 * if(flagHeader) { sb.
		 * append("   AND (q.CPSA_PARENT_QUESTION is null or q.CPSA_QUESTION_NO = '') "
		 * ); }else { sb.append("   AND q.CPSA_PARENT_QUESTION is not null "); }
		 */
				
		sb.append(" ORDER BY q.CPSA_QUESTION_ID ASC ");
		
		Query query = getSession().createSQLQuery(sb.toString());	
		
		List result = query.getResultList();
		List<CompliancePlanSelfAssessmentQuestionVo> questDataList = new ArrayList();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				CompliancePlanSelfAssessmentQuestionVo data = new CompliancePlanSelfAssessmentQuestionVo();
				data.setCpsaId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setCpsaQuestionId(obj[1] != null ? MathUtil.returnIdObjectToLong(obj[1]) : null);
				data.setCpsaQuestionNo(obj[2] != null ? (String) obj[2] : null);				
				data.setCpsaQuestion(obj[3] != null ? (String) obj[3] : null);
				data.setCpsaParentQuestion(obj[4] != null ? MathUtil.returnIdObjectToLong(obj[4]) : null);
				data.setCpsaPicId(obj[5] != null ? MathUtil.returnIdObjectToLong(obj[5]) : null);
				data.setCpsaAnswer(obj[6] != null ? (String) obj[6] : null);
				data.setCpsaAnswerName(obj[7] != null ? (String) obj[7] : null);
				data.setCpsaNote(obj[8] != null ? (String) obj[8] : null);
				questDataList.add(data);
			}
		}
		
		return questDataList;
	}
	
	public Long totalDataNotAnswer(Long cpsaId, Long userAnswerId) throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT COUNT(*) ");
		sb.append("  FROM WO_MST_CPSA_QUESTION q ");
		sb.append("       LEFT JOIN WO_MST_CPSA_ANSWER an ON q.CPSA_QUESTION_ID = an.CPSA_QUESTION_ID ");
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
	
	public Integer totalDataQuestion(Long cpsaId) throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT COUNT(*) ");
		sb.append("  FROM WO_MST_CPSA_QUESTION q ");
		sb.append(" WHERE 1=1 ");
		sb.append("       AND q.CPSA_PARENT_QUESTION IS NOT NULL ");
	       
		if(cpsaId !=null) {
			sb.append("   AND q.CPSA_ID = " + cpsaId);
		}
		
		Query query = getSession().createSQLQuery(sb.toString());			
		Number result = (Number) query.getSingleResult();
		
		Integer totalQuestion = 0;
		if(result !=null) {
			totalQuestion = result.intValue();
		}
		
		return totalQuestion;
	}
		
}
