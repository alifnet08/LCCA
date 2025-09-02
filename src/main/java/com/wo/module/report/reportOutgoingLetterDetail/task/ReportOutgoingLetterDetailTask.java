package com.wo.module.report.reportOutgoingLetterDetail.task;

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
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.report.reportGen.constant.ReportSheetNameConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.report.reportOutgoingLetterDetail.constant.ReportOutgoingLetterDetailConstants;
import com.wo.module.report.reportOutgoingLetterDetail.model.ReportOutgoingLetterDetail;
import com.wo.module.report.reportOutgoingLetterDetail.service.ReportOutgoingLetterDetailService;
import com.wo.module.report.reportOutgoingLetterRekap.model.ReportOutgoingLetterRekap;
import com.wo.module.report.reportOutgoingLetterRekap.service.ReportOutgoingLetterRekapService;

public class ReportOutgoingLetterDetailTask implements Runnable, ReportSheetNameConstant, ReportOutgoingLetterDetailConstants{

	static Logger logger = Logger.getLogger(ReportOutgoingLetterDetailTask.class);
	
	public final static String COMPLIANCE_DOC_TYPE_REPORT_OUTGOING_LETTER_DETAIL = "Report Outgoing Letter Detail";
	private final static String PIE_CHART_TITLE = "Total Outgoing Letter";
	
	private Long reportGenId;
	private String userNikName;
	
	private ReportGenService reportGenService;
	private ParameterDetailService parameterDetailService;
	private RunnableFacesUtil runnableFacesUtil;
	private ReportOutgoingLetterDetailService reportOutgoingLetterDetailService;
	private ReportOutgoingLetterRekapService reportOutgoingLetterRekapService;
	
	@SuppressWarnings("rawtypes")
	private List<? extends SearchObject> searchCriteria;
	
	private String searchCreationDateFrom;
	private String searchCreationDateTo;
	private String searchLetterDateFrom;
	private String searchLetterDateTo;
	
	private CommonReportUtil reportUtil = new CommonReportUtil();
	
