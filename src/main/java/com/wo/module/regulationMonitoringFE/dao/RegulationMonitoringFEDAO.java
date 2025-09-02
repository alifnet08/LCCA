package com.wo.module.regulationMonitoringFE.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.regulationMonitoring.model.RegMonitoringTrc;
import com.wo.module.regulationMonitoringFE.vo.RegulationMonitoringFEVO;

public interface RegulationMonitoringFEDAO extends  GenericDAO<RegMonitoringTrc, Long>, 
		RetrieverDataPage<RegulationMonitoringFEVO>{
	
	
	RegulationMonitoringFEVO searchForDetail(Long id);
}
