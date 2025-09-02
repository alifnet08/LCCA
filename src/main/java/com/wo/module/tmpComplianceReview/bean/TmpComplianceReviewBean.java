package com.wo.module.tmpComplianceReview.bean;

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

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.js.JsUtil;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.complianceTestingMockup.constant.ComplianceTestingMockupConstants;
import com.wo.module.complianceTestingMockup.model.ComplianceTesting;
import com.wo.module.complianceTestingMockup.service.ComplianceTestingService;
import com.wo.module.complianceTestingMockup.vo.ComplianceTestingVO;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.tmpAudit.model.TmpAudit;
import com.wo.module.tmpComplianceReview.constant.TmpComplianceReviewConstants;
import com.wo.module.user.model.Division;
import com.wo.module.user.service.UserService;

public class TmpComplianceReviewBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 7789378048838667090L;

	static Logger logger = Logger.getLogger(TmpComplianceReviewBean.class);

	private String searchVal;
	
	private String searchInspectionTitle;
	
	private String searchInspectionNo;
	
	private Date searchPeriodFrom;
	private Date searchPeriodTo;
	
	private String searchFollowupStatus;
	private String searchComplianceStatus;
	
	private List<SelectItem> divisions;

	private List<SelectItem> statusList;
	
	private List<SelectItem> selectFollowupStatus;
	private List<SelectItem> selectComplianceStatus;

	private DBLazyDataModel<ComplianceTestingVO> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = TmpComplianceReviewConstants.NAVIGATE_EDIT;

	private UserService userService;
	
	private ComplianceTestingService complianceTestingService;
	
	

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
		selectStatus();
		selectDivision();
		setupFollowupStatus();
		setupComplianceStatus();
	
		tableModel = new DBLazyDataModel<ComplianceTestingVO>(complianceTestingService, paging);
		
		
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

		JsUtil.reInitSelect2();
	}
	

	public void selectStatus() {
		statusList = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("DATA_STATUS");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				statusList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void search(ActionEvent actionEvent) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		tableModel.setSearchCriteria(Arrays.asList(
				new DefaultSearchObject(ComplianceTestingMockupConstants.WHERE_INSPECTION_TITLE, searchInspectionTitle),
				new DefaultSearchObject(ComplianceTestingMockupConstants.WHERE_INSPECTION_NO, searchInspectionNo),
				new DefaultSearchObject(ComplianceTestingMockupConstants.WHERE_COMPLIANCE_STATUS, searchComplianceStatus),
				new DefaultSearchObject(ComplianceTestingMockupConstants.WHERE_FOLLOWUP_STATUS, searchFollowupStatus),
				new DefaultSearchObject(ComplianceTestingMockupConstants.WHERE_PERIOD_FROM, searchPeriodFrom!=null?sdf.format(searchPeriodFrom):null),
				new DefaultSearchObject(ComplianceTestingMockupConstants.WHERE_PERIOD_TO, searchPeriodTo!=null?sdf.format(searchPeriodTo):null)
				));
	}
	
	public void reset(ActionEvent actionEvent) {
		searchInspectionTitle = null;
		searchInspectionNo = null;
		searchComplianceStatus = null;
		searchFollowupStatus = null;
		searchPeriodFrom = null;
		searchPeriodTo = null;
		
		
		search(actionEvent);
	}
	
	public void postProcessXLS(Object document) {
		try {
			HSSFWorkbook wb = (HSSFWorkbook) document;
			HSSFSheet sheet = wb.getSheetAt(0);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			List<ComplianceTestingVO> listDataXls = complianceTestingService.searchDataXls(
					Arrays.asList(
							new DefaultSearchObject(ComplianceTestingMockupConstants.WHERE_INSPECTION_TITLE, searchInspectionTitle),
							new DefaultSearchObject(ComplianceTestingMockupConstants.WHERE_INSPECTION_NO, searchInspectionNo)
							//new DefaultSearchObject(ComplianceTestingMockupConstants.WHERE_STATUS, searchStatusCode),
							//new DefaultSearchObject(ComplianceTestingMockupConstants.WHERE_PERIOD_FROM, searchAuditPeriodFrom != null ? sdf.format(searchAuditPeriodFrom) : ""),
							//new DefaultSearchObject(ComplianceTestingMockupConstants.WHERE_PERIOD_TO, searchAuditPeriodTo != null ? sdf.format(searchAuditPeriodTo) : ""),
							
							));
			
			// create Header
			HSSFRow header = sheet.createRow(0);
			for (int i = 0; i < 9; i++) {
				HSSFCell cell = header.createCell((short) i);
				if (i == 0) {
					cell.setCellValue("Judul Pemeriksaan");
				} else if (i == 1) {
					cell.setCellValue("Nomor Pemeriksaan");
				} else if (i == 2) {
					cell.setCellValue("Nama PIC");
				} else if (i == 3) {
					cell.setCellValue("Target Tanggal");
				} else if (i == 4) {
					cell.setCellValue("Status Tindak Lanjut");
				} else if (i == 5) {
					cell.setCellValue("Tgl Pemenuhan Tindak Lanjut");
				} else if (i == 6) {
					cell.setCellValue("Hasil Observasi");
				} else if (i == 7) {
					cell.setCellValue("Rekomendasi");
				}else if (i == 8) {
				    cell.setCellValue("Tindak Lanjut"); 
				}

			}
			
			//kosongin data
			int rowNum = 1;
			for(int i=0;i<9;i++) {
				HSSFRow row = sheet.createRow(rowNum);
				for (int x = 0; x < 9; x++) {
					HSSFCell cell = row.createCell((short) x);
					cell.setCellValue("");
				}
				rowNum++;
			}
			//kosongin data

			// create Data
			rowNum = 1;
			
			
			for (int i = 0; i < listDataXls.size(); i++) {
				ComplianceTestingVO er = (ComplianceTestingVO) listDataXls.get(i);
				HSSFRow row = sheet.createRow(rowNum);
				
				
				for (int x = 0; x < 14; x++) {
					HSSFCell cell = row.createCell(x);
					if (x == 0) {
						cell.setCellValue(er.getJudulPemeriksaan() != null ? er.getJudulPemeriksaan() : "");
					} else if (x == 1) {
						cell.setCellValue(er.getNoPemeriksaan() != null ? er.getNoPemeriksaan() : "");
					} else if (x == 2) {
						cell.setCellValue(er.getNamaPic() != null ? er.getNamaPic() : "");
					} else if (x == 3) {
						cell.setCellValue(er.getTargetTgl() != null ? er.getTargetTgl() : "");
					} else if (x == 4) {
						cell.setCellValue(er.getStatusTindakLanjut() != null ? er.getStatusTindakLanjut() : "");
					} else if (x == 5) {
						cell.setCellValue(er.getTglPemenuhanTindakLanjut() != null ? er.getTglPemenuhanTindakLanjut() : "");
					} else if (x == 6) {
						cell.setCellValue(er.getHasilObservasi()!= null ? er.getHasilObservasi() : "");
					} else if (x == 7) {
						cell.setCellValue(er.getRekomendasi() != null ? er.getRekomendasi() : "");
					}else if (x == 8) {
						cell.setCellValue(er.getKeterangan() != null ? er.getKeterangan(): "'");
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
	
	public void delete(Long deleteId) {
		try {
			ComplianceTesting dt = complianceTestingService.findById(deleteId);
			dt.setEnabledFlag(Constants.CONSTANT_NO);
			dt.setLastUpdateBy(facesUtil.retrieveUserLogin());
			dt.setLastUpdateDate(new Timestamp(new Date().getTime()));
			complianceTestingService.update(dt);
			facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		TmpComplianceReviewBean.logger = logger;
	}

	public String getSearchVal() {
		return searchVal;
	}

	public void setSearchVal(String searchVal) {
		this.searchVal = searchVal;
	}

	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}


	public DBLazyDataModel<ComplianceTestingVO> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<ComplianceTestingVO> tableModel) {
		this.tableModel = tableModel;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<SelectItem> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<SelectItem> divisions) {
		this.divisions = divisions;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getSearchInspectionTitle() {
		return searchInspectionTitle;
	}

	public void setSearchInspectionTitle(String searchInspectionTitle) {
		this.searchInspectionTitle = searchInspectionTitle;
	}

	public String getSearchInspectionNo() {
		return searchInspectionNo;
	}

	public void setSearchInspectionNo(String searchInspectionNo) {
		this.searchInspectionNo = searchInspectionNo;
	}

	public ComplianceTestingService getComplianceTestingService() {
		return complianceTestingService;
	}

	public void setComplianceTestingService(ComplianceTestingService complianceTestingService) {
		this.complianceTestingService = complianceTestingService;
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

	public Date getSearchPeriodFrom() {
		return searchPeriodFrom;
	}

	public void setSearchPeriodFrom(Date searchPeriodFrom) {
		this.searchPeriodFrom = searchPeriodFrom;
	}

	public Date getSearchPeriodTo() {
		return searchPeriodTo;
	}

	public void setSearchPeriodTo(Date searchPeriodTo) {
		this.searchPeriodTo = searchPeriodTo;
	}
	
	
}