	@SuppressWarnings("rawtypes")
	public ReportOutgoingLetterDetailTask(Long reportGenId,
			ReportGenService reportGenService,
			ParameterDetailService parameterDetailService,
			RunnableFacesUtil runnableFacesUtil,
			ReportOutgoingLetterDetailService reportOutgoingLetterDetailService,
			ReportOutgoingLetterRekapService reportOutgoingLetterRekapService,
			List<? extends SearchObject> searchCriteria,
			String userNikName) {
		super();
		this.reportGenId = reportGenId;
		this.reportGenService = reportGenService;
		this.parameterDetailService = parameterDetailService;
		this.reportOutgoingLetterDetailService = reportOutgoingLetterDetailService;
		this.reportOutgoingLetterRekapService = reportOutgoingLetterRekapService;
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
			if(ufw != null && ufw.getFile() != null) {
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
		SCMApiUpload saf = new SCMApiUploadImpl(absoluteResultFilePath, 
				parameterDetailService, 
				COMPLIANCE_DOC_TYPE_REPORT_OUTGOING_LETTER_DETAIL
			);	
		saf.upload();
		return saf.getAsUploadedFileWO();
	}
	
	private String writeToFile(List<Integer> listColumnView) throws Exception {
		String sheetName1 = SHEET_NAME_REPORT_OUTGOING_LETTER_DETAIL;
		String sheetName2 = SHEET_NAME_REPORT_OUTGOING_LETTER_REKAP_TUJUAN_SURAT;
		String sheetName3 = SHEET_NAME_REPORT_OUTGOING_LETTER_REKAP_TANGGAL_SURAT;
		String sheetName4 = SHEET_NAME_REPORT_OUTGOING_LETTER_DIAGRAM;
		
		String fileNamePrefix = "ReportOutgoingLetterDetail";
		
		String fileName = getReportUtil().generateFileName(fileNamePrefix, userNikName);
		String filePath = getReportUtil().retrieveFilePath(parameterDetailService);
		
		Path dirPath = Paths.get(filePath);
		if(!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}
		
		XSSFWorkbook workbook = null;
		FileOutputStream fileOutputStream = null;
		
		List<String> buildListColumnNameRow1Sheet1 = buildListColumnNameRow1Sheet1();
		
		List<ReportOutgoingLetterDetail> sheet1Results = (List<ReportOutgoingLetterDetail>)
				reportOutgoingLetterDetailService.getReportOutgoingLetterDetailByAllData(searchCriteria);
		
		List<String> buildListColumnNameRow1Sheet2 = buildListColumnNameRow1Sheet2();
		
		List<String> buildListColumnNameRow1Sheet3 = buildListColumnNameRow1Sheet3();
		
		List<ReportOutgoingLetterRekap> sheet2Results = (List<ReportOutgoingLetterRekap>)
				getReportOutgoingLetterRekapService().getReportOutgoingLetterRekapByTujuanSuratData(searchCriteria);
		List<ReportOutgoingLetterRekap> sheet3Results = (List<ReportOutgoingLetterRekap>)
				getReportOutgoingLetterRekapService().getReportOutgoingLetterRekapByTanggalSuratData(searchCriteria);
		
		try {
			
			fileOutputStream = new FileOutputStream(filePath + fileName);
			workbook = new XSSFWorkbook();
			
			writeSheet1(listColumnView, sheetName1, workbook, buildListColumnNameRow1Sheet1, sheet1Results);
			
			XSSFSheet sheet2 = workbook.createSheet(sheetName2);
			
			
			Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
			getReportUtil().createExcelCellFormat(workbook, mapCellFormat);
			
			this.writeExcelHeader(sheet2, mapCellFormat);
			
			
			// write sheet 1 column header [start]
			
			// first row
			CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER);
			int rowIndexSheet1 = 11;
			if (buildListColumnNameRow1Sheet2 != null && !buildListColumnNameRow1Sheet2.isEmpty()) {
				
				int columnIndex = 0;
				for (String columnNameAlias : buildListColumnNameRow1Sheet2) {
					
					getReportUtil().writeCell(sheet2, rowIndexSheet1, columnIndex, columnNameAlias, cfColumnHeader);
					
					columnIndex++;
				}
			}
			// write sheet 1 column header [end]
			
			// sheet 1 data [start]
			if (sheet2Results != null && !sheet2Results.isEmpty()) {
				rowIndexSheet1 += 1;
				int rowNum = 1;
				
				for (ReportOutgoingLetterRekap arrObj : sheet2Results) {
					int columnIndex = 0;
					
					writeObjectToExcelSheet2(sheet2, mapCellFormat, rowIndexSheet1, rowNum, arrObj, columnIndex);
					
					rowIndexSheet1++;
					rowNum++;
				}
			}			
			// sheet 1 data [end]
			XSSFSheet sheet3 = workbook.createSheet(sheetName3);
			this.writeExcelHeader(sheet3, mapCellFormat);
			// sheet 2 header [start]
			
			// first row
			int rowIndexSheet2 = 11;
			if (buildListColumnNameRow1Sheet3 != null && !buildListColumnNameRow1Sheet3.isEmpty()) {
				int columnIndex = 0;
							
				for (String columnNameAlias : buildListColumnNameRow1Sheet3) {
					
					getReportUtil().writeCell(sheet3, rowIndexSheet2, columnIndex, columnNameAlias, cfColumnHeader);
					
					columnIndex++;
				}
			}
			// sheet 2 header [end]
			
			// sheet 2 data [start]
			if (sheet3Results != null && !sheet3Results.isEmpty()) {
				rowIndexSheet2 += 1;
				int rowNum = 1;
				
				for (ReportOutgoingLetterRekap arrObj : sheet3Results) {
					int columnIndex = 0;
					
					writeObjectToExcelSheet3(sheet3, mapCellFormat, rowIndexSheet2, rowNum, arrObj, columnIndex);
					
					rowIndexSheet2++;
					rowNum++;
				}
			}
			// sheet 2 data [end]
			
			// sheet 3 header [start]
			writeSheet4(sheetName4, filePath, workbook, sheet2Results);
			// sheet 3 header [end]
			
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
	
	private void writeSheet4(String sheetName3, String filePath, XSSFWorkbook workbook,
			List<ReportOutgoingLetterRekap> sheet1Results) throws IOException, FileNotFoundException {
		XSSFSheet sheet3 = workbook.createSheet(sheetName3);
		CreationHelper helper = workbook.getCreationHelper();
		
		createPie(filePath, workbook, sheet1Results, sheet3, helper);
		createBar(filePath, workbook, sheet1Results, sheet3, helper);
		
	}
	
	@SuppressWarnings("rawtypes")
	private void createBar(String filePath, XSSFWorkbook workbook, List<ReportOutgoingLetterRekap> sheet1Results, XSSFSheet sheet, CreationHelper helper) throws IOException, FileNotFoundException {
		DefaultCategoryDataset barDataSet = createBarDataset(sheet1Results);
		File barPath = printBar(barDataSet, filePath);
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();
		anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);
		
		int barChartIndex = workbook.addPicture(FileUtil.readBytesFromFile(barPath), Workbook.PICTURE_TYPE_JPEG);
		
		anchor.setCol1(12);
		anchor.setRow1(1);
		anchor.setCol2(13);
		anchor.setRow2(1);
		
		Picture pictBar = drawing.createPicture(anchor, barChartIndex);
		pictBar.resize();
		
		barPath.delete();
	}
	
	private File printBar(DefaultCategoryDataset dataset, String filePath) throws IOException {
		
		JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset);
		
		CategoryPlot plot = (CategoryPlot) chart.getCategoryPlot();
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		
		ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, 
                TextAnchor.TOP_CENTER);
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setItemMargin(0);
		renderer.setPositiveItemLabelPositionFallback(position);
		
