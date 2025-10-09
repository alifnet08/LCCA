package com.wo.module.mstProvinceLocation.dao;

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
import com.wo.module.mstProvince.constants.MstProvinceConstants;
import com.wo.module.mstProvince.model.MstProvince;
import com.wo.module.mstProvince.model.MstProvinceLocation;
import com.wo.module.user.model.User;


@Repository("mstProvinceLocationDAO")
public class MstProvinceLocationDAOImpl extends GenericDAOHibernate<MstProvinceLocation, Long>
	implements MstProvinceLocationDAO {

	@SuppressWarnings("rawtypes")
	@Override
	public List<MstProvinceLocation> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<MstProvinceLocation> vo = searchDataCriteria(searchCriteria, first, pageSize);
		
		return vo;
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
		sb.append(" SELECT COUNT(1) FROM (SELECT  P.PROVINCE, P.BRANCH_CODE	 ");
		sb.append(" 	    FROM WO_MST_PROVINCE P	");
		sb.append(" 	   WHERE 1 = 1 AND P.ENABLED_FLAG = 'Y'	");
		
		this.getQueryWhereString(sb, searchCriteria);
		
		sb.append(" GROUP BY P.PROVINCE, P.BRANCH_CODE)	");
		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					
					if (StringUtils.equals(MstProvinceConstants.SEARCH_BY_PROVINCE, col)) {
						sb.append(" and UPPER(P.PROVINCE) LIKE UPPER(:province) ");
					}
				}
			}
		}
	}
	

	@SuppressWarnings("rawtypes")
	private void getQuerySetValue(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();

				if (!StringUtils.isBlank(val)) {
					
					
					if (StringUtils.equals(MstProvinceConstants.SEARCH_BY_PROVINCE, col)) {
						query.setParameter("province", "%" + val + "%");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private List<MstProvinceLocation> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" 	SELECT  P.PROVINCE, P.BRANCH_CODE	");
		sb.append(" 	    FROM WO_MST_PROVINCE P	");
		sb.append(" 	   WHERE 1 = 1 AND P.ENABLED_FLAG = 'Y'	");
		
		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" GROUP BY P.PROVINCE,P.BRANCH_CODE	ORDER BY P.PROVINCE ASC	");
		Query query = getSession().createSQLQuery(sb.toString());
		
		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();
		
		List<MstProvinceLocation> vo = new ArrayList<MstProvinceLocation>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++)  {
				Object[] obj = (Object[]) resultList.get(i);
				//String obj = (String) resultList.get(i);
				MstProvinceLocation data = new MstProvinceLocation();
				data.setProvince(obj[0]!=null?(String)obj[0]:null);
				vo.add(data);
			}
		}

		//query.setFirstResult(first);
		//query.setMaxResults(pageSize);
		
		return vo;
	}

	 @SuppressWarnings("rawtypes")
	 public MstProvinceLocation getProvinceLocationByProvince(String province)  {
			 if(province != null) {
				String hql = "FROM MstProvinceLocation where UPPER(province) = UPPER(:province)";
				Query result = getSession().createQuery(hql);
				result.setParameter("province", province);
				List list = result.getResultList();
				
				if(list.size() > 0) {
					return (MstProvinceLocation) list.get(0);
				}else {
					return null;
				}
				
			 } else {
				 return null;
			 }
		}

	
}