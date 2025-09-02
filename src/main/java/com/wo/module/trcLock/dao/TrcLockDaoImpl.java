package com.wo.module.trcLock.dao;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.trcLock.model.TrcLock;

@Repository("trcLockDao")
public class TrcLockDaoImpl extends GenericDAOHibernate<TrcLock, Long> implements TrcLockDao, Serializable {

	private static final long serialVersionUID = 5537547483638302369L;

	@Override
	public Boolean isCheckLockIsEmpty() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("select count(1) from wo_trc_lock wtl where wtl.enabled_flag = 'Y' ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		Number result = (Number) query.getSingleResult();
		
		if (result == null) {
			result = 0;
		}
		
		if (result.longValue() == 0) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public TrcLock findLastUserLockMenu(String menu) {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		
		sb.append(" SELECT wtl.LOCK_ID ");
		sb.append("     ,wmu.NAME ");
		sb.append("     ,wtl.START_DATE ");
		sb.append("     ,wmu.NIK ");
		sb.append(" FROM WO_TRC_LOCK wtl ");
		sb.append("     inner join WO_MST_USER wmu on wmu.USER_ID = wtl.USER_ID ");
		sb.append(" where 1 = 1 ");
		sb.append("     AND wtl.ENABLED_FLAG = 'Y' ");
		sb.append("     AND wtl.END_DATE IS NULL ");
		sb.append("     AND wtl.LOCK_MODULE = :lockModule ");
		sb.append(" order by wtl.LOCK_ID desc ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("lockModule", menu);
		
		List<Object[]> result = query.getResultList();
		TrcLock data = new TrcLock();
		
		if (!result.isEmpty()) {
			for (Object[] obj : result) {
				data.setLockId(obj[0] != null ? ((Number) obj[0]).longValue() : null);
				data.setUserName(obj[1] != null ? (String) obj[1] : null);
				if (obj[2] != null) {
					data.setStartDate((Timestamp) obj[2]);
					data.setStartDateStr(sdf.format((Timestamp) obj[2]));
				}
				data.setUserNik(obj[3] != null ? (String) obj[3] : null);
			}
		}
		
		return data;
	}

}
