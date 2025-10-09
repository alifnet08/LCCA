package com.wo.module.mstProvince.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.mstProvince.constants.MstProvinceConstants;
import com.wo.module.mstProvince.model.MstProvince;
import com.wo.module.mstProvince.service.MstProvinceService;
import com.wo.module.outgoingLetter.constant.OutgoingLetterConstants;
import com.wo.module.parameter.model.ParameterDetail;

public class MstProvinceBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static Logger logger = Logger.getLogger(MstProvinceBean.class);
	
	private DBLazyDataModel<MstProvince> tableModel;
	
	private MstProvinceService mstProvinceService;
	
	private String searchBranchCode;
	private String searchProvince;
	
	private int paging;
	
	private String navigateEdit = MstProvinceConstants.NAVIGATE_EDIT;
	
	private FacesUtil facesUtil;
	
	private UploadedFileWO uploadFile;
	
	private FileUtil fileUtil;
	
	private NumberFormat decimalFormatter = new DecimalFormat("#.#");
	
	@PostConstruct
	public void init() {
		super.init();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<MstProvince>(mstProvinceService, paging);	
		fileUtil = FileUtil.getInstance();
	}
	
	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		
		if (searchBranchCode != null && !searchBranchCode.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(MstProvinceConstants.SEARCH_BY_BRANCH_CODE, searchBranchCode));
		}
		
		if (searchProvince != null && !searchProvince.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(MstProvinceConstants.SEARCH_BY_PROVINCE, searchProvince));
		}
		
		tableModel.setSearchCriteria(searchCriteria);
	}
	
	public void reset(ActionEvent actionEvent) {
		searchBranchCode = "";
		searchProvince = "";
		
		search(actionEvent);
	}
	
	public void delete(Long deleteId) {
		try {			
				MstProvince entity = mstProvinceService.findById(deleteId);
				entity.setEnabledFlag(Constants.CONSTANT_NO);
				entity.setLastUpdateBy(facesUtil.retrieveUserLogin());
				entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
				
				mstProvinceService.update(entity);
				facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}
	
	
	public void handleFileUpload (FileUploadEvent event) {
		try {
			
			if (event.getFile().getFileName().endsWith(".xlsx") || 
					event.getFile().getFileName().endsWith(".xls")) {
				uploadFile = new UploadedFileWO( CallApiManager.callUploadAPI(event.getFile(), Constants.ARTICLE, parameterDetailService, false, fileUtil),
						event.getFile().getFileName(),event.getFile().getContentType(),event.getFile().getSize());
				
				getDataFromFile(event.getFile().getInputstream(), event.getFile().getFileName());
			} else {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("errorExcelFilesOnly"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	
	@SuppressWarnings("resource")
	private void getDataFromFile(InputStream is, String fileName) throws Exception {
		logger.info("Get data from file [start] ...");
		
		MstProvince uploadedProvince = new MstProvince();
		
		XSSFWorkbook xWb;
		HSSFWorkbook hWb;
		Sheet sheet = null;
		
		if (fileName.endsWith(".xls")) {
			hWb = new HSSFWorkbook(is);
			sheet = hWb.getSheetAt(0);
		} else if (fileName.endsWith(".xlsx")) {
			xWb = new XSSFWorkbook(is);
			sheet = xWb.getSheetAt(0);
		}

		boolean foundDuplicate = false;
		boolean foundBranchCodeEmpty = false;
		boolean foundProvinceEmpty =  false;
		
		String branchCodeNotFound = "";
		String rowBranchCodeEmpty = "";
		String rowProvinceEmpty = "";
		
		Row row = null;
		Cell cell = null;

		Integer startRowNum = MstProvinceConstants.PROVINCE_START_ROW;

		Integer rowNum = startRowNum;
		Integer colNum = null;
		Integer lastRowNum = sheet.getLastRowNum() + 1;

		while (rowNum <= lastRowNum) {
			if (rowNum == lastRowNum) {
				logger.info("Finished collecting data [End]");
				break;
			} else {
				System.out.println("lastrownum " + lastRowNum);
				try {
					if (rowNum == startRowNum) {
						row = sheet.getRow(startRowNum);

					} else if (rowNum > startRowNum) {
						row = sheet.getRow(rowNum);
						System.out.println("row = " + rowNum);
					}

					colNum = MstProvinceConstants.PROVINCE_BRANCH_CODE_COL;

					cell = row.getCell(colNum);

					String branchCode = getExcelValue(cell, true);
					
					if (branchCode != null && !StringUtils.isEmpty(branchCode)) {
						Integer duplicate = mstProvinceService.countBranchCodeDuplicate(branchCode);
						
						if (duplicate.intValue() > 0) {
							branchCodeNotFound = branchCodeNotFound.concat(branchCode + " ");
							
							foundDuplicate = true;
						} else {
							uploadedProvince.setBranchCode(branchCode);
							
							colNum = MstProvinceConstants.PROVINCE_PROVINCE_COL;
							
							cell = row.getCell(colNum);
							
							String province = getExcelValue(cell, true);
							
							if (province != null && !StringUtils.isEmpty(province)) {
								uploadedProvince.setProvince(province);
								
								uploadedProvince.setCreatedBy(facesUtil.retrieveUserLogin());
								uploadedProvince.setCreationDate(new Timestamp(new Date().getTime()));
								uploadedProvince.setDelId(new Long(0));
								uploadedProvince.setEnabledFlag(Constants.CONSTANT_YES);
								
								mstProvinceService.save(uploadedProvince);
							} else {
								int trueRow = rowNum + 1;
								rowProvinceEmpty = rowProvinceEmpty.concat(String.valueOf(trueRow) + " ");
								foundProvinceEmpty = true;
							}
						}
					} else {
						int trueRow = rowNum + 1;
						
						rowBranchCodeEmpty = rowBranchCodeEmpty.concat(String.valueOf(trueRow) + " ");
						foundBranchCodeEmpty = true;
					}
					
					rowNum++;
				} catch (Exception e) {
					e.printStackTrace();
					rowNum++;
				}
			}
		}
		
		if (foundDuplicate) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formMstProvinceBranchCode") 
					+ " " + branchCodeNotFound 
					+ facesUtil.retrieveMessage("errorDuplicate"));
		} 
		
		if (foundBranchCodeEmpty) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formMstProvinceBranchCode") 
					+ " " + facesUtil.retrieveMessage("errorEmptyForRow")
					+ rowBranchCodeEmpty );
		}
		
		if (foundProvinceEmpty) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formMstProvinceProvince") 
					+ " " + facesUtil.retrieveMessage("errorEmptyForRow")
					+ rowProvinceEmpty);
		}
		
	}
	
	@SuppressWarnings("resource")
	public void createTemplateFile() {
		try {
			// File file = new File(“Path of the sheet”);
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet();
			sheet.setColumnWidth(MstProvinceConstants.PROVINCE_BRANCH_CODE_COL, 5000);
			sheet.setColumnWidth(MstProvinceConstants.PROVINCE_PROVINCE_COL, 10000);
			
			XSSFCellStyle style = workbook.createCellStyle();
			style.setAlignment(HorizontalAlignment.CENTER);
	       
	        XSSFFont font = workbook.createFont();
	        font.setBold(true);
	        style.setFont(font);
	        
	        Row row = sheet.createRow(0);
			Cell cell = row.createCell(MstProvinceConstants.PROVINCE_BRANCH_CODE_COL);
			cell.setCellValue("BRANCH CODE");
			cell.setCellStyle(style);

			cell = row.createCell(MstProvinceConstants.PROVINCE_PROVINCE_COL);
			cell.setCellValue("PROVINCE");
			cell.setCellStyle(style);

			FacesContext fc = FacesContext.getCurrentInstance();

			HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();

			response.reset();

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

			response.setHeader("Content-Disposition", "filename=\"provinceTemplate.xlsx");

			workbook.write(response.getOutputStream());

			fc.responseComplete();

		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	private String getExcelValue(Cell cell, boolean toString) {
		String value = null;

		if (cell == null) {
			return StringUtils.EMPTY;
		}

		switch (cell.getCellType().toString()) {
		case "STRING":
			value = cell.getStringCellValue();
			break;
		case "NUMERIC":
			if (toString){
				long i = (long)cell.getNumericCellValue();
				value = String.valueOf(i);
			} else {
				value = decimalFormatter.format(cell.getNumericCellValue());
			}
			break;
		default:
			value = StringUtils.EMPTY;
			break;
		}

		return value;
	}

	
	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		MstProvinceBean.logger = logger;
	}

	public DBLazyDataModel<MstProvince> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<MstProvince> tableModel) {
		this.tableModel = tableModel;
	}

	public MstProvinceService getMstProvinceService() {
		return mstProvinceService;
	}

	public void setMstProvinceService(MstProvinceService mstProvinceService) {
		this.mstProvinceService = mstProvinceService;
	}

	public String getSearchBranchCode() {
		return searchBranchCode;
	}

	public void setSearchBranchCode(String searchBranchCode) {
		this.searchBranchCode = searchBranchCode;
	}

	public String getSearchProvince() {
		return searchProvince;
	}

	public void setSearchProvince(String searchProvince) {
		this.searchProvince = searchProvince;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public NumberFormat getDecimalFormatter() {
		return decimalFormatter;
	}

	public void setDecimalFormatter(NumberFormat decimalFormatter) {
		this.decimalFormatter = decimalFormatter;
	}

	public UploadedFileWO getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(UploadedFileWO uploadFile) {
		this.uploadFile = uploadFile;
	}
}