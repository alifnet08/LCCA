/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.notary.service;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFDataValidationHelper;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.notary.constant.NotaryConstants;
import com.wo.module.notary.dao.NotaryDao;
import com.wo.module.notary.dao.NotaryDocumentDao;
import com.wo.module.notary.dao.NotaryHistoryDao;
import com.wo.module.notary.model.Notary;
import com.wo.module.notary.model.NotaryDocument;
import com.wo.module.notary.model.NotaryHistory;
import com.wo.module.notary.vo.NotaryVo;
import com.wo.module.parameter.dao.ParameterDetailDao;

@Transactional
@Service("notaryService")
public class NotaryServiceImpl implements NotaryService {
    @Autowired
    @Qualifier("notaryDao")
    private NotaryDao notaryDao;
    
    @Autowired
    @Qualifier("parameterDetailDao")
    private ParameterDetailDao parameterDetailDao;

    @Autowired
    @Qualifier("notaryDocumentDao")
    private NotaryDocumentDao notaryDocumentDao;

    @Autowired
    @Qualifier("notaryHistoryDao")
    private NotaryHistoryDao notaryHistoryDao;
    
    
	public NotaryDao getNotaryDao() {
		return notaryDao;
	}

	public void setNotaryDao(NotaryDao notaryDao) {
		this.notaryDao = notaryDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<Notary> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws Exception {
		return notaryDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return notaryDao.searchCountData(searchCriteria);
	}
	
	public void save(Notary entity) {
		notaryDao.save(entity);
		persistNotaryDocuments(entity);
	}
	
	public void update(Notary entity) {
		notaryDao.update(entity);
		persistNotaryDocuments(entity);
	}
	
	public void delete(Notary entity) {
		notaryDao.delete(entity);
	}
  
    public Notary findById(Long id) {
    	Notary entity = notaryDao.getById(id);
    	if (entity != null) {
    		try {
    			entity.setNotaryDocuments(notaryDocumentDao.getNotaryDocumentByNotaryId(id));
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	return entity;
    }

	public void saveHistory(Notary notary, String historyStatus, String catatanRevisi, String userLogin) {
		if (notary == null || notary.getNotaryId() == null) {
			return;
		}
		NotaryHistory history = new NotaryHistory();
		history.setNotary(notary);
		history.setStatus(historyStatus);
		history.setCatatanRevisi(catatanRevisi);
		history.setJenisPengajuan(notary.getJenisPengajuan());
		history.setCreatedBy(userLogin);
		history.setCreationDate(new Timestamp(new Date().getTime()));
		history.setDelId(new Long(0));
		history.setEnabledFlag(CommonConstants.Y);
		notaryHistoryDao.save(history);
	}

	public List<NotaryHistory> getHistoryByNotaryId(Long notaryId) {
		try {
			return notaryHistoryDao.getNotaryHistoryByNotaryId(notaryId);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<NotaryHistory>();
		}
	}

	public List<NotaryHistory> searchHistory(String notaryName, Date tanggalDari, Date tanggalSampai) {
		try {
			List<NotaryHistory> list = notaryHistoryDao.searchHistory(notaryName);
			List<NotaryHistory> filtered = new ArrayList<NotaryHistory>();
			Long currentNotaryId = null;
			String previousJenis = null;
			String inferredJenis = null;
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					NotaryHistory hist = list.get(i);
					if (hist == null || hist.getNotary() == null) {
						continue;
					}
					Long notaryId = hist.getNotary().getNotaryId();
					if (currentNotaryId == null || !currentNotaryId.equals(notaryId)) {
						currentNotaryId = notaryId;
						previousJenis = null;
						inferredJenis = null;
					}
					inferredJenis = inferJenisFromStatus(hist.getStatus(), inferredJenis);
					if (!StringUtils.equals(NotaryConstants.HISTORY_APPROVE_SPV_LEGAL, hist.getStatus())) {
						continue;
					}
					String jenis = StringUtils.defaultIfBlank(hist.getJenisPengajuan(), inferredJenis);
					jenis = StringUtils.defaultIfBlank(jenis, NotaryConstants.JENIS_PENGAJUAN_NOTARIS_BARU);
					inferredJenis = null;
					if (previousJenis != null && StringUtils.equals(previousJenis, jenis)) {
						continue;
					}
					if (previousJenis == null) {
						hist.setPerubahan(jenis);
					} else {
						hist.setPerubahan("\"" + previousJenis + "\" to \"" + jenis + "\"");
					}
					previousJenis = jenis;
					if (hist.getCreationDate() != null && tanggalDari != null
							&& hist.getCreationDate().getTime() < tanggalDari.getTime()) {
						continue;
					}
					if (hist.getCreationDate() != null && tanggalSampai != null
							&& hist.getCreationDate().getTime() > tanggalSampai.getTime()) {
						continue;
					}
					filtered.add(hist);
				}
			}
			java.util.Collections.sort(filtered, new java.util.Comparator<NotaryHistory>() {
				public int compare(NotaryHistory a, NotaryHistory b) {
					if (a.getCreationDate() == null && b.getCreationDate() == null) {
						return 0;
					}
					if (a.getCreationDate() == null) {
						return 1;
					}
					if (b.getCreationDate() == null) {
						return -1;
					}
					return Long.compare(b.getCreationDate().getTime(), a.getCreationDate().getTime());
				}
			});
			return filtered;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<NotaryHistory>();
		}
	}

	private String inferJenisFromStatus(String status, String current) {
		if (StringUtils.isBlank(status)) {
			return current;
		}
		if (status.indexOf("Perpanjangan") >= 0) {
			return NotaryConstants.JENIS_PENGAJUAN_PERPANJANGAN;
		}
		if (status.indexOf("Update Dokumen") >= 0) {
			return NotaryConstants.JENIS_PENGAJUAN_UPDATE_DOKUMEN;
		}
		if (status.indexOf("Catatan Khusus") >= 0) {
			return NotaryConstants.JENIS_PENGAJUAN_CATATAN_KHUSUS;
		}
		if ("Submit by CDU Maker".equals(status)) {
			return NotaryConstants.JENIS_PENGAJUAN_NOTARIS_BARU;
		}
		return current;
	}

	public String generateNoPengajuan(String prefix) {
		Date now = new Date();
		SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
		SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
		String bulan = monthFormat.format(now);
		String tahun = yearFormat.format(now);
		String numberPrefix = prefix + "." + bulan + "." + tahun + ".";
		int nextSeq = 1;
		try {
			String lastNo = notaryDao.getLastNoPengajuan(numberPrefix + "%");
			if (StringUtils.isNotBlank(lastNo) && lastNo.startsWith(numberPrefix)
					&& lastNo.length() > numberPrefix.length()) {
				String seqPart = lastNo.substring(numberPrefix.length());
				nextSeq = Integer.parseInt(seqPart) + 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		DecimalFormat seqFormat = new DecimalFormat("0000");
		return numberPrefix + seqFormat.format(nextSeq);
	}

	private void persistNotaryDocuments(Notary entity) {
		if (entity == null || entity.getNotaryId() == null) {
			return;
		}
		try {
			List<NotaryDocument> existingList = notaryDocumentDao.getNotaryDocumentByNotaryId(entity.getNotaryId());
			List<NotaryDocument> newList = entity.getNotaryDocuments();
			if (newList == null || newList.isEmpty()) {
				return;
			}
			if (existingList != null) {
				for (int i = 0; i < existingList.size(); i++) {
					NotaryDocument existing = existingList.get(i);
					boolean stillActive = false;
					if (newList != null) {
						for (int j = 0; j < newList.size(); j++) {
							NotaryDocument incoming = newList.get(j);
							if (incoming != null && StringUtils.equals(existing.getAttachmentType(), incoming.getAttachmentType())
									&& StringUtils.isNotBlank(incoming.getFileId())) {
								stillActive = true;
								break;
							}
						}
					}
					if (!stillActive) {
						existing.setEnabledFlag("N");
						existing.setDelId(new Long(1));
						notaryDocumentDao.update(existing);
					}
				}
			}
			if (newList != null) {
				for (int i = 0; i < newList.size(); i++) {
					NotaryDocument doc = newList.get(i);
					if (doc == null || StringUtils.isBlank(doc.getFileId())) {
						continue;
					}
					doc.setNotary(entity);
					if (doc.getNotaryDocumentId() == null) {
						notaryDocumentDao.save(doc);
					} else {
						notaryDocumentDao.update(doc);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public NotaryVo saveUpload(UploadedFile fileUploadEvent, FacesUtil facesUtil) {
		List<SelectItem> errorList = new ArrayList<SelectItem>();
		NotaryVo notaryDataVo = new NotaryVo();
		try {			
			if (fileUploadEvent != null) {
				Workbook wb = createWorkbook(fileUploadEvent);
				Sheet sheet = wb.getSheetAt(0);
				Iterator<Row> rowIter = sheet.rowIterator();
				Integer rowNumber = 0;
				Integer indexRow = 0;
				
				List<Notary> notaryList = new ArrayList<Notary>();
				while (rowIter.hasNext()) {
					Row row = rowIter.next();

					// SKIP HEADER		
					rowNumber++;
					indexRow++;
					if (rowNumber <= 1) {
						continue;
					}
					
					SelectItem errorData = new SelectItem();
					boolean flagData = false;
					
					String notaryId = this.getExcelCellStringValue(row.getCell(0));
					String category = this.getExcelCellStringValue(row.getCell(1));
					String area = this.getExcelCellStringValue(row.getCell(2));
					String notaryName = this.getExcelCellStringValue(row.getCell(3));
					String alamakantor = this.getExcelCellStringValue(row.getCell(4));
					String areaCode = this.getExcelCellStringValue(row.getCell(5));
					String phoneNo = this.getExcelCellStringValue(row.getCell(6));

					Notary notaryUpload = null;
					if(notaryId !=null && !notaryId.equals(NotaryConstants.EMPTY)) {
						notaryUpload = notaryDao.findById(Long.parseLong(notaryId));
					}else {
						notaryUpload = new Notary();					
					}		
					
					notaryUpload.setFaxNo(this.getExcelCellStringValue(row.getCell(7)));      
					notaryUpload.setEmail(this.getExcelCellStringValue(row.getCell(8)));
					notaryUpload.setMobileNo(this.getExcelCellStringValue(row.getCell(9)));
					notaryUpload.setWorkArea(this.getExcelCellStringValue(row.getCell(10)));
					notaryUpload.setNote(this.getExcelCellStringValue(row.getCell(11)));
					notaryUpload.setEnabledFlag(this.getExcelCellStringValue(row.getCell(12)));
										
					//  Kategori*
					if(category !=null && !category.equals(NotaryConstants.EMPTY)){
						notaryUpload.setNotaryCategory(parameterDetailDao.getParameterDetailByParamDtlNameIn(category));
					}else {
						errorData.setLabel(facesUtil.retrieveMessage("formNotaryRequiredValidateRow", indexRow+"", facesUtil.retrieveMessage("formNotaryCategory")));
						errorData.setValue(rowNumber+".");
						flagData = true;
					}
					  
					//  Area*
					if(area !=null && !area.equals(NotaryConstants.EMPTY)){
						notaryUpload.setArea(area);						 
					}else {
						errorData.setLabel(facesUtil.retrieveMessage("formNotaryRequiredValidateRow", indexRow+"", facesUtil.retrieveMessage("formNotaryArea")));
						errorData.setValue(rowNumber+".");
						flagData = true;
					}
					
					// nama notaris
					if(notaryName !=null && !notaryName.equals(NotaryConstants.EMPTY)){
						notaryUpload.setNotaryName(notaryName);						 
					}else {
						errorData.setLabel(facesUtil.retrieveMessage("formNotaryRequiredValidateRow", indexRow+"", facesUtil.retrieveMessage("formNotaryNotarisName")));
						errorData.setValue(rowNumber+".");
						flagData = true;
					}
					
					// alamat kantor
					if(alamakantor !=null && !alamakantor.equals(NotaryConstants.EMPTY)){
						notaryUpload.setAddress(alamakantor);						 
					}else {
						errorData.setLabel(facesUtil.retrieveMessage("formNotaryRequiredValidateRow", indexRow+"", facesUtil.retrieveMessage("formNotaryOfficeAddr")));
						errorData.setValue(rowNumber+".");
						flagData = true;
					}
					
					// kode area
					if(areaCode !=null && !areaCode.equals(NotaryConstants.EMPTY)){
						notaryUpload.setAreaCode(areaCode);						 
					}else {
						errorData.setLabel(facesUtil.retrieveMessage("formNotaryRequiredValidateRow", indexRow+"", facesUtil.retrieveMessage("formNotaryAreaCode")));
						errorData.setValue(rowNumber+".");
						flagData = true;
					}
					
					// telp no
					if(phoneNo !=null && !phoneNo.equals(NotaryConstants.EMPTY)){
						notaryUpload.setPhoneNo(phoneNo);						 
					}else {
						errorData.setLabel(facesUtil.retrieveMessage("formNotaryRequiredValidateRow", indexRow+"", facesUtil.retrieveMessage("formNotaryTelpNo")));
						errorData.setValue(rowNumber+".");
						flagData = true;
					}					 
					
					if (!flagData) {
						if(notaryId !=null && !notaryId.equals(NotaryConstants.EMPTY)) {
							notaryUpload.setLastUpdateBy(facesUtil.retrieveUserLogin());
							notaryUpload.setLastUpdateDate(new Timestamp(new Date().getTime()));
							notaryUpload.setDelId(new Long(0));
							if(notaryUpload.getEnabledFlag() !=null && 
									!notaryUpload.getEnabledFlag().equals(NotaryConstants.EMPTY)) {
								if (notaryUpload.getEnabledFlag().equals(CommonConstants.Y)) {
									notaryUpload.setEnabledFlag(CommonConstants.N);
									notaryUpload.setDelId(new Long(1));
								} else if (notaryUpload.getEnabledFlag().equals(CommonConstants.N)) {
									notaryUpload.setEnabledFlag(CommonConstants.Y);
									notaryUpload.setDelId(new Long(0));
								}	
							} else {
								notaryUpload.setEnabledFlag(CommonConstants.Y);
								notaryUpload.setDelId(new Long(0));
							}
							
							notaryList.add(notaryUpload);
							//notaryDao.update(notaryUpload);
						}else {
							notaryUpload.setCreatedBy(facesUtil.retrieveUserLogin());
							notaryUpload.setCreationDate(new Timestamp(new Date().getTime()));
							notaryUpload.setDelId(new Long(0));
							notaryUpload.setEnabledFlag(CommonConstants.Y);
							notaryList.add(notaryUpload);
							//notaryDao.save(notaryUpload);
						}					
					} else {
						errorList.add(errorData);					
					}				
				}
				
				notaryDataVo.setNotaryList(notaryList);
				notaryDataVo.setErrorList(errorList);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return notaryDataVo;
	}

	private Workbook createWorkbook(UploadedFile fileUpload) throws IOException {
		String ext = FileUtil.getExtention(fileUpload.getFileName());
		if (StringUtils.equalsIgnoreCase("xls", ext)) {
			return new HSSFWorkbook(fileUpload.getInputstream());
		} else if (StringUtils.equalsIgnoreCase("xlsx", ext)) {
			return new XSSFWorkbook(fileUpload.getInputstream());
		} else {
			throw new RuntimeException("Extension not valid");
		}
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

	@Override
	public StreamedContent generateDataExcel(List<Notary> listDataXls, List<SelectItem> categoryList) throws Exception {
		//Workbook wb = null;
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;
		DefaultStreamedContent streamContent = null;
		Workbook wb = new HSSFWorkbook();  

		Sheet sheet = wb.createSheet("Notary");
		
		sheet = wb.createSheet("ListKategori");
		templateFileWriteListCategory(wb, sheet, categoryList);	    
		
		sheet = wb.getSheet("Notary");
		int categoryCount = categoryList.size();
		this.templateFileCreatePickList(sheet, categoryCount);
		excelWriteDataHeader(wb, sheet);
		this.excelWriteDataDtl(wb, sheet, listDataXls, categoryList);	    
		
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
			String fileName = NotaryConstants.TEMPLATE_FILE_NAME + "_";
			fileName = fileName + sdfTemp.format(new Date()) + ".xls";
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
	
	private void templateFileCreatePickList(Sheet sheet, int categoryCount) {
		// Category [start]		
		Name namedRange = sheet.getWorkbook().createName();
		namedRange.setNameName("listCategory");
		String colName = excelColumnIndexName(NotaryConstants.EXCEL_COL_IDX_LIST_CATEGORY_CATEGORY_NAME);
		String formula = String.format("%s!$%s$%d:$%s$%d",
				NotaryConstants.TEMPLATE_FILE_NAME_SHEET_CATEGORY, colName,
				NotaryConstants.EXCEL_ROW_IDX_LIST_CATEGORY_GRID_START_DATA + 1, colName,
				NotaryConstants.EXCEL_ROW_IDX_LIST_CATEGORY_GRID_START_DATA
						+ categoryCount);
		namedRange.setRefersToFormula(formula);

		this.createPickListXlsxFromFormula(sheet,
				NotaryConstants.EXCEL_ROW_IDX_GRID_START_DATA, NotaryConstants.EXCEL_ROW_IDX_LIST_CATEGORY_GRID_START_DATA,
				"listCategory");
		// Category [end]
		
	}
	
	private String excelColumnIndexName(int index) {
		StringBuilder s = new StringBuilder();

		while (index >= 26) {
			s.insert(0, (char) ('A' + index % 26));
			index = index / 26 - 1;
		}
		s.insert(0, (char) ('A' + index));

		return s.toString();
	}
	
	private void createPickListXlsxFromFormula(Sheet sheet, int startRow,
			int col, String formulaName) {
		HSSFDataValidationHelper dvHelper = new HSSFDataValidationHelper((HSSFSheet) sheet);				
		DVConstraint  dvConstraint = (DVConstraint) dvHelper
				.createFormulaListConstraint(formulaName);
		CellRangeAddressList addressList = new CellRangeAddressList(startRow,
				SpreadsheetVersion.EXCEL2007.getMaxRows() - 1, col, col);
		HSSFDataValidation validation = (HSSFDataValidation) dvHelper
				.createValidation(dvConstraint, addressList);
		// validation.setSuppressDropDownArrow(false);
		validation.setShowErrorBox(true);
		sheet.addValidationData(validation);
	}
	
	@SuppressWarnings("static-access")
	private int templateFileWriteListCategory(Workbook wb, Sheet sheet, List<SelectItem> categoryList) {
		Row row = null;
		Cell cell = null;
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellStyle(this.buildStyleForDataHeaderCenter(wb));	
		cell.setCellValue("Kategori");
		int rowNum = NotaryConstants.EXCEL_ROW_IDX_LIST_CATEGORY_GRID_START_DATA;

		for (SelectItem dataKategori : categoryList) {
			row = sheet.createRow(rowNum);
			cell = row.createCell(NotaryConstants.EXCEL_COL_IDX_LIST_CATEGORY_CATEGORY_NAME);
			cell.setCellStyle(this.buildStyleForDataDetail(wb));
			cell.setCellValue((String)dataKategori.getLabel());

			rowNum++;
		}
		
		sheet.autoSizeColumn(0);

		return categoryList.size();
	}
	
	@SuppressWarnings("static-access")
	private void excelWriteDataHeader(Workbook wb, Sheet sheet) {
		Row row = sheet.createRow(0);		
		Cell cell = row.createCell(0);
		cell.setCellStyle(this.buildStyleForDataHeaderCenter(wb));
		cell.setCellValue("ID");
	    sheet.setColumnHidden(0, true); 					
		
		cell = row.createCell(1);
		cell.setCellStyle(this.buildStyleForDataHeaderCenter(wb));
		cell.setCellValue("Kategori");	
	
		cell = row.createCell(2);				
		cell.setCellStyle(this.buildStyleForDataHeaderCenter(wb));
		cell.setCellValue("Area");
									
		cell = row.createCell(3);
		cell.setCellStyle(this.buildStyleForDataHeaderCenter(wb));
		cell.setCellValue("Nama Notaris");
	
		cell = row.createCell(4);
		cell.setCellStyle(this.buildStyleForDataHeaderCenter(wb));
		cell.setCellValue("Alamat Kantor");
		
		cell = row.createCell(5);
		cell.setCellStyle(this.buildStyleForDataHeaderCenter(wb));
		cell.setCellValue("Kode Area");
		
		cell = row.createCell(6);
		cell.setCellStyle(this.buildStyleForDataHeaderCenter(wb));
		cell.setCellValue("Telp No");
		
		cell = row.createCell(7);
		cell.setCellStyle(this.buildStyleForDataHeaderCenter(wb));
		cell.setCellValue("Fax No");
		
		cell = row.createCell(8);
		cell.setCellStyle(this.buildStyleForDataHeaderCenter(wb));
		cell.setCellValue("Email");
		
		cell = row.createCell(9);
		cell.setCellStyle(this.buildStyleForDataHeaderCenter(wb));
		cell.setCellValue("HP No");
		
		cell = row.createCell(10);
		cell.setCellStyle(this.buildStyleForDataHeaderCenter(wb));
		cell.setCellValue("Wilayah Kerja");
		
		cell = row.createCell(11);
		cell.setCellStyle(this.buildStyleForDataHeaderCenter(wb));
		cell.setCellValue("Keterangan");
		
		cell = row.createCell(12);
		cell.setCellStyle(this.buildStyleForDataHeaderCenter(wb));
		cell.setCellValue("Delete");
		
		cell = row.createCell(13);
		cell.setCellStyle(this.buildStyleForDataHeaderCenter(wb));
		cell.setCellValue("Tanggal Dibuat");
		
		cell = row.createCell(14);
		cell.setCellStyle(this.buildStyleForDataHeaderCenter(wb));
		cell.setCellValue("Tanggal Terakhir Diubah");
		
		//sheet.autoSizeColumn(0);
							
	}	
	
	
	@SuppressWarnings("static-access")
	private void excelWriteDataDtl(Workbook wb, Sheet sheet, List<Notary> listDataXls, List<SelectItem> categoryList) {
		Row row = null;
		Cell cell = null;
		int rowNum = NotaryConstants.EXCEL_ROW_IDX_GRID_START_DATA;
		SimpleDateFormat sdfConvert = new SimpleDateFormat(CommonConstants.INPUT_DATE_FORMAT);
		try {
			if (listDataXls != null && listDataXls.size() > 0) {
				for (Notary dataNotary : listDataXls) {
					row = sheet.createRow(rowNum);
					cell = row.createCell(0);
					//cell.setCellStyle(this.buildStyleForDataDetailCenter(wb));
					cell.setCellValue(dataNotary.getNotaryId());
                    sheet.setColumnHidden(0, true); 					
					
					cell = row.createCell(1);
					//cell.setCellStyle(this.buildStyleForDataDetail(wb));
					cell.setCellValue(dataNotary.getNotaryCategory().getNameIn());

					cell = row.createCell(2);				
					//cell.setCellStyle(this.buildStyleForDataDetail(wb));
					cell.setCellValue(dataNotary.getArea());
												
					cell = row.createCell(3);
					//cell.setCellStyle(this.buildStyleForDataDetail(wb));
					cell.setCellValue(dataNotary.getNotaryName());

					cell = row.createCell(4);
					//cell.setCellStyle(this.buildStyleForDataDetail(wb));
					cell.setCellValue(dataNotary.getAddress());
					
					cell = row.createCell(5);
					//cell.setCellStyle(this.buildStyleForDataDetail(wb));
					cell.setCellValue(dataNotary.getAreaCode());
					
					cell = row.createCell(6);
					//cell.setCellStyle(this.buildStyleForDataDetail(wb));
					cell.setCellValue(dataNotary.getPhoneNo());
					
					cell = row.createCell(7);
					//cell.setCellStyle(this.buildStyleForDataDetail(wb));
					cell.setCellValue(dataNotary.getFaxNo());
					
					cell = row.createCell(8);
					//cell.setCellStyle(this.buildStyleForDataDetail(wb));
					cell.setCellValue(dataNotary.getEmail());
					
					cell = row.createCell(9);
					//cell.setCellStyle(this.buildStyleForDataDetail(wb));
					cell.setCellValue(dataNotary.getMobileNo());
					
					cell = row.createCell(10);
					//cell.setCellStyle(this.buildStyleForDataDetail(wb));
					cell.setCellValue(dataNotary.getWorkArea());
					
					cell = row.createCell(11);
					//cell.setCellStyle(this.buildStyleForDataDetail(wb));
					cell.setCellValue(dataNotary.getNote());
					
					cell = row.createCell(12);
					//cell.setCellStyle(this.buildStyleForDataDetailCenter(wb));
					if (dataNotary.getEnabledFlag() !=null) {
						if (dataNotary.getEnabledFlag().equals(CommonConstants.Y)) {
							cell.setCellValue(CommonConstants.N);
						} else if (dataNotary.getEnabledFlag().equals(CommonConstants.N)) {
							cell.setCellValue(CommonConstants.Y);
						}							
					} else {
						cell.setCellValue(NotaryConstants.EMPTY);
					}
					
					cell = row.createCell(13);
					//cell.setCellStyle(this.buildStyleForDataDetail(wb));
					cell.setCellValue(sdfConvert.format(dataNotary.getCreationDate()));
					
					cell = row.createCell(14);
					//cell.setCellStyle(this.buildStyleForDataDetail(wb));
					cell.setCellValue(dataNotary.getLastUpdateDate()!=null?sdfConvert.format(dataNotary.getLastUpdateDate()):null);
							
					sheet.autoSizeColumn(rowNum);
					rowNum++;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static CellStyle buildStyleForDataHeaderCenter(Workbook wb) {
		CellStyle headerStyle = wb.createCellStyle();
	    headerStyle.setWrapText(true);  
	    headerStyle.setBorderTop(BorderStyle.MEDIUM);
	    headerStyle.setBorderBottom(BorderStyle.MEDIUM);
	    headerStyle.setBorderLeft(BorderStyle.MEDIUM); 
	    headerStyle.setBorderRight(BorderStyle.MEDIUM);
	    headerStyle.setAlignment(HorizontalAlignment.CENTER);
	    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.BLACK.getIndex()); 
	    font.setFontName("Calibri");
	    font.setFontHeightInPoints((short)12);
	    headerStyle.setFont(font);
	    headerStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.index);
	    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    headerStyle.setAlignment(HorizontalAlignment.CENTER);

		return headerStyle;
	}
	
	private static CellStyle buildStyleForDataDetail(Workbook wb) {
		CellStyle headerStyle = wb.createCellStyle();
	    headerStyle.setWrapText(true);  
	    headerStyle.setBorderTop(BorderStyle.MEDIUM);
	    headerStyle.setBorderBottom(BorderStyle.MEDIUM);
	    headerStyle.setBorderLeft(BorderStyle.MEDIUM);
	    headerStyle.setBorderRight(BorderStyle.MEDIUM);
	    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	    
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.BLACK.getIndex()); 
	    font.setFontName("Calibri");
	    font.setFontHeightInPoints((short)11);
	    headerStyle.setFont(font);

		return headerStyle;
	}
	
	private static CellStyle buildStyleForDataDetailCenter(Workbook wb) {
		CellStyle headerStyle = wb.createCellStyle();
	    /*headerStyle.setWrapText(true);  
	    headerStyle.setBorderTop(BorderStyle.MEDIUM);
	    headerStyle.setBorderBottom(BorderStyle.MEDIUM);
	    headerStyle.setBorderLeft(BorderStyle.MEDIUM); 
	    headerStyle.setBorderRight(BorderStyle.MEDIUM);
	    headerStyle.setAlignment(HorizontalAlignment.CENTER);
	    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	    
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.BLACK.getIndex()); 
	    font.setFontName("Calibri");
	    font.setFontHeightInPoints((short)11);
	    headerStyle.setFont(font); */

		return headerStyle;
	}
	
	public StreamedContent generateDataError(List<SelectItem> dataErrorList) throws Exception {
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;
		BufferedWriter writer = null;
		DefaultStreamedContent streamContent = null;
		
		try {
			baos = new ByteArrayOutputStream();		
			writer = new BufferedWriter(new OutputStreamWriter(baos));
			if(dataErrorList !=null && dataErrorList.size() > 0) {
	        	for(int i=0; i<dataErrorList.size(); i++) {
	        		SelectItem itemData = (SelectItem)dataErrorList.get(i);
	        		writer.write(itemData.getValue() + " " + itemData.getLabel());        		
	        	}
	        }
			writer.close();
		} catch (IOException e) {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e2) {
					// do nothing
				}
			}
			
			return null;
		}

		try {
			bais = new ByteArrayInputStream(baos.toByteArray());
			SimpleDateFormat sdfTemp = new SimpleDateFormat("yyyymmddHHmmss");
			String fileName = NotaryConstants.TEMPLATE_FILE_NAME_ERROR + "_";
			fileName = fileName + sdfTemp.format(new Date()) + ".txt";
			streamContent = new DefaultStreamedContent(bais, "application/txt", fileName);
		} catch (Exception e) {
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
	
}
