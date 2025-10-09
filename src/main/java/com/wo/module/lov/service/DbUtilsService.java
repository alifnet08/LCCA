/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.lov.service;

import java.util.List;
import java.util.Map;

/**
 *
 * @author hendra
 */
public interface DbUtilsService {
    Object hqlUniqueResult(String hql, Map <String, Object> params);
    Object sqlUniqueResult(String sql, Map <String, Object> params);

    @SuppressWarnings("rawtypes")
	List hqlResults(String hql, Integer first, Integer pageSize, Map <String, Object> params);
    @SuppressWarnings("rawtypes")
	List sqlResults(String sql, Integer first, Integer pageSize, Map <String, Object> params);
    
}
