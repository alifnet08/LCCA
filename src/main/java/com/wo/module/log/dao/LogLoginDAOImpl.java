package com.wo.module.log.dao;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.log.constant.LogConstant;
import com.wo.module.log.model.LogLogin;
import com.wo.module.logAccess.constant.LogAccessConstants;

@Repository("logLoginDAO")
public class LogLoginDAOImpl extends GenericDAOHibernate<LogLogin, Long> implements LogLoginDAO {
	
	@SuppressWarnings({ "rawtypes" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {

					if (StringUtils.equals(LogAccessConstants.WHERE_NAME, col)) {
						sb.append(" AND UPPER(u.NAME) like UPPER(:name) ");
					}
					else if (StringUtils.equals(LogAccessConstants.WHERE_DATE_START, col)) {
						try {
							sb.append(" and TRUNC(ACCESS_TIME) >= TO_DATE('" + sdf2.format(sdf.parse(val)) + "','yyyy-MM-dd') ");
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					else if (StringUtils.equals(LogAccessConstants.WHERE_DATE_END, col)) {
						try {
							sb.append(" and TRUNC(ACCESS_TIME) <= TO_DATE('" + sdf2.format(sdf.parse(val)) + "','yyyy-MM-dd') ");
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					else if (StringUtils.equals(LogConstant.WHERE_CREATED_BY, col)) {
						sb.append(" and u.CREATED_BY like :createdBy ");
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

					if (StringUtils.equals(LogAccessConstants.WHERE_NAME, col)) {
						query.setParameter("name", "%"+val+"%");
					}else if (StringUtils.equals(LogConstant.WHERE_CREATED_BY, col)) {
						if(StringUtils.isNotBlank(val) && !val.equals("null")) {
							query.setParameter("createdBy", "%"+val+"%");
						}else {
							query.setParameter("createdBy", "%%");
						}
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

		sb.append(" SELECT COUNT(1) COUNT ");
		sb.append(" FROM wo_log_login l ");
		sb.append("     INNER JOIN WO_MST_USER u ");
		sb.append("         ON u.USER_ID = l.USER_ID ");
		sb.append(" WHERE 1 = 1 ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<LogLogin> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<LogLogin> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<LogLogin> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT  l.LOG_LOGIN_ID  ");
		sb.append("       ,NAME ");
		sb.append("       ,u.DIVISION_NAME ");
		sb.append("       ,l.ACCESS_TIME ");
		sb.append("       ,l.RETRY_ATTEMPT ");
		sb.append("       ,l.LAST_LOGIN ");
		sb.append("       ,l.LAST_LOGOUT ");
		sb.append("		  ,u.CREATED_BY ");
		sb.append(" FROM WO_LOG_LOGIN l ");
		sb.append("     INNER JOIN WO_MST_USER u ");
		sb.append("         ON u.USER_ID = l.USER_ID ");
		sb.append(" WHERE 1 = 1 ");

		this.getQueryWhereString(sb, searchCriteria);
		
		sb.append(" ORDER BY l.LOG_LOGIN_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<LogLogin> vo = new ArrayList<LogLogin>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				LogLogin data = new LogLogin();

				data.setLoginId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setUsername(obj[1] != null ? (String) obj[1] : null);
				data.setDivisionName(obj[2] != null ? (String) obj[2] : null);
				data.setAccessTime(obj[3] != null ? (Timestamp) obj[3] : null);
				data.setRetryAttempt(obj[4] != null ? ((java.math.BigDecimal) obj[4]).intValue() : null);
				data.setLastLogin(obj[5] != null ? (Timestamp) obj[5] : null);
				data.setLastLogout(obj[6] != null ? (Timestamp) obj[6] : null);
				data.setUserCreatedBy(obj[7] != null ? (String) obj[7] : null);
				
				vo.add(data);
			}
		}

		return vo;
	}
}
