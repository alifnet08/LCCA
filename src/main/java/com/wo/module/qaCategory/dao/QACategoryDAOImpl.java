package com.wo.module.qaCategory.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.qaCategory.constant.QACategoryConstants;
import com.wo.module.qaCategory.model.QACategory;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

@Repository("qaCategoryDAO")
public class QACategoryDAOImpl extends GenericDAOHibernate<QACategory, Long> implements QACategoryDAO {

	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
	@Qualifier("parameterDetailService")
	private ParameterDetailService parameterDetailService;

	@SuppressWarnings({ "rawtypes" })
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue() != null ? searchVal.getSearchValueAsString() : "";
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(QACategoryConstants.WHERE_CATEGORY, col)) {
						sb.append(" and  C.QNA_CATEGORY_CODE = :code ");
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

				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(QACategoryConstants.WHERE_CATEGORY, col)) {
						query.setParameter("code", val);
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

		StringBuilder sb = new StringBuilder();
		/*
		 * sb.append(" select count(1) ");
		 * sb.append(" FROM WO_MST_QNA_CATEGORY_MAP C ");
		 * sb.append(" WHERE 1=1 and C.enabled_flag = 'Y' ");
		 */
		
		sb.append("	SELECT COUNT(1) ");
		sb.append("   FROM (SELECT DISTINCT C.QNA_CATEGORY_CODE ");
		sb.append("	          FROM WO_MST_QNA_CATEGORY_MAP C ");
		sb.append("	               INNER JOIN WO_MST_PARAMETER_DTL P ON C.QNA_CATEGORY_CODE = P.PARAMETER_DTL_CODE	 ");
		sb.append("	         WHERE 1 = 1 ");
		sb.append("                AND C.ENABLED_FLAG = 'Y') C ");
		sb.append("          WHERE 1=1 ");

		sb = getQueryWhereString(sb, searchCriteria);
		Query result = getSession().createSQLQuery(sb.toString());
		this.getQuerySetValue(result, searchCriteria);
		
		return (Number) result.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<QACategory> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<QACategory> voList = searchDataCriteria(searchCriteria, first, pageSize);

		return voList;
	}

	@SuppressWarnings("rawtypes")
	private List<QACategory> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT DISTINCT C.QNA_CATEGORY_CODE, '' TEMP ");
		sb.append("	  FROM WO_MST_QNA_CATEGORY_MAP C	 ");
		sb.append("	       INNER JOIN WO_MST_PARAMETER_DTL P ON C.QNA_CATEGORY_CODE = P.PARAMETER_DTL_CODE	 ");
		sb.append("	 WHERE 1 = 1 AND C.ENABLED_FLAG = 'Y'	 ");

		sb = getQueryWhereString(sb, searchCriteria);

		Query result = getSession().createSQLQuery(sb.toString());
		this.getQuerySetValue(result, searchCriteria);
		
		result.setFirstResult(first);
		result.setMaxResults(pageSize);
		List resultList = result.getResultList();

		List<QACategory> vo = new ArrayList<QACategory>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				//String obj = (String) resultList.get(i);
				Object[] obj = (Object[])resultList.get(i);
				QACategory data = new QACategory();
				
				ParameterDetail param = parameterDetailService.getParameterDetailByParamDtlCode(
						obj[0] != null ? (String) obj[0] : null);
				
				data.setQnaCategoryCode(param);

				vo.add(data);
			}
		}

		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		return vo;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<QACategory> searchCategoryListByCode(String categoryCode) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT C.QNA_CATEGORY_MAP_ID, C.QNA_CATEGORY_CODE, C.USER_ID,	 ");
		sb.append(" 	   C.CREATED_BY, C.CREATION_DATE ");
		sb.append("	  FROM WO_MST_QNA_CATEGORY_MAP C	 ");
		sb.append("	       INNER JOIN WO_MST_USER QUSER ON C.USER_ID = QUSER.USER_ID	 ");
		sb.append("	       INNER JOIN WO_MST_PARAMETER_DTL P	 ");
		sb.append("	          ON C.QNA_CATEGORY_CODE = P.PARAMETER_DTL_CODE	 ");
		sb.append("	 WHERE     1 = 1	 ");
		sb.append("	       AND C.ENABLED_FLAG = 'Y'	 ");

		if (categoryCode != null && !StringUtils.isEmpty(categoryCode)) {
			sb.append("	       AND C.QNA_CATEGORY_CODE = :categoryCode ");
		}
		
		sb.append("  ORDER BY QUSER.NAME ASC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		if (categoryCode != null && !StringUtils.isEmpty(categoryCode)) {
			query.setParameter("categoryCode", categoryCode);
		}
		
		List resultList = query.getResultList();

		List<QACategory> vo = new ArrayList<QACategory>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				QACategory data = new QACategory();
				
				data.setQnaCategoryMapId(MathUtil.returnIdObjectToLong(obj[0]));
				
				ParameterDetail param = parameterDetailService.getParameterDetailByParamDtlCode(
						obj[1] != null ? (String) obj[1] : null);
				data.setQnaCategoryCode(param);

				Long userId = (MathUtil.returnIdObjectToLong(obj[2]));
				User user = userService.findById(userId);
				
				data.setUser(user);
				data.setCreatedBy(obj[3] != null ? (String) obj[3] : null);
				data.setCreationDate(obj[4] != null ? (Timestamp) obj[4] : null);

				vo.add(data);
			}
		}
		
		return vo;
	}

	@Override
	public Integer duplicate(String categoryCode, Long userId) {
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT COUNT(1)	 ");
		sb.append("	  FROM WO_MST_QNA_CATEGORY_MAP C	 ");
		sb.append("	       INNER JOIN WO_MST_USER QUSER ON C.USER_ID = QUSER.USER_ID	 ");
		sb.append("	       INNER JOIN WO_MST_PARAMETER_DTL P	 ");
		sb.append("	          ON C.QNA_CATEGORY_CODE = P.PARAMETER_DTL_CODE	 ");
		sb.append("	 WHERE     1 = 1	 ");
		sb.append("	       AND C.ENABLED_FLAG = 'Y'	 ");
		
		if (categoryCode != null && !StringUtils.isEmpty(categoryCode)) {
			sb.append("	       AND C.QNA_CATEGORY_CODE = :categoryCode ");
		}
		
		if (userId != null && userId > 0) {
			sb.append("        AND C.USER_ID = :userId ");
		}

		Query result = getSession().createSQLQuery(sb.toString());
		
		if (categoryCode != null && !StringUtils.isEmpty(categoryCode)) {
			result.setParameter("categoryCode", categoryCode);
		}
		
		if (userId != null && userId > 0) {
			result.setParameter("userId", userId);
		}
		
		Integer dup = ((Number) result.getSingleResult()).intValue();
		return dup;
	}

}