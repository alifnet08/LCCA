/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationSocializationApproval.service;

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
import com.wo.module.regulationSocialization.dao.SocializationTmpDao;
import com.wo.module.regulationSocialization.model.SocializationTmp;
import com.wo.module.regulationSocializationApproval.dao.RegulationSocializationApprovalDao;
import com.wo.module.regulationSocializationApproval.dao.SocializationApprovalTmpDao;
import com.wo.module.regulationSocializationApproval.model.SocializationApprovalTmp;
import com.wo.module.regulationSocializationApproval.vo.RegulationSocializationApprovalVO;
import com.wo.module.user.dao.UserDao;
import com.wo.module.user.model.User;

@Transactional
@Service("regulationSocializationApprovalService")
public class RegulationSocializationApprovalServiceImpl implements RegulationSocializationApprovalService {
    @Autowired
    @Qualifier("regulationSocializationApprovalDao")
    private RegulationSocializationApprovalDao regulationSocializationApprovalDao;
    
    @Autowired
    @Qualifier("socializationTmpDao")
    private SocializationTmpDao socializationTmpDao;
    
    @Autowired
    @Qualifier("userDao")
    private UserDao userDao;
    
    @Autowired
    @Qualifier("socializationApprovalTmpDao")
    private SocializationApprovalTmpDao socializationApprovalTmpDao;

	

	@SuppressWarnings("rawtypes")
	@Override
    
    public List<RegulationSocializationApprovalVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return regulationSocializationApprovalDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return regulationSocializationApprovalDao.searchCountData(searchCriteria);
    }
	
    @Transactional(rollbackOn = { Exception.class})
	public void processApprove(SocializationTmp socializationTmp, String note, String nik, String statusReg, String statusApp) {
		try {
			SocializationTmp socializationNew = socializationTmpDao.getById(socializationTmp.getSocializationId());
			socializationNew.setStatus(statusReg);
			socializationNew.setLastUpdateBy(nik);
			socializationNew.setLastUpdateDate(new Timestamp(new Date().getTime()));

			List<SocializationApprovalTmp> approvalList = new ArrayList<SocializationApprovalTmp>();

			SocializationApprovalTmp ra = new SocializationApprovalTmp();
			ra.setSocializationTmp(socializationNew);
			User user = userDao.getUserByNik(nik);
			ra.setUser(user);
			ra.setApprovalDate(new Date());
			ra.setApprovalNote(note);
			ra.setApprovalStatus(statusApp);
			ra.setLastUpdateBy(nik);
			ra.setLastUpdateDate(new Timestamp(new Date().getTime()));
			ra.setCreatedBy(nik);
			ra.setCreationDate(new Timestamp(new Date().getTime()));
			ra.setDelId(new Long(0));
			ra.setEnabledFlag(Constants.CONSTANT_YES);
			approvalList.add(ra);

			if (socializationNew.getSocializationApprovalTmps() == null) {
				socializationNew.setSocializationApprovalTmps(approvalList);
			} else {
				socializationNew.getSocializationApprovalTmps().clear();
				socializationNew.getSocializationApprovalTmps().add(ra);
			}

			socializationTmpDao.update(socializationNew);
			//regulationDao.procedureUpdateTmpTrackRecord(regulation.getRegulationId());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
    
   
        
    public RegulationSocializationApprovalDao getRegulationSocializationApprovalDao() {
		return regulationSocializationApprovalDao;
	}

	public void setRegulationSocializationApprovalDao(RegulationSocializationApprovalDao regulationSocializationApprovalDao) {
		this.regulationSocializationApprovalDao = regulationSocializationApprovalDao;
	}
	

	

	public SocializationTmpDao getSocializationTmpDao() {
		return socializationTmpDao;
	}

	public void setSocializationTmpDao(SocializationTmpDao socializationTmpDao) {
		this.socializationTmpDao = socializationTmpDao;
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
	
	
}
