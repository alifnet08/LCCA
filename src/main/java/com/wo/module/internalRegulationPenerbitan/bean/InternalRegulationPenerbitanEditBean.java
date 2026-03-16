package com.wo.module.internalRegulationPenerbitan.bean;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.util.MathUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.common.vo.SendEmailVo;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.counterType.model.CounterTypeDtl;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.holiday.service.HolidayService;
import com.wo.module.internalRegulationPenerbitan.constant.InternalRegulationPenerbitanConstants;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitan;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitanAttachment;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitanEmailGroup;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitanEmailGroupTableModel;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitanPicIrg;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitanPicIrgTableModel;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitanPicTpg;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitanPicTpgEmail;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitanPicTpgTableModel;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitanPicTpk;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitanPicTpkEmail;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitanPicTpkTableModel;
import com.wo.module.internalRegulationPenerbitan.service.InternalRegulationPenerbitanService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.outgoingLetter.constant.OutgoingLetterConstants;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;


public class InternalRegulationPenerbitanEditBean extends CommonBean
		implements SelectorListener<Object>, Serializable {

	private static final long serialVersionUID = 8520835909409018862L;

	static Logger logger = Logger.getLogger(InternalRegulationPenerbitanEditBean.class);

	private Boolean isViewOnly;
	
	private Integer lastSequenceOfPicTpg;
	private Integer lastSequenceOfPicIrg;
	private Integer lastSequenceOfPictpk;
	private Integer indexDtlFollowupTpg;
	private Integer indexDtlFollowupTpk;
	private Integer indexDtlFollowupIrg;
	private Integer indexDtlFollowupEmailGroup;
	private Integer lastSequenceOfEmailGroup;
	
	private Long counterTypeId;

	private String actionMode;
	private String editedId;
	private String statusRegulation;
	private String typeRegulation;
	private String statusFeedbackTpg;
	private String statusFeedbackTpk;
	private String statusTglFeedbackTpg;
	private String statusProcess;
	private String typeRegObsolete;
	private String unitKerjaTpg;
	private String followUpRemainderTpk;
	private String targetDateSendEmail;
	
	private Boolean flagNewEdit;

	private List<SelectItem> statusRegulations;
	private List<SelectItem> typeRegulations;
	private List<SelectItem> statusProcesss;
	private List<SelectItem> typeRegObsoletes;
	private List<SelectItem> counterTypes;
	private List<SelectItem> unitKerjas;
	
	private SelectorInfo selectorPicTpg1;
	private SelectorInfo selectorPicTpg2;
	private SelectorInfo selectorPicTpg3;	
	private SelectorInfo selectorPicTpk1;
	private SelectorInfo selectorPicTpk2;
	private SelectorInfo selectorPicTpk3;
	private SelectorInfo selectorPicIrg2;

	private InternalRegulationPenerbitan irg;

	private InternalRegulationPenerbitanPicTpg[] selectedDataPictpg;
	private InternalRegulationPenerbitanPicTpgTableModel<InternalRegulationPenerbitanPicTpg> tableModelPictpg;

	private InternalRegulationPenerbitanPicIrg[] selectedDataPicirg;
	private InternalRegulationPenerbitanPicIrgTableModel<InternalRegulationPenerbitanPicIrg> tableModelPicirg;

	private InternalRegulationPenerbitanPicTpk[] selectedDataPictpk;
	private InternalRegulationPenerbitanPicTpkTableModel<InternalRegulationPenerbitanPicTpk> tableModelPictpk;
	
	private InternalRegulationPenerbitanEmailGroup[] selectedDataEmailGroupTpk;
	private InternalRegulationPenerbitanEmailGroupTableModel<InternalRegulationPenerbitanEmailGroup> tableModelEmailGroupTpk;
	
	private FacesUtil facesUtil;
	private FileUtil fileUtil;
	
	private List<InternalRegulationPenerbitanPicTpg> dataIrgPenerbitanPicTpgDeleteList;
	private List<InternalRegulationPenerbitanPicTpk> dataIrgPenerbitanPicTpkDeleteList;
	private List<InternalRegulationPenerbitanEmailGroup> dataIrgPenerbitanEmailGroupDeleteList;

	private String navigateSearch = InternalRegulationPenerbitanConstants.NAVIGATE_SEARCH;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
	Locale localeIndo = new Locale("in","ID");
	SimpleDateFormat sdfFull = new SimpleDateFormat("dd MMMMM yyyy",localeIndo);
	
	private CounterTypeService counterTypeService;
	private UserService userService;
	private InternalRegulationPenerbitanService internalRegulationPenerbitanService;
	private EmailTemplateService emailTemplateService;
	private HolidayService holidayService;
	
	private Boolean isValidateStep1 = true;
	private Boolean isValidateStep2 = true;
	
	private List<String> emailGroupTpkList;
	private List<UploadedFileWO> uploadedFilesIrgAttachment;
	private List<UploadedFileWO> deletedFiles;
	private Map<String, String> emailGroupTpkMap;
	private User userEmailGroupCorpSec;
	
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
		checkNewOrEdit();
		searchData();
		selectUnitKerja();
		fileUtil = FileUtil.getInstance();		
		
		selectorPicTpg1 = InternalRegulationPenerbitanConstants.buildSelectorPIC(facesUtil);
		selectorPicTpg2 = InternalRegulationPenerbitanConstants.buildSelectorPIC(facesUtil);
		selectorPicTpg3 = InternalRegulationPenerbitanConstants.buildSelectorPIC(facesUtil);
		selectorPicTpk1 = InternalRegulationPenerbitanConstants.buildSelectorPIC(facesUtil);
		selectorPicTpk2 = InternalRegulationPenerbitanConstants.buildSelectorPIC(facesUtil);
		selectorPicTpk3 = InternalRegulationPenerbitanConstants.buildSelectorPIC(facesUtil);
		selectorPicIrg2 = InternalRegulationPenerbitanConstants.buildSelectorPICIrg(facesUtil);
		
		dataIrgPenerbitanPicTpgDeleteList = new ArrayList<InternalRegulationPenerbitanPicTpg>();
		dataIrgPenerbitanPicTpkDeleteList = new ArrayList<InternalRegulationPenerbitanPicTpk>();	
		
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	private void checkNewOrEdit() {
		this.editedId = facesUtil.retrieveRequestParam("editId");
		String token = facesUtil.retrieveRequestParam("token");
		userEmailGroupCorpSec = userService.getUserByNik("DUMMY3");
		dataIrgPenerbitanEmailGroupDeleteList = new ArrayList<InternalRegulationPenerbitanEmailGroup>();
		if (StringUtils.isBlank(editedId) && StringUtils.isBlank(token)) {
			handleNew();
			flagNewEdit = true;
		} else {
			handleEdit(editedId);
			flagNewEdit = false;
		}
	}
	
	private void handleNew() {
		actionMode = Constants.ACTION_ADD;
		irg = new InternalRegulationPenerbitan();
		irg.setRegulationType(new ParameterDetail());
		irg.setRegulationStatus(new ParameterDetail());
		irg.setProcessStatus(new ParameterDetail());
		irg.setRegObsoleteType(new ParameterDetail());
		irg.setCounterType(new CounterType());	
		emailGroupTpkList = new ArrayList<String>();
		
		tableModelPictpg = new InternalRegulationPenerbitanPicTpgTableModel<InternalRegulationPenerbitanPicTpg>(
				irg.getIrgPicTpgs());
		tableModelPicirg = new InternalRegulationPenerbitanPicIrgTableModel<InternalRegulationPenerbitanPicIrg>(
				irg.getIrgPicIrgs());		
		tableModelPictpk = new InternalRegulationPenerbitanPicTpkTableModel<InternalRegulationPenerbitanPicTpk>(
				irg.getIrgPicTpks());
		tableModelEmailGroupTpk = new InternalRegulationPenerbitanEmailGroupTableModel<InternalRegulationPenerbitanEmailGroup>(
				irg.getIrgEmailGroups());
		
		InternalRegulationPenerbitanPicIrg rt = new InternalRegulationPenerbitanPicIrg();
		lastSequenceOfPicIrg = 1;
		rt.setSequence(lastSequenceOfPicIrg);
		rt.setIsEditableTemp(true);
		User userLogin = getUserLogin();
		rt.setUser1(userLogin);
		User userAtasan = userService.getUserByNik(userLogin.getPukNik());
		rt.setUser2(userAtasan);
		rt.setUser3(userEmailGroupCorpSec);
		irg.setIrgPicIrgs(new ArrayList<InternalRegulationPenerbitanPicIrg>());
		irg.getIrgPicIrgs().add(rt);
		tableModelPicirg.setWrappedData(irg.getIrgPicIrgs());
		
		uploadedFilesIrgAttachment = new ArrayList<>();
	}
	
	private void handleEdit(String editId) {
		emailGroupTpkList = new ArrayList<>();
		uploadedFilesIrgAttachment = new ArrayList<>();
		emailGroupTpkMap = new HashMap<>();
		
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotBlank(token)) {
			editId = Constants.decryptString(token);
		}
		Long editIdLong = Long.parseLong(editId);
		actionMode = Constants.ACTION_EDIT;
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		irg = new InternalRegulationPenerbitan();
		irg = internalRegulationPenerbitanService.findById(editIdLong);		
		if (irg.getIrgPicIrgs() != null) {
			lastSequenceOfPicIrg = irg.getIrgPicIrgs().size();
			for (int i = 0; i < irg.getIrgPicIrgs().size(); i++) {
				InternalRegulationPenerbitanPicIrg dtl = irg.getIrgPicIrgs().get(i);
				lastSequenceOfPicIrg = lastSequenceOfPicIrg + 1;
				dtl.setSequence(lastSequenceOfPicIrg);
			}
		}
		
		if (irg.getIrgPicTpgs() != null) {
			lastSequenceOfPicTpg = irg.getIrgPicTpgs().size();
			for (int i = 0; i < irg.getIrgPicTpgs().size(); i++) {
				InternalRegulationPenerbitanPicTpg dtl = irg.getIrgPicTpgs().get(i);
				lastSequenceOfPicTpg = lastSequenceOfPicTpg + 1;
				dtl.setSequence(lastSequenceOfPicTpg);
				
				dtl.setIsEditableTemp(false);
				irg.getIrgPicTpgs().set(i, dtl);
			}
		}
		
		if (irg.getIrgPicTpks() != null) {
			lastSequenceOfPictpk = irg.getIrgPicTpks().size();
			for (int i = 0; i < irg.getIrgPicTpks().size(); i++) {
				InternalRegulationPenerbitanPicTpk dtl = irg.getIrgPicTpks().get(i);
				lastSequenceOfPictpk = lastSequenceOfPictpk + 1;
				dtl.setSequence(lastSequenceOfPictpk);	
				
				dtl.setUploadedFilesDocument(new ArrayList<UploadedFileWO>());
				UploadedFileWO uf2 = new UploadedFileWO();
				uf2.setFileName(dtl.getAttachmentFile());
				uf2.setFileId(dtl.getFileId());
				uf2.setIsNew(false);
				uf2.setFileSize(dtl.getFileSize());
				dtl.getUploadedFilesDocument().add(uf2);
				
				if(dtl.getReviewApprovalFlag() !=null && dtl.getReviewApprovalFlag().equals("Y")) {
					dtl.setCheckFlag(true);
				}else {
					dtl.setCheckFlag(false);
				}
				
				dtl.setIsEditableTemp(false);
				irg.getIrgPicTpks().set(i, dtl);
			}
		}
		
		if(irg.getProcessStatus() == null) {
			irg.setProcessStatus(new ParameterDetail());
		}
		
		if(irg.getRegObsoleteType() == null) {
			irg.setRegObsoleteType(new ParameterDetail());
		}
		
		if (StringUtils.isNotBlank(irg.getEmailGroupTpk())) {
			if (irg.getEmailGroupTpk().contains(";")) {
				String[] split = irg.getEmailGroupTpk().split(";");
				
				for (String string : split) {
					emailGroupTpkMap.put(string, string);
					emailGroupTpkList.add(string);
				}
			} else {
				emailGroupTpkMap.put(irg.getEmailGroupTpk(), irg.getEmailGroupTpk());
				emailGroupTpkList.add(irg.getEmailGroupTpk());
			}
		}
		
		if (irg.getIrgAttachmentList() != null && !irg.getIrgAttachmentList().isEmpty()) {
			for (int i = 0; i < irg.getIrgAttachmentList().size(); i++) {
				InternalRegulationPenerbitanAttachment dataIrgAttachment = irg.getIrgAttachmentList().get(i);
				UploadedFileWO uf = new UploadedFileWO();
				
				uf.setFileName(dataIrgAttachment.getAttachmentFile());
				uf.setFileId(dataIrgAttachment.getFileId());
				uf.setIsNew(false);
				uf.setFileSize(dataIrgAttachment.getFileSize());
				
				uploadedFilesIrgAttachment.add(uf);
			}
		}
		
		lastSequenceOfEmailGroup = 0;
		if (irg.getIrgEmailGroups() != null && !irg.getIrgEmailGroups().isEmpty()) {
			for (int i = 0; i < irg.getIrgEmailGroups().size(); i++) {
				InternalRegulationPenerbitanEmailGroup dataIrgEmailGroup = irg.getIrgEmailGroups().get(i);
				lastSequenceOfEmailGroup = lastSequenceOfEmailGroup + 1;
				dataIrgEmailGroup.setSequence(lastSequenceOfEmailGroup);	
				if(dataIrgEmailGroup.getReviewApprovalFlag() !=null && dataIrgEmailGroup.getReviewApprovalFlag().equals("Y")) {
					dataIrgEmailGroup.setCheckFlag(true);
					dataIrgEmailGroup.setEditableTemp(true);
				}else {
					dataIrgEmailGroup.setCheckFlag(false);
					dataIrgEmailGroup.setEditableTemp(false);
				}
			}
		}
		
		tableModelPictpg = new InternalRegulationPenerbitanPicTpgTableModel<InternalRegulationPenerbitanPicTpg>(
				irg.getIrgPicTpgs());
		tableModelPicirg = new InternalRegulationPenerbitanPicIrgTableModel<InternalRegulationPenerbitanPicIrg>(
				irg.getIrgPicIrgs());		
		tableModelPictpk = new InternalRegulationPenerbitanPicTpkTableModel<InternalRegulationPenerbitanPicTpk>(
				irg.getIrgPicTpks());
		tableModelEmailGroupTpk = new InternalRegulationPenerbitanEmailGroupTableModel<InternalRegulationPenerbitanEmailGroup>(
				irg.getIrgEmailGroups());
		
		PrimeFaces.current().executeScript("initSelect2();");
		
	}

	public void searchData() {
		typeRegulations = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("TYPE_REGULATION");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlId());
				typeRegulations.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		statusRegulations = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("STATUS_REGULATION");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlId());
				statusRegulations.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		statusProcesss = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("STATUS_PROCESS");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlId());
				statusProcesss.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		typeRegObsoletes = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("TYPE_REGULATION_OBSOLETE");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlId());
				typeRegObsoletes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		counterTypes = new ArrayList<SelectItem>();
		try {
			counterTypes = counterTypeService.getAllCounterTypeLabelValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public void selectUnitKerja() {
		unitKerjas = new ArrayList<SelectItem>();
		try {
			List<Division> pd = userService.getAllDivision();
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((Division) pd.get(i)).getDivisionName());
				si.setValue(((Division) pd.get(i)).getDivisionId());
				unitKerjas.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("initSelect2();");
	}

	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		if (StringUtils.equals("pic1DialogTpg", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			if (indexDtlFollowupTpg == null)
				indexDtlFollowupTpg = new Integer(0);
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			irg.getIrgPicTpgs().get(indexDtlFollowupTpg).setUser1(user);

			User user2 = userService.getUserByNik(user.getPukNik());
			if (user2 != null) {
				irg.getIrgPicTpgs().get(indexDtlFollowupTpg).setUser2(user2);

//				User user3 = userService.getUserByNik(user2.getPukNik());
//				if (user3 != null) {
//					irg.getIrgPicTpgs().get(indexDtlFollowupTpg).setUser3(user3);
//				}
			}

			tableModelPictpg.setWrappedData(irg.getIrgPicTpgs());

			PrimeFaces.current().ajax().update("form:dataTablePictpg:" + indexDtlFollowupTpg + ":userName1");
			PrimeFaces.current().ajax().update("form:dataTablePictpg:" + indexDtlFollowupTpg + ":userName2");
			PrimeFaces.current().ajax().update("form:dataTablePictpg:" + indexDtlFollowupTpg + ":userName3");
		} else if (StringUtils.equals("pic2DialogTpg", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			if (indexDtlFollowupTpg == null)
				indexDtlFollowupTpg = new Integer(0);
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			irg.getIrgPicTpgs().get(indexDtlFollowupTpg).setUser2(user);

//			User user3 = userService.getUserByNik(user.getPukNik());
//			if (user3 != null) {
//				irg.getIrgPicTpgs().get(indexDtlFollowupTpg).setUser3(user3);
//			}

			tableModelPictpg.setWrappedData(irg.getIrgPicTpgs());
			PrimeFaces.current().ajax().update("form:dataTablePictpg:" + indexDtlFollowupTpg + ":userName2");
			PrimeFaces.current().ajax().update("form:dataTablePictpg:" + indexDtlFollowupTpg + ":userName3");
		} else if (StringUtils.equals("pic3DialogTpg", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;

			if (indexDtlFollowupTpg == null)
				indexDtlFollowupTpg = new Integer(0);
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			irg.getIrgPicTpgs().get(indexDtlFollowupTpg).setUser3(user);

			tableModelPictpg.setWrappedData(irg.getIrgPicTpgs());
			PrimeFaces.current().ajax().update("form:dataTablePictpg:" + indexDtlFollowupTpg + ":userName3");
		} else if (StringUtils.equals("pic1DialogTpk", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			if (indexDtlFollowupTpk == null)
				indexDtlFollowupTpk = new Integer(0);
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			irg.getIrgPicTpks().get(indexDtlFollowupTpk).setUser1(user);

			User user2 = userService.getUserByNik(user.getPukNik());
			if (user2 != null) {
				irg.getIrgPicTpks().get(indexDtlFollowupTpk).setUser2(user2);

//				User user3 = userService.getUserByNik(user2.getPukNik());
//				if (user3 != null) {
//					irg.getIrgPicTpks().get(indexDtlFollowupTpk).setUser3(user3);
//				}
			}

			tableModelPictpk.setWrappedData(irg.getIrgPicTpks());

			PrimeFaces.current().ajax().update("form:dataTablePictpk:" + indexDtlFollowupTpk + ":userName1");
			PrimeFaces.current().ajax().update("form:dataTablePictpk:" + indexDtlFollowupTpk + ":userName2");
			PrimeFaces.current().ajax().update("form:dataTablePictpk:" + indexDtlFollowupTpk + ":userName3");
		} else if (StringUtils.equals("pic2DialogTpk", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			if (indexDtlFollowupTpk == null)
				indexDtlFollowupTpk = new Integer(0);
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			irg.getIrgPicTpks().get(indexDtlFollowupTpk).setUser2(user);

//			User user3 = userService.getUserByNik(user.getPukNik());
//			if (user3 != null) {
//				irg.getIrgPicTpks().get(indexDtlFollowupTpk).setUser3(user3);
//			}

			tableModelPictpk.setWrappedData(irg.getIrgPicTpks());
			PrimeFaces.current().ajax().update("form:dataTablePictpk:" + indexDtlFollowupTpk + ":userName2");
			PrimeFaces.current().ajax().update("form:dataTablePictpk:" + indexDtlFollowupTpk + ":userName3");
		} else if (StringUtils.equals("pic3DialogTpk", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;

			if (indexDtlFollowupTpk == null)
				indexDtlFollowupTpk = new Integer(0);
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			irg.getIrgPicTpks().get(indexDtlFollowupTpk).setUser3(user);

			tableModelPictpk.setWrappedData(irg.getIrgPicTpks());
			PrimeFaces.current().ajax().update("form:dataTablePictpk:" + indexDtlFollowupTpk + ":userName3");
		} else if (StringUtils.equals("pic2DialogIrg", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;

			if (indexDtlFollowupIrg == null)
				indexDtlFollowupIrg = new Integer(0);
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			irg.getIrgPicIrgs().get(indexDtlFollowupIrg).setUser2(user);

			tableModelPicirg.setWrappedData(irg.getIrgPicIrgs());
			PrimeFaces.current().ajax().update("form:dataTablePicirg:" + indexDtlFollowupIrg + ":userName2");
		}

		PrimeFaces.current().executeScript("initSelect2();");

	}
	
	public List<String> completeRegulationTitle(String query) {
		List<String> regulationTitles = internalRegulationPenerbitanService.getDataRegulationTitle(query);
		return regulationTitles;
	}
	
	public void onChangeUnitKerjaPicTpg(int i) {		
		InternalRegulationPenerbitanPicTpg data = irg.getIrgPicTpgs().get(i);
		data.setUser1(null);
		data.setUser2(null);
		data.setUser3(null);
		data.setTargetDate(null);
				
		PrimeFaces.current().executeScript("initSelect2();");		
	}
	
	public void onAddNewPictpg() {
		if (irg.getIrgPicTpgs() == null
				|| irg.getIrgPicTpgs().size() == 0) {
			irg.setIrgPicTpgs(new ArrayList<InternalRegulationPenerbitanPicTpg>());
			lastSequenceOfPicTpg = 0;
		}  else {
			if(irg.getIrgPicTpgs().size() == 0) {
				lastSequenceOfPicTpg = 0;
			}			
		} 

		InternalRegulationPenerbitanPicTpg rt = new InternalRegulationPenerbitanPicTpg();
		lastSequenceOfPicTpg = lastSequenceOfPicTpg + 1;
		rt.setSequence(lastSequenceOfPicTpg);
		rt.setIsEditableTemp(true);
		irg.getIrgPicTpgs().add(rt);

		tableModelPictpg.setWrappedData(irg.getIrgPicTpgs());
		
		PrimeFaces.current().executeScript("initSelect2();");
	}

	public void onDeleteRowPictpg() {
		for (int i = 0; i < selectedDataPictpg.length; i++) {
			dataIrgPenerbitanPicTpgDeleteList.add(selectedDataPictpg[i]);
			irg.getIrgPicTpgs().remove(selectedDataPictpg[i]);
		}
		
		if (irg.getIrgPicTpgs() == null
				|| irg.getIrgPicTpgs().size() == 0) {
			lastSequenceOfPicTpg = 0;
		}

		tableModelPictpg.setWrappedData(irg.getIrgPicTpgs());
		
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public void clearPicTpgDetail2(int i) {	
		irg.getIrgPicTpgs().get(i).setUser2(null);
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public void clearPicTpgDetail3(int i) {	
		irg.getIrgPicTpgs().get(i).setUser3(null);
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public void onChangeUnitKerjaPicTpk(int i) {		
		InternalRegulationPenerbitanPicTpk data = irg.getIrgPicTpks().get(i);
		data.setUser1(null);
		data.setUser2(null);
		data.setUser3(null);
				
		PrimeFaces.current().executeScript("initSelect2();");		
	}
	
	public void onAddNewPictpk() {		
		if (irg.getIrgPicTpks() == null
				|| irg.getIrgPicTpks().size() == 0) {
			irg.setIrgPicTpks(new ArrayList<InternalRegulationPenerbitanPicTpk>());
			lastSequenceOfPictpk = 0;
		}  else {
			if(irg.getIrgPicTpks().size() == 0) {
				lastSequenceOfPictpk = 0;
			}			
		} 

		InternalRegulationPenerbitanPicTpk rt = new InternalRegulationPenerbitanPicTpk();
		lastSequenceOfPictpk = lastSequenceOfPictpk + 1;
		rt.setSequence(lastSequenceOfPictpk);
		rt.setIsEditableTemp(true);
		irg.getIrgPicTpks().add(rt);

		tableModelPictpk.setWrappedData(irg.getIrgPicTpks());
		
		PrimeFaces.current().executeScript("initSelect2();");	
	}

	public void onDeleteRowPictpk() {
		for (int i = 0; i < selectedDataPictpk.length; i++) {
			dataIrgPenerbitanPicTpkDeleteList.add(selectedDataPictpk[i]);
			irg.getIrgPicTpks().remove(selectedDataPictpk[i]);
		}
		
		if (irg.getIrgPicTpks() == null || irg.getIrgPicTpks().size() == 0) {
			lastSequenceOfPictpk = 0;
		}

		tableModelPicirg.setWrappedData(irg.getIrgPicTpks());
		
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public void clearPicTpkDetail2(int i) {	
		irg.getIrgPicTpks().get(i).setUser2(null);
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public void clearPicTpkDetail3(int i) {	
		irg.getIrgPicTpks().get(i).setUser3(null);
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public void clearPicIrgDetail2(int i) {	
		irg.getIrgPicIrgs().get(i).setUser2(null);
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public void onTargetDateChangePicTpk(int rowIdx) {
		followUpRemainderTpk = "Y"; 
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public void onChangeCheckBox() {
		String indexCheck = facesUtil.retrieveRequestParam("indexCheckBoxRow");
		Integer indexCheckBoxRow = Integer.parseInt(indexCheck);
		if(irg.getIrgPicTpks().get(indexCheckBoxRow).getCheckFlag()) {
			irg.getIrgPicTpks().get(indexCheckBoxRow).setReviewApprovalFlag("Y");
		}else {
			irg.getIrgPicTpks().get(indexCheckBoxRow).setReviewApprovalFlag("N");
		}
		
		PrimeFaces.current().ajax().update("form:dataTablePictpk:"+indexCheckBoxRow+":checkboxReview");
		
		PrimeFaces.current().executeScript("initSelect2();");
		
	}
	
	public void onChangeUnitKerjaTpg() {
		String directorateData = userService.getDirectorateByDivisionId(irg.getWorkUnitTpg());
		irg.setDirectorateTpg(directorateData);
		PrimeFaces.current().ajax().update("form:internalRegPenerDirektoratTPG");
	}
	
	public void deleteAttachment(String fileId, int index, String uploadType) throws Exception {
		deletedFiles = deletedFiles!=null?deletedFiles: new ArrayList<UploadedFileWO>();
		
		deletedFiles.add(new UploadedFileWO(fileId,null,null, null));
		if (uploadType.equals(InternalRegulationPenerbitanConstants.UPLOAD_TYPE_IRG_ATTACHMENT)) {
			uploadedFilesIrgAttachment.remove(uploadedFilesIrgAttachment.get(index));
		}
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public void handleFileUploadIrgAttachment(FileUploadEvent event)  {
		
		try {
			uploadedFilesIrgAttachment = uploadedFilesIrgAttachment == null ? new ArrayList<UploadedFileWO>()
					: uploadedFilesIrgAttachment;
			uploadedFilesIrgAttachment.add(new UploadedFileWO(
					CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_IRG_ATTACHMENT,
							parameterDetailService, false, fileUtil),
					event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}

	}

	public void handleFileUploadDocument(FileUploadEvent event) throws Exception {		
		try {
			String[] indexFileSplit = event.getComponent().getClientId().split(":");
			Integer indexFile = Integer.valueOf(indexFileSplit[2].toString());
			if(irg.getIrgPicTpks().get(indexFile).getUploadedFilesDocument() == null) {
				irg.getIrgPicTpks().get(indexFile).setUploadedFilesDocument(new ArrayList<UploadedFileWO>());
			}
			
			irg.getIrgPicTpks().get(indexFile).getUploadedFilesDocument().add(new UploadedFileWO(CallApiManager.callUploadAPI(event.getFile(),
					Constants.COMPLIANCE_DOC_TYPE_DOKUMEN_COMPLIANCE_REVIEW, parameterDetailService, false, fileUtil),
					event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
			PrimeFaces.current().ajax().update("form:dataTablePictpk:"+indexFile+":documentList");
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public String onFlowProcess(FlowEvent event) {
		String step = "";
		if(!StringUtils.isEmpty(event.getNewStep()) 
				&& event.getNewStep().equals("step2") ) {
			saveStep1();
			if (Boolean.TRUE.equals(isValidateStep1)) {
				step = event.getNewStep();
			} else {
				step = event.getOldStep();
			}
		}
		
		if(!StringUtils.isEmpty(event.getNewStep()) 
				&& event.getNewStep().equals("step1") ) {
			step = event.getNewStep();
		}
		
		if (irg == null) {
			irg = new InternalRegulationPenerbitan();
		}
		
		if (irg.getRegulationType() == null) {
			irg.setRegulationType(new ParameterDetail());
		}
		if (irg.getRegulationStatus() == null) {
			irg.setRegulationStatus(new ParameterDetail());
		}
		if (irg.getProcessStatus() == null) {
			irg.setProcessStatus(new ParameterDetail());
		}
		if (irg.getRegObsoleteType() == null) {
			irg.setRegObsoleteType(new ParameterDetail());
		}
		if (irg.getCounterType() == null) {
			irg.setCounterType(new CounterType());
		}
		
		PrimeFaces.current().executeScript("initSelect2();");
		
		return step;
	}
	
	private boolean isValidate() {
		isValidateStep1 = true;
		
		if (irg.getIrgPicTpks() == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationPenerbitanPICTpk")
					+ " " + facesUtil.retrieveMessage("errorMustHaveAtLeast"));
			isValidateStep1 = false;
		} else {
			int idx = 1;
			for (int i = 0; i < irg.getIrgPicTpks().size(); i++) {
				InternalRegulationPenerbitanPicTpk dataPicTpk = irg.getIrgPicTpks().get(i);
				
				if (Boolean.TRUE.equals(dataPicTpk.getIsEditableTemp())) {
					if (dataPicTpk.getUser1() == null) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationPenerbitanPICTpk")
								+ " " + facesUtil.retrieveMessage("row") 
								+ " " + idx
								+ " " + facesUtil.retrieveMessage("formInternalRegulationPenerbitanPIC1")
								+ " " + facesUtil.retrieveMessage("validateRequired"));
						isValidateStep1 = false;
					}
					
					if (dataPicTpk.getTargetDate() == null) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationPenerbitanPICTpk")
								+ " " + facesUtil.retrieveMessage("row") 
								+ " " + idx
								+ " " + facesUtil.retrieveMessage("formInternalRegulationPenerbitanTargetDate")
								+ " " + facesUtil.retrieveMessage("validateRequired"));
						isValidateStep1 = false;
					}
				}
				idx++;
			}
		}
		
		if (irg.getIrgPicTpgs() == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationPenerbitanPICTpg")
					+ " " + facesUtil.retrieveMessage("errorMustHaveAtLeast"));
			isValidateStep1 = false;
		} else {
			int idx = 1;
			for (int i = 0; i < irg.getIrgPicTpgs().size(); i++) {
				InternalRegulationPenerbitanPicTpg dataPicTpg = irg.getIrgPicTpgs().get(i);
				
				if (Boolean.TRUE.equals(dataPicTpg.getIsEditableTemp())) {
					if (dataPicTpg.getUser1() == null) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationPenerbitanPICTpg")
								+ " " + facesUtil.retrieveMessage("row") 
								+ " " + idx
								+ " " + facesUtil.retrieveMessage("formInternalRegulationPenerbitanPIC1")
								+ " " + facesUtil.retrieveMessage("validateRequired"));
						isValidateStep1 = false;
					}
					
					if (dataPicTpg.getTargetDate() == null) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationPenerbitanPICTpg")
								+ " " + facesUtil.retrieveMessage("row") 
								+ " " + idx
								+ " " + facesUtil.retrieveMessage("formInternalRegulationPenerbitanTargetDate")
								+ " " + facesUtil.retrieveMessage("validateRequired"));
						isValidateStep1 = false;
					}
				}
			}
		}
		
		if (irg.getIrgPicIrgs() != null && !irg.getIrgPicIrgs().isEmpty()) {
			int idx = 1;
			for (int i = 0; i < irg.getIrgPicIrgs().size(); i++) {
				InternalRegulationPenerbitanPicIrg dataPicIrg = irg.getIrgPicIrgs().get(i);
				
				if (dataPicIrg.getUser1() == null) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationPenerbitanPICIrg")
							+ " " + facesUtil.retrieveMessage("row") 
							+ " " + idx
							+ " " + facesUtil.retrieveMessage("formInternalRegulationPenerbitanPIC1")
							+ " " + facesUtil.retrieveMessage("validateRequired"));
					isValidateStep1 = false;
				}
				
				if (dataPicIrg.getUser2() == null) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationPenerbitanPICIrg")
							+ " " + facesUtil.retrieveMessage("row") 
							+ " " + idx
							+ " " + facesUtil.retrieveMessage("formInternalRegulationPenerbitanPIC2")
							+ " " + facesUtil.retrieveMessage("validateRequired"));
					isValidateStep1 = false;
				}
				idx++;
			}
		}
		
		if (irg.getCounterType() == null && irg.getCounterType().getCounterTypeId() == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationPenerbitanReminderType")
					+ " " + facesUtil.retrieveMessage("validateRequired"));
			isValidateStep1 = false;
		}
		
		PrimeFaces.current().ajax().update("form:msgs");
		
		return isValidateStep1;
	}
	
	private boolean isValidateStep2() {
		boolean flag = true;
		
		if (irg.getProcessStatus() == null || irg.getProcessStatus().getParameterDtlId() == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationPenerbitanProcessStatus")
					+ " " + facesUtil.retrieveMessage("validateRequired"));
			flag = false;
		}
		
		if (StringUtils.isEmpty(irg.getRegulationNo())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationPenerbitanRegulationNo")
					+ " " + facesUtil.retrieveMessage("validateRequired"));
			flag = false;
		} else {
			if (StringUtils.isEmpty(irg.getRegulationNo().trim())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationPenerbitanRegulationNo")
						+ " " + facesUtil.retrieveMessage("validateRequired"));
				flag = false;
			}
		}
		
		if (actionMode.equals(Constants.ACTION_ADD)) {
			if (Boolean.FALSE.equals(internalRegulationPenerbitanService.isCheckDataIrgByTitleAndNo(irg.getRegulationNo(), irg.getIrgTitle(), null))) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationPenerbitanValidationTitleAndRegulationNo", irg.getIrgTitle(), irg.getRegulationNo()));
				flag = false;
			}
		} else {
			if (Boolean.FALSE.equals(internalRegulationPenerbitanService.isCheckDataIrgByTitleAndNo(irg.getRegulationNo(), irg.getIrgTitle(), irg.getIrgId()))) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationPenerbitanValidationTitleAndRegulationNo", irg.getIrgTitle(), irg.getRegulationNo()));
				flag = false;
			}
		}
		
		PrimeFaces.current().executeScript("initSelect2();");
		
		return flag;
	}
	
	private void saveStep1() {
		try {
			targetDateSendEmail = "";
			if (isValidate()) {
				if(irg.getRegulationType() == null || irg.getRegulationType().getParameterDtlId() == null) {
					irg.setRegulationType(null);
				}
				
				if(irg.getRegulationStatus() == null || irg.getRegulationStatus().getParameterDtlId() == null) {
					irg.setRegulationStatus(null);
				}
				
				if(irg.getProcessStatus() == null || irg.getProcessStatus().getParameterDtlId() == null) {
					irg.setProcessStatus(null);
				}			
				
				if(irg.getCounterType() == null || irg.getCounterType().getCounterTypeId() == null) {
					irg.setCounterType(null);
				}
				
				if(irg.getRegObsoleteType() == null || irg.getRegObsoleteType().getParameterDtlId() == null) {
					irg.setRegObsoleteType(null); 
				}
				
				CounterType counterType = new CounterType();
				if (irg.getCounterType() != null && irg.getCounterType().getCounterTypeId() != null) {
					counterType = counterTypeService.findById(irg.getCounterType().getCounterTypeId());
				}
				
				if (irg.getIrgAttachmentList() == null || irg.getIrgAttachmentList().size() <= 0) {
					irg.setIrgAttachmentList(new ArrayList<>());
				}
				irg.getIrgAttachmentList().clear();
				
				if (uploadedFilesIrgAttachment != null && !uploadedFilesIrgAttachment.isEmpty()) {
					for (int i = 0; i < uploadedFilesIrgAttachment.size(); i++) {
						UploadedFileWO uf = uploadedFilesIrgAttachment.get(i); 
						InternalRegulationPenerbitanAttachment dataIrgAttachment = new InternalRegulationPenerbitanAttachment();
						
						dataIrgAttachment.setInternalRegulationPenerbitan(irg);
						dataIrgAttachment.setAttachmentFile(uf.getFileName());
						dataIrgAttachment.setAttachmentType(OutgoingLetterConstants.OUTGOING_LETTER_ATTACHMENT);
						dataIrgAttachment.setCreatedBy(facesUtil.retrieveUserLogin());
						dataIrgAttachment.setCreationDate(new Timestamp(new Date().getTime()));
						dataIrgAttachment.setDelId(0l);
						dataIrgAttachment.setEnabledFlag(Constants.CONSTANT_YES);
						
						dataIrgAttachment.setFileId(uf.getFileId());
						dataIrgAttachment.setFileSize(uf.getFileSize());
						
						irg.getIrgAttachmentList().add(dataIrgAttachment);
					}
				}
				
				if(irg.getIrgPicTpgs() !=null && irg.getIrgPicTpgs().size() > 0) {
					for(InternalRegulationPenerbitanPicTpg picTpg : irg.getIrgPicTpgs()) {
						picTpg.setIrg(irg);
						if (picTpg.getCreatedBy() == null) {
							picTpg.setCreatedBy(facesUtil.retrieveUserLogin());
							picTpg.setCreationDate(new Timestamp(new Date().getTime()));
						}else {
							picTpg.setLastUpdateBy(facesUtil.retrieveUserLogin());
							picTpg.setLastUpdateDate(new Timestamp(new Date().getTime()));
						}	
						
						picTpg.setDelId(0l);
						picTpg.setEnabledFlag(Constants.CONSTANT_YES);
						
						// save email reminder
						if (picTpg.getTargetDate() != null && picTpg.getIsEditableTemp()) {
							if (irg.getCounterType() != null 
									&& counterType.getDetails() != null 
									&& !counterType.getDetails().isEmpty()) {
								List<InternalRegulationPenerbitanPicTpgEmail> regulationPenerbitanPicTpgEmailList = new ArrayList<>();
								Calendar calendar = Calendar.getInstance();
								Date targetDateTmp = picTpg.getTargetDate();
								
								for (CounterTypeDtl dataCounterTypeDtl : counterType.getDetails()) {
									int counterDate = 0;
									
									if (dataCounterTypeDtl.getSlaType().equals("+")) {
										/*Apparently we need this code snippet to make sure when this data is created on Weekend, 
											the due date is place correctly. Else it will be increase by 1 day */
										int checkDay = calendar.get(Calendar.DAY_OF_WEEK);
										if (checkDay == 1 || checkDay == 7 ){
											counterDate +=1;
										}
										
										while (counterDate < dataCounterTypeDtl.getSla().intValue()) {
											int day = calendar.get(Calendar.DAY_OF_WEEK);
											
											if (day == 1 || day == 7) {
												//do nothing
											} else {
												if (Boolean.TRUE.equals(holidayService.isAvailableDate(targetDateTmp))) {
													counterDate++;
												}
											}
											
											if (counterDate < dataCounterTypeDtl.getSla().intValue()) {
												calendar.setTime(targetDateTmp);
												calendar.add(Calendar.DAY_OF_MONTH, 1);
												targetDateTmp = calendar.getTime();
											}
										}
									} else if (dataCounterTypeDtl.getSlaType().equals("-")) {
										while (counterDate < dataCounterTypeDtl.getSla().intValue()) {
											int day = calendar.get(Calendar.DAY_OF_WEEK);
											
											if (day == 1 || day == 7) {
												// do nothing
											} else {
												if (Boolean.TRUE.equals(holidayService.isAvailableDate(targetDateTmp))) {
													counterDate++;
												}
											}
											
											if (counterDate < dataCounterTypeDtl.getSla().intValue()) {
												calendar.setTime(targetDateTmp);
												calendar.add(Calendar.DAY_OF_MONTH, -1);
												targetDateTmp = calendar.getTime();
											}
										}
									}
									
									InternalRegulationPenerbitanPicTpgEmail internalRegulationPenerbitanPicTpgEmail = new InternalRegulationPenerbitanPicTpgEmail();
									
									internalRegulationPenerbitanPicTpgEmail.setInternalRegulationPenerbitanPicTpg(picTpg);
									internalRegulationPenerbitanPicTpgEmail.setSla(dataCounterTypeDtl.getSla().intValue());
									internalRegulationPenerbitanPicTpgEmail.setSlaType(dataCounterTypeDtl.getSlaType());
									internalRegulationPenerbitanPicTpgEmail.setEmailDate(new Timestamp(targetDateTmp.getTime()));
									internalRegulationPenerbitanPicTpgEmail.setCreatedBy(facesUtil.retrieveUserLogin());
									internalRegulationPenerbitanPicTpgEmail.setCreationDate(new Timestamp(new Date().getTime()));
									internalRegulationPenerbitanPicTpgEmail.setEnabledFlag(Constants.CONSTANT_YES);
									internalRegulationPenerbitanPicTpgEmail.setDelId(0l);
									
									regulationPenerbitanPicTpgEmailList.add(internalRegulationPenerbitanPicTpgEmail);
								}
								
								picTpg.setInternalRegulationPenerbitanPicTpgEmailList(regulationPenerbitanPicTpgEmailList);
							}
						}
						// save email reminder
						
					}
				}
				
				Integer countTargetDate = 0;
				if(irg.getIrgPicTpks() !=null && irg.getIrgPicIrgs().size() > 0) {				    
					for(InternalRegulationPenerbitanPicTpk picTpk : irg.getIrgPicTpks()) {
						picTpk.setIrg(irg);
						if (picTpk.getCreatedBy() == null) {
							picTpk.setCreatedBy(facesUtil.retrieveUserLogin());
							picTpk.setCreationDate(new Timestamp(new Date().getTime()));
						}else {
							picTpk.setLastUpdateBy(facesUtil.retrieveUserLogin());
							picTpk.setLastUpdateDate(new Timestamp(new Date().getTime()));
						}	
						
						picTpk.setDelId(0l);
						picTpk.setEnabledFlag(Constants.CONSTANT_YES);
						if(picTpk.getCheckFlag()) {
							picTpk.setReviewApprovalFlag("Y");
						}else {
							picTpk.setReviewApprovalFlag("N");
						}
						
						if(picTpk.getUploadedFilesDocument() !=null && picTpk.getUploadedFilesDocument().size() > 0) {
							for(UploadedFileWO uf : picTpk.getUploadedFilesDocument()) {
								picTpk.setAttachmentFile(uf.getFileName());
								picTpk.setFileId(uf.getFileId());
								picTpk.setFileSize(uf.getFileSize());			
							}
						}
						
						// save email reminder
						if (picTpk.getTargetDate() != null && picTpk.getIsEditableTemp()) {
							if (irg.getCounterType() != null 
									&& counterType.getDetails() != null 
									&& !counterType.getDetails().isEmpty()) {
								List<InternalRegulationPenerbitanPicTpkEmail> regulationPenerbitanPicTpkEmailList = new ArrayList<>();
								Calendar calendar = Calendar.getInstance();
								Date targetDateTmp = picTpk.getTargetDate();
								
								for (CounterTypeDtl dataCounterTypeDtl : counterType.getDetails()) {
									int counterDate = 0;
									
									if (dataCounterTypeDtl.getSlaType().equals("+")) {
										
										/*Apparently we need this code snippet to make sure when this data is created on Weekend/ Holiday, 
											the due date is place correctly. Else it will be increase by 1 day */
										int checkDay = calendar.get(Calendar.DAY_OF_WEEK);
										if (checkDay == 1 || checkDay == 7 ){
											counterDate +=1;
										}
										
										while (counterDate < dataCounterTypeDtl.getSla().intValue()) {
											int day = calendar.get(Calendar.DAY_OF_WEEK);
											
											if (day == 1 || day == 7) {
												// do nothing
											} else {
												if (Boolean.TRUE.equals(holidayService.isAvailableDate(targetDateTmp))) {
													counterDate++;
												}
											}
											
											if (counterDate < dataCounterTypeDtl.getSla().intValue()) {
												calendar.setTime(targetDateTmp);
												calendar.add(Calendar.DAY_OF_MONTH, 1);
												targetDateTmp = calendar.getTime();
											}
											
										}
									} else if (dataCounterTypeDtl.getSlaType().equals("-")) {
										while (counterDate < dataCounterTypeDtl.getSla().intValue()) {
											int day = calendar.get(Calendar.DAY_OF_WEEK);
											
											if (day == 1 || day == 7) {
												// do nothing
											} else {
												if (Boolean.TRUE.equals(holidayService.isAvailableDate(targetDateTmp))) {
													counterDate++;
												}
											}
											
											if (counterDate < dataCounterTypeDtl.getSla().intValue()) {
												calendar.setTime(targetDateTmp);
												calendar.add(Calendar.DAY_OF_MONTH, -1);
												targetDateTmp = calendar.getTime();
											}
										}
									}
									
									InternalRegulationPenerbitanPicTpkEmail internalRegulationPenerbitanPicTpkEmail =  new InternalRegulationPenerbitanPicTpkEmail();
									
									internalRegulationPenerbitanPicTpkEmail.setInternalRegulationPenerbitanPicTpk(picTpk);
									internalRegulationPenerbitanPicTpkEmail.setSla(dataCounterTypeDtl.getSla().intValue());
									internalRegulationPenerbitanPicTpkEmail.setSlaType(dataCounterTypeDtl.getSlaType());
									internalRegulationPenerbitanPicTpkEmail.setEmailDate(new Timestamp(targetDateTmp.getTime()));
									internalRegulationPenerbitanPicTpkEmail.setCreatedBy(facesUtil.retrieveUserLogin());
									internalRegulationPenerbitanPicTpkEmail.setCreationDate(new Timestamp(new Date().getTime()));
									internalRegulationPenerbitanPicTpkEmail.setEnabledFlag(Constants.CONSTANT_YES);
									internalRegulationPenerbitanPicTpkEmail.setDelId(0l);
									
									regulationPenerbitanPicTpkEmailList.add(internalRegulationPenerbitanPicTpkEmail);		
									
									//tambahan set targetDate 
									if(countTargetDate == 0) {
										targetDateSendEmail = sdfFull.format(targetDateTmp);
									}
									countTargetDate++;
									
								}
								picTpk.setInternalRegulationPenerbitanPicTpkEmailList(regulationPenerbitanPicTpkEmailList);								
							}
						}
						// save email reminder
					}
				}
				
				if(irg.getIrgPicIrgs() !=null && irg.getIrgPicIrgs().size() > 0) {
					for(InternalRegulationPenerbitanPicIrg picIrg : irg.getIrgPicIrgs()) {
						picIrg.setIrg(irg);
						if (picIrg.getCreatedBy() == null) {
							picIrg.setCreatedBy(facesUtil.retrieveUserLogin());
							picIrg.setCreationDate(new Timestamp(new Date().getTime()));
						}else {
							picIrg.setLastUpdateBy(facesUtil.retrieveUserLogin());
							picIrg.setLastUpdateDate(new Timestamp(new Date().getTime()));
						}	
						
						picIrg.setDelId(0l);
						picIrg.setEnabledFlag(Constants.CONSTANT_YES);
					}
				}
				
				/*if (emailGroupTpkList != null && !emailGroupTpkList.isEmpty()) {
					if (emailGroupTpkList.size() == 1) {
						irg.setEmailGroupTpk(emailGroupTpkList.get(0));
					} else {
						String emailGroupTpkTemp = "";
						for (String string : emailGroupTpkList) {
							if (StringUtils.isBlank(emailGroupTpkTemp)) {
								emailGroupTpkTemp = string;
							} else {
								emailGroupTpkTemp = emailGroupTpkTemp.concat(";").concat(string);
							}
						}
						irg.setEmailGroupTpk(emailGroupTpkTemp);
					}
				}*/
				
				String emailGroupTpkData = "";
				if(irg.getIrgEmailGroups() !=null && irg.getIrgEmailGroups().size() > 0) {
					for(InternalRegulationPenerbitanEmailGroup emailGroupIrg : irg.getIrgEmailGroups()) {
						emailGroupIrg.setIrg(irg);
						if (emailGroupIrg.getCreatedBy() == null) {
							emailGroupIrg.setCreatedBy(facesUtil.retrieveUserLogin());
							emailGroupIrg.setCreationDate(new Timestamp(new Date().getTime()));
						}else {
							emailGroupIrg.setLastUpdateBy(facesUtil.retrieveUserLogin());
							emailGroupIrg.setLastUpdateDate(new Timestamp(new Date().getTime()));
						}	
						
						emailGroupIrg.setDelId(0l);
						emailGroupIrg.setEnabledFlag(Constants.CONSTANT_YES);
						
						if(emailGroupIrg.getCheckFlag()) {
							emailGroupIrg.setReviewApprovalFlag("Y");
						}else {
							emailGroupIrg.setReviewApprovalFlag("N");
						}
						
						if (emailGroupIrg.getReviewApprovalFlag() != null
								&& emailGroupIrg.getReviewApprovalFlag().equals("N")) {
							if (StringUtils.isBlank(emailGroupTpkData)) {
								emailGroupTpkData = emailGroupIrg.getEmailGroup();
							} else {
								emailGroupTpkData = emailGroupTpkData.concat(";").concat(emailGroupIrg.getEmailGroup());
							}
						}
						
						irg.setEmailGroupTpkData(emailGroupTpkData);
					}
				}
				
				internalRegulationPenerbitanService.saveStep1(irg, facesUtil.retrieveUserLogin(),
						dataIrgPenerbitanPicTpgDeleteList, dataIrgPenerbitanPicTpkDeleteList, 
						dataIrgPenerbitanEmailGroupDeleteList);
				
				if(irg.getProcessStatus() == null) {
					irg.setProcessStatus(new ParameterDetail());
				}
				
				if(irg.getRegObsoleteType() == null) {
					irg.setRegObsoleteType(new ParameterDetail());
				}				
				
				// this should be a singleton
		        ExecutorService emailExecutor = Executors.newCachedThreadPool();
		        
		        // from you sendEmail() method
		        emailExecutor.execute(new Runnable() {
		        	@Override
		            public void run() {
		                try {
		                	sendEmail(irg.getIrgAttachmentList(), irg.getEmailGroupTpkData());
		                } catch (Exception e) {
		                    logger.error("send email failed", e);
		                }
		            }
		        });
			}
		} catch (Exception e) {
			e.printStackTrace();
			isValidateStep1 = false;
		}
	}
	
	private void saveStep2() {
		try {
			if(irg.getProcessStatus() == null || irg.getProcessStatus().getParameterDtlId() == null) {
				irg.setProcessStatus(null);
			}
			
			if(irg.getRegObsoleteType() == null || irg.getRegObsoleteType().getParameterDtlId() == null) {
				irg.setRegObsoleteType(null);
			}
			
			internalRegulationPenerbitanService.saveStep2(irg, facesUtil.retrieveUserLogin());
		} catch (Exception e) {
			e.printStackTrace();
			isValidateStep2 = false;
		}
	}
	
	public void cancel() {
		try {
			facesUtil.redirect("/pages/internalRegulationPenerbitan/internalRegulationPenerbitan.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveStep1AndRedirect()  {
		try {
			saveStep1();
			if(isValidateStep1) {
				facesUtil.redirect("/pages/internalRegulationPenerbitan/internalRegulationPenerbitan.faces");
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void saveStep2AndRedirect() {
		try {
			if (isValidateStep2()) {
				saveStep2();
				
				facesUtil.redirect("/pages/internalRegulationPenerbitan/internalRegulationPenerbitan.faces");
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void sendEmail(List<InternalRegulationPenerbitanAttachment> irgAttachmentList, String emailGroupTpk) {
		try {
			List<SendEmailVo> sendEmailList = new ArrayList<>();
			ParameterDetail pdFilePath = parameterDetailService.getParameterDetailByParamDtlCode("ATTACHMENT_FILE_PATH");
			
			if (actionMode.equalsIgnoreCase(Constants.ACTION_EDIT)) {
				sendEmailWhenEdit(sendEmailList, emailGroupTpk);
			} else {
				sendEmailWhenAdd(sendEmailList, emailGroupTpk);
			}
			
			if (!sendEmailList.isEmpty()) {
				for (SendEmailVo sendEmailVo : sendEmailList) {
					final String subject = sendEmailVo.getSubject();
					final String content = sendEmailVo.getContent();
					final String to = sendEmailVo.getEmailTo();
					final String cc = sendEmailVo.getEmailCc();
					
					List<File> attachmentList = new ArrayList<>();
					
					if (irgAttachmentList != null && !irgAttachmentList.isEmpty()) {
						for (InternalRegulationPenerbitanAttachment dataIrgAttachment : irgAttachmentList) {
							File file = new File(pdFilePath.getNameIn()+dataIrgAttachment.getFileId());
							File file1 = new File(pdFilePath.getNameIn()+dataIrgAttachment.getAttachmentFile());
							FileUtils.copyFile(file, file1);
							
							attachmentList.add(file1);
						}
					}
					
					if (StringUtils.isNotBlank(to)) {
						CallApiManager.sendEmailAPIWithAttachment(to, cc, subject, content, to, cc, parameterDetailService, !attachmentList.isEmpty() ? attachmentList : null);
					}
					
					if (attachmentList != null && !attachmentList.isEmpty()) {
						for (File file : attachmentList) {
							if (file.exists()) {
								file.delete();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendEmailWhenEdit(List<SendEmailVo> sendEmailList, String emailGroupTpk) {
		try {
			List<InternalRegulationPenerbitanPicTpk> irgPicTpkList = new ArrayList<>();
			EmailTemplate emailTemplate = new EmailTemplate();
			
			if(irg.getCounterType() != null && irg.getCounterType().getDetails().size()==1)
			{
				CounterTypeDtl dtl = irg.getCounterType().getDetails().get(0);
				if(dtl.getSla() == 3)
				{
					emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_IRG_REVIEW_H_PLUS_3");
				}
				else if(dtl.getSla() == 5)
				{
					emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_IRG_REVIEW_H_PLUS_5");
				} 
				else
				{
				   emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_IRG_REVIEW");
				}
			}
			else
			{
				emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_IRG_REVIEW");
			}
			
			ParameterDetail pdRegulationType = parameterDetailService.findById(irg.getRegulationType().getParameterDtlId());
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMMMM yyyy");
			
			String emailSubject = emailTemplate.getEmailSubject();
			String emailContent = emailTemplate.getEmailContent();
			
			String emailCcPicIrg = "";
			String emailCcPicTpg = "";
			String emailCcPicTpk = "";
			String emailToGroupTpk = "";
			String emailTo = "";
			String emailCc = "";
			String namePicIrg1 = "";
			String namePicIrg2 = "";
			String namePicIrg3 = "";
			String namePicTpg1 = "";
			
			if (StringUtils.isNotBlank(emailGroupTpk)) {
				if (emailGroupTpk.contains(";")) {
					String[] arrSplit = emailGroupTpk.split(";");
					for (String string : arrSplit) {
						if (!emailGroupTpkMap.containsKey(string)) {
							if (StringUtils.isBlank(emailToGroupTpk)) {
								emailToGroupTpk = string;
							} else {
								emailToGroupTpk = emailToGroupTpk.concat(",").concat(string);
							}
						}
					}
				} else {
					if (!emailGroupTpkMap.containsKey(emailGroupTpk)) {
						if (StringUtils.isBlank(emailToGroupTpk)) {
							emailToGroupTpk = emailGroupTpk;
						} else {
							emailToGroupTpk = emailToGroupTpk.concat(",").concat(emailGroupTpk);
						}
					}
				}
			}
			
			if (!irg.getIrgPicIrgs().isEmpty()) {
				for (InternalRegulationPenerbitanPicIrg dataPicIrg : irg.getIrgPicIrgs()) {
					if (dataPicIrg.getUser1() != null) {
						namePicIrg1 = dataPicIrg.getUser1().getName();
						if (StringUtils.isNotBlank(dataPicIrg.getUser1().getEmail())) {
							if (StringUtils.isBlank(emailCcPicIrg)) {
								emailCcPicIrg = dataPicIrg.getUser1().getEmail();
							} else {
								emailCcPicIrg = emailCcPicIrg.concat(",").concat(dataPicIrg.getUser1().getEmail());
							}
						}
					}
					if (dataPicIrg.getUser2() != null) {
						namePicIrg2 = dataPicIrg.getUser2().getName();
						if (StringUtils.isNotBlank(dataPicIrg.getUser2().getEmail())) {
							if (StringUtils.isBlank(emailCcPicIrg)) {
								emailCcPicIrg = dataPicIrg.getUser2().getEmail();
							} else {
								emailCcPicIrg = emailCcPicIrg.concat(",").concat(dataPicIrg.getUser2().getEmail());
							}
						}
					}
					if(dataPicIrg.getUser3() != null) {
						namePicIrg3 = dataPicIrg.getUser3().getName();
						if(StringUtils.isNotBlank(dataPicIrg.getUser2().getEmail())) {
							if (StringUtils.isBlank(emailCcPicIrg)) {
								emailCcPicIrg = dataPicIrg.getUser3().getEmail();
							} else {
								emailCcPicIrg = emailCcPicIrg.concat(",").concat(dataPicIrg.getUser3().getEmail());
							}
						}
					}
				}
			}
			
			if (!irg.getIrgPicTpgs().isEmpty()) {
				int idx = 0;
				for (InternalRegulationPenerbitanPicTpg dataIrgPicTpg : irg.getIrgPicTpgs()) {
					if (dataIrgPicTpg.getUser1() != null && StringUtils.isNotBlank(dataIrgPicTpg.getUser1().getEmail())) {
						
						if (idx == 0) {
							namePicTpg1 = dataIrgPicTpg.getUser1().getName();
						}
						
						if (StringUtils.isNotBlank(emailCcPicTpg)) {
							emailCcPicTpg = dataIrgPicTpg.getUser1().getEmail();
						} else {
							emailCcPicTpg = emailCcPicTpg.concat(",").concat(dataIrgPicTpg.getUser1().getEmail());
						}
					}
					
					if (dataIrgPicTpg.getUser2() != null && StringUtils.isNotBlank(dataIrgPicTpg.getUser2().getEmail())) {
						if (StringUtils.isBlank(emailCcPicTpg)) {
							emailCcPicTpg = dataIrgPicTpg.getUser2().getEmail();
						} else {
							emailCcPicTpg = emailCcPicTpg.concat(",").concat(dataIrgPicTpg.getUser2().getEmail());
						}
					}
					
					if (dataIrgPicTpg.getUser3() != null && StringUtils.isNotBlank(dataIrgPicTpg.getUser3().getEmail())) {
						if (StringUtils.isBlank(emailCcPicTpg)) {
							emailCcPicTpg = dataIrgPicTpg.getUser3().getEmail();
						} else {
							emailCcPicTpg = emailCcPicTpg.concat(",").concat(dataIrgPicTpg.getUser3().getEmail());
						}
					}
					idx++;
				}
			}
			
			if (!irg.getIrgPicTpks().isEmpty()) {
				for (InternalRegulationPenerbitanPicTpk dataPicTpk : irg.getIrgPicTpks()) {
					if (Boolean.TRUE.equals(dataPicTpk.getIsEditableTemp())) {
						irgPicTpkList.add(dataPicTpk);
					}
				}
			}
			
			if (!irgPicTpkList.isEmpty()) {
				Map<Date, SendEmailVo> sendEmailVoByTargetDateMap = new HashMap<>();
				
				for (int i = 0; i < irgPicTpkList.size(); i++) {
					InternalRegulationPenerbitanPicTpk dataIrgPicTpk = irgPicTpkList.get(i);
					
					if (sendEmailVoByTargetDateMap.containsKey(dataIrgPicTpk.getTargetDate())) {
						SendEmailVo dataSendEmail = sendEmailVoByTargetDateMap.get(dataIrgPicTpk.getTargetDate());
						
						emailTo = dataSendEmail.getEmailTo();
						emailCc = dataSendEmail.getEmailCc();
						
						if (dataIrgPicTpk.getUser1() != null && StringUtils.isNotBlank(dataIrgPicTpk.getUser1().getEmail())) {
							if (StringUtils.isBlank(emailCc)) {
								emailCc = dataIrgPicTpk.getUser1().getEmail();
							} else {
								emailCc = emailCc.concat(",").concat(dataIrgPicTpk.getUser1().getEmail());
							}
						}
						
						if (dataIrgPicTpk.getUser2() != null && StringUtils.isNotBlank(dataIrgPicTpk.getUser2().getEmail())) {
							if (StringUtils.isBlank(emailTo)) {
								emailTo = dataIrgPicTpk.getUser2().getEmail();
							} else {
								emailTo = emailTo.concat(",").concat(dataIrgPicTpk.getUser2().getEmail());
							}
						}
						
						if (dataIrgPicTpk.getUser3() != null && StringUtils.isNotBlank(dataIrgPicTpk.getUser3().getEmail())) {
							if (StringUtils.isBlank(emailCc)) {
								emailCc = dataIrgPicTpk.getUser3().getEmail();
							} else {
								emailCc = emailCc.concat(",").concat(dataIrgPicTpk.getUser3().getEmail());
							}
						}
						
						dataSendEmail.setEmailTo(emailTo);
						dataSendEmail.setEmailCc(emailCc);
						
						sendEmailVoByTargetDateMap.replace(dataIrgPicTpk.getTargetDate(), dataSendEmail);
					} else {
						SendEmailVo sendEmail = new SendEmailVo();
						
						emailSubject = emailSubject.replaceAll("judul_regulasi", irg.getIrgTitle());
						emailSubject = emailSubject.replaceAll("tipe_peraturan", pdRegulationType.getNameIn());						
						
						emailContent = emailContent.replaceAll("nama_pic_tpg1", replaceUserToLowerCaseAndFirstLetterUpper(namePicTpg1));
						emailContent = emailContent.replaceAll("nama_pic_irg1", replaceUserToLowerCaseAndFirstLetterUpper(namePicIrg1));
						emailContent = emailContent.replaceAll("Tanggal_Due_Date", targetDateSendEmail);
						
						if (dataIrgPicTpk.getUser1() != null && StringUtils.isNotBlank(dataIrgPicTpk.getUser1().getEmail())) {
							if (StringUtils.isBlank(emailCcPicTpk)) {
								emailCcPicTpk = dataIrgPicTpk.getUser1().getEmail();
							} else {
								emailCcPicTpk = emailCcPicTpk.concat(",").concat(dataIrgPicTpk.getUser1().getEmail());
							}
						}
						
						if (dataIrgPicTpk.getUser2() != null && StringUtils.isNotBlank(dataIrgPicTpk.getUser2().getEmail())) {
							if (StringUtils.isBlank(emailTo)) {
								emailTo = dataIrgPicTpk.getUser2().getEmail();
							} else {
								emailTo = emailTo.concat(",").concat(dataIrgPicTpk.getUser2().getEmail());
							}
						}
						
						if (dataIrgPicTpk.getUser3() != null && StringUtils.isNotBlank(dataIrgPicTpk.getUser3().getEmail())) {
							if (StringUtils.isBlank(emailCcPicTpk)) {
								emailCcPicTpk = dataIrgPicTpk.getUser3().getEmail();
							} else {
								emailCcPicTpk = emailCcPicTpk.concat(",").concat(dataIrgPicTpk.getUser3().getEmail());
							}
						}
						
						if (StringUtils.isNotBlank(emailCcPicIrg)) {
							if (StringUtils.isBlank(emailCc)) {
								emailCc = emailCcPicIrg;	
							} else {
								emailCc = emailCc.concat(",").concat(emailCcPicIrg);
							}
						}
						
						if (StringUtils.isNotBlank(emailCcPicTpg)) {
							if (StringUtils.isBlank(emailCc)) {
								emailCc = emailCcPicTpg;
							} else {
								emailCc = emailCc.concat(",").concat(emailCcPicTpg);
							}
						}
						
						if (StringUtils.isNotBlank(emailCcPicTpk)) {
							if (StringUtils.isBlank(emailCc)) {
								emailCc = emailCcPicTpk;
							} else {
								emailCc = emailCc.concat(",").concat(emailCcPicTpk);
							}
						}
						
						if (StringUtils.isNotBlank(emailToGroupTpk)) {
							if (StringUtils.isBlank(emailTo)) {
								emailTo = emailToGroupTpk;
							} else {
								emailTo = emailTo.concat(",").concat(emailToGroupTpk);
							}
						}
						
						sendEmail.setEmailTo(emailTo);
						sendEmail.setEmailCc(emailCc);
						sendEmail.setSubject(emailSubject);
						sendEmail.setContent(emailContent);						
						
						sendEmailVoByTargetDateMap.put(dataIrgPicTpk.getTargetDate(), sendEmail);
					}
					
					emailSubject = emailTemplate.getEmailSubject();
					emailContent = emailTemplate.getEmailContent();
				}
				
				if (sendEmailVoByTargetDateMap != null && !sendEmailVoByTargetDateMap.isEmpty()) {
					for (SendEmailVo sendEmailVo : sendEmailVoByTargetDateMap.values()) {
						sendEmailList.add(sendEmailVo);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendEmailWhenAdd(List<SendEmailVo> sendEmailList, String emailGroupTpk) {
		try {
			EmailTemplate emailTemplate = new EmailTemplate();
			
			if(irg !=null && irg.getCounterType() != null && irg.getCounterType().getDetails().size()==1)
			{
				CounterTypeDtl dtl = irg.getCounterType().getDetails().get(0);
				if(dtl.getSla() == 3)
				{
					emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_IRG_REVIEW_H_PLUS_3");
				}
				else if(dtl.getSla() == 5)
				{
					emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_IRG_REVIEW_H_PLUS_5");
				} 
				else
				{
				   emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_IRG_REVIEW");
				}
			}
			else
			{
				emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_IRG_REVIEW");
			}
			
			ParameterDetail pdRegulationType = parameterDetailService.findById(irg.getRegulationType().getParameterDtlId());
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMMMM yyyy");
			
			String emailSubject = emailTemplate.getEmailSubject();
			String emailContent = emailTemplate.getEmailContent();
			
			String emailCcPicIrg = "";
			String emailCcPicTpg = "";
			String emailCcPicTpk = "";
			String emailCcGroupTpk = "";
			String emailTo = "";
			String emailCc = "";
			String namePicIrg1 = "";
			String namePicIrg2 = "";
			String namePicIrg3 = "";
			String namePicTpg1 = "";
			
			if (StringUtils.isNotBlank(emailGroupTpk)) {
				if (emailGroupTpk.contains(";")) {
					String[] splitStr = emailGroupTpk.split(";");
					
					for (String string : splitStr) {
						if (StringUtils.isNotBlank(string)) {
							if (StringUtils.isBlank(emailCcGroupTpk)) {
								emailCcGroupTpk = string;
							} else {
								emailCcGroupTpk = emailCcGroupTpk.concat(",").concat(string);
							}
						}
					}
				} else {
					if (StringUtils.isNotBlank(emailGroupTpk)) {
						if (StringUtils.isEmpty(emailCcGroupTpk)) {
							emailCcGroupTpk = emailGroupTpk;
						} else {
							emailCcGroupTpk = emailCcGroupTpk.concat(",").concat(emailGroupTpk);
						}
					}
				}
			}
			
			if (!irg.getIrgPicIrgs().isEmpty()) {
				for (InternalRegulationPenerbitanPicIrg dataPicIrg : irg.getIrgPicIrgs()) {
					if (dataPicIrg.getUser1() != null) {
						namePicIrg1 = dataPicIrg.getUser1().getName();
						if (StringUtils.isNotBlank(dataPicIrg.getUser1().getEmail())) {
							if (StringUtils.isBlank(emailCcPicIrg)) {
								emailCcPicIrg = dataPicIrg.getUser1().getEmail();
							} else {
								emailCcPicIrg = emailCcPicIrg.concat(",").concat(dataPicIrg.getUser1().getEmail());
							}
						}
					}
					if (dataPicIrg.getUser2() != null) {
						namePicIrg2 = dataPicIrg.getUser2().getName();
						if (StringUtils.isNotBlank(dataPicIrg.getUser2().getEmail())) {
							if (StringUtils.isBlank(emailCcPicIrg)) {
								emailCcPicIrg = dataPicIrg.getUser2().getEmail();
							} else {
								emailCcPicIrg = emailCcPicIrg.concat(",").concat(dataPicIrg.getUser2().getEmail());
							}
						}
					}
					if(dataPicIrg.getUser3() != null) {
						namePicIrg3 = dataPicIrg.getUser3().getName();
						if(StringUtils.isNotBlank(dataPicIrg.getUser2().getEmail())) {
							if (StringUtils.isBlank(emailCcPicIrg)) {
								emailCcPicIrg = dataPicIrg.getUser3().getEmail();
							} else {
								emailCcPicIrg = emailCcPicIrg.concat(",").concat(dataPicIrg.getUser3().getEmail());
							}
						}
					}
				}
			}
			
			if (!irg.getIrgPicTpgs().isEmpty()) {
				int idx = 0;
				for (InternalRegulationPenerbitanPicTpg dataIrgPicTpg : irg.getIrgPicTpgs()) {
					if (dataIrgPicTpg.getUser1() != null && StringUtils.isNotBlank(dataIrgPicTpg.getUser1().getEmail())) {
						if (idx == 0) {
							namePicTpg1 = dataIrgPicTpg.getUser1().getName();
						}
						
						if (StringUtils.isBlank(emailCcPicTpg)) {
							emailCcPicTpg = dataIrgPicTpg.getUser1().getEmail();
						} else {
							emailCcPicTpg = emailCcPicTpg.concat(",").concat(dataIrgPicTpg.getUser1().getEmail());
						}
					}
					
					if (dataIrgPicTpg.getUser2() != null && StringUtils.isNotBlank(dataIrgPicTpg.getUser2().getEmail())) {
						if (StringUtils.isBlank(emailCcPicTpg)) {
							emailCcPicTpg = dataIrgPicTpg.getUser2().getEmail();
						} else {
							emailCcPicTpg = emailCcPicTpg.concat(",").concat(dataIrgPicTpg.getUser2().getEmail());
						}
					}
					
					if (dataIrgPicTpg.getUser3() != null && StringUtils.isNotBlank(dataIrgPicTpg.getUser3().getEmail())) {
						if (StringUtils.isBlank(emailCcPicTpg)) {
							emailCcPicTpg = dataIrgPicTpg.getUser3().getEmail();
						} else {
							emailCcPicTpg = emailCcPicTpg.concat(",").concat(dataIrgPicTpg.getUser3().getEmail());
						}
					}
					idx++;
				}
			}
			
			if (!irg.getIrgPicTpks().isEmpty()) {
				Map<Date, SendEmailVo> sendEmailVoByTargetDateMap = new HashMap<>();
				
				for (int i = 0; i < irg.getIrgPicTpks().size(); i++) {
					InternalRegulationPenerbitanPicTpk dataIrgPicTpk = irg.getIrgPicTpks().get(i);
					
					if (sendEmailVoByTargetDateMap.containsKey(dataIrgPicTpk.getTargetDate())) {
						SendEmailVo dataSendEmail = sendEmailVoByTargetDateMap.get(dataIrgPicTpk.getTargetDate());
						
						emailTo = dataSendEmail.getEmailTo();
						emailCc = dataSendEmail.getEmailCc();
						
						if (dataIrgPicTpk.getUser1() != null && StringUtils.isNotBlank(dataIrgPicTpk.getUser1().getEmail())) {
							if (StringUtils.isBlank(emailCc)) {
								emailCc = dataIrgPicTpk.getUser1().getEmail();
							} else {
								emailCc = emailCc.concat(",").concat(dataIrgPicTpk.getUser1().getEmail());
							}
						}
						
						if (dataIrgPicTpk.getUser2() != null && StringUtils.isNotBlank(dataIrgPicTpk.getUser2().getEmail())) {
							if (StringUtils.isBlank(emailTo)) {
								emailTo = dataIrgPicTpk.getUser2().getEmail();
							} else {
								emailTo = emailTo.concat(",").concat(dataIrgPicTpk.getUser2().getEmail());
							}
						}
						
						if (dataIrgPicTpk.getUser3() != null && StringUtils.isNotBlank(dataIrgPicTpk.getUser3().getEmail())) {
							if (StringUtils.isBlank(emailCc)) {
								emailCc = dataIrgPicTpk.getUser3().getEmail();
							} else {
								emailCc = emailCc.concat(",").concat(dataIrgPicTpk.getUser3().getEmail());
							}
						}
						
						dataSendEmail.setEmailTo(emailTo);
						dataSendEmail.setEmailCc(emailCc);
						
						sendEmailVoByTargetDateMap.replace(dataIrgPicTpk.getTargetDate(), dataSendEmail);
					} else {
						SendEmailVo sendEmail = new SendEmailVo();
						
						emailSubject = emailSubject.replaceAll("judul_regulasi", irg.getIrgTitle());
						emailSubject = emailSubject.replaceAll("tipe_peraturan", pdRegulationType.getNameIn());
						
						emailContent = emailContent.replaceAll("nama_pic_tpg1", replaceUserToLowerCaseAndFirstLetterUpper(namePicTpg1));
						emailContent = emailContent.replaceAll("nama_pic_irg1", replaceUserToLowerCaseAndFirstLetterUpper(namePicIrg1));
						emailContent = emailContent.replaceAll("Tanggal_Due_Date", targetDateSendEmail);
						
						if (dataIrgPicTpk.getUser1() != null && StringUtils.isNotBlank(dataIrgPicTpk.getUser1().getEmail())) {
							if (StringUtils.isBlank(emailCcPicTpk)) {
								emailCcPicTpk = dataIrgPicTpk.getUser1().getEmail();
							} else {
								emailCcPicTpk = emailCcPicTpk.concat(",").concat(dataIrgPicTpk.getUser1().getEmail());
							}
						}
						
						if (dataIrgPicTpk.getUser2() != null && StringUtils.isNotBlank(dataIrgPicTpk.getUser2().getEmail())) {
							if (StringUtils.isBlank(emailTo)) {
								emailTo = dataIrgPicTpk.getUser2().getEmail();
							} else {
								emailTo = emailTo.concat(",").concat(dataIrgPicTpk.getUser2().getEmail());
							}
						}
						
						if (dataIrgPicTpk.getUser3() != null && StringUtils.isNotBlank(dataIrgPicTpk.getUser3().getEmail())) {
							if (StringUtils.isBlank(emailCcPicTpk)) {
								emailCcPicTpk = dataIrgPicTpk.getUser3().getEmail();
							} else {
								emailCcPicTpk = emailCcPicTpk.concat(",").concat(dataIrgPicTpk.getUser3().getEmail());
							}
						}
						
						if (StringUtils.isNotBlank(emailCcPicIrg)) {
							if (StringUtils.isBlank(emailCc)) {
								emailCc = emailCcPicIrg;	
							} else {
								emailCc = emailCc.concat(",").concat(emailCcPicIrg);
							}
						}
						
						if (StringUtils.isNotBlank(emailCcPicTpg)) {
							if (StringUtils.isBlank(emailCc)) {
								emailCc = emailCcPicTpg;
							} else {
								emailCc = emailCc.concat(",").concat(emailCcPicTpg);
							}
						}
						
						if (StringUtils.isNotBlank(emailCcPicTpk)) {
							if (StringUtils.isBlank(emailCc)) {
								emailCc = emailCcPicTpk;
							} else {
								emailCc = emailCc.concat(",").concat(emailCcPicTpk);
							}
						}
						
						if (StringUtils.isNotBlank(emailCcGroupTpk)) {
							if (StringUtils.isBlank(emailCc)) {
								emailTo = emailCcGroupTpk;
							} else {
								emailTo = emailTo.concat(",").concat(emailCcGroupTpk);
							}
						}
						
						sendEmail.setEmailTo(emailTo);
						sendEmail.setEmailCc(emailCc);
						sendEmail.setSubject(emailSubject);
						sendEmail.setContent(emailContent);
						
						sendEmailVoByTargetDateMap.put(dataIrgPicTpk.getTargetDate(), sendEmail);
					}
				}
				
				if (sendEmailVoByTargetDateMap != null && !sendEmailVoByTargetDateMap.isEmpty()) {
					for (SendEmailVo sendEmailVo : sendEmailVoByTargetDateMap.values()) {
						sendEmailList.add(sendEmailVo);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onChangeCheckBoxEmailTpk() {
		String indexCheck = facesUtil.retrieveRequestParam("indexCheckBoxEmailGroupRow");
		Integer indexCheckBoxRow = Integer.parseInt(indexCheck);
		
		if(irg.getIrgEmailGroups().get(indexCheckBoxRow).getCheckFlag()) {
			irg.getIrgEmailGroups().get(indexCheckBoxRow).setReviewApprovalFlag("Y");
		}else {
			irg.getIrgEmailGroups().get(indexCheckBoxRow).setReviewApprovalFlag("N");
		}
		
		PrimeFaces.current().ajax().update("form:dataTableEmailGroupTpk:"+indexCheckBoxRow+":checkboxEmailGroupTpk");
		
		PrimeFaces.current().executeScript("initSelect2();");		
	}
	
	public void onAddNewEmailGroupTpk() {
		if (irg.getIrgEmailGroups() == null
				|| irg.getIrgEmailGroups().size() == 0) {
			irg.setIrgEmailGroups(new ArrayList<InternalRegulationPenerbitanEmailGroup>());
			lastSequenceOfEmailGroup = 0;
		}  else {
			if(irg.getIrgEmailGroups().size() == 0) {
				lastSequenceOfEmailGroup = 0;
			}			
		} 

		InternalRegulationPenerbitanEmailGroup rt = new InternalRegulationPenerbitanEmailGroup();
		lastSequenceOfEmailGroup = lastSequenceOfEmailGroup + 1;
		rt.setSequence(lastSequenceOfEmailGroup);
		//rt.setEditableTemp(true);
		irg.getIrgEmailGroups().add(rt);

		tableModelEmailGroupTpk.setWrappedData(irg.getIrgEmailGroups());
		
		PrimeFaces.current().executeScript("initSelect2();");
	}

	public void onDeleteRowEmailGroupTpk() {
		for (int i = 0; i < selectedDataEmailGroupTpk.length; i++) {
			dataIrgPenerbitanEmailGroupDeleteList.add(selectedDataEmailGroupTpk[i]);
			irg.getIrgEmailGroups().remove(selectedDataEmailGroupTpk[i]);
		}
		
		if (irg.getIrgEmailGroups() == null
				|| irg.getIrgEmailGroups().size() == 0) {
			lastSequenceOfEmailGroup = 0;
		}

		tableModelEmailGroupTpk.setWrappedData(irg.getIrgEmailGroups());
		
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	
	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		InternalRegulationPenerbitanEditBean.logger = logger;
	}

	public Boolean getIsViewOnly() {
		return isViewOnly;
	}

	public void setIsViewOnly(Boolean isViewOnly) {
		this.isViewOnly = isViewOnly;
	}

	public Integer getLastSequenceOfPicTpg() {
		return lastSequenceOfPicTpg;
	}

	public void setLastSequenceOfPicTpg(Integer lastSequenceOfPicTpg) {
		this.lastSequenceOfPicTpg = lastSequenceOfPicTpg;
	}

	public Integer getLastSequenceOfPicIrg() {
		return lastSequenceOfPicIrg;
	}

	public void setLastSequenceOfPicIrg(Integer lastSequenceOfPicIrg) {
		this.lastSequenceOfPicIrg = lastSequenceOfPicIrg;
	}

	public Integer getLastSequenceOfPictpk() {
		return lastSequenceOfPictpk;
	}

	public void setLastSequenceOfPictpk(Integer lastSequenceOfPictpk) {
		this.lastSequenceOfPictpk = lastSequenceOfPictpk;
	}

	public Integer getIndexDtlFollowupTpg() {
		return indexDtlFollowupTpg;
	}

	public void setIndexDtlFollowupTpg(Integer indexDtlFollowupTpg) {
		this.indexDtlFollowupTpg = indexDtlFollowupTpg;
	}

	public Integer getIndexDtlFollowupTpk() {
		return indexDtlFollowupTpk;
	}

	public void setIndexDtlFollowupTpk(Integer indexDtlFollowupTpk) {
		this.indexDtlFollowupTpk = indexDtlFollowupTpk;
	}

	public Integer getIndexDtlFollowupIrg() {
		return indexDtlFollowupIrg;
	}

	public void setIndexDtlFollowupIrg(Integer indexDtlFollowupIrg) {
		this.indexDtlFollowupIrg = indexDtlFollowupIrg;
	}

	public Long getCounterTypeId() {
		return counterTypeId;
	}

	public void setCounterTypeId(Long counterTypeId) {
		this.counterTypeId = counterTypeId;
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

	public String getStatusRegulation() {
		return statusRegulation;
	}

	public void setStatusRegulation(String statusRegulation) {
		this.statusRegulation = statusRegulation;
	}

	public String getTypeRegulation() {
		return typeRegulation;
	}

	public void setTypeRegulation(String typeRegulation) {
		this.typeRegulation = typeRegulation;
	}

	public String getStatusFeedbackTpg() {
		return statusFeedbackTpg;
	}

	public void setStatusFeedbackTpg(String statusFeedbackTpg) {
		this.statusFeedbackTpg = statusFeedbackTpg;
	}

	public String getStatusFeedbackTpk() {
		return statusFeedbackTpk;
	}

	public void setStatusFeedbackTpk(String statusFeedbackTpk) {
		this.statusFeedbackTpk = statusFeedbackTpk;
	}

	public String getStatusTglFeedbackTpg() {
		return statusTglFeedbackTpg;
	}

	public void setStatusTglFeedbackTpg(String statusTglFeedbackTpg) {
		this.statusTglFeedbackTpg = statusTglFeedbackTpg;
	}

	public String getStatusProcess() {
		return statusProcess;
	}

	public void setStatusProcess(String statusProcess) {
		this.statusProcess = statusProcess;
	}

	public String getTypeRegObsolete() {
		return typeRegObsolete;
	}

	public void setTypeRegObsolete(String typeRegObsolete) {
		this.typeRegObsolete = typeRegObsolete;
	}

	public String getUnitKerjaTpg() {
		return unitKerjaTpg;
	}

	public void setUnitKerjaTpg(String unitKerjaTpg) {
		this.unitKerjaTpg = unitKerjaTpg;
	}

	public List<SelectItem> getStatusRegulations() {
		return statusRegulations;
	}

	public void setStatusRegulations(List<SelectItem> statusRegulations) {
		this.statusRegulations = statusRegulations;
	}

	public List<SelectItem> getTypeRegulations() {
		return typeRegulations;
	}

	public void setTypeRegulations(List<SelectItem> typeRegulations) {
		this.typeRegulations = typeRegulations;
	}

	public List<SelectItem> getStatusProcesss() {
		return statusProcesss;
	}

	public void setStatusProcesss(List<SelectItem> statusProcesss) {
		this.statusProcesss = statusProcesss;
	}

	public List<SelectItem> getTypeRegObsoletes() {
		return typeRegObsoletes;
	}

	public void setTypeRegObsoletes(List<SelectItem> typeRegObsoletes) {
		this.typeRegObsoletes = typeRegObsoletes;
	}

	public List<SelectItem> getCounterTypes() {
		return counterTypes;
	}

	public void setCounterTypes(List<SelectItem> counterTypes) {
		this.counterTypes = counterTypes;
	}

	public List<SelectItem> getUnitKerjas() {
		return unitKerjas;
	}

	public void setUnitKerjas(List<SelectItem> unitKerjas) {
		this.unitKerjas = unitKerjas;
	}

	public SelectorInfo getSelectorPicTpg1() {
		return selectorPicTpg1;
	}

	public void setSelectorPicTpg1(SelectorInfo selectorPicTpg1) {
		this.selectorPicTpg1 = selectorPicTpg1;
	}

	public SelectorInfo getSelectorPicTpg2() {
		return selectorPicTpg2;
	}

	public void setSelectorPicTpg2(SelectorInfo selectorPicTpg2) {
		this.selectorPicTpg2 = selectorPicTpg2;
	}

	public SelectorInfo getSelectorPicTpg3() {
		return selectorPicTpg3;
	}

	public void setSelectorPicTpg3(SelectorInfo selectorPicTpg3) {
		this.selectorPicTpg3 = selectorPicTpg3;
	}

	public SelectorInfo getSelectorPicTpk1() {
		return selectorPicTpk1;
	}

	public void setSelectorPicTpk1(SelectorInfo selectorPicTpk1) {
		this.selectorPicTpk1 = selectorPicTpk1;
	}

	public SelectorInfo getSelectorPicTpk2() {
		return selectorPicTpk2;
	}

	public void setSelectorPicTpk2(SelectorInfo selectorPicTpk2) {
		this.selectorPicTpk2 = selectorPicTpk2;
	}

	public SelectorInfo getSelectorPicTpk3() {
		return selectorPicTpk3;
	}

	public void setSelectorPicTpk3(SelectorInfo selectorPicTpk3) {
		this.selectorPicTpk3 = selectorPicTpk3;
	}

	public InternalRegulationPenerbitan getIrg() {
		return irg;
	}

	public void setIrg(InternalRegulationPenerbitan irg) {
		this.irg = irg;
	}
	
	public InternalRegulationPenerbitanPicTpg[] getSelectedDataPictpg() {
		return selectedDataPictpg;
	}

	public void setSelectedDataPictpg(InternalRegulationPenerbitanPicTpg[] selectedDataPictpg) {
		this.selectedDataPictpg = selectedDataPictpg;
	}

	public InternalRegulationPenerbitanPicTpgTableModel<InternalRegulationPenerbitanPicTpg> getTableModelPictpg() {
		return tableModelPictpg;
	}

	public void setTableModelPictpg(
			InternalRegulationPenerbitanPicTpgTableModel<InternalRegulationPenerbitanPicTpg> tableModelPictpg) {
		this.tableModelPictpg = tableModelPictpg;
	}

	public InternalRegulationPenerbitanPicIrg[] getSelectedDataPicirg() {
		return selectedDataPicirg;
	}

	public void setSelectedDataPicirg(InternalRegulationPenerbitanPicIrg[] selectedDataPicirg) {
		this.selectedDataPicirg = selectedDataPicirg;
	}

	public InternalRegulationPenerbitanPicIrgTableModel<InternalRegulationPenerbitanPicIrg> getTableModelPicirg() {
		return tableModelPicirg;
	}

	public void setTableModelPicirg(
			InternalRegulationPenerbitanPicIrgTableModel<InternalRegulationPenerbitanPicIrg> tableModelPicirg) {
		this.tableModelPicirg = tableModelPicirg;
	}

	public InternalRegulationPenerbitanPicTpk[] getSelectedDataPictpk() {
		return selectedDataPictpk;
	}

	public void setSelectedDataPictpk(InternalRegulationPenerbitanPicTpk[] selectedDataPictpk) {
		this.selectedDataPictpk = selectedDataPictpk;
	}

	public InternalRegulationPenerbitanPicTpkTableModel<InternalRegulationPenerbitanPicTpk> getTableModelPictpk() {
		return tableModelPictpk;
	}

	public void setTableModelPictpk(
			InternalRegulationPenerbitanPicTpkTableModel<InternalRegulationPenerbitanPicTpk> tableModelPictpk) {
		this.tableModelPictpk = tableModelPictpk;
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

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
	}

	public String getFollowUpRemainderTpk() {
		return followUpRemainderTpk;
	}

	public void setFollowUpRemainderTpk(String followUpRemainderTpk) {
		this.followUpRemainderTpk = followUpRemainderTpk;
	}

	public InternalRegulationPenerbitanService getInternalRegulationPenerbitanService() {
		return internalRegulationPenerbitanService;
	}

	public void setInternalRegulationPenerbitanService(
			InternalRegulationPenerbitanService internalRegulationPenerbitanService) {
		this.internalRegulationPenerbitanService = internalRegulationPenerbitanService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Boolean getFlagNewEdit() {
		return flagNewEdit;
	}

	public void setFlagNewEdit(Boolean flagNewEdit) {
		this.flagNewEdit = flagNewEdit;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public HolidayService getHolidayService() {
		return holidayService;
	}

	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}

	public Boolean getIsValidateStep1() {
		return isValidateStep1;
	}

	public void setIsValidateStep1(Boolean isValidateStep1) {
		this.isValidateStep1 = isValidateStep1;
	}

	public Boolean getIsValidateStep2() {
		return isValidateStep2;
	}

	public void setIsValidateStep2(Boolean isValidateStep2) {
		this.isValidateStep2 = isValidateStep2;
	}

	public SelectorInfo getSelectorPicIrg2() {
		return selectorPicIrg2;
	}

	public void setSelectorPicIrg2(SelectorInfo selectorPicIrg2) {
		this.selectorPicIrg2 = selectorPicIrg2;
	}

	public List<String> getEmailGroupTpkList() {
		return emailGroupTpkList;
	}

	public void setEmailGroupTpkList(List<String> emailGroupTpkList) {
		this.emailGroupTpkList = emailGroupTpkList;
	}

	public List<UploadedFileWO> getUploadedFilesIrgAttachment() {
		return uploadedFilesIrgAttachment;
	}

	public void setUploadedFilesIrgAttachment(List<UploadedFileWO> uploadedFilesIrgAttachment) {
		this.uploadedFilesIrgAttachment = uploadedFilesIrgAttachment;
	}

	public Map<String, String> getEmailGroupTpkMap() {
		return emailGroupTpkMap;
	}

	public void setEmailGroupTpkMap(Map<String, String> emailGroupTpkMap) {
		this.emailGroupTpkMap = emailGroupTpkMap;
	}

	public User getUserEmailGroupCorpSec() {
		return userEmailGroupCorpSec;
	}

	public void setUserEmailGroupCorpSec(User userEmailGroupCorpSec) {
		this.userEmailGroupCorpSec = userEmailGroupCorpSec;
	}

	public List<InternalRegulationPenerbitanPicTpg> getDataIrgPenerbitanPicTpgDeleteList() {
		return dataIrgPenerbitanPicTpgDeleteList;
	}

	public void setDataIrgPenerbitanPicTpgDeleteList(
			List<InternalRegulationPenerbitanPicTpg> dataIrgPenerbitanPicTpgDeleteList) {
		this.dataIrgPenerbitanPicTpgDeleteList = dataIrgPenerbitanPicTpgDeleteList;
	}

	public List<InternalRegulationPenerbitanPicTpk> getDataIrgPenerbitanPicTpkDeleteList() {
		return dataIrgPenerbitanPicTpkDeleteList;
	}

	public void setDataIrgPenerbitanPicTpkDeleteList(
			List<InternalRegulationPenerbitanPicTpk> dataIrgPenerbitanPicTpkDeleteList) {
		this.dataIrgPenerbitanPicTpkDeleteList = dataIrgPenerbitanPicTpkDeleteList;
	}

	public InternalRegulationPenerbitanEmailGroup[] getSelectedDataEmailGroupTpk() {
		return selectedDataEmailGroupTpk;
	}

	public void setSelectedDataEmailGroupTpk(InternalRegulationPenerbitanEmailGroup[] selectedDataEmailGroupTpk) {
		this.selectedDataEmailGroupTpk = selectedDataEmailGroupTpk;
	}

	public InternalRegulationPenerbitanEmailGroupTableModel<InternalRegulationPenerbitanEmailGroup> getTableModelEmailGroupTpk() {
		return tableModelEmailGroupTpk;
	}

	public void setTableModelEmailGroupTpk(
			InternalRegulationPenerbitanEmailGroupTableModel<InternalRegulationPenerbitanEmailGroup> tableModelEmailGroupTpk) {
		this.tableModelEmailGroupTpk = tableModelEmailGroupTpk;
	}

	public Integer getIndexDtlFollowupEmailGroup() {
		return indexDtlFollowupEmailGroup;
	}

	public void setIndexDtlFollowupEmailGroup(Integer indexDtlFollowupEmailGroup) {
		this.indexDtlFollowupEmailGroup = indexDtlFollowupEmailGroup;
	}

	public Integer getLastSequenceOfEmailGroup() {
		return lastSequenceOfEmailGroup;
	}

	public void setLastSequenceOfEmailGroup(Integer lastSequenceOfEmailGroup) {
		this.lastSequenceOfEmailGroup = lastSequenceOfEmailGroup;
	}	
		

}