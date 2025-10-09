package com.wo.module.cpsaApprovalFE.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.cpsa.constant.CompliancePlanSelfAssessmentConstant;
import com.wo.module.cpsa.dao.CompliancePlanSelfAssessmentPicDao;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessment;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessmentPic;
import com.wo.module.cpsaApprovalFE.dao.CpsaApprovalFEDao;
import com.wo.module.cpsaApprovalFE.vo.CpsaApprovalFEVo;
import com.wo.module.cpsaApprovalFE.vo.CpsaApprovalQuestionFEVo;
import com.wo.module.cpsaFE.constant.CompliancePlanSelfAssessmentFEConstant;
import com.wo.module.emailTemplate.dao.EmailTemplateDao;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.parameter.dao.ParameterDetailDao;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.user.dao.UserDao;
import com.wo.module.user.model.User;

@Transactional
@Service("cpsaApprovalFEService")
public class CpsaApprovalFEServiceImpl implements CpsaApprovalFEService, Serializable{

	private static final long serialVersionUID = -3390592911825112010L;

	@Autowired
	@Qualifier("cpsaApprovalFEDao")
	private CpsaApprovalFEDao cpsaApprovalFEDao;
	
	@Autowired
	@Qualifier("compliancePlanSelfAssessmentPicDao")
	private CompliancePlanSelfAssessmentPicDao compliancePlanSelfAssessmentPicDao;
	
	@Autowired
	@Qualifier("parameterDetailDao")
	private ParameterDetailDao parameterDetailDao;
	
	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;
	
	@Autowired
	@Qualifier("emailTemplateDao")
	private EmailTemplateDao emailTemplateDao;
	
	@Autowired
	@Qualifier("parameterDetailService")
	private ParameterDetailService parameterDetailService;
	
	public CpsaApprovalFEDao getCpsaApprovalFEDao() {
		return cpsaApprovalFEDao;
	}

	public void setCpsaApprovalFEDao(CpsaApprovalFEDao cpsaApprovalFEDao) {
		this.cpsaApprovalFEDao = cpsaApprovalFEDao;
	}

	public ParameterDetailDao getParameterDetailDao() {
		return parameterDetailDao;
	}

	public void setParameterDetailDao(ParameterDetailDao parameterDetailDao) {
		this.parameterDetailDao = parameterDetailDao;
	}

	public CompliancePlanSelfAssessmentPicDao getCompliancePlanSelfAssessmentPicDao() {
		return compliancePlanSelfAssessmentPicDao;
	}

