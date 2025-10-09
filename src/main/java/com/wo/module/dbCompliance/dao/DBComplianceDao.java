/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.dbCompliance.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.dbCompliance.model.DBCompliance;

/**
 *
 * @author hendra
 */
public interface DBComplianceDao extends GenericDAO<DBCompliance, Long>, RetrieverDataPage<DBCompliance> {
	public DBCompliance getCheckDataDBCompliance(Long dbComplianceId, String reportName, String reportType);
}
