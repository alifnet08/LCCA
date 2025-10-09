package com.wo.module.internalRegulationApproval.dao;


//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.internalRegulationApproval.constant.InternalRegulationApprovalConstants;
import com.wo.module.internalRegulationApproval.model.InternalRegulationApproval;

@Repository("internalRegulationApprovalDao")
public class InternalRegulationApprovalDaoImpl extends GenericDAOHibernate<InternalRegulationApproval, Long> 
    implements InternalRegulationApprovalDao {

	@SuppressWarnings("rawtypes")
    private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue()!=null?searchVal.getSearchValueAsString():"";
				if (!StringUtils.isBlank(val)) {					
					if (StringUtils.equals(InternalRegulationApprovalConstants.WHERE_JENIS_KETENTUAN, col)) {
						sb.append(" and r.jenis_ketentuan = '" + val + "'");
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
		sb.append(" SELECT count(1) ");
		sb.append("   FROM wo_tmp_regulation r  ");
		sb.append("        LEFT JOIN wo_mst_document_type dt on r.document_type_id = dt.document_type_id  ");
		sb.append("        LEFT JOIN wo_mst_document_category dc on dc.document_category_id = r.document_category_id  ");
		//sb.append(" left join wo_mst_document_topic dp on dp.document_topic_id = r.document_topic_id  ");
		sb.append("        left join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = r.status ");
		sb.append("  WHERE 1=1 ");
		sb.append("        and r.enabled_flag = 'Y' ");
		sb.append("        and r.status = 'DATA_NEW' ");
       
        sb = getQueryWhereString(sb, searchCriteria);
        
        Query result = getSession().createSQLQuery(sb.toString());
       
        
        return (Number) result.getSingleResult();
    }
    
    @SuppressWarnings("rawtypes")
    public List<InternalRegulationApproval> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        
        List<InternalRegulationApproval> voList = searchDataCriteria(searchCriteria, first, pageSize);

        return voList;
    }
    
    @SuppressWarnings("rawtypes")
    private List<InternalRegulationApproval> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
        
        StringBuilder sb = new StringBuilder();
		sb.append(" SELECT dt.document_type_in,dt.document_type_en, dc.document_category_in, ");
		sb.append("        dc.document_category_en, '' document_topic_in, '' document_topic_en, ");
		sb.append("        r.document_no, r.name_in, r.name_en, TO_CHAR(r.published_date, 'dd-Mon-yyyy') published_date, ");
		sb.append("        TO_CHAR(r.expired_date, 'dd-Mon-yyyy') expired_date, r.publisher_unit, r.status, ");
		sb.append("        r.regulation_id, pd.name_in status_name_in, pd.name_en status_name_en ");
		sb.append("   FROM wo_tmp_regulation r  ");
		sb.append("        LEFT JOIN wo_mst_document_type dt on r.document_type_id = dt.document_type_id  ");
		sb.append(" 	   LEFT JOIN wo_mst_document_category dc on dc.document_category_id = r.document_category_id  ");
		//sb.append(" 	   LEFT JOIN wo_mst_document_topic dp on dp.document_topic_id = r.document_topic_id  ");
		sb.append("        LEFT JOIN wo_mst_parameter_dtl pd on pd.parameter_dtl_code = r.status ");
		sb.append("  WHERE 1=1 ");
		sb.append("        and r.enabled_flag = 'Y' ");
		sb.append("        and r.status = 'DATA_NEW' ");
        
        sb = getQueryWhereString(sb, searchCriteria);
        sb.append(" ORDER BY r.published_date DESC, r.document_no asc ");
        
        
        Query result = getSession().createSQLQuery(sb.toString());
        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        List resultList = result.getResultList();
        
        List<InternalRegulationApproval> vo = new ArrayList<InternalRegulationApproval>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                InternalRegulationApproval data = new InternalRegulationApproval();
                data.setDocumentTypeNameIn(obj[0]!=null?(String)obj[0]:null);
                data.setDocumentTypeNameEn(obj[1]!=null?(String)obj[1]:null);
                data.setDocumentCategoryNameIn(obj[2]!=null?(String)obj[2]:null);
                data.setDocumentCategoryNameEn(obj[3]!=null?(String)obj[3]:null);
                data.setDocumentTopicNameIn(obj[4]!=null?(String)obj[4]:null);
                data.setDocumentTopicNameEn(obj[5]!=null?(String)obj[5]:null);
                data.setDocumentNo(obj[6]!=null?(String)obj[6]:null);
                data.setNameIn(obj[7]!=null?(String)obj[7]:null);
                data.setNameEn(obj[8]!=null?(String)obj[8]:null);
                data.setPublishedDateStr(obj[9]!=null?(String)obj[9]:null);
                data.setExpiredDateStr(obj[10]!=null?(String)obj[10]:null);
                data.setPublisherUnit(obj[11]!=null?(String)obj[11]:null);
                data.setStatus(obj[12]!=null?(String)obj[12]:null);
                //data.setRegulationId(obj[13]!=null?((BigInteger)obj[13]).longValue():null);
                data.setRegulationId(obj[13]!=null?(MathUtil.returnIdObjectToLong(obj[13])):null);
                data.setStatusNameIn(obj[14]!=null?(String)obj[14]:null);
                data.setStatusNameEn(obj[15]!=null?(String)obj[15]:null);
               
                vo.add(data);
            }
        }

        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        return vo;
    }    
}
