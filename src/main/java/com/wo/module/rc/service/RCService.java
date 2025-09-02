/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.rc.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.rc.model.RC;

public interface RCService extends RetrieverDataPage<RC> {

	public void save(RC entity);

	public void update(RC entity);

	public void delete(RC entity);

	public RC findById(Long id);
	
	public List<RC> getRCList();
	
}
