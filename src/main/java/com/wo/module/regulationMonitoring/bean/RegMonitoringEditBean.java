package com.wo.module.regulationMonitoring.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
//import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.counterType.model.CounterTypeDtl;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.documentCategory.constant.DocumentCategoryConstants;
import com.wo.module.documentCategory.model.DocumentCategory;
import com.wo.module.documentCategory.service.DocumentCategoryService;
import com.wo.module.documentTopic.constant.DocumentTopicConstants;
import com.wo.module.documentTopic.model.DocumentTopic;
import com.wo.module.documentTopic.service.DocumentTopicService;
import com.wo.module.documentType.constant.DocumentTypeConstants;
import com.wo.module.documentType.model.DocumentType;
import com.wo.module.documentType.service.DocumentTypeService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.externalRegulation.constant.ExternalRegulationConstants;
import com.wo.module.externalRegulation.model.Regulation;
import com.wo.module.externalRegulation.model.RegulationTrackRecord;
import com.wo.module.externalRegulation.service.RegulationService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.regulationMonitoring.constant.RegMonitoringConstants;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICComplianceTmp;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICComplianceTmpTableModel;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpTmp;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpTmpTableModel;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpTrc;
import com.wo.module.regulationMonitoring.model.RegMonitoringRegulationTmp;
import com.wo.module.regulationMonitoring.model.RegMonitoringRegulationTmpTableModel;
import com.wo.module.regulationMonitoring.model.RegMonitoringTmp;
import com.wo.module.regulationMonitoring.service.RegMonitoringPICFollowUpTrcService;
import com.wo.module.regulationMonitoring.service.RegMonitoringTmpService;
import com.wo.module.regulationMonitoring.service.RegulationMonitoringService;
import com.wo.module.regulationMonitoring.vo.RegMonitoringApprovalVO;
import com.wo.module.regulationMonitoring.vo.StatusConfirmationVO;
import com.wo.module.tmpFine.model.TmpFinePicCompliance;
import com.wo.module.tmpFine.model.TmpFinePicFollowup;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class RegMonitoringEditBean extends CommonBean implements SelectorListener<Object>, Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(RegMonitoringEditBean.class);

	private RegMonitoringTmp regMonitoringTmp;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private Long oldCounterTypeId;
	private Long newCounterTypeId;

	private Long docTypeId;

	private Long docCategoryId;

	private Long docTopicId;
	
	private Boolean disabledFollowUpStatus;

	private Long counterTypeId;

	private List<SelectItem> divisions;

	private List<SelectItem> followUps;

	private List<SelectItem> provTypes;

	private List<SelectItem> trackCodes;

	private List<SelectItem> docTypes;

	private List<SelectItem> docCategories;

	private List<SelectItem> docTopics;

	private List<SelectItem> counterTypes;

	private List<SelectItem> reminderStatusList;

	private SelectorInfo selectorJdlPeraturan;

	private SelectorInfo selectorCompliance;

	private SelectorInfo selectorFollowup;

	private SelectorInfo selectorPic1;

	private SelectorInfo selectorPic2;

	private SelectorInfo selectorPic3;

	private RegMonitoringRegulationTmp[] selectedData;

	private RegMonitoringPICComplianceTmp[] selectedDataCompliance;

	private RegMonitoringPICFollowUpTmp[] selectedDataFollowup;

	private RegMonitoringRegulationTmpTableModel<RegMonitoringRegulationTmp> tableModel;

	private RegMonitoringPICComplianceTmpTableModel<RegMonitoringPICComplianceTmp> tableModelCompliance;

	private RegMonitoringPICFollowUpTmpTableModel<RegMonitoringPICFollowUpTmp> tableModelFollowup;
	
	private Integer lastSequenceOfRegulation;
	private Integer lastSequenceOfCompliance;
	private Integer lastSequenceOfFollowup;

	private List<StatusConfirmationVO> tableStatus;

	private List<RegMonitoringApprovalVO> tableApproval;

	private RegulationTrackRecord selectedRow;

	private Integer indexDtl;

	private Integer indexDtlCompliance;

	private Integer indexDtlFollowup;

	private RegulationMonitoringService regulationMonitoringService;

	private RegMonitoringTmpService regMonitoringTmpService;

	private ParameterDetailService parameterDetailService;

	private DocumentTypeService documentTypeService;

	private DocumentCategoryService documentCategoryService;

	private DocumentTopicService documentTopicService;

	private RegulationService regulationService;

	private UserService userService;

	private CounterTypeService counterTypeService;
	
	private RegMonitoringPICFollowUpTrcService regMonitoringPICFollowUpTrcService;

	private EmailTemplateService emailTemplateService;
	
	public FacesUtil facesUtil;

	private String navigateSearch = ExternalRegulationConstants.NAVIGATE_SEARCH;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
	
	private List<SelectItem> searchType; 

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
		selectDivision();
		selectFollowup();
		selectProvType();
		selectTrackCode();
		selectDocType();
		selectDocCategory();
		selectDocTopic();
		selectCounterType();
		selectReminderStatus();

		selectorJdlPeraturan = RegMonitoringConstants.buildSelectorJdlPeraturan(facesUtil);
		selectorCompliance = RegMonitoringConstants.buildSelectorPICCompliance(facesUtil);
		selectorFollowup = RegMonitoringConstants.buildSelectorDivision(facesUtil);
		selectorPic1 = RegMonitoringConstants.buildSelectorPIC(facesUtil);
		selectorPic2 = RegMonitoringConstants.buildSelectorPIC(facesUtil);
		selectorPic3 = RegMonitoringConstants.buildSelectorPIC(facesUtil);

		//tableApproval = new ArrayList<RegMonitoringApprovalVO>();
		tableStatus = new ArrayList<StatusConfirmationVO>();
		checkNewOrEdit();
		
		setSearchType(new ArrayList<SelectItem>());
		for (String s : Arrays.asList("No", "Judul")) {
			getSearchType().add(new SelectItem(s));
		}
	}

	public void onChangeJenisKetentuan() {
		regMonitoringTmp.getRegMonitoringRegulationTmps().clear();
	}

	public void selectReminderStatus() {
		reminderStatusList = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> listParamDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_REMINDER_STATUS);

			for (ParameterDetail vo : listParamDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				reminderStatusList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onChangeDivision(int i) {		
		RegMonitoringPICFollowUpTmp data = regMonitoringTmp.getRegMonitoringPICFollowUpTmps().get(i);
		data.setUser1(null);
		data.setUser2(null);
		data.setUser3(null);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void clearPicDetail2(int i) {	
		regMonitoringTmp.getRegMonitoringPICFollowUpTmps().get(i).setUser2(null);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void clearPicDetail3(int i) {	
		regMonitoringTmp.getRegMonitoringPICFollowUpTmps().get(i).setUser3(null);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void selectFollowup() {
		followUps = new ArrayList<SelectItem>();
		SelectItem si = new SelectItem();
		si.setLabel("Yes");
		si.setValue("Y");
		followUps.add(si);

		SelectItem si2 = new SelectItem();
		si2.setLabel("No");
		si2.setValue("N");
		followUps.add(si2);
	}

	public void selectDivision() {
		divisions = new ArrayList<SelectItem>();
		try {
			List<Division> pd = userService.getAllDivision();
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((Division) pd.get(i)).getDivisionName());
				si.setValue(((Division) pd.get(i)).getDivisionId());
				divisions.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void selectProvType() {
		provTypes = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("JENIS_KETENTUAN");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				provTypes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectTrackCode() {
		trackCodes = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("TRACK_RECORD");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				trackCodes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectDocType() {
		docTypes = new ArrayList<SelectItem>();
		try {
			List<DocumentType> pd = documentTypeService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTypeConstants.WHERE_JENIS_KETENTUAN_CODE, null)), 0,
					Integer.MAX_VALUE, null, null);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentType) pd.get(i)).getDocumentType());
				si.setValue(((DocumentType) pd.get(i)).getDocumentTypeId());
				docTypes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectDocCategory() {
		docCategories = new ArrayList<SelectItem>();
		try {
			List<DocumentCategory> pd = documentCategoryService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentCategoryConstants.WHERE_JENIS_KETENTUAN_CODE, null)),
					0, Integer.MAX_VALUE, null, null);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentCategory) pd.get(i)).getDocumentCategory());
				si.setValue(((DocumentCategory) pd.get(i)).getDocumentCategoryId());
				docCategories.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectDocTopic() {
		docTopics = new ArrayList<SelectItem>();
		try {
			List<DocumentTopic> pd = documentTopicService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTopicConstants.WHERE_JENIS_KETENTUAN_CODE, null)), 0,
					Integer.MAX_VALUE, null, null);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentTopic) pd.get(i)).getDocumentTopic());
				si.setValue(((DocumentTopic) pd.get(i)).getDocumentTopicId());
				docCategories.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectCounterType() {
		try {
			counterTypes = counterTypeService.getAllCounterTypeLabelValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onChangeFollowupStatus() {
		if(regMonitoringTmp.getFollowUp() != null) {
			/*if("Y".equals(regMonitoringTmp.getFollowUp())) {				
				if(regMonitoringTmp.getCounterType() == null) {
					regMonitoringTmp.setCounterType(new CounterType());
				}				
				
			} else if("N".equals(regMonitoringTmp.getFollowUp())) {				
				if(regMonitoringTmp.getRegMonitoringPICFollowUpTmps() != null) {
					regMonitoringTmp.getRegMonitoringPICFollowUpTmps().clear();
				}
				counterTypeId = null;
				regMonitoringTmp.setCounterType(null);
			}*/
			
			
			if("Y".equals(regMonitoringTmp.getFollowUp())) {				
				if(regMonitoringTmp.getCounterType() == null) {
					regMonitoringTmp.setCounterType(new CounterType());
				}
			} else if("N".equals(regMonitoringTmp.getFollowUp())) {				
				regMonitoringTmp.setCounterType(null);
				counterTypeId = null;
				
				if (regMonitoringTmp.getRegMonitoringPICFollowUpTmps() != null) {				
					for (int i = 0; i < regMonitoringTmp.getRegMonitoringPICFollowUpTmps().size(); i++) {
						RegMonitoringPICFollowUpTmp dtl = regMonitoringTmp.getRegMonitoringPICFollowUpTmps().get(i);
						dtl.setTargetDate(null);
					}
				}
			}
			
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	private void checkNewOrEdit() {
		String editId = facesUtil.retrieveRequestParam("id");String token = facesUtil.retrieveRequestParam("token"); 
		
		String viewId = facesUtil.retrieveRequestParam("viewId");
		isViewOnly = false;
		if (viewId != null && !viewId.isEmpty()) {
			if (viewId.trim().equalsIgnoreCase("true")) {
				isViewOnly = true;
			}
		}
		if (StringUtils.isBlank(editId) && StringUtils.isBlank(token)) {
			this.handleNew();
		} else {
			this.handleEdit(editId);
		}
	}

	private void handleNew() {
		try {
			disabledFollowUpStatus = false;
			regMonitoringTmp = new RegMonitoringTmp();
			regMonitoringTmp.setReminderStatus("REMINDER_ACTIVE");
			lastSequenceOfRegulation = 0;
			lastSequenceOfCompliance = 0;
			lastSequenceOfFollowup = 0;
			ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode("KETENTUAN_EKSTERNAL");
			regMonitoringTmp.setJenisKetentuan(pd);

			actionMode = Constants.ACTION_ADD;
			tableModel = new RegMonitoringRegulationTmpTableModel<RegMonitoringRegulationTmp>(
					regMonitoringTmp.getRegMonitoringRegulationTmps());

			tableModelCompliance = new RegMonitoringPICComplianceTmpTableModel<RegMonitoringPICComplianceTmp>(
					regMonitoringTmp.getRegMonitoringPICComplianceTmps());

			tableModelFollowup = new RegMonitoringPICFollowUpTmpTableModel<RegMonitoringPICFollowUpTmp>(
					regMonitoringTmp.getRegMonitoringPICFollowUpTmps());

			onAddNew();

			// onAddNewCompliance();
			if (regMonitoringTmp.getRegMonitoringPICComplianceTmps() == null
					|| regMonitoringTmp.getRegMonitoringPICComplianceTmps().size() == 0) {
				regMonitoringTmp.setRegMonitoringPICComplianceTmps(new ArrayList<RegMonitoringPICComplianceTmp>());
				lastSequenceOfCompliance = 0;
			}

			RegMonitoringPICComplianceTmp rt = new RegMonitoringPICComplianceTmp();
			lastSequenceOfCompliance = lastSequenceOfCompliance + 1;
			rt.setSequence(lastSequenceOfCompliance);
			
			User user = (User) facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE);
			rt.setUser(user);
			regMonitoringTmp.getRegMonitoringPICComplianceTmps().add(rt);
			tableModelCompliance.setWrappedData(regMonitoringTmp.getRegMonitoringPICComplianceTmps());
			facesUtil.setSessionAttribute("token", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		disabledFollowUpStatus = false;
		Long idLong = Long.parseLong(editId);
		regMonitoringTmp = regMonitoringTmpService.findById(idLong);
		
		lastSequenceOfRegulation = 0;
		lastSequenceOfCompliance = 0;
		lastSequenceOfFollowup = 0;

		if (regMonitoringTmp.getCounterType() != null) {
			counterTypeId = regMonitoringTmp.getCounterType().getCounterTypeId();
			oldCounterTypeId = new Long(counterTypeId);
		}		
		
		if (regMonitoringTmp.getRegMonitoringRegulationTmps() != null) {
			lastSequenceOfRegulation = regMonitoringTmp.getRegMonitoringRegulationTmps().size();
			for (int i = 0; i < regMonitoringTmp.getRegMonitoringRegulationTmps().size(); i++) {
				RegMonitoringRegulationTmp dtl = regMonitoringTmp.getRegMonitoringRegulationTmps().get(i);
				lastSequenceOfRegulation = lastSequenceOfRegulation + 1;
				dtl.setSequence(lastSequenceOfRegulation);
			}
		}		
		
		if (regMonitoringTmp.getRegMonitoringPICComplianceTmps() != null) {
			lastSequenceOfCompliance = regMonitoringTmp.getRegMonitoringPICComplianceTmps().size();
			for (int i = 0; i < regMonitoringTmp.getRegMonitoringPICComplianceTmps().size(); i++) {
				RegMonitoringPICComplianceTmp dtl = regMonitoringTmp.getRegMonitoringPICComplianceTmps().get(i);
				lastSequenceOfCompliance = lastSequenceOfCompliance + 1;
				dtl.setSequence(lastSequenceOfCompliance);				
			}
		}
		
		
		
		if (regMonitoringTmp.getRegMonitoringPICFollowUpTmps() != null) {
			lastSequenceOfFollowup = regMonitoringTmp.getRegMonitoringPICFollowUpTmps().size();
			for (int i = 0; i < regMonitoringTmp.getRegMonitoringPICFollowUpTmps().size(); i++) {
				RegMonitoringPICFollowUpTmp dtl = regMonitoringTmp.getRegMonitoringPICFollowUpTmps().get(i);
				lastSequenceOfFollowup = lastSequenceOfFollowup + 1;
				dtl.setSequence(lastSequenceOfFollowup);				
				dtl.setOldTargetDate(dtl.getTargetDate());
				
				RegMonitoringPICFollowUpTrc picFollowupTrc = regMonitoringPICFollowUpTrcService.findById(dtl.getRegMonitoringPicFollowUpTmpId());
				if(picFollowupTrc != null) {			
					if(picFollowupTrc.getFollowUpStatus() != null) {
						disabledFollowUpStatus = true;
						dtl.setIsEditableTemp(false);
					} else {
						dtl.setIsEditableTemp(true);
					}
				} else {
					dtl.setIsEditableTemp(true);
				}
			}
		}

		tableModel = new RegMonitoringRegulationTmpTableModel<RegMonitoringRegulationTmp>(
				regMonitoringTmp.getRegMonitoringRegulationTmps());
		tableModelCompliance = new RegMonitoringPICComplianceTmpTableModel<RegMonitoringPICComplianceTmp>(
				regMonitoringTmp.getRegMonitoringPICComplianceTmps());
		tableModelFollowup = new RegMonitoringPICFollowUpTmpTableModel<RegMonitoringPICFollowUpTmp>(
				regMonitoringTmp.getRegMonitoringPICFollowUpTmps());

		//tableApproval = regulationMonitoringService.getDataApprovalByRegMonitoringId(idLong);
		tableStatus = regulationMonitoringService.getDataConfirmStatusByRegMonitoringId(idLong);

	}
	
	public void onTargetDateChange(SelectEvent event) {
		String rowKey = String.valueOf((Integer) event.getComponent().getAttributes().get("seq"));
		int rowIdx = (Integer) event.getComponent().getAttributes().get("rowIdx");
		
		if(tableModelFollowup.getRowData(rowKey).getOldTargetDate() != null
				&& !tableModelFollowup.getRowData(rowKey).getOldTargetDate().equals(tableModelFollowup.getRowData(rowKey).getTargetDate()))
			tableModelFollowup.getRowData(rowKey).setRescheduleReason(null);

		PrimeFaces.current().ajax().update("form:dataTableFollowup:" + rowIdx + ":rescheduleReason");
	}

	public void onAddNew() {
		// Add one new car to the table:

		if (regMonitoringTmp.getRegMonitoringRegulationTmps() == null) {
			regMonitoringTmp.setRegMonitoringRegulationTmps(new ArrayList<RegMonitoringRegulationTmp>());
			lastSequenceOfRegulation = 0;
		}  else {
			if(regMonitoringTmp.getRegMonitoringRegulationTmps().size() == 0) {
				lastSequenceOfRegulation = 0;
			}			
		} 

		RegMonitoringRegulationTmp rt = new RegMonitoringRegulationTmp();
		lastSequenceOfRegulation = lastSequenceOfRegulation + 1;
		rt.setSequence(lastSequenceOfRegulation);
		/*
		 * Regulation reg = new Regulation(); DocumentType dt = new DocumentType();
		 * DocumentCategory dc = new DocumentCategory(); DocumentTopic dto = new
		 * DocumentTopic(); reg.setDocumentType(dt); reg.setDocumentCategory(dc);
		 * reg.setDocumentTopic(dto); rt.setRegulation(reg);
		 */

		regMonitoringTmp.getRegMonitoringRegulationTmps().add(rt);

		tableModel.setWrappedData(regMonitoringTmp.getRegMonitoringRegulationTmps());
		
		
		PrimeFaces.current().executeScript("reInitSelect2();");
		//RequestContext.getCurrentInstance().execute("reInitSelect2();");

	}

	public void onDeleteRow() {
		for (int i = 0; i < selectedData.length; i++) {
			regMonitoringTmp.getRegMonitoringRegulationTmps().remove(selectedData[i]);
		}
		
		if (regMonitoringTmp.getRegMonitoringRegulationTmps() == null
				|| regMonitoringTmp.getRegMonitoringRegulationTmps().size() == 0) {
			lastSequenceOfRegulation = 0;
		}

		tableModel.setWrappedData(regMonitoringTmp.getRegMonitoringRegulationTmps());
		
		PrimeFaces.current().executeScript("reInitSelect2();");
		//RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}

	public void onAddNewCompliance() {
		// Add one new car to the table:
		if (regMonitoringTmp.getRegMonitoringPICComplianceTmps() == null) {
			regMonitoringTmp.setRegMonitoringPICComplianceTmps(new ArrayList<RegMonitoringPICComplianceTmp>());
			lastSequenceOfCompliance = 0;
		}  else {
			if(regMonitoringTmp.getRegMonitoringPICComplianceTmps().size() == 0) {
				lastSequenceOfCompliance = 0;
			}			
		} 

		RegMonitoringPICComplianceTmp rt = new RegMonitoringPICComplianceTmp();
		lastSequenceOfCompliance = lastSequenceOfCompliance + 1;
		rt.setSequence(lastSequenceOfCompliance);
		// rt.setUser(new User());
		regMonitoringTmp.getRegMonitoringPICComplianceTmps().add(rt);
		tableModelCompliance.setWrappedData(regMonitoringTmp.getRegMonitoringPICComplianceTmps());

	}

	public void onDeleteRowCompliance() {
		for (int i = 0; i < selectedDataCompliance.length; i++) {
			regMonitoringTmp.getRegMonitoringPICComplianceTmps().remove(selectedDataCompliance[i]);
		}
		
		if (regMonitoringTmp.getRegMonitoringPICComplianceTmps() == null
				|| regMonitoringTmp.getRegMonitoringPICComplianceTmps().size() == 0) {
			lastSequenceOfCompliance = 0;
		}

		tableModelCompliance.setWrappedData(regMonitoringTmp.getRegMonitoringPICComplianceTmps());
	}

	public void onAddNewFollowup() {
		if (regMonitoringTmp.getRegMonitoringPICFollowUpTmps() == null
				|| regMonitoringTmp.getRegMonitoringPICFollowUpTmps().size() == 0) {
			regMonitoringTmp.setRegMonitoringPICFollowUpTmps(new ArrayList<RegMonitoringPICFollowUpTmp>());
			lastSequenceOfFollowup = 0;
		}  else {
			if(regMonitoringTmp.getRegMonitoringPICFollowUpTmps().size() == 0) {
				lastSequenceOfFollowup = 0;
			}			
		} 

		RegMonitoringPICFollowUpTmp rt = new RegMonitoringPICFollowUpTmp();
		lastSequenceOfFollowup = lastSequenceOfFollowup + 1;
		rt.setSequence(lastSequenceOfFollowup);
		rt.setIsEditableTemp(true);
		regMonitoringTmp.getRegMonitoringPICFollowUpTmps().add(rt);

		tableModelFollowup.setWrappedData(regMonitoringTmp.getRegMonitoringPICFollowUpTmps());
		
		PrimeFaces.current().executeScript("reInitSelect2();");
		//RequestContext.getCurrentInstance().execute("reInitSelect2();");

	}

	public void onDeleteRowFollowup() {
		for (int i = 0; i < selectedDataFollowup.length; i++) {
			regMonitoringTmp.getRegMonitoringPICFollowUpTmps().remove(selectedDataFollowup[i]);
		}
		
		if (regMonitoringTmp.getRegMonitoringPICFollowUpTmps() == null
				|| regMonitoringTmp.getRegMonitoringPICFollowUpTmps().size() == 0) {
			lastSequenceOfFollowup = 0;
		}

		tableModelFollowup.setWrappedData(regMonitoringTmp.getRegMonitoringPICFollowUpTmps());
		
		PrimeFaces.current().executeScript("reInitSelect2();");
		//RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}

	public Boolean validate() {
		Boolean flag = false;

		if (StringUtils.isEmpty(regMonitoringTmp.getJenisKetentuan().getParameterDtlCode())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationMonitoringProvType") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (StringUtils.isEmpty(regMonitoringTmp.getFollowUp())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationMonitoringFollowup") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}else {
			if(regMonitoringTmp.getFollowUp().equals(Constants.CONSTANT_NO) && StringUtils.isEmpty(regMonitoringTmp.getNotes())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationMonitoringNote") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
		}
		
		

		if (StringUtils.isEmpty(regMonitoringTmp.getReminderStatus())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationMonitoringReminderStatus") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (regMonitoringTmp.getRegMonitoringRegulationTmps() == null
				|| regMonitoringTmp.getRegMonitoringRegulationTmps().size() == 0) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationMonitoringRegTitle") + " "
					+ facesUtil.retrieveMessage("validateDetailMinOneData"));
			flag = true;
		} else if (regMonitoringTmp.getRegMonitoringRegulationTmps() != null
				&& regMonitoringTmp.getRegMonitoringRegulationTmps().size() > 0) {
			
			Set<Long> setRegulationTemp = new HashSet<Long>();
			
			for (int i = 0; i < regMonitoringTmp.getRegMonitoringRegulationTmps().size(); i++) {
				RegMonitoringRegulationTmp dtl = (RegMonitoringRegulationTmp) regMonitoringTmp
						.getRegMonitoringRegulationTmps().get(i);
				if (dtl.getRegulation() == null) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationMonitoringLinkJudul") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				}
				
				if (dtl.getRegulation()!= null && dtl.getRegulation().getRegulationId() != null) {
					if(!setRegulationTemp.add(dtl.getRegulation().getRegulationId())) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationMonitoringLinkJudul") + " "
								+ facesUtil.retrieveMessage("errorDuplicate"));
						flag = true;
					}					
				}	
			}

		}

		if (regMonitoringTmp.getRegMonitoringPICComplianceTmps() == null
				|| regMonitoringTmp.getRegMonitoringPICComplianceTmps().size() == 0) {
			/*
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationMonitoringPICCompliance") + " "
					+ facesUtil.retrieveMessage("validateDetailMinOneData"));
			flag = true;
			*/
		} else if (regMonitoringTmp.getRegMonitoringPICComplianceTmps() != null
				&& regMonitoringTmp.getRegMonitoringPICComplianceTmps().size() > 0) {
			Set<Long> setUserComplianceTemp = new HashSet<Long>();
			
			for (int i = 0; i < regMonitoringTmp.getRegMonitoringPICComplianceTmps().size(); i++) {
				RegMonitoringPICComplianceTmp dtl = (RegMonitoringPICComplianceTmp) regMonitoringTmp
						.getRegMonitoringPICComplianceTmps().get(i);
				if (dtl.getUser() == null) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationMonitoringPICCompliance")
							+ " " + facesUtil.retrieveMessage("formRegulationMonitoringNIK") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				}
				
				if (dtl.getUser() != null && dtl.getUser().getUserId() != null) {
					if(!setUserComplianceTemp.add(dtl.getUser().getUserId()) ) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationMonitoringPICCompliance")
								+ " " + facesUtil.retrieveMessage("formRegulationMonitoringNIK") + " "
								+ facesUtil.retrieveMessage("errorDuplicate"));
						flag = true;
					}					
				}	
			}

		}
		
		

		if ("Y".equals(regMonitoringTmp.getFollowUp())) {
			if (counterTypeId == null || StringUtils.isEmpty(counterTypeId.toString())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationMonitoringReminderType") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}

			if (regMonitoringTmp.getRegMonitoringPICFollowUpTmps() == null
					|| regMonitoringTmp.getRegMonitoringPICFollowUpTmps().size() == 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationMonitoringPICAndWorkUnit") + " "
						+ facesUtil.retrieveMessage("validateDetailMinOneData"));
				flag = true;

			} else {
				for (int i = 0; i < regMonitoringTmp.getRegMonitoringPICFollowUpTmps().size(); i++) {
					boolean isErrReachMaxhit = false;
					RegMonitoringPICFollowUpTmp dtl = (RegMonitoringPICFollowUpTmp) regMonitoringTmp
							.getRegMonitoringPICFollowUpTmps().get(i);
					
					if (dtl.getUser1() == null) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationMonitoringPIC1") + " "
								+ facesUtil.retrieveMessage("validateRequired"));
						flag = true;
					} 
					
					if (dtl.getTargetDate() == null) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationMonitoringTargetDate")
								+ " " + facesUtil.retrieveMessage("validateRequired"));
						flag = true;
					} else {					
						if (dtl.getRegMonitoringPicFollowUpTmpId() != null) {
							if (regulationMonitoringService
									.hasReachedMaximumReschedule(dtl.getRegMonitoringPicFollowUpTmpId())) {
								if(!dtl.getTargetDate().equals(dtl.getOldTargetDate())) {
									facesUtil.addErrMessage(facesUtil.
											retrieveMessage("formRegulationMonitoringTargetDateErrorReachMaximum"));
									isErrReachMaxhit = true;
									flag = true;
								}
							}
						}
					}
					
					// target date is reschedule
					if (StringUtils.isNotBlank(regMonitoringTmp.getLastUpdateBy())
							&& dtl.getOldTargetDate() != null
							&& !dtl.getTargetDate().equals(dtl.getOldTargetDate())
							&& StringUtils.isBlank(dtl.getRescheduleReason())
							&& !isErrReachMaxhit) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationMonitoringRescheduleReason")
								+ " " + facesUtil.retrieveMessage("validateRequired"));
						flag = true;
					}
				}
			}
		}
		
		
		/*if ("N".equals(regMonitoringTmp.getFollowUp())) {
			if (regMonitoringTmp.getRegMonitoringPICFollowUpTmps() != null
					&& regMonitoringTmp.getRegMonitoringPICFollowUpTmps().size() > 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationMonitoringPICAndWorkUnit")
						+ " " + facesUtil.retrieveMessage("validateNotRequired"));
				flag = true;
			}
		}*/

		if ("N".equals(regMonitoringTmp.getFollowUp())) {			
			if (regMonitoringTmp.getRegMonitoringPICFollowUpTmps() != null
					&& regMonitoringTmp.getRegMonitoringPICFollowUpTmps().size() > 0) {
				for (int i = 0; i < regMonitoringTmp.getRegMonitoringPICFollowUpTmps().size(); i++) {
					RegMonitoringPICFollowUpTmp dtl = (RegMonitoringPICFollowUpTmp) regMonitoringTmp
							.getRegMonitoringPICFollowUpTmps().get(i);
					
					if (dtl.getUser1() == null) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationMonitoringPIC1") + " "
								+ facesUtil.retrieveMessage("validateRequired"));
						flag = true;						
					}				
					
					
					
				}
			}
		}
		
