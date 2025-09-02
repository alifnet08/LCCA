package com.wo.module.fineFE.dao;

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
import com.wo.module.fineFE.vo.FineFEVo;
import com.wo.module.trcFineApproval.model.TrcFine;

@Repository("fineFEDao")
public class FineFEDaoImpl extends GenericDAOHibernate<TrcFine, Long>
	implements FineFEDao, Serializable{

	private static final long serialVersionUID = -5101475928845922577L;

	@SuppressWarnings("rawtypes")
	private void setQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
						sb.append(" AND (:searchStatus  <> 'PIC_DONE' or fpf.followup_status = :searchStatus) ");
						sb.append(" AND (:searchStatus  = 'PIC_DONE' or (fpf.followup_status = :searchStatus or fpf.followup_status is null))");
//						sb.append(" AND fpf.FOLLOWUP_STATUS = :searchStatus ");
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						sb.append(" AND UPPER(f.LETTER_NO) LIKE UPPER(:searchLetterNo) ");
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						sb.append(" AND (fpf.user_id_1 = :userId or fpf.user_id_2 = :userId or fpf.user_id_3 = :userId) ");
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
						query.setParameter("searchStatus", val);
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						String letterNo = "%"+val+"%";
						query.setParameter("searchLetterNo", letterNo);
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
	public List<FineFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		List<FineFEVo> vo = searchDataCriteria(searchCriteria, first, pageSize);
		
		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	private List<FineFEVo> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize){
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT f.FINE_ID ");
		sb.append("       ,fpf.FINE_PIC_FOLLOWUP_ID ");
		sb.append("       ,f.LETTER_NO ");
		sb.append("       ,fpf.FOLLOWUP_STATUS ");
		sb.append("       ,pdFollowupStatus.NAME_IN FOLLOWUP_STATUS_IN ");
		sb.append("       ,pdFollowupStatus.NAME_EN FOLLOWUP_STATUS_EN ");
		sb.append("       ,fpf.TARGET_DATE ");
		sb.append("       ,TO_CHAR(fpf.TARGET_DATE, 'dd FMMonth yyyy', 'nls_date_language=indonesian') TARGET_DATE_STR ");
		sb.append("       ,f.STATUS ");
		sb.append("       ,fpf.TARGET_RESPONSE_DATE ");
		sb.append("       ,TO_CHAR(fpf.TARGET_RESPONSE_DATE, 'dd FMMonth yyyy', 'nls_date_language=indonesian') TARGET_RESPONSE_STR ");
		sb.append(" FROM WO_TRC_FINE f ");
		sb.append("     LEFT JOIN WO_TRC_FINE_PIC_FOLLOWUP fpf ");
		sb.append("         ON fpf.FINE_ID = f.FINE_ID ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdFollowupStatus ");
		sb.append("         ON pdFollowupStatus.PARAMETER_DTL_CODE = fpf.FOLLOWUP_STATUS ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND f.ENABLED_FLAG = 'Y' ");
		
		this.setQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY f.FINE_ID DESC, fpf.FINE_PIC_FOLLOWUP_ID ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQuerySetString(query, searchCriteria);
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		List result = query.getResultList();
		List<FineFEVo> vo = new ArrayList<FineFEVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				FineFEVo data = new FineFEVo();
				
				data.setFineId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setFinePicFollowupId(obj[1] != null ? MathUtil.returnIdObjectToLong(obj[1]) : null);
				data.setLetterNo(obj[2] != null ? (String) obj[2] : null);
				data.setFollowupStatusCode(obj[3] != null ? (String) obj[3] : null);
				data.setFollowupStatusIn(obj[4] != null ? (String) obj[4] : null);
				data.setFollowupStatusEn(obj[5] != null ? (String) obj[5] : null);
				data.setTargetDate(obj[6] != null ? (Date) obj[6] : null);
				data.setTargetDateStr(obj[7] != null ? (String) obj[7] : null);
				data.setStatusCode(obj[8] != null ? (String) obj[8] : null);
				data.setTargetResponseDate(obj[9] != null ? (Date) obj[9] : null);
				data.setTargetResponseDateStr(obj[10] != null ? (String) obj[10] : null);;
				
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
		sb.append(" FROM WO_TRC_FINE f ");
		sb.append("     LEFT JOIN WO_TRC_FINE_PIC_FOLLOWUP fpf ");
		sb.append("         ON fpf.FINE_ID = f.FINE_ID ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdFollowupStatus ");
		sb.append("         ON pdFollowupStatus.PARAMETER_DTL_CODE = fpf.FOLLOWUP_STATUS ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND f.ENABLED_FLAG = 'Y' ");
		
		this.setQueryWhereString(sb, searchCriteria);
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQuerySetString(query, searchCriteria);
		
		Number result = (Number) query.getSingleResult();
		
		return result;
	}

}
