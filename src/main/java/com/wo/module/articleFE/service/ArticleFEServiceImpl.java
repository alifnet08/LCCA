/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.articleFE.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.article.model.Article;
import com.wo.module.article.model.TmpArticle;
import com.wo.module.articleFE.dao.ArticleFEDao;
import com.wo.module.articleFE.vo.ArticleFEVO;
import com.wo.module.common.paging.SearchObject;

@Transactional
@Service("articleFEService")
public class ArticleFEServiceImpl implements ArticleFEService {
    @Autowired
    @Qualifier("articleFEDao")
    private ArticleFEDao articleFEDao;
    
	
	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<ArticleFEVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws Exception {
		return articleFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return articleFEDao.searchCountData(searchCriteria);
	}

	public ArticleFEDao getArticleFEDao() {
		return articleFEDao;
	}

	public void setArticleFEDao(ArticleFEDao articleFEDao) {
		this.articleFEDao = articleFEDao;
	}
	
	public Article findById(Long id) {
		return articleFEDao.findById(id);
	}
	
	public void update(Article entity) {
		articleFEDao.update(entity);
	}
	    
}