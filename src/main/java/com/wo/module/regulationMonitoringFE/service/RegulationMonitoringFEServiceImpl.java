package com.wo.module.regulationMonitoringFE.service;

import java.util.List;

import javax.transaction.Transactional;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.regulationMonitoringFE.dao.RegulationMonitoringFEDAO;
import com.wo.module.regulationMonitoringFE.vo.RegulationMonitoringFEVO;

@Transactional
@Service("regulationMonitoringFEService")
public class RegulationMonitoringFEServiceImpl implements RegulationMonitoringFEService {

	@Autowired
	@Qualifier("regulationMonitoringFEDAO")
	private RegulationMonitoringFEDAO regulationMonitoringFEDAO;
	
	@Override
	public List<RegulationMonitoringFEVO> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return regulationMonitoringFEDAO.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return regulationMonitoringFEDAO.searchCountData(searchCriteria);
	}

	@Override
	public RegulationMonitoringFEVO searchForDetail(Long id) {
		// TODO Auto-generated method stub
		return regulationMonitoringFEDAO.searchForDetail(id);
	}	
}