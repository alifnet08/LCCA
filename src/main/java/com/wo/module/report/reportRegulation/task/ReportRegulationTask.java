package com.wo.module.report.reportRegulation.task;

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
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
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
import com.wo.module.report.reportRegulation.constant.ReportRegulationConstant;
import com.wo.module.report.reportRegulation.service.ReportRegulationService;
import com.wo.module.report.reportRegulation.vo.RegulationTrackRecordVo;
import com.wo.module.report.reportRegulation.vo.ReportRegulationDetailVo;
import com.wo.module.report.reportRegulation.vo.ReportRegulationRekapVo;
import com.wo.module.report.reportRegulation.vo.ReportRegulationTrackRecordVo;

public class ReportRegulationTask implements Runnable, Serializable{

	private static final long serialVersionUID = 2978031999504834439L;
	private static final Logger logger = Logger.getLogger(ReportRegulationTask.class);
	private static final String COMPLIANCE_DOC_TYPE_REPORT_REGULATION = "Report Regulation";
	private static final String PIE_CHART_TITLE = "Total Peraturan";

	private ReportGenService reportGenService;
	private ParameterDetailService parameterDetailService;
	private RunnableFacesUtil runnableFacesUtil;
	private ReportRegulationService reportRegulationService;
	
	private Long reportGenId;
	
	private String userNikName;
	private String searchJenisKetentuan;
	private String searchTipePeraturan;
	private String searchPublishDateFrom;
	private String searchPublishDateTo;
	private String searchExpiredDateFrom;
	private String searchExpiredhDateTo;
	private String searchStatus;
	private String searchPublisherUnit;
	
	@SuppressWarnings("rawtypes")
	private List<? extends SearchObject> searchCriteria;
	
	private CommonReportUtil reportUtil = new CommonReportUtil();
	
	@SuppressWarnings("rawtypes")
	public ReportRegulationTask(Long reportGenId,
			ReportGenService reportGenService,
			ParameterDetailService parameterDetailService,
			RunnableFacesUtil runnableFacesUtil,
			ReportRegulationService reportRegulationService,
			List<? extends SearchObject> searchCriteria,
			String userNikName) {
		super();
		this.reportGenId = reportGenId;
		this.reportGenService = reportGenService;
		this.parameterDetailService = parameterDetailService;
		this.runnableFacesUtil = runnableFacesUtil;
		this.reportRegulationService = reportRegulationService;
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
				COMPLIANCE_DOC_TYPE_REPORT_REGULATION);
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
		String sheetName1 = ReportSheetNameConstant.SHEET_NAME_REPORT_REGULATION_DETAIL;
		String sheetName2 = ReportSheetNameConstant.SHEET_NAME_REPORT_REGULATION_REKAP_PROVISION_TYPE;
		String sheetName3 = ReportSheetNameConstant.SHEET_NAME_REPORT_REGULATION_REKAP_HITS;
		String sheetName4 = ReportSheetNameConstant.SHEET_NAME_REPORT_REGULATION_DIAGRAM;
		
		String fileNamePrefix = "ReportRegulation";
		String fileName = getReportUtil().generateFileName(fileNamePrefix, userNikName);
		String filePath = getReportUtil().retrieveFilePath(parameterDetailService);
		
		Path dirPath = Paths.get(filePath);
		if (!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}
		
		XSSFWorkbook workbook = null;
		FileOutputStream fileOutputStream = null;
		
		List<String> buildListColumnNameRow1Sheet1 = buildListColumnNameRow1Sheet1();
		//List<String> buildListColumnNameRow1Sheet2 = buildListColumnNameRow1Sheet2();
		//List<String> buildListColumnNameRow1Sheet3 = buildListColumnNameRow1Sheet3();
		
		List<ReportRegulationDetailVo> sheet1Results = (List<ReportRegulationDetailVo>) reportRegulationService.getReportRegulationDetailByAllDataAsVo(searchCriteria);
		//List<ReportRegulationRekapVo> sheet2Results = (List<ReportRegulationRekapVo>) reportRegulationService.getReportRegulationRekapByProvisionTypeAsVo(searchCriteria);
		//List<ReportRegulationRekapVo> sheet3Results = (List<ReportRegulationRekapVo>) reportRegulationService.getReportRegulationRekapByHitsAsVo(searchCriteria);
		
