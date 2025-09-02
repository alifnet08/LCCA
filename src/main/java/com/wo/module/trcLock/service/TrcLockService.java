package com.wo.module.trcLock.service;

import com.wo.module.trcLock.model.TrcLock;
import com.wo.module.user.model.User;

public interface TrcLockService {

	public Boolean isCheckLockIsEmpty();
	public void save(User user);
	public void update(TrcLock trcLock);
	public TrcLock findLastUserLockMenu(String menu);
	public TrcLock findById(Long lockId);
	
}