/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.rc.dao;

//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.rc.constant.RCConstants;
import com.wo.module.rc.model.RC;

/**
 *
 * @author hendra
 */
@Repository("rcDao")
public class RCDaoImpl extends GenericDAOHibernate<RC, Long> implements RCDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(RCDaoImpl.class);
	

	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(RCConstants.SEARCH_BY_REGION, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and (upper(ct.REGION_CODE) LIKE upper(:name) OR upper(ct.WORKING_UNIT) LIKE upper(:name) OR upper(ct.REGION) LIKE upper(:name)) ");
						} else {
							sb.append(" and (upper(ct.REGION_CODE) LIKE upper(:name) OR upper(ct.WORKING_UNIT) LIKE upper(:name) OR upper(ct.REGION) LIKE upper(:name)) ");
						}
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
					if (StringUtils.equals(RCConstants.SEARCH_BY_REGION, col)) {
						query.setParameter("name", "%" + val + "%");
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
		sb.append(" from wo_mst_rc ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<RC> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<RC> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<RC> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ct.RC_ID, ct.REGION_CODE, ct.WORKING_UNIT, ct.REGION ");
		sb.append(" from wo_mst_rc ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY RC_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<RC> vo = new ArrayList<RC>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				RC data = new RC();

				Long rcId = MathUtil.returnIdObjectToLong(obj[0]);
				data.setRcId(rcId);
				data.setRegionCode(obj[1]!=null?(String) obj[1]:null);
				data.setWorkingUnit(obj[2]!=null?(String) obj[2]:null);
				data.setRegion(obj[3]!=null?(String) obj[3]:null);

				
				vo.add(data);
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	public List<RC> getRCList() {
		
		StringBuilder sb = new StringBuilder();
		sb.append(" select ct.RC_ID, ct.REGION_CODE, ct.WORKING_UNIT, ct.REGION ");
		sb.append(" from wo_mst_rc ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");
		sb.append(" ORDER BY RC_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		List resultList = query.getResultList();
		
		List<RC> vo = new ArrayList<RC>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				RC data = new RC();

				Long rcId = MathUtil.returnIdObjectToLong(obj[0]);
				data.setRcId(rcId);
				data.setRegionCode(obj[1]!=null?(String) obj[1]:null);
				data.setWorkingUnit(obj[2]!=null?(String) obj[2]:null);
				data.setRegion(obj[3]!=null?(String) obj[3]:null);
				vo.add(data);
			}
		}
		
		return vo;
	}

	
	

}
