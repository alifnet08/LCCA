package com.wo.module.report.reportLitigation.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportLitigation.vo.ReportLitiPerdataTergugatVo;
import com.wo.module.report.reportLitigation.vo.ReportLitiPerdataPenggugatVo;
import com.wo.module.report.reportLitigation.vo.ReportLitiPailitPKPUVo;
import com.wo.module.report.reportLitigation.vo.ReportLitiPidanaPelaporVo;
import com.wo.module.report.reportLitigation.vo.ReportLitiPidanaTerlaporVo;

public interface ReportLitigationService {

	@SuppressWarnings("rawtypes")
	public List<ReportLitiPerdataTergugatVo> getReportLitigationPerDataTergugatAsVo(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportLitiPerdataPenggugatVo> getReportLitigationPerDataPenggugatAsVo(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportLitiPailitPKPUVo> getReportLitigationKepailitanDanPKPUAsVo(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportLitiPidanaPelaporVo> getReportLitigationPidanaPelaporAsVo(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportLitiPidanaTerlaporVo> getReportLitigationPidanaTerlaporAsVo(List<? extends SearchObject> searchCriteria);
	
}
