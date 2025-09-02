/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.discussion.service;

import com.wo.module.discussion.model.Discussion;
import com.wo.module.common.paging.RetrieverDataPage;

public interface DiscussionService extends RetrieverDataPage<Discussion> {

	public void save(Discussion entity);

	public void update(Discussion entity);

	public void delete(Discussion entity);

	public Discussion findById(Long id);
	
}
