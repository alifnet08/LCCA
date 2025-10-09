/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationSocializationVerification.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.regulationSocialization.model.SocializationTrc;
import com.wo.module.regulationSocializationVerification.vo.RegulationSocializationVerificationSearchVO;
import com.wo.module.user.model.User;


public interface RegulationSocializationVerificationService extends RetrieverDataPage<RegulationSocializationVerificationSearchVO>  {
    
	public void processConfirm(SocializationTrc socializationTrc, User user)throws Exception;
}
