/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.documentTopic.dao;


//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.documentTopic.constant.DocumentTopicConstants;
import com.wo.module.documentTopic.model.DocumentTopic;

/**
 *
 * @author hendra
 * 
 * Modification Alex
 */

@Repository("documentTopicDao")
public class DocumentTopicDaoImpl extends GenericDAOHibernate<DocumentTopic, Long> 
    implements DocumentTopicDao {
	
	
	@SuppressWarnings({ "rawtypes", "static-access" })
    private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue()!=null? searchVal.getSearchValueAsString(): "";
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(SearchObject.ALL_COLUMNS, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and upper(document_topic_en) LIKE upper('%"+val+"%') ");
						} else {
							sb.append(" and upper(document_topic_in) LIKE upper('%"+val+"%') ");
						}
					} else if(StringUtils.equals(DocumentTopicConstants.WHERE_JENIS_KETENTUAN_CODE, col)) { 
						sb.append(" and dt.jenis_ketentuan_code = '"+val+"' ");
					
					
				/*	} else if (StringUtils.equals("JENIS_KETENTUAN_EKSTERNAL", col)) {
						sb.append(" and dt.jenis_ketentuan_code = 'KETENTUAN_EKSTERNAL' ");
						if(!StringUtils.isBlank(val) && !val.equals("0")) {
							sb.append(" and dt.document_category_id = '"+val+"' ");
						}
					}else if (StringUtils.equals("JENIS_KETENTUAN_INTERNAL", col)) {
						sb.append(" and dt.jenis_ketentuan_code = 'KETENTUAN_INTERNAL' ");
						if(!StringUtils.isBlank(val) && !val.equals("0")) {
							sb.append(" and dt.document_category_id = '"+val+"' ");
						}
					*/
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
		sb.append(" FROM wo_mst_document_topic dt inner join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = dt.jenis_ketentuan_code ");
		sb.append(" left join wo_mst_document_category dc on dc.document_category_id = dt.document_category_id ");
		sb.append(" WHERE 1=1 and dt.enabled_flag = 'Y' ");
        
        sb = getQueryWhereString(sb, searchCriteria);
        
        Query result = getSession().createSQLQuery(sb.toString());
       
        
        return (Number) result.getSingleResult();
    }
    
    @SuppressWarnings("rawtypes")
    public List<DocumentTopic> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        
        List<DocumentTopic> voList = searchDataCriteria(searchCriteria, first, pageSize);

        return voList;
    }
    
    @SuppressWarnings("rawtypes")
    private List<DocumentTopic> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
        
        
        StringBuilder sb = new StringBuilder();
		sb.append(" select document_topic_id, name_in,name_en, document_topic_in, document_topic_en,document_category_in,document_category_en  ");
		sb.append(" FROM wo_mst_document_topic dt inner join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = dt.jenis_ketentuan_code ");
		sb.append(" left join wo_mst_document_category dc on dc.document_category_id = dt.document_category_id ");
		sb.append(" WHERE 1=1 and dt.enabled_flag = 'Y' ");
		sb = getQueryWhereString(sb, searchCriteria);
        sb.append(" ORDER BY document_topic_in ASC ");
        
        
        Query result = getSession().createSQLQuery(sb.toString());
        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        List resultList = result.getResultList();
        
        List<DocumentTopic> vo = new ArrayList<DocumentTopic>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                DocumentTopic data = new DocumentTopic();
                //data.setDocumentTopicId(((BigInteger)obj[0]).longValue());
                data.setDocumentTopicId(MathUtil.returnIdObjectToLong(obj[0]));
                data.setParameterDtlNameIn((String)obj[1]);
                data.setParameterDtlNameEn((String)obj[2]);
                data.setDocumentTopicIn((String)obj[3]);
                data.setDocumentTopicEn((String)obj[4]);
                data.setDocumentCategoryStrIn((String)obj[5]);
                data.setDocumentCategoryStrEn((String)obj[6]);
                //data.setTypeDescription((String)obj[5]);
                
                vo.add(data);
            }
        }

        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        return vo;
    }



	@Override
	public Integer getDocumentTopicByProvCategoryInAndTopic(String provision, Long categoryIn, String topic)
			throws Exception {
		
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT COUNT(1) ");
		sb.append("		FROM wo_mst_document_topic dt ");
		sb.append("			LEFT JOIN wo_mst_document_category dc");
		sb.append("				ON dt.document_category_id = dc.document_category_id");
		sb.append("		WHERE dt.jenis_ketentuan_code = '" +provision+ "'");
		if (categoryIn != null && categoryIn > 0) {
			sb.append("			AND dt.document_category_id = '" +categoryIn+ "'");
		}
		sb.append("			AND UPPER(dt.document_topic_in) = UPPER('"+topic+"') and dt.enabled_flag = 'Y' ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		
		Number count = (Number) result.getSingleResult();
		if(count == null) {
			count = 0;
		}
		
		return (Integer) count.intValue();
	}



	@Override
	public Integer getEditDocumentTopicByIdProvCategoryAndTopic(Long id, String provision, Long categoryIn,
			String topic) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT COUNT(1) ");
		sb.append("		FROM wo_mst_document_topic dt ");
		sb.append("			LEFT JOIN wo_mst_document_category dc");
		sb.append("				ON dt.document_category_id = dc.document_category_id");
		sb.append("		WHERE dt.jenis_ketentuan_code = '" +provision+ "'");
		if (categoryIn != null && categoryIn > 0) {
			sb.append("			AND dt.document_category_id = '" +categoryIn+ "'");
		}
		sb.append("			AND UPPER(dt.document_topic_in) = UPPER('"+topic+"')");
		sb.append("			AND dt.document_topic_id <> '"+id+"'");
		sb.append("			AND dt.enabled_flag = 'Y' ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		
		Number count = (Number) result.getSingleResult();
		if(count == null) {
			count = 0;
		}
		
		return (Integer) count.intValue();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Boolean isUsedInTransaction(Long id) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select dt.document_topic_id ");
		sb.append(" from wo_mst_document_topic dt ");
		sb.append(" where 1=1 ");
		sb.append(" and dt.document_topic_id = :documentTopicId ");
		sb.append(" and ( exists (select reg.document_topic_id from wo_tmp_regulation reg ");
		sb.append("                where reg.document_topic_id = :documentTopicId and reg.enabled_flag = 'Y') ");
		sb.append("    ) ");
		
		Query query = getSession().createSQLQuery(sb.toString().trim());

		query.setParameter("documentTopicId", id);		

		List resultList = query.getResultList();

		if (resultList != null && resultList.size() > 0) {
			return true;
		}

		return false;
	}
	
	public Integer getDocumentTopicByDocumentTopicIn(String topicIn) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT COUNT(1) ");
		sb.append("		FROM wo_mst_document_topic dt ");
		sb.append("			WHERE UPPRE(document_topic_in) = UPPER('"+topicIn+"') and enabled_flag = 'Y'");
		
		
		Query result = getSession().createSQLQuery(sb.toString());
		
		Number count = (Number) result.getSingleResult();
		if(count == null) {
			count = 0;
		}
		
		return (Integer) count.intValue();
	}

	
    
}
