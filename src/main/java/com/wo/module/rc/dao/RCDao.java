/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.rc.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.rc.model.RC;

/**
 *
 * @author hendra
 */
public interface RCDao extends GenericDAO<RC, Long>, RetrieverDataPage<RC> {

	List<RC> getRCList();
	
}
