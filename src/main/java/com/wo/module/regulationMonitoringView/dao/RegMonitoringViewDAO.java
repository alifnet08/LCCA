package com.wo.module.regulationMonitoringView.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.regulationMonitoring.model.RegMonitoringTrc;
import com.wo.module.regulationMonitoringView.vo.RegMonitoringViewVO;

public interface RegMonitoringViewDAO extends  GenericDAO<RegMonitoringTrc, Long>, 
    RetrieverDataPage<RegMonitoringViewVO>{

	
	
}
