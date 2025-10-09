/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.division.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.division.model.Division;

/**
 *
 * @author hendra
 */
public interface DivisionDao extends GenericDAO<Division, Long>, RetrieverDataPage<Division> {
	
	public Number getCountDivisionByName(String divName);
	
	public Division getDivisionByName(String divName);
	
	public String getDivisionNameByDivisionId(Long divId);
}
