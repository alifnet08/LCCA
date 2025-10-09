package com.wo.module.common.report;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;

public class CommonReportUtil {
	static Logger logger = Logger.getLogger(CommonReportUtil.class);

	public String generateFileName(String fileNamePrefix, String nik) {
		String tempFileNamePrefix = StringUtils.EMPTY;
		if (fileNamePrefix != null && !fileNamePrefix.isEmpty()) {
			tempFileNamePrefix = fileNamePrefix
					+ CommonConstants.SEPARATOR_DASH;
		}
		String fileName = tempFileNamePrefix + System.currentTimeMillis()
				+ CommonConstants.SEPARATOR_DASH + nik
				+ CommonConstants.FILE_TYPE_XLSX;
		return fileName;
	}

	public String retrieveFilePath(ParameterDetailService parameterDetailService) {
		ParameterDetail sytemProp;
		String filePath = null;
		try {
			sytemProp = parameterDetailService
					.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_FILE_PATH_TMP);
			filePath = sytemProp.getNameIn();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return filePath;
	}

	public void createExcelCellFormat(XSSFWorkbook book,
			Map<String, CellStyle> mapCellFormat) throws Exception {
		// Cell Format Column Header
		CellStyle styleHeader = book.createCellStyle();
		// styleHeader.setDataFormat((short)BuiltinFormats.getBuiltinFormat("text"));
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
		styleHeader.setBorderBottom(BorderStyle.MEDIUM);
		styleHeader.setBorderTop(BorderStyle.MEDIUM);
		styleHeader.setBorderLeft(BorderStyle.MEDIUM);
		styleHeader.setBorderRight(BorderStyle.MEDIUM);
		styleHeader.setFillForegroundColor(HSSFColorPredefined.GREY_25_PERCENT.getIndex());
		styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont headerFont = book.createFont();
		headerFont.setFontHeightInPoints((short) 10);
		headerFont.setFontName("Arial");
		// headerFont.setColor(IndexedColors.BLACK.getIndex());
		// headerFont.setBold(true);
		// headerFont.setItalic(false);
		headerFont.setColor(HSSFColorPredefined.BLACK.getIndex());
		headerFont.setBold(true);

		styleHeader.setFont(headerFont);

		// Cell Format Data Detail String
		CellStyle styleString = book.createCellStyle();
		// styleString.setDataFormat((short)BuiltinFormats.getBuiltinFormat("text"));
		styleString.setAlignment(HorizontalAlignment.LEFT);
		styleString.setBorderBottom(BorderStyle.THIN);
		// styleString.setBorderTop(CellStyle.BORDER_THIN);
		styleString.setBorderLeft(BorderStyle.THIN);
		styleString.setBorderRight(BorderStyle.THIN);
		XSSFFont stringFont = book.createFont();
		// stringFont.setFontHeightInPoints((short)10);
		// stringFont.setFontName("Arial");
		// stringFont.setColor(IndexedColors.BLACK.getIndex());
		// stringFont.setBold(false);
		// stringFont.setItalic(false);
		stringFont.setColor(HSSFColorPredefined.BLACK.getIndex());
		// stringFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		styleString.setFont(stringFont);

		// Cell Format Data Detail Number
		CellStyle styleNumber = book.createCellStyle();
		// styleNumber.setDataFormat((short)
		// Integer.parseInt(BuiltinFormats.getBuiltinFormat(3)));
		XSSFDataFormat fmt = book.createDataFormat();
		styleNumber.setDataFormat(fmt.getFormat("#,##0"));
		styleNumber.setAlignment(HorizontalAlignment.RIGHT);
		styleNumber.setBorderBottom(BorderStyle.THIN);
		// styleString.setBorderTop(CellStyle.BORDER_THIN);
		styleNumber.setBorderLeft(BorderStyle.THIN);
		styleNumber.setBorderRight(BorderStyle.THIN);
		XSSFFont numberFont = book.createFont();
		numberFont.setFontHeightInPoints((short) 10);
		numberFont.setFontName("Arial");
		numberFont.setColor(IndexedColors.BLACK.getIndex());
		// numberFont.setBold(false);
		// numberFont.setItalic(false);
		styleNumber.setFont(numberFont);
		
		//add by Jovan - 18 Feb 2024
		// Cell Format Data Detail Decimal
		CellStyle styleDecimal = book.createCellStyle();
		XSSFDataFormat fmtDecimal = book.createDataFormat();
		styleDecimal.setDataFormat(fmtDecimal.getFormat("#,##0.00"));
		styleDecimal.setAlignment(HorizontalAlignment.RIGHT);
		styleDecimal.setBorderBottom(BorderStyle.THIN);
		styleDecimal.setBorderLeft(BorderStyle.THIN);
		styleDecimal.setBorderRight(BorderStyle.THIN);
		XSSFFont decimalFont = book.createFont();
		decimalFont.setFontHeightInPoints((short) 10);
		decimalFont.setFontName("Arial");
		decimalFont.setColor(IndexedColors.BLACK.getIndex());
		styleDecimal.setFont(decimalFont);

		// Cell Format Data Detail Date
		CellStyle styleDate = book.createCellStyle();
		// styleDate.setDataFormat((short)
		// BuiltinFormats.getBuiltinFormat("0xe"));
		// styleNumber.setDataFormat(fmt.getFormat("dd/mm/yyyy"));
		styleDate.setAlignment(HorizontalAlignment.LEFT);
		styleDate.setBorderBottom(BorderStyle.THIN);
		// styleString.setBorderTop(CellStyle.BORDER_THIN);
		styleDate.setBorderLeft(BorderStyle.THIN);
		styleDate.setBorderRight(BorderStyle.THIN);
		XSSFFont dateFont = book.createFont();
		dateFont.setFontHeightInPoints((short) 10);
		dateFont.setFontName("Arial");
		dateFont.setColor(IndexedColors.BLACK.getIndex());
		dateFont.setBold(false);
		dateFont.setItalic(false);
		styleDate.setFont(dateFont);

		// Cell Format Header Title
		// CellStyle styleHeaderTitle = book.createCellStyle();
		// styleHeaderTitle.setDataFormat((short)BuiltinFormats.getBuiltinFormat("text"));
		// styleHeaderTitle.setAlignment(CellStyle.ALIGN_CENTER);
		// styleHeaderTitle.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		// XSSFFont headerFontTitle = book.createFont();
		// headerFontTitle.setFontHeightInPoints((short)10);
		// headerFontTitle.setFontName("Arial");
		// headerFontTitle.setColor(IndexedColors.BLACK.getIndex());
		// headerFontTitle.setBold(true);
		// headerFontTitle.setItalic(false);
		// headerFontTitle.setColor(HSSFColor.BLACK.index);
		// headerFontTitle.setBoldweight(Font.BOLDWEIGHT_BOLD);
		// styleHeaderTitle.setFont(headerFontTitle);

		CellStyle styleHeaderTitle = book.createCellStyle();
		// styleHeaderTitle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		// styleHeaderTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleHeaderTitle.setAlignment(HorizontalAlignment.LEFT);
		styleHeaderTitle.setAlignment(HorizontalAlignment.CENTER);
		styleHeaderTitle.setVerticalAlignment(VerticalAlignment.CENTER);
		Font fontHeaderTitle = book.createFont();
		fontHeaderTitle.setFontHeightInPoints((short) 10);
		fontHeaderTitle.setFontName("Arial");
		fontHeaderTitle.setColor(HSSFColorPredefined.BLACK.getIndex());
		fontHeaderTitle.setBold(true);
		styleHeaderTitle.setFont(fontHeaderTitle);

		// Cell Format Header Value
		// XSSFCellStyle styleHeaderValue = book.createCellStyle();
		// styleHeaderValue.setDataFormat((short)BuiltinFormats.getBuiltinFormat("text"));
		// styleHeaderValue.setAlignment(CellStyle.ALIGN_CENTER);
		// styleHeaderValue.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		// XSSFFont headerFontValue = book.createFont();
		// headerFontValue.setFontHeightInPoints((short)10);
		// headerFontValue.setFontName("Arial");
		// headerFontValue.setColor(IndexedColors.BLACK.getIndex());
		// headerFontValue.setBold(true);
		// headerFontValue.setItalic(false);
		// headerFontValue.setColor(HSSFColor.BLACK.index);
		// headerFontValue.setBoldweight(Font.BOLDWEIGHT_BOLD);

		// styleHeaderValue.setFont(headerFontValue);

		CellStyle styleHeaderValue = book.createCellStyle();
		// styleHeaderValue.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		// styleHeaderValue.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// styleHeaderValue.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleHeaderValue.setAlignment(HorizontalAlignment.LEFT);
		styleHeaderValue.setVerticalAlignment(VerticalAlignment.CENTER);
		Font font = book.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setFontName("Arial");
		font.setColor(HSSFColorPredefined.BLACK.getIndex());
		// font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		styleHeaderValue.setFont(font);

		// Cell Format Header Label
		CellStyle styleHeaderLabel = book.createCellStyle();
		// styleHeaderLabel.setDataFormat((short)BuiltinFormats.getBuiltinFormat("text"));
		// styleHeaderLabel.setAlignment(CellStyle.ALIGN_RIGHT);
		// styleHeaderLabel.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		XSSFFont headerFontLabel = book.createFont();
		headerFontLabel.setFontHeightInPoints((short) 10);
		headerFontLabel.setFontName("Arial");
		headerFontLabel.setColor(IndexedColors.BLACK.getIndex());
		// headerFontLabel.setBold(true);
		// headerFontLabel.setItalic(false);
		// headerFontLabel.setColor(HSSFColor.BLACK.index);
		// headerFontLabel.setBoldweight(Font.BOLDWEIGHT_BOLD);
		styleHeaderLabel.setFont(headerFontLabel);

		mapCellFormat.put(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_VALUE,
				styleHeaderValue);
		mapCellFormat.put(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_LABEL,
				styleHeaderLabel);
		mapCellFormat.put(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_TITLE,
				styleHeaderTitle);
		mapCellFormat.put(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER,
				styleHeader);
		mapCellFormat.put(CommonConstants.MAP_KEY_CELL_FORMAT_DATA_DTL_NUMBER,
				styleNumber);
		mapCellFormat.put(CommonConstants.MAP_KEY_CELL_FORMAT_DATA_DTL_DATE,
				styleDate);
		mapCellFormat.put(CommonConstants.MAP_KEY_CELL_FORMAT_DATA_DTL_STRING,
				styleString);
		mapCellFormat.put(CommonConstants.MAP_KEY_CELL_FORMAT_DATA_DTL_DECIMAL,
				styleDecimal);

		// Added by Neir Kate [Start]
		// Cell Format Column Header Number
		CellStyle styleHeaderNumber = book.createCellStyle();
		// styleHeaderNumber.setDataFormat((short)BuiltinFormats.getBuiltinFormat("text"));
		styleHeaderNumber.setDataFormat(fmt.getFormat("#,##0"));
		styleHeaderNumber.setAlignment(HorizontalAlignment.RIGHT);
		styleHeaderNumber.setVerticalAlignment(VerticalAlignment.CENTER);
		styleHeaderNumber.setBorderBottom(BorderStyle.MEDIUM);
		styleHeaderNumber.setBorderTop(BorderStyle.MEDIUM);
		styleHeaderNumber.setBorderLeft(BorderStyle.MEDIUM);
		styleHeaderNumber.setBorderRight(BorderStyle.MEDIUM);
		styleHeaderNumber
				.setFillForegroundColor(HSSFColorPredefined.GREY_25_PERCENT.getIndex());
		styleHeaderNumber.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont headerNumberFont = book.createFont();
		headerNumberFont.setFontHeightInPoints((short) 10);
		headerNumberFont.setFontName("Arial");
		// headerNumberFont.setColor(IndexedColors.BLACK.getIndex());
		// headerNumberFont.setBold(true);
		// headerNumberFont.setItalic(false);
		headerNumberFont.setColor(HSSFColorPredefined.BLACK.getIndex());
		headerNumberFont.setBold(true);

		styleHeaderNumber.setFont(headerNumberFont);

		mapCellFormat.put(
				CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER_NUMBER,
				styleHeaderNumber);
		// Added by Neir Kate [End]
		
		//add by dwi
		// Cell Format Data Detail String
		CellStyle styleWrapString = book.createCellStyle();
		// styleString.setDataFormat((short)BuiltinFormats.getBuiltinFormat("text"));
		styleWrapString.setAlignment(HorizontalAlignment.LEFT);
		styleWrapString.setBorderBottom(BorderStyle.THIN);
		// styleString.setBorderTop(CellStyle.BORDER_THIN);
		styleWrapString.setBorderLeft(BorderStyle.THIN);
		styleWrapString.setBorderRight(BorderStyle.THIN);
		XSSFFont stringWrapFont = book.createFont();
		// stringFont.setFontHeightInPoints((short)10);
		// stringFont.setFontName("Arial");
		// stringFont.setColor(IndexedColors.BLACK.getIndex());
		// stringFont.setBold(false);
		// stringFont.setItalic(false);
		stringWrapFont.setColor(HSSFColorPredefined.BLACK.getIndex());
		// stringFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		styleWrapString.setFont(stringWrapFont);
		styleWrapString.setWrapText(true);
		mapCellFormat.put(CommonConstants.MAP_KEY_CELL_FORMAT_DATA_DTL_WRAP_STRING,
				styleWrapString);
		
		//add by dwi, wrap style header column
		// Cell Format Column Header
		CellStyle styleWrapHeader = book.createCellStyle();
		// styleHeader.setDataFormat((short)BuiltinFormats.getBuiltinFormat("text"));
		styleWrapHeader.setAlignment(HorizontalAlignment.CENTER);
		styleWrapHeader.setVerticalAlignment(VerticalAlignment.CENTER);
		styleWrapHeader.setBorderBottom(BorderStyle.MEDIUM);
		styleWrapHeader.setBorderTop(BorderStyle.MEDIUM);
		styleWrapHeader.setBorderLeft(BorderStyle.MEDIUM);
		styleWrapHeader.setBorderRight(BorderStyle.MEDIUM);
		styleWrapHeader.setFillForegroundColor(HSSFColorPredefined.GREY_25_PERCENT.getIndex());
		styleWrapHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		XSSFFont headerWrapFont = book.createFont();
		headerWrapFont.setFontHeightInPoints((short) 10);
		headerWrapFont.setFontName("Arial");
		// headerFont.setColor(IndexedColors.BLACK.getIndex());
		// headerFont.setBold(true);
		// headerFont.setItalic(false);
		headerWrapFont.setColor(HSSFColorPredefined.BLACK.getIndex());
		headerWrapFont.setBold(true);

		styleWrapHeader.setFont(headerWrapFont);
		styleWrapHeader.setWrapText(true);
		
		mapCellFormat.put(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_WRAP_HEADER,
				styleWrapHeader);
		
		//add by dwi, wrap style no border bottom
		// Cell Format Data Detail String
		CellStyle styleWrapNoBottomString = book.createCellStyle();
		// styleString.setDataFormat((short)BuiltinFormats.getBuiltinFormat("text"));
		styleWrapNoBottomString.setAlignment(HorizontalAlignment.LEFT);
//		styleWrapNoBottomString.setBorderBottom(BorderStyle.THIN);
		// styleString.setBorderTop(CellStyle.BORDER_THIN);
		styleWrapNoBottomString.setBorderLeft(BorderStyle.THIN);
		styleWrapNoBottomString.setBorderRight(BorderStyle.THIN);
		XSSFFont stringWrapNoBottomFont = book.createFont();
		// stringFont.setFontHeightInPoints((short)10);
		// stringFont.setFontName("Arial");
		// stringFont.setColor(IndexedColors.BLACK.getIndex());
		// stringFont.setBold(false);
		// stringFont.setItalic(false);
		stringWrapNoBottomFont.setColor(HSSFColorPredefined.BLACK.getIndex());
		// stringFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		styleWrapNoBottomString.setFont(stringWrapNoBottomFont);
		styleWrapNoBottomString.setWrapText(true);
		mapCellFormat.put(CommonConstants.MAP_KEY_CELL_FORMAT_DATA_DTL_WRAP_NO_BOTTOM_STRING,
				styleWrapNoBottomString);
		
		//add by dwi, wrap style no border top
		// Cell Format Data Detail String
		CellStyle styleWrapBorderTopOnlyString = book.createCellStyle();
		// styleString.setDataFormat((short)BuiltinFormats.getBuiltinFormat("text"));
		styleWrapBorderTopOnlyString.setAlignment(HorizontalAlignment.LEFT);
//		styleWrapTopOnlyString.setBorderBottom(BorderStyle.THIN);
		styleWrapBorderTopOnlyString.setBorderTop(BorderStyle.THIN);
//		styleWrapTopOnlyString.setBorderLeft(BorderStyle.THIN);
//		styleWrapTopOnlyString.setBorderRight(BorderStyle.THIN);
		XSSFFont stringWrapBorderTopOnlyFont = book.createFont();
		// stringFont.setFontHeightInPoints((short)10);
		// stringFont.setFontName("Arial");
		// stringFont.setColor(IndexedColors.BLACK.getIndex());
		// stringFont.setBold(false);
		// stringFont.setItalic(false);
		stringWrapBorderTopOnlyFont.setColor(HSSFColorPredefined.BLACK.getIndex());
		// stringFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		styleWrapBorderTopOnlyString.setFont(stringWrapBorderTopOnlyFont);
		styleWrapBorderTopOnlyString.setWrapText(true);
		mapCellFormat.put(CommonConstants.MAP_KEY_CELL_FORMAT_DATA_DTL_WRAP_BORDER_TOP_ONLY_STRING,
				styleWrapBorderTopOnlyString);
		
		// add by alex [start]
		CellStyle styleWarning = book.createCellStyle();
		styleWarning.setFillForegroundColor(HSSFColorPredefined.LIGHT_YELLOW.getIndex());
		styleWarning.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleWarning.setBorderBottom(BorderStyle.MEDIUM);
		styleWarning.setBorderLeft(BorderStyle.MEDIUM);
		mapCellFormat.put(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_LABEL_WARNING,
				styleWarning);
		// add by alex [end]
		
		// add by alex [start]
		CellStyle styleHeaderGreen = book.createCellStyle();
		styleHeaderGreen.setAlignment(HorizontalAlignment.CENTER);
		styleHeaderGreen.setVerticalAlignment(VerticalAlignment.CENTER);
		styleHeaderGreen.setBorderBottom(BorderStyle.MEDIUM);
		styleHeaderGreen.setBorderTop(BorderStyle.MEDIUM);
		styleHeaderGreen.setBorderLeft(BorderStyle.MEDIUM);
		styleHeaderGreen.setBorderRight(BorderStyle.MEDIUM);
		styleHeaderGreen.setFillForegroundColor(HSSFColorPredefined.LIGHT_GREEN.getIndex());
		styleHeaderGreen.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont headerFontGreen = book.createFont();
		headerFontGreen.setFontHeightInPoints((short) 10);
		headerFontGreen.setFontName("Arial");
		headerFontGreen.setColor(HSSFColorPredefined.BLACK.getIndex());
		headerFontGreen.setBold(true);

		styleHeaderGreen.setFont(headerFont);
		mapCellFormat.put(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER_GREEN, styleHeaderGreen);
		// add by alex [end]
	}

