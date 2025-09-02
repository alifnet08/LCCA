package com.wo.module.regulationMonitoringView.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.regulationMonitoringView.dao.RegMonitoringViewDAO;
import com.wo.module.regulationMonitoringView.vo.RegMonitoringViewVO;


@Transactional
@Service("regMonitoringViewService")
public class RegMonitoringViewServiceImpl implements RegMonitoringViewService {
	
    @Autowired
    @Qualifier("regMonitoringViewDAO")
    private RegMonitoringViewDAO regMonitoringViewDAO;

	@Override
	public List<RegMonitoringViewVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return regMonitoringViewDAO.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return regMonitoringViewDAO.searchCountData(searchCriteria);
	}

	public RegMonitoringViewDAO getRegMonitoringViewDAO() {
		return regMonitoringViewDAO;
	}

	public void setRegMonitoringViewDAO(RegMonitoringViewDAO regMonitoringViewDAO) {
		this.regMonitoringViewDAO = regMonitoringViewDAO;
	}    
}
