/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.division.dao;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.division.model.Division;
import com.wo.module.parameter.model.ParameterDetail;

/**
 *
 * @author hendra
 */
@Repository("divisionDao")
public class DivisionDaoImpl extends GenericDAOHibernate<Division, Long> implements DivisionDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(DivisionDaoImpl.class);

	@Override
	public List<Division> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	

	public Number getCountDivisionByName(String divName){
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_mst_division d where UPPER(division_name) like UPPER('"+divName+"') ");
		Query query = getSession().createSQLQuery(sb.toString());
		return (Number) query.getSingleResult();
	}
	
	
	public Division getDivisionByName(String divName) {

		String hql = "FROM Division where divisionName = :divName ";
		Query result = getSession().createQuery(hql);
		result.setParameter("divName", divName);
		List list = result.getResultList();
		
		if(list.size() > 0) {
			return (Division) list.get(0);
		}else {
			return null;
		}
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getDivisionNameByDivisionId(Long divId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select d.division_name  ");
		sb.append(" from wo_mst_division d ");
		sb.append(" where 1=1 ");
		sb.append(" and d.division_id = :divisionId ");
		sb.append(" order by d.division_name ");

		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("divisionId", divId);
		
		List resultList = query.getResultList();
		
		if(resultList!=null && resultList.size()>0) {
			return  (String)resultList.get(0);
		}else {
			return null;
		}
	}
}
