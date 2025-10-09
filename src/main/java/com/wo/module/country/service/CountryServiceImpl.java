package com.wo.module.country.service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.country.dao.CountryDao;
import com.wo.module.country.model.Country;
import com.wo.module.country.vo.CountryVO;

@Transactional
@Service("countryService")
public class CountryServiceImpl implements CountryService, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 676240516737330780L;
	static Logger logger = Logger.getLogger(CountryServiceImpl.class);
	
	@Autowired
	@Qualifier("countryDao")
	private CountryDao countryDao;

	@SuppressWarnings("rawtypes")
	@Override
	public List<CountryVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return countryDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return countryDao.searchCountData(searchCriteria);
	}
	
	@Override
	public void update(List<CountryVO> countryFileList, String userCode, String fileName, 
			Timestamp buttonPressedDate) throws Exception {

		List<Country> countryDBList = countryDao.findAll();
		
		/*IF FIRST TIME INPUT*/
		if(countryDBList == null) {
			for(CountryVO countryFile : countryFileList) {
				Country country = new Country();
				country.setCountryName(countryFile.getNegara());
				country.setRiskRating(countryFile.getRiskRating());
				
				country.setEnabledFlag(CommonConstants.Y);
				country.setCreatedBy(userCode);
				country.setCreationDate(buttonPressedDate);
				
				country.setFileName(fileName);
				country.setUploadDate(buttonPressedDate);
				
				countryDao.save(country);
				countryDao.flush();
			}
		/*IF NOT FIRST TIME INPUT*/
		}else {
			countryDao.deleteAll();
			
			for(CountryVO countryFile : countryFileList) {
				Country country = new Country();
				country.setCountryName(countryFile.getNegara());
				country.setRiskRating(countryFile.getRiskRating());
				
				country.setEnabledFlag(CommonConstants.Y);
				country.setCreatedBy(userCode);
				country.setCreationDate(buttonPressedDate);
				
				country.setFileName(fileName);
				country.setUploadDate(buttonPressedDate);
				
				countryDao.save(country);
				countryDao.flush();
			}
		}	
	}
	
	@Override
	public String getFileName() {
		return countryDao.getFileName();
	}

	public CountryDao getCountryDao() {
		return countryDao;
	}

	public void setCountryDao(CountryDao countryDao) {
		this.countryDao = countryDao;
	}
}
