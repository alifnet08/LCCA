package com.wo.module.complianceReviewDocument.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.complianceReviewDocument.constant.ComplianceReviewDocumentConstants;
import com.wo.module.complianceReviewDocument.model.ComplianceReviewDocument;
import com.wo.module.complianceReviewDocument.model.ComplianceReviewDocumentPicCompliance;
import com.wo.module.complianceReviewDocument.service.ComplianceReviewDocumentService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;

public class ComplianceReviewDocumentBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 8207299276289238243L;
	static Logger logger = Logger.getLogger(ComplianceReviewDocumentBean.class);

	private String searchVal;
	private String documentTypeCode;
	private String proposingUnit;

	private Long deleteId;

	private String hukNo;
	private String materi;
	private Date receivedDateFrom;
	private Date receivedDateTo;
	private Date completeDateFrom;
	private Date completeDateTo;

	private List<SelectItem> documentTypes;
	private List<SelectItem> documentSubmitter;

	private ComplianceReviewDocumentService complianceReviewDocumentService;

	private DBLazyDataModel<ComplianceReviewDocument> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = ComplianceReviewDocumentConstants.NAVIGATE_EDIT;

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void selectDocumetType() {
		documentTypes = new ArrayList<SelectItem>();

		try {
			List<ParameterDetail> pd = parameterDetailService
					.getParameterDetailByParamCode(ParameterDetail.PARAM_DET_CODE_DOCUMENT_TYPE);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				documentTypes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectDocumentSubmitter() {
		documentSubmitter = new ArrayList<SelectItem>();

		try {
			List<ParameterDetail> pd = parameterDetailService
					.getParameterDetailByParamCode(ParameterDetail.PARAM_DET_CODE_DOCUMENT_SUBMITTER);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				documentSubmitter.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void postProcessXLS(Object document) {
		try {
//			Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
			
			HSSFWorkbook wb = (HSSFWorkbook) document;
			HSSFSheet sheet = wb.getSheetAt(0);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			List<ComplianceReviewDocument> listDataXls = complianceReviewDocumentService.searchDataXLS(
					Arrays.asList(
							new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_DOCUMET_TYPE, documentTypeCode),
							new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_HUK_NO, hukNo),
							new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_MATERI, materi),
							new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_RECEIVED_DATE_START, 
									receivedDateFrom != null ? sdf.format(receivedDateFrom) : ""),
							new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_RECEIVED_DATE_END,
									receivedDateTo != null ? sdf.format(receivedDateTo) : ""),
							new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_COMPLETE_DATE_START, 
									completeDateFrom != null ? sdf.format(completeDateFrom) : ""),
							new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_COMPLETE_DATE_END,
									completeDateTo != null ? sdf.format(completeDateTo) : ""),
							new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_DOCUMENT_SUBMITTER, proposingUnit)));
			
			//create header
			HSSFRow header = sheet.createRow(0);
			for (int i = 0; i < 7; i++) {
				HSSFCell cell = header.createCell((short) i);
				
				if (i == 0) {
					cell.setCellValue(facesUtil.retrieveMessage("formComplianceReviewDocumentType"));
				} else if (i == 1) {
					cell.setCellValue(facesUtil.retrieveMessage("formComplianceReviewReceivedDate"));
				} else if (i == 2) {
					cell.setCellValue(facesUtil.retrieveMessage("formComplianceReviewCompleteDate"));
				} else if (i == 3) {
					cell.setCellValue(facesUtil.retrieveMessage("formComplianceReviewHUKNo"));
				} else if (i == 4) {
					cell.setCellValue(facesUtil.retrieveMessage("formComplianceReviewMateri"));
				}  else if (i == 5) {
					cell.setCellValue(facesUtil.retrieveMessage("formComplianceReviewProposerWorkUnit"));
				} else if (i == 6) {
					cell.setCellValue(facesUtil.retrieveMessage("formComplianceReviewDocumentPic"));
				}
			}
			
			//kosongin data
			int rowNum = 1;
			for (int i = 0; i < 7; i++) {
				HSSFRow row = sheet.createRow(rowNum);
				for (int x = 0; x < 7; x++) {
					HSSFCell cell = row.createCell((short) x);
					cell.setCellValue("");
				}
			}
			
			rowNum = 1;
			
			for (int i = 0; i < listDataXls.size(); i++) {
				ComplianceReviewDocument crd = (ComplianceReviewDocument) listDataXls.get(i);
				HSSFRow row = sheet.createRow(rowNum);
				StringBuilder picName = new StringBuilder();
				
				for (int j = 0; j < crd.getComplianceReviewDocumentPicCompliance().size(); j++) {
					ComplianceReviewDocumentPicCompliance picCompliance = crd.getComplianceReviewDocumentPicCompliance().get(j);
					picName.append(picCompliance.getUser().getName());
					if(j != crd.getComplianceReviewDocumentPicCompliance().size()-1)
						picName.append(", ");
				}
				
				for (int x = 0; x < 7; x++) {
					HSSFCell cell = row.createCell((short) x);
					if(x == 0) {
						cell.setCellValue(crd.getDocumentTypeName() != null ? crd.getDocumentTypeName() : "");
					} else if (x == 1) {
						cell.setCellValue(crd.getReceivedDateStr() != null ? crd.getReceivedDateStr() : "");
					} else if (x == 2) {
						cell.setCellValue(crd.getCompleteDateStr() != null ? crd.getCompleteDateStr() : "");
					} else if (x == 3) {
						cell.setCellValue(crd.getDocumentNo() != null ? crd.getDocumentNo() : "");
					} else if (x == 4) {
						cell.setCellValue(crd.getMateri() != null ? crd.getMateri() : "");
					} else if (x == 5) {
						cell.setCellValue(crd.getDivisionName() != null ? crd.getDivisionName() : "");
					} else if (x == 6) {
						cell.setCellValue(picName.toString() != null ? 
								picName.toString() : "");
					}
				}
				rowNum++;
			}
			
			// style
			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
				HSSFCell cell = header.getCell(i);
				cell.setCellStyle(cellStyle);
				sheet.autoSizeColumn(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@PostConstruct
	public void init() {
		super.init();
		selectDocumetType();
		selectDocumentSubmitter();
		tableModel = new DBLazyDataModel<ComplianceReviewDocument>(complianceReviewDocumentService, paging);
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent e) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		
		if(documentTypeCode != null && !documentTypeCode.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_DOCUMET_TYPE, documentTypeCode));
		}
		
		if(hukNo != null && !hukNo.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_HUK_NO, hukNo));
		}
		
		if(materi != null && !materi.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_MATERI, materi));
		}
		
		if(receivedDateFrom != null) {
			searchCriteria.add(new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_RECEIVED_DATE_START, 
					receivedDateFrom != null ? sdf.format(receivedDateFrom) : ""));
		}
		
		if(receivedDateTo != null) {
			searchCriteria.add(new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_RECEIVED_DATE_END, 
					receivedDateTo != null ? sdf.format(receivedDateTo) : ""));
		}
		
		if (completeDateFrom != null) {
			searchCriteria.add(new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_COMPLETE_DATE_START, 
					completeDateFrom != null ? sdf.format(completeDateFrom) : ""));
		}
		
		if(completeDateTo != null) {
			searchCriteria.add(new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_COMPLETE_DATE_START, 
					completeDateTo != null ? sdf.format(completeDateTo) : ""));
		}
		
		if(proposingUnit != null && !documentSubmitter.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_DOCUMENT_SUBMITTER, proposingUnit));
		}
		
		tableModel.setSearchCriteria(searchCriteria);
	}
	
	public void reset(ActionEvent e) {
		documentTypeCode = "";
		hukNo = "";
		materi = "";
		receivedDateFrom = null;
		receivedDateTo = null;
		completeDateFrom = null;
		completeDateTo = null;
		proposingUnit = "";
		search(e);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void delete(Long deleteId) {
		try {
			ComplianceReviewDocument cd = complianceReviewDocumentService.findById(deleteId);
			cd.setEnabledFlag(Constants.CONSTANT_NO);
			cd.setLastUpdateBy(facesUtil.retrieveUserLogin());
			cd.setLastUpdateDate(new Timestamp(new Date().getTime()));
			complianceReviewDocumentService.update(cd);
			facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}
	
	public void downloadFile (String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public Boolean getIsLogin() {
		if(facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE) != null
				|| facesUtil.getSessionAttribute(Constants.SESSION_KANDIDAT) != null) {
			return true;
		}
		return false;
	}

	public String getSearchVal() {
		return searchVal;
	}

	public void setSearchVal(String searchVal) {
		this.searchVal = searchVal;
	}

	public String getDocumentTypeCode() {
		return documentTypeCode;
	}

	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}

	public Long getDeleteId() {
		return deleteId;
	}

	public void setDeleteId(Long deleteId) {
		this.deleteId = deleteId;
	}

	public String getHukNo() {
		return hukNo;
	}

	public void setHukNo(String hukNo) {
		this.hukNo = hukNo;
	}


	public String getMateri() {
		return materi;
	}

	public void setMateri(String materi) {
		this.materi = materi;
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

	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	public Date getReceivedDateFrom() {
		return receivedDateFrom;
	}

	public void setReceivedDateFrom(Date receivedDateFrom) {
		this.receivedDateFrom = receivedDateFrom;
	}

	public Date getReceivedDateTo() {
		return receivedDateTo;
	}

	public void setReceivedDateTo(Date receivedDateTo) {
		this.receivedDateTo = receivedDateTo;
	}

	public Date getCompleteDateFrom() {
		return completeDateFrom;
	}

	public void setCompleteDateFrom(Date completeDateFrom) {
		this.completeDateFrom = completeDateFrom;
	}

	public Date getCompleteDateTo() {
		return completeDateTo;
	}

	public void setCompleteDateTo(Date completeDateTo) {
		this.completeDateTo = completeDateTo;
	}

	public List<SelectItem> getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(List<SelectItem> documentTypes) {
		this.documentTypes = documentTypes;
	}

	public List<SelectItem> getDocumentSubmitter() {
		return documentSubmitter;
	}

	public void setDocumentSubmitter(List<SelectItem> documentSubmitter) {
		this.documentSubmitter = documentSubmitter;
	}

	public DBLazyDataModel<ComplianceReviewDocument> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<ComplianceReviewDocument> tableModel) {
		this.tableModel = tableModel;
	}

	public String getProposingUnit() {
		return proposingUnit;
	}

	public void setProposingUnit(String proposingUnit) {
		this.proposingUnit = proposingUnit;
	}

	
}
