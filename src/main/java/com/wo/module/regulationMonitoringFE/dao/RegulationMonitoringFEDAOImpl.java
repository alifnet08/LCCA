package com.wo.module.regulationMonitoringFE.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.regulationMonitoring.model.RegMonitoringTrc;
import com.wo.module.regulationMonitoringFE.vo.RegulationMonitoringFEVO;

@Repository("regulationMonitoringFEDAO")
public class RegulationMonitoringFEDAOImpl extends GenericDAOHibernate<RegMonitoringTrc, Long>
		implements RegulationMonitoringFEDAO {

	@SuppressWarnings({ "rawtypes", "unused" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {

					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
						sb.append(" AND (:searchStatus  <> 'PIC_DONE' or f.followup_status = 'PIC_DONE') ");
						sb.append(" AND (:searchStatus  = 'PIC_DONE' or (f.followup_status <> 'PIC_DONE' or f.followup_status is null))");
//						sb.append(" and f.followup_status = :searchStatus ");
					}

					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						sb.append(
								" and (f.user_id_1 = :userId or f.user_id_2 = :userId or f.user_id_3 = :userId ) ");
					}
					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						sb.append(" and UPPER(m.name_in) like UPPER(:searchNameIn)");
					}	

				}
			}
		}
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
		sb.append(" select count(1) ");
		sb.append(" 	  FROM WO_TRC_REG_MONITORING rr	 ");
		sb.append(" 	       INNER JOIN WO_TRC_REG_MONITOR_REGULATION sr	 ");
		sb.append(" 	          ON RR.REG_MONITORING_ID = sr.REG_MONITORING_ID	 ");
		sb.append(" 	       INNER JOIN wo_mst_regulation M ON M.regulation_id = sr.regulation_id	 ");
		sb.append(" 	       INNER JOIN WO_TRC_REG_MONITORING_PIC_FP f	 ");
		sb.append(" 	          ON f.REG_MONITORING_ID = RR.REG_MONITORING_ID	 ");
		sb.append(" 	       INNER JOIN WO_MST_DOCUMENT_TYPE D	 ");
		sb.append(" 	          ON M.DOCUMENT_TYPE_ID = D.DOCUMENT_TYPE_ID	 ");
		sb.append(" 	       LEFT JOIN wo_mst_parameter_dtl pd	 ");
		sb.append(" 	          ON pd.parameter_dtl_code = f.COMPLIANCE_STATUS	 ");
		sb.append(" 	       LEFT JOIN wo_mst_parameter_dtl pd1	 ");
		sb.append(" 	          ON pd1.parameter_dtl_code = f.followup_status	 ");
		sb.append(" 	       LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1	 ");
		sb.append(" 	       LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2	 ");
		sb.append(" 	       LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3	 ");
		sb.append("			   INNER JOIN wo_tmp_reg_monitoring tsr ON tsr.reg_monitoring_id = rr.reg_monitoring_id ");
		sb.append(" 	 WHERE     1 = 1	 ");
		sb.append(" 	       AND RR.enabled_flag = 'Y'	 ");
		sb.append(" 	       AND sr.primary_flag = 'Y'	 ");
		sb.append("			   AND tsr.enabled_flag = 'Y'	  ");
		sb.append(" 	       AND RR.status = 'DATA_ACTIVE'	 ");
		sb.append(" 	       AND RR.follow_up = 'Y'	 ");
		//sb.append(" 	       AND (f.followup_status IS NULL OR f.followup_status <> 'PIC_DONE')	 ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<RegulationMonitoringFEVO> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		List<RegulationMonitoringFEVO> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<RegulationMonitoringFEVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first,
			int pageSize) {

		StringBuilder sb = new StringBuilder();
		sb.append(" 	SELECT RR.REG_MONITORING_ID,	 ");
		sb.append(" 	       M.JENIS_KETENTUAN,	 ");
		sb.append(" 	       M.DOCUMENT_NO,	 ");
		sb.append(" 	       M.NAME_IN,	 ");
		sb.append(" 	       D.DOCUMENT_TYPE_IN,	 ");
		sb.append(" 	       TO_CHAR(M.PUBLISHED_DATE, 'dd FMMonth yyyy', 'nls_date_language=indonesian') published_date,	 ");
		sb.append(" 	       TO_CHAR(M.EFFECTIVE_DATE, 'dd FMMonth yyyy', 'nls_date_language=indonesian') effective_date,	 ");
		sb.append(" 	       TO_CHAR(F.TARGET_DATE, 'dd FMMonth yyyy', 'nls_date_language=indonesian') target_date,	 ");
		sb.append(" 	       TO_CHAR(F.FOLLOWUP_DATE, 'dd FMMonth yyyy', 'nls_date_language=indonesian') followup_date,	 ");
		sb.append(" 	       RR.NOTES,	 ");
		sb.append(" 	       F.COMPLIANCE_NOTE,	 ");
		sb.append("            PD.NAME_IN COMPLIANCE_STATUS, ");
		sb.append("	           F.FOLLOWUP_STATUS, ");
		sb.append(" 	       PD1.NAME_IN STATUS_IN,	 ");
		sb.append(" 	       u1.name PIC1,	 ");
		sb.append(" 	       u2.name PIC2,	 ");
		sb.append(" 	       u3.name PIC3	, f.REG_MONITORING_PIC_FOLLOWUP_ID ");
		sb.append(" 	  FROM WO_TRC_REG_MONITORING rr	 ");
		sb.append(" 	       INNER JOIN WO_TRC_REG_MONITOR_REGULATION sr	 ");
		sb.append(" 	          ON RR.REG_MONITORING_ID = sr.REG_MONITORING_ID	 ");
		sb.append(" 	       INNER JOIN wo_mst_regulation M ON M.regulation_id = sr.regulation_id	 ");
		sb.append(" 	       INNER JOIN WO_TRC_REG_MONITORING_PIC_FP f	 ");
		sb.append(" 	          ON f.REG_MONITORING_ID = RR.REG_MONITORING_ID	 ");
		sb.append(" 	       INNER JOIN WO_MST_DOCUMENT_TYPE D	 ");
		sb.append(" 	          ON M.DOCUMENT_TYPE_ID = D.DOCUMENT_TYPE_ID	 ");
		sb.append(" 	       LEFT JOIN wo_mst_parameter_dtl pd	 ");
		sb.append(" 	          ON pd.parameter_dtl_code = f.COMPLIANCE_STATUS	 ");
		sb.append(" 	       LEFT JOIN wo_mst_parameter_dtl pd1	 ");
		sb.append(" 	          ON pd1.parameter_dtl_code = f.followup_status	 ");
		sb.append(" 	       LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1	 ");
		sb.append(" 	       LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2	 ");
		sb.append(" 	       LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3	 ");
		sb.append("			   INNER JOIN wo_tmp_reg_monitoring tsr ON tsr.reg_monitoring_id = rr.reg_monitoring_id ");
		sb.append(" 	 WHERE     1 = 1	 ");
		sb.append(" 	       AND RR.enabled_flag = 'Y'	 ");
		sb.append(" 	       AND sr.primary_flag = 'Y'	 ");
		sb.append(" 	       AND RR.status = 'DATA_ACTIVE'	 ");
		sb.append(" 	       AND RR.follow_up = 'Y'	 ");
		sb.append("			   AND tsr.enabled_flag = 'Y'	  ");
		//sb.append(" 	       AND (f.followup_status IS NULL OR f.followup_status <> 'PIC_DONE')	 ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY RR.REG_MONITORING_ID DESC ");

		Query result = getSession().createSQLQuery(sb.toString());
		
		result.setFirstResult(first);
		result.setMaxResults(pageSize);
		
		this.getQuerySetValue(result, searchCriteria);

		List resultList = result.getResultList();

		List<RegulationMonitoringFEVO> vo = new ArrayList<RegulationMonitoringFEVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				RegulationMonitoringFEVO data = new RegulationMonitoringFEVO();

				data.setRegMonitoringId(obj[0] != null ? (MathUtil.returnIdObjectToLong(obj[0])) : null);
				data.setJenisKetentuanIn(obj[1] != null ? (String) obj[1] : null);
				data.setNoDokumen(obj[2] != null ? (String) obj[2] : null);
				data.setJdlPeraturanIn(obj[3] != null ? (String) obj[3] : null);
				data.setTipeDokumenIn(obj[4] != null ? (String) obj[4] : null);
				data.setPublishedDateStr(obj[5] != null ? (String) obj[5] : null);
				data.setEffectiveDateStr(obj[6] != null ? (String) obj[6] : null);
				data.setTargetDateStr(obj[7] != null ? (String) obj[7] : null);
				data.setTglTindakLanjut(obj[8] != null ? (String) obj[8] : null);
				data.setRegMonitoringNote(obj[9] != null ? (String) obj[9] : null);
				data.setComplianceNote(obj[10] != null ? (String) obj[10] : null);
				data.setComplianceStatus(obj[11] != null ? (String) obj[11] : null);
				data.setFollowupStatusCd(obj[12] != null ? (String) obj[12] : null);
				data.setFollowUpStatus(obj[13] != null ? (String) obj[13] : null);
				data.setPicName1(obj[14] != null ? (String) obj[14] : null);
				data.setPicName2(obj[15] != null ? (String) obj[15] : null);
				data.setPicName3(obj[16] != null ? (String) obj[16] : null);
				data.setRegMonitoringPicFpId(obj[17]!=null?MathUtil.returnIdObjectToLong(obj[17]):null);
				vo.add(data);
			}
		}

		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public RegulationMonitoringFEVO searchForDetail(Long id) {
		RegulationMonitoringFEVO data = new RegulationMonitoringFEVO();
		
		StringBuilder sb = new StringBuilder();
		sb.append(" 	SELECT RR.REG_MONITORING_ID,	 ");
		sb.append(" 	       PD2.NAME_IN JENIS_KETENTUAN,	 ");
		sb.append(" 	       M.DOCUMENT_NO,	 ");
		sb.append(" 	       M.NAME_IN,	 ");
		sb.append(" 	       D.DOCUMENT_TYPE_IN,	 ");
		sb.append(" 	       TO_CHAR(M.PUBLISHED_DATE, 'dd FMMonth yyyy', 'nls_date_language=indonesian') published_date,	 ");
		sb.append(" 	       TO_CHAR(M.EFFECTIVE_DATE, 'dd FMMonth yyyy', 'nls_date_language=indonesian') effective_date,	 ");
		sb.append(" 	       TO_CHAR(F.TARGET_DATE, 'dd FMMonth yyyy', 'nls_date_language=indonesian') target_date,	 ");
		sb.append(" 	       TO_CHAR(F.FOLLOWUP_DATE, 'dd FMMonth yyyy', 'nls_date_language=indonesian') followup_date,	 ");
		sb.append(" 	       RR.NOTES,	 ");
		sb.append(" 	       F.COMPLIANCE_NOTE,	 ");
		sb.append("            PD.NAME_IN COMPLIANCE_STATUS, ");
		sb.append("	           F.FOLLOWUP_STATUS, ");
		sb.append(" 	       PD1.NAME_IN STATUS_IN,	 ");
		sb.append(" 	       u1.name PIC1,	 ");
		sb.append(" 	       u2.name PIC2,	 ");
		sb.append(" 	       u3.name PIC3	 ");
		sb.append(" 	  FROM WO_TRC_REG_MONITORING rr	 ");
		sb.append(" 	       INNER JOIN WO_TRC_REG_MONITOR_REGULATION sr	 ");
		sb.append(" 	          ON RR.REG_MONITORING_ID = sr.REG_MONITORING_ID	 ");
		sb.append(" 	       INNER JOIN wo_mst_regulation M ON M.regulation_id = sr.regulation_id	 ");
		sb.append(" 	       INNER JOIN WO_TRC_REG_MONITORING_PIC_FP f	 ");
		sb.append(" 	          ON f.REG_MONITORING_ID = RR.REG_MONITORING_ID	 ");
		sb.append(" 	       INNER JOIN WO_MST_DOCUMENT_TYPE D	 ");
		sb.append(" 	          ON M.DOCUMENT_TYPE_ID = D.DOCUMENT_TYPE_ID	 ");
		sb.append(" 	       LEFT JOIN wo_mst_parameter_dtl pd	 ");
		sb.append(" 	          ON pd.parameter_dtl_code = f.COMPLIANCE_STATUS	 ");
		sb.append(" 	       LEFT JOIN wo_mst_parameter_dtl pd1	 ");
		sb.append(" 	          ON pd1.parameter_dtl_code = f.followup_status	 ");
		sb.append(" 	       LEFT JOIN wo_mst_parameter_dtl pd2	 ");
		sb.append(" 	          ON pd2.parameter_dtl_code = M.JENIS_KETENTUAN	 ");
		sb.append(" 	       LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1	 ");
		sb.append(" 	       LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2	 ");
		sb.append(" 	       LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3	 ");
		sb.append(" 	 WHERE     1 = 1	 ");
		sb.append(" 	       AND RR.enabled_flag = 'Y'	 ");
		sb.append(" 	       AND sr.primary_flag = 'Y'	 ");
		sb.append(" 	       AND RR.status = 'DATA_ACTIVE'	 ");
		sb.append(" 	       AND RR.follow_up = 'Y'	 ");
		//sb.append(" 	       AND (f.followup_status IS NULL OR f.followup_status <> 'PIC_DONE')	 ");
		
		if (id != null && id > 0) {
			sb.append(" 		   AND RR.REG_MONITORING_ID = '" + id + "' ");
		}

		Query result = getSession().createSQLQuery(sb.toString());
		
		List resultList = result.getResultList();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
	
				data.setRegMonitoringId(obj[0] != null ? (MathUtil.returnIdObjectToLong(obj[0])) : null);
				data.setJenisKetentuanIn(obj[1] != null ? (String) obj[1] : null);
				data.setNoDokumen(obj[2] != null ? (String) obj[2] : null);
				data.setJdlPeraturanIn(obj[3] != null ? (String) obj[3] : null);
				data.setTipeDokumenIn(obj[4] != null ? (String) obj[4] : null);
				data.setPublishedDateStr(obj[5] != null ? (String) obj[5] : null);
				data.setEffectiveDateStr(obj[6] != null ? (String) obj[6] : null);
				data.setTargetDateStr(obj[7] != null ? (String) obj[7] : null);
				data.setTglTindakLanjut(obj[8] != null ? (String) obj[8] : null);
				data.setRegMonitoringNote(obj[9] != null ? (String) obj[9] : null);
				data.setComplianceNote(obj[10] != null ? (String) obj[10] : null);
				data.setComplianceStatus(obj[11] != null ? (String) obj[11] : null);
				data.setFollowupStatusCd(obj[12] != null ? (String) obj[12] : null);
				data.setFollowUpStatus(obj[13] != null ? (String) obj[13] : null);
				data.setPicName1(obj[14] != null ? (String) obj[14] : null);
				data.setPicName2(obj[15] != null ? (String) obj[15] : null);
				data.setPicName3(obj[16] != null ? (String) obj[16] : null);

			}
		}

	
		return data;
	}

}
