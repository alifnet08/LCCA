package com.wo.module.announcement.service;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.announcement.dao.AnnouncementDao;
import com.wo.module.announcement.model.Announcement;
import com.wo.module.announcement.vo.AnnouncementVo;
import com.wo.module.common.paging.SearchObject;

@Transactional
@Service("announcementService")
public class AnnouncementServiceImpl implements AnnouncementService, Serializable{

	private static final long serialVersionUID = -3174498646775193139L;

	@Autowired
	@Qualifier("announcementDao")
	private AnnouncementDao announcementDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<AnnouncementVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return announcementDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return announcementDao.searchCountData(searchCriteria);
	}

	@Override
	public void save(Announcement entity) {
		announcementDao.save(entity);
	}

	@Override
	public void update(Announcement entity) {
		announcementDao.update(entity);
	}

	@Override
	public void delete(Announcement entity) {
		announcementDao.delete(entity);
	}
	
	@Override
	public Announcement findById(Long announcementId) {
		return announcementDao.getById(announcementId); 
	}
	
	public AnnouncementDao getAnnouncementDao() {
		return announcementDao;
	}

	public void setAnnouncementDao(AnnouncementDao announcementDao) {
		this.announcementDao = announcementDao;
	}

}
