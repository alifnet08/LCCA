package com.wo.module.tmpFaqApproval.dao;

import java.sql.Date;
import java.sql.Timestamp;
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
import com.wo.module.faq.model.TmpFaq;

@Repository("tmpFaqApprovalDao")
public class TmpFaqApprovalDaoImpl extends GenericDAOHibernate<TmpFaq, Long> implements TmpFaqApprovalDao{

	@SuppressWarnings("rawtypes")
	private void setQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						sb.append(" and (u.DIVISION_NAME = (select DIVISION_NAME from wo_mst_user where user_id = :userId)) ");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void getQueryWhereString(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						query.setParameter("userId", val);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<TmpFaq> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select distinct tf.FAQ_ID ");
		sb.append("       ,tf.QUESTION_IN ");
		sb.append("       ,mpd.NAME_IN INSTITUTION ");
		sb.append("       ,mpd1.NAME_IN CATEGORY ");
		sb.append("       ,tf.creation_date ");
		sb.append(" from wo_tmp_faq tf ");
		sb.append("     inner join wo_mst_parameter_dtl mpd ");
		sb.append("         on mpd.PARAMETER_DTL_CODE = tf.INSTITUTION ");
		sb.append("     inner join wo_mst_parameter_dtl mpd1 ");
		sb.append("         on mpd1.PARAMETER_DTL_CODE = tf.FAQ_CATEGORY ");
		sb.append("     inner join wo_tmp_faq_keyword tfk ");
		sb.append("         on tfk.FAQ_ID = tf.FAQ_ID ");
		sb.append("     INNER JOIN WO_MST_USER u ");
		sb.append("         ON u.nik = tf.CREATED_BY ");
		sb.append(" where 1 = 1 ");
		sb.append("     and tf.ENABLED_FLAG = 'Y' ");
		sb.append("     and tf.STATUS = 'DATA_NEW' ");
		
		this.setQueryWhereString(sb, searchCriteria);
		sb.append(" order by tf.FAQ_ID desc ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQueryWhereString(query, searchCriteria);
		
		List result = query.getResultList();
		List<TmpFaq> vo = new ArrayList<TmpFaq>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				TmpFaq data = new TmpFaq();
				
				data.setFaqId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setQuestionIn(obj[1] != null ? (String) obj[1] : null);
				data.setInstitutionName(obj[2] != null ? (String) obj[2] : null);
				data.setCategory(obj[3] != null ? (String) obj[3] : null);
				Timestamp timestamp = obj[4] != null ? (Timestamp) obj[4] : null;
				if (timestamp != null) {
					data.setDate(new Date(timestamp.getTime()));					
				}
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select count(1) ");
		sb.append(" from wo_tmp_faq tf ");
		sb.append("     inner join wo_mst_parameter_dtl mpd ");
		sb.append("         on mpd.PARAMETER_DTL_CODE = tf.INSTITUTION ");
		sb.append("     inner join wo_mst_parameter_dtl mpd1 ");
		sb.append("         on mpd1.PARAMETER_DTL_CODE = tf.FAQ_CATEGORY ");
		sb.append("     inner join wo_tmp_faq_keyword tfk ");
		sb.append("         on tfk.FAQ_ID = tf.FAQ_ID ");
		sb.append("     INNER JOIN WO_MST_USER u ");
		sb.append("         ON u.nik = tf.CREATED_BY ");
		sb.append(" where 1 = 1 ");
		sb.append("     and tf.ENABLED_FLAG = 'Y' ");
		this.setQueryWhereString(sb, searchCriteria);
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQueryWhereString(query, searchCriteria);
		
		Number result = (Number) query.getSingleResult();
		
		if (result == null) {
			result = 0;
		}
		
		return result.longValue();
	}

}
