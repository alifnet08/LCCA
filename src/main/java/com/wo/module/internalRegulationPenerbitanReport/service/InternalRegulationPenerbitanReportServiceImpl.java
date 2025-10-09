package com.wo.module.internalRegulationPenerbitanReport.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.holiday.dao.HolidayDao;
import com.wo.module.internalRegulationPenerbitanReport.constant.InternalRegulationPenerbitanReportConstants;
import com.wo.module.internalRegulationPenerbitanReport.dao.InternalRegulationPenerbitanReportDao;
import com.wo.module.internalRegulationPenerbitanReport.model.InternalRegulationPenerbitanReport;
import com.wo.module.internalRegulationPenerbitanReport.vo.InternalRegulationPenerbitanPicIrgReportVo;
import com.wo.module.internalRegulationPenerbitanReport.vo.InternalRegulationPenerbitanPicTpgReportVo;
import com.wo.module.internalRegulationPenerbitanReport.vo.InternalRegulationPenerbitanPicTpkReportVo;
import com.wo.module.internalRegulationPenerbitanReport.vo.InternalRegulationPenerbitanReportVo;
import com.wo.module.parameter.dao.ParameterDetailDao;
import com.wo.module.parameter.model.ParameterDetail;

@Transactional
@Service("internalRegulationPenerbitanReportService")
public class InternalRegulationPenerbitanReportServiceImpl implements InternalRegulationPenerbitanReportService {
	    
    @Autowired
    @Qualifier("internalRegulationPenerbitanReportDao")
    private InternalRegulationPenerbitanReportDao internalRegulationPenerbitanReportDao;
    
    @Autowired
    @Qualifier("holidayDao")
    private HolidayDao holidayDao;
    
    @Autowired
    @Qualifier("parameterDetailDao")
    private ParameterDetailDao parameterDetailDao;
   
	public InternalRegulationPenerbitanReportDao getInternalRegulationPenerbitanReportDao() {
		return internalRegulationPenerbitanReportDao;
	}

	public void setInternalRegulationPenerbitanReportDao(
			InternalRegulationPenerbitanReportDao internalRegulationPenerbitanReportDao) {
		this.internalRegulationPenerbitanReportDao = internalRegulationPenerbitanReportDao;
	}

	public HolidayDao getHolidayDao() {
		return holidayDao;
	}

	public void setHolidayDao(HolidayDao holidayDao) {
		this.holidayDao = holidayDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<InternalRegulationPenerbitanReportVo> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return internalRegulationPenerbitanReportDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return internalRegulationPenerbitanReportDao.searchCountData(searchCriteria);
	}
	
	@Override
	public StreamedContent generateDataExcel(InternalRegulationPenerbitanReport irgDataReport) throws Exception {
		Workbook wb = null;
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;
		DefaultStreamedContent streamContent = null;
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(InternalRegulationPenerbitanReportConstants.TEMPLATE_REPORT);
	
		try {
			wb = new XSSFWorkbook(is);
		} catch (IOException e) {
			//logger.error("Error while trying to read template file", e);
			return null;
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// do nothing
			}
		}

		Sheet sheet = wb.getSheet(InternalRegulationPenerbitanReportConstants.REPORT_SHEET_NAME);
		
        List<InternalRegulationPenerbitanReportVo> dataList = internalRegulationPenerbitanReportDao.getAllIrgDataHeader(irgDataReport);
		this.templateFileWriteData2(wb, sheet, dataList);
				
