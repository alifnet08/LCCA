package com.wo.module.trcLock.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.trcLock.model.TrcLock;

public interface TrcLockDao extends GenericDAO<TrcLock, Long> {

	public Boolean isCheckLockIsEmpty();
	public TrcLock findLastUserLockMenu(String menu);
	
}
