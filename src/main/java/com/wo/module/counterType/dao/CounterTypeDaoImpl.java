/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.counterType.dao;

//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.counterType.constant.CounterTypeConstants;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.menu.dao.MenuDao;

/**
 *
 * @author hendra
 */
@Repository("counterTypeDao")
public class CounterTypeDaoImpl extends GenericDAOHibernate<CounterType, Long> implements CounterTypeDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(CounterTypeDaoImpl.class);
	@Autowired
	@Qualifier("menuDao")
	private MenuDao menuDao;

	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CounterTypeConstants.SEARCH_BY_NAME, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and UPPER(ct.counter_type_en) LIKE UPPER(:name) ");
						} else {
							sb.append(" and UPPER(ct.counter_type_in) LIKE UPPER(:name) ");
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
					if (StringUtils.equals(CounterTypeConstants.SEARCH_BY_NAME, col)) {
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
		sb.append(" from wo_mst_counter_type ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<CounterType> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<CounterType> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<CounterType> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ct.counter_type_id, ct.counter_type_in, ct.counter_type_en ");
		sb.append(" from wo_mst_counter_type ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY counter_type_id DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<CounterType> vo = new ArrayList<CounterType>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				CounterType data = new CounterType();

				//Long counterTypeId = ((BigInteger) obj[0]).longValue();
				Long counterTypeId = MathUtil.returnIdObjectToLong(obj[0]);
				data = findById(counterTypeId);

				/*
				 * data.setCounterTypeId(((BigInteger) obj[0]).longValue());
				 * data.setCounterTypeIn((String) obj[1]); data.setCounterTypeEn((String)
				 * obj[2]);
				 */

				vo.add(data);
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	public List<CounterType> getAllCounterType() throws Exception {
		/*
		 * String hql = "FROM CounterType where enabledFlag = 'Y' "; Query result =
		 * getSession().createQuery(hql); return result.getResultList();
		 */

		StringBuilder sb = new StringBuilder();
		sb.append(" select ct.counter_type_id, ct.counter_type_in, ct.counter_type_en ");
		sb.append(" from wo_mst_counter_type ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		sb.append(" ORDER BY counter_type_id DESC ");

		Query query = getSession().createSQLQuery(sb.toString());

		List resultList = query.getResultList();

		List<CounterType> vo = new ArrayList<CounterType>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				CounterType data = new CounterType();

				//Long counterTypeId = ((BigInteger) obj[0]).longValue();
				Long counterTypeId = MathUtil.returnIdObjectToLong(obj[0]);
				data = findById(counterTypeId);

				/*
				 * data.setCounterTypeId(((BigInteger) obj[0]).longValue());
				 * data.setCounterTypeIn((String) obj[1]); data.setCounterTypeEn((String)
				 * obj[2]);
				 */

				vo.add(data);
			}
		}
		return vo;
	}

	@SuppressWarnings("rawtypes")
	public Boolean isDataDuplicate(CounterType entity) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ct.counter_type_id, ct.counter_type_in, ct.counter_type_en ");
		sb.append(" from wo_mst_counter_type ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");
		sb.append(" and ct.counter_type_in = :counterTypeIn ");

		if (entity.getCounterTypeId() != null) {
			sb.append(" and ct.counter_type_id <> :countertypeId ");
		}

		Query query = getSession().createSQLQuery(sb.toString());

		query.setParameter("counterTypeIn", entity.getCounterTypeIn());

		if (entity.getCounterTypeId() != null) {
			query.setParameter("countertypeId", entity.getCounterTypeId());
		}

		List resultList = query.getResultList();

		if (resultList != null && resultList.size() > 0) {
			return true;
		}

		return false;
	}

	@SuppressWarnings("rawtypes")
	public Boolean isUsedInTransaction(Long id) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ct.counter_type_id ");
		sb.append(" from wo_mst_counter_type ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.counter_type_id = :counterTypeId ");
		sb.append(" and ( exists (select rmd.counter_type_id from wo_tmp_rmd rmd ");
		sb.append("                where rmd.counter_type_id = :counterTypeId and rmd.enabled_flag = 'Y') ");
		sb.append("     or exists (select cor.counter_type_id from wo_tmp_correspondence cor ");
		sb.append("                where cor.counter_type_id = :counterTypeId and cor.enabled_flag = 'Y') ");
		sb.append("     or exists (select soc.counter_type_id from wo_tmp_socialization soc ");
		sb.append("               where soc.counter_type_id = :counterTypeId  and soc.enabled_flag = 'Y') ");
		sb.append("     or exists (select aud.counter_type_id from wo_tmp_audit aud ");
		sb.append("               where aud.counter_type_id = :counterTypeId  and aud.enabled_flag = 'Y') ");
		sb.append("     or exists (select cr.counter_type_id from wo_tmp_compliance_review cr ");
		sb.append("               where cr.counter_type_id = :counterTypeId  and cr.enabled_flag = 'Y') ");
		sb.append("    ) ");
		
		Query query = getSession().createSQLQuery(sb.toString().trim());

		query.setParameter("counterTypeId", id);		

		List resultList = query.getResultList();

		if (resultList != null && resultList.size() > 0) {
			return true;
		}

		return false;
	}

}
