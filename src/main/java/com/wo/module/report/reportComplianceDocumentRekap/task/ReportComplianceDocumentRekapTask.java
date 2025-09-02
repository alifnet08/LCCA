package com.wo.module.report.reportComplianceDocumentRekap.task;

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
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.SCMApiUpload;
import com.wo.module.common.utility.SCMApiUploadImpl;
import com.wo.module.lov.bean.RunnableFacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.report.reportComplianceDocumentRekap.constant.ReportComplianceDocumentRekapConstants;
import com.wo.module.report.reportComplianceDocumentRekap.model.ReportComplianceDocumentRekap;
import com.wo.module.report.reportComplianceDocumentRekap.service.ReportComplianceDocumentRekapService;
import com.wo.module.report.reportGen.constant.ReportSheetNameConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.service.ReportGenService;

public class ReportComplianceDocumentRekapTask implements Runnable, ReportSheetNameConstant, ReportComplianceDocumentRekapConstants{

static Logger logger = Logger.getLogger(ReportComplianceDocumentRekapTask.class);
	
	public final static String COMPLIANCE_DOC_TYPE_REPORT_COMPLIANCE_DOCUMENT_REKAP = "Report Compliance Document Rekap";
	private static final String PIE_CHART_TITLE_TOTAL_COMPLIANCE_DOCUMENT = "Total Compliance Document";
	private static final String PIE_CHART_TITLE_TOTAL_KELOMPOK_HUKT = "Total Kelompok HUK";
	
	private Long reportGenId;
	private String userNikName;
	
	private ReportGenService reportGenService;
	private ParameterDetailService parameterDetailService;
	private RunnableFacesUtil runnableFacesUtil;
	private ReportComplianceDocumentRekapService reportComplianceDocumentRekapService;
	
	@SuppressWarnings("rawtypes")
	private List<? extends SearchObject> searchCriteria;
	
	private String searchCreationDateFrom;
	private String searchCreationDateTo;
	private String searchDocumentDateFrom;
	private String searchDocumentDateTo;
	private String searchDocumentType;
	private String searchDocumentSubmitter;
	
	private CommonReportUtil reportUtil = new CommonReportUtil();
	
	private int chartCol1;
	private int chartCol2;
	
	@SuppressWarnings("rawtypes")
	public ReportComplianceDocumentRekapTask(Long reportGenId,
			ReportGenService reportGenService,
			ParameterDetailService parameterDetailService,
			RunnableFacesUtil runnableFacesUtil,
			ReportComplianceDocumentRekapService reportComplianceDocumentRekapService,
			List<? extends SearchObject> searchCriteria,
			String userNikName) {
		
		super();
		this.reportGenId = reportGenId;
		this.reportGenService = reportGenService;
		this.parameterDetailService = parameterDetailService;
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
		
		List<Integer> listColumnViewByTipeDocument = new ArrayList<Integer>();
		List<Integer> listColumnViewByKelompokHuk = new ArrayList<Integer>();
		UploadedFileWO ufw = null;
		String absoluteResultFilePath = null;
		
		try {
			
			absoluteResultFilePath = writeToFile(listColumnViewByTipeDocument,listColumnViewByKelompokHuk);
			
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
				COMPLIANCE_DOC_TYPE_REPORT_COMPLIANCE_DOCUMENT_REKAP
			);	
		saf.upload();
		return saf.getAsUploadedFileWO();
	}
	
