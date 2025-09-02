package com.wo.module.tmpFine.bean;

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
import com.wo.module.tmpFine.constant.TmpFineConstants;
import com.wo.module.tmpFine.model.TmpFine;
import com.wo.module.tmpFine.model.TmpFinePicCompliance;
import com.wo.module.tmpFine.service.TmpFineService;
import com.wo.module.tmpFine.vo.TmpFineSearchVo;
import com.wo.module.trcFineApproval.constant.TrcFineApprovalConstants;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowup;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class TmpFineBean extends CommonBean implements Serializable {
	static Logger logger = Logger.getLogger(TmpFineBean.class);
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
	private DBLazyDataModel<TmpFineSearchVo> tableFine;
	private String navigateEdit = TmpFineConstants.NAVIGATE_EDIT;
	
	/*
	 * services
	 */
	private TmpFineService tmpFineService;
	private UserService userService;
	//private ParameterDetailService parameterDetailService;
	
	/*
	 * util
	 */
	private FacesUtil facesUtil;
	private FileUtil fileUtil;
	
	@PostConstruct
	public void construct() {
		super.init();
		tableFine = new DBLazyDataModel<TmpFineSearchVo>(tmpFineService, getPaging());
		
		populateSelect();
		fileUtil = FileUtil.getInstance();
		
		User userLogin = userService.getUserByNik(facesUtil.retrieveUserLogin());
		
		List<SearchObject> searchCriteria = new ArrayList<>();
		if (userLogin != null) {
			searchCriteria.add(new DefaultSearchObject(TrcFineApprovalConstants.SEARCH_BY_USER_LOGIN, userLogin.getUserId()));
		}
		
		tableFine.setSearchCriteria(searchCriteria);
		
	}
	
	@SuppressWarnings("unused")
	public void postProcessXLS(Object document) {
		
		try {
			Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
			
			HSSFWorkbook wb = (HSSFWorkbook) document;
			HSSFSheet sheet = wb.getSheetAt(0);
			
			
			User userLogin = userService.getUserByNik(facesUtil.retrieveUserLogin());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat(inputDateFormat);
			List<TmpFineSearchVo> listDataXls = tmpFineService.searchDataXLS(
					Arrays.asList(
							new DefaultSearchObject(TmpFineConstants.SEARCH_PENGIRIM, searchPengirim),
							new DefaultSearchObject(TmpFineConstants.SEARCH_NO_SURAT, searchNoSurat),
							new DefaultSearchObject(TmpFineConstants.SEARCH_TANGGAL_SURAT_FROM, 
									searchTanggalSuratFrom != null ? sdf.format(searchTanggalSuratFrom) : ""),
							new DefaultSearchObject(TmpFineConstants.SEARCH_TANGGAL_SURAT_TO, 
									searchTanggalSuratTo != null ? sdf.format(searchTanggalSuratTo) : ""),
							new DefaultSearchObject(TmpFineConstants.SEARCH_TANGGAL_TERIMA_SURAT_FROM, 
									searchTanggalTerimaSuratFrom != null ? sdf.format(searchTanggalTerimaSuratFrom) : ""),
							new DefaultSearchObject(TmpFineConstants.SEARCH_TANGGAL_TERIMA_SURAT_TO, 
									searchTanggalTerimaSuratTo != null ? sdf.format(searchTanggalTerimaSuratTo) : ""),
							new DefaultSearchObject(TmpFineConstants.SEARCH_STATUS, searchStatus),
							new DefaultSearchObject(TmpFineConstants.SEARCH_PERIHAL, searchPerihal),
							new DefaultSearchObject(CommonConstants.SEARCH_BY_USER_LOGIN, userLogin.getUserId())));
			
			//create header
			HSSFRow header = sheet.createRow(0);
			for (int i = 0; i < 17; i++) {
				HSSFCell cell = header.createCell((short) i);
				if (i == 0) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpFineSender"));
				} else if (i == 1) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpFineLetterReceiveDate"));
				} else if (i == 2) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpFineLetterNo"));
				} else if (i == 3) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpFineLetterDate"));
				} else if (i == 4) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpFinePerihal"));
				} else if (i == 5) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpFineLetterSummary"));
				} else if (i == 6) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpFineSearchStatus"));
				} else if (i == 7) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpFineFollowup"));
				} else if (i == 8) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpFineTargetDate"));
				} else if (i == 9) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpFinePICName"));
				}  else if (i == 10) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpFinePicFollowupStatus"));
				} else if (i == 11) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpFinePICConfirmation"));
				} else if (i == 12) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpFineConfirmationDate"));
				} else if (i == 13) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpFineFollowupDate"));
				} else if (i == 14) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpFineInformation"));
				} else if (i == 15) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpFineFollowupFulfillmentDate"));
				} else if (i == 16) {
					cell.setCellValue(facesUtil.retrieveMessage("formTmpFineComplianceCheckerStatus"));
				} 
			}
			
			//kosongin data
			int rowNum = 1;
			for(int i=0;i<17;i++) {
				HSSFRow row = sheet.createRow(rowNum);
				for (int x = 0; x < 17; x++) {
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
			String targetDate = "";
			String complianceDate = "";
			
			for (int i = 0; i < listDataXls.size(); i++) {
				TmpFineSearchVo tcs = (TmpFineSearchVo) listDataXls.get(i);
				
				picName = "";
				followUpStatus = "";
				confirmationDate = "";
				followupDate = "";
			    followupBy = "";
				information = "";
				compilanceStatus = "";
				targetDate = "";
				complianceDate = "";
				
				for(int j = 0; j < tcs.getTrcFine().getTrcFinePicFollowups().size(); j++){
					TrcFinePicFollowup trcFinePicFollowup = tcs.getTrcFine().getTrcFinePicFollowups().get(j);
					if(picName != null && !picName.equals("")){
						picName = picName.concat(",").concat(trcFinePicFollowup.getUserId1()!=null?trcFinePicFollowup.getUserId1().getName():"");
					}else{
						picName = picName.concat(trcFinePicFollowup.getUserId1()!=null?trcFinePicFollowup.getUserId1().getName():"");
					}
					
					if(picName != null && !picName.equals("")){
						picName = picName.concat(",").concat(trcFinePicFollowup.getUserId2()!=null?trcFinePicFollowup.getUserId2().getName():"");
					}else{
						picName = picName.concat(trcFinePicFollowup.getUserId2()!=null?trcFinePicFollowup.getUserId2().getName():"");
					}
					
					if(picName != null && !picName.equals("")){
						picName = picName.concat(",").concat(trcFinePicFollowup.getUserId3()!=null?trcFinePicFollowup.getUserId3().getName():"");
					}else{
						picName = picName.concat(trcFinePicFollowup.getUserId3()!=null?trcFinePicFollowup.getUserId3().getName():"");
					}
					
					if(followUpStatus != null && !followUpStatus.equals("")){
						followUpStatus = followUpStatus.concat(",").concat(trcFinePicFollowup.getFollowupStatus()!=null?trcFinePicFollowup.getFollowupStatus().getName():"");
					}else{
						followUpStatus = followUpStatus.concat(trcFinePicFollowup.getFollowupStatus()!=null?trcFinePicFollowup.getFollowupStatus().getName():"");
					}
					
					if(confirmationDate != null && !confirmationDate.equals("")){
						confirmationDate = confirmationDate.concat(",").concat(trcFinePicFollowup.getConfirmationDate()!=null?sdf2.format(trcFinePicFollowup.getConfirmationDate()):"");
					}else{
						confirmationDate = confirmationDate.concat(trcFinePicFollowup.getConfirmationDate()!=null?sdf2.format(trcFinePicFollowup.getConfirmationDate()):"");
					}
					
					if(followupDate != null && !followupDate.equals("")){
						followupDate = followupDate.concat(",").concat(trcFinePicFollowup.getFollowupDate()!=null?sdf2.format(trcFinePicFollowup.getFollowupDate()):"");
					}else{
						followupDate = followupDate.concat(trcFinePicFollowup.getFollowupDate()!=null?sdf2.format(trcFinePicFollowup.getFollowupDate()):"");
					}
					
					if(targetDate != null && !targetDate.equals("")){
						targetDate = targetDate.concat(",").concat(trcFinePicFollowup.getTargetDate()!=null?sdf2.format(trcFinePicFollowup.getTargetDate()):"");
					}else{
						targetDate = targetDate.concat(trcFinePicFollowup.getTargetDate()!=null?sdf2.format(trcFinePicFollowup.getTargetDate()):"");
					}
					
					if(complianceDate != null && !complianceDate.equals("")){
						complianceDate = complianceDate.concat(",").concat(trcFinePicFollowup.getComplianceDate()!=null?sdf2.format(trcFinePicFollowup.getComplianceDate()):"");
					}else{
						complianceDate = complianceDate.concat(trcFinePicFollowup.getComplianceDate()!=null?sdf2.format(trcFinePicFollowup.getComplianceDate()):"");
					}
					
					if(followupBy != null && !followupBy.equals("")){
						followupBy = followupBy.concat(",").concat(trcFinePicFollowup.getFollowupBy()!=null?trcFinePicFollowup.getFollowupBy().getName():"");
					}else{
						followupBy = followupBy.concat(trcFinePicFollowup.getFollowupBy()!=null?trcFinePicFollowup.getFollowupBy().getName():"");
					}
					
					if(information != null && !information.equals("")){
						information = information.concat(",").concat(trcFinePicFollowup.getFollowupNote()!=null?trcFinePicFollowup.getFollowupNote():"");
					}else{
						information = information.concat(trcFinePicFollowup.getFollowupNote()!=null?trcFinePicFollowup.getFollowupNote():"");
					}
					
					if(compilanceStatus != null && !compilanceStatus.equals("")){
						compilanceStatus = compilanceStatus.concat(",").concat(trcFinePicFollowup.getComplianceStatus()!=null?trcFinePicFollowup.getComplianceStatus().getName():"");
					}else{
						compilanceStatus = compilanceStatus.concat(trcFinePicFollowup.getComplianceStatus()!=null?trcFinePicFollowup.getComplianceStatus().getName():"");
					}
					
				}
				
				HSSFRow row = sheet.createRow(rowNum);
				for (int x = 0; x < 17; x++) {
					HSSFCell cell = row.createCell((short) x);
					if (x == 0) {
						cell.setCellValue(tcs.getSenderName() != null ? tcs.getSenderName() : "");
					} else if (x == 1) {
						cell.setCellValue(tcs.getLetterReceivedDate() != null ? sdf2.format(tcs.getLetterReceivedDate())  : "");
					} else if (x == 2) {
						cell.setCellValue(tcs.getLetterNo() != null ? tcs.getLetterNo() : "");
					} else if (x == 3) {
						cell.setCellValue(tcs.getLetterDate() != null ? sdf2.format(tcs.getLetterDate()) : "");
					} else if (x == 4) {
						cell.setCellValue(tcs.getPerihalName() != null ? tcs.getPerihalName() : "");
					} else if (x == 5) {
						cell.setCellValue(tcs.getLetterSummary() != null ? tcs.getLetterSummary() : "");
					} else if (x == 6) {
						cell.setCellValue(tcs.getStatusName() != null ? tcs.getStatusName() : "");
					} else if (x == 7) {
						cell.setCellValue(tcs.getFollowUp() != null ? tcs.getFollowUp() : "");
					} else if (x == 8) {
						cell.setCellValue(targetDate);
					} else if (x == 9) {
						cell.setCellValue(picName);
					} else if (x == 10) {
						cell.setCellValue(followUpStatus);
					} else if (x == 11) {
						cell.setCellValue(followupBy);
					} else if (x == 12) {
						cell.setCellValue(confirmationDate);
					} else if (x == 13) {
						cell.setCellValue(followupDate);
					} else if (x == 14) {
						cell.setCellValue(information);
					} else if (x == 15) {
						cell.setCellValue(complianceDate);
					} else if (x == 16) {
						cell.setCellValue(compilanceStatus);
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
			searchCriteria.add(new DefaultSearchObject(TmpFineConstants.SEARCH_PENGIRIM, searchPengirim));
		}

		if (searchNoSurat != null && !searchNoSurat.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TmpFineConstants.SEARCH_NO_SURAT, searchNoSurat));
		}

		if (searchTanggalTerimaSuratFrom != null) {
			searchCriteria.add(new DefaultSearchObject(TmpFineConstants.SEARCH_TANGGAL_TERIMA_SURAT_FROM,
					searchTanggalTerimaSuratFrom != null ? sdf.format(searchTanggalTerimaSuratFrom) : ""));
		}

		if (searchTanggalTerimaSuratTo != null) {
			searchCriteria.add(new DefaultSearchObject(TmpFineConstants.SEARCH_TANGGAL_TERIMA_SURAT_TO, 
					searchTanggalTerimaSuratTo != null ? sdf.format(searchTanggalTerimaSuratTo) : ""));
		}
		
		if (searchTanggalSuratFrom != null) {
			searchCriteria.add(new DefaultSearchObject(TmpFineConstants.SEARCH_TANGGAL_SURAT_FROM,
					searchTanggalSuratFrom != null ? sdf.format(searchTanggalSuratFrom) : "" ));
		}

		if (searchTanggalSuratTo != null) {
			searchCriteria.add(new DefaultSearchObject(TmpFineConstants.SEARCH_TANGGAL_SURAT_TO, 
					searchTanggalSuratTo != null ? sdf.format(searchTanggalSuratTo) : ""));
		}
		
		if (searchPerihal != null && !searchPerihal.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TmpFineConstants.SEARCH_PERIHAL, searchPerihal));
		}

		if (searchTargetDateFrom != null) {
			searchCriteria.add(new DefaultSearchObject(TmpFineConstants.SEARCH_TARGET_DATE_FROM, 
					searchTargetDateFrom != null ? sdf.format(searchTargetDateFrom) : ""));
		}

		if (searchTargetDateTo != null) {
			searchCriteria.add(new DefaultSearchObject(TmpFineConstants.SEARCH_TARGET_DATE_TO,
					searchTargetDateTo != null ? sdf.format(searchTargetDateTo) : ""));
		}

		if (searchStatus != null && !searchStatus.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TmpFineConstants.SEARCH_STATUS, searchStatus));
		}
		
		User userLogin = userService.getUserByNik(facesUtil.retrieveUserLogin());
		if (userLogin != null) {
			searchCriteria.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_USER_LOGIN, userLogin.getUserId()));
		}

		tableFine.setSearchCriteria(searchCriteria);
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
		search(actionEvent);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
