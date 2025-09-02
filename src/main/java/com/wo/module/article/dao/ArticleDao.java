/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.article.dao;

import com.wo.module.article.model.TmpArticle;
import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;

/**
 *
 * @author hendra
 */
public interface ArticleDao extends GenericDAO<TmpArticle, Long>, RetrieverDataPage<TmpArticle> {
	
}
