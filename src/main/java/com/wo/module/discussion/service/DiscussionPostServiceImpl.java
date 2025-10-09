package com.wo.module.discussion.service;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.discussion.dao.DiscussionPostDao;
import com.wo.module.discussion.model.DiscussionPost;

@Transactional
@Service("discussionPostService")
public class DiscussionPostServiceImpl implements DiscussionPostService, Serializable{

	private static final long serialVersionUID = 1631651979686605781L;

	@Autowired
	@Qualifier("discussionPostDao")
	private DiscussionPostDao discussionPostDao;

	@Override
	public DiscussionPost findById(Long discussionPostId) {
		return discussionPostDao.getById(discussionPostId);
	}
	
	public DiscussionPostDao getDiscussionPostDao() {
		return discussionPostDao;
	}

	public void setDiscussionPostDao(DiscussionPostDao discussionPostDao) {
		this.discussionPostDao = discussionPostDao;
	}

}
