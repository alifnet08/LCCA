package com.wo.module.report.reportSocializationRekap.task;

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
import org.jfree.chart.plot.PiePlot3D;
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
import com.wo.module.report.reportSocializationRekap.constant.ReportSocializationRekapConstants;
import com.wo.module.report.reportSocializationRekap.model.ReportSocializationRekap;
import com.wo.module.report.reportSocializationRekap.service.ReportSocializationRekapService;

public class ReportSocializationRekapTask implements Runnable, ReportSheetNameConstant{

	static Logger logger = Logger.getLogger(ReportSocializationRekapTask.class);
	
	public final static String COMPLIANCE_DOC_TYPE_REPORT_SOCIALIZATION_REKAP = "Report Socialization Rekap";
	private static final String PIE_CHART_TITLE = "Total Sosialisasi";
	
	private Long reportGenId;
	private String userNikName;
	private String documentTypeName;
	
	private ReportGenService reportGenService;
	private ParameterDetailService parameterDetailService;
	private RunnableFacesUtil runnablefacesUtil;
	private ReportSocializationRekapService reportSocializationRekapService;
	
	@SuppressWarnings("rawtypes")
	private List<? extends SearchObject> searchCriteria;
	
	private String searchCreationDateFrom;
	private String searchCreationDateTo;
	private String provType;
	private String DocType;
	
	private CommonReportUtil reportUtil = new CommonReportUtil();
	
	@SuppressWarnings("rawtypes")
	public ReportSocializationRekapTask(Long reportGenId
			,ReportGenService reportGenService
			,ParameterDetailService parameterDetailService
			,RunnableFacesUtil runnablefacesutil
			,ReportSocializationRekapService reportSocializationRekapService
			,List<? extends SearchObject> searchCriteria
			,String userNikName
			,String documentTypeName) {
		super();
		this.reportGenId = reportGenId;
		this.reportGenService = reportGenService;
		this.parameterDetailService = parameterDetailService;
		this.setRunnablefacesUtil(runnablefacesutil);
		this.reportSocializationRekapService = reportSocializationRekapService;
		this.searchCriteria = searchCriteria;
		this.userNikName = userNikName;
		this.documentTypeName = documentTypeName;
		
	}
	
	@SuppressWarnings("rawtypes" )
	private String getSearchCriteriaValue(String col) {
		if (searchCriteria != null) {
			for (SearchObject searchVal  : searchCriteria) {
				if (!StringUtils.isBlank(col)) {
					if (StringUtils.equals(searchVal.getSearchColumn(), col)) {
						return searchVal.getSearchValueAsString();
					}
				}
			}
		}
		
		return "";
	}
	
