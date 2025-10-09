/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.holiday.dao;

import java.util.Date;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.holiday.model.Holiday;
/**
 *
 * @author hendra
 * 
 * Modification Alex
 */
public interface HolidayDao extends  GenericDAO<Holiday, Long>, RetrieverDataPage<Holiday>{

	public Integer getHolidayByName(String name) throws Exception;
	
	public Integer getEditHolidayByIdAndName(Long holidayId, String holidayName) throws Exception;
	
	public Holiday getHolidayBySameData(Long holidayId, String holidayName) throws Exception;
	
	public Holiday getHolidayDataByName(String name);
	
	public Boolean isAvailableDate(Date date);
}