		int width = 480;
		int height = 360;
		File barChartImage = new File(
				filePath + "bar-chart"+ CommonConstants.SEPARATOR_DASH + System.currentTimeMillis() + CommonConstants.FILE_TYPE_JPEG);
		
		ChartUtilities.saveChartAsJPEG(barChartImage, chart, width, height);
		
		return barChartImage;
	}
	
	private DefaultCategoryDataset createBarDataset(List<ReportOutgoingLetterRekap> result) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		for (ReportOutgoingLetterRekap reportOutgoingLetterRekap : result) {
			dataset.addValue(reportOutgoingLetterRekap.getTotal(), runnableFacesUtil.retrieveLocaleMessage(reportOutgoingLetterRekap.getTujuanSuratEn(), 
					reportOutgoingLetterRekap.getTujuanSuratIn()), "Total Outgoing Letter");
		}
		
		return dataset;
	}
	
	@SuppressWarnings("rawtypes")
	private void createPie(String filePath, XSSFWorkbook workbook, List<ReportOutgoingLetterRekap> sheet1Results, XSSFSheet sheet, CreationHelper helper) throws IOException, FileNotFoundException {
		PieDataset pieDataset = createDataset(sheet1Results);
		File chartPath = printPie(pieDataset, filePath);
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
	
	private PieDataset createDataset(List<ReportOutgoingLetterRekap> result) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		for (ReportOutgoingLetterRekap reportOutgoingLetterRekap : result) {
			dataset.setValue(
					runnableFacesUtil.retrieveLocaleMessage(reportOutgoingLetterRekap.getTujuanSuratEn(),
							reportOutgoingLetterRekap.getTujuanSuratIn()),
					reportOutgoingLetterRekap.getTotal());
		}
		return dataset;
	}
	
	private File printPie(PieDataset pieDataset, String filePath) throws IOException {
		
		JFreeChart chart = ChartFactory.createPieChart(
				PIE_CHART_TITLE,
				pieDataset,
		        true, 
		        true,
		        false);
		
		PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
				"{1}", NumberFormat.getInstance(), NumberFormat.getPercentInstance()
				);
		
		PiePlot plot = (PiePlot) chart.getPlot();
