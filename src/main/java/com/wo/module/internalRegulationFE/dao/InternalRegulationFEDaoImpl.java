package com.wo.module.internalRegulationFE.dao;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.internalRegulation.constant.InternalRegulationConstants;
import com.wo.module.internalRegulation.model.InternalRegulation;
import com.wo.module.internalRegulationFE.vo.InternalRegulationFEVO;

@Repository("internalRegulationFEDao")
public class InternalRegulationFEDaoImpl extends GenericDAOHibernate<InternalRegulation, Long> 
    implements InternalRegulationFEDao {

	@SuppressWarnings("rawtypes")
    private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue()!=null?searchVal.getSearchValueAsString():"";
				if (!StringUtils.isBlank(val)) {					
					/*if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						sb.append(" and (UPPER(r.name_in) like UPPER('%" + val + "%') or r.document_no like '%" + val + "%')");
					}*/
					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
						sb.append(" and r.status = '" + val + "' ");
					}
					else if (StringUtils.equals(InternalRegulationConstants.WHERE_DOC_TYPE, col)) {
						sb.append(" and dt.document_type_id = " + val + " ");
					}
					else if (StringUtils.equals(InternalRegulationConstants.WHERE_CATEGORY, col)) {
						sb.append(" and r.document_category_id = " + val + " ");
					}
					else if (StringUtils.equals(InternalRegulationConstants.WHERE_DOC_TYPE_IS_NOT_ANOUNCEMENT, col)) {
						sb.append(" and dt.document_type_in <> '" + InternalRegulationConstants.DOCUMENT_TYPE_INFORMASI_LAINNYA + "' ");
					}
					else if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
					}
					else if (StringUtils.equals(InternalRegulationConstants.WHERE_YEAR, col)) {
						sb.append(" and to_char(r.effective_date, 'yyyy') = " + val + " ");
					}
				}
			}
		}
		return sb;
    }
	
	@SuppressWarnings("rawtypes")
	private void getQuerySetValue(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
//				Object valReal = searchVal.getSearchValue();

				if (!StringUtils.isBlank(val)) {
					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						query.setParameter("userId", val );						
					}
//					if (StringUtils.equals(CommonConstants.SEARCH_CATEGORY, col)) {
//						query.setParameter("document_category_id", val );						
//					}

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
    	String valueSearch = getSearchVal(searchCriteria);		
    	StringBuilder sb = new StringBuilder();
		sb.append(" SELECT count(1) ");
		sb.append("   FROM wo_mst_regulation r  ");
		sb.append("        LEFT JOIN wo_mst_document_type dt on r.document_type_id = dt.document_type_id  ");
		sb.append("        LEFT JOIN wo_mst_document_category dc on dc.document_category_id = r.document_category_id  ");
		sb.append("        left join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = r.status ");
		sb.append("  WHERE 1=1 ");
		sb.append("        and r.enabled_flag = 'Y' ");
		sb.append("        and r.JENIS_KETENTUAN = 'KETENTUAN_INTERNAL' ");
		//sb.append("        and r.status = 'DATA_ACTIVE' ");
		
		List<String> list = getSearchValList(searchCriteria);
		if(list!=null && !list.isEmpty()) {
			if(list.size()==1) {
				sb.append(" and (UPPER(r.name_in) like UPPER('%"+valueSearch+"%') or UPPER(r.document_no) like UPPER('%"+valueSearch+"%')) ");
			}else {
				for(int i=0;i<list.size();i++) {
					String val = list.get(i);
					if(i==0) {
						sb.append(" and (");
						sb.append(" UPPER(r.name_in) like UPPER('%"+val+"%') or UPPER(r.document_no) like UPPER('%"+val+"%') ");
					}else if(i==list.size()-1) {
						sb.append(" OR UPPER(r.name_in) like UPPER('%"+val+"%') or UPPER(r.document_no) like UPPER('%"+val+"%') ");
						sb.append(" )");
					}else {
						sb.append(" OR UPPER(r.name_in) like UPPER('%"+val+"%') or UPPER(r.document_no) like UPPER('%"+val+"%') ");
					}
				}
			}
		}
       
        sb = getQueryWhereString(sb, searchCriteria);
        
        Query result = getSession().createSQLQuery(sb.toString());
        
        
       
        
        return (Number) result.getSingleResult();
    }
    
    @SuppressWarnings("rawtypes")
    public List<InternalRegulationFEVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        
        List<InternalRegulationFEVO> voList = searchDataCriteria(searchCriteria, first, pageSize);

        return voList;
    }
    
    private String getSearchVal(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria) {
    	String valueSearch = null;
		if (searchCriteria != null) {
			for (@SuppressWarnings("rawtypes") SearchObject data : searchCriteria) {
				String col = data.getSearchColumn();
				String val = data.getSearchValueAsString();
				if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col) && !StringUtils.isBlank(val)) {
					valueSearch = val;
					break;
				}
			}
		}
		
		return valueSearch;
    }
    
    private List<String> getSearchValList(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria) {
		String valueSearch = getSearchVal(searchCriteria);	
		List<String> list = new ArrayList<String>();
		
		if(valueSearch!=null && !valueSearch.isEmpty()) {
			valueSearch = valueSearch.replace(":and", "&");
			valueSearch = valueSearch.replace(":percent", "%");
			
			String[] newStr = valueSearch.split(" ");
			String data = "";
			for (int i = 0; i < newStr.length; i++) {
				if(i==0) {
					list.add(newStr[i]);
					data = newStr[i];
				}else {
					list.add(newStr[i]);
					data = data.concat(" ").concat(newStr[i]);
					list.add(data);
				}
	        }
			
			Collections.sort(list, new Comparator<String>() {

				@Override
	            public int compare(String str1, String str2) {
	                return str2.length() - str1.length();
	            }
	        });
			
			System.out.println(list);
		}
		
		return list;
	}
    
    @SuppressWarnings("rawtypes")
    private StringBuilder getQueryWhereString2(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue()!=null?searchVal.getSearchValueAsString():"";
				if (!StringUtils.isBlank(val)) {					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						sb.append(" and (UPPER(r.name_in) like UPPER('%" + val + "%') or r.document_no like '%" + val + "%')");
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
						sb.append(" and r.status = '" + val + "' ");
					}
					else if (StringUtils.equals(InternalRegulationConstants.WHERE_DOC_TYPE, col)) {
						sb.append(" and dt.document_type_id = " + val + " ");
					}
					else if (StringUtils.equals(InternalRegulationConstants.WHERE_CATEGORY, col)) {
						sb.append(" and r.document_category_id = " + val + " ");
					}
					else if (StringUtils.equals(InternalRegulationConstants.WHERE_DOC_TYPE_IS_NOT_ANOUNCEMENT, col)) {
						sb.append(" and dt.document_type_in <> '" + InternalRegulationConstants.DOCUMENT_TYPE_INFORMASI_LAINNYA + "' ");
					}
					else if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
					}
					else if (StringUtils.equals(InternalRegulationConstants.WHERE_YEAR, col)) {
						sb.append(" and to_char(r.effective_date, 'yyyy') = " + val + " ");
					}
				}
			}
		}
		return sb;
    }
    
    @SuppressWarnings("rawtypes")
    private List<InternalRegulationFEVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {   
    	String valueSearch = getSearchVal(searchCriteria);			        
        StringBuilder sb = new StringBuilder();
        //sb.append(" SELECT * from ( ");
		sb.append(" with queryKetentuanInternal as( ");
        sb.append(" SELECT r.regulation_id, r.name_in, ");
		sb.append("        TO_CHAR(r.effective_date, 'dd FMMonth yyyy', 'nls_date_language=indonesian') effective_date, ");
		sb.append("        TO_CHAR(r.expired_date, 'dd FMMonth yyyy', 'nls_date_language=indonesian') expired_date, document_no, r.status, ");
		sb.append("        (select count(1) ");
		sb.append("           from wo_log_access a ");
		sb.append("          where a.user_id = :userId  and a.access_id = r.regulation_id   ");
		sb.append("                and ((a.access_action = '/compliance/pages/internalRegulationFE/internalRegulationFE.faces') ");
		sb.append("                     OR (a.access_action = '/compliance/pages/internalRegulationFE/internalRegulationFEView.faces')) ) flagIsAccess ");
		sb.append("   FROM wo_mst_regulation r  ");
		sb.append("        LEFT JOIN wo_mst_document_type dt on r.document_type_id = dt.document_type_id  ");
		sb.append("        LEFT JOIN wo_mst_document_category dc on dc.document_category_id = r.document_category_id  ");
		sb.append("  WHERE 1=1 ");
		sb.append("        and r.enabled_flag = 'Y' ");
		sb.append("        and r.JENIS_KETENTUAN = 'KETENTUAN_INTERNAL' ");
		sb = getQueryWhereString2(sb, searchCriteria);		
		sb.append(" ORDER BY r.published_date DESC, r.document_no asc ), ");
		sb.append(" queryKetentuanInternal2 as( ");
		sb.append(" SELECT r.regulation_id, r.name_in, ");
		sb.append("        TO_CHAR(r.effective_date, 'dd FMMonth yyyy', 'nls_date_language=indonesian') effective_date, ");
		sb.append("        TO_CHAR(r.expired_date, 'dd FMMonth yyyy', 'nls_date_language=indonesian') expired_date, document_no, r.status, ");
		sb.append("        (select count(1) ");
		sb.append("           from wo_log_access a ");
		sb.append("          where a.user_id = :userId  and a.access_id = r.regulation_id   ");
		sb.append("                and ((a.access_action = '/compliance/pages/internalRegulationFE/internalRegulationFE.faces') OR (a.access_action = '/compliance/pages/internalRegulationFE/internalRegulationFEView.faces')) ) flagIsAccess ");
		sb.append("   FROM wo_mst_regulation r  ");
		sb.append("        LEFT JOIN wo_mst_document_type dt on r.document_type_id = dt.document_type_id  ");
		sb.append("        LEFT JOIN wo_mst_document_category dc on dc.document_category_id = r.document_category_id  ");
		sb.append("  WHERE 1=1 ");
		sb.append("        and r.enabled_flag = 'Y' ");
		sb.append("        and r.JENIS_KETENTUAN = 'KETENTUAN_INTERNAL' ");
		sb.append("        and r.regulation_id not in (select regulation_id from queryKetentuanInternal) ");
		//sb.append("        and r.status = 'DATA_ACTIVE' ");
		
		List<String> list = getSearchValList(searchCriteria);
		if(list!=null && !list.isEmpty()) {
			if(list.size()==1) {
				sb.append(" and (UPPER(r.name_in) like UPPER('%"+valueSearch+"%') or UPPER(r.document_no) like UPPER('%"+valueSearch+"%')) ");
			}else {
				for(int i=0;i<list.size();i++) {
					String val = list.get(i);
					if(i==0) {
						sb.append(" and (");
						sb.append(" UPPER(r.name_in) like UPPER('%"+val+"%') or UPPER(r.document_no) like UPPER('%"+val+"%') ");
					}else if(i==list.size()-1) {
						sb.append(" OR UPPER(r.name_in) like UPPER('%"+val+"%') or UPPER(r.document_no) like UPPER('%"+val+"%') ");
						sb.append(" )");
					}else {
						sb.append(" OR UPPER(r.name_in) like UPPER('%"+val+"%') or UPPER(r.document_no) like UPPER('%"+val+"%') ");
					}
				}
			}
		}
		
        sb = getQueryWhereString(sb, searchCriteria);
        sb.append(" ORDER BY published_date DESC, document_no asc)");
        sb.append(" SELECT * from  queryKetentuanInternal ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT * from  queryKetentuanInternal2 ");
        
        Query result = getSession().createSQLQuery(sb.toString());
        
        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        this.getQuerySetValue(result, searchCriteria);
        
        List resultList = result.getResultList();
        
        List<InternalRegulationFEVO> vo = new ArrayList<InternalRegulationFEVO>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                InternalRegulationFEVO data = new InternalRegulationFEVO();
                data.setRegulationId(obj[0]!=null?MathUtil.returnIdObjectToLong(obj[0]):null);
                data.setNameIn(obj[1]!=null?(String)obj[1]:null);
                data.setEffectiveDate (obj[2]!=null?(String)obj[2]:null);
                data.setExpiredDate(obj[3]!=null?(String)obj[3]:null);
                data.setDocNo(obj[4]!=null?(String)obj[4]:null);
                data.setStatus(obj[5]!=null?(String)obj[5]:null);
                data.setIsAccess(obj[6]!=null?((BigDecimal)obj[6]).intValue():null);
                vo.add(data);
            }
        }

        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        return vo;
    }    
}
