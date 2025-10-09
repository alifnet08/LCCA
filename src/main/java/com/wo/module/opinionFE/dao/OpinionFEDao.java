/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.opinionFE.dao;

import com.wo.module.article.model.TmpArticle;
import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.opinionFE.vo.OpinionFEVO;

/**
 *
 * @author hendra
 */
public interface OpinionFEDao extends GenericDAO<TmpArticle, Long>, RetrieverDataPage<OpinionFEVO> {
	
}