//		plot.setForegroundAlpha( 0.5f );
//		plot.setStartAngle(360);
//		plot.setInteriorGap( 0.02 );
		plot.setSimpleLabels(true);
		plot.setLabelGenerator(labelGenerator);
		
		int width = 480;
		int height = 360;
		File pieChartImage = new File(
				filePath + "pie-chart"+ CommonConstants.SEPARATOR_DASH + System.currentTimeMillis() + CommonConstants.FILE_TYPE_JPEG);
		
		ChartUtilities.saveChartAsJPEG(pieChartImage, chart, width, height);
		
		return pieChartImage;
	}
	
	private void writeObjectToExcelSheet2(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum,
			ReportOutgoingLetterRekap arrObj, int columnIndex) throws Exception {
		
		sheet1.autoSizeColumn(0);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, rowNum, mapCellFormat);
		
		sheet1.autoSizeColumn(1);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getTujuanSuratEn(), arrObj.getTujuanSuratIn()), mapCellFormat);
		
		sheet1.autoSizeColumn(2);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				arrObj.getTotal(), mapCellFormat);
	}
	
	private void writeObjectToExcelSheet3(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum,
			ReportOutgoingLetterRekap arrObj, int columnIndex) throws Exception {
	
		sheet1.autoSizeColumn(0);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, rowNum, mapCellFormat);
		
		sheet1.autoSizeColumn(1);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				arrObj.getTanggalSurat(), mapCellFormat);
		
		sheet1.autoSizeColumn(2);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				arrObj.getTotal(), mapCellFormat);
	}
	
	private List<String> buildListColumnNameRow1Sheet2() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Tujuan Surat");
		listColumnNameTemp.add("Total");
		
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow1Sheet3() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Tanggal Surat");
		listColumnNameTemp.add("Total");
		
		return listColumnNameTemp;
	}

	private void writeSheet1(List<Integer> listColumnView, String sheetName1, XSSFWorkbook workbook,
			List<String> buildListColumnNameRow1Sheet1, List<ReportOutgoingLetterDetail> sheet1Results)
			throws Exception {
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
		int rowIndex = 12;
					
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
			for (ReportOutgoingLetterDetail arrObj : sheet1Results) {
				int columnIndex = 0;
				
				writeObjectToExcel(sheet1, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);
				
				rowIndex++;
				rowNum++;
			}
		}
		// write sheet 1 data detail [end]
		sheet1.autoSizeColumn(7);
	}
	
	private void writeObjectToExcel(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum,
			ReportOutgoingLetterDetail arrObj, int columnIndex) throws Exception {
	
//		sheet.autoSizeColumn(0);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, rowNum, mapCellFormat);
		
//		sheet.autoSizeColumn(1);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getTujuanSuratEn(), arrObj.getTujuanSuratIn()), mapCellFormat);
		
//		sheet.autoSizeColumn(2);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++,
				arrObj.getNoSurat(), mapCellFormat);
		
//		sheet.autoSizeColumn(3);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++,
				arrObj.getTanggalSurat(), mapCellFormat);
		
//		sheet.autoSizeColumn(4);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getPerihalEn(), arrObj.getPerihalIn()), mapCellFormat);
		
//		sheet.autoSizeColumn(5);
//		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, 
//				arrObj.getDisampaikanKepada(), mapCellFormat);
		
//		sheet.autoSizeColumn(6);
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, 
				arrObj.getTembusanSurat(), mapCellFormat);
		
//		sheet.autoSizeColumn(7);
		getReportUtil().writeWrapCellDetail(sheet, rowIndex, columnIndex++, 
				arrObj.getAttachmentDokumen(), mapCellFormat);
		
		getReportUtil().writeWrapCellDetail(sheet, rowIndex, columnIndex++, 
				arrObj.getNamaPembuatSurat(), mapCellFormat);
	}
	
	private List<String> buildListColumnNameRow1Sheet1() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Tujuan Surat");
		listColumnNameTemp.add("Nomor Surat");
		listColumnNameTemp.add("Tanggal Surat");
		listColumnNameTemp.add("Perihal");
