package com.wo.module.qaFE.dao;

import java.io.Serializable;
import java.util.ArrayList;
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
import com.wo.module.qaFE.vo.QAFEVo;

@Repository("qaBertanyaFEDao")
public class QABertanyaFEDaoImpl extends GenericDAOHibernate<QA, Long>
	implements QABertanyaFEDao, Serializable{

	private static final long serialVersionUID = -4334108483357532578L;

	@SuppressWarnings("rawtypes")
	private void setQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						//sb.append(" AND q.Q_TITLE LIKE :qTitle ");
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
					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						String qTitle = "%"+val+"%";
						query.setParameter("val", qTitle);
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
		sb.append("       ,q.QUESTION,q.answer,'QA',q.ticket_no ");
		sb.append(" FROM WO_MST_QNA q ");
		sb.append("     LEFT JOIN WO_MST_USER uQ ");
		sb.append("         ON uQ.USER_ID = q.Q_USER_ID ");
		sb.append("     INNER JOIN WO_MST_PARAMETER_DTL pdCat ");
		sb.append("         ON pdCat.PARAMETER_DTL_CODE = q.CATEGORY_TYPE ");
		sb.append("     INNER JOIN WO_MST_PARAMETER_DTL pdStatus ");
		sb.append("         ON pdStatus.PARAMETER_DTL_CODE = q.Q_STATUS ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND q.ENABLED_FLAG = 'Y' and FROM_QNA_ID is null ");
		sb.append("     AND (UPPER(q.answer) like UPPER(:val) OR UPPER(q.QUESTION) like UPPER(:val)) ");
		sb.append(" UNION ALL ");
		sb.append(" select faq_id,pd.name_in, question_in,dbms_lob.substr(answer_in, 4000, 1 ) answer,'FAQ','-'");
		sb.append(" from wo_mst_faq ct inner join WO_MST_PARAMETER_DTL pd on ct.FAQ_CATEGORY = pd.PARAMETER_DTL_CODE ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' and (UPPER(QUESTION_IN) like UPPER(:val) OR UPPER(answer_in) like UPPER(:val)) ");
		
		this.setQueryWhereString(sb, searchCriteria);
		//sb.append(" ORDER BY q.QNA_ID DESC ");
		
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
				data.setQuestion(obj[2] != null ? (String) obj[2] : null);
				data.setAnswer(obj[3] != null ? (String) obj[3] : null);
				data.setDataType(obj[4] != null ? (String) obj[4] : null);
				data.setTicketNo(obj[5] != null ? (String) obj[5] : null);
				
				
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

	

}