	/*
	 * public void writeExcelDetail(XSSFSheet sheet, String tableName,
	 * SearchFilter2 searchFilter, int startRow, Map<String, CellStyle>
	 * mapCellFormat, CommonReportService commonReportService) throws Exception
	 * { // logger.debug("call searchTableData..."); Map<String, Object> map =
	 * commonReportService.searchTableData( tableName, searchFilter);
	 * 
	 * @SuppressWarnings("unchecked") List<String> listColumnNameAlias =
	 * (List<String>) map .get(CommonConstants.MAP_KEY_COLUMN_NAME_ALIAS);
	 * 
	 * @SuppressWarnings("unchecked") List<Object[]> results = (List<Object[]>)
	 * map .get(CommonConstants.MAP_KEY_RESULTS);
	 * 
	 * // write column header [start] // logger.debug("listColumnNameAlias = " +
	 * listColumnNameAlias); CellStyle cfColumnHeader = mapCellFormat
	 * .get(CommonConstants.MAP_KEY_CELL_FORMAT_COLUMN_HEADER); if
	 * (listColumnNameAlias != null && !listColumnNameAlias.isEmpty()) {
	 * logger.debug("listColumnNameAlias = " + listColumnNameAlias.size()); int
	 * rowIndex = startRow; int columnIndex = 0; for (String columnNameAlias :
	 * listColumnNameAlias) { Row rowTemp = sheet.getRow(rowIndex); if (rowTemp
	 * == null) { rowTemp = sheet.createRow(rowIndex); } Cell cell =
	 * rowTemp.createCell(columnIndex); cell.setCellValue(columnNameAlias);
	 * cell.setCellStyle(cfColumnHeader);
	 * //sheet.createRow(rowIndex).createCell(
	 * columnIndex).setCellValue(columnNameAlias);
	 * //sheet.createRow(rowIndex).createCell
	 * (columnIndex).setCellStyle(cfColumnHeader); columnIndex++; } } // write
	 * column header [end]
	 * 
	 * // write data detail [start] if (results != null && !results.isEmpty()) {
	 * int rowIndex = startRow + 1; for (Object[] arrObj : results) { for (int
	 * columnIndex = 0; columnIndex < arrObj.length; columnIndex++) {
	 * this.writeCellDetail(sheet, rowIndex, columnIndex, arrObj[columnIndex],
	 * mapCellFormat); } rowIndex++; } } // write data detail [end] }
	 */

