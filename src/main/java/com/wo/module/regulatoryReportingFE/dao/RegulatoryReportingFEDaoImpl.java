package com.wo.module.regulatoryReportingFE.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.regulatoryReportingFE.vo.RegulatoryReportingFEVo;
import com.wo.module.trcRmd.model.TrcRmd;

@Repository("regulatoryReportingFEDao")
public class RegulatoryReportingFEDaoImpl extends GenericDAOHibernate<TrcRmd, Long>
	implements RegulatoryReportingFEDao, Serializable{

	private static final long serialVersionUID = 2498319471898604757L;

	@SuppressWarnings("rawtypes")
	private void setQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
						sb.append(" AND (:status  <> 'PIC_DONE' or trpf.FOLLOWUP_STATUS = 'PIC_DONE') ");
						sb.append(" AND (:status  = 'PIC_DONE' or (trpf.FOLLOWUP_STATUS <> 'PIC_DONE' or trpf.FOLLOWUP_STATUS is null))");
//						sb.append(" AND tr.FOLLOWUP_STATUS = :status ");
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						sb.append(" AND UPPER(tr.REPORT_NAME_IN) LIKE UPPER(:searchReportName) ");
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						sb.append(" AND (tr.user_id_1 = :userId or tr.user_id_2 = :userId or tr.user_id_3 = :userId ) ");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void setQuerySetString(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
						query.setParameter("status", val);
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						String reportName = "%"+val+"%";
						query.setParameter("searchReportName", reportName);
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						query.setParameter("userId", val);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<RegulatoryReportingFEVo> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		List<RegulatoryReportingFEVo> vo = searchDataCriteria(searchCriteria, first, pageSize);
		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	private List<RegulatoryReportingFEVo> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize){
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT tr.RMD_ID ");
		sb.append(" 	  ,tr.REPORT_NAME_IN ");
		sb.append("       ,tr.REPORT_NAME_EN ");
		sb.append("       ,tr.STATUS ");
		sb.append("       ,pdStatus.NAME_IN STATUS_IN ");
		sb.append("       ,pdStatus.NAME_EN STATUS_EN ");
		sb.append("       ,trpf.TARGET_DATE ");
		sb.append("       ,TO_CHAR(trpf.TARGET_DATE, 'dd FMMonth yyyy', 'nls_date_language=indonesian') TARGET_DATE_STR ");
		sb.append("       ,trpf.FOLLOWUP_STATUS ");
		sb.append("       ,pdFollowupStatus.NAME_IN FOLLOWUP_STATUS_IN ");
		sb.append("       ,pdFollowupStatus.NAME_EN FOLLOWUP_STATUS_EN ");
		sb.append("       ,trpf.FOLLOWUP_DATE ");
		sb.append("       ,TO_CHAR(trpf.FOLLOWUP_DATE, 'dd FMMonth yyyy', 'nls_date_language=indonesian') FOLLOWUP_DATE_STR ");
		sb.append("       ,trpf.RMD_PIC_FOLLOWUP_ID ");
		sb.append(" FROM WO_TRC_RMD tr ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdStatus ");
		sb.append("         ON pdStatus.PARAMETER_DTL_CODE = tr.STATUS ");
		sb.append("     INNER JOIN WO_TRC_RMD_PIC_FOLLOWUP trpf ");
		sb.append("         ON trpf.RMD_ID = tr.RMD_ID ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdFollowupStatus ");
		sb.append("         ON pdFollowupStatus.PARAMETER_DTL_CODE = trpf.FOLLOWUP_STATUS ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND tr.ENABLED_FLAG <> 'N' AND trpf.ENABLED_FLAG <> 'N' ");
		//sb.append("     AND (tr.FOLLOWUP_STATUS <> 'PIC_DONE' OR tr.FOLLOWUP_STATUS IS NULL) ");
		
		this.setQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY tr.RMD_ID DESC, trpf.RMD_PIC_FOLLOWUP_ID ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		this.setQuerySetString(query, searchCriteria);
		
		List result = query.getResultList();
		List<RegulatoryReportingFEVo> vo = new ArrayList<RegulatoryReportingFEVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				RegulatoryReportingFEVo data = new RegulatoryReportingFEVo();
				
				data.setRegulatoryReportingId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setReportNameIn(obj[1] != null ? (String) obj[1] : null);
				data.setReportNameEn(obj[2] != null ? (String) obj[2] : null);
				data.setStatusCode(obj[3] != null ? (String) obj[3] : null);
				data.setStatusIn(obj[4] != null ? (String) obj[4] : null);
				data.setStatusEn(obj[5] != null ? (String) obj[5] : null);
				data.setTargetDate(obj[6] != null ? (Date) obj[6] : null);
				data.setTargetDateStr(obj[7] != null ? (String) obj[7] : null);
				data.setFollowupStatusCode(obj[8] != null ? (String) obj[8] : null);
				data.setFollowupStatusIn(obj[9] != null ? (String) obj[9] : null);
				data.setFollowupStatusEn(obj[10] != null ? (String) obj[10] : null);
				data.setFollowupDate(obj[11] != null ? (Date) obj[11] : null);
				data.setFollowupDateStr(obj[12] != null ? (String) obj[12] : null);
				data.setRegulatoryReportingFollowupId(obj[13] != null ? MathUtil.returnIdObjectToLong(obj[13]) : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		Number result = searchCountDataCriteria(searchCriteria);
		
		if (result == null) {
			result = 0;
		}
		
		return result.longValue();
	}
	
	@SuppressWarnings("rawtypes")
	private Number searchCountDataCriteria(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT COUNT(1) ");
		sb.append(" FROM WO_TRC_RMD tr ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdStatus ");
		sb.append("         ON pdStatus.PARAMETER_DTL_CODE = tr.STATUS ");
		sb.append("     INNER JOIN WO_TRC_RMD_PIC_FOLLOWUP trpf ");
		sb.append("         ON trpf.RMD_ID = tr.RMD_ID ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdFollowupStatus ");
		sb.append("         ON pdFollowupStatus.PARAMETER_DTL_CODE = tr.FOLLOWUP_STATUS ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND tr.ENABLED_FLAG <> 'N' AND trpf.ENABLED_FLAG <> 'N' ");
		//sb.append("     AND (tr.FOLLOWUP_STATUS <> 'PIC_DONE' OR tr.FOLLOWUP_STATUS IS NULL) ");
		
		this.setQueryWhereString(sb, searchCriteria);
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQuerySetString(query, searchCriteria);
		
		Number result = (Number) query.getSingleResult();
		
		return result;
	}

}
