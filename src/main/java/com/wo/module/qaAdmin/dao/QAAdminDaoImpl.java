/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.qaAdmin.dao;


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
import com.wo.module.qaAdmin.constant.QAAdminConstants;
import com.wo.module.qaAdmin.vo.QAAdminVo;

/**
 *
 * @author hendra
 */

@Repository("qaAdminDao")
public class QAAdminDaoImpl extends GenericDAOHibernate<QA, Long> 
    implements QAAdminDao {
	
	@SuppressWarnings({ "rawtypes"})
    private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue()!=null? searchVal.getSearchValueAsString(): "";
				if (!StringUtils.isBlank(val)) {
					if(StringUtils.equals(QAConstants.WHERE_QUESTION, col)) { 
						sb.append(" and question like '%"+val+"%' ");
					} else if(StringUtils.equals(QAConstants.WHERE_STATUS, col)) { 
						sb.append(" and q_status = '"+val+"' ");
					} else if (StringUtils.equals(QAConstants.WHERE_CREATE_FROM, col)) {
						sb.append(" and TRUNC(qa.creation_date) >= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(QAConstants.WHERE_CREATE_TO, col)) {
						sb.append(" and TRUNC(qa.creation_date) <= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(QAConstants.WHERE_UPDATE_FROM, col)) {
						sb.append(" and TRUNC(qa.last_update_date) >= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(QAConstants.WHERE_UPDATE_TO, col)) {
						sb.append(" and TRUNC(qa.last_update_date) <= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");
					}
					
				}
			}
		}
		
		return sb;
    }
    
	@SuppressWarnings("rawtypes")
	private void getQueryWhereString1(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(QAAdminConstants.SEARCH_BY_KEYWORD, col)) {
						sb.append(" AND UPPER(qk.KEYWORD) LIKE UPPER(:keyword) ");
					}
					if (StringUtils.equals(QAAdminConstants.SEARCH_BY_STATUS, col)) {
						sb.append(" AND q.Q_STATUS = :status ");
					}
					if (StringUtils.equals(QAAdminConstants.SEARCH_BY_TICKET_NO, col)) {
						sb.append(" AND UPPER(q.TICKET_NO) LIKE UPPER(:ticketNo) ");
					}
					if (StringUtils.equals(QAAdminConstants.SEARCH_BY_CREATED_DATE_FROM, col)) {
						sb.append(" AND :createdDateFrom <= TO_CHAR(q.CREATION_DATE, 'yyyy-mm-dd') ");
					}
					if (StringUtils.equals(QAAdminConstants.SEARCH_BY_CREATED_DATE_TO, col)) {
						sb.append(" AND :createdDateTo >= TO_CHAR(q.CREATION_DATE, 'yyyy-mm-dd') ");
					}
					if (StringUtils.equals(QAAdminConstants.SEARCH_BY_TITLE, col)) {
						sb.append(" AND UPPER(q.Q_TITLE) LIKE UPPER(:title)");
					}
					//if (StringUtils.equals(QAAdminConstants.SEARCH_BY_USER_LOGIN, col)) {
					//	sb.append(" AND qcm.USER_ID = :userLogin ");
					//}
				}
				
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void getQuerySetString(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(QAAdminConstants.SEARCH_BY_KEYWORD, col)) {
						String keyword = "%"+val+"%";
						query.setParameter("keyword", keyword);
					}
					if (StringUtils.equals(QAAdminConstants.SEARCH_BY_STATUS, col)) {
						query.setParameter("status", val);
					}
					if (StringUtils.equals(QAAdminConstants.SEARCH_BY_TICKET_NO, col)) {
						String ticketNo = "%"+val+"%";
						query.setParameter("ticketNo", ticketNo);
					}
					if (StringUtils.equals(QAAdminConstants.SEARCH_BY_CREATED_DATE_FROM, col)) {
						query.setParameter("createdDateFrom", val);
					}
					if (StringUtils.equals(QAAdminConstants.SEARCH_BY_CREATED_DATE_TO, col)) {
						query.setParameter("createdDateTo", val);
					}
					if (StringUtils.equals(QAAdminConstants.SEARCH_BY_TITLE, col)) {
						String title = "%"+val+"%";
						query.setParameter("title", title);
					}
					//if (StringUtils.equals(QAAdminConstants.SEARCH_BY_USER_LOGIN, col)) {
					//	Long userIdLong = Long.parseLong(val);
					//	query.setParameter("userLogin", userIdLong);
					//}
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
    	sb.append(" SELECT COUNT(DISTINCT q.QNA_ID) ");
        sb.append(" FROM WO_MST_QNA q ");
        sb.append("     INNER JOIN WO_MST_USER userQ ");
        sb.append("         ON userQ.USER_ID = q.Q_USER_ID ");
        sb.append("     INNER JOIN WO_MST_PARAMETER_DTL paramQStatus ");
        sb.append("         ON paramQStatus.PARAMETER_DTL_CODE = q.Q_STATUS ");
        sb.append("     LEFT JOIN WO_MST_QNA_KEYWORD qk ");
        sb.append("         ON qk.QNA_ID = q.QNA_ID ");
        sb.append("     LEFT JOIN WO_MST_QNA_CATEGORY_MAP qcm ");
        sb.append("         ON qcm.QNA_CATEGORY_CODE = q.CATEGORY_TYPE ");
        sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL paramCat ");
        sb.append("         ON paramCat.PARAMETER_DTL_CODE = q.CATEGORY_TYPE ");
        sb.append("     LEFT JOIN WO_MST_USER userCatId ");
        sb.append("         ON userCatId.USER_ID = qcm.USER_ID ");
        sb.append("      LEFT JOIN WO_MST_USER aUser ");
        sb.append("         ON aUser.USER_ID = q.A_USER_ID ");
        sb.append(" WHERE 1 = 1 ");
        sb.append("     AND q.ENABLED_FLAG <> 'N' ");
       
