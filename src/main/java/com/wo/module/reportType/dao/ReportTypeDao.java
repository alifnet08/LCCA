/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.reportType.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.reportType.model.ReportType;
/**
 *
 * @author hendra
 */
public interface ReportTypeDao extends  GenericDAO<ReportType, Long>, RetrieverDataPage<ReportType>{

	public List<ReportType> getAllReportType();
	
	public Boolean isReportTypeDuplicate(ReportType reportType);
	
	public Boolean isUsedInTransaction(Long id);
}
