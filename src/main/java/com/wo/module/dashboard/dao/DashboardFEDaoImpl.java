
package com.wo.module.dashboard.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.dashboard.vo.MapVO;


@Repository("dashboardFEDao")
public class DashboardFEDaoImpl extends GenericDAOHibernate<Object, Long> implements DashboardFEDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(DashboardDaoImpl.class);

	public Long getCountUserLogin() throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		//sb.append(" WITH query1 as( select username  from WO_LOG_LOGIN where TRUNC(ACCESS_TIME) = TRUNC(SYSDATE) and (last_logout is null or last_login > last_logout) group by username) select count(1) from query1 ");
		sb.append(" WITH query1 as( select username,max(LOG_LOGIN_ID) log_login_id  from WO_LOG_LOGIN where TRUNC(ACCESS_TIME) = TRUNC(SYSDATE) group by username), "
				+ " query2 as(SELECT user_id, MAX(ACCESS_TIME) max_access_time FROM WO_LOG_ACCESS where TRUNC(ACCESS_TIME) = TRUNC(SYSDATE) group by user_id) "
				+ " select count(1) from query1 q INNER JOIN WO_LOG_LOGIN l ON q.log_login_id= l.LOG_LOGIN_ID "
				+ " LEFT JOIN query2 q2 ON q2.user_id = l.USER_ID "
				+ " WHERE last_logout IS NULL "
				+ " AND (max_access_time IS NULL OR (24 * (to_date(to_char(sysdate, 'YYYY-MM-DD hh24:mi'), 'YYYY-MM-DD hh24:mi') - to_date(to_char(max_access_time, 'YYYY-MM-DD hh24:mi'), 'YYYY-MM-DD hh24:mi'))<1))");

		Query query = getSession().createSQLQuery(sb.toString());

		return ((Number) query.getSingleResult()).longValue();
	}
	
	/*public Long getCountUserLogin() throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		//sb.append(" WITH query1 as( select username  from WO_LOG_LOGIN where TRUNC(ACCESS_TIME) = TRUNC(SYSDATE) and (last_logout is null or last_login > last_logout) group by username) select count(1) from query1 ");
//		sb.append(" WITH query1 as( select username,max(LOG_LOGIN_ID) log_login_id  from WO_LOG_LOGIN where TRUNC(ACCESS_TIME) = TRUNC(SYSDATE) group by username), "
//				+ " query2 as(SELECT user_id, MAX(ACCESS_TIME) max_access_time FROM WO_LOG_ACCESS where TRUNC(ACCESS_TIME) = TRUNC(SYSDATE) group by user_id) "
//				+ " select count(1) from query1 q INNER JOIN WO_LOG_LOGIN l ON q.log_login_id= l.LOG_LOGIN_ID "
//				+ " LEFT JOIN query2 q2 ON q2.user_id = l.USER_ID "
//				+ " WHERE last_logout IS NULL "
//				+ " AND (max_access_time IS NULL OR (24 * (to_date(to_char(sysdate, 'YYYY-MM-DD hh24:mi'), 'YYYY-MM-DD hh24:mi') - to_date(to_char(max_access_time, 'YYYY-MM-DD hh24:mi'), 'YYYY-MM-DD hh24:mi'))<1))");

		sb.append(" WITH query1 as( select username,max(LOG_LOGIN_ID) log_login_id  from WO_LOG_LOGIN where TRUNC(ACCESS_TIME) = TRUNC(SYSDATE) group by username), "
				+ " query2 as(SELECT user_id, MAX(ACCESS_TIME) max_access_time FROM WO_LOG_ACCESS where TRUNC(ACCESS_TIME) = TRUNC(SYSDATE) group by user_id) "
				+ " select count(1) from query1 q INNER JOIN WO_LOG_LOGIN l ON q.log_login_id= l.LOG_LOGIN_ID "
				+ " INNER JOIN query2 q2 ON q2.user_id = l.USER_ID "
				+ " WHERE last_logout IS NULL "
				+ " AND (max_access_time IS NULL OR (24 * (to_date(to_char(sysdate, 'YYYY-MM-DD hh24:mi'), 'YYYY-MM-DD hh24:mi') - to_date(to_char(max_access_time, 'YYYY-MM-DD hh24:mi'), 'YYYY-MM-DD hh24:mi')))<1)");
		
		Query query = getSession().createSQLQuery(sb.toString());

		return ((Number) query.getSingleResult()).longValue();
	}*/
	
	public List<MapVO> getMapData() throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct p.province, pl.pin_top, pl.pin_left from WO_LOG_LOGIN l ");
		sb.append(" inner join wo_mst_user u on u.user_id = l.user_id ");
		sb.append(" left join wo_mst_province p on P.BRANCH_CODE = u.branch_code ");
		sb.append(" left join wo_mst_province_location pl on p.province = pl.province ");
		sb.append(" where TRUNC(ACCESS_TIME) = TRUNC(SYSDATE) and u.enabled_flag = 'Y' and p.enabled_flag = 'Y' and  p.enabled_flag = 'Y' ");

		Query query = getSession().createSQLQuery(sb.toString());

		List resultList = query.getResultList();
		
		List<MapVO> vo = new ArrayList<MapVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				MapVO data = new MapVO();
				data.setProvince(obj[0]!=null?(String) obj[0]:null);
				data.setPinTop(obj[1]!=null?((java.math.BigDecimal) obj[1]).intValue():null);
				data.setPinLeft(obj[2]!=null?((java.math.BigDecimal) obj[2]).intValue():null);
				vo.add(data);
			}
		}
		
		return vo;
	}
	
}
