package com.wo.module.articleApproval.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.article.constant.ArticleConstants;
import com.wo.module.article.model.TmpArticle;
import com.wo.module.articleApproval.vo.ArticleApprovalVo;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;

@Repository("articleApprovalDao")
public class ArticleApprovalDaoImpl extends GenericDAOHibernate<TmpArticle, Long>
	implements ArticleApprovalDao, Serializable{

	private static final long serialVersionUID = 2444552716003896055L;

	@SuppressWarnings("rawtypes")
	@Override
	public List<ArticleApprovalVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		List<ArticleApprovalVo> vo = searchDataCriteria(searchCriteria, first, pageSize);
		return vo;
	}
	
	@SuppressWarnings({ "rawtypes" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					
					if (StringUtils.equals(ArticleConstants.SEARCH_BY_DIVISION_NAME, col)) {
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
	
	/*@SuppressWarnings("rawtypes")
	private void getQuerySetValue(Query query, List<? extends SearchObject> searchCriteria) {
		
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();

				
			}
		}
	}*/
	
	@SuppressWarnings("rawtypes")
	private List<ArticleApprovalVo> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize){
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT a.ARTICLE_ID ARTICLE_ID ");
		sb.append("       ,a.ARTICLE_TYPE ARTICLE_TYPE_CODE ");
		sb.append("       ,a.PUBLISH_DATE PUBLISH_DATE ");
		sb.append("       ,TO_CHAR(a.PUBLISH_DATE, 'DD MON YYYY') PUBLISH_DATE_STR ");
		sb.append("       ,a.ARTICLE_TITLE_IN ARTICLE_TITLE_IN ");
		sb.append("       ,a.ARTICLE_TITLE_EN ARTICLE_TITLE_EN ");
		sb.append("       ,pdArtType.NAME_IN ARTICLE_TYPE_IN ");
		sb.append("       ,pdArtType.NAME_EN ARTICLE_TYPE_EN ");
		sb.append(" FROM WO_TMP_ARTICLE a ");
		sb.append("     INNER JOIN WO_MST_PARAMETER_DTL pdArtType ");
		sb.append("         ON pdArtType.PARAMETER_DTL_CODE = a.ARTICLE_TYPE ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND a.ENABLED_FLAG <> 'N' ");
		sb.append("     AND a.STATUS = 'DATA_NEW' ");
		
		this.getQueryWhereString(sb, searchCriteria);
		
		sb.append(" ORDER BY a.ARTICLE_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		//this.getQuerySetValue(query, searchCriteria);
		
		List result = query.getResultList();
		List<ArticleApprovalVo> vo = new ArrayList<ArticleApprovalVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				ArticleApprovalVo data = new ArticleApprovalVo();
				
				data.setArticleId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setArticleTypeCode(obj[1] != null ? (String) obj[1] : null);
				data.setPublishDate(obj[2] != null ? (Date) obj[2] : null);
				data.setPublishDateStr(obj[3] != null ? (String) obj[3] : null);
				data.setArticleIn(obj[4] != null ? (String) obj[4] : null);
				data.setArticleEn(obj[5] != null ? (String) obj[5] : null);
				data.setArticleTypeIn(obj[6] != null ? (String) obj[6] : null);
				data.setArticleTypeEn(obj[7] != null ? (String) obj[7] : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		
		Number result = searchCountDataCriteria(searchCriteria);
		
		if (result == null) {
			result = 0;
		}
		
		return result.longValue();
	}

	@SuppressWarnings("rawtypes")
	private Number searchCountDataCriteria(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT COUNT(1) ");
		sb.append(" FROM WO_TMP_ARTICLE a ");
		sb.append("     INNER JOIN WO_MST_PARAMETER_DTL pdArtType ");
		sb.append("         ON pdArtType.PARAMETER_DTL_CODE = a.ARTICLE_TYPE ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND a.STATUS = 'DATA_NEW' ");
		sb.append("     AND a.ENABLED_FLAG <> 'N' ");
		
		this.getQueryWhereString(sb, searchCriteria);
		
		Query query = getSession().createSQLQuery(sb.toString());
		//this.getQuerySetValue(query, searchCriteria);
		
		return (Number) query.getSingleResult();
	}
	
}
