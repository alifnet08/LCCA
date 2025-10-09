package com.wo.module.litigation.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.primefaces.PrimeFaces;

import com.wo.module.litigation.constant.LitigationConstants;
import com.wo.module.litigation.model.Litigation;
import com.wo.module.litigation.model.LitigationNew;
import com.wo.module.litigation.service.LitigationNewService;
import com.wo.module.litigation.service.LitigationService;
import com.wo.module.logActivity.model.LogActivity;
import com.wo.module.logActivity.service.LogActivityService;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;

import com.wo.module.common.utility.CallApiManager;
import com.wo.module.common.vo.SendEmailVo;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;

import com.wo.module.common.util.EntityUtil;

import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.trcLock.model.TrcLock;
import com.wo.module.trcLock.service.TrcLockService;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class LitigationBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(LitigationBean.class);

	private TrcLock trcLock;

	
	private String searchCaseType;
	private String searchCaseTypeDtlPerdata;
	private String searchCaseTypeDtlPidana;
	private String searchLitigationNo;
	private String searchDivNameOrBranchOffice;
	private String searchSegment;
	private String searchDebtor;
	private String searchCaseNumber;
	private String searchReportNumber;


	private String caseType;
	private String debitur;
	private String litigationNo;
	private String searchProgress;
	private String searchPutusan;
	private String searchUpayaHukum;

	private String noteLock;
	private Boolean isLock;
	private Boolean isUnlock;
	private Boolean isActionUserLock;
	private Boolean isShowPanelLock;

	private int paging;

	private LitigationService litigationService;
	private LitigationNewService litigationNewService;
	private TrcLockService trcLockService;

	private EmailTemplateService emailTemplateService;
	
	private List<Litigation> litigationList;

	private DBLazyDataModel<LitigationNew> tableModel;
	
	private List<SelectItem> caseTypeList;
	private List<SelectItem> caseTypeDtlList;

	private List<SelectItem> categoryList;

	private List<SelectItem> statusList;

	private List<SelectItem> putusanList;
	private List<SelectItem> upayaHukumList;


	private Long userIdLogin;

	public FacesUtil facesUtil;

	// add
	private LogActivity logActivity;

	private LogActivityService logActivityService;

	private UserService userService;

	private String navigateEdit = LitigationConstants.NAVIGATE_EDIT;

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@SuppressWarnings("rawtypes")
	@PostConstruct
	public void init() {
		super.init();
		initList();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<LitigationNew>(litigationNewService, paging);
		List<SearchObject> searchCriteria = new ArrayList<>();
		User userLogin = facesUtil.getUserLogin();
		this.userIdLogin = userLogin.getUserId();
		if (userLogin != null) {
			searchCriteria.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_USER_LOGIN, userLogin.getUserId()));
		}
		
		tableModel.setSearchCriteria(searchCriteria);

		isActionUserLock = true;
		isShowPanelLock = false;
		isLock = true;
		isUnlock = false;

		trcLock = trcLockService.findLastUserLockMenu("LITIGASI");

		if (trcLock != null && trcLock.getLockId() != null) {
			if (trcLock.getUserNik().equals(facesUtil.retrieveUserLogin())) {
				isActionUserLock = false;
				isShowPanelLock = true;
			}
			noteLock = facesUtil.retrieveMessage("formLitigationLockBy", trcLock.getUserName());
			isLock = false;
			isUnlock = true;
		} else {
			isActionUserLock = false;
			isShowPanelLock = true;
		}
	}

	public void initList() {
		try {

//			caseTypeList = new ArrayList<SelectItem>();
			
//			List<ParameterDetail> getJenisPerkaraList = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_CASE_TYPE);

			categoryList = new ArrayList<SelectItem>();
			List<ParameterDetail> listCategory = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_FAQ_CATEGORY);

			for (ParameterDetail vo : listCategory) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				categoryList.add(si);
			}

			statusList = new ArrayList<SelectItem>();
			List<ParameterDetail> listStatus = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_FAQ_STATUS);

			for (ParameterDetail vo : listStatus) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				statusList.add(si);
			}

			putusanList = new ArrayList<SelectItem>();

			List<ParameterDetail> getPutusanList = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_DECISION);
			for (ParameterDetail pd : getPutusanList) {
				SelectItem si = new SelectItem();
				si.setLabel(pd.getName());
				si.setValue(pd.getParameterDtlCode());

				putusanList.add(si);
			}

			upayaHukumList = new ArrayList<SelectItem>();
			List<ParameterDetail> getUpayaHukumList = new ArrayList<ParameterDetail>();
			ParameterDetail getUpayaHukum = parameterDetailService
					.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_LITIGATION_DECISION_BANDING);
			getUpayaHukumList.add(getUpayaHukum);
			getUpayaHukum = parameterDetailService
					.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_LITIGATION_LEGAL_EFFORT_INKRACHT);
			getUpayaHukumList.add(getUpayaHukum);
			getUpayaHukum = parameterDetailService
					.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_LITIGATION_EFFORT_KASASI);
			getUpayaHukumList.add(getUpayaHukum);

			for (ParameterDetail pd : getUpayaHukumList) {
				SelectItem si = new SelectItem();
				si.setLabel(pd.getName());
				si.setValue(pd.getParameterDtlCode());

				upayaHukumList.add(si);
			}

			caseTypeList = new ArrayList<SelectItem>();

			List<ParameterDetail> getJenisPerkaraList = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_CASE_TYPE);

			for (ParameterDetail pd : getJenisPerkaraList) {
				SelectItem si = new SelectItem();
				si.setLabel(pd.getName());
				si.setValue(pd.getParameterDtlCode());

				
				caseTypeList.add(si);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		PrimeFaces.current().executeScript("reInitSelect2();");

	}
	
	private String replaceDelete(String kalimat,String users,String jenisPerkara,String noPerkara) {
		if (StringUtils.isNotBlank(users)) {
			kalimat = kalimat.replace("{user_name}", users);
		}
		if (StringUtils.isNotBlank(jenisPerkara)) {
			kalimat = kalimat.replace("{jenis_perkara}", jenisPerkara);
		}
		if (StringUtils.isNotBlank(noPerkara)) {
			kalimat = kalimat.replace("{no_perkara}", noPerkara);
		}
		return kalimat;
	}

	private String replaceExport(String kalimat, String users) {
		if (StringUtils.isNotBlank(users)) {
			kalimat = kalimat.replace("{user_name}", users);
		}
		return kalimat;
	}

	public void log() throws Exception {
		ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode("ACTIVITY_TYPE_LITIGASI");
		ParameterDetail activityDate = parameterDetailService
				.getParameterDetailByParamDtlCode("LOG_ACT_LITIGASI_EXPORT");
		LogActivity la = new LogActivity();
		la.setUser(getUserLogin());
		la.setActivityType(pd.getParameterDtlCode());
		la.setActivityDate(new Timestamp(new Date().getTime()));
		String str = activityDate.getNameIn();
		String hasil = replaceExport(str, facesUtil.getUserLogin().getName());
		la.setActivityNote(hasil);
		if (StringUtils.isBlank(la.getCreatedBy())) {
			EntityUtil.setCreationInfo(la, facesUtil.retrieveUserLogin());
		} else {
			EntityUtil.setUpdateInfo(la, facesUtil.retrieveUserLogin());
		}
		logActivityService.save(la);

	}

	//UNUSED
	public void postProcessXLS(Object document) {
		try {
			HSSFWorkbook wb = (HSSFWorkbook) document;
			HSSFSheet sheet = wb.getSheetAt(0);

			List<Litigation> listDataXls = litigationService.searchData(

//					Arrays.asList(
//							new DefaultSearchObject(CommonConstants.SEARCH_BY_USER_LOGIN, userIdLogin)
//							),

					Arrays.asList(new DefaultSearchObject(LitigationConstants.SEARCH_BY_JENIS_PERKARA, caseType),
							new DefaultSearchObject(LitigationConstants.SEARCH_BY_NO_PERKARA, litigationNo),
							new DefaultSearchObject(LitigationConstants.SEARCH_BY_PROGRESS, searchProgress),
							new DefaultSearchObject(LitigationConstants.SEARCH_BY_PUTUSAN, searchPutusan),
							new DefaultSearchObject(LitigationConstants.SEARCH_BY_UPAYA_HUKUM, searchUpayaHukum),
							new DefaultSearchObject(CommonConstants.SEARCH_BY_USER_LOGIN, userIdLogin)),

					0, Integer.MAX_VALUE, null, null);
			// create Header
			HSSFRow header = sheet.createRow(0);
			for (int i = 0; i < 8; i++) {
				HSSFCell cell = header.createCell((short) i);
				if (i == 0) {
					cell.setCellValue(facesUtil.retrieveMessage("formLitigationCaseType"));
				} else if (i == 1) {
					cell.setCellValue(facesUtil.retrieveMessage("formLitigationDebitur"));
				} else if (i == 2) {
					cell.setCellValue(facesUtil.retrieveMessage("formLitigationBranchOffice"));
				} else if (i == 3) {
					cell.setCellValue(facesUtil.retrieveMessage("formLitigationNo"));
				} else if (i == 4) {
					cell.setCellValue(facesUtil.retrieveMessage("formLitigationLawsuit"));
				} else if (i == 5) {
					cell.setCellValue(facesUtil.retrieveMessage("formLitigationPlace"));
				} else if (i == 6) {
					cell.setCellValue(facesUtil.retrieveMessage("formLitigationMaterial"));
				} else if (i == 7) {
					cell.setCellValue(facesUtil.retrieveMessage("formLitigationImmaterial"));
				}

			}

			// kosongin data
			int rowNum = 1;
			for (int i = 0; i < 8; i++) {
				HSSFRow row = sheet.createRow(rowNum);
				for (int x = 0; x < 8; x++) {
					HSSFCell cell = row.createCell((short) x);
					cell.setCellValue("");
				}
				rowNum++;
			}
			// kosongin data

			// create Data
			rowNum = 1;
			for (int i = 0; i < listDataXls.size(); i++) {
				Litigation er = (Litigation) listDataXls.get(i);
				HSSFRow row = sheet.createRow(rowNum);
				for (int x = 0; x < 10; x++) {
					HSSFCell cell = row.createCell((short) x);
					if (x == 0) {
						cell.setCellValue(er.getJenisPerkara() != null ? er.getJenisPerkara() : "");
					} else if (x == 1) {
						cell.setCellValue(er.getDebitur() != null ? er.getDebitur() : "");
					} else if (x == 2) {
						cell.setCellValue(er.getKantorCabang() != null ? er.getKantorCabang() : "");
					} else if (x == 3) {
						cell.setCellValue(er.getNoPerkara() != null ? er.getNoPerkara() : "");
					} else if (x == 4) {
						cell.setCellValue(er.getJenisGugatan() != null ? er.getJenisGugatan() : "");
					} else if (x == 5) {
						cell.setCellValue(er.getDaerahPerkara() != null ? er.getDaerahPerkara() : "");
					} else if (x == 6) {
						cell.setCellValue(er.getMaterial() != null ? er.getMaterial() : 0);
					} else if (x == 7) {
						cell.setCellValue(er.getImmaterial() != null ? er.getImmaterial() : 0);
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
			log();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (searchCaseType != null && !searchCaseType.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(LitigationConstants.SEARCH_BY_JENIS_PERKARA, searchCaseType));
		}
		if(searchDivNameOrBranchOffice != null && !searchDivNameOrBranchOffice.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(LitigationConstants.SEARCH_BY_DIV_NAME_OR_BRANCH_OFFICE, searchDivNameOrBranchOffice));
		}
		if(searchSegment != null && !searchSegment.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(LitigationConstants.SEARCH_BY_SEGMENT, searchSegment));
		}
		if(searchDebtor != null && !searchDebtor.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(LitigationConstants.SEARCH_BY_DEBTOR, searchDebtor));
		}
		if(searchCaseNumber != null && !searchCaseNumber.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(LitigationConstants.SEARCH_BY_CASE_NUMBER, searchCaseNumber));
		}
		if(searchReportNumber != null && !searchReportNumber.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(LitigationConstants.SEARCH_BY_REPORT_NUMBER, searchReportNumber));
		}
		if (userIdLogin != null) {
			searchCriteria.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_USER_LOGIN, userIdLogin));
		}

		tableModel.setSearchCriteria(searchCriteria);

	}

	public void reset(ActionEvent actionEvent) {
		searchCaseType = "";
		searchCaseTypeDtlPerdata = "";
		searchCaseTypeDtlPidana = "";
		searchLitigationNo = "";
		searchDivNameOrBranchOffice = "";
		searchSegment = "";
		searchDebtor = "";
		searchCaseNumber = "";
		searchReportNumber = "";
		
		caseType = "";
		debitur = null;
		litigationNo = null;
		searchProgress = "";
		searchPutusan = null;
		searchUpayaHukum = null;

		search(actionEvent);

		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	private Boolean isValidate() {
		Boolean flag = true;
		trcLock = trcLockService.findLastUserLockMenu("LITIGASI");

		if (trcLock != null && trcLock.getLockId() == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formLitigationMustClickLock"));
			flag = false;
		} else {
			if (trcLock != null && trcLock.getLockId() != null) {
				if (!trcLock.getUserNik().equals(facesUtil.retrieveUserLogin())) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formLitigationLockBy", trcLock.getUserName()));
					flag = false;
				}
			}
		}

		return flag;
	}

	public void delete(Long deleteId) {
		try {
			if (isValidate()) {
				LitigationNew entity = litigationNewService.findById(deleteId);
				entity.setEnabledFlag(Constants.CONSTANT_NO);
				entity.setLastUpdateBy(facesUtil.retrieveUserLogin());
				entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
				
				litigationNewService.update(entity);

				LogActivity la = new LogActivity();
				ParameterDetail activityDate = parameterDetailService
						.getParameterDetailByParamDtlCode("LOG_ACT_LITIGASI_DELETED");
				List<ParameterDetail> activityType = parameterDetailService
						.getParameterDetailByParamCode("ACTIVITY_TYPE");
				ParameterDetail paramCaseType = entity.getCaseType();
				//ParameterDetail paramCaseTypeDtl = parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getSelectCaseTypeDtl());
				for (ParameterDetail data : activityType) {
					if (data.getParameterDtlCode().equals("ACTIVITY_TYPE_LITIGASI")) {
						if(entity.getCaseType().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PAILIT_PKPU)) {
							la.setUser(getUserLogin());
							la.setActivityType(data.getParameterDtlCode());
							la.setActivityDate(new Timestamp(new Date().getTime()));
							String str = activityDate.getNameIn();
							String hasil = replaceDelete(str, facesUtil.getUserLogin().getName(),paramCaseType.getNameIn(),entity.getLitigationPailitPkpu().getCaseNumber());
							la.setActivityNote(hasil);
						}
						if(entity.getCaseType().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PERDATA) && entity.getCaseTypeDtl().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_PENGGUGAT)) {
							la.setUser(getUserLogin());
							la.setActivityType(data.getParameterDtlCode());
							la.setActivityDate(new Timestamp(new Date().getTime()));
							String str = activityDate.getNameIn();
							String hasil = replaceDelete(str, facesUtil.getUserLogin().getName(),paramCaseType.getNameIn(),entity.getLitigationPerdataPenggugat().getCaseNumber());
							la.setActivityNote(hasil);
						}
						if(entity.getCaseType().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PERDATA) && entity.getCaseTypeDtl().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_TERGUGAT)) {
							la.setUser(getUserLogin());
							la.setActivityType(data.getParameterDtlCode());
							la.setActivityDate(new Timestamp(new Date().getTime()));
							String str = activityDate.getNameIn();
							String hasil = replaceDelete(str, facesUtil.getUserLogin().getName(),paramCaseType.getNameIn(),entity.getLitigationPerdataTergugat().getCaseNumber());
							la.setActivityNote(hasil);
						}
						if(entity.getCaseType().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PIDANA) && entity.getCaseTypeDtl().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_PELAPOR)) {
							la.setUser(getUserLogin());
							la.setActivityType(data.getParameterDtlCode());
							la.setActivityDate(new Timestamp(new Date().getTime()));
							String str = activityDate.getNameIn();
							String hasil = replaceDelete(str, facesUtil.getUserLogin().getName(),paramCaseType.getNameIn(),entity.getLitigationPidanaPelapor().getCaseNumber());
							la.setActivityNote(hasil);
						}
						if(entity.getCaseType().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PIDANA) && entity.getCaseTypeDtl().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_TERLAPOR)) {
							la.setUser(getUserLogin());
							la.setActivityType(data.getParameterDtlCode());
							la.setActivityDate(new Timestamp(new Date().getTime()));
							String str = activityDate.getNameIn();
							String hasil = replaceDelete(str, facesUtil.getUserLogin().getName(),paramCaseType.getNameIn(),entity.getLitigationPidanaTerlapor().getCaseNumber());
							la.setActivityNote(hasil);
						}
					}
				}
				if (StringUtils.isBlank(la.getCreatedBy())) {
					EntityUtil.setCreationInfo(la, facesUtil.retrieveUserLogin());
				} else {
					EntityUtil.setUpdateInfo(la, facesUtil.retrieveUserLogin());
				}
				logActivityService.save(la);
				
				facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));

			}
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}

	public void lock() {
		try {
			trcLockService.save(facesUtil.getUserLogin());
			String userName = facesUtil.getUserLogin().getName();
			
			// this should be a singleton
	        ExecutorService emailExecutor = Executors.newCachedThreadPool();

	        // from you sendEmail() method
	        emailExecutor.execute(new Runnable() {
	            @Override
	            public void run() {
	                try {
	                	sendEmail(userName, true);
	                } catch (Exception e) {
	                    logger.error("send email failed", e);
	                }
	            }
	        });
	        
//			facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
			isLock = false;
			isUnlock = true;
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}

		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void unlock() {
		try {
			trcLockService.update(trcLockService.findById(trcLock.getLockId()));
			String userName = facesUtil.getUserLogin().getName();
			
			// this should be a singleton
	        ExecutorService emailExecutor = Executors.newCachedThreadPool();

	        // from you sendEmail() method
	        emailExecutor.execute(new Runnable() {
	            @Override
	            public void run() {
	                try {
	                	sendEmail(userName, false);
	                } catch (Exception e) {
	                    logger.error("send email failed", e);
	                }
	            }
	        });
			
//			facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
			isLock = true;
			isUnlock = false;
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}

		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	@SuppressWarnings("unused")
	public void sendEmail(String userNameLock, boolean isLock) {
		try {
			SendEmailVo sendEmail = new SendEmailVo();
			SimpleDateFormat sdfDate = new SimpleDateFormat("dd MMMM yyyy");
			SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
			Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
			String currentDateStr = sdfDate.format(currentDateTime);
			String currentTimeStr = sdfTime.format(currentDateTime);
			
			
			if(isLock) {
				EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode(LitigationConstants.EMAIL_LITIGATION_LOCK_CODE);
				
				String emailSubject = emailTemplate.getEmailSubject();
				String emailContent = emailTemplate.getEmailContent();
				
				//TODO set new email template
				emailContent = emailContent.replaceAll("nama_pengunci", userNameLock);
				emailContent = emailContent.replaceAll("tanggal_sekarang", currentDateStr);
				emailContent = emailContent.replaceAll("jam_sekarang", currentTimeStr);
				
				ParameterDetail prmDtlEmailTo = parameterDetailService.getParameterDetailByParamDtlCode("EMAIL_TEAM_LITIGASI");
				
				sendEmail.setEmailTo(prmDtlEmailTo.getNameIn());
				sendEmail.setEmailCc("");
				sendEmail.setSubject(emailSubject);
				sendEmail.setContent(emailContent);
				
				ExecutorService emailExecutor = Executors.newCachedThreadPool();
				
				if (StringUtils.isNotBlank(sendEmail.getEmailTo())) {
					CallApiManager.sendEmailAPI(sendEmail.getEmailTo(),sendEmail.getEmailCc(), sendEmail.getSubject(), sendEmail.getContent(), 
							LitigationConstants.EMAIL_LITIGATION_LOCK_CODE, "true", parameterDetailService);
				}
			} else {
				EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode(LitigationConstants.EMAIL_LITIGATION_UNLOCK_CODE);
				
				String emailSubject = emailTemplate.getEmailSubject();
				String emailContent = emailTemplate.getEmailContent();
				
				//TODO set new email template
				emailContent = emailContent.replaceAll("nama_pengunci", userNameLock);
				emailContent = emailContent.replaceAll("tanggal_sekarang", currentDateStr);
				emailContent = emailContent.replaceAll("jam_sekarang", currentTimeStr);
				
				ParameterDetail prmDtlEmailTo = parameterDetailService.getParameterDetailByParamDtlCode("EMAIL_TEAM_LITIGASI");
				
				sendEmail.setEmailTo(prmDtlEmailTo.getNameIn());
				sendEmail.setEmailCc("");
				sendEmail.setSubject(emailSubject);
				sendEmail.setContent(emailContent);
				
				ExecutorService emailExecutor = Executors.newCachedThreadPool();
				
				if (StringUtils.isNotBlank(sendEmail.getEmailTo())) {
					CallApiManager.sendEmailAPI(sendEmail.getEmailTo(),sendEmail.getEmailCc(), sendEmail.getSubject(), sendEmail.getContent(), 
							LitigationConstants.EMAIL_LITIGATION_UNLOCK_CODE, "true", parameterDetailService);
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomAPIException(e.getMessage()); 
		}
		
	}

	
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public Boolean getIsLogin() {
		if (facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE) != null
				|| facesUtil.getSessionAttribute(Constants.SESSION_KANDIDAT) != null)
			return true;
		else
			return false;
	}

	public LitigationService getLitigationService() {
		return litigationService;
	}

	public void setLitigationService(LitigationService litigationService) {
		this.litigationService = litigationService;
	}

	public LitigationNewService getLitigationNewService() {
		return litigationNewService;
	}

	public void setLitigationNewService(LitigationNewService litigationNewService) {
		this.litigationNewService = litigationNewService;
	}

	public DBLazyDataModel<LitigationNew> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<LitigationNew> tableModel) {
		this.tableModel = tableModel;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}

	public String getNavigateEdit() {
		if (isValidate()) {
			return navigateEdit;
		}
		return "";
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	public List<Litigation> getLitigationList() {
		return litigationList;
	}

	public void setLitigationList(List<Litigation> litigationList) {
		this.litigationList = litigationList;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		LitigationBean.logger = logger;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<SelectItem> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}

	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public String getDebitur() {
		return debitur;
	}

	public void setDebitur(String debitur) {
		this.debitur = debitur;
	}

	public String getLitigationNo() {
		return litigationNo;
	}

	public void setLitigationNo(String litigationNo) {
		this.litigationNo = litigationNo;
	}

	public String getSearchProgress() {
		return searchProgress;
	}

	public void setSearchProgress(String searchProgress) {
		this.searchProgress = searchProgress;
	}

	public String getSearchPutusan() {
		return searchPutusan;
	}

	public void setSearchPutusan(String searchPutusan) {
		this.searchPutusan = searchPutusan;
	}

	public String getSearchUpayaHukum() {
		return searchUpayaHukum;
	}

	public void setSearchUpayaHukum(String searchUpayaHukum) {
		this.searchUpayaHukum = searchUpayaHukum;
	}

	public List<SelectItem> getPutusanList() {
		return putusanList;
	}

	public void setPutusanList(List<SelectItem> putusanList) {
		this.putusanList = putusanList;
	}

	public List<SelectItem> getUpayaHukumList() {
		return upayaHukumList;
	}

	public void setUpayaHukumList(List<SelectItem> upayaHukumList) {
		this.upayaHukumList = upayaHukumList;
	}

	public List<SelectItem> getCaseTypeList() {
		return caseTypeList;
	}

	public void setCaseTypeList(List<SelectItem> caseTypeList) {
		this.caseTypeList = caseTypeList;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Long getUserIdLogin() {
		return userIdLogin;
	}

	public void setUserIdLogin(Long userIdLogin) {
		this.userIdLogin = userIdLogin;
	}

	public Boolean getIsLock() {
		return isLock;
	}

	public void setIsLock(Boolean isLock) {
		this.isLock = isLock;
	}

	public Boolean getIsUnlock() {
		return isUnlock;
	}

	public void setIsUnlock(Boolean isUnlock) {
		this.isUnlock = isUnlock;
	}

	public TrcLockService getTrcLockService() {
		return trcLockService;
	}

	public void setTrcLockService(TrcLockService trcLockService) {
		this.trcLockService = trcLockService;
	}

	public TrcLock getTrcLock() {
		return trcLock;
	}

	public void setTrcLock(TrcLock trcLock) {
		this.trcLock = trcLock;
	}

	public Boolean getIsActionUserLock() {
		return isActionUserLock;
	}

	public void setIsActionUserLock(Boolean isActionUserLock) {
		this.isActionUserLock = isActionUserLock;
	}

	public Boolean getIsShowPanelLock() {
		return isShowPanelLock;
	}

	public void setIsShowPanelLock(Boolean isShowPanelLock) {
		this.isShowPanelLock = isShowPanelLock;
	}

	public String getNoteLock() {
		return noteLock;
	}

	public void setNoteLock(String noteLock) {
		this.noteLock = noteLock;
	}

	public String getSearchCaseType() {
		return searchCaseType;
	}

	public void setSearchCaseType(String searchCaseType) {
		this.searchCaseType = searchCaseType;
	}

	public String getSearchCaseTypeDtlPerdata() {
		return searchCaseTypeDtlPerdata;
	}

	public void setSearchCaseTypeDtlPerdata(String searchCaseTypeDtlPerdata) {
		this.searchCaseTypeDtlPerdata = searchCaseTypeDtlPerdata;
	}

	public String getSearchCaseTypeDtlPidana() {
		return searchCaseTypeDtlPidana;
	}

	public void setSearchCaseTypeDtlPidana(String searchCaseTypeDtlPidana) {
		this.searchCaseTypeDtlPidana = searchCaseTypeDtlPidana;
	}

	public String getSearchLitigationNo() {
		return searchLitigationNo;
	}

	public void setSearchLitigationNo(String searchLitigationNo) {
		this.searchLitigationNo = searchLitigationNo;
	}

	public String getSearchDivNameOrBranchOffice() {
		return searchDivNameOrBranchOffice;
	}

	public void setSearchDivNameOrBranchOffice(String searchDivNameOrBranchOffice) {
		this.searchDivNameOrBranchOffice = searchDivNameOrBranchOffice;
	}

	public String getSearchSegment() {
		return searchSegment;
	}

	public void setSearchSegment(String searchSegment) {
		this.searchSegment = searchSegment;
	}

	public List<SelectItem> getCaseTypeDtlList() {
		return caseTypeDtlList;
	}

	public void setCaseTypeDtlList(List<SelectItem> caseTypeDtlList) {
		this.caseTypeDtlList = caseTypeDtlList;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public LogActivity getLogActivity() {
		return logActivity;
	}

	public void setLogActivity(LogActivity logActivity) {
		this.logActivity = logActivity;
	}

	public LogActivityService getLogActivityService() {
		return logActivityService;
	}

	public void setLogActivityService(LogActivityService logActivityService) {
		this.logActivityService = logActivityService;
	}

	public String getSearchDebtor() {
		return searchDebtor;
	}

	public void setSearchDebtor(String searchDebtor) {
		this.searchDebtor = searchDebtor;
	}

	public String getSearchCaseNumber() {
		return searchCaseNumber;
	}

	public void setSearchCaseNumber(String searchCaseNumber) {
		this.searchCaseNumber = searchCaseNumber;
	}

	public String getSearchReportNumber() {
		return searchReportNumber;
	}

	public void setSearchReportNumber(String searchReportNumber) {
		this.searchReportNumber = searchReportNumber;
	}
	
	
}