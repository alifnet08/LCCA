package com.wo.module.economySector.bean;

import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.economySector.constant.EconomySectorConstants;
import com.wo.module.economySector.service.EconomySectorService;
import com.wo.module.economySector.vo.EconomySectorVO;
import com.wo.module.lov.bean.FacesUtil;

public class EconomySectorBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(EconomySectorBean.class);

	@Autowired
	@Qualifier("economySectorService")
	private EconomySectorService economySectorService;
	
	private String searchEcoSecCode;
	private String searchEcoSecName;
	
	private int paging;
	private DBLazyDataModel<EconomySectorVO> tableModel;

	public FacesUtil facesUtil;
	
	private UploadedFileWO uploadedFile;
	private String fileNameDB;

	private SimpleDateFormat dateFormatter = new SimpleDateFormat(CommonConstants.INPUT_DATE_FORMAT);
	private NumberFormat decimalFormatter = new DecimalFormat("#.#");
	
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
		
		tableModel = new DBLazyDataModel<EconomySectorVO>(economySectorService, paging);
		
		fileNameDB = economySectorService.getFileName();
		
		PrimeFaces.current().executeScript("reInitScript2()");
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (searchEcoSecCode != null && !searchEcoSecCode.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(
					EconomySectorConstants.SEARCH_BY_CODE, searchEcoSecCode));
		}
		
		if (searchEcoSecName != null && !searchEcoSecName.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(
					EconomySectorConstants.SEARCH_BY_NAME, searchEcoSecName));
		}


		tableModel.setSearchCriteria(searchCriteria);
		
	}
	
	private Workbook createWorkbook(UploadedFile file) throws IOException {
		
		if (file.getFileName().endsWith(".xls")) {
			return new HSSFWorkbook(file.getInputstream());
		} else if (file.getFileName().endsWith(".xlsx")) {
			return new XSSFWorkbook(file.getInputstream());
		} else {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("errorExcelFilesOnly"));
			throw new RuntimeException("Extension not valid");
		}
	}
	
	public void handleFileUpload(FileUploadEvent event) throws Exception {
		try {
			/*get uploaded file from FileUploadEvent*/
			UploadedFile file = event.getFile();
			if(file.getFileName().endsWith(".xls") || file.getFileName().endsWith(".xlsx")) {
				Workbook wb = createWorkbook(file);
				Sheet sheet = wb.getSheetAt(0);
				Iterator<Row> rowIter = sheet.rowIterator();
				
				//Take header value to check if its Maybank spreadsheet or Downloaded spreadsheet
				//UNUSED - PLEASE TELL THEM TO UPLOAD USING THEIR TEMPLATE
//				Row rowHeader = sheet.getRow(0);
//				String headerCellA1 = "";
//				try {
//					if(rowHeader.getCell(0) != null)
//						if(getExcelCellStringValue(rowHeader.getCell(0)).equalsIgnoreCase("code"))
//							headerCellA1 = "CODE TEMPLATE";
//				}catch(NullPointerException ex) {
//					//do nothing
//				}
				
				List<EconomySectorVO> ecoSecFileList = new ArrayList<EconomySectorVO>();
				
				/*skip header row*/
				if(rowIter.hasNext()) 
					rowIter.next();
				
				/*change excel sheet data to object*/
				boolean uploadFlagError = false;
				//start of data row
				int rowNum = 2;
				while(rowIter.hasNext()) {
					Row row = rowIter.next();
					EconomySectorVO ecoSecFile = new EconomySectorVO();
					//UNUSED - UPLOAD AS IS
//					if(headerCellA1.equalsIgnoreCase("CODE TEMPLATE")) {
//						if( !getExcelCellStringValue(row.getCell(1)).equals(StringUtils.EMPTY) &&
//								!getExcelCellStringValue(row.getCell(2)).equals(StringUtils.EMPTY)) {
//							
//							ecoSecFile.setEconomySector(getExcelCellStringValue(
//									row.getCell(EconomySectorConstants.EXCEL_COL_IDX_ECO_SEC_NAME)));
//							ecoSecFile.setRiskRating(getExcelCellStringValue(
//									row.getCell(EconomySectorConstants.EXCEL_COL_IDX_RISK_RATING)));
//							
//							ecoSecFileList.add(ecoSecFile);
//						}else if(getExcelCellStringValue(row.getCell(2)).equals(StringUtils.EMPTY) ){
//							facesUtil.addErrMessage("Missing data at cell C" + rowNum);
//							uploadFlagError = true;
//							break;
//						}else if(getExcelCellStringValue(row.getCell(1)).equals(StringUtils.EMPTY)) {
//							facesUtil.addErrMessage("Missing data at cell B" + rowNum);
//							uploadFlagError = true;
//							break;
//						}
//					}else {
//						if( !getExcelCellStringValue(row.getCell(0)).equals(StringUtils.EMPTY) &&
//								!getExcelCellStringValue(row.getCell(1)).equals(StringUtils.EMPTY)) {
//							
//							ecoSecFile.setEconomySector(getExcelCellStringValue(
//									row.getCell(0)));
//							ecoSecFile.setRiskRating(getExcelCellStringValue(
//									row.getCell(1)));
//							
//							ecoSecFileList.add(ecoSecFile);
//						}else if(getExcelCellStringValue(row.getCell(1)).equals(StringUtils.EMPTY) ){
//							facesUtil.addErrMessage("Missing data at cell B" + rowNum);
//							uploadFlagError = true;
//							break;
//						}else if(getExcelCellStringValue(row.getCell(0)).equals(StringUtils.EMPTY)) {
//							facesUtil.addErrMessage("Missing data at cell A" + rowNum);
//							uploadFlagError = true;
//							break;
//						}
					
					if( !getExcelCellStringValue(row.getCell(0)).isEmpty() &&
							!getExcelCellStringValue(row.getCell(1)).isEmpty()) {
						ecoSecFile.setEconomySector(getExcelCellStringValue(
								row.getCell(0)));
						ecoSecFile.setRiskRating(getExcelCellStringValue(
								row.getCell(1)));
						
						ecoSecFileList.add(ecoSecFile);
					}else if(getExcelCellStringValue(row.getCell(1)).isEmpty() &&
								!getExcelCellStringValue(row.getCell(0)).isEmpty()){
						facesUtil.addErrMessage("Missing data at cell B" + rowNum);
						uploadFlagError = true;
						break;
					}else if(getExcelCellStringValue(row.getCell(0)).isEmpty() &&
								!getExcelCellStringValue(row.getCell(1)).isEmpty()) {
						facesUtil.addErrMessage("Missing data at cell A" + rowNum);
						uploadFlagError = true;
						break;
					}
					rowNum++;
				}
					
				
				if(!uploadFlagError) {
					/*remove any duplicates - UNUSED (UPLOAD AS IS)*/
//					Set<String> dupeCheck = new HashSet<String>();
//					List<EconomySectorVO> uniqueEconomySectorList = new ArrayList<EconomySectorVO>();
//					for(EconomySectorVO ecomSectorVo : ecoSecFileList) {
//						String economySectorName = ecomSectorVo.getEconomySector().toUpperCase();
//						if(dupeCheck.add(economySectorName)) {
//							uniqueEconomySectorList.add(ecomSectorVo);
//						}
//					}
					
					Timestamp buttonPressedDate = new Timestamp(System.currentTimeMillis());
					//save file name since user want same file name when exported
					String fileName = file.getFileName();
					if (fileName.endsWith(".xls")) {
						fileName = fileName.substring(0, fileName.length() - 4);
						System.out.println(fileName);
					}else if (fileName.endsWith(".xlsx")) {
						fileName = fileName.substring(0, fileName.length() - 5);
					}
					
					/*save / update*/
					economySectorService.update(ecoSecFileList, facesUtil.retrieveUserLogin(),
							fileName, buttonPressedDate);
					
					setFileNameDB(fileName);
					
					facesUtil.addSuccessMsg(facesUtil.retrieveMessage("textUploadSuccess"));
				}
			}else {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("errorExcelFilesOnly"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	private String getExcelCellStringValue(Cell cell) {
		String value = null;
		
		if(cell == null) {
			return StringUtils.EMPTY;
		}
		
		switch(cell.getCellType()) {
		case STRING:
			value = cell.getStringCellValue();
			break;
		case BLANK:
			value = StringUtils.EMPTY;
			break;
		case ERROR:
			value = StringUtils.EMPTY;
			break;
		case NUMERIC:
			if(DateUtil.isCellDateFormatted(cell)) {
				value = dateFormatter.format(cell.getDateCellValue());
			}else {
				value = decimalFormatter.format(cell.getNumericCellValue());
			}
			break;
		case BOOLEAN:
			value = String.valueOf(cell.getBooleanCellValue());
			break;
		case FORMULA:
			try {
				value = cell.getStringCellValue();
			}catch(Exception ex) {
				value = StringUtils.EMPTY;
			}
		default:
			value = StringUtils.EMPTY;
			break;
		}
		return value;
	}
	
	public void postProcessXLS(Object document) {
		try {
			
			XSSFWorkbook wb = (XSSFWorkbook) document;
			XSSFSheet sheet = wb.getSheetAt(0);
			
			List<EconomySectorVO> ecoSecSearchList = economySectorService.searchData(
					Arrays.asList(
							new DefaultSearchObject(EconomySectorConstants.SEARCH_BY_CODE, searchEcoSecCode),
							new DefaultSearchObject(EconomySectorConstants.SEARCH_BY_NAME, searchEcoSecName)), 
					0, Integer.MAX_VALUE, null, null);
			
			//create header
			XSSFRow header = sheet.createRow(0);
			for(int i=0; i<2; i++) {
				XSSFCell cell = header.createCell((short) i);
				switch(i) {
					case 0:
						cell.setCellValue("ECONOMY SECTOR");				
						break;
					case 1:
						cell.setCellValue("RISK RATING");				
						break;
					default:
						break;
				}
			}
			//create header END
			
			//empty/buffer cell
			int rowNum = 1;
			for(int i=0;i<ecoSecSearchList.size();i++) {
				XSSFRow row = sheet.createRow(rowNum);
				for (int x = 0; x < 3; x++) {
					XSSFCell cell = row.createCell((short) x);
					cell.setCellValue("");
				}
				rowNum++;
			}
			//empty/buffer cell END
			
			//fill sheet
			rowNum = 1;
			for(EconomySectorVO ecoSecDB : ecoSecSearchList) {
				XSSFRow row = sheet.createRow(rowNum);
				for(int j = 0; j<3; j++) {
					XSSFCell cell = row.createCell((short) j);
					switch(j) {
						case 0:
							cell.setCellValue(ecoSecDB.getEconomySector() != null ? ecoSecDB.getEconomySector() : "");
							break;
						case 1:
							cell.setCellValue(ecoSecDB.getRiskRating() != null ? ecoSecDB.getRiskRating() : "");
							break;
						default:
							break;
					}
				}
				rowNum++;
			}
			//fill sheet END
			
			//style header
			XSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFillForegroundColor(new XSSFColor(Color.YELLOW));
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
				XSSFCell cell = header.getCell(i);
				cell.setCellStyle(cellStyle);
				sheet.autoSizeColumn(i);
			}
			//style header END
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public void reset(ActionEvent actionEvent) {
		searchEcoSecName = null;
		//searchEcoSecCode = "";
		search(actionEvent);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		EconomySectorBean.logger = logger;
	}

	public String getSearchEcoSecCode() {
		return searchEcoSecCode;
	}

	public void setSearchEcoSecCode(String searchEcoSecCode) {
		this.searchEcoSecCode = searchEcoSecCode;
	}

	public String getSearchEcoSecName() {
		return searchEcoSecName;
	}

	public void setSearchEcoSecName(String searchEcoSecName) {
		this.searchEcoSecName = searchEcoSecName;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}

	public DBLazyDataModel<EconomySectorVO> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<EconomySectorVO> tableModel) {
		this.tableModel = tableModel;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public EconomySectorService getEconomySectorService() {
		return economySectorService;
	}

	public void setEconomySectorService(EconomySectorService economySectorService) {
		this.economySectorService = economySectorService;
	}

	public String getFileNameDB() {
		return fileNameDB;
	}

	public void setFileNameDB(String fileNameDB) {
		this.fileNameDB = fileNameDB;
	}

	public UploadedFileWO getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFileWO uploadedFile) {
		this.uploadedFile = uploadedFile;
	}	
	
	
}