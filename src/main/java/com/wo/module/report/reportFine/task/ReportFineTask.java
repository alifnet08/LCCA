package com.wo.module.report.reportFine.task;

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
import com.wo.module.report.reportFine.constant.ReportFineConstant;
import com.wo.module.report.reportFine.service.ReportFineService;
import com.wo.module.report.reportFine.vo.ReportFineDetailVo;
import com.wo.module.report.reportFine.vo.ReportFineRekapVo;
import com.wo.module.report.reportGen.constant.ReportSheetNameConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.service.ReportGenService;

public class ReportFineTask implements Runnable, Serializable{

	private static final long serialVersionUID = 186925189889272346L;
	private static final Logger logger = Logger.getLogger(ReportFineTask.class);
	private static final String REPORT_FINE = "Report Denda";
	private static final String PIE_CHART_TITLE = "Total Denda";

	private Long reportGenId;
	private String userNikName;

	private ReportGenService reportGenService;
	private ParameterDetailService parameterDetailService;
	private RunnableFacesUtil runnableFacesUtil;
	private ReportFineService reportFineService;
	
	@SuppressWarnings("rawtypes")
	private List<? extends SearchObject> searchCriteria;
	
	private String searchCreationDateFrom;
	private String searchCreationDateTo;
	private String searchSenderCode;
	private String searchTargetDateFrom;
	private String searchTargetDateTo;

	private CommonReportUtil reportUtil = new CommonReportUtil();
	
	@SuppressWarnings("rawtypes")
	public ReportFineTask(Long reportGenId,
			ReportGenService reportGenService,
			ParameterDetailService parameterDetailService,
			RunnableFacesUtil runnableFacesUtil,
			ReportFineService reportFineService,
			List<? extends SearchObject> searchCriteria,
			String userNikName) {
		super();
		this.reportGenId = reportGenId;
		this.reportGenService = reportGenService;
		this.parameterDetailService = parameterDetailService;
		this.runnableFacesUtil = runnableFacesUtil;
		this.reportFineService = reportFineService;
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
				REPORT_FINE);
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
		String sheetName1 = ReportSheetNameConstant.SHEET_NAME_REPORT_FINE_DETAIL;
		String sheetName2 = ReportSheetNameConstant.SHEET_NAME_REPORT_FINE_REKAP;
		String sheetName3 = ReportSheetNameConstant.SHEET_NAME_REPORT_FINE_DIAGRAM;
		
		String fileNamePrefix = "ReportDenda";

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
		List<String> buildListColumnNameRow2Sheet2 = buildListColumnNameRow2Sheet2();
		
		List<ReportFineDetailVo> sheet1Results = (List<ReportFineDetailVo>) reportFineService.getReportFinaDetailAsVo(searchCriteria);
		List<ReportFineRekapVo> sheet2Results = (List<ReportFineRekapVo>) reportFineService.getReportFinaRekapAsVo(searchCriteria);
		
