/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.qaAdmin.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.qa.model.QA;
import com.wo.module.qaAdmin.vo.QAAdminVo;

/**
 *
 * @author hendra
 * 
 * 
 */
public interface QAAdminDao extends GenericDAO<QA, Long>, RetrieverDataPage<QAAdminVo> {

	@SuppressWarnings("rawtypes")
	List<QA> searchDataXls(List<? extends SearchObject> searchCriteria);

}
