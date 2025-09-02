/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationSocialization.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.regulationSocialization.vo.RegulationSocializationVO;
import com.wo.module.regulationSocialization.vo.SocializationApprovalVO;
import com.wo.module.regulationSocialization.vo.StatusConfirmationVO;

public interface RegulationSocializationService extends RetrieverDataPage<RegulationSocializationVO>  {
    
	public List<SocializationApprovalVO> getDataApprovalBySocializationId(Long socializationId);
	
	public List<StatusConfirmationVO> getDataConfirmStatusBySocializationId(Long socializationId);
	
	public Boolean hasReachedMaximumReschedule(Long socializationPicFollowupId);
	
	@SuppressWarnings("rawtypes")
	public List<RegulationSocializationVO> searchDataXLS(List<? extends SearchObject> searchCriteria);
	
}
