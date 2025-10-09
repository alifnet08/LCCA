/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.user.dao;

//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
//import com.wo.module.parameter.model.ParameterDetail;
//import com.wo.module.reportType.model.ReportType;
import com.wo.module.user.constant.UserConstants;
import com.wo.module.user.model.Branch;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;

/**
 *
 * @author hendra
 */
@Repository("userDao")
public class UserDaoImpl extends GenericDAOHibernate<User, Long> implements UserDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(UserDaoImpl.class);

	@SuppressWarnings({ "rawtypes", "unused" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(UserConstants.SEARCH_BY_NIK, col)) {
						sb.append(" and UPPER(u.nik) LIKE UPPER(:nik) ");
					}

					if (StringUtils.equals(UserConstants.SEARCH_BY_NAME, col)) {
						sb.append(" and UPPER(u.name) LIKE UPPER(:name) ");
					}

					if (StringUtils.equals(UserConstants.SEARCH_BY_RESPONSIBILITY_ID, col)) {
						if ("Empty".equals(val)) {
							sb.append(" and u.responsibility_id is null ");
						} else {
							sb.append(" and u.responsibility_id = :responsibilityId ");
						}

					}
					if(StringUtils.equals(UserConstants.SEARCH_BY_DIVISION, col)) {
						sb.append(" and UPPER(u.division_name) LIKE UPPER(:division) ");
					}
					if(StringUtils.equals(UserConstants.SEARCH_BY_EMAIL, col)) {
						sb.append(" and UPPER(u.email) LIKE UPPER(:email)");
					}
					if(StringUtils.equals(UserConstants.SEARCH_BY_POSITION, col)) {
						sb.append("	and UPPER(u.position_name) LIKE UPPER(:position)");
					}

					/*
					 * if (StringUtils.equals(UserConstants.SEARCH_BY_NAME, col)) { if (locale !=
					 * null && locale.equals(locale.ENGLISH)) { sb.append(" and name LIKE :name ");
					 * } else { sb.append(" and name LIKE :name "); } }
					 */
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
					if (StringUtils.equals(UserConstants.SEARCH_BY_NIK, col)) {
						query.setParameter("nik", "%" + val + "%");
					}

					if (StringUtils.equals(UserConstants.SEARCH_BY_NAME, col)) {
						query.setParameter("name", "%" + val + "%");
					}

					if (StringUtils.equals(UserConstants.SEARCH_BY_RESPONSIBILITY_ID, col)) {
						if ("Empty".equals(val)) {

						} else {
							query.setParameter("responsibilityId", "" + val + "");
						}
					}
					if(StringUtils.equals(UserConstants.SEARCH_BY_DIVISION, col)) {
						query.setParameter("division", "%" + val + "%");
					}
					if(StringUtils.equals(UserConstants.SEARCH_BY_EMAIL, col)) {
						query.setParameter("email", "%" + val + "%");
					}
					if(StringUtils.equals(UserConstants.SEARCH_BY_POSITION, col)) {
						query.setParameter("position", "%" + val + "%");
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
		sb.append(" select count(1) ");
		sb.append(" from wo_mst_user u ");
		sb.append(" left join wo_mst_responsibility r on u.responsibility_id = r.responsibility_id ");
		sb.append(" where 1=1 ");
		sb.append(" and u.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<User> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {

		List<User> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<User> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select u.user_id, u.nik, u.name, u.email, u.division_name, r.name as role_name, u.status, u.puk_nik, u.position_name");
		sb.append(" from wo_mst_user u ");
		sb.append(" left join wo_mst_responsibility r on u.responsibility_id = r.responsibility_id ");
		sb.append(" where 1=1 ");
		sb.append(" and u.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY u.user_id DESC ");
		

		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<User> vo = new ArrayList<User>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				User data = new User();
				//data.setUserId(((BigInteger) obj[0]).longValue());
				data.setUserId(MathUtil.returnIdObjectToLong(obj[0]));
				data.setNik((String) obj[1]);
				data.setName((String) obj[2]);
				data.setEmail((String) obj[3]);
				data.setDivisionName((String) obj[4]);
				data.setRoleName((String) obj[5]);
				data.setStatus((String) obj[6]);
				data.setPukNik((String) obj[7]);
				data.setPositionName((String) obj[8]);

				vo.add(data);
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
	}
	
	
	 @SuppressWarnings("rawtypes")
	public List<Division> getAllDivision() {
		 StringBuilder sb = new StringBuilder();
			sb.append(" select distinct u.division_id, u.division_name  ");
			sb.append(" from wo_mst_user u ");
			sb.append(" where 1=1 ");
			sb.append(" and u.division_id <> 0 ");
			sb.append(" and u.enabled_flag = 'Y' ");
			sb.append(" order by u.division_name ");

			Query query = getSession().createSQLQuery(sb.toString());

			List resultList = query.getResultList();

			List<Division> vo = new ArrayList<Division>();

			if (resultList != null) {
				for (int i = 0; i < resultList.size(); i++) {
					Object[] obj = (Object[]) resultList.get(i);
					Division data = new Division();
					if(obj[0] != null) {
						//data.setDivisionId(((BigInteger) obj[0]).longValue());
						data.setDivisionId(MathUtil.returnIdObjectToLong(obj[0]));
					}
	                
	                data.setDivisionName((String) obj[1]);
					vo.add(data);
				}
			}

			return vo;
	    }
	 
	 	@SuppressWarnings("rawtypes")
		public List<Branch> getBranchByDivisionId(Long divisionId) {
			 StringBuilder sb = new StringBuilder();
				sb.append(" select distinct u.BRANCH_CODE, u.BRANCH_NAME  ");
				sb.append(" from wo_mst_user u ");
				sb.append(" where 1=1 ");
				if(divisionId!=null) {
				sb.append(" and u.division_id = :divisionId");
				}
				sb.append(" and u.enabled_flag = 'Y' ");
				sb.append(" order by u.BRANCH_CODE ");

				Query query = getSession().createSQLQuery(sb.toString());
				if(divisionId!=null) {
				query.setParameter("divisionId", divisionId);
				}

				List resultList = query.getResultList();

				List<Branch> vo = new ArrayList<Branch>();

				if (resultList != null) {
					for (int i = 0; i < resultList.size(); i++) {
						Object[] obj = (Object[]) resultList.get(i);
						Branch data = new Branch();
						if(obj[0] != null) {
							data.setBranchCode((String)obj[0]);
						}
		                
		                data.setBranchName((String) obj[1]);
						vo.add(data);
					}
				}

				return vo;
		    }
	 	
	 	@SuppressWarnings("rawtypes")
		public Branch getBranchByBranchCode(String branchCode) {
			 StringBuilder sb = new StringBuilder();
				sb.append(" select distinct u.BRANCH_CODE, u.BRANCH_NAME, u.SUB_BRANCH_NAME  ");
				sb.append(" from wo_mst_user u ");
				sb.append(" where 1=1 ");
				if(branchCode!=null) {
				sb.append(" and u.BRANCH_CODE = :branchCode");
				}
				sb.append(" and u.enabled_flag = 'Y' ");
				sb.append(" order by u.BRANCH_CODE ");

				Query query = getSession().createSQLQuery(sb.toString());
				
				if(branchCode!=null) {
				query.setParameter("branchCode", branchCode);
				}

				List resultList = query.getResultList();

				Branch data = new Branch();

				if (resultList != null) {
					for (int i = 0; i < resultList.size(); i++) {
						Object[] obj = (Object[]) resultList.get(i);
						
						
						data.setBranchCode(obj[0] != null?(String)obj[0]:null);
		                data.setBranchName(obj[1] != null?(String) obj[1]:null);
		                data.setSubBranchName(obj[2] != null?(String) obj[2]:null);
						
					}
				}

				return data;
		    }
		 
	/*
	 * public User getUserByNik(String nik) { StringBuilder sb = new
	 * StringBuilder(); sb.append(" select u.user_id, u.nik ");
	 * sb.append(" from wo_mst_user u "); sb.append(" where 1=1 ");
	 * sb.append(" and u.nik = :nik "); sb.append(" and u.enabled_flag = 'Y' ");
	 * 
	 * Query query = getSession().createSQLQuery(sb.toString());
	 * 
	 * query.setParameter("nik", nik);
	 * 
	 * List resultList = query.getResultList();
	 * 
	 * List<User> vo = new ArrayList<User>();
	 * 
	 * if (resultList != null) { for (int i = 0; i < resultList.size(); i++) {
	 * Object[] obj = (Object[]) resultList.get(i); User data =
	 * findById(((BigInteger) obj[0]).longValue());
	 * 
	 * return data; } }
	 * 
	 * return null; }
	 */

	 @SuppressWarnings("rawtypes")
	public User getUserByNik(String nik)  {
		 if(nik != null) {
			String hql = "FROM User where nik = :nik";
			Query result = getSession().createQuery(hql);
			result.setParameter("nik", nik);
			List list = result.getResultList();
			
			if(list.size() > 0) {
				return (User) list.get(0);
			}else {
				return null;
			}
			
		 } else {
			 return null;
		 }
	}
	 
	 public List<String> getDivisionByDirectorate(String directorate)  {
			
			String sql = "select distinct division_name from wo_mst_user where division_name is not null and enabled_flag = 'Y' and directorate_name like :directorateName ";
			Query query = getSession().createSQLQuery(sql);
			query.setParameter("directorateName", directorate);
			
			List resultList = query.getResultList();
			
			List<String> vo = new ArrayList<String>();

			if (resultList != null) {
				for (int i = 0; i < resultList.size(); i++) {
					Object obj = (Object) resultList.get(i);
					
					if(obj != null) {
						vo.add((String)obj);
					}
	                
				}
			}
			
		return vo;
	}
	 
	 public List<String> getAllDirectorate()  {
		
			String sql = "select distinct directorate_name from wo_mst_user where directorate_name is not null and enabled_flag = 'Y' ";
			Query query = getSession().createSQLQuery(sql);
			
			List resultList = query.getResultList();
			
			List<String> vo = new ArrayList<String>();

			if (resultList != null) {
				for (int i = 0; i < resultList.size(); i++) {
					Object obj = (Object) resultList.get(i);
					
					if(obj != null) {
						vo.add((String)obj);
					}
	                
				}
			}
			
		return vo;
	}
	 
	 @SuppressWarnings("rawtypes")
	 public User getUserByUserLogin(String userLogin)  {
			 if(userLogin != null) {
				String hql = "FROM User where SUBSTR(nik,-5) = SUBSTR(:nik,-5)";
				Query result = getSession().createQuery(hql);
				result.setParameter("nik", userLogin);
				List list = result.getResultList();
				
				if(list.size() > 0) {
					return (User) list.get(0);
				}else {
					return null;
				}
				
			 } else {
				 return null;
			 }
		}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllUser() throws Exception{
		 String hql = "FROM User";
		 Query result = getSession().createQuery(hql);
		 
		 return result.getResultList();
	 }
	
	@SuppressWarnings("rawtypes")
	public String getDivisionNameByDivisionId(Long divisionId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select u.division_name  ");
		sb.append(" from wo_mst_user u ");
		sb.append(" where 1=1 ");
		sb.append(" and u.division_id = :divisionId ");
		sb.append(" and u.enabled_flag = 'Y' ");
		sb.append(" order by u.division_name ");

		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("divisionId", divisionId);
		
		List resultList = query.getResultList();
		
		if(resultList!=null && resultList.size()>0) {
			return  (String)resultList.get(0);
		}else {
			return null;
		}
	}
	
	public String getDivisionNameByUserId(Long userId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select u.division_name  ");
		sb.append(" from wo_mst_user u ");
		sb.append(" where 1=1 ");
		sb.append(" and u.division_id = :divisionId ");
		sb.append(" and u.enabled_flag = 'Y' ");
		sb.append(" order by u.division_name ");

		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("userId", userId);
		
		List resultList = query.getResultList();
		
		if(resultList!=null && resultList.size()>0) {
			return  (String)resultList.get(0);
		}else {
			return null;
		}
	}
	
	
	public void updateDivisionId() {
		StringBuilder sb = new StringBuilder();
		sb.append(" update wo_mst_user u set division_id = (select division_id from wo_mst_division d where d.division_name = u.division_name) where u.division_id is null  ");
		Query query = getSession().createNativeQuery(sb.toString());
		query.executeUpdate();
	}
	
	public void updateEnableFlagToN() {
		StringBuilder sb = new StringBuilder();
		sb.append(" update wo_mst_user u set enabled_flag = 'N' , LAST_UPDATE_DATE = sysdate  where  enabled_flag = 'Y' and CREATED_BY <> 'MANUAL' ");
		Query query = getSession().createNativeQuery(sb.toString());
		query.executeUpdate();
	}
	
	public void updateEnableFlagToY() {
		StringBuilder sb = new StringBuilder();
		sb.append(" update wo_mst_user u set enabled_flag = 'Y' where TRUNC(LAST_UPDATE_DATE) = TRUNC(sysdate) ");
		Query query = getSession().createNativeQuery(sb.toString());
		query.executeUpdate();
	}

	@Override
	public Division findUsedDivisionByDivisionId(Long divId) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT wmu.division_id, wmu.division_name "
				+ "FROM wo_mst_user wmu "
				+ "WHERE wmu.division_id = :divId");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("divId", divId);
		
		Division division = new Division();
		List result = query.getResultList();
		
		if(result.size() > 0) {
			Object[] obj = (Object[]) result.get(0);
			
			division.setDivisionId(MathUtil.returnIdObjectToLong(obj[0]));
			division.setDivisionName((String) obj[1]);
		}
		return division;
	}
	
	@SuppressWarnings("rawtypes")
	public String getBranchNameByBranchCode(String branchCode) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct u.branch_name  ");
		sb.append("   from wo_mst_user u ");
		sb.append("  where 1=1 ");
		sb.append("        and u.branch_code = :branchCode ");
		sb.append("        and u.enabled_flag = 'Y' ");
		sb.append("  order by u.branch_name ");

		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("branchCode", branchCode);
		
		List resultList = query.getResultList();
		
		if(resultList!=null && resultList.size()>0) {
			return  (String)resultList.get(0);
		}else {
			return null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public String getDirectorateByDivisionId(Long divisionId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT DISTINCT usr.DIRECTORATE_NAME ");
		sb.append("   FROM WO_MST_USER usr");
		sb.append("  WHERE 1=1 ");
		sb.append(" 	   AND usr.division_id = :divisionId ");
		sb.append(" 	   AND usr.enabled_flag = 'Y' ");
		sb.append("  ORDER BY usr.DIRECTORATE_NAME ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("divisionId", divisionId);
		
		List resultList = query.getResultList();
		
		if(resultList!=null && resultList.size()>0) {
			return  (String)resultList.get(0);
		}else {
			return null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public String getRegionByBranchCode(String branchCode) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT DISTINCT usr.REGION_NAME  ");
		sb.append(" FROM WO_MST_USER usr ");
		sb.append(" WHERE 1=1 ");
		sb.append(" 	AND usr.BRANCH_CODE = :branchCode ");
		sb.append(" 	AND usr.ENABLED_FLAG = 'Y' ");
		sb.append("	 	AND usr.REGION_NAME LIKE 'REGIONAL%' ");
		sb.append(" ORDER BY usr.REGION_NAME ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("branchCode", branchCode);
		
		List resultList = query.getResultList();
		
		if(resultList != null && resultList.size()>0) {
			return (String)resultList.get(0);
		}else {
			return null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<Branch> getAllBranch() {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct u.BRANCH_CODE, u.BRANCH_NAME  ");
		sb.append(" from wo_mst_user u ");
		sb.append(" where 1=1 ");
		sb.append(" and u.enabled_flag = 'Y' ");
		sb.append(" order by u.BRANCH_CODE ");

		Query query = getSession().createSQLQuery(sb.toString());

		List resultList = query.getResultList();

		List<Branch> vo = new ArrayList<Branch>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				Branch data = new Branch();
				if(obj[0] != null) {
					data.setBranchCode((String)obj[0]);
				}
                
                data.setBranchName((String) obj[1]);
				vo.add(data);
			}
		}

		return vo;
	}

	@Override
	public String getSubBranchNameByBranchCode(String branchCode) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT DISTINCT usr.SUB_BRANCH_NAME  ");
		sb.append(" FROM WO_MST_USER usr ");
		sb.append(" WHERE 1=1 ");
		sb.append(" 	AND usr.BRANCH_CODE = :branchCode ");
		sb.append(" 	AND usr.ENABLED_FLAG = 'Y' ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("branchCode", branchCode);
		
		List resultList = query.getResultList();
		
		if(resultList != null && resultList.size()>0) {
			return (String)resultList.get(0);
		}else {
			return null;
		}
	}
		
}
