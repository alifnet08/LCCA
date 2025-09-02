package com.wo.module.articleApproval.service;

import com.wo.module.article.model.TmpArticle;
import com.wo.module.articleApproval.vo.ArticleApprovalVo;
import com.wo.module.common.paging.RetrieverDataPage;

public interface ArticleApprovalService extends RetrieverDataPage<ArticleApprovalVo>{

	public void update(TmpArticle entity);
	
	public TmpArticle findById(Long tmpArticleId);
	
}
