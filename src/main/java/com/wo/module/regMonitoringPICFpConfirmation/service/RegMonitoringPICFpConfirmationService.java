/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regMonitoringPICFpConfirmation.service;

import java.util.Date;
import java.util.List;

import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.regMonitoringPICFpConfirmation.vo.RegMonitoringPICFpConfirmationVO;
import com.wo.module.regulationMonitoring.model.RegMonitoringTrc;
import com.wo.module.user.model.User;


public interface RegMonitoringPICFpConfirmationService extends RetrieverDataPage<RegMonitoringPICFpConfirmationVO>  {
    
	public void processConfirm(RegMonitoringTrc regMonitoringTrc, 
			Long regMonitoringPicFollowUpTrcId, 
			String keterangan,Date followupDate,List<UploadedFileWO> uploadFiles,  User user)throws Exception;

}
