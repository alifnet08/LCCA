/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.responsibility.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.responsibility.model.Responsibility;
import com.wo.module.responsibility.vo.ResponsibilityDtlVO;

/**
 *
 * @author hendra
 */
public interface ResponsibilityDao extends GenericDAO<Responsibility, Long>, RetrieverDataPage<Responsibility> {
	public List<Responsibility> getAllResponsibility();

	public List<ResponsibilityDtlVO> searchResponsibilityMenuAllMenu(ResponsibilityDtlVO responsibilityMenuVO);

	public void deleteInsertResponsibilityMenu(
			ResponsibilityDtlVO responsibilityId, String user);
}
