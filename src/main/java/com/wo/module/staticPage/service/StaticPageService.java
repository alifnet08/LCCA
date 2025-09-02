/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.staticPage.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.staticPage.model.StaticPage;

public interface StaticPageService extends RetrieverDataPage<StaticPage> {

	public void save(StaticPage entity);

	public void update(StaticPage entity);

	public void delete(StaticPage entity);

	public StaticPage findById(Long id);
	
	public StaticPage getStaticPageByCategory(String category);
	
}
