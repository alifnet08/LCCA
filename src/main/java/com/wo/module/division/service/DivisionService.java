/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.division.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.division.model.Division;

public interface DivisionService extends RetrieverDataPage<Division> {

	public void save(Division entity);

	public void update(Division entity);

	public void delete(Division entity);

	public Division findById(Long id);
	
	public Number getCountDivisionByName(String divName);
	
	public Division getDivisionByName(String divName);
}
