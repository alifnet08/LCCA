/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.emailTemplate.dao;

//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.emailTemplate.constant.EmailTemplateConstants;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.menu.dao.MenuDao;

/**
 *
 * @author hendra
 */
@Repository("emailTemplateDao")
public class EmailTemplateDaoImpl extends GenericDAOHibernate<EmailTemplate, Long> implements EmailTemplateDao {
	//private static Logger logger = Logger.getLogger(EmailTemplateDaoImpl.class);
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
					if (StringUtils.equals(EmailTemplateConstants.SEARCH_BY_NAME, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and  UPPER(pd.name_en) LIKE  UPPER(:name) ");
						} else {
							sb.append(" and  UPPER(pd.name_in) LIKE  UPPER(:name) ");
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
					if (StringUtils.equals(EmailTemplateConstants.SEARCH_BY_NAME, col)) {
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
		sb.append(" from wo_mst_email_template et ");
		sb.append(" inner join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = et.email_template_code ");
		sb.append(" where 1=1 ");
		sb.append(" and et.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<EmailTemplate> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<EmailTemplate> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<EmailTemplate> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first,
			int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select et.email_template_id, pd.name_in, pd.name_en ");
		sb.append(" from wo_mst_email_template et ");
		sb.append(" inner join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = et.email_template_code ");
		sb.append(" where 1=1 ");
		sb.append(" and et.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY email_template_id DESC ");
		

		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<EmailTemplate> vo = new ArrayList<EmailTemplate>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				EmailTemplate data = new EmailTemplate();
				//data.setEmailTemplateId(((BigInteger) obj[0]).longValue());
				data.setEmailTemplateId(MathUtil.returnIdObjectToLong(obj[0]));
				data.setEmailTemplateNameIn((String) obj[1]);
				data.setEmailTemplateNameEn((String) obj[2]);

				vo.add(data);
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	public Boolean isEmailTemplateDuplicate(EmailTemplate emailTemplate) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select et.email_template_id, pd.name_in, pd.name_en ");
		sb.append(" from wo_mst_email_template et ");
		sb.append(" inner join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = et.email_template_code ");
		sb.append(" where 1=1 ");
		sb.append(" and et.enabled_flag = 'Y' ");
		sb.append(" and et.email_template_code = :emailTemplateCode ");

		if(emailTemplate.getEmailTemplateId() != null ) {
			sb.append(" and et.email_template_id  <> :id ");
		}	

		Query query = getSession().createSQLQuery(sb.toString());

		query.setParameter("emailTemplateCode", emailTemplate.getEmailTemplate().getParameterDtlCode());
		
		if(emailTemplate.getEmailTemplateId() != null ) {
			query.setParameter("id", emailTemplate.getEmailTemplateId());
		}

		List resultList = query.getResultList();

		if (resultList != null &&  resultList.size() > 0) {
			return true;
		}

		return false;
	}

	@Override
	public Integer getEditEmailTemplateByName(Long id, String emailName) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT COUNT(1) "
				+ "		FROM wo_mst_email_template ");
		sb.append("		WHERE email_template_id != '"+id+"' ");
		sb.append("			AND email_template_code = '"+emailName+"' ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		
		Number count = (Number) result.getSingleResult();
		if(count == null) {
			count = 0;
		}
		
		return (Integer) count.intValue();
	}
	
	@SuppressWarnings("rawtypes")
	public EmailTemplate getEmailTemplateByEmailTemplateCode(String code)  {
		 if(code != null) {
			String hql = "FROM EmailTemplate where emailTemplate.parameterDtlCode = :code and enabledFlag = 'Y' ";
			Query result = getSession().createQuery(hql);
			result.setParameter("code", code);
			
			List list = result.getResultList();
			
			if(list.size() > 0) {
				return (EmailTemplate) list.get(0);
			}else {
				return null;
			}
			
		 } else {
			 return null;
		 }
	}

}
