/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationMonitoringVerification.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.regulationMonitoring.model.RegMonitoringTrc;
import com.wo.module.regulationMonitoringVerification.vo.RegMonitoringVerificationSearchVO;
import com.wo.module.user.model.User;


public interface RegMonitoringVerificationService extends RetrieverDataPage<RegMonitoringVerificationSearchVO>  {
    
	public void processConfirm(RegMonitoringTrc regMonitoringTrc, User user)throws Exception;
}
