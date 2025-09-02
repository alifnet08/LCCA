/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.logAccess.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.logAccess.model.LogAccess;

public interface LogAccessService extends RetrieverDataPage<LogAccess> {

	public void save(LogAccess entity);

	public void update(LogAccess entity);

	public void delete(LogAccess entity);

	public LogAccess findById(Long id);
	
	
	
}
