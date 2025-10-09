package com.wo.module.report.reportCpsa.task;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.report.CommonReportUtil;
import com.wo.module.common.util.DateUtil;
import com.wo.module.common.utility.SCMApiUpload;
import com.wo.module.common.utility.SCMApiUploadImpl;
import com.wo.module.lov.bean.RunnableFacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.report.reportCpsa.constant.ReportCpsaConstants;
import com.wo.module.report.reportCpsa.model.ReportCpsa;
import com.wo.module.report.reportCpsa.service.ReportCpsaService;
import com.wo.module.report.reportGen.constant.ReportSheetNameConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.service.ReportGenService;

public class ReportCpsaTask implements Runnable,
	ReportSheetNameConstant, ReportCpsaConstants{

	static Logger logger = Logger.getLogger(ReportCpsaTask.class);
	
	public final static String COMPLIANCE_DOC_TYPE_REPORT_CPSA = "Report CPSA";
	
	private Long reportGenId;
	private String userNikName;
	
	private ReportGenService reportGenService;
	private ParameterDetailService parameterDetailService;
	private RunnableFacesUtil runnableFacesUtil;
	private ReportCpsaService reportCpsaService;
	
	@SuppressWarnings("rawtypes")
	private List<? extends SearchObject> searchCriteria;
	
	private String searchDateFrom;
	private String searchDateTo;
	private String searchTargetDateFrom;
	private String searchTargetDateTo;
	private String searchCpsaType;
	
	private Integer totalCpsaCabang;
	private Integer totalCpsaSyariah;
	private Integer totalCpsaPusat;
	
	private CommonReportUtil reportUtil = new CommonReportUtil();
	
	@SuppressWarnings("rawtypes")
	public ReportCpsaTask(Long reportGenId,
			ReportGenService reportGenService,
			ParameterDetailService parameterDetailService,
			RunnableFacesUtil runnableFacesUtil,
			ReportCpsaService reportCpsaService,
			List<? extends SearchObject> searchCriteria,
			String userNikName) {
		super();
		this.reportGenId = reportGenId;
		this.reportGenService = reportGenService;
		this.parameterDetailService = parameterDetailService;
		this.setRunnableFacesUtil(runnableFacesUtil);
		this.reportCpsaService = reportCpsaService;
		this.searchCriteria = searchCriteria;
		this.userNikName = userNikName;
	}
	
	@SuppressWarnings("rawtypes")
	private String getSearchCriteriaValue(String col) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				if (!StringUtils.isBlank(col)) {
					if(StringUtils.equals(searchVal.getSearchColumn(), col)) {
						return searchVal.getSearchValueAsString();
					}
				}
			}
		}
		
		return "";
	}
	
	@Override
	public void run() {
		List<Integer> listColumnView = new ArrayList<Integer>();
		UploadedFileWO uf = null;
		String absoluteResultFilePath = null;
		
		try {
			
			absoluteResultFilePath = writeToFile(listColumnView);
			
			uf = uploadFileToApi(absoluteResultFilePath);
			
			updateReportGenHistoryAsComplete(reportGenId, uf);
			
		} catch (CustomAPIException e) {
			e.printStackTrace();
			logger.error("Error while trying to process report", e);
			
			try {
				updateReportGenHistoryAsCompleteError(reportGenId, e.getUfw(), e.getMessage());
			} catch (Exception e2) {
				logger.error("Error ", e);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while trying to process report", e);
			
			try {
				updateReportGenHistoryAsCompleteError(reportGenId, uf, e.getMessage());
			} catch (Exception e2) {
				logger.error("Error ", e);
			}
		} finally {
			if (uf != null && uf.getFile() != null) {
				try {
					uf.getFile().delete();
				} catch (Exception e2) {
					logger.error("Error while deleting temp ", e2);
				}
			}
		}
	}

	private UploadedFileWO uploadFileToApi(String absoluteResultFilePath) throws Exception {
		SCMApiUpload saf = new SCMApiUploadImpl(absoluteResultFilePath, 
				parameterDetailService, 
				COMPLIANCE_DOC_TYPE_REPORT_CPSA
			);	
		saf.upload();
		return saf.getAsUploadedFileWO();
	}
	
	private String writeToFile(List<Integer> listColumnView) throws Exception {
		String sheetName1 = SHEET_NAME_REPORT_CPSA;
		
		String fileNamePrefix = "ReportCpsa";
		
		String fileName = getReportUtil().generateFileName(fileNamePrefix, userNikName);
		String filePath = getReportUtil().retrieveFilePath(parameterDetailService);
		
		Path dirPath = Paths.get(filePath);
		if(!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}
		
		XSSFWorkbook workbook = null;
		FileOutputStream fileOutputStream = null;
		
		List<String> buildListColumnNameRow1Sheet1 = buildListColumnNameRow1Sheet1();
		List<String> buildListColumnNameRow2Sheet1 = buildListColumnNameRow2Sheet1();
		
		List<ReportCpsa> sheet1Results = (List<ReportCpsa>)
				reportCpsaService.getReportCpsaByData(searchCriteria);
		
		try {
			
			fileOutputStream = new FileOutputStream(filePath + fileName);
			workbook = new XSSFWorkbook();
			XSSFSheet sheet1 = workbook.createSheet(sheetName1);
			
			// setting column size sheet 1
			if (listColumnView != null) {
				for (int i = 0; i < listColumnView.size(); i++) {
					sheet1.setColumnWidth(i, listColumnView.get(i));
					sheet1.autoSizeColumn(i);
				}
			}
			
			Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
			getReportUtil().createExcelCellFormat(workbook, mapCellFormat);
			
			this.writeExcelHeader(sheet1, mapCellFormat);
			
			CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER);			
			
			// sheet 1 header [start]
			int rowIndex = 8;
			if (buildListColumnNameRow1Sheet1 != null && !buildListColumnNameRow1Sheet1.isEmpty()) {
				int columnIndex = 0;
				
				for (String columnNameAlias : buildListColumnNameRow1Sheet1) {
					
					getReportUtil().writeCell(sheet1, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);	
					
					// merged Area
					if (columnIndex == 2) {
						CellRangeAddress cra = new CellRangeAddress(rowIndex,rowIndex,columnIndex,columnIndex+=1);
						sheet1.addMergedRegion(cra);
						reportUtil.mergedHeaderColumnStyle(cra, sheet1);
					} else {
						sheet1.addMergedRegion(new CellRangeAddress(rowIndex,rowIndex+1,columnIndex,columnIndex));
					}
					
					columnIndex++;
				}
			}
			rowIndex++;
			if (buildListColumnNameRow2Sheet1 != null && !buildListColumnNameRow2Sheet1.isEmpty()) {
				int columnIndex = 0;
				for (String columnNameAlias : buildListColumnNameRow2Sheet1) {
					getReportUtil().writeCell(sheet1, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);
					
					columnIndex++;
				}
			}
			// sheet 1 header [end]
			
			//sheet 1 data [start]
			if (sheet1Results != null && !sheet1Results.isEmpty()) {
				rowIndex += 1;
				int rowNum = 1;
				totalCpsaSyariah = 0;
				totalCpsaCabang = 0;
				totalCpsaPusat = 0;
				for (ReportCpsa arrObj : sheet1Results) {
					int columnIndex = 0;
					
					writeObjectToExcel(sheet1, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);
					
					rowIndex++;
					rowNum++;
				}
			}
			
			this.writeExcelSummary(sheet1,mapCellFormat,rowIndex);
			
			// sheet 1 data [end]			
			
			workbook.write(fileOutputStream);
			
		} catch (Exception e) {
			logger.error(null,e);
			throw e;
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		}
		
		return filePath + fileName;
	}
		
	private void writeObjectToExcel(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum,
			ReportCpsa arrObj, int columnIndex) throws Exception {
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, rowNum, mapCellFormat);
		
		if(arrObj.getCpsaTypeCode() !=null) {
			if(arrObj.getCpsaTypeCode().equals(ReportCpsaConstants.COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_SYARIAH)) {
				totalCpsaSyariah = totalCpsaSyariah+1;			
			}else if(arrObj.getCpsaTypeCode().equals(ReportCpsaConstants.COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_CPSA)) {
				totalCpsaCabang = totalCpsaCabang+1;
			}else if(arrObj.getCpsaTypeCode().equals(ReportCpsaConstants.COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_PUSAT)) {
				totalCpsaPusat = totalCpsaPusat+1;
			}
		}
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				arrObj.getCpsaTypeName(),
				mapCellFormat);		
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				arrObj.getPeriodFromStr(),
				mapCellFormat);		
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				arrObj.getPeriodToStr(),
				mapCellFormat);		
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				arrObj.getLetterNo(),
				mapCellFormat);		
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				arrObj.getLetterDateStr(),
				mapCellFormat);		
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				arrObj.getLetterAbout(),
				mapCellFormat);		
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				arrObj.getDivisionName(),
				mapCellFormat);		
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				arrObj.getBranchName(),
				mapCellFormat);		
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				arrObj.getUserName1(),
				mapCellFormat);		
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				arrObj.getUserName2(),
				mapCellFormat);		
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				arrObj.getUserName3(),
				mapCellFormat);		
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				arrObj.getTargetDateStr(),
				mapCellFormat);		
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				arrObj.getReviewDateStr(),
				mapCellFormat);		
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				arrObj.getCpsaStatusName(),
				mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				arrObj.getCreatedByName(),
				mapCellFormat);		
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				arrObj.getCpsaNote(),
				mapCellFormat);		
		
		if(arrObj.getMeetSla() == 1) {
			getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
					ReportCpsaConstants.MEET_SLA,
					mapCellFormat);
		}else if(arrObj.getBeforeSla() == 1) {
			getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
					ReportCpsaConstants.BEFORE_SLA,
					mapCellFormat);
		}else if(arrObj.getOverSla() == 1) {
			getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
					ReportCpsaConstants.OVER_SLA,
					mapCellFormat);
		}else {
			getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
					ReportCpsaConstants.STRING_EMPTY,
					mapCellFormat);
		}				
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				arrObj.getCreatedDateStr(),
				mapCellFormat);		
		
	}
	
	private List<String> buildListColumnNameRow1Sheet1() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Tipe Pemeriksaan");
		listColumnNameTemp.add("Periode Pemeriksaan");
		listColumnNameTemp.add("No Surat/Memorandum");
		listColumnNameTemp.add("Tanggal Surat");
		listColumnNameTemp.add("Perihal Surat");		
		listColumnNameTemp.add("Unit Kerja");
		listColumnNameTemp.add("Branch/Sub Branch");
		listColumnNameTemp.add("PIC 1");
		listColumnNameTemp.add("PIC 2");
		listColumnNameTemp.add("PIC 3");		
		listColumnNameTemp.add("Target Waktu");
		listColumnNameTemp.add("Tanggal Verifikasi");
		listColumnNameTemp.add("Status Akhir");
		listColumnNameTemp.add("PIC Admin Compliance");
		listColumnNameTemp.add("Note");
		listColumnNameTemp.add("SLA");		
		listColumnNameTemp.add("Tanggal Pembuatan");	
		
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow2Sheet1() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Tanggal Mulai");
		listColumnNameTemp.add("Tanggal Akhir");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		
		return listColumnNameTemp;
	}
	
	public void writeExcelHeader(XSSFSheet sheet,Map<String, CellStyle> mapCellFormat) throws Exception {
		
		String cpsaTypeName = "";
		String dateFrom = "";
		String dateTo = "";
		String targetDateFrom = "";
		String targetDateTo = "";
		
		int row = 0;
		int columnStart = 0;
		
		SimpleDateFormat printTimeStamp = new SimpleDateFormat(CommonConstants.INPUT_DATE_TIME_FORMAT);
		String excelPrintDate = printTimeStamp.format(new Date());
		
		// Create Header [Start]
		CellStyle cfHeaderValue = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_VALUE);
		CellStyle cfHeaderLabel = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_LABEL);
		// Create Header [End]
		
		// Create Header Title [Start]
		reportUtil.writeCellTitle(sheet, 4, 
				"Report Compliance Plan Self Assessment", mapCellFormat);
				
		row++;
		row++;
				
		// Label Creation Date [Start]
		reportUtil.writeCell(sheet, row, columnStart, runnableFacesUtil.retrieveMessage("formReportCpsaCreationDate") + " : ", cfHeaderLabel);
		// Label Creation Date [End]
		
		searchDateFrom = getSearchCriteriaValue(WHERE_CREATED_DATE_FROM);
		searchDateTo = getSearchCriteriaValue(WHERE_CREATED_DATE_TO);
		searchTargetDateFrom = getSearchCriteriaValue(WHERE_TARGET_DATE_FROM);
		searchTargetDateTo = getSearchCriteriaValue(WHERE_TARGET_DATE_TO);
		searchCpsaType = getSearchCriteriaValue(WHERE_CPSA_TYPE_CODE);
		
		// get Name EN and In
		ParameterDetail getCpsaTypeName = parameterDetailService.getParameterDetailByParamDtlCode(searchCpsaType);
		if (getCpsaTypeName != null) {
			cpsaTypeName = runnableFacesUtil.retrieveLocaleMessage(getCpsaTypeName.getNameEn(), getCpsaTypeName.getNameIn());
		}
		
		if (StringUtils.isNotBlank(searchDateFrom)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchDateFrom);
				dateFrom = DateUtil.dateToString(tmpDate);
			} catch (Exception e) {
				dateFrom = "";
			}
		}
		
		if (StringUtils.isNotBlank(searchDateTo)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchDateTo);
				dateTo = DateUtil.dateToString(tmpDate);
			} catch (Exception e) {
				dateTo = "";
			}
		}
		
		if (StringUtils.isNotBlank(searchTargetDateFrom)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchTargetDateFrom);
				targetDateFrom = DateUtil.dateToString(tmpDate);
			} catch (Exception e) {
				targetDateFrom = "";
			}
		}
		
		if (StringUtils.isNotBlank(searchTargetDateTo)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchTargetDateTo);
				targetDateTo = DateUtil.dateToString(tmpDate);
			} catch (Exception e) {
				targetDateTo = "";
			}
		}
		
		reportUtil.writeCell(sheet, row, columnStart + 1, 
				dateFrom + " "
				+ ((StringUtils.isNotBlank(searchDateFrom) && StringUtils.isNotBlank(searchDateTo))
						? runnableFacesUtil.retrieveMessage("textUntil") : "")
				+ " " + dateTo, 
				cfHeaderValue);
		row++;
		
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportCpsaTargetDate") + " : ",
				cfHeaderLabel);
		
		reportUtil.writeCell(sheet, row, columnStart + 1, 
				targetDateFrom + " "
				+ ((StringUtils.isNotBlank(targetDateFrom) && StringUtils.isNotBlank(targetDateTo))
						? runnableFacesUtil.retrieveMessage("textUntil") : "")
				+ " " + targetDateTo, 
				cfHeaderValue);
		row++;
		
		// Label Review Category
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportCpsaCpsaType") + " : ",
				cfHeaderLabel);
				
		// Value Review Category
		reportUtil.writeCell(sheet, row, columnStart + 1, 
				cpsaTypeName,
				cfHeaderValue);
		row++;
		
		// Label Printed By
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("textExcelCommonPrintedBy") + " : ", cfHeaderLabel);
		// Value Printed By
		reportUtil.writeCell(sheet, row, columnStart + 1, userNikName, cfHeaderValue);
				
		row++;

		// Label Printed On
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("textExcelCommonPrintedOn") + " : ", cfHeaderLabel);
		// Value Printed On
		reportUtil.writeCell(sheet, row, columnStart + 1, excelPrintDate, cfHeaderValue);
				
		row++;
		// Create Header Title [End]
	}
	
	public void writeExcelSummary(XSSFSheet sheet,Map<String, CellStyle> mapCellFormat, int rowIndex) throws Exception {
		int row = rowIndex + 1;
		int columnStart = 0;
				
		CellStyle cfHeaderValue = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_VALUE);
		CellStyle cfHeaderLabel = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_LABEL);
		
		reportUtil.writeCell(sheet, row, columnStart, "CPSA Cabang : ", cfHeaderLabel);
		reportUtil.writeCell(sheet, row, columnStart + 1, totalCpsaCabang, cfHeaderValue);
		row++;
		
		reportUtil.writeCell(sheet, row, columnStart, "CPSA Syariah : ", cfHeaderLabel);
		reportUtil.writeCell(sheet, row, columnStart + 1, totalCpsaSyariah, cfHeaderValue);
		row++;
		
		reportUtil.writeCell(sheet, row, columnStart, "CPSA Pusat : ", cfHeaderLabel);
		reportUtil.writeCell(sheet, row, columnStart + 1, totalCpsaPusat, cfHeaderValue);
		row++;
	}
	
	private void updateReportGenHistoryAsCompleteError(Long newReportGenId, UploadedFileWO ufw,String errorMsg) throws Exception {
		ReportGen editReportGen = reportGenService.findById(newReportGenId);
		editReportGen.setReportGenStatus(CommonConstants.REPORT_GEN_STATUS_COMPLETE_ERR);
		
		if(ufw != null)
			editReportGen.setReportGenFile(ufw.getEncodedBase64());
		
		editReportGen.setReportGenStatusMsg(errorMsg);
		reportGenService.update(editReportGen);
	}
	
	private void updateReportGenHistoryAsComplete(Long newReportGenId, UploadedFileWO ufw) throws Exception {
		ReportGen editReportGen = reportGenService.findById(newReportGenId);
		editReportGen.setReportGenStatus(CommonConstants.REPORT_GEN_STATUS_COMPLETE_SUCCESS);
		editReportGen.setReportGenFileId(ufw.getFileId());
		editReportGen.setReportGenFileSize(ufw.getFileSize());
		editReportGen.setReportGenReportFileName(ufw.getFileName());
		reportGenService.update(editReportGen);
	}
	
	public Long getReportGenId() {
		return reportGenId;
	}

	public void setReportGenId(Long reportGenId) {
		this.reportGenId = reportGenId;
	}

	public String getUserNikName() {
		return userNikName;
	}

	public void setUserNikName(String userNikName) {
		this.userNikName = userNikName;
	}

	public ReportGenService getReportGenService() {
		return reportGenService;
	}

	public void setReportGenService(ReportGenService reportGenService) {
		this.reportGenService = reportGenService;
	}

	public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}

	public RunnableFacesUtil getRunnableFacesUtil() {
		return runnableFacesUtil;
	}

	public void setRunnableFacesUtil(RunnableFacesUtil runnableFacesUtil) {
		this.runnableFacesUtil = runnableFacesUtil;
	}

	public ReportCpsaService getReportCpsaService() {
		return reportCpsaService;
	}

	public void setReportCpsaService(ReportCpsaService reportCpsaService) {
		this.reportCpsaService = reportCpsaService;
	}

	@SuppressWarnings("rawtypes")
	public List<? extends SearchObject> getSearchCriteria() {
		return searchCriteria;
	}

	@SuppressWarnings("rawtypes")
	public void setSearchCriteria(List<? extends SearchObject> searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		ReportCpsaTask.logger = logger;
	}

	public String getSearchDateFrom() {
		return searchDateFrom;
	}

	public void setSearchDateFrom(String searchDateFrom) {
		this.searchDateFrom = searchDateFrom;
	}

	public String getSearchDateTo() {
		return searchDateTo;
	}

	public void setSearchDateTo(String searchDateTo) {
		this.searchDateTo = searchDateTo;
	}

	public String getSearchTargetDateFrom() {
		return searchTargetDateFrom;
	}

	public void setSearchTargetDateFrom(String searchTargetDateFrom) {
		this.searchTargetDateFrom = searchTargetDateFrom;
	}

	public String getSearchTargetDateTo() {
		return searchTargetDateTo;
	}

	public void setSearchTargetDateTo(String searchTargetDateTo) {
		this.searchTargetDateTo = searchTargetDateTo;
	}

	public String getSearchCpsaType() {
		return searchCpsaType;
	}

	public void setSearchCpsaType(String searchCpsaType) {
		this.searchCpsaType = searchCpsaType;
	}

	public CommonReportUtil getReportUtil() {
		return reportUtil;
	}

	public void setReportUtil(CommonReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}

	public Integer getTotalCpsaCabang() {
		return totalCpsaCabang;
	}

	public void setTotalCpsaCabang(Integer totalCpsaCabang) {
		this.totalCpsaCabang = totalCpsaCabang;
	}

	public Integer getTotalCpsaSyariah() {
		return totalCpsaSyariah;
	}

	public void setTotalCpsaSyariah(Integer totalCpsaSyariah) {
		this.totalCpsaSyariah = totalCpsaSyariah;
	}

	public Integer getTotalCpsaPusat() {
		return totalCpsaPusat;
	}

	public void setTotalCpsaPusat(Integer totalCpsaPusat) {
		this.totalCpsaPusat = totalCpsaPusat;
	}
}
