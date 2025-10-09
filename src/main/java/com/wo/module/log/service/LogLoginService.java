package com.wo.module.log.service;

import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.log.model.LogLogin;

public interface LogLoginService extends RetrieverDataPage<LogLogin> {
	void save(LogLogin log) throws ConstraintViolationException, HibernateException, Exception;

	void update(LogLogin log) throws ConstraintViolationException, HibernateException, Exception;

	LogLogin findById(Long id);
}