		try {
			baos = new ByteArrayOutputStream();						 
			wb.write(baos);
		} catch (IOException e) {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e2) {
					// do nothing
				}
			}
			//logger.error("Error while trying to write to mem output stream", e);
			return null;
		}

		try {
			bais = new ByteArrayInputStream(baos.toByteArray());
			SimpleDateFormat sdfTemp = new SimpleDateFormat("yyyymmddHHmmss");
			String fileName = InternalRegulationPenerbitanReportConstants.REPORT_FILE_NAME + "_";
			fileName = fileName + sdfTemp.format(new Date()) + ".xlsx";
			streamContent = new DefaultStreamedContent(bais, "application/xls", fileName);
		} catch (Exception e) {
			//logger.error("Error while trying to create stream content", e);
			e.printStackTrace();
		} finally {
			try {
				baos.close();
				bais.close();
			} catch (IOException e) {
				// do nothing
			}
		}

		return streamContent;
	}
	
	private void templateFileWriteData2(Workbook wb, Sheet sheet, List<InternalRegulationPenerbitanReportVo> dataList) {
        Row row = null;
        Cell cell = null;
        
		int rowNum =  InternalRegulationPenerbitanReportConstants.EXCEL_ROW_IDX_GRID_START_DATA;		
		for (InternalRegulationPenerbitanReportVo irgReport : dataList) {
	        if(irgReport.getPicTpgReportList().size() >= irgReport.getPicTpkReportList().size()) {
	            int indexDtl = 0;
	            for(InternalRegulationPenerbitanPicTpgReportVo irgPicTpg : irgReport.getPicTpgReportList()) {
	            	if (rowNum == InternalRegulationPenerbitanReportConstants.EXCEL_ROW_IDX_GRID_START_DATA) {
	    				row = sheet.getRow(rowNum);
	    			} else {
	    				row = FileUtil.excelCopyRow(sheet, InternalRegulationPenerbitanReportConstants.EXCEL_ROW_IDX_GRID_START_DATA, rowNum, true);
	    			}
	                getCellDataDetailPicTpg(row, cell, wb, irgReport, irgPicTpg);
	                if(irgReport.getPicTpkReportList().size() > indexDtl) {
	                    InternalRegulationPenerbitanPicTpkReportVo irgPicTpk = irgReport.getPicTpkReportList().get(indexDtl);
	                    getCellDataDetailPicTpk(row, cell, wb, irgReport, irgPicTpk);
	                }
	                
	                indexDtl++;
	                rowNum++;
	            }
	        }else {
	            int indexDtl = 0;
	            for(InternalRegulationPenerbitanPicTpkReportVo irgPicTpg : irgReport.getPicTpkReportList()) {
	            	if (rowNum == InternalRegulationPenerbitanReportConstants.EXCEL_ROW_IDX_GRID_START_DATA) {
	    				row = sheet.getRow(rowNum);
	    			} else {
	    				row = FileUtil.excelCopyRow(sheet, InternalRegulationPenerbitanReportConstants.EXCEL_ROW_IDX_GRID_START_DATA, rowNum, true);
	    			}
	                getCellDataDetailPicTpk(row, cell, wb, irgReport, irgPicTpg);
	                if(irgReport.getPicTpgReportList().size() > indexDtl) {
	                        InternalRegulationPenerbitanPicTpgReportVo irgPicTpk = irgReport.getPicTpgReportList().get(indexDtl);
	                        getCellDataDetailPicTpg(row, cell, wb, irgReport, irgPicTpk);
	                }
	                
	                indexDtl++;
	                rowNum++;                    
	            }
	        }
		}
	}
		
	private static CellStyle buildStyleForDataLeft(Workbook wb) {
		CellStyle headerStyle = wb.createCellStyle();
		headerStyle.setWrapText(true); 
		headerStyle.setAlignment(HorizontalAlignment.LEFT);
	    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	    headerStyle.setBorderTop(BorderStyle.THIN);
	    headerStyle.setBorderBottom(BorderStyle.THIN);
	    headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);
		return headerStyle;
	}
	
	private static CellStyle buildStyleForDataCenter(Workbook wb) {
		CellStyle headerStyle = wb.createCellStyle();
		headerStyle.setWrapText(true); 
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
	    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	    headerStyle.setBorderTop(BorderStyle.THIN);
	    headerStyle.setBorderBottom(BorderStyle.THIN);
	    headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);		
		return headerStyle;
	}
	
	private void getCellDataReport(Row row, Cell cell, Workbook wb,
			InternalRegulationPenerbitanReportVo irgReport) {
		
		// EXCEL_COL_IDX_REFRENCE_NO = 0;
		cell = row.createCell(0);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgReport.getReferenceNo());
								
		// EXCEL_COL_IDX_IRG_TITLE = 1;
		cell = row.createCell(1);
		cell.setCellStyle(buildStyleForDataLeft(wb));
		cell.setCellValue(irgReport.getIrgTitle());
		
		// EXCEL_COL_IDX_REGULATION_NO = 2;
		cell = row.createCell(2);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgReport.getRegulationNo());
		
		// EXCEL_COL_IDX_REGULATION_TYPE = 3;
		cell = row.createCell(3);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgReport.getRegulationTypeName());
		
		// EXCEL_COL_IDX_REGULATION_IN_DATE = 4;
		cell = row.createCell(4);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgReport.getRegulationInDateStr());
		
		// EXCEL_COL_IDX_REGULATION_STATUS = 5;
		cell = row.createCell(5);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgReport.getRegulationStatusName());
		
		// EXCEL_COL_IDX_WORK_UNIT_TPG = 6;
		cell = row.createCell(6);
		cell.setCellStyle(buildStyleForDataLeft(wb));
		cell.setCellValue(irgReport.getWorkUnitTpgName());
		
		// EXCEL_COL_IDX_DIRECTORATE_TPG = 7;
		cell = row.createCell(7);
		cell.setCellStyle(buildStyleForDataLeft(wb));
		cell.setCellValue(irgReport.getDirectorateTpg());
				
		// EXCEL_COL_IDX_PIC_IRG_NIK_1 = 26;
		cell = row.createCell(26);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgReport.getPicIrgNik1());
		
		// EXCEL_COL_IDX_PIC_IRG_NAME_1 = 27;
		cell = row.createCell(27);
		cell.setCellStyle(buildStyleForDataLeft(wb));
		cell.setCellValue(irgReport.getPicIrgName1());
		
		// EXCEL_COL_IDX_PIC_IRG_NIK_2 = 28;
		cell = row.createCell(28);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgReport.getPicIrgNik2());
		
		// EXCEL_COL_IDX_PIC_IRG_NAME_2 = 29;
		cell = row.createCell(29);
		cell.setCellStyle(buildStyleForDataLeft(wb));
		cell.setCellValue(irgReport.getPicIrgName2());
		
		// EXCEL_COL_IDX_FINAL_IRG_DATE = 30;
		cell = row.createCell(30);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgReport.getFinalIrgDateStr());
		
		// EXCEL_COL_IDX_APPROVAL_SPV_DATE = 31;
		cell = row.createCell(31);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgReport.getApprovalSpvDateStr());
		
		// EXCEL_COL_IDX_APPROVAL_PUK_DATE = 32;
		cell = row.createCell(32);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgReport.getApprovalPukDateStr());
		
		// EXCEL_COL_IDX_SIGN_OFF_DATE = 33;
		cell = row.createCell(33);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgReport.getSignOffDateStr());
		
		// EXCEL_COL_IDX_PROCESS_STATUS = 34;
		cell = row.createCell(34);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgReport.getProcessStatusName());
		
		// EXCEL_COL_IDX_EFFECTIVE_DATE = 35;
		cell = row.createCell(35);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgReport.getEffectiveDateStr());
		
		// EXCEL_COL_IDX_EMAIL_BLAST_DATE = 36;
		cell = row.createCell(36);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgReport.getEmailBlastDateStr());
		
		// EXCEL_COL_IDX_UPLOAD_BLAST_DATE = 37;
		cell = row.createCell(37);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgReport.getUploadBlastDateStr());
		
		// EXCEL_COL_IDX_OBSOLETE_TYPE = 38;
		cell = row.createCell(38);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgReport.getRegObsoleteTypeName());
		
		// EXCEL_COL_IDX_OBSOLETE_INFO = 39;
		cell = row.createCell(39);
		cell.setCellStyle(buildStyleForDataLeft(wb));
		cell.setCellValue(irgReport.getObsoleteInfo());
		
		// EXCEL_COL_IDX_EMAIL_GROUP_TPK = 40;
		cell = row.createCell(40);
		cell.setCellStyle(buildStyleForDataLeft(wb));
		cell.setCellValue(irgReport.getEmailGroupTpk());
	}
	
	private void getCellDataDetailPicTpg(Row rowDtl, Cell cell, Workbook wb,
			InternalRegulationPenerbitanReportVo irgReportVo, InternalRegulationPenerbitanPicTpgReportVo irgPicTpg) {
		getCellDataReport(rowDtl, cell, wb, irgReportVo);		
		// EXCEL_COL_IDX_PIC_TPG_DIVISION = 8;
		cell = rowDtl.createCell(8);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgPicTpg.getDivisionName());
		
		// EXCEL_COL_IDX_PIC_TPG_NIK_1 = 9;
		cell = rowDtl.createCell(9);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgPicTpg.getPicNik1());
		
		// EXCEL_COL_IDX_PIC_TPG_NAME_1 = 10;
		cell = rowDtl.createCell(10);
		cell.setCellStyle(buildStyleForDataLeft(wb));
		cell.setCellValue(irgPicTpg.getPicName1());
		
		// EXCEL_COL_IDX_PIC_TPG_NIK_2 = 11;
		cell = rowDtl.createCell(11);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgPicTpg.getPicNik2());
		
		// EXCEL_COL_IDX_PIC_TPG_NAME_2 = 12;
		cell = rowDtl.createCell(12);
		cell.setCellStyle(buildStyleForDataLeft(wb));
		cell.setCellValue(irgPicTpg.getPicName2());
		
		// EXCEL_COL_IDX_PIC_TPG_NIK_3 = 13;
		cell = rowDtl.createCell(13);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgPicTpg.getPicNik3());
		
		// EXCEL_COL_IDX_PIC_TPG_NAME_3 = 14;
		cell = rowDtl.createCell(14);
		cell.setCellStyle(buildStyleForDataLeft(wb));
		cell.setCellValue(irgPicTpg.getPicName3());
		
		// EXCEL_COL_IDX_PIC_TPG_TARGET_WAKTU = 15;
		cell = rowDtl.createCell(15);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgPicTpg.getTargetDateStr());
	}
	
	private void getCellDataDetailPicTpk(Row rowDtl, Cell cell, Workbook wb, InternalRegulationPenerbitanReportVo irgReportVo, 
			InternalRegulationPenerbitanPicTpkReportVo irgPicTpk) {
		getCellDataReport(rowDtl, cell, wb, irgReportVo);		
		// EXCEL_COL_IDX_PIC_TPK_REVIEW_APPROVAL = 16;
		cell = rowDtl.createCell(16);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgPicTpk.getReviewApproval());
		
		// EXCEL_COL_IDX_PIC_TPK_DIVISION = 17;
		cell = rowDtl.createCell(17);
		cell.setCellStyle(buildStyleForDataLeft(wb));
		cell.setCellValue(irgPicTpk.getDivisionName());
		
		// EXCEL_COL_IDX_PIC_TPK_NIK_1 = 18;
		cell = rowDtl.createCell(18);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgPicTpk.getPicNik1());

		// EXCEL_COL_IDX_PIC_TPK_NAME_1 = 19;
		cell = rowDtl.createCell(19);
		cell.setCellStyle(buildStyleForDataLeft(wb));
		cell.setCellValue(irgPicTpk.getPicName1());

		// EXCEL_COL_IDX_PIC_TPK_NIK_2 = 20;
		cell = rowDtl.createCell(20);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgPicTpk.getPicNik2());

		// EXCEL_COL_IDX_PIC_TPK_NAME_2 = 21;
		cell = rowDtl.createCell(21);
		cell.setCellStyle(buildStyleForDataLeft(wb));
		cell.setCellValue(irgPicTpk.getPicName2());

		// EXCEL_COL_IDX_PIC_TPK_NIK_3 = 22;
		cell = rowDtl.createCell(22);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgPicTpk.getPicNik3());

		// EXCEL_COL_IDX_PIC_TPK_NAME_3 = 23;
		cell = rowDtl.createCell(23);
		cell.setCellStyle(buildStyleForDataLeft(wb));
		cell.setCellValue(irgPicTpk.getPicName3());

		// EXCEL_COL_IDX_PIC_TPK_TARGET_WAKTU = 24;
		cell = rowDtl.createCell(24);
		cell.setCellStyle(buildStyleForDataCenter(wb));
		cell.setCellValue(irgPicTpk.getTargetDateStr());

		// EXCEL_COL_IDX_PIC_TPK_NOTE = 25;
		cell = rowDtl.createCell(25);
		cell.setCellStyle(buildStyleForDataLeft(wb));
		cell.setCellValue(irgPicTpk.getNote()); 
		
		// EXCEL_COL_IDX_REMINDER_DATE_H10 = 41;
		cell = rowDtl.createCell(41);
		cell.setCellStyle(buildStyleForDataLeft(wb));
		cell.setCellValue(irgPicTpk.getReminderDateH10());
		
		// EXCEL_COL_IDX_REMINDER_DATE_H5 = 42;
		cell = rowDtl.createCell(42);
		cell.setCellStyle(buildStyleForDataLeft(wb));
		try {
			if (StringUtils.isNotBlank(irgPicTpk.getReminderDateH10())) {
				ParameterDetail pdReminderH5 = parameterDetailDao.getParameterDetailByParamDtlCode("IRG_PENERBITAN_REMAIN");
				SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy");
				Calendar calendar = Calendar.getInstance();
				Date targetDateTmp = sdf2.parse(irgPicTpk.getReminderDateH10());
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
						if (Boolean.TRUE.equals(holidayDao.isAvailableDate(targetDateTmp))) {
							counterDate++;
						}
					}
					
					if (counterDate < Integer.parseInt(pdReminderH5.getNameIn())) {
						calendar.setTime(targetDateTmp);
						calendar.add(Calendar.DAY_OF_MONTH, 1);
						targetDateTmp = calendar.getTime();
					}
				}
				cell.setCellValue(sdf2.format(targetDateTmp));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public List<InternalRegulationPenerbitanPicIrgReportVo> getDataPicIrg() {
		return internalRegulationPenerbitanReportDao.getDataPicIrg();
	}

	public ParameterDetailDao getParameterDetailDao() {
		return parameterDetailDao;
	}

	public void setParameterDetailDao(ParameterDetailDao parameterDetailDao) {
		this.parameterDetailDao = parameterDetailDao;
	}

}
