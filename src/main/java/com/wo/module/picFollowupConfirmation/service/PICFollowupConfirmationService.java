/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.picFollowupConfirmation.service;

import java.util.Date;
import java.util.List;

import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.picFollowupConfirmation.vo.PICFollowupConfirmationVO;
import com.wo.module.regulationSocialization.model.SocializationTrc;
import com.wo.module.user.model.User;


public interface PICFollowupConfirmationService extends RetrieverDataPage<PICFollowupConfirmationVO>  {
    
	public void processConfirm(SocializationTrc socializationTrc, 
			Long socializationPICFollowupTrcId, 
			String keterangan,Date followupDate,List<UploadedFileWO> uploadFile,  User nik)throws Exception;

}
