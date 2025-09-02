/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationSocializationVerification.service;

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
import com.wo.module.regulationSocialization.dao.SocializationTrcDao;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupTrc;
import com.wo.module.regulationSocialization.model.SocializationTrc;
import com.wo.module.regulationSocializationApproval.dao.SocializationApprovalTmpDao;
import com.wo.module.regulationSocializationVerification.dao.RegulationSocializationVerificationDao;
import com.wo.module.regulationSocializationVerification.vo.RegulationSocializationVerificationSearchVO;
import com.wo.module.user.dao.UserDao;
import com.wo.module.user.model.User;

@Transactional
@Service("regulationSocializationVerificationService")
public class RegulationSocializationVerificationServiceImpl implements RegulationSocializationVerificationService {
    @Autowired
    @Qualifier("regulationSocializationVerificationDao")
    private RegulationSocializationVerificationDao regulationSocializationVerificationDao;
    
    @Autowired
    @Qualifier("socializationTrcDao")
    private SocializationTrcDao socializationTrcDao;
    
    @Autowired
    @Qualifier("userDao")
    private UserDao userDao;
    
    @Autowired
    @Qualifier("socializationApprovalTmpDao")
    private SocializationApprovalTmpDao socializationApprovalTmpDao;

    @Autowired
    @Qualifier("parameterDetailDao")
    private ParameterDetailDao parameterDetailDao;
	

	@SuppressWarnings("rawtypes")
	@Override
    
    public List<RegulationSocializationVerificationSearchVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return regulationSocializationVerificationDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return regulationSocializationVerificationDao.searchCountData(searchCriteria);
    }
	
    @SuppressWarnings("unused")
	@Transactional(rollbackOn = { Exception.class})
	public void processConfirm(SocializationTrc socializationTrc, User user) throws Exception {
		try {
			SimpleDateFormat sdf= new SimpleDateFormat("dd MMM yyyy");
			
			socializationTrc.setLastUpdateBy(user.getNik());
			socializationTrc.setLastUpdateDate(new Timestamp(new Date().getTime()));
			
			
			
			if (socializationTrc.getSocializationPICFollowupTrcs() != null) {
				for (int i = 0; i < socializationTrc.getSocializationPICFollowupTrcs().size(); i++) {
					SocializationPICFollowupTrc dtl = (SocializationPICFollowupTrc) socializationTrc
							.getSocializationPICFollowupTrcs().get(i);
					
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
							dtl.setFollowupStatus(followupStatus);
						}
						
						dtl.setLastUpdateBy(user.getNik());
						dtl.setLastUpdateDate(new Timestamp(new Date().getTime()));
						dtl.setDelId(new Long(0));
						dtl.setEnabledFlag(Constants.CONSTANT_YES);
					}
				}
			}

			socializationTrcDao.update(socializationTrc);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	public RegulationSocializationVerificationDao getRegulationSocializationVerificationDao() {
		return regulationSocializationVerificationDao;
	}

	public void setRegulationSocializationVerificationDao(RegulationSocializationVerificationDao regulationSocializationVerificationDao) {
		this.regulationSocializationVerificationDao = regulationSocializationVerificationDao;
	}

	public SocializationTrcDao getSocializationTrcDao() {
		return socializationTrcDao;
	}

	public void setSocializationTrcDao(SocializationTrcDao socializationTrcDao) {
		this.socializationTrcDao = socializationTrcDao;
	}

	public SocializationApprovalTmpDao getSocializationApprovalTmpDao() {
		return socializationApprovalTmpDao;
	}

	public void setSocializationApprovalTmpDao(SocializationApprovalTmpDao socializationApprovalTmpDao) {
		this.socializationApprovalTmpDao = socializationApprovalTmpDao;
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
