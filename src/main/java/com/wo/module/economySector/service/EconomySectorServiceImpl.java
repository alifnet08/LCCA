package com.wo.module.economySector.service;

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
import com.wo.module.economySector.dao.EconomySectorDao;
import com.wo.module.economySector.model.EconomySector;
import com.wo.module.economySector.vo.EconomySectorVO;

@Transactional
@Service("economySectorService")
public class EconomySectorServiceImpl implements EconomySectorService, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 676240516737330780L;
	static Logger logger = Logger.getLogger(EconomySectorServiceImpl.class);
	
	@Autowired
	@Qualifier("economySectorDao")
	private EconomySectorDao economySectorDao;

	@SuppressWarnings("rawtypes")
	@Override
	public List<EconomySectorVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return economySectorDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return economySectorDao.searchCountData(searchCriteria);
	}
	
	@Override
	public void update(List<EconomySectorVO> ecoSecFileList, String userCode, 
			String fileName, Timestamp buttonPressedDate) throws Exception {

		List<EconomySector> economySectorDBList = economySectorDao.findAll();
		
		/*IF FIRST TIME INPUT*/
		if(economySectorDBList == null) {
			for(EconomySectorVO economySectorFile : ecoSecFileList) {
				EconomySector economySector = new EconomySector();
				//economySector.setEconomySectorCode(economySectorFile.getEconomySectorCode());
				economySector.setEconomySectorName(economySectorFile.getEconomySector());
				economySector.setRiskRating(economySectorFile.getRiskRating());
				
				economySector.setEnabledFlag(CommonConstants.Y);
				economySector.setCreatedBy(userCode);
				economySector.setCreationDate(buttonPressedDate);
				
				economySector.setFileName(fileName);
				economySector.setUploadDate(buttonPressedDate);
				
				economySectorDao.save(economySector);
				economySectorDao.flush();
			}
		/*IF NOT FIRST TIME INPUT*/
		}else {
			economySectorDao.deleteAll();
			
			for(EconomySectorVO economySectorFile : ecoSecFileList) {
				EconomySector economySector = new EconomySector();
				//economySector.setEconomySectorCode(economySectorFile.getEconomySectorCode());
				economySector.setEconomySectorName(economySectorFile.getEconomySector());
				economySector.setRiskRating(economySectorFile.getRiskRating());
				
				economySector.setEnabledFlag(CommonConstants.Y);
				economySector.setCreatedBy(userCode);
				economySector.setCreationDate(buttonPressedDate);
				
				economySector.setFileName(fileName);
				economySector.setUploadDate(buttonPressedDate);
				
				economySectorDao.save(economySector);
				economySectorDao.flush();
			}
		}	
	}

	@Override
	public String getFileName() {
		return economySectorDao.getFileName();
	}

	public EconomySectorDao getEconomySectorDao() {
		return economySectorDao;
	}

	public void setEconomySectorDao(EconomySectorDao economySectorDao) {
		this.economySectorDao = economySectorDao;
	}

	
}
