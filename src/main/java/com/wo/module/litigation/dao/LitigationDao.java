/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.litigation.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.litigation.model.Litigation;

/**
 *
 * @author hendra
 */
public interface LitigationDao extends GenericDAO<Litigation, Long>, RetrieverDataPage<Litigation> {
	
}
