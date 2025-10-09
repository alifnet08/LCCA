package com.wo.module.common.dao;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository("tableOfValuesDao")
public class TableOfValuesDAOImpl extends GenericDAOHibernate<Object, Long> implements TableOfValuesDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getListTableOfValues(String sql) throws SQLException {
		Query sqlQuery = getSession().createNativeQuery(sql);
		
		return sqlQuery.getResultList();
	}

}
