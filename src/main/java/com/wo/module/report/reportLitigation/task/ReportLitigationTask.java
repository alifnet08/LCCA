package com.wo.module.report.reportLitigation.task;

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
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
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
import com.wo.module.report.reportLitigation.constant.ReportLitigationConstant;
import com.wo.module.report.reportLitigation.service.ReportLitigationService;
import com.wo.module.report.reportLitigation.vo.ReportLitiPailitPKPUVo;
import com.wo.module.report.reportLitigation.vo.ReportLitiPerdataPenggugatVo;
import com.wo.module.report.reportLitigation.vo.ReportLitiPerdataTergugatVo;
import com.wo.module.report.reportLitigation.vo.ReportLitiPidanaPelaporVo;
import com.wo.module.report.reportLitigation.vo.ReportLitiPidanaTerlaporVo;

public class ReportLitigationTask implements Serializable, Runnable{

	private static final long serialVersionUID = 3580557850875371097L;
	private static final Logger logger = Logger.getLogger(ReportLitigationTask.class);
	private static final String REPORT_LITIGATION = "Report Litigation";

	private Long reportGenId;
	private String userNikName;
	private String documentTypeName;

	private ReportGenService reportGenService;
	private ParameterDetailService parameterDetailService;
	private RunnableFacesUtil runnableFacesUtil;
	private ReportLitigationService reportLitigationService;
	
	@SuppressWarnings("rawtypes")
	private List<? extends SearchObject> searchCriteria;
	
	private String searchCaseType;
	private String searchDebitur;
	private String searchNoCase;
	private String searchProgress;
	private String searchPutusan;
	private String searchUpayaHukum;
	private String searchProvType;
	
	private CommonReportUtil reportUtil = new CommonReportUtil();
	
