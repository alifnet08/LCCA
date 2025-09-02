/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.report.reportLogAccess.dao;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.logAccess.constant.LogAccessConstants;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportLogAccess.vo.ReportLogAccessVo;

@Repository("reportLogAccessDao")
public class ReportLogAccessDaoImpl extends GenericDAOHibernate<ReportGen, Long> implements ReportLogAccessDao, Serializable {

	private static final long serialVersionUID = 8653091813576218327L;
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ReportLogAccessDaoImpl.class);

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
						sb.append(" and TRUNC(ACCESS_TIME) >= TRUNC(TO_DATE('" + val + "', 'yyyy-MM-dd')) ");
					}
					else if (StringUtils.equals(LogAccessConstants.WHERE_DATE_END, col)) {
						sb.append(" and TRUNC(ACCESS_TIME) <= TRUNC(TO_DATE('" + val + "', 'yyyy-MM-dd')) ");						
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
	@Override
	public List<ReportLogAccessVo> getDataReport(List<? extends SearchObject> searchCriteria) {
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

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<ReportLogAccessVo> vo = new ArrayList<ReportLogAccessVo>();
		String hostLink = linkHostAccess();
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ReportLogAccessVo data = new ReportLogAccessVo();

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
