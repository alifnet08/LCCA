package com.wo.module.regulationSocialization.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupTrc;

public interface SocializationPICFollowupTrcDao extends GenericDAO<SocializationPICFollowupTrc, Long> {

	public List<SocializationPICFollowupTrc> getPICFollowupTrcBySocializationId(Long socializationId) throws Exception;
}
