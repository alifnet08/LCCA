package com.wo.module.engine.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernateOracle;
import com.wo.module.log.model.LogHeader;
import com.wo.module.user.model.User;

//@Repository("oscarJobOracleDao")
public class OscarJobOracleDaoImpl extends GenericDAOHibernateOracle<LogHeader, Long> implements OscarJobDao {

	public String getEntityName() {
		return "";
	}

	public String getPK() {
		return "id";
	}

	public String execInboundCommon(String procedureName, String nik) throws Exception {
		return null;
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<User> getListUserFromOracle(String tableQuery) {
		List<Object[]> result = null;
//		getSession().beginTransaction();
		try {
			String query = "SELECT "
					+ " NIK "
					+ " ,NAMA "
					+ " ,EMAIL_ADDRESS "
					+ " ,JOB_ID "
					+ " ,JOB "
					+ " ,ORGANIZATION_ID "
					+ " ,ORGANIZATION_NAME "
					+ " ,NIK_ATASAN_LANGSUNG "
					+ " ,BRANCH_CODE "
					+ " ,DIVISI_ID "
					+ " ,DIVISI "
					+ " FROM " + tableQuery;
			
			Query queryResult = getSession().createSQLQuery(query);
			result = queryResult.list();
			
			List<User> users = new ArrayList<User>();
			if(result != null) {
				for (Object[] o : result) {
					User u = new User();
					u.setNik(o[0] != null?(String) o[0] : ""); 
					u.setName(o[1] != null?(String) o[1] : "");
					u.setEmail(o[2] != null?(String) o[2] : ""); 
					u.setJobId(o[3] != null?((BigDecimal) o[3]).longValue() : null); 
					u.setJobName(o[4] != null?(String) o[4] : ""); 
					u.setPositionId(o[5] != null?((BigDecimal) o[5]).longValue() : null); 
					u.setPositionName(o[6] != null?(String) o[6] : ""); 
					u.setPukNik(o[7] != null?(String) o[7] : ""); 
					u.setBranchCode(o[8] != null?(String) o[8] : ""); 
					u.setDivisionId(o[9] != null?Long.parseLong(((String) o[9])) : null); 
					u.setDivisionName(o[10] != null?(String) o[10]: "");
					
					users.add(u);
				}
			}
			
			return users;

		} catch (Exception se) {
			throw se;
		} finally {
//			getSession().close();
		}
	}
}