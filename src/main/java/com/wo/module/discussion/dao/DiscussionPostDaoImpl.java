package com.wo.module.discussion.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.discussion.model.DiscussionPost;

@Repository("discussionPostDao")
public class DiscussionPostDaoImpl extends GenericDAOHibernate<DiscussionPost, Long>
	implements DiscussionPostDao, Serializable{

	private static final long serialVersionUID = 5844779712454641353L;

}
