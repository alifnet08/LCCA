/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.complianceTestingMockup.dao;

//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.complianceTestingMockup.constant.ComplianceTestingMockupConstants;
import com.wo.module.complianceTestingMockup.model.ComplianceTesting;
import com.wo.module.complianceTestingMockup.vo.ComplianceTestingVO;

/**
 *
 * @author hendra
 */
@Repository("complianceTestingDao")
public class ComplianceTestingDaoImpl extends GenericDAOHibernate<ComplianceTesting, Long> implements ComplianceTestingDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ComplianceTestingDaoImpl.class);

	@Override
	public List<ComplianceTestingVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT " + 
				  	"	ct.COMPLIANCE_TESTING_ID, " + 
					"	ct.INSPECTION_TITLE, " + 
					"	ct.INSPECTION_NO, " + 
					"	u.NAME picName," + 
					"	TO_CHAR(fp.TARGET_DATE, 'dd-Mon-yyyy') targetDate," + 
					" 	fp.FOLLOWUP_STATUS, " +
					"	pd.name_in statusIn," + 
					"	TO_CHAR(fp.FOLLOWUP_DATE, 'dd-Mon-yyyy') followupDate," + 
					"	ctd.OBSERVATION_RESULTS," + 
					"	ctd.RECOMMENDATION," +
					"	fp.FOLLOWUP, " +
					"	pd2.name_in comStatusIn, " +
				    "   (select count(1) from WO_TRC_COMPLIANCE_TESTING_DTL dtl2 INNER JOIN WO_TRC_COMP_TEST_PIC_FP fp2 on fp2.COMPLIANCE_TESTING_DTL_ID = dtl2.COMPLIANCE_TESTING_DTL_ID where dtl2.COMPLIANCE_TESTING_ID = ct.COMPLIANCE_TESTING_ID and dtl2.FOLLOWUP = 'Y' and (fp2.COMPLIANCE_STATUS is null or fp2.COMPLIANCE_STATUS <> 'COMPLIANCE_CLOSE')) closeFlag" + 
					" FROM WO_TRC_COMPLIANCE_TESTING ct" +
					" LEFT JOIN WO_TRC_COMPLIANCE_TESTING_DTL ctd ON ct.COMPLIANCE_TESTING_ID = ctd.COMPLIANCE_TESTING_ID " +
					" LEFT JOIN WO_TRC_COMP_TEST_PIC_FP fp ON ctd.COMPLIANCE_TESTING_DTL_ID = fp.COMPLIANCE_TESTING_DTL_ID" + 
					" LEFT JOIN wo_mst_user u ON u.USER_ID = fp.user_id_1" + 
					" LEFT JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = fp.FOLLOWUP_STATUS" + 
					" LEFT JOIN wo_mst_parameter_dtl pd2 ON pd2.parameter_dtl_code = fp.COMPLIANCE_STATUS" + 
					" WHERE 1=1 and ct.enabled_flag = 'Y' ");

		sb = getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY ct.COMPLIANCE_TESTING_ID DESC ");
		
		
		Query result = getSession().createSQLQuery(sb.toString());
		result.setFirstResult(first);
		result.setMaxResults(pageSize);
		List resultList = result.getResultList();

		List<ComplianceTestingVO> vo = new ArrayList<ComplianceTestingVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ComplianceTestingVO data = new ComplianceTestingVO();
				data.setId(obj[0] != null ? ((java.math.BigDecimal) obj[0]).longValue() : null);
				data.setJudulPemeriksaan(obj[1] != null ? (String) obj[1] : null);
				data.setNoPemeriksaan(obj[2] != null ? (String) obj[2] : null);
				data.setNamaPic(obj[3] != null ? (String) obj[3] : null);
				data.setTargetTgl(obj[4] != null ? (String) obj[4] : null);
				data.setStatusTindakLanjut(obj[6] != null ? (String) obj[6] : null);
				data.setTglPemenuhanTindakLanjut(obj[7] != null ? (String) obj[7] : null);
				data.setHasilObservasi(obj[8] != null ? (String) obj[8] : null);
				data.setRekomendasi(obj[9] != null ? (String) obj[9] : null);
				data.setKeterangan(obj[10] != null ? (String) obj[10] : null);
				data.setStatusCompliance(obj[11] != null ? (String) obj[11] : null);
				data.setCloseFlag(obj[12] != null ? ((Number) obj[12]).intValue() : null);
				
				vo.add(data);
			}
		}

		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		return vo;
	}

	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT count(1) " + 
				" FROM WO_TRC_COMPLIANCE_TESTING ct" +
				" LEFT JOIN WO_TRC_COMPLIANCE_TESTING_DTL ctd ON ct.COMPLIANCE_TESTING_ID = ctd.COMPLIANCE_TESTING_ID " +
				" LEFT JOIN WO_TRC_COMP_TEST_PIC_FP fp ON ctd.COMPLIANCE_TESTING_DTL_ID = fp.COMPLIANCE_TESTING_DTL_ID" + 
				" LEFT JOIN wo_mst_user u ON u.USER_ID = fp.user_id_1" + 
				" LEFT JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = fp.FOLLOWUP_STATUS" + 
				//" LEFT JOIN wo_mst_parameter_dtl pd4 ON pd4.parameter_dtl_code = ta.STATUS" + 
				" WHERE 1=1 and ct.enabled_flag = 'Y' ");

		sb = getQueryWhereString(sb, searchCriteria);

		Query result = getSession().createSQLQuery(sb.toString());

		Long countData = ((Number) result.getSingleResult()).longValue();
		
		System.out.println("countData=="+countData);
		return countData;
	}
	
	@SuppressWarnings({ "rawtypes", "unused", "static-access" })
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue() != null ? searchVal.getSearchValueAsString() : "";
				Object valReal = searchVal.getSearchValue();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ComplianceTestingMockupConstants.WHERE_INSPECTION_TITLE, col)) {
						sb.append(" and UPPER(ct.INSPECTION_TITLE) like UPPER('%" + val + "%') ");
					} else if (StringUtils.equals(ComplianceTestingMockupConstants.WHERE_INSPECTION_NO, col)) {
						sb.append(" and ct.INSPECTION_NO like '%" + val + "%' ");
					} else if (StringUtils.equals(ComplianceTestingMockupConstants.WHERE_FOLLOWUP_STATUS, col)) {
						sb.append(" and fp.FOLLOWUP_STATUS = '" + val + "' ");
					} else if (StringUtils.equals(ComplianceTestingMockupConstants.WHERE_COMPLIANCE_STATUS, col)) {
						sb.append(" and fp.COMPLIANCE_STATUS = '" + val + "' ");
					} else if (StringUtils.equals(ComplianceTestingMockupConstants.WHERE_PERIOD_FROM, col)) {
						
						sb.append(" and TRUNC(ct.START_DATE) >= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");
						
					} else if (StringUtils.equals(ComplianceTestingMockupConstants.WHERE_PERIOD_TO, col)) {
						
						sb.append(" and TRUNC(ct.END_DATE) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
						
					} 
				}
			}
		}

		return sb;
	}

	
	public List<ComplianceTestingVO> searchDataXls(List<? extends SearchObject> searchCriteria) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT " + 
				  	"	ct.COMPLIANCE_TESTING_ID, " + 
					"	ct.INSPECTION_TITLE, " + 
					"	ct.INSPECTION_NO, " + 
					"	u.NAME picName," + 
					"	TO_CHAR(fp.TARGET_DATE, 'dd-Mon-yyyy') targetDate," + 
					" 	fp.FOLLOWUP_STATUS, " +
					"	pd.name_in statusIn," + 
					"	TO_CHAR(fp.FOLLOWUP_DATE, 'dd-Mon-yyyy') followupDate," + 
					"	ctd.OBSERVATION_RESULTS," + 
					"	ctd.RECOMMENDATION," +
					"	fp.FOLLOWUP, " +
					"	pd2.name_in comStatusIn" + 
					" FROM WO_TRC_COMPLIANCE_TESTING ct" +
					" LEFT JOIN WO_TRC_COMPLIANCE_TESTING_DTL ctd ON ct.COMPLIANCE_TESTING_ID = ctd.COMPLIANCE_TESTING_ID " +
					" LEFT JOIN WO_TRC_COMP_TEST_PIC_FP fp ON ctd.COMPLIANCE_TESTING_DTL_ID = fp.COMPLIANCE_TESTING_DTL_ID" + 
					" LEFT JOIN wo_mst_user u ON u.USER_ID = fp.user_id_1" + 
					" LEFT JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = fp.FOLLOWUP_STATUS" + 
					" LEFT JOIN wo_mst_parameter_dtl pd2 ON pd2.parameter_dtl_code = fp.COMPLIANCE_STATUS" + 
					" WHERE 1=1 and ct.enabled_flag = 'Y' ");

		sb = getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY ct.COMPLIANCE_TESTING_ID DESC ");
		
		
		Query result = getSession().createSQLQuery(sb.toString());
		//result.setFirstResult(first);
		//result.setMaxResults(pageSize);
		List resultList = result.getResultList();

		List<ComplianceTestingVO> vo = new ArrayList<ComplianceTestingVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ComplianceTestingVO data = new ComplianceTestingVO();
				data.setId(obj[0] != null ? ((java.math.BigDecimal) obj[0]).longValue() : null);
				data.setJudulPemeriksaan(obj[1] != null ? (String) obj[1] : null);
				data.setNoPemeriksaan(obj[2] != null ? (String) obj[2] : null);
				data.setNamaPic(obj[3] != null ? (String) obj[3] : null);
				data.setTargetTgl(obj[4] != null ? (String) obj[4] : null);
				data.setStatusTindakLanjut(obj[6] != null ? (String) obj[6] : null);
				data.setTglPemenuhanTindakLanjut(obj[7] != null ? (String) obj[7] : null);
				data.setHasilObservasi(obj[8] != null ? (String) obj[8] : null);
				data.setRekomendasi(obj[9] != null ? (String) obj[9] : null);
				data.setKeterangan(obj[10] != null ? (String) obj[10] : null);
				data.setStatusCompliance(obj[11] != null ? (String) obj[11] : null);
				
				vo.add(data);
			}
		}

		//result.setFirstResult(first);
		//result.setMaxResults(pageSize);

		return vo;
	}
}
