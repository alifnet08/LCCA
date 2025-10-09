package com.wo.module.announcement.service;

import com.wo.module.announcement.model.Announcement;
import com.wo.module.announcement.vo.AnnouncementVo;
import com.wo.module.common.paging.RetrieverDataPage;

public interface AnnouncementService extends RetrieverDataPage<AnnouncementVo>{

	public void save(Announcement entity);
	
	public void update(Announcement entity);
	
	public void delete(Announcement entity);
	
	public Announcement findById(Long announcementId);
	
}
