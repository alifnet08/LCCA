package com.wo.module.outgoingLetter.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.outgoingLetter.model.OutgoingLetterAttachment;

public interface OutgoingLetterAttachmentDao extends GenericDAO<OutgoingLetterAttachment, Long>{
	
	public List<OutgoingLetterAttachment> getOutgoingLetterAttachmentByOutgoingLetterId(Long outgoingLetterId) throws Exception;
}