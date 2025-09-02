package com.wo.module.report.reportLogUser.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
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
import com.wo.module.report.reportLogUser.constant.ReportLogUserConstant;
import com.wo.module.report.reportLogUser.service.ReportLogUserService;
import com.wo.module.report.reportLogUser.vo.ReportLogUserDiagramVo;
import com.wo.module.report.reportLogUser.vo.ReportLogUserVo;

public class ReportLogUserTask implements Runnable, Serializable{

	private static final long serialVersionUID = -3598624159841243943L;
	private static final Logger logger = Logger.getLogger(ReportLogUserTask.class);
	private static final String REPORT_LOG_USER = "Report Log User";
	
	private Long reportGenId;
	private String userNikName;

	private ReportGenService reportGenService;
	private ParameterDetailService parameterDetailService;
	private RunnableFacesUtil runnableFacesUtil;
	private ReportLogUserService reportLogUserService;
	
	@SuppressWarnings("rawtypes")
	private List<? extends SearchObject> searchCriteria;
	
	private String searchCreationDateFrom;
	private String searchCreationDateTo;
	
	private CommonReportUtil reportUtil = new CommonReportUtil();
	
	@SuppressWarnings("rawtypes")
	public ReportLogUserTask(Long reportGenId,
			ReportGenService reportGenService,
			ParameterDetailService parameterDetailService,
			RunnableFacesUtil runnableFacesUtil,
			ReportLogUserService reportLogUserService,
			List<? extends SearchObject> searchCriteria,
			String userNikName) {
		super();
		this.reportGenId = reportGenId;
		this.reportGenService = reportGenService;
		this.parameterDetailService = parameterDetailService;
		this.runnableFacesUtil = runnableFacesUtil;
		this.reportLogUserService = reportLogUserService;
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
				REPORT_LOG_USER);
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
		String sheetName1 = ReportSheetNameConstant.SHEET_NAME_REPORT_LOG_USER_DETAIL;
		String sheetName2 = ReportSheetNameConstant.SHEET_NAME_REPORT_LOG_USER_DIAGRAM;
		
		String fileNamePrefix = "ReportLogUser";
		
		String fileName = getReportUtil().generateFileName(fileNamePrefix, userNikName);
		String filePath = getReportUtil().retrieveFilePath(parameterDetailService);
		
		Path dirPath = Paths.get(filePath);
		if (!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}

		XSSFWorkbook workbook = null;
		FileOutputStream fileOutputStream = null;
		
		List<String> buildListColumnNameRow1Sheet1 = buildListColumnNameRow1Sheet1();
		
		List<ReportLogUserVo> sheet1Results = (List<ReportLogUserVo>) reportLogUserService.getReportLogUserDetailAsVo(searchCriteria);
	//	List<ReportLogUserDiagramVo> sheet2Results = (List<ReportLogUserDiagramVo>) reportLogUserService.getReportLogUserGrafikAsVo(searchCriteria);
		
		try {
			fileOutputStream = new FileOutputStream(filePath + fileName);
			workbook = new XSSFWorkbook();

			Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
			getReportUtil().createExcelCellFormat(workbook, mapCellFormat);
			
			writeSheet1(listColumnView, sheetName1, workbook, buildListColumnNameRow1Sheet1, sheet1Results,
					mapCellFormat);
			
			//writeSheet2(sheetName2, filePath, workbook, sheet2Results);
			
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
		List<String> buildListColumnNameRow1Sheet1, List<ReportLogUserVo> sheet1Results,
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
		
		CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER);
		int rowIndex = 11;
		
		if (buildListColumnNameRow1Sheet1 != null && !buildListColumnNameRow1Sheet1.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow1Sheet1) {
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);

