/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.qaCategory.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.qaCategory.model.QACategory;
/**
 *
 * @author hendra
 * 
 * 
 */
public interface QACategoryDAO extends  GenericDAO<QACategory, Long>, RetrieverDataPage<QACategory>{

	public List<QACategory> searchCategoryListByCode(String categoryCode) throws Exception;
	
	public Integer duplicate(String categoryCode, Long userId);
}