	private String writeToFile(List<Integer> listColumnViewByTipeDocument, List<Integer> listColumnViewByKelompokHuk) throws Exception {
		String sheetName1 = SHEET_NAME_REPORT_COMPLIANCE_DOCUMENT_REKAP_BY_TIPE_DOCUMENT;
		String sheetName2 = SHEET_NAME_REPORT_COMPLIANCE_DOCUMENT_REKAP_BY_KELOMPOK_HUK;
		String sheetName3 = SHEET_NAME_REPORT_COMPLIANCE_DOCUMENT_REKAP_DIAGRAM;
		
		String fileNamePrefix = "ReportComplianceDocumentRekap";
		
		String fileName = getReportUtil().generateFileName(fileNamePrefix, userNikName);
		String filePath = getReportUtil().retrieveFilePath(parameterDetailService);
		
		Path dirPath = Paths.get(filePath);
		if(!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}
		
		XSSFWorkbook workbook = null;
		FileOutputStream fileOutputStream = null;
		
		List<String> buildListColumnNameRowSheet1 = buildListColumnNameRowSheet1();
		List<String> buildListColumnNameRowSheet2 = buildListColumnNameRowSheet2();
		
		List<ReportComplianceDocumentRekap> sheet1Results = (List<ReportComplianceDocumentRekap>)
				reportComplianceDocumentRekapService.getReportComplianceDocumentRekapByTipeDocumentData(searchCriteria);
		
		List<ReportComplianceDocumentRekap> sheet2Results = (List<ReportComplianceDocumentRekap>)
				reportComplianceDocumentRekapService.getReportComplianceDocumentRekapByKelompokHukData(searchCriteria);
		
		try {
			
			fileOutputStream = new FileOutputStream(filePath + fileName);
			workbook = new XSSFWorkbook();
			XSSFSheet sheet1 = workbook.createSheet(sheetName1);
			XSSFSheet sheet2 = workbook.createSheet(sheetName2);
			
			// setting column size [Start]
			// sheet 1
			if (listColumnViewByTipeDocument != null) {
				for (int i = 0; i < listColumnViewByTipeDocument.size(); i++) {
					sheet1.setColumnWidth(i, listColumnViewByTipeDocument.get(i));
					sheet1.autoSizeColumn(i);
				}
			}
			// sheet 2
			if (listColumnViewByKelompokHuk != null) {
				for (int i = 0; i < listColumnViewByKelompokHuk.size(); i++) {
					sheet2.setColumnWidth(i, listColumnViewByKelompokHuk.get(i));
					sheet2.autoSizeColumn(i);
				}
			}
			// setting column size [End]
			
			Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
			getReportUtil().createExcelCellFormat(workbook, mapCellFormat);
			
			this.writeExcelHeaderSheet1(sheet1, mapCellFormat);
			this.writeExcelHeaderSheet2(sheet2, mapCellFormat);
			
			CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER);
			
			// write sheet 1 column header [start]
			int rowIndexSheet1 = 11;
						
			if (buildListColumnNameRowSheet1 != null && !buildListColumnNameRowSheet1.isEmpty()) {
						
				int columnIndex = 0;
				for (String columnNameAlias : buildListColumnNameRowSheet1) {
							
					getReportUtil().writeCell(sheet1, rowIndexSheet1, columnIndex, columnNameAlias, cfColumnHeader);
								
					columnIndex++;
				}
			}
			// write sheet 1 column header [end]
			
			// write sheet 1 data detail [start]
			if (sheet1Results != null && !sheet1Results.isEmpty()) {
				rowIndexSheet1 += 1;
													
				int rowNum = 1;
				for (ReportComplianceDocumentRekap arrObj : sheet1Results) {
					int columnIndex = 0;
								
					writeObjectToExcelSheet1(sheet1, mapCellFormat, rowIndexSheet1, rowNum, arrObj, columnIndex);
								
					rowIndexSheet1++;
					rowNum++;
				}
							
			}
			// write sheet 1 data detail [end]
			
			// write sheet 2 column header [start]
			int rowIndexSheet2 = 11;
									
			if (buildListColumnNameRowSheet2 != null && !buildListColumnNameRowSheet2.isEmpty()) {
									
				int columnIndex = 0;
				for (String columnNameAlias : buildListColumnNameRowSheet2) {
										
					getReportUtil().writeCell(sheet2, rowIndexSheet2, columnIndex, columnNameAlias, cfColumnHeader);
											
					columnIndex++;
				}
			}
			// write sheet 2 column header [end]
			
			// write sheet 2 data detail [start]
			if (sheet2Results != null && !sheet2Results.isEmpty()) {
				rowIndexSheet2 += 1;
																
				int rowNum = 1;
				for (ReportComplianceDocumentRekap arrObj : sheet2Results) {
					int columnIndex = 0;
											
					writeObjectToExcelSheet2(sheet2, mapCellFormat, rowIndexSheet2, rowNum, arrObj, columnIndex);
											
					rowIndexSheet2++;
					rowNum++;
				}
										
			}
			// write sheet 2 data detail [end]
			
			// sheet 3 header [start]
			writeSheet3(sheetName3, filePath, workbook, sheet1Results, sheet2Results);
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
			List<ReportComplianceDocumentRekap> sheet1Results, List<ReportComplianceDocumentRekap> sheet2Results) throws IOException, FileNotFoundException {
		XSSFSheet sheet3 = workbook.createSheet(sheetName3);
		CreationHelper helper = workbook.getCreationHelper();
		
		createPie(filePath, workbook, sheet1Results, sheet3, helper, PIE_CHART_TITLE_TOTAL_COMPLIANCE_DOCUMENT);
		createPie(filePath, workbook, sheet2Results, sheet3, helper, PIE_CHART_TITLE_TOTAL_KELOMPOK_HUKT);
		
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
		
		chartCol1+=10;
		chartCol2+=10;

		Picture pictChart = drawing.createPicture(anchor, pieChartIndex);
		pictChart.resize();

		chartPath.delete();
	}
	
