/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationSocialization.dao;

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
//import com.wo.module.counterType.model.CounterType;
//import com.wo.module.picFollowupConfirmation.dao.PICFollowupConfirmationDao;
import com.wo.module.regulationSocialization.constant.RegulationSocializationConstants;
import com.wo.module.regulationSocialization.model.SocializationTmp;
import com.wo.module.regulationSocialization.vo.RegulationSocializationVO;
import com.wo.module.regulationSocialization.vo.SocializationApprovalVO;
import com.wo.module.regulationSocialization.vo.StatusConfirmationVO;

/**
 *
 * @author hendra
 */

@Repository("regulationSocializationDao")
public class RegulationSocializationDaoImpl extends GenericDAOHibernate<SocializationTmp, Long>
		implements RegulationSocializationDao {
	
	@Autowired
	@Qualifier("socializationPICFollowupAttachmentTrcDao")
	private SocializationPICFollowupAttachmentTrcDao socializationPICFollowupAttachmentTrcDao;

	public SocializationPICFollowupAttachmentTrcDao getSocializationPICFollowupAttachmentTrcDao() {
		return socializationPICFollowupAttachmentTrcDao;
	}

	public void setSocializationPICFollowupAttachmentTrcDao(
			SocializationPICFollowupAttachmentTrcDao socializationPICFollowupAttachmentTrcDao) {
		this.socializationPICFollowupAttachmentTrcDao = socializationPICFollowupAttachmentTrcDao;
	}

	@SuppressWarnings({ "rawtypes", "static-access", "unused" })
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		//SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		//SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue() != null ? searchVal.getSearchValueAsString() : "";
				Object valReal = searchVal.getSearchValue();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(RegulationSocializationConstants.WHERE_PROV_TYPE, col)) {
						sb.append(" and s.jenis_ketentuan = '" + val + "' ");
					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_DOC_TYPE, col)) {
						sb.append(" and dt.document_type_id = " + val + " ");
					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_CATEGORY, col)) {
						sb.append(" and dc.document_category_id = " + val + " ");
					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_TOPIC, col)) {
						sb.append(" and dp.document_topic_id = " + val + " ");
					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_DOC_NO, col)) {
						sb.append(" and r.document_no like '%" + val + "%' ");
					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_NAME, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and UPPER(r.name_en) LIKE UPPER('%" + val + "%') ");
						} else {
							sb.append(" and UPPER(r.name_in) LIKE UPPER('%" + val + "%') ");
						}
					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_PUBLISHED_DATE_START, col)) {
						
						sb.append(" and TRUNC(r.published_date) >= TO_DATE ('" +  val + "', 'yyyy-MM-dd') ");
						
					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_PUBLISHED_DATE_END, col)) {
						
						sb.append(" and TRUNC(r.published_date) <= TO_DATE ('" + val + "', 'yyyy-MM-dd') ");
						
					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_EFF_DATE_START, col)) {
						sb.append(" and TRUNC(r.effective_date) >= TO_DATE ('" + val + "', 'yyyy-MM-dd') ");
						
					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_EFF_DATE_END, col)) {
						sb.append(" and TRUNC(r.effective_date) <= TO_DATE ('" + val + "', 'yyyy-MM-dd') ");
						
					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_TARGET_DATE_START, col)) {						
						sb.append(" and EXISTS( SELECT " + "            1" + "        FROM"
								+ "            wo_tmp_socialization_pic_fp spf" + "        WHERE"
								+ "            spf.socialization_id = s.socialization_id"
								+ "                AND TRUNC(spf.target_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ) ");
						
					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_TARGET_DATE_END, col)) {						
						sb.append(" and EXISTS( SELECT " + "            1" + "        FROM"
								+ "            wo_tmp_socialization_pic_fp spf" + "        WHERE"
								+ "            spf.socialization_id = s.socialization_id"
								+ "                AND TRUNC(spf.target_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd')  ) ");						
					}

					else if (StringUtils.equals(RegulationSocializationConstants.WHERE_STATUS, col)) {
						sb.append(" and s.status = '" + val + "' ");
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
		sb.append(" select count(1) ");
		sb.append(" FROM wo_tmp_socialization s ");
		sb.append(" inner join wo_tmp_scialization_regulation sr on s.socialization_id = sr.socialization_id ");
		sb.append(" inner join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = s.jenis_ketentuan  ");
		sb.append(" inner join wo_mst_regulation r on r.regulation_id = sr.regulation_id  ");
		sb.append(" left join wo_mst_document_type dt on r.document_type_id = dt.document_type_id  ");
		sb.append(" left join wo_mst_document_category dc on dc.document_category_id = r.document_category_id  ");
		sb.append(" left join wo_mst_document_topic dp on dp.document_topic_id = r.document_topic_id  ");
		sb.append(" WHERE 1=1 and s.enabled_flag = 'Y' and sr.primary_flag = 'Y' ");

		sb = getQueryWhereString(sb, searchCriteria);

		Query result = getSession().createSQLQuery(sb.toString());

		return (Number) result.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<RegulationSocializationVO> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {

		List<RegulationSocializationVO> voList = searchDataCriteria(searchCriteria, first, pageSize);

		return voList;
	}

	@SuppressWarnings("rawtypes")
	private List<RegulationSocializationVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first,
			int pageSize) {

		StringBuilder sb = new StringBuilder();
		sb.append(" select pd.name_in jnsKetentuanIn, pd.name_en jnsKetentuanEn, ");
		sb.append(" dt.document_type_in, dt.document_type_en,  ");
		sb.append(" dc.document_category_in, dc.document_category_en, dp.document_topic_in, dp.document_topic_en,  ");
		sb.append(" r.document_no, r.name_in, r.name_en, '' PICName, ");
		sb.append(" pd2.name_in statusIn,pd2.name_en statusEn,s.socialization_id, s.status, ");
		sb.append(" CASE WHEN ( select count(1)  ");
		sb.append("		from wo_trc_socialization_pic_fp f ");
		sb.append("		where f.socialization_id = s.socialization_id ");
		sb.append("		and f.followup_date is not null ");
		sb.append("		) = 0 THEN null   ");
		sb.append("		ELSE 'ADA ISI' ");
		sb.append("	END  as FOLLOWUP_STATUS, TO_CHAR(r.published_date, 'dd-Mon-yyyy') published_date, TO_CHAR(r.expired_date, 'dd-Mon-yyyy') effective_date,");
		sb.append("	s.follow_up ");
		sb.append(" FROM wo_tmp_socialization s ");
		sb.append(" inner join wo_tmp_scialization_regulation sr on s.socialization_id = sr.socialization_id ");
		sb.append(" inner join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = s.jenis_ketentuan  ");
		sb.append(" inner join wo_mst_regulation r on r.regulation_id = sr.regulation_id  ");
		sb.append(" left join wo_mst_document_type dt on r.document_type_id = dt.document_type_id  ");
		sb.append(" left join wo_mst_document_category dc on dc.document_category_id = r.document_category_id  ");
		sb.append(" left join wo_mst_document_topic dp on dp.document_topic_id = r.document_topic_id  ");
		sb.append(" inner join wo_mst_parameter_dtl pd2 on pd2.parameter_dtl_code = s.status  ");
		sb.append(" WHERE 1=1 and s.enabled_flag = 'Y' and sr.primary_flag = 'Y' ");

		sb = getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY r.published_date DESC ");
		
		
		Query result = getSession().createSQLQuery(sb.toString());
		result.setFirstResult(first);
		result.setMaxResults(pageSize);
		
		List resultList = result.getResultList();

		List<RegulationSocializationVO> vo = new ArrayList<RegulationSocializationVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				RegulationSocializationVO data = new RegulationSocializationVO();
				data.setJenisKetentuanIn(obj[0] != null ? (String) obj[0] : null);
				data.setJenisKetentuanEn(obj[1] != null ? (String) obj[1] : null);
				data.setTipeDokumenIn(obj[2] != null ? (String) obj[2] : null);
				data.setTipeDokumenEn(obj[3] != null ? (String) obj[3] : null);
				data.setKategoriIn(obj[4] != null ? (String) obj[4] : null);
				data.setKategoriEn(obj[5] != null ? (String) obj[5] : null);
				data.setTopikIn(obj[6] != null ? (String) obj[6] : null);
				data.setTopikEn(obj[7] != null ? (String) obj[7] : null);
				data.setNoDokumen(obj[8] != null ? (String) obj[8] : null);
				data.setJdlPeraturanIn(obj[9] != null ? (String) obj[9] : null);
				data.setJdlPeraturanEn(obj[10] != null ? (String) obj[10] : null);
				data.setNamaPIC(obj[11] != null ? (String) obj[11] : null);
				data.setStatusIn(obj[12] != null ? (String) obj[12] : null);
				data.setStatusEn(obj[13] != null ? (String) obj[13] : null);
				//data.setSocializationId(obj[14] != null ? ((java.math.BigInteger) obj[14]).longValue() : null);
				data.setSocializationId(obj[14] != null ? (MathUtil.returnIdObjectToLong(obj[14])) : null);
				data.setStatusList(getDataConfirmStatusBySocializationId(data.getSocializationId()));
				data.setStatusCd(obj[15] != null ? (String) obj[15] : null);
				data.setFollowupStatusCd(obj[16] != null ? (String) obj[16] : null);
				data.setPublishedDateStr(obj[17] != null ? (String) obj[17] : null);
				data.setEffectiveDateStr(obj[18] != null ? (String) obj[18] : null);
				data.setStatusTindakLanjut(obj[19] != null ? (String) obj[19] : null);
				
				vo.add(data);
			}
		}

		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	public List<SocializationApprovalVO> getDataApprovalBySocializationId(Long socializationId) {
		StringBuilder sb = new StringBuilder();
		sb.append(
				" select u.name,d.name_in,d.name_en,TO_CHAR(approval_date, 'dd-Mon-yyyy')approval_date,approval_note  from wo_tmp_socialization_approval a "
						+ "inner join wo_mst_user u on a.user_id =u.user_id "
						+ "inner join wo_mst_parameter_dtl d on a.approval_status = d.parameter_dtl_code "
						+ "where a.socialization_id = :socializationId ");

		sb.append(" ORDER BY socialization_approval_id ASC ");

		Query result = getSession().createSQLQuery(sb.toString());
		result.setParameter("socializationId", socializationId);
		List resultList = result.getResultList();

		List<SocializationApprovalVO> vo = new ArrayList<SocializationApprovalVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				SocializationApprovalVO data = new SocializationApprovalVO();
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
	public List<StatusConfirmationVO> getDataConfirmStatusBySocializationId(Long socializationId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select u1.name pic1, u2.name pic2,u3.name pic3, "
				+ "TO_CHAR(f2.confirmation_date, 'dd-Mon-yyyy')confirmation_date, "
				+ "TO_CHAR(f2.followup_date, 'dd-Mon-yyyy')followup_date, "
				+ "f2.followup_note, TO_CHAR(f2.compliance_date, 'dd-Mon-yyyy') compliance_date, "
				+ "d.name_in compliance_statusIn,d.name_en compliance_statusEn, "
				+ "compliance_note, "
				+ "u4.name followupBy,d2.name_in,d2.name_en, f2.socialization_pic_followup_id, " 
				+ "TO_CHAR(f.target_date, 'dd-Mon-yyyy')target_date "
				+ "from wo_tmp_socialization_pic_fp f "
				+ "left join wo_trc_socialization_pic_fp f2 on f2.socialization_pic_followup_id = f.socialization_pic_followup_id "
				+ "left join wo_mst_parameter_dtl d on f2.compliance_status = d.parameter_dtl_code "
				+ "left join wo_mst_user u1 on u1.user_id = f.user_id_1  "
				+ "left join wo_mst_user u2 on u2.user_id = f.user_id_2 "
				+ "left join wo_mst_user u3 on u3.user_id = f.user_id_3 "
				+ "left join wo_mst_user u4 on u4.user_id = f2.followup_by_id  "
				+ "left join wo_mst_parameter_dtl d2 on f2.followup_status = d2.parameter_dtl_code "
				+ "where f.socialization_id = :socializationId ");

		sb.append(" ORDER BY f.socialization_pic_followup_id ASC ");

		Query result = getSession().createSQLQuery(sb.toString());
		result.setParameter("socializationId", socializationId);
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
				data.setTargetDate(obj[14] != null ? (String)obj[14] : null);
				try {
					data.setSocializationPICFollowupAttachmentTrcs(socializationPICFollowupAttachmentTrcDao.getPICFollowupAttachmentTrcBySocializationId(data.getPicFollowupId()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				vo.add(data);
			}
		}

		return vo;
	}

	
	public Boolean hasReachedMaximumReschedule(Long socializationPicFollowupId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT COUNT(1) ");
		sb.append(" FROM WO_TMP_SOCIALZTN_PIC_FP_RSCHDL  ");
		sb.append(" where 1=1 ");
		sb.append(" and socialization_pic_followup_id = :socializationPicFollowupId ");
		Query query = getSession().createSQLQuery(sb.toString());

		query.setParameter("socializationPicFollowupId", socializationPicFollowupId);
		
		Number count = (Number) query.getSingleResult();
		if(count == null) {
			count = 0;
		}
		
		StringBuilder sb2 = new StringBuilder();
		sb2.append(" SELECT name_in FROM wo_mst_parameter_dtl where parameter_dtl_code = 'MAX_RESCHEDULE_DATE' ");
	
		Query query2 = getSession().createSQLQuery(sb2.toString());

		Long maxRescheduleDate = new Long(((String) query2.getSingleResult()) );
		
		if( count.longValue() >= maxRescheduleDate.longValue() ) {
			return true;
		} else {
			return false;
		}

	}	
	
	@SuppressWarnings("rawtypes")
	public List<RegulationSocializationVO> searchDataXLS(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT s.socialization_id, pd.name_in jnsKetentuanIn, pd.name_en jnsKetentuanEn, dt.document_type_in, ");
		sb.append("        dt.document_type_en, dc.document_category_in, dc.document_category_en, dp.document_topic_in, ");
		sb.append("        dp.document_topic_en, r.document_no, r.name_in JdlPeraturanIn, r.name_en JdlPeraturanEn, pd2.name_in statusIn, ");
		sb.append("        pd2.name_en statusEn, s.status, ");
		sb.append("        TO_CHAR(r.published_date, 'dd-Mon-yyyy') published_date, ");
		sb.append("        TO_CHAR(r.expired_date, 'dd-Mon-yyyy') effective_date, ");
		sb.append("        u1.name pic1, u2.name pic2, u3.name pic3, ");
		sb.append("        TO_CHAR(f2.confirmation_date, 'dd-Mon-yyyy') confirmation_date, ");
		sb.append("        TO_CHAR(f2.followup_date, 'dd-Mon-yyyy') followup_date, ");
		sb.append("        f2.followup_note, TO_CHAR(f2.compliance_date, 'dd-Mon-yyyy') compliance_date, ");
		sb.append("        d.name_in compliance_statusIn, d.name_en compliance_statusEn, compliance_note, ");
		sb.append("        u4.name followupBy, d2.name_in followup_status_in, d2.name_en followup_status_en, s.follow_up, TO_CHAR(f.target_date, 'dd-Mon-yyyy') target_date, ");
		sb.append("        f.notes ");
		sb.append("   FROM wo_tmp_socialization s ");
		sb.append("        INNER JOIN wo_tmp_scialization_regulation sr ON s.socialization_id = sr.socialization_id ");
		sb.append("        INNER JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = s.jenis_ketentuan ");
		sb.append("        INNER JOIN wo_mst_regulation r ON r.regulation_id = sr.regulation_id ");
		sb.append("        LEFT JOIN wo_mst_document_type dt ON r.document_type_id = dt.document_type_id ");
		sb.append("        LEFT JOIN wo_mst_document_category dc ON dc.document_category_id = r.document_category_id ");
		sb.append("        LEFT JOIN wo_mst_document_topic dp ON dp.document_topic_id = r.document_topic_id ");
		sb.append("        INNER JOIN wo_mst_parameter_dtl pd2 ON pd2.parameter_dtl_code = s.status ");
		sb.append("        LEFT JOIN wo_tmp_socialization_pic_fp f ON f.socialization_id = s.socialization_id ");
		sb.append("        LEFT JOIN wo_trc_socialization_pic_fp f2 ON f2.socialization_pic_followup_id = f.socialization_pic_followup_id ");
		sb.append("        LEFT JOIN wo_mst_parameter_dtl d ON f2.compliance_status = d.parameter_dtl_code ");
		sb.append("        LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1 ");
		sb.append("        LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2 ");
		sb.append("        LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3 ");
		sb.append("        LEFT JOIN wo_mst_user u4 ON u4.user_id = f2.followup_by_id ");
		sb.append("        LEFT JOIN wo_mst_parameter_dtl d2 ON f2.followup_status = d2.parameter_dtl_code ");
		sb.append("  WHERE 1 = 1 ");
		sb.append("        AND s.enabled_flag = 'Y' ");
		sb.append("        AND sr.primary_flag = 'Y' ");
				
		sb = getQueryWhereXLSString(sb, searchCriteria);
		sb.append(" ORDER BY s.socialization_id DESC ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		List resultList = result.getResultList();

		List<RegulationSocializationVO> regulationSocVoList = new ArrayList<RegulationSocializationVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				RegulationSocializationVO data = new RegulationSocializationVO();
				
				data.setSocializationId(obj[0] != null ? (MathUtil.returnIdObjectToLong(obj[0])) : null);
				
				data.setJenisKetentuanIn(obj[1] != null ? (String) obj[1] : null);
				data.setJenisKetentuanEn(obj[2] != null ? (String) obj[2] : null);
				data.setTipeDokumenIn(obj[3] != null ? (String) obj[3] : null);
				data.setTipeDokumenEn(obj[4] != null ? (String) obj[4] : null);
				data.setKategoriIn(obj[5] != null ? (String) obj[5] : null);
				data.setKategoriEn(obj[6] != null ? (String) obj[6] : null);
				data.setTopikIn(obj[7] != null ? (String) obj[7] : null);
				data.setTopikEn(obj[8] != null ? (String) obj[8] : null);
				data.setNoDokumen(obj[9] != null ? (String) obj[9] : null);				
				data.setJdlPeraturanIn(obj[10] != null ? (String) obj[10] : null);
				data.setJdlPeraturanEn(obj[11] != null ? (String) obj[11] : null);				
				data.setStatusIn(obj[12] != null ? (String) obj[12] : null);
				data.setStatusEn(obj[13] != null ? (String) obj[13] : null);
				data.setStatusCd(obj[14] != null ? (String) obj[14] : null);				
				data.setPublishedDateStr(obj[15] != null ? (String) obj[15] : null);
				data.setEffectiveDateStr(obj[16] != null ? (String) obj[16] : null);	
				
				data.setStatusList(new ArrayList<>());
				data.setStatusTindakLanjut(obj[30] != null ? (String) obj[30] : null);
				
				StatusConfirmationVO dataConfirm = new StatusConfirmationVO();
				dataConfirm.setNamePic(obj[17] != null ? (String) obj[17] : null);
				if(obj[17] != null) {
					dataConfirm.setNamePic((String) obj[17]);
				}
				
				if(obj[18] != null) {
					dataConfirm.setNamePic(dataConfirm.getNamePic() +", " + (String) obj[18]);
				}
				
				if(obj[19] != null) {
					dataConfirm.setNamePic(dataConfirm.getNamePic() +", " + (String) obj[19]);
				}
				
				dataConfirm.setConfirmationDate(obj[20] != null ? (String) obj[20] : null);
				dataConfirm.setFollowupDate(obj[21] != null ? (String) obj[21] : null);				
				dataConfirm.setFollowupNote(obj[22] != null ? (String) obj[22] : null);
				dataConfirm.setComplianceDate(obj[23] != null ? (String) obj[23] : null);
				dataConfirm.setComplianceStatusIn(obj[24] != null ? (String) obj[24] : null);
				dataConfirm.setComplianceStatusEn(obj[25] != null ? (String) obj[25] : null);
				dataConfirm.setComplianceNote(obj[26] != null ? (String) obj[26] : null);
				dataConfirm.setFollowupBy(obj[27] != null ? (String) obj[27] : null);
				dataConfirm.setFollowupStatusIn(obj[28] != null ? (String) obj[28] : null);
				dataConfirm.setFollowupStatusEn(obj[29] != null ? (String) obj[29] : null);

				dataConfirm.setTargetDate(obj[31] != null ? (String) obj[31] : null);
//				dataConfirm.setFollowupNote(obj[32] != null ? (String) obj[32] : null);
				dataConfirm.setMakerFollowupNote(obj[32] != null ? (String) obj[32] : null);
				
				data.getStatusList().add(dataConfirm);
				
				regulationSocVoList.add(data);
			}
		}

		return regulationSocVoList;
	}
	
    @SuppressWarnings({ "rawtypes", "static-access" })
	private StringBuilder getQueryWhereXLSString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
  		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
  		if (searchCriteria != null) {
  			for (SearchObject searchVal : searchCriteria) {
  				String col = searchVal.getSearchColumn();
  				String val = searchVal.getSearchValue() != null ? searchVal.getSearchValueAsString() : "";
  				
  				if (!StringUtils.isBlank(val)) {
  					if (StringUtils.equals(RegulationSocializationConstants.WHERE_PROV_TYPE, col)) {
  						sb.append(" and s.jenis_ketentuan = '" + val + "' ");
  					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_DOC_TYPE, col)) {
  						sb.append(" and dt.document_type_id = " + val + " ");
  					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_CATEGORY, col)) {
  						sb.append(" and dc.document_category_id = " + val + " ");
  					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_TOPIC, col)) {
  						sb.append(" and dp.document_topic_id = " + val + " ");
  					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_DOC_NO, col)) {
  						sb.append(" and r.document_no like '%" + val + "%' ");
  					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_NAME, col)) {
  						if (locale != null && locale.equals(locale.ENGLISH)) {
  							sb.append(" and r.name_en LIKE '%" + val + "%' ");
  						} else {
  							sb.append(" and r.name_in LIKE '%" + val + "%' ");
  						}
  					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_PUBLISHED_DATE_START, col)) {  						
  						sb.append(" and TRUNC(r.published_date) >= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");  						
  					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_PUBLISHED_DATE_END, col)) {  						
  						sb.append(" and TRUNC(r.published_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");  						
  					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_EFF_DATE_START, col)) {
  						sb.append(" and TRUNC(r.effective_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");  						
  					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_EFF_DATE_END, col)) {
  						sb.append(" and TRUNC(r.effective_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");  						
  					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_TARGET_DATE_START, col)) {						
  						sb.append(" and TRUNC(f.target_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");  						
  					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_TARGET_DATE_END, col)) {						
  						sb.append(" and TRUNC(f.target_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");						
  					} else if (StringUtils.equals(RegulationSocializationConstants.WHERE_STATUS, col)) {
  						sb.append(" and s.status = '" + val + "' ");
  					}

  				}
  			}
  		}

  		return sb;
  	}
	
	

}
