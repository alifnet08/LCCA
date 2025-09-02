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
import com.wo.module.log.dao.LogDAO;
import com.wo.module.log.model.LogHeader;

@Transactional
@Service("logService")
public class LogServiceImpl implements LogService {
	static Logger logger = Logger.getLogger(LogServiceImpl.class);
	
	@Autowired
	@Qualifier("logDAO")
	private LogDAO logDAO;

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return logDAO.searchCountData(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<LogHeader> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return logDAO.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	public LogHeader findById(Long id) {
		return logDAO.findById(id);
	}

	@Override
	public void save(LogHeader log) throws ConstraintViolationException, HibernateException, Exception {
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
	public void update(LogHeader log) throws ConstraintViolationException, HibernateException, Exception {
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

	@Override
	public void delete(LogHeader log) throws Exception {
		logDAO.delete(log);
	}

	public LogDAO getLogDAO() {
		return logDAO;
	}

	public void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}
}