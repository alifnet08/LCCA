package com.wo.module.tmpCorrespondence.bean;

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
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.regulationSocialization.vo.StatusConfirmationVO;
import com.wo.module.tmpCorrespondence.constant.TmpCorrespondenceConstants;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondence;
import com.wo.module.tmpCorrespondence.service.TmpCorrespondenceService;
import com.wo.module.tmpCorrespondence.vo.TmpCorrespondenceSearchVo;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class TmpCorrespondenceBean extends CommonBean implements Serializable {
	static Logger logger = Logger.getLogger(TmpCorrespondenceBean.class);
	private static final long serialVersionUID = -7542280143731129467L;

	/*
	 * Search property
	 */
	private String searchPengirim;
	private String searchNoSurat;
	private Date searchTanggalTerimaSuratFrom;
	private Date searchTanggalTerimaSuratTo;
	private Date searchTanggalSuratFrom;
	private Date searchTanggalSuratTo;
	private String searchPerihal;
	private Date searchTargetDateFrom;
	private Date searchTargetDateTo;
	private String searchStatus;
	private List<SelectItem> selectSender;
	private List<SelectItem> statusList;
	private DBLazyDataModel<TmpCorrespondenceSearchVo> tableCorrespondence;
	private String navigateEdit = TmpCorrespondenceConstants.NAVIGATE_EDIT;
	private Long userDivsionId;
	
	
	/*
	 * services
	 */
	private TmpCorrespondenceService tmpCorrespondenceService;
	private UserService userService;
	//private ParameterDetailService parameterDetailService;
	
	/*
	 * util
	 */
	private FacesUtil facesUtil;
	private FileUtil fileUtil;
	
	@SuppressWarnings("rawtypes")
	@PostConstruct
	public void construct() {
		super.init();
		User getUser = facesUtil.getUserLogin();
		this.userDivsionId = getUser.getDivisionId();
		tableCorrespondence = new DBLazyDataModel<TmpCorrespondenceSearchVo>(tmpCorrespondenceService, getPaging());
		
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (getUser.getDivisionId() != null && getUser.getDivisionId() > 0) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceConstants.SEARCH_DIVISION, getUser.getDivisionId()));
		}

		tableCorrespondence.setSearchCriteria(searchCriteria);
		
		populateSelect();
		fileUtil = FileUtil.getInstance();
	}
	
	@SuppressWarnings("unused")
	public void postProcessXLS(Object document) {
		
		try {
			Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
			
			HSSFWorkbook wb = (HSSFWorkbook) document;
			HSSFSheet sheet = wb.getSheetAt(0);
			
			User userLogin = userService.getUserByNik(facesUtil.retrieveUserLogin());
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			List<TmpCorrespondenceSearchVo> listDataXls = tmpCorrespondenceService.searchDataXLS(
					Arrays.asList(
							new DefaultSearchObject(TmpCorrespondenceConstants.SEARCH_PENGIRIM, searchPengirim),
							new DefaultSearchObject(TmpCorrespondenceConstants.SEARCH_NO_SURAT, searchNoSurat),
							new DefaultSearchObject(TmpCorrespondenceConstants.SEARCH_TANGGAL_SURAT_FROM, 
									searchTanggalSuratFrom != null ? sdf.format(searchTanggalSuratFrom) : ""),
							new DefaultSearchObject(TmpCorrespondenceConstants.SEARCH_TANGGAL_SURAT_TO, 
									searchTanggalSuratTo != null ? sdf.format(searchTanggalSuratTo) : ""),
							new DefaultSearchObject(TmpCorrespondenceConstants.SEARCH_TANGGAL_TERIMA_SURAT_FROM, 
									searchTanggalTerimaSuratFrom != null ? sdf.format(searchTanggalTerimaSuratFrom) : ""),
							new DefaultSearchObject(TmpCorrespondenceConstants.SEARCH_TANGGAL_TERIMA_SURAT_TO, 
									searchTanggalTerimaSuratTo != null ? sdf.format(searchTanggalTerimaSuratTo) : ""),
							new DefaultSearchObject(TmpCorrespondenceConstants.SEARCH_STATUS, searchStatus),
							new DefaultSearchObject(TmpCorrespondenceConstants.SEARCH_PERIHAL, searchPerihal),
							new DefaultSearchObject(TmpCorrespondenceConstants.SEARCH_DIVISION, userDivsionId)/*,
							new DefaultSearchObject(CommonConstants.SEARCH_BY_USER_LOGIN, userLogin.getUserId())*/));
			
			//create header
			HSSFRow header = sheet.createRow(0);
			for (int i = 0; i < 19; i++) {
				HSSFCell cell = header.createCell((short) i);
				if (i == 0) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpCorrespondenceSender"));
				} else if (i == 1) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpCorrespondenceLetterReceiveDate"));
				} else if (i == 2) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpCorrespondenceLetterNo"));
				} else if (i == 3) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpCorrespondenceLetterDate"));
				} else if (i == 4) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpCorrespondencePerihal"));
				} else if (i == 5) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpCorrespondenceLetterSummary"));
				} else if (i == 6) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpCorrespondenceSearchStatus"));
				} else if (i == 7) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpCorrespondenceFollowup"));
				} else if (i == 8) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpCorrespondenceTargetDate"));
				} else if (i == 9) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpCorrespondencePICName"));
				} else if (i == 10) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpCorrespondenceSupportingName"));
				} else if (i == 11) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpCorrespondencePicFollowupStatus"));
				} else if (i == 12) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpCorrespondencePICConfirmation"));
				} else if (i == 13) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpCorrespondenceConfirmationDate"));
				} else if (i == 14) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpCorrespondenceFollowupDate"));
				} else if (i == 15) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpCorrespondenceInformation"));
				} else if (i == 16) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpCorrespondenceFollowupFulfillmentDate"));
				} else if (i == 17) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpCorrespondenceComplianceCheckerStatus"));
				} else if (i == 18) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpCorrespondencePICAttendance"));
				}
			}
			
			//kosongin data
			int rowNum = 1;
			for(int i=0;i<19;i++) {
				HSSFRow row = sheet.createRow(rowNum);
				for (int x = 0; x < 19; x++) {
					HSSFCell cell = row.createCell((short) x);
					cell.setCellValue("");
				}
				rowNum++;
			}
			//kosongin data
			
			//createData
			rowNum = 1;
			String picName = "";
			String followUpStatus = "";
			String confirmationDate = "";
			String followupDate = "";
			String followupBy = "";
			String information = "";
			String compilanceStatus = "";
			
			for (int i = 0; i < listDataXls.size(); i++) {
				TmpCorrespondenceSearchVo tcs = (TmpCorrespondenceSearchVo) listDataXls.get(i);
				for (int j = 0; j < tcs.getStatusList().size(); j++) {
					StatusConfirmationVO vo = tcs.getStatusList().get(j);
				    picName = vo.getNamePic();
					followUpStatus = vo.getFollowupStatus();
					confirmationDate = vo.getConfirmationDate();
					followupDate = vo.getFollowupDate();
					followupBy = vo.getFollowupBy();
					information = vo.getFollowupNote();
					compilanceStatus = vo.getComplianceStatus();
				}
				
				HSSFRow row = sheet.createRow(rowNum);
				for (int x = 0; x < 19; x++) {
					HSSFCell cell = row.createCell((short) x);
					if (x == 0) {
						cell.setCellValue(tcs.getSenderName() != null ? tcs.getSenderName() : "");
					} else if (x == 1) {
						cell.setCellValue(tcs.getLetterReceivedDateStr() != null ? tcs.getLetterReceivedDateStr() : "");
					} else if (x == 2) {
						cell.setCellValue(tcs.getLetterNo() != null ? tcs.getLetterNo() : "");
					} else if (x == 3) {
						cell.setCellValue(tcs.getLetterDateStr() != null ? tcs.getLetterDateStr() : "");
					} else if (x == 4) {
						cell.setCellValue(tcs.getPerihalName() != null ? tcs.getPerihalName() : "");
					} else if (x == 5) {
						cell.setCellValue(tcs.getLetterSummary() != null ? tcs.getLetterSummary() : "");
					} else if (x == 6) {
						cell.setCellValue(tcs.getStatusName() != null ? tcs.getStatusName() : "");
					} else if (x == 7) {
						cell.setCellValue(tcs.getFollowUp() != null ? tcs.getFollowUp() : "");
					} else if (x == 8) {
						cell.setCellValue(tcs.getTargetDateStr() != null ? tcs.getTargetDateStr() : "");
					} else if (x == 9) {
						cell.setCellValue(picName);
					} else if (x == 10) {
						cell.setCellValue(tcs.getSupportingUnitName() != null ? tcs.getSupportingUnitName() : "");
					} else if (x == 11) {
						cell.setCellValue(followUpStatus);
					} else if (x == 12) {
						cell.setCellValue(followupBy);
					} else if (x == 13) {
						cell.setCellValue(confirmationDate);
					} else if (x == 14) {
						cell.setCellValue(followupDate);
					} else if (x == 15) {
						cell.setCellValue(information);
					} else if (x == 16) {
						cell.setCellValue(tcs.getFullfillmentDateStr() != null ? tcs.getFullfillmentDateStr() : "");
					} else if (x == 17) {
						cell.setCellValue(compilanceStatus);
					} else if (x == 18) {
						cell.setCellValue(tcs.getPicAttendeeName() != null  ? tcs.getPicAttendeeName() : "");
					}
				}
				rowNum++;
			}
			
			// style
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
	
	private void populateSelect() {
		try {
			selectSender = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_SENDER, false, true, facesUtil.retrieveDefaultLocale());
			statusList = new ArrayList<SelectItem>();
			List<ParameterDetail> listParamDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_DATA_STATUS);
			
			for (ParameterDetail vo : listParamDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				statusList.add(si);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
//		RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}
	
	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (searchPengirim != null && !searchPengirim.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceConstants.SEARCH_PENGIRIM, searchPengirim));
		}

		if (searchNoSurat != null && !searchNoSurat.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceConstants.SEARCH_NO_SURAT, searchNoSurat));
		}

		if (searchTanggalTerimaSuratFrom != null) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceConstants.SEARCH_TANGGAL_TERIMA_SURAT_FROM,
					searchTanggalTerimaSuratFrom != null ? sdf.format(searchTanggalTerimaSuratFrom) : ""));
		}

		if (searchTanggalTerimaSuratTo != null) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceConstants.SEARCH_TANGGAL_TERIMA_SURAT_TO, 
					searchTanggalTerimaSuratTo != null ? sdf.format(searchTanggalTerimaSuratTo) : ""));
		}
		
		if (searchTanggalSuratFrom != null) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceConstants.SEARCH_TANGGAL_SURAT_FROM,
					searchTanggalSuratFrom != null ? sdf.format(searchTanggalSuratFrom) : "" ));
		}

		if (searchTanggalSuratTo != null) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceConstants.SEARCH_TANGGAL_SURAT_TO, 
					searchTanggalSuratTo != null ? sdf.format(searchTanggalSuratTo) : ""));
		}
		
		if (searchPerihal != null && !searchPerihal.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceConstants.SEARCH_PERIHAL, searchPerihal));
		}

		if (searchTargetDateFrom != null) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceConstants.SEARCH_TARGET_DATE_FROM, 
					searchTargetDateFrom != null ? sdf.format(searchTargetDateFrom) : ""));
		}

		if (searchTargetDateTo != null) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceConstants.SEARCH_TARGET_DATE_TO,
					searchTargetDateTo != null ? sdf.format(searchTargetDateTo) : ""));
		}

		if (searchStatus != null && !searchStatus.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceConstants.SEARCH_STATUS, searchStatus));
		}
		
		if (userDivsionId != null && userDivsionId > 0) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceConstants.SEARCH_DIVISION, userDivsionId));
		}
		
		/*User userLogin = userService.getUserByNik(facesUtil.retrieveUserLogin());
		if (userLogin != null) {
			searchCriteria.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_USER_LOGIN, userLogin.getUserId()));
		}*/

		tableCorrespondence.setSearchCriteria(searchCriteria);
	}

	public void reset(ActionEvent actionEvent) {
		searchPengirim = "";
		searchNoSurat = "";
		searchTanggalTerimaSuratFrom = null;
		searchTanggalTerimaSuratTo = null;
		searchTanggalSuratFrom = null;
		searchTanggalSuratTo = null;
		searchPerihal = null;
		searchTargetDateFrom = null;
		searchTargetDateTo = null;
		searchStatus = null;
		userDivsionId = this.getUserDivsionId();
		search(actionEvent);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void delete(Long deleteId) {
		try {
			TmpCorrespondence entity = tmpCorrespondenceService.findById(deleteId);
			entity.setEnabledFlag(Constants.CONSTANT_NO);
			entity.setLastUpdateBy(facesUtil.retrieveUserLogin());
			entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
			
			tmpCorrespondenceService.update(entity);
			facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public String getSearchPengirim() {
		return searchPengirim;
	}

	public void setSearchPengirim(String searchPengirim) {
		this.searchPengirim = searchPengirim;
	}

	public String getSearchNoSurat() {
		return searchNoSurat;
	}

	public void setSearchNoSurat(String searchNoSurat) {
		this.searchNoSurat = searchNoSurat;
	}

	public Date getSearchTanggalTerimaSuratFrom() {
		return searchTanggalTerimaSuratFrom;
	}

	public void setSearchTanggalTerimaSuratFrom(Date searchTanggalTerimaSuratFrom) {
		this.searchTanggalTerimaSuratFrom = searchTanggalTerimaSuratFrom;
	}

	public Date getSearchTanggalTerimaSuratTo() {
		return searchTanggalTerimaSuratTo;
	}

	public void setSearchTanggalTerimaSuratTo(Date searchTanggalTerimaSuratTo) {
		this.searchTanggalTerimaSuratTo = searchTanggalTerimaSuratTo;
	}

	public Date getSearchTanggalSuratFrom() {
		return searchTanggalSuratFrom;
	}

	public void setSearchTanggalSuratFrom(Date searchTanggalSuratFrom) {
		this.searchTanggalSuratFrom = searchTanggalSuratFrom;
	}

	public Date getSearchTanggalSuratTo() {
		return searchTanggalSuratTo;
	}

	public void setSearchTanggalSuratTo(Date searchTanggalSuratTo) {
		this.searchTanggalSuratTo = searchTanggalSuratTo;
	}

	public String getSearchPerihal() {
		return searchPerihal;
	}

	public void setSearchPerihal(String searchPerihal) {
		this.searchPerihal = searchPerihal;
	}

	public Date getSearchTargetDateFrom() {
		return searchTargetDateFrom;
	}

	public void setSearchTargetDateFrom(Date searchTargetDateFrom) {
		this.searchTargetDateFrom = searchTargetDateFrom;
	}

	public Date getSearchTargetDateTo() {
		return searchTargetDateTo;
	}

	public void setSearchTargetDateTo(Date searchTargetDateTo) {
		this.searchTargetDateTo = searchTargetDateTo;
	}

	public DBLazyDataModel<TmpCorrespondenceSearchVo> getTableCorrespondence() {
		return tableCorrespondence;
	}

	public void setTableCorrespondence(DBLazyDataModel<TmpCorrespondenceSearchVo> tableCorrespondence) {
		this.tableCorrespondence = tableCorrespondence;
	}

	public TmpCorrespondenceService getTmpCorrespondenceService() {
		return tmpCorrespondenceService;
	}

	public void setTmpCorrespondenceService(TmpCorrespondenceService tmpCorrespondenceService) {
		this.tmpCorrespondenceService = tmpCorrespondenceService;
	}

	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	/*public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}*/

	public List<SelectItem> getSelectSender() {
		return selectSender;
	}

	public void setSelectSender(List<SelectItem> selectSender) {
		this.selectSender = selectSender;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public String getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}

	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public Long getUserDivsionId() {
		return userDivsionId;
	}

	public void setUserDivsionId(Long userDivsionId) {
		this.userDivsionId = userDivsionId;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
	
}
