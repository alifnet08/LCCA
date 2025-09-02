package com.wo.module.announcementViewFE.service;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.announcement.model.Announcement;
import com.wo.module.announcementViewFE.dao.AnnouncementViewFEDao;
import com.wo.module.announcementViewFE.vo.AnnouncementViewFEVo;
import com.wo.module.common.paging.SearchObject;

@Transactional
@Service("announcementViewFEService")
public class AnnouncementViewFEServiceImpl implements AnnouncementViewFEService, Serializable{

	private static final long serialVersionUID = 6987589350819530850L;

	@Autowired
	@Qualifier("announcementViewFEDao")
	private AnnouncementViewFEDao announcementViewFEDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<AnnouncementViewFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return announcementViewFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return announcementViewFEDao.searchCountData(searchCriteria);
	}

	@Override
	public Announcement findById(Long announcementId) {
		return announcementViewFEDao.getById(announcementId);
	}
	
	public AnnouncementViewFEDao getAnnouncementViewFEDao() {
		return announcementViewFEDao;
	}

	public void setAnnouncementViewFEDao(AnnouncementViewFEDao announcementViewFEDao) {
		this.announcementViewFEDao = announcementViewFEDao;
	}

}