//        sb = getQueryWhereString(sb, searchCriteria);
        this.getQueryWhereString1(sb, searchCriteria);
        
        Query result = getSession().createSQLQuery(sb.toString());
       this.getQuerySetString(result, searchCriteria);
        
        return (Number) result.getSingleResult();
    }
    
    @SuppressWarnings("rawtypes")
    public List<QAAdminVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        
        List<QAAdminVo> voList = searchDataCriteria(searchCriteria, first, pageSize);

        return voList;
    }
    
    @SuppressWarnings("rawtypes")
    private List<QAAdminVo> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(" SELECT DISTINCT q.QNA_ID QNA_ID ");
        sb.append("       ,q.TICKET_NO TICKET_NO ");
        sb.append("       ,q.Q_DATE Q_DATE ");
        sb.append("       ,TO_CHAR(q.Q_DATE, 'DD MON YYYY') Q_DATE_STR ");
        sb.append("       ,userQ.NAME Q_NAME ");
        sb.append("       ,q.Q_TITLE Q_TITLE ");
        sb.append("       ,q.Q_STATUS Q_STATUS_CODE ");
        sb.append("       ,paramQStatus.NAME_IN Q_STATUS_IN ");
        sb.append("       ,paramQStatus.NAME_EN Q_STATUS_EN ");
        sb.append("       ,q.SLA_TYPE SLA_TYPE ");
        sb.append("       ,q.LAST_UPDATE_BY LAST_UPDATE_BY ");
        sb.append("       ,q.LAST_UPDATE_DATE LAST_UPDATE_DATE ");
        sb.append("       ,TO_CHAR(q.LAST_UPDATE_DATE, 'DD MON YYYY') LAST_UPDATE_DATE_STR ");
        sb.append("       ,q.CATEGORY_TYPE CATEGORY_CODE ");
        sb.append("       ,paramCat.NAME_IN CATEGORY_IN ");
        sb.append("       ,paramCat.NAME_EN CATEGORY_EN ");
        //sb.append("       ,userCatId.NAME USER_CATEGORY_NAME ");
        sb.append("       ,'' USER_CATEGORY_NAME "); // this part is empty string so I dont have to edit the resultSet below, since USER CATEGORY NAME is unused
        sb.append("       ,q.CREATION_DATE CREATION_DATE ");
        sb.append("       ,TO_CHAR(q.CREATION_DATE, 'DD MON YYYY') CREATION_DATE_STR ");
        sb.append("       ,qk.KEYWORD KEYWORD ");
        sb.append("       ,aUser.NAME ANSWER_NAME ");
        sb.append(" FROM WO_MST_QNA q ");
        sb.append("     INNER JOIN WO_MST_USER userQ ");
        sb.append("         ON userQ.USER_ID = q.Q_USER_ID ");
        sb.append("     INNER JOIN WO_MST_PARAMETER_DTL paramQStatus ");
        sb.append("         ON paramQStatus.PARAMETER_DTL_CODE = q.Q_STATUS ");
        sb.append("     LEFT JOIN WO_MST_QNA_KEYWORD qk ");
        sb.append("         ON qk.QNA_ID = q.QNA_ID ");
        sb.append("     LEFT JOIN WO_MST_QNA_CATEGORY_MAP qcm ");
        sb.append("         ON qcm.QNA_CATEGORY_CODE = q.CATEGORY_TYPE ");
        sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL paramCat ");
        sb.append("         ON paramCat.PARAMETER_DTL_CODE = q.CATEGORY_TYPE ");
        sb.append("     LEFT JOIN WO_MST_USER userCatId ");
        sb.append("         ON userCatId.USER_ID = qcm.USER_ID ");
        sb.append("      LEFT JOIN WO_MST_USER aUser ");
        sb.append("         ON aUser.USER_ID = q.A_USER_ID ");
        sb.append(" WHERE 1 = 1 ");
        sb.append("     AND q.ENABLED_FLAG <> 'N' ");
        
        this.getQueryWhereString1(sb, searchCriteria);
        sb.append(" ORDER BY q.QNA_ID DESC ");
        
