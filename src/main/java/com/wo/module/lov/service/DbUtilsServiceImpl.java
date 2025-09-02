/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.lov.service;


import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.lov.dao.DbUtilsDao;

/**
 *
 * @author hendra
 */
@Service("dbUtilsService")
public class DbUtilsServiceImpl implements DbUtilsService {
    @Autowired
    @Qualifier("dbUtilsDao")
    private DbUtilsDao dbUtilsDao;
    

    @Override
    @Transactional(readOnly=true)
    public Object hqlUniqueResult(String hql, Map<String, Object> params) {
        return dbUtilsDao.hqlUniqueResult(hql, params);
    }

    @Override
    @Transactional(readOnly=true)
    public Object sqlUniqueResult(String sql, Map<String, Object> params) {
        return dbUtilsDao.sqlUniqueResult(sql, params);
    }

    @SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public List hqlResults(String hql, Integer first, Integer pageSize, Map<String, Object> params) {
        return dbUtilsDao.hqlResults(hql, first, pageSize, params);
    }

    @SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public List sqlResults(String sql, Integer first, Integer pageSize, Map<String, Object> params) {
        return dbUtilsDao.sqlResults(sql, first, pageSize, params);
    }

    public DbUtilsDao getDbUtilsDao() {
        return dbUtilsDao;
    }

    public void setDbUtilsDao(DbUtilsDao dbUtilsDao) {
        this.dbUtilsDao = dbUtilsDao;
    }

    
}
