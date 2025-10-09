package com.wo.module.report.reportRegulationInternalHistory.task;


import java.io.FileOutputStream;
import java.io.Serializable;
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
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.report.reportGen.constant.ReportSheetNameConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.report.reportRegulationInternalHistory.constant.ReportRegulationInternalHistoryConstant;
import com.wo.module.report.reportRegulationInternalHistory.service.ReportRegulationInternalHistoryService;
import com.wo.module.report.reportRegulationInternalHistory.vo.ReportRegulationInternalHistoryVo;


public class ReportRegulationInternalHistoryTask implements Runnable, Serializable{

	private static final long serialVersionUID = 2978031999504834439L;
	private static final Logger logger = Logger.getLogger(ReportRegulationInternalHistoryTask.class);
	private static final String REPORT_LOG_ACTIVITY = "Report Log Activity Internal";

	private Long reportGenId;
	private String userNikName;

	private ReportGenService reportGenService;
	private ParameterDetailService parameterDetailService;
	private RunnableFacesUtil runnableFacesUtil;
	private ReportRegulationInternalHistoryService reportRegulationInternalHistoryService;
	
	@SuppressWarnings("rawtypes")
	private List<? extends SearchObject> searchCriteria;
	
	private String searchCreationDateFrom;
	private String searchCreationDateTo;
	
	private CommonReportUtil reportUtil = new CommonReportUtil();
	
	@SuppressWarnings("rawtypes")
	public ReportRegulationInternalHistoryTask(Long reportGenId,
			ReportGenService reportGenService,
			ParameterDetailService parameterDetailService,
			RunnableFacesUtil runnableFacesUtil,
			ReportRegulationInternalHistoryService reportRegulationInternalHistoryService,
			List<? extends SearchObject> searchCriteria,
			String userNikName) {
		super();
		this.reportGenId = reportGenId;
		this.reportGenService = reportGenService;
		this.parameterDetailService = parameterDetailService;
		this.runnableFacesUtil = runnableFacesUtil;
		this.reportRegulationInternalHistoryService = reportRegulationInternalHistoryService;
		this.searchCriteria = searchCriteria;
		this.userNikName = userNikName;
	}
	
