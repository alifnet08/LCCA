/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.qa.dao;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.qa.constant.QAConstants;
import com.wo.module.qa.model.QA;
import com.wo.module.user.model.User;

/**
 *
 * @author hendra
 */

@Repository("qaDao")
public class QADaoImpl extends GenericDAOHibernate<QA, Long> 
    implements QADao {
	
	@SuppressWarnings({ "rawtypes"})
    private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue()!=null? searchVal.getSearchValueAsString(): "";
				if (!StringUtils.isBlank(val)) {
					if(StringUtils.equals(QAConstants.WHERE_QUESTION, col)) { 
						sb.append(" and (UPPER(question) like UPPER('%"+val+"%') or UPPER(TICKET_NO) like UPPER('%"+val+"%') or or UPPER(Q_TITLE) like UPPER('%"+val+"%') ) ");
					}
				}
			}
		}
		
		return sb;
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
		sb.append(" FROM wo_mst_qna  ");
		sb.append(" WHERE 1=1 and enabled_flag = 'Y' ");
       
        sb = getQueryWhereString(sb, searchCriteria);
        
        Query result = getSession().createSQLQuery(sb.toString());
       
        
        return (Number) result.getSingleResult();
    }
    
    @SuppressWarnings("rawtypes")
    public List<QA> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        
        List<QA> voList = searchDataCriteria(searchCriteria, first, pageSize);

        return voList;
    }
    
    @SuppressWarnings("rawtypes")
    private List<QA> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        StringBuilder sb = new StringBuilder();
		sb.append(" select qna_id, q_user_id, qUser.name qUserName, q_date, question, admin_user_id, adminUser.name adminUserName, a_user_id, aUser.name aUserName,a_date,answer,q_status,sla_type,category_type,thread_close_status ");
		sb.append(" FROM wo_mst_qna qa inner join wo_mst_user qUser on qa.q_user_id = qUser.user_id ");
		sb.append(" left join wo_mst_user adminUser on qa.admin_user_id = adminUser.user_id ");
		sb.append(" left join wo_mst_user aUser on qa.a_user_id = aUser.user_id ");
		sb.append(" WHERE 1=1 and qa.enabled_flag = 'Y' ");
        
        sb = getQueryWhereString(sb, searchCriteria);
        sb.append(" ORDER BY qna_id DESC ");
        
        
        Query result = getSession().createSQLQuery(sb.toString());
        result.setFirstResult(first);result.setMaxResults(pageSize);
        List resultList = result.getResultList();
        
        List<QA> vo = new ArrayList<QA>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                QA data = new QA();
 
                data.setQnaId(MathUtil.returnIdObjectToLong(obj[0]));
                data.setqUserId(MathUtil.returnIdObjectToLong(obj[1]));
                data.setqUserName(obj[2]!=null?(String)obj[2]:null);
                data.setqDate(obj[3]!=null?(Date)obj[3]:null);
                if(data.getqDate()!=null) {
                	data.setqDateStr(sdf.format(data.getqDate()));
                }
                data.setQuestion((String)obj[4]);
                data.setAdminUserName(obj[6]!=null?(String)obj[6]:null);
                data.setaUserName(obj[8]!=null?(String)obj[8]:null);
                data.setaDate(obj[9]!=null?(Timestamp)obj[9]:null);
                if(data.getaDate()!=null) {
                	data.setaDateStr(sdf.format(data.getaDate()));
                }
                data.setAnswer(obj[10]!=null?(String)obj[10]:null);
                if(data.getAnswer() == null) {
                	data.setaUserName(null);
                }
                data.setqStatus(obj[11]!=null?(String)obj[11]:null);
                data.setSlaType(obj[12]!=null?(String)obj[12]:null);
                data.setCategoryType(obj[13]!=null?(String)obj[13]:null);
                data.setThreadStatus(obj[14]!=null?(String)obj[14]:null);
                
                vo.add(data);
            }
        }

        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        return vo;
    }
    
    public Number getTicketNo() {
    	
    	StringBuilder sb = new StringBuilder();
		sb.append(" select WO_MST_QNA_TICKET_NO_SEQ.nextval from dual ");
		
        Query result = getSession().createSQLQuery(sb.toString());
       
        
        return (Number) result.getSingleResult();
    }
    
    public Number getCountQuestionNotAnswered() {
    	
    	StringBuilder sb = new StringBuilder();
		sb.append(" select Count(1) from WO_MST_QNA where enabled_flag = 'Y' and A_DATE is null ");
		
        Query result = getSession().createSQLQuery(sb.toString());
       
        
        return (Number) result.getSingleResult();
    }
    
    public QA getQAQuestionNotAnswered() {
    	
    	StringBuilder sb = new StringBuilder();
		sb.append(" select qna_id,ticket_No from WO_MST_QNA where enabled_flag = 'Y' and A_DATE is null and rownum=1 order by qna_id desc ");
		
        Query result = getSession().createSQLQuery(sb.toString());
       
        QA data = null;
        
        List resultList = result.getResultList();
        if(resultList!=null && resultList.size()>0) {
                Object[] obj = (Object[]) resultList.get(0);
                data = findById(MathUtil.returnIdObjectToLong(obj[0]));
        }
        
        return data;
        
    }
    
    
    
}
