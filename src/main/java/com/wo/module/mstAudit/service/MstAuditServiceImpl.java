package com.wo.module.mstAudit.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.mstAudit.dao.MstAuditDao;
import com.wo.module.mstAudit.model.MstAudit;
import com.wo.module.mstAudit.vo.MstAuditVO;

@Transactional
@Service("mstAuditService")
public class MstAuditServiceImpl implements MstAuditService{

	@Autowired
	@Qualifier("mstAuditDao")
	private MstAuditDao mstAuditDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<MstAuditVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return mstAuditDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return mstAuditDao.searchCountData(searchCriteria);
	}

	public MstAuditDao getMstAuditDao() {
		return mstAuditDao;
	}

	public void setMstAuditDao(MstAuditDao mstAuditDao) {
		this.mstAuditDao = mstAuditDao;
	}

	@Override
	public void save(MstAudit entity) {
		mstAuditDao.save(entity);
	}

	@Override
	public void update(MstAudit entity) {
		mstAuditDao.update(entity);
	}

	@Override
	public void delete(MstAudit entity) {
		mstAuditDao.delete(entity);
	}

	@Override
	public MstAudit findById(Long id) {
		return mstAuditDao.getById(id);
	}

	@Override
	public Integer countSameData(String templateNameIn) {
		return mstAuditDao.countSameData(templateNameIn);
	}

	@Override
	public Integer countSameDataById(Long id, String templateNameIn) {
		return mstAuditDao.countSameDataById(id, templateNameIn);
	}

	@Override
	public List<MstAudit> getAllMstAuditData() {
		return mstAuditDao.getAllMstAuditData();
	}

	@Override
	public MstAuditVO getSingleDataMstAudit(Long mstAuditId) {
		return mstAuditDao.getSingleData(mstAuditId);
	}
	
	@Override
    public Boolean isUsedInTransaction(Long id) {
    	return mstAuditDao.isUsedInTransaction(id);
    }
	
}
