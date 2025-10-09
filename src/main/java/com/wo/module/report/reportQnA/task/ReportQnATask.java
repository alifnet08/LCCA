package com.wo.module.report.reportQnA.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
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
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

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
import com.wo.module.report.reportQnA.constant.ReportQnAConstant;
import com.wo.module.report.reportQnA.service.ReportQnAService;
import com.wo.module.report.reportQnA.vo.ReportQnADetail;
import com.wo.module.report.reportQnA.vo.ReportQnARekap;

public class ReportQnATask implements Runnable, Serializable{

	private static final long serialVersionUID = 9004270031933941764L;
	private static final Logger logger = Logger.getLogger(ReportQnATask.class);
	private static final String REPORT_QNA = "REPORT_QNA";
	private static final String PIE_CHART_TITLE = "Total QNA";
	
	private Long reportGenId;
	private String userNikName;
	
	private ReportGenService reportGenService;
	private ParameterDetailService parameterDetailService;
	private RunnableFacesUtil runnableFacesUtil;
	private ReportQnAService reportQnAService;
	
	@SuppressWarnings("rawtypes")
	private List<? extends SearchObject> searchCriteria;
	
	private String searchQuestionDateFrom;
	private String searchQuestionDateTo;
	private String searchCategory;
	
	private CommonReportUtil reportUtil = new CommonReportUtil();
	
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
	
