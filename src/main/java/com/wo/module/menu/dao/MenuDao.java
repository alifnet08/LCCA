/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.menu.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.menu.model.Menu;



/**
 *
 * @author hendra
 */
public interface MenuDao extends  GenericDAO<Menu, Long>, RetrieverDataPage<Menu> {

	public List<Menu> getAllParentMenuList();
	
}