	public void writeCellDetail(XSSFSheet sheet, int row, int column,
			Object value, Map<String, CellStyle> mapCellFormat)
			throws Exception {

		CellStyle cellFormatNumber = (CellStyle) mapCellFormat
				.get(CommonConstants.MAP_KEY_CELL_FORMAT_DATA_DTL_NUMBER);
		CellStyle cellFormatDate = (CellStyle) mapCellFormat
				.get(CommonConstants.MAP_KEY_CELL_FORMAT_DATA_DTL_DATE);
		CellStyle cellFormatString = (CellStyle) mapCellFormat
				.get(CommonConstants.MAP_KEY_CELL_FORMAT_DATA_DTL_STRING);
		CellStyle cellFormatDecimal = (CellStyle) mapCellFormat
				.get(CommonConstants.MAP_KEY_CELL_FORMAT_DATA_DTL_DECIMAL);

		if (value == null) {
			this.writeCell(sheet, row, column, value, cellFormatString);
		} else if (StringUtils.isNotBlank(value.toString()) && value.toString().charAt(value.toString().length() - 1) == '%') {
			this.writeCell(sheet, row, column, value, cellFormatNumber);
		} else if (value instanceof String) {
			this.writeCell(sheet, row, column, value, cellFormatString);
		} else if (value instanceof Integer) {
			this.writeCell(sheet, row, column, value, cellFormatNumber);
		} else if (value instanceof Double) {
			this.writeCell(sheet, row, column, value, cellFormatDecimal);
		} else if (value instanceof BigDecimal) {
			this.writeCell(sheet, row, column, value, cellFormatNumber);
		} else if (value instanceof Long) {
			this.writeCell(sheet, row, column, value, cellFormatNumber);
		}else if (value instanceof Date) {
			this.writeCell(sheet, row, column, value, cellFormatDate);
		} else if (value instanceof java.sql.Date) {
			this.writeCell(sheet, row, column, value, cellFormatDate);
		} else {
			this.writeCell(sheet, row, column, value, cellFormatString);
		}
	}
	
