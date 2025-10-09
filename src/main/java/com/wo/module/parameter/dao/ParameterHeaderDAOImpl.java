package com.wo.module.parameter.dao;

//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

//import com.mysql.cj.x.protobuf.MysqlxNotice.SessionStateChanged.Parameter;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.parameter.model.ParameterHeader;

@Repository("parameterHeaderDao")
public class ParameterHeaderDAOImpl extends GenericDAOHibernate<ParameterHeader, Long> implements ParameterHeaderDao{

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
							sb.append(" AND name_en LIKE '%"+val+"%' ");
						} else {
							sb.append(" AND name_in LIKE '%"+val+"%' ");
						}
					}
				}
			}
		}
		
		return sb;
    }
	
	@SuppressWarnings("rawtypes")
	@Override
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
		sb.append(" FROM wo_mst_parameter_dtl pd LEFT JOIN wo_mst_parameter ON pd.parameter_code = p.parameter_code");
		sb.append(" WHERE 1=1 ");
       
        sb = getQueryWhereString(sb, searchCriteria);
        
        Query result = getSession().createSQLQuery(sb.toString());
       
        
        return (Number) result.getSingleResult();
    }
	
	@SuppressWarnings("rawtypes")
    public List<ParameterHeader> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,String sortField, SortOrder sortOrder) 
            throws Exception {
        
        List<ParameterHeader> voList = searchDataCriteria(searchCriteria, first, pageSize);

        return voList;
    }
    
    @SuppressWarnings("rawtypes")
    private List<ParameterHeader> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
        
        StringBuilder sb = new StringBuilder();
		sb.append(" SELECT parameter_id "+
				"	,parameter_code "+ 
				"	,name_in "+ 
				"	,name_en ");
		sb.append(" FROM wo_mst_parameter_dtl pd LEFT JOIN wo_mst_parameter p ON pd.parameter_code = p.parameter_code");
		sb.append(" WHERE 1=1 ");
        
        sb = getQueryWhereString(sb, searchCriteria);
        sb.append(" ORDER BY parameter_id DESC ");
        
        
        Query result = getSession().createSQLQuery(sb.toString());
        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        List resultList = result.getResultList();
        
        List<ParameterHeader> vo = new ArrayList<ParameterHeader>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                ParameterHeader data = new ParameterHeader();
                //data.setParameterId(((BigInteger)obj[0]).longValue());
                data.setParameterId(MathUtil.returnIdObjectToLong(obj[0]));
                data.setParameterCode((String)obj[1]);
                data.setNameIn((String)obj[2]);
                data.setNameEn((String)obj[3]);
                
                vo.add(data);
            }
        }

        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        return vo;
    }
    
	@SuppressWarnings("rawtypes")
	public List<ParameterHeader> getListParameterAll() throws Exception{
		
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT parameter_id "+
				"	,parameter_code "+ 
				"	,name_in "+ 
				"	,name_en ");
		sb.append(" FROM wo_mst_parameter");
		sb.append(" WHERE 1=1 ");
        
        sb.append(" ORDER BY parameter_code ASC ");
        
        Query result = getSession().createSQLQuery(sb.toString());
        List resultList = result.getResultList();
        
        List<ParameterHeader> voList = new ArrayList<ParameterHeader>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                ParameterHeader data = new ParameterHeader();
                //data.setParameterId(((BigInteger)obj[0]).longValue());
                data.setParameterId(MathUtil.returnIdObjectToLong(obj[0]));
                data.setParameterCode((String)obj[1]);
                data.setNameIn((String)obj[2]);
                data.setNameEn((String)obj[3]);
                
                voList.add(data);
            }
        }
        
        return voList;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ParameterHeader getListParameterDataAll() throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT parameter_id "+
				"	,parameter_code "+ 
				"	,name_in "+ 
				"	,name_en ");
		sb.append(" FROM wo_mst_parameter");
		sb.append(" WHERE 1=1 ");
        
        sb.append(" ORDER BY parameter_code ASC ");
        
        Query result = getSession().createSQLQuery(sb.toString());
        
        List list = result.getResultList();
		
		if(list.size() > 0) {
			return (ParameterHeader) list.get(0);
		}else {
			return null;
		}
		
		//return (ParameterHeader) result.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ParameterHeader getParameterHeaderByParamCode(String paramCode) throws Exception {
		String hql = "FROM ParameterHeader where parameterCode = :paramCode";
		Query result = getSession().createQuery(hql);
		result.setParameter("paramCode", paramCode);
		
		List list = result.getResultList();
		
		if(list.size() > 0) {
			return (ParameterHeader) list.get(0);
		}else {
			return null;
		}
		
		//return (ParameterHeader) result.getSingleResult();
	}

	@SuppressWarnings({ "rawtypes", "static-access" })
	@Override
	public List<ParameterHeader> getListParameterAllOrderByName() throws Exception {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT parameter_id "+
				"	,parameter_code "+ 
				"	,name_in "+ 
				"	,name_en ");
		sb.append(" FROM wo_mst_parameter");
		sb.append(" WHERE 1=1 ");
        
		if (locale != null && locale.equals(locale.ENGLISH)) {
			sb.append(" ORDER BY name_en ASC ");
		} else {
			sb.append(" ORDER BY name_in ASC ");	
		}
		
        
        
        Query result = getSession().createSQLQuery(sb.toString());
        List resultList = result.getResultList();
        
        List<ParameterHeader> voList = new ArrayList<ParameterHeader>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                ParameterHeader data = new ParameterHeader();
                //data.setParameterId(((BigInteger)obj[0]).longValue());
                data.setParameterId(MathUtil.returnIdObjectToLong(obj[0]));
                data.setParameterCode((String)obj[1]);
                data.setNameIn((String)obj[2]);
                data.setNameEn((String)obj[3]);
                
                voList.add(data);
            }
        }
        
        return voList;
	}
}