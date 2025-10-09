/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcComplianceReviewApproval.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.complianceTestingMockup.vo.ComplianceTestingVO;
import com.wo.module.picFollowupConfirmation.constant.PICFollowupConfirmationConstants;
import com.wo.module.tmpComplianceReview.dao.ComplianceReviewDao;
import com.wo.module.tmpComplianceReview.vo.StatusConfirmationVO;
import com.wo.module.trcComplianceReview.model.TrcComplianceReview;

@Repository("trcComplianceReviewApprovalDao")
public class TrcComplianceReviewApprovalDaoImpl extends GenericDAOHibernate<TrcComplianceReview, Long>
		implements TrcComplianceReviewApprovalDao {

	@Autowired
	@Qualifier("complianceReviewDao")
	private ComplianceReviewDao complianceReviewDao;

	public ComplianceReviewDao getComplianceReviewDao() {
		return complianceReviewDao;
	}

	public void setComplianceReviewDao(ComplianceReviewDao complianceReviewDao) {
		this.complianceReviewDao = complianceReviewDao;
	}

	@SuppressWarnings("rawtypes")
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(PICFollowupConfirmationConstants.WHERE_USER_ID, col)) {

						
						//sb.append(" and (exists (select 1 from wo_trc_cmplc_review_pic_cmplc trccrpc where trccrpc.compliance_review_id = f.compliance_review_id and trccrpc.user_id = "+ val + ") OR exists (select 1 from wo_mst_user u2 inner join wo_mst_responsibility r2 on u2.responsibility_id = r2.responsibility_id and u2.user_id = "+val+" and UPPER(r2.name) = UPPER('Super Administrator')))");
						sb.append(" and (exists (select 1 from WO_TRC_COMP_TEST_PIC_RVW pr where pr.COMPLIANCE_TESTING_ID = ct.COMPLIANCE_TESTING_ID and pr.user_id = "+ val + ") OR exists (select 1 from wo_mst_user u2 inner join wo_mst_responsibility r2 on u2.responsibility_id = r2.responsibility_id and u2.user_id = "+val+" and UPPER(r2.name) = UPPER('Super Administrator')))");
						
					}
				}
			}
		}
		return sb;
	}

	@SuppressWarnings("rawtypes")
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
		/*sb.append(" SELECT COUNT(DISTINCT cr.compliance_review_id) ");
		sb.append(" FROM wo_trc_compliance_review cr");
		sb.append(" INNER JOIN wo_trc_cmplc_review_pic_fp f ON f.compliance_review_id = cr.compliance_review_id  ");
		sb.append(" LEFT JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = cr.review_category  ");
		sb.append(" WHERE 1=1 ");
		sb.append(" AND cr.enabled_flag = 'Y'");
		sb.append(" AND cr.status = 'DATA_ACTIVE' ");
		sb.append(" AND (f.followup_status = 'PIC_DONE' or f.followup_status = 'PIC_INPROGRESS') ");
		sb.append(" AND f.compliance_status IS NULL ");*/
		
		sb.append(" SELECT count(1)" + 
				" FROM WO_TRC_COMPLIANCE_TESTING ct" +
				" LEFT JOIN WO_TRC_COMPLIANCE_TESTING_DTL ctd ON ct.COMPLIANCE_TESTING_ID = ctd.COMPLIANCE_TESTING_ID " +
				" LEFT JOIN WO_TRC_COMP_TEST_PIC_FP fp ON ctd.COMPLIANCE_TESTING_DTL_ID = fp.COMPLIANCE_TESTING_DTL_ID" + 
				" LEFT JOIN wo_mst_user u ON u.USER_ID = fp.user_id_1" + 
				" LEFT JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = fp.FOLLOWUP_STATUS" + 
				//" LEFT JOIN wo_mst_parameter_dtl pd4 ON pd4.parameter_dtl_code = ta.STATUS" + 
				" WHERE 1=1 and ct.enabled_flag = 'Y' ");
		sb.append(" AND ct.status = 'DATA_ACTIVE' ");
		sb.append(" and (fp.followup_status = 'PIC_DONE' OR fp.followup_status = 'PIC_EXTENSION') and (fp.compliance_status <> 'COMPLIANCE_CLOSE' or fp.compliance_status is null) ");

		sb = getQueryWhereString(sb, searchCriteria);

		Query result = getSession().createSQLQuery(sb.toString());

		return (Number) result.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<ComplianceTestingVO> searchData(List<? extends SearchObject> searchCriteria,
			int first, int pageSize, String sortField, SortOrder sortOrder) throws Exception {

		List<ComplianceTestingVO> voList = searchDataCriteria(searchCriteria, first, pageSize);

		return voList;
	}

	@SuppressWarnings("rawtypes")
	private List<ComplianceTestingVO> searchDataCriteria(
			List<? extends SearchObject> searchCriteria, int first, int pageSize) {

		StringBuilder sb = new StringBuilder();

		/*sb.append(" SELECT DISTINCT ");
		sb.append(" cr.compliance_review_id, pd.name_in reviewCategoryIn, pd.name_en reviewCategoryEn, cr.reviewed_branch, cr.document_no, TO_CHAR(cr.document_date, 'dd-Mon-yyyy') document_date, ");
		sb.append(" cr.perihal_in, cr.perihal_en,");
		sb.append(" cr.status ");
		sb.append(" FROM wo_trc_compliance_review cr");
		sb.append(" INNER JOIN wo_trc_cmplc_review_pic_fp f ON f.compliance_review_id = cr.compliance_review_id  ");
		sb.append(" LEFT JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = cr.review_category  ");
		sb.append(" WHERE 1=1 ");
		sb.append(" AND cr.enabled_flag = 'Y'");
		sb.append(" AND cr.status = 'DATA_ACTIVE' ");
		sb.append(" AND (f.followup_status = 'PIC_DONE' or f.followup_status = 'PIC_INPROGRESS') ");
		sb.append(" AND f.compliance_status IS NULL ");*/
		
		sb.append(" SELECT " + 
			  	"	fp.COMPLIANCE_TEST_PIC_FP_ID, " + 
				"	ct.INSPECTION_TITLE, " + 
				"	ct.INSPECTION_NO, " + 
				"	u.NAME picName," + 
				"	TO_CHAR(fp.TARGET_DATE, 'dd-Mon-yyyy') targetDate," + 
				" 	fp.FOLLOWUP_STATUS, " +
				"	pd.name_in statusIn," + 
				"	TO_CHAR(fp.FOLLOWUP_DATE, 'dd-Mon-yyyy') followupDate," + 
				"	ctd.OBSERVATION_RESULTS," + 
				"	ctd.RECOMMENDATION," +
				"	fp.FOLLOWUP " +
				" FROM WO_TRC_COMPLIANCE_TESTING ct" +
				" LEFT JOIN WO_TRC_COMPLIANCE_TESTING_DTL ctd ON ct.COMPLIANCE_TESTING_ID = ctd.COMPLIANCE_TESTING_ID " +
				" LEFT JOIN WO_TRC_COMP_TEST_PIC_FP fp ON ctd.COMPLIANCE_TESTING_DTL_ID = fp.COMPLIANCE_TESTING_DTL_ID" + 
				" LEFT JOIN wo_mst_user u ON u.USER_ID = fp.user_id_1" + 
				" LEFT JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = fp.FOLLOWUP_STATUS" + 
				//" LEFT JOIN wo_mst_parameter_dtl pd4 ON pd4.parameter_dtl_code = ta.STATUS" + 
				" WHERE 1=1 and ct.enabled_flag = 'Y' ");
		sb.append(" AND ct.status = 'DATA_ACTIVE' ");
		sb.append(" and (fp.followup_status = 'PIC_DONE' OR fp.followup_status = 'PIC_EXTENSION') and (fp.compliance_status <> 'COMPLIANCE_CLOSE' or fp.compliance_status is null) ");

		sb = getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY ct.COMPLIANCE_TESTING_ID DESC ");
		

		Query result = getSession().createSQLQuery(sb.toString());
		result.setFirstResult(first);
		result.setMaxResults(pageSize);
		
		List resultList = result.getResultList();

		List<ComplianceTestingVO> vo = new ArrayList<ComplianceTestingVO>();

		if (resultList != null) {
			/*for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				TrcComplianceReviewApprovalVO data = new TrcComplianceReviewApprovalVO();
				data.setComplianceReviewId(obj[0] != null ? (MathUtil.returnIdObjectToLong(obj[0])) : null);
				data.setReviewCategoryIn(obj[1] != null ? (String) obj[1] : null);
				data.setReviewCategoryEn(obj[2] != null ? (String) obj[2] : null);
				data.setReviewedBranch(obj[3] != null ? (String) obj[3] : null);
				data.setDocumentNo(obj[4] != null ? (String) obj[4] : null);
				data.setDocumentDateStr(obj[5] != null ? (String) obj[5] : null);
				data.setPerihalIn(obj[6] != null ? (String) obj[6] : null);
				data.setPerihalEn(obj[7] != null ? (String) obj[7] : null);
				data.setStatus(obj[8] != null ? (String) obj[8] : null);
				data.setStatusList(getConfirmationDataByComplianceFollowupId(data.getComplianceReviewId()));
				vo.add(data);
			}*/
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
				vo.add(data);
			}
		}

		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	private List<StatusConfirmationVO> getConfirmationDataByComplianceFollowupId(Long complianceFollowupId) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(" select user1.name as pic1"
				+ "			,user2.name as pic2"
				+ "			,user3.name as pic3"
				+ "			,pd1.name_in as statusIn"
				+ "			,pd1.name_en as statusEn"
				+ "			,crpf.followup_status"
				+ "			,TO_CHAR(crpf.target_date, 'dd-Mon-yyyy') target_date"
				+ " from wo_trc_compliance_review cr"
				+ "	left join wo_trc_cmplc_review_pic_fp crpf on crpf.compliance_review_id = cr.compliance_review_id"
				+ "	left join wo_mst_user user1 on user1.user_id = crpf.user_id_1"
				+ "	left join wo_mst_user user2 on user2.user_id = crpf.user_id_2"
				+ "	left join wo_mst_user user3 on user3.user_id = crpf.user_id_3"
				+ "	left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = crpf.followup_status"
				+ "	where 1= 1"
				+ " 	AND cr.enabled_flag = 'Y'"
				+ "		AND cr.status = 'DATA_ACTIVE'"
				+ "		AND (crpf.followup_status = 'PIC_DONE' or crpf.followup_status = 'PIC_INPROGRESS') "
				+ "		AND (crpf.compliance_status <> 'COMPLIANCE_CLOSE' OR crpf.compliance_status IS NULL) ");
		
		if (complianceFollowupId != null) {
//			sb.append(" and crpf.compliance_review_pic_followup_id = '" + complianceFollowupId + "' ");
			sb.append(" and cr.compliance_review_id = '" + complianceFollowupId + "' ");
		}
		
		Query result = getSession().createSQLQuery(sb.toString());
		List resultList = result.getResultList();
		
		List<StatusConfirmationVO> vo = new ArrayList<StatusConfirmationVO>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				StatusConfirmationVO data = new StatusConfirmationVO();
				
				data.setPic1(obj[0] != null ? (String) obj[0] : null);
				data.setPic2(obj[1] != null ? (String) obj[1] : null);
				data.setPic3(obj[2] != null ? (String) obj[2] : null);
				data.setFollowupStatusIn(obj[3] != null ? (String) obj[3] : null);
				data.setFollowupStatusEn(obj[4] != null ? (String) obj[4] : null);
				data.setFollowupStatusCd(obj[5] != null ? (String) obj[5] : null);
				data.setTargetDate(obj[6] != null ? (String) obj[6] : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}
	
	
}
