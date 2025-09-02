/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.responsibility.dao;

//import java.math.BigDecimal;
//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.menu.dao.MenuDao;
import com.wo.module.responsibility.constant.ResponsibilityConstants;
import com.wo.module.responsibility.model.Responsibility;
import com.wo.module.responsibility.vo.ResponsibilityDtlVO;

/**
 *
 * @author hendra
 */
@Repository("responsibilityDao")
public class ResponsibilityDaoImpl extends GenericDAOHibernate<Responsibility, Long> implements ResponsibilityDao {
	private static Logger logger = Logger.getLogger(ResponsibilityDaoImpl.class);
	@Autowired
	@Qualifier("menuDao")
	private MenuDao menuDao;
	
	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ResponsibilityConstants.SEARCH_BY_NAME, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and upper(name) LIKE upper(:name) ");
						} else {
							sb.append(" and upper(name) LIKE upper(:name) ");
						}
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
					if (StringUtils.equals(ResponsibilityConstants.SEARCH_BY_NAME, col)) {
						query.setParameter("name", "%" + val + "%");
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
		sb.append(" from wo_mst_responsibility ");
		sb.append(" where 1=1 ");
		sb.append(" and enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<Responsibility> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<Responsibility> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<Responsibility> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first,
			int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select responsibility_id, name ");
		sb.append(" from wo_mst_responsibility ");
		sb.append(" where 1=1 ");
		sb.append(" and enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY responsibility_id DESC ");
		

		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<Responsibility> vo = new ArrayList<Responsibility>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				Responsibility data = new Responsibility();
				//data.setResponsibilityId(((BigInteger) obj[0]).longValue());
				data.setResponsibilityId(MathUtil.returnIdObjectToLong(obj[0]));
				data.setName((String) obj[1]);

				vo.add(data);
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	public List<Responsibility> getAllResponsibility() {
		StringBuilder sb = new StringBuilder();
		sb.append(" select responsibility_id, name ");
		sb.append(" from wo_mst_responsibility ");
		sb.append(" where 1=1 ");
		sb.append(" and enabled_flag = 'Y' ");

		sb.append(" ORDER BY responsibility_id DESC ");

		Query query = getSession().createSQLQuery(sb.toString());

		List resultList = query.getResultList();

		List<Responsibility> vo = new ArrayList<Responsibility>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				Responsibility data = new Responsibility();
				//data.setResponsibilityId(((BigInteger) obj[0]).longValue());
				data.setResponsibilityId(MathUtil.returnIdObjectToLong(obj[0]));
				data.setName((String) obj[1]);
				vo.add(data);
			}
		}

		return vo;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResponsibilityDtlVO> searchResponsibilityMenuAllMenu(ResponsibilityDtlVO responsibilityMenuVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct menu.menu_id, menu.name_in as menu_name_in, ");
		sb.append(" menu.name_en as menu_name_en, menu.parent_id, ");
		sb.append(" responsibility_id as responsibility_id, ");
		sb.append(" coalesce(res_menu.responsibility_dtl_id, 0) as responsibility_dtl_id, ");
		sb.append(" CASE WHEN res_menu.responsibility_id  is NULL THEN 'FALSE' ELSE 'TRUE' END AS check2, ");
		sb.append(" menu.action menu_action ");
		sb.append(" from wo_mst_menu menu ");
		sb.append(" left join wo_mst_responsibility_dtl res_menu ");
		sb.append(" on res_menu.menu_id = menu.menu_id  ");

		if (responsibilityMenuVO != null && responsibilityMenuVO.getResponsibility_id() != null
				&& responsibilityMenuVO.getResponsibility_id().longValue() > 0) {
			sb.append(" and res_menu.responsibility_id = :responsibilityId ");
		} else {
			sb.append(" and res_menu.responsibility_id is null ");
		}

		sb.append(" where menu.enabled_flag = 'Y' ");

		if (responsibilityMenuVO != null && responsibilityMenuVO.getParent_id() != null
				&& responsibilityMenuVO.getParent_id().longValue() > 0) {
			sb.append(" and menu.parent_id = :parentId ");
		} else {
			sb.append(" and menu.parent_id is null ");
		}

		Query query = getSession().createSQLQuery(sb.toString());

		if (responsibilityMenuVO != null && responsibilityMenuVO.getParent_id() != null
				&& responsibilityMenuVO.getParent_id().longValue() > 0) {
			query.setParameter("parentId", responsibilityMenuVO.getParent_id());
		}

		if (responsibilityMenuVO != null && responsibilityMenuVO.getResponsibility_id() != null
				&& responsibilityMenuVO.getResponsibility_id().longValue() > 0) {
			query.setParameter("responsibilityId", responsibilityMenuVO.getResponsibility_id());
		}

		List<ResponsibilityDtlVO> results = new ArrayList<ResponsibilityDtlVO>();
		List<Object[]> resultsQuery = query.getResultList();
		for (Object[] rowQuery : resultsQuery) {
			ResponsibilityDtlVO respDetVo = new ResponsibilityDtlVO();
			//respDetVo.setMenu_id(((BigInteger) rowQuery[0]).longValue());
			respDetVo.setMenu_id(MathUtil.returnIdObjectToLong(rowQuery[0]));
			respDetVo.setMenu_name_in((String) rowQuery[1]);
			respDetVo.setMenu_name_en((String) rowQuery[2]);
			if (rowQuery[3] != null) {
				//respDetVo.setParent_id(((BigInteger) rowQuery[3]).longValue());
				respDetVo.setParent_id(MathUtil.returnIdObjectToLong(rowQuery[3]));
			}

			if (rowQuery[4] != null) {
				//respDetVo.setResponsibility_id(((BigInteger) rowQuery[4]).longValue());
				respDetVo.setResponsibility_id(MathUtil.returnIdObjectToLong(rowQuery[4]));
			}

			//respDetVo.setResponsibility_dtl_id(((BigInteger) rowQuery[5]).longValue()); dikantor big integer jalan, di btpn entah kenapa jadi bigdecimal return nya
			//respDetVo.setResponsibility_dtl_id(((BigDecimal) rowQuery[5]).longValue());
			respDetVo.setResponsibility_dtl_id(MathUtil.returnIdObjectToLong(rowQuery[5]));
			respDetVo.setCheck2((String) rowQuery[6]);
			respDetVo.setMenu_action((String) rowQuery[7]);

			ResponsibilityDtlVO tempResponsibilityDtlVO = new ResponsibilityDtlVO();
			tempResponsibilityDtlVO.setParent_id(respDetVo.getMenu_id());
			tempResponsibilityDtlVO.setResponsibility_id(respDetVo.getResponsibility_id());
			respDetVo.setChildResponsibilityMenuList(searchResponsibilityMenuAllMenu(tempResponsibilityDtlVO));

			results.add(respDetVo);
		}
		return results;
	}
	
	@Override
	public void deleteInsertResponsibilityMenu(
			ResponsibilityDtlVO entityVo, String user) {
		//System.out.println("id==" + entityVo.getResponsibility_id());
		//System.out.println("size==" + entityVo.getChildResponsibilityMenuList().size());
		//delete all first
		if(entityVo.getResponsibility_id() != null) {
			String strDeleteQuery = "delete from wo_mst_responsibility_dtl " +
					" where RESPONSIBILITY_ID=:respId";
			Query delQuery = getSession().createSQLQuery(strDeleteQuery);
			delQuery.setParameter("respId", entityVo.getResponsibility_id());			
			
			int rowDel = delQuery.executeUpdate();
			logger.debug("Record " + rowDel + " deleted for resp id " + entityVo.getResponsibility_id());
			logger.debug("Record " + entityVo.getChildResponsibilityMenuList().size() + " will be re-inserted for resp id " + entityVo.getResponsibility_id());
		}
		
		
	}

	public MenuDao getMenuDao() {
		return menuDao;
	}

	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}
	
}
