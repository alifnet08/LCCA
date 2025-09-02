/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.dbCompliance.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.dbCompliance.model.DBCompliance;

public interface DBComplianceService extends RetrieverDataPage<DBCompliance> {

	public void save(DBCompliance entity);

	public void update(DBCompliance entity);

	public void delete(DBCompliance entity);

	public DBCompliance findById(Long id);
	
	public DBCompliance getCheckDataDBCompliance(Long dbComplianceId, String reportName, String reportType);
	
}
