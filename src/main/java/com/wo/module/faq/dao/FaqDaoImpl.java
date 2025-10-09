/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.faq.dao;

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

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.faq.constant.FaqConstants;
import com.wo.module.faq.model.Faq;
import com.wo.module.faq.model.TmpFaq;

/**
 *
 * @author hendra
 */
@Repository("faqDao")
public class FaqDaoImpl extends GenericDAOHibernate<TmpFaq, Long> implements FaqDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(FaqDaoImpl.class);
	

	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(FaqConstants.SEARCH_BY_KEYWORD, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and content_in LIKE :keyword ");
						} else {
							sb.append(" and content_en LIKE :keyword ");
						}
					}
					if (StringUtils.equals(FaqConstants.SEARCH_BY_CATEGORY, col)) {
						sb.append(" and faq_category = :category ");
					
					}
					if (StringUtils.equals(FaqConstants.SEARCH_BY_START_DATE, col)) {
							sb.append(" and TRUNC(creation_date) >= TO_DATE(:dateFrom,'yyyy-MM-dd') ");
						
					}
					if (StringUtils.equals(FaqConstants.SEARCH_BY_END_DATE, col)) {
						sb.append(" and TRUNC(creation_date) <= TO_DATE(:dateTo,'yyyy-MM-dd') ");
					
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						sb.append(" and (u.DIVISION_NAME = (select DIVISION_NAME from wo_mst_user where user_id = :userId)) ");
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
					if (StringUtils.equals(FaqConstants.SEARCH_BY_KEYWORD, col)) {
						query.setParameter("keyword", "%" + val + "%");
					}
					if (StringUtils.equals(FaqConstants.SEARCH_BY_CATEGORY, col)) {
						query.setParameter("category", val);
					}
					if (StringUtils.equals(FaqConstants.SEARCH_BY_START_DATE, col)) {
						query.setParameter("dateFrom", val);
					}
					if (StringUtils.equals(FaqConstants.SEARCH_BY_END_DATE, col)) {
						query.setParameter("dateTo", val);
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						query.setParameter("userId", val);
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
//		sb.append(" select count(1) ");
//		sb.append(" from wo_mst_faq ct ");
//		sb.append("     INNER JOIN WO_MST_USER u ");
//		sb.append("         ON u.nik = ct.CREATED_BY ");
//		sb.append(" where 1=1 ");
//		sb.append(" and ct.enabled_flag = 'Y' ");
		sb.append(" select count(1) ");
		sb.append(" from wo_tmp_faq ct ");
		sb.append("     INNER JOIN WO_MST_USER u ");
		sb.append("         ON u.nik = ct.CREATED_BY ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");
		

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<TmpFaq> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<TmpFaq> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<TmpFaq> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
//		sb.append(" select ct.FAQ_ID, ct.INSTITUTION, ct.QUESTION_IN, ct.QUESTION_EN,FAQ_CATEGORY,ANSWER_IN,ANSWER_EN ");
//		sb.append(" from wo_mst_faq ct ");
//		sb.append("     INNER JOIN WO_MST_USER u ");
//		sb.append("         ON u.nik = ct.CREATED_BY ");
//		sb.append(" where 1=1 ");
//		sb.append(" and ct.enabled_flag = 'Y' ");
		sb.append(" select ct.FAQ_ID, ct.INSTITUTION, ct.QUESTION_IN, ct.QUESTION_EN,FAQ_CATEGORY,ANSWER_IN,ANSWER_EN ");
		sb.append(" from wo_tmp_faq ct ");
		sb.append("     INNER JOIN WO_MST_USER u ");
		sb.append("         ON u.nik = ct.CREATED_BY ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY FAQ_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<TmpFaq> vo = new ArrayList<TmpFaq>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				TmpFaq data = new TmpFaq();

				Long faqId = MathUtil.returnIdObjectToLong(obj[0]);
				data = findById(faqId);
				/*data.setFaqId(rcId);
				data.setInstitution(obj[1]!=null?(String) obj[1]:null);
				data.setQuestionIn(obj[2]!=null?(String) obj[2]:null);
				data.setQuestionEn(obj[3]!=null?(String) obj[3]:null);
				data.setCategory(obj[4]!=null?(String) obj[4]:null);
				data.setAnswerIn(obj[5]!=null?(String) obj[5]:null);
				data.setAnswerEn(obj[6]!=null?(String) obj[6]:null);*/
				
				
				
				vo.add(data);
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
	}
	@SuppressWarnings("rawtypes")
	public List<String[]> getInstitution(String searchVal,Long userId) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct institution, name_in ");
		sb.append(" from wo_mst_faq ct inner join wo_mst_parameter_dtl d on d.parameter_dtl_code = ct.institution ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("searchVal", "%"+(searchVal == null ? "" :searchVal)+"%");
		
		List resultList = query.getResultList();

		if(resultList.size()>0){
			List<String[]> vo = new ArrayList<String[]>();
	
			if (resultList != null) {
				for (int i = 0; i < resultList.size(); i++) {
					Object[] obj = (Object[]) resultList.get(i);
					String[] str = new String[2];
					str[0] = obj[0]!=null?(String) obj[0]:null;
					str[1] = obj[1]!=null?(String) obj[1]:null;
					vo.add(str);
				}
			}
			
	
			return vo;
		}else{
			return getInstitution(userId);
		}
	}
	
	public List<String[]> getInstitution(Long userId) {
			
			StringBuilder sb = new StringBuilder();
			sb.append(" select distinct institution, name_in ");
			sb.append(" from wo_mst_faq ct inner join wo_mst_parameter_dtl d on d.parameter_dtl_code = ct.institution ");
			sb.append("     INNER JOIN WO_MST_USER u ");
			sb.append("         ON u.nik = ct.CREATED_BY ");
			sb.append(" where 1=1 ");
			sb.append(" and ct.enabled_flag = 'Y' ");
			//sb.append(" and (u.DIVISION_NAME = (select DIVISION_NAME from wo_mst_user where user_id = :userId)) ");
	
			Query query = getSession().createSQLQuery(sb.toString());
			//query.setParameter("userId", userId);
			
			List resultList = query.getResultList();
	
			List<String[]> vo = new ArrayList<String[]>();
	
			if (resultList != null) {
				for (int i = 0; i < resultList.size(); i++) {
					Object[] obj = (Object[]) resultList.get(i);
					String[] str = new String[2];
					str[0] = obj[0]!=null?(String) obj[0]:null;
					str[1] = obj[1]!=null?(String) obj[1]:null;
					vo.add(str);
				}
			}
	
			return vo;
		}
	
	@SuppressWarnings("rawtypes")
	public List<String[]> getCategoryFAQByInstitution(String institution,String searchVal,String kategori) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct faq_category, name_in ");
		sb.append(" from wo_mst_faq ct inner join wo_mst_parameter_dtl d on d.parameter_dtl_code = ct.faq_category ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' and institution = :institution and (:searchVal is null OR UPPER(faq_category) like UPPER(:searchVal)) and (:kategori is null or faq_category =:kategori) ");

		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("institution", institution);
		query.setParameter("kategori", kategori);
		query.setParameter("searchVal", "%"+(searchVal == null ? "" :searchVal)+"%");
		
		List resultList = query.getResultList();

		if(resultList.size()>0){
			List<String[]> vo = new ArrayList<String[]>();
	
			if (resultList != null) {
				for (int i = 0; i < resultList.size(); i++) {
					Object[] obj = (Object[]) resultList.get(i);
					String[] str = new String[2];
					str[0] = obj[0]!=null?(String) obj[0]:null;
					str[1] = obj[1]!=null?(String) obj[1]:null;
					vo.add(str);
				}
			}
	
			return vo;
		}else{
			return getCategoryFAQByInstitution(institution,kategori);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List<String[]> getCategoryFAQByInstitution(String institution,String kategori) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct faq_category, name_in ");
		sb.append(" from wo_mst_faq ct inner join wo_mst_parameter_dtl d on d.parameter_dtl_code = ct.faq_category ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' and institution = :institution and (:kategori is null or faq_category =:kategori) ");

		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("institution", institution);
		query.setParameter("kategori", kategori);
		
		List resultList = query.getResultList();

		List<String[]> vo = new ArrayList<String[]>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				String[] str = new String[2];
				str[0] = obj[0]!=null?(String) obj[0]:null;
				str[1] = obj[1]!=null?(String) obj[1]:null;
				vo.add(str);
			}
		}

		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	public List<Faq> getQuestionAndAnswerByCategory(String category,String searchVal, Long faqId,String institution) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct faq_id,question_in,dbms_lob.substr(answer_in, 4000, 1 ) answer_in ");
		sb.append(" from wo_mst_faq ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' and faq_category = :category and institution = :institution and (UPPER(ANSWER_IN) like UPPER(:searchVal) OR UPPER(QUESTION_IN) like UPPER(:searchVal))");

		if(faqId!=null){
			sb.append(" and faq_id = "+faqId);
		}
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("category", category);
		query.setParameter("institution", institution);
		query.setParameter("searchVal", "%"+(searchVal == null ? "" :searchVal)+"%");
		
		List resultList = query.getResultList();

		List<Faq> vo = new ArrayList<Faq>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				Faq data = new Faq();
				Long id = MathUtil.returnIdObjectToLong(obj[0]);
				data.setFaqId(id);
				data.setQuestionIn(obj[1]!=null?(String) obj[1]:null);
				data.setAnswerIn(obj[2]!=null?(String) obj[2]:null);
				vo.add(data);
			}
		}

		return vo;
	}

	

}
