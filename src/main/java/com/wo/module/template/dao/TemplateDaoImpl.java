/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.template.dao;

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
import com.wo.module.template.constant.TemplateConstants;
import com.wo.module.template.model.Template;

/**
 *
 * @author hendra
 */
@Repository("templateDao")
public class TemplateDaoImpl extends GenericDAOHibernate<Template, Long> implements TemplateDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(TemplateDaoImpl.class);
	

	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(TemplateConstants.SEARCH_BY_CATEGORY, col)) {
						sb.append(" and UPPER(template_category) like UPPER(:category) ");
					}
					if (StringUtils.equals(TemplateConstants.SEARCH_BY_SUB_CATEGORY, col)) {
							sb.append(" and UPPER(template_subcategory) like UPPER(:subCategory) ");
						
					}
					if (StringUtils.equals(TemplateConstants.SEARCH_BY_DATE, col)) {
						sb.append(" and TRUNC(creation_date) <= TO_DATE(:dateTo,'yyyy-MM-dd') ");
					
					}
					if (StringUtils.equals(TemplateConstants.SEARCH_BY_SUBJECT, col)) {
						sb.append(" and UPPER(perihal_In) like UPPER(:perihalIn) ");
					
					}
					if (StringUtils.equals(TemplateConstants.SEARCH_BY_INFORMATION, col)) {
						sb.append(" and UPPER(note) like UPPER(:note) ");
					
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
					if (StringUtils.equals(TemplateConstants.SEARCH_BY_CATEGORY, col)) {
						query.setParameter("category", "%" + val + "%");
					}
					if (StringUtils.equals(TemplateConstants.SEARCH_BY_SUB_CATEGORY, col)) {
						query.setParameter("subCategory", "%" + val + "%");
					}
					if (StringUtils.equals(TemplateConstants.SEARCH_BY_DATE, col)) {
						query.setParameter("date", val);
					}
					if (StringUtils.equals(TemplateConstants.SEARCH_BY_SUBJECT, col)) {
						query.setParameter("subject", "%" + val + "%");
					}
					if (StringUtils.equals(TemplateConstants.SEARCH_BY_INFORMATION, col)) {
						query.setParameter("note", "%" + val + "%");
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
		sb.append(" from wo_mst_template ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<Template> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<Template> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<Template> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ct.TEMPLATE_ID, ct.TEMPLATE_CATEGORY ");
		sb.append(" from wo_mst_template ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY TEMPLATE_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<Template> vo = new ArrayList<Template>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				Template data = new Template();

				Long faqId = MathUtil.returnIdObjectToLong(obj[0]);
				data = findById(faqId);
			
				vo.add(data);
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
	}

	

}
