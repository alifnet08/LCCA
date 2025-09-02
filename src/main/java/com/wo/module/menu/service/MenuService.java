/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.menu.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.menu.model.Menu;

public interface MenuService extends RetrieverDataPage<Menu> {
	public void save(Menu entity); 
	
	public void update(Menu entity);
	
	public void delete(Menu entity);
  
    public Menu findById(Long id) ;
    
    public List<Menu> getAllParentMenuList();
}
