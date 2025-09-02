package com.wo.module.report.reportComplianceOnSiteReviewDetail.task;

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
import org.apache.poi.ss.util.CellRangeAddress;
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
import com.wo.module.report.reportComplianceOnSiteReviewDetail.constant.ReportComplianceOnSiteReviewDetailConstants;
import com.wo.module.report.reportComplianceOnSiteReviewDetail.model.ReportComplianceOnSiteReviewDetail;
import com.wo.module.report.reportComplianceOnSiteReviewDetail.service.ReportComplianceOnSiteReviewDetailService;
import com.wo.module.report.reportComplianceOnSiteReviewRekap.model.ReportComplianceOnSiteReviewRekap;
import com.wo.module.report.reportComplianceOnSiteReviewRekap.service.ReportComplianceOnSiteReviewRekapService;
import com.wo.module.report.reportGen.constant.ReportSheetNameConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.service.ReportGenService;

public class ReportComplianceOnSiteReviewDetailTask
		implements Runnable, ReportSheetNameConstant, ReportComplianceOnSiteReviewDetailConstants {

	static Logger logger = Logger.getLogger(ReportComplianceOnSiteReviewDetailTask.class);

	public final static String COMPLIANCE_DOC_TYPE_REPORT_COMPLIANCE_ON_SITE_REVIEW_DETAIL = "Compliance Testing Detail";
	private static final String PIE_CHART_TITLE = "Total Compliance Testing";

	private Long reportGenId;
	private String userNikName;

	private ReportGenService reportGenService;
	private ParameterDetailService parameterDetailService;
	private RunnableFacesUtil runnableFacesUtil;
	private ReportComplianceOnSiteReviewDetailService reportComplianceOnSiteReviewDetailService;
	private ReportComplianceOnSiteReviewRekapService reportComplianceOnSiteReviewRekapService;

	@SuppressWarnings("rawtypes")
	private List<? extends SearchObject> searchCriteria;

	private String searchCreationDateFrom;
	private String searchCreationDateTo;
	private String searchReviewCategory;
	private String searchTargetDateFrom;
	private String searchTargetDateTo;

	private CommonReportUtil reportUtil = new CommonReportUtil();

	@SuppressWarnings("rawtypes")
	public ReportComplianceOnSiteReviewDetailTask(Long reportGenId, ReportGenService reportGenService,
			ParameterDetailService parameterDetailService, RunnableFacesUtil runnableFacesUtil,
			ReportComplianceOnSiteReviewDetailService reportComplianceOnSiteReviewDetailService,
			ReportComplianceOnSiteReviewRekapService reportComplianceOnSiteReviewRekapService,
			List<? extends SearchObject> searchCriteria, String userNikName) {
		super();
		this.reportGenId = reportGenId;
		this.reportGenService = reportGenService;
		this.parameterDetailService = parameterDetailService;
		this.reportComplianceOnSiteReviewDetailService = reportComplianceOnSiteReviewDetailService;
		this.reportComplianceOnSiteReviewRekapService = reportComplianceOnSiteReviewRekapService;
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
				COMPLIANCE_DOC_TYPE_REPORT_COMPLIANCE_ON_SITE_REVIEW_DETAIL);
		saf.upload();
		return saf.getAsUploadedFileWO();
	}

	private String writeToFile(List<Integer> listColumnView) throws Exception {
//		String sheetName1 = SHEET_NAME_REPORT_COMPLIANCE_ON_SITE_REVIEW_DETAIL;
//		String sheetName2 = SHEET_NAME_REPORT_COMPLIANCE_ON_SITE_REVIEW_REKAP;
//		String sheetName3 = SHEET_NAME_REPORT_COMPLIANCE_ON_SITE_REVIEW_DIAGRAM;
		String sheetName1 = SHEET_NAME_REPORT_COMPLIANCE_TESTING_DETAIL;
		String sheetName2 = SHEET_NAME_REPORT_COMPLIANCE_TESTING_REKAP;
		String sheetName3 = SHEET_NAME_REPORT_COMPLIANCE_TESTING_DIAGRAM;

//		String fileNamePrefix = "ReportComplianceOnSiteReviewDetail";
		String fileNamePrefix = "ReportComplianceTestingDetail";

		String fileName = getReportUtil().generateFileName(fileNamePrefix, userNikName);
		String filePath = getReportUtil().retrieveFilePath(parameterDetailService);

		Path dirPath = Paths.get(filePath);
		if (!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}

		XSSFWorkbook workbook = null;
		FileOutputStream fileOutputStream = null;

		List<String> buildListColumnNameRow1Sheet1 = buildListColumnNameRow1Sheet1();

		List<ReportComplianceOnSiteReviewDetail> sheet1Results = (List<ReportComplianceOnSiteReviewDetail>) reportComplianceOnSiteReviewDetailService
				.getReportComplianceOnSiteReviewDetailByData(searchCriteria);

		List<String> buildListColumnNameRow1Sheet2 = buildListColumnNameRow1Sheet2();
		List<String> buildListColumnNameRow2Sheet2 = buildListColumnNameRow2Sheet2();

		List<ReportComplianceOnSiteReviewRekap> sheet2Results = (List<ReportComplianceOnSiteReviewRekap>) reportComplianceOnSiteReviewRekapService
				.getReportComplianceOnSiteReviewRekapByData(searchCriteria);
		try {

			fileOutputStream = new FileOutputStream(filePath + fileName);
			workbook = new XSSFWorkbook();

			writeSheet1(listColumnView, sheetName1, workbook, buildListColumnNameRow1Sheet1, sheet1Results);

			writeSheet2(listColumnView, sheetName2, workbook, buildListColumnNameRow1Sheet2,
					buildListColumnNameRow2Sheet2, sheet2Results);
			// sheet 1 data [end]

			// sheet 2 header [start]
			writeSheet3(sheetName3, filePath, workbook, sheet2Results);
			// sheet 2 header [end]

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

	private void writeSheet3(String sheetName3, String filePath, XSSFWorkbook workbook,
			List<ReportComplianceOnSiteReviewRekap> sheet2Results) throws IOException, FileNotFoundException {
		XSSFSheet sheet3 = workbook.createSheet(sheetName3);
		CreationHelper helper = workbook.getCreationHelper();

		createPie(filePath, workbook, sheet2Results, sheet3, helper);
		createBar(filePath, workbook, sheet2Results, sheet3, helper);

	}

	@SuppressWarnings("rawtypes")
	private void createBar(String filePath, XSSFWorkbook workbook,
			List<ReportComplianceOnSiteReviewRekap> sheet2Results, XSSFSheet sheet, CreationHelper helper)
			throws IOException, FileNotFoundException {
		DefaultCategoryDataset barDataSet = createBarDataset(sheet2Results);
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
		File barChartImage = new File(filePath + "bar-chart" + CommonConstants.SEPARATOR_DASH
				+ System.currentTimeMillis() + CommonConstants.FILE_TYPE_JPEG);

		ChartUtilities.saveChartAsJPEG(barChartImage, chart, width, height);

		return barChartImage;
	}

	private DefaultCategoryDataset createBarDataset(List<ReportComplianceOnSiteReviewRekap> result) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (ReportComplianceOnSiteReviewRekap reportComplianceOnSiteReviewRekap : result) {
			dataset.addValue(reportComplianceOnSiteReviewRekap.getTotalReview(),
					runnableFacesUtil.retrieveLocaleMessage(reportComplianceOnSiteReviewRekap.getKategoriNameEn(),
							reportComplianceOnSiteReviewRekap.getKategoriNameIn()),
					"Total Surat Masuk");
			dataset.addValue(reportComplianceOnSiteReviewRekap.getTindakLanjutYes(),
					runnableFacesUtil.retrieveLocaleMessage(reportComplianceOnSiteReviewRekap.getKategoriNameEn(),
							reportComplianceOnSiteReviewRekap.getKategoriNameIn()),
					"Perlu Tindak Lanjut");
			dataset.addValue(reportComplianceOnSiteReviewRekap.getTindakLanjutNo(),
					runnableFacesUtil.retrieveLocaleMessage(reportComplianceOnSiteReviewRekap.getKategoriNameEn(),
							reportComplianceOnSiteReviewRekap.getKategoriNameIn()),
					"Tidak Ada Tindak Lanjut");
			dataset.addValue(reportComplianceOnSiteReviewRekap.getInProgress(),
					runnableFacesUtil.retrieveLocaleMessage(reportComplianceOnSiteReviewRekap.getKategoriNameEn(),
							reportComplianceOnSiteReviewRekap.getKategoriNameIn()),
					"In Progress");
			dataset.addValue(reportComplianceOnSiteReviewRekap.getClosed(),
					runnableFacesUtil.retrieveLocaleMessage(reportComplianceOnSiteReviewRekap.getKategoriNameEn(),
							reportComplianceOnSiteReviewRekap.getKategoriNameIn()),
					"Closed");
			dataset.addValue(reportComplianceOnSiteReviewRekap.getMeetSla(),
					runnableFacesUtil.retrieveLocaleMessage(reportComplianceOnSiteReviewRekap.getKategoriNameEn(),
							reportComplianceOnSiteReviewRekap.getKategoriNameIn()),
					"Meet SLA");
			dataset.addValue(reportComplianceOnSiteReviewRekap.getBeforeSla(),
					runnableFacesUtil.retrieveLocaleMessage(reportComplianceOnSiteReviewRekap.getKategoriNameEn(),
							reportComplianceOnSiteReviewRekap.getKategoriNameIn()),
					"Before SLA");
			dataset.addValue(reportComplianceOnSiteReviewRekap.getOverSla(),
					runnableFacesUtil.retrieveLocaleMessage(reportComplianceOnSiteReviewRekap.getKategoriNameEn(),
							reportComplianceOnSiteReviewRekap.getKategoriNameIn()),
					"Over SLA");
		}
		return dataset;
	}

	@SuppressWarnings("rawtypes")
	private void createPie(String filePath, XSSFWorkbook workbook,
			List<ReportComplianceOnSiteReviewRekap> sheet2Results, XSSFSheet sheet, CreationHelper helper)
			throws IOException, FileNotFoundException {
		PieDataset pieDataset = createDataset(sheet2Results);
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

	private PieDataset createDataset(List<ReportComplianceOnSiteReviewRekap> result) {
		DefaultPieDataset dataset = new DefaultPieDataset();

		for (ReportComplianceOnSiteReviewRekap reportComplianceOnSiteReviewRekap : result) {
			dataset.setValue(
					runnableFacesUtil.retrieveLocaleMessage(reportComplianceOnSiteReviewRekap.getKategoriNameEn(),
							reportComplianceOnSiteReviewRekap.getKategoriNameIn()),
					reportComplianceOnSiteReviewRekap.getTotalReview());
		}
		return dataset;
	}

	private File printPie(PieDataset pieDataset, String filePath) throws IOException {

		JFreeChart chart = ChartFactory.createPieChart(PIE_CHART_TITLE, pieDataset, true, true, false);

		PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{1}",
				NumberFormat.getInstance(), NumberFormat.getPercentInstance());

		PiePlot plot = (PiePlot) chart.getPlot();
//		plot.setForegroundAlpha( 0.5f );
//		plot.setStartAngle(360);
//		plot.setInteriorGap( 0.02 );
		plot.setSimpleLabels(true);
		plot.setLabelGenerator(labelGenerator);

		int width = 480;
		int height = 360;
		File pieChartImage = new File(filePath + "pie-chart" + CommonConstants.SEPARATOR_DASH
				+ System.currentTimeMillis() + CommonConstants.FILE_TYPE_JPEG);

		ChartUtilities.saveChartAsJPEG(pieChartImage, chart, width, height);

		return pieChartImage;
	}

	private void writeSheet2(List<Integer> listColumnView, String sheetName2, XSSFWorkbook workbook,
			List<String> buildListColumnNameRow1Sheet2, List<String> buildListColumnNameRow2Sheet2,
			List<ReportComplianceOnSiteReviewRekap> sheet2Results) throws Exception {
		XSSFSheet sheet2 = workbook.createSheet(sheetName2);

		// setting column size sheet 1
		if (listColumnView != null) {
			for (int i = 0; i < listColumnView.size(); i++) {
				sheet2.setColumnWidth(i, listColumnView.get(i));
				sheet2.autoSizeColumn(i);
			}
		}

		Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
		getReportUtil().createExcelCellFormat(workbook, mapCellFormat);

		this.writeExcelHeader(sheet2, mapCellFormat);

		CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER);

		// sheet 1 header [start]
		int rowIndex = 11;
		if (buildListColumnNameRow1Sheet2 != null && !buildListColumnNameRow1Sheet2.isEmpty()) {
			int columnIndex = 0;

			for (String columnNameAlias : buildListColumnNameRow1Sheet2) {

				getReportUtil().writeCell(sheet2, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);

				// merged Area
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
		// sheet 1 header [end]

		// sheet 1 data [start]
		if (sheet2Results != null && !sheet2Results.isEmpty()) {
			rowIndex += 1;
			int rowNum = 1;

			for (ReportComplianceOnSiteReviewRekap arrObj : sheet2Results) {
				int columnIndex = 0;

				writeObjectToExcel(sheet2, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);

				rowIndex++;
				rowNum++;
			}
		}
	}

	private void writeObjectToExcel(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum,
			ReportComplianceOnSiteReviewRekap arrObj, int columnIndex) throws Exception {

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, rowNum, mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getKategoriNameEn(), arrObj.getKategoriNameIn()),
				mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTotalReview(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTindakLanjutYes(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTindakLanjutNo(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getInProgress(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getClosed(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getMeetSla(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getBeforeSla(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getOverSla(), mapCellFormat);
	}

	private List<String> buildListColumnNameRow1Sheet2() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Kategori Review");
		listColumnNameTemp.add("Total Compliance Testing");
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

	private void writeSheet1(List<Integer> listColumnView, String sheetName1, XSSFWorkbook workbook,
			List<String> buildListColumnNameRow1Sheet1, List<ReportComplianceOnSiteReviewDetail> sheet1Results)
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
			for (ReportComplianceOnSiteReviewDetail arrObj : sheet1Results) {
				int columnIndex = 0;

				writeObjectToExcelSheet2(sheet1, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);

				rowIndex++;
				rowNum++;
			}
		}
		// write sheet 1 data detail [end]
		sheet1.autoSizeColumn(4);
		sheet1.autoSizeColumn(5);
		sheet1.autoSizeColumn(6);
		sheet1.autoSizeColumn(13);
	}

	private void writeObjectToExcelSheet2(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex,
			int rowNum, ReportComplianceOnSiteReviewDetail arrObj, int columnIndex) throws Exception {

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, rowNum, mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getKategoriReviewEn(), arrObj.getKategoriReviewIn()),
				mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getDocumentNo(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getPerihalEn(), arrObj.getPerihalIn()), mapCellFormat);

		getReportUtil().writeWrapCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getAreaReview(), mapCellFormat);

		getReportUtil().writeWrapCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTemuan(), mapCellFormat);

		getReportUtil().writeWrapCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getPoinTindakLanjut(),
				mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTargetDateStr(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getPicCompliance(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getPic1(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getPic2(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getPic3(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getStatusEn(), arrObj.getStatusIn()), mapCellFormat);

		getReportUtil().writeWrapCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getBuktiKonfirmasi(),
				mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTanggalPemenuhanStr(),
				mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTanggalKonfirmasiStr(),
				mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getSla(),
				mapCellFormat);
	}

	private List<String> buildListColumnNameRow1Sheet1() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Kategori Review");
		listColumnNameTemp.add("Nomor Memorandum");
		listColumnNameTemp.add("Perihal Memo");
		listColumnNameTemp.add("Area Review");
		listColumnNameTemp.add("Temuan");
		listColumnNameTemp.add("Poin Tindak Lanjut");
		listColumnNameTemp.add("Target Date");
		listColumnNameTemp.add("PIC Compliance");
		listColumnNameTemp.add("PIC 1");
		listColumnNameTemp.add("PIC 2");
		listColumnNameTemp.add("PIC 3");
		listColumnNameTemp.add("Status");
		listColumnNameTemp.add("Bukti Konfirmasi");
		listColumnNameTemp.add("Tanggal Pemenuhan Tindak Lanjut");
		listColumnNameTemp.add("Tanggal Konfirmasi");
		listColumnNameTemp.add("SLA");
		
		return listColumnNameTemp;
	}

	public void writeExcelHeader(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat) throws Exception {

		String reviewCategoryName = "";

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
		searchReviewCategory = getSearchCriteriaValue(WHERE_CATEGORY_REVIEW);
		searchTargetDateFrom = getSearchCriteriaValue(WHERE_TARGET_DATE_FROM);
		searchTargetDateTo = getSearchCriteriaValue(WHERE_TARGET_DATE_TO);

		String from = "";
		String to = "";
		String targetFrom = "";
		String targetTo = "";
		
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
		
		ParameterDetail getReviewCategoryName = parameterDetailService
				.getParameterDetailByParamDtlCode(searchReviewCategory);

		if (getReviewCategoryName != null) {
			reviewCategoryName = runnableFacesUtil.retrieveLocaleMessage(getReviewCategoryName.getNameEn(),
					getReviewCategoryName.getNameIn());
		}

		// Create Header Title [Start]
		if (sheet.getSheetName().equals(SHEET_NAME_REPORT_COMPLIANCE_TESTING_DETAIL)) {
			reportUtil.writeCellTitle(sheet, 4, "Report Compliance Testing Detail", mapCellFormat);
		} else {
			reportUtil.writeCellTitle(sheet, 4, "Report Compliance Testing Rekap", mapCellFormat);
		}
		

		row++;
		row++;

		// Label Creation Date
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportComplianceOnSiteReviewDetailCreationDate") + " : ",
				cfHeaderLabel);

		// Value Creation Date
		reportUtil.writeCell(sheet, row, columnStart + 1, from + " "
				+ ((StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to))
						? runnableFacesUtil.retrieveMessage("textUntil")
						: "")
				+ " " + to, cfHeaderValue);
		row++;

		// Label Target Date
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportComplianceOnSiteReviewDetailTargetDate") + " : ",
				cfHeaderLabel);

		// Value Target Date
		reportUtil.writeCell(sheet, row, columnStart + 1,
				targetFrom + " "
						+ ((StringUtils.isNotBlank(targetFrom) && StringUtils.isNotBlank(targetTo))
								? runnableFacesUtil.retrieveMessage("textUntil")
								: "")
						+ " " + targetTo,
				cfHeaderValue);
		row++;

		// Label Review Category
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportComplianceOnSiteReviewDetailReviewCategory") + " : ",
				cfHeaderLabel);

		// Value Review Category
		reportUtil.writeCell(sheet, row, columnStart + 1, reviewCategoryName, cfHeaderValue);
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

	public ReportComplianceOnSiteReviewDetailService getReportComplianceOnSiteReviewDetailService() {
		return reportComplianceOnSiteReviewDetailService;
	}

	public void setReportComplianceOnSiteReviewDetailService(
			ReportComplianceOnSiteReviewDetailService reportComplianceOnSiteReviewDetailService) {
		this.reportComplianceOnSiteReviewDetailService = reportComplianceOnSiteReviewDetailService;
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

	public String getSearchReviewCategory() {
		return searchReviewCategory;
	}

	public void setSearchReviewCategory(String searchReviewCategory) {
		this.searchReviewCategory = searchReviewCategory;
	}

	public CommonReportUtil getReportUtil() {
		return reportUtil;
	}

	public void setReportUtil(CommonReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}

	public ReportComplianceOnSiteReviewRekapService getReportComplianceOnSiteReviewRekapService() {
		return reportComplianceOnSiteReviewRekapService;
	}

	public void setReportComplianceOnSiteReviewRekapService(
			ReportComplianceOnSiteReviewRekapService reportComplianceOnSiteReviewRekapService) {
		this.reportComplianceOnSiteReviewRekapService = reportComplianceOnSiteReviewRekapService;
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

}
