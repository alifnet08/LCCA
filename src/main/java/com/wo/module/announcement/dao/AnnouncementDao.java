package com.wo.module.announcement.dao;

import com.wo.module.announcement.model.Announcement;
import com.wo.module.announcement.vo.AnnouncementVo;
import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;

public interface AnnouncementDao extends GenericDAO<Announcement, Long>, RetrieverDataPage<AnnouncementVo>{

}
