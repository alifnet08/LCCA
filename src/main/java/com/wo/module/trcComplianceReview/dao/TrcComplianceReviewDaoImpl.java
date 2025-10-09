/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcComplianceReview.dao;

//import java.math.BigInteger;
import java.util.ArrayList;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.picFollowupConfirmation.constant.PICFollowupConfirmationConstants;
import com.wo.module.trcComplianceReview.model.TrcComplianceReview;
import com.wo.module.trcComplianceReview.vo.TrcComplianceReviewVO;

@Repository("trcComplianceReviewDao")
public class TrcComplianceReviewDaoImpl extends GenericDAOHibernate<TrcComplianceReview, Long>
		implements TrcComplianceReviewDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(TrcComplianceReviewDaoImpl.class);

	@SuppressWarnings("rawtypes")
	@Override
	public List<TrcComplianceReviewVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		List<TrcComplianceReviewVO> voList = searchDataCriteria(searchCriteria, first, pageSize);

		return voList;
	}

	@SuppressWarnings("rawtypes")
	private List<TrcComplianceReviewVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first,
			int pageSize) {

		StringBuilder sb = new StringBuilder();

		sb.append(" SELECT  ");
		sb.append(" cr.compliance_review_id, pd.name_in reviewCategoryIn, pd.name_en reviewCategoryEn, cr.reviewed_branch, cr.document_no, TO_CHAR(cr.document_date, 'dd-Mon-yyyy') document_date, ");
		sb.append(" cr.perihal_in, cr.perihal_en,");
		sb.append(" cr.status,TO_CHAR(f.target_date, 'dd-Mon-yyyy') target_date, ");
		sb.append(" pd2.name_in statusIn, pd2.name_en statusEn, ");
		sb.append(" cmplc_review_pic_followup_id, ");
		sb.append(" u1.name PIC1, u2.name PIC2, u3.name PIC3, ");
		sb.append(" f.compliance_note ");
		sb.append(" FROM wo_trc_compliance_review cr");
		sb.append(" INNER JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = cr.review_category ");
		sb.append(" INNER JOIN wo_trc_cmplc_review_pic_fp f ON f.compliance_review_id = cr.compliance_review_id ");
		sb.append(" LEFT JOIN wo_mst_parameter_dtl pd2 ON pd2.parameter_dtl_code = f.followup_status  ");
		sb.append(" LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1  ");
		sb.append(" LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2  ");
		sb.append(" LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3  ");
		sb.append(" WHERE 1=1 ");
		sb.append(" AND cr.enabled_flag = 'Y' ");
		sb.append(" AND cr.status = 'DATA_ACTIVE' ");
		sb.append(" AND cr.follow_up = 'Y' ");
		sb.append(" AND (f.followup_status IS NULL OR f.followup_status <> 'PIC_DONE') ");
		sb.append(" AND (f.compliance_status IS NULL OR f.compliance_status <> 'COMPLIANCE_CLOSE') ");

		sb = getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY cr.compliance_review_id DESC ");
		

		Query result = getSession().createSQLQuery(sb.toString());
		result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
		List resultList = result.getResultList();

		List<TrcComplianceReviewVO> vo = new ArrayList<TrcComplianceReviewVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				TrcComplianceReviewVO data = new TrcComplianceReviewVO();
				data.setComplianceReviewId(obj[0] != null ? (MathUtil.returnIdObjectToLong(obj[0])) : null);
				data.setReviewCategoryIn(obj[1] != null ? (String) obj[1] : null);
				data.setReviewCategoryEn(obj[2] != null ? (String) obj[2] : null);
				data.setReviewedBranch(obj[3] != null ? (String) obj[3] : null);
				data.setDocumentNo(obj[4] != null ? (String) obj[4] : null);
				data.setDocumentDateStr(obj[5] != null ? (String) obj[5] : null);
				data.setPerihalIn(obj[6] != null ? (String) obj[6] : null);
				data.setPerihalEn(obj[7] != null ? (String) obj[7] : null);
				data.setStatus(obj[8] != null ? (String) obj[8] : null);
				data.setTargetDate(obj[9] != null ? (String) obj[9] : null);
				data.setStatusIn(obj[10] != null ? (String) obj[10] : null);
				data.setStatusEn(obj[11] != null ? (String) obj[11] : null);
				data.setPicFollowupId(obj[12] != null ? (MathUtil.returnIdObjectToLong(obj[12])) : null);
				data.setPicName1(obj[13] != null ? (String) obj[13] : null);
				data.setPicName2(obj[14] != null ? (String) obj[14] : null);
				data.setPicName3(obj[15] != null ? (String) obj[15] : null);
				data.setComplianceNote(obj[16] != null ? (String) obj[16] : null);

				data.setPicNameList(new ArrayList<String>());
				data.getPicNameList().add(data != null ? data.getPicName1() : null);
				data.getPicNameList().add(data != null ? data.getPicName2() : null);
				data.getPicNameList().add(data != null ? data.getPicName3() : null);

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
					if (StringUtils.equals(PICFollowupConfirmationConstants.WHERE_USER_ID, col)) {

						sb.append(" and (f.user_id_1 = " + val + " or f.user_id_2 = " + val + " or f.user_id_3 = " + val);
								//+ ")");
						sb.append(" or exists (select 1 from wo_trc_cmplc_review_pic_cmplc comp where comp.compliance_review_id = cr.compliance_review_id and comp.user_id = "+val+"))");

					}
				}
			}
		}
		return sb;
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
		sb.append(" FROM wo_trc_compliance_review cr ");
		sb.append(" INNER JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = cr.review_category ");
		sb.append(" INNER JOIN wo_trc_cmplc_review_pic_fp f ON f.compliance_review_id = cr.compliance_review_id ");
		sb.append(" LEFT JOIN wo_mst_parameter_dtl pd2 ON pd2.parameter_dtl_code = f.followup_status  ");
		sb.append(" LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1  ");
		sb.append(" LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2  ");
		sb.append(" LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3  ");
		sb.append(" WHERE 1=1 ");
		sb.append(" AND cr.enabled_flag = 'Y' ");
		sb.append(" AND cr.status = 'DATA_ACTIVE' ");
		sb.append(" AND cr.follow_up = 'Y' ");
		sb.append(" AND (f.followup_status IS NULL OR f.followup_status <> 'PIC_DONE') ");
		sb.append(" AND (f.compliance_status IS NULL OR f.compliance_status <> 'COMPLIANCE_CLOSE') ");

		sb = getQueryWhereString(sb, searchCriteria);

		Query result = getSession().createSQLQuery(sb.toString());

		return (Number) result.getSingleResult();
	}

}
