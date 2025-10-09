/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpComplianceReview.dao;

//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.tmpComplianceReview.constant.TmpComplianceReviewConstants;
//import com.wo.module.counterType.model.CounterType;
//import com.wo.module.picFollowupConfirmation.dao.PICFollowupConfirmationDao;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReview;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReviewPicFollowupFindings;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReviewPicFollowupPoints;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReviewPicFollowupRegulation;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReviewPicFollowupReview;
import com.wo.module.tmpComplianceReview.vo.ComplianceReviewApprovalVO;
import com.wo.module.tmpComplianceReview.vo.ComplianceReviewVO;
import com.wo.module.tmpComplianceReview.vo.StatusConfirmationVO;
import com.wo.module.trcComplianceReview.dao.TrcComplianceReviewPicFollowupAttachmentDao;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupFindings;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupPoints;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupRegulation;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupReview;


@Repository("complianceReviewDao")
public class ComplianceReviewDaoImpl extends GenericDAOHibernate<TmpComplianceReview, Long>
		implements ComplianceReviewDao {

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
	public List<ComplianceReviewVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		List<ComplianceReviewVO> voList = searchDataCriteria(searchCriteria, first, pageSize);

		return voList;
	}

	@SuppressWarnings("rawtypes")
	private List<ComplianceReviewVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first,
			int pageSize) {

		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT " + 
				"  cr.compliance_review_id, " + 
				"  cr.review_category, " + 
				"  pd.name_in reviewCategoryIn, " + 
				"  pd.name_en reviewCategoryEn, " + 
				"  cr.reviewed_branch, " + 
				"  cr.document_no, " + 
				"  TO_CHAR(cr.document_date, 'dd-Mon-yyyy') document_date, " + 
				"  cr.perihal_in, " + 
				"  cr.perihal_en, " + 
				"  cr.status, " + 
				"  pd2.name_in statusIn, " + 
				"  pd2.name_en statusEn, " + 
				"  cr.follow_up, " +
				"  CASE WHEN ( " + 
				"  SELECT count(1) " + 
				"	FROM wo_trc_cmplc_review_pic_fp f " + 
				"		inner join wo_trc_cmplc_rvw_pc_fp_points fp " + 
				"			ON fp.cmplc_review_pic_followup_id = f.cmplc_review_pic_followup_id " + 
				"	where f.compliance_review_id = cr.compliance_review_id " + 
				"		and fp.followup_date is not null " + 
				"	) = 0 THEN null else 'ada isi' end  followup_Status " +
				"FROM wo_tmp_compliance_review cr " +
				"  LEFT JOIN wo_mst_parameter_dtl pd " + 
				"    ON pd.parameter_dtl_code = cr.review_category " + 
				"  LEFT JOIN wo_mst_parameter_dtl pd2 " + 
				"    ON pd2.parameter_dtl_code = cr.status ");
		sb.append(" WHERE 1=1 AND cr.enabled_flag = 'Y' ");
		
