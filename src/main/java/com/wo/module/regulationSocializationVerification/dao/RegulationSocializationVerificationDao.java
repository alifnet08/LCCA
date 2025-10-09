/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationSocializationVerification.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.regulationSocialization.model.SocializationTrc;
import com.wo.module.regulationSocializationVerification.vo.RegulationSocializationVerificationSearchVO;
/**
 *
 * @author hendra
 */
public interface RegulationSocializationVerificationDao extends  GenericDAO<SocializationTrc, Long>, RetrieverDataPage<RegulationSocializationVerificationSearchVO>{

	
	
}
