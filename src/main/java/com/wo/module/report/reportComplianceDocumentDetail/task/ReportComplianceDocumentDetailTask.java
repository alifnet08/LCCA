package com.wo.module.report.reportComplianceDocumentDetail.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

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
import com.wo.module.report.reportComplianceDocumentDetail.constant.ReportComplianceDocumentDetailConstants;
import com.wo.module.report.reportComplianceDocumentDetail.model.ReportComplianceDocumentDetail;
import com.wo.module.report.reportComplianceDocumentDetail.service.ReportComplianceDocumentDetailService;
import com.wo.module.report.reportComplianceDocumentRekap.model.ReportComplianceDocumentRekap;
import com.wo.module.report.reportComplianceDocumentRekap.service.ReportComplianceDocumentRekapService;
import com.wo.module.report.reportGen.constant.ReportSheetNameConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.service.ReportGenService;

public class ReportComplianceDocumentDetailTask
		implements Runnable, ReportSheetNameConstant, ReportComplianceDocumentDetailConstants {

	static Logger logger = Logger.getLogger(ReportComplianceDocumentDetailTask.class);

	public final static String COMPLIANCE_DOC_TYPE_REPORT_COMPLIANCE_DOCUMENT_DETAIL = "Report Compliance Reporting Detail";
	private static final String PIE_CHART_TITLE_TOTAL_COMPLIANCE_DOCUMENT = "Total Compliance Reporting";
	private static final String PIE_CHART_TITLE_TOTAL_KELOMPOK_HUKT = "Total Kelompok HUK";

	private Long reportGenId;
	private String userNikName;

	private ReportGenService reportGenService;
	private ParameterDetailService parameterDetailService;
	private RunnableFacesUtil runnableFacesUtil;
	private ReportComplianceDocumentDetailService reportComplianceDocumentDetailService;
	private ReportComplianceDocumentRekapService reportComplianceDocumentRekapService;

	@SuppressWarnings("rawtypes")
	private List<? extends SearchObject> searchCriteria;

	private String searchCreationDateFrom;
	private String searchCreationDateTo;
	private String searchDocumentDateFrom;
	private String searchDocumentDateTo;
	private String searchDocumentType;
	private String searchReceivedDocumentDateFrom;
	private String searchReceivedDocumentDateTo;

	private int chartCol1;
	private int chartCol2;

	private CommonReportUtil reportUtil = new CommonReportUtil();

	@SuppressWarnings("rawtypes")
	public ReportComplianceDocumentDetailTask(Long reportGenId, ReportGenService reportGenService,
			ParameterDetailService parameterDetailService, RunnableFacesUtil runnableFacesUtil,
			ReportComplianceDocumentDetailService reportComplianceDocumentDetailService,
			ReportComplianceDocumentRekapService reportComplianceDocumentRekapService,
			List<? extends SearchObject> searchCriteria, String userNikName) {
		super();
		this.reportGenId = reportGenId;
		this.reportGenService = reportGenService;
		this.parameterDetailService = parameterDetailService;
		this.reportComplianceDocumentDetailService = reportComplianceDocumentDetailService;
		this.reportComplianceDocumentRekapService = reportComplianceDocumentRekapService;
		this.setRunnableFacesUtil(runnableFacesUtil);
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

	private UploadedFileWO uploadFileToApi(String absoluteResultFilePath) throws Exception {
		SCMApiUpload saf = new SCMApiUploadImpl(absoluteResultFilePath, parameterDetailService,
				COMPLIANCE_DOC_TYPE_REPORT_COMPLIANCE_DOCUMENT_DETAIL);
		saf.upload();
		return saf.getAsUploadedFileWO();
	}

	private String writeToFile(List<Integer> listColumnView) throws Exception {
//		String sheetName1 = SHEET_NAME_REPORT_COMPLIANCE_DOCUMENT_DETAIL;
		String sheetName1 = SHEET_NAME_REPORT_COMPLIANCE_REPORTING_DETAIL;

		String fileNamePrefix = "ReportComplianceReporting";

		String fileName = getReportUtil().generateFileName(fileNamePrefix, userNikName);
		String filePath = getReportUtil().retrieveFilePath(parameterDetailService);

		Path dirPath = Paths.get(filePath);
		if (!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}

		XSSFWorkbook workbook = null;
		FileOutputStream fileOutputStream = null;

		List<String> buildListColumnNameRow1Sheet1 = buildListColumnNameRow1Sheet1();

		List<ReportComplianceDocumentDetail> sheet1Results = (List<ReportComplianceDocumentDetail>) reportComplianceDocumentDetailService
				.getReportComplianceDocumentDetailByData(searchCriteria);

		List<Integer> listColumnViewByTipeDocument = new ArrayList<Integer>();
		List<Integer> listColumnViewByKelompokHuk = new ArrayList<Integer>();

		try {

			fileOutputStream = new FileOutputStream(filePath + fileName);
			workbook = new XSSFWorkbook();

			writeSheetComplianceDocumentDetail(listColumnView, sheetName1, workbook, buildListColumnNameRow1Sheet1,
					sheet1Results);

			writeSheetComplianceDocumentRekap(workbook, listColumnViewByTipeDocument, listColumnViewByKelompokHuk);

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

	private void writeSheetComplianceDocumentDetail(List<Integer> listColumnView, String sheetName1,
			XSSFWorkbook workbook, List<String> buildListColumnNameRow1Sheet1,
			List<ReportComplianceDocumentDetail> sheet1Results) throws Exception {

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
		int rowIndex = 11;

		if (buildListColumnNameRow1Sheet1 != null && !buildListColumnNameRow1Sheet1.isEmpty()) {

			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow1Sheet1) {

				getReportUtil().writeCell(sheet1, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);

				columnIndex++;
			}
		}
		// write sheet 1 column header [end]

		// write sheet 1 data detail [start]
		if (sheet1Results != null && !sheet1Results.isEmpty()) {
			rowIndex += 1;

			int rowNum = 1;
			for (ReportComplianceDocumentDetail arrObj : sheet1Results) {
				int columnIndex = 0;

				writeObjectToExcelSheet(sheet1, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);

				rowIndex++;
				rowNum++;
			}

		}
		// write sheet 1 data detail [end]
		sheet1.autoSizeColumn(10);
	}

	private void writeSheetComplianceDocumentRekap(XSSFWorkbook workbook, List<Integer> listColumnViewByTipeDocument,
			List<Integer> listColumnViewByKelompokHuk) throws Exception {
//		String sheetName2 = SHEET_NAME_REPORT_COMPLIANCE_DOCUMENT_REKAP_BY_TIPE_DOCUMENT;
		String sheetName2 = SHEET_NAME_REPORT_COMPLIANCE_DOCUMENT_REKAP_BY_TIPE_REGULATION;
		String sheetName3 = SHEET_NAME_REPORT_COMPLIANCE_DOCUMENT_REKAP_BY_KELOMPOK_HUK;
		String sheetName4 = SHEET_NAME_REPORT_COMPLIANCE_DOCUMENT_REKAP_DIAGRAM;

		List<String> buildListColumnNameRowSheet2 = buildListColumnNameRowSheet2();
		List<String> buildListColumnNameRowSheet3 = buildListColumnNameRowSheet3();

		List<ReportComplianceDocumentRekap> sheet2Results = (List<ReportComplianceDocumentRekap>) reportComplianceDocumentRekapService
				.getReportComplianceDocumentRekapByTipeDocumentData(searchCriteria);

		List<ReportComplianceDocumentRekap> sheet3Results = (List<ReportComplianceDocumentRekap>) reportComplianceDocumentRekapService
				.getReportComplianceDocumentRekapByKelompokHukData(searchCriteria);

		XSSFSheet sheet2 = workbook.createSheet(sheetName2);
		XSSFSheet sheet3 = workbook.createSheet(sheetName3);

		String filePath = getReportUtil().retrieveFilePath(parameterDetailService);

		// setting column size [Start]
		// sheet 1
		if (listColumnViewByTipeDocument != null) {
			for (int i = 0; i < listColumnViewByTipeDocument.size(); i++) {
				sheet2.setColumnWidth(i, listColumnViewByTipeDocument.get(i));
				sheet2.autoSizeColumn(i);
			}
		}
		// sheet 2
		if (listColumnViewByKelompokHuk != null) {
			for (int i = 0; i < listColumnViewByKelompokHuk.size(); i++) {
				sheet3.setColumnWidth(i, listColumnViewByKelompokHuk.get(i));
				sheet3.autoSizeColumn(i);
			}
		}
		// setting column size [End]

		Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
		getReportUtil().createExcelCellFormat(workbook, mapCellFormat);

		this.writeExcelHeader(sheet2, mapCellFormat);
		this.writeExcelHeader(sheet3, mapCellFormat);

		CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER);

		// write sheet 2 column header [start]
		int rowIndexSheet1 = 11;

		if (buildListColumnNameRowSheet2 != null && !buildListColumnNameRowSheet2.isEmpty()) {

			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRowSheet2) {

				getReportUtil().writeCell(sheet2, rowIndexSheet1, columnIndex, columnNameAlias, cfColumnHeader);

				columnIndex++;
			}
		}
		// write sheet 2 column header [end]

		// write sheet 2 data detail [start]
		if (sheet2Results != null && !sheet2Results.isEmpty()) {
			rowIndexSheet1 += 1;

			int rowNum = 1;
			for (ReportComplianceDocumentRekap arrObj : sheet2Results) {
				int columnIndex = 0;

				writeObjectToExcelSheet2(sheet2, mapCellFormat, rowIndexSheet1, rowNum, arrObj, columnIndex);

				rowIndexSheet1++;
				rowNum++;
			}

		}
		// write sheet 2 data detail [end]

		// write sheet 3 column header [start]
		int rowIndexSheet2 = 11;

		if (buildListColumnNameRowSheet3 != null && !buildListColumnNameRowSheet3.isEmpty()) {

			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRowSheet3) {

				getReportUtil().writeCell(sheet3, rowIndexSheet2, columnIndex, columnNameAlias, cfColumnHeader);

				columnIndex++;
			}
		}
		// write sheet 3 column header [end]

		// write sheet 3 data detail [start]
		if (sheet3Results != null && !sheet3Results.isEmpty()) {
			rowIndexSheet2 += 1;

			int rowNum = 1;
			for (ReportComplianceDocumentRekap arrObj : sheet3Results) {
				int columnIndex = 0;

				writeObjectToExcelSheet3(sheet3, mapCellFormat, rowIndexSheet2, rowNum, arrObj, columnIndex);

				rowIndexSheet2++;
				rowNum++;
			}

		}
		// write sheet 3 data detail [end]

		writeSheet4(sheetName4, filePath, workbook, sheet2Results, sheet3Results);
	}

	private void writeSheet4(String sheetName4, String filePath, XSSFWorkbook workbook,
			List<ReportComplianceDocumentRekap> sheet1Results, List<ReportComplianceDocumentRekap> sheet2Results)
			throws IOException, FileNotFoundException {
		XSSFSheet sheet4 = workbook.createSheet(sheetName4);
		CreationHelper helper = workbook.getCreationHelper();

		createPie(filePath, workbook, sheet1Results, sheet4, helper, PIE_CHART_TITLE_TOTAL_COMPLIANCE_DOCUMENT);
		createPie(filePath, workbook, sheet2Results, sheet4, helper, PIE_CHART_TITLE_TOTAL_KELOMPOK_HUKT);

	}

	@SuppressWarnings("rawtypes")
	private void createPie(String filePath, XSSFWorkbook workbook, List<ReportComplianceDocumentRekap> sheet2Results,
			XSSFSheet sheet, CreationHelper helper, String title) throws IOException, FileNotFoundException {
		PieDataset pieDataset = createDataset(sheet2Results);
		File chartPath = printPie(pieDataset, filePath, title);
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();
		anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);

		int pieChartIndex = workbook.addPicture(FileUtil.readBytesFromFile(chartPath), Workbook.PICTURE_TYPE_JPEG);

