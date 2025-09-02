/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.advocateFE.dao;

import com.wo.module.advocate.model.Advocate;
import com.wo.module.advocateFE.vo.AdvocateFEVO;
import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;

/**
 *
 * @author hendra
 */
public interface AdvocateFEDao extends GenericDAO<Advocate, Long>, RetrieverDataPage<AdvocateFEVO> {
	
}
