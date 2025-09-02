/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.litigation.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.litigation.model.LitigationNew;

/**
 *
 * @author hendra
 */
public interface LitigationNewDao extends GenericDAO<LitigationNew, Long>, RetrieverDataPage<LitigationNew> {
	
}
