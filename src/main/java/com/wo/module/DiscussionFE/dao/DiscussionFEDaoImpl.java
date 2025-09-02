package com.wo.module.DiscussionFE.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.DiscussionFE.vo.DiscussionFEVo;
import com.wo.module.DiscussionFE.vo.DiscussionPostFEVo;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.discussion.model.Discussion;

@Repository("discussionFEDao")
public class DiscussionFEDaoImpl extends GenericDAOHibernate<Discussion, Long>
	implements DiscussionFEDao, Serializable{

	private static final long serialVersionUID = 3794652827700130301L;

	@SuppressWarnings("rawtypes")
	private void setQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						sb.append(" and UPPER(THREAD_NAME_IN) like UPPER(:threadName) ");
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
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						query.setParameter("threadName", "%" + val + "%");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<DiscussionFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		List<DiscussionFEVo> vo = searchDataCriteria(searchCriteria, first, pageSize);
		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	public List<DiscussionFEVo> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT distinct d.DISCUSSION_ID ");
		sb.append("       ,d.THREAD_NAME_IN ");
		sb.append("       ,d.THREAD_START_DATE ");
		sb.append("       ,TO_CHAR(d.THREAD_START_DATE, 'DD FMMonth YYYY hh:mm', 'nls_date_language=indonesian') THREAD_START_DATE_STR ");
		sb.append("       ,d.THREAD_RATING ");
		sb.append("       ,uThreadStart.NAME STARTED_BY ");
		//sb.append("       ,uPostBy.NAME LAST_POST_BY ");
		sb.append("       ,(select name from wo_mst_user where user_id = (select MAX(POSTED_BY_ID) from WO_MST_DISCUSSION_POST dp2  where dp2.discussion_id = d.DISCUSSION_ID)) LAST_POST_BY ");
		sb.append("       ,dp.POSTED_DATE ");
		sb.append("       ,TO_CHAR(dp.POSTED_DATE, 'DD FMMonth YYYY hh:mm', 'nls_date_language=indonesian') LAST_POST_DATE_STR ");
		sb.append(" FROM WO_MST_DISCUSSION d ");
		sb.append("     INNER JOIN WO_MST_USER uThreadStart ");
		sb.append("         ON uThreadStart.USER_ID = d.THREAD_USER_ID ");
		sb.append("     LEFT JOIN WO_MST_DISCUSSION_POST dp ");
		sb.append("         ON dp.DISCUSSION_ID = d.DISCUSSION_ID ");
		sb.append("     LEFT JOIN WO_MST_USER uPostBy ");
		sb.append("         ON uPostBy.USER_ID = dp.POSTED_BY_ID ");
		sb.append(" WHERE 1 = 1 ");
		//sb.append("     AND dp.DISCUSSION_POST_ID IN(SELECT MAX(dp1.DISCUSSION_POST_ID) FROM WO_MST_DISCUSSION_POST dp1) ");
		sb.append("     AND d.ENABLED_FLAG = 'Y' ");
		
		this.setQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY d.DISCUSSION_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQuerySetString(query, searchCriteria);
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		List result = query.getResultList();
		List<DiscussionFEVo> vo = new ArrayList<DiscussionFEVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				DiscussionFEVo data = new DiscussionFEVo();
				
				data.setDiscussionId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setThread(obj[1] != null ? (String) obj[1] : null);
				data.setStartDate(obj[2] != null ? (Date) obj[2] : null);
				data.setStartDateStr(obj[3] != null ? (String) obj[3] : null);
				data.setRating(obj[4] != null ? MathUtil.returnIdObjectToLong(obj[4]) : null);
				data.setStartby(obj[5] != null ? (String) obj[5] : null);
				data.setLastPostBy(obj[6] != null ? (String) obj[6] : null);
				data.setLastPostDate(obj[7] != null ? (Date) obj[7] : null);
				data.setLastPostDateStr(obj[8] != null ? (String) obj[8] : null);
				
				data.setReplies(obj[0] != null ? getReplies(MathUtil.returnIdObjectToLong(obj[0])) : 0);
				data.setViews(obj[0] != null ? getViews(MathUtil.returnIdObjectToLong(obj[0])) : 0);
				
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
	public Number searchCountDataCriteria(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT COUNT(distinct d.discussion_id) ");
		sb.append(" FROM WO_MST_DISCUSSION d ");
		sb.append("     INNER JOIN WO_MST_USER uThreadStart ");
		sb.append("         ON uThreadStart.USER_ID = d.THREAD_USER_ID ");
		sb.append("     LEFT JOIN WO_MST_DISCUSSION_POST dp ");
		sb.append("         ON dp.DISCUSSION_ID = d.DISCUSSION_ID ");
		sb.append("     LEFT JOIN WO_MST_USER uPostBy ");
		sb.append("         ON uPostBy.USER_ID = dp.POSTED_BY_ID ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND d.ENABLED_FLAG = 'Y' ");
		
		this.setQueryWhereString(sb, searchCriteria);
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQuerySetString(query, searchCriteria);
		
		Number result = (Number) query.getSingleResult();
		
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<DiscussionPostFEVo> getDiscussionPostDataById(Long discussionId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT dp.DISCUSSION_POST_ID DETAIL_ID ");
		sb.append("       ,dp.DISCUSSION_ID HEADER_ID ");
		sb.append("       ,uPostBy.NAME POSTED_NAME ");
		sb.append("       ,dp.POSTED_DATE ");
		sb.append("       ,TO_CHAR(dp.POSTED_DATE, 'dd FMMonth yyyy', 'nls_date_language=indonesian') POSTED_DATE_STR ");
		sb.append("       ,dp.POSTED_COMMENT ");
		sb.append("       ,dp.POSTED_RATING ");
		sb.append("       ,dp.POSTED_BY_ID ");
		sb.append("       ,dp.CREATION_DATE ");
		sb.append(" FROM WO_MST_DISCUSSION_POST dp ");
		sb.append("     LEFT JOIN WO_MST_USER uPostBy ");
		sb.append("         ON uPostBy.USER_ID = dp.POSTED_BY_ID ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" 	AND dp.DISCUSSION_ID = :discussionId ");
		sb.append("     AND dp.ENABLED_FLAG = 'Y' ");
		sb.append(" ORDER BY dp.DISCUSSION_ID ASC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("discussionId", discussionId);
		
		List result = query.getResultList();
		List<DiscussionPostFEVo> vo = new ArrayList<DiscussionPostFEVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				DiscussionPostFEVo data = new DiscussionPostFEVo();
				
				data.setDiscussionPostId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setDiscussionId(obj[1] != null ? MathUtil.returnIdObjectToLong(obj[1]) : null);
				data.setPostedByName(obj[2] != null ? (String) obj[2] : null);
				data.setPostedDate(obj[3] != null ? (Date) obj[3] : null);
				data.setPostedDateStr(obj[4] != null ? (String) obj[4] : null);
				data.setPostedComment(obj[5] != null ? (String) obj[5] : null);
				data.setPostedRating(obj[6] != null ? MathUtil.returnIdObjectToLong(obj[6]) : null);
				data.setPostedById(obj[7] != null ? MathUtil.returnIdObjectToLong(obj[7]) : null);
				data.setCreationDate(obj[8] != null ? (Date) obj[8] : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@Override
	public Long getReplies(Long discussionId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select COUNT(1) ");
		sb.append(" from WO_MST_DISCUSSION_POST dp ");
		sb.append("     INNER JOIN WO_MST_DISCUSSION d ");
		sb.append("         ON dp.DISCUSSION_ID = d.DISCUSSION_ID ");
		sb.append(" where 1 = 1 ");
		sb.append("     and  dp.ENABLED_FLAG <> 'N' ");
		sb.append("     and dp.DISCUSSION_ID = :discussionId ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("discussionId", discussionId);
		
		Number result = (Number) query.getSingleResult();
		
		if (result == null) {
			result = 0;
		}
		
		return result.longValue();
	}

	@Override
	public Long getViews(Long discussionId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select count(1) ");
		sb.append(" from wo_log_access la ");
		sb.append(" where 1 = 1 ");
		sb.append("     and la.access_action = '/compliance/pages/discussionFE/discussionFE.faces' ");
		sb.append("     and la.access_id = :discussionId ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("discussionId", discussionId);
		
		Number result = (Number) query.getSingleResult();
		
		if (result == null) {
			result = 0;
		}
		
		return result.longValue();
	}
}
