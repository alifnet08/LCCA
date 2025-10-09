/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regMonitoringPICFpConfirmation.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.regMonitoringPICFpConfirmation.vo.RegMonitoringPICFpConfirmationVO;
import com.wo.module.regulationMonitoring.model.RegMonitoringTmp;
/**
 *
 * @author hendra
 */
public interface RegMonitoringPICFpConfirmationDAO extends  GenericDAO<RegMonitoringTmp, Long>, RetrieverDataPage<RegMonitoringPICFpConfirmationVO>{

	
	
}
