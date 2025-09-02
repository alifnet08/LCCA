package com.wo.module.outgoingLetterView.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.outgoingLetterView.model.OutgoingLetterAttachmentView;

public interface OutgoingLetterAttachmentViewDao extends GenericDAO<OutgoingLetterAttachmentView, Long>{

	public List<OutgoingLetterAttachmentView> getOutgoingLetterAttachmentViewByOutgoingLetterId(Long outgoingLetterViewId) throws Exception;
}
