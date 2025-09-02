/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.article.service;

import com.wo.module.article.model.TmpArticle;
import com.wo.module.common.paging.RetrieverDataPage;

public interface ArticleService extends RetrieverDataPage<TmpArticle> {

	public void save(TmpArticle entity);

	public void update(TmpArticle entity);

	public void delete(TmpArticle entity);

	public TmpArticle findById(Long id);
	
}
