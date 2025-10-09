/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.aboutUs.dao;

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

import com.wo.module.aboutUs.constant.AboutUsConstants;
import com.wo.module.aboutUs.model.AboutUs;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;

/**
 *
 * @author hendra
 */
@Repository("aboutUsDao")
public class AboutUsDaoImpl extends GenericDAOHibernate<AboutUs, Long> implements AboutUsDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(AboutUsDaoImpl.class);
	

	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(AboutUsConstants.SEARCH_BY_NAME, col)) {
						
							sb.append(" and UPPER(our_name) LIKE UPPER(:name) ");
						
					}
					if (StringUtils.equals(AboutUsConstants.SEARCH_BY_POSITION, col)) {
							sb.append(" and UPPER(our_job) LIKE UPPER(:position) ");
						
					}
					if (StringUtils.equals(AboutUsConstants.SEARCH_BY_EXT, col)) {
						sb.append(" and UPPER(ext) like UPPER(:ext) ");
					
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
					if (StringUtils.equals(AboutUsConstants.SEARCH_BY_NAME, col)) {
						query.setParameter("name", "%" + val + "%");
					}
					if (StringUtils.equals(AboutUsConstants.SEARCH_BY_POSITION, col)) {
						query.setParameter("position", "%" + val + "%");
					}
					if (StringUtils.equals(AboutUsConstants.SEARCH_BY_EXT, col)) {
						query.setParameter("ext", "%" + val + "%");
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
		sb.append(" from wo_mst_about_us ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<AboutUs> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<AboutUs> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<AboutUs> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ct.ABOUT_US_ID, ct.OUR_NAME ");
		sb.append(" from wo_mst_about_us ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY ABOUT_US_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<AboutUs> vo = new ArrayList<AboutUs>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				AboutUs data = new AboutUs();

				Long aboutUsId = MathUtil.returnIdObjectToLong(obj[0]);
				data = findById(aboutUsId);
				
				
				
				
				vo.add(data);
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
	}

	

}