//		RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}
	
	public void delete(Long deleteId) {
		try {
			TmpFine entity = tmpFineService.findById(deleteId);
			if(validateDel(entity)) {
				entity.setEnabledFlag(Constants.CONSTANT_NO);
				entity.setLastUpdateBy(facesUtil.retrieveUserLogin());
				entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
				
				tmpFineService.update(entity);
				facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
			}
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}

	private boolean validateDel(TmpFine fine) {
		Boolean valid = true;
		User userLogin = userService.getUserByNik(facesUtil.retrieveUserLogin());
		
		if(!fine.getCreatedBy().equals(userLogin.getNik()) ) {
			if(fine.getTmpFinePicCompliances() != null && fine.getTmpFinePicCompliances().size() > 0) {
				valid = false;
				for(TmpFinePicCompliance picc: fine.getTmpFinePicCompliances()) {
					if(picc.getFinePicComplianceId().equals(userLogin.getUserId())) {
						valid = true;
						break;
					}
				}
				
			}
		}
		
		if(!valid) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_WARN, null, "Anda tidak diperbolehkan hapus data ini", "");
		}
		
		return valid;
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

	public DBLazyDataModel<TmpFineSearchVo> getTableFine() {
		return tableFine;
	}

	public void setTableFine(DBLazyDataModel<TmpFineSearchVo> tableFine) {
		this.tableFine = tableFine;
	}

	public TmpFineService getTmpFineService() {
		return tmpFineService;
	}

	public void setTmpFineService(TmpFineService tmpFineService) {
		this.tmpFineService = tmpFineService;
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

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
}