//		comment/uncomment temporary due to user decision
//		Set<Long> setUserFollowupTemp = new HashSet<Long>();
//		for (int i = 0; i < regMonitoringTmp.getRegMonitoringPICFollowUpTmps().size(); i++) {
//			RegMonitoringPICFollowUpTmp dtl = (RegMonitoringPICFollowUpTmp) regMonitoringTmp
//					.getRegMonitoringPICFollowUpTmps().get(i);
//			if (dtl.getUser1() != null && dtl.getUser1().getUserId() != null) {
//				if (!setUserFollowupTemp.add(dtl.getUser1().getUserId())) {
//					facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationMonitoringPICAndWorkUnit")
//							+ " " + facesUtil.retrieveMessage("formRegulationMonitoringPIC1") + " "
//							+ facesUtil.retrieveMessage("errorDuplicate"));
//					flag = true;
//				}
//			}
//		} 

		return flag;
	}

	public void save() {
		try {

			if (!validate()) {

				if (StringUtils.isEmpty(regMonitoringTmp.getJenisKetentuan().getParameterDtlCode())) {
					ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(
							regMonitoringTmp.getJenisKetentuan().getParameterDtlCode());
					regMonitoringTmp.setJenisKetentuan(pd);
				}

				if (counterTypeId != null) {
					CounterType ct = counterTypeService.findById(counterTypeId);
					regMonitoringTmp.setCounterType(ct);
				}

				regMonitoringTmp.setStatus("DATA_NEW");

				if (regMonitoringTmp.getRegMonitoringRegulationTmps() != null) {
					for (int i = 0; i < regMonitoringTmp.getRegMonitoringRegulationTmps().size(); i++) {
						RegMonitoringRegulationTmp dtl = regMonitoringTmp.getRegMonitoringRegulationTmps().get(i);
						if(i==0) {
							dtl.setPrimaryFlag(Constants.CONSTANT_YES);
						}else {
							dtl.setPrimaryFlag(Constants.CONSTANT_NO);
						}
						dtl.setCreatedBy(facesUtil.retrieveUserLogin());
						dtl.setCreationDate(new Timestamp(new Date().getTime()));
						dtl.setDelId(new Long(0));
						dtl.setEnabledFlag(Constants.CONSTANT_YES);
						dtl.setRegMonitoringTmp(regMonitoringTmp);
					}
				}

				if (regMonitoringTmp.getRegMonitoringPICComplianceTmps() != null) {
					for (int i = 0; i < regMonitoringTmp.getRegMonitoringPICComplianceTmps().size(); i++) {
						RegMonitoringPICComplianceTmp dtl = regMonitoringTmp.getRegMonitoringPICComplianceTmps().get(i);
						dtl.setCreatedBy(facesUtil.retrieveUserLogin());
						dtl.setCreationDate(new Timestamp(new Date().getTime()));
						dtl.setDelId(new Long(0));
						dtl.setEnabledFlag(Constants.CONSTANT_YES);
						dtl.setRegMonitoringTmp(regMonitoringTmp);
					}
				}

				if (regMonitoringTmp.getRegMonitoringPICFollowUpTmps() != null) {
					for (int i = 0; i < regMonitoringTmp.getRegMonitoringPICFollowUpTmps().size(); i++) {
						RegMonitoringPICFollowUpTmp dtl = regMonitoringTmp.getRegMonitoringPICFollowUpTmps().get(i);

						if (oldCounterTypeId != null && counterTypeId != null) {
							if (oldCounterTypeId.longValue() != counterTypeId.longValue()) {
								if (dtl.getRegMonitoringPicFollowUpTmpId() != null) {
									if (!regulationMonitoringService
											.hasReachedMaximumReschedule(dtl.getRegMonitoringPicFollowUpTmpId())) {
										dtl.setLastUpdateBy(facesUtil.retrieveUserLogin());
										dtl.setLastUpdateDate(new Timestamp(new Date().getTime()));
									}
								}
							}
						}

						dtl.setCreatedBy(facesUtil.retrieveUserLogin());
						dtl.setCreationDate(new Timestamp(new Date().getTime()));
						dtl.setDelId(new Long(0));
						dtl.setEnabledFlag(Constants.CONSTANT_YES);
						dtl.setRegMonitoringTmp(regMonitoringTmp);
					}
				}
				
				if (regMonitoringTmp.getRegMonitoringTmpId() != null) {

					RegMonitoringTmp regulationDb = regMonitoringTmpService.findById(regMonitoringTmp.getRegMonitoringTmpId());
					regMonitoringTmpService.updateDataAlreadyExist(regMonitoringTmp, regulationDb, facesUtil.retrieveUserLogin());
					/*regMonitoringTmp.setLastUpdateBy(facesUtil.retrieveUserLogin());
					regMonitoringTmp.setLastUpdateDate(new Timestamp(new Date().getTime()));
					regMonitoringTmp.setDelId(new Long(0));
					regMonitoringTmp.setEnabledFlag(Constants.CONSTANT_YES);
					regMonitoringTmpService.update(regMonitoringTmp);
					regMonitoringTmp.setregulationId(null);*/
					
				} else {
					regMonitoringTmp.setCreatedBy(facesUtil.retrieveUserLogin());
					regMonitoringTmp.setCreationDate(new Timestamp(new Date().getTime()));
					regMonitoringTmp.setDelId(new Long(0));
					regMonitoringTmp.setEnabledFlag(Constants.CONSTANT_YES);
					regMonitoringTmpService.save(regMonitoringTmp);
					
					RegMonitoringTmp updRegMonitoringTmp = regMonitoringTmpService.findById(regMonitoringTmp.getRegMonitoringTmpId());
					updRegMonitoringTmp.setStatus("DATA_ACTIVE");
					regMonitoringTmpService.update(updRegMonitoringTmp);
				}
				
				// this should be a singleton
		        ExecutorService emailExecutor = Executors.newCachedThreadPool();

		        // from you sendEmail() method
		        emailExecutor.execute(new Runnable() {
		            @Override
		            public void run() {
		                try {
		                	sendEmail();
		                } catch (Exception e) {
		                    logger.error("send email failed", e);
		                }
		            }
		        });
				
				facesUtil.redirect("/pages/regulationMonitoring/regulationMonitoring.faces");

			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	public void sendEmail() {
		try {
			
			RegMonitoringRegulationTmp regMonitoringRegulationTmp = null;
			for(int i=0;i<regMonitoringTmp.getRegMonitoringRegulationTmps().size();i++){
				RegMonitoringRegulationTmp regMonitoringRegulationTmpNew = regMonitoringTmp.getRegMonitoringRegulationTmps().get(i);
				if(regMonitoringRegulationTmpNew.getPrimaryFlag()!=null && regMonitoringRegulationTmpNew.getPrimaryFlag().equals("Y")){
					regMonitoringRegulationTmp = regMonitoringRegulationTmpNew;
					break;
				}
			}
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_REGULATION_MONITORING");
			String emailSubject = emailTemplate.getEmailSubject().replaceAll("counter_type","NOTIFICATION");
				   emailSubject = emailSubject.replaceAll("regulation_title_in",regMonitoringRegulationTmp.getRegulation().getNameIn());
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc1 = "";
			String emailCc2 = "";
			String emailCc3 = "";
			String emailCc = "";
			String emailCcCompliance = "";
			
			ParameterDetail pdHostName = parameterDetailService.getParameterDetailByParamDtlCode(Constants.HOST_NAME_APPLICATION);
			
			if (regMonitoringTmp.getReminderStatus()!=null && regMonitoringTmp.getReminderStatus().equals(Constants.REMINDER_ACTIVE) && 
					regMonitoringTmp.getFollowUp() != null && regMonitoringTmp.getFollowUp().equals(Constants.CONSTANT_YES) &&
					regMonitoringTmp.getRegMonitoringPICFollowUpTmps() != null && regMonitoringTmp.getRegMonitoringPICFollowUpTmps().size() > 0
		    		) {
				for (int i = 0; i < regMonitoringTmp.getRegMonitoringPICFollowUpTmps().size(); i++) {
					RegMonitoringPICFollowUpTmp tmp = regMonitoringTmp.getRegMonitoringPICFollowUpTmps().get(i);
					emailContent = emailTemplate.getEmailContent().replaceAll("target_date", tmp.getTargetDate()!=null?sdf.format(tmp.getTargetDate()):"");
					emailContent = emailContent.replaceAll("document_number", regMonitoringRegulationTmp.getRegulation().getDocumentNo());
					emailContent = emailContent.replaceAll("regulation_title_in", regMonitoringRegulationTmp.getRegulation().getNameIn());
					emailContent = emailContent.replaceAll("published_date", regMonitoringRegulationTmp.getRegulation().getPublishedDate()!=null?sdf.format(regMonitoringRegulationTmp.getRegulation().getPublishedDate()):"");
					emailContent = emailContent.replaceAll("effective_date", regMonitoringRegulationTmp.getRegulation().getEffectiveDate()!=null?sdf.format(regMonitoringRegulationTmp.getRegulation().getEffectiveDate()):"");
				
					emailCc = "";
					emailCcCompliance = "";
					
					String token = "";
					String urlLink = "";
					String menuId = "";
					if (regMonitoringTmp.getFollowUp() != null && regMonitoringTmp.getFollowUp().equals(Constants.CONSTANT_YES)) {
						token = Constants.encryptString(tmp.getRegMonitoringPicFollowUpTmpId().toString());
						menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_REG_MONITORING);
						urlLink = pdHostName.getNameIn().concat("pages/regulationMonitoringFE/regulationMonitoringFEEdit.faces?token="+token+"&menuId="+menuId);
			    	} /*else if (regMonitoringTmp.getFollowUp() != null && regMonitoringTmp.getFollowUp().equals("N")) {
			    		token = Constants.encryptString(regMonitoringTmp.getRegMonitoringTmpId().toString());
			    		menuId = Constants.encryptString(Constants.MENU_ID_REG_MONITORING_VIEW);
						urlLink = pdHostName.getNameIn().concat("pages/regulationMonitoringFE/regulationMonitoringFEView.faces?token="+token+"&menuId="+menuId);
			    	}*/
					emailContent = emailContent.replaceAll("url_link", urlLink);
					
					if (regMonitoringTmp != null) {
						if (regMonitoringTmp.getRegMonitoringPICComplianceTmps() != null && regMonitoringTmp.getRegMonitoringPICComplianceTmps().size() > 0) {
							for (RegMonitoringPICComplianceTmp spct : regMonitoringTmp.getRegMonitoringPICComplianceTmps()) {
								User ue = null;
								if (spct.getUser() != null) {
									ue = userService.findById(spct.getUser().getUserId());
									if (ue != null && StringUtils.isNotBlank(ue.getEmail())) {
										emailCcCompliance = StringUtils.isNotEmpty(emailCcCompliance)?emailCcCompliance.concat(",").concat(ue.getEmail()):emailCcCompliance.concat(ue.getEmail());
									}
								}
							}
						}
					}
					
//					for(int x=0;x<socializationTmp.getCounterType().getDetails().size();x++) {
					if (regMonitoringTmp != null) {
						if (regMonitoringTmp.getCounterType() != null && regMonitoringTmp.getCounterType().getDetails() != null && regMonitoringTmp.getCounterType().getDetails().size() > 0) {
							CounterTypeDtl cd = regMonitoringTmp.getCounterType().getDetails().get(0);
							emailTo = cd.getEmailTo();
							emailCc1 = cd.getEmailCc1();
							emailCc2 = cd.getEmailCc2();
							
							if(emailTo.equals(Constants.REMINDER_PIC1)) {
								emailTo = tmp.getUser1()!=null?tmp.getUser1().getEmail():"";
							}
							else if(emailTo.equals(Constants.REMINDER_PIC2)) {
								emailTo = tmp.getUser2()!=null?tmp.getUser2().getEmail():"";
							}
							else if(emailTo.equals(Constants.REMINDER_PIC3)) {
								emailTo = tmp.getUser3()!=null?tmp.getUser3().getEmail():"";
							}
							
							if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC1)) {
								emailCc1 = tmp.getUser1()!=null?tmp.getUser1().getEmail():"";
							}
							else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC2)) {
								emailCc1 = tmp.getUser2()!=null?tmp.getUser2().getEmail():"";
							}
							else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC3)) {
								emailCc1 = tmp.getUser3()!=null?tmp.getUser3().getEmail():"";
							}
							
							if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC1)) {
								emailCc2 = tmp.getUser1()!=null?tmp.getUser1().getEmail():"";
							}
							else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC2)) {
								emailCc2 = tmp.getUser2()!=null?tmp.getUser2().getEmail():"";
							}
							else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC3)) {
								emailCc2 = tmp.getUser3()!=null?tmp.getUser3().getEmail():"";
							}
							
							
						} else {
							emailTo = tmp.getUser1() != null ? tmp.getUser1().getEmail() : "";
							emailCc1 = tmp.getUser2() != null ? tmp.getUser2().getEmail() : "";
							emailCc2 = tmp.getUser3() != null ? tmp.getUser3().getEmail() : "";
						}
						
						if(StringUtils.isNotEmpty(emailCc1)) {
							emailCc = emailCc.concat(emailCc1);
						}
						if(StringUtils.isNotEmpty(emailCc2)) {
							emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCc2):emailCc.concat(emailCc2);
						}
						if (StringUtils.isNotEmpty(emailCcCompliance)) {
							emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCcCompliance):emailCc.concat(emailCcCompliance);
						}
						
						ExecutorService emailExecutor = Executors.newCachedThreadPool();

						final String subject = emailSubject;
						final String content = emailContent;
						final String to = emailTo;
						//final String to = "h3ndr407@gmail.com";
						final String cc = emailCc;
						
						CallApiManager.sendEmailAPI(to,cc, subject,
								content, "EMAIL_REGULATION_MONITORING", "true", parameterDetailService);
					}
					
					
				}
			}
			
			
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}
	/*public void sendEmail() {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode(Constants.EMAIL_APPROVAL);
			String emailSubject = emailTemplate.getEmailSubject();
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		   
			String documentNumberTemp = "";
			String documentTitleTemp = "";
			for (int i = 0; i < regMonitoringTmp.getRegMonitoringRegulationTmps().size(); i++) {
				RegMonitoringRegulationTmp temp = regMonitoringTmp.getRegMonitoringRegulationTmps().get(0);
				documentNumberTemp = temp.getRegulation().getDocumentNo() != null ? " - " + temp.getRegulation().getDocumentNo() : "";
				documentTitleTemp = temp.getRegulation().getName() != null ? " - " + temp.getRegulation().getName() : "";
			}
			
			emailContent = emailTemplate.getEmailContent().replace(Constants.NOTIFICATION_TYPE_AND_DOC_NUM, "RegulationMonitoring"
					+ documentNumberTemp + documentTitleTemp);
						
			ParameterDetail paramEmail = parameterDetailService.getParameterDetailByParamDtlCode(Constants.OSCAR_CHECKER);
			emailTo = paramEmail.getNameIn();

			final String subject = emailSubject;
			final String content = emailContent;
			final String to = emailTo;
			//final String to = "h3ndr407@gmail.com";
			final String cc = emailCc;
						
			CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_REGULASI_MONITORING", "true", parameterDetailService);
						
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}*/
	
	public void cancel() {
		try {
			facesUtil.redirect("/pages/regulationMonitoring/regulationMonitoring.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		if (StringUtils.equals("jdlPeraturanDialog", widgetVar)) {
			Regulation reg = (Regulation) selectedItem;
			//Regulation reg = regulationService.findById(((java.math.BigInteger) objects[0]).longValue());
			//Regulation reg = regulationService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			regMonitoringTmp.getRegMonitoringRegulationTmps().get(indexDtl).setRegulation(reg);

			tableModel.setWrappedData(regMonitoringTmp.getRegMonitoringRegulationTmps());
		}

		else if (StringUtils.equals("picComplianceDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			//User user = userService.findById(((java.math.BigInteger) objects[0]).longValue());
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			regMonitoringTmp.getRegMonitoringPICComplianceTmps().get(indexDtlCompliance).setUser(user);

			tableModelCompliance.setWrappedData(regMonitoringTmp.getRegMonitoringPICComplianceTmps());
		}

		else if (StringUtils.equals("divisionDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;

			regMonitoringTmp.getRegMonitoringPICFollowUpTmps().get(indexDtlFollowup)
					//.setDivisionId(((java.math.BigInteger) objects[0]).longValue());
					  .setDivisionId(MathUtil.returnIdObjectToLong(objects[0]));
			;

			tableModelFollowup.setWrappedData(regMonitoringTmp.getRegMonitoringPICFollowUpTmps());
		}

		else if (StringUtils.equals("pic1Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			//User user = userService.findById(((java.math.BigInteger) objects[0]).longValue());
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			regMonitoringTmp.getRegMonitoringPICFollowUpTmps().get(indexDtlFollowup).setUser1(user);
			/*regMonitoringTmp.getRegMonitoringPICFollowUpTmps().get(indexDtlFollowup)
					.setDivisionId(user.getDivisionId());*/

			User user2 = userService.getUserByNik(user.getPukNik());
			if (user2 != null) {
				regMonitoringTmp.getRegMonitoringPICFollowUpTmps().get(indexDtlFollowup).setUser2(user2);

				User user3 = userService.getUserByNik(user2.getPukNik());
				if (user3 != null) {
					regMonitoringTmp.getRegMonitoringPICFollowUpTmps().get(indexDtlFollowup).setUser3(user3);
				}
			}

			tableModelFollowup.setWrappedData(regMonitoringTmp.getRegMonitoringPICFollowUpTmps());
		}

		else if (StringUtils.equals("pic2Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			//User user = userService.findById(((java.math.BigInteger) objects[0]).longValue());
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			regMonitoringTmp.getRegMonitoringPICFollowUpTmps().get(indexDtlFollowup).setUser2(user);
			/*regMonitoringTmp.getRegMonitoringPICFollowUpTmps().get(indexDtlFollowup)
					.setDivisionId(user.getDivisionId());*/
			User user3 = userService.getUserByNik(user.getPukNik());
			if (user3 != null) {
				regMonitoringTmp.getRegMonitoringPICFollowUpTmps().get(indexDtlFollowup).setUser3(user3);
			}

			tableModelFollowup.setWrappedData(regMonitoringTmp.getRegMonitoringPICFollowUpTmps());
		}

		else if (StringUtils.equals("pic3Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			//User user = userService.findById(((java.math.BigInteger) objects[0]).longValue());
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			regMonitoringTmp.getRegMonitoringPICFollowUpTmps().get(indexDtlFollowup).setUser3(user);
			/*regMonitoringTmp.getRegMonitoringPICFollowUpTmps().get(indexDtlFollowup)
					.setDivisionId(user.getDivisionId());*/

			tableModelFollowup.setWrappedData(regMonitoringTmp.getRegMonitoringPICFollowUpTmps());
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
		//RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		RegMonitoringEditBean.logger = logger;
	}

	public RegMonitoringTmp getRegMonitoringTmp() {
		return regMonitoringTmp;
	}

	public void setRegMonitoringTmp(RegMonitoringTmp regMonitoringTmp) {
		this.regMonitoringTmp = regMonitoringTmp;
	}

	public Boolean getIsViewOnly() {
		return isViewOnly;
	}

	public void setIsViewOnly(Boolean isViewOnly) {
		this.isViewOnly = isViewOnly;
	}

	public String getActionMode() {
		return actionMode;
	}

	public void setActionMode(String actionMode) {
		this.actionMode = actionMode;
	}

	public String getEditedId() {
		return editedId;
	}

	public void setEditedId(String editedId) {
		this.editedId = editedId;
	}

	public Long getOldCounterTypeId() {
		return oldCounterTypeId;
	}

	public void setOldCounterTypeId(Long oldCounterTypeId) {
		this.oldCounterTypeId = oldCounterTypeId;
	}

	public Long getNewCounterTypeId() {
		return newCounterTypeId;
	}

	public void setNewCounterTypeId(Long newCounterTypeId) {
		this.newCounterTypeId = newCounterTypeId;
	}

	public Long getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
	}

	public Long getDocCategoryId() {
		return docCategoryId;
	}

	public void setDocCategoryId(Long docCategoryId) {
		this.docCategoryId = docCategoryId;
	}

	public Long getDocTopicId() {
		return docTopicId;
	}

	public void setDocTopicId(Long docTopicId) {
		this.docTopicId = docTopicId;
	}

	public Boolean getDisabledFollowUpStatus() {
		return disabledFollowUpStatus;
	}

	public void setDisabledFollowUpStatus(Boolean disabledFollowUpStatus) {
		this.disabledFollowUpStatus = disabledFollowUpStatus;
	}

	public Long getCounterTypeId() {
		return counterTypeId;
	}

	public void setCounterTypeId(Long counterTypeId) {
		this.counterTypeId = counterTypeId;
	}

	public List<SelectItem> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<SelectItem> divisions) {
		this.divisions = divisions;
	}

	public List<SelectItem> getFollowUps() {
		return followUps;
	}

	public void setFollowUps(List<SelectItem> followUps) {
		this.followUps = followUps;
	}

	public List<SelectItem> getProvTypes() {
		return provTypes;
	}

	public void setProvTypes(List<SelectItem> provTypes) {
		this.provTypes = provTypes;
	}

	public List<SelectItem> getTrackCodes() {
		return trackCodes;
	}

	public void setTrackCodes(List<SelectItem> trackCodes) {
		this.trackCodes = trackCodes;
	}

	public List<SelectItem> getDocTypes() {
		return docTypes;
	}

	public void setDocTypes(List<SelectItem> docTypes) {
		this.docTypes = docTypes;
	}

	public List<SelectItem> getDocCategories() {
		return docCategories;
	}

	public void setDocCategories(List<SelectItem> docCategories) {
		this.docCategories = docCategories;
	}

	public List<SelectItem> getDocTopics() {
		return docTopics;
	}

	public void setDocTopics(List<SelectItem> docTopics) {
		this.docTopics = docTopics;
	}

	public List<SelectItem> getCounterTypes() {
		return counterTypes;
	}

	public void setCounterTypes(List<SelectItem> counterTypes) {
		this.counterTypes = counterTypes;
	}

	public List<SelectItem> getReminderStatusList() {
		return reminderStatusList;
	}

	public void setReminderStatusList(List<SelectItem> reminderStatusList) {
		this.reminderStatusList = reminderStatusList;
	}

	public SelectorInfo getSelectorJdlPeraturan() {
		return selectorJdlPeraturan;
	}

	public void setSelectorJdlPeraturan(SelectorInfo selectorJdlPeraturan) {
		this.selectorJdlPeraturan = selectorJdlPeraturan;
	}

	public SelectorInfo getSelectorCompliance() {
		return selectorCompliance;
	}

	public void setSelectorCompliance(SelectorInfo selectorCompliance) {
		this.selectorCompliance = selectorCompliance;
	}

	public SelectorInfo getSelectorFollowup() {
		return selectorFollowup;
	}

	public void setSelectorFollowup(SelectorInfo selectorFollowup) {
		this.selectorFollowup = selectorFollowup;
	}

	public SelectorInfo getSelectorPic1() {
		return selectorPic1;
	}

	public void setSelectorPic1(SelectorInfo selectorPic1) {
		this.selectorPic1 = selectorPic1;
	}

	public SelectorInfo getSelectorPic2() {
		return selectorPic2;
	}

	public void setSelectorPic2(SelectorInfo selectorPic2) {
		this.selectorPic2 = selectorPic2;
	}

	public SelectorInfo getSelectorPic3() {
		return selectorPic3;
	}

	public void setSelectorPic3(SelectorInfo selectorPic3) {
		this.selectorPic3 = selectorPic3;
	}

	public RegMonitoringRegulationTmp[] getSelectedData() {
		return selectedData;
	}

	public void setSelectedData(RegMonitoringRegulationTmp[] selectedData) {
		this.selectedData = selectedData;
	}

	public RegMonitoringPICComplianceTmp[] getSelectedDataCompliance() {
		return selectedDataCompliance;
	}

	public void setSelectedDataCompliance(RegMonitoringPICComplianceTmp[] selectedDataCompliance) {
		this.selectedDataCompliance = selectedDataCompliance;
	}

	public RegMonitoringPICFollowUpTmp[] getSelectedDataFollowup() {
		return selectedDataFollowup;
	}

	public void setSelectedDataFollowup(RegMonitoringPICFollowUpTmp[] selectedDataFollowup) {
		this.selectedDataFollowup = selectedDataFollowup;
	}

	public RegMonitoringRegulationTmpTableModel<RegMonitoringRegulationTmp> getTableModel() {
		return tableModel;
	}

	public void setTableModel(RegMonitoringRegulationTmpTableModel<RegMonitoringRegulationTmp> tableModel) {
		this.tableModel = tableModel;
	}

	public RegMonitoringPICComplianceTmpTableModel<RegMonitoringPICComplianceTmp> getTableModelCompliance() {
		return tableModelCompliance;
	}

	public void setTableModelCompliance(
			RegMonitoringPICComplianceTmpTableModel<RegMonitoringPICComplianceTmp> tableModelCompliance) {
		this.tableModelCompliance = tableModelCompliance;
	}

	public RegMonitoringPICFollowUpTmpTableModel<RegMonitoringPICFollowUpTmp> getTableModelFollowup() {
		return tableModelFollowup;
	}

	public void setTableModelFollowup(
			RegMonitoringPICFollowUpTmpTableModel<RegMonitoringPICFollowUpTmp> tableModelFollowup) {
		this.tableModelFollowup = tableModelFollowup;
	}

	public Integer getLastSequenceOfRegulation() {
		return lastSequenceOfRegulation;
	}

	public void setLastSequenceOfRegulation(Integer lastSequenceOfRegulation) {
		this.lastSequenceOfRegulation = lastSequenceOfRegulation;
	}

	public Integer getLastSequenceOfCompliance() {
		return lastSequenceOfCompliance;
	}

	public void setLastSequenceOfCompliance(Integer lastSequenceOfCompliance) {
		this.lastSequenceOfCompliance = lastSequenceOfCompliance;
	}

	public Integer getLastSequenceOfFollowup() {
		return lastSequenceOfFollowup;
	}

	public void setLastSequenceOfFollowup(Integer lastSequenceOfFollowup) {
		this.lastSequenceOfFollowup = lastSequenceOfFollowup;
	}

	public List<StatusConfirmationVO> getTableStatus() {
		return tableStatus;
	}

	public void setTableStatus(List<StatusConfirmationVO> tableStatus) {
		this.tableStatus = tableStatus;
	}

	public List<RegMonitoringApprovalVO> getTableApproval() {
		return tableApproval;
	}

	public void setTableApproval(List<RegMonitoringApprovalVO> tableApproval) {
		this.tableApproval = tableApproval;
	}

	public RegulationTrackRecord getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(RegulationTrackRecord selectedRow) {
		this.selectedRow = selectedRow;
	}

	public Integer getIndexDtl() {
		return indexDtl;
	}

	public void setIndexDtl(Integer indexDtl) {
		this.indexDtl = indexDtl;
	}

	public Integer getIndexDtlCompliance() {
		return indexDtlCompliance;
	}

	public void setIndexDtlCompliance(Integer indexDtlCompliance) {
		this.indexDtlCompliance = indexDtlCompliance;
	}

	public Integer getIndexDtlFollowup() {
		return indexDtlFollowup;
	}

	public void setIndexDtlFollowup(Integer indexDtlFollowup) {
		this.indexDtlFollowup = indexDtlFollowup;
	}

	public RegulationMonitoringService getRegulationMonitoringService() {
		return regulationMonitoringService;
	}

	public void setRegulationMonitoringService(RegulationMonitoringService regulationMonitoringService) {
		this.regulationMonitoringService = regulationMonitoringService;
	}

	public RegMonitoringTmpService getRegMonitoringTmpService() {
		return regMonitoringTmpService;
	}

	public void setRegMonitoringTmpService(RegMonitoringTmpService regMonitoringTmpService) {
		this.regMonitoringTmpService = regMonitoringTmpService;
	}

	public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}

	public DocumentTypeService getDocumentTypeService() {
		return documentTypeService;
	}

	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}

	public DocumentCategoryService getDocumentCategoryService() {
		return documentCategoryService;
	}

	public void setDocumentCategoryService(DocumentCategoryService documentCategoryService) {
		this.documentCategoryService = documentCategoryService;
	}

	public DocumentTopicService getDocumentTopicService() {
		return documentTopicService;
	}

	public void setDocumentTopicService(DocumentTopicService documentTopicService) {
		this.documentTopicService = documentTopicService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
	}

	public RegMonitoringPICFollowUpTrcService getRegMonitoringPICFollowUpTrcService() {
		return regMonitoringPICFollowUpTrcService;
	}

	public void setRegMonitoringPICFollowUpTrcService(
			RegMonitoringPICFollowUpTrcService regMonitoringPICFollowUpTrcService) {
		this.regMonitoringPICFollowUpTrcService = regMonitoringPICFollowUpTrcService;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public List<SelectItem> getSearchType() {
		return searchType;
	}

	public void setSearchType(List<SelectItem> searchType) {
		this.searchType = searchType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public RegulationService getRegulationService() {
		return regulationService;
	}

	public void setRegulationService(RegulationService regulationService) {
		this.regulationService = regulationService;
	}
}