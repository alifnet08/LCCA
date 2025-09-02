/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.staticPage.dao;

import com.wo.module.staticPage.model.StaticPage;
import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;

/**
 *
 * @author hendra
 */
public interface StaticPageDao extends GenericDAO<StaticPage, Long>, RetrieverDataPage<StaticPage> {
	
	public StaticPage getStaticPageByCategory(String category);
}