//		chartCol1 = 0;
//		chartCol2 = 1;

		anchor.setCol1(chartCol1);
		anchor.setRow1(1);
		anchor.setCol2(chartCol2);
		anchor.setRow2(1);

		chartCol1 += 10;
		chartCol2 += 10;

		Picture pictChart = drawing.createPicture(anchor, pieChartIndex);
		pictChart.resize();

		chartPath.delete();
	}

	private PieDataset createDataset(List<ReportComplianceDocumentRekap> result) {
		DefaultPieDataset dataset = new DefaultPieDataset();

		for (ReportComplianceDocumentRekap reportComplianceDocumentRekap : result) {

			if (StringUtils.isBlank(reportComplianceDocumentRekap.getTipeDokumenEn())
					|| StringUtils.isBlank(reportComplianceDocumentRekap.getTipeDokumenIn())) {
				dataset.setValue("Undefined", reportComplianceDocumentRekap.getTotal());
			} else {
				dataset.setValue(
						runnableFacesUtil.retrieveLocaleMessage(reportComplianceDocumentRekap.getTipeDokumenEn(),
								reportComplianceDocumentRekap.getTipeDokumenIn()),
						reportComplianceDocumentRekap.getTotal());
			}
		}
		return dataset;
	}

	private File printPie(PieDataset pieDataset, String filePath, String title) throws IOException {

//		JFreeChart chart = ChartFactory.createPieChart3D(
//				title,
//				pieDataset,
//		        true, 
//		        true,
//		        false);

		JFreeChart chart = ChartFactory.createPieChart(title, pieDataset, true, true, false);

		PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{1}",
				NumberFormat.getInstance(), NumberFormat.getPercentInstance());