	public void writeCellDetailWrapText(XSSFSheet sheet, int row, int column,
			Object value, Map<String, CellStyle> mapCellFormat)
			throws Exception {

		CellStyle cellFormatNumber = (CellStyle) mapCellFormat
				.get(CommonConstants.MAP_KEY_CELL_FORMAT_DATA_DTL_NUMBER);
		CellStyle cellFormatDate = (CellStyle) mapCellFormat
				.get(CommonConstants.MAP_KEY_CELL_FORMAT_DATA_DTL_DATE);
		CellStyle cellWrapString = (CellStyle) mapCellFormat.get(CommonConstants.MAP_KEY_CELL_FORMAT_DATA_DTL_WRAP_STRING);

		if (value == null) {
			this.writeCell(sheet, row, column, value, cellWrapString);
		} else if (StringUtils.isNotBlank(value.toString()) && value.toString().charAt(value.toString().length() - 1) == '%') {
			this.writeCell(sheet, row, column, value, cellFormatNumber);
		} else if (value instanceof String) {
			this.writeCell(sheet, row, column, value, cellWrapString);
		} else if (value instanceof Integer) {
			this.writeCell(sheet, row, column, value, cellFormatNumber);
		} else if (value instanceof Double) {
			this.writeCell(sheet, row, column, value, cellFormatNumber);
		} else if (value instanceof BigDecimal) {
			this.writeCell(sheet, row, column, value, cellFormatNumber);
		} else if (value instanceof Long) {
			this.writeCell(sheet, row, column, value, cellFormatNumber);
		} else if (value instanceof Date) {
			this.writeCell(sheet, row, column, value, cellFormatDate);
		} else if (value instanceof java.sql.Date) {
			this.writeCell(sheet, row, column, value, cellFormatDate);
		} else {
			this.writeCell(sheet, row, column, value, cellWrapString);
		}
	}
	
