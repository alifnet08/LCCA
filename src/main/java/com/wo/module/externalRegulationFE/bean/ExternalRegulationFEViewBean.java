package com.wo.module.externalRegulationFE.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.externalRegulation.model.RegulationMst;
import com.wo.module.externalRegulation.model.RegulationTrackRecordMst;
import com.wo.module.externalRegulation.service.RegulationMstService;
import com.wo.module.externalRegulation.service.RegulationTrackRecordMstService;
import com.wo.module.externalRegulationFE.constants.ExternalRegulationFEConstants;
import com.wo.module.externalRegulationFE.service.ExternalRegulationFEService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;

public class ExternalRegulationFEViewBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ExternalRegulationFEViewBean.class);

	private Integer hits;

	private RegulationMst regulation;

	private FacesUtil facesUtil;

	private ExternalRegulationFEService externalRegulationFEService;

	private RegulationMstService regulationService;
	
	private RegulationTrackRecordMstService regulationTrackRecordMstService;
	
	private Integer testFirst;
	
	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		ExternalRegulationFEViewBean.logger = logger;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@PostConstruct
	public void init() {
		super.init();
		if (facesUtil.retrieveRequestParam("first") != null) {
			testFirst = Integer.parseInt(facesUtil.retrieveRequestParam("first"));
			facesUtil.setSessionAttribute("FIRST_EXTER_REGULATION_FE", testFirst);
			facesUtil.setSessionAttribute("BACK_SESSION", false);
		}
		handleEdit();
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	private void handleEdit() {
		try {
			String editId = facesUtil.retrieveRequestParam("id");
			String token = facesUtil.retrieveRequestParam("token");
			if (StringUtils.isNotEmpty(token)) {
				editId = Constants.decryptString(token);
			}
			Long idLong = Long.parseLong(editId);
			regulation = regulationService.findById(idLong);

			for (int i = 0; i < regulation.getRegulationTrackRecords().size(); i++) {
				RegulationTrackRecordMst rt = regulation.getRegulationTrackRecords().get(i);
				ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(rt.getTrackCode());
				rt.setTrackName(pd.getNameIn());
				if (rt.getRegulationLinkId() != null) {
					RegulationMst reg = regulationService.findById(rt.getRegulationLinkId());
					if (reg != null && reg.getNameIn() != null) {
						rt.setRegulationLinkName(reg.getDocumentNo() + "-" + reg.getNameIn());
					}
					rt.setRegulationLinkIdEnc(Constants.encryptString(rt.getRegulationLinkId().toString()));
				}
			}
			
//			if (regulation.getStatus() != null && regulation.getStatus().equals(ParameterDetail.PARAM_DET_CODE_DATA_INACTIVE)) {
				List<RegulationTrackRecordMst> lrt = regulationTrackRecordMstService.getRegulationTrackRecordByRegulationLinkId(idLong);
				if (lrt != null && lrt.size() > 0) {
					for (int i = 0; i < lrt.size(); i++) {
						RegulationTrackRecordMst rt = lrt.get(i);
						ParameterDetail pdt = parameterDetailService.getParameterDetailByParamDtlCode(rt.getTrackCode());
						if (pdt != null && pdt.getPassiveParameterId() != null && pdt.getPassiveParameterId() > 0) {
							ParameterDetail pd = parameterDetailService.findById(pdt.getPassiveParameterId());
							rt.setTrackName(pd.getNameIn());
							if (rt.getRegulationMst() != null && rt.getRegulationMst().getRegulationId() != null) {
								RegulationMst reg = regulationService.findById(rt.getRegulationMst().getRegulationId());
								if (reg != null && reg.getNameIn() != null) {
									rt.setRegulationLinkName(reg.getDocumentNo() + "-" + reg.getNameIn());
								}
								rt.setRegulationLinkIdEnc(Constants.encryptString(rt.getRegulationMst().getRegulationId().toString()));
							}
							regulation.getRegulationTrackRecords().add(rt);
						}
					}
//				}
			}

			hits = regulationService.getCountHitRegulation(idLong,
					"/compliance/pages/externalRegulationFE/externalRegulationFE.faces");
			hits = hits + regulationService.getCountHitRegulation(idLong,
					"/compliance/pages/externalRegulationFE/externalRegulationFEView.faces%");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void cancel() {
		try {
			if (facesUtil.retrieveRequestParam("first") != null) {
				facesUtil.setSessionAttribute("BACK_SESSION", true);
			}
			facesUtil.redirect("/pages/externalRegulationFE/externalRegulationFE.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public ExternalRegulationFEService getExternalRegulationFEService() {
		return externalRegulationFEService;
	}

	public void setExternalRegulationFEService(ExternalRegulationFEService externalRegulationFEService) {
		this.externalRegulationFEService = externalRegulationFEService;
	}

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	public RegulationMst getRegulation() {
		return regulation;
	}

	public void setRegulation(RegulationMst regulation) {
		this.regulation = regulation;
	}

	public RegulationMstService getRegulationService() {
		return regulationService;
	}

	public void setRegulationService(RegulationMstService regulationService) {
		this.regulationService = regulationService;
	}

	public RegulationTrackRecordMstService getRegulationTrackRecordMstService() {
		return regulationTrackRecordMstService;
	}

	public void setRegulationTrackRecordMstService(RegulationTrackRecordMstService regulationTrackRecordMstService) {
		this.regulationTrackRecordMstService = regulationTrackRecordMstService;
	}

	public Integer getTestFirst() {
		return testFirst;
	}

	public void setTestFirst(Integer testFirst) {
		this.testFirst = testFirst;
	}

}