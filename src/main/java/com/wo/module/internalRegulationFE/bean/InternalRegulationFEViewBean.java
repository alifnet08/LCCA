package com.wo.module.internalRegulationFE.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
import com.wo.module.internalRegulationFE.constant.InternalRegulationFEConstants;
import com.wo.module.internalRegulationFE.service.InternalRegulationFEService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;

@ManagedBean(name="internalRegulationFEViewBean")
@SessionScoped
public class InternalRegulationFEViewBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(InternalRegulationFEViewBean.class);

	private Integer hits;

	private RegulationMst regulation;

	private FacesUtil facesUtil;

	private InternalRegulationFEService internalRegulationFEService;

	private RegulationMstService regulationService;
	
	private RegulationTrackRecordMstService regulationTrackRecordMstService;
	
	private String docType;
	private String searchCategory;
	private String searchStatus;
	private String searchTahun;
	private String searchVal;
	
	private Integer testFirst;
	
	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@PostConstruct
	public void init() {
		super.init();
		if (facesUtil.retrieveRequestParam("first") != null && !facesUtil.retrieveRequestParam("first").equals("")) {
			testFirst = Integer.parseInt(facesUtil.retrieveRequestParam("first"));
			facesUtil.setSessionAttribute("FIRST_INTER_REGULATION_FE", testFirst);
			facesUtil.setSessionAttribute("BACK_SESSION", false);
		}
		
		handleEdit();
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public void saveSearchData(String docType, String searchCategory, String searchStatus, String searchTahun, String searchVal) {
		if(docType != null)
			setDocType(docType);
		
		if(searchCategory != null)
			setSearchCategory(searchCategory);
		
		if(searchStatus != null) 
			setSearchStatus(searchStatus);
		
		if(searchTahun != null)
			setSearchTahun(searchTahun);
			
		if(searchVal != null) 
			setSearchVal(searchVal);
		
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
				}
//			}

//			hits = regulationService.getCountHitRegulation(idLong,
//					"/compliance/pages/internalRegulationFE/internalRegulationFE.faces");
			hits = regulationService.getCountHitRegulation(idLong,
					"/compliance/pages/internalRegulationFE/internalRegulationFEView.faces%");

			if(facesUtil.retrieveRequestParam("docType") != null)
				setDocType(facesUtil.retrieveRequestParam("docType"));
			if(facesUtil.retrieveRequestParam("searchCategory") != null)
				setDocType(facesUtil.retrieveRequestParam("searchCategory"));
			if(facesUtil.retrieveRequestParam("searchStatus") != null)
				setSearchStatus(facesUtil.retrieveRequestParam("searchStatus"));
			if(facesUtil.retrieveRequestParam("searchTahun") != null)
				setSearchTahun(facesUtil.retrieveRequestParam("searchTahun"));
			if(facesUtil.retrieveRequestParam("searchVal") != null)
				setSearchVal(facesUtil.retrieveRequestParam("searchVal"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	
	public void cancel() {
		try {
			if (facesUtil.retrieveRequestParam("first") != null) {
				facesUtil.setSessionAttribute("BACK_SESSION", true);
			}
			
			StringBuilder sb = new StringBuilder();
			sb.append("/pages/internalRegulationFE/internalRegulationFE.faces?");
			
			if(searchStatus != null) {
				if(!searchStatus.equalsIgnoreCase("null") && !searchStatus.isEmpty()) 
					sb.append("searchStatus="+searchStatus);
			}
			if(docType != null && !docType.equalsIgnoreCase("null") && !docType.isEmpty())
				sb.append("&docType="+docType);
			
			if(searchCategory != null && !searchCategory.equalsIgnoreCase("null") && !searchCategory.isEmpty())
				sb.append("&searchCategory="+searchCategory);
			
			if(searchTahun != null) {
				if(!searchTahun.equalsIgnoreCase("null") && !searchTahun.isEmpty()) 
					sb.append("&searchTahun="+searchTahun);
			}
			if(searchVal != null) {
				if(!searchVal.equalsIgnoreCase("null") && !searchVal.isEmpty()) 
					sb.append("&searchVal="+searchVal);
			}
			
			facesUtil.redirect(sb.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showSearch() {
		System.out.printf("Doc Type      : "+docType+"\n");
		System.out.printf("search Category : "+searchCategory+"\n");
		System.out.printf("Search Status : "+searchStatus+"\n");
		System.out.printf("Search Tahun  : "+searchTahun+"\n");
		System.out.printf("Search Val    : "+searchVal+"\n");
	}
	
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public InternalRegulationFEService getInternalRegulationFEService() {
		return internalRegulationFEService;
	}

	public void setInternalRegulationFEService(InternalRegulationFEService internalRegulationFEService) {
		this.internalRegulationFEService = internalRegulationFEService;
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

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
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

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getSearchCategory() {
		return searchCategory;
	}

	public void setSearchCategory(String searchCategory) {
		this.searchCategory = searchCategory;
	}

	public String getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}

	public String getSearchTahun() {
		return searchTahun;
	}

	public void setSearchTahun(String searchTahun) {
		this.searchTahun = searchTahun;
	}

	public String getSearchVal() {
		return searchVal;
	}

	public void setSearchVal(String searchVal) {
		this.searchVal = searchVal;
	}
}