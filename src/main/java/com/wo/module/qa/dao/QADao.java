/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.qa.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.qa.model.QA;
/**
 *
 * @author hendra
 * 
 * 
 */
public interface QADao extends  GenericDAO<QA, Long>, RetrieverDataPage<QA>{
	public Number getTicketNo();
	public Number getCountQuestionNotAnswered();
	public QA getQAQuestionNotAnswered();
	
}
