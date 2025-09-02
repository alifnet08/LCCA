package com.wo.module.logAccess.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

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
import com.wo.module.litigation.constant.LitigationConstants;
import com.wo.module.logAccess.constant.LogAccessConstants;
import com.wo.module.logAccess.model.LogAccess;
import com.wo.module.lov.bean.FacesUtil;

public class LogAccessBean extends CommonBean  implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(LogAccessBean.class);

	private Date startDate;
	
	private Date endDate;
	
	private String name;
	
	private String divName;

	private int paging;


	private DBLazyDataModel<LogAccess> tableModel;
	
	private Long userIdLogin;

	public FacesUtil facesUtil;

	private String navigateEdit = LitigationConstants.NAVIGATE_EDIT;

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
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<LogAccess>(logAccessService, paging);
	}
	
	
	
	public void postProcessXLS(Object document) {
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

			HSSFWorkbook wb = (HSSFWorkbook) document;
			HSSFSheet sheet = wb.getSheetAt(0);

			List<LogAccess> listDataXls = logAccessService.searchData(
					Arrays.asList(
							new DefaultSearchObject(LogAccessConstants.WHERE_DATE_START,
									startDate != null ? sdf.format(startDate) : ""),
							new DefaultSearchObject(LogAccessConstants.WHERE_DATE_END,
									endDate != null ? sdf.format(endDate) : ""),
							new DefaultSearchObject(LogAccessConstants.WHERE_NAME,name),
							new DefaultSearchObject(LogAccessConstants.WHERE_DIV_NAME,divName)
							),
					0, Integer.MAX_VALUE, null, null);
			
			// create Header
			HSSFRow header = sheet.createRow(0);
			for (int i = 0; i < 6; i++) {
				HSSFCell cell = header.createCell((short) i);
				if (i == 0) {
					cell.setCellValue("User Name");
				} else if (i == 1) {
					cell.setCellValue("Source IP");
				} else if (i == 2) {
					cell.setCellValue("Access Time");
				} else if (i == 3) {
					cell.setCellValue("Access Action");
				} else if (i == 4) {
					cell.setCellValue("Access Id");
				} else if (i == 5) {
					cell.setCellValue("Nama Divisi");
				} 

			}
			
			//kosongin data
			int rowNum = 1;
			for(int i=0;i<6;i++) {
				HSSFRow row = sheet.createRow(rowNum);
				for (int x = 0; x < 6; x++) {
					HSSFCell cell = row.createCell((short) x);
					cell.setCellValue("");
				}
				rowNum++;
			}
			//kosongin data
			
			// create Data
			rowNum = 1;
			for (int i = 0; i < listDataXls.size(); i++) {
				LogAccess er = (LogAccess) listDataXls.get(i);
				HSSFRow row = sheet.createRow(rowNum);
				for (int x = 0; x < 6; x++) {
					HSSFCell cell = row.createCell((short) x);
					if (x == 0) {
						cell.setCellValue(er.getUserName() != null ? er.getUserName() : "");
					} else if (x == 1) {
						cell.setCellValue(er.getSourceIp() != null ? er.getSourceIp() : "");
					} else if (x == 2) {
						cell.setCellValue(er.getAccessTime() != null ? sdf.format(er.getAccessTime()) : "");
					} else if (x == 3) {
						cell.setCellValue(er.getAccessAction() != null ? er.getAccessAction() : "");
					} else if (x == 4) {
						cell.setCellValue(er.getAccessId() != null ? er.getAccessId().toString() : "");
					} else if (x == 5) {
						cell.setCellValue(er.getDivisionName() != null ? er.getDivisionName().toString() : "");
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

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (startDate != null) {
			searchCriteria.add(new DefaultSearchObject(LogAccessConstants.WHERE_DATE_START, startDate != null ? sdf.format(startDate) : ""));
		}
		if (endDate != null) {
			searchCriteria.add(new DefaultSearchObject(LogAccessConstants.WHERE_DATE_END, endDate != null ? sdf.format(endDate) : ""));
		}
		if (name != null && !name.equals("")) {
			searchCriteria.add(new DefaultSearchObject(LogAccessConstants.WHERE_NAME, name));
		}
		if (divName != null && !divName.equals("")) {
			searchCriteria.add(new DefaultSearchObject(LogAccessConstants.WHERE_DIV_NAME, divName));
		}

		tableModel.setSearchCriteria(searchCriteria);
		
	}

	public void reset(ActionEvent actionEvent) {
		search(actionEvent);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
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


	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}


	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}


	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		LogAccessBean.logger = logger;
	}

	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public Long getUserIdLogin() {
		return userIdLogin;
	}

	public void setUserIdLogin(Long userIdLogin) {
		this.userIdLogin = userIdLogin;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public DBLazyDataModel<LogAccess> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<LogAccess> tableModel) {
		this.tableModel = tableModel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDivName() {
		return divName;
	}

	public void setDivName(String divName) {
		this.divName = divName;
	}

	
	
}