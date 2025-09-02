/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationMonitoring.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.regulationMonitoring.dao.RegulationMonitoringDAO;
import com.wo.module.regulationMonitoring.vo.RegMonitoringApprovalVO;
import com.wo.module.regulationMonitoring.vo.RegMonitoringVO;
import com.wo.module.regulationMonitoring.vo.StatusConfirmationVO;

@Transactional
@Service("regulationMonitoringService")
public class RegulationMonitoringServiceImpl implements RegulationMonitoringService {
    @Autowired
    @Qualifier("regulationMonitoringDAO")
    private RegulationMonitoringDAO regulationMonitoringDAO;

	@Override
	public List<RegMonitoringApprovalVO> getDataApprovalByRegMonitoringId(Long regMonitoringId) {
		return regulationMonitoringDAO.getDataApprovalByRegMonitoringId(regMonitoringId);
	}

	@Override
	public List<StatusConfirmationVO> getDataConfirmStatusByRegMonitoringId(Long regMonitoringId) {
		return regulationMonitoringDAO.getDataConfirmStatusByRegMonitoringId(regMonitoringId);
	}
 
	public RegulationMonitoringDAO getRegulationMonitoringDAO() {
		return regulationMonitoringDAO;
	}

	public void setRegulationMonitoringDAO(RegulationMonitoringDAO regulationMonitoringDAO) {
		this.regulationMonitoringDAO = regulationMonitoringDAO;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<RegMonitoringVO> searchDataXLS(List<? extends SearchObject> searchCriteria) {
		return regulationMonitoringDAO.searchDataXLS(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<RegMonitoringVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return regulationMonitoringDAO.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return regulationMonitoringDAO.searchCountData(searchCriteria);
	}

	@Override
	public Boolean hasReachedMaximumReschedule(Long regMonitoringPicFollowupId) {
		return regulationMonitoringDAO.hasReachedMaximumReschedule(regMonitoringPicFollowupId);
	}
}
