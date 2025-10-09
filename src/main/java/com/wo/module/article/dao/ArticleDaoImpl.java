/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.article.dao;

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

import com.wo.module.article.constant.ArticleConstants;
import com.wo.module.article.model.TmpArticle;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;

/**
 *
 * @author hendra
 */
@Repository("articleDao")
public class ArticleDaoImpl extends GenericDAOHibernate<TmpArticle, Long> implements ArticleDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ArticleDaoImpl.class);
	

	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ArticleConstants.SEARCH_BY_KEYWORD, col)) {
						
						sb.append(" and dbms_lob.substr(content_in, 4000, 1 ) LIKE :keyword ");
						
					}
					else if (StringUtils.equals(ArticleConstants.SEARCH_BY_PUBLISH_DATE_FROM, col)) {
							sb.append(" and publish_date >= TO_DATE(:dateFrom,'yyyy-MM-dd') ");
						
					}
					else if (StringUtils.equals(ArticleConstants.SEARCH_BY_PUBLISH_DATE_TO, col)) {
						sb.append(" and publish_date <= TO_DATE(:dateTo,'yyyy-MM-dd') ");
					
					}
					else if (StringUtils.equals(ArticleConstants.SEARCH_BY_DIVISION_NAME, col)) {
						if(val!=null && val.equals("COMPLIANCE")){
							sb.append(" and article_type in ('COMPLIANCE_FLASH','COMPLIANCE_OPINION','GENERAL','TRAINING_MATERIAL','WORKSHOP') ");
						}
						else if(val!=null && val.equals("CORPORATE LEGAL & LITIGATION")){
							sb.append(" and article_type in ('LEGAL_OPINION','LEGAL_REVIEW','GENERAL','TRAINING_MATERIAL','WORKSHOP') ");
						}else{
							sb.append(" and article_type in ('GENERAL','TRAINING_MATERIAL','WORKSHOP') ");
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
					if (StringUtils.equals(ArticleConstants.SEARCH_BY_KEYWORD, col)) {
						query.setParameter("keyword", "%" + val + "%");
					}
					if (StringUtils.equals(ArticleConstants.SEARCH_BY_PUBLISH_DATE_FROM, col)) {
						query.setParameter("dateFrom", val);
					}
					if (StringUtils.equals(ArticleConstants.SEARCH_BY_PUBLISH_DATE_TO, col)) {
						query.setParameter("dateTo", val);
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
		sb.append(" FROM WO_TMP_ARTICLE ct ");
		sb.append("     INNER JOIN WO_MST_PARAMETER_DTL pdArtType ");
		sb.append("         ON pdArtType.PARAMETER_DTL_CODE = ct.ARTICLE_TYPE ");
		sb.append("     INNER JOIN WO_MST_PARAMETER_DTL pdStatus ");
		sb.append("         ON pdStatus.PARAMETER_DTL_CODE = ct.ACTIVE_STATUS ");
		sb.append("     INNER JOIN WO_MST_PARAMETER_DTL pdStatus1 ");
		sb.append("         ON pdStatus1.PARAMETER_DTL_CODE = ct.STATUS ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("		AND ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<TmpArticle> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<TmpArticle> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<TmpArticle> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT ct.ARTICLE_ID ");
		sb.append("       ,ct.ARTICLE_TYPE ");
		sb.append("       ,ct.AUTHOR ");
		sb.append("       ,ct.PUBLISH_DATE ");
		sb.append("       ,ct.ARTICLE_TITLE_EN ");
		sb.append("       ,ct.ARTICLE_TITLE_IN ");
		sb.append("       ,ct.DESCRIPTION_IN ");
		sb.append("       ,ct.DESCRIPTION_EN ");
		sb.append("       ,ct.CONTENT_IN ");
		sb.append("       ,ct.CONTENT_EN ");
		sb.append("       ,pdArtType.NAME_IN ");
		sb.append("       ,pdArtType.NAME_EN ");
		sb.append("       ,ct.ACTIVE_STATUS ACTIVE_STATUS_CODE ");
		sb.append("       ,pdActStatus.NAME_IN ACTIVE_STATUS_IN ");
		sb.append("       ,ct.STATUS STATUS_CODE ");
		sb.append("       ,pdStatus.NAME_IN STATUS_IN ");
		sb.append(" FROM WO_TMP_ARTICLE ct ");
		sb.append("     INNER JOIN WO_MST_PARAMETER_DTL pdArtType ");
		sb.append("         ON pdArtType.PARAMETER_DTL_CODE = ct.ARTICLE_TYPE ");
		sb.append("     INNER JOIN WO_MST_PARAMETER_DTL pdActStatus ");
		sb.append("         ON pdActStatus.PARAMETER_DTL_CODE = ct.ACTIVE_STATUS ");
		sb.append("     INNER JOIN WO_MST_PARAMETER_DTL pdStatus ");
		sb.append("         ON pdStatus.PARAMETER_DTL_CODE = ct.STATUS ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("		AND ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY ARTICLE_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<TmpArticle> vo = new ArrayList<TmpArticle>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				TmpArticle data = new TmpArticle();

				Long rcId = MathUtil.returnIdObjectToLong(obj[0]);
				data.setArticleId(rcId);
				//data.setArticleType(obj[1]!=null?(String) obj[1]:null);
				data.setAuthor(obj[2]!=null?(String) obj[2]:null);
				data.setPublishDate(obj[3]!=null?(Date) obj[3]:null);
				data.setArticleTitleEn(obj[4]!=null?(String) obj[4]:null);
				data.setArticleTitleIn(obj[5]!=null?(String) obj[5]:null);
				data.setDescriptionIn(obj[6]!=null?(String) obj[6]:null);
				data.setDescriptionEn(obj[7]!=null?(String) obj[7]:null);
				data.setContentIn(obj[8]!=null?obj[8].toString():null);
				data.setContentEn(obj[9]!=null?obj[9].toString():null);
				data.setArticleTypeIn(obj[10] != null ? (String) obj[10] : null);
				data.setArticleTypeEn(obj[11] != null ? (String) obj[11] : null);
				data.setActiveStatusCd(obj[12] != null ? (String) obj[12] : null);
				data.setActiveStatusIn(obj[13] != null ? (String) obj[13] : null);
				data.setStatusCode(obj[14] != null ? (String) obj[14] : null);
				data.setStatusIn(obj[15] != null ? (String) obj[15] : null);
				
				vo.add(data);
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
	}

	

}
