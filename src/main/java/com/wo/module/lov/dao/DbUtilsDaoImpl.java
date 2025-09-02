/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.lov.dao;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;

/**
 *
 * @author hendra
 */
@Repository("dbUtilsDao")
public class DbUtilsDaoImpl 
    extends GenericDAOHibernate<Object, Serializable> implements DbUtilsDao {
    private static Logger logger = Logger.getLogger(DbUtilsDaoImpl.class);

    
    @SuppressWarnings("rawtypes")
	@Override
    public List hqlResults(String hql, 
            Integer first, Integer pageSize,
            Map <String, Object> params) {
        
        Query query = getSession().createQuery(hql);
        
        for (Map.Entry<String, Object> entry : params.entrySet()) {            
            query.setParameter(entry.getKey(), entry.getValue());            
        }
        
        if (first != null) {
            query.setFirstResult(first);
        }
        if (pageSize != null) {
            query.setMaxResults(pageSize);            
        }
        
        try {
            List result = 
                    query.getResultList();         
            return result;
            
        } catch (Exception ex) {
            logger.error("Error querying hql data [" + ex.getMessage() + "]", ex);
            throw new RuntimeException("Error querying hql data [" + ex.getMessage() + "]", ex);
        }
    }

    @SuppressWarnings("rawtypes")
	@Override
    public List sqlResults(String sql, Integer first, Integer pageSize, Map <String, Object> params) {
        
        
    	Query query = getSession().createSQLQuery(sql);
        if (first != null) {
            query.setFirstResult(first);            
        }
        if (pageSize != null) {
            query.setMaxResults(pageSize);            
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {            
            query.setParameter(entry.getKey(), entry.getValue());            
        }
        
        try {
            return query.getResultList(); 
        } catch (Exception ex) {
            logger.error("Error querying search sql data [" + ex.getMessage() + "]", ex);
            throw new RuntimeException("Error querying sql data [" + ex.getMessage() + "]", ex);
        }
        
    }

    
    @Override
    public Object hqlUniqueResult(String hql, Map <String, Object> params) {        
        Query query = getSession().createQuery(hql);

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());            
        }
 
        try {
            Number res = (Number) query.getSingleResult();        
            return res.longValue();
        } catch (Exception ex) {
            logger.error("Error querying unique result hql [" + ex.getMessage() + "]", ex);
            throw new RuntimeException("Error querying unique result hql [" + ex.getMessage() + "]", ex);
        }
    }

    @Override
    public Object sqlUniqueResult(String sql, Map <String, Object> params) {
        
    	Query query = getSession().createSQLQuery(sql);
        
        for (Map.Entry<String, Object> entry : params.entrySet()) {            
            query.setParameter(entry.getKey(), entry.getValue());            
        }
 
        try {
            Object res = query.getSingleResult();
            if (res instanceof Number) {
                return ((Number)res).longValue();
            } else {
                return res;
            }
        } catch (Exception ex) {
            logger.error("Error querying unique result sql [" + ex.getMessage() + "]", ex);
            throw new RuntimeException("Error querying unique result sql [" + ex.getMessage() + "]", ex);
        }
        
    }

    
    
}
