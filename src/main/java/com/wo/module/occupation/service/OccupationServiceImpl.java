package com.wo.module.occupation.service;

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
import com.wo.module.occupation.dao.OccupationDao;
import com.wo.module.occupation.model.Occupation;
import com.wo.module.occupation.vo.OccupationVO;

@Transactional
@Service("occupationService")
public class OccupationServiceImpl implements OccupationService, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 676240516737330780L;
	static Logger logger = Logger.getLogger(OccupationServiceImpl.class);
	
	@Autowired
	@Qualifier("occupationDao")
	private OccupationDao occupationDao;

	@SuppressWarnings("rawtypes")
	@Override
	public List<OccupationVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return occupationDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return occupationDao.searchCountData(searchCriteria);
	}
	
	@Override
	public void update(List<OccupationVO> occupationFileList, String userCode, String fileName, Timestamp buttonPressedDate) throws Exception {

		List<Occupation> occupationDBList = occupationDao.findAll();
		
		/*IF FIRST TIME INPUT*/
		if(occupationDBList == null) {
			for(OccupationVO occupationFile : occupationFileList) {
				Occupation occupation = new Occupation();
				occupation.setOccupationName(occupationFile.getOccupation());
				occupation.setRiskRating(occupationFile.getRiskRating());
				
				occupation.setEnabledFlag(CommonConstants.Y);
				occupation.setCreatedBy(userCode);
				occupation.setCreationDate(buttonPressedDate);
				
				occupation.setFileName(fileName);
				occupation.setUploadDate(buttonPressedDate);
				
				occupationDao.save(occupation);
				occupationDao.flush();
			}
		/*IF NOT FIRST TIME INPUT*/
		}else {
			occupationDao.deleteAll();
			
			for(OccupationVO occupationFile : occupationFileList) {
				Occupation occupation = new Occupation();
				occupation.setOccupationName(occupationFile.getOccupation());
				occupation.setRiskRating(occupationFile.getRiskRating());
				
				occupation.setEnabledFlag(CommonConstants.Y);
				occupation.setCreatedBy(userCode);
				occupation.setCreationDate(buttonPressedDate);
				
				occupation.setFileName(fileName);
				occupation.setUploadDate(buttonPressedDate);
				
				occupationDao.save(occupation);
				occupationDao.flush();
			}
		}	
	}
	
	@Override
	public String getFileName() {
		return occupationDao.getFileName();
	}

	public OccupationDao getOccupationDao() {
		return occupationDao;
	}

	public void setOccupationDao(OccupationDao occupationDao) {
		this.occupationDao = occupationDao;
	}
}
