/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.counterType.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.counterType.model.CounterType;

/**
 *
 * @author hendra
 */
public interface CounterTypeDao extends GenericDAO<CounterType, Long>, RetrieverDataPage<CounterType> {
	public List<CounterType> getAllCounterType() throws Exception;
	
	public Boolean isDataDuplicate(CounterType entity);
	
	 public Boolean isUsedInTransaction(Long id);
}