	public void setCompliancePlanSelfAssessmentPicDao(
			CompliancePlanSelfAssessmentPicDao compliancePlanSelfAssessmentPicDao) {
		this.compliancePlanSelfAssessmentPicDao = compliancePlanSelfAssessmentPicDao;
	}
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<CpsaApprovalFEVo> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return cpsaApprovalFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return cpsaApprovalFEDao.searchCountData(searchCriteria);
	}

	@Override
	public CompliancePlanSelfAssessment findById(long cpsaId) {
		return cpsaApprovalFEDao.getById(cpsaId);
	}
	
	@Override
	public CpsaApprovalFEVo getDataCpsaPic(Long cpsaId, Long userId1, Long userId2) throws Exception {		
		return cpsaApprovalFEDao.getDataCpsaPic(cpsaId, userId1, userId2);
	}

	@Override
	public List<CpsaApprovalQuestionFEVo> getDataQuestion(Long cpsaId, Long userId1) throws Exception {
		List<CpsaApprovalQuestionFEVo> questDtlList = cpsaApprovalFEDao.getDataQuestionDetail(cpsaId, null, userId1);
		
		return questDtlList;				
	}
	
	@SuppressWarnings({ "deprecation"})
	public void approval(CpsaApprovalFEVo cpsaVo, String userLogin)  throws Exception {
		   CompliancePlanSelfAssessmentPic cpsaPic = new CompliancePlanSelfAssessmentPic();
		   cpsaPic = compliancePlanSelfAssessmentPicDao.findById(cpsaVo.getCpsaQuestId());
		   		   		   
		   cpsaPic.setLastUpdateBy(userLogin);
		   cpsaPic.setLastUpdateDate(new Timestamp(new Date().getTime()));
		   cpsaPic.setDelId(new Long(0));
		   cpsaPic.setEnabledFlag(Constants.CONSTANT_YES);
		   cpsaPic.setStatusPic(parameterDetailDao.getParameterDetailByParamDtlCode(CompliancePlanSelfAssessmentFEConstant.STATUS_CPSA_APPROVED));
		   cpsaPic.setNote(cpsaVo.getNote());
		   cpsaPic.setApprovalDate(new Date());
		   cpsaPic.setUserApproval(userDao.getUserByNik(userLogin));
		   // to do
		   //send email pic dan admin create
		   try {
				sendEmail(cpsaPic, CompliancePlanSelfAssessmentFEConstant.STATUS_CPSA_APPROVED);
			} catch (Exception e) {
				e.printStackTrace();
			}
		   
		   compliancePlanSelfAssessmentPicDao.update(cpsaPic);
	}
	
	@SuppressWarnings({ "deprecation"})
	public void reject(CpsaApprovalFEVo cpsaVo, String userLogin)  throws Exception {
		CompliancePlanSelfAssessmentPic cpsaPic = new CompliancePlanSelfAssessmentPic();
		cpsaPic = compliancePlanSelfAssessmentPicDao.findById(cpsaVo.getCpsaQuestId());
		   		   		   
		cpsaPic.setLastUpdateBy(userLogin);
		cpsaPic.setLastUpdateDate(new Timestamp(new Date().getTime()));
		cpsaPic.setDelId(new Long(0));
		cpsaPic.setEnabledFlag(Constants.CONSTANT_YES);
		cpsaPic.setStatusPic(parameterDetailDao.getParameterDetailByParamDtlCode(CompliancePlanSelfAssessmentFEConstant.STATUS_CPSA_REJECTED));
		cpsaPic.setNote(cpsaVo.getNote());
		// to do
		//send email pic
		try {
			sendEmail(cpsaPic, CompliancePlanSelfAssessmentFEConstant.STATUS_CPSA_REJECTED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		   
		compliancePlanSelfAssessmentPicDao.update(cpsaPic);
	}
	
	private void sendEmail(CompliancePlanSelfAssessmentPic cpsaPic, String cpsaStatus) {
		try {
			EmailTemplate emailTemplate = new EmailTemplate();
			ParameterDetail pdHostName = parameterDetailDao.getParameterDetailByParamDtlCode(Constants.HOST_NAME_APPLICATION);
			String emailType = "";
			String emailSubject = "";
			String emailContent = "";
			String emailCc = "";
			
			String token = Constants.encryptString(cpsaPic.getCpsa().getCpsaId().toString());
			String menuId = Constants.encryptString(Constants.MENU_ID_CPAS_FE);
			String urlLink = pdHostName.getNameIn().concat("pages/cpsaFE/compliancePlanSelfAssessmentFEEdit.faces?token="+token+"&menuId="+menuId);
			
			if (cpsaStatus.equalsIgnoreCase(CompliancePlanSelfAssessmentFEConstant.STATUS_CPSA_REJECTED)) {
				emailTemplate = emailTemplateDao.getEmailTemplateByEmailTemplateCode("EMAIL_CPSA_REJECTED");
				emailType = "EMAIL_CPSA_REJECTED";
				
				emailSubject = emailTemplate.getEmailSubject().replaceAll("perihal_surat", cpsaPic.getCpsa().getLetterAbout());
				emailContent = emailTemplate.getEmailContent().replaceAll("nama_pic", cpsaPic.getUser1().getName());
				emailContent = emailContent.replaceAll("url_link", urlLink);
				
				User user = userDao.getUserByNik(cpsaPic.getCreatedBy());
				
				emailCc = user.getEmail();
			} else {
//				User user = new User();
//				
//				if (StringUtils.isNotEmpty(cpsaPic.getLastUpdateBy())) {
//					user = userDao.getUserByNik(cpsaPic.getLastUpdateBy());
//				} else {
//					user = userDao.getUserByNik(cpsaPic.getCreatedBy());
//				}
				User user = userDao.getUserByNik(cpsaPic.getCreatedBy());
				
				emailCc = user.getEmail();
				
				if(cpsaPic.getUser3() != null) {
					emailCc = emailCc+","+cpsaPic.getUser3().getEmail();
				}
				
				emailTemplate = emailTemplateDao.getEmailTemplateByEmailTemplateCode("EMAIL_CPSA_APPROVED");
				emailType = "EMAIL_CPSA_APPROVED";
				
				emailSubject = emailTemplate.getEmailSubject().replaceAll("perihal_surat", cpsaPic.getCpsa().getLetterAbout());
				emailContent = emailTemplate.getEmailContent().replaceAll("nama_pic", cpsaPic.getUser1().getName());
			}
			
			if (cpsaPic != null) {
				String emailTo = "";
				
				emailTo = cpsaPic.getUser1().getEmail();
				
				if(cpsaPic.getUser2() != null) {
					if(emailCc.isBlank()) {
						emailCc = cpsaPic.getUser2().getEmail();
					}else {
						emailCc = emailCc+","+cpsaPic.getUser2().getEmail();
					}
				}
				
				final String subject = emailSubject;
				final String content = emailContent;
				final String to = emailTo;
				final String cc = emailCc;
				
				if (StringUtils.isNotBlank(to)) {
					CallApiManager.sendEmailAPI(to,cc, subject, content, emailType, "true", parameterDetailService);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomAPIException(e.getMessage()); 
		}
	}
	
	@Override
	public StreamedContent generateDataExcel(Long cpsaId, Long userId1, Long userApprovalId) throws Exception {
		Workbook wb = null;
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;
		DefaultStreamedContent streamContent = null;
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(CompliancePlanSelfAssessmentConstant.TEMPLATE_FILE_PATH);
		//InputStream is = new FileInputStream(CompliancePlanSelfAssessmentConstant.TEMPLATE_FILE_PATH);
		
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

		Sheet sheet = wb.getSheet("Kertas Kerja");
		Row row = null;
		Cell cell = null;
		int rowNum = 14;
		int indexData = 14;		
		User userData = userDao.findById(userId1);
		CompliancePlanSelfAssessment compliancePlanSelfAssessment = cpsaApprovalFEDao.findById(cpsaId);
		CpsaApprovalFEVo cpsaDataVo = cpsaApprovalFEDao.getDataCpsaPic(cpsaId, userId1, userApprovalId);
		if (compliancePlanSelfAssessment.getCpsaType() != null 
				&& (compliancePlanSelfAssessment.getCpsaType().getParameterDtlCode().equals("COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_SYARIAH")
						|| compliancePlanSelfAssessment.getCpsaType().getParameterDtlCode().equals("COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_CPSA"))) {
			cpsaDataVo.setDirectorate(userDao.getRegionByBranchCode(cpsaDataVo.getBranchCode()));
//			cpsaDataVo.setDirectorate(userDao.getSubBranchNameByBranchCode(cpsaDataVo.getBranchCode()));
			cpsaDataVo.setDivisionName(userDao.getSubBranchNameByBranchCode(cpsaDataVo.getBranchCode()));
		} else {
			cpsaDataVo.setDirectorate(userDao.getDirectorateByDivisionId(cpsaDataVo.getDivisionId()));
		}
		
		if (compliancePlanSelfAssessment.getCpsaType().getParameterDtlCode().equals("COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_SYARIAH")) {
			ParameterDetail pdHeaderExcel = parameterDetailDao.getParameterDetailByParamDtlCode("CPSA_HEADER_SYARIAH");
			cpsaDataVo.setHeaderExcelCpsa(pdHeaderExcel.getNameIn());
		} else if (compliancePlanSelfAssessment.getCpsaType().getParameterDtlCode().equals("COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_CPSA")) {
			ParameterDetail pdHeaderExcel = parameterDetailDao.getParameterDetailByParamDtlCode("CPSA_HEADER_CPSA");
			cpsaDataVo.setHeaderExcelCpsa(pdHeaderExcel.getNameIn());
		} else {
			ParameterDetail pdHeaderExcel = parameterDetailDao.getParameterDetailByParamDtlCode("CPSA_HEADER_PUSAT");
			cpsaDataVo.setHeaderExcelCpsa(pdHeaderExcel.getNameIn());
		}
		
		this.excelWriteDataPic(sheet, row, cell, cpsaDataVo);
		this.excelWriteDataDtl(wb, sheet, row, cell, indexData, rowNum, cpsaDataVo);
		
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
			String fileName = CompliancePlanSelfAssessmentFEConstant.FILE_NAME_CPSA + "_";
			fileName = fileName + userData.getNik()+ "_" + sdfTemp.format(new Date()) + ".xlsx";
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
	
	private void excelWriteDataPic(Sheet sheet, Row row, Cell cell, CpsaApprovalFEVo picVo) {
		row = sheet.getRow(3);
		cell = row.getCell(11);
		cell.setCellValue(picVo.getDirectorate());
		
		cell = row.getCell(20);
		cell.setCellValue(picVo.getDivisionName());
		
		row = sheet.getRow(4);
		cell = row.getCell(11);
		cell.setCellValue(picVo.getPeriodStartDateStr() + " - " + picVo.getPeriodEndDateStr());

		cell = row.getCell(20);
		cell.setCellValue(picVo.getYearStr());
		
		cell = row.getCell(32);
		cell.setCellValue(picVo.getTotalQuestion() - picVo.getTotalAnswer());
		
		row = sheet.getRow(11);
		cell = row.getCell(1);
		cell.setCellValue(picVo.getHeaderExcelCpsa());
	}

	@SuppressWarnings("static-access")
	private void excelWriteDataDtl(Workbook wb, Sheet sheet, Row row, Cell cell, Integer indexData, Integer indexRowNum,
			CpsaApprovalFEVo picVo) {
		try {
			List<CpsaApprovalQuestionFEVo> dataList = cpsaApprovalFEDao.getDataQuestionDetail(picVo.getCpsaId(), null, picVo.getUserId1());
			if (dataList != null && dataList.size() > 0) {
				for (CpsaApprovalQuestionFEVo headerVo : dataList) {
					if (indexRowNum == indexData) {
						row = sheet.getRow(indexRowNum);
					} else {
						row = FileUtil.excelCopyRow(sheet, indexData, indexRowNum, true);
					}
                     
					if(headerVo.getCpsaParentQuestId() == null) {
						cell = row.getCell(1);
						cell.setCellStyle(this.buildStyleForDataHeaderNo(wb));
						cell.setCellValue(headerVo.getCpsaQuestNo()+".");
	
						cell = row.getCell(2);
						cell.setCellStyle(this.buildStyleForDataHeader(wb));
						cell.setCellValue(headerVo.getCpsaQuestion());
						sheet.addMergedRegion(new CellRangeAddress(indexRowNum, indexRowNum, 2, 32));					
					}else {
						cell = row.getCell(1);
						cell.setCellStyle(this.buildStyleForDataDetailNo(wb));
						cell.setCellValue(headerVo.getCpsaQuestNo()+".");

						cell = row.getCell(2);				
						int heightSize = headerVo.getCpsaQuestion().trim().length();
						row.setHeightInPoints((heightSize/4)+25);
						cell.setCellStyle(this.buildStyleForDataDetail(wb));
						cell.setCellValue(headerVo.getCpsaQuestion());
						sheet.addMergedRegion(buildStyleForBorderMergedRegion(wb, sheet, indexRowNum, indexRowNum, 2, 25));		
													
						cell = row.getCell(26);
						cell.setCellStyle(this.buildStyleForDataDetailCenter(wb));
						cell.setCellValue(headerVo.getCpsaAnswerName());

						cell = row.getCell(30);
						cell.setCellStyle(this.buildStyleForDataDetail(wb));
						cell.setCellValue(headerVo.getCpsaNote());
						sheet.addMergedRegion(buildStyleForBorderMergedRegion(wb, sheet, indexRowNum, indexRowNum, 30, 32));
						
					}
					indexRowNum++;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static CellStyle buildStyleForDataHeaderNo(Workbook wb) {
		CellStyle headerStyle = wb.createCellStyle();
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.WHITE.getIndex()); 
	    font.setBold(true);
	    font.setFontHeightInPoints((short)14);
	    headerStyle.setFont(font);
	    headerStyle.setFillForegroundColor(IndexedColors.BLACK.index);
	    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    headerStyle.setAlignment(HorizontalAlignment.CENTER);
	    
		return headerStyle;
	}
	
	private static CellStyle buildStyleForDataHeader(Workbook wb) {
		CellStyle headerStyle = wb.createCellStyle();
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.WHITE.getIndex()); 
	    font.setBold(true);
	    font.setFontHeightInPoints((short)12);
	    headerStyle.setFont(font);
	    headerStyle.setFillForegroundColor(IndexedColors.BLACK.index);
	    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    
		return headerStyle;
	}
	
	private static CellStyle buildStyleForDataDetailNo(Workbook wb) {
		CellStyle headerStyle = wb.createCellStyle();
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.BLACK.getIndex()); 	    
	    font.setFontHeightInPoints((short)11);
	    headerStyle.setFont(font);
	    headerStyle.setFillForegroundColor(IndexedColors.WHITE.index);
	    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    headerStyle.setWrapText(true);  
	    headerStyle.setAlignment(HorizontalAlignment.CENTER);
	    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	    headerStyle.setBorderTop(BorderStyle.MEDIUM);
	    headerStyle.setBorderBottom(BorderStyle.MEDIUM);
	    headerStyle.setBorderLeft(BorderStyle.MEDIUM);
	    headerStyle.setBorderRight(BorderStyle.MEDIUM);

		return headerStyle;
	}
	
	private static CellStyle buildStyleForDataDetail(Workbook wb) {
		CellStyle headerStyle = wb.createCellStyle();
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.BLACK.getIndex()); 
	    headerStyle.setFont(font);
	    font.setFontHeightInPoints((short)11);
	    headerStyle.setFillForegroundColor(IndexedColors.WHITE.index);
	    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    headerStyle.setWrapText(true);  
	    headerStyle.setBorderTop(BorderStyle.MEDIUM);
	    headerStyle.setBorderBottom(BorderStyle.MEDIUM);
	    headerStyle.setBorderLeft(BorderStyle.MEDIUM);
	    headerStyle.setBorderRight(BorderStyle.MEDIUM);
	    headerStyle.setVerticalAlignment(VerticalAlignment.TOP);

		return headerStyle;
	}
	
	private static CellStyle buildStyleForDataDetailCenter(Workbook wb) {
		CellStyle headerStyle = wb.createCellStyle();
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.BLACK.getIndex()); 
	    headerStyle.setFont(font);
	    font.setFontHeightInPoints((short)11);
	    headerStyle.setFillForegroundColor(IndexedColors.WHITE.index);
	    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    headerStyle.setWrapText(true);  
	    headerStyle.setBorderTop(BorderStyle.MEDIUM);
	    headerStyle.setBorderBottom(BorderStyle.MEDIUM);
	    headerStyle.setBorderLeft(BorderStyle.MEDIUM);
	    headerStyle.setBorderRight(BorderStyle.MEDIUM);
	    headerStyle.setAlignment(HorizontalAlignment.CENTER);
	    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		return headerStyle;
	}
	
	public static CellRangeAddress buildStyleForBorderMergedRegion(Workbook wb, Sheet sheet, int firstRow, int lastRow,
			int firstCol, int lastCol) {
		CellRangeAddress cellRangeAddress = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
		RegionUtil.setBorderTop(BorderStyle.MEDIUM, cellRangeAddress, sheet);
		RegionUtil.setBorderBottom(BorderStyle.MEDIUM, cellRangeAddress, sheet);
		RegionUtil.setBorderLeft(BorderStyle.MEDIUM, cellRangeAddress, sheet);
		RegionUtil.setBorderRight(BorderStyle.MEDIUM, cellRangeAddress, sheet);

		return cellRangeAddress;
	}

	public EmailTemplateDao getEmailTemplateDao() {
		return emailTemplateDao;
	}

	public void setEmailTemplateDao(EmailTemplateDao emailTemplateDao) {
		this.emailTemplateDao = emailTemplateDao;
	}

	public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}

	
}
