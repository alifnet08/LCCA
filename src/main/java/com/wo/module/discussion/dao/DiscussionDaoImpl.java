/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.discussion.dao;

import java.math.BigDecimal;
//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.discussion.constant.DiscussionConstants;
import com.wo.module.discussion.model.Discussion;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.rc.constant.RCConstants;
import com.wo.module.rc.model.RC;

/**
 *
 * @author hendra
 */
@Repository("discussionDao")
public class DiscussionDaoImpl extends GenericDAOHibernate<Discussion, Long> implements DiscussionDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(DiscussionDaoImpl.class);
	

	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(DiscussionConstants.SEARCH_BY_KEYWORD, col)) {
						
							sb.append(" and content_in LIKE :keyword ");
					
					}
					if (StringUtils.equals(DiscussionConstants.SEARCH_BY_STATUS, col)) {
						
						sb.append(" and THREAD_CLOSE_STATUS = :status ");
				
				    }
				   if (StringUtils.equals(DiscussionConstants.SEARCH_BY_TYPE, col)) {
						
						sb.append(" and THREAD_TYPE = :type ");
				
				    }
					if (StringUtils.equals(DiscussionConstants.SEARCH_BY_CREATED_DATE_FROM, col)) {
							sb.append(" and TRUNC(creation_date) >= TO_DATE(:dateFrom,'yyyy-MM-dd') ");
						
					}
					if (StringUtils.equals(DiscussionConstants.SEARCH_BY_CREATED_DATE_TO, col)) {
						sb.append(" and TRUNC(creation_date) <= TO_DATE(:dateTo,'yyyy-MM-dd') ");
					
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

				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(DiscussionConstants.SEARCH_BY_KEYWORD, col)) {
						query.setParameter("keyword", "%" + val + "%");
					}
					if (StringUtils.equals(DiscussionConstants.SEARCH_BY_STATUS, col)) {
						query.setParameter("status",  val );
					}
					if (StringUtils.equals(DiscussionConstants.SEARCH_BY_TYPE, col)) {
						query.setParameter("type",  val );
					}
					if (StringUtils.equals(DiscussionConstants.SEARCH_BY_CREATED_DATE_FROM, col)) {
						query.setParameter("dateFrom", val);
					}
					if (StringUtils.equals(DiscussionConstants.SEARCH_BY_CREATED_DATE_TO, col)) {
						query.setParameter("dateTo", val);
					}
				}
			}
		}
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
		sb.append(" from wo_mst_discussion ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<Discussion> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<Discussion> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<Discussion> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ct.DISCUSSION_ID, ct.thread_name_in, d.name_in, "
				+ "((select SUM(POSTED_RATING) from wo_mst_discussion_post dp where dp.discussion_id = ct.discussion_id) / (select COUNT(1) from wo_mst_discussion_post dp where dp.discussion_id = ct.discussion_id)) rating, "
				+ " (select COUNT(1) from wo_mst_discussion_post dp where dp.discussion_id = ct.discussion_id) replies, (select last_update_by from wo_mst_discussion_post dp where dp.discussion_id = ct.discussion_id and rownum = 1)  ");
		sb.append(" from wo_mst_discussion ct left join wo_mst_parameter_dtl d on d.parameter_dtl_code = ct.thread_type ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY DISCUSSION_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<Discussion> vo = new ArrayList<Discussion>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				Discussion data = new Discussion();

				Long discussionId = MathUtil.returnIdObjectToLong(obj[0]);
				data.setDiscussionId(discussionId);
				data.setThreadNameIn(obj[1]!=null?(String)obj[1]:null);
				data.setThreadTypeStr(obj[2]!=null?(String)obj[2]:null);
				data.setRating(obj[3]!=null?((BigDecimal)obj[3]).intValue():null);
				data.setReplies(MathUtil.returnIdObjectToLong(obj[4]));
				data.setLastPostBy(obj[5]!=null?(String)obj[5]:null);
				vo.add(data);
				
			}
		}

		

		return vo;
	}

	

}