	private PieDataset createDataset(List<ReportComplianceDocumentRekap> result) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		for (ReportComplianceDocumentRekap reportComplianceDocumentRekap : result) {
			
			if(StringUtils.isBlank(reportComplianceDocumentRekap.getTipeDokumenEn()) ||
					StringUtils.isBlank(reportComplianceDocumentRekap.getTipeDokumenIn())){
				dataset.setValue("Undefined", reportComplianceDocumentRekap.getTotal());
			}
			else {		
				dataset.setValue(
					runnableFacesUtil.retrieveLocaleMessage(
							reportComplianceDocumentRekap.getTipeDokumenEn(),
							reportComplianceDocumentRekap.getTipeDokumenIn()),
					reportComplianceDocumentRekap.getTotal());
			}
		}
		return dataset;
	}
	
	
	
	private File printPie(PieDataset pieDataset, String filePath, String title) throws IOException {
		
		JFreeChart chart = ChartFactory.createPieChart3D(
				title,
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
			ReportComplianceDocumentRekap arrObj, int columnIndex) throws Exception{
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, rowNum, mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getTipeDokumenEn(), arrObj.getTipeDokumenIn())
				, mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, 
				arrObj.getTotal(), mapCellFormat);
	}
	
	private void writeObjectToExcelSheet2(XSSFSheet sheet2, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum,
			ReportComplianceDocumentRekap arrObj, int columnIndex) throws Exception{
		
		getReportUtil().writeCellDetail(sheet2, rowIndex, columnIndex++, rowNum, mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet2, rowIndex, columnIndex++, 
				runnableFacesUtil.retrieveLocaleMessage(arrObj.getTipeDokumenEn(), arrObj.getTipeDokumenIn())
				, mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet2, rowIndex, columnIndex++, 
				arrObj.getTotal(), mapCellFormat);
	}
	
	private List<String> buildListColumnNameRowSheet1() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Tipe Dokumen");
		listColumnNameTemp.add("Total Compliance Dokumen");
		
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRowSheet2() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Kelompok HUK");
		listColumnNameTemp.add("Total Kelompok HUK");
		
		return listColumnNameTemp;
	}
	
	public void writeExcelHeaderSheet1(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat) throws Exception {
		
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
		searchDocumentType = getSearchCriteriaValue(WHERE_DOCUMENT_TYPE);
		searchDocumentSubmitter = getSearchCriteriaValue(WHERE_DOCUMENT_SUBMITTER);
		
		// get name En and In
		ParameterDetail getDocumentTypeName = parameterDetailService.getParameterDetailByParamDtlCode(searchDocumentType);
		
		if (getDocumentTypeName != null) {
			documentTypeName = runnableFacesUtil.retrieveLocaleMessage(getDocumentTypeName.getNameEn(), getDocumentTypeName.getNameIn());
		}
		
		// Create Header Title [Start]
		reportUtil.writeCellTitle(sheet, 4,
				"Report Compliance Document Rekap", mapCellFormat);
						
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
				
		// Label Document Date
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportComplianceDocumentDetailDocumentDate") + " : ",
				cfHeaderLabel);
												
		// Value Document Date
		reportUtil.writeCell(sheet, row, columnStart + 1,
				searchDocumentDateFrom + " "
				+ ((StringUtils.isNotBlank(searchDocumentDateFrom) && StringUtils.isNotBlank(searchDocumentDateTo))
				? runnableFacesUtil.retrieveMessage("textUntil") : "")
				+ " " + searchDocumentDateTo,
				cfHeaderValue);
		row++;
		
		// Label Document Type
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportComplianceDocumentRekapDocumentType") + " : ",
				cfHeaderLabel);
								
		// Value Document Type
		reportUtil.writeCell(sheet, row, columnStart + 1, 
				documentTypeName,
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
	
	public void writeExcelHeaderSheet2(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat) throws Exception {
		
		String documentSubmitterName = "";
		
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
		searchDocumentSubmitter = getSearchCriteriaValue(WHERE_DOCUMENT_SUBMITTER);
		
		// get name En and In
		ParameterDetail getDocumentSubmitterName = parameterDetailService.getParameterDetailByParamDtlCode(searchDocumentSubmitter);
		
		if (getDocumentSubmitterName != null) {
			documentSubmitterName = runnableFacesUtil.retrieveLocaleMessage(getDocumentSubmitterName.getNameEn(), getDocumentSubmitterName.getNameIn());
		}
		
		// Create Header Title [Start]
		reportUtil.writeCellTitle(sheet, 4,
				"Report Compliance Document Rekap", mapCellFormat);
						
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
				
		// Label Document Date
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportComplianceDocumentDetailDocumentDate") + " : ",
				cfHeaderLabel);
												
		// Value Document Date
		reportUtil.writeCell(sheet, row, columnStart + 1,
				searchDocumentDateFrom + " "
				+ ((StringUtils.isNotBlank(searchDocumentDateFrom) && StringUtils.isNotBlank(searchDocumentDateTo))
				? runnableFacesUtil.retrieveMessage("textUntil") : "")
				+ " " + searchDocumentDateTo,
				cfHeaderValue);
		row++;
		
		// Label Document Submitter
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportComplianceDocumentRekapDocumentSubmitter") + " : ",
				cfHeaderLabel);
								
		// Value Document Submitter
		reportUtil.writeCell(sheet, row, columnStart + 1, 
				documentSubmitterName,
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

	public ReportComplianceDocumentRekapService getReportComplianceDocumentRekapService() {
		return reportComplianceDocumentRekapService;
	}

	public void setReportComplianceDocumentRekapService(
			ReportComplianceDocumentRekapService reportComplianceDocumentRekapService) {
		this.reportComplianceDocumentRekapService = reportComplianceDocumentRekapService;
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

	public String getSearchDocumentSubmitter() {
		return searchDocumentSubmitter;
	}

	public void setSearchDocumentSubmitter(String searchDocumentSubmitter) {
		this.searchDocumentSubmitter = searchDocumentSubmitter;
	}

	public CommonReportUtil getReportUtil() {
		return reportUtil;
	}

	public void setReportUtil(CommonReportUtil reportUtil) {
		this.reportUtil = reportUtil;
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