	public void writeWrapCellDetail(XSSFSheet sheet, int row, int column,
			Object value, Map<String, CellStyle> mapCellFormat)
			throws Exception {

		CellStyle cellFormatNumber = (CellStyle) mapCellFormat
				.get(CommonConstants.MAP_KEY_CELL_FORMAT_DATA_DTL_NUMBER);
		CellStyle cellFormatDate = (CellStyle) mapCellFormat
				.get(CommonConstants.MAP_KEY_CELL_FORMAT_DATA_DTL_DATE);
		CellStyle cellFormatString = (CellStyle) mapCellFormat
				.get(CommonConstants.MAP_KEY_CELL_FORMAT_DATA_DTL_STRING);
		CellStyle cellFormatWrapString = (CellStyle) mapCellFormat
				.get(CommonConstants.MAP_KEY_CELL_FORMAT_DATA_DTL_WRAP_STRING);

		if (value == null) {
			this.writeCell(sheet, row, column, value, cellFormatString);
		} else if (StringUtils.isNotBlank(value.toString()) && value.toString().charAt(value.toString().length() - 1) == '%') {
			this.writeCell(sheet, row, column, value, cellFormatNumber);
		} else if (value instanceof String) {
			this.writeCell(sheet, row, column, value, cellFormatWrapString);
		} else if (value instanceof Integer) {
			this.writeCell(sheet, row, column, value, cellFormatNumber);
		} else if (value instanceof Double) {
			this.writeCell(sheet, row, column, value, cellFormatNumber);
		} else if (value instanceof BigDecimal) {
			this.writeCell(sheet, row, column, value, cellFormatNumber);
		} else if (value instanceof Long) {
			this.writeCell(sheet, row, column, value, cellFormatNumber);
		}else if (value instanceof Date) {
			this.writeCell(sheet, row, column, value, cellFormatDate);
		} else if (value instanceof java.sql.Date) {
			this.writeCell(sheet, row, column, value, cellFormatDate);
		} else {
			this.writeCell(sheet, row, column, value, cellFormatString);
		}
	}

