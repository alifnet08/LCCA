package com.wo.module.report.reportAuditDetail.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.TextAnchor;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.report.CommonReportUtil;
import com.wo.module.common.util.DateUtil;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.SCMApiUpload;
import com.wo.module.common.utility.SCMApiUploadImpl;
import com.wo.module.lov.bean.RunnableFacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.report.reportAuditDetail.constant.ReportAuditDetailConstants;
import com.wo.module.report.reportAuditDetail.model.ReportAuditDetail;
import com.wo.module.report.reportAuditDetail.service.ReportAuditDetailService;
import com.wo.module.report.reportAuditRekap.model.ReportAuditRekap;
import com.wo.module.report.reportAuditRekap.service.ReportAuditRekapService;
import com.wo.module.report.reportGen.constant.ReportSheetNameConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.trcAudit.model.TrcAuditPicFollowup;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupAuditFindings;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupAuditFindingsTable;
import com.wo.module.trcAudit.service.TrcAuditPICFollowupService;

public class ReportAuditDetailTask implements Runnable, ReportSheetNameConstant, ReportAuditDetailConstants {

	static Logger logger = Logger.getLogger(ReportAuditDetailTask.class);

	public final static String COMPLIANCE_DOC_TYPE_REPORT_AUDIT_DETAIL = "Report Audit Detail";
	public final static int SUB_TABLE_LEFT_MARGIN_COL = 1;
	public final static int SUB_TABLE_RIGHT_MARGIN_COL = 1;
	public final static String PIE_CHART_TITLE = "Total Audit";

	private Long reportGenId;
	private String userNikName;
	private Integer totalDoneTemp = 0;
	private Integer totalNotDoneTemp = 0;
	private String divisionName;
	private String templateName;
	private String findingNameIn;
	private String findingNameEn;
	
	private ReportGenService reportGenService;
	private ParameterDetailService parameterDetailService;
	private RunnableFacesUtil runnableFacesUtil;
	private ReportAuditDetailService reportAuditDetailService;
	private ReportAuditRekapService reportAuditRekapService;
	private TrcAuditPICFollowupService trcAuditPICFollowupService;

	@SuppressWarnings("rawtypes")
	private List<? extends SearchObject> searchCriteria;

//	private String searchCreationDateFrom;
//	private String searchCreationDateTo;
	private String searchAuditor;
	private String searchTargetDateFrom;
	private String searchTargetDateTo;
	private String searchAuditDateFrom;
	private String searchAuditDateTo;
	private String searchStatusFollowup;
	private String searchStatusVerification;
//	private String searchFindingName;

	private CommonReportUtil reportUtil = new CommonReportUtil();

