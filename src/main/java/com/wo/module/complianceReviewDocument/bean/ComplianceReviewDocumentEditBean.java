package com.wo.module.complianceReviewDocument.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.util.MathUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.complianceReviewDocument.constant.ComplianceReviewDocumentConstants;
import com.wo.module.complianceReviewDocument.model.ComplianceReviewDocument;
import com.wo.module.complianceReviewDocument.model.ComplianceReviewDocumentAttachment;
import com.wo.module.complianceReviewDocument.model.ComplianceReviewDocumentPicCompliance;
import com.wo.module.complianceReviewDocument.model.ComplianceReviewDocumentPicComplianceTableModel;
import com.wo.module.complianceReviewDocument.service.ComplianceReviewDocumentService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class ComplianceReviewDocumentEditBean extends CommonBean implements SelectorListener<Object>, Serializable {

	private static final long serialVersionUID = 1127547394419333935L;
	static Logger logger = Logger.getLogger(ComplianceReviewDocumentEditBean.class);
	
	private ComplianceReviewDocument complianceReviewDocument;
	
	private Boolean isViewOnly;
	
	private String actionMode;
	
	private String editedId;
	
	private List<SelectItem> documentTypes;
	private List<SelectItem> documentSubmitters;
	private List<SelectItem> divisions;
	//Temp Variable
	private List<Division> selectedDivisionObj;
	private List<Long> divisionIds;
	
	private List<UploadedFileWO> uploadFilesDocumentMemo;
	private List<UploadedFileWO> deleteFiles;
	
	private SelectorInfo selectorCompliance;
	
	private ComplianceReviewDocumentPicCompliance[] selectedDataCompliance;
	
	private ComplianceReviewDocumentPicComplianceTableModel<ComplianceReviewDocumentPicCompliance> tableModelCompliance;
	
	private Integer lastSequenceOfCompliance;
	private Integer indexDtlCompliance;
	
	private ComplianceReviewDocumentService complianceReviewDocumentService;
	private UserService userService;
	
	private FacesUtil facesUtil;
	private FileUtil fileUtil;
	
	private String navigateSearch = ComplianceReviewDocumentConstants.NAVIGATE_SEARCH;
	private String textWarningUpload;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

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
		selectDocumentType();
		selectDocumentSubmitter();
		//selectDivision();
		
		selectorCompliance = ComplianceReviewDocumentConstants.buildSelectorPICCompliance(facesUtil);
		
		checkNewOrEdit();
		
		fileUtil = FileUtil.getInstance();
		
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void selectDocumentType() {
		documentTypes = new ArrayList<SelectItem>();

		try {
			List<ParameterDetail> pd = parameterDetailService
					.getParameterDetailByParamCode(ParameterDetail.PARAM_DET_CODE_DOCUMENT_TYPE);
			for (ParameterDetail vo: pd) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				documentTypes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void selectDocumentSubmitter() {
		documentSubmitters = new ArrayList<SelectItem>();

		try {
			List<ParameterDetail> pd = parameterDetailService
					.getParameterDetailByParamCode(ParameterDetail.PARAM_DET_CODE_DOCUMENT_SUBMITTER);
			for (ParameterDetail vo : pd) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				documentSubmitters.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	//old select list for SelectOneItem - unused
//	private void selectDivision() {
//		divisions = new ArrayList<SelectItem>();
//		
//		try {
//			List<Division> pd = userService.getAllDivision();
//			for (Division vo : pd) {
//				SelectItem si = new SelectItem();
//				si.setLabel(vo.getDivisionName());
//				si.setValue(vo);
//				divisions.add(si);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		PrimeFaces.current().executeScript("reInitSelect2();");
//	}
	
	public List<Division> completeDivision(String query){
		String queryLowerCase = query.toLowerCase();
		List<Division> lists = userService.getAllDivision();
		return lists.stream().filter(t -> t.getDivisionName().toLowerCase().contains(
				queryLowerCase)).collect(Collectors.toList());
	}	
	
	//debugging ajax start - delete or comment latet
//	public void onItemAdd(SelectEvent event) {
//		System.out.println("Ajax select activated");
//		
//		Division addedDivision = (Division) event.getObject();
//		
//		System.out.println("added division name: "+addedDivision.getDivisionName());
//		System.out.println("added division id  : "+addedDivision.getDivisionId());
//		
//		StringBuilder sb = new StringBuilder();
//		for(Division div : selectedDivisionObj) {
//			sb.append(div.getDivisionId().toString()+";");
//		}
//		System.out.println("cRD obj div multi id: "+sb.toString());
//		System.out.println("Ajax select finished");
//	}
//	
//	public void onItemRemove(UnselectEvent event) {
//		System.out.println("Ajax unselect activated");
//		
//		Division removedDivision = (Division) event.getObject();
//		
//		System.out.println("removed division name: "+removedDivision.getDivisionName());
//		System.out.println("removed division id  : "+removedDivision.getDivisionId());
//		
//		StringBuilder sb = new StringBuilder();
//		for(Division div : selectedDivisionObj) {
//			sb.append(div.getDivisionId().toString()+";");
//		}
//		System.out.println("cRD obj div multi id: "+sb.toString());
//		System.out.println("Ajax unselect finished");
//	}
	//debugging ajax end - delete or comment latet
	
	private void checkNewOrEdit() {
		String editId = facesUtil.retrieveRequestParam("id");
		String token = facesUtil.retrieveRequestParam("token");
		String viewId = facesUtil.retrieveRequestParam("viewId");
		
		isViewOnly = false;
		if (viewId != null && !viewId.isEmpty()) {
			if(viewId.trim().equalsIgnoreCase("true")) {
				isViewOnly = true;
			}
		}
		
		if (StringUtils.isBlank(editId) && StringUtils.isBlank(token)) {
			this.handleNew();
		} else {
			this.handleEdit(editId);
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	private void handleNew() {
		try {
			complianceReviewDocument = new ComplianceReviewDocument();
			complianceReviewDocument.setDocumentType(new ParameterDetail());
			complianceReviewDocument.setDocumentSubmitter(new ParameterDetail());
	
			lastSequenceOfCompliance = 0;
			
			uploadFilesDocumentMemo = new ArrayList<UploadedFileWO>();
			
			actionMode = Constants.ACTION_ADD;
			
			tableModelCompliance = new ComplianceReviewDocumentPicComplianceTableModel<ComplianceReviewDocumentPicCompliance>(
					complianceReviewDocument.getComplianceReviewDocumentPicCompliance());
			
			if (complianceReviewDocument.getComplianceReviewDocumentPicCompliance() == null
					|| complianceReviewDocument.getComplianceReviewDocumentPicCompliance().size() == 0) {
				complianceReviewDocument.setComplianceReviewDocumentPicCompliance(new ArrayList<ComplianceReviewDocumentPicCompliance>());
				lastSequenceOfCompliance = 0;
			}
			
			ComplianceReviewDocumentPicCompliance cp = new ComplianceReviewDocumentPicCompliance();
			lastSequenceOfCompliance = lastSequenceOfCompliance + 1;
			cp.setSequence(lastSequenceOfCompliance);
			
			User user = (User) facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE);
			cp.setUser(user);
			complianceReviewDocument.getComplianceReviewDocumentPicCompliance().add(cp);
			tableModelCompliance.setWrappedData(complianceReviewDocument.getComplianceReviewDocumentPicCompliance());
			facesUtil.setSessionAttribute("token", null);
			
			selectedDivisionObj = new ArrayList<Division>();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long idLong = Long.parseLong(editId);
		complianceReviewDocument = complianceReviewDocumentService.findById(idLong);
		
		if (complianceReviewDocument.getDocumentSubmitter() == null) {
			complianceReviewDocument.setDocumentSubmitter(new ParameterDetail());
		}
		
		lastSequenceOfCompliance = 0;
		
		if(complianceReviewDocument.getComplianceReviewDocumentPicCompliance() != null) {
			lastSequenceOfCompliance = complianceReviewDocument.getComplianceReviewDocumentPicCompliance().size();
			for (int i = 0; i < complianceReviewDocument.getComplianceReviewDocumentPicCompliance().size(); i++) {
				ComplianceReviewDocumentPicCompliance dtl = (ComplianceReviewDocumentPicCompliance) complianceReviewDocument.getComplianceReviewDocumentPicCompliance().get(i);
				
				lastSequenceOfCompliance = lastSequenceOfCompliance + 1;
				dtl.setSequence(lastSequenceOfCompliance);
				
				if(dtl.getUser() != null) {
					dtl.setUserNIKTemp(dtl.getUser().getNik());
					dtl.setUserNameTemp(dtl.getUser().getName());
					dtl.setUserEmailTemp(dtl.getUser().getEmail());
				}
			}
		}
		
		uploadFilesDocumentMemo = new ArrayList<UploadedFileWO>();
		
		for (int i = 0; i < complianceReviewDocument.getComplianceReviewDocumentAttachments().size(); i++) {
			ComplianceReviewDocumentAttachment ra = complianceReviewDocument.getComplianceReviewDocumentAttachments().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			uploadFilesDocumentMemo.add(uf);
		}
		
		tableModelCompliance = new ComplianceReviewDocumentPicComplianceTableModel<ComplianceReviewDocumentPicCompliance>(
				complianceReviewDocument.getComplianceReviewDocumentPicCompliance());
		
		//adding objects to selectedDivisionObj and show on autoComplete field start
		selectedDivisionObj = new ArrayList<Division>();
		
		String[] arrayDivIds = complianceReviewDocument.getDivisionMultiId().split(";");
		for(int i=0; i<arrayDivIds.length; i++) {
			if(!arrayDivIds[i].equals("")) {
				Long divId = Long.parseLong(arrayDivIds[i]);
				Division divisionObj = userService.findUsedDivisionByDivisionId(divId);
				
				selectedDivisionObj.add(divisionObj);
			}else {
				break;
			}
		}
		//adding objects to selectedDivisionObj and show on autoComplete field end
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public Boolean validate() {
		Boolean flag = false;
		
		try {
			
			if (actionMode.equals(Constants.ACTION_ADD)) {
				Integer validateSameValue = complianceReviewDocumentService.getComplianceDocumentByTypeAndNo(
						complianceReviewDocument.getDocumentType().getParameterDtlCode(),
						complianceReviewDocument.getDocumentNo());
				
				if (validateSameValue > 0) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formComplianceReviewDocumentType") + " & "
							+ facesUtil.retrieveMessage("formComplianceReviewHUKNo") + " "
							+ facesUtil.retrieveMessage("errorAlreadyExists") );
					flag = true;
				}
			} else if (actionMode.equals(Constants.ACTION_EDIT)) {
				Integer validateSameValue = complianceReviewDocumentService.getComplianceDocumentByIdTypeAndNo(
						complianceReviewDocument.getComplianceReviewDocumentId(),
						complianceReviewDocument.getDocumentType().getParameterDtlCode(),
						complianceReviewDocument.getDocumentNo());
				
				if (validateSameValue > 0) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formComplianceReviewDocumentType") + " & "
							+ facesUtil.retrieveMessage("formComplianceReviewHUKNo") + " "
							+ facesUtil.retrieveMessage("errorAlreadyExists") );
					flag = true;
				}
			}
			
			if(StringUtils.isEmpty(complianceReviewDocument.getDocumentType().getParameterDtlCode())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formComplianceReviewDocumentType") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			//validate if Number is empty - unused
			if(StringUtils.isEmpty(complianceReviewDocument.getDocumentNo())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formComplianceReviewHUKNo") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
			if(complianceReviewDocument.getReceivedDate() == null) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formComplianceReviewReceivedDate") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
			if(complianceReviewDocument.getCompleteDate() == null) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formComplianceReviewCompleteDate") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			//validate if Unit Pengusul is empty
			if(selectedDivisionObj.get(0) == null) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formComplianceReviewProposerWorkUnit") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			//validate if Unit Pengusul have duplicates
			Set<Division> dupeCheck = new HashSet<>();
			for(Division div : selectedDivisionObj) {
				if(div.getDivisionId() != null) {
					if(!dupeCheck.add(div)) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formComplianceReviewProposerWorkUnit") + " "
								+ facesUtil.retrieveMessage("errorDuplicate"));
						flag = true;
						break;
					}
				}
			}
			
			/*if (complianceReviewDocument.getDocumentType().getParameterDtlCode().equals("DOC_TYPE_HUK")) {
				if(StringUtils.isEmpty(complianceReviewDocument.getDocumentSubmitter().getParameterDtlCode())) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formComplianceReviewDocumentSubmitter") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				}
			}*/
			
			//old validate for divisionId - unused
//			if(complianceReviewDocument.getDivisionId() == null) {
//				facesUtil.addErrMessage(facesUtil.retrieveMessage("formComplianceReviewProposerWorkUnit") + " "
//						+ facesUtil.retrieveMessage("validateRequired"));
//				flag = true;
//			}
			//old validate for divisionId - unused
			
			Set<Long> userCompliancetemp = new HashSet<Long>();
			for (int i = 0; i < complianceReviewDocument.getComplianceReviewDocumentPicCompliance().size(); i++) {
				ComplianceReviewDocumentPicCompliance dtl = (ComplianceReviewDocumentPicCompliance) complianceReviewDocument.getComplianceReviewDocumentPicCompliance().get(i);
				//System.out.println("PIC Compliance User Id: " + dtl.getUser().getUserId());
				try {
					if(dtl.getUser().getUserId() != null) {
						if(!userCompliancetemp.add(dtl.getUser().getUserId())) {
							facesUtil.addErrMessage(facesUtil.retrieveMessage("formComplianceReviewDocumentNIK") + " "
									+ facesUtil.retrieveMessage("errorDuplicate"));
							flag = true;
							break;
						}
					}
				}catch(NullPointerException e) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formComplianceReviewPicCompliance") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
					break;
				}
			}
			
			if (uploadFilesDocumentMemo == null || uploadFilesDocumentMemo.size() <= 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formComplianceReviewDocumentLetterTitle") + " File "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public void save() {
		try {
			if(!validate()) {
				//temp placement for unused column start
				complianceReviewDocument.setDivisionId(
						selectedDivisionObj.get(0).getDivisionId());
				complianceReviewDocument.setDivisionName(
						selectedDivisionObj.get(0).getDivisionName());
				//temp placement for unused column end
				
				//multi div save/update start
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < selectedDivisionObj.size(); i++) {
					Division div = selectedDivisionObj.get(i);
					sb.append(div.getDivisionId()+";");
				}
				
				complianceReviewDocument.setDivisionMultiId(sb.toString());
				
				//multi div save/update end
				if(complianceReviewDocument.getComplianceReviewDocumentAttachments() == null
						|| complianceReviewDocument.getComplianceReviewDocumentAttachments().size() == 0) {
					complianceReviewDocument.setComplianceReviewDocumentAttachments(new ArrayList<ComplianceReviewDocumentAttachment>());
				}
				complianceReviewDocument.getComplianceReviewDocumentAttachments().clear();
				
				if(uploadFilesDocumentMemo != null) {
					for (int i = 0; i < uploadFilesDocumentMemo.size(); i++) {
						ComplianceReviewDocumentAttachment doc = new ComplianceReviewDocumentAttachment();
						UploadedFileWO uf = (UploadedFileWO) uploadFilesDocumentMemo.get(i);
						doc.setComplianceReviewDocument(complianceReviewDocument);
						
						doc.setAttachmentFile(uf.getFileName());
						doc.setCreatedBy(facesUtil.retrieveUserLogin());
						doc.setCreationDate(new Timestamp(new Date().getTime()));
						doc.setDelId(new Long(0));
						doc.setEnabledFlag(Constants.CONSTANT_YES);
						
						doc.setFileId(uf.getFileId());
						doc.setFileSize(uf.getFileSize());
						complianceReviewDocument.getComplianceReviewDocumentAttachments().add(doc);
					}
				}
				
				if(complianceReviewDocument.getComplianceReviewDocumentPicCompliance() != null) {
					for (int i = 0; i < complianceReviewDocument.getComplianceReviewDocumentPicCompliance().size(); i++) {
						ComplianceReviewDocumentPicCompliance dtl = (ComplianceReviewDocumentPicCompliance) complianceReviewDocument.getComplianceReviewDocumentPicCompliance().get(i);
						dtl.setComplianceReviewDocument(complianceReviewDocument);
						
						if(dtl.getCreatedBy() == null) {
							dtl.setCreatedBy(facesUtil.retrieveUserLogin());
							dtl.setCreationDate(new Timestamp(new Date().getTime()));
						}
						
						dtl.setLastUpdateBy(facesUtil.retrieveUserLogin());
						dtl.setLastUpdateDate(new Timestamp(new Date().getTime()));
						dtl.setDelId(new Long(0));
						dtl.setEnabledFlag(Constants.CONSTANT_YES);
					}
				}
				
				ParameterDetail paramDocumentType = parameterDetailService.getParameterDetailByParamDtlCode(complianceReviewDocument.getDocumentType().getParameterDtlCode());
				ParameterDetail paramDocumentSubmitter = parameterDetailService.getParameterDetailByParamDtlCode(complianceReviewDocument.getDocumentSubmitter().getParameterDtlCode());
				complianceReviewDocument.setDocumentType(paramDocumentType);
				complianceReviewDocument.setDocumentSubmitter(paramDocumentSubmitter);
				
				if(complianceReviewDocument.getComplianceReviewDocumentId() != null) {
					complianceReviewDocument.setLastUpdateBy(facesUtil.retrieveUserLogin());
					complianceReviewDocument.setLastUpdateDate(new Timestamp(new Date().getTime()));
					complianceReviewDocument.setDelId(new Long(0));
					complianceReviewDocument.setEnabledFlag(Constants.CONSTANT_YES);
					complianceReviewDocumentService.update(complianceReviewDocument);		
				} else {
					//add autogenerated Number start - UNUSED DUE TO MISCOMM
//					int year = Year.now().getValue();
//					int countDataYear = complianceReviewDocumentService.countRowByCreationYear(year);
//					
//					String docNum = String.format("%03d", countDataYear+1);
//					String thisYear = Integer.toString(year);
//					StringBuilder cRDNo = new StringBuilder();
//					cRDNo.append(docNum+"/REVIEW/"+thisYear);
//					
//					complianceReviewDocument.setDocumentNo(cRDNo.toString());
					//add autogenerated Number end - UNUSED DUE TO MISCOMM
					
					//trim any space on Number - start
					
					complianceReviewDocument.setDocumentNo(complianceReviewDocument.getDocumentNo().trim()); 
					
					//trim any space on Number - end
					
					complianceReviewDocument.setCreatedBy(facesUtil.retrieveUserLogin());
					complianceReviewDocument.setCreationDate(new Timestamp(new Date().getTime()));
					complianceReviewDocument.setDelId(new Long(0));
					complianceReviewDocument.setEnabledFlag(Constants.CONSTANT_YES);
					complianceReviewDocumentService.save(complianceReviewDocument);			
				}
				
				if (deleteFiles != null) {
					for (int i = 0; i < deleteFiles.size(); i++) {
						UploadedFileWO uf = (UploadedFileWO) deleteFiles.get(i);
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}
				
				facesUtil.redirect("/pages/complianceReviewDocument/complianceReviewDocument.faces");
			}
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null,"Operation Failed : " + e.getMessage() , "");
		}
	}
	
	public void setCssLabel() {
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onAddNewRowPicCompliance() {
		if (complianceReviewDocument.getComplianceReviewDocumentPicCompliance() == null ) {
				complianceReviewDocument.setComplianceReviewDocumentPicCompliance(new ArrayList<ComplianceReviewDocumentPicCompliance>());
				lastSequenceOfCompliance = 0;
		} else {
			if (complianceReviewDocument.getComplianceReviewDocumentPicCompliance().size() == 0) {
				lastSequenceOfCompliance = 0;
			}
		}
		
		ComplianceReviewDocumentPicCompliance cp = new ComplianceReviewDocumentPicCompliance();
		lastSequenceOfCompliance = lastSequenceOfCompliance + 1;
		cp.setSequence(lastSequenceOfCompliance);
		complianceReviewDocument.getComplianceReviewDocumentPicCompliance().add(cp);
		tableModelCompliance.setWrappedData(complianceReviewDocument.getComplianceReviewDocumentPicCompliance());
	}
	
	public void onDeleteRowPicCompliance() {
		for (int i = 0; i < selectedDataCompliance.length; i++) {
			complianceReviewDocument.getComplianceReviewDocumentPicCompliance().remove(selectedDataCompliance[i]);
		}
		
		if(complianceReviewDocument.getComplianceReviewDocumentPicCompliance() == null
				|| complianceReviewDocument.getComplianceReviewDocumentPicCompliance().size() == 0) {
			lastSequenceOfCompliance = 0;
		}
		
		tableModelCompliance.setWrappedData(complianceReviewDocument.getComplianceReviewDocumentPicCompliance());
	}
	
	public void handleFileUploadDocumentMemo (FileUploadEvent event) {
		try {
			uploadFilesDocumentMemo = uploadFilesDocumentMemo == null ? new ArrayList<UploadedFileWO>() : uploadFilesDocumentMemo;
			uploadFilesDocumentMemo.add(new UploadedFileWO( CallApiManager.callUploadAPI(event.getFile()
							, Constants.COMPLIANCE_REVIEW_DOCUMENT, 
							parameterDetailService, false, fileUtil),
					event.getFile().getFileName(),event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void deleteAttachment (String fileId, int index, String uploadType) throws Exception {
		deleteFiles = deleteFiles != null ? deleteFiles : new ArrayList<UploadedFileWO>();
		deleteFiles.add(new UploadedFileWO(fileId,null,null,null));
		
		if(uploadType != null && uploadType.equals(ComplianceReviewDocumentConstants.UPLOAD_TYPE_DOCUMENT)) {
			uploadFilesDocumentMemo.remove(uploadFilesDocumentMemo.get(index));
		}
	}
	
	public void downloadFile (String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public void cancel () {
		try {
			if(uploadFilesDocumentMemo != null) {
				for (int i = 0; i < uploadFilesDocumentMemo.size(); i++) {
					UploadedFileWO uf = (UploadedFileWO) uploadFilesDocumentMemo.get(i);
					if(uf.getIsNew() == null) {
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}
			}
			facesUtil.redirect("/pages/complianceReviewDocument/complianceReviewDocument.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateKelompokHUK() {
		if (!complianceReviewDocument.getDocumentType().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_DOC_TYPE_HUK)) {
			if(actionMode.equals(Constants.ACTION_EDIT)) {
				complianceReviewDocument.getDocumentSubmitter().setParameterDtlCode(null);
				setCssLabel();
			}
		} else {
			setCssLabel();
		}
	}
	
	//temp variable getset
	public List<Long> getDivisionIds() {
		return divisionIds;
	}

	public void setDivisionIds(List<Long> divisionIds) {
		this.divisionIds = divisionIds;
	}
	
	public List<Division> getSelectedDivisionObj() {
		return selectedDivisionObj;
	}

	public void setSelectedDivisionObj(List<Division> selectedDivisionObj) {
		this.selectedDivisionObj = selectedDivisionObj;
	}
	//temp variable getset end

	public ComplianceReviewDocument getComplianceReviewDocument() {
		return complianceReviewDocument;
	}

	public void setComplianceReviewDocument(ComplianceReviewDocument complianceReviewDocument) {
		this.complianceReviewDocument = complianceReviewDocument;
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

	public List<SelectItem> getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(List<SelectItem> documentTypes) {
		this.documentTypes = documentTypes;
	}

	public List<SelectItem> getDocumentSubmitters() {
		return documentSubmitters;
	}

	public void setDocumentSubmitters(List<SelectItem> documentSubmitters) {
		this.documentSubmitters = documentSubmitters;
	}

	public ComplianceReviewDocumentService getComplianceReviewDocumentService() {
		return complianceReviewDocumentService;
	}

	public void setComplianceReviewDocumentService(ComplianceReviewDocumentService complianceReviewDocumentService) {
		this.complianceReviewDocumentService = complianceReviewDocumentService;
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

	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		if (StringUtils.equals("picComplianceDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			//User user = userService.findById(((java.math.BigInteger) objects[0]).longValue());
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			complianceReviewDocument.getComplianceReviewDocumentPicCompliance().get(indexDtlCompliance).setUser(user);

			tableModelCompliance.setWrappedData(complianceReviewDocument.getComplianceReviewDocumentPicCompliance());
		}
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<SelectItem> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<SelectItem> divisions) {
		this.divisions = divisions;
	}

	public SelectorInfo getSelectorCompliance() {
		return selectorCompliance;
	}

	public void setSelectorCompliance(SelectorInfo selectorCompliance) {
		this.selectorCompliance = selectorCompliance;
	}

	public ComplianceReviewDocumentPicCompliance[] getSelectedDataCompliance() {
		return selectedDataCompliance;
	}

	public void setSelectedDataCompliance(ComplianceReviewDocumentPicCompliance[] selectedDataCompliance) {
		this.selectedDataCompliance = selectedDataCompliance;
	}

	public ComplianceReviewDocumentPicComplianceTableModel<ComplianceReviewDocumentPicCompliance> getTableModelCompliance() {
		return tableModelCompliance;
	}

	public void setTableModelCompliance(
			ComplianceReviewDocumentPicComplianceTableModel<ComplianceReviewDocumentPicCompliance> tableModelCompliance) {
		this.tableModelCompliance = tableModelCompliance;
	}

	public Integer getLastSequenceOfCompliance() {
		return lastSequenceOfCompliance;
	}

	public void setLastSequenceOfCompliance(Integer lastSequenceOfCompliance) {
		this.lastSequenceOfCompliance = lastSequenceOfCompliance;
	}

	public Integer getIndexDtlCompliance() {
		return indexDtlCompliance;
	}

	public void setIndexDtlCompliance(Integer indexDtlCompliance) {
		this.indexDtlCompliance = indexDtlCompliance;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public List<UploadedFileWO> getUploadFilesDocumentMemo() {
		return uploadFilesDocumentMemo;
	}

	public void setUploadFilesDocumentMemo(List<UploadedFileWO> uploadFilesDocumentMemo) {
		this.uploadFilesDocumentMemo = uploadFilesDocumentMemo;
	}

	public List<UploadedFileWO> getDeleteFiles() {
		return deleteFiles;
	}

	public void setDeleteFiles(List<UploadedFileWO> deleteFiles) {
		this.deleteFiles = deleteFiles;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}
	
}