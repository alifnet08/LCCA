/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.calendar.dao;

import com.wo.module.calendar.model.Calendar;
import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;

/**
 *
 * @author hendra
 */
public interface CalendarDao extends GenericDAO<Calendar, Long>, RetrieverDataPage<Calendar> {
	
}
