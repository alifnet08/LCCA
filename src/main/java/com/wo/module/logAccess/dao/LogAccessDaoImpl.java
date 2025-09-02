/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.logAccess.dao;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.logAccess.constant.LogAccessConstants;
import com.wo.module.logAccess.model.LogAccess;

/**
 *
 * @author hendra
 */
@Repository("logAccessDao")
public class LogAccessDaoImpl extends GenericDAOHibernate<LogAccess, Long> implements LogAccessDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(LogAccessDaoImpl.class);

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
					else if (StringUtils.equals(LogAccessConstants.WHERE_DIV_NAME, col)) {
						sb.append(" AND UPPER(u.DIVISION_NAME) like UPPER(:divName) ");
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
					}
					if (StringUtils.equals(LogAccessConstants.WHERE_DIV_NAME, col)) {
						query.setParameter("divName", "%"+val+"%");
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
		sb.append(" FROM wo_log_access l ");
		sb.append("     INNER JOIN WO_MST_USER u ");
		sb.append("         ON u.USER_ID = l.USER_ID ");
		sb.append(" WHERE 1 = 1 ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<LogAccess> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<LogAccess> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<LogAccess> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT  l.LOG_ACCESS_ID  ");
		sb.append("       ,NAME ");
		sb.append("       ,SOURCE_IP ");
		sb.append("       ,l.ACCESS_TIME ");
		sb.append("       ,l.ACCESS_ACTION ");
		sb.append("       ,l.ACCESS_ID,u.DIVISION_NAME ");
		sb.append(" FROM WO_LOG_ACCESS l ");
		sb.append("     INNER JOIN WO_MST_USER u ");
		sb.append("         ON u.USER_ID = l.USER_ID ");
		sb.append(" WHERE 1 = 1 ");

		this.getQueryWhereString(sb, searchCriteria);
		
		sb.append(" ORDER BY l.LOG_ACCESS_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<LogAccess> vo = new ArrayList<LogAccess>();
		String hostLink = linkHostAccess();
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				LogAccess data = new LogAccess();

				data.setLogAccessId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setUserName(obj[1] != null ? (String) obj[1] : null);
				data.setSourceIp(obj[2] != null ? (String) obj[2] : null);
				data.setAccessTime(obj[3] != null ? (Timestamp) obj[3] : null);
				data.setAccessAction(obj[4] != null ? (hostLink + (String) obj[4]) : null);
				data.setAccessId(obj[5] != null ? MathUtil.returnIdObjectToLong(obj[5]) : null);
				data.setDivisionName(obj[6] != null ? (String) obj[6] : null);
				
				vo.add(data);
			}
		}

		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	private String linkHostAccess() {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT REPLACE(NAME_IN, '/compliance/') HOST_LINK, '' EMPTY ");
		sb.append("   FROM wo_mst_parameter_dtl ");
		sb.append("  WHERE parameter_dtl_code = 'HOST_NAME_APPLICATION' ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		List resultList = query.getResultList();
		
		String linkHost = "";
		if(resultList !=null && resultList.size() > 0) {
			Object[] obj = (Object[]) resultList.get(0);
		
			linkHost = (String)obj[0];
		}
				
		return linkHost;
	}
	
	

	
	

}
