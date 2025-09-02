package com.wo.module.qaFE.service;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.qa.model.QA;
import com.wo.module.qaFE.dao.QAFEDao;
import com.wo.module.qaFE.vo.QAFEVo;

@Transactional
@Service("qaFEService")
public class QAFEServiceImpl implements QAFEService, Serializable{

	private static final long serialVersionUID = -886046711818298321L;
	
	@Autowired
	@Qualifier("qaFEDao")
	private QAFEDao qaFEDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<QAFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return qaFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return qaFEDao.searchCountData(searchCriteria);
	}

	@Override
	public void save(QA qa) {
		qaFEDao.save(qa);
	}

	@Override
	public void update(QA qa) {
		qaFEDao.update(qa);
	}

	@Override
	public void delete(QA qa) {
		qaFEDao.delete(qa);
	}
	
	@Override
	public QA findById(Long qaId) {
		return qaFEDao.getById(qaId);
	}
	
	@Override
	public Number getTicketNo() {
		return qaFEDao.getTicketNo();
	}
	
	public String getLastTicketNo(){
		return qaFEDao.getLastTicketNo();
	}
	
	public Number getIsAdmin(Long userId){
		return qaFEDao.getIsAdmin(userId);
	}
	
	public String getCategoryIsAdmin(Long userId){
		return qaFEDao.getCategoryIsAdmin(userId);
	}
	
	public List<QA> getQAByParentId(Long fromQnaId){
		return qaFEDao.getQAByParentId(fromQnaId);
	}
	
	public List<QA> getQANotAnswerByParentId(Long fromQnaId){
		return qaFEDao.getQANotAnswerByParentId(fromQnaId);
	}
	
	public QAFEDao getQaFEDao() {
		return qaFEDao;
	}

	public void setQaFEDao(QAFEDao qaFEDao) {
		this.qaFEDao = qaFEDao;
	}

	@Override
	public Number getIsAdminByCategory(Long userId, String category) {
		return qaFEDao.getIsAdminByCategory(userId, category);
	}

	@Override
	public Number getCheckTiket(String ticketNo) {
		return qaFEDao.getCheckTiket(ticketNo);
	}

}
