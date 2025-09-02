/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulationApproval.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.externalRegulation.dao.RegulationDao;
import com.wo.module.externalRegulation.dao.RegulationMstDao;
import com.wo.module.externalRegulation.model.Regulation;
import com.wo.module.externalRegulation.model.RegulationMst;
import com.wo.module.externalRegulationApproval.dao.ExternalRegulationApprovalDao;
import com.wo.module.externalRegulationApproval.dao.RegulationApprovalDao;
import com.wo.module.externalRegulationApproval.model.ExternalRegulationApproval;
import com.wo.module.externalRegulationApproval.model.RegulationApproval;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.user.dao.UserDao;
import com.wo.module.user.model.User;

@Transactional
@Service("externalRegulationApprovalService")
public class ExternalRegulationApprovalServiceImpl implements ExternalRegulationApprovalService {
    @Autowired
    @Qualifier("externalRegulationApprovalDao")
    private ExternalRegulationApprovalDao externalRegulationApprovalDao;
    
    @Autowired
    @Qualifier("regulationDao")
    private RegulationDao regulationDao;
    
    @Autowired
    @Qualifier("userDao")
    private UserDao userDao;
    
    @Autowired
    @Qualifier("regulationApprovalDao")
    private RegulationApprovalDao regulationApprovalDao;
    
    @Autowired
    @Qualifier("regulationMstDao")
    private RegulationMstDao regulationMstDao;
    
    @Autowired
    @Qualifier("parameterDetailService")
    private ParameterDetailService parameterDetailService;

	

	@SuppressWarnings("rawtypes")
	@Override
    
    public List<ExternalRegulationApproval> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return externalRegulationApprovalDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return externalRegulationApprovalDao.searchCountData(searchCriteria);
    }
	
    @Transactional(rollbackOn = { Exception.class})
	public void processApprove(Regulation regulation, String note, String nik, String statusReg, String statusApp) throws Exception {
		Regulation regulationNew = regulationDao.getById(regulation.getRegulationId());
		regulationNew.setStatus(statusReg);
		regulationNew.setLastUpdateBy(nik);
		regulationNew.setLastUpdateDate(new Timestamp(new Date().getTime()));

		List<RegulationApproval> approvalList = new ArrayList<RegulationApproval>();

		RegulationApproval ra = new RegulationApproval();
		ra.setRegulation(regulationNew);
		User user = userDao.getUserByNik(nik);
		ra.setUser(user);
		ra.setApprovalDate(new Date());
		ra.setApprovalNote(note);
		ra.setApprovalStatus(parameterDetailService.getParameterDetailByParamDtlCode(statusApp));
		ra.setLastUpdateBy(nik);
		ra.setLastUpdateDate(new Timestamp(new Date().getTime()));
		ra.setCreatedBy(nik);
		ra.setCreationDate(new Timestamp(new Date().getTime()));
		ra.setDelId(new Long(0));
		ra.setEnabledFlag(Constants.CONSTANT_YES);
		approvalList.add(ra);

		if (regulationNew.getRegulationApprovals() == null) {
			regulationNew.setRegulationApprovals(approvalList);
		} else {
			regulationNew.getRegulationApprovals().clear();
			regulationNew.getRegulationApprovals().add(ra);
		}
		
		if(statusReg.equals("DATA_DELETED")){
			regulationNew.setEnabledFlag(Constants.CONSTANT_NO);
			RegulationMst regulationMst = regulationMstDao.getById(regulation.getRegulationId());
			if(regulationMst!=null && regulationMst.getRegulationId()!=null){
				regulationMst.setEnabledFlag(Constants.CONSTANT_NO);
				regulationMst.setStatus(statusReg);
				regulationMst.setLastUpdateBy(nik);
				regulationMst.setLastUpdateDate(new Timestamp(new Date().getTime()));
				regulationMstDao.update(regulationMst);
			}
		}
		
		if(statusReg.equals("DATA_REVISE")){
			regulationNew.setNameIn(regulationNew.getNameIn().replaceAll("(deleted)", ""));
		}

		Boolean flagValid = true;
		try {
			regulationDao.update(regulationNew);
		} catch (Exception e) {
			e.printStackTrace();
			flagValid = false;
		} finally {
			if (flagValid) {
				//regulationDao.procedureUpdateTmpTrackRecord(regulation.getRegulationId());
			}
		}
	}
    
    /*@Transactional(rollbackOn = { Exception.class})
    public void processRevise(Regulation regulation,String note, String nik) throws Exception {
		
		Regulation regulationNew = regulationDao.getById(regulation.getRegulationId());
    	regulationNew.setStatus("DATA_REVISE");
    	regulationNew.setLastUpdateBy(nik);
    	regulationNew.setLastUpdateDate(new Timestamp(new Date().getTime()));
    	
    	List<RegulationApproval> approvalList = new ArrayList<RegulationApproval>();
    	
    	RegulationApproval ra = new RegulationApproval();
    	ra.setRegulation(regulationNew);
    	User user = userDao.getUserByNik(nik);
    	ra.setUser(user);
    	ra.setApprovalDate(new Date());
    	ra.setApprovalNote(note);
    	ra.setApprovalStatus(parameterDetailService.getParameterDetailByParamDtlCode("STATUS_REVISE")); 
    	ra.setLastUpdateBy(nik);
    	ra.setLastUpdateDate(new Timestamp(new Date().getTime()));
    	ra.setCreatedBy(nik);
    	ra.setCreationDate(new Timestamp(new Date().getTime()));
		ra.setDelId(new Long(0));
		ra.setEnabledFlag(Constants.CONSTANT_YES);
		
		approvalList.add(ra);
		
		if(regulationNew.getRegulationApprovals() == null) {
			regulationNew.setRegulationApprovals(approvalList);
		}else {
			regulationNew.getRegulationApprovals().add(ra);
		}
		
		regulationDao.update(regulationNew);
    
    }*/
    
    public String procedureUpdateTmpTrackRecord(Long internalId) throws Exception {
    	return regulationDao.procedureUpdateTmpTrackRecord(internalId);
	}
        
    public ExternalRegulationApprovalDao getExternalRegulationApprovalDao() {
		return externalRegulationApprovalDao;
	}

	public void setExternalRegulationApprovalDao(ExternalRegulationApprovalDao externalRegulationApprovalDao) {
		this.externalRegulationApprovalDao = externalRegulationApprovalDao;
	}
	

	public RegulationDao getRegulationDao() {
		return regulationDao;
	}

	public void setRegulationDao(RegulationDao regulationDao) {
		this.regulationDao = regulationDao;
	}

	public RegulationApprovalDao getRegulationApprovalDao() {
		return regulationApprovalDao;
	}

	public void setRegulationApprovalDao(RegulationApprovalDao regulationApprovalDao) {
		this.regulationApprovalDao = regulationApprovalDao;
	}

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

	public RegulationMstDao getRegulationMstDao() {
		return regulationMstDao;
	}

	public void setRegulationMstDao(RegulationMstDao regulationMstDao) {
		this.regulationMstDao = regulationMstDao;
	}
	
	
	
}
