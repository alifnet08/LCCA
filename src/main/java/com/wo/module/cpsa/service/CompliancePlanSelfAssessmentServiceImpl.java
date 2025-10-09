package com.wo.module.cpsa.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.counterType.dao.CounterTypeDao;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.counterType.model.CounterTypeDtl;
import com.wo.module.cpsa.constant.CompliancePlanSelfAssessmentConstant;
import com.wo.module.cpsa.dao.CompliancePlanSelfAssesmentDao;
import com.wo.module.cpsa.dao.CompliancePlanSelfAssessmentPicDao;
import com.wo.module.cpsa.dao.CompliancePlanSelfAssessmentQuestionDao;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessment;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessmentPic;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessmentPicEmail;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessmentQuestion;
import com.wo.module.cpsa.vo.CompliancePlanSelfAssessmentPicVo;
import com.wo.module.cpsa.vo.CompliancePlanSelfAssessmentQuestionVo;
import com.wo.module.cpsa.vo.CompliancePlanSelfAssessmentVo;
import com.wo.module.holiday.dao.HolidayDao;
import com.wo.module.parameter.dao.ParameterDetailDao;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.dao.UserDao;
import com.wo.module.user.model.User;

@Transactional
@Service("compliancePlanSelfAssessmentService")
public class CompliancePlanSelfAssessmentServiceImpl implements CompliancePlanSelfAssessmentService, Serializable {

	private static final long serialVersionUID = -4356525374120213023L;

	@Autowired
	@Qualifier("compliancePlanSelfAssesmentDao")
	private CompliancePlanSelfAssesmentDao compliancePlanSelfAssesmentDao;

	@Autowired
	@Qualifier("compliancePlanSelfAssessmentQuestionDao")
	private CompliancePlanSelfAssessmentQuestionDao compliancePlanSelfAssessmentQuestionDao;

	@Autowired
	@Qualifier("counterTypeDao")
	private CounterTypeDao counterTypeDao;
	
	@Autowired
	@Qualifier("holidayDao")
	private HolidayDao holidayDao;
	
	@Autowired
	@Qualifier("parameterDetailDao")
	private ParameterDetailDao parameterDetailDao;
	
	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;
	
