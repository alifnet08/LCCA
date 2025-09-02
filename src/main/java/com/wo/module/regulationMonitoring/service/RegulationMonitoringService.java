/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationMonitoring.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.regulationMonitoring.vo.RegMonitoringApprovalVO;
import com.wo.module.regulationMonitoring.vo.RegMonitoringVO;
import com.wo.module.regulationMonitoring.vo.StatusConfirmationVO;

public interface RegulationMonitoringService extends RetrieverDataPage<RegMonitoringVO>  {
    
	public List<RegMonitoringApprovalVO> getDataApprovalByRegMonitoringId(Long regMonitoringId);
	
	public List<StatusConfirmationVO> getDataConfirmStatusByRegMonitoringId(Long regMonitoringId);
	
	public Boolean hasReachedMaximumReschedule(Long regMonitoringPicFollowupId);
	
	@SuppressWarnings("rawtypes")
	public List<RegMonitoringVO> searchDataXLS(List<? extends SearchObject> searchCriteria);
	
}
