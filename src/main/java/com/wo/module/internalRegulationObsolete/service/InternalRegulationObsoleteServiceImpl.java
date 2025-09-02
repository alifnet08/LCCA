package com.wo.module.internalRegulationObsolete.service;

import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.division.service.DivisionService;
import com.wo.module.internalRegulationObsolete.dao.InternalRegulationObsoleteDao;
import com.wo.module.internalRegulationObsolete.model.InternalRegulationObsolete;
import com.wo.module.internalRegulationObsolete.model.InternalRegulationObsoleteEmailTmp;
import com.wo.module.internalRegulationObsolete.model.InternalRegulationObsoletePic;
import com.wo.module.internalRegulationObsolete.model.InternalRegulationTest;
import com.wo.module.internalRegulationObsolete.vo.InternalRegulationObsoleteVo;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.user.dao.UserDao;
import com.wo.module.user.model.User;

@Transactional
@Service("internalRegulationObsoleteService")
public class InternalRegulationObsoleteServiceImpl implements InternalRegulationObsoleteService {
	    
    @Autowired
    @Qualifier("userDao")
    private UserDao userDao;
        
    @Autowired
    @Qualifier("parameterDetailService")
    private ParameterDetailService parameterDetailService;
    
    @Autowired
    @Qualifier("divisionService")
    private DivisionService divisionService;
    
    @Autowired
    @Qualifier("internalRegulationObsoleteDao")
    private InternalRegulationObsoleteDao internalRegulationObsoleteDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}

	public InternalRegulationObsoleteDao getInternalRegulationObsoleteDao() {
		return internalRegulationObsoleteDao;
	}

	public void setInternalRegulationObsoleteDao(InternalRegulationObsoleteDao internalRegulationObsoleteDao) {
		this.internalRegulationObsoleteDao = internalRegulationObsoleteDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<InternalRegulationObsoleteVo> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return internalRegulationObsoleteDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return internalRegulationObsoleteDao.searchCountData(searchCriteria);
	}

	@Override
	public List<InternalRegulationTest> findAllInternalRegulationTest() {
		List<InternalRegulationTest> intRegulationTestList = 
				internalRegulationObsoleteDao.findAllInternalRegulationTest();
		
		return intRegulationTestList;
	}

	@Override
	public void save(InternalRegulationObsolete regulationObsolete) {
		internalRegulationObsoleteDao.save(regulationObsolete);
	}

	@Override
	public void update(InternalRegulationObsolete regulationObsolete) {
		internalRegulationObsoleteDao.update(regulationObsolete);
	}

	@Override
	public InternalRegulationTest findInternalRegulationTestById(Long internalRegulationId) {
		InternalRegulationTest result = 
				internalRegulationObsoleteDao.findInternalRegulationTestById(internalRegulationId);
		return result;
	}

	@Override
	public InternalRegulationObsolete findById(Long idLong) {
		return internalRegulationObsoleteDao.findById(idLong);
	}

	@Override
	public boolean checkIfExistByIrgId(Long irgId) {
		return internalRegulationObsoleteDao.checkIfExistByIrgId(irgId);
	}

	@Override
	public List<String> getDataObsoleteTitle(String query) {
		return internalRegulationObsoleteDao.getDataObsoleteTitle(query);
	}

	@Override
	public void saveData(InternalRegulationObsolete regulationObsolete, FacesUtil facesUtil) {
		if (regulationObsolete.getInternalRegulationObsoletePicKonversis() != null) {
			for (int i = 0; i < regulationObsolete.getInternalRegulationObsoletePicKonversis().size(); i++) {
				InternalRegulationObsoletePic pic = regulationObsolete
						.getInternalRegulationObsoletePicKonversis().get(i);
				
				pic.setDivision(divisionService.findById(pic.getDivisionId()));
				pic.setInternalRegulasiObsolete(regulationObsolete);

				if (StringUtils.isEmpty(pic.getCreatedBy())) {
					pic.setCreatedBy(facesUtil.retrieveUserLogin());
					pic.setCreationDate(new Timestamp(System.currentTimeMillis()));
					pic.setEnabledFlag("Y");
					pic.setDelId(Long.valueOf(0));
				}else {
					pic.setLastUpdateBy(facesUtil.retrieveUserLogin());
					pic.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
				}	
				
				if(pic.getIsEditable()) { //true
					InternalRegulationObsoleteEmailTmp picEmail = new InternalRegulationObsoleteEmailTmp();
					
					picEmail.setIrgObsoletePic(pic);
				}
			}
		}

		if (regulationObsolete.getInternalRegulasiObsoleteId() != null) {
			regulationObsolete.setLastUpdateBy(facesUtil.retrieveUserLogin());
			regulationObsolete.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
			update(regulationObsolete);
		} else {
			regulationObsolete.setCreatedBy(facesUtil.retrieveUserLogin());
			regulationObsolete.setCreationDate(new Timestamp(System.currentTimeMillis()));
			regulationObsolete.setDelId(Long.valueOf(0));
			regulationObsolete.setEnabledFlag("Y");
			save(regulationObsolete);
		}
	}

	@Override
	public void delete(InternalRegulationObsolete irgObsolete) {
		internalRegulationObsoleteDao.delete(irgObsolete);
	}

	@Override
	public boolean checkIRGObsoleteByObsoleteTitle(String judulObsolete) {
		return internalRegulationObsoleteDao.checkIRGObsoleteByObsoleteTitle(judulObsolete);
	}

	@Override
	public List<User> findAllUniquePicIrgNames() {
		return internalRegulationObsoleteDao.findAllUniquePicIrgNames();
	}

	@Override
	public List<InternalRegulationObsoleteVo> getAllData() {
		return internalRegulationObsoleteDao.getAllData();
	}

	@Override
	public boolean checkIRGObsoleteByObsoleteNum(String nomorObsolete) {
		return internalRegulationObsoleteDao.checkIRGObsoleteByObsoleteNum(nomorObsolete);
	}
}