		try {
			fileOutputStream = new FileOutputStream(filePath + fileName);
			workbook = new XSSFWorkbook();

			Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
			getReportUtil().createExcelCellFormat(workbook, mapCellFormat);
			
			writeSheet1(listColumnView, sheetName1, workbook, buildListColumnNameRow1Sheet1, sheet1Results,
					mapCellFormat);
			
			XSSFSheet sheet2 = workbook.createSheet(sheetName2);
			this.writeExcelHeader(sheet2, mapCellFormat);
			
			// write sheet 1 column header [start]
			// first row
			CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER);
			int rowIndexSheet1 = 11;
			if (buildListColumnNameRow1Sheet2 != null && !buildListColumnNameRow1Sheet2.isEmpty()) {

				int columnIndex = 0;
				for (String columnNameAlias : buildListColumnNameRow1Sheet2) {

					getReportUtil().writeCell(sheet2, rowIndexSheet1, columnIndex, columnNameAlias, cfColumnHeader);

					// merged Area
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

			// second row
			rowIndexSheet1++;
			if (buildListColumnNameRow2Sheet2 != null && !buildListColumnNameRow2Sheet2.isEmpty()) {
				int columnIndex = 0;
				for (String columnNameAlias : buildListColumnNameRow2Sheet2) {
					getReportUtil().writeCell(sheet2, rowIndexSheet1, columnIndex, columnNameAlias, cfColumnHeader);

					columnIndex++;
				}
			}

			// write sheet 1 column header [end]
			
			// sheet 1 data [start]
			if (sheet2Results != null && !sheet2Results.isEmpty()) {
				rowIndexSheet1 += 1;
				int rowNum = 1;
				
				for (ReportFineRekapVo arrObj : sheet2Results) {
					int columnIndex = 0;

					writeObjectToExcelSheet2(sheet2, mapCellFormat, rowIndexSheet1, rowNum, arrObj, columnIndex);

					rowIndexSheet1++;
					rowNum++;
				}
			}
			
			// sheet 3 header [start]
			writeSheet3(sheetName3, filePath, workbook, sheet2Results);
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
	
	private void writeSheet1(List<Integer> listColumnView, String sheetName1, XSSFWorkbook workbook,
		List<String> buildListColumnNameRow1Sheet1, List<ReportFineDetailVo> sheet1Results,
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
			for (ReportFineDetailVo arrObj : sheet1Results) {
				int columnIndex = 0;
				
				writeObjectToExcelSheet1(sheet1, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);

				rowIndex++;
				rowNum++;
			}
		}
		// write sheet 1 data detail [end]
		sheet1.autoSizeColumn(34);
	}
	
	private void writeSheet3(String sheetName3, String filePath, XSSFWorkbook workbook,
		List<ReportFineRekapVo> sheet2Results) throws IOException, FileNotFoundException {
		XSSFSheet sheet3 = workbook.createSheet(sheetName3);
		CreationHelper helper = workbook.getCreationHelper();

		createPie(filePath, workbook, sheet2Results, sheet3, helper);
		createBar(filePath, workbook, sheet2Results, sheet3, helper);

	}
	
	private void writeObjectToExcelSheet1(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum,
		ReportFineDetailVo arrObj, int columnIndex) throws Exception {
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, rowNum, mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getPengirimEn(), arrObj.getPengirimIn()),
				mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getTanggalTerimaSurat(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getNoSurat(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getTanggalSurat(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getPerihalEn(), arrObj.getPerihalIn()), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getRingkasanSurat(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getNamaLaporanEn(), arrObj.getNamaLaporanIn()), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getRC(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getWorkingUnit(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getRegion(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getDebitedByOjk(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getFineAmount(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getBreaches(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getRootCause(), mapCellFormat);
	
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getKategoriEn(), arrObj.getKategoriIn()), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getRemedialAction(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getTimeline(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, "", mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getUserMaker(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getUserSpv(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, "", mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getStatusEn(), arrObj.getStatusIn()), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getPic1(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getPic2(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getPic3(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getDivisi(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getPicCompliance(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getTanggalKonfirmasi(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getTanggalTindakLanjut(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getKeterangan(), mapCellFormat);

		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getSla(), mapCellFormat);
	}
	
	private void writeObjectToExcelSheet2(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex,
		int rowNum, ReportFineRekapVo arrObj, int columnIndex) throws Exception {
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, rowNum, mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++,
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getPengirimSuratEn(), arrObj.getPengirimSuratIn()),
				mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTotalDenda(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTipeUndangan(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTindakLanjutYes(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTindakLanjutNo(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getInProgress(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getClosed(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getMeetSla(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getBeforeSla(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getOverSla(), mapCellFormat);
	}
	
	@SuppressWarnings("rawtypes")
	private void createBar(String filePath, XSSFWorkbook workbook, List<ReportFineRekapVo> sheet2Results,
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
	
	private DefaultCategoryDataset createBarDataset(List<ReportFineRekapVo> result) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		for (ReportFineRekapVo reportFineRekapVo : result) {
			dataset.addValue(reportFineRekapVo.getTotalDenda(),
					runnableFacesUtil.retrieveLocaleMessage(reportFineRekapVo.getPengirimSuratEn(),
							reportFineRekapVo.getPengirimSuratIn()),
					"Total Surat Masuk");
			dataset.addValue(reportFineRekapVo.getTindakLanjutYes(),
					runnableFacesUtil.retrieveLocaleMessage(reportFineRekapVo.getPengirimSuratEn(),
							reportFineRekapVo.getPengirimSuratIn()),
					"Perlu Tindak Lanjut");
			dataset.addValue(reportFineRekapVo.getTindakLanjutNo(),
					runnableFacesUtil.retrieveLocaleMessage(reportFineRekapVo.getPengirimSuratEn(),
							reportFineRekapVo.getPengirimSuratIn()),
					"Tidak Ada Tindak Lanjut");
			dataset.addValue(reportFineRekapVo.getInProgress(),
					runnableFacesUtil.retrieveLocaleMessage(reportFineRekapVo.getPengirimSuratEn(),
							reportFineRekapVo.getPengirimSuratIn()),
					"In Progress");
			dataset.addValue(reportFineRekapVo.getClosed(), runnableFacesUtil.retrieveLocaleMessage(
					reportFineRekapVo.getPengirimSuratEn(), reportFineRekapVo.getPengirimSuratIn()), "Closed");
			dataset.addValue(reportFineRekapVo.getMeetSla(),
					runnableFacesUtil.retrieveLocaleMessage(reportFineRekapVo.getPengirimSuratEn(),
							reportFineRekapVo.getPengirimSuratIn()),
					"Meet SLA");
			dataset.addValue(reportFineRekapVo.getBeforeSla(),
					runnableFacesUtil.retrieveLocaleMessage(reportFineRekapVo.getPengirimSuratEn(),
							reportFineRekapVo.getPengirimSuratIn()),
					"Before SLA");
			dataset.addValue(reportFineRekapVo.getOverSla(),
					runnableFacesUtil.retrieveLocaleMessage(reportFineRekapVo.getPengirimSuratEn(),
							reportFineRekapVo.getPengirimSuratIn()),
					"Over SLA");
		}
		return dataset;
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
	
	@SuppressWarnings("rawtypes")
	private void createPie(String filePath, XSSFWorkbook workbook, List<ReportFineRekapVo> sheet2Results,
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
	
	private PieDataset createDataset(List<ReportFineRekapVo> result) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		for (ReportFineRekapVo reportFineRekapVo : result) {
			dataset.setValue(runnableFacesUtil.retrieveLocaleMessage(reportFineRekapVo.getPengirimSuratEn(),
					reportFineRekapVo.getPengirimSuratIn()), reportFineRekapVo.getTotalDenda());
		}
		return dataset;
	}
	
	private File printPie(PieDataset pieDataset, String filePath) throws IOException {

		JFreeChart chart = ChartFactory.createPieChart(PIE_CHART_TITLE, pieDataset, true, true, false);

		PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{1}",
				NumberFormat.getInstance(), NumberFormat.getPercentInstance());

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
	
	private List<String> buildListColumnNameRow1Sheet1() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Regulator");
		listColumnNameTemp.add("Tanggal Terima Surat");
		listColumnNameTemp.add("Nomor Surat");
		listColumnNameTemp.add("Tanggal Surat");		
		listColumnNameTemp.add("Perihal");
		listColumnNameTemp.add("Ringkasan Surat");
		listColumnNameTemp.add("Report Name");
		listColumnNameTemp.add("RC");
		listColumnNameTemp.add("WU/B/SB");
		listColumnNameTemp.add("Region");
		listColumnNameTemp.add("Debited by BI/OJK");
		listColumnNameTemp.add("Nominal");
		listColumnNameTemp.add("Action Plan");
		listColumnNameTemp.add("Root Cause");
		listColumnNameTemp.add("Categories");
		listColumnNameTemp.add("Remedial Action");
		listColumnNameTemp.add("Timeline");
		listColumnNameTemp.add("Booked");
		listColumnNameTemp.add("User (Maker)");
		listColumnNameTemp.add("User (Spv)");
		listColumnNameTemp.add("No.IMDC (sejak Mei - 16)");
		listColumnNameTemp.add("Status");
		listColumnNameTemp.add("PIC 1");
		listColumnNameTemp.add("PIC 2");
		listColumnNameTemp.add("PIC 3");
		listColumnNameTemp.add("Division");
		listColumnNameTemp.add("PIC Compliance");
		listColumnNameTemp.add("Confirmation Date");
		listColumnNameTemp.add("Follow up Date");
		listColumnNameTemp.add("Note");
		listColumnNameTemp.add("SLA");

		return listColumnNameTemp;
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
		String from = "";
		String to = "";
		String targetFrom = "";
		String targetTo = "";
		
		int row = 0;
		int columnStart = 0;

		SimpleDateFormat printTimestamp = new SimpleDateFormat(CommonConstants.INPUT_DATE_TIME_FORMAT);
		String excelPrintDate = printTimestamp.format(new Date());

		// Create Header [start]
		CellStyle cfHeaderValue = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_VALUE);
		CellStyle cfHeaderLabel = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_LABEL);

		// Create Header [end]

		// Create Header Title [Start]
		if (sheet.getSheetName().equals(ReportSheetNameConstant.SHEET_NAME_REPORT_FINE_DETAIL)) {
			reportUtil.writeCellTitle(sheet, 4, "Report Fine Detail", mapCellFormat);
		} else {
			reportUtil.writeCellTitle(sheet, 4, "Report Fine Rekap", mapCellFormat);
		}
		row++;
		row++;
		
		searchCreationDateFrom = getSearchCriteriaValue(ReportFineConstant.WHERE_CREATION_DATE_FROM);
		searchCreationDateTo = getSearchCriteriaValue(ReportFineConstant.WHERE_CREATION_DATE_TO);
		searchSenderCode = getSearchCriteriaValue(ReportFineConstant.WHERE_SENDER_CODE);
		searchTargetDateFrom = getSearchCriteriaValue(ReportFineConstant.WHERE_TARGET_DATE_FROM);
		searchTargetDateTo = getSearchCriteriaValue(ReportFineConstant.WHERE_TARGET_DATE_TO);
		
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
		
		ParameterDetail getSenderCode = parameterDetailService.getParameterDetailByParamDtlCode(searchSenderCode);
		if (getSenderCode != null) {
			senderCodeName = runnableFacesUtil.retrieveLocaleMessage(getSenderCode.getNameEn(),
					getSenderCode.getNameIn());
		}
		
		// Label Creation Date
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportFineCreationDate") + " : ", cfHeaderLabel);

		// Value Creation Date
		reportUtil.writeCell(sheet, row, columnStart + 1, from + " "
				+ ((StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to))
						? runnableFacesUtil.retrieveMessage("textUntil")
						: "")
				+ " " + to, cfHeaderValue);
		row++;
		
		// Label Target Date
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportFineTargetDate") + " : ", cfHeaderLabel);

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
				runnableFacesUtil.retrieveMessage("formReportFineSender") + " : ", cfHeaderLabel);
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

	public ReportFineService getReportFineService() {
		return reportFineService;
	}

	public void setReportFineService(ReportFineService reportFineService) {
		this.reportFineService = reportFineService;
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

	public CommonReportUtil getReportUtil() {
		return reportUtil;
	}

	public void setReportUtil(CommonReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}

	public static String getReportFine() {
		return REPORT_FINE;
	}

	public static String getPieChartTitle() {
		return PIE_CHART_TITLE;
	}

	public static Logger getLogger() {
		return logger;
	}

}