	@SuppressWarnings("rawtypes")
	public ReportLitigationTask(Long reportGenId,
			ReportGenService reportGenService,
			ParameterDetailService parameterDetailService,
			RunnableFacesUtil runnableFacesUtil,
			ReportLitigationService reportLitigationService,
			List<? extends SearchObject> searchCriteria,
			String userNikName,
			String documentTypeName) {
		super();
		this.reportGenId = reportGenId;
		this.reportGenService =  reportGenService;
		this.parameterDetailService = parameterDetailService;
		this.runnableFacesUtil = runnableFacesUtil;
		this.reportLitigationService = reportLitigationService;
		this.searchCriteria = searchCriteria;
		this.userNikName = userNikName;
		this.documentTypeName = documentTypeName;
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
		SCMApiUpload saf = new SCMApiUploadImpl(absoluteResultFilePath, parameterDetailService, REPORT_LITIGATION);
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
				logger.error("ERror ", e);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Error while trying to process report", ex);

			try {
				updateReportGenHistoryAsCompleteError(reportGenId, ufw, ex.getMessage());
			} catch (Exception e) {
				logger.error("ERror ", e);
			}
		} finally {
			if (ufw != null && ufw.getFile() != null) {
				try {
					ufw.getFile().delete();
				} catch (Exception e) {
					logger.error("Error while deleting temp ", e);
				}
			}
		}
	}
	
	private String writeToFile(List<Integer> listColumnView) throws Exception {
		String sheetName1 = ReportSheetNameConstant.SHEET_NAME_REPORT_LITIGATION_PERDATA_TERGUGAT;
		String sheetName2 = ReportSheetNameConstant.SHEET_NAME_REPORT_LITIGATION_PERDATA_PENGGUGAT;
		String sheetName3 = ReportSheetNameConstant.SHEET_NAME_REPORT_LITIGATION_KEPAILITAN_PKPU;
		String sheetName4 = ReportSheetNameConstant.SHEET_NAME_REPORT_LITIGATION_PIDANA_PELAPOR;
		String sheetName5 = ReportSheetNameConstant.SHEET_NAME_REPORT_LITIGATION_PIDANA_TERLAPOR;

		String fileNamePrefix = "ReportLitigationDetail";

		String fileName = getReportUtil().generateFileName(fileNamePrefix, userNikName);
		String filePath = getReportUtil().retrieveFilePath(parameterDetailService);

		Path dirPath = Paths.get(filePath);
		if (!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}

		XSSFWorkbook workbook = null;
		FileOutputStream fileOutputStream = null;

		List<String> buildListColumnNameRow1Sheet1 = buildListColumnNameRow1Sheet1();
		List<String> buildListColumnNameRow2Sheet1 = buildListColumnNameRow2Sheet1();
		List<String> buildListColumnNameRow3Sheet1 = buildListColumnNameRow3Sheet1();
		List<String> buildListColumnNameRow4Sheet1 = buildListColumnNameRow4Sheet1();
		List<String> buildListColumnNameRow5Sheet1 = buildListColumnNameRow5Sheet1();
		List<String> buildListColumnNameRow1Sheet2 = buildListColumnNameRow1Sheet2();
		List<String> buildListColumnNameRow2Sheet2 = buildListColumnNameRow2Sheet2();
		List<String> buildListColumnNameRow3Sheet2 = buildListColumnNameRow3Sheet2();
		List<String> buildListColumnNameRow1Sheet3 = buildListColumnNameRow1Sheet3();
		List<String> buildListColumnNameRow2Sheet3 = buildListColumnNameRow2Sheet3();
		List<String> buildListColumnNameRow3Sheet3 = buildListColumnNameRow3Sheet3();
		List<String> buildListColumnNameRow1Sheet4 = buildListColumnNameRow1Sheet4();
		List<String> buildListColumnNameRow2Sheet4 = buildListColumnNameRow2Sheet4();
		List<String> buildListColumnNameRow3Sheet4 = buildListColumnNameRow3Sheet4();
		List<String> buildListColumnNameRow4Sheet4 = buildListColumnNameRow4Sheet4();
		List<String> buildListColumnNameRow1Sheet5 = buildListColumnNameRow1Sheet5();
		List<String> buildListColumnNameRow2Sheet5 = buildListColumnNameRow2Sheet5();
		List<String> buildListColumnNameRow3Sheet5 = buildListColumnNameRow3Sheet5();
		List<String> buildListColumnNameRow4Sheet5 = buildListColumnNameRow4Sheet5();

		List<ReportLitiPerdataTergugatVo> sheet1Results = reportLitigationService.getReportLitigationPerDataTergugatAsVo(searchCriteria);
		List<ReportLitiPerdataPenggugatVo> sheet2Results = reportLitigationService.getReportLitigationPerDataPenggugatAsVo(searchCriteria);
		List<ReportLitiPailitPKPUVo> sheet3Results = reportLitigationService.getReportLitigationKepailitanDanPKPUAsVo(searchCriteria);
		List<ReportLitiPidanaPelaporVo> sheet4Results = reportLitigationService.getReportLitigationPidanaPelaporAsVo(searchCriteria);
		List<ReportLitiPidanaTerlaporVo> sheet5Results = reportLitigationService.getReportLitigationPidanaTerlaporAsVo(searchCriteria);
		
		try {
			fileOutputStream = new FileOutputStream(filePath + fileName);
			workbook = new XSSFWorkbook();

			writeSheetPerdataTergutat(workbook, listColumnView, buildListColumnNameRow1Sheet1, buildListColumnNameRow2Sheet1, buildListColumnNameRow3Sheet1, buildListColumnNameRow4Sheet1, buildListColumnNameRow5Sheet1, sheet1Results, sheetName1);
			writeSheetPerdataPenggugat(workbook, listColumnView, buildListColumnNameRow1Sheet2, buildListColumnNameRow2Sheet2, buildListColumnNameRow3Sheet2, sheet2Results, sheetName2);
			writeSheetKepailitinPKPU(workbook, listColumnView, buildListColumnNameRow1Sheet3, buildListColumnNameRow2Sheet3, buildListColumnNameRow3Sheet3, sheet3Results, sheetName3);
			writeSheetPidanaPelapor(workbook, listColumnView, buildListColumnNameRow1Sheet4, buildListColumnNameRow2Sheet4, buildListColumnNameRow3Sheet4, buildListColumnNameRow4Sheet4, sheet4Results, sheetName4);
			writeSheetPidanaTerlapor(workbook, listColumnView, buildListColumnNameRow1Sheet5, buildListColumnNameRow2Sheet5, buildListColumnNameRow3Sheet5, buildListColumnNameRow4Sheet5, sheet5Results, sheetName5);
			
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
	
	private void writeSheetPerdataTergutat(XSSFWorkbook workbook, List<Integer> listColumnView, 
			List<String> buildListColumnNameRow1Sheet1, List<String> buildListColumnNameRow2Sheet1, List<String> buildListColumnNameRow3Sheet1,
			List<String> buildListColumnNameRow4Sheet1, List<String> buildListColumnNameRow5Sheet1,
			List<ReportLitiPerdataTergugatVo> sheet1Results, String sheetName1) throws Exception {
		XSSFSheet sheet1 = workbook.createSheet(sheetName1);
		
		// setting column size
		if (listColumnView != null) {
			for (int i = 0; i < listColumnView.size(); i++) {
				sheet1.setColumnWidth(i, listColumnView.get(i));
				sheet1.autoSizeColumn(i);
			}
		}
		
		Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
		getReportUtil().createExcelCellFormat(workbook, mapCellFormat);
		
		this.writeExcelHeader(sheet1, mapCellFormat);
		
		CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER_GREEN);
		
		int rowIndex = 10;
		
		if (buildListColumnNameRow1Sheet1 != null && !buildListColumnNameRow1Sheet1.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow1Sheet1) {
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);
				
				if (columnIndex == 1 || columnIndex == 29) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex+1, columnIndex, columnIndex+=1);
					sheet1.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet1);
				} else if (columnIndex == 4) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=3);
					sheet1.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet1);
				} else if (columnIndex == 8) { 
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=2);
					sheet1.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet1);
				} else if (columnIndex == 11 || columnIndex == 13 || columnIndex == 16) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=1);
					sheet1.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet1);
				} else if (columnIndex == 24) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=4);
					sheet1.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet1);
				} else if (columnIndex == 33) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=2);
					sheet1.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet1);
				} else if (columnIndex == 36) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=18);
					sheet1.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet1);
				} else {
					sheet1.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex+4, columnIndex, columnIndex));
				}
				
				columnIndex++;
			}
		}
		rowIndex = 11;
		
		if (buildListColumnNameRow2Sheet1 != null && !buildListColumnNameRow2Sheet1.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow2Sheet1) {
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);
				
				if (columnIndex == 24) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=2);
					sheet1.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet1);
				} else if (columnIndex == 36) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=8);
					sheet1.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet1);
				} else if (columnIndex == 45) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=9);
					sheet1.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet1);
				} else if (columnIndex == 4 || columnIndex == 5 || columnIndex == 6 || columnIndex == 7 || columnIndex == 8
						|| columnIndex == 9 || columnIndex == 10 || columnIndex == 11 || columnIndex == 12) {
					sheet1.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex+3, columnIndex, columnIndex));
				} else if (columnIndex == 13 || columnIndex == 14 || columnIndex == 16 || columnIndex == 17
						|| columnIndex == 33 || columnIndex == 34 || columnIndex == 35) {
					sheet1.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex+1, columnIndex, columnIndex));
				}
				
				columnIndex++;
			}
		}
		rowIndex = 12;
		
		if (buildListColumnNameRow3Sheet1 != null && !buildListColumnNameRow3Sheet1.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow3Sheet1) {
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);
				
				if (columnIndex == 1 || columnIndex == 2) {
					sheet1.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex+2, columnIndex, columnIndex));
				} else if (columnIndex == 25) {
					sheet1.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=1));
				} else if (columnIndex == 53) {
					sheet1.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex+2, columnIndex, columnIndex+=1));
				}
				
				columnIndex++;
			}
		}
		rowIndex = 13;
		
		if (buildListColumnNameRow4Sheet1 != null && !buildListColumnNameRow4Sheet1.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow4Sheet1) {
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);
				
				columnIndex++;
			}
		}
		rowIndex = 14;
		
		if (buildListColumnNameRow5Sheet1 != null && !buildListColumnNameRow5Sheet1.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow5Sheet1) {
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);
				
				if (columnIndex == 13) {
					sheet1.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=1));
				} else if (columnIndex == 16) {
					sheet1.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=1));
				} else if (columnIndex == 33) {
					sheet1.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=2));
				} else if (columnIndex == 36) {
					sheet1.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=7));
				} else if (columnIndex == 44) {
					sheet1.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=1));
				} else if (columnIndex == 46) {
					sheet1.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=6));
				}
				
				columnIndex++;
			}
		}
		
		if (sheet1Results != null && !sheet1Results.isEmpty()) {
			rowIndex += 1;
			int rowNum = 1;
			
			for (ReportLitiPerdataTergugatVo arrObj : sheet1Results) {
				int columnIndex = 0;
				
				writeObjectToExcelPerdataTergugat(sheet1, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);
				
				rowIndex++;
				rowNum++;
			}
		}
	}
	
	private void writeSheetPerdataPenggugat(XSSFWorkbook workbook, List<Integer> listColumnView, 
			List<String> buildListColumnNameRow1Sheet2, List<String> buildListColumnNameRow2Sheet2, List<String> buildListColumnNameRow3Sheet2,
			List<ReportLitiPerdataPenggugatVo> sheet2Results, String sheetName2) throws Exception {
		XSSFSheet sheet2 = workbook.createSheet(sheetName2);
		
		// setting column size
		if (listColumnView != null) {
			for (int i = 0; i < listColumnView.size(); i++) {
				sheet2.setColumnWidth(i, listColumnView.get(i));
				sheet2.autoSizeColumn(i);
			}
		}
		
		Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
		getReportUtil().createExcelCellFormat(workbook, mapCellFormat);
		
		this.writeExcelHeader(sheet2, mapCellFormat);
		
		CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER_GREEN);
		
		int rowIndex = 10;
		
		if (buildListColumnNameRow1Sheet2 != null && !buildListColumnNameRow1Sheet2.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow1Sheet2) {
				getReportUtil().writeCell(sheet2, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);
				
				if (columnIndex == 1) {
					
				} else if (columnIndex == 2) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=3);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else if (columnIndex == 6) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=2);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else if (columnIndex == 9) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=1);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else if (columnIndex == 11) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=1);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else if (columnIndex == 13) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=1);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else if (columnIndex == 17) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=3);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else if (columnIndex == 23) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=12);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else {
					sheet2.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex+2, columnIndex, columnIndex));
				}
				
				columnIndex++;
			}
		}
		rowIndex = 11;
		
		if (buildListColumnNameRow2Sheet2 != null && !buildListColumnNameRow2Sheet2.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow2Sheet2) {
				getReportUtil().writeCell(sheet2, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);
				
				if (columnIndex == 17) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=1);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else if (columnIndex == 23) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=5);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else if (columnIndex == 29) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=6);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else if (columnIndex == 1 || columnIndex == 2 || columnIndex == 3 || columnIndex == 4
						|| columnIndex == 5 || columnIndex == 6 || columnIndex == 7 || columnIndex == 8
						|| columnIndex == 9 || columnIndex == 10 || columnIndex == 11 || columnIndex == 12
						|| columnIndex == 13 || columnIndex == 14) {
					sheet2.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex+1, columnIndex, columnIndex));
				}
				
				columnIndex++;
			}
		}
		rowIndex = 12;
		
		if (buildListColumnNameRow3Sheet2 != null && !buildListColumnNameRow3Sheet2.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow3Sheet2) {
				getReportUtil().writeCell(sheet2, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);
				
				columnIndex++;
			}
		}
		
		if (sheet2Results != null && !sheet2Results.isEmpty()) {
			rowIndex += 1;
			int rowNum = 1;
			
			for (ReportLitiPerdataPenggugatVo arrObj : sheet2Results) {
				int columnIndex = 0;
				
				writeObjectToExcelPerdataPenggugat(sheet2, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);
				
				rowIndex++;
				rowNum++;
			}
		}
	}
	
	private void writeSheetKepailitinPKPU(XSSFWorkbook workbook, List<Integer> listColumnView, 
			List<String> buildListColumnNameRow1Sheet3, List<String> buildListColumnNameRow2Sheet3, List<String> buildListColumnNameRow3Sheet3,
			List<ReportLitiPailitPKPUVo> sheet3Results, String sheetName3) throws Exception {
		XSSFSheet sheet3 = workbook.createSheet(sheetName3);
		
		// setting column size
		if (listColumnView != null) {
			for (int i = 0; i < listColumnView.size(); i++) {
				sheet3.setColumnWidth(i, listColumnView.get(i));
				sheet3.autoSizeColumn(i);
			}
		}
		
		Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
		getReportUtil().createExcelCellFormat(workbook, mapCellFormat);
		
		this.writeExcelHeader(sheet3, mapCellFormat);
		
		CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER_GREEN);
		
		int rowIndex = 10;
		
		if (buildListColumnNameRow1Sheet3 != null && !buildListColumnNameRow1Sheet3.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow1Sheet3) {
				getReportUtil().writeCell(sheet3, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);
				
				if (columnIndex == 1 || columnIndex == 6) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=1);
					sheet3.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet3);
				} else if (columnIndex == 3) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=2);
					sheet3.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet3);
				} else if (columnIndex == 8) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=1);
					sheet3.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet3);
				} else if (columnIndex == 12) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex+1, columnIndex, columnIndex+=1);
					sheet3.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet3);
				} else if (columnIndex == 16) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=3);
					sheet3.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet3);
				} else {
					sheet3.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex+2, columnIndex, columnIndex));
				}
				
				columnIndex++;
			}
		}
		rowIndex = 11;
		
		if (buildListColumnNameRow2Sheet3 != null && !buildListColumnNameRow2Sheet3.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow2Sheet3) {
				getReportUtil().writeCell(sheet3, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);
				
				if (columnIndex == 1 || columnIndex == 2 || columnIndex == 3 || columnIndex == 4
						|| columnIndex == 5 || columnIndex == 6 || columnIndex == 7 || columnIndex == 8
						|| columnIndex == 9 || columnIndex == 16 || columnIndex == 17 || columnIndex == 18 
						|| columnIndex == 19) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex+1, columnIndex, columnIndex);
					sheet3.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet3);
				}
				
				columnIndex++;
			}
		}
		rowIndex = 12;
		
		if (buildListColumnNameRow3Sheet3 != null && !buildListColumnNameRow3Sheet3.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow3Sheet3) {
				getReportUtil().writeCell(sheet3, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);
				
				columnIndex++;
			}
		}
		
		if (sheet3Results != null && !sheet3Results.isEmpty()) {
			rowIndex += 1;
			int rowNum = 1;
			
			for (ReportLitiPailitPKPUVo arrObj : sheet3Results) {
				int columnIndex = 0;
				
				writeObjectToExcelPalilitKPU(sheet3, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);
				
				rowIndex++;
				rowNum++;
			}
		}
	}
	
	private void writeSheetPidanaPelapor(XSSFWorkbook workbook, List<Integer> listColumnView, 
			List<String> buildListColumnNameRow1Sheet4, List<String> buildListColumnNameRow2Sheet4, List<String> buildListColumnNameRow3Sheet4,
			List<String> buildListColumnNameRow4Sheet4,
			List<ReportLitiPidanaPelaporVo> sheet4Results, String sheetName4) throws Exception {
		XSSFSheet sheet4 = workbook.createSheet(sheetName4);
		
		// setting column size
		if (listColumnView != null) {
			for (int i = 0; i < listColumnView.size(); i++) {
				sheet4.setColumnWidth(i, listColumnView.get(i));
				sheet4.autoSizeColumn(i);
			}
		}
		
		Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
		getReportUtil().createExcelCellFormat(workbook, mapCellFormat);
		
		this.writeExcelHeader(sheet4, mapCellFormat);
		
		CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER_GREEN);
		
		int rowIndex = 10;
		
		if (buildListColumnNameRow1Sheet4 != null && !buildListColumnNameRow1Sheet4.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow1Sheet4) {
				getReportUtil().writeCell(sheet4, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);
		
				if (columnIndex == 1 || columnIndex == 8) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex+1, columnIndex, columnIndex+=1);
					sheet4.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet4);
				} else if (columnIndex == 3) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex+1, columnIndex, columnIndex+=4);
					sheet4.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet4);
				} else if (columnIndex == 10 || columnIndex == 11 || columnIndex == 22 || columnIndex == 23) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex+2, columnIndex, columnIndex);
					sheet4.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet4);
				} else if (columnIndex == 17) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=3);
					sheet4.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet4);
				} else if (columnIndex == 15) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=1);
					sheet4.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet4);
				} else {
					sheet4.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex+3, columnIndex, columnIndex));
				}
				
				columnIndex++;
			}
		}
		rowIndex = 11;
		
		if (buildListColumnNameRow2Sheet4 != null && !buildListColumnNameRow2Sheet4.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow2Sheet4) {
				getReportUtil().writeCell(sheet4, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);
				
				if (columnIndex == 15 || columnIndex == 16 || columnIndex == 17 || columnIndex == 18
						|| columnIndex == 19 || columnIndex == 20) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex+1, columnIndex, columnIndex);
					sheet4.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet4);
				}
				
				columnIndex++;
			}
		}
		rowIndex = 12;
		
		if (buildListColumnNameRow3Sheet4 != null && !buildListColumnNameRow3Sheet4.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow3Sheet4) {
				getReportUtil().writeCell(sheet4, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);
				
				if (columnIndex == 1 || columnIndex == 2 || columnIndex == 3 || columnIndex == 4
						|| columnIndex == 5 || columnIndex == 6 || columnIndex == 7 || columnIndex == 8 
						|| columnIndex == 9) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex+1, columnIndex, columnIndex);
					sheet4.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet4);
				}
				
				columnIndex++;
			}
		}
		rowIndex = 13;
		
		if (buildListColumnNameRow4Sheet4 != null && !buildListColumnNameRow4Sheet4.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow4Sheet4) {
				getReportUtil().writeCell(sheet4, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);
				
				columnIndex++;
			}
		}
		
		if (sheet4Results != null && !sheet4Results.isEmpty()) {
			rowIndex += 1;
			int rowNum = 1;
			
			for (ReportLitiPidanaPelaporVo arrObj : sheet4Results) {
				int columnIndex = 0;
				
				writeObjectToExcelPerdataPenggugat(sheet4, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);
				
				rowIndex++;
				rowNum++;
			}
		}
	}
	
	private void writeSheetPidanaTerlapor(XSSFWorkbook workbook, List<Integer> listColumnView, 
			List<String> buildListColumnNameRow1Sheet5, List<String> buildListColumnNameRow2Sheet5, List<String> buildListColumnNameRow3Sheet5,
			List<String> buildListColumnNameRow4Sheet5,
			List<ReportLitiPidanaTerlaporVo> sheet5Results, String sheetName5) throws Exception {
		XSSFSheet sheet5 = workbook.createSheet(sheetName5);
		
		// setting column size
		if (listColumnView != null) {
			for (int i = 0; i < listColumnView.size(); i++) {
				sheet5.setColumnWidth(i, listColumnView.get(i));
				sheet5.autoSizeColumn(i);
			}
		}
		
		Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
		getReportUtil().createExcelCellFormat(workbook, mapCellFormat);
		
		this.writeExcelHeader(sheet5, mapCellFormat);
		
		CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER_GREEN);
		
		int rowIndex = 10;
		
		if (buildListColumnNameRow1Sheet5 != null && !buildListColumnNameRow1Sheet5.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow1Sheet5) {
				getReportUtil().writeCell(sheet5, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);
		
				if (columnIndex == 1) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex+1, columnIndex, columnIndex);
					sheet5.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet5);
				} else if (columnIndex == 2) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex+1, columnIndex, columnIndex+=3);
					sheet5.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet5);
				} else if (columnIndex == 6) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex+1, columnIndex, columnIndex+=1);
					sheet5.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet5);
				} else if (columnIndex == 13) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=1);
					sheet5.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet5);
				} else if (columnIndex == 8 || columnIndex == 9 || columnIndex == 20 || columnIndex == 21) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex+2, columnIndex, columnIndex);
					sheet5.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet5);
				} else if (columnIndex == 15) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex+=3);
					sheet5.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet5);
				} else {
					sheet5.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex+3, columnIndex, columnIndex));
				}
				
				columnIndex++;
			}
		}
		rowIndex = 11;
		
		if (buildListColumnNameRow2Sheet5 != null && !buildListColumnNameRow2Sheet5.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow2Sheet5) {
				getReportUtil().writeCell(sheet5, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);
				
				if (columnIndex == 13 || columnIndex == 14 || columnIndex == 15 || columnIndex == 16
						|| columnIndex == 17 || columnIndex == 18) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex+1, columnIndex, columnIndex);
					sheet5.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet5);
				}
				
				columnIndex++;
			}
		}
		rowIndex = 12;
		
		if (buildListColumnNameRow3Sheet5 != null && !buildListColumnNameRow3Sheet5.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow3Sheet5) {
				getReportUtil().writeCell(sheet5, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);
				
				if (columnIndex == 1 || columnIndex == 2 || columnIndex == 3 || columnIndex == 4
						|| columnIndex == 5 || columnIndex == 6 || columnIndex == 7) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex+1, columnIndex, columnIndex);
					sheet5.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet5);
				}
				
				columnIndex++;
			}
		}
		rowIndex = 13;
		
		if (buildListColumnNameRow4Sheet5 != null && !buildListColumnNameRow4Sheet5.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow4Sheet5) {
				getReportUtil().writeCell(sheet5, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);
				
				columnIndex++;
			}
		}
		
		if (sheet5Results != null && !sheet5Results.isEmpty()) {
			rowIndex += 1;
			int rowNum = 1;
			
			for (ReportLitiPidanaTerlaporVo arrObj : sheet5Results) {
				int columnIndex = 0;
				
				writeObjectToExcelPerdataPenggugat(sheet5, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);
				
				rowIndex++;
				rowNum++;
			}
		}
	}
	
	private void writeObjectToExcelPerdataTergugat(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum,ReportLitiPerdataTergugatVo data, int columnIndex) throws Exception {
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, rowNum, mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getUnitKerja(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getSegmen(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getDebitur(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTahunPerkara() != null ? String.valueOf(data.getTahunPerkara()) : "", mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getNomorPerkara(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTipePengadilan(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getDomisiliPengadilan(), mapCellFormat);
		getReportUtil().writeCellDetailWrapText(sheet, rowIndex, columnIndex++, data.getParaPihakPenggugat().toString(), mapCellFormat);
		getReportUtil().writeCellDetailWrapText(sheet, rowIndex, columnIndex++, data.getParaPihakTergugat().toString(), mapCellFormat);
		getReportUtil().writeCellDetailWrapText(sheet, rowIndex, columnIndex++, data.getParaPihakTurutTergugat().toString(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPenggugatKuasaHukum(), mapCellFormat);
		
		if(data.getLawyer().equals("1")) {
			String kuasaHukumTergugat = data.getTergugatKuasaHukum() + " - " + data.getNamaKantorHukum();
			getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, kuasaHukumTergugat, mapCellFormat);
			
		}else{
			getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTergugatKuasaHukum(), mapCellFormat);
		}
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTim1(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTim2(), mapCellFormat);
//		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPic(), mapCellFormat);
		String pics = data.getPic() != null ? data.getPic() : ""; //they want to use Keterangan PIC as PIC name placeholder
		pics = data.getKeteranganPic() != null ? pics + ", " + data.getKeteranganPic() : pics;
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, pics, mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getInternal(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getLawyer(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTanggalPanggilanSidang(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPokokPerkara(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getSubPokokPerkara(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getKasusPosisiGugatan(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getKedudukanHukumMaybank(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTuntutanPenggugat(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getMaterialIDR(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getMaterialValasTipe(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getMaterialValasValue(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getImmaterial(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTotalTuntutan(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTotalNilaiHakTanggungan(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPotensiKerugianLain(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPutusanPengadilan(), mapCellFormat);
		getReportUtil().writeCellDetailWrapText(sheet, rowIndex, columnIndex++, data.getProgressTerakhir().toString(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getStatusPutusanMenang(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getStatusPutusanKalah(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getStatusPutusanSidang(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraBerjalanBani(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraBerjalanPN(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraBerjalanPT(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraBerjalanPTUN(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraBerjalanPA(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraBerjalanPTAgama(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraBerjalanMAKasasi(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraBerjalanMAPK(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTotalPerkaraBerjalan(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTotalPerkaraSelesai(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraSelesaiBani(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraSelesaiPA(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraSelesaiPN(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraSelesaiPTUN(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraSelesaiPT(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraSelesaiMAKasasi(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraBerjalanMAPK(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraSelesaiTanggalSelesai(), mapCellFormat);
	
		CellRangeAddress cra = new CellRangeAddress(rowIndex,rowIndex,53,54);
		sheet.addMergedRegion(cra);
		RegionUtil.setBorderTop(BorderStyle.THIN, cra, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, cra, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, cra, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, cra, sheet);
	}
	
	private void writeObjectToExcelPerdataPenggugat(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum, ReportLitiPerdataPenggugatVo data, int columnIndex) throws Exception {
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, rowNum, mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getUnitKerja(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTahunPerkara() != null ? String.valueOf(data.getTahunPerkara()) : "", mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getNomorPerkara(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTipePengadilan(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getDomisiliPengadilan(), mapCellFormat);
		getReportUtil().writeCellDetailWrapText(sheet, rowIndex, columnIndex++, data.getParaPihakPenggugat().toString(), mapCellFormat);
		getReportUtil().writeCellDetailWrapText(sheet, rowIndex, columnIndex++, data.getParaPihakTergugat().toString(), mapCellFormat);
		getReportUtil().writeCellDetailWrapText(sheet, rowIndex, columnIndex++, data.getParaPihakTurutTergugat().toString(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getKuasaHukumPenggugat(), mapCellFormat);
		
		if(data.getKuasaHukumLawyer().equals("1")) {
			String kuasaHukumTergugat = data.getKuasaHukumTergugat() + " - " + data.getNamaKantorHukum();
			getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, kuasaHukumTergugat, mapCellFormat);
			
		}else{
			getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getKuasaHukumTergugat(), mapCellFormat);
		}
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getKuasaHukumInternal(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getKuasaHukumLawyer(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTim1(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTim2(), mapCellFormat);
//		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPic(), mapCellFormat);
		String pics = data.getPic() != null ? data.getPic() : ""; //they want to use Keterangan PIC as PIC name placeholder
		pics = data.getKeteranganPic() != null ? pics + ", " + data.getKeteranganPic() : pics;
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, pics, mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTuntutanPenggugat(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getMaterialIDR(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getMataUangValas() + " " + data.getMaterialValas(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getImmaterial(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTotalTuntutan(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPutusanPengadilan(), mapCellFormat);
		getReportUtil().writeCellDetailWrapText(sheet, rowIndex, columnIndex++, data.getProgressTerakhir().toString(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraBerjalanPN(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraBerjalanBani(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraBerjalanPT(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraBerjalanMAKasasi(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraBerjalanMAPK(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTotalPerkaraBerjalan(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTotalPerkaraSelesai(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraSelesaiBani(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraSelesaiPN(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraSelesaiPT(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraSelesaiMAKasasi(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraSelesaiMAPK(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPerkaraSelesaiTanggalSelesai(), mapCellFormat);
		
	}
	
	private void writeObjectToExcelPalilitKPU(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum, ReportLitiPailitPKPUVo data, int columnIndex) throws Exception {
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, rowNum, mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getCabang(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getSegmen(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTahunPerkara() != null ? String.valueOf(data.getTahunPerkara()) : "", mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getNomorPerkara(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPengadilanNiaga(), mapCellFormat);
		getReportUtil().writeCellDetailWrapText(sheet, rowIndex, columnIndex++, data.getParaPihakPenggugat().toString(), mapCellFormat);
		getReportUtil().writeCellDetailWrapText(sheet, rowIndex, columnIndex++, data.getParaPihakTergugat().toString(), mapCellFormat);
		getReportUtil().writeCellDetailWrapText(sheet, rowIndex, columnIndex++, data.getTanggalPutusanPengadilan().toString(), mapCellFormat);
		getReportUtil().writeCellDetailWrapText(sheet, rowIndex, columnIndex++, data.getAmarPutusanPengadilan().toString(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getKurator().toString(), mapCellFormat);
		getReportUtil().writeCellDetailWrapText(sheet, rowIndex, columnIndex++, data.getPerkembanganTerakhir().toString(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getNilaiTagihanMaybankPKPU(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getNilaiTagihanMaybankKepailitan(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getKeterangan(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getKuasaHukumMaybank(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTim1(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTim2(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getInternal(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getLawyer(), mapCellFormat);
//		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getPic(), mapCellFormat);
		String pics = data.getPic() != null ? data.getPic() : ""; //they want to use Keterangan PIC as PIC name placeholder
		pics = data.getKeteranganPic() != null ? pics + ", " + data.getKeteranganPic() : pics;
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, pics, mapCellFormat);
	}
	
	private void writeObjectToExcelPerdataPenggugat(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum, ReportLitiPidanaPelaporVo data, int columnIndex) throws Exception {
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, rowNum, mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getCabang(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getSegmen(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTahunPerkara() != null ? String.valueOf(data.getTahunPerkara()) : "", mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTanggalLaporan(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getNomorLaporan(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTindakPidana(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getInstansiPemeriksa(), mapCellFormat);
		getReportUtil().writeCellDetailWrapText(sheet, rowIndex, columnIndex++, data.getPelapor().toString(), mapCellFormat);
		getReportUtil().writeCellDetailWrapText(sheet, rowIndex, columnIndex++, data.getTerlapor().toString(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTim1(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTim2(), mapCellFormat);
//		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getNamaPic(), mapCellFormat);
		String pics = data.getNamaPic() != null ? data.getNamaPic() : ""; //they want to use Keterangan PIC as PIC name placeholder
		pics = data.getKeteranganPic() != null ? pics + ", " + data.getKeteranganPic() : pics;
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, pics, mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getKasusPosisi(), mapCellFormat);
		getReportUtil().writeCellDetailWrapText(sheet, rowIndex, columnIndex++, data.getPerkembanganPerkara().toString(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getNilaiPerkaraIDR(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getMataUangValas() + " " + data.getNilaiPerkaraValas(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTingkatPemeriksaanPol(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTingkatPemeriksaanJaksa(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTingkatPemeriksaanPN(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTingkatPemeriksaanSelesai(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getKuasaHukumMaybank(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getInternal(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getLawyer(), mapCellFormat);
	}
	
	private void writeObjectToExcelPerdataPenggugat(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum, ReportLitiPidanaTerlaporVo data, int columnIndex) throws Exception {
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, rowNum, mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getUnitKerja(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTahunPerkara() != null ? String.valueOf(data.getTahunPerkara()) : "", mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getNomorPerkara(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTindakPidana(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getInstansiPemeriksa(), mapCellFormat);
		getReportUtil().writeCellDetailWrapText(sheet, rowIndex, columnIndex++, data.getPelapor().toString(), mapCellFormat);
		getReportUtil().writeCellDetailWrapText(sheet, rowIndex, columnIndex++, data.getTerlapor().toString(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTim1(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTim2(), mapCellFormat);
//		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getNamaPic(), mapCellFormat);
		String pics = data.getNamaPic() != null ? data.getNamaPic() : ""; //they want to use Keterangan PIC as PIC name placeholder
		pics = data.getKeteranganPic() != null ? pics + ", " + data.getKeteranganPic() : pics;
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, pics, mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getKasusPosisi(), mapCellFormat);
		getReportUtil().writeCellDetailWrapText(sheet, rowIndex, columnIndex++, data.getPerkembanganPerkara().toString(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getNilaiPerkaraIDR(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getMataUangValas() + " " +  data.getNilaiPerkaraValas(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTingkatPemeriksaanPol(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTingkatPemeriksaanJaksa(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTingkatPemeriksaanPN(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getTingkatPemeriksaanSelesai(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getKuasaHukumMaybank(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getInternal(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, data.getLawyer(), mapCellFormat);
		
	}
	
	public void writeExcelHeader(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat) throws Exception {
		String createdDateFrom = "";
		String createdDateTo = "";
		String status = "";
		
		int row = 0;
		int columnStart = 0;
		
		SimpleDateFormat printTimestamp = new SimpleDateFormat(CommonConstants.INPUT_DATE_TIME_FORMAT);
		String excelPrintDate = printTimestamp.format(new Date());
		
		CellStyle cfHeaderValue = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_VALUE);
		CellStyle cfHeaderLabel = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_LABEL);
		
		String searchCreatedDateFrom = getSearchCriteriaValue(ReportLitigationConstant.SEARCH_TANGGAL_PEMBUATAN_FROM);
		String searchCreatedDateTo = getSearchCriteriaValue(ReportLitigationConstant.SEARCH_TANGGAL_PEMBUATAN_TO);
		String searchStatus = getSearchCriteriaValue(ReportLitigationConstant.SEARCH_STATUS);
		
		if (StringUtils.isNotBlank(searchCreatedDateFrom)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchCreatedDateFrom);
				createdDateFrom = DateUtil.dateToString(tmpDate);
			} catch (Exception ex) {
				createdDateFrom = "";
			}
		}

		if (StringUtils.isNotBlank(searchCreatedDateTo)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchCreatedDateTo);
				createdDateTo = DateUtil.dateToString(tmpDate);
			} catch (Exception ex) {
				createdDateTo = "";
			}
		}
		
		if (StringUtils.isNotBlank(searchStatus)) {
			if (searchStatus.equals(Constants.CONSTANT_YES)) {
				status = "Aktif";
			} else {
				status = "Tidak Aktif";
			}
		}
		
		reportUtil.writeCellTitle(sheet, 4, "Report Litigation", mapCellFormat);
		row++;
		row++;
		
		reportUtil.writeCell(sheet, row, columnStart, "Tanggal Pembuatan : ", cfHeaderLabel);
		reportUtil.writeCell(sheet, row, columnStart + 1, createdDateFrom 
				+ " " + ((StringUtils.isNotBlank(createdDateFrom) && StringUtils.isNotBlank(createdDateTo)) ? "s/d" : "")
				+ " " + createdDateTo, cfHeaderValue);
		row++;
		
		reportUtil.writeCell(sheet, row, columnStart, "Status : ", cfHeaderLabel);
		reportUtil.writeCell(sheet, row, columnStart + 1, status, cfHeaderValue);
		row++;
		
		reportUtil.writeCell(sheet, row, columnStart, "Cetak Oleh : ", cfHeaderLabel);
		reportUtil.writeCell(sheet, row, columnStart + 1, userNikName, cfHeaderValue);
		row++;

		reportUtil.writeCell(sheet, row, columnStart, "Tanggal Cetak : ", cfHeaderLabel);
		reportUtil.writeCell(sheet, row, columnStart + 1, excelPrintDate, cfHeaderValue);
		row++;
	}
	
	private List<String> buildListColumnNameRow1Sheet1() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("LOB");
		listColumnNameTemp.add("Debitur / Nasabah");
		listColumnNameTemp.add("Registrasi Perkara");
		listColumnNameTemp.add("Para Pihak");
		listColumnNameTemp.add("Kuasa Hukum");
		listColumnNameTemp.add("Penanganan Tim");
		listColumnNameTemp.add("PIC");
		listColumnNameTemp.add("Penanganan Perkara");
		listColumnNameTemp.add("Tanggal Panggilan Sidang");
		listColumnNameTemp.add("Pokok Perkara");
		listColumnNameTemp.add("Sub Pokok Perkara");
		listColumnNameTemp.add("Kasus Posisi Gugatan");
		listColumnNameTemp.add("Kedudukan Hukum Maybank");
		listColumnNameTemp.add("Tuntutan Penggugat");
		listColumnNameTemp.add("Nilai Tuntutan Perkara");
		listColumnNameTemp.add("Potensi Kerugian");
		listColumnNameTemp.add("Putusan Pengadilan");
		listColumnNameTemp.add("Progress Terakhir");
		listColumnNameTemp.add("Status Putusan");
		listColumnNameTemp.add("Tingkat Pemeriksaan Perkara");
		
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow2Sheet1(){
		List<String> listColumnNameTemp = new ArrayList<String>();
		
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Tahun Perkara");
		listColumnNameTemp.add("Nomor Perkara");
		listColumnNameTemp.add("Tipe Pengadilan");
		listColumnNameTemp.add("Domisili Pengadilan");
		listColumnNameTemp.add("Penggugat");
		listColumnNameTemp.add("Tergugat");
		listColumnNameTemp.add("Turut Tergugat");
		listColumnNameTemp.add("Penggugat");
		listColumnNameTemp.add("Tergugat/Turut Tergugat (Maybank)");
		listColumnNameTemp.add("Tim 1");
		listColumnNameTemp.add("Tim 2");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Internal");
		listColumnNameTemp.add("Lawyer");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Material");
		listColumnNameTemp.add("Immaterial");
		listColumnNameTemp.add("Total Tuntutan");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Menang");
		listColumnNameTemp.add("Kalah");
		listColumnNameTemp.add("Sidang");
		listColumnNameTemp.add("Perkara Berjalan");
		listColumnNameTemp.add("Perkara Selesai");
		
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow3Sheet1(){
		List<String> listColumnNameTemp = new ArrayList<String>();
		
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Unit Kerja / Cabang");
		listColumnNameTemp.add("Segmen");
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
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("(IDR)");
		listColumnNameTemp.add("Valas");
		listColumnNameTemp.add("(IDR)");
		listColumnNameTemp.add("(IDR)");
		listColumnNameTemp.add("Total Nilai Hak Tanggungan / Nilai Cessie");
		listColumnNameTemp.add("Potensi Kerugian Lain (Jika ada)");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("BANI");
		listColumnNameTemp.add("PN");
		listColumnNameTemp.add("PT");
		listColumnNameTemp.add("PTUN");
		listColumnNameTemp.add("PA");
		listColumnNameTemp.add("PT Agama");
		listColumnNameTemp.add("MA-Kasasi");
		listColumnNameTemp.add("MA-PK");
		listColumnNameTemp.add("Total Perkara Berjalan");
		listColumnNameTemp.add("Total Perkara Selesai");
		listColumnNameTemp.add("BANI");
		listColumnNameTemp.add("PA");
		listColumnNameTemp.add("PN");
		listColumnNameTemp.add("PTUN");
		listColumnNameTemp.add("PT");
		listColumnNameTemp.add("MA-Kasasi");
		listColumnNameTemp.add("MA-PK");
		listColumnNameTemp.add("Tanggal Selesai");
		
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow4Sheet1(){
		List<String> listColumnNameTemp = new ArrayList<String>();
		
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
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("1");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Tipe");
		listColumnNameTemp.add("Value");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("1");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("1");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("1");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("0");
		
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow5Sheet1(){
		List<String> listColumnNameTemp = new ArrayList<String>();
		
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
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("1");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Rp");
		listColumnNameTemp.add("Rp");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("1");
		listColumnNameTemp.add("1");
		listColumnNameTemp.add("1");
		listColumnNameTemp.add("1");
		listColumnNameTemp.add("");
		
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow1Sheet2() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		
		listColumnNameTemp.add("Nomor Urut");
		listColumnNameTemp.add("LOB");
		listColumnNameTemp.add("Register Perkara");
		listColumnNameTemp.add("Para Pihak");
		listColumnNameTemp.add("Kuasa Hukum");
		listColumnNameTemp.add("Kuasa Hukum");
		listColumnNameTemp.add("Penanganan Perkara");
		listColumnNameTemp.add("PIC");
		listColumnNameTemp.add("Tuntutan Penggugat");
		listColumnNameTemp.add("Nilai Tuntutan Perkara");
		listColumnNameTemp.add("Putusan Pengadilan");
		listColumnNameTemp.add("Progress Terakhir");
		listColumnNameTemp.add("Tingkat Pemeriksaan Perkara");
		
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow2Sheet2() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Unit Kerja");
		listColumnNameTemp.add("Tahun Perkara");
		listColumnNameTemp.add("Nomor Perkara");
		listColumnNameTemp.add("Tipe Pengadilan");
		listColumnNameTemp.add("Domisili Pengadilan");
		listColumnNameTemp.add("Penggugat");
		listColumnNameTemp.add("Tergugat");
		listColumnNameTemp.add("Turut Tergugat");
		listColumnNameTemp.add("Penggugat");
		listColumnNameTemp.add("Tergugat");
		listColumnNameTemp.add("Internal");
		listColumnNameTemp.add("Lawyer");
		listColumnNameTemp.add("Tim 1");
		listColumnNameTemp.add("Tim 2");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Material");
		listColumnNameTemp.add("Immaterial");
		listColumnNameTemp.add("Total Tuntutan");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Perkara Berjalan");
		listColumnNameTemp.add("Perkara Selesai");
		
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow3Sheet2() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		
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
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("(IDR)");
		listColumnNameTemp.add("(Valas)");
		listColumnNameTemp.add("(IDR)");
		listColumnNameTemp.add("(IDR)");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("PN");
		listColumnNameTemp.add("BANI");
		listColumnNameTemp.add("PT");
		listColumnNameTemp.add("MA-Kasasi");
		listColumnNameTemp.add("MA-PK");
		listColumnNameTemp.add("Total Perkara Berjalan");
		listColumnNameTemp.add("Total Perkara Selesai");
		listColumnNameTemp.add("BANI");
		listColumnNameTemp.add("PN");
		listColumnNameTemp.add("PT");
		listColumnNameTemp.add("MA-Kasasi");
		listColumnNameTemp.add("MA-PK");
		listColumnNameTemp.add("Tanggal Selesai");
		
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow1Sheet3() {
		List<String> listColumnNameTemp = new ArrayList<>();
		
		listColumnNameTemp.add("Nomor Urut");
		listColumnNameTemp.add("LOB");
		listColumnNameTemp.add("Register Perkara");
		listColumnNameTemp.add("Para Pihak");
		listColumnNameTemp.add("Putusan Pengadilan");
		listColumnNameTemp.add("Kurator / Pengurus");
		listColumnNameTemp.add("Perkembangan Terakhir");
		listColumnNameTemp.add("Nilai Tagihan Maybank");
		listColumnNameTemp.add("Keterangan");
		listColumnNameTemp.add("Kuasa Hukum Maybank");
		listColumnNameTemp.add("Penanganan Perkara");
		listColumnNameTemp.add("PIC");
		
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow2Sheet3() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Cabang");
		listColumnNameTemp.add("Segmen");
		listColumnNameTemp.add("Tahun Perkara");
		listColumnNameTemp.add("Nomor Perkara");
		listColumnNameTemp.add("Pengadilan Niaga");
		listColumnNameTemp.add("Pemohon");
		listColumnNameTemp.add("Termohon");
		listColumnNameTemp.add("Tanggal Putusan (terakhir)");
		listColumnNameTemp.add("Amar Putusan (Status Terakhir)");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Tim 1");
		listColumnNameTemp.add("Tim 2");
		listColumnNameTemp.add("Internal");
		listColumnNameTemp.add("Lawyer");
		
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow3Sheet3() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		
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
		listColumnNameTemp.add("PKPU");
		listColumnNameTemp.add("Kepailitan");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow1Sheet4() {
		List<String> listColumnNameTemp = new ArrayList<>();
		
		listColumnNameTemp.add("Nomor Urut");
		listColumnNameTemp.add("LOB");
		listColumnNameTemp.add("Register Perkara");
		listColumnNameTemp.add("Para Pihak");
		listColumnNameTemp.add("Tim 1");
		listColumnNameTemp.add("Tim 2");
		listColumnNameTemp.add("PIC");
		listColumnNameTemp.add("Kasus Posisi");
		listColumnNameTemp.add("Perkembangan Perkara");
		listColumnNameTemp.add("Nilai Perkara");
		listColumnNameTemp.add("Tingkat Pemeriksaan");
		listColumnNameTemp.add("Kuasa Hukum Maybank");
		listColumnNameTemp.add("Internal");
		listColumnNameTemp.add("Lawyer");
		
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow2Sheet4() {
		List<String> listColumnNameTemp = new ArrayList<>();
		
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
		listColumnNameTemp.add("(IDR)");
		listColumnNameTemp.add("(VALAS) USD");
		listColumnNameTemp.add("Pol");
		listColumnNameTemp.add("Jaksa");
		listColumnNameTemp.add("PN");
		listColumnNameTemp.add("Selesai");
		
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow3Sheet4() {
		List<String> listColumnNameTemp = new ArrayList<>();
		
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Cabang");
		listColumnNameTemp.add("Segmen");
		listColumnNameTemp.add("Tahun Perkara");
		listColumnNameTemp.add("Tanggal Laporan");
		listColumnNameTemp.add("Nomor Laporan / Perkara");
		listColumnNameTemp.add("Tindak Pidana");
		listColumnNameTemp.add("Instansi Pemeriksa");
		listColumnNameTemp.add("Pelapor");
		listColumnNameTemp.add("Terlapor/Tersangka");
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
	
	private List<String> buildListColumnNameRow4Sheet4() {
		List<String> listColumnNameTemp = new ArrayList<>();
		
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
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Rp");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("0");
		
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow1Sheet5() {
		List<String> listColumnNameTemp = new ArrayList<>();
		
		listColumnNameTemp.add("Nomor Urut");
		listColumnNameTemp.add("LOB");
		listColumnNameTemp.add("Register Perkara");
		listColumnNameTemp.add("Para Pihak");
		listColumnNameTemp.add("Tim 1");
		listColumnNameTemp.add("Tim 2");
		listColumnNameTemp.add("PIC");
		listColumnNameTemp.add("Kasus Posisi");
		listColumnNameTemp.add("Perkembangan Perkara");
		listColumnNameTemp.add("Nilai Perkara");
		listColumnNameTemp.add("Tingkat Pemeriksaan");
		listColumnNameTemp.add("Kuasa Hukum Maybank");
		listColumnNameTemp.add("Internal");
		listColumnNameTemp.add("Lawyer");
		
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow2Sheet5() {
		List<String> listColumnNameTemp = new ArrayList<>();
		
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
		listColumnNameTemp.add("(IDR)");
		listColumnNameTemp.add("(VALAS)");
		listColumnNameTemp.add("Pol");
		listColumnNameTemp.add("Jaksa");
		listColumnNameTemp.add("PN");
		listColumnNameTemp.add("Selesai");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow3Sheet5() {
		List<String> listColumnNameTemp = new ArrayList<>();
		
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Unit Kerja");
		listColumnNameTemp.add("Tahun Perkara");
		listColumnNameTemp.add("Nomor Perkara");
		listColumnNameTemp.add("Tindak Pidana");
		listColumnNameTemp.add("Instansi Pemeriksa");
		listColumnNameTemp.add("Pelapor");
		listColumnNameTemp.add("Terlapor");
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
	
	private List<String> buildListColumnNameRow4Sheet5() {
		List<String> listColumnNameTemp = new ArrayList<>();
		
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("1");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Rp");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("1");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("0");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("1");
		listColumnNameTemp.add("0");
		
		return listColumnNameTemp;
	}
	
	private void updateReportGenHistoryAsCompleteError(Long newReportGenId, UploadedFileWO ufw, String errorMsg) throws Exception {
		ReportGen editReportGen = reportGenService.findById(newReportGenId);
		editReportGen.setReportGenStatus(CommonConstants.REPORT_GEN_STATUS_COMPLETE_ERR);
		editReportGen.setReportGenStatusMsg(errorMsg);

		if (ufw != null) editReportGen.setReportGenFile(ufw.getEncodedBase64());

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

	public String getDocumentTypeName() {
		return documentTypeName;
	}

	public void setDocumentTypeName(String documentTypeName) {
		this.documentTypeName = documentTypeName;
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


	@SuppressWarnings("rawtypes")
	public List<? extends SearchObject> getSearchCriteria() {
		return searchCriteria;
	}

	@SuppressWarnings("rawtypes")
	public void setSearchCriteria(List<? extends SearchObject> searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public CommonReportUtil getReportUtil() {
		return reportUtil;
	}

	public void setReportUtil(CommonReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}

	public static Logger getLogger() {
		return logger;
	}

	public ReportLitigationService getReportLitigationService() {
		return reportLitigationService;
	}

	public void setReportLitigationService(ReportLitigationService reportLitigationService) {
		this.reportLitigationService = reportLitigationService;
	}

	public String getSearchCaseType() {
		return searchCaseType;
	}

	public void setSearchCaseType(String searchCaseType) {
		this.searchCaseType = searchCaseType;
	}

	public String getSearchDebitur() {
		return searchDebitur;
	}

	public void setSearchDebitur(String searchDebitur) {
		this.searchDebitur = searchDebitur;
	}

	public String getSearchNoCase() {
		return searchNoCase;
	}

	public void setSearchNoCase(String searchNoCase) {
		this.searchNoCase = searchNoCase;
	}

	public String getSearchProgress() {
		return searchProgress;
	}

	public void setSearchProgress(String searchProgress) {
		this.searchProgress = searchProgress;
	}

	public String getSearchPutusan() {
		return searchPutusan;
	}

	public void setSearchPutusan(String searchPutusan) {
		this.searchPutusan = searchPutusan;
	}

	public String getSearchUpayaHukum() {
		return searchUpayaHukum;
	}

	public void setSearchUpayaHukum(String searchUpayaHukum) {
		this.searchUpayaHukum = searchUpayaHukum;
	}

	public String getSearchProvType() {
		return searchProvType;
	}

	public void setSearchProvType(String searchProvType) {
		this.searchProvType = searchProvType;
	}

	public static String getReportLitigation() {
		return REPORT_LITIGATION;
	}

}