	@Override
	public void run() {
		List<Integer> listColumnViewByCategoryDocument = new ArrayList<Integer>();
		List<Integer> listColumnViewByTotalSocialization = new ArrayList<Integer>();
		UploadedFileWO uf = null;
		String absoluteResultFilePath = null;
		
		try {
			absoluteResultFilePath = writeTofile(listColumnViewByCategoryDocument,listColumnViewByTotalSocialization);
			
			uf = uploadFileToApi(absoluteResultFilePath);
			
			updateReportHistoryComplete(reportGenId, uf);
			
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
	
	private UploadedFileWO uploadFileToApi(String absoluteResultFilePath) throws Exception{
		SCMApiUpload saf = new SCMApiUploadImpl(absoluteResultFilePath,
				parameterDetailService,
				COMPLIANCE_DOC_TYPE_REPORT_SOCIALIZATION_REKAP);
		saf.upload();
		return saf.getAsUploadedFileWO();
	}

	private String writeTofile(List<Integer> listColumnViewByCategoryDocument, List<Integer> listColumnVewByTotalSocialization) throws Exception {
		String sheetName1 = SHEET_NAME_REPORT_SOCIALIZATION_REKAP_CATEGORY_DOCUMENT;
		String sheetName2 = SHEET_NAME_REPORT_SOCIALIZATION_REKAP_TOTAL_SOCIALIZATION;
		String sheetName3 = SHEET_NAME_REPORT_SOCIALIZATION_DIAGRAM;
		
		String fileNamePrefix = "ReportSocializationRekap";
		
		String fileName = getReportUtil().generateFileName(fileNamePrefix, userNikName);
		String filePath = getReportUtil().retrieveFilePath(parameterDetailService);
		
		Path dirPath = Paths.get(filePath);
		if(!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}
		
		XSSFWorkbook workbook = null;
		FileOutputStream fileOutputStream = null;
		
		List<String> buildListColumnNameRow1Sheet1 = buildListColumnNameSheet1();
		List<String> buildListColumnNameRow2Sheet1 = buildListColumnRow2NameSheet1();
		
		List<String> buildListColumnNameRow1Sheet2 = buildListColumnNameSheet2();
		List<String> buildListColumnNameRow2Sheet2 = buildListColumnRow2NameSheet2();
		
		List<ReportSocializationRekap> sheet1Results = (List<ReportSocializationRekap>) reportSocializationRekapService.getReportSocializationRekapByKategoriDokumenData(searchCriteria);
		List<ReportSocializationRekap> sheet2Results = (List<ReportSocializationRekap>) reportSocializationRekapService.getReportSocializationRekapByTotalSosialisasiData(searchCriteria);
		
		try {
			fileOutputStream = new FileOutputStream(filePath + fileName);
			workbook = new XSSFWorkbook();
			XSSFSheet sheet1 = workbook.createSheet(sheetName1);
			XSSFSheet sheet2 = workbook.createSheet(sheetName2);
			
			// setting column size sheet 1
			if (listColumnViewByCategoryDocument != null) {
				for (int i = 0; i < listColumnViewByCategoryDocument.size(); i++) {
					sheet1.setColumnWidth(i, listColumnViewByCategoryDocument.get(i));
					sheet1.autoSizeColumn(i);
					
				}
			}
			
			// setting column size sheet 2
			if(listColumnVewByTotalSocialization != null) {
				for (int j = 0; j < listColumnVewByTotalSocialization.size(); j++) {
					sheet2.setColumnWidth(j, listColumnVewByTotalSocialization.get(j));
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
			logger.error(e.getMessage(),e);
			throw e;
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		}
		
		return filePath + fileName;
	}

	private void writeSheet3(String sheetName3, String filePath, XSSFWorkbook workbook,
			List<ReportSocializationRekap> sheet2Results) throws IOException, FileNotFoundException {
		XSSFSheet sheet3 = workbook.createSheet(sheetName3);
		CreationHelper helper = workbook.getCreationHelper();
		
		createPie(filePath, workbook, sheet2Results, sheet3, helper);
		createBar(filePath, workbook, sheet2Results, sheet3, helper);
		
	}

	@SuppressWarnings("rawtypes")
	private void createPie(String filePath, XSSFWorkbook workbook, List<ReportSocializationRekap> sheet2Results, XSSFSheet sheet, CreationHelper helper) throws IOException, FileNotFoundException {
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
	private void createBar(String filePath, XSSFWorkbook workbook, List<ReportSocializationRekap> sheet2Results, XSSFSheet sheet, CreationHelper helper) throws IOException, FileNotFoundException {
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
		
		JFreeChart chart = ChartFactory.createPieChart3D(
				PIE_CHART_TITLE,
				pieDataset,
		        true, 
		        true,
		        false);
		
		PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
				"{1}", NumberFormat.getInstance(), NumberFormat.getPercentInstance()
				);
		
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
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
	
	private PieDataset createDataset(List<ReportSocializationRekap> result) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		for (ReportSocializationRekap reportSocializationRekap : result) {
			dataset.setValue(
					runnablefacesUtil.retrieveLocaleMessage(reportSocializationRekap.getJenisPeraturanEn(),
							reportSocializationRekap.getJenisPeraturanIn()),
					reportSocializationRekap.getTotalSosialisasi());
		}
		return dataset;
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
	
	private DefaultCategoryDataset createBarDataset(List<ReportSocializationRekap> result) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		for (ReportSocializationRekap reportSocializationRekap : result) {
			dataset.addValue(reportSocializationRekap.getTotalSosialisasi(), runnablefacesUtil.retrieveLocaleMessage(reportSocializationRekap.getJenisPeraturanEn(),
					reportSocializationRekap.getJenisPeraturanIn()), "Total sosialisasi");
			dataset.addValue(reportSocializationRekap.getTindakLanjutYes(), runnablefacesUtil.retrieveLocaleMessage(reportSocializationRekap.getJenisPeraturanEn(),
					reportSocializationRekap.getJenisPeraturanIn()), "Perlu tindak lanjut");
			dataset.addValue(reportSocializationRekap.getTindakLanjutNo(), runnablefacesUtil.retrieveLocaleMessage(reportSocializationRekap.getJenisPeraturanEn(),
					reportSocializationRekap.getJenisPeraturanIn()), "Tidak ada tindak lanjut");
			dataset.addValue(reportSocializationRekap.getStatusTindakLanjutInProgress(), runnablefacesUtil.retrieveLocaleMessage(reportSocializationRekap.getJenisPeraturanEn(),
					reportSocializationRekap.getJenisPeraturanIn()), "In Progress");
			dataset.addValue(reportSocializationRekap.getStatusTindakLanjutClosed(), runnablefacesUtil.retrieveLocaleMessage(reportSocializationRekap.getJenisPeraturanEn(),
					reportSocializationRekap.getJenisPeraturanIn()), "Closed");
			dataset.addValue(reportSocializationRekap.getMeetSla(), runnablefacesUtil.retrieveLocaleMessage(reportSocializationRekap.getJenisPeraturanEn(),
					reportSocializationRekap.getJenisPeraturanIn()), "Meet SLA");
			dataset.addValue(reportSocializationRekap.getBeforeSla(), runnablefacesUtil.retrieveLocaleMessage(reportSocializationRekap.getJenisPeraturanEn(),
					reportSocializationRekap.getJenisPeraturanIn()), "Before SLA");
			dataset.addValue(reportSocializationRekap.getOverSla(), runnablefacesUtil.retrieveLocaleMessage(reportSocializationRekap.getJenisPeraturanEn(),
					reportSocializationRekap.getJenisPeraturanIn()), "Over SLA");
		}
		return dataset;
	}

	private void writeSheet1(List<String> buildListColumnNameRow1Sheet1, List<String> buildListColumnNameRow2Sheet1,
			List<ReportSocializationRekap> sheet1Results, XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat,
			CellStyle cfColumnHeader) throws Exception {
		int rowIndexSheet1 = 11;
		if (buildListColumnNameRow1Sheet1 != null && !buildListColumnNameRow1Sheet1.isEmpty()) {
			int columnIndex = 0;
			
			for (String columnNameAlias : buildListColumnNameRow1Sheet1) {
				getReportUtil().writeCell(sheet1, rowIndexSheet1, columnIndex, columnNameAlias, cfColumnHeader);
				
				// merged Area Tindak Lanjut
				if(columnIndex == 4) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet1,rowIndexSheet1,columnIndex,columnIndex+=1);
					sheet1.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet1);
				} else if (columnIndex == 6) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet1,rowIndexSheet1,columnIndex,columnIndex+=1);
					sheet1.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet1);
				} else if (columnIndex == 8) {
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
			
			for (ReportSocializationRekap arrObj : sheet1Results) {
				int columnIndex = 0;
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++, rowNum, mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++
						,runnablefacesUtil.retrieveLocaleMessage(arrObj.getJenisPeraturanEn(), arrObj.getJenisPeraturanIn())
						, mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++
						,runnablefacesUtil.retrieveLocaleMessage(arrObj.getKategoriDokumenEn(), arrObj.getKategoriDokumenIn())
						, mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++
						,arrObj.getTotalSosialisasi(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++
						,arrObj.getTindakLanjutYes(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++
						,arrObj.getTindakLanjutNo(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++
						,arrObj.getStatusTindakLanjutInProgress(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++
						,arrObj.getStatusTindakLanjutClosed(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++
						,arrObj.getMeetSla(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++
						,arrObj.getBeforeSla(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet1, rowIndexSheet1, columnIndex++
						,arrObj.getOverSla(), mapCellFormat);
				
				rowIndexSheet1++;
				rowNum++;
			}
			
			int mergedRow = rowIndexSheet1;
			
			sheet1.addMergedRegion(CellRangeAddress.valueOf("A" + (mergedRow + 1) + ":B"+ (mergedRow + 1) + ""));
		}
	}

	private void writeSheet2(List<String> buildListColumnNameRow1Sheet2, List<String> buildListColumnNameRow2Sheet2,
			List<ReportSocializationRekap> sheet2Results, XSSFSheet sheet2, Map<String, CellStyle> mapCellFormat,
			CellStyle cfColumnHeader) throws Exception {
		int rowIndexSheet2 = 11;
		if (buildListColumnNameRow1Sheet2 != null && !buildListColumnNameRow1Sheet2.isEmpty()) {
			int columnIndex = 0;
						
			for (String columnNameAlias : buildListColumnNameRow1Sheet2) {
				
				getReportUtil().writeCell(sheet2, rowIndexSheet2, columnIndex, columnNameAlias, cfColumnHeader);
				
				// Mergerd Area
				if (columnIndex == 3) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet2, rowIndexSheet2, columnIndex, columnIndex+=1);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else if (columnIndex == 5) {
					CellRangeAddress cra = new CellRangeAddress(rowIndexSheet2, rowIndexSheet2, columnIndex, columnIndex+=1);
					sheet2.addMergedRegion(cra);
					reportUtil.mergedHeaderColumnStyle(cra, sheet2);
				} else if (columnIndex == 7) {
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
			
			for (ReportSocializationRekap arrObj: sheet2Results) {
				int columnIndex = 0;
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++, rowNum, mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++
						, runnablefacesUtil.retrieveLocaleMessage(arrObj.getJenisPeraturanEn(), arrObj.getJenisPeraturanIn())
						, mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++
						, arrObj.getTotalSosialisasi() , mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++
						, arrObj.getTindakLanjutYes() , mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++
						, arrObj.getTindakLanjutNo() , mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++
						, arrObj.getStatusTindakLanjutInProgress() , mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++
						, arrObj.getStatusTindakLanjutClosed(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++
						, arrObj.getMeetSla() , mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++
						, arrObj.getBeforeSla(), mapCellFormat);
				
				getReportUtil().writeCellDetail(sheet2, rowIndexSheet2, columnIndex++
						, arrObj.getOverSla(), mapCellFormat);
				
				rowIndexSheet2++;
				rowNum++;
			}
			
			int mergedRow = rowIndexSheet2;
			
			//mergerd area
			sheet2.addMergedRegion(CellRangeAddress.valueOf("A" + (mergedRow + 1) + ":B" + (mergedRow + 1) + ""));
			
		}
	}
	
	private List<String> buildListColumnNameSheet1(){
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
	
	private List<String> buildListColumnNameSheet2(){
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Kategori Dokumen");
		listColumnNameTemp.add("Total Sosialisasi");
		listColumnNameTemp.add("Tindak Lanjut");
		listColumnNameTemp.add("Status Tindak Lanjut");
		listColumnNameTemp.add("SLA");
		
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnRow2NameSheet2(){
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
	
	private void updateReportGenHistoryAsCompleteError(Long newReportGenId, UploadedFileWO uf, String errorMsg) throws Exception {
		ReportGen editReportGen = reportGenService.findById(newReportGenId);
		editReportGen.setReportGenStatus(CommonConstants.REPORT_GEN_STATUS_COMPLETE_ERR);
		
		if (uf != null) {
			editReportGen.setReportGenFile(uf.getEncodedBase64());
		}
		
		editReportGen.setReportGenStatusMsg(errorMsg);
		reportGenService.update(editReportGen);
	}
	
	private void updateReportHistoryComplete(Long newReportGenId, UploadedFileWO uf) throws Exception {
		ReportGen editrepReportGen = reportGenService.findById(newReportGenId);
		editrepReportGen.setReportGenStatus(CommonConstants.REPORT_GEN_STATUS_COMPLETE_SUCCESS);
		editrepReportGen.setReportGenFileId(uf.getFileId());
		editrepReportGen.setReportGenFileSize(uf.getFileSize());
		editrepReportGen.setReportGenReportFileName(uf.getFileName());
		reportGenService.update(editrepReportGen);
	}
	
	public void writeExcelHeader(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat) throws Exception{
		
		String provTypeName = "";
		
		int row = 0;
		int columnStart = 0;
		
		SimpleDateFormat printTimestamp = new SimpleDateFormat(CommonConstants.INPUT_DATE_TIME_FORMAT);
		String excelPrintDate = printTimestamp.format(new Date());
		
		// create Header [Start]
		CellStyle cfHeaderValue = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_VALUE);
		CellStyle cfHeaderLabel = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_LABEL);
		// create Header [End]
		
		// create header Title 
		reportUtil.writeCellTitle(sheet, 4, "Report Socialization Rekap", mapCellFormat);
		
		row++;
		row++;
		
		// Label Creation Date
		reportUtil.writeCell(sheet, row, columnStart, "Creation Date : ", cfHeaderLabel);
	
		searchCreationDateFrom = getSearchCriteriaValue(ReportSocializationRekapConstants.SEARCH_DATE_FROM);
		searchCreationDateTo = getSearchCriteriaValue(ReportSocializationRekapConstants.SEARCH_DATE_TO);
		provType = getSearchCriteriaValue(ReportSocializationRekapConstants.SEARCH_JENIS_PERATURAN);
		DocType = getSearchCriteriaValue(ReportSocializationRekapConstants.SEARCH_KATEGORI_DOKUMEN);
		
		// get name En dan In
		ParameterDetail getProvTypeName = parameterDetailService.getParameterDetailByParamDtlCode(provType);
		
		if (getProvTypeName != null) {
			provTypeName =  runnablefacesUtil.retrieveLocaleMessage(getProvTypeName.getNameEn(), getProvTypeName.getNameIn());
		}
		
		reportUtil.writeCell(sheet, row, columnStart + 1, 
				searchCreationDateFrom + " "
					+ ((StringUtils.isNotBlank(searchCreationDateFrom) && StringUtils.isNotBlank(searchCreationDateTo))
							? runnablefacesUtil.retrieveMessage("textUntil") : "")
					+ " " + searchCreationDateTo
						, cfHeaderValue);
		
		row++;
		
		// label peraturan
		reportUtil.writeCell(sheet, row, columnStart, 
				runnablefacesUtil.retrieveMessage("formReportSocializationRekapProvType") + " : "
				, cfHeaderLabel);
		
		// value peraturan
		reportUtil.writeCell(sheet, row, columnStart + 1, provTypeName, cfHeaderValue);
		row++;
		
		// label doc type
		reportUtil.writeCell(sheet, row, columnStart, runnablefacesUtil.retrieveMessage("formReportSocializationRekapDocType"), cfHeaderLabel);
		
		// value doc type
		reportUtil.writeCell(sheet, row, columnStart + 1 , documentTypeName, cfHeaderValue);
		row++;
		
		// label printed by
		reportUtil.writeCell(sheet, row, columnStart, runnablefacesUtil.retrieveMessage("textExcelCommonPrintedBy"), cfHeaderLabel);
		
		// value printed by
		reportUtil.writeCell(sheet, row, columnStart + 1, userNikName, cfHeaderLabel);
		row++;
		
		// label printed on
		reportUtil.writeCell(sheet, row, columnStart, runnablefacesUtil.retrieveMessage("textExcelCommonPrintedOn"), cfHeaderLabel);
		
		// value printed on
		reportUtil.writeCell(sheet, row, columnStart + 1, excelPrintDate, cfHeaderLabel);
		
		row++;
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

	public ReportSocializationRekapService getReportSocializationRekapService() {
		return reportSocializationRekapService;
	}

	public void setReportSocializationRekapService(ReportSocializationRekapService reportSocializationRekapService) {
		this.reportSocializationRekapService = reportSocializationRekapService;
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

	public RunnableFacesUtil getRunnablefacesUtil() {
		return runnablefacesUtil;
	}

	public void setRunnablefacesUtil(RunnableFacesUtil runnablefacesUtil) {
		this.runnablefacesUtil = runnablefacesUtil;
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
		return DocType;
	}

	public void setDocType(String docType) {
		DocType = docType;
	}

	public String getDocumentTypeName() {
		return documentTypeName;
	}

	public void setDocumentTypeName(String documentTypeName) {
		this.documentTypeName = documentTypeName;
	}
	
}
