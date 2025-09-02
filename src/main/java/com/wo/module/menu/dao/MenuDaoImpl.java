/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.menu.dao;

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
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.menu.constant.MenuConstants;
import com.wo.module.menu.model.Menu;

/**
 *
 * @author hendra
 */
@Repository("menuDao")
public class MenuDaoImpl extends GenericDAOHibernate<Menu, Long> implements MenuDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(MenuDaoImpl.class);

	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(MenuConstants.SEARCH_BY_NAME, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and UPPER(m.name_en) LIKE UPPER(:name) ");
						} else {
							sb.append(" and UPPER(m.name_in) LIKE UPPER(:name) ");
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
					if (StringUtils.equals(MenuConstants.SEARCH_BY_NAME, col)) {
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
		sb.append(" from wo_mst_menu m ");
		sb.append(" where 1=1 ");
		sb.append(" and m.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<Menu> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {

		List<Menu> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<Menu> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select m.menu_id, m.name_in, m.name_en, ");
		sb.append(" m.action, p.name_in as parent_name_in, p.name_en as parent_name_en, m.description, ");
		sb.append(" m.menu_level, p.menu_order ");
		sb.append(" from wo_mst_menu m ");
		sb.append(" left join wo_mst_menu p on p.menu_id = m.parent_id ");
		sb.append(" where 1=1 ");
		sb.append(" and m.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY m.menu_id DESC ");
		

		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<Menu> vo = new ArrayList<Menu>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				Menu data = new Menu();
				//data.setMenuId(((BigInteger) obj[0]).longValue());
				data.setMenuId(MathUtil.returnIdObjectToLong(obj[0]));
				data.setNameIn((String) obj[1]);
				data.setNameEn((String) obj[2]);
				data.setAction((String) obj[3]);
				data.setParentMenuNameIn((String) obj[4]);
				data.setParentMenuNameEn((String) obj[5]);
				data.setDescription((String) obj[6]);

				if (obj[7] != null) {
					//data.setMenuLevel(((BigDecimal) obj[7]).longValue());
					data.setMenuLevel(MathUtil.returnIdObjectToLong(obj[7]));
				}

				if (obj[8] != null) {
					//data.setMenuOrder(((Integer) obj[8]).longValue());
					data.setMenuOrder(MathUtil.returnIdObjectToLong(obj[8]));
				}
				
				vo.add(data);
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
	}
	
	
	
	@SuppressWarnings("rawtypes")
	public List<Menu> getAllParentMenuList() {
		StringBuilder sb = new StringBuilder();
		sb.append(" select m.menu_id, m.name_in, m.name_en ");
		sb.append(" from wo_mst_menu m ");
		sb.append(" where 1=1 ");
		sb.append(" and menu_level = 1 ");
		sb.append(" and m.enabled_flag = 'Y' ");
		
		sb.append(" ORDER BY m.menu_id DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());


		List resultList = query.getResultList();

		List<Menu> vo = new ArrayList<Menu>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				Menu data = new Menu();
				//data.setMenuId(((BigInteger) obj[0]).longValue());
				data.setMenuId(MathUtil.returnIdObjectToLong(obj[0]));
				data.setNameIn((String) obj[1]);
				data.setNameEn((String) obj[2]);
				
				vo.add(data);
			}
		}

	

		return vo;
	}

}
