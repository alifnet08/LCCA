/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.discussion.dao;

import com.wo.module.discussion.model.Discussion;
import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;

/**
 *
 * @author hendra
 */
public interface DiscussionDao extends GenericDAO<Discussion, Long>, RetrieverDataPage<Discussion> {
	
}
