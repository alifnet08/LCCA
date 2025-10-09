package com.wo.module.common.dao;

import java.io.Serializable;

import org.hibernate.Transaction;


public interface GenericDAOOther<T, ID extends Serializable> extends GenericDAO<T, ID> 
{
	void commit();
	Transaction beginTransaction();
	void close();
}
