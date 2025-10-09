/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.opinionFE.dao;

import java.math.BigDecimal;
//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.article.constant.ArticleConstants;
import com.wo.module.article.model.TmpArticle;
import com.wo.module.opinionFE.constant.OpinionFEConstants;
import com.wo.module.opinionFE.vo.OpinionFEVO;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;

/**
 *
 * @author hendra
 */
@Repository("opinionFEDao")
public class OpinionFEDaoImpl extends GenericDAOHibernate<TmpArticle, Long> implements OpinionFEDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(OpinionFEDaoImpl.class);
	

	@SuppressWarnings("rawtypes")
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						sb.append(" and (UPPER(content_in) LIKE UPPER(:keyword) or UPPER(ARTICLE_TITLE_IN) LIKE UPPER(:keyword))  ");
					}
					else if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
							
					}
					else if (StringUtils.equals(OpinionFEConstants.SEARCH_BY_ARTICLE_TYPE, col)) {
						sb.append(" and article_type = :articleType ");
					}
					
					/*else if (StringUtils.equals(ArticleConstants.SEARCH_BY_DIVISION_NAME, col)) {
						if(val!=null && val.equals("COMPLIANCE REGULATORY AFFAIRS")){
							sb.append(" and article_type in ('COMPLIANCE_OPINION') ");
						}
						else if(val!=null && val.equals("CORPORATE LEGAL & LITIGATION")){
							sb.append(" and article_type in ('LEGAL_OPINION') ");
						}else{
							sb.append(" and article_type not in ('COMPLIANCE_OPINION','LEGAL_OPINION') ");
						}
						
					
					}*/
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
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						query.setParameter("keyword", "%" + val + "%");
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						query.setParameter("userId", val );	
					}
					if (StringUtils.equals(OpinionFEConstants.SEARCH_BY_ARTICLE_TYPE, col)) {
						query.setParameter("articleType",  val );
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
		sb.append(" FROM WO_MST_ARTICLE ct ");
		sb.append("     INNER JOIN WO_MST_PARAMETER_DTL pdArtType ");
		sb.append("         ON pdArtType.PARAMETER_DTL_CODE = ct.ARTICLE_TYPE ");
		sb.append("     INNER JOIN WO_MST_PARAMETER_DTL pdStatus ");
		sb.append("         ON pdStatus.PARAMETER_DTL_CODE = ct.STATUS ");
		//sb.append("		INNER JOIN WO_TMP_ARTICLE_APPROVAL appr ");
		//sb.append(" 		ON ct.ARTICLE_ID = appr.ARTICLE_ID ");
		//sb.append(" 			AND appr.APPROVAL_STATUS = 'STATUS_APPROVED' ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("		AND ct.enabled_flag = 'Y' ");
		sb.append(" 	AND ct.STATUS = 'DATA_ACTIVE' ");
		sb.append(" 	AND (ct.ARTICLE_TYPE = 'LEGAL_OPINION' OR ct.ARTICLE_TYPE = 'COMPLIANCE_OPINION') and :userId = :userId ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<OpinionFEVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<OpinionFEVO> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<OpinionFEVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT ct.ARTICLE_ID ");
		sb.append("       ,ct.ARTICLE_TYPE ");
		sb.append("       ,ct.AUTHOR ");
		sb.append("       ,ct.PUBLISH_DATE ");
		sb.append("       ,TO_CHAR(ct.PUBLISH_DATE, 'DD FMMonth YYYY', 'nls_date_language=indonesian') ");
		sb.append("       ,ct.ARTICLE_TITLE_EN ");
		sb.append("       ,ct.ARTICLE_TITLE_IN ");
		sb.append("       ,ct.DESCRIPTION_IN ");
		sb.append("       ,ct.DESCRIPTION_EN ");
		sb.append("       ,ct.CONTENT_IN ");
		sb.append("       ,ct.CONTENT_EN ");
		sb.append("       ,pdArtType.NAME_IN ");
		sb.append("       ,pdArtType.NAME_EN ");
		sb.append("       ,ct.STATUS STATUS_CODE ");
		sb.append("       ,pdStatus.NAME_IN STATUS_IN ");
		sb.append("       ,pdStatus.NAME_EN STATUS_EN ");
		sb.append("       ,(select count(1) from wo_log_access a where a.user_id = :userId  and a.access_id = ct.article_id   and ((a.access_action = '/compliance/pages/opinionFE/opinionFE.faces') OR (a.access_action = '/compliance/pages/opinionFE/opinionFEView.faces')) ) flagIsAccess ");
		sb.append(" FROM WO_MST_ARTICLE ct ");
		sb.append("     INNER JOIN WO_MST_PARAMETER_DTL pdArtType ");
		sb.append("         ON pdArtType.PARAMETER_DTL_CODE = ct.ARTICLE_TYPE ");
		sb.append("     INNER JOIN WO_MST_PARAMETER_DTL pdStatus ");
		sb.append("         ON pdStatus.PARAMETER_DTL_CODE = ct.STATUS ");
		//sb.append("		INNER JOIN WO_TMP_ARTICLE_APPROVAL appr ");
		//sb.append(" 		ON ct.ARTICLE_ID = appr.ARTICLE_ID ");
		//sb.append(" 			AND appr.APPROVAL_STATUS = 'STATUS_APPROVED' ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("		AND ct.enabled_flag = 'Y' ");
		sb.append(" 	AND ct.STATUS = 'DATA_ACTIVE' ");
		sb.append(" 	AND (ct.ARTICLE_TYPE = 'LEGAL_OPINION' OR ct.ARTICLE_TYPE = 'COMPLIANCE_OPINION') ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY ct.PUBLISH_DATE DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<OpinionFEVO> vo = new ArrayList<OpinionFEVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				OpinionFEVO data = new OpinionFEVO();

				Long rcId = MathUtil.returnIdObjectToLong(obj[0]);
				data.setArticleId(rcId);
				//data.setArticleType(obj[1]!=null?(String) obj[1]:null);
				data.setAuthor(obj[2]!=null?(String) obj[2]:null);
				data.setPublishDate(obj[3]!=null?(Date) obj[3]:null);
				data.setPublishDateStr(obj[4]!=null?(String) obj[4]:null);
				data.setArticleTitleEn(obj[5]!=null?(String) obj[5]:null);
				data.setArticleTitleIn(obj[6]!=null?(String) obj[6]:null);
				data.setDescriptionIn(obj[7]!=null?(String) obj[7]:null);
				data.setDescriptionEn(obj[8]!=null?(String) obj[8]:null);
				data.setContentIn(obj[9]!=null?obj[9].toString():null);
				data.setContentEn(obj[10]!=null?obj[10].toString():null);
				data.setArticleTypeIn(obj[11] != null ? (String) obj[11] : null);
				data.setArticleTypeEn(obj[12] != null ? (String) obj[12] : null);
				data.setStatusCode(obj[13] != null ? (String) obj[13] : null);
				data.setStatusIn(obj[14] != null ? (String) obj[14] : null);
				data.setStatusEn(obj[15] != null ? (String) obj[15] : null);
				data.setIsAccess(obj[16]!=null?((BigDecimal)obj[16]).intValue():null);
				
				vo.add(data);
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
	}

	

}
