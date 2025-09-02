package com.wo.module.report.reportSuratMasukAmlRekap.task;

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
import com.wo.module.report.reportSuratMasukAmlRekap.constant.ReportSuratMasukAmlRekapConstants;
import com.wo.module.report.reportSuratMasukAmlRekap.model.ReportSuratMasukAmlRekap;
import com.wo.module.report.reportSuratMasukAmlRekap.service.ReportSuratMasukAmlRekapService;

public class ReportSuratMasukAmlRekapTask implements Runnable, ReportSheetNameConstant, ReportSuratMasukAmlRekapConstants{

static Logger logger = Logger.getLogger(ReportSuratMasukAmlRekapTask.class);
	
	public final static String COMPLIANCE_DOC_TYPE_REPORT_SURAT_MASUK_AML_REKAP = "Report Surat Masuk AML Rekap";
	private static final String PIE_CHART_TITLE = "Total Surat Masuk AML";
	
	private Long reportGenId;
	private String userNik;
	
	private ReportGenService reportGenService;
	private ParameterDetailService parameterDetailService;
	private RunnableFacesUtil runnableFacesUtil;
	private ReportSuratMasukAmlRekapService reportSuratMasukAmlRekapService;
	
	@SuppressWarnings("rawtypes")
	private List<? extends SearchObject> searchCriteria;
	
	private String searchCreationDateFrom;
	private String searchCreationDateTo;
	private String searchSenderCode;
	
	private String senderName;
	
	private CommonReportUtil reportUtil = new CommonReportUtil();
	
	@SuppressWarnings("rawtypes")
	public ReportSuratMasukAmlRekapTask(Long reportGendId,
			ReportGenService reportGenService,
			ParameterDetailService parameterDetailService,
			RunnableFacesUtil runnableFacesUtil,
			ReportSuratMasukAmlRekapService reportSuratMasukAmlRekapService,
			List<? extends SearchObject> searchCriteria,
			String userNik) {
		super();
		this.reportGenId = reportGendId;
		this.reportGenService = reportGenService;
		this.parameterDetailService = parameterDetailService;
		this.setRunnableFacesUtil(runnableFacesUtil);
		this.reportSuratMasukAmlRekapService = reportSuratMasukAmlRekapService;
		this.searchCriteria = searchCriteria;
		this.userNik = userNik;
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
		List<Integer> listColumnViewBySheet1 = new ArrayList<Integer>();
		List<Integer> listColumnViewBySheet2 = new ArrayList<Integer>();
		UploadedFileWO ufw = null;
		String absoluteResultFilePath = null; 
		
		try {
			
			absoluteResultFilePath = writeToFile(listColumnViewBySheet1, listColumnViewBySheet2);
			
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
				COMPLIANCE_DOC_TYPE_REPORT_SURAT_MASUK_AML_REKAP
			);	
		saf.upload();
		return saf.getAsUploadedFileWO();
	}
	
