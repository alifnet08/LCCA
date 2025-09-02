/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.counterType.service;

import java.util.List;

import javax.faces.model.SelectItem;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.counterType.model.CounterType;

public interface CounterTypeService extends RetrieverDataPage<CounterType> {

	public void save(CounterType entity);

	public void update(CounterType entity);

	public void delete(CounterType entity);

	public CounterType findById(Long id);

	public List<CounterType> getAllCounterType() throws Exception;

	List<SelectItem> getAllCounterTypeLabelValue() throws Exception;
	
	public Boolean isDataDuplicate(CounterType entity);
	
	public Boolean isUsedInTransaction(Long id);
	
}