//		listColumnNameTemp.add("Disampaikan Kepada");
		listColumnNameTemp.add("Tembusan Surat");
		listColumnNameTemp.add("Attachment Dokumen");
		listColumnNameTemp.add("Dibuat Oleh");
		
		return listColumnNameTemp;
	}
	
	public void writeExcelHeader(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat) throws Exception {
		
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
		searchLetterDateFrom = getSearchCriteriaValue(WHERE_LETTER_DATE_FROM);
		searchLetterDateTo = getSearchCriteriaValue(WHERE_LETTER_DATE_TO);
		
		String from = "";
		String to = "";
		String letterFrom = "";
		String letterTo = "";
		
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
		
		if (StringUtils.isNotBlank(searchLetterDateFrom)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchLetterDateFrom);
				letterFrom = DateUtil.dateToString(tmpDate);
			} catch (Exception ex) {
				letterFrom = "";
			}
		}
		
		if (StringUtils.isNotBlank(searchLetterDateTo)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchLetterDateTo);
				letterTo = DateUtil.dateToString(tmpDate);
			} catch (Exception ex) {
				letterTo = "";
			}
		}
		
		// Create Header Title [Start]
		if (sheet.getSheetName().equals(SHEET_NAME_REPORT_OUTGOING_LETTER_DETAIL)) {
			reportUtil.writeCellTitle(sheet, 4,
					"Report Surat Keluar Detail", mapCellFormat);
		} else {
			reportUtil.writeCellTitle(sheet, 4,
					"Report Surat Keluar Rekap", mapCellFormat);

		}
		
				
		row++;
		row++;
		
		// Label Creation Date
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportOutgoingLetterDetailCreationDate") + " : ",
				cfHeaderLabel);
								
		// Value Creation Date
		reportUtil.writeCell(sheet, row, columnStart + 1,
				from + " "
				+ ((StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to))
				? runnableFacesUtil.retrieveMessage("textUntil") : "")
				+ " " + to,
				cfHeaderValue);
		row++;
		
		// label tanggal surat
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportOutgoingLetterDetailLetterDate") + " : ",
				cfHeaderLabel);
		
		// value tanggal surat
		reportUtil.writeCell(sheet, row, columnStart + 1,
				letterFrom + " "
				+ ((StringUtils.isNotBlank(letterFrom) && StringUtils.isNotBlank(letterTo))
				? runnableFacesUtil.retrieveMessage("textUntil") : "")
				+ " " + letterTo,
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
		
		// Label Printed On
		reportUtil.writeCell(sheet, row, columnStart,
						"Dibuat Oleh" + " : ", cfHeaderLabel);
										
		// Value Printed On
		reportUtil.writeCell(sheet, row, columnStart + 1, excelPrintDate, cfHeaderValue);
		row++;
							
		// Create Header Title [End]
	}
	
	private void updateReportGenHistoryAsCompleteError(Long newReportGenId, UploadedFileWO ufw, String errorMsg) throws Exception {
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

	public ReportOutgoingLetterDetailService getReportOutgoingLetterDetailService() {
		return reportOutgoingLetterDetailService;
	}

	public void setReportOutgoingLetterDetailService(ReportOutgoingLetterDetailService reportOutgoingLetterDetailService) {
		this.reportOutgoingLetterDetailService = reportOutgoingLetterDetailService;
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

	public String getSearchLetterDateFrom() {
		return searchLetterDateFrom;
	}

	public void setSearchLetterDateFrom(String searchLetterDateFrom) {
		this.searchLetterDateFrom = searchLetterDateFrom;
	}

	public String getSearchLetterDateTo() {
		return searchLetterDateTo;
	}

	public void setSearchLetterDateTo(String searchLetterDateTo) {
		this.searchLetterDateTo = searchLetterDateTo;
	}

	public CommonReportUtil getReportUtil() {
		return reportUtil;
	}

	public void setReportUtil(CommonReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}

	public ReportOutgoingLetterRekapService getReportOutgoingLetterRekapService() {
		return reportOutgoingLetterRekapService;
	}

	public void setReportOutgoingLetterRekapService(ReportOutgoingLetterRekapService reportOutgoingLetterRekapService) {
		this.reportOutgoingLetterRekapService = reportOutgoingLetterRekapService;
	}
}
