package com.wo.module.regulationSocialization.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupAttachmentTrc;

public interface SocializationPICFollowupAttachmentTrcDao extends  GenericDAO<SocializationPICFollowupAttachmentTrc, Long>{

	public List<SocializationPICFollowupAttachmentTrc> getPICFollowupAttachmentTrcBySocializationId(Long socializationId) throws Exception;
	
}