//		PiePlot plot = (PiePlot) chart.getPlot();
//		plot.setForegroundAlpha( 0.5f );
//		plot.setStartAngle(360);
//		plot.setInteriorGap( 0.02 );
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

	private void writeObjectToExcelSheet2(XSSFSheet sheet2, Map<String, CellStyle> mapCellFormat, int rowIndex,
			int rowNum, ReportComplianceDocumentRekap arrObj, int columnIndex) throws Exception {

		getReportUtil().writeCellDetail(sheet2, rowIndex, columnIndex++, rowNum, mapCellFormat);

		getReportUtil().writeCellDetail(sheet2, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getTipeDokumenEn(), arrObj.getTipeDokumenIn()),
				mapCellFormat);

		getReportUtil().writeCellDetail(sheet2, rowIndex, columnIndex++, arrObj.getTotal(), mapCellFormat);
	}

	private void writeObjectToExcelSheet3(XSSFSheet sheet3, Map<String, CellStyle> mapCellFormat, int rowIndex,
			int rowNum, ReportComplianceDocumentRekap arrObj, int columnIndex) throws Exception {

		getReportUtil().writeCellDetail(sheet3, rowIndex, columnIndex++, rowNum, mapCellFormat);

		getReportUtil().writeCellDetail(sheet3, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getTipeDokumenEn(), arrObj.getTipeDokumenIn()),
				mapCellFormat);

		getReportUtil().writeCellDetail(sheet3, rowIndex, columnIndex++, arrObj.getTotal(), mapCellFormat);
	}

	private List<String> buildListColumnNameRowSheet2() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Tipe Peraturan");
		listColumnNameTemp.add("Total Compliance Reporting");

		return listColumnNameTemp;
	}

	private List<String> buildListColumnNameRowSheet3() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Kelompok HUK");
		listColumnNameTemp.add("Total Kelompok HUK");

		return listColumnNameTemp;
	}

	private void writeObjectToExcelSheet(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex,
			int rowNum, ReportComplianceDocumentDetail arrObj, int columnIndex) throws Exception {

//		sheet1.autoSizeColumn(0);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, rowNum, mapCellFormat);

//		sheet1.autoSizeColumn(1);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getTipeDokumenEn(), arrObj.getTipeDokumenIn()),
				mapCellFormat);

