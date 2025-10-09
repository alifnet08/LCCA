/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.reportType.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.reportType.model.ReportType;

public interface ReportTypeService extends RetrieverDataPage<ReportType> {

	public void save(ReportType reportType);

	public void update(ReportType reportType);

	public void delete(ReportType reportType);

	public ReportType findById(Long id);

	public List<ReportType> getAllReportType();
	
	public Boolean isReportTypeDuplicate(ReportType reportType);
	
	public Boolean isUsedInTransaction(Long id);
}
