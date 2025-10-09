package com.wo.module.trcLock.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.util.EntityUtil;
import com.wo.module.trcLock.dao.TrcLockDao;
import com.wo.module.trcLock.model.TrcLock;
import com.wo.module.user.model.User;

@Transactional
@Service("trcLockService")
public class TrcLockServiceImpl implements TrcLockService {

	@Autowired
	@Qualifier("trcLockDao")
	private TrcLockDao trcLockDao;
	
	@Override
	public Boolean isCheckLockIsEmpty() {
		return trcLockDao.isCheckLockIsEmpty();
	}

	@Override
	public void save(User user) {
		TrcLock entity = new TrcLock();
		
		entity.setLockModule("LITIGASI");
		entity.setUser(user);
		entity.setStartDate(new Timestamp(System.currentTimeMillis()));
		EntityUtil.setCreationInfo(entity, user.getNik());
		
		trcLockDao.save(entity);
	}

	@Override
	public TrcLock findLastUserLockMenu(String menu) {
		return trcLockDao.findLastUserLockMenu(menu);
	}

	@Override
	public void update(TrcLock trcLock) {
		trcLock.setEndDate(new Timestamp(System.currentTimeMillis()));
		EntityUtil.setUpdateInfo(trcLock, trcLock.getUser().getNik());
		trcLockDao.update(trcLock);
	}

	@Override
	public TrcLock findById(Long lockId) {
		return trcLockDao.findById(lockId);
	}

}
