/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.advocate.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.advocate.model.Advocate;

public interface AdvocateService extends RetrieverDataPage<Advocate> {

	public void save(Advocate entity);

	public void update(Advocate entity);

	public void delete(Advocate entity);

	public Advocate findById(Long id);
	
}
