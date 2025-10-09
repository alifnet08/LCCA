/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.articleFE.service;

import com.wo.module.article.model.Article;
import com.wo.module.articleFE.vo.ArticleFEVO;
import com.wo.module.common.paging.RetrieverDataPage;

public interface ArticleFEService extends RetrieverDataPage<ArticleFEVO> {

	public Article findById(Long id);
	public void update(Article entity);

}
