package com.wo.module.common.dao;

import java.io.Serializable;


public interface GenericDAO<T, ID extends Serializable> 
{
	T findById(ID id);
	T getById(ID id);
	T save(T entity);
	T merge(T entity);
	void update(T entity);
	void delete(T entity);
	void evict(T entity);
	void flush();
	void clear();
	void rollback();
}