	@SuppressWarnings("rawtypes")
	private String getSearchCriteriaValue(String col) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				if (!StringUtils.isBlank(col)) {
					if (StringUtils.equals(searchVal.getSearchColumn(), col))
						return searchVal.getSearchValueAsString();
				}
			}
		}

		return "";
	}
	
	private UploadedFileWO uploadFileToApi(String absoluteResultFilePath) throws Exception {
		SCMApiUpload saf = new SCMApiUploadImpl(absoluteResultFilePath, parameterDetailService,
				REPORT_LOG_ACTIVITY);
		saf.upload();
		return saf.getAsUploadedFileWO();
	}
	
	@Override
	public void run() {
		List<Integer> listColumnView = new ArrayList<Integer>();
		UploadedFileWO ufw = null;
		String absoluteResultFilePath = null;
		
		try {
			absoluteResultFilePath = writeToFile(listColumnView);

			ufw = uploadFileToApi(absoluteResultFilePath);

			updateReportGenHistoryAsComplete(reportGenId, ufw);
		} catch (CustomAPIException cae) {
			cae.printStackTrace();
			logger.error("Error while trying to process report", cae);

			try {
				updateReportGenHistoryAsCompleteError(reportGenId, cae.getUfw(), cae.getMessage());
			} catch (Exception e) {
				// there are no other way to handle except log error...
				logger.error("ERror ", e);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Error while trying to process report", ex);

			try {
				updateReportGenHistoryAsCompleteError(reportGenId, ufw, ex.getMessage());
			} catch (Exception e) {
				// there are no other way to handle except log error...
				logger.error("ERror ", e);
			}
		} finally {
			if (ufw != null && ufw.getFile() != null) {
				try {
					ufw.getFile().delete();
				} catch (Exception e) {
					// there are no other way to handle except log error...
					logger.error("Error while deleting temp ", e);
				}
			}
		}	
	}

	private String writeToFile(List<Integer> listColumnView) throws Exception {
		String sheetName1 = ReportSheetNameConstant.SHEET_NAME_REPORT_LOG_ACTIVITY;
		
		String fileNamePrefix = "ReportLogActivityInternal";
		
		String fileName = getReportUtil().generateFileName(fileNamePrefix, userNikName);
		String filePath = getReportUtil().retrieveFilePath(parameterDetailService);
		
		Path dirPath = Paths.get(filePath);
		if (!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}

		XSSFWorkbook workbook = null;
		FileOutputStream fileOutputStream = null;
		
		List<String> buildListColumnNameRow1Sheet1 = buildListColumnNameRow1Sheet1();
		
		List<ReportRegulationInternalHistoryVo> sheet1Results = (List<ReportRegulationInternalHistoryVo>) reportRegulationInternalHistoryService.getReportRegulationIntHistoryDetailAsVo(searchCriteria);

		try {
			fileOutputStream = new FileOutputStream(filePath + fileName);
			workbook = new XSSFWorkbook();

			Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
			getReportUtil().createExcelCellFormat(workbook, mapCellFormat);
			
			writeSheet1(listColumnView, sheetName1, workbook, buildListColumnNameRow1Sheet1, sheet1Results,
					mapCellFormat);
			
//			writeSheet2(sheetName2, filePath, workbook, sheet2Results);
			
			workbook.write(fileOutputStream);
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		}

		return filePath + fileName;
	}
	
	private void writeSheet1(List<Integer> listColumnView, String sheetName1, XSSFWorkbook workbook,
		List<String> buildListColumnNameRow1Sheet1, List<ReportRegulationInternalHistoryVo> sheet1Results,
		Map<String, CellStyle> mapCellFormat) throws Exception {
		
		XSSFSheet sheet1 = workbook.createSheet(sheetName1);
		
		// setting column size [Start]
		if (listColumnView != null) {
			for (int i = 0; i < listColumnView.size(); i++) {
				sheet1.setColumnWidth(i, listColumnView.get(i));
				sheet1.autoSizeColumn(i);
			}
		}
		// setting column size [End]
		
		this.writeExcelHeader(sheet1, mapCellFormat);
		
		CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER);
		int rowIndex = 7;
		
		if (buildListColumnNameRow1Sheet1 != null && !buildListColumnNameRow1Sheet1.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow1Sheet1) {
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);

				columnIndex++;
			}
		}
		
		// write sheet 1 data detail [start]
		if (sheet1Results != null && !sheet1Results.isEmpty()) {
			rowIndex += 1;
			
			int rowNum = 1;
			for (ReportRegulationInternalHistoryVo arrObj : sheet1Results) {
				int columnIndex = 0;
				
				writeObjectToExcelSheet1(sheet1, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);

				rowIndex++;
				rowNum++;
			}
		}
		
		// write sheet 1 data detail [end]
		sheet1.autoSizeColumn(15);
	}
	
	
	private void writeObjectToExcelSheet1(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum,
			ReportRegulationInternalHistoryVo arrObj, int columnIndex) throws Exception{
	
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, rowNum, mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getActivityType(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getActivityDate(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getActivityNote(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getCreatedBy(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getCreatedDate(), mapCellFormat);
	}
	

	
	private List<String> buildListColumnNameRow1Sheet1() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Type Activity");
		listColumnNameTemp.add("Activity Date");
		listColumnNameTemp.add("Activity Note");
		listColumnNameTemp.add("Created Name");
		listColumnNameTemp.add("Created Date");

		return listColumnNameTemp;
	}
	
	public void writeExcelHeader(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat) throws Exception {
		String from = "";
		String to = "";
		
		int row = 0;
		int columnStart = 0;

		SimpleDateFormat printTimestamp = new SimpleDateFormat(CommonConstants.INPUT_DATE_TIME_FORMAT);
		String excelPrintDate = printTimestamp.format(new Date());

		// Create Header [start]
		CellStyle cfHeaderValue = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_VALUE);
		CellStyle cfHeaderLabel = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_LABEL);

		// Create Header [end]

		// Create Header Title [Start]
		if (sheet.getSheetName().equals(ReportSheetNameConstant.SHEET_NAME_REPORT_LOG_USER_DETAIL)) {
			reportUtil.writeCellTitle(sheet, 4, "Report Log Activity Detail", mapCellFormat);
		} else {
			reportUtil.writeCellTitle(sheet, 4, "Report Log Activity Peraturan Internal", mapCellFormat);
		}
		row++;
		row++;
		
		searchCreationDateFrom = getSearchCriteriaValue(ReportRegulationInternalHistoryConstant.WHERE_CREATE_DATE_FROM);
		searchCreationDateTo = getSearchCriteriaValue(ReportRegulationInternalHistoryConstant.WHERE_CREATE_DATE_TO);
		
		if (StringUtils.isNotBlank(searchCreationDateFrom)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchCreationDateFrom);
				from = DateUtil.dateToString(tmpDate);
			} catch (Exception ex) {
				from = "";
			}
		}
		
		if (StringUtils.isNotBlank(searchCreationDateTo)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchCreationDateTo);
				to = DateUtil.dateToString(tmpDate);
			} catch (Exception ex) {
				to = "";
			}
		}
		
		// Label Creation Date
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportLogActivityCreationDate") + " : ", cfHeaderLabel);

		// Value Creation Date
		reportUtil.writeCell(sheet, row, columnStart + 1,
				from + " "
						+ ((StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to))
								? runnableFacesUtil.retrieveMessage("textUntil")
								: "")
						+ " " + to,
				cfHeaderValue);
		row++;
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
	
	private void updateReportGenHistoryAsCompleteError(Long newReportGenId, UploadedFileWO ufw, String errorMsg)
			throws Exception {
		ReportGen editReportGen = reportGenService.findById(newReportGenId);
		editReportGen.setReportGenStatus(CommonConstants.REPORT_GEN_STATUS_COMPLETE_ERR);

		if (ufw != null)
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
		// editReportGen.setReportGenFilePath(absoluteResultFilePath);
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

	public ReportRegulationInternalHistoryService getReportRegulationInternalHistoryService() {
		return reportRegulationInternalHistoryService;
	}

	public void setReportRegulationInternalHistoryService(
			ReportRegulationInternalHistoryService reportRegulationInternalHistoryService) {
		this.reportRegulationInternalHistoryService = reportRegulationInternalHistoryService;
	}

	@SuppressWarnings("rawtypes")
	public List<? extends SearchObject> getSearchCriteria() {
		return searchCriteria;
	}

	@SuppressWarnings("rawtypes")
	public void setSearchCriteria(List<? extends SearchObject> searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public String getSearchCreationDateFrom() {
		return searchCreationDateFrom;
	}

	public void setSearchCreationDateFrom(String searchCreationDateFrom) {
		this.searchCreationDateFrom = searchCreationDateFrom;
	}

	public String getSearchCreationDateTo() {
		return searchCreationDateTo;
	}

	public void setSearchCreationDateTo(String searchCreationDateTo) {
		this.searchCreationDateTo = searchCreationDateTo;
	}

	public CommonReportUtil getReportUtil() {
		return reportUtil;
	}

	public void setReportUtil(CommonReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static String getReportLogActivity() {
		return REPORT_LOG_ACTIVITY;
	}
	

}