				columnIndex++;
			}
		}
		
		// write sheet 1 data detail [start]
		if (sheet1Results != null && !sheet1Results.isEmpty()) {
			rowIndex += 1;
			
			int rowNum = 1;
			for (ReportLogUserVo arrObj : sheet1Results) {
				int columnIndex = 0;
				
				writeObjectToExcelSheet1(sheet1, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);

				rowIndex++;
				rowNum++;
			}
		}
		
		// write sheet 1 data detail [end]
		sheet1.autoSizeColumn(15);
	}
	
	private void writeSheet2(String sheetName2, String filePath, XSSFWorkbook workbook,
			List<ReportLogUserDiagramVo> sheet2Results) throws IOException, FileNotFoundException {
		XSSFSheet sheet2 = workbook.createSheet(sheetName2);
		CreationHelper helper = workbook.getCreationHelper();
		
		createBar(filePath, workbook, sheet2Results, sheet2, helper);
	}
	
	private void writeObjectToExcelSheet1(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum,
		ReportLogUserVo arrObj, int columnIndex) throws Exception{
	
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, rowNum, mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getTitle(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getData(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getCreatedBy(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getCreatedName(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getCreatedDate(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getCreatedPosition(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getCreatedBranch(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getLastStatus(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getLastUpdateBy(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getLastUpdateName(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getLastUpdateDate(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getLastUpdatePosition(), mapCellFormat);
		
		getReportUtil().writeCellDetail(sheet, rowIndex, columnIndex++, arrObj.getLastUpdateBranch(), mapCellFormat);
	}
	
	@SuppressWarnings("rawtypes")
	private void createBar(String filePath, XSSFWorkbook workbook, List<ReportLogUserDiagramVo> sheet2Results,
			XSSFSheet sheet, CreationHelper helper) throws IOException, FileNotFoundException {
		DefaultCategoryDataset barDataSet = createBarDataset(sheet2Results);
		File barPath = printBar(barDataSet, filePath);
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();
		anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);
		
		int barChartIndex = workbook.addPicture(FileUtil.readBytesFromFile(barPath), Workbook.PICTURE_TYPE_JPEG);
		
		anchor.setCol1(0);
		anchor.setRow1(1);
		anchor.setCol2(1);
		anchor.setRow1(1);
		
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
		
		ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.TOP_CENTER);
		BarRenderer renderer = (BarRenderer)  plot.getRenderer();
		renderer.setPositiveItemLabelPositionFallback(position);
		
		int width = 640;
		int height = 520;
		File barChartImage = new File(filePath + "bar-chart" + CommonConstants.SEPARATOR_DASH
				+ System.currentTimeMillis() + CommonConstants.FILE_TYPE_JPEG);
		
		ChartUtilities.saveChartAsJPEG(barChartImage, chart, width, height);
		
		return barChartImage;
	}
	
	private DefaultCategoryDataset createBarDataset(List<ReportLogUserDiagramVo> result) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		for (ReportLogUserDiagramVo reportLogUserDiagramVo : result) {
			dataset.addValue(reportLogUserDiagramVo.getPageCount(), 
					reportLogUserDiagramVo.getPageName(),
					reportLogUserDiagramVo.getPageName());
		}
		
		return dataset;
	}
	
	private List<String> buildListColumnNameRow1Sheet1() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Title");
		listColumnNameTemp.add("Data");
		listColumnNameTemp.add("Created By");
		listColumnNameTemp.add("Created Name");
		listColumnNameTemp.add("Created Date");
		listColumnNameTemp.add("Created Position");
		listColumnNameTemp.add("Created Branch");
		listColumnNameTemp.add("Last Status");
		listColumnNameTemp.add("Last Update By");
		listColumnNameTemp.add("Last Update Name");
		listColumnNameTemp.add("Last Update Date");
		listColumnNameTemp.add("Last Update Position");
		listColumnNameTemp.add("Last Update Branch");

		return listColumnNameTemp;
	}
	
	public void writeExcelHeader(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat) throws Exception {
		String from = "";
		String to = "";
		
		int row = 0;
		int columnStart = 0;

		SimpleDateFormat printTimestamp = new SimpleDateFormat(CommonConstants.INPUT_DATE_TIME_FORMAT);
		String excelPrintDate = printTimestamp.format(new Date());

		// Create Header [start]
		CellStyle cfHeaderValue = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_VALUE);
		CellStyle cfHeaderLabel = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_LABEL);

		// Create Header [end]

		// Create Header Title [Start]
		if (sheet.getSheetName().equals(ReportSheetNameConstant.SHEET_NAME_REPORT_LOG_USER_DETAIL)) {
			reportUtil.writeCellTitle(sheet, 4, "Report Log User Detail", mapCellFormat);
		} else {
			reportUtil.writeCellTitle(sheet, 4, "Report Log User Rekap", mapCellFormat);
		}
		row++;
		row++;
		
		searchCreationDateFrom = getSearchCriteriaValue(ReportLogUserConstant.WHERE_CREATE_DATE_FROM);
		searchCreationDateTo = getSearchCriteriaValue(ReportLogUserConstant.WHERE_CREATE_DATE_TO);
		
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
		
		// Label Creation Date
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportLogUserCreationDate") + " : ", cfHeaderLabel);

		// Value Creation Date
		reportUtil.writeCell(sheet, row, columnStart + 1,
				from + " "
						+ ((StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to))
								? runnableFacesUtil.retrieveMessage("textUntil")
								: "")
						+ " " + to,
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

	public ReportLogUserService getReportLogUserService() {
		return reportLogUserService;
	}

	public void setReportLogUserService(ReportLogUserService reportLogUserService) {
		this.reportLogUserService = reportLogUserService;
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

	public CommonReportUtil getReportUtil() {
		return reportUtil;
	}

	public void setReportUtil(CommonReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static String getReportLogUser() {
		return REPORT_LOG_USER;
	}
	

}
