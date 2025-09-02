package com.wo.module.announcementViewFE.dao;

import com.wo.module.announcement.model.Announcement;
import com.wo.module.announcementViewFE.vo.AnnouncementViewFEVo;
import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;

public interface AnnouncementViewFEDao extends GenericDAO<Announcement, Long>, RetrieverDataPage<AnnouncementViewFEVo>{

}
