package com.wo.module.occupation.bean;

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
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.occupation.constant.OccupationConstants;
import com.wo.module.occupation.service.OccupationService;
import com.wo.module.occupation.vo.OccupationVO;

public class OccupationBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(OccupationBean.class);

	@Autowired
	@Qualifier("occupationService")
	private OccupationService occupationService;
	
	private String searchVal;
	
	private int paging;
	private DBLazyDataModel<OccupationVO> tableModel;

	private FacesUtil facesUtil;
	
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

		tableModel = new DBLazyDataModel<OccupationVO>(occupationService, paging);
		
		fileNameDB = occupationService.getFileName();
		
		PrimeFaces.current().executeScript("reInitSelect2();");
		
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (searchVal != null && !searchVal.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(OccupationConstants.SEARCH_BY_NAME, searchVal));
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
			//uploadFile
			UploadedFile file = event.getFile();
			if(file.getFileName().endsWith(".xls") || file.getFileName().endsWith(".xlsx")) {
				Workbook wb = createWorkbook(file);
				Sheet sheet = wb.getSheetAt(0);
				Iterator<Row> rowIter = sheet.rowIterator();
				
				List<OccupationVO> occupationFileList = new ArrayList<OccupationVO>();
				
				/*skip first row*/
				if(rowIter.hasNext())
					rowIter.next();
				
				/*change excel sheet data to object*/
				boolean uploadFlagError = false;
				//start of data row
				int rowNum = 2;
				
				while(rowIter.hasNext()) {
					Row row = rowIter.next();
					OccupationVO occupationFile = new OccupationVO();
					/*insert if next row have value, break loop if cell have no data*/
					if(!getExcelCellStringValue(row.getCell(0)).isEmpty() &&
							!getExcelCellStringValue(row.getCell(1)).isEmpty()) {
						occupationFile.setOccupation(getExcelCellStringValue(
								row.getCell(OccupationConstants.EXCEL_COL_IDX_OCCUPATION_NAME)));
						occupationFile.setRiskRating(getExcelCellStringValue(
								row.getCell(OccupationConstants.EXCEL_COL_IDX_RISK_RATING)));
						
						occupationFileList.add(occupationFile);
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
				}
				
				if(!uploadFlagError) {
					/*remove any duplicates*/
					Set<String> dupeCheck = new HashSet<String>();
					List<OccupationVO> uniqueOccupationList = new ArrayList<OccupationVO>();
					for(OccupationVO occupationVo : occupationFileList) {
						String occupationName = occupationVo.getOccupation().toUpperCase();
						if(dupeCheck.add(occupationName)) {
							uniqueOccupationList.add(occupationVo);
						}
					}
					
					//create timestamp when button export were press for db log purposes
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
					occupationService.update(occupationFileList, facesUtil.retrieveUserLogin(),
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
			
			List<OccupationVO> occupationSearchList = occupationService.searchData(
					Arrays.asList(
							new DefaultSearchObject(OccupationConstants.SEARCH_BY_NAME, searchVal)), 
					0, Integer.MAX_VALUE, null, null);
			
			//create header
			XSSFRow header = sheet.createRow(0);
			for(int i=0; i<2; i++) {
				XSSFCell cell = header.createCell((short) i);
				switch(i) {
					case 0:
						cell.setCellValue("OCCUPATION");
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
			for(int i=0;i<13;i++) {
				XSSFRow row = sheet.createRow(rowNum);
				for (int x = 0; x < 13; x++) {
					XSSFCell cell = row.createCell((short) x);
					cell.setCellValue("");
				}
				rowNum++;
			}
			//empty/buffer cell END
			
			//fill sheet
			rowNum = 1;
			for(OccupationVO occupationDB : occupationSearchList) {
				XSSFRow row = sheet.createRow(rowNum);
				for(int j = 0; j<2; j++) {
					XSSFCell cell = row.createCell((short) j);
					switch(j) {
						case 0:
							cell.setCellValue(occupationDB.getOccupation() != null ? occupationDB.getOccupation() : "");
							break;
						case 1:
							cell.setCellValue(occupationDB.getRiskRating() != null ? occupationDB.getRiskRating() : "");
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
		searchVal = "";
		search(actionEvent);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		OccupationBean.logger = logger;
	}

	public String getSearchVal() {
		return searchVal;
	}

	public void setSearchVal(String searchVal) {
		this.searchVal = searchVal;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}

	public DBLazyDataModel<OccupationVO> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<OccupationVO> tableModel) {
		this.tableModel = tableModel;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public OccupationService getOccupationService() {
		return occupationService;
	}

	public void setOccupationService(OccupationService occupationService) {
		this.occupationService = occupationService;
	}

	public UploadedFileWO getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFileWO uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String getFileNameDB() {
		return fileNameDB;
	}

	public void setFileNameDB(String fileNameDB) {
		this.fileNameDB = fileNameDB;
	}
	
}