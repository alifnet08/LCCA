package com.wo.module.complianceTestingFE.dao;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.complianceTestingFE.vo.ComplianceTestingFEDtlVO;
import com.wo.module.complianceTestingFE.vo.ComplianceTestingFEVO;
import com.wo.module.trcComplianceReview.model.TrcComplianceReview;

@Repository("complianceTestingFEDao")
public class ComplianceTestingFEDaoImpl extends GenericDAOHibernate<TrcComplianceReview, Long> 
    implements ComplianceTestingFEDao {

	@SuppressWarnings("rawtypes")
	@Override
	public List<ComplianceTestingFEDtlVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		List<ComplianceTestingFEDtlVO> voList = searchDataCriteria(searchCriteria, first, pageSize);

		return voList;
	}
	
	@SuppressWarnings("rawtypes")
	private List<ComplianceTestingFEDtlVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first,
			int pageSize) {

		StringBuilder sb = new StringBuilder();

		sb.append(" SELECT  ");
		sb.append(" ct.compliance_testing_id, ct.INSPECTION_TITLE , ct.INSPECTION_NO, TO_CHAR(ct.START_DATE, 'dd FMMonth yyyy', 'nls_date_language=indonesian') START_DATE, TO_CHAR(ct.END_DATE, 'dd FMMonth yyyy', 'nls_date_language=indonesian') END_DATE, ct.notes, ");
		sb.append(" f.followup_status,");
		sb.append(" TO_CHAR(f.target_date, 'dd FMMonth yyyy', 'nls_date_language=indonesian') target_date, ");
		sb.append(" COMPLIANCE_TEST_PIC_FP_ID, ");
		sb.append(" u1.name PIC1, u2.name PIC2, u3.name PIC3,pd3.name_in ");
		sb.append(" FROM wo_trc_compliance_testing ct");
		sb.append(" INNER JOIN WO_TRC_COMPLIANCE_TESTING_DTL ctd ON ctd.compliance_testing_id = ct.compliance_testing_id ");
		sb.append(" INNER JOIN WO_TRC_COMP_TEST_PIC_FP f ON f.COMPLIANCE_TESTING_DTL_ID = ctd.COMPLIANCE_TESTING_DTL_ID ");
		sb.append(" LEFT JOIN wo_mst_parameter_dtl pd2 ON pd2.parameter_dtl_code = f.followup_status  ");
		sb.append(" LEFT JOIN wo_mst_parameter_dtl pd3 ON pd3.parameter_dtl_code = ct.status  ");
		sb.append(" LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1  ");
		sb.append(" LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2  ");
		sb.append(" LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3  ");
		sb.append(" WHERE 1=1 ");
		sb.append(" AND ct.enabled_flag = 'Y' ");
		sb.append(" AND ct.status = 'DATA_ACTIVE' ");
		sb.append(" AND ctd.followup = 'Y' ");
		//sb.append(" AND (f.followup_status IS NULL OR f.followup_status <> 'PIC_DONE') ");
		//sb.append(" AND (f.compliance_status IS NULL OR f.compliance_status <> 'COMPLIANCE_CLOSE') ");

		sb = getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY ct.compliance_testing_id DESC ");
		

		Query result = getSession().createSQLQuery(sb.toString());
		result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        this.getQuerySetValue(result, searchCriteria);
        
		List resultList = result.getResultList();

		List<ComplianceTestingFEDtlVO> vo = new ArrayList<ComplianceTestingFEDtlVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ComplianceTestingFEDtlVO data = new ComplianceTestingFEDtlVO();
				data.setComplianceTestingId(obj[0] != null ? (MathUtil.returnIdObjectToLong(obj[0])) : null);
				data.setInspectionTitle(obj[1] != null ? (String) obj[1] : null);
				data.setInspectionNo(obj[2] != null ? (String) obj[2] : null);
				data.setStartDate(obj[3] != null ? (String) obj[3] : null);
				data.setEndDate(obj[4] != null ? (String) obj[4] : null);
				data.setNotes(obj[5] != null ? (String) obj[5] : null);
				data.setFollowupStatusCd(obj[6] != null ? (String) obj[6] : null);
				data.setTargetDate(obj[7] != null ? (String) obj[7] : null);
				data.setComplianceTestingPICFollowupId(obj[8] != null ? (MathUtil.returnIdObjectToLong(obj[8])) : null);
				data.setPicName1(obj[9] != null ? (String) obj[9] : null);
				data.setPicName2(obj[10] != null ? (String) obj[10] : null);
				data.setPicName3(obj[11] != null ? (String) obj[11] : null);
				
				data.setStatusIn(obj[12] != null ? (String) obj[12] : null);
				

				vo.add(data);
			}
		}

		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
						sb.append(" AND (:searchStatus  <> 'PIC_INPROGRESS' or f.followup_status = :searchStatus or f.followup_status is null) ");
						sb.append(" AND (:searchStatus  = 'PIC_INPROGRESS' or f.followup_status = :searchStatus)");
						//sb.append(" and f.followup_status = :searchStatus ");
						
					}

					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						sb.append(
								" and (f.user_id_1 = :userId or f.user_id_2 = :userId or f.user_id_3 = :userId ) ");
					}
					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						sb.append(" and UPPER(ct.INSPECTION_NO) like UPPER(:searchNameIn)");
					}	
				}
			}
		}
		return sb;
	}
	
	@SuppressWarnings("rawtypes")
	private void getQuerySetValue(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
//				Object valReal = searchVal.getSearchValue();

				if (!StringUtils.isBlank(val)) {
			
					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
						query.setParameter("searchStatus", val);
					}

					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						query.setParameter("userId", val);
					}
					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						query.setParameter("searchNameIn", "%" + val + "%");
					}

				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		Number results = searchCountDataCriteria(searchCriteria);
		if (results == null) {
			results = 0;
		}

		return results.longValue();
	}

	@SuppressWarnings("rawtypes")
	private Number searchCountDataCriteria(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();

		sb.append(" SELECT COUNT(1) ");
		sb.append(" FROM wo_trc_compliance_testing ct");
		sb.append(" INNER JOIN WO_TRC_COMPLIANCE_TESTING_DTL ctd ON ctd.compliance_testing_id = ct.compliance_testing_id ");
		sb.append(" INNER JOIN WO_TRC_COMP_TEST_PIC_FP f ON f.COMPLIANCE_TESTING_DTL_ID = ctd.COMPLIANCE_TESTING_DTL_ID ");
		sb.append(" LEFT JOIN wo_mst_parameter_dtl pd2 ON pd2.parameter_dtl_code = f.followup_status  ");
		sb.append(" LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1  ");
		sb.append(" LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2  ");
		sb.append(" LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3  ");
		sb.append(" WHERE 1=1 ");
		sb.append(" AND ct.enabled_flag = 'Y' ");
		sb.append(" AND ct.status = 'DATA_ACTIVE' ");
		sb.append(" AND ctd.followup = 'Y' ");
		//sb.append(" AND (f.followup_status IS NULL OR f.followup_status <> 'PIC_DONE') ");
		//sb.append(" AND (f.compliance_status IS NULL OR f.compliance_status <> 'COMPLIANCE_CLOSE') ");

		sb = getQueryWhereString(sb, searchCriteria);

		Query result = getSession().createSQLQuery(sb.toString());
		
		this.getQuerySetValue(result, searchCriteria);

		return (Number) result.getSingleResult();
	}

	

}
