package com.wo.module.externalRegulationFE.dao;

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
import com.wo.module.externalRegulation.model.ExternalRegulation;
import com.wo.module.externalRegulationFE.vo.ExternalRegulationFEVO;
import com.wo.module.internalRegulation.constant.InternalRegulationConstants;

@Repository("externalRegulationFEDAO")
public class ExternalRegulationFEDAOImpl extends GenericDAOHibernate<ExternalRegulation, Long>
	implements ExternalRegulationFEDAO {

	private String getSearchVal(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria) {
		String searchVal = null;
		if (searchCriteria != null) {
			for (@SuppressWarnings("rawtypes") SearchObject data : searchCriteria) {
				String col = data.getSearchColumn();
				String val = data.getSearchValueAsString();
				if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col) && !StringUtils.isBlank(val)) {
					searchVal = val;
					break;
				}
			}
		}
		return searchVal;
	}
	
	@SuppressWarnings("rawtypes")
    private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue()!=null?searchVal.getSearchValueAsString():"";
				if (!StringUtils.isBlank(val)) {					
					/*if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						sb.append(" and (UPPER(r.name_in) like UPPER('%" + val + "%') or r.document_no like '%" + val + "%')");
					}
					else*/ if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
						sb.append(" and r.status = '" + val + "' ");
					}
					else if (StringUtils.equals(InternalRegulationConstants.WHERE_DOC_TYPE, col)) {
						sb.append(" and dt.document_type_id = " + val + " ");
					}
					
					else if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						//sb.append(" and UPPER(r.name_in) like UPPER('%" + val + "%')");
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
    	
    	StringBuilder sb = new StringBuilder();
		sb.append(" SELECT count(1) ");
		sb.append("   FROM wo_mst_regulation r  ");
		sb.append("        LEFT JOIN wo_mst_document_type dt on r.document_type_id = dt.document_type_id  ");
		sb.append("        LEFT JOIN wo_mst_document_category dc on dc.document_category_id = r.document_category_id  ");
		sb.append("        left join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = r.status ");
		sb.append("  WHERE 1=1 ");
		sb.append("        and r.enabled_flag = 'Y' ");
		sb.append("        and r.JENIS_KETENTUAN = 'KETENTUAN_EKSTERNAL' ");
	    //sb.append("        and r.status = 'DATA_ACTIVE' ");
		
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
    public List<ExternalRegulationFEVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        
        List<ExternalRegulationFEVO> voList = searchDataCriteria(searchCriteria, first, pageSize);

        return voList;
    }
    
    @SuppressWarnings("rawtypes")
    private List<ExternalRegulationFEVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
        
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
    	
        StringBuilder sb = new StringBuilder();
        sb.append(" with queryKetentuanExternal as(");
		sb.append(" SELECT r.regulation_id internalid, r.name_in, ");
		sb.append("        TO_CHAR(r.effective_date, 'dd FMMonth yyyy', 'nls_date_language=indonesian') effective_date, ");
		sb.append("        TO_CHAR(r.expired_date, 'dd FMMonth yyyy', 'nls_date_language=indonesian') expired_date, document_no, r.status, ");
		sb.append("        (select count(1) from wo_log_access a where a.user_id = :userId  and a.access_id = r.regulation_id   and ((a.access_action = '/compliance/pages/externalRegulationFE/externalRegulationFE.faces') OR (a.access_action = '/compliance/pages/externalRegulationFE/externalRegulationFEView.faces')) ) flagIsAccess ");
		
		sb.append("   FROM wo_mst_regulation r  ");
		sb.append("        LEFT JOIN wo_mst_document_type dt on r.document_type_id = dt.document_type_id  ");
		sb.append("        LEFT JOIN wo_mst_document_category dc on dc.document_category_id = r.document_category_id  ");
		sb.append("        left join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = r.status ");
		sb.append("  WHERE 1=1 ");
		sb.append("        and r.enabled_flag = 'Y' ");
		sb.append("        and r.JENIS_KETENTUAN = 'KETENTUAN_EKSTERNAL' ");
		//sb.append("        and r.status = 'DATA_ACTIVE' ");
		sb.append(" and (UPPER(r.name_in) like UPPER('%" + valueSearch + "%') or r.document_no like '%" + valueSearch + "%')");
        sb.append(" ORDER BY published_date DESC, document_no asc ");
        sb.append(" ), queryKetentuanExternal2 as(");
        sb.append(" SELECT r.regulation_id , r.name_in, ");
		sb.append("        TO_CHAR(r.effective_date, 'dd FMMonth yyyy', 'nls_date_language=indonesian') effective_date, ");
		sb.append("        TO_CHAR(r.expired_date, 'dd FMMonth yyyy', 'nls_date_language=indonesian') expired_date, document_no, r.status, ");
		sb.append("        (select count(1) from wo_log_access a where a.user_id = :userId  and a.access_id = r.regulation_id   and ((a.access_action = '/compliance/pages/externalRegulationFE/externalRegulationFE.faces') OR (a.access_action = '/compliance/pages/externalRegulationFE/externalRegulationFEView.faces')) ) flagIsAccess ");
		
		sb.append("   FROM wo_mst_regulation r  ");
		sb.append("        LEFT JOIN wo_mst_document_type dt on r.document_type_id = dt.document_type_id  ");
		sb.append("        LEFT JOIN wo_mst_document_category dc on dc.document_category_id = r.document_category_id  ");
		sb.append("        left join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = r.status ");
		sb.append("  WHERE 1=1 ");
		sb.append("        and r.enabled_flag = 'Y' ");
		sb.append("        and r.JENIS_KETENTUAN = 'KETENTUAN_EKSTERNAL' ");
		sb.append("  and r.regulation_id not in (select internalid from queryKetentuanExternal) ");
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
		sb.append(" ORDER BY published_date DESC, document_no asc ");
		sb.append(" )");
        sb.append(" SELECT * from  queryKetentuanExternal ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT * from  queryKetentuanExternal2 ");
		
        
        Query result = getSession().createSQLQuery(sb.toString());
        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        this.getQuerySetValue(result, searchCriteria);
        
        List resultList = result.getResultList();
        
        List<ExternalRegulationFEVO> vo = new ArrayList<ExternalRegulationFEVO>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                ExternalRegulationFEVO data = new ExternalRegulationFEVO();
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

       
        
        return vo;
    }    
}