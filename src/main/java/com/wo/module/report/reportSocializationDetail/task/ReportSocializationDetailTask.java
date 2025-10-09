package com.wo.module.report.reportSocializationDetail.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
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
import com.wo.module.common.model.MergedCell;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.report.CommonReportUtil;
import com.wo.module.common.util.DateUtil;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.util.MathUtil;
import com.wo.module.common.utility.SCMApiUpload;
import com.wo.module.common.utility.SCMApiUploadImpl;
import com.wo.module.lov.bean.RunnableFacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.report.reportGen.constant.ReportSheetNameConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.report.reportSocializationDetail.constant.ReportSocializationDetailConstant;
import com.wo.module.report.reportSocializationDetail.service.ReportSocializationDetailService;
import com.wo.module.report.reportSocializationDetail.vo.ReportSocializationDetailVo;
import com.wo.module.report.reportSocializationRekap.model.ReportSocializationRekap;
import com.wo.module.report.reportSocializationRekap.service.ReportSocializationRekapService;

public class ReportSocializationDetailTask
		implements Runnable, ReportSheetNameConstant, ReportSocializationDetailConstant {
	static Logger logger = Logger.getLogger(ReportSocializationDetailTask.class);
	public final static String COMPLIANCE_DOC_TYPE_REPORT_SOCIALIZATION_DETAIL = "Report Socialization Detail";
	private static final String PIE_CHART_TITLE = "Total Sosialisasi";
	
	private Long reportGenId;
	@SuppressWarnings("unused")
	private String userId;
	private String userNikName;
	private String documentTypeName;

	private ReportGenService reportGenService;
	private ParameterDetailService parameterDetailService;
	private RunnableFacesUtil runnableFacesUtil;
	private ReportSocializationDetailService reportSocializationDetailService;
	private ReportSocializationRekapService reportSocializationRekapService;

	@SuppressWarnings("rawtypes")
	private List<? extends SearchObject> searchCriteria;

	private String searchCreationDateFrom;
	private String searchCreationDateTo;
	private String searchProvType;
	private String searchDocType;
	private String searchTargetDateFrom;
	private String searchTargetDateTo;

	private CommonReportUtil reportUtil = new CommonReportUtil();

	@SuppressWarnings("rawtypes")
	public ReportSocializationDetailTask(Long reportGenId, ReportGenService reportGenService,
			ParameterDetailService parameterDetailService, RunnableFacesUtil runnableFacesUtil,
			ReportSocializationDetailService reportSocializationDetailService,
			ReportSocializationRekapService reportSocializationRekapService,
			List<? extends SearchObject> searchCriteria, String userNikName, String documentTypeName) {
		super();
		this.reportGenId = reportGenId;
		this.reportGenService = reportGenService;
		this.parameterDetailService = parameterDetailService;
		this.setRunnableFacesUtil(runnableFacesUtil);
		this.reportSocializationDetailService = reportSocializationDetailService;
		this.setReportSocializationRekapService(reportSocializationRekapService);
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
				COMPLIANCE_DOC_TYPE_REPORT_SOCIALIZATION_DETAIL);
		saf.upload();
		return saf.getAsUploadedFileWO();
	}

	@SuppressWarnings("unused")
	private String writeToFile(List<Integer> listColumnView) throws Exception {
		String sheetName1 = SHEET_NAME_REPORT_SOCIALIZATION_DETAIL;
		String sheetName2 = SHEET_NAME_REPORT_SOCIALIZATION_REKAP_CATEGORY_DOCUMENT;
		String sheetName3 = SHEET_NAME_REPORT_SOCIALIZATION_REKAP_TOTAL_SOCIALIZATION;
		String sheetName4 = SHEET_NAME_REPORT_SOCIALIZATION_DIAGRAM;

		String fileNamePrefix = "ReportSocializationDetail";

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

		List<ReportSocializationDetailVo> sheet1Results = (List<ReportSocializationDetailVo>) reportSocializationDetailService
				.getReportSocializationDetailAsVo(searchCriteria);

		List<Integer> listColumnViewByCategoryDocument = new ArrayList<Integer>();
		List<Integer> listColumnViewByTotalSocialization = new ArrayList<Integer>();

		try {
			fileOutputStream = new FileOutputStream(filePath + fileName);
			workbook = new XSSFWorkbook();

			writeSheetSocializationDetail(listColumnView, sheetName1, workbook, buildListColumnNameRow1Sheet1,
					buildListColumnNameRow2Sheet1, sheet1Results);

			writeSheetSocializationRekap(workbook, listColumnViewByCategoryDocument,
					listColumnViewByTotalSocialization);

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

	private void writeSheetSocializationDetail(List<Integer> listColumnView, String sheetName1, XSSFWorkbook workbook,
			List<String> buildListColumnNameRow1Sheet1, List<String> buildListColumnNameRow2Sheet1,
			List<ReportSocializationDetailVo> sheet1Results) throws Exception {
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

		// write sheet 1 column header [start]
		// logger.debug("listColumnNameAlias = " + listColumnNameAlias);
		CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER);
//			CellStyle cfColumnHeaderNumber = mapCellFormat
//					.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER_NUMBER);
		int rowIndex = 11;
		if (buildListColumnNameRow1Sheet1 != null && !buildListColumnNameRow1Sheet1.isEmpty()) {
			// logger.debug("listColumnNameAlias = " +
			// listColumnNameAlias.size());

			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow1Sheet1) {

				getReportUtil().writeCell(sheet1, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);

				// Merged Area
				if (columnIndex == 9) {
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex += 2);
					sheet1.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet1);// apply border
				} else
					sheet1.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex + 1, columnIndex, columnIndex));

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
		// write sheet 1 column header [end]

		// write sheet 1 data detail [start]
		if (sheet1Results != null && !sheet1Results.isEmpty()) {
			rowIndex += 1;

			int rowNum = 1;
			int rowMergedStart = 0;
			int rowMergedEnd = rowIndex;

			List<MergedCell> mergedRow = new ArrayList<MergedCell>();
			BigInteger idPreviousVal = null;
			for (ReportSocializationDetailVo arrObj : sheet1Results) {
				int columnIndex = 0;

				// if id not equal or last row
				if (idPreviousVal != null && !idPreviousVal.equals(arrObj.getCnt())) {
					rowNum++;

				}
				writeObjectToExcel(sheet1, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);

				// if id same as previous, add cntMergedRow
				if (idPreviousVal == null) {
					idPreviousVal = arrObj.getCnt(); // 1
				} else if (idPreviousVal.equals(arrObj.getCnt())) {
					if (rowMergedStart == 0)
						rowMergedStart = rowIndex - 1;// row sebelumnya, karena pengecekan sama nya di next row

					rowMergedEnd = rowIndex;
				}

				// if id not equal or last row
				if (!idPreviousVal.equals(arrObj.getCnt())
						|| sheet1Results.indexOf(arrObj) == (sheet1Results.size() - 1)) {
					if ((rowMergedStart != 0 && rowMergedStart != rowMergedEnd)) {
						rowMergedStart = addToMergedList(rowMergedStart, rowMergedEnd, mergedRow);
					}

				}

				rowIndex++;
//					rowNum++;

				idPreviousVal = arrObj.getCnt();
			}

			// int mergedRow = rowIndex;

			// Merged Area
			int mergedColIdx[] = { 0, 1, 2, 3, 4, 5, 6 };
			for (MergedCell mc : mergedRow) {
				for (int i = 0; i < buildListColumnNameRow2Sheet1().size(); i++) {

					if (MathUtil.intContains(mergedColIdx, i)) {
						CellRangeAddress cra = new CellRangeAddress(mc.getRowStart(), mc.getRowEnd(), i, i);
						sheet1.addMergedRegion(cra);
						reportUtil.mergedContentColumnStyle(cra, sheet1);// apply border
					}
				}
			}

		}
		// write sheet 1 data detail [end]
		sheet1.autoSizeColumn(18);
	}

	private void writeSheetSocializationRekap(XSSFWorkbook workbook, List<Integer> listColumnViewByCategoryDocument,
			List<Integer> listColumnVewByTotalSocialization) throws Exception {
		List<String> buildListColumnNameRow1Sheet1 = buildListColumnNameSheet1();
		List<String> buildListColumnNameRow2Sheet1 = buildListColumnRow2NameSheet1();

		List<String> buildListColumnNameRow1Sheet2 = buildListColumnNameSheet2();
		List<String> buildListColumnNameRow2Sheet2 = buildListColumnRow2NameSheet2();

		List<ReportSocializationRekap> sheet1Results = (List<ReportSocializationRekap>) getReportSocializationRekapService()
				.getReportSocializationRekapByKategoriDokumenData(searchCriteria);
		List<ReportSocializationRekap> sheet2Results = (List<ReportSocializationRekap>) getReportSocializationRekapService()
				.getReportSocializationRekapByTotalSosialisasiData(searchCriteria);

		String sheetName2 = SHEET_NAME_REPORT_SOCIALIZATION_REKAP_CATEGORY_DOCUMENT;
		String sheetName3 = SHEET_NAME_REPORT_SOCIALIZATION_REKAP_TOTAL_SOCIALIZATION;
		String sheetName4 = SHEET_NAME_REPORT_SOCIALIZATION_DIAGRAM;
		XSSFSheet sheet2 = workbook.createSheet(sheetName2);
		XSSFSheet sheet3 = workbook.createSheet(sheetName3);

		String filePath = getReportUtil().retrieveFilePath(parameterDetailService);

		// setting column size sheet 1
		if (listColumnViewByCategoryDocument != null) {
			for (int i = 0; i < listColumnViewByCategoryDocument.size(); i++) {
				sheet2.setColumnWidth(i, listColumnViewByCategoryDocument.get(i));
				sheet2.autoSizeColumn(i);

			}
		}

		// setting column size sheet 2
		if (listColumnVewByTotalSocialization != null) {
			for (int j = 0; j < listColumnVewByTotalSocialization.size(); j++) {
				sheet2.setColumnWidth(j, listColumnVewByTotalSocialization.get(j));
				sheet2.autoSizeColumn(j);
			}
		}

		Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
		getReportUtil().createExcelCellFormat(workbook, mapCellFormat);

		this.writeExcelHeader(sheet2, mapCellFormat);
		this.writeExcelHeader(sheet3, mapCellFormat);

		CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER);

		// sheet 1 header [start]
		writeSheet2(buildListColumnNameRow1Sheet1, buildListColumnNameRow2Sheet1, sheet1Results, sheet2, mapCellFormat,
				cfColumnHeader);
		// sheet 1 data [end]

		// sheet 2 header [start]
		writeSheet3(buildListColumnNameRow1Sheet2, buildListColumnNameRow2Sheet2, sheet2Results, sheet3, mapCellFormat,
				cfColumnHeader);
		// sheet 2 data [end]

		// sheet 3 header [start]
		writeSheet4(sheetName4, filePath, workbook, sheet2Results);
		// sheet 3 header [end]
	}

	private void writeSheet4(String sheetName4, String filePath, XSSFWorkbook workbook,
			List<ReportSocializationRekap> sheet2Results) throws IOException, FileNotFoundException {
		XSSFSheet sheet3 = workbook.createSheet(sheetName4);
		CreationHelper helper = workbook.getCreationHelper();

		createPie(filePath, workbook, sheet2Results, sheet3, helper);
		createBar(filePath, workbook, sheet2Results, sheet3, helper);

	}

	@SuppressWarnings("rawtypes")
	private void createPie(String filePath, XSSFWorkbook workbook, List<ReportSocializationRekap> sheet2Results,
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

	@SuppressWarnings("rawtypes")
	private void createBar(String filePath, XSSFWorkbook workbook, List<ReportSocializationRekap> sheet2Results,
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

//		PiePlot3D plot = (PiePlot3D) chart.getPlot();
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

	private PieDataset createDataset(List<ReportSocializationRekap> result) {
		DefaultPieDataset dataset = new DefaultPieDataset();

		for (ReportSocializationRekap reportSocializationRekap : result) {
			dataset.setValue(
					runnableFacesUtil.retrieveLocaleMessage(reportSocializationRekap.getJenisPeraturanEn(),
							reportSocializationRekap.getJenisPeraturanIn()),
					reportSocializationRekap.getTotalSosialisasi());
		}
		return dataset;
	}

	private File printBar(DefaultCategoryDataset dataset, String filePath) throws IOException {

//		JFreeChart chart = ChartFactory.createBarChart3D("", "", "", dataset);
		JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset);

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

	private DefaultCategoryDataset createBarDataset(List<ReportSocializationRekap> result) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (ReportSocializationRekap reportSocializationRekap : result) {
			dataset.addValue(reportSocializationRekap.getTotalSosialisasi(),
					runnableFacesUtil.retrieveLocaleMessage(reportSocializationRekap.getJenisPeraturanEn(),
							reportSocializationRekap.getJenisPeraturanIn()),
					"Total sosialisasi");
			dataset.addValue(reportSocializationRekap.getTindakLanjutYes(),
					runnableFacesUtil.retrieveLocaleMessage(reportSocializationRekap.getJenisPeraturanEn(),
							reportSocializationRekap.getJenisPeraturanIn()),
					"Perlu tindak lanjut");
			dataset.addValue(reportSocializationRekap.getTindakLanjutNo(),
					runnableFacesUtil.retrieveLocaleMessage(reportSocializationRekap.getJenisPeraturanEn(),
							reportSocializationRekap.getJenisPeraturanIn()),
					"Tidak ada tindak lanjut");
			dataset.addValue(reportSocializationRekap.getStatusTindakLanjutInProgress(),
					runnableFacesUtil.retrieveLocaleMessage(reportSocializationRekap.getJenisPeraturanEn(),
							reportSocializationRekap.getJenisPeraturanIn()),
					"In Progress");
			dataset.addValue(reportSocializationRekap.getStatusTindakLanjutClosed(),
					runnableFacesUtil.retrieveLocaleMessage(reportSocializationRekap.getJenisPeraturanEn(),
							reportSocializationRekap.getJenisPeraturanIn()),
					"Closed");
			dataset.addValue(reportSocializationRekap.getMeetSla(),
					runnableFacesUtil.retrieveLocaleMessage(reportSocializationRekap.getJenisPeraturanEn(),
							reportSocializationRekap.getJenisPeraturanIn()),
					"Meet SLA");
			dataset.addValue(reportSocializationRekap.getBeforeSla(),
					runnableFacesUtil.retrieveLocaleMessage(reportSocializationRekap.getJenisPeraturanEn(),
							reportSocializationRekap.getJenisPeraturanIn()),
					"Before SLA");
			dataset.addValue(reportSocializationRekap.getOverSla(),
					runnableFacesUtil.retrieveLocaleMessage(reportSocializationRekap.getJenisPeraturanEn(),
							reportSocializationRekap.getJenisPeraturanIn()),
					"Over SLA");
		}
		return dataset;
	}

	private void writeSheet2(List<String> buildListColumnNameRow1Sheet1, List<String> buildListColumnNameRow2Sheet1,
			List<ReportSocializationRekap> sheet1Results, XSSFSheet sheet2, Map<String, CellStyle> mapCellFormat,
			CellStyle cfColumnHeader) throws Exception {
		int rowIndexSheet1 = 11;
		if (buildListColumnNameRow1Sheet1 != null && !buildListColumnNameRow1Sheet1.isEmpty()) {
			int columnIndex = 0;

			for (String columnNameAlias : buildListColumnNameRow1Sheet1) {
				getReportUtil().writeCell(sheet2, rowIndexSheet1, columnIndex, columnNameAlias, cfColumnHeader);

				// merged Area Tindak Lanjut
				if (columnIndex == 4) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet1, rowIndexSheet1, columnIndex,
							columnIndex += 1);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else if (columnIndex == 6) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet1, rowIndexSheet1, columnIndex,
							columnIndex += 1);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else if (columnIndex == 8) {
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
		rowIndexSheet1++;
		if (buildListColumnNameRow2Sheet1 != null && !buildListColumnNameRow2Sheet1.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow2Sheet1) {
				getReportUtil().writeCell(sheet2, rowIndexSheet1, columnIndex, columnNameAlias, cfColumnHeader);

				columnIndex++;
			}
		}

		// sheet 1 header [end]

		// sheet 1 data [start]
		if (sheet1Results != null && !sheet1Results.isEmpty()) {
			rowIndexSheet1 += 1;
			int rowNum = 1;

			for (ReportSocializationRekap arrObj : sheet1Results) {
				int columnIndex = 0;

				getReportUtil().writeCellDetail(sheet2, rowIndexSheet1, columnIndex++, rowNum, mapCellFormat);

				getReportUtil().writeCellDetail(sheet2, rowIndexSheet1, columnIndex++, runnableFacesUtil
						.retrieveLocaleMessage(arrObj.getJenisPeraturanEn(), arrObj.getJenisPeraturanIn()),
						mapCellFormat);

				getReportUtil().writeCellDetail(sheet2, rowIndexSheet1, columnIndex++, runnableFacesUtil
						.retrieveLocaleMessage(arrObj.getKategoriDokumenEn(), arrObj.getKategoriDokumenIn()),
						mapCellFormat);

				getReportUtil().writeCellDetail(sheet2, rowIndexSheet1, columnIndex++, arrObj.getTotalSosialisasi(),
						mapCellFormat);

				getReportUtil().writeCellDetail(sheet2, rowIndexSheet1, columnIndex++, arrObj.getTindakLanjutYes(),
						mapCellFormat);

				getReportUtil().writeCellDetail(sheet2, rowIndexSheet1, columnIndex++, arrObj.getTindakLanjutNo(),
						mapCellFormat);

				getReportUtil().writeCellDetail(sheet2, rowIndexSheet1, columnIndex++,
						arrObj.getStatusTindakLanjutInProgress(), mapCellFormat);

				getReportUtil().writeCellDetail(sheet2, rowIndexSheet1, columnIndex++,
						arrObj.getStatusTindakLanjutClosed(), mapCellFormat);

				getReportUtil().writeCellDetail(sheet2, rowIndexSheet1, columnIndex++, arrObj.getMeetSla(),
						mapCellFormat);

				getReportUtil().writeCellDetail(sheet2, rowIndexSheet1, columnIndex++, arrObj.getBeforeSla(),
						mapCellFormat);

				getReportUtil().writeCellDetail(sheet2, rowIndexSheet1, columnIndex++, arrObj.getOverSla(),
						mapCellFormat);

				rowIndexSheet1++;
				rowNum++;
			}

			int mergedRow = rowIndexSheet1;

			sheet2.addMergedRegion(CellRangeAddress.valueOf("A" + (mergedRow + 1) + ":B" + (mergedRow + 1) + ""));
		}
	}

	private void writeSheet3(List<String> buildListColumnNameRow1Sheet2, List<String> buildListColumnNameRow2Sheet2,
			List<ReportSocializationRekap> sheet2Results, XSSFSheet sheet3, Map<String, CellStyle> mapCellFormat,
			CellStyle cfColumnHeader) throws Exception {
		int rowIndexSheet2 = 11;
		if (buildListColumnNameRow1Sheet2 != null && !buildListColumnNameRow1Sheet2.isEmpty()) {
			int columnIndex = 0;

			for (String columnNameAlias : buildListColumnNameRow1Sheet2) {

				getReportUtil().writeCell(sheet3, rowIndexSheet2, columnIndex, columnNameAlias, cfColumnHeader);

				// Mergerd Area
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
		rowIndexSheet2++;
		if (buildListColumnNameRow2Sheet2 != null && !buildListColumnNameRow2Sheet2.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow2Sheet2) {
				getReportUtil().writeCell(sheet3, rowIndexSheet2, columnIndex, columnNameAlias, cfColumnHeader);

				columnIndex++;
			}
		}
		// sheet 2 header [end]

		// sheet 2 data [start]
		if (sheet2Results != null && !sheet2Results.isEmpty()) {
			rowIndexSheet2 += 1;
			int rowNum = 1;

			for (ReportSocializationRekap arrObj : sheet2Results) {
				int columnIndex = 0;

				getReportUtil().writeCellDetail(sheet3, rowIndexSheet2, columnIndex++, rowNum, mapCellFormat);

				getReportUtil().writeCellDetail(sheet3, rowIndexSheet2, columnIndex++, runnableFacesUtil
						.retrieveLocaleMessage(arrObj.getJenisPeraturanEn(), arrObj.getJenisPeraturanIn()),
						mapCellFormat);

				getReportUtil().writeCellDetail(sheet3, rowIndexSheet2, columnIndex++, arrObj.getTotalSosialisasi(),
						mapCellFormat);

				getReportUtil().writeCellDetail(sheet3, rowIndexSheet2, columnIndex++, arrObj.getTindakLanjutYes(),
						mapCellFormat);

				getReportUtil().writeCellDetail(sheet3, rowIndexSheet2, columnIndex++, arrObj.getTindakLanjutNo(),
						mapCellFormat);

				getReportUtil().writeCellDetail(sheet3, rowIndexSheet2, columnIndex++,
						arrObj.getStatusTindakLanjutInProgress(), mapCellFormat);

				getReportUtil().writeCellDetail(sheet3, rowIndexSheet2, columnIndex++,
						arrObj.getStatusTindakLanjutClosed(), mapCellFormat);

				getReportUtil().writeCellDetail(sheet3, rowIndexSheet2, columnIndex++, arrObj.getMeetSla(),
						mapCellFormat);

				getReportUtil().writeCellDetail(sheet3, rowIndexSheet2, columnIndex++, arrObj.getBeforeSla(),
						mapCellFormat);

				getReportUtil().writeCellDetail(sheet3, rowIndexSheet2, columnIndex++, arrObj.getOverSla(),
						mapCellFormat);

				rowIndexSheet2++;
				rowNum++;
			}

			int mergedRow = rowIndexSheet2;

			// mergerd area
			sheet3.addMergedRegion(CellRangeAddress.valueOf("A" + (mergedRow + 1) + ":B" + (mergedRow + 1) + ""));

		}
	}

	private List<String> buildListColumnNameSheet1() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Jenis Peraturan");
		listColumnNameTemp.add("Kategori Dokumen");
		listColumnNameTemp.add("Total Sosialisasi");
		listColumnNameTemp.add("Tindak Lanjut");
		listColumnNameTemp.add("Status Tindak Lanjut");
		listColumnNameTemp.add("SLA");

		return listColumnNameTemp;
	}

	private List<String> buildListColumnRow2NameSheet1() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("");
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

	private List<String> buildListColumnNameSheet2() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Kategori Dokumen");
		listColumnNameTemp.add("Total Sosialisasi");
		listColumnNameTemp.add("Tindak Lanjut");
		listColumnNameTemp.add("Status Tindak Lanjut");
		listColumnNameTemp.add("SLA");

		return listColumnNameTemp;
	}

	private List<String> buildListColumnRow2NameSheet2() {
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

	private void writeObjectToExcel(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum,
			ReportSocializationDetailVo arrObj, int columnIndex) throws Exception {
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, rowNum, mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getPeraturanEn(), arrObj.getPeraturanIn()),
				mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getJenisPeraturanEn(), arrObj.getJenisPeraturanIn()),
				mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getNoPeraturan(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getJudulPeraturanEn(), arrObj.getJudulPeraturanIn()),
				mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getKategoriDokumenEn(), arrObj.getKategoriDokumenIn()),
				mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTindakLanjut(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTindakLanjutNote(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTargetDate(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getRescheduleTargetDate1(),
				mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getRescheduleTargetDate2(),
				mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getRescheduleTargetDate3(),
				mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getStatusEn(), arrObj.getStatusIn()), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getPic1(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getPic2(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getPic3(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getDivisi(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getPicCompliance(), mapCellFormat);
		getReportUtil().writeWrapCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getBuktiKonfirmasi(),
				mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTanggalKonfirmasi(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTanggalTindakLanjut(),
				mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getKeterangan(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getSla(), mapCellFormat);
	}

	private int addToMergedList(int rowMergedStart, int rowMergedEnd, List<MergedCell> mergedRow) {
		MergedCell mergedMap = new MergedCell();
		mergedMap.setRowStart(rowMergedStart);
		mergedMap.setRowEnd(rowMergedEnd);

		mergedRow.add(mergedMap);

		rowMergedStart = 0;
		return rowMergedStart;
	}

	private List<String> buildListColumnNameRow1Sheet1() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Peraturan");
		listColumnNameTemp.add("Jenis Peraturan");
		listColumnNameTemp.add("Nomor Peraturan");
		listColumnNameTemp.add("Judul Peraturan");
		listColumnNameTemp.add("Kategori Dokumen");
		listColumnNameTemp.add("Tindak Lanjut");
		listColumnNameTemp.add("Tindak Lanjut Note");
		listColumnNameTemp.add("TARGET DATE");
		listColumnNameTemp.add("RESCHEDULE TARGET");
		listColumnNameTemp.add("STATUS");
		listColumnNameTemp.add("PIC 1");
		listColumnNameTemp.add("PIC 2");
		listColumnNameTemp.add("PIC 3");
		listColumnNameTemp.add("Divisi");
		listColumnNameTemp.add("PIC Compliance");
		listColumnNameTemp.add("Bukti Konfirmasi");
		listColumnNameTemp.add("Tanggal Konfirmasi");
		listColumnNameTemp.add("Tanggal Tindak Lanjut");
		listColumnNameTemp.add("Keterangan");
		listColumnNameTemp.add("SLA");
		return listColumnNameTemp;
	}

	private List<String> buildListColumnNameRow2Sheet1() {
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
		listColumnNameTemp.add("I");
		listColumnNameTemp.add("II");
		listColumnNameTemp.add("III");
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

	public void writeExcelHeader(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat) throws Exception {

		String provTypeName = "";

		int row = 0;
		int columnStart = 0;

//		Employee employee = employeeService.getEmployee(userId);
//		String excelPrintEmployeeName = employee.getName();

		SimpleDateFormat printTimestamp = new SimpleDateFormat(CommonConstants.INPUT_DATE_TIME_FORMAT);
		String excelPrintDate = printTimestamp.format(new Date());

		// Create Header [start]
		CellStyle cfHeaderValue = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_VALUE);
		CellStyle cfHeaderLabel = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_LABEL);

		// Create Header [end]

		// Create Header Title
		if (sheet.getSheetName().equals(SHEET_NAME_REPORT_SOCIALIZATION_DETAIL)) {
			reportUtil.writeCellTitle(sheet, 4, "Report Sosialisasi Detail", mapCellFormat);
		} else {
			reportUtil.writeCellTitle(sheet, 4, "Report Sosialisasi Rekap", mapCellFormat);
		}

		row++;
		row++;

		searchCreationDateFrom = getSearchCriteriaValue(WHERE_CREATION_DATE_FROM);
		searchCreationDateTo = getSearchCriteriaValue(WHERE_CREATION_DATE_TO);
		searchProvType = getSearchCriteriaValue(WHERE_PROV_TYPE);
		searchDocType = getSearchCriteriaValue(WHERE_DOC_TYPE);
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
		
		// get name En dan In
		ParameterDetail getProvTypeName = parameterDetailService.getParameterDetailByParamDtlCode(searchProvType);

		// initial
		if (getProvTypeName != null) {
			provTypeName = runnableFacesUtil.retrieveLocaleMessage(getProvTypeName.getNameEn(),
					getProvTypeName.getNameIn());
		}

		// Label Creation Date
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportSocializationDetailCreationDate") + " : ", cfHeaderLabel);

		// value Creation Date
		reportUtil.writeCell(sheet, row, columnStart + 1, from + " "
				+ ((StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to))
						? runnableFacesUtil.retrieveMessage("textUntil")
						: "")
				+ " " + to, cfHeaderValue);
		row++;

		// Label Target Date
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportSocializationDetailTargetDate") + " : ", cfHeaderLabel);

		// value Target Date
		reportUtil.writeCell(sheet, row, columnStart + 1,
				targetFrom + " "
						+ ((StringUtils.isNotBlank(targetFrom) && StringUtils.isNotBlank(targetTo))
								? runnableFacesUtil.retrieveMessage("textUntil")
								: "")
						+ " " + targetTo,
				cfHeaderValue);
		row++;

		// Label Peraturan
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportSocializationProvType") + " : ", cfHeaderLabel);
		// Value Peraturan
		reportUtil.writeCell(sheet, row, columnStart + 1, provTypeName, cfHeaderValue);
		row++;

		// Label Doc Type
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportSocializationDocType") + " : ", cfHeaderLabel);
		// Value Officer Name
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

	public ReportGenService getReportGenService() {
		return reportGenService;
	}

	public void setReportGenService(ReportGenService reportGenService) {
		this.reportGenService = reportGenService;
	}

	public Long getReportGenId() {
		return reportGenId;
	}

	public void setReportGenId(Long reportGenId) {
		this.reportGenId = reportGenId;
	}

	public CommonReportUtil getReportUtil() {
		return reportUtil;
	}

	public void setReportUtil(CommonReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}

	public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public ReportSocializationDetailService getReportSocializationDetailService() {
		return reportSocializationDetailService;
	}

	public void setReportSocializationDetailService(ReportSocializationDetailService reportSocializationDetailService) {
		this.reportSocializationDetailService = reportSocializationDetailService;
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

	public String getSearchProvType() {
		return searchProvType;
	}

	public void setSearchProvType(String searchProvType) {
		this.searchProvType = searchProvType;
	}

	public String getSearchDocType() {
		return searchDocType;
	}

	public void setSearchDocType(String searchDocType) {
		this.searchDocType = searchDocType;
	}

	public RunnableFacesUtil getRunnableFacesUtil() {
		return runnableFacesUtil;
	}

	public void setRunnableFacesUtil(RunnableFacesUtil runnableFacesUtil) {
		this.runnableFacesUtil = runnableFacesUtil;
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

	public ReportSocializationRekapService getReportSocializationRekapService() {
		return reportSocializationRekapService;
	}

	public void setReportSocializationRekapService(ReportSocializationRekapService reportSocializationRekapService) {
		this.reportSocializationRekapService = reportSocializationRekapService;
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
