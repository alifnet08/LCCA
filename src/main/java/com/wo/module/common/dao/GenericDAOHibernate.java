package com.wo.module.common.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


public abstract class GenericDAOHibernate<T, ID extends Serializable> implements GenericDAO<T, ID>
{
	private Class<T> persistentClass;
	
	@Autowired()
    @Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}
	
	
	@SuppressWarnings("unchecked")
	public GenericDAOHibernate()
	{
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	protected Session getSession()
	{
		return sessionFactory.getCurrentSession();
		
	}
	
	
	
	public Class<T> getPersistentClass()
	{
		return persistentClass;
	}

	public void clear()
	{
		getSession().clear();
	}
	
	public void evict(T entity)
	{
		try
		{
			getSession().evict(entity);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void rollback()
	{
		getSession().getTransaction().rollback();
	}

	public T findById(ID id)
	{
		return (T) getSession().load(getPersistentClass(), id);
	}

	public T getById(ID id)
	{
		return (T) getSession().get(getPersistentClass(), id);
	}
	
	

	public void flush()
	{
		getSession().flush();
	}

	public T save(T entity)
	{
		try
		{
			getSession().save(entity);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return entity;
	}
	
	

	@SuppressWarnings("unchecked")
	public T merge(T entity)
	{
		return (T) getSession().merge(entity);
	}

	public void update(T entity)
	{
		try
		{
			getSession().update(entity);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	

	public void delete(T entity)
	{
		try
		{
			getSession().delete(entity);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	public Date getCurrentTimestamp()
	{
		return new java.util.Date();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	

	

	
	

	
	
	
	
	
}