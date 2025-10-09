/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.parameter.constant.ParameterDetailConstant;
import com.wo.module.parameter.model.ParameterDetail;

/**
 *
 * @author hendra
 */

@Repository("parameterDetailDao")
public class ParameterDetailDaoImpl extends GenericDAOHibernate<ParameterDetail, Long> implements ParameterDetailDao {

	@SuppressWarnings("rawtypes")
	public ParameterDetail getParameterDetailByParamDtlCode(String paramDetailCode) throws Exception {

		String hql = "FROM ParameterDetail where parameterDtlCode = :paramDetailCode and enabledFlag = 'Y' ";
		Query result = getSession().createQuery(hql);
		result.setParameter("paramDetailCode", paramDetailCode);
		List list = result.getResultList();
		
		if(list.size() > 0) {
			return (ParameterDetail) list.get(0);
		}else {
			return null;
		}
		
		//return (ParameterDetail) result.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<ParameterDetail> getParameterDetailByParamCode(String paramCode) throws Exception {

		String hql = "FROM ParameterDetail where parameterHeader.parameterCode = :paramCode and enabledFlag = 'Y' order by nameIn ASC";
		Query result = getSession().createQuery(hql);
		result.setParameter("paramCode", paramCode);

		return result.getResultList();
	}

	@SuppressWarnings({ "rawtypes", "static-access" })
	private StringBuilder getQueryWhereString(StringBuilder sb,  List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if(searchCriteria != null) {
			for(SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue() != null ? searchVal.getSearchValueAsString() : "";
				
				if(!StringUtils.isBlank(val)) {
					if(StringUtils.equals(ParameterDetailConstant.WHERE_PARAMETER_DETAIL_NAME, col)) {
						if(locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" AND UPPER(pd.name_en) like UPPER('%"+val+"%') ");
						}else {
							sb.append(" AND UPPER(pd.name_in) like UPPER('%"+val+"%') ");
						}
					}else if(StringUtils.equals(ParameterDetailConstant.WHERE_PARAMETER_DETAIL, col)) {
						if(locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" AND (UPPER(pd.name_en) like UPPER('%"+val+"%') ");
							sb.append(" OR UPPER(pd.parameter_dtl_code) like UPPER'%"+val+"%') ) ");
						}else {
							sb.append(" AND (UPPER(pd.name_in) like UPPER('%"+val+"%') ");
							sb.append(" OR UPPER(pd.parameter_dtl_code) like UPPER('%"+val+"%') ) ");
						}
					}else if(StringUtils.equals(ParameterDetailConstant.WHERE_PARAMETER_CODE,col)){
						sb.append(" AND pd.parameter_code = '" +val+ "' ");
					}
				}
			}
		}
		
		return sb;
	}
	
	@SuppressWarnings("rawtypes") 
	public List<ParameterDetail> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,String sortField, SortOrder sortOrder) 
			throws Exception {
		 List<ParameterDetail> voList = searchDataCriteria(searchCriteria, first, pageSize);

	     return voList;
	}
	
	@SuppressWarnings("rawtypes")
    private List<ParameterDetail> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
        
        StringBuilder sb = new StringBuilder();
		sb.append(" SELECT pd.parameter_dtl_id"+ 
				"	, pd.parameter_dtl_code" +
				"	, pd.parameter_code" +
				"	, pd.name_in"+
				"	, pd.name_en ");
		sb.append(" FROM wo_mst_parameter_dtl pd INNER JOIN wo_mst_parameter p ON pd.parameter_code = p.parameter_code");
		sb.append(" WHERE 1=1 AND pd.enabled_flag = 'Y' ");
		
		sb = getQueryWhereString(sb, searchCriteria);
        
		sb.append(" ORDER BY pd.parameter_dtl_id DESC ");
        
        
        Query result = getSession().createSQLQuery(sb.toString());
        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        List resultList = result.getResultList();
        
        List<ParameterDetail> vo = new ArrayList<ParameterDetail>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                ParameterDetail data = new ParameterDetail();
                //data.setParameterDtlId(((BigInteger)obj[0]).longValue());
                data.setParameterDtlId(MathUtil.returnIdObjectToLong(obj[0]));
                data.setParameterDtlCode((String)obj[1]);
                data.setParameterCode((String)obj[2]);;
                data.setNameIn((String)obj[3]);
                data.setNameEn((String)obj[4]);
                
                vo.add(data);
            }
        }

        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        return vo;
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
		sb.append(" FROM wo_mst_parameter_dtl pd INNER JOIN wo_mst_parameter p ON pd.parameter_code = p.parameter_code");
		sb.append(" WHERE 1=1 AND pd.enabled_flag = 'Y' ");
       
        sb = getQueryWhereString(sb, searchCriteria);
        
        Query result = getSession().createSQLQuery(sb.toString());
       
        
        return (Number) result.getSingleResult();
    }

	@Override
	public Integer getParameterCodeByParamCodeAndParamDtlCode(String paramCode, String paramDtlCode)
			throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT COUNT(1) FROM wo_mst_parameter_dtl");
		sb.append("	WHERE parameter_code = '"+paramCode+"'");
		sb.append("	AND parameter_dtl_code = '"+paramDtlCode+"'");
		
