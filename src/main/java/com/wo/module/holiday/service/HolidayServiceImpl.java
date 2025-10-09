/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.holiday.service;

import java.util.Date;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.holiday.dao.HolidayDao;
import com.wo.module.holiday.model.Holiday;

@Transactional
@Service("holidayService")
public class HolidayServiceImpl implements HolidayService {
    @Autowired
    @Qualifier("holidayDao")
    private HolidayDao holidayDao;

	public HolidayDao getHolidayDao() {
		return holidayDao;
	}

	public void setHolidayDao(HolidayDao holidayDao) {
		this.holidayDao = holidayDao;
	}
    
	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public List<Holiday> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return holidayDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return holidayDao.searchCountData(searchCriteria);
    }

	public void save(Holiday holiday) {
		holidayDao.save(holiday);
	}
	
	public void update(Holiday holiday) {
		holidayDao.update(holiday);
	}
	
	public void delete(Holiday holiday) {
		holidayDao.delete(holiday);
	}
  
    public Holiday findById(Long id) {
    	return holidayDao.getById(id);
    }

	@Override
	@Transactional(readOnly=true)
	public Integer getHolidayByName(String name) throws Exception {
		return holidayDao.getHolidayByName(name);
	}

	@Override
	public Integer getEditHolidayByIdAndName(Long holidayId, String holidayName) throws Exception {
		return holidayDao.getEditHolidayByIdAndName(holidayId, holidayName);
	}

	@Override
	public Holiday getHolidayBySameData(Long holidayId, String holidayName) throws Exception {
		return holidayDao.getHolidayBySameData(holidayId, holidayName);
	}
	
	public Holiday getHolidayDataByName(String name){
		return holidayDao.getHolidayDataByName(name);
	}

	@Override
	public Boolean isAvailableDate(Date date) {
		return holidayDao.isAvailableDate(date);
	}
        
}