//        sb = getQueryWhereString(sb, searchCriteria);
        
        Query result = getSession().createSQLQuery(sb.toString());
        this.getQuerySetString(result, searchCriteria);
        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        List resultList = result.getResultList();
        
        List<QAAdminVo> vo = new ArrayList<QAAdminVo>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                QAAdminVo data = new QAAdminVo();
                
                data.setQnaId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
                data.setTicketNo(obj[1] != null ? (String) obj[1] : null);
                data.setqDate(obj[2] != null ? (Date) obj[2] : null);
                data.setqDateStr(obj[3] != null ? (String) obj[3] : null);
                data.setqName(obj[4] != null ? (String) obj[4] : null);
                data.setqTitle(obj[5] != null ? (String) obj[5] : null);
                data.setStatusCode(obj[6] != null ? (String) obj[6] : null);
                data.setStatusIn(obj[7] != null ? (String) obj[7] : null);
                data.setStatusEn(obj[8] != null ? (String) obj[8] : null);
                data.setSlaType(obj[9] != null ? (String) obj[9] : null);
                data.setTerakhirDiUbah(obj[10] != null ? (String) obj[10] : null);
                data.setTanggalTerkahirdiUbah(obj[11] != null ? (Date) obj[11] : null);
                data.setTanggalTerkahirdiUbahStr(obj[12] != null ? (String) obj[12] : null);
                data.setKategoriCode(obj[13] != null ? (String) obj[13] : null);
                data.setKategoriIn(obj[14] != null ? (String) obj[14] : null);
                data.setKategoriEn(obj[15] != null ? (String) obj[15] : null);
                data.setUserKategori(obj[16] != null ? (String) obj[16] : null);
                data.setCreationDate(obj[17] != null ? (Date) obj[17] : null);
                data.setCreationDateStr(obj[18] != null ? (String) obj[18] : null);
                data.setKataKunci(obj[19] != null ? (String) obj[19] : null);
                data.setAnswerBy(obj[20] != null ? (String) obj[20] : null);
                
                vo.add(data);
            }
        }
        
        return vo;
    }
    
    @SuppressWarnings("rawtypes")
    public List<QA> searchDataXls(List<? extends SearchObject> searchCriteria) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        StringBuilder sb = new StringBuilder();
		sb.append(" select qna_id, q_user_id, qUser.name qUserName, q_date, question, admin_user_id, adminUser.name adminUserName, a_user_id, aUser.name aUserName,a_date,answer,pd.name_in,sla_type,category_type,thread_close_status, q_status ");
		sb.append(" ,sla.name_in sla_name, cat.name_in category_name ");
		sb.append(" ,qa.created_by,qa.creation_date,qa.last_update_by,qa.last_update_date ");
		sb.append(" FROM wo_mst_qna qa inner join wo_mst_user qUser on qa.q_user_id = qUser.user_id ");
		sb.append(" left join wo_mst_user adminUser on qa.admin_user_id = adminUser.user_id ");
		sb.append(" left join wo_mst_user aUser on qa.a_user_id = aUser.user_id ");
		sb.append(" left join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = q_status ");
		sb.append(" left join wo_mst_parameter_dtl sla on sla.parameter_dtl_code = sla_type ");
		sb.append(" left join wo_mst_parameter_dtl cat on cat.parameter_dtl_code = category_type ");
		sb.append(" WHERE 1=1 and qa.enabled_flag = 'Y' ");
        
        sb = getQueryWhereString(sb, searchCriteria);
        sb.append(" ORDER BY qna_id DESC ");
        
        Query result = getSession().createSQLQuery(sb.toString());
        List resultList = result.getResultList();
        
        List<QA> vo = new ArrayList<QA>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                QA data = new QA();
 
                data.setQnaId(MathUtil.returnIdObjectToLong(obj[0]));
                data.setqUserName(obj[2]!=null?(String)obj[2]:null);
                data.setqDate(obj[3]!=null?(Timestamp)obj[3]:null);
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
                data.setqStatusStr(obj[11]!=null?(String)obj[11]:null);
                data.setSlaType(obj[12]!=null?(String)obj[12]:null);
                data.setCategoryType(obj[13]!=null?(String)obj[13]:null);
                data.setThreadStatus(obj[14]!=null?(String)obj[14]:null);
                data.setqStatus(obj[15]!=null?(String)obj[15]:null);
                data.setSlaTypeStr(obj[16]!=null?(String)obj[16]:null);
                data.setCategoryTypeStr(obj[17]!=null?(String)obj[17]:null);
                
                data.setCreatedBy(obj[18]!=null?(String)obj[18]:null);
                data.setCreationDate(obj[19]!=null?(Timestamp)obj[19]:null);
                data.setLastUpdateBy(obj[20]!=null?(String)obj[20]:null);
                data.setLastUpdateDate(obj[21]!=null?(Timestamp)obj[21]:null);
                
                vo.add(data);
            }
        }

        
        return vo;
    }
}
