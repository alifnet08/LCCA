/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcComplianceReviewView.dao;

//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.complianceTestingMockup.vo.ComplianceTestingVO;
import com.wo.module.tmpComplianceReview.constant.TmpComplianceReviewConstants;
import com.wo.module.tmpComplianceReview.vo.StatusConfirmationVO;
import com.wo.module.trcComplianceReview.dao.TrcComplianceReviewPicFollowupAttachmentDao;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupFindings;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupPoints;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupRegulation;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupReview;

@Repository("trcComplianceReviewViewDao")
public class TrcComplianceReviewViewDaoImpl extends GenericDAOHibernate<ComplianceTestingVO, Long>
		implements TrcComplianceReviewViewDao {
	private static Logger logger = Logger.getLogger(TrcComplianceReviewViewDaoImpl.class);

	@Autowired
	@Qualifier("trcComplianceReviewPicFollowupAttachmentDao")
	private TrcComplianceReviewPicFollowupAttachmentDao trcComplianceReviewPicFollowupAttachmentDao;

	public TrcComplianceReviewPicFollowupAttachmentDao getTrcComplianceReviewPicFollowupAttachmentDao() {
		return trcComplianceReviewPicFollowupAttachmentDao;
	}

	public void setTrcComplianceReviewPicFollowupAttachmentDao(
			TrcComplianceReviewPicFollowupAttachmentDao trcComplianceReviewPicFollowupAttachmentDao) {
		this.trcComplianceReviewPicFollowupAttachmentDao = trcComplianceReviewPicFollowupAttachmentDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ComplianceTestingVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		List<ComplianceTestingVO> voList = searchDataCriteria(searchCriteria, first, pageSize);

		return voList;
	}

	@SuppressWarnings("rawtypes")
	private List<ComplianceTestingVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first,
			int pageSize) {
		
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
				"	fp.FOLLOWUP " +
				" FROM WO_TRC_COMPLIANCE_TESTING ct" +
				" LEFT JOIN WO_TRC_COMPLIANCE_TESTING_DTL ctd ON ct.COMPLIANCE_TESTING_ID = ctd.COMPLIANCE_TESTING_ID " +
				" LEFT JOIN WO_TRC_COMP_TEST_PIC_FP fp ON ctd.COMPLIANCE_TESTING_DTL_ID = fp.COMPLIANCE_TESTING_DTL_ID" + 
				" LEFT JOIN wo_mst_user u ON u.USER_ID = fp.user_id_1" + 
				" LEFT JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = fp.FOLLOWUP_STATUS" + 
				//" LEFT JOIN wo_mst_parameter_dtl pd4 ON pd4.parameter_dtl_code = ta.STATUS" + 
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
				vo.add(data);
			}
		}



		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	public List<StatusConfirmationVO> getTrcPicListByComplianceReviewId(Long complianceReviewId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT " + 
				"  u1.name pic1, " + 
				"  u2.name pic2, " + 
				"  u3.name pic3, " +
				"  TO_CHAR(f.target_date, 'dd-Mon-yyyy') target_date, " +
				"  d1.name_in followup_status_in, " +
				"  d1.name_en followup_status_en, " +
				"  d.name_in compliance_statusIn, " +
				"  d.name_en compliance_statusEn, " +
				"  f.cmplc_review_pic_followup_id " +
				"FROM wo_trc_cmplc_review_pic_fp f " + 
				"  LEFT JOIN wo_mst_parameter_dtl d " + 
				"    ON f.compliance_status = d.parameter_dtl_code " + 
				"  LEFT JOIN wo_mst_parameter_dtl d1 " + 
				"    ON f.followup_status = d1.parameter_dtl_code " + 
				"  LEFT JOIN wo_mst_user u1 " + 
				"    ON u1.user_id = f.user_id_1 " + 
				"  LEFT JOIN wo_mst_user u2 " + 
				"    ON u2.user_id = f.user_id_2 " + 
				"  LEFT JOIN wo_mst_user u3 " + 
				"    ON u3.user_id = f.user_id_3 " + 
				"  LEFT JOIN wo_mst_user u4 " + 
				"    ON u4.user_id = f.compliance_by_id ");
		sb.append(" WHERE f.compliance_review_id = :complianceReviewId ");

		sb.append(" ORDER BY f.cmplc_review_pic_followup_id ASC ");

		Query result = getSession().createSQLQuery(sb.toString());
		result.setParameter("complianceReviewId", complianceReviewId);
		List resultList = result.getResultList();

		List<StatusConfirmationVO> vo = new ArrayList<StatusConfirmationVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				StatusConfirmationVO data = new StatusConfirmationVO();
				data.setPic1(obj[0] != null ? (String) obj[0] : null);
				data.setPic2(obj[1] != null ? (String) obj[1] : null);
				data.setPic3(obj[2] != null ? (String) obj[2] : null);
				data.setTargetDate(obj[3] != null ? (String) obj[3] : null);
				data.setFollowupStatusIn(obj[4] != null ? (String) obj[4] : null);
				data.setFollowupStatusEn(obj[5] != null ? (String) obj[5] : null);
				data.setComplianceStatusIn(obj[6] != null ? (String) obj[6] : null);
				data.setComplianceStatusEn(obj[7] != null ? (String) obj[7] : null);
				data.setComplianceReviewPicFollowupId(obj[8] != null ? MathUtil.returnIdObjectToLong(obj[8]) : null);
				data.setPointList(getTrcFollowupPointsByComplianceReviewPicFollowupId(data.getComplianceReviewPicFollowupId()));
				data.setAreaReviewList(getTrcFollowupAreaReviewsByComplianceReviewPicFollowupId(data.getComplianceReviewPicFollowupId()));
				data.setFindingsList(getTrcFollowupFindingsByComplianceReviewPicFollowupId(data.getComplianceReviewPicFollowupId()));
				data.setRegulationList(getTrcFollowupRegulationByComplianceReviewPicFollowupId(data.getComplianceReviewPicFollowupId()));

				vo.add(data);
			}
		}

		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	public List<TrcComplianceReviewPicFollowupPoints> getTrcFollowupPointsByComplianceReviewPicFollowupId(Long complianceReviewPicFollowupId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT " + 
				"  u1.name, " + 
				"  TO_CHAR(fp.confirmation_date, 'dd-Mon-yyyy') confirmation_date, " + 
				"  TO_CHAR(fp.followup_date, 'dd-Mon-yyyy') followup_date, " + 
				"  fp.followup_points " +
				"FROM wo_trc_cmplc_rvw_pc_fp_points fp " + 
				"  left join wo_mst_user u1 on u1.user_id = fp.followup_by_id ");
		sb.append(" WHERE fp.CMPLC_REVIEW_PIC_FOLLOWUP_ID = :complianceReviewPicFollowupId ");

		sb.append(" ORDER BY fp.CMPLC_REVIEW_PIC_FOLLOWUP_ID ASC ");

		Query result = getSession().createSQLQuery(sb.toString());
		result.setParameter("complianceReviewPicFollowupId", complianceReviewPicFollowupId);
		List resultList = result.getResultList();

		List<TrcComplianceReviewPicFollowupPoints> vo = new ArrayList<TrcComplianceReviewPicFollowupPoints>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				TrcComplianceReviewPicFollowupPoints data = new TrcComplianceReviewPicFollowupPoints();
				data.setStrFollowupBy(obj[0] != null ? (String) obj[0] : null);
				data.setStrConfirmationDate(obj[1] != null ? (String) obj[1] : null);
				data.setStrFollowupDate(obj[2] != null ? (String) obj[2] : null);
				data.setFollowupPoints(obj[3] != null ? (String) obj[3] : null);
				vo.add(data);
			}
		}

		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	public List<TrcComplianceReviewPicFollowupFindings> getTrcFollowupFindingsByComplianceReviewPicFollowupId(Long complianceReviewPicFollowupId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT " + 
				"  findings " +
				"FROM wo_trc_cmplc_review_pc_fp_fdgs fp ");
		sb.append(" WHERE fp.CMPLC_REVIEW_PIC_FOLLOWUP_ID = :complianceReviewPicFollowupId ");

		sb.append(" ORDER BY fp.CMPLC_REVIEW_PIC_FOLLOWUP_ID ASC ");

		Query result = getSession().createSQLQuery(sb.toString());
		result.setParameter("complianceReviewPicFollowupId", complianceReviewPicFollowupId);
		List resultList = result.getResultList();

		List<TrcComplianceReviewPicFollowupFindings> vo = new ArrayList<TrcComplianceReviewPicFollowupFindings>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				String obj = (String) resultList.get(i);
				TrcComplianceReviewPicFollowupFindings data = new TrcComplianceReviewPicFollowupFindings();
				data.setFindings(obj);
				
				vo.add(data);
			}
		}

		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	public List<TrcComplianceReviewPicFollowupReview> getTrcFollowupAreaReviewsByComplianceReviewPicFollowupId(Long complianceReviewPicFollowupId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT " + 
				"  area_review " +
				"FROM wo_trc_cmplc_rvw_pc_fp_review fp ");
		sb.append(" WHERE fp.CMPLC_REVIEW_PIC_FOLLOWUP_ID = :complianceReviewPicFollowupId ");

		sb.append(" ORDER BY fp.CMPLC_REVIEW_PIC_FOLLOWUP_ID ASC ");

		Query result = getSession().createSQLQuery(sb.toString());
		result.setParameter("complianceReviewPicFollowupId", complianceReviewPicFollowupId);
		List resultList = result.getResultList();

		List<TrcComplianceReviewPicFollowupReview> vo = new ArrayList<TrcComplianceReviewPicFollowupReview>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				String obj = (String) resultList.get(i);
				TrcComplianceReviewPicFollowupReview data = new TrcComplianceReviewPicFollowupReview();
				data.setAreaReview(obj);
				
				vo.add(data);
			}
		}

		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	private List<TrcComplianceReviewPicFollowupRegulation> getTrcFollowupRegulationByComplianceReviewPicFollowupId(Long complianceReviewPicFollowupId){
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT r1.document_no " +
				" FROM wo_trc_cmplc_rvw_pc_fp_rgltn r "
				+"	LEFT JOIN wo_mst_regulation r1 "
				+ "		ON r.regulation_id = r1.regulation_id ");
		sb.append(" WHERE r.CMPLC_REVIEW_PIC_FOLLOWUP_ID = :complianceReviewPicFollowupId ");

		sb.append(" ORDER BY r.CMPLC_REVIEW_PIC_FOLLOWUP_ID ASC ");

		Query result = getSession().createSQLQuery(sb.toString());
		result.setParameter("complianceReviewPicFollowupId", complianceReviewPicFollowupId);
		List resultList = result.getResultList();

		List<TrcComplianceReviewPicFollowupRegulation> vo = new ArrayList<TrcComplianceReviewPicFollowupRegulation>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				String obj = (String) resultList.get(i);
				TrcComplianceReviewPicFollowupRegulation data = new TrcComplianceReviewPicFollowupRegulation();
				data.setDocumentNo(obj);
				
				vo.add(data);
			}
		}

		return vo;
	}

	@SuppressWarnings({ "rawtypes", "static-access", "unused" })
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		// SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		// SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue() != null ? searchVal.getSearchValueAsString() : "";
				Object valReal = searchVal.getSearchValue();

				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(TmpComplianceReviewConstants.WHERE_REVIEW_CATEGORY, col)) {
						sb.append(" and cr.review_category = '" + val + "' ");
					} else if (StringUtils.equals(TmpComplianceReviewConstants.WHERE_REVIEWED_BRANCH, col)) {
						sb.append(" and cr.reviewed_branch LIKE '%" + val + "%' ");
					} else if (StringUtils.equals(TmpComplianceReviewConstants.WHERE_DOC_NO, col)) {
						sb.append(" and cr.document_no LIKE '%" + val + "%' ");
					} else if (StringUtils.equals(TmpComplianceReviewConstants.WHERE_DOC_DATE_START, col)) {
						sb.append(" and TRUNC(cr.document_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(TmpComplianceReviewConstants.WHERE_DOC_DATE_END, col)) {
						sb.append(" and TRUNC(cr.document_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(TmpComplianceReviewConstants.WHERE_REGARDING, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and cr.perihal_en LIKE '%" + val+ "%' ");
						} else {
							sb.append(" and cr.perihal_in LIKE '%" + val+ "%' ");
						}
					} else if (StringUtils.equals(TmpComplianceReviewConstants.WHERE_STATUS, col)) {
						sb.append(" and cr.status = '" + val + "' ");
					} else if (StringUtils.equals(TmpComplianceReviewConstants.WHERE_TARGET_DATE_START, col)) {
						sb.append(" and EXISTS( SELECT " + "            1" + "        FROM"
								+ "            wo_tmp_comp_rvw_pic_fp crpf" + "        WHERE"
								+ "            crpf.compliance_review_id = cr.compliance_review_id"
								+ "                AND TRUNC(crpf.target_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ) ");

					} else if (StringUtils.equals(TmpComplianceReviewConstants.WHERE_TARGET_DATE_END, col)) {
						sb.append(" and EXISTS( SELECT " + "            1" + "        FROM"
								+ "            wo_tmp_comp_rvw_pic_fp crpf" + "        WHERE"
								+ "            crpf.compliance_review_id = cr.compliance_review_id"
								+ "                AND TRUNC(crpf.target_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ) ");
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

		sb.append(" SELECT COUNT(1)" );
		sb.append(" FROM wo_trc_compliance_review cr ");
		sb.append(" INNER JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = cr.review_category ");
		sb.append(" INNER JOIN wo_mst_parameter_dtl pd2 ON pd2.parameter_dtl_code = cr.status ");
		sb.append(" LEFT JOIN wo_tmp_comp_rvw_pic_fp f ON f.compliance_review_id = cr.compliance_review_id ");
		sb.append(" LEFT JOIN wo_trc_cmplc_review_pic_fp f2 ON f2.cmplc_review_pic_followup_id = f.compliance_rvw_pic_fp_id ");
		sb.append("	LEFT JOIN wo_mst_parameter_dtl pd3 ON f2.compliance_status = pd3.parameter_dtl_code ");
		sb.append(" WHERE 1=1 AND cr.enabled_flag = 'Y' ");

		sb = getQueryWhereString(sb, searchCriteria);

		Query result = getSession().createSQLQuery(sb.toString());

		return (Number) result.getSingleResult();
	}

	
	@SuppressWarnings("rawtypes")
	public List<StatusConfirmationVO> getDataConfirmStatusByComplianceReviewId(Long complianceReviewId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT u1.name pic1, u2.name pic2,u3.name pic3, ");
		sb.append(" TO_CHAR(f2.confirmation_date, 'dd-Mon-yyyy')confirmation_date, ");
		sb.append(" TO_CHAR(f2.followup_date, 'dd-Mon-yyyy')followup_date, ");
		sb.append(" f2.followup_note, TO_CHAR(f2.compliance_date, 'dd-Mon-yyyy') compliance_date, ");
		sb.append(" d.name_in compliance_statusIn,d.name_en compliance_statusEn, ");
		sb.append(" compliance_note, ");
		sb.append(" u4.name followupBy,d2.name_in,d2.name_en, f2.cmplc_review_pic_followup_id,  ");
		sb.append(" TO_CHAR(f2.target_date, 'dd-Mon-yyyy')target_date ");
		sb.append(" FROM wo_tmp_comp_rvw_pic_fp f ");
		sb.append(" LEFT JOIN wo_trc_cmplc_review_pic_fp f2 ON f2.cmplc_review_pic_followup_id = f.compliance_rvw_pic_fp_id ");
		sb.append(" LEFT JOIN wo_mst_parameter_dtl d ON f2.compliance_status = d.parameter_dtl_code ");
		sb.append(" LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1  ");
		sb.append(" LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2 ");
		sb.append(" LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3 ");
		sb.append(" LEFT JOIN wo_mst_user u4 ON u4.user_id = f2.followup_by_id  ");
		sb.append(" LEFT JOIN wo_mst_parameter_dtl d2 ON f2.followup_status = d2.parameter_dtl_code ");
		sb.append(" WHERE f.compliance_review_id = :complianceReviewId ");

		sb.append(" ORDER BY f.compliance_rvw_pic_fp_id ASC ");

		Query result = getSession().createSQLQuery(sb.toString());
		result.setParameter("complianceReviewId", complianceReviewId);
		List resultList = result.getResultList();

		List<StatusConfirmationVO> vo = new ArrayList<StatusConfirmationVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				StatusConfirmationVO data = new StatusConfirmationVO();
				data.setPic1(obj[0] != null ? (String) obj[0] : null);
				data.setPic2(obj[1] != null ? (String) obj[1] : null);
				data.setPic3(obj[2] != null ? (String) obj[2] : null);
				data.setConfirmationDate(obj[3] != null ? (String) obj[3] : null);
				data.setFollowupDate(obj[4] != null ? (String) obj[4] : null);
				data.setFollowupNote(obj[5] != null ? (String) obj[5] : null);
				data.setComplianceDate(obj[6] != null ? (String) obj[6] : null);
				data.setComplianceStatusIn(obj[7] != null ? (String) obj[7] : null);
				data.setComplianceStatusEn(obj[8] != null ? (String) obj[8] : null);
				data.setComplianceNote(obj[9] != null ? (String) obj[9] : null);
				data.setFollowupBy(obj[10] != null ? (String) obj[10] : null);
				data.setFollowupStatusIn(obj[11] != null ? (String) obj[11] : null);
				data.setFollowupStatusEn(obj[12] != null ? (String) obj[12] : null);
				data.setPicFollowupId(obj[13] != null ? MathUtil.returnIdObjectToLong(obj[13]) : null);
				data.setTargetDate(obj[14] != null ? (String) obj[14] : null);
				/*try {
					data.setTrcComplianceReviewPicFollowupAttachments(trcComplianceReviewPicFollowupAttachmentDao
							.getPICFollowupAttachmentTrcByPicFollowupId(data.getPicFollowupId()));
				} catch (Exception e) {
					e.printStackTrace();
				}*/
				vo.add(data);
			}
		}

		return vo;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		TrcComplianceReviewViewDaoImpl.logger = logger;
	}

	
	 
}
