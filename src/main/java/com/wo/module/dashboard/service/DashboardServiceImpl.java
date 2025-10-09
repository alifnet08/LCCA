/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.dashboard.dao.DashboardDao;

@Transactional
@Service("dashboardService")
public class DashboardServiceImpl implements DashboardService {
	@Autowired
	@Qualifier("dashboardDao")
	private DashboardDao dashboardDao;

	public DashboardDao getDashboardDao() {
		return dashboardDao;
	}

	public void setDashboardDao(DashboardDao dashboardDao) {
		this.dashboardDao = dashboardDao;
	}

	@Override
	public Long searchCountDataExternalRegulationWaitingApproval(String nikLogin, String action) {
		return dashboardDao.searchCountDataExternalRegulationWaitingApproval(nikLogin, action);
	}

	@Override
	public Long searchCountDataInternalRegulationWaitingApproval(String nikLogin, String action) {
		return dashboardDao.searchCountDataInternalRegulationWaitingApproval(nikLogin, action);
	}
	
	@Override
	public Long searchCountDataReportMatrixDiaryWaitingApproval(String nikLogin, String action) {
		return dashboardDao.searchCountDataReportMatrixDiaryWaitingApproval(nikLogin, action);
	}
	
	@Override
	public Long searchCountDataCorrespondenceWaitingApproval(String nikLogin, String action) {
		return dashboardDao.searchCountDataCorrespondenceWaitingApproval(nikLogin, action);
	}
	
	@Override
	public Long searchCountDataRegulationSocializationWaitingApproval(String nikLogin, String action) {
		return dashboardDao.searchCountDataRegulationSocializationWaitingApproval(nikLogin, action);
	}
	
	
	
	@Override
	public Long searchCountDataExternalRegulationRevised(String nikLogin, String action) {
		return dashboardDao.searchCountDataExternalRegulationRevised(nikLogin, action);
	}

	@Override
	public Long searchCountDataInternalRegulationRevised(String nikLogin, String action) {
		return dashboardDao.searchCountDataInternalRegulationRevised(nikLogin, action);
	}
	
	@Override
	public Long searchCountDataReportMatrixDiaryRevised(String nikLogin, String action) {
		return dashboardDao.searchCountDataReportMatrixDiaryRevised(nikLogin, action);
	}
	
	@Override
	public Long searchCountDataCorrespondenceRevised(String nikLogin, String action) {
		return dashboardDao.searchCountDataCorrespondenceRevised(nikLogin, action);
	}
	
	@Override
	public Long searchCountDataRegulationSocializationRevised(String nikLogin, String action) {
		return dashboardDao.searchCountDataRegulationSocializationRevised(nikLogin, action);
	}
	
	@Override
	public Long searchCountDataCorrespondenceVerification(String nikLogin, String action) {
		return dashboardDao.searchCountDataCorrespondenceVerification(nikLogin, action);
	}
	
	@Override
	public Long searchCountDataRegulationSocializationVerification(String nikLogin, String action) {
		return dashboardDao.searchCountDataRegulationSocializationVerification(nikLogin, action);
	}

	@Override
	public Long searchCountDataAuditWaitingApproval(String nikLogin, String action) {
		return dashboardDao.searchCountDataAuditWaitingApproval(nikLogin, action);
	}

	@Override
	public Long searchCountDataAuditRevised(String nikLogin, String action) {
		return dashboardDao.searchCountDataAuditRevised(nikLogin, action);
	}

	@Override
	public Long searchCountDataAuditVerification(String nikLogin, String action) {
		return dashboardDao.searchCountDataAuditVerification(nikLogin, action);
	}

	@Override
	public Long searchCountDataComplianceOnsiteReviewWaitingApproval(String nikLogin, String action) {
		return dashboardDao.searchCountDataComplianceOnsiteReviewWaitingApproval(nikLogin, action);
	}

	@Override
	public Long searchCountDataComplianceOnsiteReviewRevised(String nikLogin, String action) {
		return dashboardDao.searchCountDataComplianceOnsiteReviewRevised(nikLogin, action);
	}

	@Override
	public Long searchCountDataComplianceOnsiteReviewVerification(String nikLogin, String action) {
		return dashboardDao.searchCountDataComplianceOnsiteReviewVerification(nikLogin, action);
	}

	@Override
	public Long searchCountDataCorrespondenceAmlWaitingApproval(String nikLogin, String action) {
		return dashboardDao.searchCountDataCorrespondenceAmlWaitingApproval(nikLogin, action);
	}

	@Override
	public Long searchCountDataCorrespondenceAmlRevised(String nikLogin, String action) {
		return dashboardDao.searchCountDataCorrespondenceAmlRevised(nikLogin, action);
	}

	@Override
	public Long searchCountDataCorrespondenceAmlVerification(String nikLogin, String action) {
		return dashboardDao.searchCountDataCorrespondenceAmlVerification(nikLogin, action);
	}

	@Override
	public Long searchCountDataQAAdmin(String nikLogin, String action) {
		return dashboardDao.searchCountDataQAAdmin(nikLogin, action);
	}

	@Override
	public Long searchCountDataQAPIC(String nikLogin, String action) {
		return dashboardDao.searchCountDataQAPIC(nikLogin, action);
	}

	@Override
	public Long searchCountDataQAView(String nikLogin, String action) {
		return dashboardDao.searchCountDataQAView(nikLogin, action);
	}
	
	@Override
	public Long searchCountDataArticleWaitingApproval(String nikLogin, String action) {
		return dashboardDao.searchCountDataArticleWaitingApproval(nikLogin, action);
	}
	
	@Override
	public Long searchCountDataArticleRevised(String nikLogin, String action) {
		return dashboardDao.searchCountDataArticleRevised(nikLogin, action);
	}
	
	@Override
	public Long searchCountDataRegulationMonitoringWaitingApproval(String nikLogin, String action) {
		return dashboardDao.searchCountDataRegulationMonitoringWaitingApproval(nikLogin, action);
	}
	
	@Override
	public Long searchCountDataRegulationMonitoringRevised(String nikLogin, String action) {
		return dashboardDao.searchCountDataRegulationMonitoringRevised(nikLogin, action);
	}
	
	@Override
	public Long searchCountDataRegulationMonitoringVerification(String nikLogin, String action) {
		return dashboardDao.searchCountDataRegulationMonitoringVerification(nikLogin, action);
	}
	
	@Override
	public Long searchCountDataFineWaitingApproval(String nikLogin, String action) {
		return dashboardDao.searchCountDataFineWaitingApproval(nikLogin, action);
	}
	
	@Override
	public Long searchCountDataFineRevised(String nikLogin, String action) {
		return dashboardDao.searchCountDataFineRevised(nikLogin, action);
	}
	
	@Override
	public Long searchCountDataFineVerification(String nikLogin, String action) {
		return dashboardDao.searchCountDataFineVerification(nikLogin, action);
	}

	@Override
	public Long searchCountDataFAQApproval(String nikLogin, String action) {
		return dashboardDao.searchCountDataFAQApproval(nikLogin, action);
	}

	@Override
	public Long searchCountDataCpsaVerification(String nikLogin, String action) {
		return dashboardDao.searchCountDataCpsaVerification(nikLogin, action);
	}
	
}