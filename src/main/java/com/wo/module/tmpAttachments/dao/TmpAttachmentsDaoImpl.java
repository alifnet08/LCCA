/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpAttachments.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.tmpAttachments.model.TmpAttachments;

@Repository("tmpAttachmentsDao")
public class TmpAttachmentsDaoImpl extends GenericDAOHibernate<TmpAttachments, Long> implements TmpAttachmentsDao {
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	@Override
	public Integer executeUpdate(String query) throws SQLSyntaxErrorException, Exception {
	    Query result = getSession().createSQLQuery(query);
	    return result.executeUpdate();
	}

	@Override
	public List<Map<String, Object>> executeSelect(String query) throws SQLSyntaxErrorException, Exception {
		List<Map<String, Object>> res = jdbcTemplate.queryForList(query);
		return res;
	}
	
	@Override
	public List<String> getColumnNames(String query) throws SQLSyntaxErrorException, Exception {
//		List<Map<String, Object>> res = jdbcTemplate.queryForList(query);
		Connection con = jdbcTemplate.getDataSource().getConnection();
		Statement statement = con.createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		ResultSetMetaData metadata = resultSet.getMetaData();
		int columnCount = metadata.getColumnCount();

	    ArrayList<String> columns = new ArrayList<String>();
	    for (int i = 1; i <= columnCount; i++) {
	      String columnName = metadata.getColumnName(i);
	      columns.add(columnName);
	    }
	    
		return columns;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> executeSelectQuery(String query) throws SQLSyntaxErrorException, Exception {
	    Query result = getSession().createSQLQuery(query);
	    return result.getResultList();
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
