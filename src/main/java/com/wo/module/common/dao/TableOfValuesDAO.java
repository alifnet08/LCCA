package com.wo.module.common.dao;

import java.sql.SQLException;
import java.util.List;

public interface TableOfValuesDAO extends GenericDAO<Object, Long>{
	public List<Object[]> getListTableOfValues(String sql) throws SQLException ;

}
