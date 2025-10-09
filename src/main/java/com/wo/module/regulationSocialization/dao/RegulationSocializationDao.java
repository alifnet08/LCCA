/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationSocialization.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.regulationSocialization.model.SocializationTmp;
import com.wo.module.regulationSocialization.vo.RegulationSocializationVO;
import com.wo.module.regulationSocialization.vo.SocializationApprovalVO;
import com.wo.module.regulationSocialization.vo.StatusConfirmationVO;
/**
 *
 * @author hendra
 */
public interface RegulationSocializationDao extends  GenericDAO<SocializationTmp, Long>, RetrieverDataPage<RegulationSocializationVO>{
	
	public List<SocializationApprovalVO> getDataApprovalBySocializationId(Long socializationId);
	
	public List<StatusConfirmationVO> getDataConfirmStatusBySocializationId(Long socializationId);
	
	public Boolean hasReachedMaximumReschedule(Long socializationId);
	
	@SuppressWarnings("rawtypes")
	public List<RegulationSocializationVO> searchDataXLS(List<? extends SearchObject> searchCriteria);
	
}
