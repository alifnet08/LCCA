package com.wo.module.runningNumber.dao;

import java.util.List;

import javax.persistence.Query;

import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.runningNumber.model.RunningNumber;

@Repository("runningNumberDao")
public class RunningNumberDaoImpl extends GenericDAOHibernate<RunningNumber, Long> implements RunningNumberDao {

	@SuppressWarnings("rawtypes")
	@Override
	public List<RunningNumber> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public RunningNumber getRunningNumber(String runningNumberNo, String runningNumberReset, String runningNumberType) throws Exception {
		String hql = "  FROM RunningNumber " +
					 " WHERE runningNumberNo = :runningNumberNo " +
					 "		 AND runningNumberReset = :runningNumberReset "	+
					 "		 AND runningNumberType = :runningNumberType " +
					 "       AND enabledFlag = 'Y' ";
		Query result = getSession().createQuery(hql);
		result.setParameter("runningNumberNo", runningNumberNo);
		result.setParameter("runningNumberReset", runningNumberReset);
		result.setParameter("runningNumberType", runningNumberType);
		List list = result.getResultList();
		
		if(list.size() > 0) {
			return (RunningNumber) list.get(0);
		}else {
			return null;
		}
	}

	public Integer getRunningNumberSeq(String runNumberNo, String runNumberReset)
			throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT NVL(RUNNING_NUMBER_SEQ,0) ");
		sb.append("   FROM WO_MST_RUNNING_NUMBER ");
		sb.append("  WHERE RUNNING_NUMBER_NO = '"+runNumberNo+"' "); 
		sb.append("        AND RUNNING_NUMBER_RESET = '"+runNumberReset+"' ");
				
		Query result = getSession().createSQLQuery(sb.toString());
		Number count = 0;
		if(result.getResultList() !=null && result.getResultList().size() > 0) {
			count = (Number)  result.getSingleResult();
		}
		if(count == null) {	
			count = 0;
		}
		
		return (Integer) count.intValue();
	}

	
}