package com.wo.module.auditMockup.bean;

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

import org.apache.commons.lang3.StringUtils;
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
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.mstAudit.model.MstAudit;
import com.wo.module.mstAudit.service.MstAuditService;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.tmpAudit.constant.AuditConstant;
import com.wo.module.tmpAudit.model.TmpAudit;
import com.wo.module.tmpAudit.service.TmpAuditService;
import com.wo.module.tmpAudit.vo.AuditConfirmationVO;
import com.wo.module.tmpAudit.vo.TmpAuditVO;
import com.wo.module.user.model.Division;
import com.wo.module.user.service.UserService;

public class TmpAuditBean extends CommonBean implements Serializable, AuditConstant {

	private static final long serialVersionUID = 538007327610852014L;

	static Logger logger = Logger.getLogger(TmpAuditBean.class);

	private String regulationSocializationTmpSearch;

	private String searchVal;

	private String searchAuditFollowUp;
	private String searchAuditObject;
	private String searchStatusCode;
	private Date searchAuditPeriodFrom;
	private Date searchAuditPeriodTo;
	private Date searchAuditTargetFrom;
	private Date searchAuditTargetTo;
	private String searchDivision;
	private String searchFollowupStatus;
	private String searchComplianceStatus;
	private String searchAuditFindingsName;
	private Long searchAuditTemplateName;
	
	private List<SelectItem> selectAuditFollowUp;
	private List<SelectItem> selectAuditObject;
	private List<SelectItem> status;
	private List<SelectItem> selectDivisions;
	private List<SelectItem> selectFollowupStatus;
	private List<SelectItem> selectComplianceStatus;
	private List<SelectItem> selectAuditTemplateName;

	private TmpAuditService tmpAuditService;
	private UserService userService;
	private MstAuditService mstAuditService;

	private DBLazyDataModel<TmpAuditVO> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = TMP_AUDIT_EDIT;

	@PostConstruct
	public void init() {
		super.init();
		constructSelectComponent();
		tableModel = new DBLazyDataModel<TmpAuditVO>(tmpAuditService, paging);
	}

	private void constructSelectComponent() {
		setupAuditFollowUp();
		setupAuditObject();
		setupStatus();
		setupAuditPicFollowupDivision();
		setupFollowupStatus();
		setupComplianceStatus();
		setupAuditTemplateName();
	}

