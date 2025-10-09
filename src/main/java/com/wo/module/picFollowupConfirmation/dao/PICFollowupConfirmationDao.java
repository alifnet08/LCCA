/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.picFollowupConfirmation.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.picFollowupConfirmation.vo.PICFollowupConfirmationVO;
import com.wo.module.regulationSocialization.model.SocializationTmp;
/**
 *
 * @author hendra
 */
public interface PICFollowupConfirmationDao extends  GenericDAO<SocializationTmp, Long>, RetrieverDataPage<PICFollowupConfirmationVO>{

	
	
}
