package com.wo.module.DiscussionFE.service;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.DiscussionFE.dao.DiscussionFEDao;
import com.wo.module.DiscussionFE.vo.DiscussionFEVo;
import com.wo.module.DiscussionFE.vo.DiscussionPostFEVo;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.discussion.model.Discussion;

@Transactional
@Service("discussionFEService")
public class DiscussionFEServiceImpl implements DiscussionFEService, Serializable{

	private static final long serialVersionUID = 135921487575572874L;
	
	@Autowired
	@Qualifier("discussionFEDao")
	private DiscussionFEDao discussionFEDao;

	@SuppressWarnings("rawtypes")
	@Override
	public List<DiscussionFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return discussionFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return discussionFEDao.searchCountData(searchCriteria);
	}

	@Override
	public Discussion findById(Long discussionId) {
		return discussionFEDao.getById(discussionId);
	}
	
	@Override
	public List<DiscussionPostFEVo> getDiscussionPostDataById(Long discussionId) {
		return discussionFEDao.getDiscussionPostDataById(discussionId);
	}
	
	@Override
	public void update(Discussion entity) {
		discussionFEDao.update(entity);
	}
	
	@Override
	public Long getReplies(Long discussionId) {
		return discussionFEDao.getReplies(discussionId);
	}

	@Override
	public Long getViews(Long discussionId) {
		return discussionFEDao.getViews(discussionId);
	}
	
	public DiscussionFEDao getDiscussionFEDao() {
		return discussionFEDao;
	}

	public void setDiscussionFEDao(DiscussionFEDao discussionFEDao) {
		this.discussionFEDao = discussionFEDao;
	}

}