	@Autowired
	@Qualifier("compliancePlanSelfAssessmentPicDao")
	private CompliancePlanSelfAssessmentPicDao compliancePlanSelfAssessmentPicDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<CompliancePlanSelfAssessmentVo> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return compliancePlanSelfAssesmentDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return compliancePlanSelfAssesmentDao.searchCountData(searchCriteria);
	}

	@Override
	public void save(CompliancePlanSelfAssessment entity) {
		compliancePlanSelfAssesmentDao.save(entity);
	}

	@Override
	public void update(CompliancePlanSelfAssessment entity) {
		compliancePlanSelfAssesmentDao.update(entity);
	}

	@Override
	public void delete(CompliancePlanSelfAssessment entity) {
		compliancePlanSelfAssesmentDao.delete(entity);
	}

	@Override
	public CompliancePlanSelfAssessment findById(Long cpsaId) {
		return compliancePlanSelfAssesmentDao.getById(cpsaId);
	}

	public CompliancePlanSelfAssesmentDao getCompliancePlanSelfAssesmentDao() {
		return compliancePlanSelfAssesmentDao;
	}

	public void setCompliancePlanSelfAssesmentDao(CompliancePlanSelfAssesmentDao compliancePlanSelfAssesmentDao) {
		this.compliancePlanSelfAssesmentDao = compliancePlanSelfAssesmentDao;
	}

	private boolean dataNumeric(String strNum) {
		Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
		if (strNum == null) {
			return false;
		}
		return pattern.matcher(strNum).matches();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void saveData(CompliancePlanSelfAssessment cpsa, FileUploadEvent fileUploadCpsa, String userLogin, boolean flagNewEdit,
			List<CompliancePlanSelfAssessmentPic> dataCpsaDeleteList) throws Exception {
		try {
			CounterType counterType = new CounterType();
			if (cpsa.getCounterType() != null && cpsa.getCounterType().getCounterTypeId() != null) {
				counterType = counterTypeDao.findById(cpsa.getCounterType().getCounterTypeId());
			}
			
			if (cpsa.getCpsaPics() != null) {
				for (int i = 0; i < cpsa.getCpsaPics().size(); i++) {
					CompliancePlanSelfAssessmentPic dtl = cpsa.getCpsaPics().get(i);
					dtl.setCpsa(cpsa);
					if (dtl.getCreatedBy() == null) {
						dtl.setCreatedBy(userLogin);
						dtl.setCreationDate(new Timestamp(new Date().getTime()));
						
						dtl.setLockFlag(CommonConstants.RECORD_FLAG_YES);
					}else {
						dtl.setLastUpdateBy(userLogin);
						dtl.setLastUpdateDate(new Timestamp(new Date().getTime()));
					}	
					
					dtl.setDelId(new Long(0));
					dtl.setEnabledFlag(Constants.CONSTANT_YES);
					
					// save emailReminder
					if (dtl.getTargetDate() != null && dtl.getIsEditableTemp()) {
						List<CompliancePlanSelfAssessmentPicEmail> compliancePlanSelfAssessmentPicEmailList = new ArrayList<CompliancePlanSelfAssessmentPicEmail>();
						
						if (counterType != null 
								&& counterType.getCounterTypeId() != null 
								&& counterType.getDetails() != null 
								&& !counterType.getDetails().isEmpty()) {
							Calendar calendar = Calendar.getInstance();
							Date targetDateTmp = dtl.getTargetDate();
							
							for (CounterTypeDtl dataCounterTypeDtl : counterType.getDetails()) {
								int counterDate = 0;
								
								if (dataCounterTypeDtl.getSlaType().equals("+")) {
									while (counterDate < dataCounterTypeDtl.getSla().intValue()) {
										calendar.setTime(targetDateTmp);
										calendar.add(Calendar.DAY_OF_MONTH, 1);
										targetDateTmp = calendar.getTime();
										
										int day = calendar.get(Calendar.DAY_OF_WEEK);
										
										if (day == 1 || day == 7) {
											// do nothing
										} else {
											if (Boolean.TRUE.equals(holidayDao.isAvailableDate(targetDateTmp))) {
												counterDate++;
											}
										}
									}
								} else if (dataCounterTypeDtl.getSlaType().equals("-")) {
									while (counterDate < dataCounterTypeDtl.getSla().intValue()) {
										calendar.setTime(targetDateTmp);
										calendar.add(Calendar.DAY_OF_MONTH, -1);
										targetDateTmp = calendar.getTime();
										
										int day = calendar.get(Calendar.DAY_OF_WEEK);
										
										if (day == 1 || day == 7) {
											// do nothing
										} else {
											if (Boolean.TRUE.equals(holidayDao.isAvailableDate(targetDateTmp))) {
												counterDate++;
											}
										}
									}
								}
								
								CompliancePlanSelfAssessmentPicEmail cpsaPicEmail = new CompliancePlanSelfAssessmentPicEmail();
								
								cpsaPicEmail.setCpsaPic(dtl);
								cpsaPicEmail.setSla(dataCounterTypeDtl.getSla().intValue());
								cpsaPicEmail.setSlaType(dataCounterTypeDtl.getSlaType());
								cpsaPicEmail.setEmailDate(new Timestamp(targetDateTmp.getTime()));
								cpsaPicEmail.setCreatedBy(userLogin);
								cpsaPicEmail.setCreationDate(new Timestamp(new Date().getTime()));
								cpsaPicEmail.setEnabledFlag(Constants.CONSTANT_YES);
								cpsaPicEmail.setDelId(0l);
								
								compliancePlanSelfAssessmentPicEmailList.add(cpsaPicEmail);
								
								targetDateTmp = dtl.getTargetDate();
							}
						}
						
						dtl.setCpsaPicEmailList(compliancePlanSelfAssessmentPicEmailList);
					}
					// save emailReminder
				}
			}
			
			Integer totalQuestion = 0;
			if (flagNewEdit && fileUploadCpsa != null) {
				ParameterDetail pdFilePath = parameterDetailDao.getParameterDetailByParamDtlCode("ATTACHMENT_FILE_PATH");
				Workbook wb = createWorkbook(pdFilePath.getNameIn()+CompliancePlanSelfAssessmentConstant.FOLDER_CPSA+"/"+cpsa.getFileQuestId());
				Sheet sheet = wb.getSheet("Kertas Kerja");
				Iterator<Row> rowIter = sheet.rowIterator();
				Long questNumber = null;
				Integer rowNumber = 1;
				while (rowIter.hasNext()) {
					Row row = rowIter.next();

					// SKIP HEADER
					rowNumber++;
					if (rowNumber <= 15) {
						continue;
					}

					CompliancePlanSelfAssessmentQuestion cpsaQuestion = new CompliancePlanSelfAssessmentQuestion();
					boolean flagNumber = dataNumeric(this.getExcelCellStringValue(row.getCell(1)));

					if (flagNumber) {
						cpsaQuestion.setCpsaParentQuestion(questNumber);
						totalQuestion = totalQuestion + 1;
					}

					cpsaQuestion.setCpsa(cpsa);
					cpsaQuestion.setCpsaQuestionNo(this.getExcelCellStringValue(row.getCell(1)));
					cpsaQuestion.setCpsaQuestion(this.getExcelCellStringValue(row.getCell(2)));
					cpsaQuestion.setCreatedBy(userLogin);
					cpsaQuestion.setCreationDate(new Timestamp(new Date().getTime()));
					cpsaQuestion.setDelId(new Long(0));
					cpsaQuestion.setEnabledFlag(Constants.CONSTANT_YES);

					if (StringUtils.isNotBlank(cpsaQuestion.getCpsaQuestionNo()) && StringUtils.isNotBlank(cpsaQuestion.getCpsaQuestion())) {
						compliancePlanSelfAssessmentQuestionDao.save(cpsaQuestion);
					} else {
						if (StringUtils.isBlank(cpsaQuestion.getCpsaQuestionNo()) && StringUtils.isBlank(cpsaQuestion.getCpsaQuestion())) {
							// Skip Proses
						} else {
							if (StringUtils.isBlank(cpsaQuestion.getCpsaQuestionNo())) {
								throw new Exception("Baris ke-" + rowNumber + " Question No diisi");
							} else if (StringUtils.isBlank(cpsaQuestion.getCpsaQuestion())) {
								throw new Exception("Baris ke-" + rowNumber + " Question harus diisi");
							}
						}
					}
					if (!flagNumber) {
						questNumber = cpsaQuestion.getCpsaQuestionId();
					}
				}
			}else {
				totalQuestion = compliancePlanSelfAssessmentQuestionDao.totalDataQuestion(cpsa.getCpsaId());
			}
			
			if (cpsa.getCpsaPics() != null) {
				for (int i = 0; i < cpsa.getCpsaPics().size(); i++) {
					CompliancePlanSelfAssessmentPic dtl = cpsa.getCpsaPics().get(i);
					
					if(flagNewEdit) {
						dtl.setTotalQuestion(totalQuestion);
					}else {
						totalQuestion = totalQuestion > 0?totalQuestion:dtl.getTotalQuestion();
						dtl.setTotalQuestion(totalQuestion);
					}
					
					cpsa.getCpsaPics().set(i, dtl);
				}
			}
			
			if (cpsa.getCpsaPics() != null) {
				a : for (CompliancePlanSelfAssessmentPic dataCpsaPic : cpsa.getCpsaPics()) {
					if (Boolean.TRUE.equals(dataCpsaPic.getIsEditableTemp())) {
						cpsa.setCpsaStatus("CPSA_INPROGRESS");
						break a;
					}
				}
			}
			
			if (cpsa.getCpsaId() != null) {
				cpsa.setLastUpdateBy(userLogin);
				cpsa.setLastUpdateDate(new Timestamp(new Date().getTime()));
				cpsa.setDelId(new Long(0));
				cpsa.setEnabledFlag(Constants.CONSTANT_YES);
				compliancePlanSelfAssesmentDao.update(cpsa);
			} else {
				cpsa.setCreatedBy(userLogin);
				cpsa.setCreationDate(new Timestamp(new Date().getTime()));
				cpsa.setDelId(new Long(0));
				cpsa.setEnabledFlag(Constants.CONSTANT_YES);
				compliancePlanSelfAssesmentDao.save(cpsa);
			}
			
			//delete data cpsa
			if(dataCpsaDeleteList !=null && dataCpsaDeleteList.size() > 0) {
				for(CompliancePlanSelfAssessmentPic cpsaDel : dataCpsaDeleteList) {
					if(cpsaDel !=null && cpsaDel.getCpsaPicId() !=null && 
							cpsaDel.getCpsaPicId() > 0) {
						compliancePlanSelfAssessmentPicDao.delete(cpsaDel);
					}
				}
			}

		} catch (Exception ex) {
			throw ex;
		}
	}

	// update by alex [16-02-2023]
	private Workbook createWorkbook(String filePath) throws IOException {
		String ext = FileUtil.getExtention(filePath);
		if (StringUtils.equalsIgnoreCase("xls", ext)) {
			FileInputStream fis = new FileInputStream(filePath);
			return new HSSFWorkbook(fis);
		} else if (StringUtils.equalsIgnoreCase("xlsx", ext)) {
			try {
				return new XSSFWorkbook(new File(filePath));
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("Extension not valid");
		}
		return null;
	}

	private String getExcelCellStringValue(org.apache.poi.ss.usermodel.Cell cell) {
		String value = null;
		SimpleDateFormat dateFormatter = new SimpleDateFormat(CommonConstants.INPUT_DATE_FORMAT);
		NumberFormat decimalFormatter = new DecimalFormat("#.#");
		if (cell == null) {
			return StringUtils.EMPTY;
		}

		switch (cell.getCellType()) {
		case STRING:
			value = cell.getStringCellValue();
			break;
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				value = dateFormatter.format(cell.getDateCellValue());
			} else {
				value = decimalFormatter.format(cell.getNumericCellValue());
			}
			break;
		case BLANK:
			value = StringUtils.EMPTY;
			break;
		case BOOLEAN:
			value = String.valueOf(cell.getBooleanCellValue());
			break;
		case ERROR:
			value = StringUtils.EMPTY;
			break;
		case FORMULA:
			try {
				value = cell.getStringCellValue();
			} catch (Exception ex) {
				value = StringUtils.EMPTY;
			}
			break;
		default:
			value = StringUtils.EMPTY;
			break;
		}

		return value;
	}

	public String downloadCpas(Long cpsaId, List<String> filenameList, String filePath) {
		String dir = CompliancePlanSelfAssessmentConstant.STRING_EMPTY;		
		try {			
			
			List<CompliancePlanSelfAssessmentPicVo> dataPicList = compliancePlanSelfAssessmentQuestionDao.getDataCpsaPic(cpsaId, null, null);
			if (dataPicList != null && dataPicList.size() > 0) {
				for (CompliancePlanSelfAssessmentPicVo picVo : dataPicList) {
					Long totalNotAnswer = compliancePlanSelfAssessmentQuestionDao.totalDataNotAnswer(picVo.getCpsaId(), picVo.getUserId1());
					picVo.setTotalNotAnswer(totalNotAnswer);
					generateDataCpsaExcel(picVo, filenameList, filePath);
				}
				
				dir = filePath + "CPAS_ZIP"+Constants.FILE_SEPARATOR+"CPSA_"+cpsaId;
			}

		} catch (Exception e) {
			e.printStackTrace();
			dir = "NOT_FOUND";
		} 
		
		return dir;
	}

	private String generateDataCpsaExcel(CompliancePlanSelfAssessmentPicVo picVo, List<String> filenameList, String filePath) throws Exception, IOException {
		Workbook wb = null;
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;
//		InputStream isFile = new FileInputStream(CompliancePlanSelfAssessmentConstant.TEMPLATE_FILE_PATH);
		InputStream isFile = this.getClass().getClassLoader().getResourceAsStream(CompliancePlanSelfAssessmentConstant.TEMPLATE_FILE_PATH);
		
		try {
			wb = new XSSFWorkbook(isFile);
		} catch (IOException e) {
			// logger.error("Error while trying to read template file", e);
			e.printStackTrace();
			return null;
		} /*
			 * finally { try { is.close(); } catch (IOException e) { e.printStackTrace(); }
			 * }
			 */
		
		Sheet sheet = wb.getSheet("Kertas Kerja");
		
		Row row = null;
		Cell cell = null;
		int rowNum = 14;
		int indexData = 14;
		
		this.excelWriteDataPic(sheet, row, cell, picVo);
		this.excelWriteDataDtl(wb, sheet, row, cell, indexData, rowNum, picVo);		 
		
		// Closing the output stream
		try {
			baos = new ByteArrayOutputStream();
			wb.write(baos);
		} catch (IOException e) {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e2) {
					// do nothing
					e2.printStackTrace();
				}
			}
			// logger.error("Error while trying to write to mem output stream", e);
			return null;
		}

		try {
			bais = new ByteArrayInputStream(baos.toByteArray());
		} catch (Exception e) {
			// logger.error("Error while trying to create stream content", e);
			e.printStackTrace();
		} finally {
			try {
				baos.close();
				bais.close();
			} catch (IOException e) {
				// do nothing
				e.printStackTrace();
			}
		}

