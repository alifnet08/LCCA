package com.wo.module.regulationMonitoring.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpAttachmentTrc;

@Repository("regMonitoringPICFollowUpAttachmentTrcDAO")
public class RegMonitoringPICFollowUpAttachmentTrcDAOImpl
		extends GenericDAOHibernate<RegMonitoringPICFollowUpAttachmentTrc, Long>
		implements RegMonitoringPICFollowUpAttachmentTrcDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<RegMonitoringPICFollowUpAttachmentTrc> getPICFollowUpAttachmentTrcByRegMonitoringId(
			Long regMonitoringPicFollowUpTrcId) throws Exception {
		String hql = "FROM RegMonitoringPICFollowUpAttachmentTrc where regMonitoringPicFollowUpTrc.regMonitoringPicFollowUpTrcId = :regMonitoringPicFollowUpTrcId";
		Query result = getSession().createQuery(hql);
		result.setParameter("regMonitoringPicFollowUpTrcId", regMonitoringPicFollowUpTrcId);
		return result.getResultList();
	}
}
