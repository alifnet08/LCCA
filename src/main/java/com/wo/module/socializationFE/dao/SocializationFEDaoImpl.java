package com.wo.module.socializationFE.dao;

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
import com.wo.module.regulationSocialization.model.SocializationTrc;
import com.wo.module.socializationFE.vo.SocializationFEVo;

@Repository("socializationFEDao")
public class SocializationFEDaoImpl extends GenericDAOHibernate<SocializationTrc, Long>
	implements SocializationFEDao, Serializable{

	private static final long serialVersionUID = 2599959950947010330L;

	@SuppressWarnings("rawtypes")
	private void setQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
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
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						sb.append(" AND  UPPER(r.NAME_IN) LIKE UPPER(:searchNameIn)");
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						sb.append(" and (f.user_id_1 = :userId or f.user_id_2 = :userId or f.user_id_3 = :userId ) ");
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
						String name = "%"+val+"%";
						query.setParameter("searchNameIn", name);
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
	public List<SocializationFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		List<SocializationFEVo> vo = searchDataCriteria(searchCriteria, first, pageSize);
		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<SocializationFEVo> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize){
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT socialization_pic_followup_id ");
		sb.append(" 	  ,s.socialization_id ");
		sb.append(" 	  ,r.NAME_IN ");
		sb.append(" 	  ,r.name_en ");
		sb.append(" 	  ,s.status ");
		sb.append(" 	  ,f.target_date ");
		sb.append(" 	  ,TO_CHAR (f.target_date, 'dd FMMonth yyyy', 'nls_date_language=indonesian') target_date_str ");
		sb.append(" 	  ,pd.NAME_IN statusIn ");
		sb.append(" 	  ,pd.name_en statusEn ");
		sb.append(" 	  ,u1.name PIC1 ");
		sb.append(" 	  ,u2.name PIC2 ");
		sb.append(" 	  ,u3.name PIC3 ");
		sb.append(" 	  ,f.compliance_note ");
		sb.append(" 	  ,f.followup_status, f.notes ");
		sb.append(" FROM wo_trc_socialization s ");
		sb.append(" 	INNER JOIN wo_trc_socialization_rgltn sr ");
		sb.append(" 		ON s.socialization_id = sr.socialization_id ");
		sb.append("     INNER JOIN wo_mst_regulation r ON r.regulation_id = sr.regulation_id ");
		sb.append("     INNER JOIN wo_trc_socialization_pic_fp f ");
		sb.append(" 		ON f.socialization_id = s.socialization_id ");
		sb.append("     LEFT JOIN wo_mst_parameter_dtl pd ");
		sb.append("         ON pd.parameter_dtl_code = f.followup_status ");
		sb.append("     LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1 ");
		sb.append("     LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2 ");
		sb.append("     LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3 ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" 	AND s.enabled_flag = 'Y' ");
		sb.append("     AND sr.primary_flag = 'Y' ");
		sb.append("     AND s.status = 'DATA_ACTIVE' ");
		sb.append("     AND s.follow_up = 'Y' ");
		// sb.append("     AND (f.followup_status IS NULL OR f.followup_status <> 'PIC_DONE') ");
		
		this.setQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY s.socialization_id DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQuerySetString(query, searchCriteria);
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		List result = query.getResultList();
		List<SocializationFEVo> vo = new ArrayList<SocializationFEVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				SocializationFEVo data = new SocializationFEVo();
				
				data.setTrcSocializationFollowupId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setTrcSocializationId(obj[1] != null ? MathUtil.returnIdObjectToLong(obj[1]) : null);
				data.setRegulationNameIn(obj[2] != null ? (String) obj[2] : null);
				data.setRegulationNameEn(obj[3] != null ? (String) obj[3] : null);
				data.setStatusCode(obj[4] != null ? (String) obj[4] : null);
				data.setTargetDate(obj[5] != null ? (Date) obj[5] : null);
				data.setTargetDateStr(obj[6] != null ? (String) obj[6] : null);
				data.setFollowupStatusIn(obj[7] != null ? (String) obj[7] : null);
				data.setFollowupStatusEn(obj[8] != null ? (String) obj[8] : null);
				data.setPic1(obj[9] != null ? (String) obj[9] : null);
				data.setPic2(obj[10] != null ? (String) obj[10] : null);
				data.setPic3(obj[11] != null ? (String) obj[11] : null);
				data.setComplianceNote(obj[12] != null ? (String) obj[12] : null);
				data.setFollowupStatusCode(obj[13] != null ? (String) obj[13] : null);
				data.setNotes(obj[14] != null ? (String) obj[14] : null);
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
		sb.append(" FROM wo_trc_socialization s ");
		sb.append(" 	INNER JOIN wo_trc_socialization_rgltn sr ");
		sb.append(" 		ON s.socialization_id = sr.socialization_id ");
		sb.append("     INNER JOIN wo_mst_regulation r ON r.regulation_id = sr.regulation_id ");
		sb.append("     INNER JOIN wo_trc_socialization_pic_fp f ");
		sb.append(" 		ON f.socialization_id = s.socialization_id ");
		sb.append("     LEFT JOIN wo_mst_parameter_dtl pd ");
		sb.append("         ON pd.parameter_dtl_code = f.followup_status ");
		sb.append("     LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1 ");
		sb.append("     LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2 ");
		sb.append("     LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3 ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" 	AND s.enabled_flag = 'Y' ");
		sb.append("     AND sr.primary_flag = 'Y' ");
		sb.append("     AND s.status = 'DATA_ACTIVE' ");
		sb.append("     AND s.follow_up = 'Y' ");
//		sb.append("     AND (f.followup_status IS NULL OR f.followup_status <> 'PIC_DONE') ");
		
		this.setQueryWhereString(sb, searchCriteria);
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQuerySetString(query, searchCriteria);
		
		Number result = (Number) query.getSingleResult();
		
		return result;
	}

}
