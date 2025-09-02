/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationSocializationApproval.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.regulationSocialization.model.SocializationTmp;
import com.wo.module.regulationSocializationApproval.vo.RegulationSocializationApprovalVO;
/**
 *
 * @author hendra
 */
public interface RegulationSocializationApprovalDao extends  GenericDAO<SocializationTmp, Long>, RetrieverDataPage<RegulationSocializationApprovalVO>{

	
	
}