	private String writeToFile(List<Integer> listColumnViewBySheet1,List<Integer> listColumnViewBySheet2) throws Exception {
		
		String sheetName1 = SHEET_NAME_REPORT_SURAT_MASUK_AML_REKAP_TIPE_SURAT;
		String sheetName2 = SHEET_NAME_REPORT_SURAT_MASUK_AML_REKAP_NON_TIPE_SURAT;
		String sheetName3 = SHEET_NAME_REPORT_SURAT_MASUK_AML_DIAGRAM;
		
		String fileNamePrefix = "ReportSuratMasukAmlRekap";
		
		String fileName = getReportUtil().generateFileName(fileNamePrefix, userNik);
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
		
		List<ReportSuratMasukAmlRekap> sheet1Results = (List<ReportSuratMasukAmlRekap>) reportSuratMasukAmlRekapService.getReportSuratMasukAmlRekapByTipeSurat(searchCriteria);
		List<ReportSuratMasukAmlRekap> sheet2Results = (List<ReportSuratMasukAmlRekap>) reportSuratMasukAmlRekapService.getReportSuratMasukAmlRekapByData(searchCriteria);
		
		try {
			
			fileOutputStream = new FileOutputStream(filePath + fileName);
			workbook = new XSSFWorkbook();
			XSSFSheet sheet1 = workbook.createSheet(sheetName1);
			XSSFSheet sheet2 = workbook.createSheet(sheetName2);
			
			// Setting column size sheet 1
			if (listColumnViewBySheet1 != null) {
				for (int i = 0; i < listColumnViewBySheet1.size(); i++) {
					sheet1.setColumnWidth(i, listColumnViewBySheet1.get(i));
					sheet1.autoSizeColumn(i);
				}
			}
			
			// Setting column size sheet 2
			if (listColumnViewBySheet2 != null) {
				for (int i = 0; i < listColumnViewBySheet2.size(); i++) {
					sheet1.setColumnWidth(i, listColumnViewBySheet2.get(i));
					sheet1.autoSizeColumn(i);
				}
			}
			
			Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
			getReportUtil().createExcelCellFormat(workbook, mapCellFormat);
			
			this.writeExcelHeader(sheet1, mapCellFormat);
			this.writeExcelHeader(sheet2, mapCellFormat);
			
			// write sheet 1 column header [start]
			
			// first row
			CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER);
			int rowIndexSheet1 = 11;
			if (buildListColumnNameRow1Sheet1 != null && !buildListColumnNameRow1Sheet1.isEmpty()) {
				
				int columnIndex = 0;
				for (String columnNameAlias : buildListColumnNameRow1Sheet1) {
					
					getReportUtil().writeCell(sheet1, rowIndexSheet1, columnIndex, columnNameAlias, cfColumnHeader);
					
					// merged Area
					if (columnIndex == 3) {
						CellRangeAddress cra = new CellRangeAddress(rowIndexSheet1,rowIndexSheet1,columnIndex,columnIndex+=1);
						sheet1.addMergedRegion(cra);
						reportUtil.mergedHeaderColumnStyle(cra, sheet1);
					} else if (columnIndex == 5) {
						CellRangeAddress cra = new CellRangeAddress(rowIndexSheet1,rowIndexSheet1,columnIndex,columnIndex+=1);
						sheet1.addMergedRegion(cra);
						reportUtil.mergedHeaderColumnStyle(cra, sheet1);
					} else if (columnIndex == 7) {
						CellRangeAddress cra = new CellRangeAddress(rowIndexSheet1,rowIndexSheet1,columnIndex,columnIndex+=1);
						sheet1.addMergedRegion(cra);
						reportUtil.mergedHeaderColumnStyle(cra, sheet1);
					} else if (columnIndex == 9) {
						CellRangeAddress cra = new CellRangeAddress(rowIndexSheet1,rowIndexSheet1,columnIndex,columnIndex+=2);
						sheet1.addMergedRegion(cra);
						reportUtil.mergedHeaderColumnStyle(cra, sheet1);
					} else {
						sheet1.addMergedRegion(new CellRangeAddress(rowIndexSheet1,rowIndexSheet1+1,columnIndex,columnIndex));
					}
					
					
					columnIndex++;
				}
			}
			
			// second row
			rowIndexSheet1++;
			if (buildListColumnNameRow2Sheet1 != null && !buildListColumnNameRow2Sheet1.isEmpty()) {
				int columnIndex = 0;
				for (String columnNameAlias : buildListColumnNameRow2Sheet1) {
					getReportUtil().writeCell(sheet1, rowIndexSheet1, columnIndex, columnNameAlias, cfColumnHeader);
					
					columnIndex++;
				}
			}
			
			// write sheet 1 column header [end]
			
			// sheet 1 data [start]
			if (sheet1Results != null && !sheet1Results.isEmpty()) {
				rowIndexSheet1 += 1;
				int rowNum = 1;
				
				for (ReportSuratMasukAmlRekap arrObj : sheet1Results) {
					int columnIndex = 0;
					
					writeObjectToExcelSheet1(sheet1, mapCellFormat, rowIndexSheet1, rowNum, arrObj, columnIndex);
					
					rowIndexSheet1++;
					rowNum++;
				}
				
			}
			
			// sheet 1 data [end]
			
			// sheet 2 header [start]
			
			// first row
			int rowIndexSheet2 = 11;
			if (buildListColumnNameRow1Sheet2 != null && !buildListColumnNameRow1Sheet2.isEmpty()) {
				int columnIndex = 0;
				
				for (String columnNameAlias : buildListColumnNameRow1Sheet2) {
					
					getReportUtil().writeCell(sheet2, rowIndexSheet2, columnIndex, columnNameAlias, cfColumnHeader);
					
					// merged Area
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
			
			// second row
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
				
				for (ReportSuratMasukAmlRekap arrObj : sheet2Results) {
					
					int columnIndex = 0;
					
					writeObjectToExcelSheet2(sheet2, mapCellFormat, rowIndexSheet2, rowNum, arrObj, columnIndex);
					
					rowIndexSheet2++;
					rowNum++;
				}
			}
			// sheet 2 data [end]
			
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
	
	private void writeSheet3(String sheetName3, String filePath, XSSFWorkbook workbook,
			List<ReportSuratMasukAmlRekap> sheet2Results) throws IOException, FileNotFoundException {
		XSSFSheet sheet3 = workbook.createSheet(sheetName3);
		CreationHelper helper = workbook.getCreationHelper();
		
		createPie(filePath, workbook, sheet2Results, sheet3, helper);
		createBar(filePath, workbook, sheet2Results, sheet3, helper);
		
	}
	
	@SuppressWarnings("rawtypes")
	private void createBar(String filePath, XSSFWorkbook workbook, List<ReportSuratMasukAmlRekap> sheet2Results, XSSFSheet sheet, CreationHelper helper) throws IOException, FileNotFoundException {
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
	
	private DefaultCategoryDataset createBarDataset(List<ReportSuratMasukAmlRekap> result) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		for (ReportSuratMasukAmlRekap reportSuratMasukAmlRekap : result) {
			dataset.addValue(reportSuratMasukAmlRekap.getTotalSuratMasuk(), runnableFacesUtil.retrieveLocaleMessage(reportSuratMasukAmlRekap.getPengirimSuratEn(),
					reportSuratMasukAmlRekap.getPengirimSuratIn()), "Total Surat Masuk");
			dataset.addValue(reportSuratMasukAmlRekap.getTindakLanjutYes(), runnableFacesUtil.retrieveLocaleMessage(reportSuratMasukAmlRekap.getPengirimSuratEn(),
					reportSuratMasukAmlRekap.getPengirimSuratIn()), "Perlu Tindak Lanjut");
			dataset.addValue(reportSuratMasukAmlRekap.getTindakLanjutNo(), runnableFacesUtil.retrieveLocaleMessage(reportSuratMasukAmlRekap.getPengirimSuratEn(),
					reportSuratMasukAmlRekap.getPengirimSuratIn()), "Tidak Ada Tindak Lanjut");
			dataset.addValue(reportSuratMasukAmlRekap.getInProgress(), runnableFacesUtil.retrieveLocaleMessage(reportSuratMasukAmlRekap.getPengirimSuratEn(),
					reportSuratMasukAmlRekap.getPengirimSuratIn()), "In Progress");
			dataset.addValue(reportSuratMasukAmlRekap.getClosed(), runnableFacesUtil.retrieveLocaleMessage(reportSuratMasukAmlRekap.getPengirimSuratEn(),
					reportSuratMasukAmlRekap.getPengirimSuratIn()), "Closed");
			dataset.addValue(reportSuratMasukAmlRekap.getMeetSla(), runnableFacesUtil.retrieveLocaleMessage(reportSuratMasukAmlRekap.getPengirimSuratEn(),
					reportSuratMasukAmlRekap.getPengirimSuratIn()), "Meet SLA");
			dataset.addValue(reportSuratMasukAmlRekap.getBeforeSla(), runnableFacesUtil.retrieveLocaleMessage(reportSuratMasukAmlRekap.getPengirimSuratEn(),
					reportSuratMasukAmlRekap.getPengirimSuratIn()), "Before SLA");
			dataset.addValue(reportSuratMasukAmlRekap.getOverSla(), runnableFacesUtil.retrieveLocaleMessage(reportSuratMasukAmlRekap.getPengirimSuratEn(),
					reportSuratMasukAmlRekap.getPengirimSuratIn()), "Over SLA");
		}
		return dataset;
	}
	
	@SuppressWarnings("rawtypes")
	private void createPie(String filePath, XSSFWorkbook workbook, List<ReportSuratMasukAmlRekap> sheet2Results, XSSFSheet sheet, CreationHelper helper) throws IOException, FileNotFoundException {
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
	
	private void writeObjectToExcelSheet1(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum,
			ReportSuratMasukAmlRekap arrObj, int columnIndex) throws Exception{
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, rowNum, mapCellFormat);

		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getPengirimSuratEn(), arrObj.getPengirimSuratIn()), 
				mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				arrObj.getTotalSuratMasuk(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				arrObj.getUndangan(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				arrObj.getNonUndangan(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				arrObj.getTindakLanjutYes(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				arrObj.getTindakLanjutNo(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				arrObj.getInProgress(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				arrObj.getClosed(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				arrObj.getMeetSla(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				arrObj.getBeforeSla(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				arrObj.getOverSla(), mapCellFormat);
	}
	
	private void writeObjectToExcelSheet2(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum,
			ReportSuratMasukAmlRekap arrObj, int columnIndex) throws Exception {
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, rowNum, mapCellFormat);

		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getPengirimSuratEn(), arrObj.getPengirimSuratIn()), 
				mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				arrObj.getTotalSuratMasuk(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				arrObj.getTindakLanjutYes(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				arrObj.getTindakLanjutNo(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				arrObj.getInProgress(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				arrObj.getClosed(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				arrObj.getMeetSla(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				arrObj.getBeforeSla(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				arrObj.getOverSla(), mapCellFormat);
	}
	
	private List<String> buildListColumnNameRow1Sheet1() {
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
	
	private List<String> buildListColumnNameRow2Sheet1() {
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
	
	private List<String> buildListColumnNameRow1Sheet2() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Pengirim");
		listColumnNameTemp.add("Total Surat Masuk");
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
		
		searchCreationDateFrom = getSearchCriteriaValue(WHERE_CREATION_DATE_FROM);
		searchCreationDateTo = getSearchCriteriaValue(WHERE_CREATION_DATE_TO);
		searchSenderCode = getSearchCriteriaValue(WHERE_SENDER_CODE);
		
		ParameterDetail getSenderCodeName = parameterDetailService.getParameterDetailByParamDtlCode(searchSenderCode);
		
		if (getSenderCodeName != null) {
			senderCodeName = runnableFacesUtil.retrieveLocaleMessage(getSenderCodeName.getNameEn(), getSenderCodeName.getNameIn());
		}
		
		// Create Header Title [Start]
		reportUtil.writeCellTitle(sheet, 4,
				"Report Surat Masuk AML Rekap", mapCellFormat);

		row++;
		row++;

		// Label Creation Date
		reportUtil.writeCell(sheet, row, columnStart,
				"Creation Date : ",
				cfHeaderLabel);
		
		// Value Creation Date
		reportUtil.writeCell(sheet, row, columnStart + 1,
				searchCreationDateFrom + " "
						+ ((StringUtils.isNotBlank(searchCreationDateFrom) && StringUtils.isNotBlank(searchCreationDateTo))
								? runnableFacesUtil.retrieveMessage("textUntil") : "")
						+ " " + searchCreationDateTo,
				cfHeaderValue);
		row++;
		
		// Label Sender code
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportSuratMasukAmlRekapSender") + " : ",
				cfHeaderLabel);
		
		// Value Sender code
		reportUtil.writeCell(sheet, row, columnStart + 1,senderCodeName , cfHeaderValue);
		row++;
		
		// Label Printed By
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("textExcelCommonPrintedBy") + " : ", cfHeaderLabel);
		
		// Value Printed By
		reportUtil.writeCell(sheet, row, columnStart + 1, userNik, cfHeaderValue);
		row++;

		// Label Printed On
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("textExcelCommonPrintedOn") + " : ", cfHeaderLabel);
		
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

	public String getUserNik() {
		return userNik;
	}

	public void setUserNik(String userNik) {
		this.userNik = userNik;
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

	public ReportSuratMasukAmlRekapService getReportSuratMasukAmlRekapService() {
		return reportSuratMasukAmlRekapService;
	}

	public void setReportSuratMasukAmlRekapService(ReportSuratMasukAmlRekapService reportSuratMasukAmlRekapService) {
		this.reportSuratMasukAmlRekapService = reportSuratMasukAmlRekapService;
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

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public CommonReportUtil getReportUtil() {
		return reportUtil;
	}

	public void setReportUtil(CommonReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}

}
