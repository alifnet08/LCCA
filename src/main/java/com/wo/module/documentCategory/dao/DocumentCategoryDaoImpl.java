/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.documentCategory.dao;


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
import com.wo.module.documentCategory.constant.DocumentCategoryConstants;
import com.wo.module.documentCategory.model.DocumentCategory;
//import com.wo.module.documentTopic.model.DocumentTopic;

/**
 *
 * @author hendra
 * 
 * Modification Alex
 */

@Repository("documentCategoryDao")
public class DocumentCategoryDaoImpl extends GenericDAOHibernate<DocumentCategory, Long> 
    implements DocumentCategoryDao {
	
	@SuppressWarnings({ "rawtypes", "static-access" })
    private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue()!=null?searchVal.getSearchValueAsString():"";
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(SearchObject.ALL_COLUMNS, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and UPPER(document_category_en) LIKE UPPER('%"+val+"%') ");
						} else {
							sb.append(" and UPPER(document_category_in) LIKE UPPER('%"+val+"%') ");
						}
					}else if(StringUtils.equals(DocumentCategoryConstants.WHERE_JENIS_KETENTUAN_CODE, col)) { 
						sb.append(" and jenis_ketentuan_code = '"+val+"' ");
					}else if (StringUtils.equals("JENIS_KETENTUAN_EKSTERNAL", col)) {
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
		sb.append(" FROM wo_mst_document_category dc inner join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = dc.jenis_ketentuan_code  ");
		sb.append(" WHERE 1=1 and dc.enabled_flag = 'Y' ");
        
        sb = getQueryWhereString(sb, searchCriteria);
        
        Query result = getSession().createSQLQuery(sb.toString());
       
        
        return (Number) result.getSingleResult();
    }
    
    @SuppressWarnings("rawtypes")
    public List<DocumentCategory> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        
        List<DocumentCategory> voList = searchDataCriteria(searchCriteria, first, pageSize);

        return voList;
    }
    
    @SuppressWarnings("rawtypes")
    private List<DocumentCategory> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
        
        
        StringBuilder sb = new StringBuilder();
		sb.append(" select document_category_id, name_in,name_en, document_category_in, document_category_en, dc.jenis_ketentuan_code  ");
		sb.append(" FROM wo_mst_document_category dc inner join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = dc.jenis_ketentuan_code ");
		sb.append(" WHERE 1=1 and dc.enabled_flag = 'Y' ");
        
        sb = getQueryWhereString(sb, searchCriteria);
        sb.append(" ORDER BY document_category_in ASC ");
        
        Query result = getSession().createSQLQuery(sb.toString());
        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        List resultList = result.getResultList();
        
        List<DocumentCategory> vo = new ArrayList<DocumentCategory>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                DocumentCategory data = new DocumentCategory();
                //data.setDocumentCategoryId(((BigInteger)obj[0]).longValue());
                data.setDocumentCategoryId(MathUtil.returnIdObjectToLong(obj[0]));
                data.setParameterDtlNameIn((String)obj[1]);
                data.setParameterDtlNameEn((String)obj[2]);
                data.setDocumentCategoryIn((String)obj[3]);
                data.setDocumentCategoryEn((String)obj[4]);
                data.setJenisKetentuanCode((String)obj[5]);
                
                vo.add(data);
            }
        }

        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        return vo;
    }



	@Override
	public Integer getDocumentCategoryByProvAndCategory(String provision, String categoryIn) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT COUNT(1) ");
		sb.append("		FROM wo_mst_document_category ");
		sb.append("		WHERE jenis_ketentuan_code = '" +provision+ "'");
		sb.append("			AND document_category_in = '" +categoryIn+ "'");
		sb.append("			AND enabled_flag = 'Y' ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		
		Number count = (Number) result.getSingleResult();
		if(count == null) {
			count = 0;
		}
		
		return (Integer) count.intValue();
	}

	@SuppressWarnings("rawtypes")
	public DocumentCategory getDocumentCategoryBySameValue(String provision,String categoryIn) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT document_category_id,jenis_ketentuan_code,document_category_in,enabled_flag ");
		sb.append("		FROM wo_mst_document_category ");
		sb.append("		WHERE jenis_ketentuan_code = '" +provision+ "'");
		sb.append("			AND document_category_in = '" +categoryIn+ "'");
		
//		if(categoryId != null && categoryId > 0) {
//			sb.append("		AND document_category_id = "+categoryId+" ");
//		}
		
		Query result = getSession().createSQLQuery(sb.toString());
		result.setMaxResults(1);
		
		List resultList = result.getResultList();
		
		DocumentCategory dc = new DocumentCategory();
		
		if(resultList != null && resultList.size() > 0) {
			Object[] obj = (Object[]) resultList.get(0);
			//dc.setDocumentCategoryId(obj[0] != null ? ((BigInteger) obj[0]).longValue() : null);
			dc.setDocumentCategoryId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
			dc.setJenisKetentuanCode((String) obj[1]);
			dc.setDocumentCategoryIn((String) obj[2]);
			dc.setEnabledFlag((String) obj[3]);
		}
		
		return dc;		
	}

	@Override
	public Integer getEditDocumentCategoryByProvAndCategory(Long id, String provision, String category)
			throws Exception {
		
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT COUNT(1) ");
		sb.append("		FROM wo_mst_document_category ");
		sb.append("		WHERE jenis_ketentuan_code = '" +provision+ "'");
		sb.append("			AND document_category_in = '" +category+ "'");
		sb.append("			AND document_category_id <> '"+id+"'");
		sb.append("			AND enabled_flag = 'Y' ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		
		Number count = (Number) result.getSingleResult();
		if(count == null) {
			count = 0;
		}
		
		return (Integer) count.intValue();
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<DocumentCategory> getDocumentTopicByJenisKetentuan(String jenisKetentuan) throws Exception {
		
		String hql = "FROM DocumentCategory where parameterDetail.parameterDtlCode= :jenisKetentuan AND enabled_flag = 'Y' ";
		Query result = getSession().createQuery(hql);
		result.setParameter("jenisKetentuan", jenisKetentuan);

		return result.getResultList();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Boolean isUsedInTransaction(Long id) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select dc.document_category_id ");
		sb.append(" from wo_mst_document_category dc ");
		sb.append(" where 1=1 ");
		sb.append(" and dc.document_category_id = :documentCategoryId ");
		sb.append(" and ( exists (select reg.document_category_id from wo_tmp_regulation reg ");
		sb.append("                where reg.document_category_id = :documentCategoryId and reg.enabled_flag = 'Y') ");
		sb.append("     or exists (select dt.document_category_id from wo_mst_document_topic dt ");
		sb.append("               where dt.document_category_id = :documentCategoryId  and dt.enabled_flag = 'Y') ");
		sb.append("    ) ");
		
		Query query = getSession().createSQLQuery(sb.toString().trim());

		query.setParameter("documentCategoryId", id);		

		List resultList = query.getResultList();

		if (resultList != null && resultList.size() > 0) {
			return true;
		}

		return false;
	}
}
