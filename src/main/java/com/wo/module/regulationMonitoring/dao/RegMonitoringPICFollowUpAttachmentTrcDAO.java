package com.wo.module.regulationMonitoring.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpAttachmentTrc;

public interface RegMonitoringPICFollowUpAttachmentTrcDAO extends  GenericDAO<RegMonitoringPICFollowUpAttachmentTrc, Long>{

	public List<RegMonitoringPICFollowUpAttachmentTrc> getPICFollowUpAttachmentTrcByRegMonitoringId(Long regMonitoringPicFollowUpTrcId) throws Exception;
	
}
