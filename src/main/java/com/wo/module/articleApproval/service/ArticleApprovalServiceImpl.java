package com.wo.module.articleApproval.service;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.article.model.TmpArticle;
import com.wo.module.articleApproval.dao.ArticleApprovalDao;
import com.wo.module.articleApproval.vo.ArticleApprovalVo;
import com.wo.module.common.paging.SearchObject;

@Transactional
@Service("articleApprovalService")
public class ArticleApprovalServiceImpl implements ArticleApprovalService, Serializable{

	private static final long serialVersionUID = -2379791935817465876L;

	@Autowired
	@Qualifier("articleApprovalDao")
	private ArticleApprovalDao articleApprovalDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ArticleApprovalVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return articleApprovalDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return articleApprovalDao.searchCountData(searchCriteria);
	}

	@Override
	public TmpArticle findById(Long tmpArticleId) {
		return articleApprovalDao.getById(tmpArticleId);
	}
	
	@Override
	public void update(TmpArticle entity) {
		articleApprovalDao.update(entity);
	}
	
	public ArticleApprovalDao getArticleApprovalDao() {
		return articleApprovalDao;
	}

	public void setArticleApprovalDao(ArticleApprovalDao articleApprovalDao) {
		this.articleApprovalDao = articleApprovalDao;
	}

}
