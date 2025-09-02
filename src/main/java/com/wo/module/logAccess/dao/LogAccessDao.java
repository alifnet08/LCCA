/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.logAccess.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.logAccess.model.LogAccess;

/**
 *
 * @author hendra
 */
public interface LogAccessDao extends GenericDAO<LogAccess, Long>, RetrieverDataPage<LogAccess>{
	
}
