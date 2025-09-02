package com.wo.module.outgoingLetter.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.outgoingLetter.dao.OutgoingLetterDao;
import com.wo.module.outgoingLetter.model.OutgoingLetter;

@Transactional
@Service("outgoingLetterService")
public class OutgoingLetterServiceImpl implements OutgoingLetterService {

	@Autowired
    @Qualifier("outgoingLetterDao")
	private OutgoingLetterDao outgoingLetterDao;
	
	public OutgoingLetterDao getOutgoingLetterDao() {
		return outgoingLetterDao;
	}

	public void setOutgoingLetterDao(OutgoingLetterDao outgoingLetterDao) {
		this.outgoingLetterDao = outgoingLetterDao;
	}

	@Override
	public void save(OutgoingLetter outgoingLetter) {
		outgoingLetterDao.save(outgoingLetter);
	}

	@Override
	public void update(OutgoingLetter outgoingLetter) {
		outgoingLetterDao.update(outgoingLetter);
	}

	@Override
	public void delete(OutgoingLetter outgoingLetter) {
		outgoingLetterDao.delete(outgoingLetter);
	}

	@Override
	public OutgoingLetter findById(Long id) {
		return outgoingLetterDao.findById(id);
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly=true)
	public List<OutgoingLetter> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return outgoingLetterDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly=true)
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return outgoingLetterDao.searchCountData(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<OutgoingLetter> searchDataXLS(List<? extends SearchObject> searchCriteria) {
		return outgoingLetterDao.searchDataXLS(searchCriteria);
	}

	@Override
	public Integer getOutgoingLetterByLetterInAndNo(String letterIn, String letterNo) throws Exception {
		return outgoingLetterDao.getOutgoingLetterByLetterInAndNo(letterIn, letterNo);
	}

	@Override
	public Integer getOutgoingLetterByLetterInAndNo(Long id, String letterIn, String letterNo) throws Exception {
		return outgoingLetterDao.getOutgoingLetterByLetterInAndNo(id, letterIn, letterNo);
	}
	
}