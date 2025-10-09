package com.wo.module.cpsaPicLockHistory.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.cpsaPicLockHistory.model.CpsaPicLockHistory;

public interface CpsaPicLockHistoryService extends RetrieverDataPage<CpsaPicLockHistory> {

	void save(Long cpsaId, String nik);

}
