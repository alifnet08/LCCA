package com.wo.module.articleApproval.dao;

import com.wo.module.article.model.TmpArticle;
import com.wo.module.articleApproval.vo.ArticleApprovalVo;
import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;

public interface ArticleApprovalDao extends GenericDAO<TmpArticle, Long>, RetrieverDataPage<ArticleApprovalVo>{

}
