/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.calendar.dao;

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

import com.wo.module.calendar.constant.CalendarConstants;
import com.wo.module.calendar.model.Calendar;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.rc.constant.RCConstants;
import com.wo.module.rc.model.RC;

/**
 *
 * @author hendra
 */
@Repository("calendarDao")
public class CalendarDaoImpl extends GenericDAOHibernate<Calendar, Long> implements CalendarDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(CalendarDaoImpl.class);
	

	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CalendarConstants.SEARCH_BY_EVENT, col)) {
						sb.append(" and calendar_event_in LIKE :event ");
					}
					if (StringUtils.equals(CalendarConstants.SEARCH_BY_START_DATE, col)) {
						sb.append(" and START_DATE >= TO_DATE('"+val+"','YYYY-MM-DD') ");
					}
					if (StringUtils.equals(CalendarConstants.SEARCH_BY_END_DATE, col)) {
						sb.append(" and END_DATE <= TO_DATE('"+val+"','YYYY-MM-DD') ");
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
					if (StringUtils.equals(CalendarConstants.SEARCH_BY_EVENT, col)) {
						query.setParameter("event", "%" + val + "%");
					}
					/*if (StringUtils.equals(CalendarConstants.SEARCH_BY_START_DATE, col)) {
						query.setParameter("startDate", val );
					}
					if (StringUtils.equals(CalendarConstants.SEARCH_BY_END_DATE, col)) {
						query.setParameter("endDate", val );
					}*/
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
		sb.append(" from wo_mst_calendar ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<Calendar> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<Calendar> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<Calendar> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ct.CALENDAR_ID, ct.CALENDAR_EVENT_IN, ct.START_DATE, ct.END_DATE,ct.HOLIDAY_FLAG ");
		sb.append(" from wo_mst_calendar ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY CALENDAR_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<Calendar> vo = new ArrayList<Calendar>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				Calendar data = new Calendar();

				Long calendarId = MathUtil.returnIdObjectToLong(obj[0]);
				data.setCalendarId(calendarId);
				data.setCalendarEvent(obj[1]!=null?(String) obj[1]:null);
				data.setStartDate(obj[2]!=null?(Date) obj[2]:null);
				data.setEndDate(obj[3]!=null?(Date) obj[3]:null);
				data.setAllDayFlag(obj[4]!=null?((BigDecimal) obj[4]).intValue():null);

				vo.add(data);
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
	}

	

}
