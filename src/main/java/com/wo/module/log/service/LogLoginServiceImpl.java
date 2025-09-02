package com.wo.module.log.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.log.dao.LogLoginDAO;
import com.wo.module.log.model.LogLogin;

@Transactional
@Service("logLoginService")
public class LogLoginServiceImpl implements LogLoginService {
	static Logger logger = Logger.getLogger(LogLoginServiceImpl.class);
	
	@Autowired
	@Qualifier("logLoginDAO")
	private LogLoginDAO logDAO;

	public LogLogin findById(Long id) {
		return logDAO.findById(id);
	}

	@Override
	public void save(LogLogin log) throws ConstraintViolationException, HibernateException, Exception {
		try {
			logDAO.save(log);
			logDAO.flush();
		} catch (ConstraintViolationException cx) {
			logDAO.rollback();
			throw cx;
		} catch (HibernateException hx) {
			logDAO.rollback();
			throw hx;
		} catch (Exception ex) {
			logDAO.rollback();
			throw ex;
		}
	}

	@Override
	public void update(LogLogin log) throws ConstraintViolationException, HibernateException, Exception {
		try {
			logDAO.update(log);
			logDAO.flush();
		} catch (ConstraintViolationException cx) {
			logDAO.rollback();
			throw cx;
		} catch (HibernateException hx) {
			logDAO.rollback();
			throw hx;
		} catch (Exception ex) {
			logDAO.rollback();
			throw ex;
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<LogLogin> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws Exception {
		return logDAO.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return logDAO.searchCountData(searchCriteria);
	}
}