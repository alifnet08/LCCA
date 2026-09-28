package com.wo.module.notary.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.notary.model.NotaryHistory;

public interface NotaryHistoryDao extends GenericDAO<NotaryHistory, Long> {

	public List<NotaryHistory> getNotaryHistoryByNotaryId(Long notaryId) throws Exception;

}