	@SuppressWarnings("rawtypes")
	public ReportAuditDetailTask(Long reportGenId, ReportGenService reportGenService,
			ParameterDetailService parameterDetailService, RunnableFacesUtil runnableFacesUtil,
			ReportAuditDetailService reportAuditDetailService, ReportAuditRekapService reportAuditRekapService,
			List<? extends SearchObject> searchCriteria, String userNikName,
			TrcAuditPICFollowupService trcAuditPICFollowupService, String divisionName, String templateName,
			String findingNameIn, String findingNameEn) {
		super();
		this.reportGenId = reportGenId;
		this.reportGenService = reportGenService;
		this.parameterDetailService = parameterDetailService;
		this.reportAuditDetailService = reportAuditDetailService;
		this.reportAuditRekapService = reportAuditRekapService;
		this.setRunnableFacesUtil(runnableFacesUtil);
		this.searchCriteria = searchCriteria;
		this.userNikName = userNikName;
		this.trcAuditPICFollowupService = trcAuditPICFollowupService;
		this.divisionName = divisionName;
		this.templateName = templateName;
		this.findingNameIn = findingNameIn;
		this.findingNameEn = findingNameEn;
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

	@Override
	public void run() {

		List<Integer> listColumnView = new ArrayList<Integer>();
		List<Integer> listColumnViewRekap = new ArrayList<Integer>();
		UploadedFileWO ufw = null;
		String absoluteResultFilePath = null;

		try {

			absoluteResultFilePath = writeToFile(listColumnView, listColumnViewRekap);

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

	private UploadedFileWO uploadFileToApi(String absoluteResultFilePath) throws Exception {
		SCMApiUpload saf = new SCMApiUploadImpl(absoluteResultFilePath, parameterDetailService,
				COMPLIANCE_DOC_TYPE_REPORT_AUDIT_DETAIL);
		saf.upload();
		return saf.getAsUploadedFileWO();
	}

	private String writeToFile(List<Integer> listColumnView, List<Integer> listColumnViewRekap) throws Exception {
		String sheetName1 = SHEET_NAME_REPORT_AUDIT_DETAIL;

		String fileNamePrefix = "ReportAuditDetail";

		String fileName = getReportUtil().generateFileName(fileNamePrefix, userNikName);
		String filePath = getReportUtil().retrieveFilePath(parameterDetailService);

		Path dirPath = Paths.get(filePath);
		if (!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}

		XSSFWorkbook workbook = null;
		FileOutputStream fileOutputStream = null;

		List<String> buildListColumnNameRow1Sheet1 = buildListColumnNameRow1Sheet1();

		List<ReportAuditDetail> sheet1Results = (List<ReportAuditDetail>) reportAuditDetailService
				.getReportAuditDetailByData(searchCriteria,findingNameEn,findingNameIn);

		try {

			fileOutputStream = new FileOutputStream(filePath + fileName);
			workbook = new XSSFWorkbook();

			writeSheetAuditDetail(listColumnView, sheetName1, workbook, buildListColumnNameRow1Sheet1, sheet1Results);

			writeSheetAuditRekap(workbook, listColumnViewRekap);

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

	private void writeSheetAuditDetail(List<Integer> listColumnView, String sheetName1, XSSFWorkbook workbook,
			List<String> buildListColumnNameRow1Sheet1, List<ReportAuditDetail> sheet1Results) throws Exception {

		XSSFSheet sheet1 = workbook.createSheet(sheetName1);

		// setting column size [Start]
		if (listColumnView != null) {
			for (int i = 0; i < listColumnView.size(); i++) {
				sheet1.setColumnWidth(i, listColumnView.get(i));
				sheet1.autoSizeColumn(i);
			}
		}
		// setting column size [End]

		Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
		getReportUtil().createExcelCellFormat(workbook, mapCellFormat);

		this.writeExcelHeader(sheet1, mapCellFormat);

		// write sheet 1 column header [start]
		CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER);
		int rowIndex = 14;

		int countMaxColumnSubTable = findMaxColumn(sheet1Results);
		int totalColumn = 0;
		if (buildListColumnNameRow1Sheet1 != null && !buildListColumnNameRow1Sheet1.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow1Sheet1) {

				if (columnIndex == 4) { // if temuan pemeriksaan
					getReportUtil().writeCell(sheet1, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);

					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex,
							columnIndex + countMaxColumnSubTable + SUB_TABLE_RIGHT_MARGIN_COL);
					sheet1.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet1);

					columnIndex += (countMaxColumnSubTable + SUB_TABLE_RIGHT_MARGIN_COL);
				} else {
					getReportUtil().writeCell(sheet1, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);
				}

				columnIndex++;
			}

			totalColumn = columnIndex;
		}
		// write sheet 1 column header [end]

		// write sheet 1 data detail [start]
		if (sheet1Results != null && !sheet1Results.isEmpty()) {
			rowIndex += 1;

			int rowNum = 1;

			for (ReportAuditDetail arrObj : sheet1Results) {
				int columnIndex = 0;
				int rowBefore = rowIndex;
				rowIndex = writeObjectToExcelSheet2(sheet1, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex,
						countMaxColumnSubTable);

				// merge
				if (rowBefore != rowIndex) {
					int totalColumnSize = buildListColumnNameRow1Sheet1.size() + countMaxColumnSubTable
							+ SUB_TABLE_RIGHT_MARGIN_COL;

					// filter how many column is temuan pemeriksaan
					List<Integer> colIdxTemuanArray = new ArrayList<Integer>();
					for (int i = 4; i <= (4 + countMaxColumnSubTable + SUB_TABLE_RIGHT_MARGIN_COL); i++) {
						colIdxTemuanArray.add(i);
//									sheet1.autoSizeColumn(i);
					}

					for (int i = 0; i < totalColumnSize; i++) {
						if (!colIdxTemuanArray.contains(i))
							mergedRowWithinSubTable(sheet1, rowBefore, rowIndex, i);
					}
				}

				rowIndex++;
				rowNum++;
			}

			int rowTemp = 0;
			rowTemp = rowIndex;

			// reapply border after detail with string empty
			CellStyle cfColumnWrapBorderTopOnly = mapCellFormat
					.get(CommonConstants.MAP_KEY_CELL_FORMAT_DATA_DTL_WRAP_BORDER_TOP_ONLY_STRING);
			for (int i = 0; i < totalColumn - 3; i++) {
				getReportUtil().writeCell(sheet1, rowTemp, i, "", cfColumnWrapBorderTopOnly);
			}

			getReportUtil().writeCell(sheet1, rowTemp, totalColumn - 3, "Total", cfColumnHeader);

			getReportUtil().writeCellDetail(sheet1, rowIndex, totalColumn - 2, totalDoneTemp, mapCellFormat);

			getReportUtil().writeCellDetail(sheet1, rowIndex, totalColumn - 1, totalNotDoneTemp, mapCellFormat);

		}
		// write sheet 1 data detail [end]

		sheet1.autoSizeColumn(12); // tanggapan bank
		sheet1.autoSizeColumn(13); // komitment bank
	}

	private void writeSheetAuditRekap(XSSFWorkbook workbook, List<Integer> listColumnViewRekap) throws Exception {
//		String sheetName2 = SHEET_NAME_REPORT_AUDIT_REKAP;
		String sheetName2 = SHEET_NAME_REPORT_AUDIT_REKAP_BY_TIPE_AUDIT;
		String sheetName3 = SHEET_NAME_REPORT_AUDIT_REKAP_BY_AUDIT_TEMPLATE;
//		String sheetName3 = SHEET_NAME_REPORT_AUDIT_DIAGRAM;
		String sheetName4 = SHEET_NAME_REPORT_AUDIT_DIAGRAM_BY_TIPE_AUDIT;
		String sheetName5 = SHEET_NAME_REPORT_AUDIT_DIAGRAM_BY_AUDIT_TEMPLATE;

		List<String> buildListColumnNameRow1Sheet2 = buildListColumnNameRow1Sheet2();
		List<String> buildListColumnNameRow2Sheet2 = buildListColumnNameRow2Sheet2();
		
		List<String> buildListColumnNameRow1Sheet3 = buildListColumnNameRow1Sheet3();
		List<String> buildListColumnNameRow2Sheet3 = buildListColumnNameRow2Sheet3();

		List<ReportAuditRekap> sheet1Results = (List<ReportAuditRekap>) reportAuditRekapService
				.getReportAuditRekapByData(searchCriteria, findingNameEn, findingNameIn);
		
		List<ReportAuditRekap> sheet3Results = (List<ReportAuditRekap>) reportAuditRekapService
				.getReportAuditRekapByTemplateName(searchCriteria, findingNameEn, findingNameIn);

		XSSFSheet sheet2 = workbook.createSheet(sheetName2);
		XSSFSheet sheet3 = workbook.createSheet(sheetName3);

		String filePath = getReportUtil().retrieveFilePath(parameterDetailService);

		// setting column size [Start]
		if (listColumnViewRekap != null) {
			for (int i = 0; i < listColumnViewRekap.size(); i++) {
				sheet2.setColumnWidth(i, listColumnViewRekap.get(i));
				sheet2.autoSizeColumn(i);
			}
		}
		
		if (listColumnViewRekap != null) {
			for (int i = 0; i < listColumnViewRekap.size(); i++) {
				sheet3.setColumnWidth(i, listColumnViewRekap.get(i));
				sheet3.autoSizeColumn(i);
			}
		}
		// setting column size [End]

		Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
		getReportUtil().createExcelCellFormat(workbook, mapCellFormat);

		this.writeExcelHeader(sheet2, mapCellFormat);
		this.writeExcelHeader(sheet3, mapCellFormat);

		// write sheet 1 column header [start]
		CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER);
		int rowIndex = 14;

		if (buildListColumnNameRow1Sheet2 != null && !buildListColumnNameRow1Sheet2.isEmpty()) {

			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow1Sheet2) {

				getReportUtil().writeCell(sheet2, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);

				if (columnIndex == 3) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex += 1);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else if (columnIndex == 5) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex += 1);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else if (columnIndex == 7) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex += 2);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else {
					sheet2.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex + 1, columnIndex, columnIndex));
				}

				columnIndex++;
			}
		}
		rowIndex++;
		if (buildListColumnNameRow2Sheet2 != null && !buildListColumnNameRow2Sheet2.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow2Sheet2) {
				getReportUtil().writeCell(sheet2, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);

				columnIndex++;
			}
		}
		// write sheet 1 column header [end]

		// sheet 1 data [start]
		if (sheet1Results != null && !sheet1Results.isEmpty()) {
			rowIndex += 1;
			int rowNum = 1;

			for (ReportAuditRekap arrObj : sheet1Results) {
				int columnIndex = 0;

				writeObjectToExcelSheet2(sheet2, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);

				rowIndex++;
				rowNum++;
			}
		}
		// sheet 1 data [end]

		// write sheet 2 column header [start]
		int rowIndexSheet3 = 14;
		if (buildListColumnNameRow1Sheet3 != null && !buildListColumnNameRow1Sheet3.isEmpty()) {

			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow1Sheet3) {

				getReportUtil().writeCell(sheet3, rowIndexSheet3, columnIndex, columnNameAlias, cfColumnHeader);

				if (columnIndex == 3) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet3, rowIndexSheet3, columnIndex, columnIndex += 1);
					sheet3.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet3);
				} else if (columnIndex == 5) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet3, rowIndexSheet3, columnIndex, columnIndex += 1);
					sheet3.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet3);
				} else if (columnIndex == 7) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet3, rowIndexSheet3, columnIndex, columnIndex += 2);
					sheet3.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet3);
				} else {
					sheet3.addMergedRegion(new CellRangeAddress(rowIndexSheet3, rowIndexSheet3 + 1, columnIndex, columnIndex));
				}

				columnIndex++;
			}
		}
		rowIndexSheet3++;
		if (buildListColumnNameRow2Sheet3 != null && !buildListColumnNameRow2Sheet3.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow2Sheet3) {
				getReportUtil().writeCell(sheet3, rowIndexSheet3, columnIndex, columnNameAlias, cfColumnHeader);

				columnIndex++;
			}
		}
		// write sheet 2 column header [end]
		
		// sheet 2 data [start]
		if (sheet3Results != null && !sheet3Results.isEmpty()) {
			rowIndexSheet3 += 1;
			int rowNum = 1;

			for (ReportAuditRekap arrObj : sheet3Results) {
				int columnIndex = 0;

				writeObjectToExcelSheet3(sheet3, mapCellFormat, rowIndexSheet3, rowNum, arrObj, columnIndex);

				rowIndexSheet3++;
				rowNum++;
			}
		}
		// sheet 2 data [end]
		
		// sheet 3 data [Start]
		writeSheet3(sheetName4, filePath, workbook, sheet1Results);
		// sheet 3 data [End]
		
		// sheet 4 data [Start]
		writeSheet4(sheetName5, filePath, workbook, sheet3Results);
		// sheet 4 data [End]
	}

	private int findMaxColumn(List<ReportAuditDetail> sheet1Results) {
		int countMaxColumnSubTable = 0;
		for (ReportAuditDetail rad : sheet1Results) {
			if (countMaxColumnSubTable < rad.getMaxAuditFindingsColumn())
				countMaxColumnSubTable = rad.getMaxAuditFindingsColumn();
		}
		return countMaxColumnSubTable;
	}

	private int writeObjectToExcelSheet2(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex,
			int rowNum, ReportAuditDetail arrObj, int columnIndex, int countMaxColumnSubTable) throws Exception {
		int rowIndexSubTable = rowIndex;
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, rowNum, mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getAuditTemplateNameEn(), arrObj.getAuditTemplateNameIn()),
				mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getTypeAuditEn(), arrObj.getTypeAuditIn()),
				mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getFindingNameEn(), arrObj.getFindingNameIn()),
				mapCellFormat);
		
		if (arrObj.getAuditPicFollowupId() != null) {
			TrcAuditPicFollowup followup = trcAuditPICFollowupService.findById(arrObj.getAuditPicFollowupId());

			/*if (followup.getTrcAuditPicFollowupAuditFindings() != null
					&& !followup.getTrcAuditPicFollowupAuditFindings().isEmpty()) {

				// print detail findings
				int idx = 0;
				for (TrcAuditPicFollowupAuditFindings findings : followup.getTrcAuditPicFollowupAuditFindings()) {
					int temuanColumnIndex = columnIndex;

					// write temuan pemeriksaan value

					CellStyle cfColumnDetailWrapNoBottom = mapCellFormat
							.get(CommonConstants.MAP_KEY_CELL_FORMAT_DATA_DTL_WRAP_NO_BOTTOM_STRING);
					getReportUtil().writeCell(sheet1, rowIndexSubTable, temuanColumnIndex, findings.getAuditFindings(),
							cfColumnDetailWrapNoBottom);

					CellRangeAddress cra = new CellRangeAddress(rowIndexSubTable, rowIndexSubTable, temuanColumnIndex,
							temuanColumnIndex + countMaxColumnSubTable + SUB_TABLE_RIGHT_MARGIN_COL);
					sheet1.addMergedRegion(cra);

					if (idx == 0)
						reportUtil.mergedContentColumnStyleNoBottom(cra, sheet1);

					rowIndexSubTable++;

					// write sub temuan pemeriksaan value
					if (findings.getTrcAuditPicFollowupAuditFindingsTables() != null
							&& !findings.getTrcAuditPicFollowupAuditFindingsTables().isEmpty()) {
						for (int i = 0; i < findings.getTrcAuditPicFollowupAuditFindingsTables().size(); i++) {
							TrcAuditPicFollowupAuditFindingsTable aft = findings
									.getTrcAuditPicFollowupAuditFindingsTables().get(i);
							int temuanColumnIndexStart = temuanColumnIndex + 1;
							for (int j = 0; j < findings.getColumn(); j++) {
								Method getMethod = aft.getClass().getMethod("getColumn" + (j + 1) + "");
								String result = (String) getMethod.invoke(aft);

								if (CommonConstants.Y.equals(aft.getIsHeader())) {
									CellStyle cfColumnHeader = mapCellFormat
											.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_WRAP_HEADER);
									getReportUtil().writeCell(sheet1, rowIndexSubTable, temuanColumnIndexStart++,
											result, cfColumnHeader);
								} else {
									getReportUtil().writeWrapCellDetail(sheet1, rowIndexSubTable,
											temuanColumnIndexStart++, result, mapCellFormat);
								}
							}

							rowIndexSubTable++;
						}
					}

					idx++;
				}

			} else {
				// columnIndex = createEmptyCell(sheet1, mapCellFormat, rowIndex, columnIndex,
				// countMaxColumnSubTable);
			}*/

		} else { // create empty box
			// columnIndex = createEmptyCell(sheet1, mapCellFormat, rowIndex, columnIndex,
			// countMaxColumnSubTable);
		}

		// merge col temuan pemeriksaan
		CellRangeAddress cra = new CellRangeAddress(rowIndexSubTable, rowIndexSubTable, columnIndex,
				columnIndex + countMaxColumnSubTable + SUB_TABLE_RIGHT_MARGIN_COL);
		sheet1.addMergedRegion(cra);
