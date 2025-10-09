/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.documentType.dao;


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
import com.wo.module.documentType.constant.DocumentTypeConstants;
import com.wo.module.documentType.model.DocumentType;

/**
 *
 * @author hendra
 */

@Repository("documentTypeDao")
public class DocumentTypeDaoImpl extends GenericDAOHibernate<DocumentType, Long> 
    implements DocumentTypeDao {
	
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
							sb.append(" and UPPER(document_type_en) LIKE UPPER('%"+val+"%') ");
						} else {
							sb.append(" and UPPER(document_type_in) LIKE UPPER('%"+val+"%') ");
						}
						
					}else if(StringUtils.equals(DocumentTypeConstants.WHERE_JENIS_KETENTUAN_CODE, col)) { 
						sb.append(" and jenis_ketentuan_code = '"+val+"' ");
					} else if (StringUtils.equals("JENIS_KETENTUAN_EKSTERNAL", col)) {						
						sb.append(" and jenis_ketentuan_code = 'KETENTUAN_EKSTERNAL' ");						
					} else if (StringUtils.equals("JENIS_KETENTUAN_INTERNAL", col)) {
						sb.append(" and jenis_ketentuan_code = '"+val+"' ");
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
		sb.append(" FROM wo_mst_document_type dt inner join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = dt.jenis_ketentuan_code ");
		sb.append(" WHERE 1=1 and dt.enabled_flag = 'Y' ");
       
        sb = getQueryWhereString(sb, searchCriteria);
        
        Query result = getSession().createSQLQuery(sb.toString());
       
        
        return (Number) result.getSingleResult();
    }
    
    @SuppressWarnings("rawtypes")
    public List<DocumentType> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        
        List<DocumentType> voList = searchDataCriteria(searchCriteria, first, pageSize);

        return voList;
    }
    
    @SuppressWarnings("rawtypes")
    private List<DocumentType> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
        
        StringBuilder sb = new StringBuilder();
		sb.append(" select document_type_id, name_in,name_en, document_type_in, document_type_en, type_description ");
		sb.append(" FROM wo_mst_document_type dt inner join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = dt.jenis_ketentuan_code ");
		sb.append(" WHERE 1=1 and dt.enabled_flag = 'Y' ");
        
        sb = getQueryWhereString(sb, searchCriteria);
        sb.append(" ORDER BY document_type_in ASC ");
        
        
        Query result = getSession().createSQLQuery(sb.toString());
        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        List resultList = result.getResultList();
        
        List<DocumentType> vo = new ArrayList<DocumentType>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                DocumentType data = new DocumentType();
                //data.setDocumentTypeId(((BigInteger)obj[0]).longValue());
                data.setDocumentTypeId(MathUtil.returnIdObjectToLong(obj[0]));
                data.setParameterDtlNameIn((String)obj[1]);
                data.setParameterDtlNameEn((String)obj[2]);
                data.setDocumentTypeIn((String)obj[3]);
                data.setDocumentTypeEn((String)obj[4]);
                data.setTypeDescription((String)obj[5]);
                
                vo.add(data);
            }
        }

        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        return vo;
    }

	@Override
	public Integer getDocumentTypeByProvAndType(String provision, String documentType) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT COUNT(1) ");
		sb.append("		FROM wo_mst_document_type ");
		sb.append("		WHERE jenis_ketentuan_code = '"+provision+"' ");
		sb.append("			AND document_type_in = '"+documentType+"' ");
		sb.append("			AND enabled_flag = 'Y' ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		
		Number count = (Number) result.getSingleResult();
		if(count == null) {
			count = 0;
		}
		
		return (Integer) count.intValue() ;
	}
	
	public Long getDocumentTypeIdByProvAndType(String provision, String documentType) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT document_type_id,document_type_in ");
		sb.append("		FROM wo_mst_document_type ");
		sb.append("		WHERE jenis_ketentuan_code = '"+provision+"' ");
		sb.append("			AND document_type_in = '"+documentType+"' ");
		sb.append("			AND enabled_flag = 'Y' ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		
		@SuppressWarnings("rawtypes")
		List resultList = result.getResultList();
		Long docTypeId = null;
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				docTypeId = MathUtil.returnIdObjectToLong(obj[0]);
			}
		}
		
		return docTypeId ;
	}

	@Override
	public Integer getEditDocumentTypeByIdProvAndType(Long id, String provision, String type) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT COUNT(1)"
				+ "		FROM wo_mst_document_type ");
		sb.append("		WHERE document_type_id != '" +id+ "'");
		sb.append("			AND document_type_in = '" +type+ "' ");
		sb.append("			AND jenis_ketentuan_code = '" +provision+ "' ");
		sb.append("			AND enabled_flag = 'Y' ");
		
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
		sb.append(" select dt.document_type_id ");
		sb.append(" from wo_mst_document_type dt ");
		sb.append(" where 1=1 ");
		sb.append(" and dt.document_type_id = :documentTypeId ");
		sb.append(" and ( exists (select reg.document_type_id from wo_tmp_regulation reg ");
		sb.append("                where reg.document_type_id = :documentTypeId and reg.enabled_flag = 'Y') ");
		sb.append("    ) ");
		
		Query query = getSession().createSQLQuery(sb.toString().trim());

		query.setParameter("documentTypeId", id);		

		List resultList = query.getResultList();

		if (resultList != null && resultList.size() > 0) {
			return true;
		}

		return false;
	}
	
    
}
