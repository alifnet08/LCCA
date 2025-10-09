/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationMonitoringVerification.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.regulationMonitoring.model.RegMonitoringTrc;
import com.wo.module.regulationMonitoringVerification.vo.RegMonitoringVerificationSearchVO;
/**
 *
 * @author hendra
 */
public interface RegMonitoringVerificationDAO extends  GenericDAO<RegMonitoringTrc, Long>, RetrieverDataPage<RegMonitoringVerificationSearchVO>{

	
	
}