//		String hql = "FROM ParameterDetail where parameterHeader.parameterCode = :paramCode AND parameterDtlCode = :paramDtlCode";
//		Query result = getSession().createQuery(hql);
//		result.setParameter("paramCode", paramCode);
//		result.setParameter("paramDtlCode", paramDtlCode);
			
		Query result = getSession().createSQLQuery(sb.toString());
		
		Number count = (Number)  result.getSingleResult();
		if(count == null) {	
			count = 0;
		}
		
		return (Integer) count.intValue();
	}

	@SuppressWarnings({ "rawtypes", "static-access" })
	public List<ParameterDetail> getParameterDetailByParamCodeOrdered(String paramCode) throws Exception {

		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT pd.parameter_dtl_id, "
				+ "		pd.parameter_dtl_code, "
				+ "		pd.name_in, "
				+ "		pd.name_en, "
				+ "		pd.parameter_code "
				+ "	FROM wo_mst_parameter_dtl pd "
				+ "		INNER JOIN wo_mst_parameter p "
				+ "			ON pd.parameter_code = p.parameter_code "
				+ "	WHERE p.parameter_code = '" + paramCode + "' ");
		
		
		sb.append(" ORDER BY pd.name_in ASC ");
		
		
		 Query result = getSession().createSQLQuery(sb.toString());
		 List resultList = result.getResultList();
	        
	     List<ParameterDetail> vo = new ArrayList<ParameterDetail>();
	        
	     if(resultList!=null) {
		     for(int i=0; i<resultList.size(); i++) {
		    	 Object[] obj = (Object[]) resultList.get(i);
		         ParameterDetail data = new ParameterDetail();
		         //data.setParameterDtlId(((BigInteger)obj[0]).longValue());
		         data.setParameterDtlId(MathUtil.returnIdObjectToLong(obj[0]));
		         data.setParameterDtlCode((String)obj[1]);
		         data.setNameIn((String)obj[2]);
		         data.setNameEn((String)obj[3]);
		         data.setParameterCode((String)obj[4]);
		                
		         vo.add(data);
		     }
	     }

	     return vo;
	}

	@SuppressWarnings({ "rawtypes", "static-access" })
	@Override
	public List<ParameterDetail> getParameterDetailByTwoParamCode(String paramCode1, String paramCode2) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT pd.parameter_dtl_id, "
				+ "		pd.parameter_dtl_code, "
				+ "		pd.name_in, "
				+ "		pd.name_en, "
				+ "		pd.parameter_code "
				+ "	FROM wo_mst_parameter_dtl pd "
				+ "		INNER JOIN wo_mst_parameter p "
				+ "			ON pd.parameter_code = p.parameter_code "
				+ "	WHERE p.parameter_code = :paramCode1 "
				+ "		OR p.parameter_code = :paramCode2 ");
		
		
		 sb.append(" ORDER BY pd.name_in ASC ");
		
		
		 Query result = getSession().createSQLQuery(sb.toString());
		 
		 result.setParameter("paramCode1", paramCode1);
		 result.setParameter("paramCode2", paramCode2);
		 
		 List resultList = result.getResultList();
	        
	     List<ParameterDetail> vo = new ArrayList<ParameterDetail>();
	        
	     if(resultList!=null) {
		     for(int i=0; i<resultList.size(); i++) {
		    	 Object[] obj = (Object[]) resultList.get(i);
		         ParameterDetail data = new ParameterDetail();
		         //data.setParameterDtlId(((BigInteger)obj[0]).longValue());
		         data.setParameterDtlId(MathUtil.returnIdObjectToLong(obj[0]));
		         data.setParameterDtlCode((String)obj[1]);
		         data.setNameIn((String)obj[2]);
		         data.setNameEn((String)obj[3]);
		         data.setParameterCode((String)obj[4]);
		                
		         vo.add(data);
		     }
	     }

	     return vo;
	}

	@SuppressWarnings({ "rawtypes", "static-access" })
	@Override
	public List<ParameterDetail> getParameterDetailByParamCodeOrDtlCode(String paramCode, String paramDtlCode)
			throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT pd.parameter_dtl_id, " 
				+ "		pd.parameter_dtl_code, "
				+ "		pd.name_in, "
				+ "		pd.name_en, " 
				+ "		pd.parameter_code " 
				+ "	FROM wo_mst_parameter_dtl pd "
				+ "		INNER JOIN wo_mst_parameter p " 
				+ "			ON pd.parameter_code = p.parameter_code "
				+ "	WHERE 1 = 1 "
				+ "		AND p.ENABLED_FLAG = 'Y' ");

		if (paramCode != null && !paramCode.equals("")) {
			sb.append("  AND p.parameter_code = :paramCode ");
		}
		if (paramDtlCode != null && !paramDtlCode.equals("")) {
			sb.append("  AND pd.parameter_dtl_code = :paramDtlCode ");
		}

		
		sb.append(" ORDER BY pd.name_in ASC ");
		

		Query result = getSession().createSQLQuery(sb.toString());

		if (paramCode != null && !paramCode.equals("")) {
			result.setParameter("paramCode", paramCode);
		}
		if (paramDtlCode != null && !paramDtlCode.equals("")) {
			result.setParameter("paramDtlCode", paramDtlCode);
		}
		
		List resultList = result.getResultList();

		List<ParameterDetail> vo = new ArrayList<ParameterDetail>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ParameterDetail data = new ParameterDetail();
				data.setParameterDtlId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setParameterDtlCode(obj[1] != null ? (String) obj[1] : null);
				data.setNameIn(obj[2] != null ? (String) obj[2] : null);
				data.setNameEn(obj[3] != null ? (String) obj[3] : null);
				data.setParameterCode(obj[4] != null ? (String) obj[4] : null);

				vo.add(data);
			}
		}

		return vo;
	}
	
	@SuppressWarnings("unchecked")
	public List<ParameterDetail> getParameterDetailByParamCodeOrderById(String paramCode) throws Exception {

		String hql = "FROM ParameterDetail where parameterHeader.parameterCode = :paramCode and enabledFlag = 'Y' order by parameterDtlId ASC";
		Query result = getSession().createQuery(hql);
		result.setParameter("paramCode", paramCode);

		return result.getResultList();
	}
	
	@SuppressWarnings("rawtypes")
	public ParameterDetail getParameterDetailByParamDtlNameIn(String paramDetailNameIn) throws Exception {

		String hql = "FROM ParameterDetail where nameIn = :paramDetailNameIn and enabledFlag = 'Y' ";
		Query result = getSession().createQuery(hql);
		result.setParameter("paramDetailNameIn", paramDetailNameIn);
		List list = result.getResultList();
		
		if(list.size() > 0) {
			return (ParameterDetail) list.get(0);
		}else {
			return null;
		}
		
		//return (ParameterDetail) result.getSingleResult();
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	@Override
	public String getParamDtlNameByParamDtlCode(String parameterDtlCode) {
		StringBuilder sb = new StringBuilder();
		String paramName = "";
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		sb.append(" SELECT wmpd.NAME_IN ");
		sb.append(" 	,wmpd.NAME_EN ");
		sb.append(" FROM WO_MST_PARAMETER_DTL wmpd ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" 	AND wmpd.PARAMETER_DTL_CODE = :paramDtlCode ");

		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("paramDtlCode", parameterDtlCode);

		List<Object[]> result = query.getResultList();
		
		if (!result.isEmpty()) {
			for (Object[] obj : result) {
				if (locale != null && locale.equals(locale.ENGLISH)) {
					paramName = obj[1] != null ? (String) obj[1] : null;
				} else {
					paramName = obj[0] != null ? (String) obj[0] : null;
				}
			}
		}
		
		return paramName;
	}
}