		try {
			fileOutputStream = new FileOutputStream(filePath + fileName);
			workbook = new XSSFWorkbook();
			
			Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
			getReportUtil().createExcelCellFormat(workbook, mapCellFormat);
			writeSheet1(listColumnView, sheetName1, workbook, buildListColumnNameRow1Sheet1, sheet1Results, mapCellFormat);
			//writeSheet2(listColumnView, sheetName2, workbook, buildListColumnNameRow1Sheet2, sheet2Results, mapCellFormat);
			//writeSheet3(listColumnView, sheetName3, workbook, buildListColumnNameRow1Sheet3, sheet3Results, mapCellFormat);
			//writeSheet4(sheetName4, filePath, workbook, sheet2Results);
			
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
		List<String> buildListColumnNameRow1Sheet1, List<ReportRegulationDetailVo> sheet1Results,
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
		
		/* Write sheet 1 column header [Start] */
		CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER);
		int rowIndex = 11;
		
		if (buildListColumnNameRow1Sheet1 != null && !buildListColumnNameRow1Sheet1.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow1Sheet1) {
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);

				columnIndex++;
			}
		}
		/* Write sheet 1 column header [End] */
		
		/* write sheet 1 data detail [start] */
		if (sheet1Results != null && !sheet1Results.isEmpty()) {
			rowIndex +=1;
			
			int rowNum = 1;
			for (ReportRegulationDetailVo arrObj : sheet1Results) {
				int columnIndex = 0;

				int idx = writeObjectToExcelSheet1(sheet1, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);
				rowIndex = idx;

				rowNum++;
			
//				int columnIndex = 0;
//				int rowBefore = rowIndex;
//				rowIndex = writeObjectToExcelSheet1(sheet1, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);
//				if(rowBefore!=rowIndex) {
//					CellRangeAddress address = new CellRangeAddress(rowBefore, rowIndex, columnIndex, columnIndex+10);
//					sheet1.addMergedRegion(address);
//				}
//				rowIndex++;
//				rowNum++;
			}
		}
		/* write sheet 1 data detail [end] */
		sheet1.autoSizeColumn(34);
	}
	
	private void writeSheet2(List<Integer> listColumnView, String sheetName2, XSSFWorkbook workbook,
			List<String> buildListColumnNameRow1Sheet2, List<ReportRegulationRekapVo> sheet2Results,
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
		
		/* Write sheet 2 column header [Start] */
		CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER);
		int rowIndex = 11;
		
		if (buildListColumnNameRow1Sheet2 != null && !buildListColumnNameRow1Sheet2.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow1Sheet2) {
				getReportUtil().writeCell(sheet2, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);

				columnIndex++;
			}
		}
		/* Write sheet 2 column header [End] */
		
		/* write sheet 2 data detail [start] */
		if (sheet2Results != null && !sheet2Results.isEmpty()) {
			rowIndex +=1;
			
			int rowNum = 1;
			for (ReportRegulationRekapVo arrObj : sheet2Results) {
				int columnIndex = 0;
				
				writeObjectToExcelSheet2(sheet2, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);
				
				rowIndex++;
				rowNum++;
			}
		}
		/* write sheet 2 data detail [end] */
		sheet2.autoSizeColumn(34);
	}
	
	private void writeSheet3(List<Integer> listColumnView, String sheetName3, XSSFWorkbook workbook,
			List<String> buildListColumnNameRow1Sheet3, List<ReportRegulationRekapVo> sheet3Results,
			Map<String, CellStyle> mapCellFormat) throws Exception {
		
		XSSFSheet sheet3 = workbook.createSheet(sheetName3);
		
		// setting column size [Start]
		if (listColumnView != null) {
			for (int i = 0; i < listColumnView.size(); i++) {
				sheet3.setColumnWidth(i, listColumnView.get(i));
				sheet3.autoSizeColumn(i);
			}
		}
		// setting column size [End]
		
		this.writeExcelHeader(sheet3, mapCellFormat);
		
		/* Write sheet 3 column header [Start] */
		CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER);
		int rowIndex = 11;
		
		if (buildListColumnNameRow1Sheet3 != null && !buildListColumnNameRow1Sheet3.isEmpty()) {
			int columnIndex = 0;
			for (String columnNameAlias : buildListColumnNameRow1Sheet3) {
				getReportUtil().writeCell(sheet3, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);

				columnIndex++;
			}
		}
		/* Write sheet 3 column header [End] */
		
		/* write sheet 3 data detail [start] */
		if (sheet3Results != null && !sheet3Results.isEmpty()) {
			rowIndex +=1;
			
			int rowNum = 1;
			for (ReportRegulationRekapVo arrObj : sheet3Results) {
				int columnIndex = 0;
				
				writeObjectToExcelSheet3(sheet3, mapCellFormat, rowIndex, rowNum, arrObj, columnIndex);
				
				rowIndex++;
				rowNum++;
			}
		}
		/* write sheet 3 data detail [end] */
		sheet3.autoSizeColumn(34);
	}
	
	private void writeSheet4(String sheetName4, String filePath, XSSFWorkbook workbook, 
			List<ReportRegulationRekapVo> sheet2Results) throws IOException, FileNotFoundException {
		XSSFSheet sheet4 = workbook.createSheet(sheetName4);
		CreationHelper helper = workbook.getCreationHelper();

		createPie(filePath, workbook, sheet2Results, sheet4, helper);
		createBar(filePath, workbook, sheet2Results, sheet4, helper);
		
	}
	
	private int writeObjectToExcelSheet1(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum, ReportRegulationDetailVo arrObj, int columnIndex) throws Exception {
		if (arrObj.getCountTrackRecord() <= 1 ) {
			if (arrObj.getExpiredWarning() == 1) {
				CellStyle cellFormatWarning = (CellStyle) mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_LABEL_WARNING);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, rowNum, cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, runnableFacesUtil.retrieveLocaleMessage(arrObj.getPeraturanEn(), arrObj.getPeraturanIn()), cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, arrObj.getDirectorate(), cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, arrObj.getJudul(), cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, arrObj.getPublishDate(), cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, arrObj.getUrl(), cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, runnableFacesUtil.retrieveLocaleMessage(arrObj.getDocumentTypeEn(), arrObj.getDocumentTypeIn()), cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, arrObj.getHits(), cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, arrObj.getExpiredDate(), cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, arrObj.getExpiredWarning(), cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, runnableFacesUtil.retrieveLocaleMessage(arrObj.getStatusEn(), arrObj.getStatusIn()), cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, arrObj.getTypeReviewDateName(), cellFormatWarning);
				if(!arrObj.getTrackRecordVos().isEmpty()) {
					for (RegulationTrackRecordVo t : arrObj.getTrackRecordVos()) {
						getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, t.getTrackName(), cellFormatWarning);
						if(!t.getTrackCode().equals(ParameterDetail.PARAM_DET_CODE_TRACK_RECORD_NEW_REGULATION)) getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, t.getRegulationNoPrev(), cellFormatWarning);
						if(!t.getTrackCode().equals(ParameterDetail.PARAM_DET_CODE_TRACK_RECORD_NEW_REGULATION)) getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, t.getRegulationNamePrev(), cellFormatWarning);
					}
				}
			} else {
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, rowNum, mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, runnableFacesUtil.retrieveLocaleMessage(arrObj.getPeraturanEn(), arrObj.getPeraturanIn()), mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getDirectorate(), mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getJudul(), mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getPublishDate(), mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getUrl(), mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, runnableFacesUtil.retrieveLocaleMessage(arrObj.getDocumentTypeEn(), arrObj.getDocumentTypeIn()), mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getHits(), mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getExpiredDate(), mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getExpiredWarning(), mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, runnableFacesUtil.retrieveLocaleMessage(arrObj.getStatusEn(), arrObj.getStatusIn()), mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTypeReviewDateName(), mapCellFormat);
				if(!arrObj.getTrackRecordVos().isEmpty()) {
					for (RegulationTrackRecordVo t : arrObj.getTrackRecordVos()) {
						getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, t.getTrackName(), mapCellFormat);
						if(!t.getTrackCode().equals(ParameterDetail.PARAM_DET_CODE_TRACK_RECORD_NEW_REGULATION)) getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, t.getRegulationNoPrev(), mapCellFormat);
						if(!t.getTrackCode().equals(ParameterDetail.PARAM_DET_CODE_TRACK_RECORD_NEW_REGULATION)) getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, t.getRegulationNamePrev(), mapCellFormat);
					}
				}
			}
		} else {
			int firstRow = rowIndex;
			if (arrObj.getExpiredWarning() == 1) {
				CellStyle cellFormatWarning = (CellStyle) mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_LABEL_WARNING);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, rowNum, cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, runnableFacesUtil.retrieveLocaleMessage(arrObj.getPeraturanEn(), arrObj.getPeraturanIn()), cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, arrObj.getDirectorate(), cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, arrObj.getJudul(), cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, arrObj.getPublishDate(), cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, arrObj.getUrl(), cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, runnableFacesUtil.retrieveLocaleMessage(arrObj.getDocumentTypeEn(), arrObj.getDocumentTypeIn()), cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, arrObj.getHits(), cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, arrObj.getExpiredDate(), cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, arrObj.getExpiredWarning(), cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, runnableFacesUtil.retrieveLocaleMessage(arrObj.getStatusEn(), arrObj.getStatusIn()), cellFormatWarning);
				getReportUtil().writeCell(sheet1, rowIndex, columnIndex++, arrObj.getTypeReviewDateName(), cellFormatWarning);
				if(!arrObj.getTrackRecordVos().isEmpty()) {
					for (RegulationTrackRecordVo t : arrObj.getTrackRecordVos()) {
						getReportUtil().writeCell(sheet1, rowIndex, 12, t.getTrackName(), cellFormatWarning);
						if(!t.getTrackCode().equals(ParameterDetail.PARAM_DET_CODE_TRACK_RECORD_NEW_REGULATION)) getReportUtil().writeCell(sheet1, rowIndex, 13, t.getRegulationNoPrev(), cellFormatWarning);
						if(!t.getTrackCode().equals(ParameterDetail.PARAM_DET_CODE_TRACK_RECORD_NEW_REGULATION)) getReportUtil().writeCell(sheet1, rowIndex, 14, t.getRegulationNamePrev(), cellFormatWarning);
						rowIndex++;
					}
				}
			} else {
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, rowNum, mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, runnableFacesUtil.retrieveLocaleMessage(arrObj.getPeraturanEn(), arrObj.getPeraturanIn()), mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getDirectorate(), mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getJudul(), mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getPublishDate(), mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getUrl(), mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, runnableFacesUtil.retrieveLocaleMessage(arrObj.getDocumentTypeEn(), arrObj.getDocumentTypeIn()), mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getHits(), mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getExpiredDate(), mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getExpiredWarning(), mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, runnableFacesUtil.retrieveLocaleMessage(arrObj.getStatusEn(), arrObj.getStatusIn()), mapCellFormat);
				getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTypeReviewDateName(), mapCellFormat);
				if(!arrObj.getTrackRecordVos().isEmpty()) {
					for (RegulationTrackRecordVo t : arrObj.getTrackRecordVos()) {
						getReportUtil().writeCellDetail(sheet1, rowIndex, 12, t.getTrackName(), mapCellFormat);
						if(!t.getTrackCode().equals(ParameterDetail.PARAM_DET_CODE_TRACK_RECORD_NEW_REGULATION)) getReportUtil().writeCellDetail(sheet1, rowIndex, 13, t.getRegulationNoPrev(), mapCellFormat);
						if(!t.getTrackCode().equals(ParameterDetail.PARAM_DET_CODE_TRACK_RECORD_NEW_REGULATION)) getReportUtil().writeCellDetail(sheet1, rowIndex, 14, t.getRegulationNamePrev(), mapCellFormat);
						rowIndex++;
					}
				}
			}
			rowIndex = rowIndex - 1;
			for (int i = 0; i < 12; i++) {
				mergedExcel(sheet1, firstRow, rowIndex, i, i);
			}
		}
		rowIndex = rowIndex + 1;
		return rowIndex;
	}
	
	private void mergedExcel(Sheet sheet, int firstRow, int lastRow, int firstColumn, int lastColumn) {
		CellRangeAddress cra = new CellRangeAddress(firstRow,lastRow,firstColumn,lastColumn);
		sheet.addMergedRegion(cra);
		RegionUtil.setBorderTop(BorderStyle.THIN, cra, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, cra, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, cra, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, cra, sheet);
	}
	
	private void writeObjectToExcelSheet2(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum,
			ReportRegulationRekapVo arrObj, int columnIndex) throws Exception {
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, rowNum, mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getDocumentTypeIn(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTotalRegulation(), mapCellFormat);
	}
	
	private void writeObjectToExcelSheet3(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum,
			ReportRegulationRekapVo arrObj, int columnIndex) throws Exception {
	
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, rowNum, mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getDocumentTypeIn(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, columnIndex++, arrObj.getTotalRegulation(), mapCellFormat);
	}
	
	@SuppressWarnings("rawtypes")
	private void createBar(String filePath, XSSFWorkbook workbook,
			List<ReportRegulationRekapVo> sheet2Results, XSSFSheet sheet, CreationHelper helper)
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
	
	private DefaultCategoryDataset createBarDataset(List<ReportRegulationRekapVo> result) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		for (ReportRegulationRekapVo reportRegulationRekapVo : result) {
			dataset.addValue(reportRegulationRekapVo.getTotalRegulation(),
					reportRegulationRekapVo.getDocumentTypeIn(), reportRegulationRekapVo.getDocumentTypeIn() );
		}
		
		return dataset;
	}
	
	@SuppressWarnings("rawtypes")
	private void createPie(String filePath, XSSFWorkbook workbook,
			List<ReportRegulationRekapVo> sheet2Results, XSSFSheet sheet, CreationHelper helper) throws IOException, FileNotFoundException {
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
	
	private PieDataset createDataset(List<ReportRegulationRekapVo> result) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		for (ReportRegulationRekapVo reportRegulationRekapVo : result) {
			dataset.setValue(reportRegulationRekapVo.getDocumentTypeIn(),
					reportRegulationRekapVo.getTotalRegulation());
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
	
	private List<String> buildListColumnNameRow1Sheet1(){
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Jenis Peraturan");
		listColumnNameTemp.add("Direktorat");
		listColumnNameTemp.add("Judul Peraturan");
		listColumnNameTemp.add("Tanggal Terbit");
		listColumnNameTemp.add("URL");
		listColumnNameTemp.add("Tipe Peraturan");
		listColumnNameTemp.add("Hits");
		listColumnNameTemp.add("Tanggal Ulasan");
		listColumnNameTemp.add("Expired Warning");
		listColumnNameTemp.add("Status");
		listColumnNameTemp.add("Tipe Tanggal Ulasan");
		listColumnNameTemp.add("Rekam Jejak");
		listColumnNameTemp.add("No. Peraturan Lama");
		listColumnNameTemp.add("Judul Peraturan Lama");
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow1Sheet2(){
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Tipe Peraturan");
		listColumnNameTemp.add("Total Peraturan");
		return listColumnNameTemp;
	}
	
	private List<String> buildListColumnNameRow1Sheet3(){
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("Tipe Peraturan");
		listColumnNameTemp.add("Total Peraturan");
		return listColumnNameTemp;
	}
	
	public void writeExcelHeader(XSSFSheet sheet, Map<String, CellStyle> mapCellFormat) throws Exception{
		
		int row = 0;
		int columnStart = 0;
		
		SimpleDateFormat printTimestamp = new SimpleDateFormat(CommonConstants.INPUT_DATE_TIME_FORMAT);
		String excelPrintDate = printTimestamp.format(new Date());
		
		/* Create Header [Start] */
		CellStyle cfHeaderValue = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_VALUE);
		CellStyle cfHeaderLabel = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_LABEL);
		/* Create Header [End] */
		
		searchJenisKetentuan = getSearchCriteriaValue(ReportRegulationConstant.SEARCH_BY_JENIS_PERATURAN);
		searchTipePeraturan = getSearchCriteriaValue(ReportRegulationConstant.SEARCH_BY_TIPE_PERATURAN);
		searchPublishDateFrom = getSearchCriteriaValue(ReportRegulationConstant.SEARCH_BY_PUBLISH_DATE_FROM);
		searchPublishDateTo = getSearchCriteriaValue(ReportRegulationConstant.SEARCH_BY_PUBLISH_DATE_TO);
		searchExpiredDateFrom = getSearchCriteriaValue(ReportRegulationConstant.SEARCH_BY_EXPIRED_DATE_FROM);
		searchExpiredhDateTo = getSearchCriteriaValue(ReportRegulationConstant.SEARCH_BY_EXPIRED_DATE_TO);
		searchStatus = getSearchCriteriaValue(ReportRegulationConstant.SEARCH_BY_STATUS);
		searchPublisherUnit = getSearchCriteriaValue(ReportRegulationConstant.SEARCH_BY_UNIT_PENGUSUL);
		
		String publishFrom = "";
		String publishTo = "";
		String expiredFrom = "";
		String expiredTo = "";
		String jenisKetentuan = "";
		String tipePeraturan = "";
		String status = "";
		
		/* init value */
		if (StringUtils.isNotBlank(searchPublishDateFrom)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchPublishDateFrom);
				publishFrom = DateUtil.dateToString(tmpDate);
			} catch (Exception e) {
				publishFrom = "";
			}
		}
		if (StringUtils.isNotBlank(searchPublishDateTo)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchPublishDateTo);
				publishTo = DateUtil.dateToString(tmpDate);
			} catch (Exception e) {
				publishTo = "";
			}
		}
		if (StringUtils.isNotBlank(searchExpiredDateFrom)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchExpiredDateFrom);
				expiredFrom = DateUtil.dateToString(tmpDate);
			} catch (Exception e) {
				expiredFrom = "";
			}
		}
		if (StringUtils.isNotBlank(searchExpiredhDateTo)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchExpiredhDateTo);
				expiredTo = DateUtil.dateToString(tmpDate);
			} catch (Exception e) {
				expiredTo = "";
			}
		}
		
		ParameterDetail getJenisKetentuan = parameterDetailService.getParameterDetailByParamDtlCode(searchJenisKetentuan);
		ParameterDetail getStatus = parameterDetailService.getParameterDetailByParamDtlCode(searchStatus);
		if (getJenisKetentuan != null) {
			jenisKetentuan = runnableFacesUtil.retrieveLocaleMessage(getJenisKetentuan.getNameEn(), getJenisKetentuan.getNameIn());
		}
		if (getStatus != null) {
			status = runnableFacesUtil.retrieveLocaleMessage(getStatus.getNameEn(), getStatus.getNameIn());
		}
		if(searchTipePeraturan!= null){
			tipePeraturan = searchTipePeraturan;
		}
		/* init value */
		
		/* Create Header Title [Start] */
		if (sheet.getSheetName().equals(ReportSheetNameConstant.SHEET_NAME_REPORT_REGULATION_DETAIL)) {
			reportUtil.writeCellTitle(sheet, 4, "Report Regulation Detail", mapCellFormat);
		} else {
			reportUtil.writeCellTitle(sheet, 4, "Report Regulation Rekap", mapCellFormat);
		}
		/* Create Header Title [End] */
		row++;
		row++;
		
		/* Label Jenis Ketentuan */
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportRegulationJenisKetentuan") + " : ", cfHeaderLabel);
		/* Value Jenis Ketentuan */
		reportUtil.writeCell(sheet, row, columnStart + 1, jenisKetentuan, cfHeaderValue);
		row++;
		
		/* Label Tipe Peraturan */
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formInternalRegulationRegulationType") + " : ", cfHeaderLabel);
		/* Value Tipe Peraturan */
		reportUtil.writeCell(sheet, row, columnStart + 1, tipePeraturan, cfHeaderValue);
		row++;
		
		/* Label Publish Date */
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportRegulationPublishDate") + " : ", cfHeaderLabel);
		/* Value Publish Date */
		reportUtil.writeCell(sheet, row, columnStart + 1,
				publishFrom != null ? publishFrom : "" + " "
						+ ((StringUtils.isNotBlank(publishFrom) && StringUtils.isNotBlank(publishTo))
								? runnableFacesUtil.retrieveMessage("textUntil")
								: "")
						+ " " + publishTo != null ? publishTo : "",
				cfHeaderValue);
		row++;
		
		/* Label Expired Date */
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportRegulationExpiredDate") + " : ", cfHeaderLabel);
		/* Value Expired Date */
		reportUtil.writeCell(sheet, row, columnStart + 1,
				expiredFrom != null ? expiredFrom : "" + " "
						+ ((StringUtils.isNotBlank(expiredFrom) && StringUtils.isNotBlank(expiredTo))
								? runnableFacesUtil.retrieveMessage("textUntil")
								: "")
						+ " " + expiredTo != null ? expiredTo : "",
				cfHeaderValue);
		row++;
		
		/* Label Publisher Unit */
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportRegulationpPublisherUnit") + " : ", cfHeaderLabel);
		/* Value Publisher Unit */
		reportUtil.writeCell(sheet, row, columnStart + 1, searchPublisherUnit, cfHeaderValue);
		row++;
		
		/* Label Status */
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportRegulationStatus") + " : ", cfHeaderLabel);
		/* Value Status */
		reportUtil.writeCell(sheet, row, columnStart + 1, status, cfHeaderValue);
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

	public ReportRegulationService getReportRegulationService() {
		return reportRegulationService;
	}

	public void setReportRegulationService(ReportRegulationService reportRegulationService) {
		this.reportRegulationService = reportRegulationService;
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

	public String getSearchJenisKetentuan() {
		return searchJenisKetentuan;
	}

	public void setSearchJenisKetentuan(String searchJenisKetentuan) {
		this.searchJenisKetentuan = searchJenisKetentuan;
	}

	public String getSearchPublishDateFrom() {
		return searchPublishDateFrom;
	}

	public void setSearchPublishDateFrom(String searchPublishDateFrom) {
		this.searchPublishDateFrom = searchPublishDateFrom;
	}

	public String getSearchPublishDateTo() {
		return searchPublishDateTo;
	}

	public void setSearchPublishDateTo(String searchPublishDateTo) {
		this.searchPublishDateTo = searchPublishDateTo;
	}

	public String getSearchExpiredDateFrom() {
		return searchExpiredDateFrom;
	}

	public void setSearchExpiredDateFrom(String searchExpiredDateFrom) {
		this.searchExpiredDateFrom = searchExpiredDateFrom;
	}

	public String getSearchExpiredhDateTo() {
		return searchExpiredhDateTo;
	}

	public void setSearchExpiredhDateTo(String searchExpiredhDateTo) {
		this.searchExpiredhDateTo = searchExpiredhDateTo;
	}

	public String getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static String getComplianceDocTypeReportRegulation() {
		return COMPLIANCE_DOC_TYPE_REPORT_REGULATION;
	}

	public String getSearchPublisherUnit() {
		return searchPublisherUnit;
	}

	public void setSearchPublisherUnit(String searchPublisherUnit) {
		this.searchPublisherUnit = searchPublisherUnit;
	}

	public static String getPieChartTitle() {
		return PIE_CHART_TITLE;
	}

}
