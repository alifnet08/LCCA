package com.wo.module.regulationMonitoring.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpTrc;

public interface RegMonitoringPICFollowUpTrcDAO extends GenericDAO<RegMonitoringPICFollowUpTrc, Long> {

	public List<RegMonitoringPICFollowUpTrc> getPICFollowUpTrcByRegMonitoringId(Long regMonitoringTrcId) throws Exception;
}
