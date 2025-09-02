/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.holiday.dao;

//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.holiday.model.Holiday;
import com.wo.module.user.model.User;

/**
 *
 * @author hendra
 * 
 *         Modification Alex
 */

@Repository("holidayDao")
public class HolidayDaoImpl extends GenericDAOHibernate<Holiday, Long> implements HolidayDao {

	@SuppressWarnings({ "rawtypes", "static-access" })
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(SearchObject.ALL_COLUMNS, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and UPPER(holiday_name) LIKE UPPER('%" + val + "%') ");
						} else {
							sb.append(" and UPPER(holiday_name) LIKE UPPER('%" + val + "%') ");
						}
					}
				}
			}
		}

		return sb;
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
		sb.append(" FROM wo_mst_holiday h ");
		sb.append(" WHERE 1=1 and h.enabled_flag = 'Y' ");

		sb = getQueryWhereString(sb, searchCriteria);

		Query result = getSession().createSQLQuery(sb.toString());

		return (Number) result.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<Holiday> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<Holiday> voList = searchDataCriteria(searchCriteria, first, pageSize);

		return voList;
	}

	@SuppressWarnings("rawtypes")
	private List<Holiday> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {

		StringBuilder sb = new StringBuilder();
		sb.append(" select holiday_id, holiday_name, holiday_date_from, holiday_date_to  ");
		sb.append(" FROM wo_mst_holiday h ");
		sb.append(" WHERE 1=1 and h.enabled_flag = 'Y' ");

		sb = getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY holiday_id DESC ");
		

		Query result = getSession().createSQLQuery(sb.toString());
		result.setFirstResult(first);
		result.setMaxResults(pageSize);
		
		List resultList = result.getResultList();

		List<Holiday> vo = new ArrayList<Holiday>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				Holiday data = new Holiday();
				//data.setHolidayId(((BigInteger) obj[0]).longValue());
				data.setHolidayId(MathUtil.returnIdObjectToLong(obj[0]));
				data.setHolidayName((String) obj[1]);
				data.setHolidayDateFrom(obj[2] != null ? (Date) obj[2] : null);
				data.setHolidayDateTo(obj[3] != null ? (Date) obj[3] : null);

				vo.add(data);
			}
		}

		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		return vo;
	}

	@Override
	public Integer getHolidayByName(String name) throws Exception {

		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT COUNT(1) " + "		FROM wo_mst_holiday ");

		sb.append("		WHERE holiday_name = '" + name + "'");

		Query result = getSession().createSQLQuery(sb.toString());

		Number count = (Number) result.getSingleResult();
		if (count == null) {
			count = 0;
		}

		return (Integer) count.intValue();
	}

	@Override
	public Integer getEditHolidayByIdAndName(Long holidayId, String holidayName) throws Exception {

		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT COUNT(1) ");
		sb.append("	FROM wo_mst_holiday ");
		sb.append(" WHERE holiday_id <> '" + holidayId + "'");
		sb.append("		AND holiday_name = '" + holidayName + "' ");
		sb.append("		AND enabled_flag = 'Y' ");

		Query result = getSession().createSQLQuery(sb.toString());

		Number count = (Number) result.getSingleResult();
		if (count == null) {
			count = 0;
		}

		return (Integer) count.intValue();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Holiday getHolidayBySameData(Long holidayId, String holidayName) throws Exception {

		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT holiday_id, holiday_name, enabled_flag" + "	FROM wo_mst_holiday "
				+ "	WHERE holiday_name = '" + holidayName + "' ");

		if (holidayId != null && holidayId > 0) {
			sb.append("	AND holiday_id <> 0");
		}

		Query result = getSession().createSQLQuery(sb.toString());
		result.setMaxResults(1);

		List resultList = result.getResultList();

		Holiday holiday = new Holiday();

		if (resultList != null && resultList.size() > 0) {
			Object[] obj = (Object[]) resultList.get(0);
			//holiday.setHolidayId(obj[0] != null ? ((BigInteger) obj[0]).longValue() : null);
			holiday.setHolidayId(obj[0] != null ? (MathUtil.returnIdObjectToLong(obj[0])) : null);
			holiday.setHolidayName((String) obj[1]);
			holiday.setEnabledFlag((String) obj[2]);
		}

		return holiday;
	}
	
	public Holiday getHolidayDataByName(String name)  {
		 if(name != null) {
			String hql = "FROM Holiday where UPPER(holidayName) = UPPER(:name) and enabledFlag = 'Y' ";
			Query result = getSession().createQuery(hql);
			result.setParameter("name", name);
			List list = result.getResultList();
			
			if(list.size() > 0) {
				return (Holiday) list.get(0);
			}else {
				return null;
			}
			
		 } else {
			 return null;
		 }
	}

	@Override
	public Boolean isAvailableDate(Date date) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT count(1) ");
		sb.append(" FROM WO_MST_HOLIDAY wmh ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" 	AND wmh.enabled_flag = 'Y' ");
		sb.append(" 	AND :targetDate BETWEEN wmh.HOLIDAY_DATE_FROM AND wmh.HOLIDAY_DATE_TO ");

		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("targetDate", date);

		Number result = (Number) query.getSingleResult();
		
		if (result == null) {
			result = 0;
		}
		
		if (result.longValue() == 0) {
			return true;
		} else {
			return false;
		}

	}
}
