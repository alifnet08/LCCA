package com.wo.module.litigation.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.litigation.dao.LitigationAttachmentDao;
import com.wo.module.litigation.model.LitigationAttachment;

@Transactional
@Service("litigationAttachmentService")
public class LitigationAttachmentServiceImpl implements LitigationAttachmentService, Serializable{

	private static final long serialVersionUID = 5521163208369554687L;
	
	@Autowired
	@Qualifier("litigationAttachmentDao")
	private LitigationAttachmentDao litigationAttachmentDao;

	@Override
	public List<LitigationAttachment> getLitigationAttachmentList(Long litigationId, Long attachId, String attachCode) {
		return litigationAttachmentDao.getLitigationAttachmentList(litigationId, attachId, attachCode);
	}

	public LitigationAttachmentDao getLitigationAttachmentDao() {
		return litigationAttachmentDao;
	}

	public void setLitigationAttachmentDao(LitigationAttachmentDao litigationAttachmentDao) {
		this.litigationAttachmentDao = litigationAttachmentDao;
	}

}
