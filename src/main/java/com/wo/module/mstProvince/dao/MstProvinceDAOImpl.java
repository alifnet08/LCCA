package com.wo.module.mstProvince.dao;

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
import com.wo.module.user.model.User;

@Repository("mstProvinceDAO")
public class MstProvinceDAOImpl extends GenericDAOHibernate<MstProvince, Long>
	implements MstProvinceDAO {

	@SuppressWarnings("rawtypes")
	@Override
	public List<MstProvince> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<MstProvince> vo = searchDataCriteria(searchCriteria, first, pageSize);
		
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
		sb.append(" 	SELECT COUNT(1) ");
		sb.append(" 	    FROM WO_MST_PROVINCE P	");
		sb.append(" 	   WHERE 1 = 1 AND P.ENABLED_FLAG = 'Y'	");
		
		this.getQueryWhereString(sb, searchCriteria);

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
					if (StringUtils.equals(MstProvinceConstants.SEARCH_BY_BRANCH_CODE, col)) {
						sb.append(" and UPPER(P.BRANCH_CODE) LIKE UPPER(:branchCode) ");
					}
					
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
					if (StringUtils.equals(MstProvinceConstants.SEARCH_BY_BRANCH_CODE, col)) {
						query.setParameter("branchCode", "%" + val + "%");
					}
					
					if (StringUtils.equals(MstProvinceConstants.SEARCH_BY_PROVINCE, col)) {
						query.setParameter("province", "%" + val + "%");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private List<MstProvince> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" 	SELECT P.PROVINCE_ID, P.PROVINCE, P.BRANCH_CODE	");
		sb.append(" 	    FROM WO_MST_PROVINCE P	");
		sb.append(" 	   WHERE 1 = 1 AND P.ENABLED_FLAG = 'Y'	");
		
		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" 	ORDER BY P.BRANCH_CODE ASC	");
		Query query = getSession().createSQLQuery(sb.toString());
		
		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();
		
		List<MstProvince> vo = new ArrayList<MstProvince>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++)  {
				Object[] obj = (Object[]) resultList.get(i);
				MstProvince data = new MstProvince();
				
				data.setProvinceId(MathUtil.returnIdObjectToLong(obj[0]));
				data.setProvince((String) obj[1]);
				data.setBranchCode((String) obj[2]);
				
				vo.add(data);
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		return vo;
	}

	@Override
	public Integer countBranchCodeDuplicate(String branchCode) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" 	SELECT COUNT(*) ");
		sb.append(" 	    FROM WO_MST_PROVINCE P	");
		sb.append(" 	   WHERE 1 = 1 AND P.ENABLED_FLAG = 'Y'	");
		sb.append("          AND UPPER(P.BRANCH_CODE) = UPPER('" + branchCode + "') ");
		
		Query query = getSession().createSQLQuery(sb.toString());

		Number results = (Number) query.getSingleResult();
		if (results == null) {
			results = 0;
		}

		return results.intValue();
	}
	
	 @SuppressWarnings("rawtypes")
	 public MstProvince getProvinceByProvinceName(String name)  {
			 if(name != null) {
				String hql = "FROM MstProvince where UPPER(province) = UPPER(:name)";
				Query result = getSession().createQuery(hql);
				result.setParameter("name", name);
				List list = result.getResultList();
				
				if(list.size() > 0) {
					return (MstProvince) list.get(0);
				}else {
					return null;
				}
				
			 } else {
				 return null;
			 }
		}
}