	@SuppressWarnings("rawtypes")
	public ReportQnATask(Long reportGenId,
			ReportGenService reportGenService,
			ParameterDetailService parameterDetailService,
			RunnableFacesUtil runnableFacesUtil,
			ReportQnAService reportQnAService,
			List<? extends SearchObject> searchCriteria,
			String userNikName) {
		super();
		this.reportGenId = reportGenId;
		this.reportGenService = reportGenService;
		this.parameterDetailService = parameterDetailService;
		this.runnableFacesUtil = runnableFacesUtil;
		this.reportQnAService = reportQnAService;
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
				REPORT_QNA);
		saf.upload();
		return saf.getAsUploadedFileWO();
	}
	
	private String writeToFile(List<Integer> listColumnView) throws Exception {
		String sheetName1 = ReportSheetNameConstant.SHEET_NAME_REPORT_QNA_DETAIL;
		String sheetName2 = ReportSheetNameConstant.SHEET_NAME_REPORT_QNA_REKAP;
		String sheetName3 = ReportSheetNameConstant.SHEET_NAME_REPORT_QNA_DIAGRAM;
		
		String fileNamePrefix = "ReportQnA";

		String fileName = getReportUtil().generateFileName(fileNamePrefix, userNikName);
		String filePath = getReportUtil().retrieveFilePath(parameterDetailService);

		Path dirPath = Paths.get(filePath);
		if (!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}

		XSSFWorkbook workbook = null;
		FileOutputStream fileOutputStream = null;
		
		List<String> buildListColumnNameRow1Sheet1 = buildListColumnNameRow1Sheet1();
		List<String> buildListColumnNameRow1Sheet2 = buildListColumnNameRow1Sheet2();
		
		List<ReportQnADetail> sheet1Results = (List<ReportQnADetail>) reportQnAService.getReportQnADetailAsVo(searchCriteria);
		List<ReportQnARekap> sheet2Results = (List<ReportQnARekap>) reportQnAService.getReportQnARekapAsVo(searchCriteria);
		
		try {
			fileOutputStream = new FileOutputStream(filePath + fileName);
			workbook = new XSSFWorkbook();

			Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
			getReportUtil().createExcelCellFormat(workbook, mapCellFormat);
			
			writeSheet1(listColumnView, sheetName1, workbook, buildListColumnNameRow1Sheet1, sheet1Results,
					mapCellFormat);
			writeSheet2(listColumnView, sheetName2, workbook, buildListColumnNameRow1Sheet2, sheet2Results,
					mapCellFormat);
			
			writeSheet3(sheetName3, filePath, workbook, sheet2Results);
			
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
			List<String> buildListColumnNameRow1Sheet1, List<ReportQnADetail> sheet1Results,
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
			for (ReportQnADetail arrObj : sheet1Results) {
				int columnIndex = 0;
				
				writeObjectToExcelSheet1(sheet1, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);

				rowIndex++;
				rowNum++;
			}
		}
		// write sheet 1 data detail [end]
		sheet1.autoSizeColumn(34);
	}
	
	private void writeSheet2(List<Integer> listColumnView, String sheetName2, XSSFWorkbook workbook,
			List<String> buildListColumnNameRow1Sheet2, List<ReportQnARekap> sheet2Results,
			Map<String, CellStyle> mapCellFormat) throws Exception {
		XSSFSheet sheet2 = workbook.createSheet(sheetName2);
		
		// setting column size [Start]
		if (listColumnView != null) {
			for (int i = 0; i < listColumnView.size(); i++) {
				sheet2.setColumnWidth(i, listColumnView.get(i));
				sheet2.autoSizeColumn(i);
			}
		}
		// setting column size [End]
		
		this.writeExcelHeader(sheet2, mapCellFormat);
		
		// write sheet 1 column header [start]
		CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER);
		int rowIndex = 11;
		
		if (buildListColumnNameRow1Sheet2 != null && !buildListColumnNameRow1Sheet2.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow1Sheet2) {
				getReportUtil().writeCell(sheet2, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);

				columnIndex++;
			}
		}
		// write sheet 1 column header [end]
		
		// write sheet 1 data detail [start]
		if (sheet2Results != null && !sheet2Results.isEmpty()) {
			rowIndex += 1;
			
			int rowNum = 1;
			for (ReportQnARekap arrObj : sheet2Results) {
				int columnIndex = 0;
				
				writeObjectToExcelSheet2(sheet2, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);

				rowIndex++;
				rowNum++;
			}
		}
		// write sheet 1 data detail [end]
		sheet2.autoSizeColumn(34);
	}
	
	private void writeSheet3(String sheetName3, String filePath, XSSFWorkbook workbook,
		List<ReportQnARekap> sheet2Results) throws IOException, FileNotFoundException {
		XSSFSheet sheet3 = workbook.createSheet(sheetName3);
		CreationHelper helper = workbook.getCreationHelper();

		createPie(filePath, workbook, sheet2Results, sheet3, helper);
		createBar(filePath, workbook, sheet2Results, sheet3, helper);
	}
	
	private void writeObjectToExcelSheet1(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum,
			ReportQnADetail arrObj, int columnIndex) throws Exception {
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, rowNum, mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTicketNo(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getPenanya(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTanggalTanya(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getKategori(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getJudul(), mapCellFormat);
		if(arrObj.getQuestion() != null && !StringUtils.isEmpty(arrObj.getQuestion())) {
			// to remove XSS Cross-site scripting
			String questionCleanHtml = Jsoup.clean(arrObj.getQuestion(), Safelist.basic());
			// to parse html to string
			String questionCleanStr = Jsoup.parse(questionCleanHtml).text();
			getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, questionCleanStr, mapCellFormat);
		}else {
			getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getQuestion(), mapCellFormat);
		}
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getPenjawab1(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTanggalJawab1(), mapCellFormat);
		if(arrObj.getJawaban1() != null && !StringUtils.isEmpty(arrObj.getJawaban1())) {
			// to remove XSS Cross-site scripting
			String answer1CleanHtml = Jsoup.clean(arrObj.getJawaban1(), Safelist.basic());
			// to parse html to string
			String answer1CleanStr = Jsoup.parse(answer1CleanHtml).text();
			getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, answer1CleanStr, mapCellFormat);
		}else {
			getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getJawaban1(), mapCellFormat);
		}	
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getPenjawab2(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTanggalJawab2(), mapCellFormat);
		if(arrObj.getJawaban2() != null && !StringUtils.isEmpty(arrObj.getJawaban2())) {
			// to remove XSS Cross-site scripting
			String answer2CleanHtml = Jsoup.clean(arrObj.getJawaban2(), Safelist.basic());
			// to parse html to string
			String answer2CleanStr = Jsoup.parse(answer2CleanHtml).text();
			getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, answer2CleanStr, mapCellFormat);
		}else {
			getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getJawaban2(), mapCellFormat);
		}
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getPenjawab3(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTanggalJawab3(), mapCellFormat);
		if(arrObj.getJawaban3() != null && !StringUtils.isEmpty(arrObj.getJawaban3())) {
			// to remove XSS Cross-site scripting
			String answer3CleanHtml = Jsoup.clean(arrObj.getJawaban3(), Safelist.basic());
			// to parse html to string
			String answer3CleanStr = Jsoup.parse(answer3CleanHtml).text();
			getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, answer3CleanStr, mapCellFormat);
		}else {
			getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getJawaban3(), mapCellFormat);
		}
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getStatus(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getSudahDibaca(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getSLA(), mapCellFormat);
	}
	
	private void writeObjectToExcelSheet2(XSSFSheet sheet2, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum,
			ReportQnARekap arrObj, int columnIndex) throws Exception {
		
		getReportUtil().writeCellDetail(sheet2, rowIndex, columnIndex++, rowNum, mapCellFormat);
		getReportUtil().writeCellDetail(sheet2, rowIndex, columnIndex++, runnableFacesUtil.retrieveLocaleMessage(arrObj.getKategoriEn(), arrObj.getKategoriIn()), mapCellFormat);
		getReportUtil().writeCellDetail(sheet2, rowIndex, columnIndex++, arrObj.getMeetSla(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet2, rowIndex, columnIndex++, arrObj.getOverSla(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet2, rowIndex, columnIndex++, arrObj.getOverDue(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet2, rowIndex, columnIndex++, arrObj.getBeforeSla(), mapCellFormat);
	}
	
	@SuppressWarnings("rawtypes")
	private void createBar(String filePath, XSSFWorkbook workbook,
			List<ReportQnARekap> sheet2Results, XSSFSheet sheet, CreationHelper helper)
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
	
	private DefaultCategoryDataset createBarDataset(List<ReportQnARekap> result) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		for (ReportQnARekap reportQnARekap : result) {
			dataset.addValue(reportQnARekap.getMeetSla(),
					runnableFacesUtil.retrieveLocaleMessage(
							reportQnARekap.getKategoriEn(), reportQnARekap.getKategoriIn()),
					"Meet SLA");
			dataset.addValue(reportQnARekap.getOverSla(),
					runnableFacesUtil.retrieveLocaleMessage(
							reportQnARekap.getKategoriEn(), reportQnARekap.getKategoriIn()),
					"Over SLA");
			dataset.addValue(reportQnARekap.getOverDue(),
					runnableFacesUtil.retrieveLocaleMessage(
							reportQnARekap.getKategoriEn(), reportQnARekap.getKategoriIn()),
					"Over Due");
			dataset.addValue(reportQnARekap.getBeforeSla(),
					runnableFacesUtil.retrieveLocaleMessage(
							reportQnARekap.getKategoriEn(), reportQnARekap.getKategoriIn()),
					"Before SLA");
		}
		
		return dataset;
	}
	
	@SuppressWarnings("rawtypes")
	private void createPie(String filePath, XSSFWorkbook workbook,
			List<ReportQnARekap> sheet2Results, XSSFSheet sheet, CreationHelper helper)
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
	
	private PieDataset createDataset(List<ReportQnARekap> result) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		for (ReportQnARekap reportQnARekap : result) {
			dataset.setValue(
					runnableFacesUtil.retrieveLocaleMessage(
							reportQnARekap.getKategoriEn(), reportQnARekap.getKategoriIn()),
					reportQnARekap.getMeetSla());
			dataset.setValue(
					runnableFacesUtil.retrieveLocaleMessage(
							reportQnARekap.getKategoriEn(), reportQnARekap.getKategoriIn()),
					reportQnARekap.getOverSla());
			dataset.setValue(
					runnableFacesUtil.retrieveLocaleMessage(
							reportQnARekap.getKategoriEn(), reportQnARekap.getKategoriIn()),
					reportQnARekap.getOverDue());
			dataset.setValue(
					runnableFacesUtil.retrieveLocaleMessage(
							reportQnARekap.getKategoriEn(), reportQnARekap.getKategoriIn()),
					reportQnARekap.getBeforeSla());
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
	
	private List<String> buildListColumnNameRow1Sheet1() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Ticket No");
		listColumnNameTemp.add("Penanya");
		listColumnNameTemp.add("Tanggal Tanya");
		listColumnNameTemp.add("Kategori");
		listColumnNameTemp.add("Judul");
		listColumnNameTemp.add("Pertanyaan");
		listColumnNameTemp.add("Penjawab 1");
		listColumnNameTemp.add("Tanggal Penjawab 1");
		listColumnNameTemp.add("Jawaban 1");
		listColumnNameTemp.add("Penjawab 2");
		listColumnNameTemp.add("Tanggal Penjawab 2");
		listColumnNameTemp.add("Jawaban 2");
		listColumnNameTemp.add("Penjawab 3");
		listColumnNameTemp.add("Tanggal Penjawab 3");
		listColumnNameTemp.add("Jawaban 3");
		listColumnNameTemp.add("Status");
		listColumnNameTemp.add("Sudah Dibaca");
		listColumnNameTemp.add("SLA");
		
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow1Sheet2() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Kategori");
		listColumnNameTemp.add("Meet SLA");
		listColumnNameTemp.add("Over SLA");
		listColumnNameTemp.add("Over DUE");
		listColumnNameTemp.add("Before SLA");
		
		return listColumnNameTemp;
	}
	
	public void writeExcelHeader(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat) throws Exception {
		String questionDateFrom = "";
		String questionDateTo = "";
		String categoryName = "";
		int row = 0;
		int columnStart = 0;
		
		SimpleDateFormat printTimestamp = new SimpleDateFormat(CommonConstants.INPUT_DATE_TIME_FORMAT);
		String excelPrintDate = printTimestamp.format(new Date());
		
		// Create Header [start]
		CellStyle cfHeaderValue = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_VALUE);
		CellStyle cfHeaderLabel = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_LABEL);
		// Create Header [end]
		
		// Create Header Title [Start]
		if (sheet.getSheetName().equals(ReportSheetNameConstant.SHEET_NAME_REPORT_QNA_DETAIL)) {
			reportUtil.writeCellTitle(sheet, 4, "Report QnA Detail", mapCellFormat);
		} else {
			reportUtil.writeCellTitle(sheet, 4, "Report QnA Rekap", mapCellFormat);
		}
		row++;
		row++;
		
		searchQuestionDateFrom = getSearchCriteriaValue(ReportQnAConstant.SEARCH_BY_QUESTION_DATE_FROM);
		searchQuestionDateTo = getSearchCriteriaValue(ReportQnAConstant.SEARCH_BY_QUESTION_DATE_TO);
		searchCategory = getSearchCriteriaValue(ReportQnAConstant.SEARCH_BY_CATEGORY);
		
		if (StringUtils.isNotBlank(searchQuestionDateFrom)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchQuestionDateFrom);
				questionDateFrom = DateUtil.dateToString(tmpDate);
			} catch (Exception e) {
				questionDateFrom = "";
			}
		}
		
		if (StringUtils.isNotBlank(searchQuestionDateTo)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchQuestionDateTo);
				questionDateTo = DateUtil.dateToString(tmpDate);
			} catch (Exception e) {
				questionDateTo = "";
			}
		}
		
		ParameterDetail getCategoryName = parameterDetailService.getParameterDetailByParamDtlCode(searchCategory);
		if (getCategoryName != null) {
			categoryName = runnableFacesUtil.retrieveLocaleMessage(getCategoryName.getNameEn(), getCategoryName.getNameIn());
		}
		
		// Label Question Date
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportQnAQuestionDate") + " : ", cfHeaderLabel);

		// Value Question Date
		reportUtil.writeCell(sheet, row, columnStart + 1, questionDateFrom + " "
				+ ((StringUtils.isNotBlank(questionDateFrom) && StringUtils.isNotBlank(questionDateTo))
						? runnableFacesUtil.retrieveMessage("textUntil")
						: "")
				+ " " + questionDateTo, cfHeaderValue);
		row++;
		
		// Label Sender code
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportQnACategory") + " : ", cfHeaderLabel);
		// Value Sender code
		reportUtil.writeCell(sheet, row, columnStart + 1, categoryName, cfHeaderValue);
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

	public ReportQnAService getReportQnAService() {
		return reportQnAService;
	}

	public void setReportQnAService(ReportQnAService reportQnAService) {
		this.reportQnAService = reportQnAService;
	}

	@SuppressWarnings("rawtypes")
	public List<? extends SearchObject> getSearchCriteria() {
		return searchCriteria;
	}

	@SuppressWarnings("rawtypes")
	public void setSearchCriteria(List<? extends SearchObject> searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public String getSearchQuestionDateFrom() {
		return searchQuestionDateFrom;
	}

	public void setSearchQuestionDateFrom(String searchQuestionDateFrom) {
		this.searchQuestionDateFrom = searchQuestionDateFrom;
	}

	public String getSearchQuestionDateTo() {
		return searchQuestionDateTo;
	}

	public void setSearchQuestionDateTo(String searchQuestionDateTo) {
		this.searchQuestionDateTo = searchQuestionDateTo;
	}

	public String getSearchCategory() {
		return searchCategory;
	}

	public void setSearchCategory(String searchCategory) {
		this.searchCategory = searchCategory;
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

	public static String getReportQna() {
		return REPORT_QNA;
	}

	public static String getPieChartTitle() {
		return PIE_CHART_TITLE;
	}

}
