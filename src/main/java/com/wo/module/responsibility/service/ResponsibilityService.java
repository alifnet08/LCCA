/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.responsibility.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.responsibility.model.Responsibility;
import com.wo.module.responsibility.vo.ResponsibilityDtlVO;

public interface ResponsibilityService extends RetrieverDataPage<Responsibility> {
	public List<Responsibility> getAllResponsibility();

	public void save(Responsibility entity);

	public void update(Responsibility entity);

	public void delete(Responsibility entity);

	public Responsibility findById(Long id);

	public List<ResponsibilityDtlVO> searchResponsibilityMenuAllMenu(ResponsibilityDtlVO responsibilityMenuVO);
	
	
	public void deleteInsertResponsibilityMenu(
			ResponsibilityDtlVO responsibilityId, String user) ;
	
}