	public void writeCell(XSSFSheet sheet, int row, int column, Object value,
			CellStyle cf) throws Exception {

		Row rowTemp = sheet.getRow(row);
		if (rowTemp == null) {
			rowTemp = sheet.createRow(row);
		}

		Cell cell = rowTemp.createCell(column);

		// logger.debug("value Write Cell = " + value);

		cell.setCellStyle(cf);
		if (value == null) {
			cell.setCellValue(StringUtils.EMPTY);
		} else if (value instanceof String) {
			cell.setCellValue(value != null ? (String) value
					: StringUtils.EMPTY);
		} else if (value instanceof Integer) {
			cell.setCellValue(value != null ? (Integer) value : 0);
		} else if (value instanceof Double) {
			cell.setCellValue(value != null ? (Double) value : 0);
		} else if (value instanceof BigDecimal) {
			cell.setCellValue(value != null ? ((BigDecimal) value)
					.doubleValue() : 0);
		} else if (value instanceof BigInteger) {
			cell.setCellValue(value != null ? ((BigInteger) value)
					.doubleValue() : 0);
		}else if (value instanceof Long) {
			cell.setCellValue(value != null ? (Long) value : 0);
		} else if (value instanceof Date) {
			SimpleDateFormat sdf = createDateFormatWithDateOnly();
			cell.setCellValue(sdf.format((Date) value));
		} else if (value instanceof java.sql.Date) {
			value = new Date(((java.sql.Date) value).getTime());
			SimpleDateFormat sdf = createDateFormatWithDateOnly();
			cell.setCellValue(sdf.format((Date) value));
		} else {
			// logger.debug("value = " + value.getClass().getName());
			// logger.debug("value = " + value);
			throw new Exception("Unmapped excel value type...");
		}
	}

