package com.wo.module.regulationMonitoringFE.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.regulationMonitoringFE.vo.RegulationMonitoringFEVO;

public interface RegulationMonitoringFEService extends RetrieverDataPage<RegulationMonitoringFEVO>  {
    
	
	RegulationMonitoringFEVO searchForDetail(Long id);
}
