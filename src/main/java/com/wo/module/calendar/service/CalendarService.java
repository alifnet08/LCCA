/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.calendar.service;

import com.wo.module.calendar.model.Calendar;
import com.wo.module.common.paging.RetrieverDataPage;

public interface CalendarService extends RetrieverDataPage<Calendar> {

	public void save(Calendar entity);

	public void update(Calendar entity);

	public void delete(Calendar entity);

	public Calendar findById(Long id);
	
}
