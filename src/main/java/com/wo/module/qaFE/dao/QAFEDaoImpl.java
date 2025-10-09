package com.wo.module.qaFE.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.qa.model.QA;
import com.wo.module.qaFE.constant.QAFEConstant;
import com.wo.module.qaFE.vo.QAFEVo;

@Repository("qaFEDao")
public class QAFEDaoImpl extends GenericDAOHibernate<QA, Long>
	implements QAFEDao, Serializable{

	private static final long serialVersionUID = -4334108483357532578L;

	@SuppressWarnings("rawtypes")
	private void setQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(QAFEConstant.SEARCH_BY_CATEGORY, col)) {
						sb.append(" AND q.CATEGORY_TYPE = :categoryType ");
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
						sb.append(" AND q.Q_STATUS = :status ");
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						sb.append(" AND (UPPER(q.Q_TITLE) LIKE UPPER(:qTitle) OR UPPER(q.QUESTION) LIKE UPPER(:qTitle) ");
						sb.append("       OR UPPER(q.TICKET_NO) LIKE UPPER(:qTitle)) ");
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						if(getIsAdmin(new Long(val)).intValue() > 0){
//							sb.append(" AND q.CATEGORY_TYPE IN (select qna_category_code from WO_MST_QNA_CATEGORY_MAP cm where user_id = :userId) ");
						}  else {
							sb.append("     AND ( ");
							sb.append("     	CASE WHEN q.Q_USER_ID = :userId THEN 'Y' ");
							sb.append("       	WHEN q.Q_USER_ID != :userId AND ( ");
							sb.append("       		SELECT 1 FROM WO_MST_QNA q2 WHERE q2.QNA_ID = q.QNA_ID AND q2.PUBLISH_QNA = 'Y' ");
							sb.append("       		) = 1 THEN 'Y' ");
							sb.append("       	ELSE 'N' ");
							sb.append("        END ");
							sb.append("     ) = 'Y' ");
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void setQuerySetString(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(QAFEConstant.SEARCH_BY_CATEGORY, col)) {
						query.setParameter("categoryType", val);
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
						query.setParameter("status", val);
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						String qTitle = "%"+val+"%";
						query.setParameter("qTitle", qTitle);
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						if(getIsAdmin(new Long(val)).intValue() > 0){
							
						}  else {
							query.setParameter("userId", val);
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<QAFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		List<QAFEVo> vo = searchDataCriteria(searchCriteria, first, pageSize);
		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<QAFEVo> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT q.QNA_ID ");
		sb.append("       ,q.Q_TITLE ");
		sb.append("       ,uQ.NAME SENDER ");
		sb.append("       ,q.Q_DATE ");
		sb.append("       ,TO_CHAR(Q_DATE, 'DD-MON-YYYY HH24:MI') Q_DATE_STR ");
		sb.append("       ,q.QUESTION ");
		sb.append("       ,q.CATEGORY_TYPE CAT_TYPE_CODE ");
		sb.append("       ,pdCat.NAME_IN CAT_TYPE_IN ");
		sb.append("       ,pdCat.NAME_EN CAT_TYPE_EN ");
		sb.append("       ,q.Q_STATUS STATUS_CODE ");
		sb.append("       ,pdStatus.NAME_IN STATUS_IN ");
		sb.append("       ,pdStatus.NAME_EN STATUS_EN ");
		sb.append("       ,null is_admin ");
		sb.append("       ,q.ticket_no, pdLastUpdate.NAME LAST_UPDATE_BY,q.LAST_UPDATE_DATE ");
		sb.append("       ,uQ.division_name ");
		sb.append(" FROM WO_MST_QNA q ");
		sb.append("     LEFT JOIN WO_MST_USER uQ ");
		sb.append("         ON uQ.USER_ID = q.Q_USER_ID ");
		sb.append("     INNER JOIN WO_MST_PARAMETER_DTL pdCat ");
		sb.append("         ON pdCat.PARAMETER_DTL_CODE = q.CATEGORY_TYPE ");
		sb.append("     INNER JOIN WO_MST_PARAMETER_DTL pdStatus ");
		sb.append("         ON pdStatus.PARAMETER_DTL_CODE = q.Q_STATUS ");
		sb.append("     LEFT JOIN WO_MST_USER pdLastUpdate ");
		sb.append("         ON pdLastUpdate.NIK = q.LAST_UPDATE_BY ");
		//sb.append("     LEFT JOIN WO_MST_QNA_CATEGORY_MAP cm on cm.user_id = :userId");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND q.ENABLED_FLAG = 'Y' and FROM_QNA_ID is null ");
		
		this.setQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY q.QNA_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQuerySetString(query, searchCriteria);
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		List result = query.getResultList();
		List<QAFEVo> vo = new ArrayList<QAFEVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				QAFEVo data = new QAFEVo();
				
				data.setQaId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setqTitle(obj[1] != null ? (String) obj[1] : null);
				data.setSender(obj[2] != null ? (String) obj[2] : null);
				data.setqDate(obj[3] != null ? (Date) obj[3] : null);
				data.setqDateStr(obj[4] != null ? (String) obj[4] : null);
				data.setQuestion(obj[5] != null ? extractedText((String) obj[5]) : null);
				
				data.setCategoryTypeCode(obj[6] != null ? (String) obj[6] : null);
				data.setCategoryTypeIn(obj[7] != null ? (String) obj[7] : null);
				data.setCategoryTypeEn(obj[8] != null ? (String) obj[8] : null);
				data.setStatusCode(obj[9] != null ? (String) obj[9] : null);
				data.setStatusIn(obj[10] != null ? (String) obj[10] : null);
				data.setStatusEn(obj[11] != null ? (String) obj[11] : null);
				data.setIsAdmin(obj[12] != null ? ((BigDecimal) obj[12]).intValue() : null);
				data.setTicketNo(obj[13] != null ? (String) obj[13] : null);
				data.setLastUpdateBy(obj[14] != null ? (String) obj[14] : null);
				data.setLastUpdateDate(obj[15] != null ? (Date) obj[15] : null);
				data.setDivisionName(obj[16] != null ? (String) obj[16] : null);
				
				if (searchCriteria != null) {
					for (SearchObject searchVal : searchCriteria) {
						String col = searchVal.getSearchColumn();
						String val = searchVal.getSearchValueAsString();
						
						if (!StringUtils.isBlank(val)) {
							if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
								data.setIsAdmin(getIsAdmin(Long.parseLong(val)).intValue());
							}
						}
					}
				}
				
				vo.add(data);
			}
		}
		
		return vo;
	}
	
	public String extractedText(String text){
		text=text.replaceAll("\\<.*?\\>", " ");
		if(text.trim().length()>10){
			text.trim().substring(0,10);
		}
	    return text.trim();
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
		sb.append(" FROM WO_MST_QNA q ");
		sb.append("     LEFT JOIN WO_MST_USER uQ ");
		sb.append("         ON uQ.USER_ID = q.Q_USER_ID ");
		sb.append("     INNER JOIN WO_MST_PARAMETER_DTL pdCat ");
		sb.append("         ON pdCat.PARAMETER_DTL_CODE = q.CATEGORY_TYPE ");
		sb.append("     INNER JOIN WO_MST_PARAMETER_DTL pdStatus ");
		sb.append("         ON pdStatus.PARAMETER_DTL_CODE = q.Q_STATUS ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND q.ENABLED_FLAG = 'Y' and FROM_QNA_ID is null ");
		
		this.setQueryWhereString(sb, searchCriteria);
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQuerySetString(query, searchCriteria);
		
		Number result = (Number) query.getSingleResult();
		
		return result;
	}

	@Override
	public Number getTicketNo() {
		StringBuilder sb = new StringBuilder();
		sb.append(" select WO_MST_QNA_TICKET_NO_SEQ.nextval from dual ");
		
        Query result = getSession().createSQLQuery(sb.toString());
       
        
        return (Number) result.getSingleResult();
	}
	
	public String getLastTicketNo() {
		StringBuilder sb = new StringBuilder();
		sb.append(" select MAX(TICKET_NO) from wo_mst_qna ");
		
        Query result = getSession().createSQLQuery(sb.toString());
       
        
        return (String) result.getSingleResult();
	}
	
	@Override
	public Number getIsAdmin(Long userId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select COUNT(1) from WO_MST_QNA_CATEGORY_MAP where USER_ID = :userId ");
		
        Query result = getSession().createSQLQuery(sb.toString());
        result.setParameter("userId", userId);
       
        
        return (Number) result.getSingleResult();
	}
	
	public String getCategoryIsAdmin(Long userId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select QNA_CATEGORY_CODE,USER_ID from WO_MST_QNA_CATEGORY_MAP where USER_ID = :userId and rownum = 1 ");
		
        Query result = getSession().createSQLQuery(sb.toString());
        result.setParameter("userId", userId);
       
        List list = result.getResultList();
        
        if(list!=null && list.size()>0){
        	
				Object[] obj = (Object[]) list.get(0);
				return (String)obj[0];
        	
        }else{
        	return null;
        }
        
        /*if(result.getSingleResult()!=null){
        	return (String) result.getSingleResult();
        }else{
        	return null;
        }*/
	}
	
	public List<QA> getQAByParentId(Long fromQnaId)  {
		
		String hql = "FROM QA where fromQnaId.qnaId = :fromQnaId and enabledFlag = 'Y' order by qnaId asc, qDate asc, aDate asc ";
		
		Query result = getSession().createQuery(hql);
		result.setParameter("fromQnaId", fromQnaId);
		List list = result.getResultList();
		
		return list;
	}
	
	public List<QA> getQANotAnswerByParentId(Long fromQnaId)  {
		
		String hql = "FROM QA where fromQnaId.qnaId = :fromQnaId and answer is null and enabledFlag = 'Y' ";
		
		Query result = getSession().createQuery(hql);
		result.setParameter("fromQnaId", fromQnaId);
		List list = result.getResultList();
		
		return list;
	}
	
	@Override
	public Number getIsAdminByCategory(Long userId, String category) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select COUNT(1) from WO_MST_QNA_CATEGORY_MAP where USER_ID = :userId and QNA_CATEGORY_CODE = :category ");
		
        Query result = getSession().createSQLQuery(sb.toString());
        result.setParameter("userId", userId);
        result.setParameter("category", category);
        
        return (Number) result.getSingleResult();
	}
	
	public Number getCheckTiket(String ticketNo) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) from wo_mst_qna where TICKET_NO = '"+ticketNo+"' ");		
        Query result = getSession().createSQLQuery(sb.toString());      
                        
        return (Number) (result.getSingleResult() !=null?result.getSingleResult():0);
	}

}
