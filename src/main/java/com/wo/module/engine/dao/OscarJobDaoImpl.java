package com.wo.module.engine.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.jdbc.Work;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.log.model.LogHeader;
import com.wo.module.user.model.User;

@Repository("oscarJobDao")
public class OscarJobDaoImpl extends GenericDAOHibernate<LogHeader, Long> implements OscarJobDao {

	public String getEntityName() {
		return "";
	}

	public String getPK() {
		return "id";
	}

	public String execInboundCommon(String procedureName, String jsonData) throws Exception {
		System.out.println("execOutboundCommon : " + procedureName + " is started");
		
		try {
			getSession().beginTransaction();
			String sql = "{ CALL " + procedureName + "(?)}";
			getSession().doWork(new Work() {
				
				@Override
				public void execute(Connection connection) throws SQLException {
					CallableStatement cstmt = null;
					try {
						cstmt = connection.prepareCall(sql);
						cstmt.setString(1, jsonData);
						cstmt.execute();
					} catch (SQLException se) {
						throw se;
					} finally {
						try {
							if (cstmt != null)
								cstmt.close();
						} catch (SQLException sqlexception) {
						}
					}
				}
			});
			
		} catch (Exception e) {
			throw e;
		} finally {
			getSession().close();
		}
		
		System.out.println("execOutboundCommon : " + procedureName + " is finished");

		return "SUCCESS";

	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getSystemProperty(String propertyCode) {
		String result = null;
//		getSession().beginTransaction();
		try {
			String query = "SELECT NAME_IN FROM wo_mst_parameter_dtl " + "WHERE PARAMETER_DTL_CODE = :propertyCode";
			
			Query queryResult = getSession().createSQLQuery(query);
			queryResult.setParameter("propertyCode", propertyCode);
			result = (String) queryResult.uniqueResult();

		} catch (Exception se) {
			throw se;
		} finally {
//			getSession().close();
		}

		return result;
	}

	@Override
	public List<User> getListUserFromOracle(String tableQuery) {
		return null;
	}
}