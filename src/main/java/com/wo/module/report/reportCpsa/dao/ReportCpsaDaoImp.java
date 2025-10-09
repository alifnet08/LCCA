package com.wo.module.report.reportCpsa.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportCpsa.constant.ReportCpsaConstants;
import com.wo.module.report.reportCpsa.model.ReportCpsa;
import com.wo.module.report.reportGen.model.ReportGen;

@Repository("reportCpsaDao")
public class ReportCpsaDaoImp extends GenericDAOHibernate<ReportGen, Long> 
	implements ReportCpsaConstants, ReportCpsaDao{

	@SuppressWarnings("rawtypes")
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString() != null ? searchVal.getSearchValueAsString() : "";
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(WHERE_CREATED_DATE_FROM, col)) {
						sb.append(" AND TRUNC(cpsa.CREATION_DATE) >= TRUNC(TO_DATE('" + val + "', 'yyyy-MM-dd')) ");
					} else if (StringUtils.equals(WHERE_CREATED_DATE_TO, col)) {
						sb.append(" AND TRUNC(cpsa.CREATION_DATE) <= TRUNC(TO_DATE('" + val + "', 'yyyy-MM-dd')) ");
					} else if (StringUtils.equals(WHERE_TARGET_DATE_FROM, col)) {
						sb.append(" AND TRUNC(pic.TARGET_DATE) >= TRUNC(TO_DATE('" + val + "', 'yyyy-MM-dd')) ");
					} else if (StringUtils.equals(WHERE_TARGET_DATE_TO, col)) {
						sb.append(" AND TRUNC(pic.TARGET_DATE) <= TRUNC(TO_DATE('" + val + "', 'yyyy-MM-dd')) ");
					} else if(StringUtils.equals(WHERE_CPSA_TYPE_CODE, col)) {
						sb.append(" AND cpsa.CPSA_TYPE = '" + val + "' ");
					}					
				}
			}
		}
		
		return sb;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportCpsa> getReportCpsaByData(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT pdtl.NAME_IN CPSA_TYPE_NAME, TO_CHAR(PERIOD_FROM, 'DD-MON-YYYY') PERIOD_FROM_STR, TO_CHAR(PERIOD_TO, 'DD-MON-YYYY') PERIOD_TO_STR,");
		sb.append("       LETTER_NO, TO_CHAR(LETTER_DATE, 'DD-MON-YYYY') LETTER_DATE_STR, LETTER_ABOUT, div.DIVISION_NAME, usr1.BRANCH_NAME,");
		sb.append("       usr1.NAME USER_NAME1, usr2.NAME USER_NAME2, usr3.NAME USER_NAME3, TO_CHAR(pic.TARGET_DATE, 'DD-MON-YYYY') TARGET_DATE_STR,");
		sb.append("       TO_CHAR(pic.REVIEW_DATE, 'DD-MON-YYYY') REVIEW_DATE_STR, ");
		sb.append("       status1.PARAMETER_DTL_CODE CPSA_STATUS_CODE, status1.NAME_IN CPSA_STATUS_NAME, ");
		sb.append("       status2.PARAMETER_DTL_CODE STATUS_PIC_CODE, status2.NAME_IN STATUS_PIC_NAME, ");
		sb.append("       usrc.NAME CREATED_BY_NAME, pic.NOTE, pic.CPSA_NOTE");
		sb.append("       ,CASE WHEN status1.PARAMETER_DTL_CODE = 'COMPLIANCE_CLOSE' AND pic.REVIEW_DATE = pic.TARGET_DATE THEN 1 ELSE 0 END meet_sla");
		sb.append("       ,CASE WHEN status1.PARAMETER_DTL_CODE = 'COMPLIANCE_CLOSE' AND pic.REVIEW_DATE < pic.TARGET_DATE THEN 1 ELSE 0 END before_sla");
		sb.append("       ,CASE WHEN status1.PARAMETER_DTL_CODE = 'COMPLIANCE_CLOSE' AND pic.REVIEW_DATE > pic.TARGET_DATE THEN 1 ELSE 0 END over_sla ");
		sb.append("       ,pdtl.PARAMETER_DTL_CODE CPSA_TYPE_CODE, TO_CHAR(cpsa.CREATION_DATE, 'DD-MON-YYYY') TANGGAL_PEMBUATAN_STR");
		sb.append("  FROM WO_MST_CPSA cpsa ");
		sb.append("       INNER JOIN WO_MST_CPSA_PIC pic ON cpsa.CPSA_ID = pic.CPSA_ID");
		sb.append("       INNER JOIN WO_MST_DIVISION div ON pic.DIVISION_ID = div.DIVISION_ID");
		sb.append("       INNER JOIN WO_MST_PARAMETER_DTL pdtl ON pdtl.PARAMETER_DTL_CODE = cpsa.CPSA_TYPE");
		sb.append("       INNER JOIN WO_MST_USER usr1 ON pic.USER_ID_1 = usr1.USER_ID");
		sb.append("                                   AND usr1.BRANCH_CODE = pic.BRANCH_CODE");
		sb.append("       LEFT JOIN WO_MST_USER usr2 ON pic.USER_ID_2 = usr2.USER_ID");
		sb.append("       LEFT JOIN WO_MST_USER usr3 ON pic.USER_ID_3 = usr3.USER_ID");
		sb.append("       LEFT JOIN WO_MST_USER usrc ON cpsa.CREATED_BY = usrc.NIK");
		sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL status1 ON pic.CPSA_STATUS_ID = status1.PARAMETER_DTL_ID");
		sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL status2 ON pic.STATUS_PIC_ID = status2.PARAMETER_DTL_ID");
		sb.append(" WHERE 1=1 ");
	
		sb = getQueryWhereString(sb, searchCriteria);
		
		sb.append("ORDER BY cpsa.CPSA_ID DESC");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<ReportCpsa> vo = new ArrayList<ReportCpsa>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ReportCpsa data = new ReportCpsa();
				
				data.setCpsaTypeName(obj[0] != null ? (String) obj[0] : null);
				data.setPeriodFromStr(obj[1] != null ? (String) obj[1] : null);
				data.setPeriodToStr(obj[2] != null ? (String) obj[2] : null);
				data.setLetterNo(obj[3] != null ? (String) obj[3] : null);
				data.setLetterDateStr(obj[4] != null ? (String) obj[4] : null);
				data.setLetterAbout(obj[5] != null ? (String) obj[5] : null);
				data.setDivisionName(obj[6] != null ? (String) obj[6] : null);
				data.setBranchName(obj[7] != null ? (String) obj[7] : null);				
				data.setUserName1(obj[8] != null ? (String) obj[8] : null);
				data.setUserName2(obj[9] != null ? (String) obj[9] : null);
				data.setUserName3(obj[10] != null ? (String) obj[10] : null);
				data.setTargetDateStr(obj[11] != null ? (String) obj[11] : null);
				data.setReviewDateStr(obj[12] != null ? (String) obj[12] : null);
				data.setCpsaStatusCode(obj[13] != null ? (String) obj[13] : null);
				data.setCpsaStatusName(obj[14] != null ? (String) obj[14] : null);				
				data.setStatusPicCode(obj[15] != null ? (String) obj[15] : null);
				data.setStatusPicName(obj[16] != null ? (String) obj[16] : null);	
				
				if(data.getStatusPicCode() !=null && 
					  (data.getStatusPicCode().equals(ReportCpsaConstants.STATUS_CPSA_WAITING_APPROVAL) 
					     || data.getStatusPicCode().equals(ReportCpsaConstants.STATUS_CPSA_REJECTED))) {
					data.setCpsaStatusCode(data.getStatusPicCode());
					data.setCpsaStatusName(data.getStatusPicName());
				}
				
				data.setCreatedByName(obj[17] != null ? (String) obj[17] : null);				
				data.setNote(obj[18] != null ? (String) obj[18] : null);
				data.setCpsaNote(obj[19] != null ? (String) obj[19] : null);			
				
				if(data.getStatusPicCode() !=null && 
					  (data.getStatusPicCode().equals(ReportCpsaConstants.STATUS_CPSA_WAITING_APPROVAL) 
					     || data.getStatusPicCode().equals(ReportCpsaConstants.STATUS_CPSA_REJECTED))) {
					data.setCpsaNote(data.getNote());
				}
				
				data.setMeetSla(obj[20] != null ? (((BigDecimal) obj[20]).toBigInteger()).intValue() : null);
				data.setBeforeSla(obj[21] != null ? (((BigDecimal) obj[21]).toBigInteger()).intValue() : null);
				data.setOverSla(obj[22] != null ? (((BigDecimal) obj[22]).toBigInteger()).intValue() : null);			
				data.setCpsaTypeCode(obj[23] != null ? (String) obj[23] : null);
				data.setCreatedDateStr(obj[24] != null ? (String) obj[24] : null);	
				
				vo.add(data);
			}
		}
		
		return vo;
	}
	
}
