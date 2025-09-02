/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationMonitoring.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.regulationMonitoring.model.RegMonitoringTmp;
import com.wo.module.regulationMonitoring.vo.RegMonitoringApprovalVO;
import com.wo.module.regulationMonitoring.vo.RegMonitoringVO;
import com.wo.module.regulationMonitoring.vo.StatusConfirmationVO;
/**
 *
 * @author hendra
 */
public interface RegulationMonitoringDAO extends  GenericDAO<RegMonitoringTmp, Long>, RetrieverDataPage<RegMonitoringVO>{
	
	public List<RegMonitoringApprovalVO> getDataApprovalByRegMonitoringId(Long regMonitoringId);
	
	public List<StatusConfirmationVO> getDataConfirmStatusByRegMonitoringId(Long regMonitoringId);
	
	public Boolean hasReachedMaximumReschedule(Long regMonitoringPicFollowupId);
	
	@SuppressWarnings("rawtypes")
	public List<RegMonitoringVO> searchDataXLS(List<? extends SearchObject> searchCriteria);
}