	private void setupAuditFollowUp() {
		selectAuditFollowUp = new ArrayList<SelectItem>();
		try {
			selectAuditFollowUp = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_AUDITOR,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setupAuditObject() {
		selectAuditObject = new ArrayList<SelectItem>();
		try {
			selectAuditObject = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_AUDIT_OBJECT,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setupStatus() {
		status = new ArrayList<SelectItem>();
		try {
			status = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_DATA_STATUS,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setupAuditPicFollowupDivision() {
		selectDivisions = new ArrayList<SelectItem>();
		try {
			List<Division> pd = userService.getAllDivision();
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((Division) pd.get(i)).getDivisionName());
				si.setValue(((Division) pd.get(i)).getDivisionId());
				selectDivisions.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public void setupFollowupStatus() {
		selectFollowupStatus = new ArrayList<SelectItem>();
		try {
			selectFollowupStatus = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_PIC_FOLLOWUP_STATUS,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setupComplianceStatus() {
		selectComplianceStatus = new ArrayList<SelectItem>();
		try {
			selectComplianceStatus = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_COMPLIANCE_CHECK_STATUS,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setupAuditTemplateName() {
		selectAuditTemplateName = new ArrayList<SelectItem>();
		try {
			List<MstAudit> listMstAudit = mstAuditService.getAllMstAuditData();
			for (int i = 0; i < listMstAudit.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel((listMstAudit).get(i).getAuditTemplate());
				si.setValue((listMstAudit).get(i).getMstAuditId());
				selectAuditTemplateName.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void search(ActionEvent actionEvent) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		tableModel.setSearchCriteria(Arrays.asList(
				new DefaultSearchObject(WHERE_AUDIT_FOLLOWUP, searchAuditFollowUp),
				new DefaultSearchObject(WHERE_AUDIT_OBJECT, searchAuditObject),
				new DefaultSearchObject(WHERE_AUDIT_STATUS, searchStatusCode),
				new DefaultSearchObject(WHERE_AUDIT_PERIOD_FROM, searchAuditPeriodFrom != null ? sdf.format(searchAuditPeriodFrom) : ""),
				new DefaultSearchObject(WHERE_AUDIT_PERIOD_TO, searchAuditPeriodTo != null ? sdf.format(searchAuditPeriodTo) : ""),
				new DefaultSearchObject(WHERE_AUDIT_TARGET_FROM, searchAuditTargetFrom != null ? sdf.format(searchAuditTargetFrom) : ""),
				new DefaultSearchObject(WHERE_AUDIT_TARGET_TO, searchAuditTargetTo != null ? sdf.format(searchAuditTargetTo) : ""),
				new DefaultSearchObject(WHERE_AUDIT_DIVISION_ID, searchDivision),
				new DefaultSearchObject(WHERE_AUDIT_FOLLOWUP_STATUS, searchFollowupStatus),
				new DefaultSearchObject(WHERE_AUDIT_COMPLIANCE_STATUS, searchComplianceStatus),
				new DefaultSearchObject(WHERE_AUDIT_FINDINGS_NAME, searchAuditFindingsName),
				new DefaultSearchObject(WHERE_AUDIT_TEMPLATE_NAME, searchAuditTemplateName)
				));
	}

	public void reset(ActionEvent actionEvent) {
		searchAuditFollowUp = null;
		searchAuditObject = null;
		searchStatusCode = null;
		searchAuditPeriodFrom = null;
		searchAuditPeriodTo= null;
		searchAuditTargetFrom= null;
		searchAuditTargetTo = null;
		searchDivision = null;
		searchFollowupStatus = null;
		searchComplianceStatus = null;
		searchAuditTemplateName = null;
		searchAuditFindingsName = null;
		
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));
		
		PrimeFaces.current().executeScript("reInitSelect2();");
//		RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}
	
	public void delete(Long deleteId) {
		try {
			TmpAudit dt = tmpAuditService.findById(deleteId);
			dt.setEnabledFlag(Constants.CONSTANT_NO);
			dt.setLastUpdateBy(facesUtil.retrieveUserLogin());
			dt.setLastUpdateDate(new Timestamp(new Date().getTime()));
			tmpAuditService.update(dt);
			facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public Boolean getIsLogin() {
		if (facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE) != null
				|| facesUtil.getSessionAttribute(Constants.SESSION_KANDIDAT) != null)
			return true;
		else
			return false;
	}
	
	public void postProcessXLS(Object document) {
		try {
			HSSFWorkbook wb = (HSSFWorkbook) document;
			HSSFSheet sheet = wb.getSheetAt(0);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			List<TmpAuditVO> listDataXls = tmpAuditService.searchDataXLS(
					Arrays.asList(
							new DefaultSearchObject(WHERE_AUDIT_FOLLOWUP, searchAuditFollowUp),
							new DefaultSearchObject(WHERE_AUDIT_OBJECT, searchAuditObject),
							new DefaultSearchObject(WHERE_AUDIT_STATUS, searchStatusCode),
							new DefaultSearchObject(WHERE_AUDIT_PERIOD_FROM, searchAuditPeriodFrom != null ? sdf.format(searchAuditPeriodFrom) : ""),
							new DefaultSearchObject(WHERE_AUDIT_PERIOD_TO, searchAuditPeriodTo != null ? sdf.format(searchAuditPeriodTo) : ""),
							new DefaultSearchObject(WHERE_AUDIT_TARGET_FROM, searchAuditTargetFrom != null ? sdf.format(searchAuditTargetFrom) : ""),
							new DefaultSearchObject(WHERE_AUDIT_TARGET_TO, searchAuditTargetTo != null ? sdf.format(searchAuditTargetTo) : ""),
							new DefaultSearchObject(WHERE_AUDIT_DIVISION_ID, searchDivision),
							new DefaultSearchObject(WHERE_AUDIT_FOLLOWUP_STATUS, searchFollowupStatus),
							new DefaultSearchObject(WHERE_AUDIT_COMPLIANCE_STATUS, searchComplianceStatus),
							new DefaultSearchObject(WHERE_AUDIT_FINDINGS_NAME, searchAuditFindingsName),
							new DefaultSearchObject(WHERE_AUDIT_TEMPLATE_NAME, searchAuditTemplateName)
							));
			
			// create Header
			HSSFRow header = sheet.createRow(0);
			for (int i = 0; i < 14; i++) {
				HSSFCell cell = header.createCell((short) i);
				if (i == 0) {
					cell.setCellValue(facesUtil.retrieveMessage("formAuditTemplateName"));
				} else if (i == 1) {
					cell.setCellValue(facesUtil.retrieveMessage("formAuditType"));
				} else if (i == 2) {
					cell.setCellValue(facesUtil.retrieveMessage("formAuditObject"));
				} else if (i == 3) {
					cell.setCellValue(facesUtil.retrieveMessage("formAuditFindingName"));
				} else if (i == 4) {
					cell.setCellValue(facesUtil.retrieveMessage("formAuditCategory"));
				} else if (i == 5) {
					cell.setCellValue(facesUtil.retrieveMessage("formAuditPeriode"));
				} else if (i == 6) {
					cell.setCellValue(facesUtil.retrieveMessage("formAuditScope"));
				} else if (i == 7) {
					cell.setCellValue(facesUtil.retrieveMessage("formAuditStatus"));
				} else if (i == 8) {
					 cell.setCellValue(facesUtil.retrieveMessage("formAuditTargetDate")); 
				} else if (i == 9) {
					cell.setCellValue(facesUtil.retrieveMessage("formAuditPICFollowupStatus")); 
				} else if (i == 10) {
					cell.setCellValue(facesUtil.retrieveMessage("formAuditPICConfirmation")); 
				} else if (i == 11) {
					cell.setCellValue(facesUtil.retrieveMessage("formAuditPICConfirmationDate"));
				} else if (i == 12) {
					cell.setCellValue(facesUtil.retrieveMessage("formAuditPICFollowupDate")); 
				} else if (i == 13) { 
					cell.setCellValue(facesUtil.retrieveMessage("formAuditPICComplianceCheckStatus")); 
				}

			}
			
			//kosongin data
			int rowNum = 1;
			for(int i=0;i<14;i++) {
				HSSFRow row = sheet.createRow(rowNum);
				for (int x = 0; x < 14; x++) {
					HSSFCell cell = row.createCell((short) x);
					cell.setCellValue("");
				}
				rowNum++;
			}
			//kosongin data

			// create Data
			rowNum = 1;
			
			
			for (int i = 0; i < listDataXls.size(); i++) {
				TmpAuditVO er = (TmpAuditVO) listDataXls.get(i);
				HSSFRow row = sheet.createRow(rowNum);
				StringBuilder targetDate = new StringBuilder("");
				StringBuilder followUpStatus = new StringBuilder("");
				StringBuilder followupBy = new StringBuilder("");
				StringBuilder confirmationDate = new StringBuilder("");
				StringBuilder followupDate = new StringBuilder("");
				StringBuilder compilanceStatus = new StringBuilder("");
				
				for (AuditConfirmationVO attach : er.getStatusList()) {
					if (er.getStatusList().indexOf(attach) == (er.getStatusList().size()-1)) {
						// target date
						targetDate.append(StringUtils.isNotBlank(attach.getTargetDate())?attach.getTargetDate():"");
						if (targetDate != null && targetDate.length() > 1 && targetDate.substring(targetDate.length()-2).equals(", ")) { targetDate.deleteCharAt(targetDate.lastIndexOf(", ")); }
						if (targetDate != null && targetDate.length() > 1) { targetDate.append("\n"); }
						
						// follow up status
						followUpStatus.append(StringUtils.isNotBlank(attach.getFollowupStatus())?attach.getFollowupStatus():"");
						if (followUpStatus != null && followUpStatus.length() > 1 && followUpStatus.substring(followUpStatus.length()-2).equals(", ")) { followUpStatus.deleteCharAt(followUpStatus.lastIndexOf(", ")); }
						if (followUpStatus != null && followUpStatus.length() > 1) { followUpStatus.append("\n"); }
						
						// follow up by
						followupBy.append(StringUtils.isNotBlank(attach.getFollowupBy())?attach.getFollowupBy():"");
						if (followupBy != null && followupBy.length() > 1 && followupBy.substring(followupBy.length()-2).equals(", ")) { followupBy.deleteCharAt(followupBy.lastIndexOf(", ")); }
						if (followupBy != null && followupBy.length() > 1) { followupBy.append("\n"); }
						
						// confirmation date
						confirmationDate.append(StringUtils.isNotBlank(attach.getComplianceDate())?attach.getComplianceDate():"");
						if (confirmationDate != null && confirmationDate.length() > 1 && confirmationDate.substring(confirmationDate.length()-2).equals(", ")) { confirmationDate.deleteCharAt(confirmationDate.lastIndexOf(", ")); }
						if (confirmationDate != null && confirmationDate.length() > 1) { confirmationDate.append("\n"); }
						
						// follow up date
						followupDate.append(StringUtils.isNotBlank(attach.getFollowupDate())?attach.getFollowupDate():"");
						if (followupDate != null && followupDate.length() > 1 && followupDate.substring(followupDate.length()-2).equals(", ")) { followupDate.deleteCharAt(followupDate.lastIndexOf(", ")); }
						if (followupDate != null && followupDate.length() > 1) { followupDate.append("\n"); }
						
						// compliance status
						compilanceStatus.append(StringUtils.isNotBlank(attach.getComplianceStatus())?attach.getComplianceStatus():"");
						if (compilanceStatus != null && compilanceStatus.length() > 1 && compilanceStatus.substring(compilanceStatus.length()-2).equals(", ")) { compilanceStatus.deleteCharAt(compilanceStatus.lastIndexOf(", ")); }
						if (compilanceStatus != null && compilanceStatus.length() > 1) { compilanceStatus.append("\n"); }
					} else {
						// target date
						targetDate.append(StringUtils.isNotBlank(attach.getTargetDate())?attach.getTargetDate():"");
						if (targetDate != null && targetDate.length() > 0) { if (StringUtils.isNotBlank(attach.getTargetDate())) {targetDate.append(", ");} }
						
						// follow up status
						followUpStatus.append(StringUtils.isNotBlank(attach.getFollowupStatus())?attach.getFollowupStatus():"");
						if (followUpStatus != null && followUpStatus.length() > 0) { if (StringUtils.isNotBlank(attach.getFollowupStatus())) {followUpStatus.append(", ");} } 
						
						// follow up by
						followupBy.append(StringUtils.isNotBlank(attach.getFollowupBy())?attach.getFollowupBy():"");
						if (followupBy != null && followupBy.length() > 0) { if (StringUtils.isNotBlank(attach.getFollowupBy())) {followupBy.append(", ");} }
						
						// confirmation date
						confirmationDate.append(StringUtils.isNotBlank(attach.getConfirmationDate())?attach.getConfirmationDate():"");
						if (confirmationDate != null && confirmationDate.length() > 0) { if (StringUtils.isNotBlank(attach.getConfirmationDate())) {confirmationDate.append(", ");} }
						
						// follow up date
						followupDate.append(StringUtils.isNotBlank(attach.getFollowupDate())?attach.getFollowupDate():"");
						if (followupDate != null && followupDate.length() > 0) { if (StringUtils.isNotBlank(attach.getFollowupDate())) {followupDate.append(", ");} }
						
						// compliance status
						compilanceStatus.append(StringUtils.isNotBlank(attach.getComplianceStatus())?attach.getComplianceStatus():"");
						if (compilanceStatus != null && compilanceStatus.length() > 0) { if (StringUtils.isNotBlank(attach.getComplianceStatus())) {compilanceStatus.append(", ");} }
					}
				}
				
				for (int x = 0; x < 14; x++) {
					HSSFCell cell = row.createCell(x);
					if (x == 0) {
						cell.setCellValue(er.getAuditTemplateName() != null ? er.getAuditTemplateName() : "");
					} else if (x == 1) {
						cell.setCellValue(er.getAuditor() != null ? er.getAuditor() : "");
					} else if (x == 2) {
						cell.setCellValue(er.getAuditObject() != null ? er.getAuditObject() : "");
					} else if (x == 3) {
						cell.setCellValue(er.getFindingName() != null ? er.getFindingName() : "");
					} else if (x == 4) {
						cell.setCellValue(er.getAuditCategory() != null ? er.getAuditCategory() : "");
					} else if (x == 5) {
						StringBuffer sb = new StringBuffer();
						sb.append(er.getAuditDateFrom() != null ? er.getAuditDateFrom() : "");
						if(er.getAuditDateTo() != null) {
							sb.append(" - ");
							sb.append(er.getAuditDateTo() != null ? er.getAuditDateTo() : "");
						}
						
						cell.setCellValue(sb.toString());
					} else if (x == 6) {
						cell.setCellValue(er.getScope() != null ? er.getScope() : "");
					} else if (x == 7) {
						cell.setCellValue(er.getStatus() != null ? er.getStatus() : "");
					} else if (x == 8) {
						cell.setCellValue(targetDate != null ? targetDate.toString() : "");
					} else if (x == 9) {
						cell.setCellValue(followUpStatus != null ? followUpStatus.toString() : "");
					} else if (x == 10) {
						cell.setCellValue(followupBy != null ? followupBy.toString() : "'");
					} else if (x == 11) {
						cell.setCellValue(confirmationDate != null ? confirmationDate.toString() : "");
					} else if (x == 12) {
						cell.setCellValue(followupDate != null ? followupDate.toString() : "");
					} else if (x == 13) {
						cell.setCellValue(compilanceStatus != null ? compilanceStatus.toString() : "");
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

	public String getRegulationSocializationTmpSearch() {
		return regulationSocializationTmpSearch;
	}

	public void setRegulationSocializationTmpSearch(String regulationSocializationTmpSearch) {
		this.regulationSocializationTmpSearch = regulationSocializationTmpSearch;
	}

	public String getSearchVal() {
		return searchVal;
	}

	public void setSearchVal(String searchVal) {
		this.searchVal = searchVal;
	}

	public String getNavigateEdit() {
		facesUtil.removeSessionAttribute(EMAIL_TEMPLATE_AUDIT);
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<SelectItem> getStatus() {
		return status;
	}

	public void setStatus(List<SelectItem> status) {
		this.status = status;
	}

	public DBLazyDataModel<TmpAuditVO> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<TmpAuditVO> tableModel) {
		this.tableModel = tableModel;
	}

	public List<SelectItem> getSelectAuditFollowUp() {
		return selectAuditFollowUp;
	}

	public void setSelectAuditFollowUp(List<SelectItem> selectAuditFollowUp) {
		this.selectAuditFollowUp = selectAuditFollowUp;
	}

	public List<SelectItem> getSelectAuditObject() {
		return selectAuditObject;
	}

	public void setSelectAuditObject(List<SelectItem> selectAuditObject) {
		this.selectAuditObject = selectAuditObject;
	}

	public String getSearchAuditFollowUp() {
		return searchAuditFollowUp;
	}

	public void setSearchAuditFollowUp(String searchAuditFollowUp) {
		this.searchAuditFollowUp = searchAuditFollowUp;
	}

	public String getSearchAuditObject() {
		return searchAuditObject;
	}

	public void setSearchAuditObject(String searchAuditObject) {
		this.searchAuditObject = searchAuditObject;
	}

	public String getSearchStatusCode() {
		return searchStatusCode;
	}

	public void setSearchStatusCode(String searchStatusCode) {
		this.searchStatusCode = searchStatusCode;
	}

	public Date getSearchAuditPeriodFrom() {
		return searchAuditPeriodFrom;
	}

	public void setSearchAuditPeriodFrom(Date searchAuditPeriodFrom) {
		this.searchAuditPeriodFrom = searchAuditPeriodFrom;
	}

	public Date getSearchAuditPeriodTo() {
		return searchAuditPeriodTo;
	}

	public void setSearchAuditPeriodTo(Date searchAuditPeriodTo) {
		this.searchAuditPeriodTo = searchAuditPeriodTo;
	}

	public Date getSearchAuditTargetFrom() {
		return searchAuditTargetFrom;
	}

	public void setSearchAuditTargetFrom(Date searchAuditTargetFrom) {
		this.searchAuditTargetFrom = searchAuditTargetFrom;
	}

	public Date getSearchAuditTargetTo() {
		return searchAuditTargetTo;
	}

	public void setSearchAuditTargetTo(Date searchAuditTargetTo) {
		this.searchAuditTargetTo = searchAuditTargetTo;
	}

	public TmpAuditService getTmpAuditService() {
		return tmpAuditService;
	}

	public void setTmpAuditService(TmpAuditService tmpAuditService) {
		this.tmpAuditService = tmpAuditService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<SelectItem> getSelectDivisions() {
		return selectDivisions;
	}

	public void setSelectDivisions(List<SelectItem> selectDivisions) {
		this.selectDivisions = selectDivisions;
	}

	public String getSearchDivision() {
		return searchDivision;
	}

	public void setSearchDivision(String searchDivision) {
		this.searchDivision = searchDivision;
	}

	public String getSearchFollowupStatus() {
		return searchFollowupStatus;
	}

	public void setSearchFollowupStatus(String searchFollowupStatus) {
		this.searchFollowupStatus = searchFollowupStatus;
	}

	public String getSearchComplianceStatus() {
		return searchComplianceStatus;
	}

	public void setSearchComplianceStatus(String searchComplianceStatus) {
		this.searchComplianceStatus = searchComplianceStatus;
	}

	public List<SelectItem> getSelectFollowupStatus() {
		return selectFollowupStatus;
	}

	public void setSelectFollowupStatus(List<SelectItem> selectFollowupStatus) {
		this.selectFollowupStatus = selectFollowupStatus;
	}

	public List<SelectItem> getSelectComplianceStatus() {
		return selectComplianceStatus;
	}

	public void setSelectComplianceStatus(List<SelectItem> selectComplianceStatus) {
		this.selectComplianceStatus = selectComplianceStatus;
	}

	public Long getSearchAuditTemplateName() {
		return searchAuditTemplateName;
	}

	public void setSearchAuditTemplateName(Long searchAuditTemplateName) {
		this.searchAuditTemplateName = searchAuditTemplateName;
	}

	public List<SelectItem> getSelectAuditTemplateName() {
		return selectAuditTemplateName;
	}

	public void setSelectAuditTemplateName(List<SelectItem> selectAuditTemplateName) {
		this.selectAuditTemplateName = selectAuditTemplateName;
	}

	public MstAuditService getMstAuditService() {
		return mstAuditService;
	}

	public void setMstAuditService(MstAuditService mstAuditService) {
		this.mstAuditService = mstAuditService;
	}

	public String getSearchAuditFindingsName() {
		return searchAuditFindingsName;
	}

	public void setSearchAuditFindingsName(String searchAuditFindingsName) {
		this.searchAuditFindingsName = searchAuditFindingsName;
	}

}