//		sb.append(" SELECT cr.compliance_review_id, cr.review_category, pd.name_in reviewCategoryIn, pd.name_en reviewCategoryEn, cr.reviewed_branch, cr.document_no, ");
//		sb.append(" TO_CHAR(cr.document_date, 'dd-Mon-yyyy') document_date, cr.perihal_in, cr.perihal_en, cr.status,  ");
//		sb.append(" pd2.name_in statusIn, pd2.name_en statusEn,  ");
//		sb.append(" CASE WHEN ( SELECT COUNT(1)  ");
//		sb.append(" FROM wo_trc_cmplc_review_pic_fp f ");
//		sb.append(" WHERE f.compliance_review_id = cr.compliance_review_id ");
//		sb.append(" AND f2.followup_date IS NOT NULL  ");
//		sb.append(" ) = 0 THEN NULL   ");
//		sb.append(" ELSE 'ADA ISI' ");
//		sb.append(" END  AS FOLLOWUP_STATUS, cr.follow_up, pd3.name_in compliance_statusIn, pd3.name_en compliance_statusEn ");
//		sb.append(" FROM wo_tmp_compliance_review cr ");
//		sb.append(" INNER JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = cr.review_category ");
//		sb.append(" INNER JOIN wo_mst_parameter_dtl pd2 ON pd2.parameter_dtl_code = cr.status ");
//		sb.append(" LEFT JOIN wo_trc_cmplc_review_pic_fp f ON f.compliance_review_id = cr.compliance_review_id ");
//		sb.append(" LEFT JOIN wo_trc_cmplc_rvw_pc_fp_points f2 ON f2.cmplc_review_pic_followup_id = f.compliance_rvw_pic_fp_id ");
//		sb.append("	LEFT JOIN wo_mst_parameter_dtl pd3 ON f.compliance_status = pd3.parameter_dtl_code ");
//		sb.append(" WHERE 1=1 AND cr.enabled_flag = 'Y' ");

		sb = getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY cr.compliance_review_id DESC ");
		

		Query result = getSession().createSQLQuery(sb.toString());
		result.setFirstResult(first);
		result.setMaxResults(pageSize);
		
		List resultList = result.getResultList();

		List<ComplianceReviewVO> vo = new ArrayList<ComplianceReviewVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ComplianceReviewVO data = new ComplianceReviewVO();
				data.setComplianceReviewId(obj[0] != null ? (MathUtil.returnIdObjectToLong(obj[0])) : null);
				data.setReviewCategoryCd(obj[1] != null ? (String) obj[1] : null);
				data.setReviewCategoryIn(obj[2] != null ? (String) obj[2] : null);
				data.setReviewCategoryEn(obj[3] != null ? (String) obj[3] : null);
				data.setReviewedBranch(obj[4] != null ? (String) obj[4] : null);
				data.setDocumentNo(obj[5] != null ? (String) obj[5] : null);
				data.setDocumentDateStr(obj[6] != null ? (String) obj[6] : null);
				data.setPerihalIn(obj[7] != null ? (String) obj[7] : null);
				data.setPerihalEn(obj[8] != null ? (String) obj[8] : null);
				data.setStatusCd(obj[9] != null ? (String) obj[9] : null);
				data.setStatusIn(obj[10] != null ? (String) obj[10] : null);
				data.setStatusEn(obj[11] != null ? (String) obj[11] : null);
				//data.setFollowupStatusCd(obj[12] != null ? (String) obj[12] : null);
				data.setStatusTindakLanjut(obj[12] != null ? (String) obj[12] : null);
				//data.setComplianceStatusIn(obj[14] != null ? (String) obj[14] : null);
				//data.setComplianceStatusEn(obj[15] != null ? (String) obj[15] : null);
				data.setFollowupStatusCd(obj[13] != null ? (String) obj[13] : null);
				data.setStatusList(getDataConfirmStatusByComplianceReviewId(data.getComplianceReviewId()));
				data.setPicList(getTrcPicListByComplianceReviewId(data.getComplianceReviewId()));
				data.setPicListTmp(getTmpPicListByComplianceReviewId(data.getComplianceReviewId()));
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
		sb.append(" WHERE fp.cmplc_review_pic_followup_id = :complianceReviewPicFollowupId ");

		sb.append(" ORDER BY fp.cmplc_review_pic_followup_id ASC ");

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
		sb.append(" WHERE fp.cmplc_review_pic_followup_id = :complianceReviewPicFollowupId ");

		sb.append(" ORDER BY fp.cmplc_review_pic_followup_id ASC ");

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
		sb.append(" WHERE fp.cmplc_review_pic_followup_id = :complianceReviewPicFollowupId ");

		sb.append(" ORDER BY fp.cmplc_review_pic_followup_id ASC ");

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
		sb.append(" WHERE r.cmplc_review_pic_followup_id = :complianceReviewPicFollowupId ");

		sb.append(" ORDER BY r.cmplc_review_pic_followup_id ASC ");

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

		sb.append(" SELECT COUNT(1) ");
		sb.append(" FROM wo_tmp_compliance_review cr ");
		sb.append(" INNER JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = cr.review_category ");
		sb.append(" INNER JOIN wo_mst_parameter_dtl pd2 ON pd2.parameter_dtl_code = cr.status ");
		sb.append(" LEFT JOIN wo_trc_cmplc_review_pic_fp f ON f.compliance_review_id = cr.compliance_review_id ");
		sb.append(" LEFT JOIN wo_trc_cmplc_rvw_pc_fp_points f2 ON f2.cmplc_review_pic_followup_id = f.cmplc_review_pic_followup_id  ");
		sb.append("	LEFT JOIN wo_mst_parameter_dtl pd3 ON f.compliance_status = pd3.parameter_dtl_code ");
		sb.append(" WHERE 1=1 AND cr.enabled_flag = 'Y' ");

		sb = getQueryWhereString(sb, searchCriteria);

		Query result = getSession().createSQLQuery(sb.toString());

		return (Number) result.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ComplianceReviewApprovalVO> getDataApprovalByComplianceReviewId(Long complianceReviewId) {
		StringBuilder sb = new StringBuilder();
		sb.append(
				" SELECT u.name, d.name_in, d.name_en, TO_CHAR(approval_date, 'dd-Mon-yyyy')approval_date, approval_note  "
				+ "FROM wo_tmp_compliance_review_appr a "
				+ "INNER JOIN wo_mst_user u ON u.user_id  = a.user_id "
				+ "INNER JOIN wo_mst_parameter_dtl d ON d.parameter_dtl_code = a.approval_status "
				+ "where a.compliance_review_id = :complianceReviewId ");

		sb.append(" ORDER BY compliance_review_approval_id ASC ");

		Query result = getSession().createSQLQuery(sb.toString());
		result.setParameter("complianceReviewId", complianceReviewId);
		List resultList = result.getResultList();

		List<ComplianceReviewApprovalVO> vo = new ArrayList<ComplianceReviewApprovalVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ComplianceReviewApprovalVO data = new ComplianceReviewApprovalVO();
				data.setApprovalBy(obj[0] != null ? (String) obj[0] : null);
				data.setApprovalStatusIn(obj[1] != null ? (String) obj[1] : null);
				data.setApprovalStatusEn(obj[2] != null ? (String) obj[2] : null);
				data.setApprovalDate(obj[3] != null ? (String) obj[3] : null);
				data.setApprovalNote(obj[4] != null ? (String) obj[4] : null);
				vo.add(data);
			}
		}

		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<StatusConfirmationVO> getDataConfirmStatusByComplianceReviewId(Long complianceReviewId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT u1.name pic1, u2.name pic2,u3.name pic3, ");
		sb.append(" TO_CHAR(f2.confirmation_date, 'dd-Mon-yyyy')confirmation_date, "); 
		sb.append(" TO_CHAR(f2.followup_date, 'dd-Mon-yyyy')followup_date, ");
		sb.append(" f2.followup_note, TO_CHAR(fol.compliance_date, 'dd-Mon-yyyy') compliance_date, ");
		sb.append(" d.name_in compliance_statusIn,d.name_en compliance_statusEn, ");
		sb.append(" compliance_note, ");
		sb.append(" u4.name followupBy,d2.name_in,d2.name_en, f2.cmplc_review_pic_followup_id,  ");
		sb.append(" TO_CHAR(f.target_date, 'dd-Mon-yyyy')target_date, ");
		sb.append(" f2.CMPLC_RVW_PIC_FP_POINTS_ID ");
		sb.append(" FROM wo_tmp_comp_rvw_pic_fp f ");
		sb.append(" LEFT JOIN wo_trc_cmplc_review_pic_fp fol ON fol.compliance_review_id = f.compliance_review_id ");
		sb.append(" LEFT JOIN wo_trc_cmplc_rvw_pc_fp_points f2 ON f2.cmplc_review_pic_followup_id = fol.cmplc_review_pic_followup_id ");
		sb.append(" LEFT JOIN wo_mst_parameter_dtl d ON fol.compliance_status = d.parameter_dtl_code ");
		sb.append(" LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1  ");
		sb.append(" LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2 ");
		sb.append(" LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3 ");
		sb.append(" LEFT JOIN wo_mst_user u4 ON u4.user_id = f2.followup_by_id  ");
		sb.append(" LEFT JOIN wo_mst_parameter_dtl d2 ON fol.followup_status = d2.parameter_dtl_code ");
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
				data.setPicFollowupPointsId(obj[15] != null ? MathUtil.returnIdObjectToLong(obj[15]) : null);
				try {
					data.setTrcComplianceReviewPicFollowupAttachments(trcComplianceReviewPicFollowupAttachmentDao
							.getPICFollowupAttachmentTrcByPicFollowupId(data.getPicFollowupPointsId()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				vo.add(data);
			}
		}

		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<StatusConfirmationVO> getDataPicFollowupComplianceReviewId(Long complianceReviewId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT " + 
				"  u1.name pic1, " + 
				"  u2.name pic2, " + 
				"  u3.name pic3 " + 
				"FROM wo_tmp_comp_rvw_pic_fp f " + 
				"  LEFT JOIN wo_mst_user u1 " + 
				"    ON u1.user_id = f.user_id_1 " + 
				"  LEFT JOIN wo_mst_user u2 " + 
				"    ON u2.user_id = f.user_id_2 " + 
				"  LEFT JOIN wo_mst_user u3 " + 
				"    ON u3.user_id = f.user_id_3 ");
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
				
				vo.add(data);
			}
		}

		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	public List<StatusConfirmationVO> getDataConfirmStatusByFollowup(Long complianceReviewId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT u1.name pic1, u2.name pic2,u3.name pic3, ");
		sb.append(" TO_CHAR(fol.compliance_date, 'dd-Mon-yyyy') compliance_date, ");
		sb.append(" d.name_in compliance_statusIn,d.name_en compliance_statusEn, ");
		sb.append(" compliance_note, ");
		sb.append(" d2.name_in,d2.name_en, ");
		sb.append(" TO_CHAR(f.target_date, 'dd-Mon-yyyy')target_date");
		sb.append(" FROM wo_tmp_comp_rvw_pic_fp f ");
		sb.append(" LEFT JOIN wo_trc_cmplc_review_pic_fp fol ON fol.compliance_review_id = f.compliance_review_id ");
		sb.append(" LEFT JOIN wo_mst_parameter_dtl d ON fol.compliance_status = d.parameter_dtl_code ");
		sb.append(" LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1  ");
		sb.append(" LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2 ");
		sb.append(" LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3 ");
		sb.append(" LEFT JOIN wo_mst_parameter_dtl d2 ON fol.followup_status = d2.parameter_dtl_code ");
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
				data.setPicFollowupPointsId(obj[15] != null ? MathUtil.returnIdObjectToLong(obj[15]) : null);
				try {
					data.setTrcComplianceReviewPicFollowupAttachments(trcComplianceReviewPicFollowupAttachmentDao
							.getPICFollowupAttachmentTrcByPicFollowupId(data.getPicFollowupPointsId()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				vo.add(data);
			}
		}

		return vo;
	}

	@Override
	public Boolean hasReachedMaximumReschedule(Long complianceReviewPicFollowupId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT COUNT(1) ");
		sb.append(" FROM WO_TMP_CMPLC_RVW_PIC_FP_RESC ");
		sb.append(" where 1=1 ");
		sb.append(" and COMPLIANCE_RVW_PIC_FP_ID = :complianceReviewPicFollowupId ");
		Query query = getSession().createSQLQuery(sb.toString());

		query.setParameter("complianceReviewPicFollowupId", complianceReviewPicFollowupId);
		
		Number count = (Number) query.getSingleResult();
		if(count == null) {
			count = 0;
		}
		
		StringBuilder sb2 = new StringBuilder();
		sb2.append(" SELECT name_in FROM wo_mst_parameter_dtl where parameter_dtl_code = 'MAX_RESCHEDULE_DATE' ");
	
		Query query2 = getSession().createSQLQuery(sb2.toString());

		Long maxRescheduleDate = new Long(((String) query2.getSingleResult()) );
		
		if (count.longValue() >= maxRescheduleDate.longValue()) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ComplianceReviewVO> searchDataXLS(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT " + 
				"  cr.compliance_review_id, " + 
				"  cr.review_category, " + 
				"  pd.name_in reviewCategoryIn, " + 
				"  pd.name_en reviewCategoryEn, " + 
				"  cr.reviewed_branch, " + 
				"  cr.document_no, " + 
				"  TO_CHAR(cr.document_date, 'dd-Mon-yyyy') document_date, " + 
				"  cr.perihal_in, " + 
				"  cr.perihal_en, " + 
				"  cr.status, " + 
				"  pd2.name_in statusIn, " + 
				"  pd2.name_en statusEn, " + 
				"  cr.follow_up " + 
				"FROM wo_tmp_compliance_review cr " +
				"  LEFT JOIN wo_mst_parameter_dtl pd " + 
				"    ON pd.parameter_dtl_code = cr.review_category " + 
				"  LEFT JOIN wo_mst_parameter_dtl pd2 " + 
				"    ON pd2.parameter_dtl_code = cr.status ");
//		sb.append(" SELECT cr.compliance_review_id, pd.name_in reviewCategoryIn, pd.name_en reviewCategoryEn, cr.reviewed_branch, cr.document_no, ");
//		sb.append(" TO_CHAR(cr.document_date, 'dd-Mon-yyyy') document_date, cr.perihal_in, cr.perihal_en, cr.status,");
//		sb.append(" pd2.name_in statusIn, pd2.name_en statusEn,");
//		sb.append(" u1.name pic1, u2.name pic2, u3.name pic3, ");
//		sb.append(" TO_CHAR(CURDATE(), 'dd-Mon-yyyy') confirmation_date, ");
//		sb.append(" TO_CHAR(CURDATE(), 'dd-Mon-yyyy') followup_date, ");
//		sb.append(" '', TO_CHAR(f2.compliance_date, 'dd-Mon-yyyy') compliance_date, ");
//		sb.append(" pd3.name_in compliance_statusIn, pd3.name_en compliance_statusEn, f2.compliance_note, ");
//		sb.append(" u4.name followupBy, pd4.name_in followup_status_in, pd4.name_en followup_status_en, cr.follow_up, TO_CHAR(f.target_date, 'dd-Mon-yyyy') target_date, ");
//		sb.append(" f.notes ");
//		sb.append(" FROM wo_tmp_compliance_review cr ");
//		sb.append(" INNER JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = cr.review_category");
//		sb.append(" INNER JOIN wo_mst_parameter_dtl pd2 ON pd2.parameter_dtl_code = cr.status ");
//		sb.append(" LEFT JOIN wo_tmp_comp_rvw_pic_fp f ON f.compliance_review_id = cr.compliance_review_id ");
//		sb.append(" LEFT JOIN wo_trc_cmplc_review_pic_fp f2 ON f2.cmplc_review_pic_followup_id = f.compliance_rvw_pic_fp_id ");
//		sb.append(" LEFT JOIN wo_mst_parameter_dtl pd3 ON pd3.parameter_dtl_code = f2.compliance_status");
//		sb.append(" LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1 ");
//		sb.append(" LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2 ");
//		sb.append(" LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3 ");
//		sb.append(" LEFT JOIN wo_mst_user u4 ON u4.user_id = f2.followup_by_id ");
//		sb.append(" LEFT JOIN wo_mst_parameter_dtl pd4 ON pd4.parameter_dtl_code = f2.followup_status");
//		sb.append(" WHERE 1 = 1 ");
//		sb.append(" AND cr.enabled_flag = 'Y' ");
				
		sb = getQueryWhereXLSString(sb, searchCriteria);
		sb.append(" ORDER BY cr.compliance_review_id DESC ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		List resultList = result.getResultList();

		List<ComplianceReviewVO> complianceReviewVoList = new ArrayList<ComplianceReviewVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ComplianceReviewVO data = new ComplianceReviewVO();
				data.setComplianceReviewId(obj[0] != null ? (MathUtil.returnIdObjectToLong(obj[0])) : null);
				data.setReviewCategoryCd(obj[1] != null ? (String) obj[1] : null);
				data.setReviewCategoryIn(obj[2] != null ? (String) obj[2] : null);
				data.setReviewCategoryEn(obj[3] != null ? (String) obj[3] : null);
				data.setReviewedBranch(obj[4] != null ? (String) obj[4] : null);
				data.setDocumentNo(obj[5] != null ? (String) obj[5] : null);
				data.setDocumentDateStr(obj[6] != null ? (String) obj[6] : null);
				data.setPerihalIn(obj[7] != null ? (String) obj[7] : null);
				data.setPerihalEn(obj[8] != null ? (String) obj[8] : null);
				data.setStatusCd(obj[9] != null ? (String) obj[9] : null);
				data.setStatusIn(obj[10] != null ? (String) obj[10] : null);
				data.setStatusEn(obj[11] != null ? (String) obj[11] : null);
				//data.setFollowupStatusCd(obj[12] != null ? (String) obj[12] : null);
				data.setStatusTindakLanjut(obj[12] != null ? (String) obj[12] : null);
				//data.setComplianceStatusIn(obj[14] != null ? (String) obj[14] : null);
				//data.setComplianceStatusEn(obj[15] != null ? (String) obj[15] : null);
				//data.setStatusList(getDataConfirmStatusByComplianceReviewId(data.getComplianceReviewId()));
				data.setPicList(getTrcPicListByComplianceReviewId(data.getComplianceReviewId()));
				data.setPicListTmp(getTmpPicListByComplianceReviewId(data.getComplianceReviewId()));
				
				complianceReviewVoList.add(data);
			}
		}
		
//		if (resultList != null) {
//			for (int i = 0; i < resultList.size(); i++) {
//				Object[] obj = (Object[]) resultList.get(i);
//				ComplianceReviewVO data = new ComplianceReviewVO();
//				
//				data.setComplianceReviewId(obj[0] != null ? (MathUtil.returnIdObjectToLong(obj[0])) : null);
//				data.setReviewCategoryIn(obj[1] != null ? (String) obj[1] : null);
//				data.setReviewCategoryEn(obj[2] != null ? (String) obj[2] : null);
//				data.setReviewedBranch(obj[3] != null ? (String) obj[3] : null);
//				data.setDocumentNo(obj[4] != null ? (String) obj[4] : null);
//				data.setDocumentDateStr(obj[5] != null ? (String) obj[5] : null);
//				data.setPerihalIn(obj[6] != null ? (String) obj[6] : null);
//				data.setPerihalEn(obj[7] != null ? (String) obj[7] : null);
//				data.setStatusCd(obj[8] != null ? (String) obj[8] : null);
//				data.setStatusIn(obj[9] != null ? (String) obj[9] : null);
//				data.setStatusEn(obj[10] != null ? (String) obj[10] : null);
//				data.setStatusList(new ArrayList<>());
//				StatusConfirmationVO dataConfirm = new StatusConfirmationVO();
//				dataConfirm.setNamePic(obj[10] != null ? (String) obj[11] : null);
//				if(obj[11] != null) {
//					dataConfirm.setNamePic((String) obj[11]);
//				}
//				
//				if(obj[12] != null) {
//					dataConfirm.setNamePic(dataConfirm.getNamePic() +", " + (String) obj[12]);
//				}
//				
//				if(obj[13] != null) {
//					dataConfirm.setNamePic(dataConfirm.getNamePic() +", " + (String) obj[13]);
//				}
//				
//				dataConfirm.setConfirmationDate(obj[14] != null ? (String) obj[14] : null);
//				dataConfirm.setFollowupDate(obj[15] != null ? (String) obj[15] : null);				
//				dataConfirm.setFollowupNote(obj[16] != null ? (String) obj[16] : null);
//				dataConfirm.setComplianceDate(obj[17] != null ? (String) obj[17] : null);
//				dataConfirm.setComplianceStatusIn(obj[18] != null ? (String) obj[18] : null);
//				dataConfirm.setComplianceStatusEn(obj[19] != null ? (String) obj[19] : null);
//				dataConfirm.setComplianceNote(obj[20] != null ? (String) obj[20] : null);
//				dataConfirm.setFollowupBy(obj[21] != null ? (String) obj[21] : null);
//				dataConfirm.setFollowupStatusIn(obj[22] != null ? (String) obj[22] : null);
//				dataConfirm.setFollowupStatusEn(obj[23] != null ? (String) obj[23] : null);
//				data.setStatusTindakLanjut(obj[24] != null ? (String) obj[24] : null);
//				dataConfirm.setTargetDate(obj[25] != null ? (String) obj[25] : null);
//				dataConfirm.setFollowupNote(obj[26] != null ? (String) obj[26] : null);
//				
//				data.getStatusList().add(dataConfirm);
//				
//				complianceReviewVoList.add(data);
//			}
//		}

		return complianceReviewVoList;
	}

	 @SuppressWarnings({ "rawtypes", "static-access" })
		private StringBuilder getQueryWhereXLSString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
	  		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
	  		if (searchCriteria != null) {
	  			for (SearchObject searchVal : searchCriteria) {
	  				String col = searchVal.getSearchColumn();
	  				String val = searchVal.getSearchValue() != null ? searchVal.getSearchValueAsString() : "";
	  				
	  				if (!StringUtils.isBlank(val)) {
						if (StringUtils.equals(TmpComplianceReviewConstants.WHERE_REVIEW_CATEGORY, col)) {
							sb.append(" and cr.review_category = '" + val + "' ");
						} else if (StringUtils.equals(TmpComplianceReviewConstants.WHERE_REVIEWED_BRANCH, col)) {
							sb.append(" and cr.reviewed_branch LIKE '%" + val + "%' ");
						} else if (StringUtils.equals(TmpComplianceReviewConstants.WHERE_DOC_NO, col)) {
							sb.append(" and cr.document_no = '" + val + "' ");
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
	 
	@Override
	public Number countDocNo(String complianceReviewDocNo) {
		
		StringBuilder sb = new StringBuilder();

		sb.append(" SELECT COUNT(document_no) FROM wo_tmp_compliance_review "
				+ "WHERE UPPER(document_no) = '" + complianceReviewDocNo.toUpperCase() + "' "
				+ "AND enabled_flag = 'Y' ");

		Query result = getSession().createSQLQuery(sb.toString());

		return (Number) result.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<StatusConfirmationVO> getTmpPicListByComplianceReviewId(Long complianceReviewId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT " + 
				"  u1.name pic1, " + 
				"  u2.name pic2, " + 
				"  u3.name pic3, " +
				"  TO_CHAR(f.target_date, 'dd-Mon-yyyy') target_date, " +
				"  f.compliance_rvw_pic_fp_id " +
				"FROM wo_tmp_comp_rvw_pic_fp f " + 
				"  LEFT JOIN wo_mst_user u1 " + 
				"    ON u1.user_id = f.user_id_1 " + 
				"  LEFT JOIN wo_mst_user u2 " + 
				"    ON u2.user_id = f.user_id_2 " + 
				"  LEFT JOIN wo_mst_user u3 " + 
				"    ON u3.user_id = f.user_id_3 " );
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
				data.setTargetDate(obj[3] != null ? (String) obj[3] : null);
				data.setComplianceReviewPicFollowupIdTmp(obj[4] != null ? MathUtil.returnIdObjectToLong(obj[4]) : null);
				data.setPointListTmp(getTmpFollowupPointsByComplianceReviewPicFollowupId(data.getComplianceReviewPicFollowupIdTmp()));
				data.setAreaReviewListTmp(getTmpFollowupAreaReviewsByComplianceReviewPicFollowupId(data.getComplianceReviewPicFollowupIdTmp()));
				data.setFindingsListTmp(getTmpFollowupFindingsByComplianceReviewPicFollowupId(data.getComplianceReviewPicFollowupIdTmp()));
				data.setRegulationTmp(getTmpFollowupRegulationByComplianceReviewPicFollowupId(data.getComplianceReviewPicFollowupIdTmp()));
				
				vo.add(data);
			}
		}

		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	public List<TmpComplianceReviewPicFollowupPoints> getTmpFollowupPointsByComplianceReviewPicFollowupId(Long complianceReviewPicFollowupId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT fp.followup_points " +
				" FROM wo_tmp_cmplc_rvw_pc_fp_points fp ");
		sb.append(" WHERE fp.COMPLIANCE_RVW_PIC_FP_ID = :complianceReviewPicFollowupId ");

		sb.append(" ORDER BY fp.CMPLC_RVW_PC_FP_POINTS_ID ASC ");

		Query result = getSession().createSQLQuery(sb.toString());
		result.setParameter("complianceReviewPicFollowupId", complianceReviewPicFollowupId);
		List resultList = result.getResultList();

		List<TmpComplianceReviewPicFollowupPoints> vo = new ArrayList<TmpComplianceReviewPicFollowupPoints>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				String obj = (String) resultList.get(i);
				TmpComplianceReviewPicFollowupPoints data = new TmpComplianceReviewPicFollowupPoints();
				data.setFollowupPoints(obj);
				vo.add(data);
			}
		}

		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	public List<TmpComplianceReviewPicFollowupFindings> getTmpFollowupFindingsByComplianceReviewPicFollowupId(Long complianceReviewPicFollowupId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT fp.findings " +
				"	FROM wo_tmp_cmplc_rvw_pc_fp_find fp ");
		sb.append(" WHERE fp.COMPLIANCE_RVW_PIC_FP_ID = :complianceReviewPicFollowupId ");

		sb.append(" ORDER BY fp.COMPLIANCE_RVW_PC_FP_FIND_ID ASC ");

		Query result = getSession().createSQLQuery(sb.toString());
		result.setParameter("complianceReviewPicFollowupId", complianceReviewPicFollowupId);
		List resultList = result.getResultList();

		List<TmpComplianceReviewPicFollowupFindings> vo = new ArrayList<TmpComplianceReviewPicFollowupFindings>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				String obj = (String) resultList.get(i);
				TmpComplianceReviewPicFollowupFindings data = new TmpComplianceReviewPicFollowupFindings();
				data.setFindings(obj);
				
				vo.add(data);
			}
		}

		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	public List<TmpComplianceReviewPicFollowupReview> getTmpFollowupAreaReviewsByComplianceReviewPicFollowupId(Long complianceReviewPicFollowupId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT fp.area_review " +
				"FROM wo_tmp_cmplc_rvw_pic_fp_rvw fp ");
		sb.append(" WHERE fp.COMPLIANCE_RVW_PIC_FP_ID = :complianceReviewPicFollowupId ");

		sb.append(" ORDER BY fp.CMPLC_RVW_PIC_FP_RVW_ID ASC ");

		Query result = getSession().createSQLQuery(sb.toString());
		result.setParameter("complianceReviewPicFollowupId", complianceReviewPicFollowupId);
		List resultList = result.getResultList();

		List<TmpComplianceReviewPicFollowupReview> vo = new ArrayList<TmpComplianceReviewPicFollowupReview>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				String obj = (String) resultList.get(i);
				TmpComplianceReviewPicFollowupReview data = new TmpComplianceReviewPicFollowupReview();
				data.setAreaReview(obj);
				
				vo.add(data);
			}
		}

		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	private  List<TmpComplianceReviewPicFollowupRegulation> getTmpFollowupRegulationByComplianceReviewPicFollowupId(Long complianceReviewPicFollowupId){
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT r1.document_no " +
				" FROM wo_tmp_cmplc_rvw_pc_fp_reg r "
				+"	LEFT JOIN wo_mst_regulation r1 "
				+ "		ON r.regulation_id = r1.regulation_id ");
		sb.append(" WHERE r.COMPLIANCE_RVW_PIC_FP_ID = :complianceReviewPicFollowupId ");

		sb.append(" ORDER BY r.CMPLC_RVW_PC_FP_REG_ID ASC ");

		Query result = getSession().createSQLQuery(sb.toString());
		result.setParameter("complianceReviewPicFollowupId", complianceReviewPicFollowupId);
		List resultList = result.getResultList();

		List<TmpComplianceReviewPicFollowupRegulation> vo = new ArrayList<TmpComplianceReviewPicFollowupRegulation>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				String obj = (String) resultList.get(i);
				TmpComplianceReviewPicFollowupRegulation data = new TmpComplianceReviewPicFollowupRegulation();
				data.setDocumentNo(obj);
				
				vo.add(data);
			}
		}

		return vo;
	}
}