	// public void writeCellTitle(WritableSheet sheet, int mergeColumn,
	// String title, Map<String, WritableCellFormat> mapCellFormat)
	// throws WriteException {
	// WritableCellFormat cfHeaderTitle = mapCellFormat
	// .get(CommonReportBean.MAP_KEY_CELL_FORMAT_HEADER_TITLE);
	//
	// sheet.mergeCells(0, 0, mergeColumn, 0);
	// sheet.addCell(new Label(0, 0, title, cfHeaderTitle));
	// }

	public void writeCellTitle(XSSFSheet sheet, int mergeColumn, String title,
			Map<String, CellStyle> mapCellFormat) throws Exception {
		CellStyle cfHeaderTitle = mapCellFormat
				.get(CommonConstants.MAP_KEY_CELL_FORMAT_HEADER_TITLE);

		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue(title);
		cell.setCellStyle(cfHeaderTitle);

		sheet.addMergedRegion(new CellRangeAddress(0, // mention first row here
				0, // mention last row here, it is 4 as we are doing a row wise
					// merging
				0, // mention first column of merging
				mergeColumn // mention last column to include in merge
		));

	}
	
	public void mergedHeaderColumnStyle(CellRangeAddress cellRangeAddress, XSSFSheet sheet) {
		RegionUtil.setBorderTop(BorderStyle.MEDIUM, cellRangeAddress, sheet);
		RegionUtil.setBorderRight(BorderStyle.MEDIUM, cellRangeAddress, sheet);
		RegionUtil.setBorderBottom(BorderStyle.MEDIUM, cellRangeAddress, sheet);
		RegionUtil.setBorderLeft(BorderStyle.MEDIUM, cellRangeAddress, sheet);
	}
	
	public void mergedContentColumnStyle(CellRangeAddress cellRangeAddress, XSSFSheet sheet) {
		RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
	}
	
	public void mergedContentColumnStyleNoBottom(CellRangeAddress cellRangeAddress, XSSFSheet sheet) {
		RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
//		RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
	}
	
	public void mergedContentColumnStyleNoTop(CellRangeAddress cellRangeAddress, XSSFSheet sheet) {
		RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
//		RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
	}

	public static SimpleDateFormat createDateFormatWithDateOnly() {
		return new SimpleDateFormat("dd-MMM-yyyy");
	}
	
	public static boolean isRowEmpty(Row row) {
	    for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
	        Cell cell = row.getCell(c);
	        if (cell != null && cell.getCellType() != CellType.BLANK)
	            return false;
	    }
	    return true;
	}
}
