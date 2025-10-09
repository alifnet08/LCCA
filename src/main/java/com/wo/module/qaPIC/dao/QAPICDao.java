/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.qaPIC.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.qa.model.QA;
/**
 *
 * @author hendra
 * 
 * 
 */
public interface QAPICDao extends  GenericDAO<QA, Long>, RetrieverDataPage<QA>{

	
}
