/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationMonitoringVerification.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.parameter.dao.ParameterDetailDao;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.regulationMonitoring.dao.RegMonitoringTrcDAO;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpTrc;
import com.wo.module.regulationMonitoring.model.RegMonitoringTrc;
import com.wo.module.regulationMonitoringVerification.dao.RegMonitoringVerificationDAO;
import com.wo.module.regulationMonitoringVerification.vo.RegMonitoringVerificationSearchVO;
import com.wo.module.user.dao.UserDao;
import com.wo.module.user.model.User;

@Transactional
@Service("regMonitoringVerificationService")
public class RegMonitoringVerificationServiceImpl implements RegMonitoringVerificationService {
    @Autowired
    @Qualifier("regMonitoringVerificationDAO")
    private RegMonitoringVerificationDAO regMonitoringVerificationDAO;
    
    @Autowired
    @Qualifier("regMonitoringTrcDAO")
    private RegMonitoringTrcDAO regMonitoringTrcDAO;
    
    @Autowired
    @Qualifier("userDao")
    private UserDao userDao;
 
    @Autowired
    @Qualifier("parameterDetailDao")
    private ParameterDetailDao parameterDetailDao;
	

	@SuppressWarnings("rawtypes")
	@Override
    
    public List<RegMonitoringVerificationSearchVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return regMonitoringVerificationDAO.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return regMonitoringVerificationDAO.searchCountData(searchCriteria);
    }
	
    @SuppressWarnings("unused")
	@Transactional(rollbackOn = { Exception.class})
	public void processConfirm(RegMonitoringTrc regMonitoringTrc, User user) throws Exception {
		try {
			SimpleDateFormat sdf= new SimpleDateFormat("dd MMM yyyy");
			
			regMonitoringTrc.setLastUpdateBy(user.getNik());
			regMonitoringTrc.setLastUpdateDate(new Timestamp(new Date().getTime()));
			
			
			
			if (regMonitoringTrc.getRegMonitoringPICFollowUpTrcs() != null) {
				for (int i = 0; i < regMonitoringTrc.getRegMonitoringPICFollowUpTrcs().size(); i++) {
					RegMonitoringPICFollowUpTrc dtl = (RegMonitoringPICFollowUpTrc) regMonitoringTrc
							.getRegMonitoringPICFollowUpTrcs().get(i);
					
					if (dtl.getComplianceStatus() != null && dtl.getComplianceStatus().getParameterDtlCode() != null
						&& !dtl.getComplianceStatus().getParameterDtlCode().equals("")) {
						ParameterDetail complianceStatus = parameterDetailDao
								.getParameterDetailByParamDtlCode(dtl.getComplianceStatus().getParameterDtlCode());					
						dtl.setComplianceStatus(complianceStatus);
						//dtl.setComplianceNote(dtl.getComplianceNote());
						dtl.setComplianceBy(user);
						dtl.setComplianceDate(new Date());		
						
						if (ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_OPEN
								.equals(complianceStatus.getParameterDtlCode())) {
							ParameterDetail followupStatus = parameterDetailDao
									.getParameterDetailByParamDtlCode(
											ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_INPROGRESS);
							dtl.setFollowUpStatus(followupStatus);
						}
						
						dtl.setLastUpdateBy(user.getNik());
						dtl.setLastUpdateDate(new Timestamp(new Date().getTime()));
						dtl.setDelId(new Long(0));
						dtl.setEnabledFlag(Constants.CONSTANT_YES);
					}
				}
			}

			regMonitoringTrcDAO.update(regMonitoringTrc);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public ParameterDetailDao getParameterDetailDao() {
		return parameterDetailDao;
	}

	public void setParameterDetailDao(ParameterDetailDao parameterDetailDao) {
		this.parameterDetailDao = parameterDetailDao;
	}
	
	
	
}
