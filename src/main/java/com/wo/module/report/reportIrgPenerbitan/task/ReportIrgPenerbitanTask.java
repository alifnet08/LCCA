package com.wo.module.report.reportIrgPenerbitan.task;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.report.CommonReportUtil;
import com.wo.module.common.util.DateUtil;
import com.wo.module.common.utility.SCMApiUpload;
import com.wo.module.common.utility.SCMApiUploadImpl;
import com.wo.module.holiday.service.HolidayService;
import com.wo.module.lov.bean.RunnableFacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.report.reportGen.constant.ReportSheetNameConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.report.reportIrgPenerbitan.constant.ReportIrgPenerbitanConstants;
import com.wo.module.report.reportIrgPenerbitan.model.ReportIrgPenerbitan;
import com.wo.module.report.reportIrgPenerbitan.model.ReportIrgPenerbitanTpg;
import com.wo.module.report.reportIrgPenerbitan.model.ReportIrgPenerbitanTpk;
import com.wo.module.report.reportIrgPenerbitan.service.ReportIrgPenerbitanService;
import com.wo.module.user.service.UserService;

public class ReportIrgPenerbitanTask implements Runnable,
	ReportSheetNameConstant, ReportIrgPenerbitanConstants {

	static Logger logger = Logger.getLogger(ReportIrgPenerbitanTask.class);
	
	public final static String COMPLIANCE_DOC_TYPE_REPORT_IRG_PENERBITAN = "Report Internal Regulation Penerbitan";
	
	private Long reportGenId;
	private String userNikName;
	
	private ReportGenService reportGenService;
	private ParameterDetailService parameterDetailService;
	private RunnableFacesUtil runnableFacesUtil;
	private ReportIrgPenerbitanService reportIrgPenerbitanService;
	private UserService userService;
	private HolidayService holidayService;
	
	@SuppressWarnings("rawtypes")
	private List<? extends SearchObject> searchCriteria;
	
	private String searchDateFrom;
	private String searchDateTo;
	private String searchRegulationType;
	private String searchUnitKerjaTpg;
		
	private CommonReportUtil reportUtil = new CommonReportUtil();
	
	@SuppressWarnings("rawtypes")
	public ReportIrgPenerbitanTask(Long reportGenId,
			ReportGenService reportGenService,
			ParameterDetailService parameterDetailService,
			UserService userService,
			HolidayService holidayService,
			RunnableFacesUtil runnableFacesUtil,
			ReportIrgPenerbitanService reportIrgPenerbitanService,
			List<? extends SearchObject> searchCriteria,
			String userNikName) {
		super();
		this.reportGenId = reportGenId;
		this.reportGenService = reportGenService;
		this.parameterDetailService = parameterDetailService;
		this.userService = userService;
		this.holidayService = holidayService;
		this.setRunnableFacesUtil(runnableFacesUtil);
		this.reportIrgPenerbitanService = reportIrgPenerbitanService;
		this.searchCriteria = searchCriteria;
		this.userNikName = userNikName;
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
		List<Integer> listColumnView = new ArrayList<Integer>();
		UploadedFileWO uf = null;
		String absoluteResultFilePath = null;
		
		try {
			
			absoluteResultFilePath = writeToFile(listColumnView);
			
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
				parameterDetailService, COMPLIANCE_DOC_TYPE_REPORT_IRG_PENERBITAN);	
		saf.upload();
		return saf.getAsUploadedFileWO();
	}
	
	private String writeToFile(List<Integer> listColumnView) throws Exception {
		String sheetName1 = SHEET_NAME_REPORT_IRG_PENERBITAN;
		
		String fileNamePrefix = "ReportIrgPenerbitan";
		
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
		
		List<ReportIrgPenerbitan> sheet1Results = (List<ReportIrgPenerbitan>)
				reportIrgPenerbitanService.getAllIrgDataHeader(searchCriteria);
		
		try {
			
			fileOutputStream = new FileOutputStream(filePath + fileName);
			workbook = new XSSFWorkbook();
			XSSFSheet sheet1 = workbook.createSheet(sheetName1);
			
			// setting column size sheet 1
			if (listColumnView != null) {
				for (int i = 0; i < listColumnView.size(); i++) {
					sheet1.setColumnWidth(i, listColumnView.get(i));
					sheet1.autoSizeColumn(i);
				}
			}
			
			Map<String, CellStyle> mapCellFormat = new HashMap<String, CellStyle>();
			getReportUtil().createExcelCellFormat(workbook, mapCellFormat);
			
			this.writeExcelHeader(sheet1, mapCellFormat);
			
			CellStyle cfColumnHeader = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER);			
			
			// sheet 1 header [start]
			int rowIndex = 8;
			if (buildListColumnNameRow1Sheet1 != null && !buildListColumnNameRow1Sheet1.isEmpty()) {
				int columnIndex = 0;
				
				for (String columnNameAlias : buildListColumnNameRow1Sheet1) {
					
					getReportUtil().writeCell(sheet1, rowIndex, columnIndex, columnNameAlias, cfColumnHeader);	
					
					// merged Area
					if (columnIndex == 9) {
						CellRangeAddress cra = new CellRangeAddress(rowIndex,rowIndex,columnIndex,columnIndex+=7);
						sheet1.addMergedRegion(cra);
						reportUtil.mergedHeaderColumnStyle(cra, sheet1);
					} else if (columnIndex == 17) {
						CellRangeAddress cra = new CellRangeAddress(rowIndex,rowIndex,columnIndex,columnIndex+=9);
						sheet1.addMergedRegion(cra);
						reportUtil.mergedHeaderColumnStyle(cra, sheet1);
					} else if (columnIndex == 27) {
						CellRangeAddress cra = new CellRangeAddress(rowIndex,rowIndex,columnIndex,columnIndex+=3);
						sheet1.addMergedRegion(cra);
						reportUtil.mergedHeaderColumnStyle(cra, sheet1);
					} else {
						sheet1.addMergedRegion(new CellRangeAddress(rowIndex,rowIndex+1,columnIndex,columnIndex));
					}
					
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
			// sheet 1 header [end]		
			
			// sheet 1 data [start]			
			if (sheet1Results != null && !sheet1Results.isEmpty()) {
				rowIndex += 1;
				int rowNum = 1;
				
				for (ReportIrgPenerbitan arrObj : sheet1Results) {
			        if(arrObj.getPicTpgReportList().size() >= arrObj.getPicTpkReportList().size()) {
			            int columnIndex = 0;
			            for(ReportIrgPenerbitanTpg arrObjTpg : arrObj.getPicTpgReportList()) {			            	
			            	writeObjectToExcelTpg(sheet1, mapCellFormat, rowIndex, rowNum, arrObj, arrObjTpg, columnIndex);
			                if(arrObj.getPicTpkReportList().size() > columnIndex) {
			                	ReportIrgPenerbitanTpk arrObjTpk = arrObj.getPicTpkReportList().get(columnIndex);
			                	writeObjectToExcelTpk(sheet1, mapCellFormat, rowIndex, rowNum, arrObj, arrObjTpk, columnIndex);
			                }
			                
			                columnIndex++;
			                rowNum++;
			                rowIndex++;
			            }
			        }else {
			            int columnIndex = 0;
			            for(ReportIrgPenerbitanTpk arrObjTpk : arrObj.getPicTpkReportList()) {
			            	writeObjectToExcelTpk(sheet1, mapCellFormat, rowIndex, rowNum, arrObj, arrObjTpk, columnIndex);
			                if(arrObj.getPicTpgReportList().size() > columnIndex) {
			                	ReportIrgPenerbitanTpg arrObjTpg = arrObj.getPicTpgReportList().get(columnIndex);
			                	writeObjectToExcelTpg(sheet1, mapCellFormat, rowIndex, rowNum, arrObj, arrObjTpg, columnIndex);
			                }
			                
			                columnIndex++;
			                rowNum++;          
			                rowIndex++;
			            }
			        }
				}
			}			
			// sheet 1 data [end]			
			
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
			
	private void writeObjectToExcelHeader(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum,
			ReportIrgPenerbitan arrObj, int columnIndex) throws Exception {
		getReportUtil().writeCellDetail(sheet1, rowIndex, 0, rowNum, mapCellFormat);		
		getReportUtil().writeCellDetail(sheet1, rowIndex, 1, arrObj.getReferenceNo(), mapCellFormat);				
		getReportUtil().writeCellDetail(sheet1, rowIndex, 2, arrObj.getIrgTitle(), mapCellFormat);				
		getReportUtil().writeCellDetail(sheet1, rowIndex, 3, arrObj.getRegulationNo(), mapCellFormat);					
		getReportUtil().writeCellDetail(sheet1, rowIndex, 4, arrObj.getRegulationTypeName(), mapCellFormat);							
		getReportUtil().writeCellDetail(sheet1, rowIndex, 5, arrObj.getRegulationInDateStr(), mapCellFormat);							
		getReportUtil().writeCellDetail(sheet1, rowIndex, 6, arrObj.getRegulationStatusName(), mapCellFormat);							
		getReportUtil().writeCellDetail(sheet1, rowIndex, 7, arrObj.getWorkUnitTpgName(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, 8, arrObj.getDirectorateTpg(), mapCellFormat);	
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, 27, arrObj.getPicIrgNik1(), mapCellFormat);	
		getReportUtil().writeCellDetail(sheet1, rowIndex, 28, arrObj.getPicIrgName1(), mapCellFormat);			
		getReportUtil().writeCellDetail(sheet1, rowIndex, 29, arrObj.getPicIrgNik2(), mapCellFormat);							
		getReportUtil().writeCellDetail(sheet1, rowIndex, 30, arrObj.getPicIrgName2(), mapCellFormat);			
		getReportUtil().writeCellDetail(sheet1, rowIndex, 31, arrObj.getFinalIrgDateStr(), mapCellFormat);							
		getReportUtil().writeCellDetail(sheet1, rowIndex, 32, arrObj.getApprovalSpvDateStr(), mapCellFormat);							
		getReportUtil().writeCellDetail(sheet1, rowIndex, 33, arrObj.getApprovalPukDateStr(), mapCellFormat);			
		getReportUtil().writeCellDetail(sheet1, rowIndex, 34, arrObj.getSignOffDateStr(), mapCellFormat);			
		getReportUtil().writeCellDetail(sheet1, rowIndex, 35, arrObj.getProcessStatusName(), mapCellFormat);		
		getReportUtil().writeCellDetail(sheet1, rowIndex, 36, arrObj.getEffectiveDateStr(), mapCellFormat);			
		getReportUtil().writeCellDetail(sheet1, rowIndex, 37, arrObj.getEmailBlastDateStr(), mapCellFormat);			
		getReportUtil().writeCellDetail(sheet1, rowIndex, 38, arrObj.getUploadBlastDateStr(), mapCellFormat);						
		getReportUtil().writeCellDetail(sheet1, rowIndex, 39, arrObj.getRegObsoleteTypeName(), mapCellFormat);							
		getReportUtil().writeCellDetail(sheet1, rowIndex, 40, arrObj.getObsoleteInfo(), mapCellFormat);							
		getReportUtil().writeCellDetail(sheet1, rowIndex, 41, arrObj.getEmailGroupTpk(), mapCellFormat);
		
	}
	
	private void writeObjectToExcelTpg(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum,
			ReportIrgPenerbitan arrObj, ReportIrgPenerbitanTpg arrObjTpg, 
			int columnIndex) throws Exception {
		writeObjectToExcelHeader(sheet1, mapCellFormat, rowIndex, rowNum, arrObj, 0);	
		
		getReportUtil().writeCellDetail(sheet1, rowIndex, 9, arrObjTpg.getDivisionName(), mapCellFormat);			
		getReportUtil().writeCellDetail(sheet1, rowIndex, 10, arrObjTpg.getPicNik1(), mapCellFormat);			
		getReportUtil().writeCellDetail(sheet1, rowIndex, 11, arrObjTpg.getPicName1(), mapCellFormat);			
		getReportUtil().writeCellDetail(sheet1, rowIndex, 12, arrObjTpg.getPicNik2(), mapCellFormat);			
		getReportUtil().writeCellDetail(sheet1, rowIndex, 13, arrObjTpg.getPicName2(), mapCellFormat);					
		getReportUtil().writeCellDetail(sheet1, rowIndex, 14, arrObjTpg.getPicNik3(), mapCellFormat);			
		getReportUtil().writeCellDetail(sheet1, rowIndex, 15, arrObjTpg.getPicName3(), mapCellFormat);					
		getReportUtil().writeCellDetail(sheet1, rowIndex, 16, arrObjTpg.getTargetDateStr(), mapCellFormat);			
	}
	
	private void writeObjectToExcelTpk(XSSFSheet sheet1, Map<String, CellStyle> mapCellFormat, int rowIndex, int rowNum,
			ReportIrgPenerbitan arrObj, ReportIrgPenerbitanTpk arrObjTpk, int columnIndex) throws Exception {		
	
		writeObjectToExcelHeader(sheet1, mapCellFormat, rowIndex, rowNum, arrObj, 0);	
			
		getReportUtil().writeCellDetail(sheet1, rowIndex, 17, arrObjTpk.getReviewApproval(), mapCellFormat);			
		getReportUtil().writeCellDetail(sheet1, rowIndex, 18, arrObjTpk.getDivisionName(), mapCellFormat);					
		getReportUtil().writeCellDetail(sheet1, rowIndex, 19, arrObjTpk.getPicNik1(), mapCellFormat);	
		getReportUtil().writeCellDetail(sheet1, rowIndex, 20, arrObjTpk.getPicName1(), mapCellFormat);			
		getReportUtil().writeCellDetail(sheet1, rowIndex, 21, arrObjTpk.getPicNik2(), mapCellFormat);			
		getReportUtil().writeCellDetail(sheet1, rowIndex, 22, arrObjTpk.getPicName2(), 	mapCellFormat);			
		getReportUtil().writeCellDetail(sheet1, rowIndex, 23, arrObjTpk.getPicNik3(), mapCellFormat);			
		getReportUtil().writeCellDetail(sheet1, rowIndex, 24, arrObjTpk.getPicName3(), mapCellFormat);			
		getReportUtil().writeCellDetail(sheet1, rowIndex, 25, arrObjTpk.getTargetDateStr(), mapCellFormat);	
		getReportUtil().writeCellDetail(sheet1, rowIndex, 26, arrObjTpk.getNote(), mapCellFormat);
		getReportUtil().writeCellDetail(sheet1, rowIndex, 42, arrObjTpk.getReminderDateH10(), mapCellFormat);
		
		try {
			if (StringUtils.isNotBlank(arrObjTpk.getReminderDateH10())) {
				ParameterDetail pdReminderH5 = parameterDetailService.getParameterDetailByParamDtlCode("IRG_PENERBITAN_REMAIN");
				SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy");
				Calendar calendar = Calendar.getInstance();
				Date targetDateTmp = sdf2.parse(arrObjTpk.getReminderDateH10());
				int counterDate = 0;
				
				/*Apparently we need this code snippet to make sure when this data is created on Weekend, 
					the due date is place correctly. Else it will be increase by 1 day */
				int checkDay = calendar.get(Calendar.DAY_OF_WEEK);
				if (checkDay == 1 || checkDay == 7){
					counterDate +=1;
				}
				
				while (counterDate < Integer.parseInt(pdReminderH5.getNameIn())) {
					int day = calendar.get(Calendar.DAY_OF_WEEK);
					if (day == 1 || day == 7) {
						
					} else {
						if (Boolean.TRUE.equals(holidayService.isAvailableDate(targetDateTmp))) {
							counterDate++;
						}
					}
					
					if (counterDate < Integer.parseInt(pdReminderH5.getNameIn())) {
						calendar.setTime(targetDateTmp);
						calendar.add(Calendar.DAY_OF_MONTH, 1);
						targetDateTmp = calendar.getTime();
					}
				}
				getReportUtil().writeCellDetail(sheet1, rowIndex, 43, sdf2.format(targetDateTmp), mapCellFormat);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private List<String> buildListColumnNameRow1Sheet1() {
		List<String> listColumnNameTemp = new ArrayList<String>();
		listColumnNameTemp.add("No");
		listColumnNameTemp.add("No. Referensi");
		listColumnNameTemp.add("Judul Regulasi");
		listColumnNameTemp.add("No. Regulasi");
		listColumnNameTemp.add("Tipe Regulasi");
		listColumnNameTemp.add("Tanggal Masuk Regulasi");		
		listColumnNameTemp.add("Status Regulasi");
		listColumnNameTemp.add("Unit Kerja TPG");
		listColumnNameTemp.add("Direktorat TPG");
		listColumnNameTemp.add("PIC TPG");
		listColumnNameTemp.add("PIC TPK");
		listColumnNameTemp.add("PIC IRG");		
		listColumnNameTemp.add("Target Final IRG");
		listColumnNameTemp.add("Tanggal Approval SPV");
		listColumnNameTemp.add("Tanggal  Approval PUK");
		listColumnNameTemp.add("Tanggal Sign Off");
		listColumnNameTemp.add("Status Process");
		listColumnNameTemp.add("Tangggal Efektif");		
		listColumnNameTemp.add("Tanggal Email Blast");	
		listColumnNameTemp.add("Tanggal Upload Blast");			
		listColumnNameTemp.add("Tipe Regulasi Obsolete");	
		listColumnNameTemp.add("Info Obsolete");	
		listColumnNameTemp.add("Group Email TPK");
		listColumnNameTemp.add("Tanggal Due Date (H+10)");
		listColumnNameTemp.add("Tanggal Due Date (H+5)");
		
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
		listColumnNameTemp.add("Divisi");		
		listColumnNameTemp.add("NIK 1");
		listColumnNameTemp.add("Nama 1");
		listColumnNameTemp.add("NIK 2");		
		listColumnNameTemp.add("Nama 2");
		listColumnNameTemp.add("NIK 3");
		listColumnNameTemp.add("Nama 3");		
		listColumnNameTemp.add("Target Waktu");
		listColumnNameTemp.add("Review dan Approval");
		listColumnNameTemp.add("Divisi");		
		listColumnNameTemp.add("NIK 1");
		listColumnNameTemp.add("Nama 1");
		listColumnNameTemp.add("NIK 2");		
		listColumnNameTemp.add("Nama 2");
		listColumnNameTemp.add("NIK 3");
		listColumnNameTemp.add("Nama 3");		
		listColumnNameTemp.add("Target Waktu");
		listColumnNameTemp.add("Catatan");
		listColumnNameTemp.add("NIK 1");
		listColumnNameTemp.add("Nama 1");
		listColumnNameTemp.add("NIK 2");		
		listColumnNameTemp.add("Nama 2");	
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
		listColumnNameTemp.add("");	
		listColumnNameTemp.add("");	
		
		return listColumnNameTemp;
	}
	
	public void writeExcelHeader(XSSFSheet sheet,Map<String, CellStyle> mapCellFormat) throws Exception {		
		String dateFrom = "";
		String dateTo = "";
		String regulationTypeName = "";
		String unitKerjaTpgName = "";
		
		int row = 0;
		int columnStart = 0;
		
		SimpleDateFormat printTimeStamp = new SimpleDateFormat(CommonConstants.INPUT_DATE_TIME_FORMAT);
		String excelPrintDate = printTimeStamp.format(new Date());
		
		// Create Header [Start]
		CellStyle cfHeaderValue = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_VALUE);
		CellStyle cfHeaderLabel = mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_LABEL);
		// Create Header [End]
		
		// Create Header Title [Start]
		reportUtil.writeCellTitle(sheet, 4, 
				"Report Internal Regulation Penerbitan", mapCellFormat);
				
		row++;
		row++;
				
		// Label Creation Date [Start]
		reportUtil.writeCell(sheet, row, columnStart, runnableFacesUtil.retrieveMessage("formReportIrgPenerbitanCreationDate") + " : ", cfHeaderLabel);
		// Label Creation Date [End]
		
		searchDateFrom = getSearchCriteriaValue(WHERE_CREATED_DATE_FROM);
		searchDateTo = getSearchCriteriaValue(WHERE_CREATED_DATE_TO);
		searchRegulationType = getSearchCriteriaValue(WHERE_REGULATION_TYPE);
		searchUnitKerjaTpg = getSearchCriteriaValue(WHERE_UNIT_KERJA_TPG);
		
		// get Name EN and In
		ParameterDetail getRegulationType = parameterDetailService.getParameterDetailByParamDtlCode(searchRegulationType);
		if (getRegulationType != null) {
			regulationTypeName = runnableFacesUtil.retrieveLocaleMessage(getRegulationType.getNameEn(), getRegulationType.getNameIn());
		}
	
		unitKerjaTpgName = searchUnitKerjaTpg;
		
		if (StringUtils.isNotBlank(searchDateFrom)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchDateFrom);
				dateFrom = DateUtil.dateToString(tmpDate);
			} catch (Exception e) {
				dateFrom = "";
			}
		}
		
		if (StringUtils.isNotBlank(searchDateTo)) {
			try {
				Date tmpDate = DateUtil.stringToDateFromYYYYMMDD(searchDateTo);
				dateTo = DateUtil.dateToString(tmpDate);
			} catch (Exception e) {
				dateTo = "";
			}
		}
						
		reportUtil.writeCell(sheet, row, columnStart + 1, 
				dateFrom + " "
				+ ((StringUtils.isNotBlank(searchDateFrom) && StringUtils.isNotBlank(searchDateTo))
						? runnableFacesUtil.retrieveMessage("textUntil") : "")
				+ " " + dateTo, 
				cfHeaderValue);
		row++;
		
		// Label Regulation Type
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportIrgPenerbitanRegulationType") + " : ",
				cfHeaderLabel);
		
		// Value Regulation Type
		reportUtil.writeCell(sheet, row, columnStart + 1, 
				regulationTypeName,
				cfHeaderValue);
		
		row++;
		
		// Label Work Unit Tpg
		reportUtil.writeCell(sheet, row, columnStart,
				runnableFacesUtil.retrieveMessage("formReportIrgPenerbitanWorkUnitTpg") + " : ",
				cfHeaderLabel);
				
		// Value Work Unit Tpg
		reportUtil.writeCell(sheet, row, columnStart + 1, 
				unitKerjaTpgName,
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

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		ReportIrgPenerbitanTask.logger = logger;
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
	
	public ReportIrgPenerbitanService getReportIrgPenerbitanService() {
		return reportIrgPenerbitanService;
	}

	public void setReportIrgPenerbitanService(ReportIrgPenerbitanService reportIrgPenerbitanService) {
		this.reportIrgPenerbitanService = reportIrgPenerbitanService;
	}

	public static String getComplianceDocTypeReportIrgPenerbitan() {
		return COMPLIANCE_DOC_TYPE_REPORT_IRG_PENERBITAN;
	}

	@SuppressWarnings("rawtypes")
	public List<? extends SearchObject> getSearchCriteria() {
		return searchCriteria;
	}

	@SuppressWarnings("rawtypes")
	public void setSearchCriteria(List<? extends SearchObject> searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public String getSearchDateFrom() {
		return searchDateFrom;
	}

	public void setSearchDateFrom(String searchDateFrom) {
		this.searchDateFrom = searchDateFrom;
	}

	public String getSearchDateTo() {
		return searchDateTo;
	}

	public void setSearchDateTo(String searchDateTo) {
		this.searchDateTo = searchDateTo;
	}

	public CommonReportUtil getReportUtil() {
		return reportUtil;
	}

	public void setReportUtil(CommonReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getSearchRegulationType() {
		return searchRegulationType;
	}

	public void setSearchRegulationType(String searchRegulationType) {
		this.searchRegulationType = searchRegulationType;
	}

	public String getSearchUnitKerjaTpg() {
		return searchUnitKerjaTpg;
	}

	public void setSearchUnitKerjaTpg(String searchUnitKerjaTpg) {
		this.searchUnitKerjaTpg = searchUnitKerjaTpg;
	}

	public HolidayService getHolidayService() {
		return holidayService;
	}

	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}

}
