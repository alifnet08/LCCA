/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regMonitoringPICFpConfirmation.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.parameter.dao.ParameterDetailDao;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.regMonitoringPICFpConfirmation.dao.RegMonitoringPICFpConfirmationDAO;
import com.wo.module.regMonitoringPICFpConfirmation.vo.RegMonitoringPICFpConfirmationVO;
import com.wo.module.regulationMonitoring.dao.RegMonitoringTrcDAO;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpAttachmentTrc;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpTrc;
import com.wo.module.regulationMonitoring.model.RegMonitoringTrc;
import com.wo.module.user.dao.UserDao;
import com.wo.module.user.model.User;

@Transactional
@Service("regMonitoringPICFpConfirmationService")
public class RegMonitoringPICFpConfirmationServiceImpl implements RegMonitoringPICFpConfirmationService {
	@Autowired
	@Qualifier("regMonitoringPICFpConfirmationDAO")
	private RegMonitoringPICFpConfirmationDAO regMonitoringPICFpConfirmationDAO;

	@Autowired
	@Qualifier("regMonitoringTrcDAO")
	private RegMonitoringTrcDAO regMonitoringTrcDAO;

	@Autowired
	@Qualifier("parameterDetailDao")
	private ParameterDetailDao parameterDetailDao;

	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;

	@SuppressWarnings("rawtypes")
	@Override

	public List<RegMonitoringPICFpConfirmationVO> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return regMonitoringPICFpConfirmationDAO.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
    
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return regMonitoringPICFpConfirmationDAO.searchCountData(searchCriteria);
    }
	
    @SuppressWarnings("unused")
	@Transactional(rollbackOn = { Exception.class})
	@Override
	public void processConfirm(RegMonitoringTrc regMonitoringTrc, Long regMonitoringPicFollowUpTrcId, String keterangan,
			Date followupDate, List<UploadedFileWO> uploadFiles, User user) throws Exception {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

			RegMonitoringTrc regMonitoringNew = regMonitoringTrcDAO.getById(regMonitoringTrc.getRegMonitoringTrcId());

			regMonitoringNew.setLastUpdateBy(user.getNik());
			regMonitoringNew.setLastUpdateDate(new Timestamp(new Date().getTime()));

			if (regMonitoringNew.getRegMonitoringPICFollowUpTrcs() != null) {
				for (int i = 0; i < regMonitoringNew.getRegMonitoringPICFollowUpTrcs().size(); i++) {
					RegMonitoringPICFollowUpTrc pft = (RegMonitoringPICFollowUpTrc) regMonitoringNew
							.getRegMonitoringPICFollowUpTrcs().get(i);

					if(pft.getRegMonitoringPicFollowUpTrcId() == regMonitoringPicFollowUpTrcId.longValue()) {
						pft.setFollowUpDate(followupDate);
						pft.setFollowUpNote(keterangan);
						pft.setConfirmationDate(new Date());
						ParameterDetail followupStatus = parameterDetailDao.getParameterDetailByParamDtlCode(
								ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE);
						pft.setFollowUpStatus(followupStatus);
						pft.setFollowUpBy(user);

						List<RegMonitoringPICFollowUpAttachmentTrc> listDtl = new ArrayList<RegMonitoringPICFollowUpAttachmentTrc>();

						for (int x = 0; x < uploadFiles.size(); x++) {
							UploadedFileWO u = uploadFiles.get(x);
							
							RegMonitoringPICFollowUpAttachmentTrc dtl = new RegMonitoringPICFollowUpAttachmentTrc();
							dtl.setRegMonitoringPicFollowUpTrc(pft);
							dtl.setAttachmentFile(u.getFileName());
							dtl.setCreatedBy(user.getNik());
							dtl.setCreationDate(new Timestamp(new Date().getTime()));
							dtl.setDelId(new Long(0));
							dtl.setFileId(u.getFileId());
							dtl.setFileSize(u.getFileSize());
							dtl.setEnabledFlag(Constants.CONSTANT_YES);
							
							listDtl.add(dtl);
						}

						if (pft.getRegMonitoringPICFollowUpAttachmentTrcs() == null) {
							pft.setRegMonitoringPICFollowUpAttachmentTrcs(listDtl);
						} else {
							pft.getRegMonitoringPICFollowUpAttachmentTrcs().clear();
							pft.getRegMonitoringPICFollowUpAttachmentTrcs().addAll(listDtl);
						}
					}

				}
			}

			regMonitoringTrcDAO.update(regMonitoringNew);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
}