//		reportUtil.mergedContentColumnStyleNoTop(cra, sheet1);

		columnIndex += SUB_TABLE_LEFT_MARGIN_COL + countMaxColumnSubTable + SUB_TABLE_RIGHT_MARGIN_COL;

		// columnIndex = createEmptyCell(sheet1, mapCellFormat, rowIndex, columnIndex,
		// countMaxColumnSubTable);

		// wrap
		getReportUtil().writeWrapCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTanggapanBank(), mapCellFormat);

		getReportUtil().writeWrapCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getKomitmenBank(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getUnitKerja(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getPic1(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getPic2(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getPic3(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTargetDate(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getRescheduleTarget1(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getRescheduleTarget2(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getRescheduleTarget3(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getStatusFollowUpEn(), arrObj.getStatusFollowUpIn()),
				mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getStatusVerifikasiEn(), arrObj.getStatusVerifikasiIn()),
				mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getAttachmentDokumenTindakLanjut(),
				mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getKeterangan(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getAttachmentSuratOjkBi(),
				mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getSla(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTotalDone(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTotalNotDone(), mapCellFormat);

		if (arrObj.getTotalDone() != null && !arrObj.getTotalDone().trim().isEmpty()) {
			totalDoneTemp++;
		}
		if (arrObj.getTotalNotDone() != null && !arrObj.getTotalNotDone().trim().isEmpty()) {
			totalNotDoneTemp++;
		}

		return rowIndexSubTable;
	}

	private void mergedRowWithinSubTable(XSSFSheet sheet1, int rowIndex, int rowIndexSubTable, int colIdx) {
		CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndexSubTable, colIdx, colIdx);
		sheet1.addMergedRegion(cra);
		reportUtil.mergedContentColumnStyle(cra, sheet1);

		for (int i = cra.getFirstRow(); i <= cra.getLastRow(); i++) {
			Row r = sheet1.getRow(i);
			for (int j = cra.getFirstColumn(); j <= cra.getLastColumn(); j++) {
				Cell c = r.getCell(j);
				CellUtil.setVerticalAlignment(c, VerticalAlignment.TOP);
			}
		}

	}

	@SuppressWarnings("unused")
	private int createEmptyCell(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex, int columnIndex,
			int countMaxColumnSubTable) throws Exception {
		for (int j = 0; j < SUB_TABLE_LEFT_MARGIN_COL + countMaxColumnSubTable + SUB_TABLE_RIGHT_MARGIN_COL; j++) {
			getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, StringUtils.EMPTY, mapCellFormat);
		}
		return columnIndex;
	}

	private void writeSheet3(String sheetName3, String filePath, XSSFWorkbook workbook,
			List<ReportAuditRekap> sheet1Results) throws Exception {
		XSSFSheet sheet3 = workbook.createSheet(sheetName3);
		CreationHelper helper = workbook.getCreationHelper();

		createPie(filePath, workbook, sheet1Results, sheet3, helper);
		createBar(filePath, workbook, sheet1Results, sheet3, helper);
	}

	private void writeSheet4(String sheetName5, String filePath, XSSFWorkbook workbook,
			List<ReportAuditRekap> sheet3Results) throws Exception {
		XSSFSheet sheet5 = workbook.createSheet(sheetName5);
		CreationHelper helper = workbook.getCreationHelper();

		createPieByTemplateName(filePath, workbook, sheet3Results, sheet5, helper);
		createBarByTemplateName(filePath, workbook, sheet3Results, sheet5, helper);
	}
	
	@SuppressWarnings("rawtypes")
	private void createPie(String filePath, XSSFWorkbook workbook, List<ReportAuditRekap> sheet1Results,
			XSSFSheet sheet, CreationHelper helper) throws IOException, FileNotFoundException {
		PieDataset pieDataSet = createDateSet(sheet1Results);
		File chartPath = printPie(pieDataSet, filePath);
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();
		anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);

		int pieChartIndex = workbook.addPicture(FileUtil.readBytesFromFile(chartPath), Workbook.PICTURE_TYPE_JPEG);

		anchor.setCol1(0);
		anchor.setRow1(1);
		anchor.setCol2(1);
		anchor.setRow2(1);

		Picture pictChart = drawing.createPicture(anchor, pieChartIndex);
		pictChart.resize();

		chartPath.delete();
	}

	@SuppressWarnings("rawtypes")
	private void createPieByTemplateName(String filePath, XSSFWorkbook workbook, List<ReportAuditRekap> sheet3Results,
			XSSFSheet sheet, CreationHelper helper) throws IOException, FileNotFoundException {
		PieDataset pieDataSet = createDateSetByTemplateName(sheet3Results);
		File chartPath = printPieByTemplateName(pieDataSet, filePath);
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();
		anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);

		int pieChartIndex = workbook.addPicture(FileUtil.readBytesFromFile(chartPath), Workbook.PICTURE_TYPE_JPEG);

		anchor.setCol1(0);
		anchor.setRow1(1);
		anchor.setCol2(1);
		anchor.setRow2(1);

		Picture pictChart = drawing.createPicture(anchor, pieChartIndex);
		pictChart.resize();

		chartPath.delete();
	}
	
	private File printPie(PieDataset pieDataSet, String filePath) throws IOException {
		// JFreeChart chart = ChartFactory.createPieChart3D(PIE_CHART_TITLE, pieDateSet,
		// true, true, false);

		JFreeChart chart = ChartFactory.createPieChart(PIE_CHART_TITLE, pieDataSet, true, true, false);

		PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{1}",
				NumberFormat.getInstance(), NumberFormat.getPercentInstance());

//		PiePlot3D plot = (PiePlot3D) chart.getPlot();
//		plot.setForegroundAlpha(0.5f);
//		plot.setStartAngle(360);
//		plot.setInteriorGap(0.02);
//		plot.setSimpleLabels(true);
//		plot.setLabelGenerator(labelGenerator);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSimpleLabels(true);
		plot.setLabelGenerator(labelGenerator);

		int width = 480;
		int height = 360;
		File pieChartImage = new File(filePath + "pie-chart" + CommonConstants.SEPARATOR_DASH
				+ System.currentTimeMillis() + CommonConstants.FILE_TYPE_JPEG);

		ChartUtilities.saveChartAsJPEG(pieChartImage, chart, width, height);

		return pieChartImage;
	}

	private File printPieByTemplateName(PieDataset pieDataSet, String filePath) throws IOException {
		// JFreeChart chart = ChartFactory.createPieChart3D(PIE_CHART_TITLE, pieDateSet,
		// true, true, false);

		JFreeChart chart = ChartFactory.createPieChart(PIE_CHART_TITLE, pieDataSet, true, true, false);

		PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{1}",
				NumberFormat.getInstance(), NumberFormat.getPercentInstance());

//		PiePlot3D plot = (PiePlot3D) chart.getPlot();
//		plot.setForegroundAlpha(0.5f);
//		plot.setStartAngle(360);
//		plot.setInteriorGap(0.02);
//		plot.setSimpleLabels(true);
//		plot.setLabelGenerator(labelGenerator);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSimpleLabels(true);
		plot.setLabelGenerator(labelGenerator);

		int width = 480;
		int height = 360;
		File pieChartImage = new File(filePath + "pie-chart" + CommonConstants.SEPARATOR_DASH
				+ System.currentTimeMillis() + CommonConstants.FILE_TYPE_JPEG);

		ChartUtilities.saveChartAsJPEG(pieChartImage, chart, width, height);

		return pieChartImage;
	}
	
	private PieDataset createDateSet(List<ReportAuditRekap> result) {
		DefaultPieDataset dataSet = new DefaultPieDataset();

		for (ReportAuditRekap reportAuditRekap : result) {
			dataSet.setValue(runnableFacesUtil.retrieveLocaleMessage(reportAuditRekap.getTypeAuditEn(),
					reportAuditRekap.getTypeAuditIn()), reportAuditRekap.getTotalAudit());
		}

		return dataSet;
	}

	private PieDataset createDateSetByTemplateName(List<ReportAuditRekap> result) {
		DefaultPieDataset dataSet = new DefaultPieDataset();

		for (ReportAuditRekap reportAuditRekap : result) {
			dataSet.setValue(runnableFacesUtil.retrieveLocaleMessage(reportAuditRekap.getAuditTemplateNameEn(),
					reportAuditRekap.getAuditTemplateNameIn()), reportAuditRekap.getTotalAudit());
		}

		return dataSet;
	}
	
	@SuppressWarnings("rawtypes")
	private void createBar(String filePath, XSSFWorkbook workbook, List<ReportAuditRekap> sheet1Results,
			XSSFSheet sheet, CreationHelper helper) throws IOException, FileNotFoundException {
		DefaultCategoryDataset barDataSet = createBarDataSet(sheet1Results);
		File barPath = printBar(barDataSet, filePath);
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();
		anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);

		int barChartIndex = workbook.addPicture(FileUtil.readBytesFromFile(barPath), Workbook.PICTURE_TYPE_JPEG);

		anchor.setCol1(12);
		anchor.setRow1(1);
		anchor.setCol2(13);
		anchor.setRow2(1);

		Picture picBar = drawing.createPicture(anchor, barChartIndex);
		picBar.resize();

		barPath.delete();
	}

	@SuppressWarnings("rawtypes")
	private void createBarByTemplateName(String filePath, XSSFWorkbook workbook, List<ReportAuditRekap> sheet3Results,
			XSSFSheet sheet, CreationHelper helper) throws IOException, FileNotFoundException {
		DefaultCategoryDataset barDataSet = createBarDataSetByTemplateName(sheet3Results);
		File barPath = printBarByTemplateName(barDataSet, filePath);
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();
		anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);

		int barChartIndex = workbook.addPicture(FileUtil.readBytesFromFile(barPath), Workbook.PICTURE_TYPE_JPEG);

		anchor.setCol1(12);
		anchor.setRow1(1);
		anchor.setCol2(13);
		anchor.setRow2(1);

		Picture picBar = drawing.createPicture(anchor, barChartIndex);
		picBar.resize();

		barPath.delete();
	}
	
	private File printBar(DefaultCategoryDataset dataSet, String filePath) throws IOException {
//		JFreeChart chart = ChartFactory.createBarChart3D("", "", "", dataSet);
		JFreeChart chart = ChartFactory.createBarChart("", "", "", dataSet);

		CategoryPlot plot = (CategoryPlot) chart.getCategoryPlot();
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.TOP_CENTER);
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setItemMargin(0);
		renderer.setPositiveItemLabelPositionFallback(position);

		int width = 480;
		int height = 360;

		File barChartImage = new File(filePath + "bar-chart" + CommonConstants.SEPARATOR_DASH
				+ System.currentTimeMillis() + CommonConstants.FILE_TYPE_JPEG);

		ChartUtilities.saveChartAsJPEG(barChartImage, chart, width, height);

		return barChartImage;
	}

	private File printBarByTemplateName(DefaultCategoryDataset dataSet, String filePath) throws IOException {
//		JFreeChart chart = ChartFactory.createBarChart3D("", "", "", dataSet);
		JFreeChart chart = ChartFactory.createBarChart("", "", "", dataSet);

		CategoryPlot plot = (CategoryPlot) chart.getCategoryPlot();
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.TOP_CENTER);
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setItemMargin(0);
		renderer.setPositiveItemLabelPositionFallback(position);

		int width = 480;
		int height = 360;

		File barChartImage = new File(filePath + "bar-chart" + CommonConstants.SEPARATOR_DASH
				+ System.currentTimeMillis() + CommonConstants.FILE_TYPE_JPEG);

		ChartUtilities.saveChartAsJPEG(barChartImage, chart, width, height);

		return barChartImage;
	}
	
	private DefaultCategoryDataset createBarDataSet(List<ReportAuditRekap> sheet1Results) {
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

		for (ReportAuditRekap reportAuditRekap : sheet1Results) {
			dataSet.setValue(reportAuditRekap.getTotalAudit(), runnableFacesUtil.retrieveLocaleMessage(
					reportAuditRekap.getTypeAuditEn(), reportAuditRekap.getTypeAuditIn()), "Total Audit");

			dataSet.setValue(reportAuditRekap.getTindakLanjutYes(), runnableFacesUtil.retrieveLocaleMessage(
					reportAuditRekap.getTypeAuditEn(), reportAuditRekap.getTypeAuditIn()), "Perlu Tindak Lanjut");

			dataSet.setValue(reportAuditRekap.getTindakLanjutNo(), runnableFacesUtil.retrieveLocaleMessage(
					reportAuditRekap.getTypeAuditEn(), reportAuditRekap.getTypeAuditIn()), "Tidak Ada Tindak Lanjut");

			dataSet.setValue(reportAuditRekap.getInProgress(), runnableFacesUtil.retrieveLocaleMessage(
					reportAuditRekap.getTypeAuditEn(), reportAuditRekap.getTypeAuditIn()), "In Progress");

			dataSet.setValue(reportAuditRekap.getClosed(), runnableFacesUtil.retrieveLocaleMessage(
					reportAuditRekap.getTypeAuditEn(), reportAuditRekap.getTypeAuditIn()), "Closed");

			dataSet.setValue(reportAuditRekap.getMeetSla(), runnableFacesUtil.retrieveLocaleMessage(
					reportAuditRekap.getTypeAuditEn(), reportAuditRekap.getTypeAuditIn()), "Meet SLA");

			dataSet.setValue(reportAuditRekap.getBeforeSla(), runnableFacesUtil.retrieveLocaleMessage(
					reportAuditRekap.getTypeAuditEn(), reportAuditRekap.getTypeAuditIn()), "Before SLA");

			dataSet.setValue(reportAuditRekap.getOverSla(), runnableFacesUtil.retrieveLocaleMessage(
					reportAuditRekap.getTypeAuditEn(), reportAuditRekap.getTypeAuditIn()), "Over SLA");
		}

		return dataSet;
	}

	private DefaultCategoryDataset createBarDataSetByTemplateName(List<ReportAuditRekap> sheet3Results) {
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

		for (ReportAuditRekap reportAuditRekap : sheet3Results) {
			dataSet.setValue(reportAuditRekap.getTotalAudit(), runnableFacesUtil.retrieveLocaleMessage(
					reportAuditRekap.getAuditTemplateNameEn(), reportAuditRekap.getAuditTemplateNameIn()), "Total Audit");

			dataSet.setValue(reportAuditRekap.getTindakLanjutYes(), runnableFacesUtil.retrieveLocaleMessage(
					reportAuditRekap.getAuditTemplateNameEn(), reportAuditRekap.getAuditTemplateNameIn()), "Perlu Tindak Lanjut");

			dataSet.setValue(reportAuditRekap.getTindakLanjutNo(), runnableFacesUtil.retrieveLocaleMessage(
					reportAuditRekap.getAuditTemplateNameEn(), reportAuditRekap.getAuditTemplateNameIn()), "Tidak Ada Tindak Lanjut");

			dataSet.setValue(reportAuditRekap.getInProgress(), runnableFacesUtil.retrieveLocaleMessage(
					reportAuditRekap.getAuditTemplateNameEn(), reportAuditRekap.getAuditTemplateNameIn()), "In Progress");

			dataSet.setValue(reportAuditRekap.getClosed(), runnableFacesUtil.retrieveLocaleMessage(
					reportAuditRekap.getAuditTemplateNameEn(), reportAuditRekap.getAuditTemplateNameIn()), "Closed");

			dataSet.setValue(reportAuditRekap.getMeetSla(), runnableFacesUtil.retrieveLocaleMessage(
					reportAuditRekap.getAuditTemplateNameEn(), reportAuditRekap.getAuditTemplateNameIn()), "Meet SLA");

			dataSet.setValue(reportAuditRekap.getBeforeSla(), runnableFacesUtil.retrieveLocaleMessage(
					reportAuditRekap.getAuditTemplateNameEn(), reportAuditRekap.getAuditTemplateNameIn()), "Before SLA");

			dataSet.setValue(reportAuditRekap.getOverSla(), runnableFacesUtil.retrieveLocaleMessage(
					reportAuditRekap.getAuditTemplateNameEn(), reportAuditRekap.getAuditTemplateNameIn()), "Over SLA");
		}

		return dataSet;
	}
	
	private void writeObjectToExcelSheet2(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex,
			int rowNum, ReportAuditRekap arrObj, int columnIndex) throws Exception {

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, rowNum, mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getTypeAuditEn(), arrObj.getTypeAuditIn()),
				mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTotalAudit(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTindakLanjutYes(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTindakLanjutNo(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getInProgress(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getClosed(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getMeetSla(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getBeforeSla(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getOverSla(), mapCellFormat);
	}

	private void writeObjectToExcelSheet3(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex,
			int rowNum, ReportAuditRekap arrObj, int columnIndex) throws Exception {

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, rowNum, mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getAuditTemplateNameEn(), arrObj.getAuditTemplateNameIn()),
				mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTotalAudit(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTindakLanjutYes(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTindakLanjutNo(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getInProgress(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getClosed(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getMeetSla(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getBeforeSla(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getOverSla(), mapCellFormat);
	}
	
	private List<String> buildListColumnNameRow1Sheet1() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Nama Template Audit");
		listColumnNameTemp.add("Tipe Audit");
		listColumnNameTemp.add("Nama Temuan");
		listColumnNameTemp.add("Temuan Pemeriksaan");
		listColumnNameTemp.add("Tanggapan Bank");
		listColumnNameTemp.add("Komitmen Bank");
		listColumnNameTemp.add("Direktorat");
		listColumnNameTemp.add("PIC 1");
		listColumnNameTemp.add("PIC 2");
		listColumnNameTemp.add("PIC 3");
		listColumnNameTemp.add("Target Date");
		listColumnNameTemp.add("Revisi Target I");
		listColumnNameTemp.add("Revisi Target II");
		listColumnNameTemp.add("Revisi Target III");
		listColumnNameTemp.add("Status Tindak Lanjut");
		listColumnNameTemp.add("Status Verifikiasi");
		listColumnNameTemp.add("Attachment Dokumen Tindak Lanjut");
		listColumnNameTemp.add("Keterangan");
		listColumnNameTemp.add("Attachment Surat ke OJK/BI ");
		listColumnNameTemp.add("SLA");
		listColumnNameTemp.add("Done");
		listColumnNameTemp.add("Not Done");

		return listColumnNameTemp;
	}

	private List<String> buildListColumnNameRow1Sheet2() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
//		listColumnNameTemp.add("Kategori Review");
		listColumnNameTemp.add("Tipe Audit");
		listColumnNameTemp.add("Total");
		listColumnNameTemp.add("Tindak Lanjut");
		listColumnNameTemp.add("Status Tindak Lanjut(Yes)");
		listColumnNameTemp.add("SLA");

		return listColumnNameTemp;
	}

	private List<String> buildListColumnNameRow2Sheet2() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Perlu Tindak Lanjut");
		listColumnNameTemp.add("Tidak Ada Tindak Lanjut");
		listColumnNameTemp.add("In Progress");
		listColumnNameTemp.add("Closed");
		listColumnNameTemp.add("Meet SLA");
		listColumnNameTemp.add("Before SLA");
		listColumnNameTemp.add("Over SLA");

		return listColumnNameTemp;
	}

	private List<String> buildListColumnNameRow1Sheet3() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Nama Template Audit");
		listColumnNameTemp.add("Total");
		listColumnNameTemp.add("Tindak Lanjut");
		listColumnNameTemp.add("Status Tindak Lanjut(Yes)");
		listColumnNameTemp.add("SLA");

		return listColumnNameTemp;
	}

	private List<String> buildListColumnNameRow2Sheet3() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Perlu Tindak Lanjut");
		listColumnNameTemp.add("Tidak Ada Tindak Lanjut");
		listColumnNameTemp.add("In Progress");
		listColumnNameTemp.add("Closed");
		listColumnNameTemp.add("Meet SLA");
		listColumnNameTemp.add("Before SLA");
		listColumnNameTemp.add("Over SLA");

		return listColumnNameTemp;
	}
	
	public void writeExcelHeader(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat) throws Exception {

		String auditorName = "";
		String statusFollowupName = "";
		String statusVerificationName = "";
//		String divisonName = "";

		int row = 0;
		int columnStart = 0;

		SimpleDateFormat printTimestamp = new SimpleDateFormat(CommonConstants.INPUT_DATE_TIME_FORMAT);
		String excelPrintDate = printTimestamp.format(new Date());

		// Create Header [start]
		CellStyle cfHeaderValue = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_VALUE);
		CellStyle cfHeaderLabel = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_LABEL);
		// Create Header [end]

//		searchCreationDateFrom = getSearchCriteriaValue(WHERE_CREATION_DATE_FROM);
//		searchCreationDateTo = getSearchCriteriaValue(WHERE_CREATION_DATE_TO);
		searchAuditor = getSearchCriteriaValue(WHERE_AUDITOR);
		searchTargetDateFrom = getSearchCriteriaValue(WHERE_TARGET_DATE_FROM);
		searchTargetDateTo = getSearchCriteriaValue(WHERE_TARGET_DATE_TO);
		searchAuditDateFrom = getSearchCriteriaValue(WHERE_AUDIT_DATE_FROM);
		searchAuditDateTo = getSearchCriteriaValue(WHERE_AUDIT_DATE_TO);
		searchStatusFollowup = getSearchCriteriaValue(WHERE_STATUS_FOLLOWUP);
		searchStatusVerification = getSearchCriteriaValue(WHERE_STATUS_VERIFICATION);
//		searchFindingName = getSearchCriteriaValue(WHERE_FINDING_NAME);

//		String from = "";
//		String to = "";
		String targetFrom = "";
		String targetTo = "";
		String auditFrom = "";
		String auditTo = "";

//		if (StringUtils.isNotBlank(searchCreationDateFrom)) {
//			try {
//				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchCreationDateFrom);
//				from = DateUtil.dateToString(tmpDate);
//			} catch (Exception ex) {
//				from = "";
//			}
//		}
//		
//		if (StringUtils.isNotBlank(searchCreationDateTo)) {
//			try {
//				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchCreationDateTo);
//				to = DateUtil.dateToString(tmpDate);
//			} catch (Exception ex) {
//				to = "";
//			}
//		}

		if (StringUtils.isNotBlank(searchTargetDateFrom)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchTargetDateFrom);
				targetFrom = DateUtil.dateToString(tmpDate);
			} catch (Exception ex) {
				targetFrom = "";
			}
		}

		if (StringUtils.isNotBlank(searchTargetDateTo)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchTargetDateTo);
				targetTo = DateUtil.dateToString(tmpDate);
			} catch (Exception ex) {
				targetTo = "";
			}
		}

		if (StringUtils.isNotBlank(searchAuditDateFrom)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchAuditDateFrom);
				auditFrom = DateUtil.dateToString(tmpDate);
			} catch (Exception ex) {
				auditFrom = "";
			}
		}

		if (StringUtils.isNotBlank(searchAuditDateTo)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchAuditDateTo);
				auditTo = DateUtil.dateToString(tmpDate);
			} catch (Exception ex) {
				auditTo = "";
			}
		}

		// get name En dan In
		ParameterDetail getAuditorName = parameterDetailService.getParameterDetailByParamDtlCode(searchAuditor);
		ParameterDetail getStatusFollowupName = parameterDetailService
				.getParameterDetailByParamDtlCode(searchStatusFollowup);
		ParameterDetail getStatusVerificationName = parameterDetailService
				.getParameterDetailByParamDtlCode(searchStatusVerification);
		
		if (getAuditorName != null) {
			auditorName = runnableFacesUtil.retrieveLocaleMessage(getAuditorName.getNameEn(),
					getAuditorName.getNameIn());
		}
		if (getStatusFollowupName != null) {
			statusFollowupName = runnableFacesUtil.retrieveLocaleMessage(getStatusFollowupName.getNameEn(),
					getStatusFollowupName.getNameIn());
		}
		if (getStatusVerificationName != null) {
			statusVerificationName = runnableFacesUtil.retrieveLocaleMessage(getStatusVerificationName.getNameEn(),
					getStatusVerificationName.getNameIn());
		}
		
		// Create Header Title [Start]
		if (sheet.getSheetName().equals(SHEET_NAME_REPORT_AUDIT_DETAIL)) {
			reportUtil.writeCellTitle(sheet, 4, "Report Audit Detail", mapCellFormat);
		} else {
			reportUtil.writeCellTitle(sheet, 4, "Report Audit Rekap", mapCellFormat);
		}

		row++;
		row++;

		/*
		 * // Label Creation Date reportUtil.writeCell(sheet, row, columnStart,
		 * runnableFacesUtil.retrieveMessage("formReportAuditDetailCreationDate") +
		 * " : ", cfHeaderLabel);
		 * 
		 * // Value Creation Date reportUtil.writeCell(sheet, row, columnStart + 1, from
		 * + " " + ((StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to)) ?
		 * runnableFacesUtil.retrieveMessage("textUntil") : "") + " " + to,
		 * cfHeaderValue); row++;
		 */

		// Label Audit Date
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportAuditDetailAuditDate") + " : ", cfHeaderLabel);

		// Value Audit Date
		reportUtil.writeCell(sheet, row, columnStart + 1,
				auditFrom + " "
						+ ((StringUtils.isNotBlank(auditFrom) && StringUtils.isNotBlank(auditTo))
								? runnableFacesUtil.retrieveMessage("textUntil")
								: "")
						+ " " + auditTo,
				cfHeaderValue);
		row++;

		// Label Target Date
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportAuditDetailTargetDate") + " : ", cfHeaderLabel);

		// Value Target Date
		reportUtil.writeCell(sheet, row, columnStart + 1,
				targetFrom + " "
						+ ((StringUtils.isNotBlank(targetFrom) && StringUtils.isNotBlank(targetTo))
								? runnableFacesUtil.retrieveMessage("textUntil")
								: "")
						+ " " + targetTo,
				cfHeaderValue);
		row++;

		// Label Auditor
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportAuditDetailAuditType") + " : ", cfHeaderLabel);

		// Value Auditor
		reportUtil.writeCell(sheet, row, columnStart + 1, auditorName, cfHeaderValue);
		row++;

		// Label Finding Name
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportAuditDetailFindingName") + " : ", cfHeaderLabel);

		// Value Finding Name
		reportUtil.writeCell(sheet, row, columnStart + 1, 
				runnableFacesUtil.retrieveLocaleMessage(findingNameEn, findingNameIn)
				, cfHeaderValue);
		row++;
		
		// Label Audit Template Name
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportAuditDetailTemplateName") + " : ", cfHeaderLabel);

		// Value Audit Template Name
		reportUtil.writeCell(sheet, row, columnStart + 1, templateName, cfHeaderValue);
		row++;
		
		// Label Division
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportAuditDetailDivision") + " : ", cfHeaderLabel);

		// Value Divison
		reportUtil.writeCell(sheet, row, columnStart + 1, divisionName, cfHeaderValue);
		row++;

		// Label Status Followup
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportAuditDetailStatusFollowup") + " : ", cfHeaderLabel);

		// Value Status Followup
		reportUtil.writeCell(sheet, row, columnStart + 1, statusFollowupName, cfHeaderValue);
		row++;

		// Label Status Verification
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportAuditDetailStatusVerification") + " : ", cfHeaderLabel);

		// Value Status Verification
		reportUtil.writeCell(sheet, row, columnStart + 1, statusVerificationName, cfHeaderValue);
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

	public ReportAuditDetailService getReportAuditDetailService() {
		return reportAuditDetailService;
	}

	public void setReportAuditDetailService(ReportAuditDetailService reportAuditDetailService) {
		this.reportAuditDetailService = reportAuditDetailService;
	}

	@SuppressWarnings("rawtypes")
	public List<? extends SearchObject> getSearchCriteria() {
		return searchCriteria;
	}

	@SuppressWarnings("rawtypes")
	public void setSearchCriteria(List<? extends SearchObject> searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

//	public String getSearchCreationDateFrom() {
//		return searchCreationDateFrom;
//	}
//
//	public void setSearchCreationDateFrom(String searchCreationDateFrom) {
//		this.searchCreationDateFrom = searchCreationDateFrom;
//	}
//
//	public String getSearchCreationDateTo() {
//		return searchCreationDateTo;
//	}
//
//	public void setSearchCreationDateTo(String searchCreationDateTo) {
//		this.searchCreationDateTo = searchCreationDateTo;
//	}

	public String getSearchAuditor() {
		return searchAuditor;
	}

	public void setSearchAuditor(String searchAuditor) {
		this.searchAuditor = searchAuditor;
	}

	public CommonReportUtil getReportUtil() {
		return reportUtil;
	}

	public void setReportUtil(CommonReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}

	public TrcAuditPICFollowupService getTrcAuditPICFollowupService() {
		return trcAuditPICFollowupService;
	}

	public void setTrcAuditPICFollowupService(TrcAuditPICFollowupService trcAuditPICFollowupService) {
		this.trcAuditPICFollowupService = trcAuditPICFollowupService;
	}

	public Integer getTotalDoneTemp() {
		return totalDoneTemp;
	}

	public void setTotalDoneTemp(Integer totalDoneTemp) {
		this.totalDoneTemp = totalDoneTemp;
	}

	public Integer getTotalNotDoneTemp() {
		return totalNotDoneTemp;
	}

	public void setTotalNotDoneTemp(Integer totalNotDoneTemp) {
		this.totalNotDoneTemp = totalNotDoneTemp;
	}

	public ReportAuditRekapService getReportAuditRekapService() {
		return reportAuditRekapService;
	}

	public void setReportAuditRekapService(ReportAuditRekapService reportAuditRekapService) {
		this.reportAuditRekapService = reportAuditRekapService;
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

	public String getSearchAuditDateFrom() {
		return searchAuditDateFrom;
	}

	public void setSearchAuditDateFrom(String searchAuditDateFrom) {
		this.searchAuditDateFrom = searchAuditDateFrom;
	}

	public String getSearchAuditDateTo() {
		return searchAuditDateTo;
	}

	public void setSearchAuditDateTo(String searchAuditDateTo) {
		this.searchAuditDateTo = searchAuditDateTo;
	}

	public String getSearchStatusFollowup() {
		return searchStatusFollowup;
	}

	public void setSearchStatusFollowup(String searchStatusFollowup) {
		this.searchStatusFollowup = searchStatusFollowup;
	}

	public String getSearchStatusVerification() {
		return searchStatusVerification;
	}

	public void setSearchStatusVerification(String searchStatusVerification) {
		this.searchStatusVerification = searchStatusVerification;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getFindingNameIn() {
		return findingNameIn;
	}

	public void setFindingNameIn(String findingNameIn) {
		this.findingNameIn = findingNameIn;
	}

	public String getFindingNameEn() {
		return findingNameEn;
	}

	public void setFindingNameEn(String findingNameEn) {
		this.findingNameEn = findingNameEn;
	}
	
}
