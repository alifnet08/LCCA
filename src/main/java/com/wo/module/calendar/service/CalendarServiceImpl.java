/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.calendar.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.calendar.dao.CalendarDao;
import com.wo.module.calendar.model.Calendar;
import com.wo.module.common.paging.SearchObject;

@Transactional
@Service("calendarService")
public class CalendarServiceImpl implements CalendarService {
    @Autowired
    @Qualifier("calendarDao")
    private CalendarDao calendarDao;
    
	
	public CalendarDao getCalendarDao() {
		return calendarDao;
	}

	public void setCalendarDao(CalendarDao calendarDao) {
		this.calendarDao = calendarDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<Calendar> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws Exception {
		return calendarDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return calendarDao.searchCountData(searchCriteria);
	}
	
	public void save(Calendar entity) {
		calendarDao.save(entity);
	}
	
	public void update(Calendar entity) {
		calendarDao.update(entity);
	}
	
	public void delete(Calendar entity) {
		calendarDao.delete(entity);
	}
  
    public Calendar findById(Long id) {
    	return calendarDao.getById(id);
    }
    
   
        
}
