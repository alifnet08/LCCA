/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.staticPage.dao;

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

import com.wo.module.staticPage.constant.StaticPageConstants;
import com.wo.module.staticPage.model.StaticPage;
import com.wo.module.user.model.User;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;


/**
 *
 * @author hendra
 */
@Repository("staticPageDao")
public class StaticPageDaoImpl extends GenericDAOHibernate<StaticPage, Long> implements StaticPageDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(StaticPageDaoImpl.class);
	

	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(StaticPageConstants.SEARCH_BY_CONTENT, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and content_in LIKE :content ");
						} else {
							sb.append(" and content_en LIKE :content ");
						}
					}
					if (StringUtils.equals(StaticPageConstants.SEARCH_BY_TITLE, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and title_in LIKE :title ");
						} else {
							sb.append(" and title_en LIKE :title ");
						}
						
					}
					
					if (StringUtils.equals(StaticPageConstants.SEARCH_BY_CATEGORY, col)) {
						sb.append(" and static_page_category LIKE :category ");
						
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
					if (StringUtils.equals(StaticPageConstants.SEARCH_BY_CONTENT, col)) {
						query.setParameter("content", "%" + val + "%");
					}
					if (StringUtils.equals(StaticPageConstants.SEARCH_BY_TITLE, col)) {
						query.setParameter("title", val);
					}
					if (StringUtils.equals(StaticPageConstants.SEARCH_BY_CATEGORY, col)) {
						query.setParameter("category", val);
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
		sb.append(" from wo_mst_static_page ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<StaticPage> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<StaticPage> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<StaticPage> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ct.STATIC_PAGE_ID,ct.STATIC_PAGE_CATEGORY");
		sb.append(" from wo_mst_static_page ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY STATIC_PAGE_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<StaticPage> vo = new ArrayList<StaticPage>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				StaticPage data = new StaticPage();

				Long staticPageId = MathUtil.returnIdObjectToLong(obj[0]);
				data = findById(staticPageId);
				
				
				
				
				vo.add(data);
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
	}

	public StaticPage getStaticPageByCategory(String category)  {
		 if(category != null) {
			String hql = "FROM StaticPage where staticPageCategory.parameterDtlCode = :category and enabledFlag = 'Y' ";
			Query result = getSession().createQuery(hql);
			result.setParameter("category", category);
			List list = result.getResultList();
			
			if(list.size() > 0) {
				return (StaticPage) list.get(0);
			}else {
				return null;
			}
			
		 } else {
			 return null;
		 }
	}

}
