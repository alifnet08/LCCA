/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.article.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.article.dao.ArticleDao;
import com.wo.module.article.model.TmpArticle;
import com.wo.module.common.paging.SearchObject;

@Transactional
@Service("articleService")
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    @Qualifier("articleDao")
    private ArticleDao articleDao;
    
	public ArticleDao getArticleDao() {
		return articleDao;
	}

	public void setArticleDao(ArticleDao articleDao) {
		this.articleDao = articleDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<TmpArticle> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws Exception {
		return articleDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return articleDao.searchCountData(searchCriteria);
	}
	
	public void save(TmpArticle entity) {
		articleDao.save(entity);
	}
	
	public void update(TmpArticle entity) {
		articleDao.update(entity);
	}
	
	public void delete(TmpArticle entity) {
		articleDao.delete(entity);
	}
  
    public TmpArticle findById(Long id) {
    	return articleDao.getById(id);
    }
    
   
        
}
