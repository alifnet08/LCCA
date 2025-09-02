package com.wo.module.DiscussionFE.dao;

import java.util.List;

import com.wo.module.DiscussionFE.vo.DiscussionFEVo;
import com.wo.module.DiscussionFE.vo.DiscussionPostFEVo;
import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.discussion.model.Discussion;

public interface DiscussionFEDao extends GenericDAO<Discussion, Long>, RetrieverDataPage<DiscussionFEVo>{

	public List<DiscussionPostFEVo> getDiscussionPostDataById(Long discussionId);
	
	public Long getReplies(Long discussionId);
	
	public Long getViews(Long discussionId);
	
}
