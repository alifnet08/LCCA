package com.wo.module.regulationMonitoring.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpTrc;

@Repository("regMonitoringPICFollowUpTrcDAO")
public class RegMonitoringPICFollowUpTrcDAOImpl extends GenericDAOHibernate<RegMonitoringPICFollowUpTrc, Long>
		implements RegMonitoringPICFollowUpTrcDAO {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RegMonitoringPICFollowUpTrc> getPICFollowUpTrcByRegMonitoringId(Long regMonitoringTrcId) throws Exception {
		String hql = "FROM RegMonitoringPICFollowUpTrc where regMonitoringPICFollowUpTrc.regulationMonitoringTrc.regMonitoringTrcId = :regMonitoringTrcId";
		 Query result = getSession().createQuery(hql);
		 result.setParameter("regMonitoringTrcId", regMonitoringTrcId);
		 return result.getResultList();
	}
}
