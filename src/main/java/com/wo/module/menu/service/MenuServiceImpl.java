/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.menu.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.menu.dao.MenuDao;
import com.wo.module.menu.model.Menu;

@Transactional
@Service("menuService")
public class MenuServiceImpl implements MenuService {
	@Autowired
	@Qualifier("menuDao")
	private MenuDao menuDao;

	public MenuDao getMenuDao() {
		return menuDao;
	}

	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<Menu> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return menuDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return menuDao.searchCountData(searchCriteria);
	}

	public void save(Menu entity) {
		menuDao.save(entity);
	}

	public void update(Menu entity) {
		menuDao.update(entity);
	}

	public void delete(Menu entity) {
		menuDao.delete(entity);
	}

	public Menu findById(Long id) {
		return menuDao.getById(id);
	}
	
	public List<Menu> getAllParentMenuList() {
		return menuDao.getAllParentMenuList();
	}

}
