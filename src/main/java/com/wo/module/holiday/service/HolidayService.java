/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.holiday.service;

import java.util.Date;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.holiday.model.Holiday;

public interface HolidayService extends RetrieverDataPage<Holiday> {
    
	public void save(Holiday holiday); 
	
	public void update(Holiday holiday);
	
	public void delete(Holiday holiday);
  
    public Holiday findById(Long id) ;
    
    public Integer getHolidayByName(String name) throws Exception;
    
    public Integer getEditHolidayByIdAndName(Long holidayId, String holidayName) throws Exception;
    
    public Holiday getHolidayBySameData(Long holidayId, String holidayName) throws Exception;
    
    public Holiday getHolidayDataByName(String name);
    
    public Boolean isAvailableDate(Date date);
}
