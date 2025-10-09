package com.wo.module.report.reportRmdRekap.task;

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
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.SCMApiUpload;
import com.wo.module.common.utility.SCMApiUploadImpl;
import com.wo.module.lov.bean.RunnableFacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.report.reportGen.constant.ReportSheetNameConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.report.reportRmdRekap.constant.ReportRmdRekapConstants;
import com.wo.module.report.reportRmdRekap.model.ReportRmdRekap;
import com.wo.module.report.reportRmdRekap.service.ReportRmdRekapService;

public class ReportRmdRekapTask implements Runnable, ReportSheetNameConstant, ReportRmdRekapConstants{

	static Logger logger = Logger.getLogger(ReportRmdRekapTask.class);
	
	public final static String COMPLIANCE_DOC_TYPE_REPORT_RMD_REKAP = "Report Rmd Rekap";
	private static final String PIE_CHART_TITLE = "Jumlah Laporan";
	
	private Long reportGenId;
	private String userNikName;
	private String documentTypeName;
	private String documentTypeNameIn;
	private String documentTypeNameEn;
	
	private ReportGenService reportGenService;
	private ParameterDetailService parameterDetailService;
	private RunnableFacesUtil runnableFacesUtil;
	private ReportRmdRekapService reportRmdRekapService;
	
	@SuppressWarnings("rawtypes")
	private List<? extends SearchObject> searchCriteria;
	
	private String searchCreationDateFrom;
	private String searchCreationDateTo;
	private String provType;
	private String docType;
	private String senderType;
	
	private CommonReportUtil reportUtil = new CommonReportUtil();
	
	@SuppressWarnings("rawtypes")
	public ReportRmdRekapTask(Long reportGenId,
			ReportGenService reportGenService,
			ParameterDetailService parameterDetailService,
			RunnableFacesUtil runnableFacesUtil,
			ReportRmdRekapService reportRmdRekapService,
			List<? extends SearchObject> searchCriteria,
			String userNikName,
			String documentTypeName,
			String documentTypeNameIn,
			String documentTypeNameEn) {
		super();
		this.reportGenId = reportGenId;
		this.reportGenService = reportGenService;
		this.parameterDetailService = parameterDetailService;
		this.setRunnableFacesUtil(runnableFacesUtil);
		this.reportRmdRekapService = reportRmdRekapService;
		this.searchCriteria = searchCriteria;
		this.userNikName = userNikName;
		this.documentTypeName = documentTypeName;
		this.documentTypeNameEn = documentTypeNameEn;
		this.documentTypeNameIn = documentTypeNameIn;
	}
	
