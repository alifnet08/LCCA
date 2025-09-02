/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.articleFE.dao;

import com.wo.module.article.model.Article;
import com.wo.module.articleFE.vo.ArticleFEVO;
import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;

/**
 *
 * @author hendra
 */
public interface ArticleFEDao extends GenericDAO<Article, Long>, RetrieverDataPage<ArticleFEVO> {
	
}