//		File dir = new File("D:\\home\\maybank\\cpsa\\CPSA_"+picVo.getCpsaId());
		File dir = new File(filePath + "CPSA_ZIP"+CommonConstants.FILE_SEPARATOR+"CPSA_"+picVo.getCpsaId());
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		byte[] byteData = bais.readAllBytes();
		SimpleDateFormat sdfTemp = new SimpleDateFormat("yyyymmddHHmmss");
		String fileName = CompliancePlanSelfAssessmentConstant.FILE_NAME_CPSA + "_";
		fileName = fileName + picVo.getUserNik1()+ "_" + sdfTemp.format(new Date());
		String sourceFile = dir + CommonConstants.FILE_SEPARATOR + fileName + ".xlsx";
		filenameList.add(sourceFile);
		try (FileOutputStream fos = new FileOutputStream(sourceFile)) {
			fos.write(byteData);
		    fos.close();
		}

		return null;
	}

	private void excelWriteDataPic(Sheet sheet, Row row, Cell cell, CompliancePlanSelfAssessmentPicVo picVo) {
		row = sheet.getRow(3);
		cell = row.getCell(11);
		cell.setCellValue(picVo.getDirectorateName());
		
		cell = row.getCell(20);
		cell.setCellValue(picVo.getDivisionName());
		
		row = sheet.getRow(4);
		cell = row.getCell(11);
		cell.setCellValue(picVo.getPeriodStartDateStr() + " - " + picVo.getPeriodEndDateStr());

		cell = row.getCell(20);
		cell.setCellValue(picVo.getYearStr());
		
		cell = row.getCell(32);
		cell.setCellValue(picVo.getTotalNotAnswer()+"");
		
		row = sheet.getRow(11);
		cell = row.getCell(1);
		cell.setCellValue(picVo.getHeaderExcelCpsa());
	}

	@SuppressWarnings("static-access")
	private void excelWriteDataDtl(Workbook wb, Sheet sheet, Row row, Cell cell, Integer indexData, Integer indexRowNum,
			CompliancePlanSelfAssessmentPicVo picVo) {
		try {
//			List<CompliancePlanSelfAssessmentQuestionVo> questHeaderList = compliancePlanSelfAssessmentQuestionDao.getDataPicAnswer(picVo.getCpsaId(),
//					picVo.getCpsaPicId(), null, null, false);
			List<CompliancePlanSelfAssessmentQuestionVo> questHeaderList = compliancePlanSelfAssessmentQuestionDao.getDataPicAnswer(picVo.getCpsaId(),
					picVo.getUserId1(), null, null, false);
			if (questHeaderList != null && questHeaderList.size() > 0) {
				for (CompliancePlanSelfAssessmentQuestionVo headerVo : questHeaderList) {
					if (indexRowNum == indexData) {
						row = sheet.getRow(indexRowNum);
					} else {
						row = FileUtil.excelCopyRow(sheet, indexData, indexRowNum, true);
					}
                     
					if(headerVo.getCpsaParentQuestion() == null) {
						cell = row.getCell(1);
						cell.setCellStyle(this.buildStyleForDataHeaderNo(wb));
						cell.setCellValue(headerVo.getCpsaQuestionNo()+".");
	
						cell = row.getCell(2);
						cell.setCellStyle(this.buildStyleForDataHeader(wb));
						cell.setCellValue(headerVo.getCpsaQuestion());
						sheet.addMergedRegion(new CellRangeAddress(indexRowNum, indexRowNum, 2, 32));					
					}else {
						cell = row.getCell(1);
						cell.setCellStyle(this.buildStyleForDataDetailNo(wb));
						cell.setCellValue(headerVo.getCpsaQuestionNo());

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
					/*List<CompliancePlanSelfAssessmentQuestionVo> questDtlList = compliancePlanSelfAssessmentQuestionDao
							.getDataPicAnswer(picVo.getCpsaId(), null, picVo.getCpsaPicId(),
									headerVo.getCpsaQuestionId(), false);
					if (questDtlList != null && questDtlList.size() > 0) {
						for (CompliancePlanSelfAssessmentQuestionVo questDetailVo : questDtlList) {
							row = FileUtil.excelCopyRow(sheet, indexData, indexRowNum, true);*/
														
							
							//indexRowNum++;
						//}
					//}
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

	@Override
	public List<String> fileExcelCpsaList(Long cpsaId, String filePath) {
		List<String> fileNameList = new ArrayList<>();
		
		try {
			CompliancePlanSelfAssessment compliancePlanSelfAssessment = compliancePlanSelfAssesmentDao.findById(cpsaId);
			List<CompliancePlanSelfAssessmentPicVo> dataPicList = compliancePlanSelfAssessmentQuestionDao.getDataCpsaPic(cpsaId, null, null);
			if (dataPicList != null && dataPicList.size() > 0) {
				for (CompliancePlanSelfAssessmentPicVo picVo : dataPicList) {
					Long totalNotAnswer = compliancePlanSelfAssessmentQuestionDao.totalDataNotAnswer(picVo.getCpsaId(), picVo.getUserId1());
					picVo.setTotalNotAnswer(totalNotAnswer);
					
//					User user1 = userDao.findById(picVo.getUserId1());
					if (compliancePlanSelfAssessment.getCpsaType() != null 
							&& (compliancePlanSelfAssessment.getCpsaType().getParameterDtlCode().equals("COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_SYARIAH")
									|| compliancePlanSelfAssessment.getCpsaType().getParameterDtlCode().equals("COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_CPSA"))) {
						picVo.setDirectorateName(userDao.getRegionByBranchCode(picVo.getBranchCode()));
//						picVo.setDirectorateName(userDao.getSubBranchNameByBranchCode(picVo.getBranchCode()));
						picVo.setDivisionName(userDao.getSubBranchNameByBranchCode(picVo.getBranchCode()));
					}else {
						picVo.setDirectorateName(userDao.getDirectorateByDivisionId(picVo.getDivisionId()));;
					}
					
					if (compliancePlanSelfAssessment.getCpsaType().getParameterDtlCode().equals("COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_SYARIAH")) {
						ParameterDetail pdHeaderExcel = parameterDetailDao.getParameterDetailByParamDtlCode("CPSA_HEADER_SYARIAH");
						picVo.setHeaderExcelCpsa(pdHeaderExcel.getNameIn());
					} else if (compliancePlanSelfAssessment.getCpsaType().getParameterDtlCode().equals("COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_CPSA")) {
						ParameterDetail pdHeaderExcel = parameterDetailDao.getParameterDetailByParamDtlCode("CPSA_HEADER_CPSA");
						picVo.setHeaderExcelCpsa(pdHeaderExcel.getNameIn());
					} else {
						ParameterDetail pdHeaderExcel = parameterDetailDao.getParameterDetailByParamDtlCode("CPSA_HEADER_PUSAT");
						picVo.setHeaderExcelCpsa(pdHeaderExcel.getNameIn());
					}
					
					generateDataCpsaExcel(picVo, fileNameList, filePath);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return fileNameList;
	}

	public CounterTypeDao getCounterTypeDao() {
		return counterTypeDao;
	}

	public void setCounterTypeDao(CounterTypeDao counterTypeDao) {
		this.counterTypeDao = counterTypeDao;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public HolidayDao getHolidayDao() {
		return holidayDao;
	}

	public void setHolidayDao(HolidayDao holidayDao) {
		this.holidayDao = holidayDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Override
	public void saveLockCPSAPic(Long cpsaPicId, String userLogin) {
		CompliancePlanSelfAssessmentPic entityCPSAPic = compliancePlanSelfAssessmentPicDao.findById(cpsaPicId);

		entityCPSAPic.setLockFlag(Constants.CONSTANT_YES);
		entityCPSAPic.setUserNikLock(userLogin);
		entityCPSAPic.setLastUpdateBy(userLogin);
		entityCPSAPic.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
		
		compliancePlanSelfAssessmentPicDao.update(entityCPSAPic);;
	}

	
}
