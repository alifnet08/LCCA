package com.wo.module.regulationMonitoring.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.engine.vo.SendEmailVO;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpEmailTrc;

public interface RegMonitoringPICFollowUpEmailTrcDAO extends  GenericDAO<RegMonitoringPICFollowUpEmailTrc, Long>{

	SendEmailVO getEmailPicByFollowUpEmailId(Long emailFollowId);

	
}