	@SuppressWarnings("rawtypes")
	private String getSearchCriteriaValue(String col) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				if (!StringUtils.isBlank(col)) {
					if(StringUtils.equals(searchVal.getSearchColumn(), col)) {
						return searchVal.getSearchValueAsString();
					}
				}
			}
		}
		
		return "";
	}
	
	@Override
	public void run() {
		List<Integer> listColumnViewByData = new ArrayList<Integer>();
		List<Integer> listColumnViewBySameValue = new ArrayList<Integer>();
		UploadedFileWO uf = null;
		String absoluteResultFilePath = null;
		
		try {
			
			absoluteResultFilePath = writeToFile(listColumnViewByData, listColumnViewBySameValue);
			
			uf = uploadFileToApi(absoluteResultFilePath);
			
			updateReportGenHistoryAsComplete(reportGenId, uf);
			
		} catch (CustomAPIException e) {
			e.printStackTrace();
			logger.error("Error while trying to process report", e);
			
			try {
				updateReportGenHistoryAsCompleteError(reportGenId, e.getUfw(), e.getMessage());
			} catch (Exception e2) {
				logger.error("Error ", e);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while trying to process report", e);
			
			try {
				updateReportGenHistoryAsCompleteError(reportGenId, uf, e.getMessage());
			} catch (Exception e2) {
				logger.error("Error ", e);
			}
		} finally {
			if (uf != null && uf.getFile() != null) {
				try {
					uf.getFile().delete();
				} catch (Exception e2) {
					logger.error("Error while deleting temp ", e2);
				}
			}
		}
		
	}

	private UploadedFileWO uploadFileToApi(String absoluteResultFilePath) throws Exception {
		SCMApiUpload saf = new SCMApiUploadImpl(absoluteResultFilePath, 
				parameterDetailService, 
				COMPLIANCE_DOC_TYPE_REPORT_RMD_REKAP
			);	
		saf.upload();
		return saf.getAsUploadedFileWO();
	}
	
	private String writeToFile(List<Integer> listColumnViewData, List<Integer> listColumnViewSameValue) throws Exception {
		String senderCodeNameEn = "";
		String senderCodeNameIn = "";
		String senderCode = "";
		
		String sheetName1 = SHEET_NAME_REPORT_MATRIX_DIARY_REKAP;
		String sheetName2 = SHEET_NAME_REPORT_MATRIX_DIARY_REKAP_MERGED_VALUE;
		String sheetName3 = SHEET_NAME_REPORT_MATRIX_DIARY_DIAGRAM;
		
		String fileNamePrefix = "ReportMatrixDiaryRekap";
		
		String fileName = getReportUtil().generateFileName(fileNamePrefix, userNikName);
		String filePath = getReportUtil().retrieveFilePath(parameterDetailService);
		
		Path dirPath = Paths.get(filePath);
		if(!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}
		
		XSSFWorkbook workbook = null;
		FileOutputStream fileOutputStream = null;
		
		List<String> buildListColumnNameRow1Sheet1 = buildListColumnNameRow1Sheet1();
		List<String> buildListColumnNameRow2Sheet1 = buildListColumnNameRow2Sheet1();
		
		List<String> buildListColumnNameRow1Sheet2 = buildListColumnNameRow1Sheet2();
		List<String> buildListColumnNameRow2Sheet2 = buildListColumnNameRow2Sheet2();
		
		senderCode = getSearchCriteriaValue(WHERE_SENDER);
		
		ParameterDetail getSenderCodeName =  parameterDetailService.getParameterDetailByParamDtlCode(senderCode);
		
		if (getSenderCodeName != null) {
			senderCodeNameEn = getSenderCodeName.getNameEn();
			senderCodeNameIn = getSenderCodeName.getNameIn();
		}
		
		List<ReportRmdRekap> sheet1Results = (List<ReportRmdRekap>) reportRmdRekapService.getReportRmdRekapByData(searchCriteria);
		List<ReportRmdRekap> sheet2Results = (List<ReportRmdRekap>) reportRmdRekapService.getReportRmdRekapByJenisOrPeraturanSame(searchCriteria,
				documentTypeNameEn,documentTypeNameIn,senderCodeNameEn,senderCodeNameIn);
		
		try {
			
			fileOutputStream = new FileOutputStream(filePath + fileName);
			workbook = new XSSFWorkbook();
			XSSFSheet sheet1 = workbook.createSheet(sheetName1);
			XSSFSheet sheet2 = workbook.createSheet(sheetName2);
			
			// setting column size sheet 1
			if (listColumnViewData != null) {
				for (int i = 0; i < listColumnViewData.size(); i++) {
					sheet1.setColumnWidth(i, listColumnViewData.get(i));
						sheet1.autoSizeColumn(i);
				}
			}
			
			// setting column size sheet 2
			if(listColumnViewSameValue != null) {
				for (int j = 0; j < listColumnViewSameValue.size(); j++) {
					sheet2.setColumnWidth(j, listColumnViewSameValue.get(j));
						sheet2.autoSizeColumn(j);
				}
			}
			
			Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
			getReportUtil().createExcelCellFormat(workbook, mapCellFormat);
			
			this.writeExcelHeader(sheet1, mapCellFormat);
			this.writeExcelHeader(sheet2, mapCellFormat);
			
			CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER);			
			
			// sheet 1 header [start]
			writeSheet1(buildListColumnNameRow1Sheet1, buildListColumnNameRow2Sheet1, sheet1Results, sheet1,
					mapCellFormat, cfColumnHeader);
			// sheet 1 data [end]
			
			// sheet 2 header [start]
			writeSheet2(buildListColumnNameRow1Sheet2, buildListColumnNameRow2Sheet2, sheet2Results, sheet2,
					mapCellFormat, cfColumnHeader);
			// sheet 2 data [end]
			
			// sheet 3 header [start]
			writeSheet3(sheetName3, filePath, workbook, sheet2Results);
			// sheet 3 header [end]
						
			workbook.write(fileOutputStream);
			
		} catch (Exception e) {
			logger.error(null,e);
			throw e;
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		}
		
		return filePath + fileName;
	}
	
	private void writeSheet3(String sheetName3, String filePath, XSSFWorkbook workbook,
			List<ReportRmdRekap> sheet2Results) throws IOException, FileNotFoundException {
		XSSFSheet sheet3 = workbook.createSheet(sheetName3);
		CreationHelper helper = workbook.getCreationHelper();
		
		createPie(filePath, workbook, sheet2Results, sheet3, helper);
		createBar(filePath, workbook, sheet2Results, sheet3, helper);
		
	}
	
	@SuppressWarnings("rawtypes")
	private void createBar(String filePath, XSSFWorkbook workbook, List<ReportRmdRekap> sheet2Results, XSSFSheet sheet, CreationHelper helper) throws IOException, FileNotFoundException {
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
		
		JFreeChart chart = ChartFactory.createBarChart3D("", "", "", dataset);
		
		CategoryPlot plot = (CategoryPlot) chart.getCategoryPlot();
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		
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
	
	private DefaultCategoryDataset createBarDataset(List<ReportRmdRekap> result) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		for (ReportRmdRekap reportRmdRekap : result) {
			dataset.addValue(reportRmdRekap.getJumlahLaporan(), runnableFacesUtil.retrieveLocaleMessage(reportRmdRekap.getJenisPeraturanEn(),
					reportRmdRekap.getJenisPeraturanIn()), "Jumlah Laporan");
			dataset.addValue(reportRmdRekap.getInProgress(), runnableFacesUtil.retrieveLocaleMessage(reportRmdRekap.getJenisPeraturanEn(),
					reportRmdRekap.getJenisPeraturanIn()), "In Progress");
			dataset.addValue(reportRmdRekap.getClosed(), runnableFacesUtil.retrieveLocaleMessage(reportRmdRekap.getJenisPeraturanEn(),
					reportRmdRekap.getJenisPeraturanIn()), "Closed");
			dataset.addValue(reportRmdRekap.getMeetSla(), runnableFacesUtil.retrieveLocaleMessage(reportRmdRekap.getJenisPeraturanEn(),
					reportRmdRekap.getJenisPeraturanIn()), "Meet SLA");
			dataset.addValue(reportRmdRekap.getBeforeSla(), runnableFacesUtil.retrieveLocaleMessage(reportRmdRekap.getJenisPeraturanEn(),
					reportRmdRekap.getJenisPeraturanIn()), "Before SLA");
			dataset.addValue(reportRmdRekap.getOverSla(), runnableFacesUtil.retrieveLocaleMessage(reportRmdRekap.getJenisPeraturanEn(),
					reportRmdRekap.getJenisPeraturanIn()), "Over SLA");
		}
		return dataset;
	}
	
	@SuppressWarnings("rawtypes")
	private void createPie(String filePath, XSSFWorkbook workbook, List<ReportRmdRekap> sheet2Results, XSSFSheet sheet, CreationHelper helper) throws IOException, FileNotFoundException {
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
	
	private PieDataset createDataset(List<ReportRmdRekap> result) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		for (ReportRmdRekap reportRmdRekap : result) {
			
			if(StringUtils.isBlank(reportRmdRekap.getJenisPeraturanEn()) ||
					StringUtils.isBlank(reportRmdRekap.getJenisPeraturanIn())){
				dataset.setValue("Undefined", reportRmdRekap.getJumlahLaporan());
			}
			else {		
				dataset.setValue(
						runnableFacesUtil.retrieveLocaleMessage(reportRmdRekap.getJenisPeraturanEn(),
								reportRmdRekap.getJenisPeraturanIn()),
						reportRmdRekap.getJumlahLaporan());
			}
		}
		return dataset;
	}
	
	
	
	private File printPie(PieDataset pieDataset, String filePath) throws IOException {
		
		JFreeChart chart = ChartFactory.createPieChart3D(
				PIE_CHART_TITLE,
				pieDataset,
		        true, 
		        true,
		        false);
		
		PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
				"{1}", NumberFormat.getInstance(), NumberFormat.getPercentInstance()
				);
		
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setForegroundAlpha( 0.5f );
		plot.setStartAngle(360);
		plot.setInteriorGap( 0.02 );
		plot.setSimpleLabels(true);
		plot.setLabelGenerator(labelGenerator);
		
		int width = 480;
		int height = 360;
		File pieChartImage = new File(
				filePath + "pie-chart"+ CommonConstants.SEPARATOR_DASH + System.currentTimeMillis() + CommonConstants.FILE_TYPE_JPEG);
		
		ChartUtilities.saveChartAsJPEG(pieChartImage, chart, width, height);
		
		return pieChartImage;
	}

	private void writeSheet2(List<String> buildListColumnNameRow1Sheet2, List<String> buildListColumnNameRow2Sheet2,
			List<ReportRmdRekap> sheet2Results, XSSFSheet sheet2, Map<String, CellStyle> mapCellFormat,
			CellStyle cfColumnHeader) throws Exception {
		int rowIndexSheet2 = 11;
		if (buildListColumnNameRow1Sheet2 != null && !buildListColumnNameRow1Sheet2.isEmpty()) {
			int columnIndex = 0;
			
			for (String columnNameAlias : buildListColumnNameRow1Sheet2) {
				
				getReportUtil().writeCell(sheet2, rowIndexSheet2, columnIndex, columnNameAlias, cfColumnHeader);
				
				// Merged Area
				if (columnIndex == 2) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet2, rowIndexSheet2, columnIndex, columnIndex+=6);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else if (columnIndex == 10) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet2, rowIndexSheet2, columnIndex, columnIndex+=1);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else if (columnIndex == 12) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet2, rowIndexSheet2, columnIndex, columnIndex+=2);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else {
					sheet2.addMergedRegion(new CellRangeAddress(rowIndexSheet2, rowIndexSheet2+1, columnIndex, columnIndex));	
				}
				columnIndex++;
				
			}
		}
		rowIndexSheet2++;
		if (buildListColumnNameRow2Sheet2 != null && !buildListColumnNameRow2Sheet2.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow2Sheet2) {
				getReportUtil().writeCell(sheet2, rowIndexSheet2, columnIndex, columnNameAlias, cfColumnHeader);
				
				columnIndex++;
			}
		}
		// sheet 2 header [end]
		
		// sheet 2 data [start]
		if (sheet2Results != null && !sheet2Results.isEmpty()) {
			rowIndexSheet2 += 1;
			int rowNum = 1;
			
			for (ReportRmdRekap arrObj : sheet2Results) {
				int columnIndex = 0;
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++, rowNum, mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++, 
						runnableFacesUtil.retrieveLocaleMessage(arrObj.getJenisPeraturanEn(), arrObj.getJenisPeraturanIn()), 
						mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++, 
						arrObj.getBulanan(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++, 
						arrObj.getTahunan(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++,
						arrObj.getTriwulanan(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++, 
						arrObj.getSemester(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++, 
						arrObj.getInsidentil(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++, 
						arrObj.getMingguan(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++, 
						arrObj.getHarian(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++, 
						arrObj.getJumlahLaporan(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++, 
						arrObj.getInProgress(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++, 
						arrObj.getClosed(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++, 
						arrObj.getMeetSla(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++, 
						arrObj.getBeforeSla(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++, 
						arrObj.getOverSla(), mapCellFormat);
				
				rowIndexSheet2++;
				rowNum++;
			}
		}
	}

	private void writeSheet1(List<String> buildListColumnNameRow1Sheet1, List<String> buildListColumnNameRow2Sheet1,
			List<ReportRmdRekap> sheet1Results, XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat,
			CellStyle cfColumnHeader) throws Exception {
		int rowIndexSheet1 = 11;
		if (buildListColumnNameRow1Sheet1 != null && !buildListColumnNameRow1Sheet1.isEmpty()) {
			int columnIndex = 0;
			
			for (String columnNameAlias : buildListColumnNameRow1Sheet1) {
				getReportUtil().writeCell(sheet1, rowIndexSheet1, columnIndex, columnNameAlias, cfColumnHeader);
				
				// merged Area
				if (columnIndex == 3) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet1,rowIndexSheet1,columnIndex,columnIndex+=6);
					sheet1.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet1);
				} else if (columnIndex == 11) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet1,rowIndexSheet1,columnIndex,columnIndex+=1);
					sheet1.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet1);
				} else if (columnIndex == 13) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet1,rowIndexSheet1,columnIndex,columnIndex+=2);
					sheet1.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet1);
				} else {
					sheet1.addMergedRegion(new CellRangeAddress(rowIndexSheet1,rowIndexSheet1+1,columnIndex,columnIndex));
				}
				columnIndex++;
				
			}
		}
		rowIndexSheet1++;
		if (buildListColumnNameRow2Sheet1 != null && !buildListColumnNameRow2Sheet1.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow2Sheet1) {
				getReportUtil().writeCell(sheet1, rowIndexSheet1, columnIndex, columnNameAlias, cfColumnHeader);
				
				columnIndex++;
			}
		}
		
		// sheet 1 header [end]
		
		//sheet 1 data [start]
		if (sheet1Results != null && !sheet1Results.isEmpty()) {
			rowIndexSheet1 += 1;
			int rowNum = 1;
			
			for (ReportRmdRekap arrObj : sheet1Results) {
				int columnIndex = 0;
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++, rowNum, mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++, 
						runnableFacesUtil.retrieveLocaleMessage(arrObj.getJenisPeraturanEn(), arrObj.getJenisPeraturanIn()), 
						mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++, 
						runnableFacesUtil.retrieveLocaleMessage(arrObj.getPengirimSuratEn(), arrObj.getPengirimSuratIn()),
						mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++, 
						arrObj.getBulanan(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++, 
						arrObj.getTahunan(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++,
						arrObj.getTriwulanan(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++, 
						arrObj.getSemester(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++, 
						arrObj.getInsidentil(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++, 
						arrObj.getMingguan(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++, 
						arrObj.getHarian(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++, 
						arrObj.getJumlahLaporan(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++, 
						arrObj.getInProgress(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++, 
						arrObj.getClosed(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++, 
						arrObj.getMeetSla(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++, 
						arrObj.getBeforeSla(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++, 
						arrObj.getOverSla(), mapCellFormat);
				
				rowIndexSheet1++;
				rowNum++;
			}
		}
	}
	
	private List<String> buildListColumnNameRow1Sheet1() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Jenis Peraturan");
		listColumnNameTemp.add("Pengirim Surat");
		listColumnNameTemp.add("Tipe Laporan");
		listColumnNameTemp.add("Jumlah Laporan");
		listColumnNameTemp.add("Status Tindak Lanjut (Yes)");
		listColumnNameTemp.add("SLA");
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow2Sheet1() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Bulanan");
		listColumnNameTemp.add("Tahunan");
		listColumnNameTemp.add("Tri Wulanan");
		listColumnNameTemp.add("Semester");
		listColumnNameTemp.add("Insidentil");
		listColumnNameTemp.add("Mingguan");
		listColumnNameTemp.add("Harian");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("In Progress");
		listColumnNameTemp.add("Closed");
		listColumnNameTemp.add("Meet SLA");
		listColumnNameTemp.add("Before SLA");
		listColumnNameTemp.add("Over SLA");
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow1Sheet2(){
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Jenis Peraturan / Pengirim Surat");
		listColumnNameTemp.add("Tipe Laporan");
		listColumnNameTemp.add("Jumlah Laporan");
		listColumnNameTemp.add("Status Tindak Lanjut (Yes)");
		listColumnNameTemp.add("SLA");
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow2Sheet2(){
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("Bulanan");
		listColumnNameTemp.add("Tahunan");
		listColumnNameTemp.add("Tri Wulanan");
		listColumnNameTemp.add("Semester");
		listColumnNameTemp.add("Insidentil");
		listColumnNameTemp.add("Mingguan");
		listColumnNameTemp.add("Harian");
		listColumnNameTemp.add("");
		listColumnNameTemp.add("In Progress");
		listColumnNameTemp.add("Closed");
		listColumnNameTemp.add("Meet SLA");
		listColumnNameTemp.add("Before SLA");
		listColumnNameTemp.add("Over SLA");
		return listColumnNameTemp;
	}
	
	public void writeExcelHeader(XSSFSheet sheet,Map<String, CellStyle> mapCellFormat) throws Exception {
		
		String provTypeName = "";
		String senderCodeName = "";
		
		int row = 0;
		int columnStart = 0;
		
		SimpleDateFormat printTimeStamp = new SimpleDateFormat(CommonConstants.INPUT_DATE_TIME_FORMAT);
		String excelPrintDate = printTimeStamp.format(new Date());
		
		// Create Header [Start]
		CellStyle cfHeaderValue = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_VALUE);
		CellStyle cfHeaderLabel = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_LABEL);
		// Create Header [End]
		
		// Create Header Title [Start]
		reportUtil.writeCellTitle(sheet, 4, "Report Matrix Diary Rekap", mapCellFormat);
		
		row++;
		row++;
		
		// Label Creation Date [Start]
		reportUtil.writeCell(sheet, row, columnStart, "Creation Date : ", cfHeaderLabel);
		// Label Creation Date [End]
		
		searchCreationDateFrom = getSearchCriteriaValue(WHERE_CREATION_DATE_FROM);
		searchCreationDateTo = getSearchCriteriaValue(WHERE_CREATION_DATE_TO);
		provType = getSearchCriteriaValue(WHERE_PROVISION_TYPE);
		docType = getSearchCriteriaValue(WHERE_DOCUMENT_TYPE);
		senderType = getSearchCriteriaValue(WHERE_SENDER);
		
		// get Name EN and In
		ParameterDetail getProvTypeName = parameterDetailService.getParameterDetailByParamDtlCode(provType);
		ParameterDetail getSenderCodeName =  parameterDetailService.getParameterDetailByParamDtlCode(senderType);
		
		// inital
		if (getProvTypeName != null) {
			provTypeName = runnableFacesUtil.retrieveLocaleMessage(getProvTypeName.getNameEn(), getProvTypeName.getNameIn());
		}
		if (getSenderCodeName != null) {
			senderCodeName = runnableFacesUtil.retrieveLocaleMessage(getSenderCodeName.getNameEn(), getSenderCodeName.getNameIn());
		}
		
		reportUtil.writeCell(sheet, row, columnStart + 1, 
				searchCreationDateFrom + " "
				+ ((StringUtils.isNotBlank(searchCreationDateFrom) && StringUtils.isNotBlank(searchCreationDateTo))
						? runnableFacesUtil.retrieveMessage("textUntil") : "")
				+ " " + searchCreationDateTo, 
				cfHeaderValue);
		row++;
		
		// label peraturan
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportRmdRekapProvType") + " : ", 
				cfHeaderLabel);
		
		// value peraturan
		reportUtil.writeCell(sheet, row, columnStart + 1,
				provTypeName, 
				cfHeaderValue);
		
		row++;
		
		// label document
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportRmdRekapDocType") + " : ", 
				cfHeaderLabel);
		
		// value document
		reportUtil.writeCell(sheet, row, columnStart + 1,
				documentTypeName, 
				cfHeaderValue);
		
		row++;
		
		// label sender
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportRmdRekapSender") + " : ", 
				cfHeaderLabel);
		
		// value sender
		reportUtil.writeCell(sheet, row, columnStart + 1,
				senderCodeName, 
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
		// Create Header Title [End]
	}
	
	private void updateReportGenHistoryAsCompleteError(Long newReportGenId, UploadedFileWO ufw,String errorMsg) throws Exception {
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

	public String getUserId() {
		return userNikName;
	}

	public void setUserId(String userNikName) {
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

	public ReportRmdRekapService getReportRmdRekapService() {
		return reportRmdRekapService;
	}

	public void setReportRmdRekapService(ReportRmdRekapService reportRmdRekapService) {
		this.reportRmdRekapService = reportRmdRekapService;
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

	public String getProvType() {
		return provType;
	}

	public void setProvType(String provType) {
		this.provType = provType;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getSenderType() {
		return senderType;
	}

	public void setSenderType(String senderType) {
		this.senderType = senderType;
	}

	public CommonReportUtil getReportUtil() {
		return reportUtil;
	}

	public void setReportUtil(CommonReportUtil reportUtil) {
		this.reportUtil = reportUtil;
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

	public String getDocumentTypeNameIn() {
		return documentTypeNameIn;
	}

	public void setDocumentTypeNameIn(String documentTypeNameIn) {
		this.documentTypeNameIn = documentTypeNameIn;
	}

	public String getDocumentTypeNameEn() {
		return documentTypeNameEn;
	}

	public void setDocumentTypeNameEn(String documentTypeNameEn) {
		this.documentTypeNameEn = documentTypeNameEn;
	}
}
