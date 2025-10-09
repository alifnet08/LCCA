package com.wo.module.qa.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.qa.constant.QAConstants;
import com.wo.module.qa.model.QA;

@Repository("qaBackEndDao")
public class QABackEndDaoImpl extends GenericDAOHibernate<QA, Long> implements QABackEndDao{

	@SuppressWarnings("rawtypes")
	private void setQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(QAConstants.WHERE_TICKET_NO, col)) {
						sb.append(" and upper(mq.TICKET_NO) like upper(:ticketNo) ");
					}
					if (StringUtils.equals(QAConstants.WHERE_QUESTION, col)) {
						sb.append(" and upper(mq.QUESTION) like upper(:question) ");
					}
					if (StringUtils.equals(QAConstants.WHERE_Q_TITLE, col)) {
						sb.append(" and upper(mq.Q_TITLE) like upper(:title) ");
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
					if (StringUtils.equals(QAConstants.WHERE_TICKET_NO, col)) {
						query.setParameter("ticketNo", "%"+val+"%");
					}
					if (StringUtils.equals(QAConstants.WHERE_QUESTION, col)) {
						query.setParameter("question", "%"+val+"%");
					}
					if (StringUtils.equals(QAConstants.WHERE_Q_TITLE, col)) {
						query.setParameter("title", "%"+val+"%");
					}
				}
				
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<QA> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select mq.QNA_ID ");
		sb.append("       ,mq.TICKET_NO ");
		sb.append("       ,mq.Q_USER_ID ");
		sb.append("       ,mq.Q_DATE ");
		sb.append("       ,mq.QUESTION ");
		sb.append("       ,mq.Q_TITLE ");
		sb.append("       ,mpd.NAME_IN ");
		sb.append("       ,mpd.NAME_EN ");
		sb.append("       ,mu.NAME Q_NAME ");
		sb.append("       ,mq.ADMIN_USER_ID ");
		sb.append("       ,mu1.NAME ADMIN_NAME ");
		sb.append("       ,mq.A_USER_ID ");
		sb.append("       ,mu2.NAME A_NAME ");
		sb.append("       ,mq.A_DATE ");
		sb.append("       ,mq.ANSWER ");
		sb.append("       ,mq.Q_STATUS ");
		sb.append(" from WO_MST_QNA mq ");
		sb.append("     inner join WO_MST_USER mu ");
		sb.append("         on mu.USER_ID = mq.Q_USER_ID ");
		sb.append("     left join WO_MST_USER mu1 ");
		sb.append("         on mu1.USER_ID = mq.ADMIN_USER_ID ");
		sb.append("     left join WO_MST_USER mu2 ");
		sb.append("         on mu2.USER_ID = mq.A_USER_ID ");
		sb.append("     inner join WO_MST_PARAMETER_DTL mpd ");
		sb.append("         on mpd.PARAMETER_DTL_CODE = mq.Q_STATUS ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     and mq.ENABLED_FLAG = 'Y' ");
		
		this.setQueryWhereString(sb, searchCriteria);
		
		sb.append(" ORDER BY mq.QNA_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		this.getQueryWhereString(query, searchCriteria);
		
		List resultList = query.getResultList();
		List<QA> vo = new ArrayList<QA>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				QA data = new QA();
				
				data.setQnaId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setTicketNo(obj[1] != null ? (String) obj[1] : null);
				data.setqUserId(obj[2] != null ? MathUtil.returnIdObjectToLong(obj[2]) : null);
				data.setqDate(obj[3] != null ? (Date) obj[3] : null);
				data.setQuestion(obj[4] != null ? (String) obj[4] : null);
				data.setqTitle(obj[5] != null ? (String) obj[5] : null);
				data.setStatusNameIn(obj[6] != null ? (String) obj[6] : null);
				data.setStatusNameIn(obj[7] != null ? (String) obj[7] : null);
				data.setqName(obj[8] != null ? (String) obj[8] : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select COUNT(1) ");
		sb.append(" from WO_MST_QNA mq ");
		sb.append("     inner join WO_MST_USER mu ");
		sb.append("         on mu.USER_ID = mq.Q_USER_ID ");
		sb.append("     left join WO_MST_USER mu1 ");
		sb.append("         on mu1.USER_ID = mq.ADMIN_USER_ID ");
		sb.append("     left join WO_MST_USER mu2 ");
		sb.append("         on mu2.USER_ID = mq.A_USER_ID ");
		sb.append("     inner join WO_MST_PARAMETER_DTL mpd ");
		sb.append("         on mpd.PARAMETER_DTL_CODE = mq.Q_STATUS ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     and mq.ENABLED_FLAG = 'Y' ");
		
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