//		sheet1.autoSizeColumn(2);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getNoDokumen(), mapCellFormat);

//		sheet1.autoSizeColumn(3);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTanggalDokumen(), mapCellFormat);

//		sheet1.autoSizeColumn(4);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTanggalDiterima(), mapCellFormat);

//		sheet1.autoSizeColumn(5);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getMateri(), mapCellFormat);

//		sheet1.autoSizeColumn(6);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getKelompokHukEn(), arrObj.getKelompokHukIn()),
				mapCellFormat);

//		sheet1.autoSizeColumn(7);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getUnitKerjaPengusul(), mapCellFormat);

//		sheet1.autoSizeColumn(8);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getPicCompliance(), mapCellFormat);

//		sheet1.autoSizeColumn(9);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getKeterangan(), mapCellFormat);

//		sheet1.autoSizeColumn(10);
		getReportUtil().writeWrapCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getAttachmentDokumen(),
				mapCellFormat);
	}

	private List<String> buildListColumnNameRow1Sheet1() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Tipe Peraturan");
		listColumnNameTemp.add("No Peraturan");
		listColumnNameTemp.add("Tanggal Peraturan");
		listColumnNameTemp.add("Tanggal Diterima");
		listColumnNameTemp.add("Materi");
		listColumnNameTemp.add("Kelompok HUK");
		listColumnNameTemp.add("Direktorat");
		listColumnNameTemp.add("PIC Compliance");
		listColumnNameTemp.add("Keterangan");
		listColumnNameTemp.add("Lampiran Peraturan");

		return listColumnNameTemp;
	}

	public void writeExcelHeader(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat) throws Exception {

		String documentTypeName = "";

		int row = 0;
		int columnStart = 0;

		SimpleDateFormat printTimestamp = new SimpleDateFormat(CommonConstants.INPUT_DATE_TIME_FORMAT);
		String excelPrintDate = printTimestamp.format(new Date());

		// Create Header [start]
		CellStyle cfHeaderValue = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_VALUE);
		CellStyle cfHeaderLabel = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_LABEL);
		// Create Header [end]

		searchCreationDateFrom = getSearchCriteriaValue(WHERE_CREATION_DATE_FROM);
		searchCreationDateTo = getSearchCriteriaValue(WHERE_CREATION_DATE_TO);
		searchDocumentDateFrom = getSearchCriteriaValue(WHERE_DOCUMENT_DATE_FROM);
		searchDocumentDateTo = getSearchCriteriaValue(WHERE_DOCUMENT_DATE_TO);
		setSearchReceivedDocumentDateFrom(getSearchCriteriaValue(WHERE_RECEIVED_DOCUMENT_DATE_FROM));
		setSearchReceivedDocumentDateTo(getSearchCriteriaValue(WHERE_RECEIVED_DOCUMENT_DATE_TO));
		searchDocumentType = getSearchCriteriaValue(WHERE_DOCUMENT_TYPE);

		String from = "";
		String to = "";
		String dokumenFrom = "";
		String dokumenTo = "";
		String receivedDokumenFrom = "";
		String receivedDokumenTo = "";
		
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
		
		if (StringUtils.isNotBlank(searchDocumentDateFrom)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchDocumentDateFrom);
				dokumenFrom = DateUtil.dateToString(tmpDate);
			} catch (Exception ex) {
				dokumenFrom = "";
			}
		}
		
		if (StringUtils.isNotBlank(searchDocumentDateTo)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchDocumentDateTo);
				dokumenTo = DateUtil.dateToString(tmpDate);
			} catch (Exception ex) {
				dokumenTo = "";
			}
		}
		
		if (StringUtils.isNotBlank(searchReceivedDocumentDateFrom)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchReceivedDocumentDateFrom);
				receivedDokumenFrom = DateUtil.dateToString(tmpDate);
			} catch (Exception ex) {
				receivedDokumenFrom = "";
			}
		}
		
		if (StringUtils.isNotBlank(searchReceivedDocumentDateTo)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchReceivedDocumentDateTo);
				receivedDokumenTo = DateUtil.dateToString(tmpDate);
			} catch (Exception ex) {
				receivedDokumenTo = "";
			}
		}
		
		// get name En and In
		ParameterDetail getDocumentType = parameterDetailService.getParameterDetailByParamDtlCode(searchDocumentType);

		if (getDocumentType != null) {
			documentTypeName = runnableFacesUtil.retrieveLocaleMessage(getDocumentType.getNameEn(),
					getDocumentType.getNameIn());
		}

		// Create Header Title [Start]
		if (sheet.getSheetName().equals(SHEET_NAME_REPORT_COMPLIANCE_REPORTING_DETAIL)) {
			reportUtil.writeCellTitle(sheet, 4, "Report Compliance Reporting Detail", mapCellFormat);
		} else {
			reportUtil.writeCellTitle(sheet, 4, "Report Compliance Reporting Rekap", mapCellFormat);
		}
		

		row++;
		row++;

		// Label Creation Date
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportComplianceDocumentDetailCreationDate") + " : ",
				cfHeaderLabel);

		// Value Creation Date
		reportUtil.writeCell(sheet, row, columnStart + 1, from + " "
				+ ((StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to))
						? runnableFacesUtil.retrieveMessage("textUntil")
						: "")
				+ " " + to, cfHeaderValue);
		row++;

		// Label Document Date
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportComplianceReportingRegulationDate") + " : ",
				cfHeaderLabel);

		// Value Document Date
		reportUtil.writeCell(sheet, row, columnStart + 1, dokumenFrom + " "
				+ ((StringUtils.isNotBlank(dokumenFrom) && StringUtils.isNotBlank(dokumenTo))
						? runnableFacesUtil.retrieveMessage("textUntil")
						: "")
				+ " " + dokumenTo, cfHeaderValue);
		row++;

		// Label Document Date
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportComplianceReportingReceivedRegulationDate") + " : ",
				cfHeaderLabel);

		// Value Document Date
		reportUtil.writeCell(sheet, row, columnStart + 1,
				receivedDokumenFrom + " "
						+ ((StringUtils.isNotBlank(receivedDokumenFrom)
								&& StringUtils.isNotBlank(receivedDokumenTo))
										? runnableFacesUtil.retrieveMessage("textUntil")
										: "")
						+ " " + receivedDokumenTo,
				cfHeaderValue);
		row++;

		// Label Document Type
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportComplianceReportingRegulationType") + " : ",
				cfHeaderLabel);

		// Value Document Type
		reportUtil.writeCell(sheet, row, columnStart + 1, documentTypeName, cfHeaderValue);
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

	public ReportComplianceDocumentDetailService getReportComplianceDocumentDetailService() {
		return reportComplianceDocumentDetailService;
	}

	public void setReportComplianceDocumentDetailService(
			ReportComplianceDocumentDetailService reportComplianceDocumentDetailService) {
		this.reportComplianceDocumentDetailService = reportComplianceDocumentDetailService;
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

	public String getSearchDocumentDateFrom() {
		return searchDocumentDateFrom;
	}

	public void setSearchDocumentDateFrom(String searchDocumentDateFrom) {
		this.searchDocumentDateFrom = searchDocumentDateFrom;
	}

	public String getSearchDocumentDateTo() {
		return searchDocumentDateTo;
	}

	public void setSearchDocumentDateTo(String searchDocumentDateTo) {
		this.searchDocumentDateTo = searchDocumentDateTo;
	}

	public String getSearchDocumentType() {
		return searchDocumentType;
	}

	public void setSearchDocumentType(String searchDocumentType) {
		this.searchDocumentType = searchDocumentType;
	}

	public CommonReportUtil getReportUtil() {
		return reportUtil;
	}

	public void setReportUtil(CommonReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}

	public String getSearchReceivedDocumentDateFrom() {
		return searchReceivedDocumentDateFrom;
	}

	public void setSearchReceivedDocumentDateFrom(String searchReceivedDocumentDateFrom) {
		this.searchReceivedDocumentDateFrom = searchReceivedDocumentDateFrom;
	}

	public String getSearchReceivedDocumentDateTo() {
		return searchReceivedDocumentDateTo;
	}

	public void setSearchReceivedDocumentDateTo(String searchReceivedDocumentDateTo) {
		this.searchReceivedDocumentDateTo = searchReceivedDocumentDateTo;
	}

	public ReportComplianceDocumentRekapService getReportComplianceDocumentRekapService() {
		return reportComplianceDocumentRekapService;
	}

	public void setReportComplianceDocumentRekapService(
			ReportComplianceDocumentRekapService reportComplianceDocumentRekapService) {
		this.reportComplianceDocumentRekapService = reportComplianceDocumentRekapService;
	}

	public int getChartCol1() {
		return chartCol1;
	}

	public void setChartCol1(int chartCol1) {
		this.chartCol1 = chartCol1;
	}

	public int getChartCol2() {
		return chartCol2;
	}

	public void setChartCol2(int chartCol2) {
		this.chartCol2 = chartCol2;
	}

}
