package com.wo.module.report.reportSuratMasukAmlDetail.task;

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
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Workbook;
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
import com.wo.module.report.reportGen.constant.ReportSheetNameConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.report.reportSuratMasukAmlDetail.constant.ReportSuratMasukAmlDetailConstants;
import com.wo.module.report.reportSuratMasukAmlDetail.model.ReportSuratMasukAmlDetail;
import com.wo.module.report.reportSuratMasukAmlDetail.service.ReportSuratMasukAmlDetailService;
import com.wo.module.report.reportSuratMasukAmlRekap.model.ReportSuratMasukAmlRekap;
import com.wo.module.report.reportSuratMasukAmlRekap.service.ReportSuratMasukAmlRekapService;

public class ReportSuratMasukAmlDetailTask
		implements Runnable, ReportSheetNameConstant, ReportSuratMasukAmlDetailConstants {

	static Logger logger = Logger.getLogger(ReportSuratMasukAmlDetailTask.class);
	public final static String COMPLIANCE_DOC_TYPE_REPORT_SURAT_MASUK__AML_DETAIL = "Report Surat Masuk AML Detail";
	private static final String PIE_CHART_TITLE = "Total Surat Masuk AML";

	private Long reportGenId;
	private String userNikName;

	private ReportGenService reportGenService;
	private ParameterDetailService parameterDetailService;
	private RunnableFacesUtil runnableFacesUtil;
	private ReportSuratMasukAmlDetailService reportSuratMasukAmlDetailService;
	private ReportSuratMasukAmlRekapService reportSuratMasukAmlRekapService;

	@SuppressWarnings("rawtypes")
	private List<? extends SearchObject> searchCriteria;

	private String searchCreationDateFrom;
	private String searchCreationDateTo;
	private String searchSenderCode;
	private String searchTargetDateFrom;
	private String searchTargetDateTo;

	private CommonReportUtil reportUtil = new CommonReportUtil();

	@SuppressWarnings("rawtypes")
	public ReportSuratMasukAmlDetailTask(Long reportGenId, ReportGenService reportGenService,
			ParameterDetailService parameterDetailService, RunnableFacesUtil runnableFacesUtil,
			ReportSuratMasukAmlDetailService reportSuratMasukAmlDetailService,
			ReportSuratMasukAmlRekapService reportSuratMasukAmlRekapService,
			List<? extends SearchObject> searchCriteria, String userNikName) {
		super();
		this.reportGenId = reportGenId;
		this.reportGenService = reportGenService;
		this.parameterDetailService = parameterDetailService;
		this.setRunnableFacesUtil(runnableFacesUtil);
		this.reportSuratMasukAmlDetailService = reportSuratMasukAmlDetailService;
		this.reportSuratMasukAmlRekapService = reportSuratMasukAmlRekapService;
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
				COMPLIANCE_DOC_TYPE_REPORT_SURAT_MASUK__AML_DETAIL);
		saf.upload();
		return saf.getAsUploadedFileWO();
	}

	private String writeToFile(List<Integer> listColumnView) throws Exception {
		String sheetName1 = SHEET_NAME_REPORT_SURAT_MASUK_AML_DETAIL;

		String fileNamePrefix = "ReportSuratMasukAmlDetail";

		String fileName = getReportUtil().generateFileName(fileNamePrefix, userNikName);
		String filePath = getReportUtil().retrieveFilePath(parameterDetailService);

		Path dirPath = Paths.get(filePath);
		if (!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}

		XSSFWorkbook workbook = null;
		FileOutputStream fileOutputStream = null;

		List<String> buildListColumnNameRow1Sheet1 = buildListColumnNameRow1Sheet1();

		List<ReportSuratMasukAmlDetail> sheet1Results = (List<ReportSuratMasukAmlDetail>) reportSuratMasukAmlDetailService
				.getReportSuratMasukAmlDetailByData(searchCriteria);

		List<Integer> listColumnViewBySheet2 = new ArrayList<Integer>();
		List<Integer> listColumnViewBySheet3 = new ArrayList<Integer>();

		try {

			fileOutputStream = new FileOutputStream(filePath + fileName);
			workbook = new XSSFWorkbook();

			writeSheetSuratMasukAmlDetail(workbook, sheetName1, listColumnView, buildListColumnNameRow1Sheet1,
					sheet1Results);
			writeSheetSuratMasukAmlRekap(workbook, listColumnViewBySheet2, listColumnViewBySheet3);

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

	private void writeSheetSuratMasukAmlDetail(XSSFWorkbook workbook, String sheetName1, List<Integer> listColumnView,
			List<String> buildListColumnNameRow1Sheet1, List<ReportSuratMasukAmlDetail> sheet1Results)
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
			for (ReportSuratMasukAmlDetail arrObj : sheet1Results) {
				int columnIndex = 0;

				writeObjectToExcel(sheet1, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);

				rowIndex++;
				rowNum++;
			}
		}
		// write sheet 1 data detail [end]
		sheet1.autoSizeColumn(14);
	}

	private void writeSheetSuratMasukAmlRekap(XSSFWorkbook workbook, List<Integer> listColumnViewBySheet2,
			List<Integer> listColumnViewBySheet3) throws Exception {
		String sheetName2 = SHEET_NAME_REPORT_SURAT_MASUK_AML_REKAP_TIPE_SURAT;
		String sheetName3 = SHEET_NAME_REPORT_SURAT_MASUK_AML_REKAP_NON_TIPE_SURAT;
		String sheetName4 = SHEET_NAME_REPORT_SURAT_MASUK_AML_DIAGRAM;

		List<String> buildListColumnNameRow1Sheet2 = buildListColumnNameRow1Sheet2();
		List<String> buildListColumnNameRow2Sheet2 = buildListColumnNameRow2Sheet2();

		List<String> buildListColumnNameRow1Sheet3 = buildListColumnNameRow1Sheet3();
		List<String> buildListColumnNameRow2Sheet3 = buildListColumnNameRow2Sheet3();

		List<ReportSuratMasukAmlRekap> sheet2Results = (List<ReportSuratMasukAmlRekap>) reportSuratMasukAmlRekapService
				.getReportSuratMasukAmlRekapByTipeSurat(searchCriteria);
		List<ReportSuratMasukAmlRekap> sheet3Results = (List<ReportSuratMasukAmlRekap>) reportSuratMasukAmlRekapService
				.getReportSuratMasukAmlRekapByData(searchCriteria);

		String filePath = getReportUtil().retrieveFilePath(parameterDetailService);

		XSSFSheet sheet2 = workbook.createSheet(sheetName2);
		XSSFSheet sheet3 = workbook.createSheet(sheetName3);

		// Setting column size sheet 1
		if (listColumnViewBySheet2 != null) {
			for (int i = 0; i < listColumnViewBySheet2.size(); i++) {
				sheet2.setColumnWidth(i, listColumnViewBySheet2.get(i));
				sheet2.autoSizeColumn(i);
			}
		}

		// Setting column size sheet 2
		if (listColumnViewBySheet3 != null) {
			for (int i = 0; i < listColumnViewBySheet3.size(); i++) {
				sheet3.setColumnWidth(i, listColumnViewBySheet3.get(i));
				sheet3.autoSizeColumn(i);
			}
		}

		Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
		getReportUtil().createExcelCellFormat(workbook, mapCellFormat);

		this.writeExcelHeader(sheet2, mapCellFormat);
		this.writeExcelHeader(sheet3, mapCellFormat);

		CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER);

		// write sheet 2 column header [start]

		// first row
		int rowIndexSheet1 = 11;
		if (buildListColumnNameRow1Sheet2 != null && !buildListColumnNameRow1Sheet2.isEmpty()) {

			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow1Sheet2) {

				getReportUtil().writeCell(sheet2, rowIndexSheet1, columnIndex, columnNameAlias, cfColumnHeader);

				// merged Area
				if (columnIndex == 3) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet1, rowIndexSheet1, columnIndex,
							columnIndex += 1);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else if (columnIndex == 5) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet1, rowIndexSheet1, columnIndex,
							columnIndex += 1);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else if (columnIndex == 7) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet1, rowIndexSheet1, columnIndex,
							columnIndex += 1);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else if (columnIndex == 9) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet1, rowIndexSheet1, columnIndex,
							columnIndex += 2);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else {
					sheet2.addMergedRegion(
							new CellRangeAddress(rowIndexSheet1, rowIndexSheet1 + 1, columnIndex, columnIndex));
				}

				columnIndex++;
			}
		}

		// second row
		rowIndexSheet1++;
		if (buildListColumnNameRow2Sheet2 != null && !buildListColumnNameRow2Sheet2.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow2Sheet2) {
				getReportUtil().writeCell(sheet2, rowIndexSheet1, columnIndex, columnNameAlias, cfColumnHeader);

				columnIndex++;
			}
		}

		// write sheet 2 column header [end]

		// sheet 2 data [start]
		if (sheet2Results != null && !sheet2Results.isEmpty()) {
			rowIndexSheet1 += 1;
			int rowNum = 1;

			for (ReportSuratMasukAmlRekap arrObj : sheet2Results) {
				int columnIndex = 0;

				writeObjectToExcelSheet2(sheet2, mapCellFormat, rowIndexSheet1, rowNum, arrObj, columnIndex);

				rowIndexSheet1++;
				rowNum++;
			}

		}

		// sheet 2 data [end]

		// sheet 3 header [start]

		// first row
		int rowIndexSheet2 = 11;
		if (buildListColumnNameRow1Sheet3 != null && !buildListColumnNameRow1Sheet3.isEmpty()) {
			int columnIndex = 0;

			for (String columnNameAlias : buildListColumnNameRow1Sheet3) {

				getReportUtil().writeCell(sheet3, rowIndexSheet2, columnIndex, columnNameAlias, cfColumnHeader);

				// merged Area
				if (columnIndex == 3) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet2, rowIndexSheet2, columnIndex,
							columnIndex += 1);
					sheet3.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet3);
				} else if (columnIndex == 5) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet2, rowIndexSheet2, columnIndex,
							columnIndex += 1);
					sheet3.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet3);
				} else if (columnIndex == 7) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet2, rowIndexSheet2, columnIndex,
							columnIndex += 2);
					sheet3.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet3);
				} else {
					sheet3.addMergedRegion(
							new CellRangeAddress(rowIndexSheet2, rowIndexSheet2 + 1, columnIndex, columnIndex));
				}

				columnIndex++;
			}
		}

		// second row
		rowIndexSheet2++;
		if (buildListColumnNameRow2Sheet3 != null && !buildListColumnNameRow2Sheet3.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow2Sheet3) {
				getReportUtil().writeCell(sheet3, rowIndexSheet2, columnIndex, columnNameAlias, cfColumnHeader);

				columnIndex++;
			}
		}
		// sheet 3 header [end]

		// sheet 3 data [start]
		if (sheet3Results != null && !sheet3Results.isEmpty()) {
			rowIndexSheet2 += 1;
			int rowNum = 1;

			for (ReportSuratMasukAmlRekap arrObj : sheet3Results) {

				int columnIndex = 0;

				writeObjectToExcelSheet3(sheet3, mapCellFormat, rowIndexSheet2, rowNum, arrObj, columnIndex);

				rowIndexSheet2++;
				rowNum++;
			}
		}
		// sheet 3 data [end]

		// sheet 4 header [start]
		writeSheet4(sheetName4, filePath, workbook, sheet3Results);
		// sheet 4 header [end]
	}

	private void writeSheet4(String sheetName4, String filePath, XSSFWorkbook workbook,
			List<ReportSuratMasukAmlRekap> sheet2Results) throws IOException, FileNotFoundException {
		XSSFSheet sheet4 = workbook.createSheet(sheetName4);
		CreationHelper helper = workbook.getCreationHelper();

		createPie(filePath, workbook, sheet2Results, sheet4, helper);
		createBar(filePath, workbook, sheet2Results, sheet4, helper);

	}

	@SuppressWarnings("rawtypes")
	private void createBar(String filePath, XSSFWorkbook workbook, List<ReportSuratMasukAmlRekap> sheet2Results,
			XSSFSheet sheet, CreationHelper helper) throws IOException, FileNotFoundException {
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

//		JFreeChart chart = ChartFactory.createBarChart3D("", "", "", dataset);
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

	private DefaultCategoryDataset createBarDataset(List<ReportSuratMasukAmlRekap> result) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (ReportSuratMasukAmlRekap reportSuratMasukAmlRekap : result) {
			dataset.addValue(reportSuratMasukAmlRekap.getTotalSuratMasuk(),
					runnableFacesUtil.retrieveLocaleMessage(reportSuratMasukAmlRekap.getPengirimSuratEn(),
							reportSuratMasukAmlRekap.getPengirimSuratIn()),
					"Total Surat Masuk");
			dataset.addValue(reportSuratMasukAmlRekap.getTindakLanjutYes(),
					runnableFacesUtil.retrieveLocaleMessage(reportSuratMasukAmlRekap.getPengirimSuratEn(),
							reportSuratMasukAmlRekap.getPengirimSuratIn()),
					"Perlu Tindak Lanjut");
			dataset.addValue(reportSuratMasukAmlRekap.getTindakLanjutNo(),
					runnableFacesUtil.retrieveLocaleMessage(reportSuratMasukAmlRekap.getPengirimSuratEn(),
							reportSuratMasukAmlRekap.getPengirimSuratIn()),
					"Tidak Ada Tindak Lanjut");
			dataset.addValue(reportSuratMasukAmlRekap.getInProgress(),
					runnableFacesUtil.retrieveLocaleMessage(reportSuratMasukAmlRekap.getPengirimSuratEn(),
							reportSuratMasukAmlRekap.getPengirimSuratIn()),
					"In Progress");
			dataset.addValue(reportSuratMasukAmlRekap.getClosed(),
					runnableFacesUtil.retrieveLocaleMessage(reportSuratMasukAmlRekap.getPengirimSuratEn(),
							reportSuratMasukAmlRekap.getPengirimSuratIn()),
					"Closed");
			dataset.addValue(reportSuratMasukAmlRekap.getMeetSla(),
					runnableFacesUtil.retrieveLocaleMessage(reportSuratMasukAmlRekap.getPengirimSuratEn(),
							reportSuratMasukAmlRekap.getPengirimSuratIn()),
					"Meet SLA");
			dataset.addValue(reportSuratMasukAmlRekap.getBeforeSla(),
					runnableFacesUtil.retrieveLocaleMessage(reportSuratMasukAmlRekap.getPengirimSuratEn(),
							reportSuratMasukAmlRekap.getPengirimSuratIn()),
					"Before SLA");
			dataset.addValue(reportSuratMasukAmlRekap.getOverSla(),
					runnableFacesUtil.retrieveLocaleMessage(reportSuratMasukAmlRekap.getPengirimSuratEn(),
							reportSuratMasukAmlRekap.getPengirimSuratIn()),
					"Over SLA");
		}
		return dataset;
	}

	@SuppressWarnings("rawtypes")
	private void createPie(String filePath, XSSFWorkbook workbook, List<ReportSuratMasukAmlRekap> sheet2Results,
			XSSFSheet sheet, CreationHelper helper) throws IOException, FileNotFoundException {
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

	private PieDataset createDataset(List<ReportSuratMasukAmlRekap> result) {
		DefaultPieDataset dataset = new DefaultPieDataset();

		for (ReportSuratMasukAmlRekap reportSuratMasukAmlRekap : result) {
			dataset.setValue(
					runnableFacesUtil.retrieveLocaleMessage(reportSuratMasukAmlRekap.getPengirimSuratEn(),
							reportSuratMasukAmlRekap.getPengirimSuratIn()),
					reportSuratMasukAmlRekap.getTotalSuratMasuk());
		}
		return dataset;
	}

	private File printPie(PieDataset pieDataset, String filePath) throws IOException {

//		JFreeChart chart = ChartFactory.createPieChart3D(
//				PIE_CHART_TITLE,
//				pieDataset,
//		        true, 
//		        true,
//		        false);

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

	private void writeObjectToExcel(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum,
			ReportSuratMasukAmlDetail arrObj, int columnIndex) throws Exception {
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, rowNum, mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getJenisPeraturanEn(), arrObj.getJenisPeraturanIn()),
				mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getTanggalTerimaSurat(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getNoSurat(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getTanggalSurat(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getPerihalEn(), arrObj.getPerihalIn()), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getRingkasanSurat(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getTindakLanjut(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getTargetDate(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getStatusEn(), arrObj.getStatusIn()), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getPic1(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getPic2(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getPic3(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getDivisi(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getPicCompliance(), mapCellFormat);

		getReportUtil().writeWrapCellDetail(sheet, rowIndex, columnIndex++, arrObj.getBuktiKonfirmasi(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getTanggalKonfirmasi(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getTanggalTindakLanjut(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getKeterangan(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getSla(), mapCellFormat);
	}

	private List<String> buildListColumnNameRow1Sheet1() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Pengirim");
		listColumnNameTemp.add("Tanggal Terima Surat");
		listColumnNameTemp.add("Nomor Surat");
		listColumnNameTemp.add("Tanggal Surat");
		listColumnNameTemp.add("Perihal");
		listColumnNameTemp.add("Ringkasan Surat");
		listColumnNameTemp.add("Tindak Lanjut");
		listColumnNameTemp.add("Target Date");
		listColumnNameTemp.add("Status");
		listColumnNameTemp.add("PIC 1");
		listColumnNameTemp.add("PIC 2");
		listColumnNameTemp.add("PIC 3");
		listColumnNameTemp.add("Divis");
		listColumnNameTemp.add("PIC Compliance");
		listColumnNameTemp.add("Bukti Konfirmasi");
		listColumnNameTemp.add("Tanggal Konformasi");
		listColumnNameTemp.add("Tanggal Tindak Lanjut");
		listColumnNameTemp.add("Keterangan");
		listColumnNameTemp.add("SLA");

		return listColumnNameTemp;
	}

	private void writeObjectToExcelSheet2(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex,
			int rowNum, ReportSuratMasukAmlRekap arrObj, int columnIndex) throws Exception {

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, rowNum, mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getPengirimSuratEn(), arrObj.getPengirimSuratIn()),
				mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTotalSuratMasuk(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getUndangan(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getNonUndangan(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTindakLanjutYes(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTindakLanjutNo(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getInProgress(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getClosed(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getMeetSla(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getBeforeSla(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getOverSla(), mapCellFormat);
	}

	private void writeObjectToExcelSheet3(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex,
			int rowNum, ReportSuratMasukAmlRekap arrObj, int columnIndex) throws Exception {

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, rowNum, mapCellFormat);

		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getPengirimSuratEn(), arrObj.getPengirimSuratIn()),
				mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTotalSuratMasuk(), mapCellFormat);
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
		listColumnNameTemp.add("Pengirim");
		listColumnNameTemp.add("Total Surat Masuk");
		listColumnNameTemp.add("Tipe Surat");
		listColumnNameTemp.add("Tindak Lanjut");
		listColumnNameTemp.add("Status Tindak Lanjut (Yes)");
		listColumnNameTemp.add("SLA");

		return listColumnNameTemp;
	}

	private List<String> buildListColumnNameRow2Sheet2() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Undangan");
		listColumnNameTemp.add("Non Undangan");
		listColumnNameTemp.add("Yes");
		listColumnNameTemp.add("No");
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
		listColumnNameTemp.add("Pengirim");
		listColumnNameTemp.add("Total Surat Masuk");
		listColumnNameTemp.add("Tindak Lanjut");
		listColumnNameTemp.add("Status Tindak Lanjut (Yes)");
		listColumnNameTemp.add("SLA");

		return listColumnNameTemp;
	}

	private List<String> buildListColumnNameRow2Sheet3() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Yes");
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("In Progress");
		listColumnNameTemp.add("Closed");
		listColumnNameTemp.add("Meet SLA");
		listColumnNameTemp.add("Before SLA");
		listColumnNameTemp.add("Over SLA");

		return listColumnNameTemp;
	}

	public void writeExcelHeader(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat) throws Exception {

		String senderCodeName = "";

		int row = 0;
		int columnStart = 0;

		SimpleDateFormat printTimestamp = new SimpleDateFormat(CommonConstants.INPUT_DATE_TIME_FORMAT);
		String excelPrintDate = printTimestamp.format(new Date());

		// Create Header [start]
		CellStyle cfHeaderValue = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_VALUE);
		CellStyle cfHeaderLabel = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_LABEL);

		// Create Header [end]

		// Create Header Title [Start]
		if (sheet.getSheetName().equals(SHEET_NAME_REPORT_SURAT_MASUK_AML_DETAIL)) {
			reportUtil.writeCellTitle(sheet, 4, "Report Surat Masuk AML Detail", mapCellFormat);
		} else {
			reportUtil.writeCellTitle(sheet, 4, "Report Surat Masuk AML Rekap", mapCellFormat);
		}

		row++;
		row++;

		searchCreationDateFrom = getSearchCriteriaValue(WHERE_CREATION_DATE_FROM);
		searchCreationDateTo = getSearchCriteriaValue(WHERE_CREATION_DATE_TO);
		searchSenderCode = getSearchCriteriaValue(WHERE_SENDER_CODE);
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
		
		ParameterDetail getSenderCodeName = parameterDetailService.getParameterDetailByParamDtlCode(searchSenderCode);

		if (getSenderCodeName != null) {
			senderCodeName = runnableFacesUtil.retrieveLocaleMessage(getSenderCodeName.getNameEn(),
					getSenderCodeName.getNameIn());
		}

		// Label Creation Date
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportSuratMasukAmlDetailCreationDate") + " : ", cfHeaderLabel);

		// Value Creation Date
		reportUtil.writeCell(sheet, row, columnStart + 1, from + " "
				+ ((StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to))
						? runnableFacesUtil.retrieveMessage("textUntil")
						: "")
				+ " " + to, cfHeaderValue);
		row++;

		// Label Target Date
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportSuratMasukAmlDetailTargetDate") + " : ", cfHeaderLabel);

		// Value Target Date
		reportUtil.writeCell(sheet, row, columnStart + 1,
				targetFrom + " "
						+ ((StringUtils.isNotBlank(targetFrom) && StringUtils.isNotBlank(targetTo))
								? runnableFacesUtil.retrieveMessage("textUntil")
								: "")
						+ " " + targetTo,
				cfHeaderValue);
		row++;

		// Label Sender code
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportSuratMasukAmlDetailSender") + " : ", cfHeaderLabel);
		// Value Sender code
		reportUtil.writeCell(sheet, row, columnStart + 1, senderCodeName, cfHeaderValue);
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

	public ReportSuratMasukAmlDetailService getReportSuratMasukAmlDetailService() {
		return reportSuratMasukAmlDetailService;
	}

	public void setReportSuratMasukAmlDetailService(ReportSuratMasukAmlDetailService reportSuratMasukAmlDetailService) {
		this.reportSuratMasukAmlDetailService = reportSuratMasukAmlDetailService;
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

	public String getSearchSenderCode() {
		return searchSenderCode;
	}

	public void setSearchSenderCode(String searchSenderCode) {
		this.searchSenderCode = searchSenderCode;
	}

	public CommonReportUtil getReportUtil() {
		return reportUtil;
	}

	public void setReportUtil(CommonReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}

	public ReportSuratMasukAmlRekapService getReportSuratMasukAmlRekapService() {
		return reportSuratMasukAmlRekapService;
	}

	public void setReportSuratMasukAmlRekapService(ReportSuratMasukAmlRekapService reportSuratMasukAmlRekapService) {
		this.reportSuratMasukAmlRekapService = reportSuratMasukAmlRekapService;
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
