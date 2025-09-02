/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.litigation.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.litigation.model.Litigation;

public interface LitigationService extends RetrieverDataPage<Litigation> {

	public void save(Litigation entity);

	public void update(Litigation entity);

	public void delete(Litigation entity);

	public Litigation findById(Long id);
	
}
