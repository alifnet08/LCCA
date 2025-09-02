package com.wo.module.outgoingLetter.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.outgoingLetter.constant.OutgoingLetterConstants;
import com.wo.module.outgoingLetter.model.OutgoingLetter;
import com.wo.module.outgoingLetter.service.OutgoingLetterService;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class OutgoingLetterBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -2651496569669107542L;
	static Logger logger = Logger.getLogger(OutgoingLetterBean.class);
	
	private String tujuanSurat;
	private String perihal;
	private String nomorSurat;
	private String sampaikanKepada;
	private Date tanggalSuratFrom;
	private Date tanggalSuratTo;
	private String tembusanSurat;
	private Long userDivisionId;
	
	private String navigateEdit = OutgoingLetterConstants.NAVIGATE_EDIT;
	
	private OutgoingLetterService outgoingLetterService;
	private UserService userService;
	
	private DBLazyDataModel<OutgoingLetter> tableModel;
	
	private FacesUtil facesUtil;
	private FileUtil fileUtil;
	
	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	@SuppressWarnings("unused")
	public void postProcessXLS (Object document) {
		try {
			
			Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
			
			HSSFWorkbook wb = (HSSFWorkbook) document;
			HSSFSheet sheet = wb.getSheetAt(0);
			
			User userLogin = userService.getUserByNik(facesUtil.retrieveUserLogin());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			List<OutgoingLetter> listDataXLS = outgoingLetterService.searchDataXLS(
					Arrays.asList(
							new DefaultSearchObject(OutgoingLetterConstants.WHERE_LETTER_PURPOSE,tujuanSurat),
							new DefaultSearchObject(OutgoingLetterConstants.WHERE_LETTER_NO, nomorSurat),
							new DefaultSearchObject(OutgoingLetterConstants.WHERE_LETTER_DATE_FROM,
									tanggalSuratFrom != null ? sdf.format(tanggalSuratFrom) : ""),
							new DefaultSearchObject(OutgoingLetterConstants.WHERE_LETTER_DATE_TO, 
									tanggalSuratTo != null ? sdf.format(tanggalSuratTo) : ""),
							new DefaultSearchObject(OutgoingLetterConstants.WHERE_PERIHAL, perihal),
//							new DefaultSearchObject(OutgoingLetterConstants.WHERE_DELIVERED_TO, sampaikanKepada),
							new DefaultSearchObject(OutgoingLetterConstants.WHERE_TEMBUSAN, tembusanSurat),
							new DefaultSearchObject(OutgoingLetterConstants.WHERE_DIVISION, userDivisionId)/*,
							new DefaultSearchObject(CommonConstants.SEARCH_BY_USER_LOGIN, userLogin.getUserId())*/
							));
			
			//create header
			HSSFRow header = sheet.createRow(0);
			for (int i = 0; i < 5; i++) {
				HSSFCell cell = header.createCell((short) i);
				if ( i == 0) {
					cell.setCellValue(facesUtil.retrieveMessage("formOutgoingLetterPurpose"));
				} else if ( i == 1) {
					cell.setCellValue(facesUtil.retrieveMessage("formOutgoingLetterNo"));
				} else if ( i == 2) {
					cell.setCellValue(facesUtil.retrieveMessage("formOutgoingLetterDate"));
				} else if ( i == 3) {
					cell.setCellValue(facesUtil.retrieveMessage("formOutgoingLetterPerihal"));
				} else if ( i == 4) {
					cell.setCellValue(facesUtil.retrieveMessage("formOutgoingLetterCarbonCopyNatation"));
				}
			}
			
			// kosingin data
			int rowNum = 1;
			for (int i = 0; i < 5; i++) {
				HSSFRow row = sheet.createRow(rowNum);
				for (int x = 0; x < 5; x++) {
					HSSFCell cell = row.createCell((short) x);
					cell.setCellValue("");
				}
			}
			
			//create Data
			rowNum = 1;
			
			for (int i = 0; i < listDataXLS.size(); i++) {
				OutgoingLetter ol = (OutgoingLetter) listDataXLS.get(i);
				HSSFRow row = sheet.createRow(rowNum);
				for (int x = 0; x < 5; x++) {
					HSSFCell cell = row.createCell((short) x);
					if (x == 0) {
						cell.setCellValue(ol.getLetterPursposeName() != null ? ol.getLetterPursposeName() : "");
					} else if (x == 1) {
						cell.setCellValue(ol.getLetterNo() != null ? ol.getLetterNo() : "");
					} else if (x == 2) {
						cell.setCellValue(ol.getLetterDateStr() != null ? ol.getLetterDateStr() : "");
					} else if (x == 3) {
						cell.setCellValue(ol.getPerihalName() != null ? ol.getPerihalName() : "");
					} else if (x == 4) {
						cell.setCellValue(ol.getTembusan() != null ? ol.getTembusan() : "");
					}
				}
				rowNum++;
			}
			
			//style
			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
				HSSFCell cell = header.getCell(i);
				cell.setCellStyle(cellStyle);
				sheet.autoSizeColumn(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("rawtypes")
	@PostConstruct
	public void init() {
		super.init();
		User getUserLogin = facesUtil.getUserLogin();
		this.userDivisionId = getUserLogin.getDivisionId();
		tableModel = new DBLazyDataModel<OutgoingLetter>(outgoingLetterService, paging);
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (getUserLogin.getDivisionId() != null && getUserLogin.getDivisionId() > 0) {
			searchCriteria.add(new DefaultSearchObject(OutgoingLetterConstants.WHERE_DIVISION, getUserLogin.getDivisionId()));
		}
		
		tableModel.setSearchCriteria(searchCriteria);
		
		fileUtil = FileUtil.getInstance();
	}
	
	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
		
		if(tujuanSurat != null && !tujuanSurat.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(OutgoingLetterConstants.WHERE_LETTER_PURPOSE, tujuanSurat));
		}
		
		if(nomorSurat != null && !nomorSurat.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(OutgoingLetterConstants.WHERE_LETTER_NO, nomorSurat));
		}
		
		if(perihal != null && !perihal.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(OutgoingLetterConstants.WHERE_PERIHAL, perihal));
		}
		
		if(tembusanSurat != null && !tembusanSurat.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(OutgoingLetterConstants.WHERE_TEMBUSAN, tembusanSurat));
		}
		
		if(sampaikanKepada != null && !sampaikanKepada.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(OutgoingLetterConstants.WHERE_DELIVERED_TO, sampaikanKepada));
		}
		
		if(tanggalSuratFrom != null) {
			searchCriteria.add(new DefaultSearchObject(OutgoingLetterConstants.WHERE_LETTER_DATE_FROM, 
					tanggalSuratFrom != null ? sdf.format(tanggalSuratFrom) : ""));
		}
		
		if (tanggalSuratTo != null) {
			searchCriteria.add(new DefaultSearchObject(OutgoingLetterConstants.WHERE_LETTER_DATE_TO, 
					tanggalSuratTo != null ? sdf.format(tanggalSuratTo) : ""));
		}
		
		if (userDivisionId != null && userDivisionId > 0) {
			searchCriteria.add(new DefaultSearchObject(OutgoingLetterConstants.WHERE_DIVISION, Long.toString(userDivisionId)));
		}
		
		/*User userLogin = userService.getUserByNik(facesUtil.retrieveUserLogin());
		if (userLogin != null) {
			searchCriteria.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_USER_LOGIN, userLogin.getUserId()));
		}*/
		
		tableModel.setSearchCriteria(searchCriteria);
	}
	
	public void reset(ActionEvent actionEvent) {
		tujuanSurat = "";
		tanggalSuratFrom = null;
		tanggalSuratTo = null;
		sampaikanKepada = "";
		perihal = "";
		nomorSurat = "";
		tembusanSurat = "";
		userDivisionId = this.getUserDivisionId();
	
		search(actionEvent);
	}
	
	public void delete(Long deleteId) {
		try {
			OutgoingLetter ol = outgoingLetterService.findById(deleteId);
			ol.setEnabledFlag(Constants.CONSTANT_NO);
			ol.setLastUpdateBy(facesUtil.retrieveUserLogin());
			ol.setLastUpdateDate(new Timestamp(new Date().getTime()));
			
			outgoingLetterService.update(ol);
			facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), " ");
		}
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public Boolean getIsLogin() {
		if(facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE) != null
				|| facesUtil.getSessionAttribute(Constants.SESSION_KANDIDAT) != null) {
			return true;
		}
		return false;
	}
	
	public String getTujuanSurat() {
		return tujuanSurat;
	}
	public void setTujuanSurat(String tujuanSurat) {
		this.tujuanSurat = tujuanSurat;
	}
	public String getPerihal() {
		return perihal;
	}
	public void setPerihal(String perihal) {
		this.perihal = perihal;
	}
	public String getNomorSurat() {
		return nomorSurat;
	}
	public void setNomorSurat(String nomorSurat) {
		this.nomorSurat = nomorSurat;
	}
	public String getSampaikanKepada() {
		return sampaikanKepada;
	}
	public void setSampaikanKepada(String sampaikanKepada) {
		this.sampaikanKepada = sampaikanKepada;
	}
	public Date getTanggalSuratFrom() {
		return tanggalSuratFrom;
	}
	public void setTanggalSuratFrom(Date tanggalSuratFrom) {
		this.tanggalSuratFrom = tanggalSuratFrom;
	}
	public Date getTanggalSuratTo() {
		return tanggalSuratTo;
	}
	public void setTanggalSuratTo(Date tanggalSuratTo) {
		this.tanggalSuratTo = tanggalSuratTo;
	}
	public String getTembusanSurat() {
		return tembusanSurat;
	}
	public void setTembusanSurat(String tembusanSurat) {
		this.tembusanSurat = tembusanSurat;
	}
	public String getNavigateEdit() {
		return navigateEdit;
	}
	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}
	public OutgoingLetterService getOutgoingLetterService() {
		return outgoingLetterService;
	}
	public void setOutgoingLetterService(OutgoingLetterService outgoingLetterService) {
		this.outgoingLetterService = outgoingLetterService;
	}
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}
	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}
	public FileUtil getFileUtil() {
		return fileUtil;
	}
	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public DBLazyDataModel<OutgoingLetter> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<OutgoingLetter> tableModel) {
		this.tableModel = tableModel;
	}

	public Long getUserDivisionId() {
		return userDivisionId;
	}

	public void setUserDivisionId(Long userDivisionId) {
		this.userDivisionId = userDivisionId;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
	
}