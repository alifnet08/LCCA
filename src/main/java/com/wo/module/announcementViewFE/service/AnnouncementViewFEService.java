package com.wo.module.announcementViewFE.service;

import com.wo.module.announcement.model.Announcement;
import com.wo.module.announcementViewFE.vo.AnnouncementViewFEVo;
import com.wo.module.common.paging.RetrieverDataPage;

public interface AnnouncementViewFEService extends RetrieverDataPage<AnnouncementViewFEVo>{

	public Announcement findById(Long announcementId);
	
}
