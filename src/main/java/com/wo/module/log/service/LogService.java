package com.wo.module.log.service;

import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.log.model.LogHeader;

public interface LogService extends RetrieverDataPage<LogHeader> {
	void save(LogHeader log) throws ConstraintViolationException, HibernateException, Exception;

	void update(LogHeader log) throws ConstraintViolationException, HibernateException, Exception;

	void delete(LogHeader log) throws Exception;

	LogHeader findById(Long id);
}